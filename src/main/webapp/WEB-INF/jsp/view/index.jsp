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
        <h2>Index</h2>
        <p>Hello 
            <security:authorize access="hasAnyRole('USER','ADMIN')">
                <security:authentication property="principal.username" />
            </security:authorize>!</p>
        <ul>
            <li><a href="<c:url value="/message/list"/>">Lecture</a></li>
            <li><a href="<c:url value="/message/list"/>">Lab</a></li>
            <li><a href="<c:url value="/message/list"/>">Other</a></li>
        </ul>
    </body>
</html>