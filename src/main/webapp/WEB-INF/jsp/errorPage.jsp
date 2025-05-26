<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
         <title>Custom Errors Page</title>
        <style>
             /*body {
                font-family: Arial, sans-serif;
                background-color: lightgray;
                margin: 0;
                padding: 0;
            }
           .container {
                max-width: 600px;
                margin: 20px auto;
                padding: 50px;
                background-color: #fff;
                border-radius: 5px;
                box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
                text-align: center;
            }
            h1 {
                color: red;
            }
            p {
                color: #b70b0b;
                font-size: 20px;
            }
            a {
                color: green;
                text-decoration: underline;
            }*/
            /*a:hover {
                text-decoration: underline;
            }*/
            p {
                color: #b70b0b;
                font-size: 20px;
            }

        </style>
        <link href="/css/bootstrap.min.css" type="text/css" media="all" rel="stylesheet">
    </head>
    <body>
        <div class="container w-100 h-100 p-4 mt-3 mb-5 shadow-lg" style="text-align: center; background-color: #cccccc; border-radius: 10px;">
            <h2 class="mb-3">Page Not Found</h2>
            <p id="errormsg">${msg}</p>
            <img src="/images/notFound.jpg" alt="error" style="width:600px; height: 400px;"/>            
           
            <p class="mt-3">
                Please head to <a href="/loginpage">login</a> or <button style="color: #007bff; padding: 0px; border: none; cursor: pointer; background-color: #cccccc;" onclick="history.back()"> go back</button>
            </p>
            <p ></p>

        </div>
            <%@include file="jspfs/bootstrapJS_jqueryJS.jspf"%>
    </body>
</html>

<script type="text/javascript">
    
    function goBack(url) {
    const previousUrl = document.referrer;
    document.getElementById("pp").innerHTML = previousUrl;
    
}
</script>