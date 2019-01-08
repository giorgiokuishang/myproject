<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
	<div class="container-fluid" style="height:100%;">
		<div class="row-fluid" style="height:30px;">
			<h3 class="text-primary">
				<ol class="breadcrumb">
					<li>资料管理</li>
                    <li style="color:black;">水文数据</li>
				</ol>
			</h3>
		</div>
		<div id="maincontent" class="row-fluid" style="height:calc(100% - 110px);">
			<!-- tab -->
			<ul class="nav nav-tabs" style="margin-bottom: 5px;">
				<li class="active"><a data-tab href="#tabContent1">降水量</a></li>
				<li class=""><a data-tab href="#tabContent6">日降水量</a></li>
				<li class=""><a data-tab href="#tabContent2">日蒸发量</a></li>
				<li class=""><a data-tab href="#tabContent3">河道水情</a></li>
				<li class=""><a data-tab href="#tabContent4">水库水情</a></li>
				<li class=""><a data-tab href="#tabContent5">土壤墒情</a></li>
			</ul>
			<div id="shuiwenshujutabcontent" class="tab-content" style="height:calc(100% - 55px);">
  				<div class="tab-pane active" id="tabContent1" style="height:100%;">
			  		<%@include file="precipitation/list.jsp"%>
			  	</div>
			  	<div class="tab-pane" id="tabContent6" style="height:100%;">
			  		<%@include file="dayprecipitation/list.jsp"%>
			  	</div>
  				<div class="tab-pane" id="tabContent2" style="height:100%;">
			  		<%@include file="diurnalEvaporation/list.jsp"%>
			  	</div>
  				<div class="tab-pane" id="tabContent3" style="height:100%;">
			  		<%@include file="riverWater/list.jsp"%>
			  	</div>
  				<div class="tab-pane" id="tabContent4" style="height:100%;">
			  		<%@include file="reservoirWater/list.jsp"%>
			  	</div>
  				<div class="tab-pane" id="tabContent5" style="height:100%;">
			  		<%@include file="soilMoisture/list.jsp"%>
			  	</div>
			</div>
        </div>
	  </div> <!-- maincontent -->
<script>
    var tm="";
</script>
<!-- 不要改变以下引用顺序 -->
<%@include file="precipitation/upload.jsp"%>
<%@include file="riverWater/upload.jsp"%>
<%@include file="dayprecipitation/upload.jsp"%>
<%@include file="diurnalEvaporation/upload.jsp"%>
<%@include file="precipitation/edit.jsp"%>
<%@include file="dayprecipitation/edit.jsp"%>
<%@include file="diurnalEvaporation/edit.jsp"%>
<%@include file="riverWater/edit.jsp"%>
<%@include file="reservoirWater/edit.jsp"%>
<%@include file="soilMoisture/edit.jsp"%>

<%@include file="precipitation/details.jsp" %>
<%@include file="dayprecipitation/details.jsp" %>
<%@include file="diurnalEvaporation/details.jsp" %>
<%@include file="riverWater/details.jsp" %>
<%@include file="reservoirWater/details.jsp" %>
<%@include file="soilMoisture/details.jsp" %>

<script src="../business/consumer/hydrologicalData/precipitation/query.js"></script>
<script src="../business/consumer/hydrologicalData/diurnalEvaporation/query.js"></script>
<script src="../business/consumer/hydrologicalData/riverWater/query.js"></script>
<script src="../business/consumer/hydrologicalData/reservoirWater/query.js"></script>
<script src="../business/consumer/hydrologicalData/soilMoisture/query.js"></script>
<script src="../business/consumer/hydrologicalData/dayprecipitation/query.js"></script>
<script src="../business/consumer/hydrologicalData/list.js"></script>
