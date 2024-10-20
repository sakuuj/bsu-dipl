import React from "react";
import { useState } from "react";

import Grammar from "./Grammar";
import ActionButtons from "./ActionButtons";
import Result from "./Result";

export default function Main({role}) {

    let [grammar, setGrammar] = useState({
        terminals: '',
        nonTerminals: '',
        definingEquations: ''
    });

    console.log("main " + role)
    let [result, setResult] = useState('');

    return (
        <main className="flex flex-col md:flex-row justify-between items-stretch h-4/5">

            <Grammar
                grammar={grammar}
                onGrammarChanged={setGrammar}
            />

            <ActionButtons
                grammar={grammar}
                onGrammarChoosed={setGrammar}

                onResultAcquired={setResult}
                role= {role}
            />

            <Result
                computedResult={result} />
        </main>
    );
}