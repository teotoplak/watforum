/**
 * Created by teo on 5/16/17.
 */
/*Fold text to abbreviation JS*/
var steps = 35;
var timeStep = 7;


async function setFoldEffect(initialLetterId,textElementId) {
    var textElement = document.getElementById(textElementId);
    var initialLetter = document.getElementById(initialLetterId);
    var initialWidth = parseInt($(textElement).css('width'), 10);
    initialLetter.addEventListener("mouseover", function () {
        foldOut(textElement,initialWidth)
    }, true);
    initialLetter.addEventListener("mouseout", function () {
        foldIn(textElement)
    }, false);
    // textElement.addEventListener("mouseover",foldOut(textElement,initialWidth),true);
    // textElement.addEventListener("mouseout",foldIn(textElement,initialWidth),true);
    // initialLetter.addEventListener("mouseover",alertMe());
    // initialLetter.addEventListener("mouseover",foldOut(textElement,initialWidth),true);
    // initialLetter.addEventListener("mouseout",foldIn(textElement,initialWidth),false);
    // foldIn(textElement,initialWidth);

}

function sleep(ms) {
    return new Promise((resolve) => setTimeout(resolve, ms));
}

// textElement should be span id
async function foldOut(textElement, initialWidth) {
    var step = initialWidth / steps;
    var currentSize = 0;
    var i;
    for(i = 0; i < steps; i++) {
        await sleep(timeStep);
        currentSize += step;
        textElement.style.maxWidth = currentSize + "px";
    }

}
async function foldIn(textElement) {
    var initialWidth = parseInt($(textElement).css('width'), 10);
    var step = initialWidth / steps;
    var currentSize = initialWidth;
    var i;
    for(i = 0; i < steps; i++) {
        await sleep(timeStep);
        currentSize -= step;
        textElement.style.maxWidth = currentSize + "px";
    }
    textElement.style.maxWidth = 0 + "px";

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