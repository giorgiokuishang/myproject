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
import com.lyht.business.consumer.hydrologicalData.bean.Rsvr;
import com.lyht.business.consumer.hydrologicalData.control.RsvrControl;
import com.lyht.business.consumer.hydrologicalData.formbean.RsvrFormBean;
import com.lyht.business.consumer.hydrologicalData.service.RsvrService;
import com.lyht.util.BaseLyhtAction;
import com.lyht.util.CommonFunction;
import com.lyht.util.CommonUtil;

import net.sf.json.JSONArray;

@Namespace("/rsvr")
@Controller
@Scope("prototype")
@SuppressWarnings("rawtypes")
public class RsvrAction extends BaseLyhtAction {
	
	private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger("RsvrAction");
	private RsvrFormBean mRsvrFormBean=new RsvrFormBean();
	
	/**
	 * 上传文件域对象
	 */
	private File[] file;
	/**
	 * 上传文件名
	 */	
	private String[] fileFileName;
	@Resource
	private RsvrControl mRsvrControl;
	@Resource
	private RsvrService mRsvrService;

	/**
	 * 获取水库水情列表
	 * */
	public String list(){
		log.info("RsvrAction=list:获取水库水情列表");
		HashMap<String, Object> mHashMap=new HashMap<String,Object>();
		RetMessage mRetMessage=new RetMessage();
		PageResults mPageResults=new PageResults();
		mRetMessage= mRsvrControl.getRsvrMes(mRsvrFormBean, mPageResults);
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
	 * 初始化水库水情FORM表单数据
	 * */
	public String edit(){
		log.info("RsvrAction=edit:初始化水库水情FORM表单数据");
		HashMap<String, Object> mHashMap = new HashMap<String, Object>();
		RetMessage mRetMessage=new RetMessage();
		Rsvr mRsvr=new Rsvr();
		mRetMessage=mRsvrControl.view(mRsvrFormBean,mRsvr);
		mHashMap.put("mRsvrFormBean", mRsvr);
		mHashMap.put(RetMessage.AJAX_RETFLAG, mRetMessage.getRetflag());
		mHashMap.put(RetMessage.AJAX_MESSAGE, mRetMessage.getMessage());		
		CommonFunction.writeResponse(getResponse(), mHashMap);
		return null;
	}
	
	/**
	 * 保存水库水情FORM表单数据
	 * */
	public String save(){
		log.info("RsvrAction=save:保存水库水情FORM表单数据");
		HashMap<String, Object> mHashMap = new HashMap<String, Object>();
		RetMessage mRetMessage=new RetMessage();
		Rsvr mRsvr = new Rsvr();
		mRsvr=mRsvrService.getRsvrInfoById(mRsvrFormBean);
		if(CommonUtil.trim(mRsvr.getStcd()).length()<1){
			mRetMessage=mRsvrControl.create(mRsvrFormBean.getmRsvrInfoBean());
		}else{
			mRetMessage=mRsvrControl.update(mRsvrFormBean.getmRsvrInfoBean());
		}
		mHashMap.put("mRsvrFormBean", mRsvrFormBean.getmRsvrInfoBean());
		mHashMap.put(RetMessage.AJAX_RETFLAG, mRetMessage.getRetflag());
		mHashMap.put(RetMessage.AJAX_MESSAGE, mRetMessage.getMessage());		
		CommonFunction.writeResponse(getResponse(), mHashMap);
		return null;
	}
	
	/**
	 * 批量删除列表数据
	 * */
	public String removeids(){
		log.info("批量删除==RsvrAction.removeids");
		HashMap<String, Object> mHashMap = new HashMap<String, Object>();
		RetMessage mRetMessage=new RetMessage();
		mRetMessage=mRsvrControl.deletRsvrInfoByIds(mRsvrFormBean);
		mHashMap.put(RetMessage.AJAX_RETFLAG, mRetMessage.getRetflag());
		mHashMap.put(RetMessage.AJAX_MESSAGE, mRetMessage.getMessage());		
		CommonFunction.writeResponse(getResponse(), mHashMap);
		return null;
	}
	
	/**
	 * 导出水库水情
	 * */
	public String export(){
		log.info("RsvrAction=export: ");
		PageResults prs = new PageResults();
		HttpServletRequest req = getRequest();
		HttpServletResponse res =getResponse();
		mRsvrControl.export(mRsvrFormBean,prs,req,res);
		return null;
	}
	
	/**
	 * 导入水库水情
	 */
	public String importPptn(){
		log.info("RsvrAction=importPptn: 导入水库水情");
		RetMessage ret=new RetMessage();
		ret=mRsvrControl.importPptn(getFile(),getFileFileName());
		if (ret.getRetflag().equals(RetMessage.RETFLAG_ERROR)){
			CommonFunction.writeResponse(getResponse(), RetMessage.RETFLAG_ERROR);
		}else{
			CommonFunction.writeResponse(getResponse(), RetMessage.RETFLAG_SUCCESS);
		}
		return null;
	}
	
	public RsvrFormBean getmRsvrFormBean() {
		return mRsvrFormBean;
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
