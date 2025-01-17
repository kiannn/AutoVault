<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
    <head>
        <title>title</title>
        <link href="/css/bootstrap.min.css"  rel="stylesheet">
    </head>
    <body>
        <%@include file="jspfs/navigationBar.jspf"%>
        <div class="container justify-content-center w-50 bg-light text-dark alert-link" style="font-size: 13.25px; margin-bottom: 100px;">
            <c:if test="${passwordUpdateMSG!=nuu}">
                <div class="justify-content-center" style="display: flex; font-size: 15px">
                    <c:if  test="${passwordUpdateMSG.contains('Successfully')}">
                        <span class="alert alert-success position-absolute" id="msg0">${passwordUpdateMSG}</span>
                    </c:if>    
                    <c:if  test="${passwordUpdateMSG.contains('Unsuccessful')}">
                        <span class="alert alert-danger position-absolute" id="msg1">${passwordUpdateMSG}</span>
                    </c:if>
                </div>
            </c:if>
            <form:form modelAttribute="passObj" method="POST">
                <h5 class="mb-3 mt-5" style="text-align: left">Current Password</h5>
                <fieldset class="p-3 shadow-lg bg-white mb-2" style="border:2px solid darkgray; border-radius: 10px; line-height: 10px">
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <form:label path="currentPass" for="idCurrentPass">Password</form:label>
                            <form:input  path="currentPass"  type="password" cssClass="form-control" oninput="makeEnable()" id="idCurrentPass" placeholder="Enter Current Password" maxlength="20" required="true"/>
                            <form:errors path="currentPass" cssClass="text-danger font-weight-normal"  style="line-height: 20px;"/>
                        </div>
                    </div>
                </fieldset>
                <h5 class="mb-3 mt-4" style="text-align: left">New Password</h5>
                <fieldset class="p-3 shadow-lg bg-white mb-2" style="border:2px solid darkgray; border-radius: 10px; line-height: 10px">
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <form:label path="newPass" for="idNewPass">Password</form:label>
                            <form:input  path="newPass"  type="password" cssClass="form-control" oninput="makeEnable()" id="idNewPass" placeholder="Enter New Password" maxlength="20" required="true"/>
                            <form:errors path="newPass" cssClass="text-danger font-weight-normal"  style="line-height: 20px;"/>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <form:label path="confirmPass" for="idNonfirmNewPass">Confirm Password</form:label>
                            <form:input  path="confirmPass"  type="password" cssClass="form-control" oninput="makeEnable()" id="idCurrentPass" placeholder="Confirm New Password" maxlength="20" required="true"/>
                            <form:errors  cssClass="text-danger font-weight-normal"  style="line-height: 20px;"/>
                        </div>
                    </div>
                </fieldset>
                <div style="text-align: right; margin-top: 20px;"> 
                    <input type="button" value="Cancel" onclick="window.location.href='/userpro'" disabled="true" id="buttonB" class="btn btn-light btn-lg mr-3" style="border:2px solid blue; color: darkblue"/>
                    <input type="submit" value="Save Chaneges" disabled="true" id="submitB" class="btn btn-dark btn-lg text-black" style="border: 2px solid black;"/>
                </div>
            </form:form>

        </div>

        <%@include file="jspfs/bootstrapJS_jqueryJS.jspf"%>
    </body>
</html>

<script type="text/javascript">

    function makeEnable(){
        
        document.getElementById("buttonB").disabled=false;
        document.getElementById("submitB").disabled=false;
        
    }
   
   setTimeout(function(){ 
      document.getElementById("msg0").style.display = "none"; 
    }, 3000); 
    setTimeout(function(){ 
      document.getElementById("msg1").style.display = "none"; 
    }, 3000); 
</script>