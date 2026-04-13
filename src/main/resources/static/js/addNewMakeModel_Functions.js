
function resset() {
    document.getElementById("make").value = '';
    document.getElementById("model").value = '';
    document.getElementById("model.errors").textContent = ''; // Error message also cleared when 'Clear’  button is pressed

}

window.onload = function () {

    // Scroll the window page to make the selected item visible each time the page is loaded
    
    if (selectedItem !== '') {
        const selectElement = document.getElementById("make");
        for (let i = 0; i < selectElement.options.length; i++) {
            if (selectElement.options[i].value === selectedItem) {

                selectElement.options[i].scrollIntoView({block: "center"});
                break;
            }
        }
    }
};