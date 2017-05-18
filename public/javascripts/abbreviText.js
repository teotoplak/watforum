/**
 * Created by teo on 5/16/17.
 */
/*Fold text to abbreviation JS*/
var steps = 35;
var timeStep = 9;
var stepPerElement = [];
var textElements = [];
var textElementsCurrentWidths = [];
var textElementsInitialWidths = [];
var timeoutId;

async function abbreviText(abbrvTextId,textElementIds) {
    //prepare all data
    for(var index = 0; index<textElementIds.length;index++) {
        var textElement = document.getElementById(textElementIds[index]);
        var initialWidth = parseInt($(textElement).css('width'), 10);
        var step = initialWidth / steps;
        stepPerElement.push(step);
        textElements.push(textElement);
        textElementsCurrentWidths.push(initialWidth);
        textElementsInitialWidths.push(initialWidth);
    }
    await sleep(1100);
    foldIn()
    var abbrvText = document.getElementById(abbrvTextId);
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
    for(var counter = 0; counter < steps; counter++) {
        await sleep(timeStep);
        for(var index = 0; index < textElements.length; index++) {
            var currentWidth = textElementsCurrentWidths[index];
            var newWidth = currentWidth + stepPerElement[index];
            textElementsCurrentWidths[index] = newWidth;
            textElements[index].style.maxWidth = newWidth + "px";
        }
    }
    for(var index = 0; index < textElements.length; index++) {
        textElements[index].style.maxWidth = textElementsInitialWidths[index] + "px";
        textElementsCurrentWidths[index] = textElementsInitialWidths[index];
    }
}
async function foldIn(textElement) {
    for(var counter = 0; counter < steps; counter++) {
        await sleep(timeStep);
        for(var index = 0; index < textElements.length; index++) {
            var currentWidth = textElementsCurrentWidths[index];
            var newWidth = currentWidth - stepPerElement[index];
            textElementsCurrentWidths[index] = newWidth;
            textElements[index].style.maxWidth = newWidth + "px";
        }
    }
    for(var j = 0; j < textElements.length; j++) {
        textElements[j].style.maxWidth = 0 + "px";
        textElementsCurrentWidths[j] = 0;
    }
}