import React from "react";

export default function Grammar({
    grammar,
    onGrammarChanged
}) {

    return (
        <div className="basis-20 m-2 flex grow flex-col justify-between items-stretch">
            <p className="mt-2 mb-1 text-2xl  text-center">Грамматика</p>
            <p className="basis-10 text-center mt-3">
                <label htmlFor="non-terminals" className="text-xl">Нетерминалы (первый в списке &ndash; стартовый) :</label>
            </p>
            <p className="basis-20 grow text-center flex justify-center items-stretch">
                <textarea
                    className=" border-solid border border-_dark-blue w-4/5 resize-none"
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

            <p className="basis-10 text-center mt-3">
                <label htmlFor="terminals" className="text-xl">Терминалы:</label>
            </p>
            <p className="basis-20 grow text-center flex justify-center items-stretch">
                <textarea
                    className="border-solid border border-_dark-blue  w-4/5 resize-none "
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

            <p className="basis-10 text-center mt-3">
                <label htmlFor="defining-equations" className="text-xl">Определяющие уравнения:</label>
            </p>
            <p className="basis-40 grow text-center flex flex-row justify-center items-stretch">
                <textarea
                    className="border-solid border border-_dark-blue w-4/5 resize-none"
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
        </div>
    );
}