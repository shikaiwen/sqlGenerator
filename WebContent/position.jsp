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
//文字居中暂时解决办法，让行宽等于包含文字容器的宽度
function test1(){
	var space = 5;//距离间隙
	var left = tableName.offsetLeft + tableName.offsetWidth;
	var top = tableName.offsetTop ;//+ tableName.offsetHeight/2;
	$("#errorMsg").css("position","absolute")
			.css("left",left+space +"px")
			.css("height",tableName.offsetHeight + "px")
			.css("line-height",tableName.offsetHeight +"px")
			.show();
	
}

 $(function(){
	validate($("#userDiv"));
})

//字符串 sn-n代表长度范围 如s1-6表示要求输入1-6位字符串
function validate(toValidateContainer){
	//找到有dataType属性的元素
	$(toValidateContainer).find("[dataType]").each(function(){
		$(this).bind("blur",validateSingle);
	});
}

//单个验证
function validateSingle(){
	var validateStr = $(this).attr("dataType");
	var validateResult = false;
	//如果为false，表示验证失败，则显示失败信息,并停止验证
	if(!validateResult){
		//这里的this应该是待验证的控件
		var errorMsg = $(this).attr("errorMsg");
		//	var left = tableName.offsetLeft + tableName.offsetWidth;
		//	var top = tableName.offsetTop ;//+ tableName.offsetHeight/2;
		var space = 5;//距离间隙
		var left = Number($(this).attr("offsetLeft")) + Number($(this).attr("offsetWidth"));
		var top = $(this).attr("offsetTop");
		//创建一个span用于放置errorMsg
		var span = $("<span></span>");
		span.css("position","absolute")
			.css("left",left + space +"px")
			.css("top",top+"px")
			.css("height",$(this).attr("offsetHeight") + "px")
			.css("line-height",$(this).attr("offsetHeight" + "px"))
			.appendTo($("body"));

			//跳出each循环，即终止执行
		
	}
	
//	alert(validateStr);
}

function getRegExp(dataType){
	
	var firstChar = dataType.charAt(0);
	if("s" == firstChar){
		
		//此处根据传进来的字符串生成正则表达式并进行验证
/* 		var str = dataType.substr(1,dataType.length-1);
		var arr = str.split['-'];
		var regExpStr = "\S{"+arr[0]+","+arr[1]; */
		
	}
}


function triggerValidate(event){
	if(event.keyCode == 13){
		validate($("#userDiv"));
	}
}

function Person(name,age){
	this.name = name;
	this.age = age;
	this.sayName = function(){
		name = "kkk";
		alert(this.name);
	//	alert(name)
	//	alert(window.name);
	}
}
	


function test(){
	var person1 = new Person("kevin",23);
	person1.sayName();
	alert(person1.sayName.name)
}
</script>
<body>
<br>

<br><br>
<span id="errorMsg" style="display:none;vertical-align:middle;font-size:8px;color:red">*这个东西不合法</span>
	<div id="userDiv" style="height:300px;width:350px;margin-left:auto;margin-right:auto;">
		<input type="button"  value="测试" onclick="test()">
		<input type="text" dataType="s2-5" id="tableName" size="12"/><br>
		<input type="text" dataType="s1-6" name="username"><br>
		<input type="password" name="password"/><br>
		<input type="text" name="age"/><br>
		
	</div>

</body>
</html>