<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<style>
#step3_div3 thead > tr div.layui-table-cell{
	height:80px;
}
#step3_div3 thead > tr div.layui-table-cell > div{
	height:80px;
	line-height:80px;
}
#step3_div3 thead > tr div.layui-table-cell > div > div{
	height:20px;
	line-height:20px;
}
</style>
<div class="container-fluid" id="cljs_mainList">
		<div style="width:100%;height:200px;">
			<div style="height:40px;width:100%;">
				<div style="padding-left:30px;float:left;width:40%;line-height:40px;">1、推理过程线计算,计算m值</div>
				<div style="width:60%;float: right;">
					<label>站名:</label>
					<input id="huiliujisuan_step3_stcd" class="form-control" style="width: 150px;display:inline;"></input>
					<label>洪号:</label>
					<select id="huiliujisuan_step3_pch" class="form-control" style="width: 150px;display:inline;"></select>
					<button class="layui-btn" onclick="queryHuiLiuStep3Table1()" value="查询">查询</button>
					<button class="layui-btn" onclick="calcMandTable3()" value="计算">计算</button>
					<button class="layui-btn" onclick="deleteHuiliuStep3Result()" value="删除">删除</button>
					<input type="button" onclick="jsm_export()" id="jsm_export" style="margin-left: 20px;" value="导出到excel"  class="layui-btn" >
				</div>
			</div>
			<div id="step3_div2" style="height:calc(100% - 40px);width:100%;">
				<table class="layui-hide" id="step3_table2" lay-filter="hszlb_table"></table>
			</div>
		</div>
		<div style="width:100%;height:400px;margin-top:20px;">
			<div style="height:40px;width:100%;">
				<div style="padding-left:30px;float:left;width:40%;line-height:40px;">2、推理过程线计算,表3数据</div>
				<div style="width:60%;float: right;">
				</div>
			</div>
			<div id="step3_div3" style="height:calc(100% - 40px);width:100%;">
				<table class="layui-hide" id="step3_table3" lay-filter="hszlb_table"></table>
			</div>
		</div>
		<div style="width:100%;height:400px;margin-top:20px;">
			<div style="height:40px;">
				<div style="width:50%;float:left;padding-left:30px;line-height:40px;">3.推理过程线计算,单站综合及误差统计</div>
				<div style="width:50%;float:right;">
					<button class="layui-btn" value="单站综合及误差统计" onclick="step3Zhcx()">单站综合及误差统计</button>
					<input type="button" onclick="step3_export3()" id="step3_export3" style="margin-left: 20px;" value="导出到excel"  class="layui-btn" >
				</div>
			</div>
			<div style="height:calc(100% - 40px);width:100%;">
				<div id="step3_div4" style="width:50%;height:100%;float:left;">
					<table class="layui-hide" id="step3_table4" lay-filter="hszlb_table"></table>
				</div>
				<div id="step2_echart" style="width:50%;height:100%;float:right;">
					<div id="huiliu_step3_echarts" style="height:100%;"></div>
				</div>
			</div>
		</div>
</div>
<!-- 不要改变以下引用顺序 -->
<script type="text/javascript">
//初始化统计图				
//var myChart = echarts.init(document.getElementById('tsqx_chart'));
var step3SubForm={};
$(function(){
	initHuiliuStep3StcdCombox();
	//getwdrsTable("#wdrs_div","#wdrs_tab","test_jls_tab.json");
	//getTlgcxTable("#wdrs_div","#tlgcx_tab","test_jls_tab.json"); //	2、推理过程线计算
	//getwctj_tab("#tlgs_div","#wctj_tab","test_jls_tab.json");
//	getGxt("tsqx_chart","test_tsd3.json","");
})
function deleteHuiliuStep3Result(){
	var stcd =$("#huiliujisuan_step3_stcd").combox("getValue");
	var pch = $("#huiliujisuan_step3_pch").val();
	var url= basePath + "huiliu/huiliu!deleteStep3Result.action";
	layer.confirm('确定要删除以前计算结果吗？', {
			  btn: ['确定','取消'] //按钮
	}, function(index){
		layer.close(index);
		$.ajax({
			url:url,
			type:"post",
			data:{"stcd":stcd,"pch":pch},
			dataType:"json",
			success:function(response){
				if(response.reflag==1||response.reflag=="1"){
					queryHuiLiuStep3Table1();
				}else{
					layer.msg(response.message);
				}
			}
		});
	}, function(index){
		layer.close(index);
	});
}
function jsm_export(){
	var stcd =$("#huiliujisuan_step3_stcd").combox("getValue");
	var pch = $("#huiliujisuan_step3_pch").val();
	if(pch==null ){
		layer.msg("请先选择一个测站!");
		return ;
	}
	var url= basePath + "huiliu/huiliu!jsm_export.action?STCD="+stcd+"&PCH="+pch;
		confirm("<i class='icon icon-circle-arrow-up'></i>&nbsp;导出到excel","您确定要导出到excel吗？","icon-info", function(result) {
			   if(result){
				   window.location.href=url;
			   }
			}); 
		queryHuiLiuStep3Table1();
	}
	
function step3_export3(){
	var stcd = $("#huiliujisuan_step3_stcd").combox("getValue");
	var url= basePath + "huiliu/huiliu!step3_export3.action?STCD="+stcd;
		confirm("<i class='icon icon-circle-arrow-up'></i>&nbsp;导出到excel","您确定要导出到excel吗？","icon-info", function(result) {
			   if(result){
				   window.location.href=url;
			   }
			}); 
	}
	
function initHuiliuStep3StcdCombox(){
	$("#huiliujisuan_step3_stcd").combox({
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
			loadHuiliuStep3PchSelect();
		}
	});
	if(qjstcd!=null && $.trim(qjstcd).length>0 && qjstnm!=null && $.trim(qjstnm).length>0){
		$("#huiliujisuan_step3_stcd").combox("setValue",{label:qjstnm,value:qjstcd});
		loadHuiliuStep3PchSelect();
	}
}
function loadHuiliuStep3PchSelect(){
	var stcd = $("#huiliujisuan_step3_stcd").combox("getValue");
	var url= basePath + "huiliu/huiliu!huiliuStep2PchSelect.action";
	$.ajax({
		url:url,
		type:"post",
		data:{"stcd":stcd},
		dataType:"json",
		success:function(response){
			var pchList = response.dataList;
			$("#huiliujisuan_step3_pch").empty();
			if(pchList !=null && pchList.length>0){
				for (var i = 0; i < pchList.length; i++) {
					if(i==0 && (qjpch==null || $.trim(qjpch).length<1)){
						qjpch=pchList[i].PCH;
					}
					$("#huiliujisuan_step3_pch").append("<option value='"+pchList[i].PCH+"' > "+pchList[i].PCH+"</option>");
				}
				$("#huiliujisuan_step3_pch").val(qjpch);
				if(qjpch!=null && $.trim(qjpch).length>0){
					queryHuiLiuStep3Table1();
				}
			}else{
				$("#huiliujisuan_step3_pch").append("<option value='' selected='selected' > 请选择  </option>")
			}
		}
	});
}
//第三步综合查询
function step3Zhcx(){
	var stcd = $("#huiliujisuan_step3_stcd").combox("getValue");
	var url= basePath + "huiliu/huiliu!huiliuStep3Search.action?STCD="+stcd;
	common_ajax(null, url, function(response) {
			if(response.reflag==1||response.reflag=="1"){
				var mjList = response.mjDataList;
				var mcList = response.mcDataList;
				var cxList = response.cxDataList;
				
				step3SearhcTable("#step3_div4","#step3_table4",cxList);
				var dataOne = [];
				var dataTwo = [];
				for(var i=0;i<mjList.length;i++){
					dataOne.push([mjList[i].m,mjList[i].qf]);
				}
				for(var j=0;j<mcList.length;j++){
					dataTwo.push([mcList[j].m,mcList[j].qf]);
				}
				loadStep3ZhcxEchart(dataOne,dataTwo);
			}	
	});
}
function isNumber(val){
    var regPos = /^\d+(\.\d+)?$/; //非负浮点数
    if(regPos.test(val)){
        return true;
    }else{
        return false;
    }

}
//调用接口计算M值和表3中的数据
function calcMandTable3(){
	var stcd = $("#huiliujisuan_step3_stcd").combox("getValue");
	var pch = $("#huiliujisuan_step3_pch").val();
	if(step3SubForm.table2Data.l==null ||$.trim(step3SubForm.table2Data.l).length<1){
		layer.msg("L不能为空!");
		return ;
	}
	if(step3SubForm.table2Data.j==null ||$.trim(step3SubForm.table2Data.j).length<1){
		layer.msg("J不能为空!");
		return ;
	}
	if(step3SubForm.table2Data.lymj==null ||$.trim(step3SubForm.table2Data.lymj).length<1){
		layer.msg("流域面积不能为空!");
		return ;
	}
	if(step3SubForm.table2Data.m==null ||$.trim(step3SubForm.table2Data.m).length<1){
		layer.msg("M值不能为空!");
		return ;
	}
	if(!isNumber(step3SubForm.table2Data.l)){
		layer.msg("L必须为数字!");
		return ;
	}
	if(Number(step3SubForm.table2Data.l)<=0){
		layer.msg("L必须为大于0的数字!");
		return ;
	}
	if(!isNumber(step3SubForm.table2Data.j)){
		layer.msg("J必须为数字!");
		return ;
	}
	if(Number(step3SubForm.table2Data.j)<=0){
		layer.msg("J必须为大于0的数字!");
		return ;
	}
	if(!isNumber(step3SubForm.table2Data.lymj)){
		layer.msg("流域面积必须为数字!");
		return ;
	}
	if(Number(step3SubForm.table2Data.lymj)<=0){
		layer.msg("流域面积必须为大于0的数字!");
		return ;
	}
	//
	var data={
		M:step3SubForm.table2Data.m,
		F:step3SubForm.table2Data.lymj,
		L:step3SubForm.table2Data.l,
		J:step3SubForm.table2Data.j,
		_t:step3SubForm._t,
		DATA:JSON.stringify({"DATA":step3SubForm.submitData})
	};
	console.log("--------request----"+JSON.stringify(data))
	var url= serverUrl + "/CHLService.asmx/getQResult";
	$.ajax({
		url:url,
		type:"post",
		data:data,
		dataType:"json",
		success:function(response){
			console.log("-----step3-----result-----"+JSON.stringify(response))
			step3SubForm.table2Data.jsjg=JSON.stringify(response);
			step3SubForm.table2Data.m3=response.M;
			var tablePropers=gernerStep3Table3Data(response);
			var maxQsj=tablePropers.maxQsj;
			var maxQs=tablePropers.maxQs;
			step3SubForm.table2Data.qmsj=maxQsj;
			step3SubForm.table2Data.qms=maxQs;
			step3Table2("#step3_div2","#step3_table2",step3SubForm.table2Data);
			
			step3Table3("#step3_div3","#step3_table3",tablePropers);
			saveHuiliuStep3Result(step3SubForm.table2Data);
		}
	});
}
function saveHuiliuStep3Result(data){
	var url= basePath + "huiliu/huiliu!saveHuiliuStep3Result.action";
	$.ajax({
		url:url,
		type:"post",
		data:data,
		dataType:"json",
		success:function(response){
			if(response.reflag==1||response.reflag=="1"){
				layer.msg("保存成功!");
			}else{
				layer.msg(response.message);
			}
		}
	});
}
//第三步散点趋势图
function loadStep3ZhcxEchart(dataOne,dataTwo){
	var myChart = echarts.getInstanceByDom(document.getElementById('huiliu_step3_echarts'));
	if(myChart){
		myChart.dispose();
	}
	myChart = echarts.init(document.getElementById('huiliu_step3_echarts'));
	option = {
	    title : {
	        text: '散点分部趋势图'
	    },
	    legend: {
	        data:['m计散点','趋势图']
	    },
	    xAxis : [
	        {
	            type : 'value',
	            name:'mm',
	            scale:true,
	            axisLabel : {
	                formatter: '{value}'
	            }
	        }
	    ],
	    yAxis : [
	        {
	            type : 'value',
	            name:'mm/h',
	            scale:true,
	            axisLabel : {
	                formatter: '{value}'
	            }
	        }
	    ],
	    series : [
	        {
	            name:'m计',
	            type:'scatter',
	            data: dataOne,
	            markPoint : {
	                data : [
	                    {type : 'max', name: '最大值'},
	                    {type : 'min', name: '最小值'}
	                ]
	            }
	        },
	        {
	            name:'趋势图',
	            type:'line',
	            data: dataTwo,
	            markPoint : {
	                data : [
	                    {type : 'max', name: '最大值'},
	                    {type : 'min', name: '最小值'}
	                ]
	            }
	        }
	    ]
	};
	myChart.setOption(option);	
}
//查询第三步第一个数据表格
function queryHuiLiuStep3Table1(){
	var stcd =$("#huiliujisuan_step3_stcd").combox("getValue");
	var pch = $("#huiliujisuan_step3_pch").val();
	var url= basePath + "huiliu/huiliu!queryInitHuiliuStep3Data.action?STCD="+stcd+"&PCH="+pch;
	$.ajax({
		url:url,
		type:"post",
		data:{STCD:stcd,PCH:pch},
		dataType:"json",
		success:function(response){
			if(response.reflag==1||response.refalg=="1"){
				step3SubForm.submitData=response.subData;
				step3SubForm._t=response.sjjg;
				var qdatalist=response.thirdData!=null?response.thirdData:[];
				step3SubForm.qdatalist=qdatalist;
				var table2Data=response.table1Data;
				step3Table2("#step3_div2","#step3_table2",table2Data);
				if(table2Data!=null && table2Data.jsjg!=null){
					var table3Data=table2Data.jsjg;
					var tablePropers=gernerStep3Table3Data(table3Data);
					step3Table3("#step3_div3","#step3_table3",tablePropers);
				}
			}else{
				layer.msg(response.message);
			}
		}
	});
}
	//1、导入净雨值
	function step3Drjyl(chart, tab, tableData){
	    var height = $(chart).height();
	    var width = $(chart).width();
	    layui.use('table', function() {
	        var table = layui.table;
	        table.render({
	            elem: tab,
	            height: height,
	            width: width,
	            id:'step3Table1',
	            data:tableData,
	            limit:tableData.length,
	            cols: [
	                [{
	                    field: 't',
	                    title: '时间',
	                },{
	                    field: 'Rt',
	                    title: 'Rt',
	                    edit: 'text'
	                }]
	            ],
	            page: false
	        });
	    });
    }
//初始化第三步的测站和洪号下拉框数据
//测站列表数据表格
	function step3Table2(chart, tab, tableData){
	    var height = $(chart).height();
	    var width = $(chart).width();
	    layui.use('table', function() {
	        var table = layui.table;  
	        table.render({
	            elem: tab,
	            height: height,
	            width: width,
	            id:'step3Table2',
	            limit:1,
	            data:[tableData],
	            cols: [
	                [{
	                    field: 'pch',
	                    title: '洪   号',
	                    width:'200'
	                },
	                {
	                    field: 'lymj',
	                    title: '流域面积F',
	                    edit:'text',
	                    width:'200'
	                },
	                {
	                    field: 'l',
	                    title: 'L',
	                    edit:'text',
	                    width:'200'
	                },
	                {
	                    field: 'j',
	                    title: 'J',
	                    edit:'text',
	                    width:'200'
	                },
	                {
	                    field: 'qm',
	                    title: 'QM',
	                    edit:'text',
	                    width:'200'
	                },
	                {
	                    field: 'm',
	                    title: 'M',
	                    edit:'text',
	                    width:'150'
	                },
	                {
	                    field: 'qmf',
	                    title: 'QM/F',
	                    edit: 'text',
	                    width:'190',
	                    templet:function(row){
	                    	if(row.qm!=null && row.lymj!=null){
	                    		var qmf=parseFloat(row.qm)/parseFloat(row.lymj);
	                    		return Number(qmf).toFixed(3);
	                    	}else{
	                    		return "";
	                    	}
	                    }
	                }]
	            ],
	            page: false,
	            done: function(res, curr, count){
			        step3SubForm.table2Data=res.data&&res.data.length>0?res.data[0]:null;
			    }
	        });
	    });
    }
    function formatterDateTime(dateStr){
    	var date = new Date(dateStr);
    	var yyyy=date.getFullYear();
    	var mm=date.getMonth()+1;
    	var dd=date.getDate();
    	var hh=date.getHours();
    	var mi=date.getMinutes();
    	var str=yyyy.toString();
    	str=str+"-"+(mm<10?"0"+mm.toString():mm.toString());
    	str=str+"-"+(dd<10?"0"+dd.toString():dd.toString());
    	str=str+" "+(hh<10?"0"+hh.toString():hh.toString());
    	str=str+":"+(mi<10?"0"+mi.toString():mi.toString());
    	return str;
    }
    function formatterTimeStr(dateStr){
    	var date = new Date(dateStr);
    	var yyyy=date.getFullYear();
    	var mm=date.getMonth()+1;
    	var dd=date.getDate();
    	var hh=date.getHours();
    	var mi=date.getMinutes();
    	var str=yyyy.toString();
    	str=str+(mm<10?"0"+mm.toString():mm.toString());
    	str=str+(dd<10?"0"+dd.toString():dd.toString());
    	str=str+(hh<10?"0"+hh.toString():hh.toString());
    	str=str+(mi<10?"0"+mi.toString():mi.toString());
    	return str;
    }
    function gernerStep3Table3Data(tableJson){
    	var inputQlist=step3SubForm.qdatalist;
    	var tableData=tableJson.DATA;
    	var m=tableJson.M;
    	var dataMap={};
    	var columns=[];
    	var dataList=new Array();
    	var column1=new Array();
    	var column2=new Array();
    	var column3=new Array();
    	var column4=new Array();
    	var maxQs=0,maxQsj=0;
    	column1.push({field: 'XH',title: '序号',width:'60',templet:function(row){
	                    return (row.LAY_TABLE_INDEX+1);
	                }});
	    column1.push({field: 'DT',title: '时间',width:'160'});
	    column1.push({field: 'RS',title: '时段地表净雨R上',width:'150'});   
	    column1.push({field: 'QM',title: '时段最大流量QM',width:'150'});  
	    
    	//1.先找出数据中的时间，构建map对象
    	if(tableData!=null && tableData.length>0){
    		for(var i=0;i<tableData.length;i++){
    			var rupdata=tableData[i];
    			var qdatalist = rupdata.QData;
    			var qm=rupdata.Qm;
    			var title='<div>R上'+(i+1)+'='+Number(rupdata.Rup).toFixed(2)+'</div><div>t总='+Number(rupdata.tTotal).toFixed(2)+'</div><div>tc='+Number(rupdata.tc).toFixed(2)+'</div><div>tB='+Number(rupdata.tB).toFixed(2)+'</div>';
    			column1.push({field:'RS'+(i+1),title:title,width:'130'});
    			
    			var qkey=formatterTimeStr(rupdata.T);
    			var qjobj=dataMap[qkey];
    			if(qjobj==null){
    				qjobj={};
    			}
    			qjobj.DT=formatterDateTime(rupdata.T);
    			qjobj.RS=Number(rupdata.Rup).toFixed(1);
    			qjobj.QM=rupdata.Qm;
    			dataMap[qkey]=qjobj;
    			if(qdatalist!=null && qdatalist.length>0){
    				for(var j=0;j<qdatalist.length;j++){
    					var qdata=qdatalist[j];
    					if(qdata!=null){
    						var tm=qdata.T;
    						var key=formatterTimeStr(tm);
    						var obj=dataMap[key];
    						if(obj==null){
    							obj={};
    						}
    						obj["DT"]=formatterDateTime(tm);;
    						obj["RS"+(i+1)]=qdata.Q;
    						//
    						dataMap[key]=obj;
    					}
    				}
    			}
    		}
    	}
    	column1.push({field: 'QSJ',title: 'Q上计',width:'100'}); 
	    column1.push({field: 'QS',title: 'Q上',width:'100'});   
	    column1.push({field: 'JG',title: '结果',width:'120'});   
    	columns.push(column1);
    	if(inputQlist!=null && inputQlist.length>0){
    		for(var i=0;i<inputQlist.length;i++){
    			var data=inputQlist[i];
    			var key=formatterTimeStr(data.DATE);
    			if(dataMap[key]!=null){
    				dataMap[key].QS=data.LL;
    				if(parseFloat(maxQs)<parseFloat(data.LL)){
    					maxQs=data.LL;
    				}
    			}
    		}
    	}
    	//组织数据集合
    	for(var key in dataMap){
    		var qsj=0;
    		for(var i=0;i<tableData.length;i++){
    			if(dataMap[key]["RS"+(i+1)]!=null){
    				qsj=parseFloat(qsj)+parseFloat(dataMap[key]["RS"+(i+1)]);
    			}
    		}
    		if(parseFloat(maxQsj)<parseFloat(qsj)){
    			maxQsj=qsj;
    		}
    		dataMap[key].QSJ=qsj>0?Number(qsj).toFixed(3):qsj;
    		dataMap[key].M=m;
    		dataList.push(dataMap[key]);
    	}
    	if(dataList!=null && dataList.length>1){
    		dataList.sort(function(d1, d2) {
    			if(new Date(d1.DT).getTime()>new Date(d2.DT).getTime()){
    				return 1;
    			}else if(new Date(d1.DT).getTime()<new Date(d2.DT).getTime()){
    				return -1;
    			}else{
    				return 0;
    			}
    		});
    	}
    	return {columns:columns,dataList:dataList,maxQsj:maxQsj,maxQs:maxQs};
    }
    function calcTable3Result(dataList){
    	var maxQsj=null;
    	var maxQs=null;
    	if(dataList!=null && dataList.length>0){
    		for(var i=0;i<dataList.length;i++){
    			var data = dataList[i];
    			if(data!=null && data.QSJ!=null){
    				if(maxQsj==null){
    					maxQsj=data.QSJ;
    				}else{
    					if(parseFloat(maxQsj)<parseFloat(data.QSJ)){
    						maxQsj=data.QSJ;
    					}
    				}
    			}
    			if(data!=null && data.QS!=null){
    				if(maxQs==null){
    					maxQs=data.QS;
    				}else{
    					if(parseFloat(maxQs)<parseFloat(data.QS)){
    						maxQs=data.QS;
    					}
    				}
    			}
    		}
    	}
    	return {maxQsj:maxQsj,maxQs:maxQs};
    }
    //	2、推理过程线计算
	function step3Table3(chart, tab, tablePropers){
		var columns=tablePropers.columns;
		var dataList = tablePropers.dataList;
	    var height = $(chart).height();
	    var width = $(chart).width();
	    layui.use('table', function() {
	        var table = layui.table;
	        table.render({
	            elem: tab,
	            width: width,
	            height:height,
	            id:'step3Table3',
	            data:dataList,
	            limit:dataList.length,
	            cols: columns,
	            page: false,
	            done: function(res, curr, count){
	                $("#step3_div3 tbody td[data-field='JG']").each(function(index,td){
	                	if(index==0){
	                		$(td).attr("rowspan",res.data.length);
	                		var result=calcTable3Result(res.data);
	                		var html="";
	                		if(result.maxQsj!=null){
	                			html=html+"<div style='width:120px'>Qm上计="+result.maxQsj+"</div>";
	                		}
	                		if(result.maxQs!=null){
	                			html=html+"<div style='width:120px'>Qm上="+result.maxQs+"</div>";
	                		}
	                		if(result.maxQsj!=null && result.maxQs!=null){
	                			var ds=parseFloat(result.maxQs)!=0?(parseFloat(result.maxQs)-parseFloat(result.maxQsj))*100/parseFloat(result.maxQs):0;
	                			ds=Number(ds).toFixed(2);
	                			html=html+"<div>δ="+ds+"%</div>";
	                		}
	                		$(td).html(html);
	                	}else{
	                		$(td).remove();
	                	}
	                });
	          	}
	        });
	    });
    }
  //单站综合及误差统计table
	function step3SearhcTable(chart, tab, tableData){
	    var height = $(chart).height();
	    var width = $(chart).width();
	    layui.use('table', function() {
	        var table = layui.table;
	        table.render({
	            elem: tab,
	            height: height,
	            width: width,
	            id:'step3Table4',
	            data:tableData,
	            limit:tableData.length,
	            cols: [
	            	[ //标题栏
	            	    {field: 'PCH', title: '洪号', rowspan: 2,align: 'center',edit:"text"} //rowspan即纵向跨越的单元格数
	            	    ,{field: 'QF', title: 'Q/F(毫米/小时) ',  rowspan: 2,align: 'center',edit:"text"}
	            	    ,{field: 'MJ', title: 'm计 ',  rowspan: 2,align: 'center',edit:"text"}
	            	    ,{align: 'center', title: 'Q/F ~m', colspan: 3} //colspan即横跨的单元格数，这种情况下不用设置field和width
	            	  ], [
	            	    {field: 'MC', title: 'm查', align: 'center'}
	            	    ,{field: 'MS', title: '(m计-m查)/ m计',align: 'center' }
	            	    ,{field: 'SFHG', title: '是否合格', align: 'center'}
	            	  ]
	            ],
	            page: false
	        });
	    });
    }
    function getGxt(chart,url,data) {
        $.ajax({
            url : url,
            type : "get",
            dataType : "JSON",
            async : false,
            traditional: true,
            // data : data,
            success : function(response) {
                getChart(chart,response,data);
            }
        });
    }


//格式化时间
function getFormatDate(date) {
    var seperator1 = "/";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if(month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if(strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var hours="";
    var minut="";
    var seconds="";
    if(date.getHours()<10){
        hours="0"+date.getHours();
    }else{
        hours=date.getHours();
    }

    if(date.getMinutes()<10){
        minut="0"+date.getMinutes();
    }else{
        minut=date.getMinutes();
    }

    if(date.getSeconds()<10){
        seconds="0"+date.getSeconds();
    }else{
        seconds=date.getSeconds();
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
        + " " + hours + seperator2 + minut
        + seperator2 + seconds;
    return currentdate;
}
</script>
