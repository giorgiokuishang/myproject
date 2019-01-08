<%@ page language="java" pageEncoding="UTF-8"%>
<style>
    #setid{
      float:left;
    }
   #sassid{
    
   width: 898px;
   
    margin-left: -22px;
    height: 45px;
    background: white;
   }
   #footid{
    background-color: white;
   
   }
   #footid{
     padding:0px;
   }
   #gropl{
    position: absolute;
    width: 100%;
    bottom: -56px;
    height: 57px;
    background:#e0f2f3;
    border-top: 1px solid #e5e5e5;
   }
   #btn_group_save{
   margin-top:12px;   
   }
   #areas{
    margin-right:22px;
    margin-top:12px;
   }
</style>
	<div class="modal fade" id="set_dialog_group">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span><span class="sr-only">关闭</span>
					</button>
					<h4 class="modal-title">信息维护-用户管理</h4>
				</div>
				
				<input type="hidden" id="id_role" name="id_role"/>
				<input type="hidden" id="mark" name="mark"/>
				<div class="modal-body" style="height:500px;overflow-y:auto;">
					<div id="maincontent" class="row-fluid">
						<div class="row-fluid col-md-12">
						  <div id="setid">
							<div class="btn-toolbar">
								<div class="btn-group pull-right col-lg-4 col-md-4 col-sm-4 col-xs-6">
									<div class="input-group ">
										<input type="text" id="searchName_group" class="form-control"
											placeholder="输入关键字进行模糊查询"><span class="input-group-btn">
											<button class="btn" id="btn_ref_group" type="button">
												<div class="visible-md visible-lg">
													<i class="icon icon-search"></i>&nbsp;查询
												</div>
												<div class="visible-xs visible-sm">
													<i class="icon icon-search"></i>
												</div>
											</button>
										</span>
									</div>
								</div>
							</div>
						</div>
						<div id="sassid" style="margin-left: -15px;"></div>
							<!-- 按钮工具条结束 -->
							<table id="tbinfo_group" class="table-condensed table-hover">
								<thead>
									<tr>
										<th data-halign="center" data-align="center"
											data-sortable="false" data-field="state" data-checkbox="true"
											data-formatter="FMT_Check_staff"></th>
										<th data-halign="center" data-align="center"
											data-sortable="false" data-width="60" data-formatter="FMT_Num_staff">
											序号</th>
										<th data-halign="center" data-align="center" data-sortable="true"
											data-field="GROUP_CODE" data-width="200">分组编码</th>
										<th data-halign="center" data-align="center" data-sortable="true"
											data-field="GROUP_NAME" data-width="200">分组名称</th>
										<!-- <th data-halign="center" data-align="center"
											data-sortable="false" data-width="80" 
											data-field="STATE">状态</th> -->
									</tr>
								</thead>
							</table>
						</div>
					</div>
				</div>
				<div class="modal-footer" id="footid">
				<div id="gropl"> 
					<button type="button" id="btn_group_save" class="btn btn-primary" >
						<i class="icon icon-save"></i> 保存
					</button>
					<button type="button" class="btn" id="areas" data-dismiss="modal">
						<i class="icon icon-signout"></i> 关闭
					</button>
				</div>	
				</div>
			</div>
		</div>
	</div>
<script src="../business/system/sysGroup/def.js"></script>
<script src="../business/system/sysGroup/list.js"></script>