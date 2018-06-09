(function($) {
	//当前工程内码
	var engineeringNm = sessionStorage.getItem("engineeringNm");
	$.SectionTest_Query = function(option) {
		//option自定义参数覆盖
		//A、界面控件变量
		var opt_control={
			query_table_sectionTest :$('#query_table_sectionTest')		//页面BootStrapTable的ID
			,query_searchName		:$('#query_searchName_rvsect')	//页面模糊查询input
			,query_info_sectionTest	:$("#query_info_sectionTest")	//双击事件窗口id
			,query_ref				:$("#query_ref_rvsect")		//模糊查询
			,query_form				:$("#query_form")
			,edit_dialog_sectionTest:$("#edit_dialog_sectionTest")//添加编辑窗口
			,edit_dialog_sectionTest_x:$("#edit_dialog_sectionTest_x")//添加编辑窗口
			,query_add_sectionTest	:$("#query_add_sectionTest")//添加按钮
			,query_add_sectionTest_x	:$("#query_add_sectionTest_x")//添加按钮
			,btn_del_sectionTest	:$("#btn_del_sectionTest")//批量删除按钮
			,info_form_sectionTest	:$("#info_form_sectionTest")//添加编辑的表单
			,info_form_sectionTest_x	:$("#info_form_sectionTest_x")//添加编辑的表单
			,info_upload			:$("#info_upload")	//文件上传模态框
			,btn_into_pptn			:$("#btn_into_pptn_rvsect")	//导入按钮
			,btn_outAll_pptn	    :$('#btn_outAll_pptn_rvsect') //导出全部按钮
			,btn_outPage_pptn		:$('#btn_outPage_pptn_rvsect') //导出当前页按钮
			,btn_save				:$('#btn_save')		//保存按钮
			,btn_stcd_rvsect		:$('#stcd_rvsect')		//测站下拉框
			,btn_stcd_rvsect_x		:$('#stcd_rvsect_x')		//测站下拉框
			,btn_save_x				:$('#btn_save_sectionTest_x')		
		}
		
		//B、请求地址URL
		var opt_url={
			url_list			:basePath + "rvsect/rvsect!list.action"//查询数据URL	
			,url_save			:basePath + "rvsect/rvsect!save.action"//添加或修改保存URL,url_save	
			,url_save_x			:basePath + "rvsect/rvsect!saveX.action"//添加或修改保存URL,url_save	:basePath + "contInfo/contInfo!save.action"//添加或修改保存主合同URL
			,url_remove			:basePath + "rvsect/rvsect!removeids.action"//删除URL
			,url_edit			:basePath + "rvsect/rvsect!edit.action"//修改查询数据
			,url_import			:basePath + "rvsect/rvsect!importPptn.action"//导入
			,url_export			:basePath + "rvsect/rvsect!export.action"//导出
			,url_upstcd_Stbprp  :basePath + "stbprp/stbprp!upstcd_Stbprp.action"//下拉列表
		}
		//全部变量，自定义的覆盖默认变量
		var opt_all=($.extend(opt_control,opt_url,option));
	    //////////////////////////////////////////////////////////////////////////////////////////////////
		this.InitData=function(opt){
			opt_all=$.extend(opt_all,opt);   
			opt_all.query_table_sectionTest.bootstrapTable($.extend(g_bootstrapTable_Options,
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
			    $("#query_table_sectionTest tbody tr[data-index="+i+"]").addClass("success");
			   }
			   //全不选时颜色恢复
			   function onUncheckAll(){
			    $("#query_table_sectionTest tbody tr").removeClass("success");
			   }
			   //选中一行改变表格背景色
			   function onCheck(rows){
			    $("#query_table_sectionTest tbody tr[data-uniqueid="+rows.id+"]").addClass("success");
			   }
			   //不选中时颜色恢复
			   function onUncheck(rows){
			    $("#query_table_sectionTest tbody tr[data-uniqueid="+rows.id+"]").removeClass("success");
			   }
			// 提交查询函数
			function queryParams(params) {  // 配置参数
				//查询条件
				var opt_parms={
					 "mRvsectFormBean.searchName"			:opt_control.query_searchName.val() // 查询关键字
				};
			    var temp = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			       "mRvsectFormBean.pageBean.limit"			: params.limit   // 页面大小
			      ,"mRvsectFormBean.pageBean.offset"			: params.offset  // 当前记录偏移条数
			      ,"mRvsectFormBean.pageBean.sort"				: params.sort  	  // 排序列名
			      ,"mRvsectFormBean.pageBean.sortOrder" 		: params.order   // 排位命令（desc，asc）
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
				var data = $('#query_table_sectionTest').bootstrapTable('getData', true);
				mergeCells(data,"STNM",1, $('#query_table_sectionTest'));
				mergeCells(data,"MSTM",1, $('#query_table_sectionTest'));
				mergeCells(data,"STCD",1, $('#query_table_sectionTest'));
			}
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		// 初始化增加、修改和删除,公开函数
		this.InitAddEditDel=function(opt){
			opt_all=$.extend(opt_all,opt);
			opt_all.query_add_sectionTest.bind("click",event_add);
			opt_all.btn_del_sectionTest.bind("click",event_del);
			opt_all.query_ref.bind("click",event_ref);
			opt_all.btn_into_pptn.bind("click",upload_model_show);
			opt_all.btn_outPage_pptn.bind("click",exportPagePptn);
			opt_all.btn_outAll_pptn.bind("click",exportAllPptn);
			opt_all.query_add_sectionTest_x.bind("click",x_xzcs);
			opt_all.btn_save_x.bind("click",btn_save_x);
			
			//初始化主合同编辑表单 保存button类型为submit
			opt_all.info_form_sectionTest.bootstrapValidator().on("success.form.bv", function(e) {
				e.preventDefault(); // 去掉默认提交事件
				// 校验数据正确,执行保存数据
				_save();
				
			}).on("error.form.bv", function(e) {
				e.preventDefault(); // 去掉默认提交事件
			});	
			
		}
		
		//下拉数据初始化
		this.InitSelect = function (){
			Load_select_stcd_rvsect();
			Load_select_stcd_rvsect_x();
		}
		function Load_select_stcd_rvsect(){
			opt_control.btn_stcd_rvsect.empty();
			var url = opt_all.url_upstcd_Stbprp;
			opt_control.btn_stcd_rvsect.append("<option value=''>请选择测站名称</option>");
			common_ajax(null,url, function(data) {
				$.each(data.rows, function(i) {
					opt_control.btn_stcd_rvsect.append(
						'<option value=' + data.rows[i].STCD + '>'+ data.rows[i].STNM + '</option>');
				});
			});
		}
		
		function Load_select_stcd_rvsect_x(){
			opt_control.btn_stcd_rvsect_x.empty();
			var url = opt_all.url_upstcd_Stbprp;
			opt_control.btn_stcd_rvsect_x.append("<option value=''>请选择测站名称</option>");
			common_ajax(null,url, function(data) {
				$.each(data.rows, function(i) {
					opt_control.btn_stcd_rvsect_x.append(
						'<option value=' + data.rows[i].STCD + '>'+ data.rows[i].STNM + '</option>');
				});
			});
		}
		
	    // 修改记录，调出窗体,公开函数
		this.edit=function(stcd,mstm) {
			_edit(stcd,mstm);
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
           var stcd = g_select_and_tip(opt_all.query_table_sectionTest,"STCD");
           var mstm = (tm.substring(tm.length-1)==',')?tm.substring(0,tm.length-1):tm;
           var vtno = g_select_and_tip(opt_all.query_table_sectionTest,"VTNO");
           if (stcd.length==0) return;
           
           $(".fixed-table-container tbody tr.selected td").addClass("row-bcground");
           
           var url= opt_all.url_remove+"?stcd="+stcd+"&mstm="+mstm+"&vtno="+vtno;
            
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
			//加载Edit页面基本选择数据，成功后调用显示编辑页面
			//加载字典分类数据下拉框数据
			Load_EditSelectData( 
				function(){ 
					LoadEditData(stcd,mstm);
				}
			);
		}
		
		
		function _save() {
			// 参数需要保存的表单，保存url,需要更新的bootstrapTable,必须设置uniqueId: "id"
	 		var json=opt_all.info_form_sectionTest.serialize();
	 		common_ajax(json, opt_all.url_save, function(response){
	 			opt_all.edit_dialog_sectionTest.modal("hide");
			    //重置表单
			    _reset();
			    // 刷新列表
			    _refresh();
	 		});
		}
		
		function btn_save_x(){
			var json=opt_all.info_form_sectionTest_x.serialize();
			common_ajax(json, opt_all.url_save_x, function(response){
	 			opt_all.edit_dialog_sectionTest_x.modal("hide");
			    _refresh();
	 		});
		}
		
		//显示详情
		function _table(queryId){
			var url = opt_all.url_list+"?mRvsectFormBean.mRvsectInfoBean.stcd=" + queryId;
			common_ajax(null,url, function(response) {
				for(var key in response.rows[0]){
				   if($("#"+key+"_detail_d")[0]){
					   $("#"+key+"_detail_d").html(response.rows[0][key]);
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
				$("#MODITIME_detail_d").html(tm_m);
				$("#MSTM_detail_d").html(tm);
			});
			opt_all.query_info_sectionTest.modal({
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
			var data="mRvsectFormBean.pageBean.limit=99999999"
					+"&mRvsectFormBean.pageBean.offset=0" 
					+"&mRvsectFormBean.pageBean.sortOrder="+TableQueryParams.order
					+"&mRvsectFormBean.pageBean.sort="+TableQueryParams.sort
					+"&mRvsectFormBean.searchName="+opt_all.query_searchName.val();
			confirm("<i class='icon icon-reply'></i>&nbsp;导出全部","您确定要导出全部信息吗？","icon-info", function(result) {
			   if(result){
				   window.location.href=url+data;
               }
			});
		}
		
		//导出当前页
		function exportPagePptn(){
			var url= opt_all.url_export+"?";
			var data_page="mRvsectFormBean.pageBean.limit="+TableQueryParams.limit
						 +"&mRvsectFormBean.pageBean.offset="+TableQueryParams.offset
						 +"&mRvsectFormBean.pageBean.sortOrder="+TableQueryParams.order
						 +"&mRvsectFormBean.pageBean.sort="+TableQueryParams.sort
						 +"&mRvsectFormBean.searchName"+opt_all.query_searchName.val();
			confirm("<i class='icon icon-circle-arrow-up'></i>&nbsp;导出当前页","您确定要导出当前页吗？","icon-info", function(result) {
			   if(result){
				   window.location.href=url+data_page;
			   }
			});
		}
		
		//加载Edit页面上数据，并调出增加或编辑页面显示
		function LoadEditData(stcd,mstm){
			var mstm=(mstm.substring(mstm.length-1)==',')?mstm.substring(0,mstm.length-1):mstm;
			if(stcd != "" && mstm!=""){
				var url = opt_all.url_edit+"?mRvsectFormBean.mRvsectInfoBean.stcd=" + stcd+"&mRvsectFormBean.mRvsectInfoBean.mstm="+mstm;
				// 动态加载页面数据
				common_ajax(null,url, function(response) {
					var table = $("#query_table_sectionTest_x");
					table.empty();
					var tr = "<tr>"+
								"<td style='width: 100px; text-align: center; font-weight: bold; background: #f1f1f1'>"+
									"<lable>起点距：</lable></td>"+
								"<td style='width: 100px; text-align: center; font-weight: bold; background: #f1f1f1'>"+
									"<lable>河底高程：</lable></td>"+
								"<td style='width: 100px; text-align: center; font-weight: bold; background: #f1f1f1'>"+
									"<lable>垂号线：</lable></td>"+
								/*"<td style='width: 100px; text-align: center; font-weight: bold; background: #f1f1f1'>"+
									"<lable>备注：</lable></td>"+
									"<td style='width: 100px; text-align: center; font-weight: bold; background: #f1f1f1'>"+
									"<lable>测试时间：</lable></td>"+*/
								"<td style='width: 100px; text-align: center; font-weight: bold; background: #f1f1f1'>"+
									"<lable>操作：</lable></td></tr>";
					table.append(tr);
					for(var i =0;i<response.mRvsectFormBean.length;i++){
						var MSTM=new Date(response.mRvsectFormBean[i].MSTM.time).format("yyyy-MM-dd hh:mm:ss");
						$("#stcd_rvsect_x").val(response.mRvsectFormBean[i].STCD);
						$("#stnm_rvsect_x").val(response.mRvsectFormBean[i].STNM);
						$("#mstm_rvsect_x").val(MSTM);
						var tr = "<tr>"+
						"<td style='text-align: left'><input style='margin-left: 15px;' class='a' id='di_rvsect_x' name='di' value="+response.mRvsectFormBean[i].DI+"></td>"+
						"<td style='text-align: left'><input style='margin-left: 15px;' class='a' id='zb_rvsect_x' name='zb' value="+response.mRvsectFormBean[i].ZB+"></td>"+
						"<td style='text-align: left'><input style='margin-left: 15px;' class='a' id='vtno_rvsect_x' name='vtno' value="+response.mRvsectFormBean[i].VTNO+"></td>"+
						/*"<td style='text-align: left'><input style='margin-left: 15px;' class='a' id='comments_rvsect_x' value="+response.mRvsectFormBean[i].comments+"></td>"+
						"<td style='text-align: left'><input style='margin-left: 15px;' class='a' id='mstm_rvsect_x' value="+response.mRvsectFormBean[i].mstm+"></td>"+*/
						"<td style='text-align: left'>" +
						"<a href='#' onclick='del_se(this)'>&nbsp;&nbsp;删除</a>" +
						"</td>"+
					"</tr>";
						table.append(tr);
					}
					opt_all.edit_dialog_sectionTest_x.find('.modal-title').html("<i class='icon icon-pencil'></i>&nbsp;修改断面测试");
					opt_all.edit_dialog_sectionTest_x.modal({
						 show : true
						,backdrop : "static" // 背景遮挡
						,moveable : true
					}).on('hide.zui.modal', function() {
						_reset();//当第一次验证正确后，关闭窗体。再进来时，重置窗体(保留窗体上数据)。
		            });
				});
			}else{
				$("#stcd_rvsect").val("");
				$("#ids_rvsect").val("add");
				//先清除添加过的数据再弹窗
				opt_all.edit_dialog_sectionTest.find('.modal-title').html("<i class='icon icon-plus-sign'></i>&nbsp;新增断面测试") ;
				opt_all.edit_dialog_sectionTest.modal({
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
	    	opt_all.query_table_sectionTest.bootstrapTable('refresh');
	    }
	    this.flash=function(nm){
	    	engineeringNm=nm;
			_refresh();
		}
		// 重置主合同
		function _reset(){
			opt_all.info_form_sectionTest.data('bootstrapValidator').resetForm(true);
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
			var table = $("#query_table_sectionTest_x");
			var tr = "<tr>"+
				"<td style='text-align: left'><input style='margin-left: 15px;' class='a' name='di' id='di_rvsect_x' ></td>"+
				"<td style='text-align: left'><input style='margin-left: 15px;' class='a' name='zb' id='zb_rvsect_x' ></td>"+
				"<td style='text-align: left'><input style='margin-left: 15px;' class='a' name='vtno' id='vtno_rvsect_x' ></td>"+
				/*"<td style='text-align: left'><input style='margin-left: 15px;' class='a'  id='comments_rvsect_x' ></td>"+
				"<td style='text-align: left'><input style='margin-left: 15px;' class='a'  id='mstm_rvsect_x' ></td>"+*/
				"<td style='text-align: left'>" +
				"<a href='#' onclick='del_se(this)'>&nbsp;&nbsp;删除</a>" +
				"</td>"+
			"</tr>";
			table.append(tr);
		}
	//删除参数
	del_se = function(obj){
		$(obj).parent().parent().remove();
	  }
	
	addsj_se = function(obj){
		var tr = obj.parentNode.parentNode; 
		var tds = tr.cells; 
		var str = ""; 
		for(var i = 0;i < tds.length-1;i++){
			
			str += $(tds[i].innerHTML).val()+",";
		}
		str=(str.substring(str.length-1)==',')?str.substring(0,str.length-1):str;
		var stcd = $("#stcd_rvsect_x").val();
		stcd +=",";
		var sj = stcd + str;
		var url = opt_all.url_save+"?mRvsectFormBean.idsup=" +sj;
		common_ajax(null,url, function(response) {
			
		});
	}
	};
})(jQuery);
			
