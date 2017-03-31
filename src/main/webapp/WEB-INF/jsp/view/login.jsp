<!DOCTYPE html>
<html>
    <head>
        <title>Login Page</title>
    </head>
    <body>
        <h1>User Login</h1>
        <c:if test="${loginFailed}">
            <p>The username and password you entered are not correct. Please try again.</p>
        </c:if>
        <form:form method="post" modelAttribute="loginForm" >
            <form:label path="username">Username:</form:label> <br />
            <form:input type="text" path="username" /><br /><br />
            <form:label path="password">Password:</form:label><br />
            <form:input type="password" path="password" /><br /><br />
            <input type="submit" value="Log In"/>
        </form:form>
    </body>
</html>
