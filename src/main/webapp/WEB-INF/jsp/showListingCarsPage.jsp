<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
    <head>
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
                <c:if test="${param.addOrEditMsg!=null}">
                    <span style="margin-top:50px ; position: absolute" id="msg"
                          class="alert ${param.addOrEditMsg.contains('delet')?' alert-warning':' alert-success'}  alert-link">${param.addOrEditMsg}
                    </span>
                </c:if>
            </div>

            <!-- sort using SortPage
            
               <p style="margin-left: 110px; font-weight: bold; width: 220px; color: darkblue;">
                    Sort by :&nbsp;&nbsp;  &#60%@include  file = "jspfs/SortPage.jspf"%&#62
                </p><br> 
            -->
            
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