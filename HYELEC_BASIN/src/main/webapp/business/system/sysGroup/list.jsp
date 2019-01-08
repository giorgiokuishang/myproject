﻿<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<div style="height:100%;">
		<div class="row-fluid" style="height:30px;">
			<h3 class="text-primary">
				<ol class="breadcrumb">
					<li>功能列表</li>
					<li class="active">分组管理</li>
				</ol>
			</h3>
		</div>
		<div style="height:calc(100% - 30px);width:100%;">
			<div class="row-fluid col-md-12" style="height:40px;">
				<!-- 按钮工具条开始 -->
				<div id="tbar" class="btn-toolbar">
					<div class="btn-group">
						<button type="button" id="btn_add_group" class="btn btn-primary">
							<div class="visible-md visible-lg">
								<i class="icon icon-file-o"></i>&nbsp;新增
							</div>
							<div class="visible-xs visible-sm">
								<i class="icon icon-file-o"></i>
							</div>
						</button>
					</div>
					<div class="btn-group">
						<button type="button" id="btn_set_user" class="btn btn-primary">
							<div class="visible-md visible-lg">
								<i class="icon icon-user"></i>&nbsp;设置用户
							</div>
							<div class="visible-xs visible-sm">
								<i class="icon icon-user"></i>
							</div>
						</button>
					</div>
			<!-- 		<div class="btn-group">
						<button type="button" id="btn_set_basin" class="btn btn-primary">
							<div class="visible-md visible-lg">
								<i class="icon icon-user"></i>&nbsp;设置流域
							</div>
							<div class="visible-xs visible-sm">
								<i class="icon icon-user"></i>
							</div>
						</button>
					</div> -->
					<div class="btn-group">
						<button type="button" id="btn_del_group" class="btn btn-primary">
							<div class="visible-md visible-lg">
								<i class="icon icon-times"></i>&nbsp;删除
							</div>
							<div class="visible-xs visible-sm">
								<i class="icon icon-times"></i>
							</div>
						</button>
					</div>

					<div
						class="btn-group pull-right col-lg-4 col-md-4 col-sm-4 col-xs-6">
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
				<!-- 按钮工具条结束 -->
			<div id="tbinfo_group_div" style="width:100%;height:calc(100% - 100px);">
					<table id="tbinfo_group" class="table-condensed table-hover">
						<thead>
							<tr>
								<th data-halign="center" data-align="center"
									data-sortable="false" data-field="state" data-checkbox="true"
									data-formatter="FMT_Check_group"></th>
								<th data-halign="center" data-align="center"
									data-sortable="false" data-width="60" data-formatter="FMT_Num_group">
									序号</th>
								<th data-halign="center" data-align="center" data-sortable="true"
									data-field="GROUP_CODE" data-width="200">分组编码</th>
								<th data-halign="center" data-align="center" data-sortable="true"
									data-field="GROUP_NAME" data-width="200">分组名称</th>
							<!-- 	<th data-sortable="false" data-width="50" data-field="RVNM_NAME"
									data-formatter="FMT_rvnmName">已分配流域</th> -->
								<th data-halign="center" data-align="center" data-sortable="false"
									data-field="STAFF_NAME" 
									data-width="300">已分配人员</th>
								<!-- <th data-halign="center" data-align="center" data-sortable="true"
									data-field="REMARK" class="visible-md visible-lg">备注</th> -->
								<th data-halign="center" data-align="center"
									data-sortable="false" data-width="50" 
									data-formatter="FMT_Oper_group">操作</th>
							</tr>
						</thead>
					</table>
				</div>
		</div>
	</div>
<!-- 不要改变以下引用顺序 -->
<%@include file="edit.inc"%>
<%@include file="setUser.jsp"%>
<%@include file="setBasin.jsp"%>
<script src="../business/system/sysGroup/def.js"></script>
<script src="../business/system/sysGroup/list.js"></script>
<script src="../business/system/sysBasin/tree.js"></script>
