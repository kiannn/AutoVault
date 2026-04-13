
function tableSelectFunction(value) {

    const form = document.getElementById("myForm");
    var table = document.getElementById("listingTable");
    var rows = table.rows;

    if (value === "select") {

        for (var i = 1; i < rows.length; i++) {

            var lastCell = table.rows[i].cells[table.rows[i].cells.length - 1];
            var divInsideLastCell = lastCell.querySelector('div');

            const parentLabel = document.createElement("label");
            parentLabel.style.backgroundColor = "white"
            parentLabel.style.width = "25px";
            parentLabel.style.height = "25px";
            parentLabel.style.display = "flex";
            parentLabel.style.justifyContent = "center";
            parentLabel.style.alignItems = "center";
            parentLabel.style.borderStyle = "solid";
            parentLabel.style.borderColor = "whitesmoke";
            parentLabel.style.borderWidth = "2px";
            parentLabel.style.borderRadius = "50px";

            const check = document.createElement("input");
            check.type = "checkbox";
            check.name = "idxX";
            check.value = table.rows[i].cells[0].textContent;
            check.style.width = "14px";
            check.style.height = "14px";
            check.style.accentColor = "#b21f2d";

            parentLabel.append(check);
            divInsideLastCell.append(parentLabel);
            lastCell.append(divInsideLastCell);
        }

        const btnd = document.createElement("input");
        btnd.type = "button";
        btnd.value = "delete all";
        btnd.id = "deleteAll";
        btnd.className = "btn btn-warning mr-3";
        btnd.style = "width: 150px";
        btnd.onclick = function () {
            deletFunc(null);
        }

        const btns = document.createElement("input");
        btns.type = "button";
        btns.value = "select all";
        btns.id = "selectAll";
        btns.className = "btn btn-info";
        btns.style = "width: 150px";
        btns.onclick = function () {
            checkboxes = document.getElementsByName('idxX');
            let modeIsSelect = btns.value === "select all";
            for (let ch of checkboxes) {
                ch.checked = modeIsSelect ? true : false;
            }
            btns.value = modeIsSelect ? "de-select all" : "select all";
        };

        document.getElementById("selectAll_addAll_div").append(btnd, btns);

        // In the following we target elements to be ecxluded from getting disabled when 'select' button is clicked
        var select_cancel_button = document.querySelector("thead th:nth-last-child(1) div input");
        select_cancel_button.value = "cancel";
        var allViewDocsButton = document.querySelectorAll("div.lastColumnDiv input[value='view']");
        var all_modal_header_Buttons = document.querySelectorAll("div.modal-header button");
        var all_modal_footer_Buttons = document.querySelectorAll("div.modal-footer button");

        for (let element of form.elements) {
            if (element === select_cancel_button
                    || element.type === "checkbox"
                    || element.value === "delete all"
                    || element.value === "select all"
                    || Array.from(allViewDocsButton).includes(element) // NodeList does not have the includes() method. Use Array.from(...).includes() 
                    || Array.from(all_modal_footer_Buttons).includes(element) // === means whether both variables reference the exact same DOM element (same object in memory) or not
                    || Array.from(all_modal_header_Buttons).includes(element)) {

                continue;
            }
            element.disabled = true;
        }


    } else if (value === "cancel") {

        document.querySelectorAll("div.lastColumnDiv label").forEach((lab) => lab.remove());
        document.querySelector("thead th:nth-last-child(1) div input").value = "select";
        document.getElementById("deleteAll").remove();
        document.getElementById("selectAll").remove();

        for (let element of form.elements) {
            element.disabled = false;
        }
    }
}

function deletFunc(id) {

    if (id !== null && window.confirm("Are you sure you want to delete car id = " + id + " ?")) {

        window.location.href = '/cars/allcars/deletepage/' + id;
        return;
    }

    if (id === null) {

        let ids = "";

        for (let ch of document.getElementsByName("idxX")) {
            ids = ch.checked ? ids + ch.value.concat(',') : ids;
        }

        if (ids === "") {

            window.alert("please select at least one item to delet");

        } else if (window.confirm("Are you sure you want to delet car id = " + ids + " ?")) {

            window.location.href = '/cars/allcars/deletepage/' + ids.substr(0, ids.length - 1);
        }
    }
}

function tableSortChangeURL(rdirect) {

    window.location.href = rdirect;

}

function showModal(modalId) {
    $('#' + modalId).modal('show');
}
