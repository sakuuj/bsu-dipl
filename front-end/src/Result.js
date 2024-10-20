import React from "react";

export default function Result({ computedResult }) {
    return (
        <div className=" basis-20 grow base flex  flex-col justify-stretch">
            <p className="mt-2 mb-1 text-2xl text-center">
                Результат
            </p>

            <p className=" basis-40 grow text-center flex flex-row justify-center items-stretch">
                <textarea
                    className="p-3 relative border-solid border border-_dark-blue w-4/5 resize-none"
                    id="result"
                    name="result"
                    value={computedResult}
                    readOnly />
            </p>
        </div>
    );
}