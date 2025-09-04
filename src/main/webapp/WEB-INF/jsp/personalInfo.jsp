<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
    <head>
        <link href="/css/bootstrap.min.css"  rel="stylesheet">
        <link href="/css/bootstrap-datepicker.standalone.min.css" rel="stylesheet">
        <title>title</title>
    </head>
    <body>
        <%@include file="jspfs/navigationBar.jspf"%>
        <div class="container w-50 bg-light text-dark alert-link" style="font-size: 13.25px; margin-bottom: 100px;">
            <c:if test="${personalInfoUpdateMSG!=null}">
                <div class="justify-content-center" style="display: flex; font-size: 15px">
                    <c:if  test="${personalInfoUpdateMSG.contains('successfully')}">
                        <span class="alert alert-success position-absolute" id="msg0">${personalInfoUpdateMSG}</span>
                    </c:if>    
                    <c:if  test="${personalInfoUpdateMSG.contains('Unsuccessful')}">
                        <span class="alert alert-danger position-absolute" id="msg1">${personalInfoUpdateMSG}</span>
                    </c:if>
                </div>
            </c:if>
            <form:form modelAttribute="personalInfo">
                <h5 class="mb-3 mt-5" style="text-align: left">Basic Information</h5>  
                <fieldset class ="p-3 shadow-lg bg-white mb-2" style="border: 1px solid darkgrey; border-radius: 10px; line-height: 10px ">
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <form:label path="firstName" for="inputFirstName">First Name </form:label>
                            <form:input  path="firstName" cssClass="form-control" oninput="makeEnable()" id="inputFirstName" placeholder="Enter New First Name" maxlength="20" required="true"/>
                            <form:errors path="firstName" cssClass="text-danger font-weight-normal"  style="line-height: 20px;"/>
                        </div> 
                    </div>     
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <form:label path="lastName" for="inputLastName">Last Name </form:label>
                            <form:input  path="lastName" cssClass="form-control" oninput="makeEnable()" id="inputLastName" placeholder="Enter New Last Name" maxlength="20" required="true"/>
                            <form:errors path="lastName" cssClass="text-danger font-weight-normal"  style="line-height: 20px;"/>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <form:label path="dob" for="inputdob">Date of Birth </form:label>
                            <form:input  path="dob" id="inputdob"  cssClass="form-control" onclick="makeEnable()" placeholder="Enter New Date Of Birth"/>
                            <form:errors path="dob" cssClass="text-danger font-weight-normal"  style="line-height: 20px;"/>
                        </div>
                    </div>
                </fieldset>
                <h5 class="mb-3 mt-4" style="text-align: left">Email</h5>
                <fieldset class ="p-3 shadow-lg bg-white mb-3" style="border: 1px solid darkgrey; border-radius: 10px; line-height: 10px ">
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <form:label path="email" for="inputEmail">Email </form:label>
                            <form:input  path="email" cssClass="form-control" oninput="makeEnable()" id="inputEmail" placeholder="Enter New Email" maxlength="20"/>
                            <form:errors path="email" cssClass="text-danger font-weight-normal"  style="line-height: 20px;"/>
                        </div>
                    </div>
                </fieldset> 
                <div style="float: left; margin-top: 25px;"> 
                    <a href="/deletaccount" >Delete Account</a>
                </div>
                <div style="text-align: right; margin-top: 20px;"> 
                    <input type="button" value="Cancel" onclick="window.location.href='/userpro'" disabled="true" id="buttonB" class="btn btn-light btn-lg mr-3" style="border:2px solid blue; color: darkblue"/>
                    <input type="submit" value="Save Chaneges" disabled="true" id="submitB" class="btn btn-dark btn-lg text-black" style="border: 2px solid black;"/>
                </div>
            </form:form>
        </div>
        <%@include file="jspfs/bootstrapJS_jqueryJS.jspf"%>
        <script src="/js/bootstrap-datepicker.min.js"></script>
    </body>
</html>

<script type="text/javascript">

    $('#inputdob').datepicker({
        format: 'yyyy-M-dd'

    });
    
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