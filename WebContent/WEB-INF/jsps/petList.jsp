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

<h1 align="center">宠物列表</h1><hr/>
<table  width="60%" height="80%" align="center">
	<tr>
		<td>宠物编号</td>
		<td>宠物名称</td>
		<td>宠物类别</td>
		<td>出生日期</td>
		<td>毛色</td>
		<td>描述</td>
		<td>&nbsp;&nbsp;</td>
	</tr>
	
<c:forEach items="${petList}" var="pet">
<tr>
		<td><span><c:out value="${pet.petNo}"/></span></td>
		<td><span><c:out value="${pet.petName}"/></span></td>
		<td><c:out value="${pet.category.category.categoryName}"/>: <c:out value="${pet.category.subCategoryName}"/></td>
		<td><c:out value="${pet.birthDay}"/></td>
		<td><c:out value="${pet.hairColor}"/></td>
		<td><c:out value="${pet.descriptions}"/></td>
		<td><a href="modifyPet?petNo=<c:out value="${pet.petNo}"/>">修改</a></td>
		<td><a href="deletePet?petNo=<c:out value="${pet.petNo}"/>">删除</a></td>
</tr>
	</c:forEach>
</table>

<a href="toMainPage">返回</a>
</body>
</html>