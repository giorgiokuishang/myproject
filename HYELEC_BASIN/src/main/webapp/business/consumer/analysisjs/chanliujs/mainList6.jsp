<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
	<div class="container-fluid">
		<div style="width:100%;height:500px;">
			<div class="col-md-8" style="height:100%;">
				<div>
					<label>测站名称:<input type="hidden" id="stcd">  </label>
					<input id="stnm" type="text" name="" class="form-control" 
					readonly style="width: 100px;display:inline;">
					<label>洪号:	<input type="hidden" id="start"><input type="hidden" id="end"></label>
					<input id="TAB_PCH" type="text"  class="form-control"
					style="width: 130px;display:inline;" >
					<label >蒸发资料关联站:</label>
					<input id="step6_zfzlglz" type="text" name="pa" class="form-control" 
					style="width: 130px;display:inline;" value="">
					<label>Im:</label>
					<input id="step6_im" type="text" class="form-control" style="width: 70px;display:inline;">
					<label>b:</label>
					<input id="step6_b" type="text" class="form-control" style="width: 70px;display:inline;">
				</div>
				<div>
					<label>R实:</label>
					<input id="rs" type="text"  class="form-control"
					style="width: 70px;display:inline;" value="">
					<label>R下:</label>
					<input id="rx" type="text" class="form-control"
					style="width: 70px;display:inline;" value="">
					<label > 前期影响雨量Pa(mm):</label>
					<input id="step6_pa" type="text" name="pa" class="form-control" 
					style="width: 70px;display:inline;" value="">
					<label>流域面积(km²):</label> 
					<input id="step6_lymj" type="text" class="form-control" style="width: 70px;display:inline;">
					<input type="button" onclick="deleteStep6Result()" id="chanliu_daochu6" value="删除"  class="btn btn-primary" >
					<input type="button" onclick="cl_excel6()" id="chanliu_daochu6" style="margin-left: 5px;" value="导出到excel"  class="btn btn-primary" >
					<input type="button" onclick="calcStep6Service();" value="计算" class="btn">
					<input type="button" onclick="saveStep6Result();" value="保存" class="btn">
				</div>
				<div id="wdrs_div" style="height:calc(100% - 70px);width:100%;">
					<table class="layui-hide" id="wdrs_tab" lay-filter="hszlb_table"></table>	
				</div>
			</div>
			<div id="jyjl_div" class="col-md-4" style="height:100%;">
				<div id="jyjl_chart" style="width: 100%;height:100%;"></div>
			</div>
		</div>
		<div style="width:100%;height:80px;">
			<div style="height:100%;width:100%;">
				<div id="step6_div2" style="width:70%;height:100%;float:left;">
					<table class="layui-hide" id="step6_table2" lay-filter="hszlb_table"></table>
				</div>
			</div>
		</div>
		<div style="width:100%;height:400px;margin-top:30px;">
			<div style="height:40px;">
				<div style="width:50%;float:left;padding-left:30px;line-height:40px;"></div>
				<div style="width:50%;float:right;">
					<button class="layui-btn" value="单站综合及误差统计" onclick="chanliuStep6zhcx()">单站综合及误差统计</button>
					<input type="button" onclick="chanliu6Excel()" id="shdwxf_export3" style="margin-left: 20px;" value="导出到excel"  class="layui-btn" >	
				</div>
			</div>
			<div style="height:calc(100% - 40px);width:100%;">
				<div id="step6_div3" style="width:60%;height:100%;float:left;">
					<table class="layui-hide" id="step6_table3" lay-filter="hszlb_table"></table>
				</div>
				<div id="step6_echart" style="width:40%;height:100%;float:right;">
					<div id="chanliu_step6_echarts" style="height:400px;"></div>
				</div>
			</div>
		</div>
	</div>
<script>
var _stepCalcResultTableHaveChange=false;//记录第六步计算结果中R查是否已手动修改
var _changeData=new Array();
var _step6InitData=[];
var _step6EchartData=[];
var hjp1;
var hje1;
var hjsdr1;
var hjrg1;
var hjrgpx1;
$(function(){
	if(secondBeginDate==null || secondBeginDate==""){
		secondBeginDate=beginDate;
	}
	if(secondEndDate==null || secondEndDate==""){
		secondEndDate=endDate;
	}
	$("#stnm").val(stnm);
	$("#stcd").val(stcd);
	$("#start").val(secondBeginDate);
	$("#end").val(secondEndDate);
	$("#jiange").val(interval);
    $("#step6_pa").val(pa6);
    if(rshi!=null){
    	$("#rs").val(Number(rshi).toFixed(1));
    }
    $("#rx").val(rxia);
	$("#TAB_PCH").val(hh);//洪号
	$("#step6_lymj").val(lymj);//流域面积
	step6ZfglzCombox();
	initStep6Data();
	//getwdrsTable("#wdrs_div","#wdrs_tab",basePath + "chanliu/chanliu!step6.action",{"stcd":stcd,"pch":hh,"start":beginDate, "end":endDate});
	//getGxt("jyjl_chart","",stcd);
	//loadRrffEcharts();
	//testEcharts();
});
	
function step6ZfglzCombox(){
	var bd=secondBeginDate.substring(0,10)+" 00:00:00";
	var ed=secondEndDate.substring(0,10);
	var edate=addDate(ed,1)+" 23:59:59";
	$("#step6_zfzlglz").combox({
		url:basePath + "chanliu/chanliu!queryZfzCombox.action?start="+bd+"&end="+edate,
		label:"STNM",
		value:"STCD",
		dataKey:"dataList",
		height:'250px',
		width:'150px',
		isPager:false,
		select:function(item){
			loadStep6EdataValues(item.STCD,bd,edate);
		}
	});
}
//加载E雨数据
function loadStep6EdataValues(stcd,bd,ed){
	_step6InitData.splice(_step6InitData.length-1, 1);
	$.ajax({
		url:basePath+"chanliu/chanliu!queryEdata.action",
		type:"post",
		data:{
			stcd:stcd,
			start:bd,
			end:ed,
			INTERVAL:interval,
			DATA:JSON.stringify(_step6InitData)
		},
		dataType:"json",
		success:function(response){
			if(response.reflag==1||response.reflag=="1"){
				loadStep6Table("#wdrs_div","#wdrs_tab",response.tableData);
			}else{
				layer.msg(response.message);
			}
		}
	});	
}
	function cl_excel6(){
		var h=hh;
		if(h==''){
			confirm("产流计算", "请选择一条计算结果")
			return false;
		}
		var url= basePath + "chanliu/chanliu!chanLiuExportExcel6.action?pch="+hh+"&stcd="+stcd +"&start="+secondBeginDate+"&end="
				+secondEndDate+"&hjp1="+hjp1+"&hje1="+hje1+"&hjsdr1="+hjsdr1+"&hjrg1="+hjrg1+"&hjrgpx1="+hjrgpx1+"&INTERVAL="+interval;      
		confirm("<i class='icon icon-circle-arrow-up'></i>&nbsp;导出到excel","您确定要导出到excel吗？","icon-info", function(result) {
			   if(result){
				   window.location.href=url;
			   }
			});
	}
	
function chanliu6Excel(){
	var stcd=$("#stcd").val();
	var url= basePath + "chanliu/chanliu!chanliu6Excel.action?stcd="+stcd;      
	confirm("<i class='icon icon-circle-arrow-up'></i>&nbsp;导出到excel","您确定要导出到excel吗？","icon-info", function(result) {
		   if(result){
			   window.location.href=url;
		   }
		});
}
function chanliuStep6zhcx(){
	var stcd=$("#stcd").val();
	var url= basePath + "chanliu/chanliu!chanliuStep6Zhcx.action" ;
	$.ajax({
		url:url,
		type:"post",
		data:{stcd:stcd},
		dataType:"json",
		success:function(response){
			if(response.reflag==1 || response.reflag=="1"){
				var mjList = response.mjDataList;
				var mcList = response.mcDataList;
				var cxList = response.cxDataList;
				step6SearhcTable("#step6_div3","#step6_table3",cxList);
				
				var dataOne = [];
				var dataTwo = [];
				for(var i=0;i<mjList.length;i++){
					dataOne.push([mjList[i].fc,mjList[i].rctc]);
				}
				for(var j=0;j<mcList.length;j++){
					dataTwo.push([mcList[j].fc,mcList[j].rctc]);
				}
				loadStep6ZhcxEchart(dataOne,dataTwo);
			}else{
				layer.msg(response.message);
			}
		}
	});
}
//第三步散点趋势图
function loadStep6ZhcxEchart(dataOne,dataTwo){
	var myChart = echarts.getInstanceByDom(document.getElementById('chanliu_step6_echarts'));
	if(myChart){
		myChart.dispose();
	}
	myChart = echarts.init(document.getElementById('chanliu_step6_echarts'));
	option = {
	    title : {
	        text: '散点分部趋势图'
	    },
	    legend: {
	        data:['fc计散点','趋势图']
	    },
	    xAxis : [
	        {
	            type : 'value',
	            name:'FC',
	            scale:true,
	            axisLabel : {
	                formatter: '{value}'
	            }
	        }
	    ],
	    yAxis : [
	        {
	            type : 'value',
	            name:'RC/TC',
	            scale:true,
	            axisLabel : {
	                formatter: '{value}'
	            }
	        }
	    ],
	    series : [
	        {
	            name:'fc计',
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
//单站综合及误差统计table
	function step6SearhcTable(chart, tab, tableData){
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
	            	    {field: 'PCH', title: '洪号',width:'150',align: 'center'} //rowspan即纵向跨越的单元格数
	            	    ,{field: 'TC', title: 'T<sub>c</sub>',width:'100',align: 'center'}
	            	    ,{field:'RC',align: 'center', title: 'R<sub>c</sub>',width:'80'} //colspan即横跨的单元格数，这种情况下不用设置field和width
	            	    ,{field:'RCTC',title: 'R<sub>c</sub>/T<sub>c</sub>',width:'80',align: 'center'} //colspan即横跨的单元格数，这种情况下不用设置field和width
	            	    ,{field:'FC',title: 'FC',width:'80',align: 'center'} //colspan即横跨的单元格数，这种情况下不用设置field和width
	            	  	,{field:'FCC',title: 'FC<sub>查</sub>',width:'80',align: 'center'} //colspan即横跨的单元格数，这种情况下不用设置field和width
	            	  	,{field:'FCS',title: '(FC-FC<sub>查</sub>)/FC',width:'130',align: 'center'} //colspan即横跨的单元格数，这种情况下不用设置field和width
	            	  	,{field:'SFHG',title: '是否合格',width:'80',align: 'center'} //colspan即横跨的单元格数，这种情况下不用设置field和width
	            	  ]
	            ],
	            page: false
	        });
	    });
    }
function deleteStep6Result(){
	layer.confirm("确定要删除原有计算结果数据吗?", { title: "删除确认" }, function (index) {
            layer.close(index);
			var url= basePath + "chanliu/chanliu!deleteStep6Result.action" ;
			$.ajax({
				url:url,
				type:"post",
				data:{stcd:stcd,pch:hh},
				dataType:"json",
				success:function(response){
					if(response.reflag==1 || response.reflag=="1"){
						initStep6Data();
					}else{
						layer.msg(response.message);
					}
				}
			});
	});
}

//左边的pa，在右边的曲线不存在，那么判断左边的Pa介于哪两个Pa中间，然后
var col=[];
var col1={};
//计算
var pData=['0', '5', '10', '15', '20', '25', '30','35','40','45']; //Y周
var p1Data="0, 0.5,1.5 ,3.5 , 6, 8, 10,12,14,16"; //Pa=10 X
var p2Data="0, 1, 2.5, 5, 7.8, 10, 12,14,16,18"; //Pa=20 X
var p3Data="0, 2, 4, 7, 10, 12.5, 15,17.5,20,22.5"; //Pa=30X
var paData=[];
var rData=[];
var paLineList=new Array();
var fcjg={};
function testEcharts(){
	var rx=4.07;
	var fcslist=[18.91,17.00,14.53,12.82,12.49,12.04,11.73,7.62,7.62,7.55,7.54,6.59,6.44,5.40,4.90,4.75,3.99,3.99,3.99,3.99,3.99,3.99,3.99,3.99,3.99,3.99,3.99,3.99,3.99,3.99,3.20,2.24];
	var fcjlist=new Array();
	var fcolist=new Array();
	var rjglist=new Array();
	var len=fcslist.length;
	for(var i=0;i<len;i++){
		var fcs=fcslist[i]*60/interval;
		fcolist.push([i,fcs]);
		var rt=0;
		var tc=0;
		for(var j=len-1;j>-1;j--){
			if(fcslist[j]<fcslist[i]){
				rt=rt+fcslist[j];
				tc=tc+interval;
			}else{
				break;
			}
		}
		var fcj=(rx-rt)*60/tc;
		fcjlist.push([i,fcj]);
		rjglist.push([i,fcj-fcs]);
	}
	
    			option = {
                		title: {
							text: 'test'
					   	},
            		    xAxis: {
            		        name:'R(mm)',
            		        type: 'value',
            		        min:0,
            		        max:32
            		    },
            		    tooltip: {
            		        trigger: 'axis'
            		    },
            		    grid: {
            		        left: '3%',
            		        right: '50',
            		        bottom: '3%',
            		        containLabel: true
            		    },
            		    legend: {
            		    	top:30,
            		        data:["fcs","fcj","result"]
            		    },
            		    yAxis: {
            		    	name:'P(mm)',
            		        type: 'value',
            		        boundaryGap: false,
            		        min:0,
            		        max:20
            		    },
            		    series: [
            		      {
			                "name":"fcs",
			                type: 'line',
							smooth:true,
							smoothMonotone:'none',
			                "lineStyle": {
			                "normal":
			                {
			                "width": 1.5
			                }
			                },
			                "data":fcolist
			              },
			              {
			                "name":"fcj",
			                type: 'line',
							smooth:true,
							smoothMonotone:'none',
			                "lineStyle": {
			                "normal":
			                {
			                "width": 1.5
			                }
			                },
			                "data":fcjlist
			              },
			              {
			                "name":"result",
			                type: 'line',
							smooth:true,
							smoothMonotone:'none',
			                "lineStyle": {
			                "normal":
			                {
			                "width": 1.5
			                }
			                },
			                "data":rjglist
			              }
			            ]
            		    };	
	            var monthLineChart = echarts.init(document.getElementById("jyjl_chart"));
	            //清空画布，防止缓存
	            monthLineChart.clear();
	            monthLineChart.setOption(option); 
}
function initStep6Data(){
	$.ajax({
		url:basePath+"chanliu/chanliu!initLoadStep6Data.action",
		type:"post",
		data:{"stcd":stcd,"pch":hh,"start":secondBeginDate, "end":secondEndDate,"INTERVAL":interval},
		dataType:"json",
		success:function(response){
			if(response.reflag==1||response.reflag=="1"){
				var second=response.secondData;
				var stinfo=response.stinfo;
				if(stinfo!=null){
					$("#step6_zfzlglz").combox("setValue",{label:stinfo.stnm,value:stinfo.stcd});
				}
				var planValue = response.planValue;
				if(!planValue){
					planValue={'K':'1.2','B':'0.3','C':'0.1','IM':'0.02','WM':'130','WUM':'0.2','WLM':'0.8','SM':'60','EX':'1.5','KG':'0.4','KI':'0.3'};
				}
				if(second!=null){
    				$("#rx").val(second.rx);
    				$("#step6_pa").val(second.pa);
    				$("#rs").val(second.rs);
					$("#step6_lymj").val(second.LLMJ);//流域面积
					if(second.im){
						$("#step6_im").val(second.im);
					}else{
						$("#step6_im").val(planValue.WM);
					}
					if(second.b){
						$("#step6_b").val(second.b);
					}else{
						$("#step6_b").val(planValue.B);
					}    				loadStep6Table("#wdrs_div","#wdrs_tab",response.tableData);
				}else{
					$("#step6_im").val(planValue.WM);
					$("#step6_b").val(planValue.B);
				}
				var resultBean=response.resultBean;
				if(resultBean==null){
					resultBean={};
				}
				var echartData = response.echartData;
				loadSaveEcharts(echartData);
				loadStep6ResultTable("#step6_div2","#step6_table2",[resultBean]);
			}else{
				layer.msg(response.message);
			}
		}
	});
}
function loadStep6ResultTable(chart,tab,tableData){
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
	            	 [//标题栏
	            	    {field: 'pch', title: '洪号',align: 'center'} //rowspan即纵向跨越的单元格数
	            	    ,{field: 'tc', title: 'T<sub>c</sub>',align: 'center'}
	            	    ,{field:'rc',align: 'center', title: 'R<sub>c</sub>'} //colspan即横跨的单元格数，这种情况下不用设置field和width
	            	    ,{field:'rctc',title: 'R<sub>c</sub>/T<sub>c</sub>',align: 'center'} //colspan即横跨的单元格数，这种情况下不用设置field和width
	            	    ,{field:'fc',title: 'FC',align: 'center'} //colspan即横跨的单元格数，这种情况下不用设置field和width
	            	  ]
	            ],
	            page: false
	        });
	    });
}
function calcStep6Service(){
	var pa=$("#step6_pa").val();
	if(pa==null || $.trim(pa).length<1){
		alert("Pa不能为空")
		return false;
	}
	var rs=$("#rs").val();
	if(rs==null || $.trim(rs).length<1){
		alert("R实不能为空")
		return false;
	}
	var rx=$("#rx").val();
	if(rx==null || $.trim(rx).length<1){
		alert("R下不能为空")
		return false;
	}
	var Im=$("#step6_im").val();
	if(Im==null || $.trim(Im).length<1){
		alert("Im不能为空")
		return false;
	}
	var b=$("#step6_b").val();
	if(b==null || $.trim(b).length<1){
		alert("b不能为空")
		return false;
	}
	if(parseFloat(pa)>parseFloat(Im)){
		alert("Im必须大于等于Pa值！")
		return false;
	}
	var reqData=[];
	var calcData=col.data;
	var dataList=[];
	if(calcData!=null && calcData.length>0){
		var epe=0;
		for(var i=0;i<calcData.length-1;i++){
			var rowData=calcData[i];
			if(i==0){
				epe=rowData.PE;
			}else{
				epe=parseFloat(epe)+parseFloat(rowData.PE);
			}
			rowData.EPE=epe;
			rowData.PAPE=Number(parseFloat(pa)+parseFloat(epe)).toFixed(1);
			dataList.push(rowData);
			reqData.push({t:rowData.DATE,p:rowData.EPE});
		}
	}
	loadStep6Table("#wdrs_div","#wdrs_tab",dataList);
	
	var url=serverUrl+"/CHLService.asmx/getPaPR";
	var reqJson={
		Pa:$("#step6_pa").val(),
		Im:$("#step6_im").val(),
		b:$("#step6_b").val(),
		DATA:JSON.stringify({DATA:reqData})
	};
	console.log("----------------request-----------------data-----------"+JSON.stringify(reqJson));
	$.ajax({
			url:url,
			type:'post',
			data:reqJson,
			dataType:"json",
			success:function(response){
				console.log("--------------response------"+JSON.stringify(response));
				if(response.CODE==1||response.CODE=="1"){
					var data=response.DATA;
					var lineList=response.LineDATA;
					_step6EchartData=lineList;
					loadCalcServcieEchartsData(lineList);
					saveCalcServiceResult(calcData,data,lineList,rs,rx,pa);
				}else{
					layer.msg("调用计算m接口出错!");
				}
			}
		});
}
function saveCalcServiceResult(calcData,data,lineList,rs,rx,pa){
	if(calcData!=null && calcData.length>0){
		calcData.splice(calcData.length-1,1);
	}
	var json={
			"czmc":$("#stnm").val(),
			"stcd":$("#stcd").val(),
			"pch":$("#TAB_PCH").val(),
			"LYMJ":$("#step6_lymj").val(),
			"rs":rs,
			"rx":rx,
			"pa":pa,
			"zfzstcd":$("#step6_zfzlglz").val(),
			"Im":$("#step6_im").val(),
			"b":$("#step6_b").val(),
			"DATA":JSON.stringify({
				interval:interval,
				tabledata:calcData,
				calcrdata:data
			})
    	}
    var url= basePath + "chanliu/chanliu!saveCalcServiceResult.action";
    $.ajax({
        url : url,
        type : "post",
        dataType : "json",
        async : false,
        traditional: true,
        data : json,
        success : function(response) {
        	if(response.reflag==1||response.reflag=="1"){
        		layer.msg("计算结果成功");
        		fcjg=response.FCJG?response.FCJG:{};
        		var resultData={
        			stcd:fcjg.STCD!=null?fcjg.STCD:"",
        			pch:fcjg.PCH!=null?fcjg.PCH:"",
        			rc:fcjg.RC!=null?fcjg.RC:"",
        			tc:fcjg.TC!=null?fcjg.TC:"",
        			rctc:fcjg.RCTC!=null?fcjg.RCTC:"",
        			fc:fcjg.FC!=null?fcjg.FC:""
        		};
        		console.log("-----------------table---------data------"+JSON.stringify(fcjg));
        		loadStep6Table("#wdrs_div","#wdrs_tab",response.tableData); 
        		loadStep6ResultTable("#step6_div2","#step6_table2",[resultData]);
        	}else{
        		layer.msg(response.message);
        	}
        }
    });
}
function calcStep6Data(){
	var pa=$("#step6_pa").val();
	if(pa==null || $.trim(pa).length<1){
		alert("Pa不能为空")
		return false;
	}
	var rs=$("#rs").val();
	if(rs==null || $.trim(rs).length<1){
		alert("R实不能为空")
		return false;
	}
	var rx=$("#rx").val();
	if(rx==null || $.trim(rx).length<1){
		alert("R下不能为空")
		return false;
	}
	if( $("#step6_pa").val()==""){
			alert("Pa不能为空")
			return false;
		}
	if($("#rs").val()==""){
		alert("R实不能为空")
		return false;
	}
	if($("#rx").val()==""){
		alert("R下不能为空")
		return false;
	}
	var url= basePath + "chanliu/chanliu!calcStep6Data.action";
	var calcData=col.data;
	if(calcData!=null && calcData.length>0){
		calcData.splice(calcData.length-1,1);
	}
 	var json={
			"czmc":$("#stnm").val(),
			"stcd":$("#stcd").val(),
			"pch":$("#TAB_PCH").val(),
			"LYMJ":$("#step6_lymj").val(),
			"rs":rs,
			"rx":rx,
			"pa":pa,
			"DATA":JSON.stringify({
			erisedit:(_stepCalcResultTableHaveChange?1:0),
			editData:_changeData,
			interval:interval,tabledata:calcData,echartdata:paLineList})
    	}
    $.ajax({
        url : url,
        type : "post",
        dataType : "json",
        async : false,
        traditional: true,
        data : json,
        success : function(response) {
        	if(response.reflag==1||response.reflag=="1"){
        		layer.msg("计算结果成功");
        		fcjg=response.FCJG?response.FCJG:{};
        		var resultData={
        			stcd:fcjg.STCD!=null?fcjg.STCD:"",
        			pch:fcjg.PCH!=null?fcjg.PCH:"",
        			rc:fcjg.RC!=null?fcjg.RC:"",
        			tc:fcjg.TC!=null?fcjg.TC:"",
        			rctc:fcjg.RCTC!=null?fcjg.RCTC:"",
        			fc:fcjg.FC!=null?fcjg.FC:""
        		};
        		console.log("-----------------table---------data------"+JSON.stringify(fcjg));
        		loadStep6Table("#wdrs_div","#wdrs_tab",response.tableData); 
        		loadStep6ResultTable("#step6_div2","#step6_table2",[resultData]);
        	}else{
        		layer.msg(response.message);
        	}
        }
    });
}
function saveStep6Result(){
		var calcData=col.data;
		if(calcData!=null && calcData.length>0){
			calcData.splice(calcData.length-1,1);
		}
		var url= basePath + "chanliu/chanliu!saveStep6Result.action";
		$.ajax({
	        url : url,
	        type : "post",
	        dataType : "json",
	        traditional: true,
	        data : {stcd:$("#stcd").val(),pch:$("#TAB_PCH").val(),
	        DATA:JSON.stringify({
	        Im:$("#step6_im").val(),
	        b:$("#step6_b").val(),
	        zfzstcd:$("#step6_zfzlglz").combox("getValue"),
	        lymj:$("#step6_lymj").val(),
			rs:$("#rs").val(),
			rx:$("#rx").val(),
			pa:$("#step6_pa").val(),
	        tableData:calcData,
	        fcjg:fcjg,
	        echartData:_step6EchartData})},
	        success : function(response) {
	        	if(response.reflag==1||response.reflag=="1"){
	        		layer.msg("保存结果成功")
	        	}else{
	        		layer.msg(response.message);
	        	}
	        }
	    });
}
//初始化统计图
var myChart = echarts.init(document.getElementById('jyjl_chart'));
function isNumber(val){
    var regPos = /^\d+(\.\d+)?$/;
    var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; //负浮点数
    if(regPos.test(val) || regNeg.test(val)){
        return true;
    }else{
        return false;
    }

}
function getHejiRowData(tableData){
	if(tableData!=null && tableData.length>0){
		var heji={};
		var hjp=0,hje=0,hjsdr=0,hjrg=0,hjrgpx=0,hjerg=0;
		for(var i=0;i<tableData.length;i++){
			var p=tableData[i].P;
			if(p!=null && isNumber(p)){
				hjp=parseFloat(hjp)+parseFloat(p);
			}
			var e=tableData[i].E;
			if(e!=null && isNumber(e)){
				hje=parseFloat(hje)+parseFloat(e);
			}
			var sdrc=tableData[i].SDRC;
			if(sdrc!=null && isNumber(sdrc)){
				hjsdr=parseFloat(hjsdr)+parseFloat(sdrc);
			}
			var r=tableData[i].R;
			if(r!=null && isNumber(r)){
				hjrg=parseFloat(hjrg)+parseFloat(r);
			}
			var rgdx=tableData[i].RGDX;
			if(rgdx!=null && isNumber(rgdx)){
				hjrgpx=parseFloat(hjrgpx)+parseFloat(rgdx);
			}
			tableData[i].RGDX=Number(rgdx).toFixed(1);
			var er=tableData[i].ER;
			if(er!=null && isNumber(er)){
				hjerg=parseFloat(hjerg)+parseFloat(er);
			}
			tableData[i].ER=Number(er).toFixed(1);
		}
		hjp=Number(hjp).toFixed(1);
		hje=Number(hje).toFixed(1);
		hjsdr=Number(hjsdr).toFixed(2);
		hjrg=Number(hjrg).toFixed(2);
		hjrgpx=Number(hjrgpx).toFixed(2);
		hjerg=Number(hjerg).toFixed(2);
		heji={DATE:"合计",P:hjp,E:hje,SDRC:hjsdr,R:hjrg,RGDX:hjrgpx};
		hjp1=hjp;
		hje1=hje;
		hjsdr1=hjsdr;
		hjrg1=hjrg;
		hjrgpx1=hjrgpx;
		tableData.push(heji);
	}
	return tableData;
}
function loadStep6Table(chart, tab,dataList){
		_stepCalcResultTableHaveChange=false;
		_changeData=new Array();
		var height = $(chart).height();
	    var width = $(chart).width();
	    dataList=getHejiRowData(dataList);
	    layui.use('table', function() {
	        var table = layui.table;
	        table.render({
	            elem: tab,
	            data: dataList,
	            height: height,
	            width: width,
	            id:'cz',
	            limit:dataList.length,
	            cols: [
	                [{
	                    field: 'XH',
	                    title: '序号',
	                    templet:function(row){
	                    	return (row.LAY_TABLE_INDEX+1);
	                    }
	                },{
	                    field: 'DATE',
	                    title: '日期',
	                    width:'150'
	                },{
	                    field: 'P',
	                    title: 'P',
	                    width:'70'
	                },{
	                    field: 'E',
	                    title: 'E雨',
	                    //edit: 'text',
	                    width:'70'
	                },{
	                    field: 'PE',
	                    title: 'P-E雨',
	                    edit: 'text',
	                    width:'70'
	                },{
	                    field: 'EPE',
	                    title: '∑(P-E雨)',
	                    edit: 'text',
	                    width:'100'
	                },{
	                    field: 'PAPE',
	                    title: 'Pa+∑(P-E雨)',
	                    edit: 'text',
	                    width:'120'
	                },{
	                    field: 'ERC',
	                    title: '∑R查',
	                    edit: 'text',
	                    width:'80'
	                },{
	                    field: 'SDRC',
	                    title: '时段R查',
	                    edit: 'text',
	                    width:'100'
	                },{
	                    field: 'R',
	                    title: 'R改',
	                    edit: 'text',
	                    width:'80',
	                    templet:function(row){
	                    	if(row.R!=null){
	                    		return Number(row.R).toFixed(1);
	                    	}
	                    	return "";
	                    }
	                },{
	                    field: 'RGDX',
	                    title: 'R改大→小',
	                    edit: 'text',
	                    width:'100',
	                    templet:function(row){
	                    	if(row.RGDX!=null){
	                    		return Number(row.RGDX).toFixed(1);
	                    	}
	                    	return "";
	                    }
	                },{
	                    field: 'ER',
	                    title: '∑R改',
	                    edit: 'text',
	                    width:'80',
	                    templet:function(row){
	                    	if(row.ER!=null){
	                    		return Number(row.ER).toFixed(1);
	                    	}
	                    	return "";
	                    }
	                }]
	            ],
	            page: false,
	            done: function(res, curr, count){
	                //如果是异步请求数据方式，res即为你接口返回的信息。
	                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
	                col=res;
	                _step6InitData=res.data;
	                //得到当前页码
	                //得到数据总量
	          }
	        });
	        table.on('edit(hszlb_table)', function(obj){ 
	          _stepCalcResultTableHaveChange=true;
	          console.log(obj)
	          _changeData.push({
	          	rowIndex:obj.data.LAY_TABLE_INDEX,
	          	field:obj.field
	          });
			});
	    });
}

//测站列表数据表格
	function getwdrsTable(chart, tab, url,data){
	    var height = $(chart).height();
	    var width = $(chart).width();
	    layui.use('table', function() {
	        var table = layui.table;
	        table.render({
	            elem: tab,
	            url: url,
	            where: data,
	            height: height,
	            width: width,
	            id:'cz',
	            request: {pageName: 'pageIndex' //页码的参数名称，默认：page
	                ,limitName: 'pageSize' //每页数据量的参数名，默认：limit
	            },
	            response: {
	                statusName: 'CODE' //数据状态的字段名称，默认：code
	                ,
	                statusCode: 1 //成功的状态码，默认：0
	                ,
	                msgName: 'MESSAGE' //状态信息的字段名称，默认：msg
	                ,
	                countName: 'TOTALAMOUNT' //数据总数的字段名称，默认：count
	                ,
	                dataName: 'DATA'
	            } //数据列表的字段名称，默认：data} //如果无需自定义数据响应名称，可不加该参数
	            ,
	            cols: [
	                [{
	                    field: 'XH',
	                    title: '序号',
	                    templet:function(row){
	                    	return (row.LAY_TABLE_INDEX+1);
	                    }
	                },{
	                    field: 'DATE',
	                    title: '日期',
	                    width:'150'
	                },{
	                    field: 'P',
	                    title: 'P',
	                    width:'60'
	                },{
	                    field: 'E',
	                    title: 'E雨',
	                    edit: 'text',
	                    width:'60'
	                },{
	                    field: 'PE',
	                    title: 'P-E雨',
	                    edit: 'text',
	                    width:'60'
	                },{
	                    field: 'EPE',
	                    title: '∑(P-E雨)',
	                    edit: 'text',
	                    width:'100'
	                },{
	                    field: 'PAPE',
	                    title: 'Pa+∑(P-E雨)',
	                    edit: 'text',
	                    width:'100'
	                },{
	                    field: 'ERC',
	                    title: '∑R查',
	                    edit: 'text',
	                    width:'100'
	                },{
	                    field: 'SDRC',
	                    title: '时段R查',
	                    edit: 'text',
	                    width:'60'
	                },{
	                    field: 'R',
	                    title: 'R改',
	                    edit: 'text',
	                    width:'60'
	                },{
	                    field: 'RGDX',
	                    title: 'R改大→小',
	                    edit: 'text',
	                    width:'60'
	                },{
	                    field: 'ER',
	                    title: '∑R改',
	                    edit: 'text',
	                    width:'60'
	                }]
	            ],
	            page: false,
	            done: function(res, curr, count){
	                //如果是异步请求数据方式，res即为你接口返回的信息。
	                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
	                console.log(res);
	                col=res;
	                //得到当前页码
	                console.log(curr);
	                //得到数据总量
	                console.log(count);
	                }
	        });
	    });
    }
   	//调用接口返回的数据组织echart数据
    function loadCalcServcieEchartsData(lineList){
    			console.log("----lineList----"+JSON.stringify(lineList));
    			var leaged=new Array();
		        var echartSerials=new Array();
		        if(lineList!=null && lineList.length>0){
		        	for(var i=0;i<lineList.length;i++){
		        		var line=lineList[i];
		        		if(line!=null){
		        			leaged.push(line.Pa.toString());
		        			var lineEchart=new Array();
		        			var rdata=line.RData;
		        			if(rdata!=null && rdata.length>0){
		        				for(var j=0;j<rdata.length;j++){
		        					var rd=rdata[j];
		        					lineEchart.push([rd.R,rd["P+Pa"]]);
		        				}
		        			}
		        			if(lineEchart!=null && lineEchart.length>0){
					           echartSerials.push(
					            {
							       name:line.Pa.toString(),
							       data: lineEchart,
							       type: 'line',
							       lineStyle:{
							       	 width:1
							       }
							    });
					        }
		        		}
		        	}
		        }
                option = {
                		title: {
							text: '降雨径流折线图'
					   	},
            		    xAxis: {
            		        name:'R(mm)',
            		        type: 'category',
            		        type: 'value'
            		    },
            		    tooltip: {
            		        trigger: 'axis',
            		        axisPointer: {//xy提示轴线
				                type: 'cross',
				                label: {
				                    backgroundColor: '#505765'
				                }
				            },
				    		formatter:function(params, ticket, callback){
				            	var desc="";
				            	if(params!=null && params.length>0){
				            		for(var i=0;i<params.length;i++){
				            			var label=params[i].seriesName;
				            			var data=params[i].data;
				            			if(i==0){
				            				desc="R:"+data[0]+"<br/>";
				            			}
				            			desc=desc+"Pa:"+label+"<br/>";
				            			desc=desc+"P+Pa:"+data[1]+"<br/>";
				            		}
				            	}
				            	return desc;
				            }
            		    },
            		    grid: {
            		        left: '3%',
            		        right: '50',
            		        bottom: '50',
            		        containLabel: true
            		    },
            		    legend: {
            		    	type:"scroll",
            		    	bottom:0,
            		        data:leaged
            		    },
            		    yAxis: {
            		    	name:'P+Pa(mm)',
            		        type: 'value',
            		        boundaryGap: false
            		    },
            		    series: echartSerials
            	};	
            	var monthLineChart = echarts.getInstanceByDom(document.getElementById('jyjl_chart'));
				if(monthLineChart){
					monthLineChart.dispose();
				}
	            monthLineChart = echarts.init(document.getElementById("jyjl_chart"));
	            //清空画布，防止缓存
	            monthLineChart.clear();
	            monthLineChart.setOption(option); 
    }
    function loadSaveEcharts(echartData){
    			var leaged=new Array();
            	var pa=0;
            	var lineEchart=new Array();
            	var echartSerials=new Array();
            	if(echartData!=null && echartData.length>0){
            		for(var i=0;i<echartData.length;i++){
            			if(i==0){
            				pa=echartData[i].PA;
            				leaged.push(pa.toString());
            				lineEchart.push([echartData[i].R,echartData[i].PPA]);
            			}else{
            				if(echartData[i].PA==pa){
            					lineEchart.push([echartData[i].R,echartData[i].PPA]);
            				}else{
            					echartSerials.push(
            					{
		            		        name:pa.toString(),
		            		        data: lineEchart,
		            		        type: 'line'
		            		    });
            					pa=echartData[i].PA;
            					leaged.push(pa.toString());
            					lineEchart=new Array();
            					lineEchart.push([echartData[i].R,echartData[i].PPA]);
            				}
            			}
            		}
            		if(lineEchart!=null && lineEchart.length>0){
            			echartSerials.push(
            					{
		            		        name:pa.toString(),
		            		        data: lineEchart,
		            		        type: 'line',
							       lineStyle:{
							       	 width:1
							       }
		            		    });
            		}
            	}
                option = {
                		title: {
							text: '降雨径流折线图'
					   	},
            		    xAxis: {
            		        name:'R(mm)',
            		        type: 'value'
            		    },
            		    tooltip: {
            		        trigger: 'axis',
            		        axisPointer: {//xy提示轴线
				                type: 'cross',
				                label: {
				                    backgroundColor: '#505765'
				                }
				            },
				    		formatter:function(params, ticket, callback){
				            	var desc="";
				            	if(params!=null && params.length>0){
				            		for(var i=0;i<params.length;i++){
				            			var label=params[i].seriesName;
				            			var data=params[i].data;
				            			if(i==0){
				            				desc="R:"+data[0]+"<br/>";
				            			}
				            			desc=desc+"Pa:"+label+"<br/>";
				            			desc=desc+"P+Pa:"+data[1]+"<br/>";
				            		}
				            	}
				            	return desc;
				            }
            		    },
            		    grid: {
            		        left: '3%',
            		        right: '50',
            		        bottom: '50',
            		        containLabel: true
            		    },
            		    legend: {
            		    	type:"scroll",
            		        data:leaged,
            		        bottom:0
            		    },
            		    yAxis: {
            		    	name:'P+Pa(mm)',
            		        type: 'value',
            		        boundaryGap: false
            		    },
            		    series: echartSerials
            	};	
            	var monthLineChart = echarts.getInstanceByDom(document.getElementById('jyjl_chart'));
				if(monthLineChart){
					monthLineChart.dispose();
				}
	            monthLineChart = echarts.init(document.getElementById("jyjl_chart"));
	            //清空画布，防止缓存
	            monthLineChart.clear();
	            monthLineChart.setOption(option); 
    }
    function loadRrffEcharts(){
    	var url= basePath + "chanliu/chanliu!queryRsffEchartsData.action";
    	$.ajax({
            url : url,
            type : "post",
            dataType : "JSON",
            data:{stcd:$("#stcd").val()},
            traditional: true,
            success : function(response) {
            	var leaged=new Array();
            	var pa=0;
            	var lineData=new Array();
            	var lineEchart=new Array();
            	var echartSerials=new Array();
            	
            	if(response!=null && response.length>0){
            		for(var i=0;i<response.length;i++){
            			if(i==0){
            				pa=response[i].PA;
            				leaged.push(pa.toString());
            				lineData.push({x:response[i].R,y:response[i].P});
            				lineEchart.push([response[i].R,response[i].P]);
            			}else{
            				if(response[i].PA==pa){
            					lineData.push({x:response[i].R,y:response[i].P});
            					lineEchart.push([response[i].R,response[i].P]);
            				}else{
            					paLineList.push({pa:pa,pointList:lineData});
            					echartSerials.push(
            					{
		            		        name:pa.toString(),
		            		        data: lineEchart,
		            		        type: 'line',
							       lineStyle:{
							       	 width:1
							       }
		            		    });
            					pa=response[i].PA;
            					leaged.push(pa.toString());
            					lineData=new Array();
            					lineEchart=new Array();
            					lineData.push({x:response[i].R,y:response[i].P});
            					lineEchart.push([response[i].R,response[i].P]);
            				}
            			}
            		}
            		if(lineData!=null && lineData.length>0){
            			paLineList.push({pa:pa,pointList:lineData});
            		}
            		if(lineEchart!=null && lineEchart.length>0){
            			echartSerials.push(
            					{
		            		        name:pa.toString(),
		            		        data: lineEchart,
		            		        type: 'line'
		            		    });
            		}
            	}
            	var myChart = echarts.init(document.getElementById('jyjl_chart'));
                option = {
                		title: {
							text: '降雨径流折线图'
					   	},
            		    xAxis: {
            		        name:'R(mm)',
            		        type: 'value'
            		    },
            		    tooltip: {
            		        trigger: 'axis',
            		        axisPointer: {//xy提示轴线
				                type: 'cross',
				                label: {
				                    backgroundColor: '#505765'
				                }
				            },
				    		formatter:function(params, ticket, callback){
				            	var desc="";
				            	if(params!=null && params.length>0){
				            		for(var i=0;i<params.length;i++){
				            			var label=params[i].seriesName;
				            			var data=params[i].data;
				            			if(i==0){
				            				desc="R:"+data[0]+"<br/>";
				            			}
				            			desc=desc+"Pa:"+label+"<br/>";
				            			desc=desc+"P+Pa:"+data[1]+"<br/>";
				            		}
				            	}
				            	return desc;
				            }
            		    },
            		    grid: {
            		        left: '3%',
            		        right: '50',
            		        bottom: '3%',
            		        containLabel: true
            		    },
            		    legend: {
            		    	type:"scroll",
            		    	top:30,
            		        data:leaged
            		    },
            		    yAxis: {
            		    	name:'P(mm)',
            		        type: 'value',
            		        boundaryGap: false
            		    },
            		    series: echartSerials
            		    };	
	            var monthLineChart = echarts.init(document.getElementById("jyjl_chart"));
	            //清空画布，防止缓存
	            monthLineChart.clear();
	            myChart.setOption(option); 
            }
        });
    }
    function getGxt(chart,url,data) {
     	var Pa = [];
    	var P = [];
    	var R = [];
		var index = [];
        $.ajax({
            url : basePath + "rrff/rrff!list.action?mRrffFormBean.mRrffInfoBean.stcd=" + stcd,
            type : "get",
            dataType : "JSON",
            async : false,
            traditional: true,
            success : function(response) {
                for(var i =0;i<response.rows.length;i++){
                	Pa.push(response.rows[i].Pa);
					P.push(response.rows[i].P);
					R.push(response.rows[i].R);
					index.push(i);
				}
                pData=P;
                paData=Pa;
            	var myChart = echarts.init(document.getElementById('jyjl_chart'));

                option = {
            		    xAxis: {
            		        name:'R(mm)',
            		        type: 'category',
            		        type: 'value'
            		    },
            		    tooltip: {
            		        trigger: 'axis',
            		        axisPointer: {//xy提示轴线
				                type: 'cross',
				                label: {
				                    backgroundColor: '#505765'
				                }
				            },
				    		formatter:function(params, ticket, callback){
				            	var desc="";
				            	if(params!=null && params.length>0){
				            		for(var i=0;i<params.length;i++){
				            			var label=params[i].seriesName;
				            			var data=params[i].data;
				            			if(i==0){
				            				desc="R:"+data[0]+"<br/>";
				            			}
				            			desc=desc+"Pa:"+label+"<br/>";
				            			desc=desc+"P+Pa:"+data[1]+"<br/>";
				            		}
				            	}
				            	return desc;
				            }
            		    },
            		    grid: {
            		        left: '3%',
            		        right: '4%',
            		        bottom: '3%',
            		        containLabel: true
            		    },
            		    legend: {
            		        data:['Pa10','Pa20','Pa30']
            		    },
            		    yAxis: {
            		    	name:'P(mm)',
            		        type: 'category',
            		        boundaryGap: false,
            		        data: ['0', '5', '10', '15', '20', '25', '30','35','40','45']
            		    },
            		    series: [{
            		        name:'Pa10',
            		        data: [0, 0.5,1.5 ,3.5 , 6, 8, 10,12,14,16],
            		        type: 'line'
            		    },{
            		        name:'Pa20',
            		        data: [0, 1, 2.5, 5, 7.8, 10, 12,14,16,18],
            		        type: 'line'
            		    },{
            		        name:'Pa30',
            		        data: [0, 2, 4, 7, 10, 12.5, 15,17.5,20,22.5],
            		        type: 'line'
            		    }
            		]};	
	            var monthLineChart = echarts.init(document.getElementById("jyjl_chart"));
	            //清空画布，防止缓存
	            monthLineChart.clear();
	            myChart.setOption(option); 
            }
        });
    }

  //单Y轴
    function getYAxis(){
        var temp = {type: 'value'};
        return temp;
    }
    function getSeries(data){
        var temp = new Array();
        for(var i = 0 ; i<data.length ;i++){
            if(data[i].TYPE == "bar"){
                temp[i] = {
                    "name":data[i].NAME,
                    "type":data[i].TYPE,
                    "yAxisIndex":1,
                    ' barCategoryGap':'10%'  ,
                    "barWidth":10,
                    "data":data[i].DATA
                };
            }else if(data[i].TYPE == "line"){
                temp[i] = {
                    "name":data[i].NAME,
                    "type":data[i].TYPE,
                    "showSymbol": false,
                    "smooth": true,
                    "lineStyle": {
    	                "normal":{
    	                	"width": 1.5
    	                }
                    },
                    "data":data[i].DATA
                };
            }

        }
        return temp;
    }
    function getLegend_data(data) {
        var temp = new Array(data.length);
        for(var i = 0 ; i < data.length ; i < i++){
            temp[i] = data[i].NAME;
        }
        return temp;
    }
//eChart宽高自适应
var resizeWorldMapContainer = function () {
	document.getElementById('jyjl_chart').style.width = document.getElementById('jyjl_div').innerWidth+'px';
	document.getElementById('jyjl_chart').style.height = document.getElementById('jyjl_div').innerHeight+'px';
};
    
resizeWorldMapContainer();
window.onresize = function () {
	$(".layui-border-box").width($("#wdrs_div").width() - 2);
	//重置容器高宽
    resizeWorldMapContainer();
    myChart.resize();
};


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
<!-- 不要改变以下引用顺序 -->