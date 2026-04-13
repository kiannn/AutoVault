<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
    <head>
        <link href="/css/bootstrap.min.css"  rel="stylesheet">
        <link href="/css/addNewMakeModel.css" rel="stylesheet" />
        
        <title>title</title>
    </head>

    <body style=" background-image: url('/images/cari.jpg'); background-size: cover; background-position-y: 15px; padding-bottom: 30px">
        <%@include file="jspfs/navigationBar.jspf"%>
        <div class="container">
            <p>Add a new Model and Make</p>
            <%@include  file="jspfs/addToDropDownList.jspf" %>   
        </div>
            <%@include file="jspfs/bootstrapJS_jqueryJS.jspf"%>
            <script>   
                const selectedItem = '${carMakeModelDTO.getMake()}'; // "const a = '&dollar;{listOfAcceptedfileExts}'" is not readable in the formFunctionalities.js file, So I had to put it in a separate script.
            </script>
            <script src="/js/addNewMakeModel_Functions.js"></script>
    </body>
</html>
