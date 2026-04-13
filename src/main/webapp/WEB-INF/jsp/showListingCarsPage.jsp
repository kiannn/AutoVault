<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="/css/bootstrap.min.css"  rel="stylesheet">
        <link href="/css/listingTable.css"  rel="stylesheet">

        <title>All Available Vehicles</title>

        <style>
            #theParentDiv{
                display: flex;
                flex-direction: column;
                justify-content: space-between;
                align-items: center;
                height:65px;
            }
            #theParentDiv h5{
                font-weight: bolder
            }
            #theParentDiv span{
                text-align: center;
                box-shadow: 0 30px 40px rgba(0,0,0,0.4);
            }

        </style>
    </head>
    <body style=" background-image: url('/images/wps.jpg');">
        <%@include file="jspfs/navigationBar.jspf"%> 
        <div class="container">
            <div id="theParentDiv">
                <h5> All Available Vehicles </h5>
                <c:if test="${addOrEditMsg!=null}">
                    <span  id="msg" style=""
                           class="pb-0 pt-0 w-50 alert ${addOrEditMsg.contains('delet')?' alert-warning':' alert-success'}  alert-link">${addOrEditMsg}
                    </span>
                </c:if>
            </div>
            <%@include file="jspfs/table.jspf"%>   
            
        </div>       
        <%@include file="jspfs/bootstrapJS_jqueryJS.jspf"%>
        <script src="/js/tableFunctionalities.js"></script>
        <script  type="text/javascript">
              setTimeout(function () {
                   document.getElementById("msg").style.display = "none";
              }, 3000);
        </script> 
    </body>
</html>


<!--------------------------------------------------------------
Old approach, when 'addOrEditMsg' was included in request parameter:
 c:if test="${param.addOrEditMsg!=null}" 
    <span style="margin-top:50px ; position: absolute" id="msg"
          class="alert ${param.addOrEditMsg.contains('delet')?' alert-warning':' alert-success'}  alert-link">${param.addOrEditMsg}
    </span>
 /c:if 
--------------------------------------------------------------->