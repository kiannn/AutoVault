<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">


        <title>Login-Page</title>

        <link href="/css/bootstrap.min.css" type="text/css" media="all" rel="stylesheet">
        <link href="/css/bgStyles.css" type="text/css" media="all" rel="stylesheet">
        <style>
            .custom-w-div1{
                width: 50%;
            }
            @media(max-width:768px){
                .custom-w-div1{
                    width: 100%;
                }
            }
            .custom-w-div2{
                width: 75%;
            }
            @media(max-width:768px){
                .custom-w-div2{
                    width: 100%;
                }
            }
            .content{
                height: 90%;
                position: absolute;
                transform: translate(55%, -140%);
            }

        </style>
    </head>
    <body class="text-white">
        <div class="background"></div>
        <div class="container custom-w-div1 p-3 shadow-lg content bg-transparent">
            <div class="container custom-w-div2 justify-content-center h-75 mt-3 shadow-lg bg-transparent" style="line-height: 10px;">
                <h3 class="display-5 mb-3">Login</h3>

                <c:if test= "${param.error.isEmpty()}">
                    <div id="invalid" class="text-center alert alert-danger" style="margin-top: 15px;">
                        Invalid username or password
                    </div>
                </c:if>
                <c:if test= "${param.logout.isEmpty()}">
                    <div id="loggedout" class="text-center alert alert-warning" "${param.logout}">
                        You have been logged out
                    </div>
                </c:if>
                <c:if test= "${param.sigUpSuccess.isEmpty()}">
                    <div id="success" class="text-center alert alert-success"style="line-height: 20px;">
                        You successfully signed up! Please continue to login.
                    </div>
                </c:if>
                <c:if test= "${param.deleteAccountMSG.isEmpty()}">
                    <div id = "deleted" class="text-center alert alert-warning">
                        You Account Has Been Deleted ! 
                    </div>
                </c:if>
                <c:if test= "${param.sessionTimedOut.isEmpty()}">
                    <div class="text-center alert alert-danger">
                        Request Timeout ! 
                    </div>
                </c:if>
                
                <form:form action="/loginprocess" method="POST">

                    <div class="form-group" style="margin-top: 40px">
                        <label for="inputUsername">Username</label>
                        <input type="input" class="form-control" name="username" id="inputUsername" placeholder="Enter Username" maxlength="20" required>
                    </div>  
                    <div class="form-group mt-4">
                        <label for="inputPassword">Password</label>
                        <input type="password" class="form-control" name="password" id="inputPassword" placeholder="Enter Password" maxlength="20" required>
                    </div> 
                    <!--  <input type="text" name="_csrf" value="${_csrf.token}" class="d-none"/> -->
                    <button type="submit" class="btn btn-primary" id="login-button">Login</button>
                </form:form>

                <div class="form-group" style="margin-top: 2em;">
                    <label>Click <a href="/signup">here</a> to sign up</label>
                </div>

            </div>
        </div>
        <%@include file="jspfs/bootstrapJS_jqueryJS.jspf"%>
    </body>
</html>

