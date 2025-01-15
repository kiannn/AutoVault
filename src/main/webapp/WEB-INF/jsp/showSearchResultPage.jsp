<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
    <head>
        <link href="/css/bootstrap.min.css"  rel="stylesheet">
        <title>title</title>
    </head>
    <body>
        
    <%@include file="jspfs/navigationBar.jspf"%>
    <div class="container justify-content-center p-3 mb-5 bg-light text-dark alert-link">
            
        <p style="color:blue; font-size: 20px; font-weight: bold; text-align: left;">
            Results found for :<br><br>
            <table class="serachInputTable">
                <tr>
                    <td style="width: 20%;"> Make = ${searchUserInput.make}</td>
                    <td style="width: 20%;">Model(s)=  
                        ${searchUserInput.modelList.isEmpty() ? 'unknown' : searchUserInput.modelList}</td>
                    <td colspan="2" style="width: 30%;">
                        Year From = ${searchUserInput.year==null ? 'unknown' : searchUserInput.year}&emsp;
                        Year To = ${searchUserInput.yearTo==null ? 'unknown' : searchUserInput.yearTo}</td>
                    
                </tr>
                <tr>
                    <td colspan="2">
                        Purchase Date From = ${searchUserInput.datePurchased==null ? 'unknown' : searchUserInput.datePurchased}&emsp;
                        Purchased Date To = ${searchUserInput.datePurchasedTo==null ? 'unknown' : searchUserInput.datePurchasedTo}</td>
                    <td colspan="2">
                        Price From = ${searchUserInput.price==null ? 'unknown' : searchUserInput.price}&emsp;
                        Price To = ${searchUserInput.priceTo==null ? 'unknown' : searchUserInput.priceTo}</td>
                </tr>
            </table>
        </p><br>

        <%@include file="jspfs/table.jspf"%>
        <br>
        <p style="font-size:18px; font-style: oblique; background-color: yellow; margin-bottom: 12px;">
            Number of records found : ${showAll.size()==null ? 0 : showAll.size()}
        </p><br><br>

        <c:if test="${!resultMsg.isEmpty()}">
            <c:forEach items="${resultMsg.split('-')}" var="m">
                <span style="font-size:14px;"><span style="color: red">** </span><c:out value="${m}"/></span><br>
            </c:forEach>   
        </c:if>
    </div>
    <%@include file="jspfs/bootstrapJS_jqueryJS.jspf"%>
</body>
</html>

<script type="text/javascript">

    $('#datePurchased').datepicker({
        format: 'yyyy*M-dd'

    });
    $('#datePurchasedTo').datepicker({
        format: 'yyyy*M-dd'

    });
    
</script> 