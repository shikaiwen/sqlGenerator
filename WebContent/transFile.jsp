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
			url:'listFileController/getSubNodes.htm',
			autoParam:['path']
		},
		view:{
			addHoverDom: addHoverDom,
			removeHoverDom: removeHoverDom
		}
	};
	
function addHoverDom(treeId, treeNode) {
	
	if($("#downloadLink").length > 0)return;
	var aObj = $("#" + treeNode.tId + "_a");//zTree树节点的命名方式
	var filePath = treeNode.path;
	filePath = encodeURI(filePath);
	var downloadLink = "<%=base%>/listFileController/downloadFile.htm?path="+filePath;
	var a = $("<a id='downloadLink' target='_blank' href='"+downloadLink+"'>下载</a>");
	$(aObj).append(a);return;
	  
/* 	if ($("#diyBtn_"+treeNode.id).length>0) return;
	var editStr = "<span id='diyBtn_space_" +treeNode.id+ "' > </span>"
		+ "<button type='button' class='diyBtn1' id='diyBtn_" + treeNode.id
		+ "' title='"+treeNode.name+"' onfocus='this.blur();'></button>";
	aObj.append(editStr);
	var btn = $("#diyBtn_"+treeNode.id);
	if (btn) btn.bind("click", function(){alert("diy Button for " + treeNode.name);}); */
}

function removeHoverDom(treeId, treeNode) {
	var a = $("#downloadLink");
	$(a).remove();
	return;
/* 	$("#diyBtn_"+treeNode.id).unbind().remove();
	$("#diyBtn_space_" +treeNode.id).unbind().remove(); */
};

$(function(){
	$.ajax({
		type:"POST",
		url:'<%=base%>/listFileController/listRootFile.htm',
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
}







</script>
<body class="easyui-layout">
<br>
    
    <div id="westArea" data-options="region:'west',split:true" style="width:250px;">
    	<ul id="myTree" class="ztree"></ul>
    
    </div>
<div id="xx"></div>
<div id="xx"></div>
<div id="xx"></div>
<div id="xx"></div>
</body>
</html>