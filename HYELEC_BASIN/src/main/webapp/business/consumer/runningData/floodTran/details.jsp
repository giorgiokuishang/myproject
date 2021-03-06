﻿<%@ page language="java" pageEncoding="UTF-8"%>

<form id="query_floodtran_form" name="info_form" class="form-inline"
	autocomplete="off" method="post"
	data-bv-message="This value is not valid"
	data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
	data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
	data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">

	<div class="modal fade" id="query_info_floodTran">
		<div class="modal-dialog " style="width: 900px">
			<div class="modal-content">
				<div class="modal-header"
					style="height: 40px">
					<button type="button" class="btn btn-link close"
						data-dismiss="modal">
						<i class="icon icon-times"></i><span class="sr-only">关闭</span>
					</button>
					<h4 class="modal-title" style="line-height: 15px;">
						<i class="icon icon-home"></i>&nbsp;&nbsp;洪水传播时间详细
					</h4>
				</div>
				<div class="modal-body">
					<table class="table table-bordered" style="margin-bottom: 0px;">
						<tr>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>上游站码：</lable>
							</td>
							<td style="width: 300px; text-align: left"><lable
								style="margin-left: 15px;" id="UPSTCD_pptnDetail">BM20180100</lable></td>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>下游站码：</lable>
							</td>
							<td style="width: 300px; text-align: left"><lable
								style="margin-left: 15px;" id="DWSTCD_pptnDetail">BM20180101</lable></td>
						</tr>
						<tr>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>河段长：</lable>
							</td>
							<td style="width: 300px; text-align: left"><lable
								style="margin-left: 15px;" id="RCHLEN_pptnDetail">120km</lable></td>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>安全泄量：</lable>
							</td>
							<td style="text-align: left"><lable
								style="margin-left: 15px;" id="SFTQ_pptnDetail">4000m³/s</lable></td>
						</tr>
						<tr>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>流量量级：</lable>
							</td>
							<td style="text-align: left"><lable
								style="margin-left: 15px;" id="QMGN_pptnDetail">III</lable></td>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>最小传播时间：</lable>
							</td>
							<td style="text-align: left"><lable
								style="margin-left: 15px;" id="MNTRTM_pptnDetail">4h</lable></td>
						</tr>
						<tr>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>最大传播时间：</lable>
							</td>
							<td style="text-align: left"><lable
								style="margin-left: 15px;" id="MXTRTM_pptnDetail">24h</lable></td>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>平均传播时间：</lable>
							</td>
							<td style="text-align: left"><lable
								style="margin-left: 15px;" id="AVTRTM_pptnDetail">11h</lable></td>
						</tr>
						<tr>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable>修改时间：</lable>
							</td>
							<td style="text-align: left"><lable
								style="margin-left: 15px;" id="MODITIME_pptnDetail"></lable></td>
							<td
								style="width: 100px; text-align: right; font-weight: bold; background: #f1f1f1">
								<lable></lable>
							</td>
							<td style="text-align: left"><lable
								style="margin-left: 15px;" id="CONTRACTDATE_detail"></lable></td>
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



