
$('#datePurchased').datepicker({
    format: 'yyyy-M-dd'

});

function blank() {

    const form = document.getElementById('fo');
    const form_Elements = form.elements;

    for (let i = 0; i < form_Elements.length; i++) {

        const field = form_Elements[i];

        if (field.value === 'Cancel' ||
                field.value === 'Add' ||
                field.value === 'Update' ||
                field.value === 'Clear' ||
                field.value === 'Upload more file' ||
                field.value === 'remove') {

            continue;
        }

        field.value = '';
    }
}

function addOrdelete() {

    const table = document.getElementById("tableAdd");

    if (table.rows.length < 5) {

        var newrow = table.insertRow(-1);
        var cell1 = newrow.insertCell(0);

        var fileUploadDiv = document.createElement("div");
        fileUploadDiv.id = 'addFileDiv';

        var newRemoveBut = document.createElement("input");
        newRemoveBut.type = "button";
        newRemoveBut.value = "remove";
        newRemoveBut.className = "btn";
        newRemoveBut.onclick = function () {
            table.deleteRow(this.parentNode.parentNode.parentNode.rowIndex);
        };

        var newUploaBut = document.createElement("input");
        newUploaBut.type = "file";
        newUploaBut.name = "filee";
        newUploaBut.className = "btn btn-info";
        newUploaBut.oninput = function () {
            checkFileSize(this, "tableAdd");
        };

        var msgExceed = document.createElement("div");
        /* Old approach - we assigned an 'id' to each 'div' within the third 
         *                cell of each row derived from the current row id : msgExceed.id = "msgExceed"+row_Indx;
         * This approach did not work as whenever a row is deleted the rest of the rows indices
         * would be inconsistent with those rows corresponding <div> ids. 
         * If there are three rows with indices 0, 1, 2 and corresponding <div> ids msgExceed0, msgExceed1, msgExceed2
         * and if row index 1 is deleted, then the corresponding <div> of row index 1 would have an id of msgExceed2            
         */
        msgExceed.setAttribute("name", "largeUploadError"); // msgExceed.name="largeUploadError"; will not work for a <div>

        fileUploadDiv.append(newRemoveBut, newUploaBut, msgExceed);
        cell1.append(fileUploadDiv);
    }

}

function checkFileSize(input, tableID) {

    const maxSizeInBytes = 5.5 * 1024 * 1024;
    const file = input.files[0];

    var table = document.getElementById(tableID);

    const rowIndx = input.parentNode.parentNode.parentNode.rowIndex;

    var division = table.rows[rowIndx].cells[table.rows[rowIndx].cells.length - 1].querySelector('div[name = "largeUploadError"]');

    if (file.size > maxSizeInBytes) {
        modifyDiv(division, input, 'Maximum upload size exceeded');

    } else {

        let inputV = input.value;
        inputV = inputV.substring(inputV.lastIndexOf('.'));
        inputV = inputV.concat(","); // inputV must starts with '.' and ends with ','

        //const a = '${listOfAcceptedfileExts}'; --> '${listOfAcceptedfileExts}' is not readable in the js file
        if (!a.includes(inputV)) {

            let msg = inputV.includes('fakepath') ? "Invalid file" :
                    "'" + inputV.substring(1, inputV.length - 1) + "' is invalid format";

            modifyDiv(division, input, msg);
        }
    }

}

function modifyDiv(division, input, msg) {

    division.style.display = "block"; // division.style.display = "block" must be set in order to get 'division' visible for the next invocation
    input.value = '';
    
    division.innerHTML = msg; // could also be divv.textContent
    setTimeout(function () {
        division.style.display = "none";
    }, 3000);

}

