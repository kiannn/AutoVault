<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
         <title>Custom Errors Page</title>
        <style>
            p {
                color: #b70b0b;
                font-size: 20px;
            }

        </style>
        <link href="/css/bootstrap.min.css" type="text/css" media="all" rel="stylesheet">
    </head>
    <body>
        <div class="container w-100 h-75 p-4 mt-3 mb-5 shadow-lg" style="text-align: center; background-color: #cccccc; border-radius: 10px;">
            <img src="/images/unavalablePage.jpg" alt="error" style="width:380px; height: 170px;"/>
            <p id="errormsg" class="mt-3">${msg}</p>           
            <p class="mt-3">
                Please head to <a href="/loginpage">login</a> or <button style="color: #007bff; padding: 0px; border: none; cursor: pointer; background-color: #cccccc;" onclick="history.back()"> go back</button>
            </p>
            <p ></p>

        </div>
            <%@include file="jspfs/bootstrapJS_jqueryJS.jspf"%>
    </body>
</html>
