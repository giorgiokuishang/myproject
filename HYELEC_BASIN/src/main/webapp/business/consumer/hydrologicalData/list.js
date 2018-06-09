﻿var opt={};
var precipitationQuery=new $.Precipitation_Query
var diurnalEvaporationQuery=new $.DiurnalEvaporation_Query
var riverWaterQuery=new $.RiverWater_Query
var reservoirWaterQuery=new $.ReservoirWater_Query 
var soilMoistureQuery=new $.SoilMoisture_Query 

$(function(){
	//检查session
	comm_checksession();
	//初始化BootStrapTable的数据
	precipitationQuery.InitData(opt);
	precipitationQuery.InitAddEditDel(opt);
	precipitationQuery.InitSelect();
	diurnalEvaporationQuery.InitData(opt);
	diurnalEvaporationQuery.InitAddEditDel(opt);
	diurnalEvaporationQuery.InitSelect();
	riverWaterQuery.InitData(opt);
	riverWaterQuery.InitAddEditDel(opt);
	riverWaterQuery.InitSelect();
	reservoirWaterQuery.InitData(opt);
	reservoirWaterQuery.InitAddEditDel(opt);
	reservoirWaterQuery.InitSelect();
	soilMoistureQuery.InitData(opt);
	soilMoistureQuery.InitAddEditDel(opt);
	soilMoistureQuery.InitSelect();
	// 窗体变化时，调整组件的大小
	$(window).resize(function(){
		_AutoSize();
	});
});

// 调整界面布局大小
function _AutoSize(){
	// 设置Table的高度
	$("#query_table_precipitation").bootstrapTable("resetView",{"height":comm_getHeight()-80});      
	$("#query_table_waterLevelFlow").bootstrapTable("resetView",{"height":comm_getHeight()-80});      
	$("#query_table_storageCapacity").bootstrapTable("resetView",{"height":comm_getHeight()-80});      
	$("#query_table_sectionTest").bootstrapTable("resetView",{"height":comm_getHeight()-80});      
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
//列格式化-时间
function fmt_date(value,row,index){
	return new Date(row.TM.time).format("yyyy-MM-dd hh:mm:ss");
}
//列格式化-序号
function FMT_Num1(value,row,index){
	 var pageNumber = $("#query_table_precipitation").bootstrapTable('getOptions').pageNumber;
  	 var pageSize   = $("#query_table_precipitation").bootstrapTable('getOptions').pageSize;
  	 return (pageNumber-1) * pageSize+index+1;
}
function FMT_Num2(value,row,index){
	var pageNumber = $("#query_table_diurnalEvaporation").bootstrapTable('getOptions').pageNumber;
	var pageSize   = $("#query_table_diurnalEvaporation").bootstrapTable('getOptions').pageSize;
	return (pageNumber-1) * pageSize+index+1;
}
function FMT_Num3(value,row,index){
	var pageNumber = $("#query_table_riverWater").bootstrapTable('getOptions').pageNumber;
	var pageSize   = $("#query_table_riverWater").bootstrapTable('getOptions').pageSize;
	return (pageNumber-1) * pageSize+index+1;
}
function FMT_Num4(value,row,index){
	var pageNumber = $("#query_table_reservoirWater").bootstrapTable('getOptions').pageNumber;
	var pageSize   = $("#query_table_reservoirWater").bootstrapTable('getOptions').pageSize;
	return (pageNumber-1) * pageSize+index+1;
}
function FMT_Num5(value,row,index){
	var pageNumber = $("#query_table_soilMoisture").bootstrapTable('getOptions').pageNumber;
	var pageSize   = $("#query_table_soilMoisture").bootstrapTable('getOptions').pageSize;
	return (pageNumber-1) * pageSize+index+1;
}
function FMT_amount(value,row){
	return Number(value).toFixed(2);
}

//格式化降水量
function FMT_WTH(value,row){
	var html="";
	if(row.WTH=="5"){
		html="雪";
	}else if(row.WTH=="6"){
		html="雨夹雪";
	}else if(row.WTH=="7"){
		html="雨";
	}else if(row.WTH=="8"){
		html="阴";
	}else if(row.WTH=="9"){
		html="晴";
	}
	return html;
}

//格式化蒸发器
function FMT_EPTP(value,row){
	var html="";
	if(row.EPTP=="1"){
		html="E601B";
	}else if(row.EPTP=="2"){
		html="Φ20";
	}else if(row.EPTP=="3"){
		html="Φ80";
	}else if(row.EPTP=="9"){
		html="其它";
	}
	return html;
}

//格式化河(库)水特征码
function FMT_FLWCHRCD(value,row){
	var html="";
	if(row.FLWCHRCD=="1" || row.RWCHRCD=="1"){
		html="干涸";
	}else if(row.FLWCHRCD=="2" || row.RWCHRCD=="2"){
		html="断流";
	}else if(row.FLWCHRCD=="3" || row.RWCHRCD=="3"){
		html="流向不定";
	}else if(row.FLWCHRCD=="4" || row.RWCHRCD=="4"){
		html="逆流";
	}else if(row.FLWCHRCD=="5" || row.RWCHRCD=="5"){
		html="起涨";
	}else if(row.FLWCHRCD=="6" || row.RWCHRCD=="6"){
		html="洪峰";
	}else if(row.FLWCHRCD=="P" || row.RWCHRCD=="P"){
		html="水电厂发电流量";
	}else if(row.FLWCHRCD==" " || row.RWCHRCD==" "){
		html="一般情况";
	}
	return html;
}

//格式化水势
function FMT_WPTN(value,row){
	var html="";
	if(row.WPTN=="4" || row.RWPTN=="4"){
		html="落";
	}else if(row.WPTN=="5" || row.RWPTN=="5"){
		html="涨";
	}else if(row.WPTN=="6" || row.RWPTN=="6"){
		html="平";
	}
	return html;
}

//格式化作物种类
function FMT_CRPTY(value,row){
	var html="";
	if(row.CRPTY=="0"){
		html="白地";
	}else if(row.CRPTY=="1"){
		html="小麦";
	}else if(row.CRPTY=="2"){
		html="水稻";
	}else if(row.CRPTY=="3"){
		html="春播杂粮";
	}else if(row.CRPTY=="4"){
		html="夏播杂粮";
	}else if(row.CRPTY=="5"){
		html="薯类";
	}else if(row.CRPTY=="6"){
		html="棉花";
	}else if(row.CRPTY=="7"){
		html="油菜";
	}else if(row.CRPTY=="8"){
		html="甘蔗";
	}else if(row.CRPTY=="9"){
		html="其他作物";
	}
	return html;
}

//格式化土壤类别
function FMT_SLTP(value,row){
	var html="";
	if(row.SLTP=="0"){
		html="其他";
	}else if(row.SLTP=="1"){
		html="沙土";
	}else if(row.SLTP=="2"){
		html="壤土";
	}else if(row.SLTP=="3"){
		html="粘土";
	}else if(row.SLTP=="4"){
		html="壤砂土";
	}else if(row.SLTP=="5"){
		html="砂壤土";
	}else if(row.SLTP=="6"){
		html="粘壤土";
	}
	return html;
}

//土壤含水率测法
function FMT_SLMMMT(value,row){
	var html="";
	if(row.SLMMMT=="1"){
		html="烘干法";
	}else if(row.SLMMMT=="2"){
		html="中子水分仪法";
	}else if(row.SLMMMT=="3"){
		html="时域反射法";
	}else if(row.SLMMMT=="4"){
		html="张力计法";
	}else if(row.SLMMMT=="5"){
		html="频域法";
	}else if(row.SLMMMT=="9"){
		html="其它方法";
	}
	return html;
}

//操作(降水量)
function FMT_handle1(value,row) {
    var html="";
    var TM=new Date(row.TM.time).format("yyyy-MM-dd hh:mm:ss");
    tm="";
    if(TM!=""){
    	tm+=TM+",";
    }
    html+="<a href='#' onclick='javascript:precipitationQuery.edit(\""+row.STCD + "\",\""+TM + "\")'>" +
		"<button class='btn btn-xs btn-primary'>" +
		"<div class='visible-md visible-lg'><i class='icon icon-pencil'></i>&nbsp;修改</div>" +
		"<div class='visible-xs visible-sm'><i class='icon icon-pencil'></i></div>" +
		"</button></a>" +
		"&nbsp;&nbsp;" +
		"<a href='#' onclick='javascript:precipitationQuery.del(\""+row.STCD + "\",\""+TM + "\")'>" +
		"<button class='btn btn-xs btn-danger btn_del_color'>" +
		"<div class='visible-md visible-lg'><i class='icon icon-trash'></i>&nbsp;删除</div>" +
		"<div class='visible-xs visible-sm'><i class='icon icon-trash'></i></div>" +
		"</button></a>" +
		"&nbsp;&nbsp;";
    return html;
}

//操作(日蒸发量)
function FMT_handle2(value,row){
	var html="";
	var TM=new Date(row.TM.time).format("yyyy-MM-dd hh:mm:ss");
	tm="";
	if(TM!=""){
		tm+=TM+",";
    }
	html+="<a href='#' onclick='javascript:diurnalEvaporationQuery.edit(\""+row.STCD + "\",\""+TM + "\")'>" +
	"<button class='btn btn-xs btn-primary'>" +
	"<div class='visible-md visible-lg'><i class='icon icon-pencil'></i>&nbsp;修改</div>" +
	"<div class='visible-xs visible-sm'><i class='icon icon-pencil'></i></div>" +
	"</button></a>" +
	"&nbsp;&nbsp;" +
	"<a href='#' onclick='javascript:diurnalEvaporationQuery.del(\""+row.STCD + "\",\""+TM + "\")'>" +
	"<button class='btn btn-xs btn-danger btn_del_color'>" +
	"<div class='visible-md visible-lg'><i class='icon icon-trash'></i>&nbsp;删除</div>" +
	"<div class='visible-xs visible-sm'><i class='icon icon-trash'></i></div>" +
	"</button></a>" +
	"&nbsp;&nbsp;";
	return html;
}

//操作(河道水情)
function FMT_handle3(value,row){
	var html="";
	var TM=new Date(row.TM.time).format("yyyy-MM-dd hh:mm:ss");
	tm="";
	if(TM!=""){
		tm+=TM+",";
    }
	html+="<a href='#' onclick='javascript:riverWaterQuery.edit(\""+row.STCD + "\",\""+TM + "\")'>" +
	"<button class='btn btn-xs btn-primary'>" +
	"<div class='visible-md visible-lg'><i class='icon icon-pencil'></i>&nbsp;修改</div>" +
	"<div class='visible-xs visible-sm'><i class='icon icon-pencil'></i></div>" +
	"</button></a>" +
	"&nbsp;&nbsp;" +
	"<a href='#' onclick='javascript:riverWaterQuery.del(\""+row.STCD + "\",\""+TM + "\")'>" +
	"<button class='btn btn-xs btn-danger btn_del_color'>" +
	"<div class='visible-md visible-lg'><i class='icon icon-trash'></i>&nbsp;删除</div>" +
	"<div class='visible-xs visible-sm'><i class='icon icon-trash'></i></div>" +
	"</button></a>" +
	"&nbsp;&nbsp;";
	return html;
}

//操作(水库水情)
function FMT_handle4(value,row){
	var html="";
	var TM=new Date(row.TM.time).format("yyyy-MM-dd hh:mm:ss");
	tm="";
	if(TM!=""){
		tm+=TM+",";
    }
	html+="<a href='#' onclick='javascript:reservoirWaterQuery.edit(\""+row.STCD + "\",\""+TM + "\")'>" +
	"<button class='btn btn-xs btn-primary'>" +
	"<div class='visible-md visible-lg'><i class='icon icon-pencil'></i>&nbsp;修改</div>" +
	"<div class='visible-xs visible-sm'><i class='icon icon-pencil'></i></div>" +
	"</button></a>" +
	"&nbsp;&nbsp;" +
	"<a href='#' onclick='javascript:reservoirWaterQuery.del(\""+row.STCD + "\",\""+TM + "\")'>" +
	"<button class='btn btn-xs btn-danger btn_del_color'>" +
	"<div class='visible-md visible-lg'><i class='icon icon-trash'></i>&nbsp;删除</div>" +
	"<div class='visible-xs visible-sm'><i class='icon icon-trash'></i></div>" +
	"</button></a>" +
	"&nbsp;&nbsp;";
	return html;
}

//操作(土壤墒情)
function FMT_handle5(value,row){
	var html="";
	var TM=new Date(row.TM.time).format("yyyy-MM-dd hh:mm:ss");
	tm="";
	if(TM!=""){
		tm+=TM+",";
    }
	html+="<a href='#' onclick='javascript:soilMoistureQuery.edit(\""+row.STCD + "\",\""+TM + "\",\""+row.EXKEY + "\")'>" +
	"<button class='btn btn-xs btn-primary'>" +
	"<div class='visible-md visible-lg'><i class='icon icon-pencil'></i>&nbsp;修改</div>" +
	"<div class='visible-xs visible-sm'><i class='icon icon-pencil'></i></div>" +
	"</button></a>" +
	"&nbsp;&nbsp;" +
	"<a href='#' onclick='javascript:soilMoistureQuery.del(\""+row.STCD + "\",\""+TM + "\",\""+row.EXKEY + "\")'>" +
	"<button class='btn btn-xs btn-danger btn_del_color'>" +
	"<div class='visible-md visible-lg'><i class='icon icon-trash'></i>&nbsp;删除</div>" +
	"<div class='visible-xs visible-sm'><i class='icon icon-trash'></i></div>" +
	"</button></a>" +
	"&nbsp;&nbsp;";
	return html;
}
