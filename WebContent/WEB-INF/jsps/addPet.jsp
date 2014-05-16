<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<% 		String base = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<script src="jsfiles/jquery-1.4.4.min.js"></script>
<script>
$(function(){
	$("#categoryId").bind("change",function(event){
		if($(this).val() !=0){
			$("#subSelect").children().remove();
			$.ajax({
				type:'POST',
				url:'getSubCategory?categoryId='+$(this).val(),
				sync:true,
				success:function(datas){
					$(datas).appendTo("#subSelect");
				}
			})
			$("#subCategory").show();
		}else{
			$("#subCategory").hide();
			$("#subSelect").children().remove();
		}

	})
})
</script>

<h1 align="center">添加宠物</h1><hr/>
<form:form method="POST" modelAttribute="pet">
<table>
	<tr>
		<td>宠物编号</td>
		<td><form:input path="petNo"></form:input></td>
	</tr>
	<tr>
		<td>宠物名称</td>
		<td><form:input path="petName"></form:input></td>
	</tr>
	<tr>
		<td>宠物类别</td>
		<td>
		<form:select path="category.category.categoryId" id="categoryId">
			<form:option value="0" label="--请选择"></form:option>
			<form:options items="${categoryList}" itemValue="categoryId" itemLabel="categoryName"/>
		</form:select>
		<span id="subCategory" style="display:none">子类：<select id="subSelect" name="category.id"></select></span></td>
	</tr>
	<tr>
		<td>出生日期</td>
		<td><form:input path="birthDay"></form:input></td>
	</tr>
	<tr>
		<td>毛色</td>
		<td><form:input path="hairColor"></form:input></td>
	</tr>
	<tr>
		<td>描述</td>
		<td><form:textarea path="descriptions" rows="10" cols="50"/></td>
	</tr>
</table>
<input type="submit" value="提交">
</form:form>
</body>
</html>