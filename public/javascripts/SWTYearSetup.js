var userId = document.currentScript.getAttribute('data-userId');
var table = document.getElementById("SWTYearTable")
var addYear = document.getElementById("addYear")
var addButton = document.getElementById("addButton");
var addAgency = document.getElementById("addAgency");
var deleteButtons = document.getElementsByClassName("deleteButton");
var currentYear;

//put delete buttons on existing data
for(var i = 0; i < deleteButtons.length; i++)
{
    var currentDeleteBtn = deleteButtons.item(i);
    currentDeleteBtn.addEventListener("click", createDeleteBtnFunction(currentDeleteBtn));
}

//year selector
currentYear = new Date().getFullYear();
for (var i = 0; i <= 12; i++) {
    var option = document.createElement('option');
    option.value = currentYear - i;
    option.innerHTML = currentYear - i;
    addYear.appendChild(option);
}
addYear.value = currentYear;

//add button listener
addButton.addEventListener("click", function () {
    var addingYear = addYear.options[addYear.selectedIndex].value;
    //if there is no years
    if (table.rows.length == 1) {
        addYearToRow(1);
        return;
    }
    var lastYear;
    for (var i = 1; i < table.rows.length; i++) {
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

    if(table.rows.length == 1) {
        $(table).show();
    }

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

    if(currentYear == addingYear) {
        $('.currentParticipantInfo').slideDown();
    }


}

//this is because of the closures in javascript
function createDeleteBtnFunction(deleteButton) {
    return function () {
        deleteBtnFunction(deleteButton);
    }
}

function deleteBtnFunction(deleteButton) {

    if(!confirm("All ratings you made with this SWT year will be deleted, proceed?")) {
        return
    }

    var rowToDelete = deleteButton.parentNode.parentNode;

    var yearText = rowToDelete.cells[0].innerHTML;
    var agencyText = rowToDelete.cells[1].innerHTML;

    //trimming string
    yearText = yearText.replace(/\s/g,'')
    agencyText = $.trim(agencyText)

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