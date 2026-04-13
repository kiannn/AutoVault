<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
    <head>
        <link href="/css/bootstrap.min.css" type="text/css" media="all" rel="stylesheet">
        <title>Logout-verify</title>
    </head>
    <body>   
        <div class="container" style="text-align:  center">
            <h3 style="margin-top: 100px; margin-bottom: 20px">Are you sure you want to logout ?</h3>
            <form:form action="/logout" method="post" >   
                <!--<input type="text" name="_csrf" value="${_csrf.token}"/> -->
                <input type="submit"  value="log-out" class="btn btn-button bg-dark text-danger alert-link"/>  
                
                <input type="button" onclick="history.back()" value="cancel"  class="btn btn-button bg-secondary text-white alert-link"/>                
            </form:form>
        </div>
    </body>
</html>

