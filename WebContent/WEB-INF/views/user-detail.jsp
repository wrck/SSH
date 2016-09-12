<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<table>
		<tr>
			<c:if test="${!empty model}"><th>ID</th></c:if>
			<th>用户名</th>
			<th>姓名</th>
			<th>年龄</th>
			<th>分组</th>
			<th>操作</th>
		</tr>
		<tr>
			<form action="${pageContext.request.contextPath}/user/<c:choose><c:when test="${empty model}">add</c:when><c:otherwise>${model.id}?_method=PUT</c:otherwise></c:choose>" method="post">
			<c:if test="${!empty model}"><td><input type="text" name="id" value="${model.id}"
				readonly="true"></td></c:if>
			<td><input type="text" name="name" value="${model.name}"></td>
			<td><input type="text" name="realName" value="${model.realName}"></td>
			<td><input type="text" name="age" value="${model.age}"></td>
			<td><input type="text" name="usersGroup.id"
				value="${model.usersGroup.id}"></td>
			<td><input type="submit" value="完成"></td>
			</form>
		</tr>
	</table>
	<form action="add" method="post">
		<input type="submit" value="提交">
	</form>
	<form action="delete/12?_method=DELETE" method="post">
		<input type="submit" value="删除">
	</form>
</body>
</html>