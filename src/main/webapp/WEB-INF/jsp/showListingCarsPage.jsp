<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="/css/bootstrap.min.css"  rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <title>All Available Vehicles</title>
    </head>
    <body>
        <%@include file="jspfs/navigationBar.jspf"%> 
        
        <div class="container">
            <div style="margin-bottom: 75px; justify-content: center; display: flex">
                <h5 style="color:darkgray; font-weight: bolder; text-align: center;">
                    All Available Vehicles
                </h5>
                <!--------------------------------------------------------------
                Old approach, when 'addOrEditMsg' was included in request parameter:
                    <c:if test="${param.addOrEditMsg!=null}">
                        <span style="margin-top:50px ; position: absolute" id="msg"
                              class="alert ${param.addOrEditMsg.contains('delet')?' alert-warning':' alert-success'}  alert-link">${param.addOrEditMsg}
                        </span>
                    </c:if>
                --------------------------------------------------------------->
                <c:if test="${addOrEditMsg!=null}">
                    <span style="margin-top:50px ; position: absolute" id="msg"
                          class="alert ${addOrEditMsg.contains('delet')?' alert-warning':' alert-success'}  alert-link">${addOrEditMsg}
                    </span>
                </c:if>
            </div>

            <%@include file="jspfs/table.jspf"%>   
           
            <span id="recordNumber" style="font-size:13px; font-weight: bold; font-style: oblique; background-color: yellow; text-align: center">
                Number of records found : ${showAll.size()}
            </span><br><br><br>
        </div>       
        <%@include file="jspfs/bootstrapJS_jqueryJS.jspf"%>
    </body>
</html>

<script  type="text/javascript">
   
    setTimeout(function(){ 
      document.getElementById("msg").style.display = "none"; 
    }, 2000); 
    
</script> 