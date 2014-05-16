<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@include file="include/needed/commonResource.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="jsfiles/commonJsMethod.js"></script>

</head>
<body>
<script type="text/javascript">

////////////树形结构///////////


var treeObj  = null;
var setting = {
		callback: {
			onClick: zTreeOnClick
		},
		data:{
			keep:{
				parent:true
			}
		},
		async:{
			enable:true,
			url:'packageController/getSubNodes',
			//异步加载发送的数据结构 1:name , 2 type:设置的类型，服务器端根据类型进行相应的处理
			// 3:identifyInfo 用于鉴别该节点的信息，如java节点就存包名，method节点就存放包名+类名 
			// 查找java的方法需要 packageName + id
			//查找method下的节点需要canonicalClassName + id(存的是方法名)//paramTypeArrStr
			autoParam:['name','id','type','packageName','canonicalClassName','paramTypeArrStr']
		}
	};
	
	
$(function(){
	$.ajax({
		type:"POST",
		url:'packageController/getPackageInfo',
		success:function(data){
			var nodeData = $.parseJSON(data);
			
			treeObj = $.fn.zTree.init( $("#myTree") , setting, nodeData );
		}
	});
});

var currentNode = null;
function zTreeOnClick(event,treeId,treeNode){
	currentNode = treeNode;
	
	
	if(currentNode['type'] == "jsonFile"){
		//rightIFrame.src = "viewPage.jsp";
		document.getElementById("rightIFrame").src="viewPage.jsp";
		return;

	}else if(currentNode['type'] == 'method'){
		document.getElementById("rightIFrame").src="operation.jsp";
	}
	//alert(JSON.stringify(currentNode));
}






/*
 *
 构造的数据格式
Json data structure 
	{
		'tableBlock':[
		           	{tableName:'member_info',
		           	 alias:'a',
		           	 useCols['col1','col2'....]
		           	 notUseCols['col1']
		           	}
		           ],
		 'relationBlock':[{
		               table1Alias:'a',
		               table1Col:'member_id'
		               table2Alias:'b',
		               table2Col:'member_id',
		               joinType:'inner',
		               condition:'='
		 				}   
		               ]
	}
*/
//当前工作的sqlArea
var currentSqlArea = null;
$(function(){
	currentSqlArea = $("#sqlArea1");
});

//展开还是收起
function openOrClose(linkObj){
	var status = $(linkObj).attr("status");  
	var currTableBlock = $(linkObj).parents("div[name='tableBlock']");
	var detailTable = currTableBlock.find("[name='detailTable']");
	if('1' == status){
		//1为展开
		$(detailTable).hide();
		$(linkObj).attr("status","0").html("展开");
	}else{
		$(detailTable).show();
		$(linkObj).attr("status","1").html("收起");
	}
}

//获取表信息
function getTableInfo(clickedButton){
	var currTableBlock = $(clickedButton).parents("div[name='tableBlock']");
	var tableName = currTableBlock.find("#tableInfoArea").find("#tableName").val();
	var detailTable = $(currTableBlock).find("#detailTable");
	$.ajax({
		type:'GET',
		data:{"tableName":tableName},
		url:'sqlGen/getTableInfo',
		success:function(datas){
			var jsonObj = $.parseJSON(datas);
			initTableInfo(jsonObj,detailTable);
		}
	});
}

//填充表信息
function initTableInfo(jsonObj,detailTable){
	for(var v = 0; v<jsonObj.length;v++){
		var curr = jsonObj[v];
		var tr = $("<tr>");
		var checkBox = $("<input type='checkBox' name='colBtn'/>")
						.attr("colName",curr['columnName'])
						.attr("checked",true);
		var td1 = $("<td>").append(checkBox).appendTo(tr);
		var td2 = $("<td>").html(curr['columnName']).appendTo(tr);
		var td3 = $("<td>").html(curr['typeName']).appendTo(tr);
		var td4 = $("<td>").html(curr['remarks']).appendTo(tr);
		tr.appendTo(detailTable);
	}
}

//全选按钮,表字段的全选
function followMe(clickedObj){
	var detailTable = $(clickedObj).parents("#detailTable");
	$(detailTable).find("tr:gt(0)").each(function(){
		$(this).find("td").eq(0).find("input[type='checkbox']").attr("checked",$(clickedObj).is(":checked"));
	});
}

//添加tableBlock
function addTableBlock(){
	var tableBlock = $($("#tableBlockTemplate").html());
	var tableBlockCount = currentSqlArea.find("[name='tableBlock']").length;
	$(tableBlock).attr("id","tableBlock"+(tableBlockCount + 1));
	tableBlock.appendTo(currentSqlArea);
	return tableBlock;
}
//添加关系
function addRelationBlock(){
	//按顺序对relationBlock进行编号	
	var relationBlock = $($("#relationTemplate").html());
	var currRelationBlockCount = $(currentSqlArea).find("[name='relationBlock']").length;
	$(relationBlock).attr("id","relationBlock"+(currRelationBlockCount + 1));
	$(relationBlock).appendTo(currentSqlArea);
	return relationBlock;
}


function getJson(){
	var jsonObj = {};
	var tableBlockArr = new Array();
	
	jsonObj['tableBlock'] = tableBlockArr;
	
	//页面数据的TableBlock
	var tableBlockArray = currentSqlArea.find("[name='tableBlock']");
 	for(var v=0;v<tableBlockArray.length;v++){
		var block = tableBlockArray[v];
		var obj = {};
		var tableName = $(block).find("#tableName").val();
		var alias = $(block).find("#alias").val();
		
		obj['tableName'] = tableName;
		obj['alias'] = alias;
		
		var useCols = new Array();
		var notUseCols = new Array();
		
		$(block).find("#detailTable").find("tr:gt(0)").each(function(){
			var tr = $(this);
			var tdArr = $(this).find("td");
			var checkbox = $( $(tdArr[0]).find("input") ); 
			if( checkbox.is(":checked") ){
				useCols.push( $(tdArr[1]).html() );
			}else{
				notUseCols.push( $(tdArr[1]).html() );
			}
		});
		obj['useCols'] = useCols;
		obj['notUseCols'] = notUseCols;
		
		tableBlockArr.push( obj );
	}
 	
 	//relationBlock 处理
 	var relationBlockArr = new Array();
 	jsonObj['relationBlock'] = relationBlockArr;
 	var relationBlockArray = $(currentSqlArea).find("[name='relationBlock']");
 	
 	for(var v = 0;v< relationBlockArray.length; v++){
 		
 		var relationObj = {};
 		
 		var relationBlock = $(relationBlockArray[v]);
 		var table1Str = $(relationBlock).find("#table1Alias").val();
 		var table2Str = $(relationBlock).find("#table2Alias").val();
 		var table1Alias = table1Str.split("\.")[0];
 		var table1Col = table1Str.split("\.")[1];
 		
 		relationObj['table1Alias'] = table1Alias;
 		relationObj['table1Col'] = table1Col;
 		
 		var table2Alias = table2Str.split("\.")[0];
 		var table2Col = table2Str.split("\.")[1];
 		
 		relationObj['table2Alias'] = table2Alias;
 		relationObj['table2Col'] = table2Col;
 		
 		var joinType = $(relationBlock).find("#joinType").val();
 		relationObj['joinType'] = joinType;
 		
 		var condition  = $(relationBlock).find("#condition").val();
 		relationObj['condition'] = condition;
 		
 		relationBlockArr.push(relationObj);
 	}
 	
 	var allStr = JSON.stringify(jsonObj);
 	$("#showSqlArea").html(allStr);
 	return allStr;
}
 
 //删除关系
 function deleteRelation(){
	 var relationArr = $(currentSqlArea).find("[name='relationBlock']");
	 $(relationArr[relationArr.length -1 ]).remove();
 }
 //删除表
 function deleteTable(){
	 var tableArr = $(currentSqlArea).find("[name='tableBlock']");
	 $( tableArr[tableArr.length -1 ] ).remove();
 }
 
 //发送Json数据
 function sendJson(){
	 $('#jsonNameDg').dialog('open');
 }
function realSend(){
	 var sendData = {};
	 sendData['jsonStr'] = getJson();
	 sendData['jsonFileName'] = $("#jsonNameDg").find("#jsonName").val(); 
	 sendData['id'] = currentNode['id'];
	 sendData['canonicalClassName'] = currentNode['canonicalClassName'];
	 $.post("sqlGen/handleJson",sendData,function(datas){
			alert(datas);
	 });
} 
 
 ///  TODO 调整if等语句的括号后面的空格
 
 
 /////////////////jsonfile反序列化/////////////////////
 //这里应该进行更严格的处理 
function deserializeData(datas){
	 var jsonObj = datas;
	 var tableBlock = jsonObj['tableBlock'];
	 var relationBlock = jsonObj['relationBlock'];
	 for(var v = 0; v < tableBlock.length ; v ++){
		//对tableBlock进行反序列化
		initTableBlock(tableBlock[v]);	 
		//对relationBlock进行反序列化,这里进行判断，如果存在才进行
		if(relationBlock[v]){
			initRelationBlock(relationBlock[v]);	
		}
	 }
 }
 //反序列化关系
function initRelationBlock(relationBlockData){
	var relationBlock = addRelationBlock();
	var table1Alias = relationBlockData['table1Alias'];
	var table1Col = relationBlockData['table1Col'];
	
	var table2Alias = relationBlockData['table2Alias'];
	var table2Col = relationBlockData['table2Col'];
	
	var joinType = relationBlockData['joinType'];
	var condition = relationBlockData['condition'];
	
	relationBlock.find("#table1Alias").val(table1Alias +"."+table1Col );
	relationBlock.find("#table2Alias").val(table2Alias +"."+table2Col);
	
	switch (joinType){
		case "1":
			relationBlock.find("#leftJoin").attr("checked",true);
			break;
		case "2":
			relationBlock.find("#innerJoin").attr("checked",true);
			break;
		case "3":
			relationBlock.find("#rightJoin").attr("checked",true);
	}
	relationBlock.find("#condition").val(condition);	
}
 
//反序列化tableBlock
function initTableBlock(tableBlockObj){
	
	var tableName = tableBlockObj['tableName'];
	var alias = tableBlockObj['alias'];
	
	var useCols = tableBlockObj['useCols'];
	var tableBlock = addTableBlock();
	
	//表格
	var detailTable = $(tableBlock).find("#detailTable");
	
	var tableInfoData = null;
	$.ajax({
		type:'POST',
		data:{"tableName":tableName},
		async:false,
		url:'sqlGen/getTableInfo',
		success:function(datas){
			var jsonObj = $.parseJSON(datas);
			if(!jsonObj) alert("获取表"+tableName+"数据失败");
			tableInfoData = jsonObj;
		}
	});
	
	tableBlock.find("#tableName").val(tableName);
	tableBlock.find("#alias").val(alias);
	//填充数据
	for(var v = 0; v<tableInfoData.length;v++){
		var curr = tableInfoData[v];
		var tr = $("<tr>");
		var checkBox = $("<input type='checkBox' name='colBtn'/>")
						.attr("colName",curr['columnName'])
						;
		if( contains(curr['columnName'], useCols) ){
			$(checkBox).attr("checked",true);
		}
		
		$("<td>").append(checkBox).appendTo(tr);
		$("<td>").html(curr['columnName']).appendTo(tr);
		$("<td>").html(curr['typeName']).appendTo(tr);
		$("<td>").html(curr['remarks']).appendTo(tr);
		tr.appendTo(detailTable);
	}
}
 
 
//是否包含
function contains(str,arr){
	for(var v = 0;v<arr.length;v++){
		if(str == arr[v]) return true;
	}
	return false;
}
</script>
<body class="easyui-layout">
<br>
 <style>
 	.normalTableBorder{
 	border:1px solid black;
 	}
 	table{
		width:400px;
 	}
	table td{
	border:1px solid black;
	padding:0px;
	margin:0px;
	}
	tr{
	}
 </style>
    
    <div id="jsonNameDg" class="easyui-dialog" style="width:500px;height:200px;" closed="true" buttons="#jsonNameDg-buttons">
 	 	<table width="100%" border="0" cellspacing="0" align="center" class="add">
            <tr>
              	<td align="right" width="130px">名称：</td>
              	<td >
			  		<input type="text" id="jsonName"/>
              	</td>
            </tr>
         </table>
    </div>
	<div style="text-align:center" id="jsonNameDg-buttons">
		<a href="javascript:void(0);" plain='false' class="easyui-linkbutton"
			iconCls="icon-ok" onclick="realSend()">保存</a> 
	</div>
    
    
    <div id="westArea" data-options="region:'west',split:true" style="width:250px;">
    	<ul id="myTree" class="ztree"></ul>
    
    </div>
    
    <div id="centerArea" data-options="region:'center'">
    
	<iframe id="rightIFrame" src="operation.jsp" width="100%" height="100%"  scrolling="auto">
	</iframe>
    </div>
    
 

</body>
</html>