<!DOCTYPE html>
<html>
    <head>
        <title>Discussion Forum</title>
    </head>
    <body>
        <c:url var="logoutUrl" value="/logout"/>
        <form action="${logoutUrl}" method="post">
            <input type="submit" value="Log out" />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>

        <h2>Course Discussion Forum Index</h2>
        
        <ul>
            <li><a href="<c:url value="/message"/>">Lecture</a></li>
            <li><a href="<c:url value="/message"/>">Lab</a></li>
            <li><a href="<c:url value="/message"/>">Other</a></li>
            <li><a href="<c:url value="/login"/>">Login</a></li>
        </ul>
    </body>
</html>