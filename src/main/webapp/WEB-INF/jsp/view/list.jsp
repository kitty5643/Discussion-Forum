
<!DOCTYPE html>
<html>
    <head>
        <title>Course Discussion Forum</title>
    </head>
    <body>
        <security:authorize access="!hasAnyRole('USER','ADMIN')">
            <a href="<c:url value="/user/create" />">Register</a>
            <c:url var="logoutUrl" value="/login"/>
            <form action="${logoutUrl}" method="post">
                <input type="submit" value="Log in" />
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            </form>

        </security:authorize>
       <security:authorize access="hasAnyRole('USER','ADMIN')">
            <c:url var="logoutUrl" value="/logout"/>
            <form action="${logoutUrl}" method="post">
                <input type="submit" value="Log out" />
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
        </security:authorize>

        <h2>Lecture</h2>
        <security:authorize access="hasRole('ADMIN')">
            <a href="<c:url value="/user" />">Manage User Accounts</a><br /><br />
        </security:authorize>

        <a href="<c:url value="/message/create" />">Create a Topic</a><br /><br />
        <c:choose>
            <c:when test="${fn:length(ticketDatabase) == 0}">
                <i>There are no topics in the system.</i>
            </c:when>
            <c:otherwise>
                <c:forEach items="${ticketDatabase}" var="entry">
                    Topic ${entry.key}:
                    <a href="<c:url value="/message/view/${entry.key}" />">
                        <c:out value="${entry.value.subject}" /></a>
                    (User: <c:out value="${entry.value.customerName}" />)
                    <security:authorize access="hasRole('ADMIN') "> 
                        <security:authorize access="principal.username=='${entry.value.customerName}'">
                        [<a href="<c:url value="/message/edit/${entry.key}" />">Edit</a>]
                        </security:authorize>
                    </security:authorize>
                    <security:authorize access="hasRole('ADMIN')">            
                        [<a href="<c:url value="/message/delete/${entry.key}" />">Delete</a>]
                       
                    </security:authorize>
                    <br />  
                </c:forEach>
                         
            </c:otherwise>
        </c:choose>
                    
             <br /><br />       
         <a href="<c:url value="/message" />">Return to index</a>
    </body>
</html>