<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<style>
	.form-group{
		margin-bottom: -4px;
	}
	th{
	white-space: nowrap;
	}
	input[type=checkbox], input[type=radio]{
	margin: 2px 0 0;
	}
	.c2{
	width:110%;
	margin-right: 10px;
	}
	
	.tableA{
	margin-left: 20px;
	}
	
</style>
<form id="modellist_form" name="info_form" class="form-horizontal"
	autocomplete="off" method="post"
	data-bv-message="This value is not valid"
	data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
	data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
	data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">

	<div class="modal fade" id="modellist_dialog">
		<div class="modal-dialog modal-lg" style="width: 900px">
			<div class="modal-content">
				<div class="modal-header" style="height: 40px">
					<button type="button" class="btn btn-link close"
						data-dismiss="modal">
						<span aria-hidden="true"><i class="icon icon-times"></i></span><span class="sr-only">关闭</span>
					</button>
					<h4 class="modal-title" style="line-height: 15px;"><i class='icon icon-plus'></i>新增测站信息</h4>
				</div>

				<div class="modal-body" style="padding-top: 8px;">
					<div style="display: none;">
						id<input type="text" class="form-control"  id="modelCode_modelinfo"
							name="ModelInfoFormBean.ModelInfoFormBean.modelCode">
						
					</div>
					<div class="form-group" style="margin-top: 10px;margin-left: -70px;">
						<label class="col-md-2 control-label"> 
                              <span class="text-danger">*&nbsp;</span> 模型名称：
						</label>
						<div class="col-md-4 rowGroup">
                            <input type="text" class="form-control" id="modelName_modelinfo" 
                                   name="ModelInfoFormBean.ModelInfoFormBean.modelName" 
                                   value="<s:property value='ModelInfoFormBean.ModelInfoFormBean.modelName' />" 
                                   data-bv-group=".rowGroup" 
                                   required
                                   maxlength="25"
                                   data-bv-notempty-message="模型名称不能为空"   
                                   data-bv-stringlength-max="50" data-bv-stringlength-message="不能超过25个字"
                                placeholder="请输入模型名称"
                            >
						</div>
						<label class="col-md-2 control-label"> 
                              <span class="text-danger">*&nbsp;</span> 所属分类：
						</label>
						<div class="col-md-4 rowGroup">
							<select name="ModelInfoFormBean.ModelInfoFormBean.modelType" class="form-control" 
								id="modelType_modelinfo"
                            	data-bv-group=".rowGroup" 
                            	required data-bv-notempty-message="请选择分类">
                            	<option>请选择分类</option>
                            	<option value="1"  >汇流模型</option>
                            	<option value="2" >产流模型</option>
							</select>
						</div>
					</div>
					<div class="form-group" style="margin-top: 10px;margin-left: -70px;">
						<label class="col-md-2 control-label"> 
                              <span class="text-danger">&nbsp;</span> 描述：
						</label>
						<div class="col-md-10 rowGroup">
                            <textarea type="text" class="form-control" id="remark_modelinfo" 
                                   name="ModelInfoFormBean.ModelInfoFormBean.remark" 
                                   value="<s:property value='ModelInfoFormBean.ModelInfoFormBean.remark' />" 
                                   data-bv-group=".rowGroup" 
                                   maxlength="25"
                                   data-bv-notempty-message="描述不能为空"   
                                   data-bv-stringlength-max="50" data-bv-stringlength-message="不能超过25个字"
                                placeholder="请输入描述"></textarea>
						</div>
					</div>
					<div class="form-group" style="margin-top: 10px;margin-left: -70px;">
						<label class="col-md-2 control-label"> 
                              <span>&nbsp;</span> 输入参数：
						</label>&nbsp;&nbsp;
						<div class="col-md-4 rowGroup">
							<button type="button" id="add_cs" class="btn btn-primary">
								   <div class="visible-md visible-lg"><i class="icon icon-plus-sign"></i>&nbsp;新增参数</div>
								   <div class="visible-xs visible-sm"><i class="icon icon-file-o"></i></div>
							</button>
						</div>
					</div>
					
					<div class="form-group" style="margin-top: 10px;">
						 <table id="query_cs" class="col-md-10 control-label"> 
					        <thead>
					            
					  
					        </thead>
					    </table>
					   
					</div>
				</div>
				<div class="modal-footer"style="height: 40px;">
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
	</div>
</form>