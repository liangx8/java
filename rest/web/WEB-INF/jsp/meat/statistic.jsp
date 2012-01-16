<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>餐费统计</title>
</head>
<body>
<table bgcolor="#ffeaea" width="100%">
<tr bgcolor="#eeffee"><th>员工</th><th>餐次<th>餐票日期</th></tr>
<c:forEach items="${report }" var="it" varStatus="itStatus">
<tr <c:if test="${itStatus.index % 2 == 0 }">bgcolor="#eeeeff"</c:if>>
	<td>${it.empName }</td>
	<td>${it.count }</td>
	<td>
	<c:forEach items="${it.history }" var="inner">
		<fmt:formatDate value="${inner.ticketDate }" pattern="yyyy-MM-dd"/>:${inner.ampm };
	</c:forEach>
	</td>
</tr>	
</c:forEach>
</table>
<table>
<tr>
	<td width="80"><a href="<spring:url value="/meat_statistic.html" />">返回</a></td>
	<td>
		<form action="<spring:url value="/meat/report.do" />" method="POST">
		<input type="hidden" name="from" value="${from}"/>
		<input type="hidden" name="to" value="${to }"/>
		<input type="submit" value="生产XLS文件"/>
		</form>
	</td>
</tr></table>
</body>
</html>