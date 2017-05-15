var userId = document.currentScript.getAttribute('data-userId');
var table = document.getElementById("SWTYearTable")
var addYear = document.getElementById("addYear")
var addButton = document.getElementById("addButton");
var addAgency = document.getElementById("addAgency");
var deleteButtons = document.getElementsByClassName("deleteButton")

//put delete buttons on existing data
for(var i = 0; i < deleteButtons.length; i++)
{
    var currentDeleteBtn = deleteButtons.item(i);
    currentDeleteBtn.addEventListener("click", createDeleteBtnFunction(currentDeleteBtn));
}

//year selector
var current = new Date().getFullYear();
for (var i = 0; i <= 15; i++) {
    var option = document.createElement('option');
    option.value = current - i;
    option.innerHTML = current - i;
    addYear.appendChild(option);
}
addYear.value = current;
// addYear.style.minWidth = "80px";

//add button listener
addButton.addEventListener("click", function () {
    var addingYear = addYear.options[addYear.selectedIndex].value;
    //if there is no years
    if (table.rows.length == 2) {
        addYearToRow(2);
        return;
    }
    var lastYear;
    for (var i = 2; i < table.rows.length; i++) {
        var row = table.rows[i];
        lastYear = row.cells[0].innerHTML;
        //trimming
        lastYear = lastYear.replace(/\s/g,'')

        if (lastYear == addingYear) {
            alert("You already added that year!");
            return;
        }
        if (lastYear < addingYear) {
            addYearToRow(i);
            return;
        }
    }
    addYearToRow(table.rows.length);

});

function addYearToRow(i) {

    var row = table.insertRow(i);
    var cell1 = row.insertCell(-1);
    var cell2 = row.insertCell(-1);
    var cell3 = row.insertCell(-1);
    var addingYear = addYear.options[addYear.selectedIndex].value;
    var addingAgency = addAgency.value;
    cell1.innerHTML = addingYear;
    cell2.innerHTML = addAgency.value;
    var deleteButton = document.createElement("button");
    deleteButton.className = "btn btn-danger";
    deleteButton.innerHTML = "<span class=' glyphicon glyphicon-minus'></span>";
    cell3.appendChild(deleteButton);
    deleteButton.addEventListener("click", function() { deleteBtnFunction(deleteButton, userId)});

    addAgency.value = "";

    var obj = {
        year: addingYear,
        agency: addingAgency,
        userId: userId
    };

    $.ajax({
        type: 'POST',
        url: '/saveYear',
        data: JSON.stringify(obj),
        headers: {
            'Content-Type': 'application/json'
        }
    });
}

//this is because of the closures in javascript
function createDeleteBtnFunction(deleteButton) {
    return function () {
        deleteBtnFunction(deleteButton);
    }
}

function deleteBtnFunction(deleteButton) {

    var rowToDelete = deleteButton.parentNode.parentNode;

    var yearText = rowToDelete.cells[0].innerHTML;
    var agencyText = rowToDelete.cells[1].innerHTML;

    //trimming string
    yearText = yearText.replace(/\s/g,'')
    agencyText = agencyText.replace(/\s/g,'')

    rowToDelete.parentNode.removeChild(rowToDelete);

    var obj = {
        year: yearText,
        agency: agencyText,
        userId: userId
    };
    $.ajax({
        type: 'POST',
        url: '/deleteYear',
        data: JSON.stringify(obj),
        headers: {
            'Content-Type': 'application/json'
        }
    });
}