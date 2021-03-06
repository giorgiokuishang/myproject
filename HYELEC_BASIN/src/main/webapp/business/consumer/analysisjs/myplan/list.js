﻿var opt={};
var query=new $.Myplan_Query

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
});

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
	$("#query_table").bootstrapTable("resetView",{"height":comm_getHeight()-80});      
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
	 var pageNumber = $("#query_myplan_table").bootstrapTable('getOptions').pageNumber;
  	 var pageSize   = $("#query_myplan_table").bootstrapTable('getOptions').pageSize;
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
//操作
function FMT_handle(value,row,sysRole) {
	var dangqianstaff=$("#sfatt").val();
	var sysRole=$("#sysRole").val();
	var staff=row.CREATE_STAFF;
    var html="";
	if(dangqianstaff==staff||sysRole=="admins"){ //如果当前登录用户是方案的创建人或系统管理员，那么可以修改
		   html+="<a href='#' onclick='javascript:query.edit("+JSON.stringify(value)+")'>" +
			"<button class='btn btn-xs btn-primary'>" +
			"<div class='visible-md visible-lg'><i class='icon icon-pencil'></i>&nbsp;修改</div>" +
			"<div class='visible-xs visible-sm'><i class='icon icon-pencil'></i></div>" +
			"</button></a>" +
			"&nbsp;&nbsp;" +
		    	"<a href='#' onclick='javascript:query.del("+JSON.stringify(value)+")'>" +
				"<button class='btn btn-xs btn-danger btn_del_color'>" +
				"<div class='visible-md visible-lg'><i class='icon icon-trash'></i>&nbsp;删除</div>" +
				"<div class='visible-xs visible-sm'><i class='icon icon-trash'></i></div>" +
				"</button></a>" +
				"&nbsp;&nbsp;";
		    return html;
	}else{ 
		 html+="<a href='#' >" +
			"<button class='btn btn-xs btn-primary' disabled='disabled'>" +
			"<div class='visible-md visible-lg'><i class='icon icon-pencil'></i>&nbsp;修改</div>" +
			"<div class='visible-xs visible-sm'><i class='icon icon-pencil'></i></div>" +
			"</button></a>" +
			"&nbsp;&nbsp;" +
		    	"<a href='#' >" +
				"<button class='btn btn-xs btn-danger btn_del_color' disabled='disabled'>" +
				"<div class='visible-md visible-lg'><i class='icon icon-trash'></i>&nbsp;删除</div>" +
				"<div class='visible-xs visible-sm'><i class='icon icon-trash'></i></div>" +
				"</button></a>" +
				"&nbsp;&nbsp;";
		    return html;
	}
 
}
