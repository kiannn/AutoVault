<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
    <head>
        <title>title</title>
        <link href="/css/bootstrap.min.css"  rel="stylesheet">
        <link href="/css/passwordUpdate.css" rel="stylesheet"/>
    </head>
    <body>
        <%@include file="jspfs/navigationBar.jspf"%>
        <div class="container bg-light">
            <c:if test="${passwordUpdateMSG!=null}">
                <c:if  test="${passwordUpdateMSG.contains('Successfully')}">
                    <span class="alert alert-success">${passwordUpdateMSG}</span>
                </c:if>    
                <c:if  test="${passwordUpdateMSG.contains('Unsuccessful')}">
                    <span class="alert alert-danger ">${passwordUpdateMSG}</span>
                </c:if>
            </c:if>
            <form:form modelAttribute="passObj" method="POST">
                <h5 class="mb-1 mt-5 ml-1">Current Password</h5>
                <fieldset class="p-3 shadow-lg bg-white mb-2">
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <form:label path="currentPass" for="idCurrentPass">Password</form:label>
                            <form:input  path="currentPass"  type="password" cssClass="form-control" oninput="makeEnable()" id="idCurrentPass" placeholder="Enter Current Password" maxlength="20" required="true"/>
                            <form:errors path="currentPass" cssClass="text-danger font-weight-normal" />
                        </div>
                    </div>
                </fieldset>
                <h5 class="mb-1 mt-4 ml-1">New Password</h5>
                <fieldset class="p-3 shadow-lg bg-white mb-2">
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <form:label path="newPass" for="idNewPass">Password</form:label>
                            <form:input  path="newPass"  type="password" cssClass="form-control" oninput="makeEnable()" id="idNewPass" placeholder="Enter New Password" maxlength="20" required="true"/>
                            <form:errors path="newPass" cssClass="text-danger font-weight-normal" />
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <form:label path="confirmPass" for="idNonfirmNewPass">Confirm Password</form:label>
                            <form:input  path="confirmPass"  type="password" cssClass="form-control" oninput="makeEnable()" id="idCurrentPass" placeholder="Confirm New Password" maxlength="20" required="true"/>
                            <form:errors  cssClass="text-danger font-weight-normal" />
                        </div>
                    </div>
                </fieldset>
                <div id="buttonDiv"> 
                    <input type="button" value="Reset" onclick="window.location.href='/userpass'" disabled="true" class="btn btn-light" />
                    <input type="submit" value="Save Chaneges" disabled="true"  class="btn btn-dark"/>
                </div>
            </form:form>

        </div>

        <%@include file="jspfs/bootstrapJS_jqueryJS.jspf"%>
    </body>
</html>

<script type="text/javascript">

    function makeEnable(){
        
        document.querySelectorAll("#buttonDiv input").forEach(b => b.disabled=false);
        
    }
   
   setTimeout(function(){ 
      document.querySelector("span.alert").style.display = "none"; 
    }, 3000); 
</script>