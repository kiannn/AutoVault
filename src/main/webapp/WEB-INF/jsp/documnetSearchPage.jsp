<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
    <head>
        <title>title</title>
        <link href="/css/bootstrap.min.css"  rel="stylesheet">
    </head>
    <body>
        <%@include file="jspfs/navigationBar.jspf"%>
        <div class="container justify-content-center p-3 mb-5 mt-4 bg-light text-dark alert-link">
            <p style="font-weight: bolder; font-size: 20px; color: darkgray; text-align: center; margin-bottom: 50px">
                Search for Documents
            </p>
            <form:form method="POST" modelAttribute="docSearchInput">
                <fieldset style="display: flex; margin-left: 250px; line-height: 10px;">
                    <div class="form-row">
                        <div class="form-group col-md-7">
                            <form:label path="name" for="docName">Name</form:label>
                            <form:input  path="name" cssClass="form-control" id="docName" placeholder="Enter document name" maxlength="50"/>   
                            <form:checkbox path="caseSensitive" id="ch" value="case-sensitive" cssClass = "mt-2 mr-1"/>
                            <form:label path="name" for = "ch" style= "font-size: 14px; font-weight: normal;">case sensitive</form:label>
                            <!-- 
                            The label tag with for = "ch" ensures that the label is associated with the checkbox. 
                            Clicking the label itself will toggle the checkbox.
                            --> 
                        </div>  
                        <div class="form-group col-md-4">
                            <form:label path="extension">Extension</form:label>
                            <form:select path="extension" cssClass="form-control">
                                <form:option value="" label="Please Select"  hidden="true"/> <!-- value=""  will be converted to a null for properties of Enum types within the objects of controller method's arguments, at the time of binding from form to controller -->
                                <form:option value="${any}" label="${any.ex}" cssClass="alert-link text-danger "/>  
                                <form:options items ="${allExten}"  itemLabel="ex" cssClass="alert-link"/>
                            </form:select>
                        </div> 
                        <div class="form-group col-sm-1 mt-3">
                            <input type="submit" value="Search" formaction="/document/searchdocs" class="btn btn-dark text-black ml-3" style="border: 2px solid black;"/>
                            <!-- 
                                Why formaction="/document/searchdocs" ? 
                                The documnetSearchPage page has a different URL than "home/{by}" or "showsearchresult/{by}" 
                                which are the endpoint URLs of home and search pages.
                                In order to perform sorting within document search page a post request should be made at /document/searchdocs.
                            -->
                        </div>
                    </div>                    
                </fieldset>              
            </form:form>     
            <c:if test="${showTable.isEmpty()}"> 
                <p class="mb-4 mt-4 ml-4 font-weight-bold text-black" style="font-size: 15px;">
                    <c:if test="${docSearchInput.name.trim().isEmpty()}">Search results for all documents with extension of '${docSearchInput.extension}' </c:if> 
                    <c:if test = "${!docSearchInput.name.trim().isEmpty()}">Search results for document name: ${docSearchInput.name.concat('.'.concat(docSearchInput.extension))}</c:if> 
                </p>
                <%@include file="jspfs/table.jspf"%> 
            </c:if>

        </div>
        <%@include file="jspfs/bootstrapJS_jqueryJS.jspf"%>
    </body>
</html>

