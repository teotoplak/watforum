/**
 * Created by teo on 5/16/17.
 */
/*Fold text to abbreviation JS*/
var steps = 35;
var timeStep = 7;
var stepPerElement = [];
var textElements = [];
var textElementsCurrentWidths = [];

async function setFoldEffect(initialLetterIds,textElementIds) {
    //process all elements steps
    for(var i = 0; i<textElementIds.length;i++) {
        var textElement = document.getElementById(textElementIds[i]);
        var initialWidth = parseInt($(textElement).css('width'), 10);
        var step = initialWidth / steps;
        stepPerElement.push(step);
        textElements.push(textElement);
        textElementsCurrentWidths.push(initialWidth);
    }
    await sleep(1100);
    foldIn()
    for(var i = 0; i<initialLetterIds.length;i++) {
        var initialLetter = document.getElementById(initialLetterIds[i]);
        initialLetter.onmouseout = createFoldInFunction()
        initialLetter.onmouseover = createFoldOutFunction()
    }

}

function sleep(ms) {
    return new Promise((resolve) => setTimeout(resolve, ms));
}

function createFoldOutFunction() {
    return function () {
        foldOut();
    }
}

function createFoldInFunction() {
    return function () {
        foldIn();
    }
}

// textElement should be span id
async function foldOut() {
    for(var i = 0; i < steps; i++) {
        await sleep(timeStep);
        for(var j = 0; j < textElements.length; j++) {
            var currentWidth = textElementsCurrentWidths[j];
            var stepi = stepPerElement[j];
            var newWidth = currentWidth + stepi;
            textElementsCurrentWidths[j] = newWidth;
            textElements[j].style.maxWidth = newWidth + "px";
        }
    }
  
}
async function foldIn(textElement) {
    for(var i = 0; i < steps; i++) {
        await sleep(timeStep);
        for(var j = 0; j < textElements.length; j++) {
            var currentWidth = textElementsCurrentWidths[j];
            var stepi = stepPerElement[j];
            var newWidth = currentWidth - stepi;
            textElementsCurrentWidths[j] = newWidth;
            textElements[j].style.maxWidth = newWidth + "px";
        }
    }
    for(var j = 0; j < textElements.length; j++) {
        textElements[j].style.maxWidth = 0 + "px";
        textElementsCurrentWidths[j] = 0;
    }

}



/**
* Calculate word dimensions for given text using HTML elements.
* Optionally classes can be added to calculate with
    * a specific style / layout.
*
* @param {String} text The word for which you would like to know the
*   dimensions.
* @param {String[]} [classes] An array of strings which represent
*   css classes which should be applied to the DIV which is used for
    *   the calculation of word dimensions.
* @param {Boolean} [escape] Whether or not the word should be escaped.
*   Defaults to true.
* @return {Object} An object with width and height properties.
*/
var calculateWordDimensions = function(text, classes, escape) {
    classes = classes || [];

    if (escape === undefined) {
        escape = true;
    }

    classes.push('foldText');

    var div = document.createElement('div');
    div.setAttribute('class', classes.join(' '));

    if (escape) {
        $(div).text(text);
    } else {
        div.innerHTML = text;
    }

    document.body.appendChild(div);

    var dimensions = {
        width : jQuery(div).outerWidth(),
        height : jQuery(div).outerHeight()
    };

    div.parentNode.removeChild(div);

    return dimensions;
};