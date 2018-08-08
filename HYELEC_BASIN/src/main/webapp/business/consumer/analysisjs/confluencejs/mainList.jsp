<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<title>资料管理</title>
<style type="text/css">
        .div_items {  
            position: relative;  
            width: 100%;  
            height: 200px;  
            border: 1px solid #66afe9;  
            border-top: 0px;  
            overflow: auto;  
            display: none;  
        }  
        .div_item {  
            width: 100%;  
            height: 20px;  
            margin-top: 1px;  
            font-size: 13px;  
            line-height: 20px;  
        }  
#stnm{
 width:200px;
}
#base{
 margin-bottom:10px;
}
.date{
 margin-top:0px;
}
/* #table1{
 width:1124px;
} */
.addTd2{
  width:172px;
}
.datetimepicker{
 margin-top:30px;margin-left:-7px;
}
</style>
</head>
<body style="background-color: #FCFCFC;">
	<div class="container-fluid" id="cljs_mainList">
		<div class="row-fluid">
			<h3 class="text-primary">
				<ol class="breadcrumb">
					<li>分析计算</li>
					<li style="color: black;">汇流计算</li>
				</ol>
			</h3>
		</div>
		<hr style="margin-top:-5px;">
			1、测站地表径流过程计算数据统计表
			<div class="row" style="width:100%;height:500px;">
			<div id="wdrs_div" class="col-md-8" style="height:100%;">
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
				<table class="layui-hide" id="wdrs_tab" lay-filter="hszlb_table"></table>
			</div>
		</div><br><br>
		<div class="row" style="width:100%;height:200px;">
			<div id="tlgs_div" class="col-md-12" style="height:100%;">
			2.推理公式计算,汇流参数m计算表
				<table class="layui-hide" id="tlgs_tab" lay-filter="hszlb_table"></table>
					<button class="layui-btn" value="单站综合及误差统计">单站综合及误差统计</button><br>
				<table class="layui-hide" id="wctj_tab" lay-filter="hszlb_table"></table>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
$(function(){
	getwdrsTable("#wdrs_div","#wdrs_tab",basePath + "huiliu/huiliu!huiliuStep1.action?STCD="+stcd);
	getTlgsTable("#tlgs_div","#tlgs_tab","test_jls_tab.json");
	getwctj_tab("#tlgs_div","#wctj_tab","test_jls_tab.json");
})
//测站列表数据表格
	function getwdrsTable(chart, tab, url){
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
	                    title: '时间'
	                  //  edit:'text'
	                },{
	                    field: 'LL',
	                    title: '流量',
	                    edit:'text'
	                },{
	                    field: 'LLDX',
	                    title: '流量(地下)',
	                    edit:'text'
	                }]
	            ],
	            page: false
	        });
	    });
    }

	//2、推理公式计算
	function getTlgsTable(chart, tab, url){
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
	                    field: 'HH',
	                    title: '洪号'
	                    //width:'180'
	                },{
	                    field: 'QJ',
	                    title: 'Q净(m³/s)',
	                },{
	                    field: 'T',
	                    title: '汇流时间t(消失)',
	                },{
	                    field: 'R',
	                    title: '地表净雨深(mm)',
	                    edit: 'text'
	                },{
	                    field: 'F',
	                    title: '流域面积F(km²)',
	                },{
	                    field: 'L',
	                    title: 'L',
	                },{
	                    field: 'J',
	                    title: 'J',
	                },{
	                    field: 'J13',
	                    title: 'J 1/3',
	                },{
	                    field: 'L3F',
	                    title: 'L/ (J1/3*F)',
	                },{
	                    field: 'QM',
	                    title: 'Qm3/4',
	                },{
	                    field: 'QMR',
	                    title: 'Qm3/4/ R',
	                },{
	                    field: 'M',
	                    title: '汇流参数m',
	                }]
	            ],
	            page: false
	        });
	    });
    }
    //单站综合及误差统计table
	function getwctj_tab(chart, tab, url){
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
	            	[ //标题栏
	            	    {field: 'PCH', title: '洪号', rowspan: 2,align: 'center',edit:"text"} //rowspan即纵向跨越的单元格数
	            	    ,{field: 'RTC', title: 'R上/tc(毫米/小时) ',  rowspan: 2,align: 'center',edit:"text"}
	            	    ,{field: 'SUMM', title: 'm计 ',  rowspan: 2,align: 'center',edit:"text"}
	            	    ,{align: 'center', title: 'R上/tc ~m', colspan: 3} //colspan即横跨的单元格数，这种情况下不用设置field和width
	            	  ], [
	            	    {field: 'MC', title: 'm查', align: 'center'}
	            	    ,{field: 'MJJMC', title: '(m计-m查)/ m计',align: 'center' }
	            	    ,{field: 'SFHG', title: '是否合格', align: 'center'}
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

</html>
