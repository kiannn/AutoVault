<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml" xmlns:th="https://www.thymeleaf.org">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

        <link href="/css/bootstrap.min.css"  rel="stylesheet">
        <link href="/css/bootstrap-datepicker.standalone.min.css" rel="stylesheet">

        <title>Sign Up Page</title>
        <style>

            .custom-w-div1{
                width: 50%;
            }
            @media(max-width:768px){
                .custom-w-div1{
                    width: 100%;
                }
            }
        </style>
    </head>
    <body class="p-3 mb-2 bg-light text-dark alert-link" style="font-size: 13.25px">
        <div class="container justify-content-center custom-w-div1 p-3 shadow-lg" style="background-color: lightgray; margin-top: 0.1em;">
                <div class="form-group font-weight-normal mb-0">
                    <label style="text-decoration: underline; color: #006dcc"><a href="/loginpage">Back to Login</a></label>
                    <c:if test="${sigUpErrorGeneralMsg!=null}">
                        <div class="alert alert-danger alert-link" style="text-align: center">
                            <span>${sigUpErrorGeneralMsg}</span>
                        </div>
                    </c:if>
                </div>

            <h4 class="mb-3 text-dark" style="text-align: center">Create your account</h4> 
            <form:form action="/signup" method="POST" modelAttribute="signupForm">
                <fieldset class ="p-3 shadow-lg bg-white mb-2" style="border: 1px solid darkgrey; border-radius: 10px; line-height: 10px ">    
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <form:label path="firstName" for="inputFirstName">First Name </form:label>
                            <form:input  path="firstName" cssClass="form-control" id="inputFirstName" placeholder="Enter First Name" maxlength="20" required="true"/>
                            <form:errors path="firstName" cssClass="text-danger font-weight-normal"  style="line-height: 20px;"/>
                        </div>
                        <div class="form-group col-md-6">
                            <form:label path="lastName" for="inputLastName">Last Name </form:label>
                            <form:input  path="lastName" cssClass="form-control" id="inputLastName" placeholder="Enter Last Name" maxlength="20" required="true"/>
                            <form:errors path="lastName" cssClass="text-danger font-weight-normal"  style="line-height: 20px;"/>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6" >
                            <form:label path="dob" for="inputdob">Date of Birth </form:label>
                            <form:input  path="dob" id="inputdob"  cssClass="form-control"  placeholder="Enter Date Of Birth"/>
                            <form:errors path="dob" cssClass="text-danger font-weight-normal"  style="line-height: 20px;"/>
                        </div>
                        <div class="form-group col-md-6" >
                            <form:label path="email" for="inputEmail">Email </form:label>
                            <form:input  path="email" cssClass="form-control" id="inputEmail" placeholder="Enter Email" maxlength="20"/>
                            <form:errors path="email" cssClass="text-danger font-weight-normal"  style="line-height: 20px;"/>
                        </div>
                    </div>
                </fieldset>    
                <fieldset class ="p-3 shadow-lg bg-white" style="border: 1px solid darkgrey; border-radius: 10px; line-height: 10px;">
                    <div class="form-row ">
                        <div class="form-group col-md-6">
                            <form:label path="username" for="inputUsername">Username </form:label>
                            <form:input  path="username" cssClass="form-control" id="inputUsername" placeholder="Enter Username" maxlength="20" required="true"/>
                            <form:errors cssClass="text-danger font-weight-normal" path="username" style="line-height: 20px;"/>
                        </div>
                    </div>
                    <div class="form-row ">
                        <div class="form-group col-md-6">
                            <form:label path="credentials.password" for="inputPassword">Password </form:label>
                            <form:input type="password" path="credentials.password" cssClass="form-control" id="inputPassword" placeholder="Enter Password" maxlength="20" required="true"/>
                            <form:errors path="credentials.password" cssClass="text-danger font-weight-normal"  style="line-height: 20px;"/>
                        </div>
                    </div>
                    <div class="form-row ">
                        <div class="form-group col-md-6">
                            <form:label path="credentials.confirmPassword" for="inputconfirmPassword">Confirm Password </form:label>
                            <form:input type="password" path="credentials.confirmPassword" cssClass="form-control" id="inputconfirmPassword" placeholder="Confirm Password" maxlength="20" required="true"/>
                            <form:errors path="credentials" cssClass="text-danger font-weight-normal"  style="line-height: 20px;"/>

                        </div>
                    </div>
                </fieldset>
                        <button type="submit" style="width: 100%; margin-top: 20px" class="btn btn-primary">Sign Up</button>

            </form:form>
        </div>
        <%@include file="jspfs/bootstrapJS_jqueryJS.jspf"%>
        <script src="/js/bootstrap-datepicker.min.js"></script>
    </body>
</html>

<script type="text/javascript">

    $('#inputdob').datepicker({ 
        format: 'yyyy-M-dd'  
        
        /* 
         * 'inputdob', the id of the form:input tag, is used for datepicker 
            while 'path' is also present in it.
         */

    });
</script>