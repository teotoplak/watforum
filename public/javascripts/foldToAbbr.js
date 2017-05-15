/**
 * Created by teo on 5/16/17.
 */
function setFoldEffect(textElementId) {
    var textElement = document.getElementById(textElementId);
    var text = textElement.innerHTML;
    var dimensions = calculateWordDimensions(text);
    var width = dimensions.width;
    textElement.style.maxWidth = "15px";

}

// textElement should be span id
function foldIn(textElement) {

}
function foldOut(textElement) {


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