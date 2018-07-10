<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<style>
	.form-group{
		margin-bottom: -4px;
	}
</style>
<form id="info_form_waterLevelFlow" name="info_form" class="form-horizontal"
	autocomplete="off" method="post"
	data-bv-message="This value is not valid"
	data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
	data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
	data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">

	<div class="modal fade" id="edit_dialog_waterLevelFlow">
		<div class="modal-dialog modal-lg" style="width: 600px">
			<div class="modal-content">
				<div class="modal-header" style="height: 40px">
					<button type="button" class="btn btn-link close"
						data-dismiss="modal">
						<span aria-hidden="true"><i class="icon icon-times"></i></span><span class="sr-only">关闭</span>
					</button>
					<h4 class="modal-title" style="line-height: 15px;"><i class='icon icon-plus'></i>新增洪水传播时间</h4>
				</div>

				<div class="modal-body" style="padding-top: 8px;">
					 <div style="display: none;">
							ids<input type="text" class="form-control" id="ids_zqrl"
							name="mZqrlFormBean.ids">
							ptno<input type="text" class="form-control" id="ptno_zqrl"
							name="mZqrlFormBean.mZqrlInfoBean.ptno">
					</div> 
				<div class="form-group" style="margin-top: 10px;">
						<label class="col-md-2 control-label"> 
                              <span class="text-danger">*&nbsp;</span> 测站名称：
						</label>
						<div class="col-md-4 rowGroup">
							<select id="stcd_zqrl" name="mZqrlFormBean.mZqrlInfoBean.stcd" class="form-control" 
                            	data-bv-group=".rowGroup" 
                            	required data-bv-notempty-message="请选择测站名称">
							</select>
						</div>
						<label class="col-md-2 control-label"> 
                              <span class="text-danger">*&nbsp;</span> 曲线名称：
						</label>
						<div class="col-md-4 rowGroup">
                            <input type="text" class="form-control" id="lnnm_zqrl" 
                                   name="mZqrlFormBean.mZqrlInfoBean.lnnm" 
                                   data-bv-group=".rowGroup" 
                                   required
                                   maxlength="20"
                                   data-bv-notempty-message="曲线名称不能为空"   
                                   data-bv-stringlength-max="20" data-bv-stringlength-message="不能超过20个字"
                                placeholder="请输入曲线名称"
                            >
						</div>
					</div>
					<div class="form-group" style="margin-top: 10px;">
						<label class="col-md-2 control-label"> 
                              <span class="text-danger">*&nbsp;</span> 水位：
						</label>
						<div class="col-md-4 rowGroup">
							<input type="text" class="form-control" id="z_zqrl" 
                                   name="mZqrlFormBean.mZqrlInfoBean.z" 
                                   value="<s:property value='mZqrlFormBean.mZqrlInfoBean.z' />" 
                                   data-bv-group=".rowGroup" 
                                   required
                                   maxlength="7"
                                   data-bv-notempty-message="水位不能为空"   
                                   data-bv-stringlength-max="7" data-bv-stringlength-message="不能超过7个字"
                                placeholder="请输入水位"
                            >
						</div>
						<label class="col-md-2 control-label"> 
                              <span class="text-danger">*&nbsp;</span> 流量：
						</label>
						<div class="col-md-4 rowGroup">
							<input type="text" class="form-control" id="q_zqrl" 
                                   name="mZqrlFormBean.mZqrlInfoBean.q" 
                                   value="<s:property value='mZqrlFormBean.mZqrlInfoBean.q' />" 
                                   data-bv-group=".rowGroup" 
                                   required
                                   maxlength="9"
                                   data-bv-notempty-message="流量不能为空"   
                                   data-bv-stringlength-max="9" data-bv-stringlength-message="不能超过9个字"
                                placeholder="请输入流量"
                            >
						</div>
					</div>
					<div class="form-group" style="margin-top: 10px;">
						<label class="col-md-2 control-label"> 
                              <span class="text-danger">*&nbsp;</span> 备注：
						</label>
						<div class="col-md-10 rowGroup">
                            <textarea type="text" class="form-control" id="comments_zqrl" 
                                   name="mZqrlFormBean.mZqrlInfoBean.comments" 
                                   value="<s:property value='mZqrlFormBean.mZqrlInfoBean.comments' />" 
                                   data-bv-group=".rowGroup" 
                                   required
                                   maxlength="200"
                                   data-bv-notempty-message="备注不能为空"   
                                   data-bv-stringlength-max="200" data-bv-stringlength-message="不能超过200个字"
                                placeholder="请输入备注"></textarea>
						</div>
					</div>
				</div>
			</div>
				<div class="modal-footer"
					style="height: 40px;">
					<button type="submit" id="btn_save" class="btn btn-primary"
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



