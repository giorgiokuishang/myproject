﻿(function($) {
	$.System_SysMenu = function(option) {
		//option自定义参数覆盖
		//A、界面控件变量
		var opt_control={
			 btn_add		:$('#btn_add')		//增加按钮
			,btn_del     	:$('#btn_del')		//删除按钮	
			,btn_ref     	:$('#btn_ref')		//刷新按钮
			,searchInput	:$('#searchName')	//模糊查询input
			,info_dialog	:$('#info_dialog')  // 新增和编辑对应的窗体，注意和info_form的区别
			,info_form		:$('#info_form')    // 新增和编辑对应的表单，注意和info_dialog的区别
			,treeGrid 		:$('#treeGrid')		//BootStrapTreeList的ID--
			,select_pcode	:$('#select_pcode') //选中根节点--
			,btn_basedata	:$('#btn_basedata') //刷新根节点下拉框数据按钮
			,btn_save		:$('#btn_sysmenu_save')		//保存按钮
		}
		//C、请求地址URL
		var opt_url={
			 url_list		:basePath+"system/sysmenu!list.action"			        //查询数据URL
			,url_add		:basePath+"system/sysmenu!initChildTreeFormData.action"	//新增子节点数据URL--
			,url_edit		:basePath+"system/sysmenu!initRootTreeFormData.action"	//编辑根节点数据URL
			,url_save		:basePath+"system/sysmenu!saveTreeFormData.action"	    //保存数据URL
			,url_remove		:basePath+"system/sysmenu!removeIds.action"		    	//删除数据URL
			,url_listroot   :basePath+"system/sysmenu!listroot.action"		    	//获取根数据URL--
		}

		//全部变量，自定义的覆盖默认变量
		var opt_all=($.extend(opt_control,opt_url,option));
	    //////////////////////////////////////////////////////////////////////////////////////////////////
		// 动态加载页面基础数据：下拉框数据
		function _LoadBaseData(){
			//加载根单位下拉框数据
			common_ajax(null,opt_all.url_listroot, function(response) {
				opt_all.select_pcode.empty();
				opt_all.select_pcode.append($("<option>").text("显示全部数据").val(""));
				var rows=response.rows;
				for (var i = 0 ; i< rows.length;i++){
					var option = $("<option>").text("("+rows[i].SCODE+")"+rows[i].MENU_NAME).val(rows[i].SCODE);
					opt_all.select_pcode.append(option);
				}
                comm_chose_init(opt_all.select_pcode);
			});
		}
        
		//flag状态
		function FMT_Flag(value) {
		    var html="";
		    if (value==0){html="未生效"}
		    if (value==1){html="生效"}
		   return html;
		}
        
	    //加载treegrid数据
		function _LoadData(opt){
		    var temp = {   
				  "mSysMenuFormBean.pageBean.limit": 100000000   // 页面大小
				 ,"mSysMenuFormBean.pageBean.offset": 0  // 当前记录偏移条数	
		 	      // 在此增加查询条件
				 ,"mSysMenuFormBean.mSysMenuInfoBean.fCode":opt_all.select_pcode.val()
                 ,"mSysMenuFormBean.mSysMenuInfoBean.state"	:""
		 	    };
		    
		    temp=$.extend(temp,opt);
			common_ajax(temp,opt_all.url_list, function(response) {
				var html=[];
				opt_all.treeGrid.html("");
				html.push("<thead><tr>");
				html.push("<th>系统菜单名称</th>");
				html.push("<th width='100'>系统菜单编码</th>");
				html.push("<th width='50'>图标</td>");
				html.push("<th class='visible-md visible-lg' width='50'>url</td>");
				html.push("<th class='visible-md visible-lg' width='50'>状态</th>");
				html.push("<th width='100'>操作</th>");
				html.push("</tr></thead>");	
			/**内容*/
			var rows=response.rows;
				for (var i = 0 ; i< rows.length;i++){
					html.push(" <tr class='treegrid-"+rows[i].SCODE);
					if (rows[i].SUPER_CODE.length>0){
						html.push(" treegrid-parent-"+rows[i].SUPER_CODE);
					}
						html.push("' ondblclick='_edit("+rows[i].MENU_CODE+",true)'>");
						html.push("   <td>"+rows[i].MENU_NAME+"</td>");
						html.push("   <td>"+rows[i].FCODE+"</td>");
						html.push("   <td align='center' ><i class='icon "+rows[i].MENU_ICON+"'></i></td>");
						html.push("   <td class='visible-md visible-lg'>"+rows[i].MENU_URL+"</td>");
						html.push("   <td class='visible-md visible-lg'>"+FMT_Flag(rows[i].STATE)+"</td>");
						html.push("   <td>");
						html.push("&nbsp;<a href='#' title='新增子项' onclick='_add("+JSON.stringify(rows[i].FCODE)+")'><i class='icon icon-node'></i></a>");
						html.push("&nbsp;<a href='#' title='编辑信息，或在数据行上双击鼠标左键。' onclick='_edit("+JSON.stringify(rows[i].FCODE)+")'><i class='icon icon-edit'></i></a>");
						html.push("&nbsp;<a href='#' title='删除本项及所有子项' onclick='_removeids("+rows[i].FCODE+")'><i class='icon icon-remove'></i></a>");
						html.push("   </td>");
						html.push(" </tr>");
				}
				
				opt_all.treeGrid.append(html.join(""));
				
				opt_all.treeGrid.addClass("table");
				opt_all.treeGrid.addClass("table-striped");
				opt_all.treeGrid.addClass("table-bordered");
				opt_all.treeGrid.addClass("table-condensed");//更为紧凑
				
				if ($("#select_pcode").val()==""){
					opt_all.treeGrid.treegrid({
						 initialState:'collapsed'
						,expanderExpandedClass:  'icon icon-minus'
			            ,expanderCollapsedClass: 'icon icon-plus'
			        });				
				} else {
					opt_all.treeGrid.treegrid({
						 initialState:'expanded'
						,expanderExpandedClass:  'icon icon-minus'
			            ,expanderCollapsedClass: 'icon icon-plus'
			        });	
				}
		});
	}
		
	    //加载主表数据，必须提供url_list，toolbar,opt-传递查询条件，可覆盖
		this.InitData=function(opt){
			opt_all=$.extend(opt_all,opt);
			_LoadBaseData();
			_LoadData(opt);
			
		}////end this.InitData
		////////////////////////////////////////////////////////////////////////////////////////////////////
		// 初始化增加、修改和删除,公开函数
		this.InitAddEditDel=function(opt){
			opt_all=$.extend(opt_all,opt);
			opt_all.btn_add.bind("click",event_add);
			opt_all.btn_ref.bind("click",event_ref);
			opt_all.btn_basedata.bind("click",event_basedata);
			
			// 保存button类型为submit
			opt_all.info_form.bootstrapValidator().on("success.form.bv", function(e) {
				e.preventDefault(); // 去掉默认提交事件
				// 校验数据正确,执行保存数据
				_save();
				
			}).on("error.form.bv", function(e) {
				e.preventDefault(); // 去掉默认提交事件
				// //校验数据正确
			});	
		}
	    // 新增子记录，调出窗体,公开函数
		this.add=function(id) {
			_add(id);
		}
		//新增子记录函数
		function _add(id){
			//加载页面基本选择数据，成功后调用显示新增页面
			//加载字典分类数据下拉框数据
			Load_EditSelectData( 
					function(){ 
						LoadAddData(id);
					}
			);
		}		
		
	    // 修改记录，调出窗体,公开函数
		this.edit=function(id,onlyread) {
			_edit(id,onlyread);
		}
		//编辑函数
		function _edit(id,onlyread){
			//加载Edit页面基本选择数据，成功后调用显示编辑页面
			//加载字典分类数据下拉框数据
			Load_EditSelectData( 
					function(){ 
						LoadEditData(id,onlyread);
					}
			);
		}
		
		//增加或修改时，加载父节点信息pdata对象数据到界面
		function showFormDataP(pdata,id){
			$('#superCode_menu_').val(pdata.fCode);
			if (pdata.fCode.length>0){
				$('#pname_menu_').val("("+pdata.fCode+")"+pdata.menuName);
			} else {
				$('#pname_menu_').val("根节点");
			}
		}
		
		//加载Edit页面上数据，并调出增加或编辑页面显示
		function LoadAddData(id){
			$("#ids_menu_").val(id);
			var url = opt_all.url_add+"?mSysMenuFormBean.mSysMenuInfoBean.fCode=" + id;
			// 动态加载页面数据
			common_ajax(null,url, function(response) {
				// 获取到数据，显示在界面上
				comm_loadFormData_flag(response.mSysMenuInfoBean,"_menu_");//显示本级数据
				showFormDataP(response.mPSysMenuInfoBean,id);   //显示父节点数据
				
	            var title="<i class='icon icon-file-o'></i> 新增子信息";
				opt_all.info_dialog.find('.modal-title').html(title) ;
				opt_all.btn_save.show();
				
				opt_all.info_dialog.modal({
					 show : true
					,backdrop : false // 背景遮挡
					,moveable : true
				}).on('shown.zui.modal', function() {
					 _reset();   // 当第一次验证正确后，关闭窗体。再进来时，重置窗体(保留窗体上数据)。
	            });
			});
		}
		//加载Edit页面上数据，并调出增加或编辑页面显示
		function LoadEditData(id,onlyread){
			var url = opt_all.url_edit+"?mSysMenuFormBean.mSysMenuInfoBean.fCode=" + id;
			// 动态加载页面数据
			common_ajax(null,url, function(response) {
				// 获取到数据，显示在界面上
				comm_loadFormData_flag(response.mSysMenuInfoBean,"_menu_");//显示本级数据
				showFormDataP(response.mPSysMenuInfoBean,id);   //显示父节点数据
	            var title="信息维护";
				if (id==0) {
					title="<i class='icon icon-file-o'></i> 新增根信息";
				}
				if (id!=0) title="<i class='icon icon-edit'></i> 编辑信息";
                
				opt_all.info_dialog.find('.modal-title').html(title) ;
				//是否显示保存按钮
				if (onlyread){
					opt_all.btn_save.hide();
				} else {
					opt_all.btn_save.show();
				}
				
				opt_all.info_dialog.modal({
					 show : true
					,backdrop : false // 背景遮挡
					,moveable : true
				}).on('shown.zui.modal', function() {
					 _reset();   // 当第一次验证正确后，关闭窗体。再进来时，重置窗体(保留窗体上数据)。
	            });
			});
		}
	    // 保存数据
		function _save() {
			//设置本级全编码
			$('#fCode_menu_').val($('#superCode_menu_').val()+$('#sCode_menu_').val());	
			// 参数需要保存的表单，保存url,需要更新的bootstrapTable,必须设置uniqueId: "id"
	 		var json=opt_all.info_form.serialize();
	 		common_ajax(json, opt_all.url_save, function(response){
 				$('#info_dialog').modal("hide");
 				_refresh();						    
	 		});	
		}
		//公开函数删除
		this.removeids=function(id){
			_removeids(id);
		}
	    // 批量删除
		function _removeids(id){
	           var url= opt_all.url_remove+"?mSysMenuFormBean.ids="+id
				bootbox.confirm("确认需要删除本级及所有子项记录吗?", function(result) {
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
		
	 //////////////////////////////////////////////////////////////////
		//****绑定事件
		//绑定新增事件
		function event_add(){
			_edit(0);
		}
		//绑定刷新事件
		function event_ref(){
			 _refresh();
		}
		
		function event_basedata(){
			_LoadBaseData();
		}
		
		//****绑定事件******end
		
		this.refresh=function(){
			_refresh();
		}
        
	    // 刷新
	    function _refresh(){
	    	_LoadData();
	    }  
	    
		// 重置窗体
		function _reset(){
			opt_all.info_form.data('bootstrapValidator').resetForm(false);
		}
		//公开函数
		this.reset=function(){
			_reset();
		}
		/////////////////////////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////////////
		//加载所有外键表到下拉框，无
		function Load_EditSelectData(callback){
			//所有编辑页面下拉框加载
			callback();
		}
		/////////////////////////////////////////////////////////////////////////////////
	};
})(jQuery);
			
