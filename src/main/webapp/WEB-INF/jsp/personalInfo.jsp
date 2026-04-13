<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
    <head>
        <link href="/css/bootstrap.min.css"  rel="stylesheet">
        <link href="/css/bootstrap-datepicker.standalone.min.css" rel="stylesheet">
        <link rel="stylesheet" href="/css/personalInfo.css"/>
        <title>title</title>
    </head>
    <body>
        <%@include file="jspfs/navigationBar.jspf"%>
        <div class="container bg-light">
            <c:if test="${personalInfoUpdateMSG!=null}">
                <div id="msg">
                    <c:if  test="${personalInfoUpdateMSG.contains('successfully')}">
                        <span class="alert alert-success" >${personalInfoUpdateMSG}</span>
                    </c:if>    
                    <c:if  test="${personalInfoUpdateMSG.contains('Unsuccessful')}">
                        <span class="alert alert-danger" >${personalInfoUpdateMSG}</span>
                    </c:if>
                </div>
            </c:if>
            <form:form modelAttribute="personalInfo">
                <h5 class="mb-2 mt-4 ml-1">Basic Information</h5>  
                <fieldset class ="p-3 shadow-lg bg-white mb-2">
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <form:label path="firstName" for="inputFirstName">First Name </form:label>
                            <form:input  path="firstName" cssClass="form-control" oninput="makeEnable()" id="inputFirstName" placeholder="Enter New First Name" maxlength="20" required="true"/>
                            <form:errors path="firstName" cssClass="text-danger font-weight-normal" />
                        </div> 
                    </div>     
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <form:label path="lastName" for="inputLastName">Last Name </form:label>
                            <form:input  path="lastName" cssClass="form-control" oninput="makeEnable()" id="inputLastName" placeholder="Enter New Last Name" maxlength="20" required="true"/>
                            <form:errors path="lastName" cssClass="text-danger font-weight-normal" />
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <form:label path="dob" for="inputdob">Date of Birth </form:label>
                            <form:input  path="dob" id="inputdob"  cssClass="form-control" onclick="makeEnable()" placeholder="Enter New Date Of Birth"/>
                            <form:errors path="dob" cssClass="text-danger font-weight-normal" />
                        </div>
                    </div>
                </fieldset>
                <h5 class="mb-2 mt-4 ml-1">Email</h5>
                <fieldset class ="p-3 shadow-lg bg-white mb-3" >
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <form:label path="email" for="inputEmail">Email </form:label>
                            <form:input  path="email" cssClass="form-control" oninput="makeEnable()" id="inputEmail" placeholder="Enter New Email" maxlength="20"/>
                            <form:errors path="email" cssClass="text-danger font-weight-normal" />
                        </div>
                    </div>
                </fieldset> 
                <div id="buttonsDiv"> 
                    <a href="/deletaccount" >Delete Account</a>
                    <div> 
                        <input type="button" value="Reset" onclick="window.location.href='/userpro'" disabled="true" class="btn btn-light"/>
                        <input type="submit" value="Save Chaneges" disabled="true" class="btn btn-dark"/>
                    </div>
                </div>
            </form:form>
        </div>
        <%@include file="jspfs/bootstrapJS_jqueryJS.jspf"%>
        <script src="/js/bootstrap-datepicker.min.js"></script>
    </body>
</html>

<script type="text/javascript">

    $('#inputdob').datepicker({
        format: 'yyyy-MM-dd'
    });
    
    function makeEnable(){
        
        document.querySelectorAll('#buttonsDiv input').forEach(b => b.disabled=false);
        
    }
   
   setTimeout(function(){ 
      document.getElementById("msg").style.display = "none"; 
    }, 3000); 

</script>