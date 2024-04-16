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
    let terminalsTitleTextNode = document.createTextNode("Терминалы");
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
    let nonTerminalsTitleTextNode = document.createTextNode("Нетерминалы");
    nonTerminalsTitleNode.appendChild(nonTerminalsTitleTextNode);

    let nonTerminalsContentNode = document.createElement("p");
    let nonTerminalsContentText = "{";
    for (let i = 0; i < nonTerminals.length; i++) {
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
    let definingEquationsTitleTextNode = document.createTextNode("Определяющие уравнения");
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

    cfgElem.appendChild(pId);
    insertAfter(pId, pIdTextNode);

    insertAfter(pIdTextNode, terminalsTitleNode);
    insertAfter(terminalsTitleNode, terminalsContentNode);
    insertAfter(terminalsContentNode, nonTerminalsTitleNode);
    insertAfter(nonTerminalsTitleNode, nonTerminalsContentNode);
    insertAfter(nonTerminalsContentNode, definingEquationsTitleNode);
    insertAfter(definingEquationsTitleNode, definingEquationsContentNode);

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

const showCfgsCloseChooseGrammarButton = document.getElementById("show-cfgs-close-button");

const showCfgsPopupContent = document.getElementById("show-cfgs");
const showCfgsEmptyPage = document.getElementById("show-cfgs-empty-page");
const defaultPageSize = 3;
let currentPageNumber = 1;

prevPageButton.onclick = function() {
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

nextPageButton.onclick = function() {
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

    let currDisplay = showCfgsPopupContent.style.display;
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

        for (let i = examples.length - 1; i >= 0; i--) {
            console.log(totalCount);
            createExampleElement(examples[i]);
        }
        if (examples.length > 0) {
            showCfgsEmptyPage.style.display = "none";
        } else {
            showCfgsEmptyPage.style.display = "block"
        }
    };
    xhr.onerror = function () {
        alert(`Error when trying to get grammars`);
    }

    xhr.send();

    console.log(currDisplay);
    if (currDisplay === "none" || currDisplay === "") {
        showCfgsPopupContent.style.display = "flex";
    }
}

showCfgsCloseChooseGrammarButton.onclick = function () {
    showCfgsPopupContent.style.display = "none";
};
// <div class="cfg">
//                     <p>#2</p>
//                     <p class="ta-left">Терминалы:</p>
//                     <p>{a, b, c}</p>
//                     <p class="ta-left">Нетерминалы:</p>
//                     <p>{A, B, C}</p>
//                     <p class="ta-left">Определяющие уравнения:</p>
//                     <pre>
// {
//     A = B | C | a;
//     C = b;
// //     B = c;
// }</pre>
//                     <p>
//                         <chooseGrammarButton type="button">Выбрать</button>
//                     </p>
//                 </div>

