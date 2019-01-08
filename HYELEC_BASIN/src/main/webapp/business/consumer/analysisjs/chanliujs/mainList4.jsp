<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
	<div class="container-fluid">
		<div class="row" style="width:100%;height:500px;">
			<div id="hszlb_step4_div" class="col-md-6" style="height:100%;">
				<div id="hszlb_step4_chart" style="width: 100%;height:100%;"></div>
			</div>
			<div id="dxll_step4_div" class="col-md-6" style="height:100%;">
				<div id="dxll_step4_chart" style="width: 100%;height:100%;"></div>
			</div>
			<div id="jls_step4_div" class="col-md-12" style="height:100%;">
				<div style="width:800px;height:32px;float: right;">
				<input type="button" onclick="cl_excel4()" id="chanliu_daochu4" style="margin-left: 20px;" value="导出到excel"  class="btn btn-primary" >
						<label>站名:</label>
						<input type="text" name=""  class="form-control"  id="stnm"
							readonly style="width: 80px;display:inline;">
						<label>洪号:</label>
						<input type="text" class="form-control"  id="TAB_PCH"
							readonly style="width: 100px;display:inline;">
						<label>流域面积(km²):</label>
						<input type="text" class="form-control"   id="lymj"
							readonly style="width: 100px;display:inline;">
					</div>
					<table class="layui-hide" id="chanliu_step4_table" lay-filter="hszlb_table"></table>
			</div>
		</div>
	</div>
<script>
var symbolSize = 20;
var newData = [];
var oldData = [];
var tempData = [];
var tempData2 = [];
var flowLineData = [];
var excelData=[];
var beginPoint=null;
var beginPosition=0;
//初始化统计图				
var myChart = echarts.init(document.getElementById('hszlb_step4_chart'));
var myChart2 = echarts.init(document.getElementById('dxll_step4_chart'));
$(function(){
	$("#stnm").val(stnm);
	$("#TAB_PCH").val(hh);//洪号
	loadStep4LlEchartData();
	//getHszlbGxt("hszlb_step4_chart",basePath + "chanliu/chanliu!getStep4.action",{"stcd":stcd,"start":beginDate, "end":endDate,"pch":hh} );
	//getHszlbGxt("hszlb_step4_chart","testsyq_1-n2.json","");
	//getHszlbGxt("dxll_step4_chart",basePath + "chanliu/chanliu!getLog.action",{"stcd":stcd,"start":beginDate, "end":endDate,"pch":hh} );
	//getJlsTable("#jls_step4_div","#chanliu_step4_table",basePath + "chanliu/chanliu!getStep4Table.action?STCD="+stcd+"&INTERVAL="+interval+"&pch="+hh+"&LYMJ="+lymj); //查询table
	//	getHszlbGxt("dxll_step4_chart","test_tsd2.json","" );
	//test_tsd2
	// getJlsTable("#jls_step4_div","#chanliu_step4_table","test_jls_tab.json"); 
})
//导出
function cl_excel4(){
		if(excelData==null || excelData.length<1){
			confirm("产流计算", "请选择一条计算结果")
			return false;
		}
		var url= basePath + "chanliu/chanliu!chanliuStep4ExportExcel4.action";    
		confirm("<i class='icon icon-circle-arrow-up'></i>&nbsp;导出到excel","您确定要导出到excel吗？","icon-info", function(result) {
			   if(result){
			   	   $.ajax({
			   	   	  url:url,
			   	   	  type:"post",
			   	   	  data:{pch:hh,DATA:JSON.stringify(excelData)},
			   	   	  dataType:"json",
			   	   	  success:function(response){
			   	   	  	if(response.reflag==1||response.reflag=="1"){
			   	   	  		 window.location.href=basePath + response.fileUrl;
			   	   	  	}else{
			   	   	  		layer.msg("导出excel出错!");
			   	   	  	}
			   	   	  }
			   	   });
			   }
			});
	}
	
var llhjdx;//流量合计
var rs4;//R实	
function loadDsEcharts(lldata){
	var dsdata=new Array();
	var maxY=0;
	var start=null;
	var end=null;
	if(lldata!=null && lldata.length>0){
		for(var i=0;i<lldata.length;i++){
			var lg = Math.log(lldata[i].LL) ;
			lg=Number(lg).toFixed(2);
			dsdata.push([lldata[i].DATE,lg]);
			if(i==0){
				start=beginPoint.DATE;
				maxY=lg;
			}else{
				if(maxY<lg){
					maxY=lg;
				}
			}
			if(i==lldata.length-1){
				end=lldata[i].DATE;
			}
		}
	}
	if(maxY==0){
	   maxY=10;
	}
	var dard=standard(maxY,0,5);
	maxY=dard[0];
	var scale=dard[3];
	var option = {
        title : {
            //text: "洪水过程摘录图",//主标题
            //subtext: '2009-6-12 2:00 - 2009-10-18 8:00',//副标题
            x: 'center',//标题剧中
            padding:0
        },
        color:['#000000','#0000EE','#6699FF','#FF8833','#6666FF','#FF9966','#66CCFF','#FFCC66','#99CCFF','#FFCC99','#CCFFFF'],
        grid: {
            left: '3%',
            right: '4%',
            top:60,
            containLabel: true
        },
        tooltip : {//鼠标悬浮提示信息
            trigger: 'axis',//显示横坐标信息
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
		            				desc=data[0]+"<br/>";
		            			}
		            			desc=desc+params[i].marker+label+":"+data[1]+"<br/>";
		            		}
		            	}
		            	return desc;
		            }
        },
        legend: {
        	top:30,
            data:["对数线"]
        },
        xAxis : [
            {
                type : 'time',
                min:start,
                max:end,
                scale:true
            }
        ],
        yAxis: {
	            	smooth:true,
					smoothMonotone:'none',
	                name: "lgQ",
	                type: "value",
	                max:maxY,
	                min:0,
	            	interval:scale//刻度值
            },
        series:[{
                "name":"对数线",
                "type":"line",
            	smooth:true,
				smoothMonotone:'none',
                "lineStyle": {
	                "normal":{
	                	"width": 1.5
	                }
                },
                data:dsdata
                }]
    };
	myChart2.clear();
    myChart2.setOption(option);
    myChart2.on('click', function (params) {
			newData = new Array(params.dataIndex+1);
			var resultLine=new Array();
			var pos=params.dataIndex;
			var hjll=0;
			var resultTableData=new Array();
			var fm=(params.dataIndex-beginPosition);
			if(fm<=0){
				layer.msg("请选切点后的点！")
				return ;
			}
			var x = (parseFloat(lldata[params.dataIndex].LL) - parseFloat(beginPoint.LL))/fm;
			for(var i = beginPosition;i < lldata.length;i++){
				if(i<=pos){
					var temp = [lldata[i].DATE,((x*(i-beginPosition))+(parseFloat(beginPoint.LL))).toFixed(3)];
					resultLine.push(temp);
				}else{
					var temp = [lldata[i].DATE,Number(lldata[i].LL).toFixed(3)];
					resultLine.push(temp);
				}
				if(i>beginPosition){
					var jg=lldata[i-1].INTERVAL;
					var ll1=resultLine[i-1-beginPosition][1];
					var ll2=resultLine[i-beginPosition][1];
					var qt=(Number(ll1)+Number(ll2))*Number(jg)*60/2;
					if(beginPosition>0){
						var fq=findFQValue(lldata,lldata[i-1].DATE);
						qt=parseFloat(qt)-parseFloat(fq);
					}
					qt=Number(qt).toFixed(3);
					hjll=hjll+qt;
					resultTableData.push({DATE:lldata[i-1].DATE,INTERVAL:jg,LL:ll1,QT:qt});
				}
				if(i==lldata.length-1){
					resultTableData.push({DATE:lldata[i].DATE,INTERVAL:lldata[i].INTERVAL,LL:resultLine[i-beginPosition][1],QT:0});
				}
			}
			myChart.setOption({
				series:[{
	    			id:"result",
	                "name":"地下径流",
	                "type":"line",
	            	smooth:true,
					smoothMonotone:'none',
	                "lineStyle": {
		                "normal":{
		                	"width": 1.5
		                }
	                },
	                "data":resultLine
	            }]
			});
			var obj=sumQtAndRs(resultTableData);
			loadStep4ResultTable("#jls_step4_div","#chanliu_step4_table",obj.dataList);
			
			saveStep4Data({sumQt:obj.sumQt,rs:obj.rs,subdata:obj.tableData});
			// getHszlbGxt("hszlb_step4_chart",basePath + "chanliu/chanliu!getStep4.action",{"stcd":stcd,"start":beginDate, "end":endDate,"pch":hh} );
		    //getHszlbGxt("hszlb_step4_chart","testsyq_1-n2.json","");
		});
}
//查找要减去的QT
function findFQValue(lldata,date){
	for(var i=0;i<beginPosition;i++){
		if(lldata[i].DATE==date){
			return lldata[i].QT;
		}
	}
	return 0;
}
function sumQtAndRs(tableData){
		var dataList=new Array();
		var rs=0,llhj=0;
		if(tableData!=null && tableData.length>0){
			tableData[tableData.length-1].QT="0";
			for(var i=0;i<tableData.length;i++){
				if(i<tableData.length-1){
					tableData[i].QT=Number(tableData[i].QT).toFixed(3);
					llhj=parseFloat(llhj)+parseFloat(tableData[i].QT);
				}
				tableData[i].LL=Number(tableData[i].LL).toFixed(3);
				dataList.push(tableData[i]);
			}
			llhj=Number(llhj).toFixed(3);
			// dataList.push({DATE:"流量(地下)合计",INTERVAL:"",LL:"",QT:llhj})
			lymj=$("#lymj").val();
			lymj=(lymj!=null && $.trim(lymj).length>0)?lymj:0;
			if(lymj!=0){
				rs=parseFloat(llhj)/parseFloat(lymj)/1000;
				rs=Number(rs).toFixed(2);
			}
			dataList.push({DATE:"R下(mm)",INTERVAL:"",LL:"",QT:rs})
		}else{
			dataList=[{}];
		}
	
		return {dataList:dataList,sumQt:llhj,rs:rs,tableData:tableData};
}
function loadStep4LlEchartData(){
	$.ajax({
        url : basePath + "chanliu/chanliu!queryStepLlechartData.action",
        type : "post",
        dataType : "JSON",
        traditional: true,
        data : {"stcd":stcd,"start":secondBeginDate, "end":secondEndDate,"pch":hh},
        success : function(response) {
        	if(response.reflag==1||response.reflag=="1"){
	        	var lldata=response.llData;
	        	var result=response.result;
	        	var thirdLess=response.thirdLess;
	        	beginPoint=lldata[0];
	        	beginPosition=thirdLess!=null?thirdLess.length:0;
	        	if(thirdLess!=null && thirdLess.length>0){
	        		for(var i=0;i<thirdLess.length;i++){
	        			lldata.unshift(thirdLess[i]);
	        		}
	        	}
	        	loadStep4LlEchart(lldata,result);
	        	loadDsEcharts(lldata);
	        	var secondList = response.secondList;
	        	var dataList=new Array();
	        	if(result!=null && result.length>0){
	        		dataList = dataList.concat(result);
	        		if(secondList!=null && secondList.length>0){
	        			var second=secondList[0];
						$("#lymj").val(second.LYMJ);//流域面积
	        		}
	        	}
	        	var obj=sumQtAndRs(dataList);
				loadStep4ResultTable("#jls_step4_div","#chanliu_step4_table",obj.dataList);
        	}else{
        		layer.msg(response.message);
        	}
        }
    });
}
function loadStep4ResultTable(chart, tab, tableData){
	    var height = $(chart).height();
	    var width = $(chart).width();
	    layui.use('table', function() {
	        var table = layui.table;
	        table.render({
	            elem: tab,
	            data: tableData,
	            height: height,
	            width: width,
	            id:'cz',
	            limit:tableData.length,
	            cols: [
	                [{
	                    field: 'DATE',
	                    title: '日期',
	                },{
	                    field: 'INTERVAL',
	                    title: '时差',
	                },{
	                    field: 'LL',
	                    title: '流量(地下)',
	                    templet:function(row){
	                    	if(row.LL!=null && row.LL!=""){
	                    		return Number(row.LL).toFixed(3);
	                    	}else{
	                    		return "";
	                    	}
	                    }
	                },{
	                    field: 'QT',
	                    title: 'Q*T',
	                    templet:function(row){
	                    	if(row.QT!=null && row.QT!="" && row.QT>0){
	                    		return Number(row.QT).toFixed(3);
	                    	}else{
	                    		return "0";
	                    	}
	                    }
	                }]
	            ],
	            page: false,
	            done: function(res, curr, count){
			        excelData=res.data;
			    }
	        });
	    });
    }
function loadStep4LlEchart(lldata,result){
	var llechar=new Array();
	var resultchar=new Array();
	var maxY=0;
	if(lldata!=null && lldata.length>0){
		for(var i=0;i<lldata.length;i++){
			llechar.push([lldata[i].DATE,lldata[i].LL]);
			if(i==0){
				maxY=lldata[i].LL;
			}else{
				if(maxY<lldata[i].LL){
					maxY=lldata[i].LL;
				}
			}
		}
	}
	if(result!=null && result.length>0){
		for(var i=0;i<result.length;i++){
			resultchar.push([result[i].DATE,result[i].LL]);
		}
	}
	if(maxY==0){
	   maxY=10;
	}
	var dard=standard(maxY,0,5);
	maxY=dard[0];
	var scale=dard[3];
	var start=beginPoint!=null?beginPoint.DATE:null;
	var end=lldata!=null && lldata.length>0?lldata[lldata.length-1].DATE:null;
	var series=new Array();
	series.push({
                "name":"流量",
                "type":"line",
            	smooth:true,
				smoothMonotone:'none',
                "lineStyle": {
	                "normal":{
	                	"width": 1.5
	                }
                },
                "data":llechar
            });
    if(resultchar!=null && resultchar.length>0){
    	series.push({
    			id:"result",
                "name":"地下径流",
                "type":"line",
            	smooth:true,
				smoothMonotone:'none',
                "lineStyle": {
	                "normal":{
	                	"width": 1.5
	                }
                },
                "data":resultchar
            });
    }
	var option = {
        title : {
            text: "洪水过程摘录图",//主标题
            //subtext: '2009-6-12 2:00 - 2009-10-18 8:00',//副标题
            x: 'center',//标题剧中
            padding:0
        },
        color:['#000000','#0000EE','#6699FF','#FF8833','#6666FF','#FF9966','#66CCFF','#FFCC66','#99CCFF','#FFCC99','#CCFFFF'],
        grid: {
            left: '3%',
            right: '4%',
            top:60,
            containLabel: true
        },
        tooltip : {//鼠标悬浮提示信息
            trigger: 'axis',//显示横坐标信息
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
		            				desc=data[0]+"<br/>";
		            			}
		            			desc=desc+params[i].marker+label+":"+data[1]+"<br/>";
		            		}
		            	}
		            	return desc;
		            }      
        },
        legend: {
        	top:30,
            data:["流量","地下径流"]
        },
        xAxis : [
            {
                type : 'time',
                min:start,
                max:end,
                scale:true
            }
        ],
        yAxis: {
	            	smooth:true,
					smoothMonotone:'none',
	                name: "流量(m³/s)",
	                type: "value",
	                max:maxY,
	                min:0,
	            	interval:scale//刻度值
            },
        series:  series
    };
	myChart.clear();
    myChart.setOption(option);
}

function saveStep4Data(resultLine){
	var json = {
    	"pch":hh,
   		"stcd" : stcd,
   		"BEGINDATE" : secondBeginDate,
   		"ENDDATE" : secondEndDate,
   		"INTERVAL" : interval,
   		"DATA" : JSON.stringify(resultLine)
    }
    $.ajax({
        url : basePath + "chanliu/chanliu!saveStep4Data.action",
        type : "post",
        dataType : "json",
        traditional: true,
        data : json,
        success : function(response) {
        	if(response.reflag==1||response.reflag=="1"){
        		layer.msg("保存成功!");
        		loadStep4LlEchartData();
        	}else{
        		layer.msg(response.message);
        	}
        }
    });
}
//提交单场洪水数据
function saveLine2(resultLine){
    var json = {
    		"pch":hh,
   		"STCD" : stcd,
   		"BEGINDATE" : secondBeginDate,
   		"ENDDATE" : secondEndDate,
   		"INTERVAL" : interval,
   		"DATA" : [resultLine]
    }
	console.log(json);
    
    $.ajax({
        url : basePath + "chanliu/chanliu!saveLineFor4.action",
        type : "post",
        dataType : "json",
        traditional: true,
        data : json,
        success : function(response) {
        	if(response.reflag==1||response.reflag=="1"){
        		layer.msg("保存成功!");
        	}else{
        		layer.msg(response.message);
        	}
        	//getJlsTable("#jls_step4_div","#chanliu_step4_table",basePath + "chanliu/chanliu!getStep4Table.action?STCD="+stcd+"&INTERVAL="+interval+"&pch="+hh+"&LYMJ="+lymj); //查询table
        }
    });
}
//测站列表数据表格
	function getJlsTable(chart, tab, url){
	    var height = $(chart).height();
	    var width = $(chart).width();
	    layui.use('table', function() {
	        var table = layui.table;
	        table.render({
	            elem: tab,
	            url: url,
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
	                    field: 'DATE',
	                    title: '日期',
	                },{
	                    field: 'SC',
	                    title: '时差',
	                },{
	                    field: 'LL',
	                    title: '流量(地下)',
	                },{
	                    field: 'QT',
	                    title: 'Q*T',
	                }]
	            ],
	            page: false
	        });
	    });
    }


function getHszlbGxt(chart,url,data) {
    $.ajax({
        url : url,
        type : "post",
        dataType : "JSON",
        async : false,
        traditional: true,
        data : data,
        success : function(response) {
        	//初始化点击退水段数据添加到降雨流量统计图的坐标
        	if(chart == "hszlb_step4_chart"){
        		//debugger;
        		tempData[0] = response.DATA[0].DATA[0];
       			tempData2 = new Array(response.DATA[0].DATA.length);
       			console.log(tempData2);
        		for(var i = 0 ;i < response.DATA[0].DATA.length ;i++){
        			var tempLog = Math.log(parseInt(response.DATA[0].DATA[i][1]));
        			var reTempData = [response.DATA[0].DATA[i][0],tempLog];
        			tempData2[i] = reTempData;
        		}
        			console.log(tempData2);
        	}
            getChart(chart,response,data);
        }
    });
}

//生成统计图
function  getChart(chart,response,data) {
	var series = {};
	var yAxis = {};
	yAxis = getYAxis(response.DATA);
	if(chart == "hszlb_step4_chart"){
		var myChart = echarts.init(document.getElementById('hszlb_step4_chart'));
		series = getSeriesOfdxll(response.DATA);
		yAxis = getYAxis(response.DATA);
	}else if(chart == "dxll_step4_chart"){
		var myChart2 = echarts.init(document.getElementById('dxll_step4_chart'));
		series = getSeries(response.DATA);
		yAxis = getYAxisOf1Y(response.DATA);
	}
    var legend_data= getLegend_data(response.DATA);
    var title = response.EXTRADATA.CHARTTITLE;
    var start = response.EXTRADATA.START;
    var end = response.EXTRADATA.END;
// 指定图表的配置项和数据
    var option = {
        title : {
            text: title,//主标题
            //subtext: '2009-6-12 2:00 - 2009-10-18 8:00',//副标题
            x: 'center',//标题剧中
            padding:0
        },
        color:['#000000','#0000EE','#6699FF','#FF8833','#6666FF','#FF9966','#66CCFF','#FFCC66','#99CCFF','#FFCC99','#CCFFFF'],
        grid: {
            left: '3%',
            right: '4%',
            top:20,
            bottom: 30,
            containLabel: true
        },
        tooltip : {//鼠标悬浮提示信息
            trigger: 'axis',//显示横坐标信息
            axisPointer: {//xy提示轴线
                type: 'cross',
                label: {
                    backgroundColor: '#505765'
                }
            }
        },
        legend: {
            data:legend_data, //最上边的切换按钮
            bottom:0
        },
        xAxis : [
            {
                type : 'time',
                min:start,
                max:end,
                scale:true
            },
            {
                type : 'time',
                axisLine: {
                    onZero: false,
                }
            }
        ],
        yAxis: yAxis,
        series:  series
    };

// 使用刚指定的配置项和数据显示图表。
    if(chart == "hszlb_step4_chart"){
    	myChart.clear();
    	myChart.setOption(option);
    	oldData = myChart.getOption().series[0].data;
	}else if(chart == "dxll_step4_chart"){
		myChart2.clear();
		myChart2.setOption(option);
		myChart2.on('click', function (params) {

			newData = new Array(params.dataIndex+1);
			var x = (parseInt(oldData[params.dataIndex][1]) - parseInt(tempData[0][1]))/params.dataIndex;
			for(var i = 0;i <= params.dataIndex ;i++){
				var temp = [oldData[i][0],((x*i)+(parseInt(tempData[0][1]))).toFixed(3)];
				newData[i] = temp;
			}
			flowLineData = new Array(oldData.length);
			for(var y = 0;y < oldData.length ;y++){
				if(y < newData.length){
					flowLineData[y] = newData[y];
				}else{
					flowLineData[y] = oldData[y];
				}
			}
			saveLine2();
			getHszlbGxt("hszlb_step4_chart",basePath + "chanliu/chanliu!getStep4.action",{"stcd":stcd,"start":secondBeginDate, "end":secondEndDate,"pch":hh} );
		    //getHszlbGxt("hszlb_step4_chart","testsyq_1-n2.json","");
		});
	}

}
//双Y轴
function getYAxis(data){
	if(data[0].MAX==0){
	   data[0].MAX=10;
	}
	var mjarr = standard(data[0].MAX,data[0].MIN,5);
	var maxMj=mjarr[0];
	var minMj=mjarr[1];
	var mjscale=mjarr[3];
	var scaleValue=mjarr[2];
	if(minMj==data[0].MIN && minMj>scaleValue){
		minMj=minMj-scaleValue;
		mjscale=mjscale+1;
	}
	if(maxMj==data[0].MAX ){
		maxMj=maxMj+scaleValue;
		mjscale=mjscale+1;
	}
    var temp =[{
	            	smooth:true,
					smoothMonotone:'none',
	                name: data.NAME,
	                type: "value",
	                max:maxMj,
	                min:minMj,
	            	interval:mjscale//刻度值
            }] 
    return temp;
}
//单Y轴
function getYAxisOf1Y(data){
	if(data[0].MAX==0){
	   data[0].MAX=10;
	}
	var mjarr = standard(data[0].MAX,data[0].MIN,5);
	var maxMj=mjarr[0];
	var minMj=mjarr[1];
	var mjscale=mjarr[3];
	var scaleValue=mjarr[2];
	if(minMj==data[0].MIN && minMj>scaleValue){
		minMj=minMj-scaleValue;
		mjscale=mjscale+1;
	}
	if(maxMj==data[0].MAX ){
		maxMj=maxMj+scaleValue;
		mjscale=mjscale+1;
	}
    var temp =[{
	            	smooth:true,
					smoothMonotone:'none',
	                name:'lgQ',
	                type: "value",
	                max:maxMj,
	                min:minMj,
	            	interval:mjscale//刻度值
            }] 
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
            	smooth:true,
				smoothMonotone:'none',
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
function getSeriesOfdxll(data){
    var temp = new Array();
    for(var i = 0 ; i<data.length ;i++){
        if(data[i].TYPE == "bar"){
            temp[i] = {
                "name":data[i].NAME,
                "type":data[i].TYPE,
                "yAxisIndex":1,
            	smooth:true,
				smoothMonotone:'none',
                "barWidth":10,
                "data":data[i].DATA
            };
        }else if(data[i].TYPE == "line"){
            temp[i] = {
                "name":data[i].NAME,
                "type":data[i].TYPE,
            	smooth:true,
				smoothMonotone:'none',
                "lineStyle": {
	                "normal":{
	                	"width": 1.5
	                }
                },
                "data":data[i].DATA
            };
        }

    }
    temp[data.length] = {
        	"id":"dxll",
            "type": "line",
            "data": newData,
            
            "name":"对数线",
            "showSymbol": true,
            "lineStyle": {
    	        "normal":{
    	        	"width": 1.5
    	        }
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
	document.getElementById('hszlb_step4_chart').style.width = document.getElementById('hszlb_step4_div').innerWidth+'px';
	document.getElementById('hszlb_step4_chart').style.height = document.getElementById('hszlb_step4_div').innerHeight+'px';
	document.getElementById('dxll_step4_chart').style.width = document.getElementById('dxll_step4_div').innerWidth+'px';
	document.getElementById('dxll_step4_chart').style.height = document.getElementById('dxll_step4_div').innerHeight+'px';
};

resizeWorldMapContainer();
window.onresize = function () {
	$(".layui-border-box").width($("#jls_step4_div").width() - 2);
    //重置容器高宽
    resizeWorldMapContainer();
    myChart.resize();
    myChart2.resize();
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
        + " " + hours + seperator2 + minut;
        /* + seperator2 + seconds; */
    return currentdate;
}
</script>
<!-- 不要改变以下引用顺序 -->