<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<style>
	.form-group{
		margin-bottom: -4px;
	}
.className{
    width:270px;
    height:150px;
    margin:0 auto;
}
</style>
<form id="modellist_form" name="info_form" class="form-horizontal" >
	<div class="modal fade" id="cjfa_fxjs">
		<div class="modal-dialog modal-lg" style="width:600px">
			<div class="modal-content">
				<div class="modal-header" style="height: 40px">
					<button type="button" class="btn btn-link close"
						data-dismiss="modal">
						<span aria-hidden="true"><i class="icon icon-times"></i></span><span class="sr-only">关闭</span>
					</button>
					<h4 class="modal-title" style="line-height: 15px;"><i class='icon icon-plus'></i>新增测站信息</h4>
				</div>

				<div class="modal-body" style="padding-top: 8px;">
					<div class="flag" style="margin-top: 10px;margin-left: -70px;">
						<div id="cjfa_main" class="className" style="width:600px;height:300px;margin-left: 60px;">
							
						</div>
					</div>
				</div>
				<div class="modal-footer"
					style="height: 40px;">
					<button type="reset" class="btn btn-large"
						data-dismiss="modal"
						style="margin-top: -14px; margin-right: -5px;">
						<i class="icon icon-times"></i> 取消
					</button>
				</div>
			</div>
		</div>
	</div>
</form>