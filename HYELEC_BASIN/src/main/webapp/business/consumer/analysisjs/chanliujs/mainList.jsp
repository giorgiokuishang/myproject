<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<style type="text/css">

#stnm1{
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
	<div class="container-fluid" id="cljs_mainList">
		<hr style="margin-top:-5px;">
		<form id="info_form_cjfa" name="" method="post">
			<table>
				<tr>
					<td>请输入站名 : </td>
					<td>
						<input	type="hidden" id="stcd" name="stcd">
						<input type="text" id="stnm1" class="form-control" style="display:inline;width:150px;"
						name="mStbprpFormBean.mStbprpInfoBean.stnm" onchange="clearStep1Data()">
						<input type="button" name="" style="width:50px;"  value="查询" onclick="getStp()" class="btn btn-primary">
					</td>
					<td>
						<div  style="display:none;width:250px;" id="show_result"> 
						结果查询：<select id="selectResult" style="height:32px;width:180px;" onchange="pchChange()"></select></div>
					</td>
					<td>
						<div style="width:250px;">
							洪&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：<input type="text" style="width:150px;height:32px;" name="hh" id="honghao">
						</div>
					</td>
					<td>
						<div style="float:left;">
								站&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码：<font color="green " style="display: none;" id="zhanma"></font>
							<font color="red " style="display: none;" id="m">提示 : 测站名称不存在</font>
						</div>
						<div style="float:right;">
								类&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;型：<font color="green" id="zhansttp">&nbsp;&nbsp;</font>
						</div>
					</td>
					<td>
						<input type="button" onclick="cl_excel()" id="chanliu_daochu" style="margin-left: 20px;" value="导出到excel"  class="btn btn-primary" >
					</td>
				</tr>
				<tr style="height:40px;">
					<td>
						雨量站个数 :
					</td>
					<td>
						<input type="text" class="form-control" style="width:180px" name="" value="" id="cols">
					</td>
					<td>
						<div class="input-append date form_datetime" style="width:250px;">
							开始时间：<input size="16" id="start" style="width:150px;height:32px;" onchange="startTimeChange(this)" type="text" readonly> <span
							 class="add-on"><i class="icon-th"></i></span>
						</div>
					</td>
					<td>
						<div class="input-append date form_datetime" style="width:250px;">
							截止时间：<input size="16" id="end" style="width:150px;height:32px;" type="text" readonly> <span
								class="add-on"><i class="icon-th"></i></span>
						</div>
					</td>
					<td>
						<div style="width:250px;float:left;">
							时间间隔：
							<input type="text" id="jiange" style="width:120px;height:32px;" name="" value="">
							(分钟)
						</div>
						<input type="button" style="margin-left:3px;height:30px;" value="确定" onclick="queding()"  >
					</td>
					<td>
						<input type="button" value="计算" onclick="jisuan()" class="btn btn-primary" style="margin-left:5px;">
						<input type="button" name="" value="保存计算结果" onclick="save()" class="btn btn-save">
					</td>
				</tr>
			</table>
			<div style="width:100%;overflow-x: auto;">
				<table border="1" id="table1"  style="margin-left: 10px;">
				<thead id="chanliu_table1_thead">
					<tr id="tr1">
						<td rowspan="2" align="center">序号</td>
						<td rowspan="2" align="center" style="width:190px" id="riqi">日期</td>
						<td colspan="2" align="center" id="td1" class="addTd">
							<input	type="text" name="czmc"   id='getStnm_combox0'class="getStnm" placeholder="请输入测站名"  style="width:100%">
						</td>
						<td rowspan="2" align="center">雨面量</td>
					</tr>
					<tr align="center" id="tr2">
						<td style="width:100px" id='qz'>权重</td>
						<td style="width:100px" id="td2" class="addTd2" align="center"><input style="width:100px" type="text"  name="quanzhong" id="quanzhong"></td>
					</tr>
					</thead>
					<tbody id="table1_tbdody">
					</tbody>
				</table>
			 </div>
			</form>
	</div>
	
<script type="text/javascript">
$(function(){
	stnmMohu();
	if(stcd!=null && $.trim(stcd).length>0){
		$("#stcd").val($.trim(stcd));
		$("#stnm1").val(stnm);
	}
	if(hh!=null && $.trim(hh).length>0){
		$("#honghao").val($.trim(hh));
	}
	if(stcd!=null && $.trim(stcd).length>0 && hh!=null && $.trim(hh).length>0){
		$("#cols").val(ylz);
		$("#start").val(beginDate);
		$("#end").val(endDate);
		$("#jiange").val(interval);
		getStp();
		initStep1Data(stcd,hh);
	}
})
function startTimeChange(obj){
	var pch=$("#honghao").val();
	var t1=$(obj).val();
	var date1=new Date(t1);
	if(pch==null||$.trim(pch).length<1){
		pch=formatDateTime2(date1);
		$("#honghao").val(pch);
	}
}
//定位
function dingwei(x,y,name){
	dingweiPoint(x,y);
}
function stnmMohu(){
	$("#stnm1").combox({
		url:basePath + "chanliu/chanliu!getStbprpMoHu.action",
		label:"STNM",
		value:"STCD",
		dataKey:"dataList",
		height:'250px',
		width:'150px',
		isPager:false,
		select:function(item){
			dingwei(item.LGTD1,item.LTTD1,item.STNM);//定位
		}
	});
	
}
function base64 (content) {
    return window.btoa(unescape(encodeURIComponent(content)));         
}
 /*
 *@tableId: table的Id
 *@fileName: 要生成excel文件的名字（不包括后缀，可随意填写）
 */
 var tableToExcel = (function () {
     var uri = 'data:application/vnd.ms-excel;base64,',
         template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>',
         base64 = function (s) { return window.btoa(unescape(encodeURIComponent(s))) },
         format = function (s, c) { return s.replace(/{(\w+)}/g, function (m, p) { return c[p]; }) };
     return function (table, name, filename) {
         if (!table.nodeType) table = document.getElementById(table)
         var ctx = { worksheet: name || 'Worksheet', table: table.innerHTML }

         document.getElementById("dlink").href = uri + base64(format(template, ctx));
         document.getElementById("dlink").download = filename;
         document.getElementById("dlink").click();
     }
 })()
 
 
	window.onresize = "";
	$(".form_datetime").datetimepicker({
		format : " yyyy-mm-dd hh:ii",
		autoclose : true,// 选择好自动关闭
		language : 'zh-CN', // 汉化
	});
	


//项点击  回显
function on(i){
	var par=$(i).parent().parent().attr("id");
	var str=par.replace("div_","");
   $("#"+str+"").val($(i).text());  //选择的测站回显到输入框
    var stcd=$(i).attr("id");//获得测站编码
    var json = "mStbprpFormBean.mStbprpInfoBean.stcd="+stcd; //查询
	var url = basePath + "chanliu/chanliu!list.action" //路径
	common_ajax(json, url, function(response) {//ajax提交。回调函数
    	var shijian = document.getElementsByName("time");
		cle(shijian)//清空input
		for (var i = 0; i < response.rows.length; i++) {
			var date1=response.rows[i].TM.time;
			var d1=date1.toString();
			var newstr=d1.substring(0,d1.length-1);
			$("#"+date1+"").val(response.rows[i].DRP);//数据回显
		}
		rem();//隐藏下拉DIV选项
	});
}   

//移入移出效果  
$(".div_item").hover(function () {  
    $(this).css('background-color', '#1C86EE').css('color', 'white');  
}, function () {  
    $(this).css('background-color', 'white').css('color', 'black');  
});  

//清空input
function cle(shijian){
 	var date1 = new Date($("#start").val()); 
	var date2 = date1.getTime();
	var shu = Number($("#jiange").val()) * 60;// 分钟*秒
	var haomiao = shu * 1000;
	var num = $("#cols").val();
	var addTime = 0;
	for (var i = 0; i < shijian.length; i++) {// 下面的switch是生成时间都
		switch (i) {
		case 0:
			for(var j=0;j<num;j++){
				var x=2+j;
				var d1=date1.getTime();
				$("#"+d1+"").val("");
				//$("#removeTr"+i+"").find("td:eq("+x+")").find("input").attr("id",d1+j);
			}
			break;
		case 1:
			for(var j=0;j<num;j++){
				var x=2+j;
				var d2=date2 + haomiao;
				$("#"+d2+"").val("");
			//	$("#removeTr"+i+"").find("td:eq("+x+")").find("input").attr("id",d2+j);
			}
			var t1 = formatDateTime(new Date(date2 + haomiao));
			shijian[i].value = t1;
			break;
		default:
			for(var j=0;j<num;j++){
				var x=2+j;
				var d3=date2 + addTime;
				$("#"+d3+"").val("");
				$("#removeTr"+i+"").find("td:eq("+x+")").find("input").attr("id",d3+j);
			}
			var t2 = formatDateTime(new Date(date2 + addTime));
			shijian[i].value = t2;
			;
		}
		addTime += haomiao;// 每次叠加1个时间段
	}
}

    
    
</script>


