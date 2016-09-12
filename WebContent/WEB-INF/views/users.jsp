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
	<h1>Count : ${count}</h1>
	<h1>Message : ${message}</h1>
	<table>
		<tr>
			<form action="">
				<label>用户名</label><input type="text" name="name" value="${param.name}" placeholder="请输入用户名，加%可进行模糊查询">
				<label>姓名</label><input type="text" name="realName" value="${param.realName}" placeholder="请输入姓名，加%可进行模糊查询">
				<label>年龄</label><input type="text" name="age" value="${param.age}" placeholder="请输入年龄，加%可进行模糊查询">
				<input type="submit" value="搜索">
			</form>
		</tr>
		<tr>
			<th>ID</th>
			<th>用户名</th>
			<th>姓名</th>
			<th>年龄</th>
			<th>操作</th>
		</tr>
		<c:forEach items="${model}" var="user">
			<tr>
				<td>${user.id}</td>
				<td>${user.name}</td>
				<td>${user.realName}</td>
				<td>${user.age}</td>
				<td><a href="user/${user.id}">查看</a></td>
				<td>
					<form action="user/${user.id}?_method=DELETE" method="post">
						<input type="submit" value="删除">
					</form>
				</td>
			</tr>
		</c:forEach>
	</table>
	<form action="user/add" method="get">
		<input type="submit" value="增加">
	</form>
</body>
</html>