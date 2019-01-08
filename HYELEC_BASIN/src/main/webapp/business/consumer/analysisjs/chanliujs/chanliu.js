var _sttpList={
		MM:"气象站",
		BB:"蒸发站",
		DD:"堰闸水文站",
		TT:"潮位站",
		DP:"泵站",
		SS:"墒情站",
		PP:"雨量站",
		ZZ:"河道水文站",
		ZS:"河道水位站",
		RR:"水库水文站",
		ZB:"分洪水位站"
}
	
//导出到excel
function cl_excel(){
	if(stnm==''){
		confirm("产流计算", "请输入测站名称")
		return false;
	}
	var h=$("#honghao").val();
	if(h==''){
		confirm("产流计算", "请选择一条计算结果")
		return false;
	}
	var url= basePath + "chanliu/chanliu!chanLiuExportExcel.action?pch="+h+"&stcd="+ $("#stcd").val()  ;      
	confirm("<i class='icon icon-circle-arrow-up'></i>&nbsp;导出到excel","您确定要导出到excel吗？","icon-info", function(result) {
		   if(result){
			   window.location.href=url;
		   }
		});
}
var jylarr=[];
var datearr=[];
var ymlarr=[];
var thead=[];//定义测站名称数组
var theadqz=[];//定义权重数组
var addtrtdlist;
var cznameList;
var pchList=new Array();
function choose(){//下拉选择洪号的时候
	jylarr=[];
	var value=	$('#selectResult option:selected') .val();
	var json={
			'pch':value,
			'stcd':$("#stcd").val()
			};
	$("#honghao").val(value);
	hh=value;
	var url= basePath + "chanliu/chanliu!getHistory.action"               
	common_ajax(json, url, function(response) {
		$("#table1_tbdody").empty();
		$(".add").remove();
		$(".getStnm").val("")
		$("#quanzhong").val("")
		if(response.rows.length>0){
			stn=response.rows[0].STNM; //测站名称
			var num=stn.split(","); //测站名分割
			thead=num;//测站名称数组赋值
			var qz=response.rows[0].QZ.split(","); //权重分割
			theadqz=qz;//权重数组赋值
			hh=response.rows[0].PCH;
			lymj=response.rows[0].LYMJ;
		
			ylz=response.ylz;
				for(var j=0;j<num.length;j++){//这个循环处理的是thead内容
					if(num.length==1){//1个测站直接赋值
						$(".getStnm").val(stnm)
						$("#quanzhong").val(qz)
					}else{
						if(j==0){//当第一个值，不创建新的td直接赋值
							$(".getStnm").val(num[j])//测站第j=0个
							$("#quanzhong").val(qz[j])//权重第j=0个
						}else{//否则创建td并赋值
							var td = "<td colspan='2' class='add' align='center'><input style='width:100%' type='text' value='"+num[j].replace(/^\s+|\s+$/g,"")+"'    id='getStnm_combox"+j+"' class='getStnm'name='czmc'  placeholder='请输入测站名'>" +
							"</td>";
						var td2 = "<td  class='add'style='width:100px'>权重</td><td  class='add' style='width:100px'><input style='width:100px' type='text' name='quanzhong'  value='"+qz[j].replace(/^\s+|\s+$/g,"")+"'></td>";
					$(".addTd").after(td);
					$(".addTd2").after(td2);
						}
					}
				}
			$("#cols").val(ylz);//雨量站个数赋值
			var total=response.rows.length-1;
			$("#start").val(response.rows[0].DATE); //开始时间
			$("#end").val(response.rows[total].DATE);//结束时间
			 beginDate =response.rows[0].DATE; //开始时间赋值
				endDate=response.rows[total].DATE;//结束时间赋值
				interval=response.rows[0].SJJG;//时间间隔赋值
				$("#jiange").val(interval);
				stcd=response.rows[0].STCD;
			for (var i = 0; i < response.rows.length; i++) {//循环遍历输出tbody内容
						var jyl=response.rows[i].JYL.split(",");
						var tr="<tr align='center' id='removeTr"+i+"'  >"+
						"<td style='text-align: center;'>"+(i+1)+"</td>"+
						"<td><input type='text' name='time' value='"+response.rows[i].DATE+" 'style='width:100%'></td>";
						for( var j=0;j<num.length;j++){//拼接字符串
							tr =tr+"<td colspan='2' id='td4' class='addTd3' align='center'><input type='text' name='yu' value='"+jyl[j].replace(/^\s+|\s+$/g,"")+"' align='center' style='width:100%' value='0'></td>";
						}
						tr=tr+"<td  align='center' class='addTd4' ><input type='text' name='result' value='"+response.rows[i].YML+"'align='center' style='width:100%'></td></tr>";
						$("#table1_tbdody").append(tr); //添加行的函数
						jylarr.push(response.rows[i].JYL);
						datearr.push(response.rows[i].DATE)
						ymlarr.push(response.rows[i].YML)
			}
			
		
		}
		//$(".add").remove()
		var czmc = document.getElementsByName("czmc");
		cznameList=czmc;
	
		initHuiliuStep1StcdCombox(ylz);
	});

}
function initTableData(){
	$("#chanliu_table1_thead").empty();
	var tr1=$("<tr id='tr1'></tr>");
	tr1.append("<td rowspan='2' align='center'>序号</td>");
	tr1.append("<td rowspan='2' align='center' style='width:190px' id='riqi'>日期</td>");
	var num=1;
	if(num>0){
		for(var i=0;i<num;i++){
			tr1.append("<td colspan='2' align='center' id='thead_tr1_td_"+i+"' class='addTd'><input type='text' name='czmc' id='getStnm_combox"+i+"'class='getStnm' placeholder='请输入测站名'  style='width:100%'></td>");
		}
	}
	tr1.append("<td rowspan='2' align='center'>雨面量</td>");
	$("#chanliu_table1_thead").append(tr1);
	var tr2=$("<tr align='center' id='tr2'></tr>");
	if(num>0){
		for(var i=0;i<num;i++){
			tr2.append("<td style='width:100px' id='thead_tr2_td_"+i+"'>权重</td>");
			tr2.append("<td style='width:100px' id='thead_tr2_td_qz_"+i+"' class='addTd2' align='center'><input style='width:100px' type='text'  name='quanzhong' id='quanzhong_"+i+"'></td>");
		}
	}
	$("#chanliu_table1_thead").append(tr2);
	$("#table1_tbdody").empty();
	var tr=$("<tr align='center'></tr>");
	tr.append("<td style='text-align: center;'></td>");
	tr.append("<td><input type='text' name='time' style='width:100%'></td>");
	if(num>0){
		for(var j=0;j<num;j++){
			tr.append("<td colspan='2' class='addTd3' align='center'><input type='text' name='yu' align='center' style='width:100%' value='0'></td>");
		}
	}
	tr.append("<td align='center' class='addTd4' ><input type='text' name='result' align='center' style='width:100%'></td>");
	$("#table1_tbdody").append(tr);
	if(num>0){
		initHuiliuStep1StcdCombox(num);
	}
}
function initHuiliuStep1StcdCombox(ylzsl){//下拉模糊匹配组件在\common\js\combox\combox.js
	var comboxList = new Array();
	for(var i=0;i<ylzsl;i++){
		comboxList.push(i);
	}
	$.each(comboxList,function(index,czname){
		$("#getStnm_combox"+index).combox({
			url:basePath + "chanliu/chanliu!getStbprpMoHu1.action",
			label:"STNM",
			value:"STCD",
			dataKey:"dataList",
			height:'250px',
			width:'150px',
			isPager:false,
			select:function(item){
				loadLLDataByStcd(index,item.STCD);
			}
		});
	});
}
function loadLLDataByStcd(index,stcd){
	var start=$("#start").val();
	if(start!=null){
		start=$.trim(start);
	}
	var end=$("#end").val();
	if(end!=null){
		end=$.trim(end);
	}
	var jge=$("#jiange").val();
	if(jge!=null){
		jge=$.trim(jge);
	}
	$.ajax({
		url:basePath + "chanliu/chanliu!getStep1Jyl.action",
		type:"post",
		data:{stcd:stcd,start:start,end:end,INTERVAL:jge},
		dataType:"json",
		success:function(response){
			console.log("----i===="+index+"-----result----"+JSON.stringify(response))
			if(response!=null && response.length>0){
				for(var i=0;i<response.length;i++){
					var dt = response[i].DT;
					var jyl=response[i].DRP;
					var tdid="tr_"+timeStr(dt)+"_"+index;
					$("#"+tdid).val($.trim(jyl));
				}
			}
		}
	});
}
function getStep1StcdsAndStnms(){
	var stcds="",stnms="",qzs="";
	var ylzsl=$("#cols").val();
	for(var i=0;i<ylzsl;i++){
		var value=$("#getStnm_combox"+i).combox("getValue");
		var label=$("#getStnm_combox"+i).combox("getLabel");
		var qz=$("#quanzhong_"+i).val();
		if(label!=null && $.trim(label).length>0){
			if(value==null||$.trim(value).length<1){
				var str="00000000";
				var zst=str.substring(0,6-(i+1).toString().length);
				value="MY"+zst+(i+1).toString();
			}
			if(stcds!=null && stcds.length>0){
				stcds=stcds+","+value;
			}else{
				stcds=value;
			}
			if(stnms!=null && stnms.length>0){
				stnms=stnms+","+label;
			}else{
				stnms=label;
			}
			if(qz!=null && qz.length>0){
				if(qzs!=null && qzs.length>0){
					qzs=qzs+","+qz;
				}else{
					qzs=qz;
				}
			}
		}
	}
	return {stcds:stcds,stnms:stnms,qzs:qzs};
}
function initStep1Data(stcd,hh){
	var json={
			'pch':hh,
			'stcd':stcd
	};
	var url= basePath + "chanliu/chanliu!queryStep1TableDataByStcdAndPch.action";
	$.ajax({
		url:url,
		data:json,
		type:"post",
		dataType:"json",
		success:function(response){
			if(response.reflag==1||response.reflag=="1"){
				var header = response.header;
				if(header!=null){
					var stn=header.stnms;
					var stnmarr=stn.split(",");
					var kssj=header.kssj;
					var jssj=header.jssj;
					var jge=header.SJJG;
					var cl=stnmarr!=null?stnmarr.length:1;
					ylz=cl;
					secondBeginDate=header.kssj2;
					secondEndDate=header.jssj2;
					secondInterval=header.sjjg2;
					$("#cols").val(cl);//雨量站个数赋值
					$("#start").val(kssj); //开始时间
					$("#end").val(jssj);//结束时间 
					$("#jiange").val(jge);
					generTable();
					loadHeaderData(response.header);
				}
				if(response.rows!=null && response.rows.length>0){
					var dataList=response.rows;
					loadCellData(dataList);
				}
			}else{
				layer.msg(response.message);
			}
		}
	});
}
function pchChange(){
	stcd= $("#stcd").val(); // 测站编码
	stcd=stcd!=null?$.trim(stcd):"";
	hh=$('#selectResult option:selected').val();
	$("#honghao").val(hh);
	hh=hh!=null?hh.replace(/\s+/g, ""):"";
	if((hh==null || hh.length<1)||(stcd==null||stcd.length<1)){
		secondBeginDate="";
		secondEndDate="";
		secondInterval="";
		initTableData();
	}else{
		initStep1Data(stcd,hh);
	}
}
function generTable(){
	 stcd= $("#stcd").val(); // 测站编码
	 stnm=$("#stnm1").val();//测站名称
	 beginDate  = $("#start").val(); // 开始时间
	 beginDate=beginDate!=null?$.trim(beginDate):null;
	 endDate  = $("#end").val(); // 结束时间
	 endDate = endDate!=null?$.trim(endDate):null;
	 interval =$("#jiange").val(); //时间间隔
	 interval=interval!=null?$.trim(interval):null;
	 var num = $("#cols").val();// 列数、
	 num=num!=null?$.trim(num):null;
	if(beginDate==null||beginDate.length<1){
		alert("请选择开始时间！")
		return false;
	}
	if(endDate==null || endDate.length<1){
		alert("请选择结束时间！")
		return false;
	}
	if(interval==null||interval.length<1){
		alert("请输入时间间隔！")
		return false;
	}
	if(num==null||num.length<1){
		alert("请输入雨量站数量！")
		return false;
	}
	var re = /^[1-9]+[0-9]*]*$/;
	if(!re.test(interval)){
		alert("时间间隔请输入正整数");
		return false;
	}
	if(!re.test(num)){
		alert("雨量站请输入整数")
		return false;
	}
	$("#chanliu_table1_thead").empty();
	var tr1=$("<tr id='tr1'></tr>");
	tr1.append("<td rowspan='2' style='width:50px' align='center'>序号</td>");
	tr1.append("<td rowspan='2' align='center' style='width:150px' id='riqi'>日期</td>");
	if(num>0){
		for(var i=0;i<num;i++){
			tr1.append("<td colspan='2' align='center' style='width:150px' id='thead_tr1_td_"+i+"' class='addTd'><input type='text' name='czmc' id='getStnm_combox"+i+"'class='getStnm' placeholder='请输入测站名'  style='width:100%'></td>");
		}
	}
	tr1.append("<td rowspan='2' style='width:100px' align='center'>雨面量</td>");
	$("#chanliu_table1_thead").append(tr1);
	var tr2=$("<tr align='center' id='tr2'></tr>");
	if(num>0){
		for(var i=0;i<num;i++){
			tr2.append("<td style='width:50px' id='thead_tr2_td_"+i+"'>权重</td>");
			tr2.append("<td style='width:100px' id='thead_tr2_td_qz_"+i+"' class='addTd2' align='center'><input style='width:100px' type='text'  name='quanzhong' id='quanzhong_"+i+"'></td>");
		}
	}
	$("#chanliu_table1_thead").append(tr2);
	$("#table1_tbdody").empty();
	var timeList = getTimeList();
	if (timeList!=null && timeList.length > 0) { 
		for(var i=0;i<timeList.length;i++){
			var tr=$("<tr align='center' id='removeTr"+i+"'  ></tr>");
			tr.append("<td style='text-align: center;'>"+(i+1)+"</td>");
			tr.append("<td><input type='text' name='time' style='width:100%' value='"+timeList[i]+"'></td>");
			if(num>0){
				for(var j=0;j<num;j++){
					var tdid="tr_"+timeStr(timeList[i])+"_"+j;
					tr.append("<td colspan='2' class='addTd3' align='center'><input type='text' id='"+tdid+"' name='yu' align='center' style='width:100%' value='0'></td>");
				}
			}
			tr.append("<td align='center' class='addTd4' ><input type='text' id='tr_"+timeStr(timeList[i])+"_myl' name='result' align='center' style='width:100%'></td>");
			$("#table1_tbdody").append(tr);
		}
	}
	if(num>0){
		initHuiliuStep1StcdCombox(num);
	}
}
function queding(){
	var stcdObj=getStep1StcdsAndStnms();
	generTable();
	var pch=$("#honghao").val();
	pch=pch!=null?pch.replace(/\s+/g, ""):"";
	if(pch!=null && pch.length>0){
		loadStep1TableData(stcd,pch,stcdObj);
	}
}
function loadStep1TableData(stcd,pch,stcdObj){
	var stcds=stcdObj.stcds;
	var stnms=stcdObj.stnms;
	var qzs=stcdObj.qzs;
	var kssj=$("#start").val();
	var jssj=$("#end").val();
	var jge=$("#jiange").val();
	var json={
			'pch':pch,
			'stcd':stcd,
			'start':kssj,
			'end':jssj,
			'stcds':stcds,
			'stnms':stnms,
			'qzs':qzs,
			'INTERVAL':jge
	};
	var url= basePath + "chanliu/chanliu!getStep1TableData.action"               
	$.ajax({
		url:url,
		data:json,
		type:"post",
		dataType:"json",
		success:function(response){
			if(response.reflag==1||response.reflag=="1"){
				var header=response.header;
				secondBeginDate=header.kssj2;
				secondEndDate=header.jssj2;
				secondInterval=header.sjjg2;
				loadHeaderData(header);
				if(response.rows!=null && response.rows.length>0){
					var dataList=response.rows;
					loadCellData(dataList);
				}
			}else{
				layer.msg(response.message);
			}
		}
	});
}
function loadHeaderData(header){
	if(header!=null && header.stcds!=null && header.stnms!=null && header.qzs!=null){
		var stnms=header.stnms;
		var stcds=header.stcds;
		var qzs=header.qzs;
		var stnmarr=stnms.split(",");
		var stcdarr=stcds.split(",");
		var qzarr=qzs.split(",");
		if(stnmarr!=null && stnmarr.length>0 && stcdarr!=null && stcdarr.length>0){
			for(var i=0;(i<stnmarr.length && stcdarr.length);i++){
				$("#getStnm_combox"+i).combox("setValue",{label:$.trim(stnmarr[i]),value:$.trim(stcdarr[i])});
			}
		}
		if(qzarr!=null && qzarr.length>0){
			for(var i=0;i<qzarr.length;i++){
				$("#quanzhong_"+i).val($.trim(qzarr[i]));
			}
		}
	}
}
function loadCellData(dataList){
	for(var i = 0; i < dataList.length; i++){
		var dt=dataList[i].DATE;
		dt=dt.substring(0,16);
		var jyls=dataList[i].JYL.split(",");
		if(jyls!=null && jyls.length>0){
			for(var j=0;j<jyls.length;j++){
				var tdid="tr_"+timeStr(dt)+"_"+j;
				$("#"+tdid).val($.trim(jyls[j]));
			}
		}
		if(dataList[i].YML!=null){
			$("#tr_"+timeStr(dt)+"_myl").val($.trim(dataList[i].YML))
		}
	}
}
function timeStr(time){
	time=time.replace(/-/g, "");
	time=time.replace(/:/g,"");
	time=time.replace(/\s+/g, "");
	return time;
}
function getTimeList(){
	var timeList=new Array();
	var date1 = $("#start").val(); // 开始时间
	date1=$.trim(date1);
	var date2 = $("#end").val(); // 结束时间
	date2=$.trim(date2);
	var shu = $("#jiange").val();
	var jg=parseInt(shu);
	while(date1<=date2){
		timeList.push(date1);
		date1=formatDateTime(new Date(new Date(date1).getTime()+jg*60*1000));
	}
	return timeList;
}
//确定
function autocreate() {
	 stcd= $("#stcd").val(); // 测站编码
	 stnm=$("#stnm1").val();//测站名称
	 beginDate  = $("#start").val(); // 开始时间
	 endDate = $("#end").val(); // 结束时间
	 interval =$("#jiange").val(); //时间间隔
	var num = $("#cols").val();// 列数、
	var f= Number.isInteger(Number(num)); //雨量站验证
	var l= Number.isInteger(Number(interval)); //雨量站验证
	if(f==false){
		alert("雨量站请输入整数")
		return false;
	}
	if(l==false){
		alert("时间间隔请输入整数")
		return false;
	}
	var d1 = $("#start").val().toString();
	if(d1==""){
		alert("请选择时间段！")
		return false;
	}
	if(num==""){
	alert("请输入雨量站个数！");
	return false;
	}
	$(".add").remove();// 先把添加的移除，避免重复添加
	// 动态添加列
	if(num>thead.length&&thead.length!=0){
		for (var i = 0; i < num-thead.length; i++) {
			var td = "<td colspan='2' class='add' align='center' ><input id='getStnm_combox"+i+"'  style='width:100%' type='text'  ' class='getStnm'name='czmc'  placeholder='请输入测站名'>" +
			"</td>";
			var td2 = "<td  class='add' style='width:50px'>权重</td><td  class='add' style='width:100px'><input style='width:100px' type='text' name='quanzhong' value='' ></td>";
			$(".addTd").after(td);
			$(".addTd2").after(td2);
		}
	}
	
		if(thead.length>0&&num>=thead.length){//如果测站名称数组有值
			for(var j=1;j<thead.length;j++){
				var td = "<td colspan='2' class='add' align='center'><input  id='getStnm_combox"+i+"'  style='width:100%' type='text'  value='"+thead[j].replace(/^\s+|\s+$/g,"")+"' class='getStnm'name='czmc'  placeholder='请输入测站名'>" +
				"</td>";
				var td2 = "<td  class='add'style='width:100px'>权重</td><td  class='add'style='width:100px'><input  style='width:100px' type='text' name='quanzhong' value='"+theadqz[j].replace(/^\s+|\s+$/g,"")+"' ></td>";
				$(".addTd").after(td);
				$(".addTd2").after(td2);
			}
	}else if(num==1){//如果1列不加
	
		}else{
			for (var i = 1; i < num; i++) {
				var td = "<td colspan='2' class='add' align='center'><input   id='getStnm_combox"+i+"'  style='width:100%' type='text'  ' class='getStnm'name='czmc'  placeholder='请输入测站名'>" +
				"</td>";
				var td2 = "<td  class='add' style='width:100px'>权重</td><td  class='add' style='width:100px'><input  style='width:100px' type='text' name='quanzhong' value='' ></td>";
				$(".addTd").after(td);
				$(".addTd2").after(td2);
		}
	}
	var list = cha();
	if (list > 0) { 
		$("#table1_tbdody").empty();//移除
		var k=jylarr.length;//降雨量数组长度，数据长度
		var a=0;
		for (var i = 0; i <=list; i++) {//循环添加tr
			var tr="<tr align='center' id='removeTr"+i+"'  >"+
			"<td style='text-align: center;'>"+(i+1)+"</td>"+
			"<td><input type='text' name='time' style='width:100%'></td>";
			//num为列数
			if(num==1){//如果为1列
				for( var j=0;j<num;j++){
					if(jylarr.length==0){//如果没选择数据
						tr =tr+"<td colspan='2' id='td4' class='addTd3' align='center'><input type='text' name='yu' align='center' style='width:100%' value='0'></td>";
					}else{//否则选择了一个计算结果数据
						if(list>jylarr.length){//如果新增行数小于等一已有数据
							//先添加已有数据
							if(a==k){//当已有数据=新添加的行
								tr =tr+"<td colspan='2' id='td4' class='addTd3' align='center'><input type='text' name='yu' align='center' style='width:100%' value='0'></td>";
							}else{
								//if()
								var jyl=jylarr[a].split(",");
								tr =tr+"<td colspan='2' id='td4' class='addTd3' align='center'><input type='text' name='yu' align='center' style='width:100%' value='"+jyl[0].replace(/^\s+|\s+$/g,"")+"'></td>";
								a+=1;
							}
						}else{
							var jyl=jylarr[i].split(",");
							tr =tr+"<td colspan='2' id='td4' class='addTd3' align='center'><input type='text' name='yu' align='center' style='width:100%' value='"+jyl[0].replace(/^\s+|\s+$/g,"")+"'></td>";
						
						}
					
					}
					
				}
			}else if(list<jylarr.length){//如果新-增的行数小于等于已有的数据，那么，如果有数据，并且每一条数据的降雨量都有1个以上
				
				var jyl=jylarr[i].split(",");
				if(jyl.length>num){//如果每一行雨量个数大于列数，那么新增的列赋值
					for(var z=0;z<num;z++){//先循环已经有数据的新增td并赋值
						tr =tr+"<td colspan='2' id='td4' class='addTd3' align='center'><input type='text' name='yu' align='center' style='width:100%' value='"+jyl[z].replace(/^\s+|\s+$/g,"")+"'></td>";
					}
				}else{
					for(var z=0;z<jyl.length;z++){//先循环已经有数据的新增td并赋值
						tr =tr+"<td colspan='2' id='td4' class='addTd3' align='center'><input type='text' name='yu' align='center' style='width:100%' value='"+jyl[z].replace(/^\s+|\s+$/g,"")+"'></td>";
					}
					for(var h=0;h<num-jyl.length;h++){//再新增td赋值没数据的
						tr =tr+"<td colspan='2' id='td4' class='addTd3' align='center'><input type='text' name='yu' align='center' style='width:100%' value='0'></td>";
					}
				}
				
				}else if(list>=jylarr.length){//如果新增行数大于已有的数据长度，
					if(a>=k){
						for(var l=0;l<num;l++){
							tr =tr+"<td colspan='2' id='td4' class='addTd3' align='center'><input type='text' name='yu' align='center' style='width:100%' value='0'></td>";
						}
					}else{
						var jyl=jylarr[a].split(",");
						if(jyl.length>num){//如果每一行雨量的值大于列数
							for(var n=0;n<num;n++){//有雨量值的情况回显数据
								tr =tr+"<td colspan='2' id='td4' class='addTd3' align='center'><input type='text' name='yu' align='center' style='width:100%' value='"+jyl[n].replace(/^\s+|\s+$/g,"")+"'></td>";
							}
						}else{
							for(var n=0;n<jyl.length;n++){//有雨量值的情况回显数据
								tr =tr+"<td colspan='2' id='td4' class='addTd3' align='center'><input type='text' name='yu' align='center' style='width:100%' value='"+jyl[n].replace(/^\s+|\s+$/g,"")+"'></td>";
							}
							for(var n=0;n<num-jyl.length;n++){//没有雨量值的情况默认填0
								tr =tr+"<td colspan='2' id='td4' class='addTd3' align='center'><input type='text' name='yu' align='center' style='width:100%' value='0'></td>";
							}
							
						}
						a+=1;
					}
				
				}else{//否则添加列
					for( var j=0;j<num;j++){
					tr =tr+"<td colspan='2' id='td4' class='addTd3' align='center'><input type='text' name='yu' align='center' style='width:100%' value='0'></td>";
				}
			
			}
			tr=tr+"<td  align='center' class='addTd4' ><input type='text' name='result' align='center' style='width:100%'></td></tr>";//计算结果闭合
			$("#table1_tbdody").append(tr); //添加行的函数
			
		}
	}else if(list<0){
		alert("注意：结束时间应该大于开始时间！")
		var tr="";
		return false;
	}
	var shijian = document.getElementsByName("time");// 获取所有时间input，往里面添加时间
	var date1 = new Date($("#start").val()); // 开始时间
	var date2 = date1.getTime();// 将时间转换时间戳
	var curTime = formatDateTime(date1)// 转化时间格式
	var shu = Number($("#jiange").val()) * 60;// 分钟*秒
	var haomiao = shu * 1000;// 毫秒，增加时间的时候用毫秒
	var addTime = 0;
	for (var i = 0; i < shijian.length; i++) {// 下面的switch是生成时间都
		switch (i) {
		case 0:
			for(var j=0;j<num;j++){
				var x=2+j;
				var d1=date1.getTime();
				$("#removeTr"+i+"").find("td:eq("+x+")").find("input").attr("id",d1+j);
				$("#removeTr"+i+"").find("td:eq("+(x+1)+")").find("input").attr("id",i);
			}
			
			shijian[i].value = curTime;// 当第一个就是开始时间
			break;
		case 1:
			for(var j=0;j<num;j++){
				var x=2+j;
				var d2=date2 + haomiao;
				$("#removeTr"+i+"").find("td:eq("+x+")").find("input").attr("id",d2+j);
				$("#removeTr"+i+"").find("td:eq("+(x+1)+")").find("input").attr("id",i);
			}
			var t1 = formatDateTime(new Date(date2 + haomiao));
			shijian[i].value = t1;
			break;
		default:
			for(var j=0;j<num;j++){
				var x=2+j;
				var d3=date2 + addTime;
				$("#removeTr"+i+"").find("td:eq("+x+")").find("input").attr("id",d3+j);
				$("#removeTr"+i+"").find("td:eq("+(x+1)+")").find("input").attr("id",i);
			}
			var t2 = formatDateTime(new Date(date2 + addTime));
			shijian[i].value = t2;
			;
		}
		addTime += haomiao;// 每次叠加1个时间段
	}
	//给stnm附加方法模糊查询
	var czmc = document.getElementsByName("czmc");
	cznameList=czmc;

	initHuiliuStep1StcdCombox(num);
}
function clearStep1Data(){
	$("#honghao").val("");
	if($("#selectResult")){
		$("#selectResult").empty();
	}
	$("#start").val("");
	$("#end").val("");
}
//查询输入的测站名称是否存在
function getStp() {
	var json = $("#info_form_cjfa").serialize();
	 stnm=$("#stnm1").val();
	if(stnm==''){
		confirm("产流计算", "请输入测站名称")
		return false;
	}
	var url = basePath + "chanliu/chanliu!getStbprpByName.action"
	common_ajax(json, url, function(response) {
		staffName=	response.mSysStaff.staffName;
		staffCode=	response.mSysStaff.staffCode;
		// 如果total是0，则该测站名称不存在
		if (response.total == 0) {
			$("#m").show();// 提示
			$("#zhanma").empty();
			$("#stcd").val("");
			$("#show_result").hide(); //计算结果
		} else {
			$("#show_result").show();
			$("#stcd").val(response.rows[0].STCD);
			console.log("---------step1----stcd----"+response.rows[0].STCD)
			var json={'stcd':response.rows[0].STCD};
			var url= basePath + "chanliu/chanliu!getSelect.action";
			common_ajax(json, url, function(response) {
				$("#selectResult").empty();
				if (response.total == 0) {
					$("#selectResult").empty();
				} else {
					pchList=response.rows;
					$("#selectResult").append("<option value=''> --请选择--</option>")
					for (var i = 0; i < response.rows.length; i++) {
						$("#selectResult").append("<option value='"+response.rows[i].PCH+"'  > "+response.rows[i].PCH+"</option>")
					}
				}
				$("#selectResult").val($("#honghao").val());
			});
			if(response.rows.length>0){
				$("#zhanma").html(response.rows[0].STCD);
				$("#stcd").val(response.rows[0].STCD);// 测站编码赋值
				$("#zhansttp").html(_sttpList[response.rows[0].STTP]);
			}
			stcd=$("#stcd").val();
			$("#zhanma").show();
			$("#m").hide();// 不提示
		}
	});
}

function isHavePch(pch){
	if(pchList!=null && pchList.length>0){
		for(var i=0;i<pchList.length;i++){
			if($.trim(pch)==$.trim(pchList[i].PCH)){
				return true;
			}
		}
	}
	return false;
}
// 保存计算结果
function save() {	
	var t1=$("#start").val();
	var t2=$("#end").val();
	var date1 = new Date(t1);
	var pch=$("#honghao").val();
	var l=$("#cols").val();
	var jg=$("#jiange").val();
	var st=$("#stcd").val();
	if(pch==null || $.trim(pch).length<1){
		pch=formatDateTime2(date1);
		$("#honghao").val(pch);
	}
	if(st==null || $.trim(st).length<1){
		alert("点击查询！")	
		return false;
	}
	if(jg==null || $.trim(jg).length<1){
		alert("请填写时间间隔！")
		return false;
	}
	var stobjs=getStep1StcdsAndStnms();
	var selectPch=$("#selectResult")?$("#selectResult").val():null;
	var json = $("#info_form_cjfa").serialize();
	json=json+"&stcds="+stobjs.stcds;
	json=json+"&stnms="+stobjs.stnms;
	json=json+"&start="+t1;
	json=json+"&end="+t2;
	json+="&pch="+pch+"&INTERVAL="+jg+"&stationNum="+l;
	var url = basePath + "chanliu/chanliu!save.action";
		if(isHavePch(pch)){
			confirm("产流计算","洪号已存在，是否要覆盖原洪号的数据?", "icon-save-sign", function(result) {
				if (result) {
					common_ajax(json, url, function(response) {
						hh=response.pch; 
						stcd=response.stcd;
						ylz=response.ylz;
						beginDate=$("#start").val();
						endDate=$("#end").val();
						interval=$("#jiange").val();
						$("#honghao").val(hh);
						getStp();
						layer.msg("保存成功!");
					});
				}
			});
		}else{
			confirm("产流计算", "您确定要保存计算结果吗？", "icon-save-sign", function(result) {
				if (result) {
					common_ajax(json, url, function(response) {
						hh=response.pch; 
						stcd=response.stcd;
						ylz=response.ylz;
						beginDate=$("#start").val();
						endDate=$("#end").val();
						interval=$("#jiange").val();
						$("#honghao").val(hh);
						getStp();
						layer.msg("保存成功!");
					});
				}
			});
		}


}

// 计算
function jisuan() {
	//
	 interval =$("#jiange").val(); //时间间隔
	 var num = $("#cols").val();// 列数
	var f= Number.isInteger(Number(num)); //雨量站验证
	var l= Number.isInteger(Number(interval)); 
	if(f==false){
		alert("雨量站请输入整数")
		return false;
	}
	if(l==false){
		alert("时间间隔请输入整数")
		return false;
	}
	
	var arr = []; // 声明一个数组用来存储雨面量
	arr = yumianliang();
	var res = document.getElementsByName("result");
	for (var i = 0; i < res.length; i++) {
		   var y = String(arr[i]).indexOf(".") + 1;//获取小数点的位置
		    var count = String(arr[i]).length - y;//获取小数点后的个数
		    if(count>2){//如果超过2位小数，四舍五入
		    	$("#removeTr" + i + " td:last input:first").val(arr[i].toFixed(1));
		    }else{
		    	$("#removeTr" + i + " td:last input:first").val(arr[i]);
		    }
		
	}

}
// 计算雨面量 返回结果数组
function yumianliang() {
	// **校验权重----START------
	var qz = document.getElementsByName("quanzhong");// 获取所有权重的Input，
	var yu = document.getElementsByName("yu");// 获取所有降雨量的Input，
	var y = 0;// 权重的和
	var yuliang = 0;// 降雨量的和，累加
	var arr1 = [];// 定义一个数组来接收雨量值,这个值可以是输入的也可以是查询的
	for (var i = 0; i < qz.length; i++) {
		if (isNaN(qz[i].value)) {
			confirm("产流计算", "权重:请输入数字")
			return false;
		}
		if (qz[i].value == '') {
			confirm("产流计算", "权重不能为空")
			return false;
		}
		y += Number(qz[i].value);
	} // yuliang+=Number(qz[i].value)*Number(yu[i].value); //乘积之后求和
	if (y > 1) { // 值超过1判断
		confirm("产流计算", "权重值不能超过1")
		return false;
	}
	if (y < 1) { // 值小于1判断
		confirm("产流计算", "权重值不能小于1")
		return false;
	}
	for (var j = 0; j < yu.length; j++) {
		var he = Number(yu[j].value);
		var c=yu[j].value.toString();
		var f= isNaN(Number(he)); //雨量Input验证
		if(f==true){
			alert("降雨量不能为非数字")
			return false;
		}
		arr1.push(he);
	}
	var yuliang2 = [];
	var result = [];
	var num = Number($("#cols").val());
	if (arr1.length > 1) { // 加入判断，防止死循环崩溃
		for (var i = 0, len = arr1.length; i < len; i += num) {
			result.push(arr1.slice(i, i + num));
		}
	}
	var zonghe = [];
	// var re=0;//
	for (var i = 0; i < result.length; i++) {
		var re = 0;
		for (var j = 0; j < result[i].length; j++) {

			re = result[i][j] * Number(qz[j].value);// 量*权重 求和
			zonghe.push(re);
		}
	}
	// 将求和过后的数组在分割
	var q = [];
	for (var i = 0, len = zonghe.length; i < len; i += num) {
		q.push(zonghe.slice(i, i + num));
	}
	var p = [];
	for (var i = 0; i < q.length; i++) {
		p.push(sum(q[i]));
	}
	return p;
}

// 数组求和
function sum(arr) {
	var len = arr.length;
	if (len == 0) {
		return 0;
	} else if (len == 1) {
		return arr[0];
	} else {
		return arr[0] + sum(arr.slice(1));
	}
}




// 计算时间差
function cha() {
	var date1 = new Date($("#start").val()).getTime(); // 开始时间
	var date2 = new Date($("#end").val()).getTime(); // 结束时间
	var shu = $("#jiange").val();
	if(shu==""){
		alert("请输入时间间隔！")
		return false;
	}
	var date3 = date2 - date1; // 时间差的毫秒数
	// ------------------------------
	// 计算出相差天数
	var days = Math.floor(date3 / (24 * 3600 * 1000))
	// 计算出小时数
	var leave1 = date3 % (24 * 3600 * 1000) // 计算天数后剩余的毫秒数
	var hours = Math.floor(leave1 / (3600 * 1000))
	// 计算相差分钟数
	var leave2 = leave1 % (3600 * 1000) // 计算小时数后剩余的毫秒数
	var minutes = Math.floor(leave2 / (60 * 1000))
	// 计算相差秒数
	var leave3 = leave2 % (60 * 1000) // 计算分钟数后剩余的毫秒数
	var seconds = Math.round(leave3 / 1000)
	var fen = days * 24 * 60 + hours * 60 + minutes
	var n = Number(shu)
	var tr = fen / n
	// alert(" 相差 "+days+"天 "+hours+"小时 "+minutes+" 分钟")
	// alert("总行数:"+tr)
	return tr;
}

// 时间格式化
var formatDateTime = function(date) {
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	m = m < 10 ? ('0' + m) : m;
	var d = date.getDate();
	d = d < 10 ? ('0' + d) : d;
	var h = date.getHours();
	h = h < 10 ? ('0' + h) : h;
	var minute = date.getMinutes();
	minute = minute < 10 ? ('0' + minute) : minute;
	var second = date.getSeconds();
	second = second < 10 ? ('0' + second) : second;
	return y + '-' + m + '-' + d + ' ' + h + ':' + minute; // 返回时：分
};


//时间格式化
var formatDateTime2 = function(date) {
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	m = m < 10 ? ('0' + m) : m.toString();
	var d = date.getDate();
	d = d < 10 ? ('0' + d) : d.toString();
	var h = date.getHours();
	h = h < 10 ? ('0' + h) : h.toString();
	var minute = date.getMinutes();
	minute = minute < 10 ? ('0' + minute) : minute.toString();
	var second = date.getSeconds();
	second = second < 10 ? ('0' + second) : second.toString();
	return y  + m + d  + h  + minute; // 返回时：分
};