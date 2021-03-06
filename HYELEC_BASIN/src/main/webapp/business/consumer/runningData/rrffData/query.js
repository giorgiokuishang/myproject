(function($) {
	// 当前工程内码
	var engineeringNm = sessionStorage.getItem("engineeringNm");
	$("body").children("#edit_dialog_rrff_x").remove();
	$("body").append($("#edit_dialog_rrff_x"));
	
	// option自定义参数覆盖
	// A、界面控件变量
	$.RrffData_Query = function(option) {
		var opt_control = {
			query_table_rrff 			: $("#query_table_rrff")		// 页面table表格
		   ,query_searchName_rrff 		: $("#query_searchName_rrff")	// 模糊查询关键字
		   ,query_ref				    : $("#query_ref_rrff")			// 查询按钮
		   ,query_info_rrffData			: $("#query_info_rrffData")		// 双击时间窗口ID
		   ,query_add_rrff				: $("#query_add_rrff")			// 新增按钮ID
		   ,info_form_rrffData			: $("#info_form_rrffData")		// 新增form表单
		   ,edit_dialog_rrffData		: $("#edit_dialog_rrffData")	// 新增窗口ID
		   ,stcd_rrff					: $("#stcd_rrff")				// 增加页面测站下拉框
		   ,btn_save					: $("#btn_rrff_save")				// 新增保存窗口
		   ,btn_del_rrff				: $("#btn_del_rrff")			// 批量删除按钮
		   ,info_upload					: $("#info_upload_rrff")		// 文件上传模态框
		   ,btn_into_rrff				: $("#btn_into_rrff")			// 导入按钮
		   ,btn_outAll_rrff				: $("#btn_outAll_rrff")			// 导入全部按钮
		   ,btn_outPage_rrff			: $("#btn_outPage_rrff")		// 导入当前页
		   ,edit_dialog_rrff_x			: $("#edit_dialog_rrff_x")		// 修改窗口ID
		   ,query_table_rrff_x			: $("#query_table_rrff_x")		// 修改表格ID
		   ,myEchart_rrff				: $("#myEchart_rrff")			// echart表格ID
		   ,query_add_rrff_x			: $("#query_add_rrff_x")		// 修改页面新增按钮ID
		   ,btn_save_rrff_x				: $("#btn_save_rrff_x")			// 修改页面保存按钮ID
		   ,info_form_rrff_x			: $("#info_form_rrff_x")		// 修改页面from表单ID
		   ,btn_muban_rrff				: $("#btn_muban_rrff")			// 下载模板
		}
		var opt_url = {
			url_list 					: basePath + "rrff/rrff!list.action" // 页面初始化加载表格url
		   ,url_upstcd_Stbprp  			: basePath + "stbprp/stbprp!upstcd_Stbprp.action"//测站下拉框
		   ,url_save					: basePath + "rrff/rrff!save.action"	//增加降雨径流
		   ,url_save_x					: basePath + "rrff/rrff!save_x.action"	//修改降雨径流
		   ,url_remove					: basePath + "rrff/rrff!removeids.action"	//删除url
		   ,url_import					: basePath + "rrff/rrff!importRrff.action" //导入url
		   ,url_export					: basePath + "rrff/rrff!export.action"//导出url
		   ,url_getUserName    			:basePath + "tsqx/tsqx!getUserName.action"//下拉列表
		}
		// 全部变量，自定义的覆盖默认变量
		var opt_all = ($.extend(opt_control, opt_url, option));
		this.InitData = function(opt) {
			opt_all=$.extend(opt_all,opt);  
			opt_all.query_table_rrff.bootstrapTable($.extend(
					g_bootstrapTable_Options, {
						url : opt_all.url_list // 请求后台的URL（*）
						,
						queryParams : queryParams // 传递参数（*）
						,
						onDblClickRow : onDblClickRow // 行双击事件
						,
						onSort : onSort // 排序事件
						,
						rowStyle : comm_rowStyle // 行样式，可自定义
						,
						onLoadSuccess : comm_onLoadSuccess // 数据加载错误
						,
						onCheckAll : onCheckAll // 全选
						,
						onCheck : onCheck // 单选
						,
						onUncheck : onUncheck // 不选
						,
						onUncheckAll : onUncheckAll // 全不选
						,
						singleSelect : false,
						uniqueId : "ID",
						pageSize : 15
					}));
			// 选中多行改变表格背景色
			function onCheckAll(rows) {
				for (var i = 0; i < rows.length; i++) {
					commRowStyle(i);
				}
			}
			// 循环改变所有行颜色
			function commRowStyle(i) {
				$("#query_table_rrff tbody tr[data-index=" + i + "]")
						.addClass("success");
			}
			// 全不选时颜色恢复
			function onUncheckAll() {
				$("#query_table_rrff tbody tr").removeClass("success");
			}
			// 选中一行改变表格背景色
			function onCheck(rows) {
				$("#query_table_rrff tbody tr[data-uniqueid=" + rows.id + "]")
						.addClass("success");
			}
			// 不选中时颜色恢复
			function onUncheck(rows) {
				$("#query_table_rrff tbody tr[data-uniqueid=" + rows.id + "]")
						.removeClass("success");
			}
			// 提交查询函数
			function queryParams(params) { // 配置参数
				// 查询条件
				var opt_parms = {
					"mRrffFormBean.searchName" : opt_control.query_searchName_rrff
							.val()// 查询关键字
				
				};
				var temp = { // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
					"mRrffFormBean.pageBean.limit" : params.limit // 页面大小
					,
					"mRrffFormBean.pageBean.offset" : params.offset // 当前记录偏移条数
					,
					"mRrffFormBean.pageBean.sort" : params.sort // 排序列名
					,
					"mRrffFormBean.pageBean.sortOrder" : params.order
				// 排位命令（desc，asc）
				};
				temp = $.extend(temp, opt_parms, opt);
				return temp;
			}
			// 双击事件
			function onDblClickRow(row) {
//				if (row) {
//					_table(row.STCD,row.USERNAME);
//				}
			}
			// 排序事件
			function onSort(name, order) {
				_refresh();
			}
			function comm_onLoadSuccess(data) {
				var data = $('#query_table_rrff').bootstrapTable('getData',
						true);
				console.log(JSON.stringify(data))
			}
		}//this.InitData
		//****绑定事件******end
	    // 刷新
	    function _refresh(){
	    	opt_all.query_table_rrff.bootstrapTable('refresh');
	    }
	    this.InitAddEditDel=function(opt){
	    	opt_all=$.extend(opt_all,opt);
	    	opt_all.query_ref.bind("click",event_ref);
	    	opt_all.query_add_rrff.bind("click",event_add);
	    	opt_all.btn_del_rrff.bind("click",event_del);
	    	opt_all.btn_into_rrff.bind("click",upload_model_show);
	    	opt_all.btn_outAll_rrff.bind("click",exportAllPptn);
	    	opt_all.btn_outPage_rrff.bind("click",exportPagePptn);
	    	opt_all.query_add_rrff_x.bind("click",x_xzcs);
	    	opt_all.btn_save_rrff_x.bind("click",save_x);
	    	opt_all.btn_save.bind("click",btn_save);
	    	opt_all.btn_muban_rrff.bind("click",downloadTemplate);
	    	
	    	//初始化主合同编辑表单 保存button类型为submit
			opt_all.info_form_rrffData.bootstrapValidator().on("success.form.bv", function(e) {
				e.preventDefault(); // 去掉默认提交事件
				// 校验数据正确,执行保存数据
				_save();
				
			}).on("error.form.bv", function(e) {
				e.preventDefault(); // 去掉默认提交事件
			});	
	    }//this.InitAddEditDel
		function x_xzcs(){//修改页面点击新增一行
			var table = $("#query_table_rrff_x");
			var tr = "<tr>"+
				"<td style='text-align: left'><input style='margin-left: 15px;width: 90px' name='pa' id='pa_rrff_x'></td>"+
				"<td style='text-align: left'><input style='margin-left: 15px;width: 90px' name='p' id='p_rrff_x'></td>"+
				"<td style='text-align: left'><input style='margin-left: 15px;width: 90px' name='r' id='r_rrff_x'></td>"+
				/*"<td style='text-align: left'><input style='margin-left: 15px;' id='moditime_zqrl_x'></td>"+*/
				"<td style='text-align: left'>" +
				/*"<a href='#' onclick='addsj_(this)'>&nbsp;&nbsp;保存</a>" +*/
				"<a href='javascript:void(0)' onclick='delsj(this)'>&nbsp;&nbsp;删除</a></td>"+
			"</tr>";
			table.append(tr);
		}
		// 保存降雨径流数据
		function _save() {
			// 参数需要保存的表单，保存url,需要更新的bootstrapTable,必须设置uniqueId: "id"
			var json=opt_all.info_form_rrffData.serialize();
			common_ajax(json, opt_all.url_save, function(response){
				opt_all.edit_dialog_rrffData.modal("hide");
				//重置表单
				_reset();
				// 刷新列表
				_refresh();
			});
		}
		//下载模板
		function downloadTemplate(){
			window.location.href=basePath+"/common/download/降雨径流表.xlsx"
		}
		//新增点击保存按钮
		var stnm1;
		function btn_save(){
			var name=$("#userName_rrff").val();
			var stcd=$('#stcd_rrff_').val();
			/*var stnm=$("#stcd_tsqx").find("option:selected").text();*/
			var stnm=$("#stnm_rrff").val();
			stnm1=stnm;
			if(stcd==""){
				alert("请选择1个测站");
				return false;
			}
			LoadEditData(stcd,name)//跳转到修改页面
		}
		
		//点击保存按钮
		function save_x(){
			var $form=$('#info_form_rrff_x').serialize();
			common_ajax($form, opt_all.url_save_x, function(response){
				opt_all.edit_dialog_rrffData.modal("hide");
	 			opt_all.edit_dialog_rrff_x.modal("hide");
			    _refresh();
	 		});
		}
		//导出当前页
		function exportPagePptn(){
			var url= opt_all.url_export+"?";
			var data_page="mRrffFormBean.pageBean.limit="+TableQueryParams.limit
						 +"&mRrffFormBean.pageBean.offset="+TableQueryParams.offset
						 +"&mRrffFormBean.pageBean.sortOrder="+TableQueryParams.order
						 +"&mRrffFormBean.pageBean.sort="+TableQueryParams.sort
						 +"&mRrffFormBean.searchName"+opt_all.query_searchName_rrff.val();
			confirm("<i class='icon icon-circle-arrow-up'></i>&nbsp;导出当前页","您确定要导出当前页吗？","icon-info", function(result) {
			   if(result){
				   window.location.href=url+data_page;
			   }
			});
		}
		//导出全部
		function exportAllPptn(){
			var url= opt_all.url_export+"?";
			var data="mRrffFormBean.pageBean.limit=99999999"
					+"&mRrffFormBean.pageBean.offset=0" 
					+"&mRrffFormBean.pageBean.sortOrder="+TableQueryParams.order
					+"&mRrffFormBean.pageBean.sort="+TableQueryParams.sort
					+"&mRrffFormBean.searchName="+opt_all.query_searchName_rrff.val();
			confirm("<i class='icon icon-reply'></i>&nbsp;导出全部","您确定要导出全部信息吗？","icon-info", function(result) {
			   if(result){
				   window.location.href=url+data;
               }
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
			$('#myUploader_rrff').uploader(file_options);
		}
		//上传文件
		this.uploading=function(contId,evalId,engineerCode){
			//履约评价的文件上传
			filesUpload_eval(evalId,engineerCode);
			//主合同的文件上传
			filesUpload(contId,engineerCode);
			filesUploadShow();
		}
		
		//单条删除
		this.del=function(stcd,userName){
			if(stcd != "" && userName != ""){
				_del(stcd,userName);
			}
		}
		//删除单条记录
		function _del(stcd,userName){
			var url = opt_all.url_remove+"?mRrffFormBean.mRrffInfoBean.stcd="+stcd+"&mRrffFormBean.mRrffInfoBean.userName="+userName;
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
		//绑定删除事件
		function event_del(){
			_removeids();
		}
		// 批量删除
		function _removeids(){
           // 获取删除选中的ids
           var stcd = g_select_and_tip(opt_all.query_table_rrff,"STCD");
           var userName = g_select_and_tip(opt_all.query_table_rrff,"USERNAME");
           if (stcd.length==0 && userName.length==0) return;
           $(".fixed-table-container tbody tr.selected td").addClass("row-bcground");
           
           var url= opt_all.url_remove+"?mRrffFormBean.mRrffInfoBean.stcd="+stcd+"&mRrffFormBean.mRrffInfoBean.userName="+userName;
            
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
		this.edit=function(stcd,userName) {
			_edit(stcd,userName);
		}
	    //绑定刷新事件
		function event_ref(){
			 _refresh();
		}
		
		function event_add(){
			var url = opt_all.url_getUserName;
			// 动态加载页面数据
			$.ajax({
				url:url,
				dataType:'json',
				success:function(response){
					$("#userName_rrff").val(response.userName)
				}
			})
			_edit("");
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
		}//_edit
		//加载所有外键表到下拉框，无
		function Load_EditSelectData(callback){
			//所有编辑页面下拉框加载
			callback();
		}
		//下拉数据初始化
		this.InitSelect = function (){
			Load_select_stcd_zqrl();
		}
		function Load_select_stcd_zqrl(){
			opt_control.stcd_rrff.empty();
			var url = opt_all.url_upstcd_Stbprp;
			opt_control.stcd_rrff.append("<option value=''>请选择测站名称</option>");
			common_ajax(null,url, function(data) {
				console.log(JSON.stringify(data.rows))
				$.each(data.rows, function(i) {
					opt_control.stcd_rrff.append(
						'<option value=' + data.rows[i].STCD + '>'+ data.rows[i].STNM + '</option>');
				});
			});
		}
		//新增窗口 和修改窗口
		function LoadEditData(stcd,userName){
			var Pa = [];
			var P = [];
			var R = [];
			var index = [];
			if(userName != "" && stcd !=""){
				var url = opt_all.url_list+"?mRrffFormBean.mRrffInfoBean.stcd=" + stcd+"&mRrffFormBean.mRrffInfoBean.userName=" + userName;
				// 动态加载页面数据
				common_ajax(null,url, function(response) {
					var table = $("#query_table_rrff_x");
					table.empty();
				
					var tr = "<tr>"+
					"<td style='width: 130px; text-align: center; font-weight: bold; background: #f1f1f1'>"+
						"<lable>前期影响雨量(mm)</lable></td>"+
					"<td style='width: 90px; text-align: center; font-weight: bold; background: #f1f1f1'>"+
						"<lable>降雨量(mm)</lable></td>"+
					"<td style='width: 90px; text-align: center; font-weight: bold; background: #f1f1f1'>"+
						"<lable>径流(mm)</lable></td>"+
					"<td style='width: 70px; text-align: center; font-weight: bold; background: #f1f1f1'>"+
						"<lable>操作</lable></td></tr>";
					table.append(tr);
					if(response.rows.length==0){//如果没数据
						$("#stnm_rrff_x").val(stnm1);//STNM赋值
						$("#userName_rrff_x").val(userName);//username赋值
						$("#stcd_rrff_x").val(stcd);//stcd赋值
					}else{
						for(var i =0;i<response.rows.length;i++){
							$("#stcd_rrff_x").val(response.rows[i].STCD);
							$("#stnm_rrff_x").val(response.rows[i].STNM);
							$("#userName_rrff_x").val(response.rows[i].USERNAME);
						Pa.push(response.rows[i].Pa);
						P.push(response.rows[i].P);
						R.push(response.rows[i].R);
						index.push(i);
						var tr = "<tr>"+
						"<td style='text-align: left'><input style='margin-left: 15px;width: 90px' class='a' id='pa_rrff_x"+i+"' value="+response.rows[i].Pa+" name='pa'></td>"+
						"<td style='text-align: left'><input style='margin-left: 15px;width: 90px' class='a' id='p_rrff_x"+i+"' value="+response.rows[i].P+" name='p'></td>"+
						"<td style='text-align: left'><input style='margin-left: 15px;width: 90px' class='a' id='r_rrff_x"+i+"' value="+response.rows[i].R+" name='r'></td>"+
						"<td style='text-align: left'>" +
						"<a href='#' onclick='delsj(this)'>&nbsp;&nbsp;删除</a></td>"+
					"</tr>";
						table.append(tr);
						}
					}
					if(response.rows.length==0){//如果没数据
						opt_all.edit_dialog_rrff_x.find('.modal-title').html("<i class='icon icon-pencil'></i>&nbsp;新增降雨径流信息");
					}else{
						opt_all.edit_dialog_rrff_x.find('.modal-title').html("<i class='icon icon-pencil'></i>&nbsp;修改降雨径流信息");
					}
					opt_all.edit_dialog_rrff_x.modal({
						 show : true
						,backdrop : false // 背景遮挡
						,moveable : true
					}).on('hide.zui.modal', function() {
						_reset();//当第一次验证正确后，关闭窗体。再进来时，重置窗体(保留窗体上数据)。
		            });
					//加载折线图
					loadJyjlzxtEchar();
			});
			}else{
				/*$("#stcd_zqrl").val("");
				$("#ids_zqrl").val("add");*/
				//先清除添加过的数据再弹窗
				opt_all.edit_dialog_rrffData.find('.modal-title').html("<i class='icon icon-plus-sign'></i>&nbsp;新增降雨径流");
				opt_all.edit_dialog_rrffData.modal({
					 show : true
					,backdrop : false // 背景遮挡
					,moveable : true
				}).on('hide.zui.modal', function() {
					_reset();//当第一次验证正确后，关闭窗体。再进来时，重置窗体(保留窗体上数据)。
	            });
			}
		}
		function getJyjlzxtEchartData(){
			var dataList=[];
			$("#query_table_rrff_x").find("tr").each(function(index,tr){
				if(index>0){
					var data={};
					$(tr).find("input").each(function(i,input){
						if(i==0){
							data.p=$(input).val();
						}else if(i==1){
							data.q=$(input).val();
						}else if(i==2){
							data.r=$(input).val();
						}
					});
					dataList.push(data);
				}
			});
			var dataJson={};
			if(dataList!=null && dataList.length>0){
				for(var i=0;i<dataList.length;i++){
					var data=dataList[i];
					if(data!=null && data.p!=null){
						var klist=dataJson["key_"+data.p];
						if(klist){
							klist.p=data.p;
							klist.data.push(data);
						}else{
							klist={p:data.p,data:[data]};
						}
						dataJson["key_"+data.p]=klist;
					}
				}
			}
			var lineList = [];
			for(var key in dataJson){
				lineList.push(dataJson[key]);
			}
			lineList.sort(function(a, b) {
				return parseFloat(a.p)-parseFloat(b.p);
			});
			return lineList;
		}
		function loadJyjlzxtEchar(){
			var lineList = getJyjlzxtEchartData();
			var series=[];
			var legend=[];
			if(lineList!=null && lineList.length>0){
				for(var i=0;i<lineList.length;i++){
					var line = lineList[i];
					var data = line.data;
					data.sort(function(a, b) {
						return parseFloat(a.r)-parseFloat(b.r);
					});
					var point = [];
					if(data!=null && data.length>0){
						for(var j=0;j<data.length;j++){
							var p=data[j];
							point.push([p.r,p.q]);
						}
					}
					legend.push(line.p);
					series.push({
						name:line.p,
						type: 'line',
						smooth:true,
						smoothMonotone:'none',
						data: point
					});
				}
			}
			var myChart = echarts.init(document.getElementById('myEchart_rrff'));
			option = {
					title: {
						text: '降雨径流折线图'
					},
					tooltip: {
						trigger: 'axis',
						formatter:function(params){
							var tip="";
							if(params && params.length>0){
								for(var i=0;i<params.length;i++){
									var param = params[i];
									if(param && param.data && param.data.length>0 && param.data[0]!=null && param.data[1]!=null){
										tip=tip+"前期影响雨量:"+param.seriesName+",径流:"+param.data[0]+",降雨量:"+param.data[1]+"<br/>";
									}
								}
							}
							return tip;
						}
					},
					legend: {
						 data:legend
						 },
					xAxis: {
						name:'R(mm)',
						type: 'value'
						},
					yAxis: {
						name:'P(mm)',
						type: 'value'
						},
					series: series
				};	
            var monthLineChart = echarts.init(document.getElementById("myEchart_rrff"));
            //清空画布，防止缓存
            monthLineChart.clear();
            myChart.setOption(option); 
		}
		  //修改页面键盘抬起事件
	    $("#query_table_rrff_x").keyup(function(){
	    	 //统计图表初始化
	    	loadJyjlzxtEchar();
			})
		//删除参数
		delsj = function(obj){
	    	len=0;
	    	$(obj).parent().parent().remove();
	    	loadJyjlzxtEchar();
		  }
		 // 重置窗体
	    function _reset(){
			opt_all.info_form_rrffData.data('bootstrapValidator').resetForm(true);
		}
		//显示详情
		function _table(queryId,userName){
			var url = opt_all.url_list+"?mRrffFormBean.mRrffInfoBean.stcd=" + queryId+"&mRrffFormBean.mRrffInfoBean.userName=" + userName;
			common_ajax(null,url, function(response) {
				for(var key in response.rows[0]){
				   if($("#"+key+"_rrffDetail_s")[0]){
					   $("#"+key+"_rrffDetail_s").html(response.rows[0][key]);
				   }
				}
			});
			opt_all.query_info_rrffData.modal({
				 show 	   : true
				,backdrop  : "static" // 背景遮挡
				,moveable  : true
			}).on('shown.zui.modal', function() {
			});
		}//_table
	}//$.RrffData_Query
})(jQuery);
			
