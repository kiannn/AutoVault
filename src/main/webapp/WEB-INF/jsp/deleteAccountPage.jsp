<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
    <head>
        <title>title</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="/css/bootstrap.min.css"  rel="stylesheet">
        <link rel="stylesheet" href="/css/deleteAccount.css"/>
    </head>
    <body>
        <%@include file="jspfs/navigationBar.jspf"%>

        <div class="container bg-light p-4 alert-link">
            <c:if test="${deleteAccountMSG!=null}">
                <span class="alert alert-danger">${deleteAccountMSG}</span>
            </c:if>
            <h4 class="mb-2 mt-4">Delete your Account</h4>  
            <fieldset class="top_fieldset">
                <h6 class="mb-4 mt-3">Are you sure you want to delete your account ?<br>
                This will permanently remove your entire account and you can not undo it</h6>  
                <div>
                    <input type="button" class="btn btn-dark alert-link" value="Proceed to Delete Acoount" onclick="showPasswordsFields()"/>
                    <input type="button" class="btn btn-info alert-link" value="Cancel" onclick='window.location.href="/userpro"'/>
                </div>
            </fieldset>
            
            <c:set var="show" value="${showPassF==true ? 'display: block;' : 'display:none;'}"/> 

            <form:form modelAttribute="deletpage">
                <fieldset class ="bottom_fieldset p-5 mt-5 shadow-lg bg-white" style="${show}">
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <form:label path="currentPass" for="idCurrentPass">Enter Password</form:label>
                            <form:input  path="currentPass"  type="password" cssClass="form-control" oninput="makeEnable()" id="idCurrentPass" placeholder="Enter Current Password" maxlength="20" required="true"/>
                            <form:errors path="currentPass"/>
                        </div>
                    </div>  
                    <div class="form-row">  
                        <div class="form-group col-md-6">
                            <form:label path="confirmPass" for="idNonfirmNewPass">Confirm Password</form:label>
                            <form:input  path="confirmPass"  type="password" cssClass="form-control" oninput="makeEnable()" id="idCurrentPass" placeholder="Confirm New Password" maxlength="20" required="true"/>
                            <form:errors />
                        </div>
                    </div> 
                    <div> 
                        <input type="submit" value="Delete Account" class="btn btn-dark text-black"/>
                    </div>
                </fieldset>   
            </form:form>

        </div>
        <%@include file="jspfs/bootstrapJS_jqueryJS.jspf"%>
    </body>
</html>

<script type="text/javascript">
    
    setTimeout(function(){ 
          document.querySelector("span.alert").style.display = "none"; 
        }, 3000); 
        
    function showPasswordsFields() {
        
        document.querySelector("fieldset.bottom_fieldset").style.display = "block";
}
</script>