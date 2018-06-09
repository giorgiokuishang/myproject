<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<title>资料管理</title>
<%@include file="/common/inc/inc.inc"%>
<%@include file="/common/inc/bootstrapTable.inc"%>
<style type="text/css">
.clgl-13{ width:100%; margin:0 auto;}
.clgl-14{ float:left; width:20%;  padding-top:20px; line-height:35px;  text-align:center; }
.clgl-16{ float:left; width:80%;  padding-top:20px; line-height:35px; text-align:center; }
</style>
</head>
<script src="http://echarts.baidu.com/build/dist/echarts-all.js"></script>
<body style="background-color: #FCFCFC;">
<div class="container-fluid">
		<%@include file="/common/inc/top.jsp"%>
		<div class="row-fluid">
			<h3 class="text-primary">
				<ol class="breadcrumb">
					<li>分析计算</li>
                    <li style="color:black;">产流计算</li>
				</ol>
			</h3>
		</div>
		<hr style="margin-top: -5px;">
			
		<!-- 	<h1 style="text-align: center;">产流计算</h1> -->
	 <form id="info_form_cjfa" name="" method="post" >
	    <div id="base" class="">
	    	请输入站名:<input type="text" id="stnm" name="mStbprpFormBean.mStbprpInfoBean.stnm" value="">
	    	<input type="button" name="" value="查询" onclick="getStp()">
	    	<input type="submit" name="" value="保存计算结果" onclick="save()">
	    	<input type="hidden"  id="stcd" name="stcd">
	    	站名： <font color="green " style="display: none;" id="zhanming"></font>
	    <font color="red " style="display: none;" id="m">提示:测站名称不存在</font> 
	    </div>	
	    <div class="row-fluid">
		    <div class="col-md-3"> 
		   		雨量站:<input type="text" name="" value=""   id="cols"><input type="button" value="确定" onclick="autocreate()">
		   		<input type="button" value="计算" onclick="jisuan()">
		   		</div>
		   	<div class="col-md-3 "> 
		   				<div class="input-append date form_datetime">
									时间区间:<input size="16" id="start"  type="text" readonly>
									    <span class="add-on"><i class="icon-th"></i></span>
						</div>
						</div>
						<div class="col-md-2 ">
							<div class="input-append date form_datetime">
									    	--  <input size="16"  id="end" type="text"  readonly>
									    <span class="add-on"><i class="icon-th"></i></span>
						</div>
			</div>
		   	<div class="col-md-4">
		   		时间间隔:<input type="text"  id="jiange" name="" value="60">(时间精确到分钟)
		   	</div>
		    </div>
  
   <br> <br> <br>
    <!-- <div id="test"></div>   -->
  <table border="1" id="table1">
    		<tr id="tr1">
    			<td rowspan="2" align="center" >序号</td> 
    			<td rowspan="2" align="center" >日期</td> 
    			<td colspan="2" align="center" id="td1" class="addTd"><input type="text" value=""></td>   
                <td rowspan="2" align="center">雨面量</td> 
    		</tr >
    		<tr align="center" id="tr2">
    			<td>权重</td>
    			<td id="td2" class="addTd2" align="center"><input type="text" align="center" name="quanzhong" id="quanzhong"></td>     
    		</tr>
    		<tr align="center" id="tr3">
    			<td id="xuhao1">1</td>
    			<td><input type="text" name="time" ></td>
    			<td colspan="2" id="td4" class="addTd4" align="center"><input type="text" name="yu" id="yu0"  align="center"></td>   	
                <td colspan="2" align="center"><input type="text" name="result"   align="center"></td>               
    		</tr> 
    	</table>
        </div>
  </form>
</body>
</html>
<!-- 不要改变以下引用顺序 -->
<script src="chanliu.js"></script>
<%-- <script src="jyjlysjs/query.js"></script>
<script src="tlflfxmcl/query.js"></script>
<script src="jyjl/query.js"></script>
<script src="wdrl/query.js"></script>
<script src="cc/query.js"></script> --%>
<script type="text/javascript">
$(".form_datetime").datetimepicker({
    format: " yyyy-mm-dd hh:ii:ss"
});
</script>
