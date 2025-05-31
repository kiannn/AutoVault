<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
    <head>
        <link href="/css/bootstrap.min.css"  rel="stylesheet">
        <title>title</title>
    </head>
    <body style=" background-image: url('/images/cari.jpg'); background-size: cover; background-position-y: 15px">
    <%@include file="jspfs/navigationBar.jspf"%>
    <div class="container">
        <p style="font-size: 20px; font-weight: bold; color:black; text-align: center;">Add a new Model and Make</p>
        
        <%@include  file="jspfs/addToDropDownList.jspf" %>   
       <p style="text-align: center"> 
        
        </p>
    </div>

        <%@include file="jspfs/bootstrapJS_jqueryJS.jspf"%>
    </body>
</html>
<script type="text/javascript">

    function resset(){
        document.getElementById("clearMake").value='';
        document.getElementById("clearModel").value='';
    }
</script>