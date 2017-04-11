var table = document.getElementById("SWTYearTable")
var addYear = document.getElementById("addYear")
var addButton = document.getElementById("addButton");
var addAgency = document.getElementById("addAgency");
var addCountry = document.getElementById("addCountry");


//year selector
var current = new Date().getFullYear();
for(var i=0; i<=15; i++) {
    var option = document.createElement('option');
    option.value = current - i;
    option.innerHTML = current - i;
    addYear.appendChild(option);
}
addYear.value = current;
addYear.style.minWidth = "80px";

addButton.addEventListener("click", function(){
var addingYear = addYear.options[addYear.selectedIndex].value;
if(table.rows.length == 2) {
    addYearToRow(1);
    return;
}
var lastYear;
    for (var i = 1; i < table.rows.length-1; i++) {
        var row = table.rows[i];
        lastYear = row.cells[0].innerHTML;
        if(lastYear == addingYear) {
            alert("You already added that year!");
            return;
        }
        if(lastYear < addingYear) {
            addYearToRow(i);
            return;
        }
    }
    addYearToRow(table.rows.length-1);

});


function addYearToRow(i) {
    var row = table.insertRow(i);
    var cell1 = row.insertCell(-1);
    var cell2 = row.insertCell(-1);
    var cell3 = row.insertCell(-1);
    var cell4 = row.insertCell(-1);
    cell1.innerHTML = addYear.options[addYear.selectedIndex].value;
    cell2.innerHTML = addAgency.value;
    cell3.innerHTML = addCountry.value;
    var deleteButton = document.createElement("button");
    deleteButton.className = "btn btn-danger";
    deleteButton.innerHTML = "<span class=' glyphicon glyphicon-minus'></span>";
    deleteButton.addEventListener("click", function(){
        var rowToDelete = deleteButton.parentNode.parentNode;
        rowToDelete.parentNode.removeChild(row);
    });
    cell4.appendChild(deleteButton);

    addAgency.value = "";
    addCountry.value = "";
}