<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
<%@include file="includes/header.jsp" %>
<title>Log in</title>
</head>

<body>

<div class="container">

    <form method="POST" action="${contextPath}/forgot-password" class="form-signin">
        <h2 class="form-heading"><img src="${contextPath}/resources/img/logo.png" alt="HandsOnSpring" />Password reset</h2>
        <div class="form-group ${error != null ? 'has-error' : ''}">
            <span>${message}</span>
            <input name="email" type="text" class="form-control" placeholder="Email"
                   autofocus="true"/>
        <spring:hasBindErrors name="forgotPasswordForm">
                <c:forEach var="error" items="${errors.allErrors}">
                <p><span><spring:message message="${error}" /></span></p>
                </c:forEach>
         </spring:hasBindErrors>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <button class="btn btn-lg btn-primary btn-block" type="submit">Reset Password</button>
            <h4 class="text-center"><a href="${contextPath}/log">Log in</a></h4>
        </div>

    </form>

</div>
</body>
</html>
