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
            <li><a href="<c:url value="/guestbook"/>">Lecture</a></li>
            <li><a href="<c:url value="/guestbook"/>">Lab</a></li>
            <li><a href="<c:url value="/guestbook"/>">Other</a></li>
        </ul>
    </body>
</html>