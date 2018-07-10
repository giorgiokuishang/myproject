(function($) {
	//当前工程内码
	var engineeringNm = sessionStorage.getItem("engineeringNm");
	$.FloodTran_Query = function(option) {
		//option自定义参数覆盖
		//A、界面控件变量
		var opt_control={
			query_table_floodTran 	:$('#query_table_floodTran')		//页面BootStrapTable的ID
			,query_searchName		:$('#query_searchName')	//页面模糊查询input
			,query_info_floodTran	:$("#query_info_floodTran")	//双击事件窗口id
			,query_ref				:$("#query_ref")		//模糊查询
			,query_form				:$("#query_form")
			,edit_dialog_floodTran	:$("#edit_dialog_floodTran")		//添加编辑窗口
			,query_add_floodTran	:$("#query_add_floodTran")//添加按钮
			,btn_del_floodTran		:$("#btn_del_floodTran")//批量删除按钮
			,info_form_floodTran	:$("#info_form_floodTran")//添加编辑的表单
			,info_upload			:$("#info_upload_floodTran")	//文件上传模态框
			,btn_into_pptn			:$("#btn_into_pptn")	//导入按钮
			,btn_outAll_pptn	    :$('#btn_outAll_pptn') //导出全部按钮
			,btn_outPage_pptn		:$('#btn_outPage_pptn') //导出当前页按钮
			,btn_save				:$('#btn_save')		//保存按钮
			,btn_upstcd_fsdr		:$('#upstcd_fsdr')		//上游下拉框
			,btn_dwstcd_fsdr		:$('#dwstcd_fsdr')		//下游下拉框
		}
		
		//B、请求地址URL
		var opt_url={
			url_list			:basePath + "fsdr/fsdr!list.action"//查询数据URL
			,url_save			:basePath + "fsdr/fsdr!save.action"//添加或修改保存洪水传播时间表URL
			,url_remove			:basePath + "fsdr/fsdr!removeids.action"//删除URL
			,url_edit			:basePath + "fsdr/fsdr!edit.action"//修改查询数据
			,url_import			:basePath + "fsdr/fsdr!importPptn.action"//导入
			,url_export			:basePath + "fsdr/fsdr!export.action"//导出
			,url_upstcd_Stbprp    :basePath + "stbprp/stbprp!upstcd_Stbprp.action"//上游编码
		}
		//全部变量，自定义的覆盖默认变量
		var opt_all=($.extend(opt_control,opt_url,option));
	    //////////////////////////////////////////////////////////////////////////////////////////////////
		this.InitData=function(opt){
			opt_all=$.extend(opt_all,opt);   
			opt_all.query_table_floodTran.bootstrapTable($.extend(g_bootstrapTable_Options,
				{
				      url			 	:opt_all.url_list   // 请求后台的URL（*）
			         ,queryParams	  	:queryParams		// 传递参数（*）
			         ,onDblClickRow 	:onDblClickRow		// 行双击事件
			         ,onSort			:onSort             // 排序事件
		             ,rowStyle			:comm_rowStyle		//行样式，可自定义
		             ,onLoadSuccess		:comm_onLoadSuccess //数据加载错误
		             ,onCheckAll 		:onCheckAll   //全选
		             ,onCheck  			:onCheck   //单选
		             ,onUncheck  		:onUncheck   //不选
		             ,onUncheckAll 		:onUncheckAll  //全不选
		             ,singleSelect		:false
		             ,uniqueId			:"ID"
		             ,pageSize:15
				}));
			//选中多行改变表格背景色
			   function onCheckAll(rows){
			    for(var i=0;i<rows.length;i++){
			     commRowStyle(i);
			    }
			   }
			   //循环改变所有行颜色
			   function commRowStyle(i){
			    $("#query_table_floodTran tbody tr[data-index="+i+"]").addClass("success");
			   }
			   //全不选时颜色恢复
			   function onUncheckAll(){
			    $("#query_table_floodTran tbody tr").removeClass("success");
			   }
			   //选中一行改变表格背景色
			   function onCheck(rows){
			    $("#query_table_floodTran tbody tr[data-uniqueid="+rows.id+"]").addClass("success");
			   }
			   //不选中时颜色恢复
			   function onUncheck(rows){
			    $("#query_table_floodTran tbody tr[data-uniqueid="+rows.id+"]").removeClass("success");
			   }
			// 提交查询函数
			function queryParams(params) {  // 配置参数
				//查询条件
				TableQueryParams = params;
				var opt_parms={
					 "mFsdrFormBean.searchName"			:opt_control.query_searchName.val() // 查询关键字
				};
			    var temp = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			       "mFsdrFormBean.pageBean.limit"			: params.limit   // 页面大小
			      ,"mFsdrFormBean.pageBean.offset"			: params.offset  // 当前记录偏移条数
			      ,"mFsdrFormBean.pageBean.sort"				: params.sort  	  // 排序列名
			      ,"mFsdrFormBean.pageBean.sortOrder" 		: params.order   // 排位命令（desc，asc）
			    };
			    temp=$.extend(temp,opt_parms,opt);
				return temp;
			}
			// 双击事件
			function onDblClickRow(row){
				if (row) {
					_table(row.UPSTCD);
				}
			}
			// 排序事件
			function onSort(name, order){
				_refresh();
			}
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		// 初始化增加、修改和删除,公开函数
		this.InitAddEditDel=function(opt){
			opt_all=$.extend(opt_all,opt);
			opt_all.query_add_floodTran.bind("click",event_add);
			opt_all.btn_del_floodTran.bind("click",event_del);
			opt_all.query_ref.bind("click",event_ref);
			opt_all.btn_into_pptn.bind("click",upload_model_show);
			opt_all.btn_outPage_pptn.bind("click",exportPagePptn);
			opt_all.btn_outAll_pptn.bind("click",exportAllPptn);
			
			// 保存button类型为submit
			opt_all.info_form_floodTran.bootstrapValidator().on("success.form.bv", function(e) {
				e.preventDefault(); // 去掉默认提交事件
				// 校验数据正确,执行保存数据
				_save();
				
			}).on("error.form.bv", function(e) {
				e.preventDefault(); // 去掉默认提交事件
				// //校验数据正确
			});	
		}
		//下拉数据初始化
		this.InitSelect = function (){
			Load_select_upstcd_fsdr();
			Load_select_dwstcd_fsdr();
		}
		
		function Load_select_upstcd_fsdr(){
			opt_control.btn_upstcd_fsdr.empty();
			var url = opt_all.url_upstcd_Stbprp;
			opt_control.btn_upstcd_fsdr.append("<option value=''>请选择上游名称</option>");
			common_ajax(null,url, function(data) {
				$.each(data.rows, function(i) {
					opt_control.btn_upstcd_fsdr.append(
						'<option value=' + data.rows[i].STCD + '>'+ data.rows[i].STNM + '</option>');
				});
			});
		}
		
		function Load_select_dwstcd_fsdr(){
			opt_control.btn_dwstcd_fsdr.empty();
			var url = opt_all.url_upstcd_Stbprp;
			opt_control.btn_dwstcd_fsdr.append("<option value=''>请选择下游名称</option>");
			common_ajax(null,url, function(data) {
				$.each(data.rows, function(i) {
					opt_control.btn_dwstcd_fsdr.append(
						'<option value=' + data.rows[i].STCD + '>'+ data.rows[i].STNM + '</option>');
				});
			});
		}
		
	    // 修改记录，调出窗体,公开函数
		this.edit=function(upstcd,dwstcd,onlyread) {
			_edit(upstcd,dwstcd);
		}
		//单条删除
		this.del=function(upstcd,dwstcd){
			if(upstcd != "" && dwstcd != ""){
				_del(upstcd,dwstcd);
			}
		}
		//绑定删除事件
		function event_del(){
			_removeids();
		}
		// 批量删除
		function _removeids(){
           var ids = g_select_and_tip(opt_all.query_table_floodTran,"UPSTCD");
           var ids_ = g_select_and_tip(opt_all.query_table_floodTran,"DWSTCD");
           if (ids.length==0 && ids_.length==0) return;
           
           $(".fixed-table-container tbody tr.selected td").addClass("row-bcground");
           
           var url= opt_all.url_remove+"?mFsdrFormBean.mFsdrInfoBean.upstcd="+ids+"&mFsdrFormBean.mFsdrInfoBean.dwstcd="+ids_
           confirm("资料管理","您确定要删除这些记录吗？","icon-remove-sign", function(result) {
			   if(result){
			 		common_ajax(null, url, function(response){
					    _refresh();
					    var msg = new $.zui.Messager("消息提示："+response.message, {placement: "center",type:"primary"});
					    msg.show();	
						    
			 		}); 
               }
			});    
		}
		
		//删除单条记录
		function _del(upstcd,dwstcd){
			var url= opt_all.url_remove+"?mFsdrFormBean.mFsdrInfoBean.upstcd="+upstcd+"&mFsdrFormBean.mFsdrInfoBean.dwstcd="+dwstcd
			$(".table tbody tr[data-uniqueid="+upstcd+"] td").addClass("row-bcground");
			confirm("资料管理","您确定要删除该条记录吗？","icon-remove-sign", function(result) {
			   if(result){
			 		common_ajax(null, url, function(response){
					    _refresh();
					    var msg = new $.zui.Messager("消息提示："+response.message, {placement: "center",type:"primary"});
					    msg.show();	
			 		}); 
               }
			}); 
		}
		
		//新增和编辑函数
		function _edit(upstcd,dwstcd,onlyread){
			//加载Edit页面基本选择数据，成功后调用显示编辑页面
			//加载字典分类数据下拉框数据
			Load_EditSelectData( 
				function(){ 
					LoadEditData(upstcd,dwstcd,onlyread);
				}
			);
		}
		
		//加载Edit页面上数据，并调出增加或编辑页面显示
		function LoadEditData(upstcd,dwstcd,onlyread){
			if(upstcd!="" && dwstcd!=""){
				var url = opt_all.url_edit+"?mFsdrFormBean.mFsdrInfoBean.upstcd=" + upstcd+"&mFsdrFormBean.mFsdrInfoBean.dwstcd="+dwstcd;
				// 动态加载页面数据
				common_ajax(null,url, function(response) {
				
					$('#upstcd_fsdr').val(response.mFsdrFormBean.upstcd);
					$('#dwstcd_fsdr').val(response.mFsdrFormBean.dwstcd);
					
					comm_loadFormData_flag(response.mFsdrFormBean,"_fsdr");//显示洪水传播表信息
					
					$('#upstcd_fsdr').prop('disabled',true);
					$('#dwstcd_fsdr').prop('disabled',true);
					
					opt_all.edit_dialog_floodTran.find('.modal-title').html("<i class='icon icon-pencil'></i>&nbsp;修改洪水传播时间");
					opt_all.edit_dialog_floodTran.modal({
						 show : true
						,backdrop : "static" // 背景遮挡
						,moveable : true
					}).on('hide.zui.modal', function() {
						$('#upstcd_fsdr').val('');
						$('#dwstcd_fsdr').val('');
						_reset();//当第一次验证正确后，关闭窗体。再进来时，重置窗体(保留窗体上数据)。
		            });
				});
			}else{
				$('#upstcd_fsdr').prop('disabled',false);
				$('#dwstcd_fsdr').prop('disabled',false);
				
				opt_all.edit_dialog_floodTran.find('.modal-title').html("<i class='icon icon-plus-sign'></i>&nbsp;新增洪水传播时间") ;
				opt_all.edit_dialog_floodTran.modal({
					 show : true
					,backdrop : "static" // 背景遮挡
					,moveable : true
				}).on('hide.zui.modal', function() {
					_reset();//当第一次验证正确后，关闭窗体。再进来时，重置窗体(保留窗体上数据)。
	            });
			}
		}
		
		// 保存洪水传播时间表数据
		function _save() {
			$('#upstcd_fsdr_').val('');
			$('#dwstcd_fsdr_').val('');
			
			$('#upstcd_fsdr_').val($('#upstcd_fsdr').val());
			$('#dwstcd_fsdr_').val($('#dwstcd_fsdr').val());
			
			// 参数需要保存的表单，保存url,需要更新的bootstrapTable,必须设置uniqueId: "id"
	 		var json=opt_all.info_form_floodTran.serialize();
	 		common_ajax(json, opt_all.url_save, function(response){
	 			opt_all.edit_dialog_floodTran.modal("hide");
			    //重置表单
			    _reset();
			    // 刷新列表
			    _refresh();
	 		});
		}
		
		//显示详情
		function _table(queryId){
			var url = opt_all.url_list+"?mFsdrFormBean.mFsdrInfoBean.upstcd=" + queryId;
			common_ajax(null,url, function(response) {
				for(var key in response.rows[0]){
					   if($("#"+key+"_pptnDetail")[0]){
						   $("#"+key+"_pptnDetail").html(response.rows[0][key]);
					   }
					}
				var now = new Date(response.rows[0].MODITIME.time);
				var year = now.getFullYear(),
				　　month = now.getMonth() + 1,
				　　date = now.getDate(),
				　　hour = now.getHours(),
				　　minute = now.getMinutes(),
				　　second = now.getSeconds();
				var tm = year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
				$("#MODITIME_pptnDetail").html(tm);
			});
			opt_all.query_info_floodTran.modal({
				show 	   : true
				,backdrop  : "static" // 背景遮挡
					,moveable  : true
			}).on('shown.zui.modal', function() {
			});
		}
		
		//上传文件
		this.uploading=function(contId,evalId,engineerCode){
			//履约评价的文件上传
			filesUpload_eval(evalId,engineerCode);
			//主合同的文件上传
			filesUpload(contId,engineerCode);
			filesUploadShow();
		}
		
		
		//删除文件
		function deleteFileById(fileId,id){
			var url=basePath+"file/upload!deleteFileById.action?fileId="+fileId;
			common_ajax(null,url, function(response) {
				if(response.retflag=="success"){
				}else{
					var msg = new $.zui.Messager("消息提示："+response.message, {placement: "center",type:"primary"});
				    msg.show();	
				}
			})
		}
		
		
		//文件上传
		function filesUpload(){
			var file_options = {
				//初始化参数
				url:opt_all.url_import	//文件上传地址
			   ,filters:{//文件过滤器
				   	// 只允许上传Excel
				    mime_types: [
				        {title: 'Excel', extensions: 'xlsx,xls'},
				    ],
				    // 最大上传文件为 10MB
				    max_file_size: '10mb',
				    // 不允许上传重复文件
				    prevent_duplicates: true
				}
			   ,responseHandler:function(responseObject, file){
				   // 当服务器返回的文本内容包含 `'error'` 文本时视为上传失败
				   if(responseObject.response=='error') {
					   var msg = new $.zui.Messager("消息提示：导入失败", {placement: "center",type:"primary"});
					   msg.show();	
					   return '导入失败';
				   }else if(responseObject.response=='success') {
					   _refresh();
				   }
			   }
			}
			$('#myUploader_floodTran').uploader(file_options);
		}
		
		//文件上传模态框
		function upload_model_show(){
			filesUpload();
			opt_all.info_upload.modal({
				 show : true
				,backdrop : "static" // 背景遮挡
				,moveable : true
			}).on('shown.zui.modal', function() {
	
	        });
		}
		
		//导出全部
		function exportAllPptn(){
			var url= opt_all.url_export+"?";
			var data="mPptnFormBean.pageBean.limit=99999999"
					+"&mPptnFormBean.pageBean.offset=0" 
					+"&mPptnFormBean.pageBean.sortOrder="+TableQueryParams.order
					+"&mPptnFormBean.pageBean.sort="+TableQueryParams.sort
					+"&mPptnFormBean.searchName="+opt_all.query_searchName.val();
			confirm("<i class='icon icon-reply'></i>&nbsp;导出全部","您确定要导出全部信息吗？","icon-info", function(result) {
			   if(result){
				   window.location.href=url+data;
               }
			});
		}
		
		//导出当前页
		function exportPagePptn(){
			var url= opt_all.url_export+"?";
			var data_page="mPptnFormBean.pageBean.limit="+TableQueryParams.limit
						 +"&mPptnFormBean.pageBean.offset="+TableQueryParams.offset
						 +"&mPptnFormBean.pageBean.sortOrder="+TableQueryParams.order
						 +"&mPptnFormBean.pageBean.sort="+TableQueryParams.sort
						 +"&mPptnFormBean.searchName"+opt_all.query_searchName.val();
			confirm("<i class='icon icon-circle-arrow-up'></i>&nbsp;导出当前页","您确定要导出当前页吗？","icon-info", function(result) {
			   if(result){
				   window.location.href=url+data_page;
			   }
			});
		}
		//****绑定事件
		
		function event_add(){
			_edit("","");
		}

		//绑定刷新事件
		function event_ref(){
			 _refresh();
		}
		
		
		//****绑定事件******end
	    // 刷新
	    function _refresh(){
	    	opt_all.query_table_floodTran.bootstrapTable('refresh');
	    }
	    // 重置窗体
	    function _reset(){
			opt_all.info_form_floodTran.data('bootstrapValidator').resetForm(true);
		}
		//公开函数
		this.reset=function(){
			_reset();
		}
		////////////////////////////////////////////////////////////////////////////////
		//加载所有外键表到下拉框，无
		function Load_EditSelectData(callback){
			//所有编辑页面下拉框加载
			callback();
		}
	};
})(jQuery);
			
