var userId = document.currentScript.getAttribute('data-userId');
var table = document.getElementById("SWTYearTable");
var addYear = document.getElementById("addYear");
var addSponsorId = document.getElementById("sponsorId");
var addButton = document.getElementById("addButton");
var addSponsor = document.getElementById("addSponsor");
var deleteButtons = document.getElementsByClassName("deleteButton");
var currentYear;

//put delete buttons on existing data
for(var i = 0; i < deleteButtons.length; i++)
{
    var currentDeleteBtn = deleteButtons.item(i);
    currentDeleteBtn.addEventListener("click", createDeleteBtnFunction(currentDeleteBtn));
}

//year selector
currentYear = 2017;
for (var i = -1; i <= 11; i++) {
    var option = document.createElement('option');
    option.value = currentYear - i;
    option.innerHTML = currentYear - i;
    addYear.appendChild(option);
}
addYear.value = currentYear;

//add button listener
addButton.addEventListener("click", function () {
    let addingYear = addYear.options[addYear.selectedIndex].value;
    //if there is no years
    if (table.rows.length == 1) {
        addYearToRow(1);
        return;
    }
    let lastYear;
    for (var i = 1; i < table.rows.length; i++) {
        var row = table.rows[i];
        lastYear = row.cells[0].innerHTML;
        //trimming
        lastYear = lastYear.replace(/\s/g, '');

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

    let addingYear = addYear.options[addYear.selectedIndex].value;
    let addingSponsor = addSponsor.value;
    let sponsorId = addSponsorId.value;

    let obj = {
        year: addingYear,
        userId: userId
    };

    if(sponsorId) {
        obj.sponsorId = sponsorId;
    } else {
        obj.newSponsorName = addingSponsor;
    }

    $.ajax({
        type: 'POST',
        url: '/saveYear',
        data: JSON.stringify(obj),
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (msg, status, jqXHR) {
            var swtYear = msg;

            if(table.rows.length == 1) {
                $(table).show();
            }

            var row = table.insertRow(i);
            row.setAttribute('data-year-id', `${swtYear.id}`);
            var cell1 = row.insertCell(-1);
            var cell2 = row.insertCell(-1);
            var cell3 = row.insertCell(-1);

            cell1.innerHTML = addingYear;
            cell2.innerHTML = swtYear.sponsor.fullName;
            var deleteButton = document.createElement("button");
            deleteButton.className = "btn btn-danger";
            deleteButton.innerHTML = "<span class=' glyphicon glyphicon-minus'></span>";
            cell3.appendChild(deleteButton);
            deleteButton.addEventListener("click", function() { deleteBtnFunction(deleteButton, userId)});
            addAgency.value = "";
            addSponsorId.value = "";
            sponsorId = null;
            if(currentYear == addingYear) {
                $('.currentParticipantInfo').slideDown();
            }

        },
        error: function (jqXHR, textStatus, errorThrown) {
            // TODO
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

    if(!confirm("All ratings you made with this SWT year will be deleted, proceed?")) {
        return
    }

    let rowToDelete = deleteButton.parentNode.parentNode;
    let yearId = rowToDelete.dataset.yearId;

    rowToDelete.parentNode.removeChild(rowToDelete);

    let obj = {
        yearId: yearId
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