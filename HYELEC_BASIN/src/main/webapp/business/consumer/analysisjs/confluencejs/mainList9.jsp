<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<head>
<%@include file="/common/inc/inc.inc"%>
<%@include file="/common/inc/bootstrapTable.inc"%>
<link rel="stylesheet" type="text/css" href="<%=basePath%>common/layui/css/layui.css"/>
<style type="text/css">
.clgl-13 {
	width: 100%;
	margin: 0 auto;
}

.clgl-14 {
	float: left;
	width: 20%;
	padding-top: 20px;
	line-height: 35px;
	text-align: center;
}

.clgl-16 {
	float: left;
	width: 80%;
	padding-top: 20px;
	line-height: 35px;
	text-align: center;
}
</style>
</head>
<script src="http://echarts.baidu.com/build/dist/echarts-all.js"></script>
<body style="background-color: #FCFCFC;">
	<div class="container-fluid" style="position: relative;">
		<%@include file="/common/inc/top.jsp"%>
		<div class="row-fluid">
			<h3 class="text-primary">
				<ol class="breadcrumb">
					<li>分析计算</li>
					<li style="color: black;">汇流计算</li>
				</ol>
			</h3>
		</div>
		<hr style="margin-top: -5px;">
		<button class="btn btn-primary" onclick="clicktr()" type="button">主要按钮</button>
		<div id="body-content" style="position: absolute;bottom: 0px;width: 100%;height: 50%;min-height:500px;">
			<div id="body-content-top" style="width:100%;height:50px;">
				<div class="btn-group">
				  <button id="step_one" onClick="nextStep(1)" class="btn">推理公式</button>
				  <button id="step_two" onClick="nextStep(2)" class="btn">推理峰量法</button>
				  <button id="step_three" onClick="nextStep(3)" class="btn">推理过程线法</button>
			 <button id="step_three" onClick="nextStep(4)" class="btn">瞬时单位线法</button>
				  <button id="step_seven" onClick="nextStep(7)" class="btn">计算结果</button>
				  <button id="hide_show" class="btn">隐藏/显示</button>
				</div>
			</div>
			<div id="body-content-bottom"  style="width:100%;height:calc(100% - 50px);overflow-y:auto"></div>
		</div>
	</div>
</body>
<!-- 不要改变以下引用顺序 -->
<script src="<%=basePath%>common/layui/layui.js"></script>
<script src="<%=basePath%>common/layui/layui.all.js"></script>
<script src="<%=basePath%>common/eCharts/chanliu-echarts.min.js"></script>
<script type="text/javascript">
	var stcd= ""; // 测站编码
	var stnm= "";//测站名称
	var beginDate = ""; // 开始时间
	var endDate = ""; // 结束时间
	var interval = ""; //时间间隔

	$(".form_datetime").datetimepicker({
		format : " yyyy-mm-dd hh:ii:ss"
	});
	$("#body-content-bottom").load("mainList.jsp",function(response,status,xhr){   //div的id用来load刷新   请求后台
	if(status!="success"){
	if(xhr.status==444){
	alert(xhr.responseText);
	}else{
	alert(status);
	}
	}
	});
	
	function nextStep(num){
							if (num == 1){
								tempUrl = "mainList.jsp";
							}else if (num == 2){
								tempUrl = "mainList2.jsp";
							}else if (num == 3){
								tempUrl = "mainList3.jsp";
							}else if (num == 4){
								tempUrl = "mainList4.jsp";
							}
							$("#body-content-bottom").load(tempUrl,function(response,status,xhr){   //div的id用来load刷新   请求后台
								if(status!="success"){
									if(xhr.status==444){
										alert(xhr.responseText);
									}else{
										alert(status);
									}
								}
							});
	}
	
	var i = 0;
	$("#hide_show").click(function(){
	if(i == 0){
	$("#body-content-bottom").hide();
	$("#body-content").css("height","32px");
	$("#body-content").css("min-height","32px");
	i = 1;
	}else{
	$("#body-content-bottom").show();
	$("#body-content").css("height","50%");
	$("#body-content").css("min-height","500px");
	i = 0;
	}
	});
</script>
