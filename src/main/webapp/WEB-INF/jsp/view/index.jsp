<!DOCTYPE html>
<html>
    <head>
        <title>Customer Support</title>
    </head>
    <body>
        <c:url var="logoutUrl" value="/logout"/>
        <form action="${logoutUrl}" method="post">
            <input type="submit" value="Log out" />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>

        <h2>Index</h2>
        <p>Hello <security:authentication property="principal.username" />!</p>
        <ul>
            <li><a href="<c:url value="/message/list"/>">Lecture</a></li>
            <li><a href="<c:url value="/message/list"/>">Lab</a></li>
            <li><a href="<c:url value="/message/list"/>">Other</a></li>
        </ul>
    </body>
</html>