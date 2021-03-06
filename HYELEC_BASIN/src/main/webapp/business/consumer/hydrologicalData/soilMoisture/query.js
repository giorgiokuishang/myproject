(function($) {
	//当前工程内码
	var engineeringNm = sessionStorage.getItem("engineeringNm");
	$("body").children("#edit_dialog_soilMoisture").remove();
	$("body").append($("#edit_dialog_soilMoisture"));
	$.SoilMoisture_Query = function(option) {
		//option自定义参数覆盖
		//A、界面控件变量
		var opt_control={
			query_table_soilMoisture 		:$('#query_table_soilMoisture')		//页面BootStrapTable的ID
			,query_searchName				:$('#query_searchName_soil')	//页面模糊查询input
			,query_startTime_soil			:$('#startTime_soil')	//页面查询起始时间
			,query_endTime_soil				:$('#endTime_soil')	//页面查询结束时间
			,query_stcd_soil				:$('#stcd_soil')	//页面查询根据测站名称
			,query_info_soilMoisture		:$("#query_info_soilMoisture")	//双击事件窗口id
			,query_ref						:$("#query_ref_soil")		//模糊查询
			,query_form						:$("#query_soilmoisture_form")
			,edit_dialog_soilMoisture		:$("#edit_dialog_soilMoisture")		//添加编辑窗口
			,query_add_soilMoisture			:$("#query_add_soilMoisture")//添加按钮
			,btn_del_soilMoisture			:$("#btn_del_soilMoisture")//批量删除按钮
			,info_form_soilMoisture			:$("#info_form_soilMoisture")//添加编辑的表单
			,info_upload					:$("#info_upload")	//文件上传模态框
			,btn_into_pptn					:$("#btn_into_pptn_soil")	//导入按钮
			,btn_outAll_pptn	    		:$('#btn_outAll_pptn_soil') //导出全部按钮
			,btn_outPage_pptn				:$('#btn_outPage_pptn_soil') //导出当前页按钮
			,btn_stcd_soil					:$('#stcd_soil_list')		//测站下拉框
			,btn_muban_soil					:$('#btn_muban_soil')		//下载模板
			,btn_soilmoisture_save			:$('#btn_soilmoisture_save')//保存按钮
		}
		//B、请求地址URL
		var opt_url={
			url_list			:basePath + "soil/soil!list.action"//查询数据URL
			,url_save			:basePath + "soil/soil!save.action"//添加或修改保存主合同URL
			,url_remove			:basePath + "soil/soil!removeids.action"//删除URL
			,url_edit			:basePath + "soil/soil!edit.action"//修改查询数据
			,url_import			:basePath + "soil/soil!importPptn.action"//导入
			,url_export			:basePath + "soil/soil!export.action"//导出
			,url_upstcd_Stbprp  :basePath + "stbprp/stbprp!upstcd_Stbprp.action"//下拉列表
		}
		//全部变量，自定义的覆盖默认变量
		var opt_all=($.extend(opt_control,opt_url,option));
	    //////////////////////////////////////////////////////////////////////////////////////////////////
		this.InitData=function(opt){
			opt_all=$.extend(opt_all,opt);   
			opt_all.query_table_soilMoisture.bootstrapTable($.extend(g_bootstrapTable_Options,
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
			    $("#query_table_soilMoisture tbody tr[data-index="+i+"]").addClass("success");
			   }
			   //全不选时颜色恢复
			   function onUncheckAll(){
			    $("#query_table_soilMoisture tbody tr").removeClass("success");
			   }
			   //选中一行改变表格背景色
			   function onCheck(rows){
			    $("#query_table_soilMoisture tbody tr[data-uniqueid="+rows.id+"]").addClass("success");
			   }
			   //不选中时颜色恢复
			   function onUncheck(rows){
			    $("#query_table_soilMoisture tbody tr[data-uniqueid="+rows.id+"]").removeClass("success");
			   }
			// 提交查询函数
			function queryParams(params) {  // 配置参数
				//查询条件
				TableQueryParams = params;
				var opt_parms={
					 "mSoilFormBean.searchName"				:opt_control.query_searchName.val() // 查询关键字
					,"mSoilFormBean.startTime"				:opt_control.query_startTime_soil.val()// 查询关键字
					,"mSoilFormBean.endTime"				:opt_control.query_endTime_soil.val() // 查询关键字
					,"mSoilFormBean.stcd"					:opt_control.btn_stcd_soil.val()// 查询关键字
				};
			    var temp = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			       "mSoilFormBean.pageBean.limit"			: params.limit   // 页面大小
			      ,"mSoilFormBean.pageBean.offset"			: params.offset  // 当前记录偏移条数
			      ,"mSoilFormBean.pageBean.sort"				: params.sort  	  // 排序列名
			      ,"mSoilFormBean.pageBean.sortOrder" 		: params.order   // 排位命令（desc，asc）
			    };
			    temp=$.extend(temp,opt_parms,opt);
				return temp;
			}
			// 双击事件
			function onDblClickRow(row){
//				if (row) {
//					_table(row.STCD);
//				}
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
			opt_all.query_add_soilMoisture.bind("click",event_add);
			opt_all.btn_del_soilMoisture.bind("click",event_del);
			opt_all.query_ref.bind("click",event_ref);
			opt_all.btn_into_pptn.bind("click",upload_model_show);
			opt_all.btn_outPage_pptn.bind("click",exportPagePptn);
			opt_all.btn_outAll_pptn.bind("click",exportAllPptn);
			opt_all.btn_muban_soil.bind("click",downloadTemplate);
			opt_all.btn_soilmoisture_save.bind("click",_save);
			
			//初始化主合同编辑表单 保存button类型为submit
			opt_all.info_form_soilMoisture.bootstrapValidator().on("success.form.bv", function(e) {
				e.preventDefault(); // 去掉默认提交事件
				// 校验数据正确,执行保存数据
				_save();
				
			}).on("error.form.bv", function(e) {
				e.preventDefault(); // 去掉默认提交事件
			});	
			
		}
		
		//下拉数据初始化
		this.InitSelect = function (){
			Load_select_stcd_soil();
			Load_select_stcd_soil_list();
		}
		
		//下载模板
		function downloadTemplate(){
			window.location.href=basePath+"/common/download/土壤墒情表.xlsx"
		}
		
		function Load_select_stcd_soil(){
			opt_control.query_stcd_soil.empty();
			var url = opt_all.url_upstcd_Stbprp;
			opt_control.query_stcd_soil.append("<option value=''>请选择测站名称</option>");
			common_ajax(null,url, function(data) {
				console.log(JSON.stringify(data));
				$.each(data.rows, function(i) {
					opt_control.query_stcd_soil.append(
						'<option value=' + data.rows[i].STCD + '>'+ data.rows[i].STNM + '</option>');
				});
			});
		}
		function Load_select_stcd_soil_list(){
			opt_control.btn_stcd_soil.empty();
			var url = opt_all.url_upstcd_Stbprp;
			opt_control.btn_stcd_soil.append("<option value=''>请选择测站名称</option>");
			common_ajax(null,url, function(data) {
				console.log(JSON.stringify(data));
				$.each(data.rows, function(i) {
					opt_control.btn_stcd_soil.append(
							'<option value=' + data.rows[i].STCD + '>'+ data.rows[i].STNM + '</option>');
				});
			});
		}
		
	    // 修改记录，调出窗体,公开函数
		this.edit=function(stcd,tm,exKey) {
			_edit(stcd,tm,exKey);
		}
		//单条删除
		this.del=function(stcd,tm,exKey){
			if(tm != "" && stcd != "" && exKey != ""){
				_del(stcd,tm,exKey);
			}
		}
		
		//绑定删除事件
		function event_del(){
			_removeids();
		}
		// 批量删除
		function _removeids(){
           var ids = g_select_and_tip(opt_all.query_table_soilMoisture,"STCD");
           var ids_ =(tm.substring(tm.length-1)==',')?tm.substring(0,tm.length-1):tm;
           var _ids = g_select_and_tip(opt_all.query_table_soilMoisture,"EXKEY");
           if (ids.length==0 && ids_.length==0 && _ids.length==0 ) return;
           
           $(".fixed-table-container tbody tr.selected td").addClass("row-bcground");
           
           var url= opt_all.url_remove+"?mSoilFormBean.mSoilInfoBean.stcd="+ids+"&mSoilFormBean.mSoilInfoBean.tm="+ids_+"&mSoilFormBean.mSoilInfoBean.exkey="+_ids;
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
		function _del(stcd,tm,exKey){
			var url= opt_all.url_remove+"?mSoilFormBean.mSoilInfoBean.stcd="+stcd+"&mSoilFormBean.mSoilInfoBean.tm="+tm+"&mSoilFormBean.mSoilInfoBean.exkey="+exKey;
			$(".table tbody tr[data-uniqueid="+stcd+"] td").addClass("row-bcground");
			confirm("资料管理","您确定要删除该条记录吗？","icon-remove-sign", function(result) {
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
		
		//新增和编辑函数
		function _edit(stcd,tm,exKey){
			//加载Edit页面基本选择数据，成功后调用显示编辑页面
			//加载字典分类数据下拉框数据
			Load_EditSelectData( 
				function(){ 
					LoadEditData(stcd,tm,exKey);
				}
			);
		}
		
		// 保存主合同数据
		function _save() {
			$('#stcd_soil_').val('');	
			$('#tm_soil_').val('');
			$('#stcd_soil_').val($('#stcd_soil').val());	
			$('#tm_soil_').val($('#tm_soil').val());
	 		var json=opt_all.info_form_soilMoisture.serialize();
	 		common_ajax(json, opt_all.url_save, function(response){
	 			opt_all.edit_dialog_soilMoisture.modal("hide");
			    //重置表单
			    _reset();
			    // 刷新列表
			    _refresh();
	 		});
		}
		
		//显示详情
		function _table(queryId){
			var url = opt_all.url_list+"?mSoilFormBean.mSoilInfoBean.stcd=" + queryId;
			common_ajax(null,url, function(response) {
				for(var key in response.rows[0]){
					//主合同信息
				   if($("#"+key+"_detail_t")[0]){
					   $("#"+key+"_detail_t").html(response.rows[0][key]);
				   }
				}
				var now = new Date(response.rows[0].TM.time);
				var year = now.getFullYear(),
				　　month = now.getMonth() + 1,
				　　date = now.getDate(),
				　　hour = now.getHours(),
				　　minute = now.getMinutes(),
				　　second = now.getSeconds();
				var tm = year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
				$("#TM_detail_t").html(tm);
			});
			opt_all.query_info_soilMoisture.modal({
				show 	   : true
				,backdrop  : "static" // 背景遮挡
					,moveable  : true
			}).on('shown.zui.modal', function() {
			});
		}
		
		//文件上传模态框
		function upload_model_show(){
			filesUpload();
			opt_all.info_upload.modal({
				 show : true
				,backdrop : false // 背景遮挡
				,moveable : true
			}).on('shown.zui.modal', function() {
	
	        });
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
			$('#myUploader').uploader(file_options);
		}
		
		//上传文件
		this.uploading=function(contId,evalId,engineerCode){
			//履约评价的文件上传
			filesUpload_eval(evalId,engineerCode);
			//主合同的文件上传
			filesUpload(contId,engineerCode);
			filesUploadShow();
		}
		
		//导出全部
		function exportAllPptn(){
			var url= opt_all.url_export+"?";
			var data="mSoilFormBean.pageBean.limit=99999999"
					+"&mSoilFormBean.pageBean.offset=0" 
					+"&mSoilFormBean.pageBean.sortOrder="+TableQueryParams.order
					+"&mSoilFormBean.pageBean.sort="+TableQueryParams.sort
					+"&mSoilFormBean.searchName="+opt_all.query_searchName.val()
					+"&mSoilFormBean.startTime="+opt_control.query_startTime_soil.val()// 查询关键字
					+"&mSoilFormBean.endTime="+opt_control.query_endTime_soil.val() // 查询关键字
					+"&mSoilFormBean.stcd="+opt_control.btn_stcd_soil.val()// 查询关键字
			confirm("<i class='icon icon-reply'></i>&nbsp;导出全部","您确定要导出全部信息吗？","icon-info", function(result) {
			   if(result){
				   window.location.href=url+data;
               }
			});
		}
		
		//导出当前页
		function exportPagePptn(){
			var url= opt_all.url_export+"?";
			var data_page="mSoilFormBean.pageBean.limit="+TableQueryParams.limit
						 +"&mSoilFormBean.pageBean.offset="+TableQueryParams.offset
						 +"&mSoilFormBean.pageBean.sortOrder="+TableQueryParams.order
						 +"&mSoilFormBean.pageBean.sort="+TableQueryParams.sort
						 +"&mSoilFormBean.searchName"+opt_all.query_searchName.val()
						 +"&mSoilFormBean.startTime="+opt_control.query_startTime_soil.val()// 查询关键字
						 +"&mSoilFormBean.endTime="+opt_control.query_endTime_soil.val() // 查询关键字
						 +"&mSoilFormBean.stcd="+opt_control.btn_stcd_soil.val()// 查询关键字
			confirm("<i class='icon icon-circle-arrow-up'></i>&nbsp;导出当前页","您确定要导出当前页吗？","icon-info", function(result) {
			   if(result){
				   window.location.href=url+data_page;
			   }
			});
		}
		
		//加载Edit页面上数据，并调出增加或编辑页面显示
		function LoadEditData(stcd,tm,exKey){
			if(tm != "" && stcd!="" && exKey!=""){
				var url = opt_all.url_edit+"?mSoilFormBean.mSoilInfoBean.stcd=" + stcd+"&mSoilFormBean.mSoilInfoBean.tm="+tm+"&mSoilFormBean.mSoilInfoBean.exkey="+exKey;
				common_ajax(null,url, function(response) {
					
					$('#stcd_soil').val(response.mSoilFormBean.stcd);	
					$('#tm_soil').val(response.mSoilFormBean.tm);
						
					comm_loadFormData_flag(response.mSoilFormBean,"_soil");//显示主合同信息
					
					$('#stcd_soil').prop('disabled',true);	
					$('#tm_soil').prop('disabled',true);
					
					opt_all.edit_dialog_soilMoisture.find('.modal-title').html("<i class='icon icon-pencil'></i>&nbsp;修改土壤墒情");
				});
			}else{
				$('#stcd_soil').prop('disabled',false);	
				$('#tm_soil').prop('disabled',false);
				opt_all.edit_dialog_soilMoisture.find('.modal-title').html("<i class='icon icon-plus-sign'></i>&nbsp;新增土壤墒情") ;
			}
			opt_all.edit_dialog_soilMoisture.modal({
				 show : true
				,backdrop : false // 背景遮挡
				,moveable : true
			}).on('hide.zui.modal', function() {
				$('#stcd_soil').val('');	
				$('#tm_soil').val('');
				_reset();//当第一次验证正确后，关闭窗体。再进来时，重置窗体(保留窗体上数据)。
           });
		}
		
		//****绑定事件
		//绑定添加或修改事件
		function event_add(){
			_edit("","","");
		}

		//绑定刷新事件
		function event_ref(){
			 _refresh();
		}
		
		//****绑定事件******end
	    // 刷新
	    function _refresh(){
	    	opt_all.query_table_soilMoisture.bootstrapTable('refresh');
	    }
	    this.flash=function(nm){
	    	engineeringNm=nm;
			_refresh();
		}
		// 重置主合同
		function _reset(){
			opt_all.info_form_soilMoisture.data('bootstrapValidator').resetForm(true);
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
			
