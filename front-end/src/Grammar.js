import React from "react";

export default function Grammar({
    grammar,
    onGrammarChanged,
    inputForAnalysis,
    onInputForAnalysisChanged,
}) {

    return (
        <div className="basis-20 mt-1 flex grow flex-col justify-between items-stretch">
            <p className="mt-2 mb-1 text-xl  text-center">Грамматика</p>
            <p className="basis-5 text-center mt-1">
                <label htmlFor="non-terminals" className="text-lg">Нетерминалы (первый - аксиома) :</label>
            </p>
            <p className="basis-10 grow text-center flex justify-center items-stretch">
                <textarea
                    className=" border-solid border px-2 border-_dark-blue w-4/5 resize-none"
                    id="non-terminals"
                    name="non-terminals"
                    value={
                        grammar.nonTerminals
                    }
                    onChange={(e) => {
                        onGrammarChanged({
                            ...grammar,
                            nonTerminals: e.target.value
                        });
                    }}
                />
            </p>

            <p className="basis-5 text-center mt-1">
                <label htmlFor="terminals" className="text-lg">Терминалы:</label>
            </p>
            <p className="basis-10 grow text-center flex justify-center items-stretch">
                <textarea
                    className="border-solid border px-2 border-_dark-blue  w-4/5 resize-none "
                    id="terminals"
                    name="terminals"
                    value={grammar.terminals}
                    onChange={(e) => {
                        onGrammarChanged({
                            ...grammar,
                            terminals: e.target.value
                        });
                    }}
                >
                </textarea>
            </p>

            <p className="basis-5 text-center mt-1">
                <label htmlFor="defining-equations" className="text-lg">Определяющие уравнения:</label>
            </p>
            <p className="basis-40 grow text-center flex flex-row justify-center items-stretch">
                <textarea
                    className="border-solid border px-2 border-_dark-blue w-4/5 resize-none"
                    id="defining-equations"
                    name="defining-equations"
                    value={
                        grammar.definingEquations
                    }
                    onChange={(e) => {
                        onGrammarChanged({
                            ...grammar,
                            definingEquations: e.target.value
                        });
                    }}
                >
                </textarea>
            </p>

            <p className="basis-5 text-center mt-2">
                <p className="text-xl  text-center">Ввод для анализа:</p>
            </p>
            <p className="basis-15 grow text-center flex flex-row justify-center items-stretch">
                <textarea
                    className="border-solid border px-2 border-_dark-blue w-4/5 resize-none"
                    id="defining-equations"
                    name="defining-equations"
                    value={inputForAnalysis}
                    onChange={(e) => {onInputForAnalysisChanged(e.target.value);}}
                >
                </textarea>
            </p>
        </div>
    );
}