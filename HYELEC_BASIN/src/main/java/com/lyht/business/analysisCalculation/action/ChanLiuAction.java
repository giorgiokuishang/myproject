package com.lyht.business.analysisCalculation.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.lyht.Constants;
import com.lyht.RetMessage;
import com.lyht.base.hibernate.common.PageResults;
import com.lyht.business.analysisCalculation.bean.Result;
import com.lyht.business.analysisCalculation.bean.ResultFourth;
import com.lyht.business.analysisCalculation.bean.ResultThrid;
import com.lyht.business.analysisCalculation.control.ResultControl;
import com.lyht.business.analysisCalculation.control.ResultFourthControl;
import com.lyht.business.analysisCalculation.control.ResultThridControl;
import com.lyht.business.analysisCalculation.formbean.ResultFormBean;
import com.lyht.business.analysisCalculation.formbean.ResultFourthFormBean;
import com.lyht.business.analysisCalculation.formbean.ResultThridFormBean;
import com.lyht.business.analysisCalculation.service.ResultFourthService;
import com.lyht.business.analysisCalculation.service.ResultService;
import com.lyht.business.analysisCalculation.service.ResultThridService;
import com.lyht.business.consumer.hydrologicalData.bean.Pptn;
import com.lyht.business.consumer.hydrologicalData.bean.River;
import com.lyht.business.consumer.hydrologicalData.bean.Tsqx;
import com.lyht.business.consumer.hydrologicalData.control.PptnControl;
import com.lyht.business.consumer.hydrologicalData.control.StbprpControl;
import com.lyht.business.consumer.hydrologicalData.formbean.PptnFormBean;
import com.lyht.business.consumer.hydrologicalData.formbean.RiverFormBean;
import com.lyht.business.consumer.hydrologicalData.formbean.StbprpFormBean;
import com.lyht.business.consumer.hydrologicalData.formbean.TsqxFormBean;
import com.lyht.business.consumer.hydrologicalData.service.RiverService;
import com.lyht.business.consumer.hydrologicalData.service.StbprpService;
import com.lyht.business.system.bean.SysStaff;
import com.lyht.util.BaseLyhtAction;
import com.lyht.util.CommonFunction;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 产流计算action
 * @author 刘魁
 * 时间:2018.5.30
 */
@Namespace("/chanliu")
@Controller
@Scope("prototype")
@SuppressWarnings("rawtypes")
@Action("/chanliu")
public class ChanLiuAction extends BaseLyhtAction{

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger("ChanLiuAction");
	private PptnFormBean mPptnFormBean=new PptnFormBean();//降水量
	private StbprpFormBean mStbprpFormBean=new StbprpFormBean();//测站
	private ResultFormBean resultFormBean=new ResultFormBean();//计算结果
	@Resource
	private StbprpControl mStbprpControl;//测站
	@Resource
	private ResultControl resultControl;//计算结果
	@Resource
	private PptnControl mPptnControl;//降水量
	@Resource  
	private ResultThridControl  resultThridControl;//第三步的control
	@Resource  
	private ResultFourthControl resultFourthControl;//第四步的control
	
	@Resource private StbprpService mStbprpService; //测站
	@Resource private  ResultService resultService;//产流计算结果
	@Resource private RiverService riverService;//河道水清
	@Resource private ResultThridService resultThridService;//第三步
	@Resource private ResultFourthService resultFourthService; //第四步
	private String quanzhong; //权重
	private String yu;//降雨量
	private String time;//时间
	private String result;//雨面量
	private String czmc;//测站名称
	private String pch; //批次号 时间年月日
	private String stcd; //测站编码
	private String start; //开始时间
	private String end;   //结束时间
	private String beginDate;
	private String endDate;
	private String  STCD;
	private String DATE;
	private Integer JYL; //降雨量
	private double LL;//流量
	
	//第三步传过来的数据
	private String BEGINDATE;//开始时间
	private String ENDDATE;//结束时间
	private String INTERVAL;//时差
	private String   DATA;//数据
	/**
	 * 根据测站名称查询测站信息
	 * @return
	 */
	public String getStbprp() {
		log.info("ChanLiuAction=list:根据测站名称查询测站信息");
		HashMap<String, Object> mHashMap=new HashMap<String,Object>();
		RetMessage mRetMessage=new RetMessage();
		PageResults mPageResults=new PageResults();
		mRetMessage= mStbprpControl.getStbprpMes(mStbprpFormBean, mPageResults);
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
	 * 模糊查询
	 * @return
	 */
	public String getStbprpMoHu() {
		log.info("ChanLiuAction=list:根据测站名称模糊查询测站信息");
		HashMap<String, Object> mHashMap=new HashMap<String,Object>();
		RetMessage mRetMessage=new RetMessage();
		PageResults mPageResults=new PageResults();
		mRetMessage= mStbprpControl.getStbprpMesMoHu(mStbprpFormBean, mPageResults);
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
	 * 保存计算结果
	 * @return
	 */
	public String save() {
		log.info("ChanLiuAction=save:保存计算结果");
		HashMap<String, Object> mHashMap=new HashMap<String,Object>();
		RetMessage mRetMessage=new RetMessage();
		Result cresult=new Result();
		Result result1=new Result();
		SysStaff  mSysStaff = (SysStaff) getSession().getAttribute(Constants.SESSION_STAFF);
		cresult.setYmlTime(time);
		cresult.setPch(pch);
		cresult.setJyl(yu);
		cresult.setStnm(czmc);
		cresult.setQz(quanzhong);
		cresult.setCreateStaff(mSysStaff.getStaffCode());//存入当前用户code
		mRetMessage=resultControl.create(cresult, result1,result);
		mHashMap.put(RetMessage.AJAX_RETFLAG, mRetMessage.getRetflag());
		mHashMap.put(RetMessage.AJAX_MESSAGE, mRetMessage.getMessage());		
		CommonFunction.writeResponse(getResponse(), mHashMap);
		return null;
	}
	
	
	/**
	 * 获取降水量
	 * @return
	 */
	public String list(){
		log.info("ChanLiuAction=list: 根据测站名称获取降水量列表");
		HashMap<String, Object> mHashMap=new HashMap<String,Object>();
		RetMessage mRetMessage=new RetMessage();
		PageResults mPageResults=new PageResults();
		mRetMessage= mStbprpControl.getStbprpMes(mStbprpFormBean, mPageResults);
		mPageResults.getResults();
		String stcd=mStbprpFormBean.getmStbprpInfoBean().getStcd();
		Pptn pptn=new Pptn();
		pptn.setStcd(stcd);
		mPptnFormBean.setmPptnInfoBean(pptn);  
		mRetMessage= mPptnControl.getPptnMes(mPptnFormBean, mPageResults); 	//查
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
	 * 获取计算结果历史记录，数据到前台select
	 * @return
	 */
	public String getSelect() {
		log.info("ChanLiuAction=list: 获取计算结果历史记录");
		HashMap<String, Object> mHashMap=new HashMap<String,Object>();
		RetMessage mRetMessage=new RetMessage();
		PageResults mPageResults=new PageResults();
		mRetMessage= resultControl.getResult(resultFormBean,mPageResults);
		if (mRetMessage.getRetflag().equals(RetMessage.RETFLAG_ERROR)){
			JSONArray mJSONArray = new JSONArray();
			mHashMap.put("total", 0);
			mHashMap.put("rows", mJSONArray);	
		}else {
			mHashMap.put("total", mPageResults.getTotalCount());
			mHashMap.put("rows", mPageResults.getResults());	
		}
		mHashMap.put(RetMessage.AJAX_RETFLAG, mRetMessage.getRetflag());
		mHashMap.put(RetMessage.AJAX_MESSAGE, mRetMessage.getMessage());
		CommonFunction.writeResponse(getResponse(), mHashMap);
		return null;
	}
	
	
	/**
	 * 根据选择的历史记录查找相应的计算结果信息
	 * @return
	 */
	public String getHistory() {
		log.info("ChanLiuAction=list: 根据计算结果编号查询计算数据");
		HashMap<String, Object> mHashMap=new HashMap<String,Object>();
		RetMessage mRetMessage=new RetMessage();
		PageResults mPageResults=new PageResults();
		resultFormBean.getResultBean().setPch(pch);//设置批次号
		mRetMessage= resultControl.getHistory(resultFormBean,mPageResults);
		if (mRetMessage.getRetflag().equals(RetMessage.RETFLAG_ERROR)){
			JSONArray mJSONArray = new JSONArray();
			mHashMap.put("total", 0);
			mHashMap.put("rows", mJSONArray);	
		}else {
			mHashMap.put("total", mPageResults.getTotalCount());
			mHashMap.put("rows", mPageResults.getResults());	
		}
		mHashMap.put(RetMessage.AJAX_RETFLAG, mRetMessage.getRetflag());
		mHashMap.put(RetMessage.AJAX_MESSAGE, mRetMessage.getMessage());
		CommonFunction.writeResponse(getResponse(), mHashMap);
		return null;
	}
	
	/**
	 * 步骤2提供json1入口
	 * @return
	 */
	public String step2() {
		log.info("ChanLiuAction=list: 步骤一.2 Table,json1的数据来源");
		HashMap<String, Object> mHashMap=new HashMap<String,Object>();
		RetMessage mRetMessage=new RetMessage();
		PageResults mPageResults=new PageResults();
		mHashMap.put("CODE", "1");
		mHashMap.put("MESSAGE", "0");
		mHashMap.put("PAGESIZE", 0);
		mHashMap.put("PAGEINDEX", 0);
		mHashMap.put("TOTALAMOUNT", 0);
		mRetMessage= resultControl.step2(resultFormBean,start,end,mPageResults);//第二步需要的sql查询方法
		List listdata=mPageResults.getResults();
		mHashMap.put("DATA", listdata);//给DATA添加数据
		List<HashMap<String, Object>>list =new ArrayList<HashMap<String, Object>>();//EXTRADATA
		HashMap<String, Object> listMap=new HashMap<String, Object>();	
		listMap.put("STNM", "站名");
		listMap.put("PCH", "洪号");
		listMap.put("LYMJ", "流域面积");
		list.add(listMap);//添加数据
		mHashMap.put("EXTRADATA", list);
		JSONObject json = JSONObject.fromObject(mHashMap);
		CommonFunction.writeResponse(getResponse(), json);
		//getResponse().setContentType("json;charset=utf-8");
		return null;
	}
	
	
	/**
	 * JSON2入口 testsyq_1-n.json Echarts图数据来源
	 * @return
	 */
	public String step2chart() {
		log.info("ChanLiuAction=list:步骤一.2 Echarts, json2的数据来源");
		HashMap<String, Object> mHashMap=new HashMap<String,Object>();
		RetMessage mRetMessage=new RetMessage();
		PageResults mPageResults=new PageResults();
		mHashMap.put("CODE", "1");
		mHashMap.put("MESSAGE", "0");
		mHashMap.put("PAGESIZE", 0);
		mHashMap.put("PAGEINDEX", 0);
		mHashMap.put("TOTALAMOUNT", 2);
		List<HashMap<String, Object>>dataList =new ArrayList<HashMap<String, Object>>();//DATA
		HashMap<String, Object> map=new HashMap<String, Object>();// DATA 
		map.put("STCD", "");
		map.put("STNM", "");
		map.put("NAME", "流量");
		map.put("TYPE", "line");
		List list=resultService.maxLL();//流量最大值查询接口
		HashMap<String, Object> lmax= (HashMap<String, Object>) list.get(0);
		Object max=  lmax.get("LL");
		int llmax=Integer.parseInt(max.toString())*2;//最大值*2
		map.put("MAX",	llmax);//流量的最大值
		dataList.add(map);
		mRetMessage= resultControl.getLiuliang(resultFormBean, start, end, mPageResults);//流量
		List<HashMap<String, Object>> liuLiangList=	mPageResults.getResults();//获得流量的list去遍历封装数据
		HashMap<String, Object> liuLiangmap=new HashMap<String, Object>();
		List<Object[]> newList=new ArrayList<Object[]>();//流量数据的list
		for(int i=0;i<liuLiangList.size();i++) {
			liuLiangmap.put((String) liuLiangList.get(i).get("DATE"), liuLiangList.get(i).get("LL"));//这个map键是时间，值对应的就是流量
			Object [] ma=new Object [2];
			ma[0]=liuLiangList.get(i).get("DATE");//时间
			ma[1]=liuLiangList.get(i).get("LL");//流量
			newList.add(ma);//添加数据
		}
		 Object[] array =newList.toArray();
		map.put("DATA", array); //流量
		HashMap<String, Object> jiangYuMap=new HashMap<String, Object>();//降雨量
		jiangYuMap.put("STCD", "");
		jiangYuMap.put("STNM", "");
		jiangYuMap.put("NAME", "降雨量");
		jiangYuMap.put("TYPE", "bar");
		List ymllist=resultService.maxYml();
		HashMap<String, Object> ymllistmax= (HashMap<String, Object>) ymllist.get(0);
		Object JYL=  ymllistmax.get("JYL");
		int maxJyl=Integer.parseInt(JYL.toString())*2;
		jiangYuMap.put("MAX", maxJyl);//降雨量的最大值
		mRetMessage= resultControl.getyml(resultFormBean, start, end, mPageResults);//降雨量
		List<HashMap<String, Object>> jList=	mPageResults.getResults();
		HashMap<String, Object> jyLmap=new HashMap<String, Object>();
		List<Object[]> newList1=new ArrayList<Object[]>();
		for(int i=0;i<jList.size();i++) {
			jyLmap.put((String) jList.get(i).get("DATE"), jList.get(i).get("JYL"));
			Object [] jy=new Object [2];
			jy[0]=jList.get(i).get("DATE");//时间
			jy[1]=jList.get(i).get("JYL");//降雨量
			newList1.add(jy);
		}
		//JSONObject json2 = JSONObject.fromObject(jyLmap);
		 Object[]  strArray = newList1.toArray();
		jiangYuMap.put("DATA", strArray);//降雨量
		dataList.add(jiangYuMap);
		mHashMap.put("DATA", dataList); //给DATA数据
		//结尾数据
		List<HashMap<String, Object>>exta =new ArrayList<HashMap<String, Object>>();//EXTRADATA
		HashMap<String, Object> map1=new HashMap<String, Object>();//EXTRADATA map
		map1.put("START", "2018-06-05 00:00");
		map1.put("END", "2018-06-06 00:00");
		map1.put("CHARTTITLE", "降雨量流量关系图");
		exta.add(map1);//添加数据
		mHashMap.put("EXTRADATA", exta);
		JSONObject json = JSONObject.fromObject(mHashMap);
		CommonFunction.writeResponse(getResponse(), json);
		return null;
	}
	
	/**
	 * 步骤2修改table数据
	 * @return
	 */
	public String updateData() {
		log.info("ChanLiuAction=update:步骤一.2修改table数据");
		HashMap<String, Object> mHashMap = new HashMap<String, Object>();
		RetMessage mRetMessage=new RetMessage();
		RiverFormBean rb=new RiverFormBean();
		resultFormBean.getResultBean().setYml(JYL);
		resultFormBean.getResultBean().setYmlTime(DATE);
		rb.getmRiverInfoBean().setQ(LL);
		rb.getmRiverInfoBean().setStcd(STCD);
		rb.getmRiverInfoBean().setTm(DATE);
		mRetMessage= resultControl.updateTableData(resultFormBean,rb);//修改方法
		mHashMap.put(RetMessage.AJAX_RETFLAG, mRetMessage.getRetflag());
		mHashMap.put(RetMessage.AJAX_MESSAGE, mRetMessage.getMessage());		
		CommonFunction.writeResponse(getResponse(), mHashMap);
		return null;
	}
	
	/**
	 * 步骤一.3退水曲线json
	 * @return
	 */
	public String  getTsqx() {
		log.info("ChanLiuAction=getTsqx:获取退水曲线");
		HashMap<String, Object> mHashMap = new HashMap<String, Object>();
		RetMessage mRetMessage=new RetMessage();
		PageResults mPageResults=new PageResults();
		TsqxFormBean mTsqxFormBean=new TsqxFormBean ();
		resultFormBean.getResultBean().setStcd(stcd);//测站编码
		//获取退水曲线的数据
		mHashMap.put("CODE", "1");
		mHashMap.put("MESSAGE", "0");
		mHashMap.put("PAGESIZE", 0);
		mHashMap.put("PAGEINDEX", 0);
		mHashMap.put("TOTALAMOUNT", 2);
		List<HashMap<String, Object>> newList1=new ArrayList<HashMap<String, Object>> ();//DATA
		List<HashMap<String, Object>> list=resultService.numHongFeng(resultFormBean);//得到洪峰的值，去重
		mRetMessage=resultControl.getTsqx(mPageResults, resultFormBean, mTsqxFormBean);//查询退水曲线
		List<HashMap<String, Object>> list2=mPageResults.getResults();//退水曲线结果list
		for(int i=0;i<list.size();i++) {//退水曲线的洪峰，遍历之后去结果去匹配
			HashMap< String, Object> dataMap=new HashMap< String, Object>();
			dataMap.put("NAME", list.get(i).get("QM"));
			dataMap.put("MAX","25");
			dataMap.put("TYPE", "line");
			Double qm=	(Double) list.get(i).get("QM"); //得到洪峰
			List list3=new ArrayList();
			for(int j=0;j<list2.size();j++) {
				Double list2qm= (Double) list2.get(j).get("QM");
				if(qm.equals(list2qm)) {//如果洪峰相等 那么就添加相应的流量和时间段到集合里，这个集合是json数组的数据来源
					Object [] data=new Object [2];
					data[0]=list2.get(j).get("T");
					data[1]=list2.get(j).get("Q");
					list3.add(data); //添加到list里
				}
			}
			dataMap.put("DATA", list3);//DATA的数据是list3就是上面添加的T和Q
			newList1.add(dataMap);
		}
		 Object[]  strArray = newList1.toArray();//list转JSON
		 mHashMap.put("DATA", strArray);//将拼装好的Object数组添加到map里
		List<HashMap<String, Object>>exta =new ArrayList<HashMap<String, Object>>();//EXTRADATA
		HashMap<String, Object> map1=new HashMap<String, Object>();//EXTRADATA map
		int []a= {1,2,3,4}; 
		map1.put("XDATA", a);
		map1.put("CHARTTITLE", "历史退水段");
		exta.add(map1);//添加数据
		mHashMap.put("EXTRADATA", exta);//添加EXTRADATA
		JSONObject json = JSONObject.fromObject(mHashMap);//将hashmap转json
		CommonFunction.writeResponse(getResponse(), json);
		return  null;
	}
	
	/**
	 *步骤3，保存
	 * @return
	 */
	public String saveLineFor3() {
		log.info("ChanLiuAction=qiuHe:步骤3保存流量数据");
		HashMap<String, Object> mHashMap = new HashMap<String, Object>();
		//保存数据步骤
		String data=DATA;
		String sc=INTERVAL;
		int shicha=Integer.parseInt(sc);
		List<Double> list1=new ArrayList<Double>();//流量
		List<String> list2=new ArrayList<String>(); //时间
		List<Double> list3= new ArrayList<Double>();//Q*T
		String  []data1=data.split(",");
		for(int i=0;i<data1.length;i++) {
			if(i%2==0){
				list2.add(data1[i]);//时间
				}else{
					if(Double.parseDouble(data1[i])<0) {//流量如果小于0
						list1.add(0.0);
						continue;
					}else {
						list1.add(Double.parseDouble(data1[i]));
					}
				
				 }
		}
		for(int i=0;i<list1.size();i++) {//这个循环执行插入或更新河道水清流量信息
			ResultThrid  resultThrid=new ResultThrid();
			int j=i+1;//后1个数据的下标
			double q1=list1.get(i);
			if(i==list1.size()-1) {
				j=i;
			}
			double q2=list1.get(j);
			double qt=(q1+q2)*shicha/2;
			//list3.add(Double.valueOf(String.valueOf(qt))); //Q*T
			resultThrid.setQ(list1.get(i));
			resultThrid.setTm(list2.get(i));
			resultThrid.setStcd(STCD);
			resultThrid.setQt(qt);
			resultThrid.setInterval(Integer.parseInt(INTERVAL));
			ResultThrid 	resultThrid2=resultThridService.getResultThridByTm(resultThrid);
			if(resultThrid2==null||resultThrid2.getTm()==""||resultThrid2.getTm().equals("")) {//如果没有
				resultThridService.saveResultThrid(resultThrid);//保存第三步的数据
			}else {
				//riverService.update(mRiver);
			}
		}
		CommonFunction.writeResponse(getResponse(), mHashMap);
		return null;
	}
	
	/**
	 * 第三步，数据表格
	 * @return
	 */
	public String qiuHe() {
		log.info("ChanLiuAction=qiuHe:计算求和");
		HashMap<String, Object> mHashMap = new HashMap<String, Object>();
		RetMessage mRetMessage=new RetMessage();
		PageResults mPageResults=new PageResults();
		ResultThridFormBean resultThrid=new ResultThridFormBean();
		//计算求和步骤
		resultThrid.getResultThrid().setStcd(STCD);
		mRetMessage=resultThridControl.step3(resultThrid, mPageResults);
		List<HashMap<String, Object>> listdata=mPageResults.getResults();
		HashMap<String, Object>  sumQ=new HashMap<String, Object>();
		sumQ.put("DATE"," 流量合计");
		sumQ.put("SC",INTERVAL);
		List list=resultThridService.sumQ(resultThrid);
		HashMap<String, Object> ll= (HashMap<String, Object>) list.get(0);
		Object sumLL=  ll.get("LL");
		int llmax=Integer.parseInt(sumLL.toString());//流量合计
		sumQ.put("LL",	llmax);//流量合计
		//** Q*T合计
		List qtlist=resultThridService.sumQT(resultThrid);//QT合计
		HashMap<String, Object> qt= (HashMap<String, Object>) qtlist.get(0);
		Object sumqt=  qt.get("QT");
		int sumQt=Integer.parseInt(sumqt.toString());//QT合计
		sumQ.put("QT",	sumQt);//QT合计
		listdata.add(sumQ);
		//R实
		HashMap<String, Object> rshi=new HashMap<String, Object>();
		rshi.put("DATE"," R实(mm)");
		rshi.put("QT",sumQt/200/1000);
		listdata.add(rshi);
		mHashMap.put("DATA", listdata);//给DATA添加数据
		mHashMap.put("CODE", "1");
		mHashMap.put("MESSAGE", "0");
		mHashMap.put("PAGESIZE", 0);
		mHashMap.put("PAGEINDEX", 0);
		mHashMap.put("TOTALAMOUNT", 1);
		JSONObject json = JSONObject.fromObject(mHashMap);
		CommonFunction.writeResponse(getResponse(), json);
		return null;
	}
	
	/**
	 * 第四步左边的图数据查询 testsyq_1-n2.json
	 * @return
	 */
	public String getStep4() {
		log.info("ChanLiuAction=list:步骤一.4 Echarts, 第四步的数据来源");
		HashMap<String, Object> mHashMap=new HashMap<String,Object>();
		RetMessage mRetMessage=new RetMessage();
		PageResults mPageResults=new PageResults();
		mHashMap.put("CODE", "1");
		mHashMap.put("MESSAGE", "查询数据成功");
		mHashMap.put("PAGESIZE", 0);
		mHashMap.put("PAGEINDEX", 0);
		mHashMap.put("TOTALAMOUNT", 2);
		ResultThridFormBean resultThrid=new ResultThridFormBean();
		resultThrid.getResultThrid().setStcd(stcd);
		mRetMessage=resultThridControl.getStep4List(resultThrid, mPageResults);
		HashMap<String, Object> step4map=new HashMap<String, Object>();
		List<HashMap<String, Object>> step4List=mPageResults.getResults();//数据库查询的数据结果集
		HashMap<String, Object> liuLiangmap=new HashMap<String, Object>();
		List<Object[]> newList=new ArrayList<Object[]>();//流量数据的list
		for(int i=0;i<step4List.size();i++) {
			liuLiangmap.put((String) step4List.get(i).get("DATE"), step4List.get(i).get("LL"));//这个map键是时间，值对应的就是流量
			Object [] ma=new Object [2];
			ma[0]=step4List.get(i).get("DATE");//时间
			ma[1]=step4List.get(i).get("LL");//流量
			newList.add(ma);//添加数据
		}
		 Object[]  strArray = newList.toArray();
		 step4map.put("DATA", strArray);
		 step4map.put("STCD", stcd);
		 step4map.put("STNM", "");
		 step4map.put("NAME", "流量");
		 step4map.put("TYPE", "line");
		List ymllist=resultThridService.maxQ(resultThrid);
		HashMap<String, Object> ymllistmax= (HashMap<String, Object>) ymllist.get(0);
		Object JYL=  ymllistmax.get("LL");
		int maxQ=Integer.parseInt(JYL.toString())*2;
		 step4map.put("MAX", maxQ);//最大值
		 List<HashMap<String, Object>> dataList= new ArrayList<HashMap<String, Object>>();//定义一个list
			//List<Object[]> newDataList=new ArrayList<Object[]>();//流量数据的list
		 dataList.add(step4map);//将map添加到List里面
		 Object[]  dataArray = dataList.toArray();
		 mHashMap.put("DATA", dataArray);//要将DATA转化为数组
			HashMap<String, Object> map1=new HashMap<String, Object>();//EXTRADATA map
			map1.put("START", start);
			map1.put("END", end);
			map1.put("CHARTTITLE", "洪水过程摘录图");
		mHashMap.put("EXTRADATA", map1);
		JSONObject json = JSONObject.fromObject(mHashMap);
		CommonFunction.writeResponse(getResponse(), json);
		return null;
	}
	
	
	/**
	 * 第四部求对数  test_tsd2.json
	 * @return
	 */
	public String getLog() {
		log.info("ChanLiuAction=qiuLog:求对数的方法");
		HashMap<String, Object> mHashMap = new HashMap<String, Object>();
		RetMessage mRetMessage=new RetMessage();
		PageResults mPageResults=new PageResults();
		ResultThridFormBean resultThrid=new ResultThridFormBean();
		resultThrid.getResultThrid().setStcd(stcd);
		mRetMessage=resultThridControl.getStep4List(resultThrid, mPageResults);
		List<HashMap<String, Object>> step4List=mPageResults.getResults();//数据库查询的数据结果集
		HashMap<String, Object> liuLiangmap=new HashMap<String, Object>();
		List<Object[]> newList=new ArrayList<Object[]>();//流量数据的list
		for(int i=0;i<step4List.size();i++) {
			liuLiangmap.put((String) step4List.get(i).get("DATE"), step4List.get(i).get("LL"));//这个map键是时间，值对应的就是流量
			Object [] ma=new Object [2];
			ma[0]=step4List.get(i).get("DATE");//时间
			if(((BigDecimal) step4List.get(i).get("LL")).doubleValue()==0) {
				ma[1]=0;
			}else {
				ma[1]=Math.log(((BigDecimal) step4List.get(i).get("LL")).doubleValue());//流量 求对数
			}
			newList.add(ma);//添加数据
		}
		 Object[]  strArray = newList.toArray();
		 HashMap<String, Object> dataMap=new HashMap<String, Object>();
		 dataMap.put("DATA", strArray);//时间-对数
		 dataMap.put("STCD", stcd);
		 dataMap.put("STNM", "");
		 dataMap.put("NAME", "对数线");
		 dataMap.put("TYPE", "line");
		 		List ymllist=resultThridService.maxQ(resultThrid);//Q最大值
				HashMap<String, Object> ymllistmax= (HashMap<String, Object>) ymllist.get(0);
				Object JYL=  ymllistmax.get("LL");
				int maxQ=Integer.parseInt(JYL.toString())*2;
		dataMap.put("MAX", Math.log(maxQ)*2);
		List<HashMap<String, Object>> dataList=new ArrayList<HashMap<String, Object>>();
		dataList.add(dataMap);
		 Object[]  dataArray = dataList.toArray();
			mHashMap.put("CODE", 1);
			mHashMap.put("MESSAGE", "");
			mHashMap.put("PAGESIZE", 0);
			mHashMap.put("PAGEINDEX", 0);
			mHashMap.put("TOTALAMOUNT", 2);
		mHashMap.put("DATA", dataArray);
		HashMap<String, Object> map1=new HashMap<String, Object>();//EXTRADATA map
		map1.put("START", start);
		map1.put("END", end);
		map1.put("CHARTTITLE", "");
		mHashMap.put("EXTRADATA", map1);
		JSONObject json = JSONObject.fromObject(mHashMap);
		CommonFunction.writeResponse(getResponse(), json);
		return null;
	}
	
	/**
	 * 保存第四步
	 * @return
	 */
	public String saveLineFor4() {
		log.info("ChanLiuAction=step4:保存选择之后的数据");
		String data=DATA;
		String sc=INTERVAL;
		int shicha=Integer.parseInt(sc);
		List<Double> list1=new ArrayList<Double>();//流量
		List<String> list2=new ArrayList<String>(); //时间
		List<Double> list3= new ArrayList<Double>();//Q*T
		String  []data1=data.split(",");
		for(int i=0;i<data1.length;i++) {
			if(i%2==0){
				list2.add(data1[i]);//时间
				}else{
					if(Double.parseDouble(data1[i])<0) {//流量如果小于0
						list1.add(0.0);
						continue;
					}else {
						list1.add(Double.parseDouble(data1[i]));
					}
				
				 }
		}
		for(int i=0;i<list1.size();i++) {//这个循环执行插入或更新河道水清流量信息
			int j=i+1;//后1个数据的下标
			double q1=list1.get(i);
			if(i==list1.size()-1) {
				j=i;
			}
			double q2=list1.get(j);
			double qt=(q1+q2)*shicha/2;
			ResultFourth  resultFourth=new ResultFourth();
			resultFourth.setQdx(list1.get(i));
			resultFourth.setTm(list2.get(i));
			resultFourth.setStcd(STCD);
			resultFourth.setQt(qt);
			resultFourth.setInterval(Integer.parseInt(INTERVAL));
			ResultFourth 	resultFourth2=resultFourthService.getResultFourthByTm(resultFourth);
			if(resultFourth2==null||resultFourth2.getTm()==""||resultFourth2.getTm().equals("")) {//如果没有
				resultFourthService.saveResultFourth(resultFourth);//保存第四步的数据
			}else {
				
			}
		}
		return null;
	}
	/**
	 * 第四步table
	 * @return
	 */
	public String getStep4Table() {
		log.info("ChanLiuAction=getStep4Table:第四步下面的table");
		HashMap<String, Object> mHashMap = new HashMap<String, Object>();
		RetMessage mRetMessage=new RetMessage();
		PageResults mPageResults=new PageResults();
		ResultFourthFormBean resultFourth=new ResultFourthFormBean();
		//计算求和步骤
		resultFourth.getResultFourth().setStcd(STCD);
		mRetMessage=resultFourthControl.getStep4(resultFourth, mPageResults);
		List<HashMap<String, Object>> listdata=mPageResults.getResults();
		HashMap<String, Object>  sumQ=new HashMap<String, Object>();
		sumQ.put("DATE"," 流量(地下)合计");
		sumQ.put("SC",INTERVAL);
		List list=resultFourthService.sumQdx(resultFourth);
		HashMap<String, Object> ll= (HashMap<String, Object>) list.get(0);
		Object sumLL=  ll.get("LL");
		int llmax=Integer.parseInt(sumLL.toString());//流量合计
		sumQ.put("LL",	llmax);//流量合计
		//** Q*T合计
		List qtlist=resultFourthService.sumQT(resultFourth);//QT合计
		HashMap<String, Object> qt= (HashMap<String, Object>) qtlist.get(0);
		Object sumqt=  qt.get("QT");
		int sumQt=Integer.parseInt(sumqt.toString());//QT合计
		sumQ.put("QT",	sumQt);//QT合计
		listdata.add(sumQ);
		//R实
		HashMap<String, Object> rshi=new HashMap<String, Object>();
		rshi.put("DATE"," R实(mm)");
		rshi.put("QT",sumQt/200/1000);
		listdata.add(rshi);
		mHashMap.put("DATA", listdata);//给DATA添加数据
		mHashMap.put("CODE", "1");
		mHashMap.put("MESSAGE", "0");
		mHashMap.put("PAGESIZE", 0);
		mHashMap.put("PAGEINDEX", 0);
		mHashMap.put("TOTALAMOUNT", 1);
		JSONObject json = JSONObject.fromObject(mHashMap);
		CommonFunction.writeResponse(getResponse(), json);
		return null;
	}
	
	/**
	 * 产流计算第五步
	 * @return
	 */
	public String step5() {
		
		return null;
	}
	
	/**
	 * 产流计算第六步
	 * @return
	 */
	public String step6() {
		log.info("ChanLiuAction=list: 第六步， Table,json1的数据来源");
		HashMap<String, Object> mHashMap=new HashMap<String,Object>();
		RetMessage mRetMessage=new RetMessage();
		PageResults mPageResults=new PageResults();
		mHashMap.put("CODE", "1");
		mHashMap.put("MESSAGE", "0");
		mHashMap.put("PAGESIZE", 0);
		mHashMap.put("PAGEINDEX", 0);
		mHashMap.put("TOTALAMOUNT", 0);
		mRetMessage= resultControl.step2(resultFormBean,start,end,mPageResults);//第二步需要的sql查询方法
		List listdata=mPageResults.getResults();
		mHashMap.put("DATA", listdata);//给DATA添加数据
		List<HashMap<String, Object>>list =new ArrayList<HashMap<String, Object>>();//EXTRADATA
		HashMap<String, Object> listMap=new HashMap<String, Object>();	
		listMap.put("STNM", "站名");
		listMap.put("PCH", "洪号");
		listMap.put("LYMJ", "流域面积");
		list.add(listMap);//添加数据
		mHashMap.put("EXTRADATA", list);
		JSONObject json = JSONObject.fromObject(mHashMap);
		CommonFunction.writeResponse(getResponse(), json);
		//getResponse().setContentType("json;charset=utf-8");
		
		return null;
	}
	//========GET SET
	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getStcd() {
		return stcd;
	}

	public void setStcd(String stcd) {
		this.stcd = stcd;
	}

	public ResultFormBean getResultFormBean() {
		return resultFormBean;
	}

	public void setResultFormBean(ResultFormBean resultFormBean) {
		this.resultFormBean = resultFormBean;
	}

	public String getCzmc() {
		return czmc;
	}

	public void setCzmc(String czmc) {
		this.czmc = czmc;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getQuanzhong() {
		return quanzhong;
	}

	public void setQuanzhong(String quanzhong) {
		this.quanzhong = quanzhong;
	}

	public String getYu() {
		return yu;
	}

	public void setYu(String yu) {
		this.yu = yu;
	}


	public StbprpFormBean getmStbprpFormBean() {
		return mStbprpFormBean;
	}

	public void setmStbprpFormBean(StbprpFormBean mStbprpFormBean) {
		this.mStbprpFormBean = mStbprpFormBean;
	}

	public PptnFormBean getmPptnFormBean() {
		return mPptnFormBean;
	}

	public void setmPptnFormBean(PptnFormBean mPptnFormBean) {
		this.mPptnFormBean = mPptnFormBean;
	}

	public String getPch() {
		return pch;
	}

	public void setPch(String pch) {
		this.pch = pch;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getSTCD() {
		return STCD;
	}

	public void setSTCD(String sTCD) {
		STCD = sTCD;
	}

	public String getDATE() {
		return DATE;
	}

	public void setDATE(String dATE) {
		DATE = dATE;
	}

	public Integer getJYL() {
		return JYL;
	}

	public void setJYL(Integer jYL) {
		JYL = jYL;
	}

	public double getLL() {
		return LL;
	}

	public void setLL(double lL) {
		LL = lL;
	}

	public String getBEGINDATE() {
		return BEGINDATE;
	}

	public void setBEGINDATE(String bEGINDATE) {
		BEGINDATE = bEGINDATE;
	}

	public String getENDDATE() {
		return ENDDATE;
	}

	public void setENDDATE(String eNDDATE) {
		ENDDATE = eNDDATE;
	}

	public String getINTERVAL() {
		return INTERVAL;
	}

	public void setINTERVAL(String iNTERVAL) {
		INTERVAL = iNTERVAL;
	}

	public String getDATA() {
		return DATA;
	}

	public void setDATA(String dATA) {
		DATA = dATA;
	}

	
	
	
	
	
	
}