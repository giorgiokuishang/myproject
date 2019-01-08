<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
	<div class="container-fluid" style="height:100%;">
		<div class="row-fluid" style="height:30px;">
			<h3 class="text-primary">
				<ol class="breadcrumb">
					<li>资料管理</li>
                    <li style="color:black;">运行数据</li>
				</ol>
			</h3>
		</div>
		<div id="maincontent" class="row-fluid" style="height:calc(100% - 110px);">
			<!-- tab -->
			<ul class="nav nav-tabs" style="margin-bottom: 5px;padding-top:0px;">
				<li class="active"><a data-tab href="#tabContent1">洪水传播时间表</a></li>
				<li class=""><a data-tab href="#tabContent5">退水曲线表</a></li>
				<li class=""><a data-tab href="#tabContent2">水位流量关系曲线</a></li>
				<li class=""><a data-tab href="#tabContent3">库（湖）容曲线</a></li>
				<li class=""><a data-tab href="#tabContent4">断面测试成果</a></li>
				<li class=""><a data-tab href="#tabContent6">降雨径流相关图</a></li>
			</ul>
			<div id="yunxingshujutabcontent" class="tab-content" style="height:calc(100% - 40px);">
  				<div class="tab-pane active" id="tabContent1" style="height:100%;">
			  		<%@include file="floodTran/list.jsp"%>
			  	</div>
  				<div class="tab-pane" id="tabContent2" style="height:100%;">
			  		<%@include file="waterLevelFlow/list.jsp"%>
			  	</div>
  				<div class="tab-pane" id="tabContent3" style="height:100%;">
			  		<%@include file="storageCapacity/list.jsp"%>
			  	</div>
  				<div class="tab-pane" id="tabContent4" style="height:100%;">
			  		<%@include file="sectionTest/list.jsp"%>
			  	</div>
			  	<div class="tab-pane" id="tabContent6" style="height:100%;">
			  		<%@include file="rrffData/list.jsp"%>
			  	</div>
			  	<div class="tab-pane" id="tabContent5" style="height:100%;">
			  		<%@include file="/business/consumer/tsqxData/tsqx/list.jsp"%>
			  	</div>
			</div>
        </div>
	  </div> <!-- maincontent -->
<script type="text/javascript">
 var tm="";
</script>
 <!-- 不要改变以下引用顺序 -->
<%@include file="upload_floodTran.jsp"%>
<%@include file="upload_rrffData.jsp"%>
<%@include file="upload_sectionTest.jsp"%>
<%@include file="upload_storageCapacity.jsp"%>
<%@include file="upload_waterlevelFloe.jsp"%>
<%@include file="floodTran/edit.jsp"%>
<%@include file="waterLevelFlow/edit.jsp"%>
<%@include file="waterLevelFlow/edit_x.jsp"%>
<%@include file="storageCapacity/edit.jsp"%>
<%@include file="storageCapacity/edit_x.jsp"%>
<%@include file="sectionTest/edit.jsp"%>
<%@include file="sectionTest/edit_x.jsp"%>
<%@include file="rrffData/edit.jsp"%>
<%@include file="rrffData/edit_x.jsp"%>
<%@include file="floodTran/details.jsp" %>
<%@include file="waterLevelFlow/details.jsp" %>
<%@include file="storageCapacity/details.jsp" %>
<%@include file="sectionTest/details.jsp" %>
<%@include file="rrffData/details.jsp" %>
<%@include file="myEcharts.jsp"%>
<%@include file="rainRunoffecharts.jsp"%>



<script src="../business/consumer/runningData/myEcharts.js"></script><!-- echarts -->
<script src="../business/consumer/runningData/floodTran/query.js"></script>
<script src="../business/consumer/runningData/waterLevelFlow/query.js"></script>
<script src="../business/consumer/runningData/storageCapacity/query.js"></script>
<script src="../business/consumer/runningData/sectionTest/query.js"></script>
<script src="../business/consumer/runningData/rrffData/query.js"></script>
<script src="../business/consumer/runningData/list.js"></script>

