<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>员工数据</title>
</head>
<body>
<table bgcolor="#ffeaea" width="100%">
<tr><th>工号</th><th>姓名</th><th>身份证</th><th>身份证</th><th>性别</th></tr>
<c:forEach items="${page.data }" var="it" varStatus="itStatus">
<tr <c:if test="${itStatus.index % 2 == 0 }">bgcolor="#eeeeff"</c:if>>
	<td>${it.empCode }</td>
	<td>${it.empName }</td>
	<td>${it.personId }</td>
	<td>${it.strDepartment }</td>
	<td>${it.strGender }</td>
</tr>
</c:forEach>
</table>
<a href="page.do?start=0">首页</a>|<a href="page.do?start=${page.prev }">上页</a>|<a href="page.do?start=${page.next }">下页</a>|<a href="page.do?start=${page.last }">最后</a>|<a href="<spring:url value="/" />">返回</a>
</body>
</html>