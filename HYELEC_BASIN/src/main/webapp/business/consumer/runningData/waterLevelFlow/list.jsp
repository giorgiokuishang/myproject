<%@ page language="java" pageEncoding="UTF-8"%>
<!-- 按钮工具条开始 -->
<div class="row-fluid col-md-12" style="height:35px;">
	<div id="tbar" class="btn-toolbar">
		<div class="btn-group">
			<button type="button" id="query_add_waterLevelFlow" class="btn btn-primary">
			   <div class="visible-md visible-lg"><i class="icon icon-plus-sign"></i>&nbsp;新增</div>
			   <div class="visible-xs visible-sm"><i class="icon icon-file-o"></i></div>
			</button>
		</div>
		<div class="btn-group">
			<button type="button" id="btn_del_waterLevelFlow" class="btn btn-danger btn_del_color">
			   <div class="visible-md visible-lg"><i class="icon-trash"></i>&nbsp;批量删除</div>
			   <div class="visible-xs visible-sm"><i class="icon-trash"></i></div>
			</button>
		</div>
		<div class="btn-group pull-left visible-lg visible-md visible-sm">
			<button class="btn btn-primary" id="btn_into_zqrl" type="button">
				   <div class="visible-md visible-lg"><i class="icon icon-cloud-download"></i>&nbsp;导入</div>
				   <div class="visible-xs visible-sm"><i class="icon icon-cloud-download"></i></div>
			</button>
		</div>
		<div class="btn-group pull-left visible-lg visible-md visible-sm">
			<button class="btn btn-primary" id="btn_outAll_zqrl" type="button">
				   <div class="visible-md visible-lg"><i class="icon icon-reply"></i>&nbsp;导出全部</div>
				   <div class="visible-xs visible-sm"><i class="icon icon-reply"></i></div>
			</button>
		</div>
		<div class="btn-group pull-left visible-lg visible-md visible-sm">
			<button class="btn btn-primary" id="btn_outPage_zqrl" type="button">
				   <div class="visible-md visible-lg"><i class="icon icon-circle-arrow-up"></i>&nbsp;导出当前页</div>
				   <div class="visible-xs visible-sm"><i class="icon icon-circle-arrow-up"></i></div>
			</button>
		</div>
		<div class="btn-group pull-left visible-lg visible-md visible-sm">
			<button class="btn btn-primary" id="btn_muban_zqrl" type="button">
				   <div class="visible-md visible-lg"><i class="icon icon-cloud-download"></i>&nbsp;下载模板</div>
				   <div class="visible-xs visible-sm"><i class="icon icon-cloud-download"></i></div>
			</button>
		</div>
		<div class="btn-group pull-right col-lg-4 col-md-4 col-sm-4 col-xs-6">
			<div class="input-group">
				<input type="text" id="query_searchName_zqrl" autofocus="autofocus" class="form-control" placeholder="输入测站名称进行模糊查询"> 
				<span class="input-group-btn">
					<button class="btn btn-primary" id="query_ref_zqrl" type="button">
					   <div class="visible-md visible-lg"><i class="icon icon-search"></i>&nbsp;查询</div>
					   <div class="visible-xs visible-sm"><i class="icon icon-search"></i></div>
					</button>
				</span>
			</div>
		</div>
	</div>
</div>
<!-- 按钮工具条结束 -->
<div id="query_table_waterLevelFlow_div" style="width:100%;height:calc(100% - 40px);overflow:auto;">
	<table id="query_table_waterLevelFlow"
		class="table-condensed table-hover table-cursor">
		<thead>
			<tr>
				<th data-halign="center" data-align="center" data-sortable="false"
					data-width="50" data-field="state" data-checkbox="true"
					data-formatter="FMT_Check"></th>
				<th data-halign="center" data-align="center" data-sortable="false"
					data-width="40px" data-formatter="FMT_Num2">序号</th>
				<th data-halign="center" data-align="center" data-sortable="false"
					data-field="STNM" data-width="" data-formatter="">测站名称</th>
				<th data-halign="center" data-align="center" data-sortable="false"
					data-field="LNNM" data-width="">曲线名称</th>
				<!--<th data-halign="center" data-align="center" data-sortable="false"
					data-field="PTNO" data-width="" data-formatter="">曲线序号</th>
				<th data-halign="center" data-align="right" data-sortable="false"
					data-field="Z" data-width="" data-formatter="">水位</th>
				<th data-halign="center" data-align="right" data-sortable="false"
					data-field="Q" data-width="" data-formatter="">流量</th> -->
				<th data-halign="center" data-align="center" data-sortable="false"
					data-field="STCD" data-width="140px" data-formatter="FMT_handle2">操作</th>
			</tr>
		</thead>
	</table>
</div>