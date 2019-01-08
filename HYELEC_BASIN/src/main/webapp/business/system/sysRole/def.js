﻿(function($) {
	$.System_Sysrole = function(option) {
		//option自定义参数覆盖
		//A、界面控件变量
		var opt_control={
			 btn_add		:$('#btn_add_role')		//增加按钮
			,btn_del     	:$('#btn_del_role')		//删除按钮	
			,btn_set_user	:$('#set_user')		    //设置用户按钮
			,btn_set_group	:$('#set_group')		//设置分组按钮
			,btn_ref     	:$('#btn_ref_role')		//刷新按钮
			,table 			:$('#tbinfo_role')		//BootStrapTable的ID
			,searchInput	:$('#searchName_role')	//模糊查询input
			,info_dialog	:$('#info_dialog_role')  // 新增和编辑对应的窗体，注意和info_form的区别
			,info_form		:$('#info_form_role')    // 新增和编辑对应的表单，注意和info_dialog的区别
            ,btn_save		:$('#btn_save_role')	 //保存按钮
            ,set_dialog		:$('#set_user_dialog')	 //设置用户窗体
            ,set_dialog_group:$('#set_dialog_group') //设置分组窗体
		}
		//$("body").append($("#info_dialog_role"));
		//C、请求地址URL
		var opt_url={
			 url_list		:basePath+"system/sysrole!list.action"			//查询数据URL
			,url_edit		:basePath+"system/sysrole!edit.action"			//编辑数据URL
			,url_save		:basePath+"system/sysrole!save.action"			//保存数据URL
			,url_remove		:basePath+"system/sysrole!removeids.action"		//删除数据URL
		}
		//全部变量，自定义的覆盖默认变量
		var opt_all=($.extend({},opt_control,opt_url,option));
		//////////////////////////////////////////////////////////////////////////////////////////////////
		//查询条件及页面分页信息设置
		var opt_tb_query={};
		this.Init_TB_QueryParms=function(opt){
			opt_tb_query=$.extend({},opt_tb_query,opt);
		}
		//D、BootStrapTable参数
		var opt_tb_base={
	      	  url			:opt_all.url_list   // 请求后台的URL（*）
	         ,queryParams	:queryParams		// 传递参数（*）
	         ,onDblClickRow	:onDblClickRow		// 行双击事件
	         ,onSort		:onSort             // 排序事件
             ,rowStyle		:comm_rowStyle		//行样式，可自定义
             ,onLoadSuccess	:comm_onLoadSuccess //数据加载错误	
		}
		function queryParams(params) {  // 配置参数
			//查询条件
		    var temp = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
		       "mSysRoleFormBean.pageBean.limit"	: params.limit   // 页面大小
		      ,"mSysRoleFormBean.pageBean.offset"	: params.offset  // 当前记录偏移条数
		      ,"mSysRoleFormBean.pageBean.sort"		: params.sort  // 排序列名
		      ,"mSysRoleFormBean.pageBean.sortOrder": params.order// 排位命令（desc，asc）
		      ,"mSysRoleFormBean.searchName"		: opt_control.searchInput.val() // 查询关键字
		    };
		    temp=$.extend({},temp,opt_tb_query);
		    return temp;
		}
		// 双击事件
		function onDblClickRow(row){
			if (row) {
				_edit(row.ID,true);
			}
		}
		// 排序事件
		function onSort(name, order){
			_refresh;
		}
		
		//全部BootStrapTable参数变量
		var opt_tb_all=$.extend({},g_bootstrapTable_Options,opt_tb_base);
		//公用函数，初始化BootStrapTable参数变量，自定义覆盖
		this.Init_TB_Option=function(opt){
			opt_tb_all=$.extend({},opt_tb_all,opt);
		}
		/////////////////////////////////////////////////////////////////////////////////////////////////
	    //加载主表数据，必须提供url_list，toolbar,opt-传递查询条件，可覆盖
		this.Init_TB_Data=function(){
			opt_all.table.bootstrapTable(opt_tb_all);
			// 提交查询函数
		}////end this.InitData
		////////////////////////////////////////////////////////////////////////////////////////////////////
		// 初始化增加、修改和删除,公开函数
		this.InitAddEditDel=function(opt){
			opt_all=$.extend({},opt_all,opt);
			opt_all.btn_add.bind("click",event_add);
			opt_all.btn_del.bind("click",event_del);
			opt_all.btn_ref.bind("click",event_ref);
			opt_all.btn_set_user.bind("click",event_user);
			opt_all.btn_set_group.bind("click",event_group);
			opt_all.info_form.bootstrapValidator().on("success.form.bv", function(e) {
				e.preventDefault(); // 去掉默认提交事件
				// 校验数据正确,执行保存数据
				_save();
				
			}).on("error.form.bv", function(e) {
				e.preventDefault(); // 去掉默认提交事件
				// //校验数据正确
			});	
		}
	    // 修改记录，调出窗体,公开函数
		this.edit=function(id,onlyread) {
			_edit(id);
		}
		//新增和编辑函数
		function _edit(id,onlyread){
			//加载Edit页面基本选择数据，成功后调用显示编辑页面
			//加载字典分类数据下拉框数据
			Load_EditSelectData( 
					function(){ 
						LoadEditData(id,onlyread);
					}
			);
		}
		//加载Edit页面上数据，并调出增加或编辑页面显示
		function LoadEditData(id,onlyread){
			var url = opt_all.url_edit+"?mSysRoleFormBean.mSysRoleInfoBean.id=" + id;
			// 动态加载页面数据
			common_ajax(null,url, function(response) {
				// 获取到数据，显示在界面上
				comm_loadFormData_flag(response.mSysRoleFormBean,"_role");
	            var title="信息维护";
				if (id==0) {
					title="<i class='icon icon-file-o'></i> 新增信息";
				}
				if (id!=0) title="<i class='icon icon-edit'></i> 编辑信息";
				
                
				//是否显示保存按钮
				if (onlyread){
					opt_all.btn_save.hide();
				} else {
					opt_all.btn_save.show();
				}
                
				opt_all.info_dialog.find('.modal-title').html(title) ;
				opt_all.info_dialog.modal({
					 show : true
					,backdrop : false // 背景遮挡
					,moveable : true
				}).on('shown.zui.modal', function() {
					reset_role();//当第一次验证正确后，关闭窗体。再进来时，重置窗体(保留窗体上数据)。
	            });
			});
		}
		
	    // 保存数据
		function _save() {
			// 参数需要保存的表单，保存url,需要更新的bootstrapTable,必须设置uniqueId: "id"
	 		var json=opt_all.info_form.serialize();
	 		//保存请求
	 		common_ajax(json, opt_all.url_save, function(response){
	 			//隐藏窗体
	 			opt_all.info_dialog.modal("hide");
	 			
	 			//为了保存后，同时更新表格中的外键字段名称
	            var info=response.mSysRoleFormBean;	
				var str;
				//取下拉框的文本后，去掉前面()，剩下的文本
	 			
			    // 增加数据
			    if ($('#id').val()==0){
			        // 增加表格数据，插入到第一位
			    	opt_all.table.bootstrapTable("prepend", upperJSONKey(info));
			    	_refresh();
			    } else {
			        // 修改表格数据，根据id修改
			    	opt_all.table.bootstrapTable("updateByUniqueId", {
		                id: info.ID,
		                row: upperJSONKey(info),
		                showRefresh:_refresh() //刷新表格
		            });	
	            }						    
	 		}); 	
		}
		
	    // 批量删除
		function _removeids(){
           // 获取删除选中的ids
           var ids=g_select_and_tip(opt_all.table,"ID");
           var roleCode=g_select_and_tip(opt_all.table,"ROLE_CODE");
           if(roleCode.indexOf("admins")!=-1){
        	   var msg = new $.zui.Messager("消息提示：系统管理员不能删除！！！", {placement: "center",type:"primary"});
			   msg.show();	
        	   return;
           }
           if (ids.length==0) return;
           
           var url= opt_all.url_remove+"?mSysRoleFormBean.ids="+ids
		   bootbox.confirm("确认需要批量删除选中的多条记录吗?", function(result) {
			   if(result){
			 		common_ajax(null, url, function(response){
						// 删除后，从后台取出返回数据
					    _refresh();
					    var msg = new $.zui.Messager("消息提示："+response.message, {placement: "center",type:"primary"});
					    msg.show();	
						    
			 		}); 
               }
			});  
		   
		}
		
		//设置用户
		function event_user(){
			var ids=g_select_and_tip(opt_all.table,"ID");
	        if (ids.length==0){
	        	return false;
	        }
	        if(ids.length>2){
	        	var msg = new $.zui.Messager("消息提示：请选择一条记录！！！", {placement: "center",type:"primary"});
			    msg.show();	
	        	return false;
	        }
	        $("#id_group").val(ids);
	        $("#mark").val("role");
	        var title="<i class='icon icon-user'></i> 设置用户信息";
			opt_all.set_dialog.find('.modal-title').html(title) ;
			opt_all.set_dialog.modal({
				 show : true
				,backdrop : false // 背景遮挡
				,moveable : true
			}).on('shown.zui.modal', function() {
				
            });
		}
		
		//设置分组
		function event_group(){
			var ids=g_select_and_tip(opt_all.table,"ID");
			if (ids.length==0){
	        	return false;
	        }
	        $("#id_role").val(ids);
	        var title="<i class='icon icon-user'></i> 设置分组信息";
			opt_all.set_dialog_group.find('.modal-title').html(title) ;
			opt_all.set_dialog_group.modal({
				 show : true
				,backdrop : false // 背景遮挡
				,moveable : true
			}).on('shown.zui.modal', function() {
				
            });
	        
		}
		
	 //////////////////////////////////////////////////////////////////
		//****绑定事件
		//绑定新增事件
		function event_add(){
			_edit(0);
		}
		//绑定刷新事件
		function event_ref(){
			opt_all.table.bootstrapTable('refresh');
		}
		//绑定删除事件
		function event_del(){
			_removeids();
		}
		//****绑定事件******end
		
	    //公开函数
	    this.refresh=function(){
	    	_refresh();
	    }
	    // 刷新
	    function _refresh(){
	    	opt_all.table.bootstrapTable('refresh');
	    } 
	    //公开函数
		this.reset=function(){
			_reset();
		}
		// 重置窗体
		function _reset(){
			opt_all.info_form.data('bootstrapValidator').resetForm(false);
		}
		//加载所有外键表到下拉框
		function Load_EditSelectData(callback){
			//所有编辑页面下拉框加载
			callback();
		}
	 };
})(jQuery);
