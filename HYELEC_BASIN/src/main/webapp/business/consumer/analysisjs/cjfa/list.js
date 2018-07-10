﻿var opt={};
var query=new $.cjfa_Query ();
$(function(){
	//检查session
	comm_checksession();
	//初始化BootStrapTable的数据
	//延迟500毫秒
	query.InitData(opt);
	query.InitAddEditDel(opt);
//	query.InitSelect();
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

function countTotalPrice(){}

function removeSelf(id,event){}

// 调整界面布局大小
function _AutoSize(){
	// 设置Table的高度
	$("#query_table").bootstrapTable("resetView",{"height":comm_getHeight()-80});      
} 

function btnSave(){
	if($(" #modeCode ").val()==""){
		  confirm("提示","请选择一个模型");
		   return;
	}
		var json=$("#info_form_cjfa") .serialize();
		var url_save=basePath + "cjfa/cjfa!save.action";
        confirm("创建方案","您确定要保存方案吗？","icon-save-sign", function(result) {
			   if(result){
			 		common_ajax(json, url_save, function(response){
						// 删除后，从后台取出返回数据
			 			$("#progCode").val(response.progCode);
			 			//alert(response.progCode);
					    var msg = new $.zui.Messager("消息提示："+response.message, {placement: "center",type:"primary"});
					    msg.show();	
					    _refresh();
			 		}); 
            }
			});    

}

function btn_fj(){
	var myCharts = echarts.init(document.getElementById("cjfa_main"));
	var symbolSize = 3;
	var points = [];
	option = {
			title : {
		        text: '分析计算图表',
		        x: 'center',
		        align: 'right'
		    },
	    tooltip: {
	    	show:true,
	        formatter: function (params) {
	            var data = params.data || [0, 0];
	            return data[0].toFixed(2) + ', ' + data[1].toFixed(2);
	        }
	    },
	    grid: {
	        left: '3%',
	        right: '4%',
	        bottom: '3%',
	        containLabel: true
	    },
	    xAxis: [{
	    	type : 'category',
	    	boundaryGap : false,
            data : function (){
                var list = [];
                for (var i = 1; i <= 6; i++) {
                    if(i<= 12){
                        list.push('2018-'+i);
                    }else{
                        list.push('2018-'+(i-12));
                    }
                }
                return list;
            }()
	    }],
	    yAxis: {
	        type: 'value',
	        axisLine: {onZero: false}
	    },
	    series: [
	        {
	            id: 'a',
	            type: 'line',
	            smooth: true,
	            symbolSize: symbolSize,
	            data:[800, 300, 500, 800, 300, 600,500,600]
	        },
	        {
	            id: 'b',
	            type: 'line',
	            smooth: true,
	            symbolSize: symbolSize,
	            data:[600, 300, 400, 200, 300, 300,200,400]
	        }
	    ]
	};
	myCharts.setOption(option);
	
	$("#cjfa_fxjs").find('.modal-title').html("<i class='icon icon-pencil'></i>&nbsp;分析计算");
	$("#cjfa_fxjs").modal({
		 show : true
		,backdrop : "static" // 背景遮挡
		,moveable : true
	}).on('hide.zui.modal', function() {
		//_reset();//当第一次验证正确后，关闭窗体。再进来时，重置窗体(保留窗体上数据)。
    });

	
}
////////////////////////////格式化BootStrap表中的格式
//列格式化-选择
function FMT_Check(value,row,index) {}
//列格式化-序号
function FMT_Num(value,row,index) {}
function FMT_amount(value,row){}
function FMT_file(value,row){}


