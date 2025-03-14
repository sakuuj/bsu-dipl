import React from "react";
import { useState } from "react";
import Cookies from "js-cookie";

import ChooseGrammarPopup from "./ChooseGrammarPopup";

const JWT_TOKEN_KEY = "jwtToken";
const PROCESSOR_HOST = "http://localhost:80"
const EXAMPLES_HOST = "http://localhost:80"

export default function ActionButtons({
    grammar,
    onGrammarChoosed,
    role,
    onResultAcquired
}) {

    let [isPopupActivated, activatePopup] = useState(false);

    console.log(role);
    return (
        <div className=" relative z-10 basis-15 self-center flex justify-center items-center flex-col gap-2">
            {(() => {
                if (isPopupActivated) {
                    return <ChooseGrammarPopup
                        onGrammarChoosed={onGrammarChoosed}
                        activatePopup={activatePopup} />
                }
            })()}
            <button
                className="bg-_smoke p-2 w-4/5 hover:text-_grayer-white hover:bg-_dark-blue"
                type="button"
                id="chooseGrammar"
                onClick={() => activatePopup(true)}>Выбрать грамматику</button>
            <p></p>
            <button
                className="relative bg-_smoke p-2 w-4/5 hover:text-_grayer-white hover:bg-_dark-blue"
                type="button"
                onClick={() => findFirst1AndPublishResult(onResultAcquired, grammar)}
            >FIRST_1</button>
            <p></p>
            <button
                className="bg-_smoke p-2 w-4/5 hover:text-_grayer-white hover:bg-_dark-blue"
                type="button"
                onClick={() => findFollowAndPublishResult(onResultAcquired, grammar)}
            >FOLLOW</button>

            {
                role != "ADMIN" ? null :
                    (<>
                        <p></p>
                        <button
                            className="bg-_smoke p-2 w-4/5 hover:text-_grayer-white hover:bg-_dark-blue"
                            type="button"
                            id="findFirst_2"
                            onClick={() => insertGrammarAndPublishResult(onResultAcquired, grammar)}
                        >Добавить грамматику</button>
                    </>)
            }
        </div>
    );
}

function findFirst1AndPublishResult(onResultAcquired, grammarState) {
    let xhr = new XMLHttpRequest();

    xhr.open('POST', `${PROCESSOR_HOST}/processor/first1`);
    xhr.responseType = 'json';
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onload = function () {
        if (xhr.status !== 200) {
            const errorResponse = xhr.response;
            if (errorResponse !== null) {
                onResultAcquired("ОШИБКА:\n" + errorResponse.message);
            } else {
                onResultAcquired("ОШИБКА");
            }
            return;
        }
        const response = xhr.response;
        const cfgResponse = response.cfgResponse;
        const first1Map = response.first1Map;

        console.log(response);
        let ntStr = nonTerminalsToString(cfgResponse.nonTerminals, cfgResponse.startSymbol);
        let tStr = terminalsToString(cfgResponse.terminals);
        let nttStr = nonTerminalsToSolution(first1Map);

        let rez = "Нетерминалы: " + ntStr + "\n Терминалы: " + tStr + "\n FIRST_1:" + nttStr;

        onResultAcquired(rez);
    };
    xhr.onerror = function () {
        onResultAcquired("ОШИБКА");
        return;
    }

    let grammar = parseGrammar(grammarState);
    console.log(JSON.stringify(grammar));
    xhr.send(JSON.stringify(grammar));
}

function findFollowAndPublishResult(onResultAcquired, grammarState) {
    let xhr = new XMLHttpRequest();

    xhr.open('POST', `${PROCESSOR_HOST}/processor/follow`);
    xhr.responseType = 'json';
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onload = function () {
        if (xhr.status !== 200) {
            const errorResponse = xhr.response;
            if (errorResponse !== null) {
                onResultAcquired("ОШИБКА:\n" + errorResponse.message);
            } else {
                onResultAcquired("ОШИБКА");
            }
            return;
        }
        const response = xhr.response;
        const cfgResponse = response.cfgResponse;

        console.log(response);
        let ntStr = nonTerminalsToString(cfgResponse.nonTerminals, cfgResponse.startSymbol);
        let tStr = terminalsToString(cfgResponse.terminals);
        let followStr = nonTerminalsToSolution(response.followMap);
        let first1Str = nonTerminalsToSolution(response.first1Map);

        let rez = "Нетерминалы: " + ntStr 
        + "\n Терминалы: " + tStr 
        +"\n FIRST_1: " + first1Str
        +"\n FOLLOW:" + followStr;

        onResultAcquired(rez);
    };
    xhr.onerror = function () {
        onResultAcquired("ОШИБКА");
        return;
    }

    let grammar = parseGrammar(grammarState);
    console.log(JSON.stringify(grammar));
    xhr.send(JSON.stringify(grammar));
}

function parseGrammar(grammarState) {

    const nonTerminals = grammarState.nonTerminals
        .split(",")
        .map((item) => item.trim());

    const startSymbol = nonTerminals.length > 0 ? nonTerminals.at(0) : '';

    const terminals = grammarState.terminals
        .split(",")
        .map((item) => item.trim());

    const definingEquations = grammarState.definingEquations
        .split("\n")
        .map((item) => item.trim());

    const parsedGrammar = {
        startSymbol: startSymbol,
        nonTerminals: nonTerminals,
        terminals: terminals,
        definingEquations: definingEquations
    }

    return parsedGrammar;
}


function insertGrammarAndPublishResult(onResultAcquired, grammarState) {
    let xhr = new XMLHttpRequest();

    xhr.open('POST', `${EXAMPLES_HOST}/examples`);
    xhr.responseType = 'json';
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.setRequestHeader('Authorization', 'Bearer ' + localStorage.getItem(JWT_TOKEN_KEY));
    xhr.onload = function () {
        if (xhr.status !== 201) {
            const errorResponse = xhr.response;
            if (errorResponse !== null) {
                onResultAcquired("ОШИБКА:\n" + errorResponse.message);
            } else {
                onResultAcquired("ОШИБКА");
            }
            return;
        }
        const response = xhr.response;

        onResultAcquired("Грамматика добавлена");
    };
    xhr.onerror = function () {

        onResultAcquired("ОШИБКА");
    }

    let grammar = parseGrammar(grammarState);
    console.log(JSON.stringify(grammar));
    xhr.send(JSON.stringify(grammar));
}


function nonTerminalsToString(nonTerminals, startSymbol) {
    const startSymbolIndex = nonTerminals.indexOf(startSymbol);

    if (nonTerminals.length === 0) {
        return "";
    }

    if (nonTerminals.length === 1) {
        return String(startSymbol);
    }

    return startSymbol + nonTerminals
        .filter((item, index) => index !== startSymbolIndex)
        .reduce((accumulator, current) => accumulator + ', ' + current, "")
}

function terminalsToString(terminals) {
    const terminalsReduced = terminals
        .map(item => String(item))
        .reduce((accumulator, current) => accumulator + ', ' + current, '');
    if (terminalsReduced.length > 1) {
        return terminalsReduced.slice(1);
    }
    return '';
}

function nonTerminalsToSolution(ntt) {
    let rez = '';
    for (const [key, value] of Object.entries(ntt)) {
        rez += "\n" + key + ": \n" + value
            .map(item => String(item))
            .reduce((accumulator, current) => accumulator + '\n\t' + current, "")
            .slice(1)
    }
    return rez;
}