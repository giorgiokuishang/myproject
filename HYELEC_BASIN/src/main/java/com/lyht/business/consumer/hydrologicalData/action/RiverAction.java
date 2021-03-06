package com.lyht.business.consumer.hydrologicalData.action;

import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.lyht.RetMessage;
import com.lyht.base.hibernate.common.PageResults;
import com.lyht.business.consumer.hydrologicalData.bean.River;
import com.lyht.business.consumer.hydrologicalData.control.RiverControl;
import com.lyht.business.consumer.hydrologicalData.formbean.RiverFormBean;
import com.lyht.business.consumer.hydrologicalData.service.RiverService;
import com.lyht.util.BaseLyhtAction;
import com.lyht.util.CommonFunction;
import com.lyht.util.CommonUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Namespace("/river")
@Controller
@Scope("prototype")
@SuppressWarnings("rawtypes")
public class RiverAction extends BaseLyhtAction {
	
	private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger("RiverAction");
	private RiverFormBean mRiverFormBean=new RiverFormBean();
	
	/**
	 * 上传文件域对象
	 */
	private File[] file;
	/**
	 * 上传文件名
	 */	
	private String[] fileFileName;
	
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	@Resource
	private RiverControl mRiverControl;
	@Resource
	private RiverService mRiverService;
	
	public String getStcd() {
		return stcd;
	}
	public void setStcd(String stcd) {
		this.stcd = stcd;
	}
	private String stcd;
	
	private String beginTime;
	
	private String endTime;
	
	private String interval;

	public String getInterval() {
		return interval;
	}
	public void setInterval(String interval) {
		this.interval = interval;
	}
	public String queryRiverEchart(){
		Hashtable<String,Object> table = new Hashtable<String,Object>();
		try {
			table = mRiverService.queryRiverEchartData(stcd,beginTime, endTime, interval);
			table.put("reflag", "1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			table.put("reflag", "0");
			table.put("message", "查询河道水情数据出错!");
		}
		CommonFunction.writeResponse(getResponse(), JSONObject.fromObject(table));
		return null;
	}
	/**
	 * 获取河道水情列表
	 * */
	public String list(){
		log.info("RiverAction=list:获取河道水情列表");
		HashMap<String, Object> mHashMap=new HashMap<String,Object>();
		RetMessage mRetMessage=new RetMessage();
		PageResults mPageResults=new PageResults();
		mRetMessage= mRiverControl.getRiverMes(mRiverFormBean, mPageResults);
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
	 * 初始化河道水情FORM表单数据
	 * */
	public String edit(){
		log.info("RiverAction=edit:初始化河道水情FORM表单数据");
		HashMap<String, Object> mHashMap = new HashMap<String, Object>();
		RetMessage mRetMessage=new RetMessage();
		River mRiver = new River();
		mRetMessage=mRiverControl.view(mRiverFormBean,mRiver);
		mHashMap.put("mRiverFormBean", mRiver);
		mHashMap.put(RetMessage.AJAX_RETFLAG, mRetMessage.getRetflag());
		mHashMap.put(RetMessage.AJAX_MESSAGE, mRetMessage.getMessage());		
		CommonFunction.writeResponse(getResponse(), mHashMap);
		return null;
	}
	
	/**
	 * 保存河道水情FORM表单数据
	 * */
	public String save(){
		log.info("RiverAction=save:保存河道水情FORM表单数据");
		HashMap<String, Object> mHashMap = new HashMap<String, Object>();
		RetMessage mRetMessage=new RetMessage();
		River mRiver = mRiverFormBean.getmRiverInfoBean();
		String nm = mRiver.getStcd()+mRiver.getTm().replace("-", "").replace(" ", "").replace(":", "");
		mRiver.setNm(nm);
		mRiverFormBean.setmRiverInfoBean(mRiver);
		mRetMessage=mRiverControl.update(mRiverFormBean.getmRiverInfoBean());
		mHashMap.put("mRiverFormBean", mRiverFormBean.getmRiverInfoBean());
		mHashMap.put(RetMessage.AJAX_RETFLAG, mRetMessage.getRetflag());
		mHashMap.put(RetMessage.AJAX_MESSAGE, mRetMessage.getMessage());		
		CommonFunction.writeResponse(getResponse(), mHashMap);
		return null;
	}
	
	/**
	 * 批量删除列表数据
	 * */
	public String removeids(){
		log.info("批量删除==RiverAction.removeids");
		HashMap<String, Object> mHashMap = new HashMap<String, Object>();
		RetMessage mRetMessage=new RetMessage();
		mRetMessage=mRiverControl.deletRiverInfoByIds(mRiverFormBean);
		mHashMap.put(RetMessage.AJAX_RETFLAG, mRetMessage.getRetflag());
		mHashMap.put(RetMessage.AJAX_MESSAGE, mRetMessage.getMessage());		
		CommonFunction.writeResponse(getResponse(), mHashMap);
		return null;
	}
	
	/**
	 * 导出河道水情
	 * */
	public String export(){
		log.info("RiverAction=export: ");
		PageResults prs = new PageResults();
		HttpServletRequest req = getRequest();
		HttpServletResponse res =getResponse();
		mRiverControl.export(mRiverFormBean,prs,req,res);
		return null;
	}
	
	/**
	 * 导入河道水情
	 */
	public String importPptn(){
		log.info("RiverAction=importPptn: 导入河道水情");
		RetMessage ret=new RetMessage();
		ret=mRiverControl.importPptn(getFile(),getFileFileName());
		CommonFunction.writeResponse(getResponse(), JSONObject.fromObject(ret));
		return null;
	}
	
	public RiverFormBean getmRiverFormBean() {
		return mRiverFormBean;
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
