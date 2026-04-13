<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
    <head>
        <title>title</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="/css/bootstrap.min.css"  rel="stylesheet">
    </head>
    <body>
        <%@include file="jspfs/navigationBar.jspf"%>
        <c:if test="${deleteAccountMSG!=null}">
            <div class="position-absolute" style="font-size: 15px; top:15%; left: 40%">
                <span class="alert alert-danger alert-link pb-2 pt-2" id="msg">${deleteAccountMSG}</span>
            </div>
        </c:if>
        <div class="container w-50 bg-light p-4 text-dark alert-link" style="font-size: 13.25px; margin-bottom: 100px;">
            <h4 class="mb-2 mt-4">Delete your Account</h4>  
            <fieldset>
                <h6 class="mb-4 mt-3">Are you sure you want to delete your account ?<br>
                This will permanently remove your entire account and you can not undo it</h6>  
                <div style="display: flex; justify-content: center; align-items: center; gap:50px;">
                    <input type="button" class="btn btn-dark alert-link" value="Proceed to Delete Acoount" onclick="showPasswordsFields()"/>
                    <input type="button" class="btn btn-info alert-link" value="Cancel" onclick='window.location.href="/userpro"'/>
                </div>
            </fieldset>
            
            <c:set var="show" value="${showPassF==true ? 'display: block;' : 'display:none;'}"/> 

            <form:form modelAttribute="deletpage">
                <fieldset class ="p-5 shadow-lg bg-white  mt-5" id="passF" style="${show} border: 1px solid darkgrey; border-radius: 10px; line-height: 10px ">
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <form:label path="currentPass" for="idCurrentPass">Enter Password</form:label>
                            <form:input  path="currentPass"  type="password" cssClass="form-control" oninput="makeEnable()" id="idCurrentPass" placeholder="Enter Current Password" maxlength="20" required="true"/>
                            <form:errors path="currentPass" cssClass="text-danger font-weight-normal"  style="line-height: 20px;"/>
                        </div>
                    </div>  
                    <div class="form-row">  
                        <div class="form-group col-md-6">
                            <form:label path="confirmPass" for="idNonfirmNewPass">Confirm Password</form:label>
                            <form:input  path="confirmPass"  type="password" cssClass="form-control" oninput="makeEnable()" id="idCurrentPass" placeholder="Confirm New Password" maxlength="20" required="true"/>
                            <form:errors  cssClass="text-danger font-weight-normal"  style="line-height: 20px;"/>
                        </div>
                    </div> 
                    <div style=" margin-top: 15px;"> 
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
          document.getElementById("msg").style.display = "none"; 
        }, 3000); 
        
    function showPasswordsFields() {
        
        document.getElementById("passF").style.display = "block";
    

}
</script>