<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>餐票登记</title>
</head>
<body>

<form action="<spring:url value="/meat/input.do" />" method="POST">
<table bgcolor="#ffeaea" width="100%">
<tr bgcolor="#eeffee"><th /><th>餐票号</th><th>员工</th><th>餐票日期</th><th /><th>状态</th></tr>
<c:forEach items="${list }" var="it" varStatus="itStatus">
<tr <c:if test="${itStatus.index % 2 == 0 }">bgcolor="#eeeeff"</c:if>>
<td><c:if test="${it.used == 0 }"><input type="checkbox" name="ticket_id" value="${it.id }"/></c:if></td><td>${it.id }</td><td>${it.empName }</td><td><fmt:formatDate value="${it.ticketDate }" pattern="yyyy-MM-dd"/></td><td>${it.ampmName }</td><td>${it.usedName }</td>
</tr>
</c:forEach>
</table>
<a href="<spring:url value="/check_ticket.html" />">返回</a> | <input type="submit" value="保存" />
</form>
</body>
</html>
