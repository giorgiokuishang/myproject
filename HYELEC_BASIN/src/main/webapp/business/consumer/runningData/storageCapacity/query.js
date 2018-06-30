(function($) {
	//当前工程内码
	var engineeringNm = sessionStorage.getItem("engineeringNm");
	$.StorageCapacity_Query = function(option) {
		//option自定义参数覆盖
		//A、界面控件变量
		var opt_control={
			query_table_storageCapacity	:$('#query_table_storageCapacity')		//页面BootStrapTable的ID
			,query_searchName			:$('#query_searchName_zvarl')	//页面模糊查询input
			,query_info_storageCapacity	:$("#query_info_storageCapacity")//双击事件窗口id
			,query_ref					:$("#query_ref_zvarl")		//模糊查询
			,query_form					:$("#query_form")
			,edit_dialog_storageCapacity:$("#edit_dialog_storageCapacity")//添加编辑窗口
			,edit_dialog_storageCapacity_x:$("#edit_dialog_storageCapacity_x")//添加编辑窗口
			,query_add_storageCapacity	:$("#query_add_storageCapacity")//添加按钮
			,btn_del_storageCapacity	:$("#btn_del_storageCapacity")//批量删除按钮
			,info_form_storageCapacity	:$("#info_form_storageCapacity")//添加编辑的表单
			,info_form_storageCapacity_x	:$("#info_form_storageCapacity_x")//添加编辑的表单
			,info_upload				:$("#info_upload")	//文件上传模态框
			,btn_into_pptn				:$("#btn_into_pptn_zvarl")	//导入按钮
			,btn_outAll_pptn	    	:$('#btn_outAll_pptn_zvarl') //导出全部按钮
			,btn_outPage_pptn			:$('#btn_outPage_pptn_zvarl') //导出当前页按钮
			,btn_save					:$('#btn_save')		//保存按钮
			,btn_stcd_zvarl				:$('#stcd_zvarl')		//测站下拉框
			,btn_stcd_zvarl_x				:$('#stcd_zvarl_x')
			,query_add_storageCapacity_x :$("#query_add_storageCapacity_x")
			,btn_storageCapacity_x		 :$('#btn_storageCapacity_x')
		}
		
		//B、请求地址URL
		var opt_url={
			url_list			:basePath + "zvarl/zvarl!list.action"//查询数据URL
			,url_save			:basePath + "zvarl/zvarl!save.action"//添加或修改保存URL
			,url_save_x			:basePath + "zvarl/zvarl!saveX.action"//修改查询数据
			,url_remove			:basePath + "zvarl/zvarl!removeids.action"//删除URL
			,url_edit			:basePath + "zvarl/zvarl!edit.action"//修改查询数据
			,url_import			:basePath + "zvarl/zvarl!importPptn.action"//导入
			,url_export			:basePath + "zvarl/zvarl!export.action"//导出
			,url_upstcd_Stbprp  :basePath + "stbprp/stbprp!upstcd_Stbprp.action"//上游编码
		}
		//全部变量，自定义的覆盖默认变量
		var opt_all=($.extend(opt_control,opt_url,option));
	    //////////////////////////////////////////////////////////////////////////////////////////////////
		this.InitData=function(opt){
			opt_all=$.extend(opt_all,opt);   
			opt_all.query_table_storageCapacity.bootstrapTable($.extend(g_bootstrapTable_Options,
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
			    $("#query_table_storageCapacity tbody tr[data-index="+i+"]").addClass("success");
			   }
			   //全不选时颜色恢复
			   function onUncheckAll(){
			    $("#query_table_storageCapacity tbody tr").removeClass("success");
			   }
			   //选中一行改变表格背景色
			   function onCheck(rows){
			    $("#query_table_storageCapacity tbody tr[data-uniqueid="+rows.id+"]").addClass("success");
			   }
			   //不选中时颜色恢复
			   function onUncheck(rows){
			    $("#query_table_storageCapacity tbody tr[data-uniqueid="+rows.id+"]").removeClass("success");
			   }
			// 提交查询函数
			function queryParams(params) {  // 配置参数
				//查询条件
				var opt_parms={
					 "mZvarlFormBean.searchName"			:opt_control.query_searchName.val() // 查询关键字
				};
			    var temp = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			       "mZvarlFormBean.pageBean.limit"			: params.limit   // 页面大小
			      ,"mZvarlFormBean.pageBean.offset"			: params.offset  // 当前记录偏移条数
			      ,"mZvarlFormBean.pageBean.sort"				: params.sort  	  // 排序列名
			      ,"mZvarlFormBean.pageBean.sortOrder" 		: params.order   // 排位命令（desc，asc）
			    };
			    temp=$.extend(temp,opt_parms,opt);
				return temp;
			}
			// 双击事件
			function onDblClickRow(row){
				if (row) {
					_table(row.STCD);
				}
			}
			// 排序事件
			function onSort(name, order){
				_refresh();
			}
			function comm_onLoadSuccess(data){
				var data = $('#query_table_storageCapacity').bootstrapTable('getData', true);
				console.log(JSON.stringify(data))
				mergeCells(data,"STNM",1, $('#query_table_storageCapacity'));
				//mergeCells(data,"MSTM",1, $('#query_table_storageCapacity'));
				mergeCells(data,"STCD",1, $('#query_table_storageCapacity'));
			}
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		// 初始化增加、修改和删除,公开函数
		this.InitAddEditDel=function(opt){
			opt_all=$.extend(opt_all,opt);
			opt_all.query_add_storageCapacity.bind("click",event_add);
			opt_all.btn_del_storageCapacity.bind("click",event_del);
			opt_all.query_ref.bind("click",event_ref);
			opt_all.btn_into_pptn.bind("click",upload_model_show);
			opt_all.btn_outPage_pptn.bind("click",exportPagePptn);
			opt_all.btn_outAll_pptn.bind("click",exportAllPptn);
			opt_all.query_add_storageCapacity_x.bind("click",x_xzcs);
			opt_all.btn_storageCapacity_x.bind("click",btn_storageCapacity_x);
			
			//初始化主合同编辑表单 保存button类型为submit
			opt_all.info_form_storageCapacity.bootstrapValidator().on("success.form.bv", function(e) {
				e.preventDefault(); // 去掉默认提交事件
				// 校验数据正确,执行保存数据
				_save();
				
			}).on("error.form.bv", function(e) {
				e.preventDefault(); // 去掉默认提交事件
			});	
			
		}
		
		//下拉数据初始化
		this.InitSelect = function (){
			Load_select_stcd_zvarl();
			Load_select_stcd_zvarl_x();
		}
		
		function Load_select_stcd_zvarl(){
			opt_control.btn_stcd_zvarl.empty();
			var url = opt_all.url_upstcd_Stbprp;
			opt_control.btn_stcd_zvarl.append("<option value=''>请选择测站名称</option>");
			common_ajax(null,url, function(data) {
				$.each(data.rows, function(i) {
					opt_control.btn_stcd_zvarl.append(
						'<option value=' + data.rows[i].STCD + '>'+ data.rows[i].STNM + '</option>');
				});
			});
		}
		function Load_select_stcd_zvarl_x(){
			opt_control.btn_stcd_zvarl_x.empty();
			var url = opt_all.url_upstcd_Stbprp;
			opt_control.btn_stcd_zvarl_x.append("<option value=''>请选择测站名称</option>");
			common_ajax(null,url, function(data) {
				$.each(data.rows, function(i) {
					opt_control.btn_stcd_zvarl_x.append(
						'<option value=' + data.rows[i].STCD + '>'+ data.rows[i].STNM + '</option>');
				});
			});
		}
		
	    // 修改记录，调出窗体,公开函数
		this.edit=function(stcd,mstm) {
			_edit(stcd,mstm);
		}
		//显示合同详细
		this.onClickContract = function(id){
			window.location.href = opt_all.url_details + "?contractId="+id;
		}
		//单条删除
		this.del=function(stcd,mstm){
			if(stcd != "" && mstm != ""){
				_del(stcd,mstm);
			}
		}
		//绑定删除事件
		function event_del(){
			_removeids();
		}
		// 批量删除
		function _removeids(){
           var stcd = g_select_and_tip(opt_all.query_table_storageCapacity,"STCD");
           var mstm=(tm.substring(tm.length-1)==',')?tm.substring(0,tm.length-1):tm;
           var ptno = g_select_and_tip(opt_all.query_table_storageCapacity,"PTNO");
           if (stcd.length==0 && mstm.length==0 && ptno.length==0) return;
           
           $(".fixed-table-container tbody tr.selected td").addClass("row-bcground");
           
           var url= opt_all.url_remove+"?stcd="+stcd+"&mstm="+mstm+"&ptno="+ptno;
           confirm("资料管理","您确定要删除这些记录吗？","icon-remove-sign", function(result) {
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
		function _del(stcd,mstm){
			var mstm=(mstm.substring(mstm.length-1)==',')?mstm.substring(0,mstm.length-1):mstm;
			var url= opt_all.url_remove+"?stcd="+stcd+"&mstm="+mstm+"&flag=1";
			//$(".table tbody tr[data-uniqueid="+stcd+"] td").addClass("row-bcground");
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
		function _edit(stcd,mstm){
			Load_EditSelectData( 
				function(){ 
					LoadEditData(stcd,mstm);
				}
			);
		}
		
		
		function _save() {
	 		var json=opt_all.info_form_storageCapacity.serialize();
	 		common_ajax(json, opt_all.url_save, function(response){
	 			opt_all.edit_dialog_storageCapacity.modal("hide");
			    _refresh();
	 		});
		}
		
		//新增或修改表单
		function btn_storageCapacity_x(){
			var json=opt_all.info_form_storageCapacity_x.serialize();
	 		common_ajax(json, opt_all.url_save_x, function(response){
	 			opt_all.edit_dialog_storageCapacity_x.modal("hide");
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
			var url = opt_all.url_list+"?mZvarlFormBean.mZvarlInfoBean.stcd=" + queryId;
			common_ajax(null,url, function(response) {
				for(var key in response.rows[0]){
				   if($("#"+key+"_detail")[0]){
					   $("#"+key+"_detail").html(response.rows[0][key]);
				   }
				}
				var now = new Date(response.rows[0].MSTM.time);
				var year = now.getFullYear(),
				　　month = now.getMonth() + 1,
				　　date = now.getDate(),
				　　hour = now.getHours(),
				　　minute = now.getMinutes(),
				　　second = now.getSeconds();
				var tm = year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
				
				var now_m = new Date(response.rows[0].MODITIME.time);
				var year_m = now_m.getFullYear(),
				　　month_m = now_m.getMonth() + 1,
				　　date_m = now_m.getDate(),
				　　hour_m = now_m.getHours(),
				　　minute_m = now_m.getMinutes(),
				　　second_m = now_m.getSeconds();
				var tm_m = year_m + "-" + month_m + "-" + date_m + " " + hour_m + ":" + minute_m + ":" + second_m;
				$("#MODITIME_detail").html(tm_m);
				$("#MSTM_detail").html(tm);
			});
			opt_all.query_info_storageCapacity.modal({
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
				,backdrop : "static" // 背景遮挡
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
			var data="mZvarlFormBean.pageBean.limit=99999999"
					+"&mZvarlFormBean.pageBean.offset=0" 
					+"&mZvarlFormBean.pageBean.sortOrder="+TableQueryParams.order
					+"&mZvarlFormBean.pageBean.sort="+TableQueryParams.sort
					+"&mZvarlFormBean.searchName="+opt_all.query_searchName.val();
			confirm("<i class='icon icon-reply'></i>&nbsp;导出全部","您确定要导出全部信息吗？","icon-info", function(result) {
			   if(result){
				   window.location.href=url+data;
               }
			});
		}
		
		//导出当前页
		function exportPagePptn(){
			var url= opt_all.url_export+"?";
			var data_page="mZvarlFormBean.pageBean.limit="+TableQueryParams.limit
						 +"&mZvarlFormBean.pageBean.offset="+TableQueryParams.offset
						 +"&mZvarlFormBean.pageBean.sortOrder="+TableQueryParams.order
						 +"&mZvarlFormBean.pageBean.sort="+TableQueryParams.sort
						 +"&mZvarlFormBean.searchName"+opt_all.query_searchName.val();
			confirm("<i class='icon icon-circle-arrow-up'></i>&nbsp;导出当前页","您确定要导出当前页吗？","icon-info", function(result) {
			   if(result){
				   window.location.href=url+data_page;
			   }
			});
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
		function LoadEditData(stcd,mstm){
			if(stcd != "" && mstm!=""){
				var mstm=(mstm.substring(mstm.length-1)==',')?mstm.substring(0,mstm.length-1):mstm;
				var url = opt_all.url_edit+"?mZvarlFormBean.mZvarlInfoBean.stcd=" + stcd+"&mZvarlFormBean.mZvarlInfoBean.mstm="+mstm;
				common_ajax(null,url, function(response) {
					var table = $("#query_table_storageCapacity_x");
					table.empty();
					var tr = "<tr>"+
								"<td style='width: 100px; text-align: center; font-weight: bold; background: #f1f1f1'>"+
									"<lable>曲线序号：</lable></td>"+
								"<td style='width: 100px; text-align: center; font-weight: bold; background: #f1f1f1'>"+
									"<lable>库水位：</lable></td>"+
								"<td style='width: 100px; text-align: center; font-weight: bold; background: #f1f1f1'>"+
									"<lable>	蓄水量：</lable></td>"+
									"<td style='width: 100px; text-align: center; font-weight: bold; background: #f1f1f1'>"+
									"<lable>	水面面积：</lable></td>"+
								/*"<td style='width: 100px; text-align: center; font-weight: bold; background: #f1f1f1'>"+
									"<lable>施测时间：</lable></td>"+*/
								"<td style='width: 100px; text-align: center; font-weight: bold; background: #f1f1f1'>"+
									"<lable>操作：</lable></td></tr>";
					table.append(tr);
					for(var i =0;i<response.mZvarlFormBean.length;i++){
						var MSTM=response.mZvarlFormBean[i].MSTM;
						MSTM=new Date(MSTM.time).format("yyyy-MM-dd hh:mm:ss");
						$("#stcd_zvarl_x").val(response.mZvarlFormBean[i].STCD);
						$("#stnm_zvarl_x").val(response.mZvarlFormBean[i].STNM);
						$("#mstm_zvarl_x").val(MSTM);
						var tr = "<tr>"+
						"<td style='text-align: left'><input style='margin-left: 15px;' class='a' readonly='readonly' name='ptno' id='ptno_zvarl_x' value="+response.mZvarlFormBean[i].PTNO+"></td>"+
						"<td style='text-align: left'><input style='margin-left: 15px;' class='a'  id='rz_zvarl_x' name='rz' value="+response.mZvarlFormBean[i].RZ+"></td>"+
						"<td style='text-align: left'><input style='margin-left: 15px;' class='a'  id='w_zvarl_x' name='w' value="+response.mZvarlFormBean[i].W+"></td>"+
						"<td style='text-align: left'><input style='margin-left: 15px;' class='a'  id='wsfa_zvarl_x' name='wsfa' value="+response.mZvarlFormBean[i].WSFA+"></td>"+
						/*"<td style='text-align: left'><input style='margin-left: 15px;' class='a'  id='mstm_zvarl_x' value="+response.mZvarlFormBean[i].MSTM+"></td>"+*/
						"<td style='text-align: left'>" +
						/*"<a href='#' onclick='addsj_st(this)'>&nbsp;&nbsp;保存</a>" +*/
						"<a href='#' onclick='del_st(this)'>&nbsp;&nbsp;删除</a>" +
						"</td>"+
					"</tr>";
						table.append(tr);
					}
					opt_all.edit_dialog_storageCapacity_x.find('.modal-title').html("<i class='icon icon-pencil'></i>&nbsp;修改库（湖）容");
					opt_all.edit_dialog_storageCapacity_x.modal({
						 show : true
						,backdrop : "static" // 背景遮挡
						,moveable : true
					}).on('hide.zui.modal', function() {
						_reset();//当第一次验证正确后，关闭窗体。再进来时，重置窗体(保留窗体上数据)。
		            });
				});
			}else{
				$("#stcd_zvarl").val("");
				$("#ids_zvarl").val("add");
				//先清除添加过的数据再弹窗
				opt_all.edit_dialog_storageCapacity.find('.modal-title').html("<i class='icon icon-plus-sign'></i>&nbsp;新增库（湖）容") ;
				opt_all.edit_dialog_storageCapacity.modal({
					 show : true
					,backdrop : "static" // 背景遮挡
					,moveable : true
				}).on('hide.zui.modal', function() {
					_reset();//当第一次验证正确后，关闭窗体。再进来时，重置窗体(保留窗体上数据)。
	            });
			}
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
	    	opt_all.query_table_storageCapacity.bootstrapTable('refresh');
	    }
	    this.flash=function(nm){
	    	engineeringNm=nm;
			_refresh();
		}
		// 重置主合同
		function _reset(){
			opt_all.info_form_storageCapacity.data('bootstrapValidator').resetForm(true);
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
		function mergeCells(data,fieldName,colspan,target){
		    //声明一个map计算相同属性值在data对象出现的次数和
		    var sortMap = {};
		    for(var i = 0 ; i < data.length ; i++){
		        for(var prop in data[i]){
		            if(prop == fieldName){
		                var key = data[i][prop]
		                if(sortMap.hasOwnProperty(key)){
		                    sortMap[key] = sortMap[key] * 1 + 1;
		                } else {
		                    sortMap[key] = 1;
		                }
		                break;
		            } 
		        }
		    }
		    var index = 0;
		    for(var prop in sortMap){
		        var count = sortMap[prop] * 1;
		        $(target).bootstrapTable('mergeCells',{index:index, field:fieldName, colspan: colspan, rowspan: count});   
		        index += count;
		    }
		}
		function x_xzcs(){
			var table = $("#query_table_storageCapacity_x");
			var tr = "<tr>"+
				"<td style='text-align: left'><input style='margin-left: 15px;' name='ptno' readonly='readonly' class='a' id='ptno_zvarl_x' ></td>"+
				"<td style='text-align: left'><input style='margin-left: 15px;' name='rz' class='a' id='rz_zvarl_x' ></td>"+
				"<td style='text-align: left'><input style='margin-left: 15px;' name='w' class='a' id='w_zvarl_x' ></td>"+
				"<td style='text-align: left'><input style='margin-left: 15px;' name='wsfa' class='a' id='wsfa_zvarl_x' ></td>"+
				/*"<td style='text-align: left'><input style='margin-left: 15px;' name='mZvarlFormBean.mZvarlInfoBean.mstm' class='a' id='mstm_zvarl_x' ></td>"+*/
				"<td style='text-align: left'>" +
				"<a href='#' onclick='del_st(this)'>&nbsp;&nbsp;删除</a>" +
				"</td>"+
			"</tr>";
			table.append(tr);
		}
	//删除参数
	del_st = function(obj){
		$(obj).parent().parent().remove();
	  }
	
	addsj_st = function(obj){
		var tr = obj.parentNode.parentNode; 
		var tds = tr.cells; 
		var str = ""; 
		for(var i = 0;i < tds.length-1;i++){
			str += $(tds[i].innerHTML).val()+",";
		}
		str=(str.substring(str.length-1)==',')?str.substring(0,str.length-1):str;
		var stcd = $("#stcd_zvarl_x").val();
		stcd +=",";
		var sj = stcd + str;
		var url = opt_all.url_save+"?mZvarlFormBean.idsup=" +sj;
		common_ajax(null,url, function(response) {
			
		});
	}
	
	};
})(jQuery);
			