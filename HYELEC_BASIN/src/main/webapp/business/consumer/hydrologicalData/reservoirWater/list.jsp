<%@page import="com.lyht.util.DateUtil"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<!-- 按钮工具条开始 -->
<div class="row-fluid col-md-12">
	<div id="tbar" class="btn-toolbar">
		<div class="btn-group">
			<button type="button" id="query_add_reservoirWater" class="btn btn-primary">
			   <div class="visible-md visible-lg"><i class="icon icon-plus-sign"></i>&nbsp;新增</div>
			   <div class="visible-xs visible-sm"><i class="icon icon-file-o"></i></div>
			</button>
		</div>
		<div class="btn-group">
			<button type="button" id="btn_del_reservoirWater" class="btn btn-danger btn_del_color">
			   <div class="visible-md visible-lg"><i class="icon-trash"></i>&nbsp;批量删除</div>
			   <div class="visible-xs visible-sm"><i class="icon-trash"></i></div>
			</button>
		</div>
		<div class="btn-group pull-left visible-lg visible-md visible-sm">
			<button class="btn btn-primary" id="btn_into_pptn_rsvr" type="button">
				   <div class="visible-md visible-lg"><i class="icon icon-cloud-download"></i>&nbsp;导入</div>
				   <div class="visible-xs visible-sm"><i class="icon icon-cloud-download"></i></div>
			</button>
		</div>
		<div class="btn-group pull-left visible-lg visible-md visible-sm">
			<button class="btn btn-primary" id="btn_outAll_pptn_rsvr" type="button">
				   <div class="visible-md visible-lg"><i class="icon icon-reply"></i>&nbsp;导出全部</div>
				   <div class="visible-xs visible-sm"><i class="icon icon-reply"></i></div>
			</button>
		</div>
		<div class="btn-group pull-left visible-lg visible-md visible-sm">
			<button class="btn btn-primary" id="btn_outPage_pptn_rsvr" type="button">
				   <div class="visible-md visible-lg"><i class="icon icon-circle-arrow-up"></i>&nbsp;导出当前页</div>
				   <div class="visible-xs visible-sm"><i class="icon icon-circle-arrow-up"></i></div>
			</button>
		</div>
		<div class="btn-group pull-left visible-lg visible-md visible-sm" style="float: left;left: 70px">
			<div class="input-group ">
				<input type='text' class="form-control laydatetime"
					autofocus="autofocus" style="width: 150px; height: 32px;"
					readonly="readonly"
					value='<%=DateUtil.addDay(DateUtil.getDate(), -7)+" 00:00:00"%>'
					id="startTime_rsvr" name="startTime" required
					data-bv-notempty-message="时间不能为空" placeholder="请选择日期" /> <span
					class="input-group-addon"
					style="width: 39px; height: 30px; background-color: #f9f9f9;">
					<span class="icon-calendar"></span>
				</span> <input type='text' class="form-control laydatetime"
					autofocus="autofocus" style="width: 150px; height: 32px;"
					readonly="readonly" id="endTime_rsvr" name="endTime"
					value='<%=DateUtil.getDate()+" 23:59:59"%>' required
					data-bv-notempty-message="时间不能为空" placeholder="请选择日期" /> <span
					class="input-group-addon"
					style="width: 39px; height: 30px; background-color: #f9f9f9;">
					<span class="icon-calendar"></span>
				</span>
			</div>
		</div>
		<div class="btn-group pull-right col-lg-2 col-md-2 col-sm-2 col-xs-4" >
			<div class="input-group" style="float: left;right: 190px">
				<select id="stcd_rsvr_list" class="form-control" style="width: 180px" 
                            	data-bv-group=".rowGroup" 
                            	required data-bv-notempty-message="请选择测站名称">
				</select>
			</div>
			<div class="input-group" style="float: left;margin-top: -32px;left: 0px">
				<input type="text" id="query_searchName_rsvr" name="searchName" autofocus="autofocus" class="form-control" placeholder="输入关键字进行模糊查询">
				<span class="input-group-btn">
					<button class="btn btn-primary" id="query_ref_rsvr" type="button">
					   <div class="visible-md visible-lg"><i class="icon icon-search"></i>&nbsp;查询</div>
					   <div class="visible-xs visible-sm"><i class="icon icon-search"></i></div>
					</button>
				</span>
			</div>
		</div>
	</div>
</div>
<!-- 按钮工具条结束 -->
<table id="query_table_reservoirWater"  class="table-condensed table-hover table-cursor">
    <thead>
        <tr>
        	<th data-halign="center" data-align="center" data-sortable="false" data-width="50" data-field="state"  data-checkbox="true" data-formatter="FMT_Check"></th>
			<th data-halign="center" data-align="center" data-sortable="false" data-width="40px" data-formatter="FMT_Num4">序号</th>
			<th data-halign="center" data-align="center" data-sortable="false" data-field="STNM" data-width="" data-formatter="">测站名称</th>
			<th data-halign="center" data-align="center" data-sortable="false" data-field="TM" data-width="" data-formatter="fmt_date">时间</th>
			<th data-halign="center" data-align="right" data-sortable="false" data-field="RZ" data-width="" data-formatter="">库水位(m)</th>
			<th data-halign="center" data-align="right" data-sortable="false" data-field="INQ" data-width="" data-formatter="">入库流量(m<sup>3</sup>/s)</th>
			<th data-halign="center" data-align="right" data-sortable="false" data-field="OTQ" data-width="" data-formatter="">出库流量(m<sup>3</sup>/s)</th>
			<th data-halign="center" data-align="center" data-sortable="false" data-field="RWCHRCD" data-width="" data-formatter="FMT_FLWCHRCD">库水特征码</th>
			<th data-halign="center" data-align="right" data-sortable="false" data-field="RWPTN" data-width="" data-formatter="FMT_WPTN">库水水势</th>
			<th data-halign="center" data-align="center" data-sortable="false" data-field="STCD" data-width="140px" data-formatter="FMT_handle4">操作</th>
        </tr>
    </thead>
</table>