    var userId = document.currentScript.getAttribute('data-userId');
    var table = document.getElementById("SWTYearTable")
    var addYear = document.getElementById("addYear")
    var addButton = document.getElementById("addButton");
    var addAgency = document.getElementById("addAgency");


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
        var addingYear = addYear.options[addYear.selectedIndex].value;
        var addingAgency = addAgency.value;
            cell1.innerHTML = addingYear;
        cell2.innerHTML = addAgency.value;
        var deleteButton = document.createElement("button");
        deleteButton.className = "btn btn-danger";
        deleteButton.innerHTML = "<span class=' glyphicon glyphicon-minus'></span>";
        deleteButton.addEventListener("click", function(){
            var rowToDelete = deleteButton.parentNode.parentNode;
            rowToDelete.parentNode.removeChild(row);
            var obj = {
                year: addingYear,
                agency: addingAgency,
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
        });
        cell3.appendChild(deleteButton);
        var yearInput = document.createElement("input");
        yearInput.setAttribute("type", "hidden");
        yearInput.setAttribute("value", addingYear);
        yearInput.setAttribute("name", "year[" + i + "][year]");
        row.appendChild(yearInput);
        var agencyInput = document.createElement("input");
        agencyInput.setAttribute("type", "hidden");
        agencyInput.setAttribute("value", addingAgency);
        agencyInput.setAttribute("name", "year[" + i + "][agency]");
        row.appendChild(agencyInput);

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