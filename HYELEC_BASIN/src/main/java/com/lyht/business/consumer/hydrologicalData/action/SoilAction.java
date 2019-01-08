package com.lyht.business.consumer.hydrologicalData.action;

import java.io.File;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.lyht.RetMessage;
import com.lyht.base.hibernate.common.PageResults;
import com.lyht.business.consumer.hydrologicalData.bean.Soil;
import com.lyht.business.consumer.hydrologicalData.control.SoilControl;
import com.lyht.business.consumer.hydrologicalData.formbean.SoilFormBean;
import com.lyht.business.consumer.hydrologicalData.service.SoilService;
import com.lyht.util.BaseLyhtAction;
import com.lyht.util.CommonFunction;
import com.lyht.util.CommonUtil;

import net.sf.json.JSONArray;

@Namespace("/soil")
@Controller
@Scope("prototype")
@SuppressWarnings("rawtypes")
public class SoilAction extends BaseLyhtAction {
	
	private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger("SoilAction");
	private SoilFormBean mSoilFormBean=new SoilFormBean();
	
	/**
	 * 上传文件域对象
	 */
	private File[] file;
	/**
	 * 上传文件名
	 */	
	private String[] fileFileName;
	@Resource
	private SoilControl mSoilControl;
	@Resource
	private SoilService mSoilService;

	/**
	 * 获取土壤墒情列表
	 * */
	public String list(){
		log.info("SoilAction=list:获取土壤墒情列表");
		HashMap<String, Object> mHashMap=new HashMap<String,Object>();
		RetMessage mRetMessage=new RetMessage();
		PageResults mPageResults=new PageResults();
		mRetMessage= mSoilControl.getSoilMes(mSoilFormBean, mPageResults);
		if (mRetMessage.getRetflag().equals(RetMessage.RETFLAG_ERROR)){
			JSONArray mJSONArray = new JSONArray();
			mHashMap.put("total", 0);
			mHashMap.put("rows", mJSONArray);			
		} else {
			mHashMap.put("total", mPageResults.getTotalCount());
			mHashMap.put("rows", mPageResults.getResults());			
		}
		mHashMap.put(RetMessage.AJAX_RETFLAG, mRetMessage.getRetflag());
		mHashMap.put(RetMessage.AJAX_MESSAGE, mRetMessage.getMessage());
		CommonFunction.writeResponse(getResponse(), mHashMap);
		return null;
	}
	
	/**
	 * 初始化土壤墒情FORM表单数据
	 * */
	public String edit(){
		log.info("SoilAction=edit:初始化土壤墒情FORM表单数据");
		HashMap<String, Object> mHashMap = new HashMap<String, Object>();
		RetMessage mRetMessage=new RetMessage();
		Soil mSoil = new Soil();
		mRetMessage=mSoilControl.view(mSoilFormBean,mSoil);
		mHashMap.put("mSoilFormBean", mSoil);
		mHashMap.put(RetMessage.AJAX_RETFLAG, mRetMessage.getRetflag());
		mHashMap.put(RetMessage.AJAX_MESSAGE, mRetMessage.getMessage());		
		CommonFunction.writeResponse(getResponse(), mHashMap);
		return null;
	}
	
	/**
	 * 保存土壤墒情FORM表单数据
	 * */
	public String save(){
		log.info("SoilAction=save:保存土壤墒情FORM表单数据");
		HashMap<String, Object> mHashMap = new HashMap<String, Object>();
		RetMessage mRetMessage=new RetMessage();
		Soil mSoil = new Soil();
		mSoil=mSoilService.getSoilInfoById(mSoilFormBean);
		if(CommonUtil.trim(mSoil.getStcd()).length()<1
				&& CommonUtil.trim(mSoil.getExkey()).length()<1){
			mRetMessage=mSoilControl.create(mSoilFormBean.getmSoilInfoBean());
		}else{
			mRetMessage=mSoilControl.update(mSoilFormBean.getmSoilInfoBean());
		}
		mHashMap.put("mSoilFormBean", mSoilFormBean.getmSoilInfoBean());
		mHashMap.put(RetMessage.AJAX_RETFLAG, mRetMessage.getRetflag());
		mHashMap.put(RetMessage.AJAX_MESSAGE, mRetMessage.getMessage());		
		CommonFunction.writeResponse(getResponse(), mHashMap);
		return null;
	}
	
	/**
	 * 批量删除列表数据
	 * */
	public String removeids(){
		log.info("批量删除==SoilAction.removeids");
		HashMap<String, Object> mHashMap = new HashMap<String, Object>();
		RetMessage mRetMessage=new RetMessage();
		mRetMessage=mSoilControl.deletSoilInfoByIds(mSoilFormBean);
		mHashMap.put(RetMessage.AJAX_RETFLAG, mRetMessage.getRetflag());
		mHashMap.put(RetMessage.AJAX_MESSAGE, mRetMessage.getMessage());		
		CommonFunction.writeResponse(getResponse(), mHashMap);
		return null;
	}
	
	/**
	 * 导出土壤墒情
	 * */
	public String export(){
		log.info("SoilAction=export: ");
		PageResults prs = new PageResults();
		HttpServletRequest req = getRequest();
		HttpServletResponse res =getResponse();
		mSoilControl.export(mSoilFormBean,prs,req,res);
		return null;
	}
	
	/**
	 * 导入土壤墒情
	 */
	public String importPptn(){
		log.info("SoilAction=importPptn: 导入土壤墒情");
		RetMessage ret=new RetMessage();
		ret=mSoilControl.importPptn(getFile(),getFileFileName());
		if (ret.getRetflag().equals(RetMessage.RETFLAG_ERROR)){
			CommonFunction.writeResponse(getResponse(), RetMessage.RETFLAG_ERROR);
		}else{
			CommonFunction.writeResponse(getResponse(), RetMessage.RETFLAG_SUCCESS);
		}
		return null;
	}
	
	public SoilFormBean getmSoilFormBean() {
		return mSoilFormBean;
	}
	
	/**
	 * 上传文件域对象
	 */
	public File[] getFile() {
		return file;
	}
	public void setFile(File[] file) {
		this.file = file;
	}
	/**
	 * 上传文件名
	 */	
	public String[] getFileFileName() {
		return fileFileName;
	}
	public void setFileFileName(String[] fileFileName) {
		this.fileFileName = fileFileName;
	}
	
}
