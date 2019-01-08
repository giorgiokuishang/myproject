<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<style>
</style>
<div class="container-fluid" id="hljs_mainList_step2">
		<div style="width:100%;height:400px;">
			<div style="height:40px;width:80%;">
				<div style="padding-left:30px;float:left;width:40%;line-height:40px;">1、推理公式法</div>
				<div style="width:60%;float: right;">
					<label>站名:</label>
					<input id="huiliujisuan_step5_stcd" class="form-control" style="width: 150px;display:inline;"></input>
					<label>洪号:</label>
					<select id="huiliujisuan_step5_pch" class="form-control" style="width: 150px;display:inline;"></select>
					<button  class="btn btn-primary" onclick="queryHuiLiuStep5TableAll()" value="查询">查询</button>
					<input type="button" onclick="deleteHuiLiuStep5Table1()" id="huiliuStep5Delete1" style="margin-left: 20px;" value="删除"  class="btn btn-primary" >
					<button  class="btn btn-primary" onclick="huiliu5_export(1)" value="导出到excel">导出到excel</button>
				</div>
			</div>
			<div id="step5_div1" style="height:calc(100% - 40px);width:100%;">
				<table class="layui-hide" id="step5_table1" lay-filter="hszlb_table"></table>
			</div>
		</div>
		<br>
		<div style="width:100%;height:400px;">
			<div style="height:40px;width:60%;">
				<div style="padding-left:30px;float:left;width:40%;line-height:40px;">2、推理峰量法
				<input type="button" onclick="deleteHuiLiuStep5Table2()" id="huiliuStep5Delete2" style="margin-left: 20px;" value="删除"  class="btn btn-primary" >
				<button class="btn btn-primary" onclick="huiliu5_export(2)" value="导出到excel">导出到excel</button><br>
				</div>
			</div>
			<div id="step5_div2" style="height:calc(100% - 40px);width:100%;">
				<table class="layui-hide" id="step5_table2" lay-filter="hszlb_table"></table>
			</div>
		</div>
		<div style="width:100%;height:400px;">
			<div style="height:40px;width:60%;"><br>
				<div style="padding-left:30px;float:left;width:40%;line-height:40px;">3、推理过程线法
				<input type="button" onclick="deleteHuiLiuStep5Table3()" id="huiliuStep5Delete3" style="margin-left: 20px;" value="删除"  class="btn btn-primary" >
				<button  class="btn btn-primary" onclick="huiliu5_export(3)" value="导出到excel">导出到excel</button>
				</div>
			</div>
			<div id="step5_div3" style="height:calc(100% - 40px);width:100%;">
				<table class="layui-hide" id="step5_table3" lay-filter="hszlb_table"></table>
			</div>
		</div>
		<br>
		<div style="width:100%;height:400px;">
			<div style="height:40px;width:60%;">
				<div style="padding-left:30px;float:left;width:40%;line-height:40px;">4、瞬时单位线法
				<input type="button" onclick="deleteHuiLiuStep5Table4()" id="huiliuStep5Delete4" style="margin-left: 20px;" value="删除"  class="btn btn-primary" >
				<button class="btn btn-primary" onclick="shdwxf_export3()" value="导出到excel">导出到excel</button>
				</div>
			</div>
			<div id="step5_div4" style="height:calc(100% - 40px);width:100%;">
				<table class="layui-hide" id="step5_table4" lay-filter="hszlb_table"></table>
			</div>
		</div>
  </div>
<!-- 不要改变以下引用顺序 -->
<script type="text/javascript">
$(function(){
	initHuiliuStep5StcdCombox();
})
//删除第1个表格的计算结果
function deleteHuiLiuStep5Table1(){
	var stcd = $("#huiliujisuan_step5_stcd").combox("getValue");
	var checkStatus = layui.table.checkStatus('huiliustep5Table1');
	if(checkStatus.data && checkStatus.data.length>0){
		layer.confirm("确定要删除选择洪水的推理公式法计算结果数据吗?", { title: "删除确认" }, function (index) {
             	layer.close(index);
             	var url = basePath + "huiliu/huiliu!deleteHuiliuStepResult.action";
				$.ajax({
					url:url,
					type:"post",
					data:{stcd:stcd,step:"step1",DATA:JSON.stringify(checkStatus.data)},
					dataType:"json",
					success:function(response){
						if(response.reflag==1||response.reflag=="1"){
							step5Zhcx1();
						}else{
							layer.msg(response.message);
						}
					}
				});
        });
	}else{
		layer.msg("请选择要删除的洪水记录!");
	}
}
//删除第2个表格的计算结果
function deleteHuiLiuStep5Table2(){
	var stcd = $("#huiliujisuan_step5_stcd").combox("getValue");
	var checkStatus = layui.table.checkStatus('huiliustep5Table2');
	if(checkStatus.data && checkStatus.data.length>0){
		layer.confirm("确定要删除选择洪水的推理峰量法计算结果数据吗?", { title: "删除确认" }, function (index) {
             	layer.close(index);
             	var url = basePath + "huiliu/huiliu!deleteHuiliuStepResult.action";
				$.ajax({
					url:url,
					type:"post",
					data:{stcd:stcd,step:"step2",DATA:JSON.stringify(checkStatus.data)},
					dataType:"json",
					success:function(response){
						if(response.reflag==1||response.reflag=="1"){
							step5Zhcx2();
						}else{
							layer.msg(response.message);
						}
					}
				});
        });
	}else{
		layer.msg("请选择要删除的洪水记录!");
	}
}
//删除第3个表格的计算结果
function deleteHuiLiuStep5Table3(){
	var stcd = $("#huiliujisuan_step5_stcd").combox("getValue");
	var checkStatus = layui.table.checkStatus('huiliustep5Table3');
	if(checkStatus.data && checkStatus.data.length>0){
		layer.confirm("确定要删除选择洪水的推理过程线法计算结果数据吗?", { title: "删除确认" }, function (index) {
             	layer.close(index);
             	var url = basePath + "huiliu/huiliu!deleteHuiliuStepResult.action";
				$.ajax({
					url:url,
					type:"post",
					data:{stcd:stcd,step:"step3",DATA:JSON.stringify(checkStatus.data)},
					dataType:"json",
					success:function(response){
						if(response.reflag==1||response.reflag=="1"){
							step5Zhcx3();
						}else{
							layer.msg(response.message);
						}
					}
				});
        });
	}else{
		layer.msg("请选择要删除的洪水记录!");
	}
}
//删除第4个表格的计算结果
function deleteHuiLiuStep5Table4(){
	var stcd = $("#huiliujisuan_step5_stcd").combox("getValue");
	var checkStatus = layui.table.checkStatus('huiliustep5Table4');
	if(checkStatus.data && checkStatus.data.length>0){
		layer.confirm("确定要删除选择洪水的瞬时单位线法计算结果数据吗?", { title: "删除确认" }, function (index) {
             	layer.close(index);
             	var url = basePath + "huiliu/huiliu!deleteHuiliuStepResult.action";
				$.ajax({
					url:url,
					type:"post",
					data:{stcd:stcd,step:"step4",DATA:JSON.stringify(checkStatus.data)},
					dataType:"json",
					success:function(response){
						if(response.reflag==1||response.reflag=="1"){
							step5Zhcx4();
						}else{
							layer.msg(response.message);
						}
					}
				});
        });
	}else{
		layer.msg("请选择要删除的洪水记录!");
	}
}
function huiliu5_export(num){
	var stcd = $("#huiliujisuan_step5_stcd").combox("getValue");
	var pch = $("#huiliujisuan_step5_pch").val();
	if(pch==null ){
		layer.msg("请先选择一个测站!");
		return ;
	}
	var url= basePath + "huiliu/huiliu!huiliu5_export.action?stcd="+stcd+"&pch="+pch+"&table="+num;
		confirm("<i class='icon icon-circle-arrow-up'></i>&nbsp;导出到excel","您确定要导出到excel吗？","icon-info", function(result) {
			   if(result){
				   window.location.href=url;
			   }
			}); 
		
		queryHuiLiuStep5TableAll();
	}

function shdwxf_export3(num){
	var stcd = $("#huiliujisuan_step5_stcd").combox("getValue");
	if(stcd==null){
		layer.msg("请选择一条记录");
		return false;
	}
	var url= basePath + "huiliu/huiliu!shdwxf_export3.action?STCD="+stcd;
		confirm("<i class='icon icon-circle-arrow-up'></i>&nbsp;导出到excel","您确定要导出到excel吗？","icon-info", function(result) {
			   if(result){
				   window.location.href=url;
			   }
			}); 
		queryHuiLiuStep5TableAll();
	}

function initHuiliuStep5StcdCombox(){
	$("#huiliujisuan_step5_stcd").combox({
		url:basePath + "huiliu/huiliu!huiliuStep2StcdSelect.action",
		label:"STNM",
		value:"STCD",
		dataKey:"dataList",
		height:'250px',
		width:'150px',
		isPager:false,
		select:function(item){
			qjstcd=item.STCD;
			qjstnm=item.STNM;
			loadHuiliuStep5PchSelect();
		}
	});
	if(qjstcd!=null && $.trim(qjstcd).length>0 && qjstnm!=null && $.trim(qjstnm).length>0){
		$("#huiliujisuan_step5_stcd").combox("setValue",{label:qjstnm,value:qjstcd});
		loadHuiliuStep5PchSelect();
	}
}
function loadHuiliuStep5PchSelect(){
	var stcd = $("#huiliujisuan_step5_stcd").combox("getValue");
	var url= basePath + "huiliu/huiliu!huiliuStep2PchSelect.action";
	$.ajax({
		url:url,
		type:"post",
		data:{"stcd":stcd},
		dataType:"json",
		success:function(response){
			var pchList = response.dataList;
			$("#huiliujisuan_step5_pch").empty();
			$("#huiliujisuan_step5_pch").append("<option value='' >---请选择---</option>");
			if(pchList !=null && pchList.length>0){
				for (var i = 0; i < pchList.length; i++) {
					if(i==0 && (qjpch==null || $.trim(qjpch).length<1)){
						qjpch=pchList[i].PCH;
					}
					$("#huiliujisuan_step5_pch").append("<option value='"+pchList[i].PCH+"' > "+pchList[i].PCH+"</option>");
				}
				$("#huiliujisuan_step5_pch").val(qjpch);
				if(qjpch!=null && $.trim(qjpch).length>0){
					queryHuiLiuStep5TableAll();
				}
			}else{
				$("#huiliujisuan_step5_pch").append("<option value='' selected='selected' > 请选择  </option>")
			}
		}
	});
}
function queryHuiLiuStep5TableAll(){
	
	step5Zhcx1();
	
	step5Zhcx2();
	
	step5Zhcx3();
	
	step5Zhcx4();
}
function step5Zhcx1(){
	var stcd = $("#huiliujisuan_step5_stcd").combox("getValue");
	var pch = $("#huiliujisuan_step5_pch").combox("getValue");
	var url= basePath + "huiliu/huiliu!queryStep5Table1Data.action";
	$.ajax({
		url:url,
		type:"post",
		data:{stcd:stcd,pch:pch},
		dataType:"json",
		success:function(response){
			step1SearhcTable("#step5_div1","#step5_table1",response);
		}
	});
}
//单站综合及误差统计table
	function step1SearhcTable(chart, tab, table4Data){
	    var height = $(chart).height();
	    var width = $(chart).width();
	    layui.use('table', function() {
	        var table = layui.table;
	        table.render({
	            elem: tab,
	            height: height,
	            width: width,
	            id:'huiliustep5Table1',
	            limit:table4Data.length,
	            data:table4Data,
	            cols: [
	            	[
	            	{
	                 	type:"checkbox"
	                },
	            	{
	                    field: 'PCH',
	                    title: '洪号',
	                    width:130
	                },{
	                    field: 'Q',
	                    title: 'Q净(m³/s)',
	                    width:100
	                },{
	                    field: 'T',
	                    title: '汇流时间t(小时)',
	                    width:130
	                },{
	                    field: 'R',
	                    title: '地表净雨深R(mm)',
	                    width:110
	                },{
	                    field: 'MJ',
	                    title: '流域面积F(km²)',
	                    width:130
	                },{
	                    field: 'L',
	                    title: 'L',
	                    width:60,
	                    edit: 'text'
	                },{
	                    field: 'J',
	                    title: 'J',
	                    width:90,
	                    edit: 'text'
	                },{
	                    field: 'JS1',
	                    title: 'J<sup>1/3</sup>',
	                    width:90
	                },{
	                    field: 'JS2',
	                    title: 'L/(J<sup>1/3</sup>*F)',
	                    width:100
	                },{
	                    field: 'JS3',
	                    title: 'Q<sub>m</sub><sup>3/4</sup>',
	                    width:80
	                },{
	                    field: 'JS4',
	                    title: 'Q<sub>m</sub><sup>3/4</sup>/R',
	                    width:100
	                },{
	                    field: 'MJ',
	                    title: 'M计',
	                    width:70
	                },{
	                    field: 'MC',
	                    title: 'M查',
	                    width:70
	                },{
	                    field: 'MJMC',
	                    title: '是否合格',
	                    width:80,
	                    templet:function(row){
	                    	if(row.MJMC!=null){
	                    		if(parseFloat(row.MJMC)>=0 && parseFloat(row.MJMC)<=0.2){
		                    		return "合格";
		                    	}else if(parseFloat(row.MJMC)<0 && 0-parseFloat(row.MJMC)<=0.2){
		                    		return "合格";
		                    	}else{
		                    		return "不合格";
		                    	}
	                    	}else{
	                    		return "";
	                    	}
	                    }
	                }]
	            ],
	            page: false
	        });
	    });
    }
//第二步最终统计查询
function step5Zhcx2(){
	var stcd = $("#huiliujisuan_step5_stcd").combox("getValue");
	var pch = $("#huiliujisuan_step5_pch").combox("getValue");
	var url= basePath + "huiliu/huiliu!queryStep5Table2Data.action";
	$.ajax({
		url:url,
		type:"post",
		data:{stcd:stcd,pch:pch},
		dataType:"json",
		success:function(response){
			step2SearhcTable("#step5_div2","#step5_table2",response);
		}
	});
}
  //单站综合及误差统计table
	function step2SearhcTable(chart, tab, tableData){
	    var height = $(chart).height();
	    var width = $(chart).width();
	    layui.use('table', function() {
	        var table = layui.table;
	        table.render({
	            elem: tab,
	            height: height,
	            width: width,
	            id:'huiliustep5Table2',
	            limit:tableData.length,
	            data:tableData,
	            cols: [
		            	  [
		            	  {
			                type:"checkbox",
			                rowspan:"2"
			               },
		            	  {
		                    field: 'PCH',
		                    title: '洪号',
		                    align: 'center',
		                    rowspan:2,
		                    width:'150'
		                },{
		                    field: 'Q',
		                    title: '净峰流量Q净(m3/s)',
		                    align: 'center',
		                    rowspan:2,
		                    width:150
		                },{
		                    field: 'RS',
		                    title: 'R上(mm)',
		                    rowspan:2,
		                    width:100
		                },{
		                    field: 'TCJ',
		                    rowspan:2,
		                    title: 'tc均(小时)'
		                },{
		                    field: 'RSJ',
		                    rowspan:2,
		                    title: 'R上均(mm)'
		                },{
		                    field: 'LYMJ',
		                    rowspan:2,
		                    title: '流域面积F(km2)'
		                },{
		                    field: 'L',
		                    title: 'L',
		                    rowspan:2,
		                    width:80
		                },{
		                    field: 'J',
		                    title: 'J',
		                    rowspan:2,
		                    width:80
		                },{
		                    field: 'JS1',
		                    title: 'τ=(0.278FR_上)/Qm',
		                    rowspan:2,
		                    width:150
		                },
		                {
		                    field: 'NTT',
		                    align:'center',
		                    title: 'N=1.2',
		                    colspan:6
		                }
		                ],
		                [
		                {
		                    field: 'K',
		                    title: 'K',
		                    edit:'text',
		                    width:80
		                },{
		                    field: 'JS2',
		                    title: '(R上均^n)/(tc均^0.4 )',
		                    width:130
		                },{
		                    field: 'QMK',
		                    title: 'Qm/K',
		                    width:'130'
		                },{
	                    field: 'M',
	                    title: 'M计',
	                    width:100
	                },{
	                    field: 'MC',
	                    title: 'M查',
	                    width:100
	                },{
	                    field: 'MJMC',
	                    title: '是否合格',
	                    width:100,
	                    templet:function(row){
	                    	if(row.MJMC!=null){
	                    		if(parseFloat(row.MJMC)>=0 && parseFloat(row.MJMC)<=0.2){
		                    		return "合格";
		                    	}else if(parseFloat(row.MJMC)<0 && 0-parseFloat(row.MJMC)<=0.2){
		                    		return "合格";
		                    	}else{
		                    		return "不合格";
		                    	}
	                    	}else{
	                    		return "";
	                    	}
	                    }
	                }]
	            ],
	            page: false
	        });
	    });
    }
function step5Zhcx3(){
	var stcd = $("#huiliujisuan_step5_stcd").combox("getValue");
	var pch = $("#huiliujisuan_step5_pch").combox("getValue");
	var url= basePath + "huiliu/huiliu!queryStep5Table3Data.action";
	$.ajax({
		url:url,
		type:"post",
		data:{stcd:stcd,pch:pch},
		dataType:"json",
		success:function(response){
			step3SearhcTable("#step5_div3","#step5_table3",response);
		}
	});
}
function step3SearhcTable(chart, tab, tableData){
	    var height = $(chart).height();
	    var width = $(chart).width();
	    layui.use('table', function() {
	        var table = layui.table;
	        table.render({
	            elem: tab,
	            height: height,
	            width: width,
	            id:'huiliustep5Table3',
	            data:tableData,
	            limit:tableData.length,
	            cols: [
	            	[ //标题栏
	            		{
			                type:"checkbox"
			               },
	            	    {field: 'PCH', title: '洪号', align: 'center'} //rowspan即纵向跨越的单元格数
	            	    ,{field: 'QM', title: 'Q<sub>M</sub> ',align: 'center'}
	            	    ,{field: 'QMF', title: 'Q<sub>M</sub>/F(毫米/小时) ',align: 'center'}
	            	    ,{field: 'M', title: 'M计 ',align: 'center'},
	            	    {field: 'MC', title: 'M查', align: 'center'}
	            	    ,{field: 'QMSJ', title: 'Q<sub>m</sub>上计',align: 'center' }
	            	    ,{field: 'QMS', title: 'Q<sub>m</sub>上',align: 'center' }
	            	    ,{field: 'MJMC', title: 'δ=',align: 'center' }
	            	    ,{field: 'SFHG', title: '是否合格', align: 'center',templet:function(row){
	                    	if(row.MJMC!=null){
	                    		if(parseFloat(row.MJMC)>=0 && parseFloat(row.MJMC)<=0.2){
		                    		return "合格";
		                    	}else if(parseFloat(row.MJMC)<0 && 0-parseFloat(row.MJMC)<=0.2){
		                    		return "合格";
		                    	}else{
		                    		return "不合格";
		                    	}
	                    	}else{
	                    		return "";
	                    	}
	                    }}
	            	  ]
	            ],
	            page: false
	        });
	    });
    }
    
  function step5Zhcx4(){
	var stcd = $("#huiliujisuan_step5_stcd").combox("getValue");
	var pch = $("#huiliujisuan_step5_pch").combox("getValue");
	var url= basePath + "huiliu/huiliu!queryStep5Table4Data.action";
	$.ajax({
		url:url,
		type:"post",
		data:{stcd:stcd,pch:pch},
		dataType:"json",
		success:function(response){
			step4SearhcTable("#step5_div4","#step5_table4",response);
		}
	});
}
//单站综合及误差统计table
	function step4SearhcTable(chart, tab, tableData){
	    var height = $(chart).height();
	    var width = $(chart).width();
	    layui.use('table', function() {
	        var table = layui.table;
	        table.render({
	            elem: tab,
	            height: height,
	            width: width,
	            id:'huiliustep5Table4',
	            data:tableData,
	            limit:tableData.length,
	            cols: [
	            	[ //标题栏
	            		{
			                type:"checkbox",
			                rowspan:"2"
			               },
	            	    {field: 'PCH', title: '洪号', width:130,rowspan: 2,align: 'center',edit:"text"} //rowspan即纵向跨越的单元格数
	            	    ,{field: 'YX', title: '优选',  colspan: 3,align: 'center',edit:"text"}
	            	    ,{field: 'A13AM', title: 'a>1/3a<sub>max</sub>',  rowspan: 2,align: 'center',edit:"text"}
	            	    ,{field:'M1C',align: 'center', title: 'm<sub>1查</sub>', rowspan: 2} //colspan即横跨的单元格数，这种情况下不用设置field和width
	            	    ,{field:'KJ',title: 'k<sub>计</sub>',align: 'center', rowspan: 2} //colspan即横跨的单元格数，这种情况下不用设置field和width
	            	    ,{field:'QMJ',title: 'Q<sub>m计</sub>',align: 'center', rowspan: 2} //colspan即横跨的单元格数，这种情况下不用设置field和width
	            	  	,{field:'QMS',title: 'Q<sub>m实</sub>',align: 'center', rowspan: 2} //colspan即横跨的单元格数，这种情况下不用设置field和width
	            	  	,{field:'WC',title: '误差',align: 'center', rowspan: 2} //colspan即横跨的单元格数，这种情况下不用设置field和width
	            	  	,{field:'SFHG',title: '是否合格',align: 'center', rowspan: 2,templet:function(row){
	                    	if(row.WC!=null && row.MJ!=0){
	                    		if(parseFloat(row.WC)/parseFloat(row.Mj)>=0 && parseFloat(row.WC)/parseFloat(row.Mj)<=0.2){
		                    		return "合格";
		                    	}else if(parseFloat(row.WC)/parseFloat(row.Mj)<0 && 0-parseFloat(row.WC)/parseFloat(row.Mj)<=0.2){
		                    		return "合格";
		                    	}else{
		                    		return "不合格";
		                    	}
	                    	}else{
	                    		return "";
	                    	}
	                    }} //colspan即横跨的单元格数，这种情况下不用设置field和width
	            	  ], [
	            	    {field: 'N',width:80, title: 'n', align: 'center'}
	            	    ,{field: 'K',width:80, title: 'k',align: 'center' }
	            	    ,{field: 'M1',width:80, title: 'm<sub>1</sub>', align: 'center'}
	            	  ]
	            ],
	            page: false
	        });
	    });
    }
</script>
