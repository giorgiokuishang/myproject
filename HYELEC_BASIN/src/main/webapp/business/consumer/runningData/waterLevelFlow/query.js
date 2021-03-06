(function($) {
	//当前工程内码
	var engineeringNm = sessionStorage.getItem("engineeringNm");
	$("body").children("#edit_dialog_waterLevelFlow_x").remove();
	$("body").append($("#edit_dialog_waterLevelFlow_x"));
	
	$.WaterLevelFlow_Query = function(option) {
		//option自定义参数覆盖
		//A、界面控件变量
		var opt_control={
			query_table_waterLevelFlow 	:$('#query_table_waterLevelFlow')		//页面BootStrapTable的ID
			,query_searchName			:$('#query_searchName_zqrl')	//页面模糊查询input
			,query_info_waterLevelFlow	:$("#query_info_waterLevelFlow")//双击事件窗口id
			,query_ref					:$("#query_ref_zqrl")		//模糊查询
			,query_form					:$("#query_waterlevel_form")
			,edit_dialog_waterLevelFlow	:$("#edit_dialog_waterLevelFlow")//添加编辑窗口
			,edit_dialog_waterLevelFlow_x:$("#edit_dialog_waterLevelFlow_x")
			,query_add_waterLevelFlow	:$("#query_add_waterLevelFlow")		//添加按钮
			,btn_del_waterLevelFlow		:$("#btn_del_waterLevelFlow")			//批量删除按钮
			,info_form_waterLevelFlow	:$("#info_form_waterLevelFlow")		//添加编辑的表单
			,info_form_waterLevelFlow_x	:$("#info_form_waterLevelFlow_x")
			,info_upload				:$("#info_upload_zqrl")	//文件上传模态框
			,btn_into_zqrl				:$("#btn_into_zqrl")	//导入按钮
			,btn_outAll_zqrl	    	:$('#btn_outAll_zqrl') //导出全部按钮
			,btn_outPage_zqrl			:$('#btn_outPage_zqrl') //导出当前页按钮
			,btn_save					:$('#btn_waterlevel_save')		//保存按钮
			,btn_stcd_zqrl				:$('#stcd_zqrl')		//测站下拉框
			,btn_stcd_zqrl_x			:$('#stcd_zqrl_x')
			,query_add_waterLevelFlow_x :$("#query_add_waterLevelFlow_x")
			,btn_waterlevel_save_add					:$('#btn_waterlevel_save_add')
			,btn_save_x					:$('#btn_save_x')
			,myEcharts					:$('#myEcharts')
			,btn_muban_zqrl				:$('#btn_muban_zqrl')//下载模板
		}
		//B、请求地址URL
		var opt_url={
			url_list			:basePath + "zqrl/zqrl!list.action"//查询数据URL
			,url_save			:basePath + "zqrl/zqrl!save.action"//添加或修改保存主合同URL
			,url_save_x			:basePath + "zqrl/zqrl!saveX.action"//添加或修改保存主合同URL
			,url_remove			:basePath + "zqrl/zqrl!removeids.action"//删除URL
			,url_edit			:basePath + "zqrl/zqrl!edit.action"//修改查询数据
			,url_import			:basePath + "zqrl/zqrl!importPptn.action"//导入
			,url_export			:basePath + "zqrl/zqrl!export.action"//导出
			,url_upstcd_Stbprp  :basePath + "stbprp/stbprp!upstcd_Stbprp.action"//上游编码
		}
		//全部变量，自定义的覆盖默认变量
		var opt_all=($.extend(opt_control,opt_url,option));
	    //////////////////////////////////////////////////////////////////////////////////////////////////
		this.InitData=function(opt){
			opt_all=$.extend(opt_all,opt);   
			opt_all.query_table_waterLevelFlow.bootstrapTable($.extend(g_bootstrapTable_Options,
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
			    $("#query_table_waterLevelFlow tbody tr[data-index="+i+"]").addClass("success");
			   }
			   //全不选时颜色恢复
			   function onUncheckAll(){
			    $("#query_table_waterLevelFlow tbody tr").removeClass("success");
			   }
			   //选中一行改变表格背景色
			   function onCheck(rows){
			    $("#query_table_waterLevelFlow tbody tr[data-uniqueid="+rows.id+"]").addClass("success");
			   }
			   //不选中时颜色恢复
			   function onUncheck(rows){
			    $("#query_table_waterLevelFlow tbody tr[data-uniqueid="+rows.id+"]").removeClass("success");
			   }
			// 提交查询函数
			function queryParams(params) {  // 配置参数
				//查询条件
				TableQueryParams = params;
				var opt_parms={
					 "mZqrlFormBean.searchName"			:opt_control.query_searchName.val() // 查询关键字
					 
				};
			    var temp = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			       "mZqrlFormBean.pageBean.limit"			: params.limit   // 页面大小
			      ,"mZqrlFormBean.pageBean.offset"			: params.offset  // 当前记录偏移条数
			      ,"mZqrlFormBean.pageBean.sort"			: params.sort  	  // 排序列名
			      ,"mZqrlFormBean.pageBean.sortOrder" 		: params.order   // 排位命令（desc，asc）
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
			
			function comm_onLoadSuccess(data){
					var data = $('#query_table_waterLevelFlow').bootstrapTable('getData', true);
					/*mergeCells(data,"STNM",1, $('#query_table_waterLevelFlow'));
					mergeCells(data,"LNNM",1, $('#query_table_waterLevelFlow'));
					mergeCells(data,"STCD",1, $('#query_table_waterLevelFlow'));*/
					
			}
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		// 初始化增加、修改和删除,公开函数
		this.InitAddEditDel=function(opt){
			opt_all=$.extend(opt_all,opt);
			opt_all.query_add_waterLevelFlow.bind("click",event_add);
			opt_all.btn_del_waterLevelFlow.bind("click",event_del);
			opt_all.query_ref.bind("click",event_ref);
			opt_all.btn_into_zqrl.bind("click",upload_model_show);
			opt_all.btn_outPage_zqrl.bind("click",exportPagePptn);
			opt_all.btn_outAll_zqrl.bind("click",exportAllPptn);
			opt_all.query_add_waterLevelFlow_x.bind("click",x_xzcs);
			opt_all.btn_save_x.bind("click",btn_save_x);
			opt_all.btn_waterlevel_save_add.bind("click",btn_waterlevel_save_add);
			opt_all.btn_muban_zqrl.bind("click",downloadTemplate);
			
			//初始化主合同编辑表单 保存button类型为submit
			opt_all.info_form_waterLevelFlow.bootstrapValidator().on("success.form.bv", function(e) {
				e.preventDefault(); // 去掉默认提交事件
				// 校验数据正确,执行保存数据
				_save();
				
			}).on("error.form.bv", function(e) {
				e.preventDefault(); // 去掉默认提交事件
			});	
		}
		//下拉数据初始化
		this.InitSelect = function (){
			Load_select_stcd_zqrl();
			Load_select_stcd_zqrl_x();
		}
		
		//下载模板
		function downloadTemplate(){
			window.location.href=basePath+"/common/download/水位流量关系曲线表.xlsx"
		}
		
		function Load_select_stcd_zqrl(){
			opt_control.btn_stcd_zqrl.empty();
			var url = opt_all.url_upstcd_Stbprp;
			opt_control.btn_stcd_zqrl.append("<option value=''>请选择测站名称</option>");
			common_ajax(null,url, function(data) {
				console.log(JSON.stringify(data.rows))
				$.each(data.rows, function(i) {
					opt_control.btn_stcd_zqrl.append(
						'<option value=' + data.rows[i].STCD + '>'+ data.rows[i].STNM + '</option>');
				});
			});
		}
		function Load_select_stcd_zqrl_x(){
			opt_control.btn_stcd_zqrl_x.empty();
			var url = opt_all.url_upstcd_Stbprp;
			opt_control.btn_stcd_zqrl_x.append("<option value=''>请选择测站名称</option>");
			common_ajax(null,url, function(data) {
				$.each(data.rows, function(i) {
					opt_control.btn_stcd_zqrl_x.append(
						'<option value=' + data.rows[i].STCD + '>'+ data.rows[i].STNM + '</option>');
				});
			});
		}
		
		//显示详情
		function _table(queryId){
			//alert(ptnoId);
			var url = opt_all.url_list+"?mZqrlFormBean.mZqrlInfoBean.stcd=" + queryId;
			common_ajax(null,url, function(response) {
				for(var key in response.rows[0]){
				   if($("#"+key+"_pptnDetail_s")[0]){
					   $("#"+key+"_pptnDetail_s").html(response.rows[0][key]);
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
				$("#MODITIME_pptnDetail_s").html(tm);
			});
			opt_all.query_info_waterLevelFlow.modal({
				show 	   : true
				,backdrop  : "static" // 背景遮挡
					,moveable  : true
			}).on('shown.zui.modal', function() {
			});
		}
		
	    // 修改记录，调出窗体,公开函数
		this.edit=function(stcd,lnnm) {
			_edit(stcd,lnnm);
		}
		//单条删除
		this.del=function(stcd,lnnm){
			if(stcd != "" && lnnm != ""){
				_del(stcd,lnnm);
			}
		}
		//绑定删除事件
		function event_del(){
			_removeids();
		}
		// 批量删除
		function _removeids(){
           // 获取删除选中的ids
           var stcd = g_select_and_tip(opt_all.query_table_waterLevelFlow,"STCD");
           var lnnm = g_select_and_tip(opt_all.query_table_waterLevelFlow,"LNNM");
           var ptno = g_select_and_tip(opt_all.query_table_waterLevelFlow,"PTNO");
           if (stcd.length==0 && lnnm.length==0 && ptno.length==0) return;
           $(".fixed-table-container tbody tr.selected td").addClass("row-bcground");
           
           var url= opt_all.url_remove+"?stcd="+stcd+"&lnnm="+lnnm+"&ptno="+ptno;
            
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
		function _del(stcd,lnnm){
			var url= opt_all.url_remove+"?stcd="+stcd+"&lnnm="+lnnm+"&flag=1";
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
		function _edit(stcd,lnnm){
			//加载Edit页面基本选择数据，成功后调用显示编辑页面
			//加载字典分类数据下拉框数据
			Load_EditSelectData( 
				function(){ 
					LoadEditData(stcd,lnnm);
				}
			);
		}
		
		
		// 保存水位流量关系曲线数据
		function _save() {
			// 参数需要保存的表单，保存url,需要更新的bootstrapTable,必须设置uniqueId: "id"
	 		var json=opt_all.info_form_waterLevelFlow.serialize();
	 		common_ajax(json, opt_all.url_save, function(response){
	 			opt_all.edit_dialog_waterLevelFlow.modal("hide");
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
			$('#myUploader_zqrl').uploader(file_options);
		}
		
		//上传文件
		this.uploading=function(contId,evalId,engineerCode){
			//履约评价的文件上传
			filesUpload_eval(evalId,engineerCode);
			//主合同的文件上传
			filesUpload(contId,engineerCode);
			filesUploadShow();
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
		//导出全部
		function exportAllPptn(){
			var url= opt_all.url_export+"?";
			var data="mZqrlFormBean.pageBean.limit=99999999"
					+"&mZqrlFormBean.pageBean.offset=0" 
					+"&mZqrlFormBean.pageBean.sortOrder="+TableQueryParams.order
					+"&mZqrlFormBean.pageBean.sort="+TableQueryParams.sort
					+"&mZqrlFormBean.searchName="+opt_all.query_searchName.val();
			confirm("<i class='icon icon-reply'></i>&nbsp;导出全部","您确定要导出全部信息吗？","icon-info", function(result) {
			   if(result){
				   window.location.href=url+data;
               }
			});
		}
		
		//导出当前页
		function exportPagePptn(){
			var url= opt_all.url_export+"?";
			var data_page="mZqrlFormBean.pageBean.limit="+TableQueryParams.limit
						 +"&mZqrlFormBean.pageBean.offset="+TableQueryParams.offset
						 +"&mZqrlFormBean.pageBean.sortOrder="+TableQueryParams.order
						 +"&mZqrlFormBean.pageBean.sort="+TableQueryParams.sort
						 +"&mPptnFormBean.searchName"+opt_all.query_searchName.val();
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
		
		//字符串转日期
		function formatDate(date,format)
		{
			if(!date)
			{
				return '';
			}
			else if(typeof(date) == "string")
			{
				if("null"==date)
				{
					return '';
				}
				return date;
			}
			else if(typeof(date) == "undefined" || "undefined"==date)
			{
				return '';
			}
			else if(typeof(date) == "object")
			{
				var year = date.year + 1900;
				var month = (date.month + 1)>=10 ? (date.month + 1):"0"+(date.month + 1);
				var day = date.date>=10 ? date.date:"0"+date.date;
				var hours = date.hours>=10 ? date.hours:"0"+date.hours;
				var minutes = date.minutes>=10 ? date.minutes:"0"+date.minutes;
				var seconds = date.seconds>=10 ? date.seconds:"0"+date.seconds;
				if(format && "hh:mi:ss" == format)
				{
					return hours+":"+minutes+":"+seconds
				}
				else if(format && "yyyy-mm-dd" == format)
				{
					return year+"-"+month+"-"+day
				}
				else if(format && "yyyy-mm-dd hh:mi:ss" == format)
				{
					return year+"-"+month+"-"+day+" "+hours+":"+minutes+":"+seconds
				}
				else if(format && "yyyy/mm/dd" == format)
				{
					return year+"/"+month+"/"+day
				}
				else if(format && "yyyy/mm/dd hh:mi:ss" == format)
				{
					return year+"/"+month+"/"+day+" "+hours+":"+minutes+":"+seconds
				}
				return year+"-"+month+"-"+day+" "+hours+":"+minutes+":"+seconds;
			}
			return date;
		}
		
		
		//加载Edit页面上数据，并调出增加或编辑页面显示
		function LoadEditData(stcd,lnnm){
			len=0;
			var times=[];// 用来盛放X轴坐标值：时间
			var waterLevel=[];// 用来盛放Y坐标值：水位
			var flow=[];// 用来盛放Y坐标值：流量
			if(lnnm != "" && stcd!=""){
				var url = opt_all.url_edit+"?mZqrlFormBean.mZqrlInfoBean.stcd=" + stcd+"&mZqrlFormBean.mZqrlInfoBean.lnnm="+lnnm;
				// 动态加载页面数据
				common_ajax(null,url, function(response) {
					debugger;
					var table = $("#query_table_waterLevelFlow_x");
					table.empty();
					var tr = "<tr>"+
								"<td style='width: 90px; text-align: center; font-weight: bold; background: #f1f1f1'>"+
									"<lable>曲线序号</lable></td>"+
								"<td style='width: 90px; text-align: center; font-weight: bold; background: #f1f1f1'>"+
									"<lable>水位(m)</lable></td>"+
								"<td style='width: 90px; text-align: center; font-weight: bold; background: #f1f1f1'>"+
									"<lable>流量(m<sup>3</sup>/s)</lable></td>"+
								/*"<td style='width: 100px; text-align: center; font-weight: bold; background: #f1f1f1'>"+
									"<lable>修改时间：</lable></td>"+*/
								"<td style='width: 90px; text-align: center; font-weight: bold; background: #f1f1f1'>"+
									"<lable>操作</lable></td></tr>";
					table.append(tr);
					if(response.mZqrlFormBean.length==0){
						$("#stcd_zqrl_x").val(stcd);
						$("#stnm_zqrl_x").val(stnm1);
						$("#lnnm_zqrl_x").val(lnnm);
						opt_all.edit_dialog_waterLevelFlow_x.find('.modal-title').html("<i class='icon icon-pencil'></i>&nbsp;新增水位流量");
					}else{
						for(var i =0;i<response.mZqrlFormBean.length;i++){
							/*len=Math.max(response.mZqrlFormBean[i].PTNO);*/
							$("#stcd_zqrl_x").val(response.mZqrlFormBean[i].STCD);
							$("#stnm_zqrl_x").val(response.mZqrlFormBean[i].STNM);
							$("#lnnm_zqrl_x").val(response.mZqrlFormBean[i].LNNM);
							var tr = "<tr>"+
							/*"<td style='text-align: left'><input style='margin-left: 15px;width: 90px' class='a' readonly=true value="+response.mZqrlFormBean[i].PTNO+" name='ptno'></td>"+*/
							"<td style='text-align: left'><input style='margin-left: 15px;width: 90px' class='a' readonly=true value="+(1+len++)+" name='ptno'></td>"+
							"<td style='text-align: left'><input style='margin-left: 15px;width: 90px' class='a' value="+response.mZqrlFormBean[i].Z+" name='z'></td>"+
							"<td style='text-align: left'><input style='margin-left: 15px;width: 90px' class='a' value="+response.mZqrlFormBean[i].Q+" name='q'></td>"+
							/*"<td style='text-align: left'><input style='margin-left: 15px;' class='a' value="+response.mZqrlFormBean[i].MODITIME+" name='mZqrlFormBean.mZqrlInfoBean.moditime'></td>"+*/
							"<td style='text-align: left'>" +
							/*"<a href='#' onclick='addsj_(this)'>&nbsp;&nbsp;保存</a>" +*/
							"<a href='#' onclick='delsjs(this)'>&nbsp;&nbsp;删除</a></td>"+
						"</tr>";
							table.append(tr);
						}
						opt_all.edit_dialog_waterLevelFlow_x.find('.modal-title').html("<i class='icon icon-pencil'></i>&nbsp;修改水位流量");
					}
					
					
					opt_all.edit_dialog_waterLevelFlow_x.modal({
						 show : true
						,backdrop : false // 背景遮挡
						,moveable : true
					}).on('hide.zui.modal', function() {
						_reset();//当第一次验证正确后，关闭窗体。再进来时，重置窗体(保留窗体上数据)。
		            });
		            //统计图表初始化
		            loadSwllgxqxEchart();
				});
			}else{
				$("#stcd_zqrl").val("");
				$("#ids_zqrl").val("add");
				//先清除添加过的数据再弹窗
				opt_all.edit_dialog_waterLevelFlow.find('.modal-title').html("<i class='icon icon-plus-sign'></i>&nbsp;新增水位流量");
				opt_all.edit_dialog_waterLevelFlow.modal({
					 show : true
					,backdrop : false // 背景遮挡
					,moveable : true
				}).on('hide.zui.modal', function() {
					_reset();//当第一次验证正确后，关闭窗体。再进来时，重置窗体(保留窗体上数据)。
	            });
			}
		}
		function resetTrindex(){
			$("#query_table_waterLevelFlow_x").find("tr").each(function(index,tr){
				if(index>0){
					$(tr).find("input").each(function(i,input){
						if(i==0){
							$(input).val(index);
						}
					});
				}
			});
		}
		function getLoadSwllgxqxEchartData(){
			var dataList=[];
			$("#query_table_waterLevelFlow_x").find("tr").each(function(index,tr){
				var data={};
				$(tr).find("input").each(function(i,input){
					if(i==1){
						data.z=$(input).val();
					}else if(i==2){
						data.q=$(input).val();
					}
				});
				dataList.push(data);
			});
			dataList.sort(function(a, b) {
				return parseFloat(a.z)-parseFloat(b.z);
			})
			return dataList;
		}
		function loadSwllgxqxEchart(){
			var dataList = getLoadSwllgxqxEchartData();
			var pointList = [];
			var minY=dataList[0].q;
			var maxY=dataList[0].q;
			var minX=dataList[0].z;
			var maxX=dataList[0].z;
			if(dataList!=null && dataList.length>0){
				for(var i=0;i<dataList.length;i++){
					var data = dataList[i];
					pointList.push([data.z,data.q]);
				}
			}
			var myChart = echarts.init(document.getElementById('myEcharts'));
            option = {// 指定图表的配置项和数据
            		tooltip: {
						trigger: 'axis',
						formatter:function(params,ticket,callback){
							return "水位:"+params[0].data[0]+"<br/>流量:"+params[0].data[1];
						}
					},
				grid:{
					right:'100px'
				},
				 yAxis: {
				    	name : '流量(m³/s)',
    					type : 'value',
    					scale:true
				    },
			    xAxis: {
			    	name:'水位(m)',
    				type : 'value',
					scale:true
			    },
			   
			    series: {
			    	name:'水位',
					type:'line',
					smooth:true,
					smoothMonotone:'none',
					data:pointList
			    }
            		/*title : {
            			text: '水位流量关系曲线',
            			//subtext: '2018-03',
            			x: 'center'
            		},
            		tooltip : {
            			trigger: 'axis'
            		},
            		legend: {
            			data:['水位','流量'],
            			x: 'left'
            		},
            		dataZoom : {
            			show : true,
            			realtime : true,
            			start : 0,
            			end : 100
            		},
            		xAxis : 
            			{
            				name:'水位',
            				type : 'value'
            			}
            			,
            			yAxis : 
            				{
            					name : '流量(mm)',
            					type : 'value'
            				}
            			,
            				series : [
            					{
            						name:'水位',
            						type:'line',
            						itemStyle: {normal: {areaStyle: {type: 'default'}}},
            						data:pointList
            					}
            				]*/
            };
            var monthLineChart = echarts.init(document.getElementById("myEcharts"));
            //清空画布，防止缓存
            monthLineChart.clear();
            myChart.setOption(option); 
		}
		function event_add(){
			_edit("","");
		}

		//点击保存按钮
		function btn_save_x(){
			var $form=$('#info_form_waterLevelFlow_x').serialize();
			common_ajax($form, opt_all.url_save_x, function(response){
	 			opt_all.edit_dialog_waterLevelFlow_x.modal("hide");
	 			opt_all.edit_dialog_waterLevelFlow.modal("hide");
			    _refresh();
	 		});
		}
		//点新增之后点保存
		var stnm1;
		function btn_waterlevel_save_add(){
			debugger;
			var stcd=$("#stcd_zqrl").val();
			var stnm=$("#stcd_zqrl").val();
			stnm1=stnm;
			var lnnm=$("#lnnm_zqrl").val();
			if(stcd==""){
				alert("请选择1个测站");
				return false;
			}
			if(lnnm==""){
				alert("曲线名称不能为空");
				return false;
			}
			LoadEditData(stcd,lnnm)//跳转到修改页面
		}
		//绑定刷新事件
		function event_ref(){
			 _refresh();
		}
		
		
		//****绑定事件******end
	    // 刷新
	    function _refresh(){
	    	opt_all.query_table_waterLevelFlow.bootstrapTable('refresh');
	    }
	 // 重置窗体
	    function _reset(){
			opt_all.info_form_waterLevelFlow.data('bootstrapValidator').resetForm(true);
		}
	    this.flash=function(nm){
	    	engineeringNm=nm;
			_refresh();
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
			var table = $("#query_table_waterLevelFlow_x");
			var tr = "<tr>"+
				"<td style='text-align: left'><input style='margin-left: 15px;width: 90px' readonly=true; value='"+(++len)+"' name='ptno'></td>"+
				"<td style='text-align: left'><input style='margin-left: 15px;width: 90px' name='z' id='z_zqrl_x'></td>"+
				"<td style='text-align: left'><input style='margin-left: 15px;width: 90px' name='q' id='q_zqrl_x'></td>"+
				/*"<td style='text-align: left'><input style='margin-left: 15px;' id='moditime_zqrl_x'></td>"+*/
				"<td style='text-align: left'>" +
				/*"<a href='#' onclick='addsj_(this)'>&nbsp;&nbsp;保存</a>" +*/
				"<a href='javascript:void(0)' onclick='delsjs(this)'>&nbsp;&nbsp;删除</a></td>"+
			"</tr>";
			table.append(tr);
			resetTrindex();
		}
	    //修改页面键盘抬起事件
	    $("#query_table_waterLevelFlow_x").keyup(function(){
	    	 //统计图表初始化
            loadSwllgxqxEchart();
   		})
		//删除参数
		delsjs = function(obj){
			len=0;
			$(obj).parent().parent().remove();
			loadSwllgxqxEchart();
			resetTrindex();
		}
	};
	
})(jQuery);
			
;