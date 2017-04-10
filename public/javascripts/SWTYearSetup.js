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
    alert(table.rows.length)
if(table.rows.length == 0) {
    addYearToRow(0);
    return;
}
var lastYear;
    for (var i = 0, row; row = table.rows[i]; i++) {
        lastYear = row.cells[0].value;
        if(lastYear <= addingYear) {
            addYearToRow(i);
        }
    }


});


function addYearToRow(i) {
    var row = table.insertRow(i);
    var cell1 = row.insertCell(0);
    var cell2 = row.insertCell(1);
    var cell3 = row.insertCell(3);
    cell1.innerHTML = addYear.value;
    cell2.innerHTML = addAgency.value;
    cell3.innerHTML = addCountry.value;
}

