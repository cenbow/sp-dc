<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Error Page</title>
</head>
<body>
<h1>Error Page</h1>
    <p>Application has encountered an error. Please contact support on ...</p>
    
    Failed URL: ${url}<br>
    Timestamp:  ${timestamp}<br>
    Exception:  ${exception.message}<br>
	<c:forEach items="${exception.stackTrace}" var="ste">    
        ${ste}<br> 
    </c:forEach>
</body>
</html>