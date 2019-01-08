<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<style>
	.form-group{
		margin-bottom: -4px;
	}
</style>
	<div class="modal fade" id="edit_dialog_sectionTest_x">
		<div class="modal-dialog modal-lg" style="width: 1020px">
			<div class="modal-content">
				<div class="modal-header" style="height: 40px">
					<button type="button" class="btn btn-link close"
						data-dismiss="modal">
						<span aria-hidden="true"><i class="icon icon-times"></i></span><span class="sr-only">关闭</span>
					</button>
					<h4 class="modal-title" style="line-height: 15px;"><i class='icon icon-plus'></i>新增洪水传播时间</h4>
				</div>

				<div class="modal-body" style="padding-top: 8px;">
					<form id="info_form_sectionTest_x" name="info_form" class="form-horizontal"
						autocomplete="off" method="post"
						data-bv-message="This value is not valid"
						data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
						data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
						data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">
					 <div style="display: none;">
							ids<input type="text" class="form-control" id="ids_rvsect_x"
							name="mRvsectFormBean.ids">
							stcd<input type="text" class="form-control" id="stcd_rvsect_x"
							name="mRvsectFormBean.mRvsectInfoBean.stcd">
					</div> 
				<div class="form-group" style="margin-top: 10px;">
					<label class="col-md-2 control-label"> 
                              <span class="text-danger">*&nbsp;</span> 测站名称：
					</label>
					<div class="col-md-2 rowGroup">
						<input type="text" class="form-control" id="stnm_rvsect_x" 
                                  name="mRvsectFormBean.mRvsectInfoBean.stnm" 
                                  data-bv-group=".rowGroup" 
                                  required
                                  readonly="readonly">
					</div>
					<label class="col-md-2 control-label"> 
                              <span class="text-danger">*&nbsp;</span> 施测时间：
					</label>
					<div class="col-md-2 rowGroup">
							<input type="text" class="form-control" id="mstm_rvsect_x" 
                                  name="mRvsectFormBean.mRvsectInfoBean.mstm" 
                                  data-bv-group=".rowGroup" 
                                  required
                                  readonly="readonly">
					</div>
					<div class="col-md-2 rowGroup">
						<button type="button" id="query_add_sectionTest_x" class="btn btn-primary">
						   <div class="visible-md visible-lg"><i class="icon icon-plus-sign"></i>&nbsp;新增数据</div>
						   <div class="visible-xs visible-sm"><i class="icon icon-file-o"></i></div>
						</button>
					</div>
				</div>
				<div style="min-height: 400px;width: 400px;overflow-y:auto;">
				<table class="table-bordered" style="margin-bottom: 0px;margin-top: 30px;" id="query_table_sectionTest_x" >
				</table>
				</div>
				<div id="myEchart_section" style="position:absolute; top:70px; left:450px;width: 550px; height:400px;">
				</div>
				</form>
				</div>
			</div>
				<div class="modal-footer"
					style="height: 40px;">
					<button type="button" id="btn_save_sectionTest_x" class="btn btn-primary"
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



