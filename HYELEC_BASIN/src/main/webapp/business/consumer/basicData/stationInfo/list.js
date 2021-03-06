﻿var opt={};
var query=new $.Storage_Query

$(function(){
	//检查session
	comm_checksession();
	//初始化BootStrapTable的数据
	//延迟500毫秒
	query.InitData(opt);
	query.InitAddEditDel(opt);
	query.InitSelect();
	// 窗体变化时，调整组件的大小
	$(window).resize(function(){
		_AutoSize();
	});
	_AutoSize();
});

//格式化站类
function FMT_STTP(value,row){
	var html="";
	var row=row.STTP;
	if(row=="MM"){
		html="气象站";
	}else if(row=="BB"){
		html="蒸发站";
	}else if(row=="DD"){
		html="堰闸水文站";
	}else if(row=="TT"){
		html="潮位站";
	}else if(row=="DP"){
		html="泵站";
	}else if(row=="SS"){
		html="墒情站";
	}else if(row=="PP"){
		html="雨量站";
	}else if(row=="ZZ"){
		html="河道水文站";
	}else if(row=="ZS"){
		html="河道水位站";
	}else if(row=="RR"){
		html="水库水文站";
	}else if(row=="ZB"){
		html="分洪水位站";
	}
	return html;
}

//格式化报汛等级
function FMT_FRGRD(value,row){
	var html="";
	var row=row.FRGRD;
	if(row=="1"){
		html="中央报汛站";
	}else if(row=="2"){
		html="省级重点报汛站，向部水文局或流域机构水文局（信息中心）报汛";
	}else if(row=="3"){
		html="省级报汛站，仅向省水文局（信息中心）报汛";
	}else if(row=="4"){
		html="不属于以上两级的其它报汛站";
	}
	return html;
}

$("#font").bind("DOMNodeInserted",function(e){
    console.log($(e.target).html());
    setTimeout(function(){
    var nm  =sessionStorage.getItem("engineeringNm")
    query.flash(nm);
    },500)
})

function countTotalPrice(){
	var nums = $(".materiel-num");
	var prices = $(".price");
	var proposalPrices = $(".proposal-price");
	for(var i=0;i<nums.length;i++){
		nums[i].value=nums[i].value.replace(/[^\d]/g,'');
		if(prices.length == nums.length && prices[i] != null){
			var num = Number(nums[i].value);
			var price = Number(prices[i].value);
			if(proposalPrices.length == nums.length 
				&& proposalPrices[i] != null){
				proposalPrices[i].value = (num * price).toFixed(2);
			}
		}
	}
}

function removeSelf(id,event){
	var obj = event.srcElement||event.target;
    var tr  = obj.parentNode.parentNode.parentNode.parentNode;
    if(tr.localName == 'td'){
    	tr  = tr.parentNode;
    }
    tr.remove();
    
    var removeIds = $("#removeIds").val();
    $("#removeIds").val(removeIds + "," + id);
    var numTds = $(".num-td");
	for(var i=0;i<numTds.length;i++){
		numTds.eq(i).html(i+1);
	}
}

// 调整界面布局大小
function _AutoSize(){
	// 设置Table的高度
	$("#query_stations_table").bootstrapTable("resetView",{"height":$("#query_stations_table_div").height()});      
} 


////////////////////////////格式化BootStrap表中的格式
//列格式化-选择
function FMT_Check(value,row,index) {
    if (index<0) {
        return {
            disabled: true,
            checked: false
        };
    }
    return value;
}
//列格式化-序号
function FMT_Num(value,row,index) {
	 var pageNumber = $("#query_stations_table").bootstrapTable('getOptions').pageNumber;
  	 var pageSize   = $("#query_stations_table").bootstrapTable('getOptions').pageSize;
  	 if(pageSize == "All"){
  		pageSize = 10000000;
  	 }
  	 return (pageNumber-1) * pageSize+index+1;
}
function FMT_amount(value,row){
	return Number(value).toFixed(2);
}
function FMT_file(value,row){
	if(value == null && row.ACCESSORY_E == null){
		return "<a href='javascript:query.uploading("+row.ID+","+row.EVASTATE+",\""+row.ENGINEERCODE+"\")'>"+0+"</a>";
	}
	return "<a href='javascript:query.uploading("+row.ID+","+row.EVASTATE+",\""+row.ENGINEERCODE+"\")'>"+(Number(value) + Number(row.ACCESSORY_E))+"</a>";
}
function FMT_evaState(value,row){
	if(value != null){
		return "<font style='color:green'>已评价</font>";
	}else {
		return "<font style='color:red'>未评价</font>";
	}
}
//操作
function FMT_handle(value,row) {
    var html="";
    html+="<a href='#' onclick='javascript:query.edit(\""+row.STCD + "\")'>" +
		"<button class='btn btn-xs btn-primary'>" +
		"<div class='visible-md visible-lg'><i class='icon icon-pencil'></i>&nbsp;修改</div>" +
		"<div class='visible-xs visible-sm'><i class='icon icon-pencil'></i></div>" +
		"</button></a>" +
		"&nbsp;&nbsp;" +
		"<a href='#' onclick='javascript:query.del(\""+row.STCD + "\")'>" +
		"<button class='btn btn-xs btn-danger btn_del_color'>" +
		"<div class='visible-md visible-lg'><i class='icon icon-trash'></i>&nbsp;删除</div>" +
		"<div class='visible-xs visible-sm'><i class='icon icon-trash'></i></div>" +
		"</button></a>" +
		"&nbsp;&nbsp;";
    return html;
}
