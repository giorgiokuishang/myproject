package com.lyht.business.analysisCalculation.control;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.lyht.RetMessage;
import com.lyht.annotations.Optlog;
import com.lyht.base.hibernate.common.PageResults;
import com.lyht.business.analysisCalculation.bean.Result;
import com.lyht.business.analysisCalculation.bean.ResultFifth;
import com.lyht.business.analysisCalculation.bean.ResultFourth;
import com.lyht.business.analysisCalculation.formbean.ModelProgramFromBean;
import com.lyht.business.analysisCalculation.formbean.ResultFormBean;
import com.lyht.business.analysisCalculation.formbean.ResultThridFormBean;
import com.lyht.business.analysisCalculation.service.ResultFifthService;
import com.lyht.business.analysisCalculation.service.ResultService;
import com.lyht.business.consumer.hydrologicalData.bean.River;
import com.lyht.business.consumer.hydrologicalData.formbean.RiverFormBean;
import com.lyht.business.consumer.hydrologicalData.formbean.StbprpFormBean;
import com.lyht.business.consumer.hydrologicalData.formbean.TsqxFormBean;
import com.lyht.business.modelManage.formbean.ModelInfoFormBean;
import com.lyht.business.system.bean.SysMenu;
import com.lyht.business.system.bean.SysStaff;
/**
 *作者： 刘魁
 *脚本日期:2018年6月4日 15:24:11
 *说明:  产流计算结果Control 
*/
@Controller
@Scope("prototype")
public class ResultControl {
	@Resource
	private 	ResultService  resultService;
	@Resource
	private 	ResultFifthService  resultFifService;
	/**
	 * 保存
	 */
	@Optlog(menuflag="modelProaramCreate", opttype = "create")  
	public  RetMessage create(Result result,Result result1,String r,String INTERVAL,String stcds,String stnms,String start,String end) {
		RetMessage mRetMessage=new RetMessage();
		try {
			BeanUtils.copyProperties(result,resultService.saveResult(result,result1, r,INTERVAL,stcds,stnms,start,end));
			mRetMessage.setRetflag(RetMessage.RETFLAG_SUCCESS);
			mRetMessage.setMessage("新增信息成功！");
		}catch (Exception e) {
			mRetMessage.setRetflag(RetMessage.RETFLAG_ERROR);
			mRetMessage.setMessage(RetMessage.ERROR_SERVICE_MSG+"新增信息失败！");
		}
		return mRetMessage;
	}
	/**
	 * 导出到excel
	 */
	@Optlog(menuflag="ChanliuExport", opttype = "export") 
	public RetMessage export(ResultFormBean resultFormBean,PageResults prs,HttpServletRequest request,HttpServletResponse response){
		RetMessage mRetMessage=new RetMessage();
		try{
			resultService.export(resultFormBean,prs,request,response);
			mRetMessage.setRetflag(RetMessage.RETFLAG_SUCCESS);
			mRetMessage.setMessage("导出数据成功！");
		}catch (Exception e) {
			mRetMessage.setRetflag(RetMessage.RETFLAG_ERROR);
			mRetMessage.setMessage(RetMessage.ERROR_SERVICE_MSG+"导出数据失败！");
			e.printStackTrace();
		}
		return mRetMessage;
	}
	@Optlog(menuflag="ChanliuExport", opttype = "export") 
	public RetMessage export2(ResultFormBean resultFormBean,String s,String end,PageResults prs,HttpServletRequest request,HttpServletResponse response){
		RetMessage mRetMessage=new RetMessage();
		try{
			resultService.export2(resultFormBean,s,end,prs,request,response);
			mRetMessage.setRetflag(RetMessage.RETFLAG_SUCCESS);
			mRetMessage.setMessage("导出数据成功！");
		}catch (Exception e) {
			mRetMessage.setRetflag(RetMessage.RETFLAG_ERROR);
			mRetMessage.setMessage(RetMessage.ERROR_SERVICE_MSG+"导出数据失败！");
			e.printStackTrace();
		}
		return mRetMessage;
	}
	@Optlog(menuflag="ChanliuExport", opttype = "export") 
	public RetMessage export3(ResultThridFormBean resultThrid,String hj,String rs,String s,String end,PageResults prs,HttpServletRequest request,HttpServletResponse response){
		RetMessage mRetMessage=new RetMessage();
		try{
			resultService.export3(resultThrid,hj,rs,s,end,prs,request,response);
			mRetMessage.setRetflag(RetMessage.RETFLAG_SUCCESS);
			mRetMessage.setMessage("导出数据成功！");
		}catch (Exception e) {
			mRetMessage.setRetflag(RetMessage.RETFLAG_ERROR);
			mRetMessage.setMessage(RetMessage.ERROR_SERVICE_MSG+"导出数据失败！");
			e.printStackTrace();
		}
		return mRetMessage;
	}
	
	@Optlog(menuflag="ChanliuExport", opttype = "export") 
	public RetMessage export4(ResultFourth rFourth,String hj,String rs,String s,String end,PageResults prs,HttpServletRequest request,HttpServletResponse response){
		RetMessage mRetMessage=new RetMessage();
		try{
			resultService.export4(rFourth,hj,rs,s,end,prs,request,response);
			mRetMessage.setRetflag(RetMessage.RETFLAG_SUCCESS);
			mRetMessage.setMessage("导出数据成功！");
		}catch (Exception e) {
			mRetMessage.setRetflag(RetMessage.RETFLAG_ERROR);
			mRetMessage.setMessage(RetMessage.ERROR_SERVICE_MSG+"导出数据失败！");
			e.printStackTrace();
		}
		return mRetMessage;
	}
	@Optlog(menuflag="ChanliuExport", opttype = "export") 
	public RetMessage export6(Map map,Map map1,String stcd,String pch,String start,String end,PageResults prs,HttpServletRequest request,HttpServletResponse response){
		RetMessage mRetMessage=new RetMessage();
		try{
			resultService.export6(map,map1,stcd,pch,start,end,prs,request,response);
			mRetMessage.setRetflag(RetMessage.RETFLAG_SUCCESS);
			mRetMessage.setMessage("导出数据成功！");
		}catch (Exception e) {
			mRetMessage.setRetflag(RetMessage.RETFLAG_ERROR);
			mRetMessage.setMessage(RetMessage.ERROR_SERVICE_MSG+"导出数据失败！");
			e.printStackTrace();
		}
		return mRetMessage;
	}
	
	@Optlog(menuflag="ChanliuExport", opttype = "export") 
	public RetMessage export7(String stcd,String pch,String start,String end,PageResults prs,HttpServletRequest request,HttpServletResponse response){
		RetMessage mRetMessage=new RetMessage();
		try{
			resultService.export7(stcd,pch,start,end,prs,request,response);
			mRetMessage.setRetflag(RetMessage.RETFLAG_SUCCESS);
			mRetMessage.setMessage("导出数据成功！");
		}catch (Exception e) {
			mRetMessage.setRetflag(RetMessage.RETFLAG_ERROR);
			mRetMessage.setMessage(RetMessage.ERROR_SERVICE_MSG+"导出数据失败！");
			e.printStackTrace();
		}
		return mRetMessage;
	}
	/**
	 * 计算结果查询List
	 */
	@Optlog(menuflag="resultList", opttype = "getResultMes") 
	public RetMessage getResult(ResultFormBean resultFormBean,PageResults mPageResults) {
		RetMessage mRetMessage=new RetMessage();
		try {
			BeanUtils.copyProperties(mPageResults,resultService.getSelect(resultFormBean));
			mRetMessage.setRetflag(RetMessage.RETFLAG_SUCCESS);
			mRetMessage.setMessage("查询数据成功！");
		} catch (Exception e) {
			mRetMessage.setRetflag(RetMessage.RETFLAG_ERROR);
			mRetMessage.setMessage(RetMessage.ERROR_SERVICE_MSG+"查询数据失败！");
		}
		return mRetMessage;
	}
	
	
	/**
	 * 计算结果查询List
	 */
	@Optlog(menuflag="getResultList", opttype = "getResultMes") 
	public RetMessage getHistory(ResultFormBean resultFormBean,PageResults mPageResults) {
		RetMessage mRetMessage=new RetMessage();
		try {
			BeanUtils.copyProperties(mPageResults,resultService.getHistory(resultFormBean));
			mRetMessage.setRetflag(RetMessage.RETFLAG_SUCCESS);
			mRetMessage.setMessage("查询数据成功！");
		} catch (Exception e) {
			mRetMessage.setRetflag(RetMessage.RETFLAG_ERROR);
			mRetMessage.setMessage(RetMessage.ERROR_SERVICE_MSG+"查询数据失败！");
		}
		return mRetMessage;
	}
	
	/**
	 * 第五步计算结果查询List
	 */
	@Optlog(menuflag="getResultList", opttype = "getResultMes") 
	public RetMessage getFif(ResultFifth rf ,PageResults mPageResults) {
		RetMessage mRetMessage=new RetMessage();
		try {
			BeanUtils.copyProperties(mPageResults,resultFifService.getFif(rf));
			mRetMessage.setRetflag(RetMessage.RETFLAG_SUCCESS);
			mRetMessage.setMessage("查询数据成功！");
		} catch (Exception e) {
			mRetMessage.setRetflag(RetMessage.RETFLAG_ERROR);
			mRetMessage.setMessage(RetMessage.ERROR_SERVICE_MSG+"查询数据失败！");
		}
		return mRetMessage;
	}
	/**
	 * 步骤2
	 * @param resultFormBean
	 * @param mPageResults
	 * @return
	 */
	@Optlog(menuflag="getResultStep2List", opttype = "getResultMes") 
	public RetMessage step2(ResultFormBean resultFormBean,String start,String end,PageResults mPageResults) {
		RetMessage mRetMessage=new RetMessage();
		try {
			BeanUtils.copyProperties(mPageResults,resultService.step2(resultFormBean,start,end));
			mRetMessage.setRetflag(RetMessage.RETFLAG_SUCCESS);
			mRetMessage.setMessage("查询数据成功！");
		} catch (Exception e) {
			mRetMessage.setRetflag(RetMessage.RETFLAG_ERROR);
			mRetMessage.setMessage(RetMessage.ERROR_SERVICE_MSG+"查询数据失败！");
		}
		return mRetMessage;
	}
	
	/**
	 * 获得流量
	 * @param resultFormBean
	 * @param start
	 * @param end
	 * @param mPageResults
	 * @return
	 */
	@Optlog(menuflag="getliuliangList", opttype = "getResultMes") 
	public RetMessage getLiuliang(ResultFormBean resultFormBean,String start,String end,PageResults mPageResults) {
		RetMessage mRetMessage=new RetMessage();
		try {
			BeanUtils.copyProperties(mPageResults,resultService.getLiuLiang(resultFormBean, start, end));
			mRetMessage.setRetflag(RetMessage.RETFLAG_SUCCESS);
			mRetMessage.setMessage("查询数据成功！");
		} catch (Exception e) {
			mRetMessage.setRetflag(RetMessage.RETFLAG_ERROR);
			mRetMessage.setMessage(RetMessage.ERROR_SERVICE_MSG+"查询数据失败！");
		}
		return mRetMessage;
	}
	
	
	/**
	 * 获得降雨量
	 * @param resultFormBean
	 * @param start
	 * @param end
	 * @param mPageResults
	 * @return
	 */
	@Optlog(menuflag="getymlList", opttype = "getResultMes") 
	public RetMessage getyml(ResultFormBean resultFormBean,String start,String end,PageResults mPageResults) {
		RetMessage mRetMessage=new RetMessage();
		try {
			BeanUtils.copyProperties(mPageResults,resultService.getyml(resultFormBean, start, end));
			mRetMessage.setRetflag(RetMessage.RETFLAG_SUCCESS);
			mRetMessage.setMessage("查询数据成功！");
		} catch (Exception e) {
			mRetMessage.setRetflag(RetMessage.RETFLAG_ERROR);
			mRetMessage.setMessage(RetMessage.ERROR_SERVICE_MSG+"查询数据失败！");
		}
		return mRetMessage;
	}
	
	/**
	 * 修改table中的降雨量
	 */
	@Optlog(menuflag="updateTableData",opttype = "update")  
	public RetMessage updateTableData(ResultFormBean resultFormBean,RiverFormBean mRiverFormBean) {
		RetMessage mRetMessage=new RetMessage();
		try {
			resultService.updateTableData(resultFormBean,mRiverFormBean);
			mRetMessage.setRetflag(RetMessage.RETFLAG_SUCCESS);
			mRetMessage.setMessage("修改信息成功！");
		} catch (Exception e) {
			mRetMessage.setRetflag(RetMessage.RETFLAG_ERROR);
			mRetMessage.setMessage(RetMessage.ERROR_SERVICE_MSG+"修改信息失败！");
		}
		return mRetMessage;
	}
	
	@Optlog(menuflag="getTsqx", opttype = "getTsqxMes") 
	public RetMessage getTsqx(PageResults mPageResults,ResultFormBean resultFormBean,TsqxFormBean mTsqxFormBean){
		RetMessage mRetMessage=new RetMessage();
		try {
			BeanUtils.copyProperties(mPageResults,resultService.getTsqx( resultFormBean, mTsqxFormBean));
			mRetMessage.setRetflag(RetMessage.RETFLAG_SUCCESS);
			mRetMessage.setMessage("查询数据成功！");
		} catch (Exception e) {
			mRetMessage.setRetflag(RetMessage.RETFLAG_ERROR);
			mRetMessage.setMessage(RetMessage.ERROR_SERVICE_MSG+"查询数据失败！");
		}
		return mRetMessage;
	}
	//步骤6
	@Optlog(menuflag="getResultStep6List", opttype = "getResultMes") 
	public RetMessage step6(ResultFormBean resultFormBean,String start,String end,PageResults mPageResults) {
		RetMessage mRetMessage=new RetMessage();
		try {
			BeanUtils.copyProperties(mPageResults,resultService.getStep6(resultFormBean, start, end));
			mRetMessage.setRetflag(RetMessage.RETFLAG_SUCCESS);
			mRetMessage.setMessage("查询数据成功！");
		} catch (Exception e) {
			mRetMessage.setRetflag(RetMessage.RETFLAG_ERROR);
			mRetMessage.setMessage(RetMessage.ERROR_SERVICE_MSG+"查询数据失败！");
		}
		return mRetMessage;
	}
}
