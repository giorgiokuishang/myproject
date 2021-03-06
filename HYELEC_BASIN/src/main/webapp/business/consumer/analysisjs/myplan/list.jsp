<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<% String sysStaff= session.getAttribute("mSysStaff").toString();
	String sysRole = session.getAttribute("mSysRole").toString();
%>
<style type="text/css">
.clgl-13{ width:100%; margin:0 auto;}
.clgl-14{ float:left; width:20%;  padding-top:20px; line-height:35px;  text-align:center; }
.clgl-16{ float:left; width:100%;  padding-top:20px; line-height:35px; text-align:center; }
  #style2{
   margin-top: -87px;
  } 

</style>
<%-- <%= %> --%>
	<div class="container-fluid">

		<div id="maincontent" class="row-fluid">
			<!-- tab -->
			<div class="tab-content">
  				<div class="tab-pane active" id="tabContent1">
					<!-- 按钮工具条开始 -->
					<div class="row-fluid col-md-12">
					<div id="tbar" class="btn-toolbar">
						<div class="btn-group">
							<button type="button" id="btn_myplan_del" class="btn btn-danger btn_del_color">
							   <div class="visible-md visible-lg"><i class="icon-trash"></i>&nbsp;批量删除</div>
							   <div class="visible-xs visible-sm"><i class="icon-trash"></i></div>
							</button>
						</div>
						<div class="btn-group pull-right col-lg-4 col-md-4 col-sm-4 col-xs-6">
							<div class="input-group" id="style2">
								<input type="text" id="query_searchName" autofocus="autofocus" class="form-control" placeholder="输入关键字进行模糊查询"> 
								<span class="input-group-btn">
									<button class="btn btn-primary" id="query_myplan_ref" type="button">
									   <div class="visible-md visible-lg"><i class="icon icon-search"></i>&nbsp;查询</div>
									   <div class="visible-xs visible-sm"><i class="icon icon-search"></i></div>
									</button>
								</span>
							</div>
						</div>
					</div>
				</div>
				<!-- 按钮工具条结束 -->
				<div class="clgl-13">
					<div class="clgl-30 tab-content">
					    <table id="query_myplan_table"  class="table-condensed table-hover table-cursor">
					        <input type="hidden" id="sfatt"  value="<%=sysStaff %>">
					           <input type="hidden" id="sysRole"  value="<%=sysRole %>">
					        <thead>
					            <tr>
					            	<th data-halign="center" data-align="center" data-sortable="false" data-width="50" data-field="state"  data-checkbox="true" data-formatter="FMT_Check"></th>
									<th data-halign="center" data-align="center" data-sortable="false" data-width="40px" data-formatter="FMT_Num">序号</th>
									<th data-halign="center" data-align="center" data-sortable="false" data-field="STNM" data-width="" data-formatter="">测站名称</th>
									<th data-halign="center" data-align="center" data-sortable="false" data-field="PROG_NAME" data-width="" data-formatter="">方案名称</th>
									<th data-halign="center" data-align="center" data-sortable="false" data-field="MODEL_NAME" data-width="">模型名称</th>
									<th data-halign="center" data-align="right" data-sortable="false" data-field="CREATE_TIME" data-width="" data-formatter="">方案日期</th>
									<th data-halign="center" data-align="right" data-sortable="false" data-field="REMARK" data-width="" data-formatter="">说明</th>
									<th data-halign="center" data-align="center" data-sortable="false" data-field="PROG_CODE" data-width="140px" data-formatter="FMT_handle">操作</th>
					            </tr>
					        </thead>
					    </table>
				    </div>
			    </div>
			  </div>
			</div>
        </div>
	  </div> <!-- maincontent -->
<%@include file="edit.jsp"%>
<!-- 不要改变以下引用顺序 -->

<script src="../business/consumer/analysisjs/myplan/query.js"></script>
<script src="../business/consumer/analysisjs/myplan/list.js"></script>
