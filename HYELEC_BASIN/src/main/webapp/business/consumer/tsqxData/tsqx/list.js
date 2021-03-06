﻿var opt={};
var query=new $.Tsqx_Query;
$(function(){
	//检查session
	comm_checksession();
	query.InitData(opt);
	query.InitAddEditDel(opt);
	query.InitSelect();
	// 窗体变化时，调整组件的大小
	$(window).resize(function(){
		_AutoSize();
	});
});

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
	$("#query_table").bootstrapTable("resetView",{"height":comm_getHeight()-80});      
} 

function _reset_(){
	query.reset();
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
	 var pageNumber = $("#query_table_tsqx").bootstrapTable('getOptions').pageNumber;
  	 var pageSize   = $("#query_table_tsqx").bootstrapTable('getOptions').pageSize;
  	 return (pageNumber-1) * pageSize+index+1;
}
//操作
function FMT_handle(value,row) {
    var html="";
    html+="<a href='#' onclick='javascript:query.edit(\""+row.STCD + "\",\""+row.USERNAME + "\")'>" +
		"<button class='btn btn-xs btn-primary'>" +
		"<div class='visible-md visible-lg'><i class='icon icon-pencil'></i>&nbsp;修改</div>" +
		"<div class='visible-xs visible-sm'><i class='icon icon-pencil'></i></div>" +
		"</button></a>" +
		"&nbsp;&nbsp;" +
		"<a href='#' onclick='javascript:query.del(\""+row.STCD + "\",\""+row.USERNAME + "\")'>" +
		"<button class='btn btn-xs btn-danger btn_del_color'>" +
		"<div class='visible-md visible-lg'><i class='icon icon-trash'></i>&nbsp;删除</div>" +
		"<div class='visible-xs visible-sm'><i class='icon icon-trash'></i></div>" +
		"</button></a>" +
		"&nbsp;&nbsp;";
    return html;
}
$(".form_datetime").datetimepicker({
    language:  "zh-CN",
    format: "yyyy-mm-dd hh:ii"
});
