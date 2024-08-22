"use strict"

function createExampleElement(example) {

    const elemToAddCfgAfter = document.getElementById("to-add-cfgs-after");

    let id = example.id;
    let startSymbol = example.content.startSymbol;
    let nonTerminals = example.content.nonTerminals;
    let terminals = example.content.terminals;
    let definingEquations = example.content.definingEquations;

    let cfgElem = document.createElement("div");
    cfgElem.setAttribute("class", "cfg");

    let pId = document.createElement("p");
    let pIdTextNode = document.createTextNode("#" + id);
    pId.appendChild(pIdTextNode);

    let terminalsTitleNode = document.createElement("p");
    let terminalsTitleTextNode = document.createTextNode("Терминалы:");
    terminalsTitleNode.appendChild(terminalsTitleTextNode);


    let terminalsContentNode = document.createElement("p");
    let terminalsContentText = "{";
    for (let i = 0; i < terminals.length; i++) {
        terminalsContentText += terminals[i];
        if (i !== terminals.length - 1) {
            terminalsContentText += ", ";
        } else {
            terminalsContentText += "}"
        }
    }
    let terminalsContentTextNode = document.createTextNode(terminalsContentText);
    terminalsContentNode.appendChild(terminalsContentTextNode);




    let nonTerminalsTitleNode = document.createElement("p");
    nonTerminalsTitleNode.setAttribute("style", "white-space: pre-wrap;")
    let nonTerminalsTitleTextNode = document.createTextNode("Нетерминалы \n (первый в списке - стартовый) :");
    nonTerminalsTitleNode.appendChild(nonTerminalsTitleTextNode);

    let nonTerminalsContentNode = document.createElement("p");
    let nonTerminalsContentText = "{" + startSymbol;
    if (nonTerminals.length > 1) {
        nonTerminalsContentText += ", ";
    }
    for (let i = 0; i < nonTerminals.length; i++) {
        if (nonTerminals[i] === startSymbol) {
            continue;
        }

        nonTerminalsContentText += nonTerminals[i];
        if (i !== nonTerminals.length - 1) {
            nonTerminalsContentText += ", ";
        } else {
            nonTerminalsContentText += "}"
        }
    }
    let nonTerminalsContentTextNode = document.createTextNode(nonTerminalsContentText);
    nonTerminalsContentNode.appendChild(nonTerminalsContentTextNode);



    let definingEquationsTitleNode = document.createElement("p");
    let definingEquationsTitleTextNode = document.createTextNode("Определяющие уравнения:");
    definingEquationsTitleNode.appendChild(definingEquationsTitleTextNode);

    let definingEquationsContentNode = document.createElement("p");
    let definingEquationsContentText = "{\n";
    for (let i = 0; i < definingEquations.length; i++) {
        definingEquationsContentText += "\t" + definingEquations[i];
        if (i !== definingEquations.length - 1) {
            definingEquationsContentText += ",\n ";
        } else {
            definingEquationsContentText += "\n}"
        }
    }
    definingEquationsContentNode.setAttribute("style", "white-space: pre-wrap; text-align: left;");
    let definingEquationsContentTextNode = document.createTextNode(definingEquationsContentText);
    definingEquationsContentNode.appendChild(definingEquationsContentTextNode);

    let chooseButtonContentNode = document.createElement("p");
    let chooseButton = document.createElement("button");
    chooseButton.setAttribute("type", "button");
    chooseButton.grammar = example.content;
    chooseButton.onclick = function () {
        extractGrammar(chooseButton.grammar);
        showCfgsCloseChooseGrammarButton.onclick.apply();
    }
    chooseButtonContentNode.appendChild(chooseButton);
    let chooseButtonTextNode = document.createTextNode("Выбрать");
    chooseButton.appendChild(chooseButtonTextNode);

    cfgElem.appendChild(pId);
    insertAfter(pId, pIdTextNode);

    insertAfter(pIdTextNode, terminalsTitleNode);
    insertAfter(terminalsTitleNode, terminalsContentNode);
    insertAfter(terminalsContentNode, nonTerminalsTitleNode);
    insertAfter(nonTerminalsTitleNode, nonTerminalsContentNode);
    insertAfter(nonTerminalsContentNode, definingEquationsTitleNode);
    insertAfter(definingEquationsTitleNode, definingEquationsContentNode);
    insertAfter(definingEquationsContentNode, chooseButtonContentNode);

    insertAfter(elemToAddCfgAfter, cfgElem);
}

function insertAfter(referenceNode, newNode) {
    referenceNode.parentNode.insertBefore(newNode, referenceNode.nextSibling);
}

function removeElemsWithClassName(className) {
    var paras = document.getElementsByClassName(className);

    while (paras[0]) {
        paras[0].parentNode.removeChild(paras[0]);
    }
}

const chooseGrammarButton = document.getElementById("chooseGrammar");
const prevPageButton = document.getElementById("prev-page-button");
const nextPageButton = document.getElementById("next-page-button");
const currPageNumberSpan = document.getElementById("cur-page-number");

const findFirst1Button = document.getElementById("findFirst_1");
findFirst1Button.onclick = function () {
    firstK(1);
}

function firstK(k) {
    let xhr = new XMLHttpRequest();


    xhr.open('POST', `http://localhost:8080/processor/first-k/` + k);
    xhr.responseType = 'json';
    xhr.setRequestHeader('Content-Type', 'application/json');

    xhr.onload = function () {
        if (xhr.status != 200) {
            const errorResponse = xhr.response;
            console.log(errorResponse);
            setTextToResult("ОШИБКА:\n" + errorResponse.message);
            return;
        }
        const response = xhr.response;

        setGrammarResponseToResult(response);
    };
    xhr.onerror = function () {
        alert(`Error when trying to get grammars`);
    }

    let grammar = retrieveGrammar();
    console.log(JSON.stringify(grammar));
    xhr.send(JSON.stringify(grammar));

}

const findFirst2Button = document.getElementById("findFirst_2");
findFirst2Button.onclick = function () {
    firstK(2);
}

const showCfgsCloseChooseGrammarButton = document.getElementById("show-cfgs-close-button");

const showCfgsPopupContent = document.getElementById("show-cfgs");
const showCfgsEmptyPage = document.getElementById("show-cfgs-empty-page");
const defaultPageSize = 3;
let currentPageNumber = 1;


function setTextToResult(text) {
    const resultTextArea = document.getElementById("result");
    resultTextArea.value = text;
}

function setGrammarResponseToResult(response) {

    const resultTextArea = document.getElementById("result");

    let nonTerminals = response.nonTerminals;
    const terminals = response.terminals;
    const startSymbol = response.startSymbol;
    const nonTerminalToSolution = response.nonTerminalToSolution;

    let result = "Стартовый символ: " + startSymbol + "\n";

    result += "\nНетерминалы: ";
    for (let i = 0; i < nonTerminals.length; i++) {
        result += nonTerminals[i];

        if (i !== nonTerminals.length - 1) {
            result += " , ";
        }
    }

    result += "\nТерминалы: ";
    for (let i = 0; i < terminals.length; i++) {
        result += terminals[i];

        if (i !== terminals.length - 1) {
            result += " , ";
        }
    }

    result += "\nРешение: ";

    let obj = nonTerminalToSolution;
    for (var prop in obj) {
        if (Object.prototype.hasOwnProperty.call(obj, prop)) {
            result += "\n\t" + prop + " = {\n";
            for (let i = 0; i < obj[prop].length; i++) {
                result += obj[prop][i];

                if (i !== obj[prop].length - 1) {
                    result += " , ";
                }
            }
            result += "\n\t}\n";
        }
    }

    resultTextArea.value = result;
}

function extractGrammar(grammar) {
    const terminalsTextArea = document.getElementById("terminals");
    const nonTerminalsTextArea = document.getElementById("non-terminals");
    const definingEquationsTextArea = document.getElementById("defining-equations");

    let nonTerminals = grammar.nonTerminals;
    const terminals = grammar.terminals;
    const startSymbol = grammar.startSymbol;
    const definingEquations = grammar.definingEquations;

    let nonTerminalsText = "" + startSymbol;

    if (nonTerminals.length > 1) {
        nonTerminalsText += " , ";
    }
    for (let i = 0; i < nonTerminals.length; i++) {
        if (nonTerminals[i] == startSymbol) {
            continue;
        }

        nonTerminalsText += nonTerminals[i];

        if (i !== nonTerminals.length - 1) {
            nonTerminalsText += " , ";
        }
    }

    let terminalsText = "";
    for (let i = 0; i < terminals.length; i++) {
        terminalsText += terminals[i];

        if (i !== terminals.length - 1) {
            terminalsText += " , ";
        }
    }

    let definingEquationsText = "";
    for (let i = 0; i < definingEquations.length; i++) {
        definingEquationsText += definingEquations[i];

        if (i !== definingEquations.length - 1) {
            definingEquationsText += " ,\n";
        }
    }

    nonTerminalsTextArea.value = nonTerminalsText;
    terminalsTextArea.value = terminalsText;
    definingEquationsTextArea.value = definingEquationsText;
}

function retrieveGrammar() {
    const terminalsTextArea = document.getElementById("terminals");
    const nonTerminalsTextArea = document.getElementById("non-terminals");
    const definingEquationsTextArea = document.getElementById("defining-equations");

    const nonTerminalsText = nonTerminalsTextArea.value;

    let startSymbol;
    let nonTerminals = nonTerminalsText.split(",");
    for (let i = 0; i < nonTerminals.length; i++) {
        nonTerminals[i] = nonTerminals[i].trim();
    }

    if (nonTerminals.length < 1) {
        startSymbol = null;
    } else {
        startSymbol = nonTerminals[0];
    }

    let terminals = terminalsTextArea.value.split(",");
    for (let i = 0; i < terminals.length; i++) {
        terminals[i] = terminals[i].trim();
    }



    let definingEquations = definingEquationsTextArea.value.split(",");
    for (let i = 0; i < definingEquations.length; i++) {
        definingEquations[i] = definingEquations[i].trim();
    }



    let grammar = {
        "startSymbol": startSymbol,
        "nonTerminals": nonTerminals,
        "terminals": terminals,
        "definingEquations": definingEquations
    };

    return grammar;
}

prevPageButton.onclick = function () {
    if (currentPageNumber <= 1) {
        return;
    }

    currentPageNumber--;
    let children = currPageNumberSpan.childNodes;
    for (let i = 0; i < children.length; i++) {
        currPageNumberSpan.removeChild(children[i]);
    }
    let currPageTextNode = document.createTextNode(currentPageNumber);
    currPageNumberSpan.appendChild(currPageTextNode);

    removeElemsWithClassName("cfg");
    chooseGrammarButton.onclick.apply();
}

nextPageButton.onclick = function () {
    if (currentPageNumber < 1) {
        return;
    }

    currentPageNumber++;
    let children = currPageNumberSpan.childNodes;
    for (let i = 0; i < children.length; i++) {
        currPageNumberSpan.removeChild(children[i]);
    }
    let currPageTextNode = document.createTextNode(currentPageNumber);
    currPageNumberSpan.appendChild(currPageTextNode);

    removeElemsWithClassName("cfg");
    chooseGrammarButton.onclick.apply();
}

chooseGrammarButton.onclick = function () {

    let xhr = new XMLHttpRequest();


    xhr.open('GET', `http://localhost:8080/examples?page-number=${currentPageNumber}&page-size=${defaultPageSize}`);
    xhr.responseType = 'json';

    xhr.onload = function () {
        if (xhr.status != 200) {
            alert(`ERROR`);
            return;
        }
        const response = xhr.response;

        const examples = response.content;
        const pageNumber = response.pageNumber;
        const totalCount = response.totalCount;
        const perPageCount = response.perPageCount;
        const isFirst = response.isFirst;
        const isLast = response.isLast;

        removeElemsWithClassName("cfg");
        for (let i = examples.length - 1; i >= 0; i--) {
            console.log(totalCount);
            createExampleElement(examples[i]);
        }
        if (examples.length > 0) {
            showCfgsEmptyPage.style.display = "none";
        } else {
            showCfgsEmptyPage.style.display = "block"
        }

        if (showCfgsPopupContent.style.display !== "flex") {
            showCfgsPopupContent.style.display = "flex";
        }
    };
    xhr.onerror = function () {
        alert(`Error when trying to get grammars`);
    }

    xhr.send();


}

showCfgsCloseChooseGrammarButton.onclick = function () {
    showCfgsPopupContent.style.display = "none";
};

