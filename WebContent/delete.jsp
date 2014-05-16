<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="jsfiles/jquery-1.4.4.min.js"></script>
<script src="jsfiles/commonJsMethod.js"></script>

</head>
<body>
<script type="text/javascript">
	var StringUtils = new StringUtils();
	function getTableInfo(callback,clickedButton){
		var currSqlBlock = $(clickedButton).parent();
		var tableName = currSqlBlock.find("#tableName").val();
		$.ajax({
			type:'GET',
			data:{"tableName":tableName},
			url:'sqlGen/getTableInfo',
			success:function(datas){
				var jsonObj = $.parseJSON(datas);
				callback(jsonObj,clickedButton);
			}
		})
	}
	
	//初始化数据
	function initData(jsonObj,clickedButton){
		var currSqlBlock = $(clickedButton).parent();
	//	var tableName = currSqlBlock.find("tableName").val(); 
		var ul = currSqlBlock.find("#tableInfoDiv").find("ul");
		var templateLi = currSqlBlock.find("#templateLi");
		for(var v = 0; v<jsonObj.length;v++){
			var currentObj = jsonObj[v];
			var str = currentObj['columnName'] +" -" +currentObj['typeName'] +currentObj['remarks'];
			
			var checkBoxStr = $(templateLi).html();
			var li = templateLi.clone();
			$(checkBoxStr).show().attr("checked",true).attr("name",currentObj['columnName']).appendTo(li);
			$("<span></span>").html(str).appendTo(li);
			li.appendTo(ul);
		}
		currSqlBlock.find("#tableInfoDiv").show();
		//$("#tableDiv").show();
	}
	
	
	$(function(){
		//全选按钮
		$("#sqlBlock1").find("#checkAll").click(function(){
			$(this).parent().parent().children(":gt(1)").find("input").attr("checked",$(this).attr("checked"));
		});
		
		//获取sql按钮
		$("#getSqlButton").click(function(){
			handleXml();

		})
	})
	

//全选按钮	
function checkAll(obj){
	$(this).parent().parent().children(":gt(1)").find("input").attr("checked",$(this).attr("checked"));
}



function getMaxIdObject(arr){
	var max = 0;
	for(var j = 1; j<arr.length; j ++){
		if($(arr[j]).attr("id") > $(arr[max]).attr("id")){
			max = j;
		}
	}
	return arr[max];
}




function generateSqlWithJs(){
	
	var sqlBlock = $("#sqlBlock1");
	var sql = "delete from ";
	sql += sqlBlock.find("#tableName").val() +" where ";
	sqlBlock.find("li:gt(1)").find(":checked").each(function(){
		sql += $(this).attr("name") + "= ? and ";
	});
	sql = sql.substring(0,sql.lastIndexOf("and"));
	
			
	sql = sql.substring(0,sql.length -1);
	//显示出来
	sql = 'StringBuffer sql = new StringBuffer("' + sql + '");';
	$("#showSqlArea").val(sql);
}

//复位
function restart(){
	$("div[name='sqlBlock']").each(function(){
		$(this).find("li:gt(1)").remove();
		$(this).find("input[type='text']").val('');
		$(this).find("#tableInfoDiv").hide();
	});
	$("#showSqlArea").val('');
}

function showColumns(event){
	if(event.keyCode == 13){
		getTableInfoButton.click();
	}
}

$(function(){
	tableName.focus();
})
</script>
<body onkeydown="showColumns(event)">
<br>
<input type="button" id="clearAll" onclick="restart()" value="复位键"/>
<input type="button" id="getSqlButton" onclick="generateSqlWithJs()" value="获取sql"/>
<input type="button" value="testClick" id="testClick" onclick="testClick()"/>

<br><br>

	<div id="sqlBlock1" name="sqlBlock" style="float:left;width:350px;">
		<input type="button" onclick="getTableInfo(initData,this)" id="getTableInfoButton" value="获取表信息">
		<input type="text" id="tableName" id="tableName" size="10"/>
		<div id="tableInfoDiv" style="border:1px solid black;width:300px;display:none">
			<ul style="list-style-type:none">
				<li id="templateLi"><input type="checkbox" style="display:none"/></li>
				<li>全选<input type="checkbox" checked id="checkAll"></li>
			</ul>
		</div>
	</div>

<div style="width:800px;float:right;margin-left:auto;margin-right:auto;margin-top:3%">
	<textarea rows="10" cols="100" id="showSqlArea"></textarea>
</div>





</body>
</html>