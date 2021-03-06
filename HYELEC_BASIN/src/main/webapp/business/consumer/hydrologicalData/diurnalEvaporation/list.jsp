<%@page import="com.lyht.util.DateUtil"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<!-- 按钮工具条开始 -->
<div class="row-fluid col-md-12" style="height:35px;">
	<div id="tbar" class="btn-toolbar">
		<div class="btn-group">
			<button type="button" id="query_add_diurnalEvaporation" class="btn btn-primary">
			   <div class="visible-md visible-lg"><i class="icon icon-plus-sign"></i>&nbsp;新增</div>
			   <div class="visible-xs visible-sm"><i class="icon icon-file-o"></i></div>
			</button>
		</div>
		<div class="btn-group">
			<button type="button" id="btn_del_diurnalEvaporation" class="btn btn-danger btn_del_color">
			   <div class="visible-md visible-lg"><i class="icon-trash"></i>&nbsp;批量删除</div>
			   <div class="visible-xs visible-sm"><i class="icon-trash"></i></div>
			</button>
		</div>
		<div class="btn-group pull-left visible-lg visible-md visible-sm">
			<button class="btn btn-primary" id="btn_into_pptn_dayev" type="button">
				   <div class="visible-md visible-lg"><i class="icon icon-cloud-download"></i>&nbsp;导入</div>
				   <div class="visible-xs visible-sm"><i class="icon icon-cloud-download"></i></div>
			</button>
		</div>
		<div class="btn-group pull-left visible-lg visible-md visible-sm">
			<button class="btn btn-primary" id="btn_outAll_pptn_dayev" type="button">
				   <div class="visible-md visible-lg"><i class="icon icon-reply"></i>&nbsp;导出全部</div>
				   <div class="visible-xs visible-sm"><i class="icon icon-reply"></i></div>
			</button>
		</div>
		<div class="btn-group pull-left visible-lg visible-md visible-sm">
			<button class="btn btn-primary" id="btn_outPage_pptn_dayev" type="button">
				   <div class="visible-md visible-lg"><i class="icon icon-circle-arrow-up"></i>&nbsp;导出当前页</div>
				   <div class="visible-xs visible-sm"><i class="icon icon-circle-arrow-up"></i></div>
			</button>
		</div>
		<div class="btn-group pull-left visible-lg visible-md visible-sm">
			<button class="btn btn-primary" id="btn_muban_dayev" type="button">
				   <div class="visible-md visible-lg"><i class="icon icon-cloud-download"></i>&nbsp;下载模板</div>
				   <div class="visible-xs visible-sm"><i class="icon icon-cloud-download"></i></div>
			</button>
		</div>
		<div class="btn-group pull-left visible-lg visible-md visible-sm">
			<a class='date' style="left:10px;"> <label
				class="input" style="display: inline;width:170px;"> <input
					type='text' class="laydatetime" autofocus="autofocus"
					style="width: 170px; height: 30px;background-color:#fff;" readonly="readonly"
					value='<%=DateUtil.addDay(DateUtil.getDate(), -7)+" 00:00:00"%>'
					id="startTime_dayev" name="startTime" required
					data-bv-group=".rowGroup"
					data-bv-notempty-message="时间不能为空"
					placeholder="请选择时间"> <span
					style="width: 39px; height: 30px;background-color:#f9f9f9;"> <span
						class="icon-calendar"></span>
					</span>
				</label>
			</a>
		</div>
		<div class="btn-group pull-left visible-lg visible-md visible-sm">
			<a class='date' style="left:10px;"> <label
				class="input" style="display: inline;width:170px;"> <input
					type='text' class="laydatetime" autofocus="autofocus"
					style="width: 170px; height: 30px;background-color:#fff;" 
					data-bv-group=".rowGroup"
					data-bv-notempty-message="时间不能为空"
					readonly="readonly" id="endTime_dayev" name="endTime"
					value='<%=DateUtil.getDate()+" 23:59:59"%>' required
					placeholder="请选择时间"> <span
					style="width: 39px; height: 30px;background-color:#f9f9f9;"> <span
						class="icon-calendar"></span>
					</span>
				</label>
			</a>
		</div>
		<div class="btn-group pull-left visible-lg visible-md visible-sm">
			<select id="stcd_dayev_list" class="form-control" style="width: 150px;margin-left:10px;" 
                      	data-bv-group=".rowGroup" 
                      	required data-bv-notempty-message="请选择测站名称">
			</select>
		</div>
		<div class="btn-group pull-left visible-lg visible-md visible-sm">
			<input type="text" id="query_searchName_dayev" style="width:150px;float:left;" name="searchName" autofocus="autofocus" class="form-control" placeholder="输入关键字进行模糊查询">
			<button class="btn btn-primary" style="width:80px;float:right;" id="query_ref_dayev" type="button">
			   <div class="visible-md visible-lg"><i class="icon icon-search"></i>&nbsp;查询</div>
			   <div class="visible-xs visible-sm"><i class="icon icon-search"></i></div>
			</button>
		</div>
		<%-- <div class="btn-group pull-left visible-lg visible-md visible-sm" style="float: left;width:850px;">
			<div style="width:calc(100% - 260px);float:left;">
					<div style="width:calc(100% - 200px);float:left;">
							<div style="width:50%;float:left;">
						   		<a class='date' style="left:10px;"> <label
									class="input" style="display: inline;width:200px;"> <input
										type='text' class="laydatetime" autofocus="autofocus"
										style="width: 170px; height: 30px;background-color:#fff;" readonly="readonly"
										value='<%=DateUtil.addDay(DateUtil.getDate(), -7)+" 00:00:00"%>'
										id="startTime_dayev" name="startTime" required
										data-bv-group=".rowGroup"
										data-bv-notempty-message="时间不能为空"
										placeholder="请选择时间"> <span
										style="width: 39px; height: 30px;background-color:#f9f9f9;"> <span
											class="icon-calendar"></span>
										</span>
									</label>
								</a>
						   </div>
						   <div style="width:50%;float:right;">
						   		<a class='date' style="left:10px;"> <label
									class="input" style="display: inline;width:200px;"> <input
										type='text' class="laydatetime" autofocus="autofocus"
										style="width: 170px; height: 30px;background-color:#fff;" 
										data-bv-group=".rowGroup"
										data-bv-notempty-message="时间不能为空"
										readonly="readonly" id="endTime_dayev" name="endTime"
										value='<%=DateUtil.getDate()+" 23:59:59"%>' required
										placeholder="请选择时间"> <span
										style="width: 39px; height: 30px;background-color:#f9f9f9;"> <span
											class="icon-calendar"></span>
										</span>
									</label>
								</a>
						   </div>						
					</div>
					<div style="width:200px;float:right;">
						<select id="stcd_dayev_list" class="form-control" style="width: 150px;margin-left:10px;" 
	                            	data-bv-group=".rowGroup" 
	                            	required data-bv-notempty-message="请选择测站名称">
						</select>
					</div>
			</div>
			<div style="width:260px;float:right;">
					<input type="text" id="query_searchName_dayev" style="width:180px;float:left;" name="searchName" autofocus="autofocus" class="form-control" placeholder="输入关键字进行模糊查询">
						<button class="btn btn-primary" style="width:80px;float:right;" id="query_ref_dayev" type="button">
						   <div class="visible-md visible-lg"><i class="icon icon-search"></i>&nbsp;查询</div>
						   <div class="visible-xs visible-sm"><i class="icon icon-search"></i></div>
						</button>
			</div>
		</div> --%>
	</div>
</div>
<!-- 按钮工具条结束 -->
<div id="query_table_diurnalEvaporation_div" style="width:100%;height:calc(100% - 40px);overflow:auto;">
	<table id="query_table_diurnalEvaporation"  class="table-condensed table-hover table-cursor">
	    <thead>
	        <tr>
	        	<th data-halign="center" data-align="center" data-sortable="false" data-width="50" data-field="state"  data-checkbox="true" data-formatter="FMT_Check"></th>
				<th data-halign="center" data-align="center" data-sortable="false" data-width="40px" data-formatter="FMT_Num2">序号</th>
				<th data-halign="center" data-align="center" data-sortable="false" data-field="STNM" data-width="" data-formatter="">测站名称</th>
				<th data-halign="center" data-align="center" data-sortable="false" data-field="TM" data-width="" data-formatter="fmt_tmdate">时间</th>
				<th data-halign="center" data-align="center" data-sortable="false" data-field="EMPT" data-width="">蒸发器类型</th>
				<th data-halign="center" data-align="right" data-sortable="false" data-field="DYE" data-width="" data-formatter="">日蒸发量</th>
				<th data-halign="center" data-align="center" data-sortable="false" data-field="STCD" data-width="140px" data-formatter="FMT_handle2">操作</th>
	        </tr>
	    </thead>
	</table>
</div>
