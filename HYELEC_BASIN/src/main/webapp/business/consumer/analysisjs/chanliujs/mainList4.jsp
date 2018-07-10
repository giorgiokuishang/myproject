<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<title>资料管理</title>
</head>
<body style="background-color: #FCFCFC;">
	<div class="container-fluid">
		<div class="row" style="width:100%;height:500px;">
			<div id="hszlb_div" class="col-md-6" style="height:100%;">
				<div id="hszlb_chart" style="width: 100%;height:100%;"></div>
			</div>
			<div id="dxll_div" class="col-md-6" style="height:100%;">
				<div id="dxll_chart" style="width: 100%;height:100%;"></div>
			</div>
			<div id="jls_div" class="col-md-12" style="height:100%;">
				<div style="width:460px;height:32px;float: right;">
						<label>站名:</label>
						<input type="text" name="" value="江永站" class="form-control" 
							readonly style="width: 80;display:inline;">
						<label>洪号:</label>
						<input type="text" class="form-control" value="20180501 "
							readonly style="width: 100;display:inline;">
						<label>流域面积(km²):</label>
						<input type="text" class="form-control" value="200"
							readonly style="width: 100;display:inline;">
					</div>
				<table class="layui-hide" id="jls_tab" lay-filter="hszlb_table"></table>
			</div>
		</div>
	</div>
</body>
<script>
var symbolSize = 20;
var newData = [];
var oldData = [];
var tempData = [];
var tempData2 = [];
var flowLineData = [];

$(function(){
	getHszlbGxt("hszlb_chart",basePath + "chanliu/chanliu!getStep4.action",{"stcd":stcd,"start":beginDate, "end":endDate} );
	//getHszlbGxt("hszlb_chart","testsyq_1-n2.json","");
//	getHszlbGxt("dxll_chart",basePath + "chanliu/chanliu!getLog.action",{"stcd":stcd,"start":beginDate, "end":endDate} );
		getHszlbGxt("dxll_chart","test_tsd2.json","" );
	//test_tsd2
	 getJlsTable("#jls_div","#jls_tab","test_jls_tab.json"); 
})
//提交单场洪水数据
function saveLine2(){

    var json = {
   		"STCD" : stcd,
   		"BEGINDATE" : beginDate,
   		"ENDDATE" : endDate,
   		"INTERVAL" : interval,
   		"DATA" : [flowLineData]
    }
	console.log(json);
    
    $.ajax({
        url : basePath + "chanliu/chanliu!saveLineFor4.action",
        type : "post",
        dataType : "json",
        async : false,
        traditional: true,
        data : json,
        success : function(response) {
        	getJlsTable("#jls_div","#jls_tab","test_jls_tab.json");
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

//初始化统计图				
var myChart = echarts.init(document.getElementById('hszlb_chart'));
var myChart2 = echarts.init(document.getElementById('dxll_chart'));

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
        	if(chart == "hszlb_chart"){
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
	if(chart == "hszlb_chart"){
		var myChart = echarts.init(document.getElementById('hszlb_chart'));
		series = getSeriesOfdxll(response.DATA);
		yAxis = getYAxis(response.DATA);
	}else if(chart == "dxll_chart"){
		var myChart2 = echarts.init(document.getElementById('dxll_chart'));
		series = getSeries(response.DATA);
		yAxis = getYAxisOf1Y();
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
            //x: 'left'//按钮位置
            bottom:0
        },
        xAxis : [
            {
                splitLine:{show: false},//去除网格线
                type : 'time',
                min:start,
                max:end,
            },
            {
                splitLine:{show: false},//去除网格线
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
    if(chart == "hszlb_chart"){
    	myChart.clear();
    	myChart.setOption(option);
    	oldData = myChart.getOption().series[0].data;
	}else if(chart == "dxll_chart"){
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
			getHszlbGxt("hszlb_chart",basePath + "chanliu/chanliu!step2chart.action",{"stcd":stcd,"start":beginDate, "end":endDate} );
		    //getHszlbGxt("hszlb_chart","testsyq_1-n2.json","");
		});
	}

}
//双Y轴
function getYAxis(data){
    var temp = new Array(data.length);
    for(var i = 0 ; i < data.length ; i < i++){
        if(data[i].TYPE == "line"){
            temp[i] = {
                "splitLine":{"show": false},//去除网格线
                "name": data[i].NAME,
                "type": "value",
                "max":data[i].MAX,
                "min":data[i].MIN
            }
        }else if(data[i].TYPE == "bar"){
            temp[i] = {
                "splitLine":{show: false},//去除网格线
                "name": data[i].NAME,
                "nameLocation": 'start',
                "type": 'value',
                "inverse": true,//反向坐标
                "max":data[i].MAX,
                "min":data[i].MIN
            }
        }

    }
    return temp;
}
//单Y轴
function getYAxisOf1Y(){
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
                "barWidth":10,
                "data":data[i].DATA
            };
        }else if(data[i].TYPE == "line"){
            temp[i] = {
                "name":data[i].NAME,
                "type":data[i].TYPE,
                "showSymbol": false,
                //"smooth": true,
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
	document.getElementById('hszlb_chart').style.width = document.getElementById('hszlb_div').innerWidth+'px';
	document.getElementById('hszlb_chart').style.height = document.getElementById('hszlb_div').innerHeight+'px';
	document.getElementById('dxll_chart').style.width = document.getElementById('dxll_div').innerWidth+'px';
	document.getElementById('dxll_chart').style.height = document.getElementById('dxll_div').innerHeight+'px';
};

resizeWorldMapContainer();
window.onresize = function () {
	$(".layui-border-box").width($("#jls_div").width() - 2);
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
</html>
<!-- 不要改变以下引用顺序 -->