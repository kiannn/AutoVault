<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
    <head>
        <title>document search</title>
        <link href="/css/bootstrap.min.css"  rel="stylesheet">
        <link href="/css/listingTable.css"  rel="stylesheet">
        <link rel="stylesheet" href="/css/documnetSearch.css"/>
    </head>
    <body>
        <%@include file="jspfs/navigationBar.jspf"%>
        <div class="container p-3 mb-5 mt-4 bg-light text-dark alert-link">
            <p class="header">Search for Documents </p>
            <form:form method="POST" modelAttribute="docSearchInput" action="searchdocs" > <!-- 'action="searchdocs"' could be omitted in this case, as the POST request must be made at the same URL as the one that loads 'documnetSearchPage.jsp' (the current page). -->
                <fieldset>
                    <div class="name-div">
                        <form:label cssClass="name-label" path="name" for="docName">Name</form:label>
                        <form:input  path="name" id="docName" cssClass="form-control" placeholder="Enter document name" maxlength="50"/>  
                        <div>
                            <form:checkbox path="caseSensitive" id="ch" value="case-sensitive"/>
                            <form:label path="caseSensitive" for = "ch">case sensitive</form:label>
                                <!-- 
                                The label tag with for = "ch" ensures that the label is associated with the checkbox. 
                                Clicking the label itself will toggle the checkbox.
                                --> 
                        </div>  
                    </div>
                    <div>
                        <form:label cssClass="extension-label" path="extension">Extension</form:label>
                        <form:select path="extension" cssClass="form-control">
                            <form:option value="" label="Please Select"  hidden="true"/> <!-- value=""  will be converted to a null for properties of Enum types within the objects of controller method's arguments, at the time of binding from form to controller -->
                            <form:option value="${any}" label="${any.ex}" cssClass="alert-link text-danger"/>  
                            <form:options items ="${allExten}"  itemLabel="ex" cssClass="alert-link"/>
                        </form:select>
                    </div> 
                    <div class="searchButton-div">
                        <input type="submit" value="Search" class="btn btn-dark"/>
                    </div>                  
                </fieldset>              
            </form:form>     
            <c:if test="${showTable.isEmpty()}"> 
                <p class="searchResult-message">
                    <c:if test="${docSearchInput.name.trim().isEmpty()}">Search results for all documents with extension of '${docSearchInput.extension}' </c:if> 
                    <c:if test = "${!docSearchInput.name.trim().isEmpty()}">Search results for document name: ${docSearchInput.name.concat('.'.concat(docSearchInput.extension))}</c:if> 
                </p>
                <%@include file="jspfs/table.jspf"%> 
            </c:if>

        </div>
        <%@include file="jspfs/bootstrapJS_jqueryJS.jspf" %>
        <script src="/js/tableFunctionalities.js"></script>
    </body>
</html>

