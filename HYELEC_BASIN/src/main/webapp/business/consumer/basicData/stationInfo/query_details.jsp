﻿<%@ page language="java" pageEncoding="UTF-8"%>

<form id="query_stationinfo_form" name="info_form" class="form-inline"
	autocomplete="off" method="post"
	data-bv-message="This value is not valid"
	data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
	data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
	data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">

	<div class="modal fade" id="query_info_show">
		<div class="modal-dialog " style="width: 850px">
			<div class="modal-content">
				<div class="modal-header"
					style="height: 40px">
					<button type="button" class="btn btn-link close"
						data-dismiss="modal">
						<i class="icon icon-times"></i><span class="sr-only">关闭</span>
					</button>
					<h4 class="modal-title" style="line-height: 15px;">
						<i class="icon icon-home"></i>&nbsp;&nbsp;测站信息详细
					</h4>
				</div>
				<div class="modal-body">
					<table class="table table-bordered" style="margin-bottom: 0px;">
						<tr>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>测站名称：</lable>
							</td>
							<td style="width: 700px; text-align: left" colspan="3"><lable
								style="margin-left: 15px;" id="STNM_detail">湘江流域第一测站</lable></td>
						</tr>
						<tr>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>测站编码：</lable>
							</td>
							<td style="width: 300px; text-align: left"><lable
								style="margin-left: 15px;" id="STCD_detail">BM20180101</lable></td>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>行政区划码：</lable>
							</td>
							<td style="width: 300px;text-align: left"><lable
								style="margin-left: 15px;" id="ADDVCD_detail">HN001089</lable></td>
						</tr>
						<tr>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>水系名称：</lable>
							</td>
							<td style="width: 300px; text-align: left"><lable
								style="margin-left: 15px;" id="HNNM_detail">洞庭湖水系</lable></td>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>河流名称：</lable>
							</td>
							<td style="text-align: left"><lable
								style="margin-left: 15px;" id="RVNM_detail">湘江</lable></td>
						</tr>
						<tr>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>流域名称：</lable>
							</td>
							<td style="text-align: left"><lable
								style="margin-left: 15px;" id="BSNM_detail">长江流域</lable></td>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>站址：</lable>
							</td>
							<td style="text-align: left"><lable
								style="margin-left: 15px;" id="STLC_detail">湖南省长沙市</lable></td>
						</tr>
						<tr>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>经度：</lable>
							</td>
							<td style="text-align: left"><lable
								style="margin-left: 15px;" id="LGTD1_detail">112 °58′42〃E </lable></td>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>纬度：</lable>
							</td>
							<td style="text-align: left"><lable
								style="margin-left: 15px;" id="LTTD1_detail">28 °11′49〃N</lable></td>
						</tr>
						<tr>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>基面名称：</lable>
							</td>
							<td style="text-align: left"><lable
								style="margin-left: 15px;" id="DTMNM_detail">120m²</lable></td>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>基面高程：</lable>
							</td>
							<td style="text-align: left"><lable
								style="margin-left: 15px;" id="DTMEL_detail">50m</lable></td>
						</tr>
						<tr>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>基面修正值：</lable>
							</td>
							<td style="text-align: left"><lable
								style="margin-left: 15px;" id="DTPR_detail">-0.01m</lable></td>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>集水面积：</lable>
							</td>
							<td style="text-align: left"><lable
								style="margin-left: 15px;" id="DRNA_detail">0.5m²</lable></td>
						</tr>
						<tr>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>建站年月：</lable>
							</td>
							<td style="text-align: left"><lable
								style="margin-left: 15px;" id="ESSTYM_detail">2011-12-11</lable></td>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>始报年月：</lable>
							</td>
							<td style="text-align: left"><lable
								style="margin-left: 15px;" id="BGFRYM_detail">2011-12-11</lable></td>
						</tr>
						<tr>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>隶属单位：</lable>
							</td>
							<td style="text-align: left"><lable
								style="margin-left: 15px;" id="ATCUNIT_detail">长沙市水文局</lable></td>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>管理单位：</lable>
							</td>
							<td style="text-align: left"><lable
								style="margin-left: 15px;" id="ADMAUTH_detail">长沙市水文局</lable></td>
						</tr>
						<tr>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>领导机关：</lable>
							</td>
							<td style="text-align: left"><lable
								style="margin-left: 15px;" id="LOCALITY_detail">湖南省水利局</lable></td>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>测站岸别：</lable>
							</td>
							<td style="text-align: left"><lable
								style="margin-left: 15px;" id="STBK_detail">左岸</lable></td>
						</tr>
						<tr>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>测站方位：</lable>
							</td>
							<td style="text-align: left"><lable
								style="margin-left: 15px;" id="STAZT_detail">北</lable></td>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>至河口距离：</lable>
							</td>
							<td style="text-align: left"><lable
								style="margin-left: 15px;" id="DSTRVM_detail">10km</lable></td>
						</tr>
						<tr>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>拼音码：</lable>
							</td>
							<td style="text-align: left"><lable
								style="margin-left: 15px;" id="PHCD_detail">pym</lable></td>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>田间持水量：</lable>
							</td>
							<td style="text-align: left"><lable
								style="margin-left: 15px;" id="FIELDCAP_detail">2.23%</lable></td>
						</tr>
					</table>
				</div>
				<div class="modal-footer"
					style="height: 40px;">
					<button type="button" class="btn btn-large btn-primary"
						data-dismiss="modal"
						style="margin-top: -14px; margin-right: -5px">
						<i class="icon icon-times"></i> 取消
					</button>
				</div>
			</div>
		</div>
	</div>


</form>



