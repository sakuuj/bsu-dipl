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
    inputForAnalysis,
    role,
    onResultAcquired
}) {

    let [isPopupActivated, activatePopup] = useState(false);

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
            >FIRST</button>
            <p></p>
            <button
                className="bg-_smoke p-2 w-4/5 hover:text-_grayer-white hover:bg-_dark-blue"
                type="button"
                onClick={() => findFollowAndPublishResult(onResultAcquired, grammar)}
            >FOLLOW</button>
            <p></p>
            <button
                className="bg-_smoke p-2 w-4/5 hover:text-_grayer-white hover:bg-_dark-blue"
                type="button"
                onClick={() => parseInput(onResultAcquired, inputForAnalysis, grammar)}
            >Анализировать ввод</button>
            {
                role != "ADMIN" ? null :
                    (<>
                        <p></p>
                        <button
                            className="bg-_smoke p-2 w-4/5 hover:text-_grayer-white hover:bg-_dark-blue"
                            type="button"
                            id="findFirst_2"
                            onClick={() => insertGrammarAndPublishResult(onResultAcquired, grammar, inputForAnalysis)}
                        >Добавить грамматику</button>
                    </>)
            }
        </div>
    );
}

function parseInput(onResultAcquired, inputForAnalysis, grammar) {
    let xhr = new XMLHttpRequest();

    xhr.open('POST', `${PROCESSOR_HOST}/processor/parse`);
    xhr.responseType = 'json';
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onload = function () {
        if (xhr.status !== 200) {
            const errorResponse = xhr.response;
            processErrorResponse(errorResponse, onResultAcquired);
            return;
        }
        const response = xhr.response;
        const cfgResponse = response.grammarResponse;
        const parsingMetadata = response.parsingMetadata;

        console.log(response);
        let ntStr = nonTerminalsToString(cfgResponse.nonTerminals, cfgResponse.startSymbol);
        let tStr = terminalsToString(cfgResponse.terminals);

        let parsingResult = parsingMetadata.parsingStatus === "SUCCESS"
            ? `ввод "${inputForAnalysis}" может быть получен из грамматики`
            : `ввод "${inputForAnalysis}" не может быть получен из грамматики`;

        let parsingSteps = parsingMetadata.parsingSteps
            .map((step, i) => `\nШаг ${i}.` + "\n\tДействие: " + "\n\t\t" + (step.actionDescription || "_") + "\n\tСодержимое стека: " + "\n\t\t" + step.stackContent + "\n\tОставшийся текст: " + "\n\t\t" + step.unmatchedInput)
            .reduce((accumulator, currentValue) => accumulator + currentValue);

        let rez = "Результат анализа: \t" + parsingResult
            + ".\n\nПоследовательность вывода: " + parsingSteps;

        onResultAcquired(rez);
    };
    xhr.onerror = function () {
        onResultAcquired("ОШИБКА");
        return;
    }

    let grammarParsed = parseGrammar(grammar);
    let request = {
        text: inputForAnalysis,
        grammarRequest: grammarParsed
    }

    console.log(JSON.stringify(request));
    xhr.send(JSON.stringify(request));
}

function processErrorResponse(errorResponse, onResultAcquired) {
    if (errorResponse !== null) {
        onResultAcquired("ОШИБКА:\n\t"
            + errorResponse.message
            + (errorResponse.errors !== undefined ? "\n\t" + errorResponse.errors : "")
        );
    } else {
        onResultAcquired("ОШИБКА");
    }
}

function findFirst1AndPublishResult(onResultAcquired, grammarState) {
    let xhr = new XMLHttpRequest();

    xhr.open('POST', `${PROCESSOR_HOST}/processor/first1`);
    xhr.responseType = 'json';
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onload = function () {
        if (xhr.status !== 200) {
            const errorResponse = xhr.response;
            processErrorResponse(errorResponse, onResultAcquired);
            return;
        }
        const response = xhr.response;
        const cfgResponse = response.grammarResponse;
        const first1Map = response.first1Map;

        console.log(response);
        let ntStr = nonTerminalsToString(cfgResponse.nonTerminals, cfgResponse.startSymbol);
        let tStr = terminalsToString(cfgResponse.terminals);
        let nttStr = nonTerminalsToSolution(first1Map);

        let rez = "Нетерминалы: " + ntStr + "\nТерминалы: " + tStr + "\nFIRST:" + nttStr;

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
  
            processErrorResponse(errorResponse, onResultAcquired);

            return;
        }
        const response = xhr.response;
        const cfgResponse = response.grammarResponse;

        console.log(response);
        let ntStr = nonTerminalsToString(cfgResponse.nonTerminals, cfgResponse.startSymbol);
        let tStr = terminalsToString(cfgResponse.terminals);
        let followStr = nonTerminalsToSolution(response.followMap);
        let first1Str = nonTerminalsToSolution(response.first1Map);

        let rez = "Нетерминалы: " + ntStr
            + "\nТерминалы: " + tStr
            + "\nFIRST: " + first1Str
            + "\nFOLLOW:" + followStr;

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


function insertGrammarAndPublishResult(onResultAcquired, grammarState, inputForAnalysis) {
    let xhr = new XMLHttpRequest();

    xhr.open('POST', `${EXAMPLES_HOST}/examples`);
    xhr.responseType = 'json';
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.setRequestHeader('Authorization', 'Bearer ' + localStorage.getItem(JWT_TOKEN_KEY));
    xhr.onload = function () {
        if (xhr.status !== 201) {
            const errorResponse = xhr.response;
  
            processErrorResponse(errorResponse, onResultAcquired);

            return;
        }
        const response = xhr.response;

        onResultAcquired(`Грамматика была добавлена: ID = ${inputForAnalysis}`);
    };
    xhr.onerror = function () {

        onResultAcquired("ОШИБКА");
    }

    let grammar = parseGrammar(grammarState);
    grammar.id = inputForAnalysis;
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
        .filter((_, index) => index !== startSymbolIndex)
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
        rez += "\n\t" + key + ": \n" + value
            .map(item => String(item))
            .reduce((accumulator, current) => accumulator + '\n\t\t' + current, "")
            .slice(1)
    }
    return rez;
}