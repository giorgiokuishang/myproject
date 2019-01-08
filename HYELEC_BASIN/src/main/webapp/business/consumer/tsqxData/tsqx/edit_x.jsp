<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<style>
	.form-group{
		margin-bottom: -4px;
	}
	.date{
	 margin-top:0px;
	}
	.datetimepicker{
	 margin-top:30px;margin-left:-7px;
	}
</style>


	<div class="modal fade" id="edit_dialog_tsqxData_x">
		<div class="modal-dialog modal-lg" style="width: 1200px">
			<div class="modal-content" style="height:550px;">
				<div class="modal-header" style="height: 40px">
					<button type="button" class="btn btn-link close"
						data-dismiss="modal">
						<span aria-hidden="true"><i class="icon icon-times"></i></span><span class="sr-only">关闭</span>
					</button>
					<h4 class="modal-title" style="line-height: 15px;"><i class='icon icon-plus'></i>修改退水曲线</h4>
				</div>

				<div class="modal-body" style="padding-top: 8px;">
				<form id="info_form_tsqxData_x" name="info_form" class="form-horizontal"
					autocomplete="off" method="post"
					data-bv-message="This value is not valid"
					data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
					data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
					data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">
					 <div style="display: none;">
							stcd<input type="text" class="form-control" id="stcd_tsqx_x"
							name="mTsqxFormBean.mTsqxInfoBean.stcd">
					</div> 
					<div>
						<table>
							<tr>
								<td>
									<label><span class="text-danger">*</span> 测站名称：</label>
								</td>
								<td>
									<input type="text" id="stnm_tsqx_x" 
                                  name="mTsqxFormBean.mTsqxInfoBean.stnm" 
                                  data-bv-group=".rowGroup" 
                                  required disabled
                                  style="width:120px;"
                                  readonly="readonly">
								</td>
								<td>
									<label> 
				                             <span class="text-danger">*</span> 用户名：
									</label>
								</td>
								<td>
									<input type="text" id="userName_tsqx_x" 
                                  name="mTsqxFormBean.mTsqxInfoBean.username" 
                                  data-bv-group=".rowGroup" 
                                  style="width:90px;"
                                  required disabled
                                  readonly="readonly">
								</td>
								<td>
									<label> 
				                             <span class="text-danger">*</span> 开始时间：
									</label>
								</td>
								<td style="width:150px;">
									<div class="input-append date form_datetime">
										<input size="16" id="beginTime" type="text" readonly> <span
										 class="add-on"><i class="icon-th"></i></span>
									</div>
								</td>
								<td>
									<label> 
				                             <span class="text-danger">*</span> 结束时间：
									</label>
								</td>
								<td style="width:150px;">
									<div class="input-append date form_datetime">
										<input size="16" id="endTime" type="text" readonly> <span
										 class="add-on"><i class="icon-th"></i></span>
									</div>
								</td>
								<td>
									<label> 
				                             <span class="text-danger">*</span> 间隔(分钟)：
									</label>
								</td>
								<td>
									<input type="text" id="timeUnit" 
	                                  data-bv-group=".rowGroup" 
	                                  style="width:60px;"
	                                  required >
								</td>
								<td>
									<button type="button" id="query_river_echart" class="btn btn-primary">
									   <div class="visible-md visible-lg"><i class="icon icon-plus-search"></i>&nbsp;查询</div>
									   <div class="visible-xs visible-sm"><i class="icon icon-file-o"></i></div>
									</button>
								</td>
								<td>
									<button type="button" id="query_add_tsqxData_x" class="btn btn-primary">
									   <div class="visible-md visible-lg"><i class="icon icon-plus-sign"></i>新增</div>
									   <div class="visible-xs visible-sm"><i class="icon icon-file-o"></i></div>
									</button>
								</td>
							</tr>
						</table>
					</div>
					<div>
						<div style="height: 400px;width: 30%;overflow-y:auto;float:left;">
							<table class="table-bordered" style="margin-bottom: 0px;margin-top: 10px;" id="query_table_tsqxData_x" >
							</table>
						</div>
						<div style="width: 35%; height:400px;float:right;">
							<div id="myEcharts_hdsq" style="width: 400px; height:400px;float:right;"></div>
						</div>
						<div style="width: 35%; height:400px;float:right;">
							<div id="myEcharts_tsqx" style="width: 400px; height:400px;float:right;"></div>
						</div>
					</div>
				</form>
				</div>
			</div>
				<div class="modal-footer"
					style="height: 40px;">
					<button type="button" id="btn_save_tsqx" class="btn btn-primary"
						style="margin-top: -14px">
						<i class="icon icon-download-alt"></i> 保存
					</button>
					<button type="button" class="btn btn-large"
						data-dismiss="modal"
						style="margin-top: -14px; margin-right: -5px;">
						<i class="icon icon-times"></i> 取消
					</button>
				</div>
			</div>
		</div>

</form>



