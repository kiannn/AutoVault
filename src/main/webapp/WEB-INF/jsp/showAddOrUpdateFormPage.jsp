<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="/css/bootstrap.min.css"  rel="stylesheet">
        <link href="/css/bootstrap-datepicker.standalone.min.css" rel="stylesheet">
        <link href="/css/add_edite_page.css" rel="stylesheet" />
        
        <title>Add Or Update Page</title>
    </head>
    <body style=" background-image: url('/images/plain_white_wallpaper.jpg');">
        <%@include file="jspfs/navigationBar.jspf"%>
        <div class="container  mb-5 alert-link" >
            <c:if test="${message.contains('Edit') && docdelet!=null}">
                    <!---------------------------------------------------------- 
                    Old approach, when 'docdelet' was included in request parameter:
                    <span style="position: absolute; height: 40px;" id="msg" class="alert alert-warning pt-2">${param.docdelet}</span> 
                    ------------------------------------------------------------>        
                <span id="msg" style="left:31.5%; top:12.30%; box-shadow: 0 20px 40px rgba(0,0,0,0.4);" class="position-absolute  alert alert-warning pt-0 pb-0">${docdelet}</span>
            </c:if>  
            <p style="color:black; font-weight: bolder; font-size: 20px;text-align: center; margin-top: 40px">${message}</p>
            <p style="color:darkcyan; font-size: 16px; text-align: center;">Enter vehicle details and press ${button} button to ${button.toLowerCase()} it</p>
            <%@include file="jspfs/form.jspf"%>
        </div>            
        <%@include file="jspfs/bootstrapJS_jqueryJS.jspf"%>
        
        <script src="/js/bootstrap-datepicker.min.js"></script>
        <script> const a = '${listOfAcceptedfileExts}';</script> <!-- "const a = '&dollar;{listOfAcceptedfileExts}'" is not readable in the formFunctionalities.js file, So I had to put it in a separate script  -->
        <script src="/js/formFunctionalities.js"></script>
        <script>
            setTimeout(function () {
            (document.getElementById("msg")).style.display = "none";
            }, 3000);
        </script>
    </body>
</html>
