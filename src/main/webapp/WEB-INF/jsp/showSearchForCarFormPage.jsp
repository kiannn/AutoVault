<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
    <head> 
        <link href="/css/bootstrap.min.css"  rel="stylesheet">
        <link href="/css/bootstrap-datepicker.standalone.min.css" rel="stylesheet">

        <title>title</title>
    </head>
    <body>
        <%@include file="jspfs/navigationBar.jspf"%>
        <div class="container justify-content-center p-3 mb-5 mt-4 bg-light text-dark alert-link">
          <p style="font-weight: bolder; font-size: 20px; color: darkgray; text-align: center;">
            ${message}
          </p>
          <p style="color: darkcyan; font-size: 16px; text-align: center">Enter car's specification to search for:</p>
          <%@include file="jspfs/formSearch.jspf"%>       
        </div>        
        <%@include file="jspfs/bootstrapJS_jqueryJS.jspf"%>
       <script src="/js/bootstrap-datepicker.min.js"></script>
    </body>
</html>


<script type="text/javascript">

    $('#datePurchased').datepicker({
        format: 'yyyy-M-dd'

    });
    $('#datePurchasedTo').datepicker({
        format: 'yyyy-M-dd'

    });
</script> 