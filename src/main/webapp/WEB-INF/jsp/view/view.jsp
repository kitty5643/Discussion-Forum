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
            

        </security:authorize>
        <security:authorize access="hasAnyRole('USER','ADMIN')">
            <c:url var="logoutUrl" value="/logout"/>
            <form action="${logoutUrl}" method="post">
                <input type="submit" value="Log out" />
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
        </security:authorize>

        <h2><c:out value="${category}" /> Topic #${messageId}: <c:out value="${ticket.subject}" /></h2>
        <security:authorize access="hasRole('ADMIN') "> 
            <security:authorize access="principal.username=='${ticket.customerName}'">       
                [<a href="<c:url value="/message/edit/${messageId}" />">Edit</a>]
            </security:authorize>
        </security:authorize>
        <security:authorize access="hasRole('ADMIN')">            
            [<a href="<c:url value="/message/delete/${messageId}" />">Delete</a>]
        </security:authorize>
        <br /><br />
        <i>User Name - <c:out value="${ticket.customerName}" /></i><br /><br />
        <c:out value="${ticket.body}" /><br /><br />
        <security:authorize access="hasAnyRole('USER','ADMIN')">   
            <c:if test="${ticket.numberOfAttachments > 0}">

                Attachments:
                <c:forEach items="${ticket.attachments}" var="attachment"
                           varStatus="status">
                    <c:if test="${!status.first}">, </c:if>
                    <a href="<c:url value="/message/${messageId}/attachment/${attachment.name}" />">
                        <c:out value="${attachment.name}" /></a>
                </c:forEach><br /><br />
            </c:if>
        </security:authorize>
        <a href="<c:url value="/message/${category}/list" />">Return to list topics</a>
    </body>
</html>