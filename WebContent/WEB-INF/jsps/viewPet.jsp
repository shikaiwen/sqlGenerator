<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1 align="center">查看宠物</h1><hr/>
<form:form method="POST" modelAttribute="pet">
<table>
	<tr>
		<td>宠物编号</td>
		<td><span>${pet.petNo}</span></td>
	</tr>
	<tr>
		<td>宠物名称</td>
		<td><span>${pet.petName}</span></td>
	</tr>
	<tr>
		<td>宠物类别</td>
		<td>${pet.category.category.categoryName} : ${pet.category.subCategoryName}</td>
	</tr>
	<tr>
		<td>出生日期</td>
		<td>${pet.birthDay}</td>
	</tr>
	<tr>
	<td>毛色</td>
		<td>${pet.hairColor}</td>
	</tr>
	<tr>
		<td>描述</td>
		<td>${pet.descriptions}</td>
	</tr>
</table>
</form:form>
<a href="toMainPage">返回</a>
</body>
</html>