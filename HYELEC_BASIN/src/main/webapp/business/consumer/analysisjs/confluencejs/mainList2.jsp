<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="container-fluid" id="hljs_mainList_step2">
		<div style="width:100%;height:400px;">
			<div style="height:40px;width:60%;">
				<div style="padding-left:30px;float:left;width:40%;line-height:40px;">1、导入净雨值</div>
				<div style="width:60%;float: right;">
					<label>站名:</label>
					<input id="huiliujisuan_step2_stcd" class="form-control" style="width: 150px;display:inline;"></input>
					<label>洪号:</label>
					<select id="huiliujisuan_step2_pch" class="form-control" style="width: 150px;display:inline;"></select>
					<button class="layui-btn" onclick="queryHuiLiuStep2Table1()" value="查询">查询</button>
				</div>
			</div>
			<div id="step2_div1" style="height:calc(100% - 40px);width:60%;">
				<table class="layui-hide" id="step2_table1" lay-filter="hszlb_table"></table>
			</div>
		</div>
		<div style="width:100%;height:200px;margin-top:20px;">
			<div style="height:40px;width:100%;">
				<div style="padding-left:30px;float:left;width:40%;line-height:40px;">2、推理峰量计算m值</div>
				<div style="width:60%;float: right;">
					<label>tc:</label>
					<input type="text" id="huiliujisuan_step2_tc" class="form-control" style="display:inline;width:100px;">
					<label>N:</label>
					<input type="text" id="huiliujisuan_step2_n" class="form-control" value="1.2" style="display:inline;width:100px;">
					<button class="layui-btn" onclick="calcHuiliuStep2Table2()" value="计算">计算</button>
					<button class="layui-btn" onclick="deleteHuiliuStep2Result()" value="删除">删除</button>
					<input type="button" onclick="tlfl_export1()" id="huiliu_export2" style="margin-left: 20px;" value="导出到excel"  class="layui-btn" >
				</div>
			</div>
			<div id="step2_div2" style="height:calc(100% - 40px);width:100%;">
				<table class="layui-hide" id="step2_table2" lay-filter="hszlb_table"></table>
			</div>
		</div>
		<div style="width:100%;height:400px;margin-top:20px;">
			<div style="height:40px;">
				<div style="width:50%;float:left;padding-left:30px;line-height:40px;">3.推理峰量计算,单站综合及误差统计</div>
				<div style="width:50%;float:right;">
					<button class="layui-btn" value="单站综合及误差统计" onclick="step2Zhcx()">单站综合及误差统计</button>
					<input type="button" onclick="tlfl_export2()" id="tlfl_export2" style="margin-left: 20px;" value="导出到excel"  class="layui-btn" >
				</div>
			</div>
			<div style="height:calc(100% - 40px);width:100%;">
				<div id="step2_div3" style="width:50%;height:100%;float:left;">
					<table class="layui-hide" id="step2_table3" lay-filter="hszlb_table"></table>
				</div>
				<div id="step2_echart" style="width:50%;height:100%;float:right;">
					<div id="huiliu_step2_echarts" style="height:100%;"></div>
				</div>
			</div>
		</div>
  </div>
<!-- 不要改变以下引用顺序 -->
<script type="text/javascript">
var step2SubForm={};
$(function(){
	initHuiliuStep2StcdCombox();
	//dyjyzTable("#wdrs_div","#dyjyz_tab","test_jls_tab.json");
	//getwdrsTable("#wdrs_div","#wdrs_tab","test_jls_tab.json");
	//getwctj_tab("#tlgs_div","#wctj_tab","test_jls_tab.json");
})
function deleteHuiliuStep2Result(){
	var stcd = $("#huiliujisuan_step2_stcd").combox("getValue");
	var pch = $("#huiliujisuan_step2_pch").val();
	var url= basePath + "huiliu/huiliu!deleteStep2Result.action";
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
					queryHuiLiuStep2Table1();
				}else{
					layer.msg(response.message);
				}
			}
		});
	}, function(index){
		layer.close(index);
	});
}
	//导出到excel
	function tlfl_export1(){
		var qjpch=$("#huiliujisuan_step1_pch").val();
		var tc = $("#huiliujisuan_step2_tc").val();
		var ns = $("#huiliujisuan_step2_n").val();
		if(tc==null || $.trim(tc).length<1){
			layer.msg("tc不能为空!");
			return ;
		}
		if(ns==null || $.trim(ns).length<1){
			layer.msg("n不能为空!");
			return ;
		}
		var stcd = $("#huiliujisuan_step2_stcd").combox("getValue");
		var pch = $("#huiliujisuan_step2_pch").val();
		var url= basePath + "huiliu/huiliu!tlfl_export1.action?STCD="+stcd+"&PCH="+pch+"&N="+ns;
		confirm("<i class='icon icon-circle-arrow-up'></i>&nbsp;导出到excel","您确定要导出到excel吗？","icon-info", function(result) {
			   if(result){
				   window.location.href=url;
			   }
			}); 
		
		queryHuiLiuStep2Table1();
	}
	
function tlfl_export2(){
	var stcd = $("#huiliujisuan_step2_stcd").combox("getValue");
	var url= basePath + "huiliu/huiliu!tlfl_export2.action?STCD="+stcd;
	confirm("<i class='icon icon-circle-arrow-up'></i>&nbsp;导出到excel","您确定要导出到excel吗？","icon-info", function(result) {
		   if(result){
			   window.location.href=url;
		   }
		}); 
}
	
function initHuiliuStep2StcdCombox(){
	$("#huiliujisuan_step2_stcd").combox({
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
			loadHuiliuStep2PchSelect();
		}
	});
	if(qjstcd!=null && $.trim(qjstcd).length>0 && qjstnm!=null && $.trim(qjstnm).length>0){
		$("#huiliujisuan_step2_stcd").combox("setValue",{label:qjstnm,value:qjstcd});
		loadHuiliuStep2PchSelect();
	}
}
function loadHuiliuStep2PchSelect(){
	var stcd = $("#huiliujisuan_step2_stcd").combox("getValue");
	var url= basePath + "huiliu/huiliu!huiliuStep2PchSelect.action";
	$.ajax({
		url:url,
		type:"post",
		data:{"stcd":stcd},
		dataType:"json",
		success:function(response){
			var pchList = response.dataList;
			$("#huiliujisuan_step2_pch").empty();
			if(pchList !=null && pchList.length>0){
				for (var i = 0; i < pchList.length; i++) {
					if(i==0 && (qjpch==null || $.trim(qjpch).length<1)){
						qjpch=pchList[i].PCH;
					}
					$("#huiliujisuan_step2_pch").append("<option value='"+pchList[i].PCH+"' > "+pchList[i].PCH+"</option>");
				}
				$("#huiliujisuan_step2_pch").val(qjpch);
				if(qjpch!=null && $.trim(qjpch).length>0){
					queryHuiLiuStep2Table1();
				}
			}else{
				$("#huiliujisuan_step2_pch").append("<option value='' selected='selected' > 请选择  </option>")
			}
		}
	});
}
//第二步最终统计查询
function step2Zhcx(){
	var stcd = $("#huiliujisuan_step2_stcd").combox("getValue");
	var url= basePath + "huiliu/huiliu!huiliuStep2Search.action?STCD="+stcd;
	common_ajax(null, url, function(response) {
			if(response.reflag==1||response.reflag=="1"){
				var mjList = response.mjDataList;
				var mcList = response.mcDataList;
				var cxList = response.cxDataList;
				
				step2SearhcTable("#step2_div3","#step2_table3",cxList);
				var dataOne = [];
				var dataTwo = [];
				for(var i=0;i<mjList.length;i++){
					dataOne.push([mjList[i].m,mjList[i].ertc]);
				}
				for(var j=0;j<mcList.length;j++){
					dataTwo.push([mcList[j].m,mcList[j].ertc]);
				}
				loadStep2ZhcxEchart(dataOne,dataTwo);
			}	
	});
}
function loadStep2ZhcxEchart(dataOne,dataTwo){
	var myChart = echarts.getInstanceByDom(document.getElementById('huiliu_step2_echarts'));
	if(myChart){
		myChart.dispose();
	}
	myChart = echarts.init(document.getElementById('huiliu_step2_echarts'));
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
//第二步第二个表格计算M值
function calcHuiliuStep2Table2(){
	//提交数据调接口，计算m
	//
	var stcd = $("#huiliujisuan_step2_stcd").combox("getValue");
	var pch = $("#huiliujisuan_step2_pch").val();
	var tc = $("#huiliujisuan_step2_tc").val();
	var ns = $("#huiliujisuan_step2_n").val();
	if(tc==null || $.trim(tc).length<1){
		layer.msg("tc不能为空!");
		return ;
	}
	if(ns==null || $.trim(ns).length<1){
		layer.msg("n不能为空!");
		return ;
	}
	if(step2SubForm.table2Data.L==null ||$.trim(step2SubForm.table2Data.L).length<1){
		layer.msg("L不能为空!");
		return ;
	}
	if(step2SubForm.table2Data.J==null ||$.trim(step2SubForm.table2Data.J).length<1){
		layer.msg("J不能为空!");
		return ;
	}
// 	if(step2SubForm.table2Data.K==null ||$.trim(step2SubForm.table2Data.K).length<1){
// 		layer.msg("K不能为空!");
// 		return ;
// 	}
	//调用接口，获取m值
// 		if(step2SubForm.submitData!=null && step2SubForm.submitData.length>0){
// 			for(var i=0;i<step2SubForm.submitData.length;i++){
// 				if(step2SubForm.submitData[i].Rt==null || step2SubForm.submitData[i].Rt!=''){
// 					layer.msg("调用接口的参数中Rt值不能为空!错误数据："+JSON.stringify(step2SubForm.submitData[i]));
// 					return ;
// 				}
// 				if(step2SubForm.submitData[i].t==null || step2SubForm.submitData[i].t!=''){
// 					layer.msg("调用接口的参数中t值不能为空!错误数据："+JSON.stringify(step2SubForm.submitData[i]));
// 					return ;
// 				}
// 				if(parseFloat(step2SubForm.submitData[i].Rt)<0){
// 					layer.msg("调用接口的参数中Rt值不能为负数!错误数据："+JSON.stringify(step2SubForm.submitData[i]));
// 					return ;
// 				}
// 				if(parseFloat(step2SubForm.submitData[i].t)<0){
// 					layer.msg("调用接口的参数中t值不能为负数!错误数据："+JSON.stringify(step2SubForm.submitData[i]));
// 					return ;
// 				}
// 			}
// 		}
// 		if(step2SubForm.table1Data!=null && step2SubForm.table1Data.length>0){
// 			for(var i=0;i<step2SubForm.table1Data.length;i++){
// 				if(step2SubForm.table1Data[i].Rt==null || step2SubForm.table1Data[i].Rt!=''){
// 					layer.msg("调用接口的参数中Rt值不能为空!错误数据："+JSON.stringify(step2SubForm.table1Data[i]));
// 					return ;
// 				}
// 				if(parseFloat(step2SubForm.table1Data[i].Rt)<0){
// 					layer.msg("调用接口的参数中Rt值不能为负数!错误数据："+JSON.stringify(step2SubForm.table1Data[i]));
// 					return ;
// 				}
// 			}
// 		}
// 		var data={
// 			Qm:step2SubForm.table2Data.Q,
// 			F:step2SubForm.table2Data.LLMJ,
// 			tc:tc,
// 			_t:step2SubForm.table2Data.SJJG,
// 			Rup:step2SubForm.table2Data.R,
// 			L:step2SubForm.table2Data.L,
// 			J:step2SubForm.table2Data.J,
// 			n:ns,
// 			DATA:JSON.stringify({DATA:step2SubForm.submitData}),
// 			RUPLIST:JSON.stringify({RUPLIST:step2SubForm.table1Data})
// 		};
		
		var data={
			Qm:129,
			F:96.4,
			tc:tc,
			_t:step2SubForm.table2Data.SJJG,
			Rup:60.8,
			L:23,
			J:0.00821,
			n:ns,
			DATA:JSON.stringify({"DATA":[{"t":"1","Rt":"12.3"},{"t":"2","Rt":"10.6"},{"t":"3","Rt":"9.6"},{"t":"4","Rt":"9.2"},{"t":"5","Rt":"8.7"},{"t":"6","Rt":"7.9"},{"t":"7","Rt":"7.3"},{"t":"8","Rt":"6.8"},{"t":"9","Rt":"6.3"},{"t":"10","Rt":"5.8"},{"t":"11","Rt":"5.4"},{"t":"12","Rt":"5.0"},{"t":"13","Rt":"4.6"},{"t":"14","Rt":"4.3"}]}),
			RUPLIST:JSON.stringify({"RUPLIST":[{"t":"1","Rt":"12.3"},{"t":"2","Rt":"10.6"},{"t":"3","Rt":"9.6"},{"t":"4","Rt":"9.2"},{"t":"5","Rt":"8.7"},{"t":"6","Rt":"7.9"},{"t":"7","Rt":"7.3"},{"t":"8","Rt":"6.8"},{"t":"9","Rt":"6.3"},{"t":"10","Rt":"5.8"},{"t":"11","Rt":"5.4"},{"t":"12","Rt":"5.0"},{"t":"13","Rt":"4.6"},{"t":"14","Rt":"4.3"}]})
		};
		console.log("--------------datasubmit------"+JSON.stringify(data));
		$.ajax({
			url:serverUrl+"/CHLService.asmx/getm2",
			type:'post',
			data:data,
			dataType:"json",
			success:function(response){
				console.log("--------------response------"+JSON.stringify(response));
				if(response.CODE==1||response.CODE=="1"){
					var data=response.DATA;
					if(data!=null && data.length>0){
						//获取到m值，然后发送到后台，计算
						step2SubForm.STCD=stcd;
						step2SubForm.PCH=pch;
						step2SubForm.TC=tc;
						step2SubForm.N=ns;
						step2SubForm.table2Data.M=data[0].m;
						step2SubForm.table2Data.TJ=data[0].tcAve;
						step2SubForm.table2Data.R=data[0].Rup;
						step2SubForm.table2Data.RJ=data[0].RtAve;
						step2SubForm.table2Data.JS2=data[0]["RtAven/tcAven"];
						step2SubForm.table2Data.JS1=data[0]["tt"];
						step2SubForm.table2Data.JS3=data[0]["Qm/K"];
						step2SubForm.table2Data.K=data[0].K;
						var data={data:JSON.stringify(step2SubForm)};
						data2=data;
						var url= basePath + "huiliu/huiliu!calcStep2Table2.action";
						common_ajax(data, url, function(response) {
							if(response.reflag==1||response.reflag=="1"){
								var table2Data = response.table2Data;
								mjscsTable("#step2_div2","#step2_table2",table2Data);
							}
						});
					}
				}else{
					layer.msg("调用计算m接口出错!");
				}
			}
		});
	//
}
//第二步第一个表格计算
function calcHuiliuStep2Table1(stcd,pch){
	//调用接口获取R上均和TC均
	var rj=10;
	var tj=10;
	//
	var url= basePath + "huiliu/huiliu!calcStep2Table1.action?STCD="+stcd+"&PCH="+pch+"&RJ="+rj+"&TJ="+tj;
	common_ajax(null, url, function(response) {
		if(response.reflag==1||response.reflag=="1"){
			var table2Data = response.table2Data;
			step2SubForm.table2Data=table2Data;
			mjscsTable("#step2_div2","#step2_table2",table2Data);
		}
	});
}
//查询第二步第一个数据表格
function queryHuiLiuStep2Table1(){
	var stcd = $("#huiliujisuan_step2_stcd").combox("getValue");
	var pch = $("#huiliujisuan_step2_pch").val();
	var url= basePath + "huiliu/huiliu!huiliuStep2Table1.action?STCD="+stcd+"&PCH="+pch;
	common_ajax(null, url, function(response) {
		if(response.reflag==1||response.reflag=="1"){
			var table1Data = response.table1Data;
			var submitData = response.submitData;
			var table2Data = response.table2Data;
			step2SubForm.table1Data=table1Data;
			step2SubForm.submitData=submitData;
			step2SubForm.table2Data=table2Data;
			dyjyzTable("#step2_div1","#step2_table1",table1Data);
			if(table2Data==null){
				table2Data={};
			}else{
				$("#huiliujisuan_step2_tc").val(table2Data.TC);
				$("#huiliujisuan_step2_n").val(table2Data.N);
			}
			mjscsTable("#step2_div2","#step2_table2",table2Data);
			//calcHuiliuStep2Table1(stcd,pch);
		}
	});
}
//1、导入净雨值
	function dyjyzTable(chart, tab, tableData){
	    var height = $(chart).height();
	    var width = $(chart).width();
	    layui.use('table', function() {
	        var table = layui.table;
	        table.render({
	            elem: tab,
	            height: height,
	            width: width,
	            id:'step2Table1',
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
    //计算m的参数表格
    function mjscsTable(chart, tab, tableData){
    	$("#huiliujisuan_step2_n").val(tableData.N);
	    var height = $(chart).height();
	    var width = $(chart).width();
	    layui.use('table', function() {
	        var table = layui.table;
	        table.render({
	            elem: tab,
	            height: height,
	            width: width,
	            id:'step2Table2',
	            data:[tableData],
	            limit:1,
	            cols: [
	                [{
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
	                    field: 'R',
	                    title: 'R上(mm)',
	                    rowspan:2,
	                    width:100
	                },{
	                    field: 'TJ',
	                    rowspan:2,
	                    width:100,
	                    title: 'tc均(小时)'
	                },{
	                    field: 'RJ',
	                    rowspan:2,
	                    width:100,
	                    title: 'R上均(mm)'
	                },{
	                    field: 'LLMJ',
	                    rowspan:2,
	                    width:120,
	                    title: '流域面积F(km2)'
	                },{
	                    field: 'L',
	                    title: 'L',
	                    edit:'text',
	                    rowspan:2,
	                    width:80
	                },{
	                    field: 'J',
	                    title: 'J',
	                    edit:'text',
	                    rowspan:2,
	                    width:100
	                },{
	                    field: 'JS1',
	                    title: 'τ=(0.278FR_上)/Qm',
	                    rowspan:2,
	                    width:150
	                },
	                {
	                    field: 'NT',
	                    align:'center',
	                    title: 'N='+$("#huiliujisuan_step2_n").val(),
	                    colspan:4
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
	                    width:160
	                },{
	                    field: 'JS3',
	                    title: 'Qm/K',
	                    width:'130'
	                },{
	                    field: 'M',
	                    title: '汇流参数m',
	                    width:'100'
	                }
	                ]
	            ],
	            page: false,
	            done: function(res, curr, count){
			        step2SubForm.table2Data=res.data&&res.data.length>0?res.data[0]:null;
			    }
	        });
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
	            id:'step2Table3',
	            limit:tableData.length,
	            data:tableData,
	            cols: [
	            	  [//标题栏
	            	    {field: 'PCH', title: '洪号', rowspan: 2,align: 'center',width:100} //rowspan即纵向跨越的单元格数
	            	    ,{field: 'ERET', title: 'R上/tc(毫米/小时) ',  rowspan: 2,align: 'center',width:150}
	            	    ,{field: 'MJ', title: 'm计 ',  rowspan: 2,align: 'center',width:80}
	            	    ,{align: 'center', title: 'R上/tc ~m', colspan: 3} //colspan即横跨的单元格数，这种情况下不用设置field和width
	            	  ], [
	            	    {field: 'MC', title: 'm查', align: 'center',width:80}
	            	    ,{field: 'MS', title: '(m计-m查)/ m计',align: 'center',width:150 }
	            	    ,{field: 'SFHG', title: '是否合格', align: 'center',width:100}
	            	  ]
	            ],
	            page: false
	        });
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
