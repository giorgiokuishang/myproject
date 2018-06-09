(function($) {
	//当前工程内码
	var engineeringNm = sessionStorage.getItem("engineeringNm");
	$.Modellist_Query = function(option) {
		//option自定义参数覆盖
		//A、界面控件变量
		var opt_control={
			 query_table 			:$('#query_table')		//页面BootStrapTable的ID
			,query_searchName		:$('#query_searchName')	//页面模糊查询input
			,query_info_show		:$("#query_info_show")	//双击事件窗口id
			,query_ref				:$("#query_ref")		//模糊查询
			,query_form				:$("#query_form")
			,modellist_dialog		:$("#modellist_dialog")		//添加编辑窗口
			,query_add				:$("#query_add")		//添加按钮
			,material_add			:$("#material_add")		//添加物资按钮
			,btn_del				:$("#btn_del")			//批量删除按钮
			,modellist_form			:$("#modellist_form")		//添加编辑的表单
			,uploading_data_dialog	:$('#uploading_data_dialog') //文件上传模态框
			,enginner_dialog		:$('#enginner_dialog') //选择工程模态框
			,info_upload			:$("#info_upload")	//文件上传模态框
			,btn_into_pptn			:$("#btn_into_pptn")	//导入按钮
			,btn_outAll_pptn	    :$('#btn_outAll_pptn') //导出全部按钮
			,btn_outPage_pptn		:$('#btn_outPage_pptn') //导出当前页按钮
			,add_cs					:$("#add_cs")//添加参数
		}
		
		//B、请求地址URL
		var opt_url={
			url_list			:basePath + "modellist/modellist!list.action"//查询数据URL//查询数据URL
			,url_save			:basePath + "modellist/modellist!save.action"//添加或修改保存URL
			,url_remove	:basePath + "modellist/modellist!removeids.action"//删除URL
			,url_edit			:basePath + "modellist/modellist!edit.action"//修改数据
			,url_import			:basePath + "modellist/modellist!importModel.action"//导入
			,url_export			:basePath + "modellist/modellist!export.action"//导出
		}
		//全部变量，自定义的覆盖默认变量
		var opt_all=($.extend(opt_control,opt_url,option));
	    //////////////////////////////////////////////////////////////////////////////////////////////////
		this.InitData=function(opt){
			opt_all=$.extend(opt_all,opt);   
			opt_all.query_table.bootstrapTable($.extend(g_bootstrapTable_Options,
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
			    $("#query_table tbody tr[data-index="+i+"]").addClass("success");
			   }
			   //全不选时颜色恢复
			   function onUncheckAll(){
			    $("#query_table tbody tr").removeClass("success");
			   }
			   //选中一行改变表格背景色
			   function onCheck(rows){
			    $("#query_table tbody tr[data-uniqueid="+rows.id+"]").addClass("success");
			   }
			   //不选中时颜色恢复
			   function onUncheck(rows){
			    $("#query_table tbody tr[data-uniqueid="+rows.id+"]").removeClass("success");
			   }
			// 提交查询函数
			function queryParams(params) {  // 配置参数
				//查询条件
				TableQueryParams = params;
				var opt_parms={
					 "modelInfoFormBean.searchName"			:opt_control.query_searchName.val() // 查询关键字
				};
			    var temp = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			       "modelInfoFormBean.pageBean.limit"			: params.limit   // 页面大小
			      ,"modelInfoFormBean.pageBean.offset"			: params.offset  // 当前记录偏移条数
			      ,"modelInfoFormBean.pageBean.sort"				: params.sort  	  // 排序列名
			      ,"modelInfoFormBean.pageBean.sortOrder" 		: params.order   // 排位命令（desc，asc）
			    };
			    temp=$.extend(temp,opt_parms,opt);
				return temp;
			}
			// 双击事件
			function onDblClickRow(row){
				if (row) {
					_table(row.MODEL_CODE);
				}
			}
			// 排序事件
			function onSort(name, order){
				_refresh();
			}
		}
		
		//加载下拉列表数据
		this.InitSelect=function(){
			
		}
		
		
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		// 初始化增加、修改和删除,公开函数
		this.InitAddEditDel=function(opt){
			opt_all=$.extend(opt_all,opt);
			opt_all.query_add.bind("click",event_add);
			opt_all.btn_del.bind("click",event_del);
			opt_all.query_ref.bind("click",event_ref);
			opt_all.btn_into_pptn.bind("click",upload_model_show);
			opt_all.btn_outAll_pptn.bind("click",exportAllPptn);
			opt_all.btn_outPage_pptn.bind("click",exportPagePptn);
			opt_all.add_cs.bind("click",tjcs)
			//初始化主合同编辑表单 保存button类型为submit
			opt_all.modellist_form.bootstrapValidator().on("success.form.bv", function(e) {
				e.preventDefault(); // 去掉默认提交事件
				// 校验数据正确,执行保存数据
				_save();
				
			}).on("error.form.bv", function(e) {
				e.preventDefault(); // 去掉默认提交事件
			});	
			
		}
		
	    // 修改记录，调出窗体,公开函数
		this.edit=function(id) {
			_edit(id);
		}
	
		//单条删除
		this.del=function(id){
			if(id != null && id != "" && id != 0){
				_del(id);
			}
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
			var data="modelInfoFormBean.pageBean.limit=99999999"
					+"&modelInfoFormBean.pageBean.offset=0" 
					+"&modelInfoFormBean.pageBean.sortOrder="+TableQueryParams.order
					+"&modelInfoFormBean.pageBean.sort="+TableQueryParams.sort
					+"&modelInfoFormBean.searchName="+opt_all.query_searchName.val();
			confirm("<i class='icon icon-reply'></i>&nbsp;导出全部","您确定要导出全部信息吗？","icon-info", function(result) {
			   if(result){
				   window.location.href=url+data;
               }
			});
		}
		
		//导出当前页
		function exportPagePptn(){
			var url= opt_all.url_export+"?";
			var data_page="modelInfoFormBean.pageBean.limit="+TableQueryParams.limit
						 +"&modelInfoFormBean.pageBean.offset="+TableQueryParams.offset
						 +"&modelInfoFormBean.pageBean.sortOrder="+TableQueryParams.order
						 +"&modelInfoFormBean.pageBean.sort="+TableQueryParams.sort
						 +"&modelInfoFormBean.searchName"+opt_all.query_searchName.val();
			confirm("<i class='icon icon-circle-arrow-up'></i>&nbsp;导出当前页","您确定要导出当前页吗？","icon-info", function(result) {
			   if(result){
				   window.location.href=url+data_page;
			   }
			});
		}
		//绑定删除事件
		function event_del(){
			_removeids();
		}
		// 批量删除
		function _removeids(){
           // 获取删除选中的ids
           var ids = g_select_and_tip(opt_all.query_table,"MODEL_CODE");
           if (ids.length==0) return;
           $(".fixed-table-container tbody tr.selected td").addClass("row-bcground");
           var url= opt_all.url_remove+"?modelInfoFormBean.ids="+ids
           confirm("模型管理","您确定要删除这些记录吗？","icon-remove-sign", function(result) {
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
		
		//删除单条记录
		function _del(id){
			var url= opt_all.url_remove+"?modelInfoFormBean.ids="+id
			$(".table tbody tr[data-uniqueid="+id+"] td").addClass("row-bcground");
			confirm("模型管理","您确定要删除该条记录吗？","icon-remove-sign", function(result) {
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
		function _edit(id){
			//加载Edit页面基本选择数据，成功后调用显示编辑页面
			//加载字典分类数据下拉框数据
			Load_EditSelectData( 
				function(){ 
					LoadEditData(id);
				}
			);
		}
		
		// 保存
		function _save() {
			// 参数需要保存的表单，保存url,需要更新的bootstrapTable,必须设置uniqueId: "id"
	 		var  json=opt_all.modellist_form.serialize();
	 		common_ajax(json, opt_all.url_save, function(response){
	 			opt_all.modellist_dialog.modal("hide");
			    //重置表单
			    _reset();
			    // 刷新列表
			    _refresh();
	 		});
		}
		
		
		//新增和编辑履约评价加载数据弹窗
		function _Evaluation(id){
			//加载Edit页面基本选择数据，成功后调用显示编辑页面
			//加载字典分类数据下拉框数据
			Load_EditSelectData( 
				function(){ 
					LoadEvaluationData(id);
				}
			);
		}
		//显示详情
		function _table(queryId){
			var url = opt_all.url_list+"?modelInfoFormBean.modelInfoFormBean.modelCode=" + queryId;
			common_ajax(null,url, function(response) {
				for(var key in response.rows[0]){
				   if($("#"+key+"_detail")[0]){
					   $("#"+key+"_detail").html(response.rows[0][key]);
				   }
				}
		});
			opt_all.query_info_show.modal({
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
		
		
		//加载Edit页面上数据，并调出增加或编辑页面显示
		function LoadEditData(id,onlyread){
			if(id != null && id!=""){
				var url = opt_all.url_edit+"?modelInfoFormBean.modelInfoFormBean.modelCode=" + id;
				// 动态加载页面数据
				common_ajax(null,url, function(response) {
					if(response.ModelInfoFormBean.modelType=="1"){
						console.log(response.ModelInfoFormBean.modelType)
				}
					opt_all.modellist_dialog.find('.modal-title').html("<i class='icon icon-pencil'></i>&nbsp;修改模型信息");
					　$("#query_cs").empty();//移除
					comm_loadFormData_flag(response.ModelInfoFormBean,"_modelinfo");
				
					for(var  i=0;i<response.ModelPataList.length;i++){
						var tr = "";
						tr+= "<tr class='t1'>"+
								"<th>&nbsp;&nbsp;&nbsp;&nbsp;<input type='checkbox' name='ModelInfoFormBean.modelParaBean.isRequired' value='1' />是否必填</th>"+
								"<th>&nbsp; 参数名称</th>"+
								"<th><input type='text' class='form-control'" +
			                   " name='ModelInfoFormBean.modelParaBean.paraName'"+
			                   "value='"+response.ModelPataList[i].PARA_NAME+"'"+
			                   " placeholder='请输入参数名称'"+
			                   	"><input type='hidden' name='ModelInfoFormBean.modelParaBean.paraCode' value='"+response.ModelPataList[i].PARA_CODE+"'></th>"+
								"<th >&nbsp;&nbsp;符号&nbsp;&nbsp;</th>"+
								"<th><input type='text' class='form-control'"+ 
			                   " name='ModelInfoFormBean.modelParaBean.paraSymbol'" +
			                   "value='"+response.ModelPataList[i].PARA_SYMBOL+"'"+
			                   " placeholder='请输入参数符号' maxlength='5'"+
			                   "></th>"+
			               	"<th>&nbsp;&nbsp;最大值&nbsp;&nbsp;</th>"+
							"<th><input type='text' class='form-control'" +
			               " name='ModelInfoFormBean.modelParaBean.paraMax' "+
			               "value='"+response.ModelPataList[i].PARA_MAX+"'"+
			               " placeholder='请输入最大值' maxlength='10'"+
			               	"></th>"+
							"<th >&nbsp;&nbsp;最小值&nbsp;&nbsp;</th>"+
							"<th><input type='text' class='form-control mainWidth' "+ 
			               " name='ModelInfoFormBean.modelParaBean.paraMin'" +
			               "value='"+response.ModelPataList[i].PARA_MIN+"'"+
			               " placeholder='请输入最小值'  maxlength='5'"+
			               "></th>"+
								"<th><a href='#' onclick='del(this)'  class='tableA'>删除</a></th>"+
								"</tr>"
				　  　$("#query_cs").append(tr);　　  
					}
				
					$("#list").val(response.ModelPataList);
					opt_all.modellist_dialog.modal({
						 show : true
						,backdrop : "static" // 背景遮挡
						,moveable : true
					}).on('hide.zui.modal', function() {
						_reset();//当第一次验证正确后，关闭窗体。再进来时，重置窗体(保留窗体上数据)。
		            });
			});
			}else{
				//先清除添加过的数据再弹窗
				$("#modelCode_modelinfo").val("");//新增的时候没有modelCode
				opt_all.modellist_dialog.find('.modal-title').html("<i class='icon icon-plus-sign'></i>&nbsp;新增模型信息") ;
				//新增的时候，默认给一条Input填，可以添加多个input
				　$(".t1").remove();//移除
				
				var tr = "";
				tr+= "<tr>"+
						"<th>&nbsp;&nbsp;&nbsp;&nbsp;<input type='checkbox' name='ModelInfoFormBean.modelParaBean.isRequired' value='1'  />是否必填</th>"+
						"<th> &nbsp; 参数名称</th>"+
						"<th><input type='text' class='form-control ' " +
	                   " name='ModelInfoFormBean.modelParaBean.paraName'"+
	                   " placeholder='请输入参数名称'  maxlength='10'"+
	                   	"></th>"+
						"<th >&nbsp;&nbsp;符号&nbsp;&nbsp;</th>"+
						"<th><input type='text' class='form-control ' "+ 
	                   " name='ModelInfoFormBean.modelParaBean.paraSymbol'" +
	                   " placeholder='请输入参数符号'    maxlength='5'"+
	                   "></th>"+
	               	"<th>&nbsp;&nbsp;最大值&nbsp;&nbsp;</th>"+
					"<th><input type='text' class='form-control '" +
	               " name='ModelInfoFormBean.modelParaBean.paraMax' "+
	               " placeholder='请输入最大值' maxlength='10'"+
	               	"></th>"+
					"<th >&nbsp;&nbsp;最小值&nbsp;&nbsp;</th>"+
					"<th><input type='text' class='form-control c2' "+ 
	               " name='ModelInfoFormBean.modelParaBean.paraMin'" +
	               " placeholder='请输入最小值'  maxlength='5'"+
	               "></th>"+
						"<th><a href='#' onclick='del(this)'  class='tableA'>删除</a></th>"+
						"</tr>"
		　  　$("#query_cs").append(tr);　　  
				  
				opt_all.modellist_dialog.modal({
					 show : true
					,backdrop : "static" // 背景遮挡
					,moveable : true
				}).on('hide.zui.modal', function() {
					_reset();//当第一次验证正确后，关闭窗体。再进来时，重置窗体(保留窗体上数据)。
	            });
			}
		}
		//删除参数
		del = function(obj){
	//		alert(obj.ModelInfoFormBean)
			
			$(obj).parent().parent().remove();
		  }
		
			
		//添加参数
		function tjcs(){
			var tr = "";
			tr+= "<tr>"+
					"<th>&nbsp;&nbsp;&nbsp;&nbsp;<input type='checkbox'  name='ModelInfoFormBean.modelParaBean.isRequired' value='1' />是否必填</th>"+
					"<th>&nbsp; 参数名称</th>"+
					"<th><input type='text' class='form-control'" +
                   " name='ModelInfoFormBean.modelParaBean.paraName' "+
                   " placeholder='请输入参数名称' maxlength='10'"+
                   	"></th>"+
					"<th >&nbsp;&nbsp;符号&nbsp;&nbsp;</th>"+
					"<th><input type='text' class='form-control' "+ 
                   " name='ModelInfoFormBean.modelParaBean.paraSymbol'" +
                   " placeholder='请输入参数符号'  maxlength='5'"+
                   "></th>"+
				"<th>&nbsp;&nbsp;最大值&nbsp;&nbsp;</th>"+
				"<th><input type='text' class='form-control'" +
               " name='ModelInfoFormBean.modelParaBean.paraMax' "+
               " placeholder='请输入最大值' maxlength='10'"+
               	"></th>"+
				"<th >&nbsp;&nbsp;最小值&nbsp;&nbsp;</th>"+
				"<th><input type='text' class='form-control  c2' "+ 
               " name='ModelInfoFormBean.modelParaBean.paraMin'" +
               " placeholder='请输入最小值'  maxlength='5'"+
               "></th>"+
					"<th><a href='#' onclick='del(this)' class='tableA'>删除</a></th>"+
					"</tr>"
	　  　$("#query_cs").append(tr);　　  
			  
		}
		
		//****绑定事件
		//绑定添加或修改事件
		function event_showZtree(){
			opt_all.enginner_dialog.modal({
				show : true
				,backdrop : "static" // 背景遮挡
				,moveable : true
			}).on('shown.zui.modal', function() {
//				_reset();//当第一次验证正确后，关闭窗体。再进来时，重置窗体(保留窗体上数据)。
			});
		}
		
		function event_add(){
			_edit(0);
		}

		function event_addMaterial(){
			//新增物资时显示输入框
			_edit_material();
		}
		
		//绑定刷新事件
		function event_ref(){
			 _refresh();
		}
		
		//****绑定事件******end
	    // 刷新
	    function _refresh(){
	    	opt_all.query_table.bootstrapTable('refresh');
	    }
	    this.flash=function(nm){
	    	engineeringNm=nm;
			_refresh();
		}
		//公开函数
		this.reset=function(){
			_reset();
		}
		// 重置窗体
	    function _reset(){
			opt_all.modellist_form.data('bootstrapValidator').resetForm(true);
		}
		////////////////////////////////////////////////////////////////////////////////
		//加载所有外键表到下拉框，无
		function Load_EditSelectData(callback){
			//所有编辑页面下拉框加载
			callback();
		}
	};
})(jQuery);
			
