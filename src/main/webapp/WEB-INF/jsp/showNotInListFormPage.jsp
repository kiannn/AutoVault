<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
    <head>
        <link href="/css/bootstrap.min.css"  rel="stylesheet">
        <title>title</title>
    </head>
    <body style=" background-image: url('/images/cari.jpg'); background-size: cover; background-position-y: 15px; padding-bottom: 30px">
        <%@include file="jspfs/navigationBar.jspf"%>
        <div class="container">
            <p style="font-size: 20px; font-weight: bold; color:black; text-align: center;">Add a new Model and Make</p>
            <%@include  file="jspfs/addToDropDownList.jspf" %>   
        </div>

            <%@include file="jspfs/bootstrapJS_jqueryJS.jspf"%>
    </body>
</html>
<script type="text/javascript">

    function resset(){
        document.getElementById("make").value='';
        document.getElementById("model").value='';
        document.getElementById("model.errors").textContent=''; // Error message also cleared when 'Clear’  button is pressed
        
    }
    window.onload = function() {
            const selectedItem = '${carMakeModelDTO.getMake()}';/*car.getMake() : access model attribute named 'car' and invoke a method on it,
                                                                   MUST be wrapped in single quotes*/

            document.getElementById("showP").textContent ='selectedItem='+selectedItem+"      |selectedItem=='' ? "+(selectedItem=='')+"    selectedItem==null?"+(selectedItem==null);
            
            // Scroll the window page to make the selected item visible each time the page is loaded
            if(selectedItem!=null){
                const selectElement = document.getElementById("make");
                for (let i = 0; i < selectElement.options.length; i++) {
                    if (selectElement.options[i].value === selectedItem) {

                        selectElement.options[i].scrollIntoView({ block: "nearest" });
                        break;
                    }
                }
        }
    };
</script>