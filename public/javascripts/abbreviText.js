/**
 * Created by teo on 5/16/17.
 */
/*Fold text to abbreviation JS*/
let steps = 35;
let timeStep = 9;
let stepPerElement = [];
let textElements = [];
let textElementsCurrentWidths = [];
let textElementsInitialWidths = [];
let timeoutId;

async function abbreviText(abbrvTextId,textElementIds) {
    //prepare all data
    for(let index = 0; index<textElementIds.length;index++) {
        let textElement = document.getElementById(textElementIds[index]);
        let initialWidth = parseInt($(textElement).css('width'), 10);
        let step = initialWidth / steps;
        stepPerElement.push(step);
        textElements.push(textElement);
        textElementsCurrentWidths.push(initialWidth);
        textElementsInitialWidths.push(initialWidth);
    }
    await sleep(1100);
    foldIn();
    let abbrvText = document.getElementById(abbrvTextId);
    abbrvText.onmouseover = function() {
        clearTimeout(timeoutId);
        foldOut();
    };
    abbrvText.onmouseout = function () {
            timeoutId = setTimeout(function () {
                foldIn();
            }, 500);
    };
}
function sleep(ms) {
    return new Promise((resolve) => setTimeout(resolve, ms));
}
async function foldOut() {
    for(let counter = 0; counter < steps; counter++) {
        await sleep(timeStep);
        for(let index = 0; index < textElements.length; index++) {
            let currentWidth = textElementsCurrentWidths[index];
            let newWidth = currentWidth + stepPerElement[index];
            textElementsCurrentWidths[index] = newWidth;
            textElements[index].style.maxWidth = newWidth + "px";
        }
    }
    for(let index = 0; index < textElements.length; index++) {
        textElements[index].style.maxWidth = textElementsInitialWidths[index] + "px";
        textElementsCurrentWidths[index] = textElementsInitialWidths[index];
    }
}
async function foldIn(textElement) {
    for(let counter = 0; counter < steps; counter++) {
        await sleep(timeStep);
        for(let index = 0; index < textElements.length; index++) {
            let currentWidth = textElementsCurrentWidths[index];
            let newWidth = currentWidth - stepPerElement[index];
            textElementsCurrentWidths[index] = newWidth;
            textElements[index].style.maxWidth = newWidth + "px";
        }
    }
    for(let j = 0; j < textElements.length; j++) {
        textElements[j].style.maxWidth = 0 + "px";
        textElementsCurrentWidths[j] = 0;
    }
}