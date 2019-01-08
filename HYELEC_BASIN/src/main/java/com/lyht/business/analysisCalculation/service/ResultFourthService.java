package com.lyht.business.analysisCalculation.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lyht.base.hibernate.common.PageResults;
import com.lyht.business.analysisCalculation.bean.HResultSecond;
import com.lyht.business.analysisCalculation.bean.HResultThrid;
import com.lyht.business.analysisCalculation.bean.Hresult;
import com.lyht.business.analysisCalculation.bean.HresultFourth;
import com.lyht.business.analysisCalculation.bean.HresultFourthZhcx;
import com.lyht.business.analysisCalculation.bean.HresultSecondZhcx;
import com.lyht.business.analysisCalculation.bean.HresultThirdZhcx;
import com.lyht.business.analysisCalculation.bean.HresultZhcx;
import com.lyht.business.analysisCalculation.bean.ResultFourth;
import com.lyht.business.analysisCalculation.bean.ResultSecond;
import com.lyht.business.analysisCalculation.bean.ResultSixthResult;
import com.lyht.business.analysisCalculation.bean.ResultThirdLess;
import com.lyht.business.analysisCalculation.bean.ResultThrid;
import com.lyht.business.analysisCalculation.dao.HResultSecondDao;
import com.lyht.business.analysisCalculation.dao.HResultThridDao;
import com.lyht.business.analysisCalculation.dao.HresultDao;
import com.lyht.business.analysisCalculation.dao.HresultFourthDao;
import com.lyht.business.analysisCalculation.dao.HresultFourthZhcxDao;
import com.lyht.business.analysisCalculation.dao.HresultSecondZhcxDao;
import com.lyht.business.analysisCalculation.dao.HresultThirdZhcxDao;
import com.lyht.business.analysisCalculation.dao.HresultZhcxDao;
import com.lyht.business.analysisCalculation.dao.ResultFourthDao;
import com.lyht.business.analysisCalculation.dao.ResultSecondDao;
import com.lyht.business.analysisCalculation.dao.ResultSixthResultDao;
import com.lyht.business.analysisCalculation.dao.ResultThirdLessDao;
import com.lyht.business.analysisCalculation.dao.ResultThridDao;
import com.lyht.business.analysisCalculation.formbean.ResultFourthFormBean;
import com.lyht.business.consumer.hydrologicalData.dao.RiverDao;
import com.lyht.util.CommonUtil;
import com.lyht.util.LinearRegression;
import com.lyht.util.Point;
import com.lyht.util.Randomizer;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 *作者： 刘魁
 *脚本日期:2018年6月24日 15:24:11
 *说明:  产流计算结果第四步service
*/
@Service
@Scope("prototype")
@Transactional
public class ResultFourthService {
	@Resource ResultFourthDao resultFourthDao;
	@Resource RiverDao riverDao;
	@Resource HresultDao hresultDao;
	@Resource HResultSecondDao hresultSecondDao;
	@Resource HResultThridDao hresultThirdDao;
	@Resource ResultThridDao resultThridDao;
	@Resource ResultSixthResultDao resultSixthResultDao;
	@Resource HresultFourthDao hresultFourthDao;
	@Resource
	private ResultSecondDao rsDao;
	@Resource private HresultZhcxDao hresultZhcxDao;
	@Resource private HresultSecondZhcxDao hresultSecondZhcxDao;
	@Resource private HresultThirdZhcxDao hresultThirdZhcxDao;
	@Resource private HresultFourthZhcxDao hresultFourthZhcxDao;
	@Resource private ResultThirdLessDao resultThirdLessDao;
	/**
		 * 保存第四步结果
		 */
	@Transactional(propagation=Propagation.REQUIRED)
	public ResultFourth saveResultFourth(ResultFourth resultFourth) {
		resultFourthDao.saveResutFourth(resultFourth);;
		return resultFourth;
	}
	public Hashtable<String,Object> saveStep3Result(String stcd,String pch,String data){
		Hashtable<String,Object> table = new Hashtable<String,Object>();
		try {
			JSONObject submitData=JSONObject.fromObject(data);
			if(submitData.containsKey("subdata")){
				JSONArray jsonArr = submitData.getJSONArray("subdata");
				if(jsonArr!=null && jsonArr.size()>0){
					ResultThrid  resultThrid1=new ResultThrid();
					resultThrid1.setStcd(stcd);
					resultThrid1.setPch(pch);
					resultThridDao.delResultThrid(resultThrid1);
					for(int i=0;i<jsonArr.size();i++){
						JSONObject json = jsonArr.getJSONObject(i);
						if(json!=null){
							ResultThrid resultThrid=new ResultThrid();
							resultThrid.setInterval(json.getInt("INTERVAL"));
							resultThrid.setPch(pch);
							resultThrid.setQ(json.getDouble("Q"));
							resultThrid.setQt(json.getDouble("QT"));
							resultThrid.setStcd(stcd);
							resultThrid.setTm(json.getString("DT"));
							resultThridDao.saveResultThrid(resultThrid);
						}
					}
					double sumQt=submitData.getDouble("sumQt");
					double rs=submitData.getDouble("rs");
					ResultSecond rSecond=new ResultSecond();
					rSecond.setPch(pch);
					rSecond.setSTCD(stcd);
					rSecond.setRs(rs);//R下
					rSecond.setSqt3(sumQt);
					List<Map> rsList = rsDao.querySecondByStcdAndPch(stcd, pch);
					 if(rsList!=null && rsList.size()>0) {
						 rsDao.updateSecondRs(rSecond);
					 }else {
						 rsDao.saveResultSecond(rSecond);//新增
					 }
				}
			}
			if(submitData.containsKey("lessLineData")){
				JSONArray jsonArr = submitData.getJSONArray("lessLineData");
				if(jsonArr!=null && jsonArr.size()>0){
					resultThirdLessDao.deleteByStcdAndPch(stcd, pch);
					for(int i=0;i<jsonArr.size();i++){
						JSONObject json = jsonArr.getJSONObject(i);
						if(json!=null){
							ResultThirdLess thirdLess = new ResultThirdLess();
							thirdLess.setId(java.util.UUID.randomUUID().toString());
							thirdLess.setStcd(stcd);
							thirdLess.setPch(pch);
							thirdLess.setInterval(json.getInt("INTERVAL"));
							thirdLess.setTm(json.getString("DT"));
							thirdLess.setQ(json.getDouble("Q"));
							thirdLess.setQt(json.getDouble("QT"));
							resultThirdLessDao.saveOrUpdate(thirdLess);
						}
					}
				}
			}
			table.put("reflag", "1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			table.put("reflag", "0");
			table.put("message", "保存失败!");
		}
		return table;
	}
	public Hashtable<String,Object> saveStep4Data(String stcd,String pch,String data){
		Hashtable<String,Object> table = new Hashtable<String,Object>();
		try {
			ResultFourth  resultFourth1=new ResultFourth();
			resultFourth1.setStcd(stcd);
			resultFourth1.setPch(pch);
			resultFourthDao.delResutFourth(resultFourth1);
			JSONObject submitData=JSONObject.fromObject(data);
			JSONArray jsonArr = submitData.getJSONArray("subdata");
			if(jsonArr!=null && jsonArr.size()>0){
				for(int i=0;i<jsonArr.size();i++){
					JSONObject json = jsonArr.getJSONObject(i);
					if(json!=null){
						ResultFourth resultFourth=new ResultFourth();
						String id=Randomizer.nextNumber("rtf", 5);
						resultFourth.setId(id);
						resultFourth.setInterval(json.getInt("INTERVAL"));
						resultFourth.setPch(pch);
						resultFourth.setQdx(json.getDouble("LL"));
						resultFourth.setQt(json.getDouble("QT"));
						resultFourth.setStcd(stcd);
						resultFourth.setTm(json.getString("DATE"));
						resultFourthDao.saveResutFourth(resultFourth);
					}
				}
				double sumQt=submitData.getDouble("sumQt");
				double rs=submitData.getDouble("rs");
				ResultSecond rSecond=new ResultSecond();
				rSecond.setPch(pch);
				rSecond.setSTCD(stcd);
				rSecond.setRx(rs);//R下
				rSecond.setSqt4(sumQt);
				List<Map> rsList = rsDao.querySecondByStcdAndPch(stcd, pch);
				 if(rsList!=null && rsList.size()>0) {
					 rsDao.updateSecondRx(rSecond);
				 }else {
					 rsDao.saveResultSecond(rSecond);//新增
				}
			}
		
			table.put("reflag", "1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			table.put("reflag", "0");
			table.put("message", "保存失败!");
		}
		return table;
	}
	/**
	 * 删除第四步结果
	 */
@Transactional(propagation=Propagation.REQUIRED)
public ResultFourth delResultFourth(ResultFourth resultFourth) {
	resultFourthDao.delResutFourth(resultFourth);;
	return resultFourth;
}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public ResultFourth getResultFourthByTm(ResultFourth resultFourth) {
		return resultFourthDao.getResutFourthByTm(resultFourth);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public PageResults getStep4(ResultFourthFormBean resultThrid) {
		return resultFourthDao.getStep4Table(resultThrid);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List  sumQdx(ResultFourthFormBean resultFourth) {
		return resultFourthDao.sumQdx(resultFourth);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List  sumQT(ResultFourthFormBean resultFourth) {
		return resultFourthDao.sumQT(resultFourth);
	}
	/**
	 * 获取第三步第一个表格和第二个表格数据
	 * @param stcd
	 * @param pch
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Hashtable calcStep3Table1AndTable2(String stcd,String pch,String tj,String rj){
		Hashtable table = new Hashtable();
		//表格2数据
		Map lymjMap = this.resultFourthDao.getHuiLiuStep1Lymj(stcd, pch);
		if(lymjMap==null){
			lymjMap=new HashMap();
			lymjMap.put("LLMJ", "");
		}
		List<Map> table1List = resultFourthDao.getHuiLiuStep1Table1(stcd,pch);
		double maxQ = this.jisuanQjinzhi(table1List);
		lymjMap.put("STCD", stcd);
		lymjMap.put("PCH", pch);
		lymjMap.put("QJ", maxQ);
		lymjMap.put("RJ", rj);
		lymjMap.put("TJ", tj);
		lymjMap.put("L", "");
		lymjMap.put("J", "");
		table.put("table2Data", lymjMap);
		//表格3数据
		List<Map> table3Data = this.resultFourthDao.queryHuiliuStep3Table3(stcd, pch);
		table.put("table3Data", table3Data);
		return table;
	}
	@SuppressWarnings("unchecked")
	public Hashtable chanliuStep6Search(String stcd) throws Exception{
		Hashtable table = new Hashtable();
		List<ResultSixthResult> mjList = this.resultSixthResultDao.queryResultSixthResultByStcd(stcd);
//		List<ResultSixthResult> mjList = new ArrayList<ResultSixthResult>();
//		double[] m1=new double[]{1.63,1.02,0.88,1.39,1.31,0.93,1.52};
//		double[] t1=new double[]{5.05,1.51,1,6.92,3.5,2.56,5.05};
//		for(int a=0;a<7;a++){
//			ResultSixthResult t = new ResultSixthResult();
//			t.setNm("000"+(a+1)+"00000"+(a+1));
//			t.setStcd("000"+(a+1));
//			t.setPch("00000"+(a+1));
//			t.setTc(m1[a]*t1[a]);
//			t.setRc(m1[a]*2*t1[a]);
//			t.setFc(m1[a]);
//			t.setRctc(t1[a]);
//			t.setFc(m1[a]/2);
//			mjList.add(t);
//		}
//		mjList = this.sortStep6List(mjList);
		List<ResultSixthResult> mcList = new ArrayList<ResultSixthResult>();
		List<Map> resultList = new ArrayList<Map>();
		if(mjList!=null && mjList.size()>0){
			List<Point> pointList = new ArrayList<Point>();
			for(int n=0;n<mjList.size();n++){
				ResultSixthResult mjResult = mjList.get(n);
				pointList.add(new Point(mjResult.getFc(),mjResult.getRctc()));
			}
			for(int i=0;i<mjList.size();i++){
				@SuppressWarnings("rawtypes")
				Map map = new HashMap();
				ResultSixthResult mj = mjList.get(i);
				map.put("PCH", mj.getPch());
				map.put("TC", mj.getTc());
				map.put("RC", mj.getRc());
				map.put("RCTC", mj.getRctc());
				map.put("FC", mj.getFc());
				ResultSixthResult mc = new ResultSixthResult();
				BeanUtils.copyProperties(mc,mj);
				double cy = LinearRegression.getExponentialY(pointList, mj.getFc());
				mc.setRctc(this.formateDouble(cy,2));
				double cm=LinearRegression.getExponentialX(pointList, mj.getRctc());
				cm=new BigDecimal(cm).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//				mc.setM(this.formateDouble(cm,2));
				mcList.add(mc);
				map.put("FCC", this.formateDouble(cm,2));
				double cz=mj.getFc()-cm;
				double ms = mj.getFc()!=0?cz/mj.getFc():0;
				ms=new BigDecimal(ms).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				map.put("FCS", ms);
				double msjdz = ms>=0?ms:(0-ms);
				String sfhg = msjdz>0.2?"不合格":"合格";
				map.put("SFHG", sfhg);
				resultList.add(map);
				mj.setFcc(cm);
				mj.setFcs(ms);
				this.resultSixthResultDao.saveOrUpdate(mj);;
			}
		}
		table.put("mjDataList", mjList);
		table.put("mcDataList", mcList);
		table.put("cxDataList", resultList);
		return table;
	}
	/**
	 * 第三步综合查询
	 * @param stcd
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Hashtable huiliuStep4Search(String stcd) throws Exception{
		Hashtable table = new Hashtable();
		List<HresultFourth> mjList = this.hresultFourthDao.findByStcdOrderBy(stcd);
//		List<HresultFourth> mjList = new ArrayList<HresultFourth>();
//		double[] m1=new double[]{1.63,1.02,0.88,1.39,1.31,0.93,1.52};
//		double[] t1=new double[]{5.05,1.51,1,6.92,3.5,2.56,5.05};
//		for(int a=0;a<7;a++){
//			HresultFourth t = new HresultFourth();
//			t.setStcd("000"+(a+1));
//			t.setPch("00000"+(a+1));
//			t.setM1(m1[a]);
//			t.setAm13(t1[a]);
//			t.setN(Double.valueOf(a+1));
//			t.setK(Double.valueOf(a+1.5));
//			t.setQmj(new BigDecimal(a*1.2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//			t.setQms(new BigDecimal(a*1.3).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//			mjList.add(t);
//		}
//		mjList = this.sortStep4List(mjList);
		//删除
		hresultFourthZhcxDao.deleteByStcd(stcd);
		List<HresultFourth> mcList = new ArrayList<HresultFourth>();
		
		List<Map> resultList = new ArrayList<Map>();
		if(mjList!=null && mjList.size()>0){
			List<Point> pointList = new ArrayList<Point>();
			for(int n=0;n<mjList.size();n++){
				HresultFourth mjResult = mjList.get(n);
				pointList.add(new Point(mjResult.getM1(),mjResult.getAm13()));
			}
			for(int i=0;i<mjList.size();i++){
				Map map = new HashMap();
				HresultFourth mj = mjList.get(i);
				map.put("PCH", mj.getPch());
				map.put("N", mj.getN());
				map.put("K", mj.getK());
				map.put("M1", mj.getM1());
				map.put("AM13", mj.getAm13());
				HresultFourth mc = new HresultFourth();
				BeanUtils.copyProperties(mc,mj);
				double cy = LinearRegression.getExponentialY(pointList, mj.getM1());
				mc.setAm13(this.formateDouble(cy,2));
				double cm=LinearRegression.getExponentialX(pointList, mj.getAm13());
				cm=new BigDecimal(cm).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//				mc.setM(this.formateDouble(cm,2));
				mcList.add(mc);
				map.put("M1C", this.formateDouble(cm,2));
				double kj=mj.getN()!=0?cm/mj.getN():0;
				kj=new BigDecimal(kj).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				map.put("KJ", kj);
				map.put("QMJ", mj.getQmj());
				map.put("QMS", mj.getQms());
				double cz=mj.getM1()-cm;
				double wc=mj.getM1()!=0?cz*100/mj.getM1():0;
				wc=new BigDecimal(wc).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				map.put("WC", wc+"%");
				double ms = mj.getM1()!=0?cz/mj.getM1():0;
				ms=new BigDecimal(ms).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				map.put("MS", ms);
				double msjdz = ms>=0?ms:(0-ms);
				String sfhg = msjdz>0.2?"不合格":"合格";
				map.put("SFHG", sfhg);
				resultList.add(map);
				
				HresultFourthZhcx cxbean = new HresultFourthZhcx();
				cxbean.setNm(mj.getStcd()+mj.getPch());
				cxbean.setStcd(stcd);
				cxbean.setPch(mj.getPch());
				cxbean.setN(mj.getN());
				cxbean.setK(mj.getK());
				cxbean.setM1(mj.getM1());
				cxbean.setA13am(mj.getAm13());
				cxbean.setM1c(cm);
				cxbean.setKj(kj);
				cxbean.setQmj(mj.getQmj());
				cxbean.setQms(mj.getQms());
				cxbean.setWc(wc);
				this.hresultFourthZhcxDao.save(cxbean);
			}
		}
		table.put("mjDataList", mjList);
		table.put("mcDataList", mcList);
		table.put("cxDataList", resultList);
		return table;
	}
	/**
	 * 第三步综合查询
	 * @param stcd
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Hashtable huiliuStep3Search(String stcd) throws Exception{
		Hashtable table = new Hashtable();
		List<HResultThrid> mjList = this.hresultThirdDao.findByStcdOrderBy(stcd);
//		List<HResultThrid> mjList = new ArrayList<HResultThrid>();
//		double[] m1=new double[]{1.63,1.02,0.88,1.39,1.31,0.93,1.52};
//		double[] t1=new double[]{5.05,1.51,1,6.92,3.5,2.56,5.05};
//		for(int a=0;a<7;a++){
//			HResultThrid t = new HResultThrid();
//			t.setStcd("000"+(a+1));
//			t.setPch("00000"+(a+1));
//			t.setM(m1[a]);
//			t.setQf(t1[a]);
//			mjList.add(t);
//		}
//		mjList = this.sortStep3List(mjList);
		
		//删除
		hresultThirdZhcxDao.deleteByStcd(stcd);
		
		List<HResultThrid> mcList = new ArrayList<HResultThrid>();
		
		List<Map> resultList = new ArrayList<Map>();
		if(mjList!=null && mjList.size()>0){
			List<Point> pointList = new ArrayList<Point>();
			for(int n=0;n<mjList.size();n++){
				HResultThrid mjResult = mjList.get(n);
				pointList.add(new Point(mjResult.getM(),mjResult.getQms()));
			}
			for(int i=0;i<mjList.size();i++){
				Map map = new HashMap();
				HResultThrid mj = mjList.get(i);
				map.put("PCH", mj.getPch());
				map.put("QF", mj.getQf());
				map.put("MJ", mj.getM());
				HResultThrid mc = new HResultThrid();
				BeanUtils.copyProperties(mc,mj);
				double cy = LinearRegression.getExponentialY(pointList, mj.getM());
				mc.setQf(this.formateDouble(cy,2));
				double cm=LinearRegression.getExponentialX(pointList, mj.getQms());
//				mc.setM(this.formateDouble(cm,2));
				mcList.add(mc);
				map.put("MC", this.formateDouble(cm,2));
				double ms = mj.getM()!=0?(mj.getM()-cm)/mj.getM():0;
				map.put("MS", this.formateDouble(ms,2));
				double msjdz = ms>=0?ms:(0-ms);
				String sfhg = msjdz>0.2?"不合格":"合格";
				map.put("SFHG", sfhg);
				resultList.add(map);
				
				HresultThirdZhcx cxbean = new HresultThirdZhcx();
				cxbean.setNm(mj.getStcd()+mj.getPch());
				cxbean.setStcd(stcd);
				cxbean.setPch(mj.getPch());
				cxbean.setQf(mj.getQms());
				cxbean.setMj(mj.getM());
				cxbean.setMc(this.formateDouble(cm,2));
				cxbean.setMjmc(this.formateDouble(ms,2));
				this.hresultThirdZhcxDao.save(cxbean);
			}
		}
		table.put("mjDataList", mjList);
		table.put("mcDataList", mcList);
		table.put("cxDataList", resultList);
		return table;
	}
	/**
	 * 第二步综合查询
	 * @param stcd
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Hashtable huiliuStep2Search(String stcd) throws Exception{
		Hashtable table = new Hashtable();
		List<HResultSecond> mjList = this.hresultSecondDao.findByStcdOrderBy(stcd);
//		List<HResultSecond> mjList = new ArrayList<HResultSecond>();
//		double[] m1=new double[]{1.63,1.02,0.88,1.39,1.31,0.93,1.52};
//		double[] t1=new double[]{5.05,1.51,1,6.92,3.5,2.56,5.05};
//		for(int a=0;a<7;a++){
//			HResultSecond t = new HResultSecond();
//			t.setStcd("000"+(a+1));
//			t.setPch("00000"+(a+1));
//			t.setM(m1[a]);
//			t.setErtc(t1[a]);
//			mjList.add(t);
//		}
//		mjList = this.sortStep2List(mjList);
		//删除
		hresultSecondZhcxDao.deleteByStcd(stcd);
		List<HResultSecond> mcList = new ArrayList<HResultSecond>();
		
		List<Map> resultList = new ArrayList<Map>();
		if(mjList!=null && mjList.size()>0){
			List<Point> pointList = new ArrayList<Point>();
			for(int n=0;n<mjList.size();n++){
				HResultSecond mjResult = mjList.get(n);
				pointList.add(new Point(mjResult.getM(),mjResult.getErtc()));
			}
			for(int i=0;i<mjList.size();i++){
				Map map = new HashMap();
				HResultSecond mj = mjList.get(i);
				map.put("PCH", mj.getPch());
				map.put("ERET", mj.getErtc());
				map.put("MJ", mj.getM());
				HResultSecond mc = new HResultSecond();
				BeanUtils.copyProperties(mc,mj);
				double cy = LinearRegression.getExponentialY(pointList, mj.getM());
				mc.setErtc(this.formateDouble(cy,2));
				double cm=LinearRegression.getExponentialX(pointList, mj.getErtc());
//				mc.setM(this.formateDouble(cm,2));
				mcList.add(mc);
				map.put("MC", this.formateDouble(cm,2));
				double ms = mj.getM()!=0?(mj.getM()-cm)/mj.getM():0;
				map.put("MS", this.formateDouble(ms,2));
				double msjdz = ms>=0?ms:(0-ms);
				String sfhg = msjdz>0.2?"不合格":"合格";
				map.put("SFHG", sfhg);
				resultList.add(map);
				
				HresultSecondZhcx cxbean = new HresultSecondZhcx();
				cxbean.setNm(mj.getStcd()+mj.getPch());
				cxbean.setStcd(stcd);
				cxbean.setPch(mj.getPch());
				cxbean.setErtc(mj.getErtc());
				cxbean.setMj(mj.getM());
				cxbean.setMc(this.formateDouble(cm,2));
				cxbean.setMjmc(this.formateDouble(ms,2));
				this.hresultSecondZhcxDao.save(cxbean);
			}
		}
		table.put("mjDataList", mjList);
		table.put("mcDataList", mcList);
		table.put("cxDataList", resultList);
		return table;
	}
	/**
	 * 第一步综合查询
	 * @param stcd
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Hashtable huiliuStep1Search(String stcd) throws Exception{
		Hashtable table = new Hashtable();
		List<Hresult> mjList = this.hresultDao.findByStcdOrderBy(stcd);
//		List<Hresult> mjList = new ArrayList<Hresult>();
//		double[] m1=new double[]{1.63,1.02,0.88,1.39,1.31,0.93,1.52};
//		double[] t1=new double[]{5.05,1.51,1,6.92,3.5,2.56,5.05};
//		for(int a=0;a<7;a++){
//			Hresult t = new Hresult();
//			t.setPch("00000"+(a+1));
//			t.setM(m1[a]);
//			t.setErtc(t1[a]);
//			mjList.add(t);
//		}
//		mjList = this.sortStep1List(mjList);
		
		List<Hresult> mcList = new ArrayList<Hresult>();
		//删除综合查询
		hresultZhcxDao.deleteByStcd(stcd);
		List<Map> resultList = new ArrayList<Map>();
		if(mjList!=null && mjList.size()>0){
			List<Point> pointList = new ArrayList<Point>();
			for(int n=0;n<mjList.size();n++){
				Hresult mjResult = mjList.get(n);
				pointList.add(new Point(mjResult.getM(),mjResult.getErtc()));
			}
			for(int i=0;i<mjList.size();i++){
				
				Map map = new HashMap();
				Hresult mj = mjList.get(i);
				map.put("PCH", mj.getPch());
				map.put("ERET", mj.getErtc());
				map.put("MJ", mj.getM());
				
				Hresult mc = new Hresult();
				BeanUtils.copyProperties(mc,mj);
				double cy = LinearRegression.getExponentialY(pointList, mj.getM());
				mc.setErtc(this.formateDouble(cy,2));
				double cm=LinearRegression.getExponentialX(pointList, mj.getErtc());
//				mc.setM(this.formateDouble(cm,2));
				mcList.add(mc);
				map.put("MC", this.formateDouble(cm,2));
				double ms = mj.getM()!=0?(mj.getM()-cm)/mj.getM():0;
				map.put("MS", this.formateDouble(ms,2));
				double msjdz = ms>=0?ms:(0-ms);
				String sfhg = msjdz>0.2?"不合格":"合格";
				map.put("SFHG", sfhg);
				resultList.add(map);
				
				HresultZhcx cxbean = new HresultZhcx();
				cxbean.setNm(mj.getStcd()+mj.getPch());
				cxbean.setStcd(stcd);
				cxbean.setPch(mj.getPch());
				cxbean.setErtc(mj.getErtc());
				cxbean.setMj(mj.getM());
				cxbean.setMc(this.formateDouble(cm,2));
				cxbean.setMjmc(this.formateDouble(ms,2));
				this.hresultZhcxDao.save(cxbean);
			}
		}
		table.put("mjDataList", mjList);
		table.put("mcDataList", mcList);
		table.put("cxDataList", resultList);
		return table;
	}
	/**
	 * 汇流计算第一步的最后结果数据
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JSONObject calcHuiliuStep1Result(String data){
		JSONObject table = new JSONObject();
		if(CommonUtil.trim(data).length()<1){
			table.put("reflag", "0");
			table.put("message", "提交数据为空!");
			return table;
		}
		JSONObject dataJson = JSONObject.fromObject(data);
		String pch = dataJson.getString("PCH");
		String stcd= dataJson.getString("STCD");
		double tc = dataJson.getDouble("TC");
		JSONObject table2Data = dataJson.getJSONObject("table2Data");
		table2Data.put("PCH", pch);
		table2Data.put("STCD", stcd);
		table2Data.put("TC", tc);
		//汇流时间
		double t = table2Data.getDouble("HLSJ");
		t=this.formateDouble(t, 2);
		table2Data.put("HLSJ", t);
		//Q净流量
		double Q = table2Data.getDouble("QJLL"); 
		//地表净雨深
		double R = table2Data.getDouble("RG"); 
		R=this.formateDouble(R, 2);
		table2Data.put("RG", R);
		//流域面积
		double F = table2Data.getDouble("LLMJ"); 
		//L
		double L = table2Data.getDouble("L"); 
		//J
		double J = table2Data.getDouble("J"); 
		//m
		double m = table2Data.getDouble("M"); 
		m=this.formateDouble(m, 2);
		table2Data.put("M", m);
		//开始计算
		//计算J的3次开方
		double js1=table2Data.getDouble("J13"); 
		js1=this.formateDouble(js1, 5);
		table2Data.put("J13", js1);
		//计算L/ (J1/3*F)	Qm3/4	
		double js2=table2Data.getDouble("L3F"); 
		js2=this.formateDouble(js2, 5);
		table2Data.put("L3F", js2);
		//计算Qm3/4
		double js3=table2Data.getDouble("QM"); 
		js3=this.formateDouble(js3, 2);
		table2Data.put("QM", js3);
		//计算Qm3/4/ R上
		double js4=table2Data.getDouble("QMR"); 
		js4=this.formateDouble(js4, 2);
		table2Data.put("QMR", js4);
		
		table.put("table2Data", table2Data);
		//保存计算结果
		Hresult result = new Hresult();
		result.setNm(stcd+pch);
		result.setPch(pch);
		result.setStcd(stcd);
		result.setQ(Q);
		result.setT(t);
		result.setR(R);
		result.setMj(F);
		result.setL(L);
		result.setJ(J);
		result.setJs1(js1);
		result.setJs2(js2);
		result.setJs3(js3);
		result.setJs4(js4);
		result.setM(m);
		result.setTc(tc);
		double ertc=tc!=0?R/tc:0;
		result.setErtc(ertc);
		this.hresultDao.saveOrUpdate(result);
		return table;
	}
	/**
	 * 删除汇流计算第一步计算结果
	 * @param stcd
	 * @param pch
	 */
	public void deleteHuiliuStep1Result(String stcd,String pch){
		hresultDao.deleteById(stcd+pch);
	}
	private double calcChanliuStep3Rs(String stcd,String pch,double lymj){
		double sumqt=this.resultThridDao.queryStep3SumQt(stcd, pch);
		if(lymj!=0){
			double rs=sumqt/lymj/1000;
			rs=new BigDecimal(rs).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			return rs;
		}
		return 0;
	}
	private double calcChanliuStep4Rx(String stcd,String pch,double lymj){
		double sumqt=this.resultFourthDao.queryChanliuStep4SumQt(stcd, pch);
		if(lymj!=0){
			double rx=sumqt/lymj/1000;
			rx=new BigDecimal(rx).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			return rx;
		}
		return 0;
	}
	/**
	 * 汇流计算第一步查询
	 * @param rf
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(propagation=Propagation.REQUIRED)
	public Hashtable getHuiLiuStep1(String stcd,String pch) {
		Hashtable table = new Hashtable();
		//求最大Q净和表格数据
		List<Map> table1List = resultFourthDao.getHuiLiuStep1Table1(stcd,pch);
		double maxQ = this.jisuanQjinzhi(table1List);
		Map map=new HashMap();
		map.put("DATE", "Q净");
		map.put("LL", maxQ);
		map.put("LLDX", "");
		table1List.add(map);
		//显示需要计算m的数据表格
		List<Map> table2DataList = resultFourthDao.getHuiLiuStep1Table2(stcd,pch);
		//获取时间间隔
		Map sjjgMap =  resultFourthDao.getSjjgByStcdAndPch(stcd,pch);
		int sjjgfz=(sjjgMap!=null && sjjgMap.get("SJJG")!=null)?CommonUtil.getIntValue(sjjgMap.get("SJJG").toString()):0;
		//计算表格3数据
		List<Map> table3List = new ArrayList<Map>();
		List<Map> columnList = new ArrayList<Map>();
		
		//接口需要调用的计算TC的数据集合
		List<Map> subDataList = new ArrayList<Map>();
		
		Map lymjMap = this.resultFourthDao.getHuiLiuStep1Lymj(stcd, pch);
		double lymj = lymjMap!=null && lymjMap.get("LYMJ")!=null?CommonUtil.getFloatValue(lymjMap.get("LYMJ").toString()):0d;
		double rs=calcChanliuStep3Rs(stcd,pch,lymj);
		double rx=calcChanliuStep4Rx(stcd,pch,lymj);
		double rhj = 0d;
		int rmc=0;//r>0的时段
		if(table2DataList!=null && table2DataList.size()>0){
			Map rhjMap = new HashMap();
			Map pjMap = new HashMap();
			Map oneColumn=new HashMap();
			oneColumn.put("field", "sjsm");
			oneColumn.put("title", "∑t(小时)");
			oneColumn.put("width", "150");
			columnList.add(oneColumn);
			rhjMap.put("sjsm", "∑R上(毫米)");
			pjMap.put("sjsm", "R上/t(毫米/小时)");
			for(int i=0;i<table2DataList.size();i++){
				Map map2=table2DataList.get(i);
				Map columnMap = new HashMap();
				columnMap.put("field", "hour"+(i+1));
				columnList.add(columnMap);
				String sjhj=this.division((i+1)*sjjgfz,60);
				columnMap.put("title", sjhj);
				columnMap.put("width", "80");
				double r=((map2!=null && map2.get("R")!=null)?CommonUtil.getFloatValue(map2.get("R").toString()):0.0);
				if(rs!=0){
					r=r-(1-(rs-rx)/rs)*r;
				}
				if(r>0){
					rmc++;
				}
				rhj=rhj+r;
				double hj=new BigDecimal(rhj).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
				rhjMap.put("hour"+(i+1), hj);
				String pj=CommonUtil.getFloatValue(sjhj)>0?this.division(rhj, CommonUtil.getFloatValue(sjhj)):"0.0";
				pjMap.put("hour"+(i+1), pj);
				
				//接口传递数据集合
				Map sd = new HashMap();
				sd.put("t", sjhj);
				sd.put("Rt", pj);
				subDataList.add(sd);
			}
			rhj=new BigDecimal(rhj).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
			table3List.add(rhjMap);
			table3List.add(pjMap);
		}
		//查询流域面积
		//查询是否有计算过的表格2的结果
		Map table2Data = new HashMap();
		Hresult hresult = this.hresultDao.get(stcd+pch);
		if(hresult!=null && hresult.getNm()!=null 
				&& hresult.getNm().trim().length()>0){
			table2Data.put("PCH", hresult.getPch());
			if(hresult.getQ()!=null && hresult.getQ()>0){
				table2Data.put("QJLL", hresult.getQ());
			}else{
				table2Data.put("QJLL", maxQ);
			}
			table2Data.put("HLSJ", hresult.getT());
			table2Data.put("RG", hresult.getR());
			table2Data.put("LLMJ", hresult.getMj());
			table2Data.put("L",hresult.getL());
			table2Data.put("J",hresult.getJ());
			table2Data.put("J13",hresult.getJs1());
			table2Data.put("L3F",hresult.getJs2());
			table2Data.put("QM",hresult.getJs3());
			table2Data.put("QMR",hresult.getJs4());
			table2Data.put("M",hresult.getM());
			table2Data.put("TC", hresult.getTc());
		}else{
			Map lastBean = hresultDao.queryLastLJvalueBean(stcd);
			table2Data.put("PCH", pch);
			table2Data.put("QJLL", maxQ);
			//求RG
			double rgdata=0;
			Map qtmap=resultFourthDao.queryHuiliuStep1OfRg(stcd,pch);
			if(qtmap!=null && qtmap.get("QT3")!=null && qtmap.get("QT4")!=null){
				double qt3=CommonUtil.getFloatValue(qtmap.get("QT3").toString());
				double qt4=CommonUtil.getFloatValue(qtmap.get("QT4").toString());
				if(lymj!=0){
					rgdata=(qt3-qt4)/lymj/1000;
					rgdata=new BigDecimal(rgdata).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留2位小数
				}
			}
			
			//int len = table2DataList!=null?table2DataList.size():0;
			//table2Data.put("HLSJ", this.division(len*sjjgfz,60));
			//table2Data.put("RG", rhj);
			table2Data.put("HLSJ", this.division(rmc*sjjgfz,60));
			table2Data.put("RG", rgdata);
			table2Data.put("LLMJ", lymj);
			table2Data.put("L",((lastBean==null || lastBean.get("L")==null || lastBean.get("L").toString().length()<1)?"":lastBean.get("L").toString()));
			table2Data.put("J",((lastBean==null || lastBean.get("J")==null || lastBean.get("J").toString().length()<1)?"":lastBean.get("J").toString()));
			table2Data.put("J13","");
			table2Data.put("L3F","");
			table2Data.put("QM","");
			table2Data.put("QMR","");
			table2Data.put("M","");
			table2Data.put("TC", this.division(rmc*sjjgfz,60));
		}
		table.put("table1Data", table1List);
		table.put("table2Data", table2Data);
		table.put("table3Data", table3List);
		table.put("columnList", columnList);
		table.put("rhj", rhj);
		table.put("submitData", subDataList);
		return table;
	}
	private List<Hresult> sortStep1List(List<Hresult> rList){
		Collections.sort(rList, new Comparator<Hresult>(){
            /*
             * 返回负数表示：p1 小于p2，
             * 返回0 表示：p1和p2相等，
             * 返回正数表示：p1大于p2
             */
            public int compare(Hresult p1, Hresult p2) {
                //进行升序排列
                if(p1.getM() > p2.getM()){
                    return 1;
                }
                if(p1.getM() == p2.getM()){
                    return 0;
                }
                return -1;
            }
        });
		return rList;
	}
	private List<HResultSecond> sortStep2List(List<HResultSecond> rList){
		Collections.sort(rList, new Comparator<HResultSecond>(){
            /*
             * 返回负数表示：p1 小于p2，
             * 返回0 表示：p1和p2相等，
             * 返回正数表示：p1大于p2
             */
            public int compare(HResultSecond p1, HResultSecond p2) {
                //进行升序排列
                if(p1.getM() > p2.getM()){
                    return 1;
                }
                if(p1.getM() == p2.getM()){
                    return 0;
                }
                return -1;
            }
        });
		return rList;
	}
	private List<ResultSixthResult> sortStep6List(List<ResultSixthResult> rList){
		Collections.sort(rList, new Comparator<ResultSixthResult>(){
            /*
             * 返回负数表示：p1 小于p2，
             * 返回0 表示：p1和p2相等，
             * 返回正数表示：p1大于p2
             */
            public int compare(ResultSixthResult p1, ResultSixthResult p2) {
                //进行升序排列
                if(p1.getFc() > p2.getFc()){
                    return 1;
                }
                if(p1.getFc() == p2.getFc()){
                    return 0;
                }
                return -1;
            }
        });
		return rList;
	}
	private List<HresultFourth> sortStep4List(List<HresultFourth> rList){
		Collections.sort(rList, new Comparator<HresultFourth>(){
            /*
             * 返回负数表示：p1 小于p2，
             * 返回0 表示：p1和p2相等，
             * 返回正数表示：p1大于p2
             */
            public int compare(HresultFourth p1, HresultFourth p2) {
                //进行升序排列
                if(p1.getM1() > p2.getM1()){
                    return 1;
                }
                if(p1.getM1() == p2.getM1()){
                    return 0;
                }
                return -1;
            }
        });
		return rList;
	}
	private List<HResultThrid> sortStep3List(List<HResultThrid> rList){
		Collections.sort(rList, new Comparator<HResultThrid>(){
            /*
             * 返回负数表示：p1 小于p2，
             * 返回0 表示：p1和p2相等，
             * 返回正数表示：p1大于p2
             */
            public int compare(HResultThrid p1, HResultThrid p2) {
                //进行升序排列
                if(p1.getM() > p2.getM()){
                    return 1;
                }
                if(p1.getM() == p2.getM()){
                    return 0;
                }
                return -1;
            }
        });
		return rList;
	}
	private String division(double a,double b){
		String result = "";
        double num =a/b;
        num=new BigDecimal(num).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        DecimalFormat df = new DecimalFormat("0.0");
        result = df.format(num);
        return result;
	}
	private String formatter(double num){
		DecimalFormat df = new DecimalFormat("0.0");
        return df.format(num);
	}
	private String division(int a ,int b){
        String result = "";
        double num =(double)a/b;
        num=new BigDecimal(num).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        DecimalFormat df = new DecimalFormat("0.0");

        result = df.format(num);

        return result;
    }
	private double formateDouble(double d,int p){
		return new BigDecimal(d).setScale(p, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	/**
	 * 结果集最大流量数据的差值
	 * @return
	 */
	private double jisuanQjinzhi(List<Map> listdata){
		if(listdata==null || listdata.size()<1){
			return 0;
		}
		double maxLl=0;
		double llz=0;
		for(int i=0;i<listdata.size();i++){
			Map map = listdata.get(i);
			if(map!=null && map.get("LL")!=null 
					&& CommonUtil.getFloatValue(map.get("LL").toString())>0
					&& CommonUtil.getFloatValue(map.get("LL").toString())>maxLl){
				maxLl=CommonUtil.getFloatValue(map.get("LL").toString());
				if(map.get("LLDX")!=null && CommonUtil.getFloatValue(map.get("LLDX").toString())>0){
					llz=CommonUtil.getFloatValue(map.get("LLDX").toString());
				}
			}
		}
		return new BigDecimal((maxLl-llz)).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	/**
	 * 汇流计算第一步的下拉框数据查询
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(propagation=Propagation.REQUIRED)
	public Hashtable getHuiliuStep1Select(){
		Hashtable table = new Hashtable();
		List<Map> step1StcdSelect = this.resultFourthDao.getHuiLiuStep1StcdSelect();
		List<Map> step1PchSelect = this.resultFourthDao.getHuiLiuStep1PchSelect();
		table.put("stcdSelect", step1StcdSelect);
		table.put("pchSelect", step1PchSelect);
		return table;
	}
	/**
	 * 计算保存第三不m值和表3数据
	 * @param data
	 * @return
	 */
	public JSONObject calcStep3MAandTable3(String data){
		JSONObject table = new JSONObject();
		if(data==null ||CommonUtil.trim(data).length()<1){
			table.put("reflag", "0");
			table.put("message", "缺少数据!");
			return null;
		}
		JSONObject dataJson = JSONObject.fromObject(data);
		String stcd = dataJson.getString("STCD");
		String pch = dataJson.getString("PCH");
		double tc = dataJson.getDouble("TC");
		JSONObject table2Data = dataJson.getJSONObject("table2Data");
		double q = table2Data.getDouble("QJ");
		double tj = table2Data.getDouble("TJ");
		double rj = table2Data.getDouble("RJ");
		double f = table2Data.getDouble("LLMJ");
		double l = table2Data.getDouble("L");
		double j = table2Data.getDouble("J");
		double m = table2Data.getDouble("M");
		
		HResultThrid third = new HResultThrid();
		third.setNm(stcd+pch);
		third.setStcd(stcd);
		third.setPch(pch);
		third.setQj(q);;
		third.setRsj(rj);;
		third.setTcj(tj);
		third.setTc(tc);
		third.setLymj(f);
		third.setL(l);
		third.setJ(j);
		third.setM(m);
		
		//计算QM
		double qm = this.formateDouble(q*m, 2);
		//计算QM/F
		double qmf = f>0?this.formateDouble(q*m/f, 3):0;
		third.setQm(qm);
		third.setQmf(qmf);
		hresultThirdDao.saveOrUpdate(third);
		
		//回显
		table2Data.put("QM", qm);
		table2Data.put("QMF", qmf);
		//是否保存表3数据
		
		table.put("table2Data", table2Data);
		table.put("reflag", "1");
		return table;
	}
	public void deleteHuiliuStep2Result(String stcd,String pch){
		hresultSecondDao.deleteById(stcd+pch);
	}
	/**
	 * 第二步计算m
	 * @param data
	 * @return
	 */
	public JSONObject calcHuiliuStep2Table2(String data){
		JSONObject table = new JSONObject();
		if(data==null ||CommonUtil.trim(data).length()<1){
			table.put("reflag", "0");
			table.put("message", "缺少数据!");
			return null;
		}
		JSONObject dataJson = JSONObject.fromObject(data);
		String stcd = dataJson.getString("STCD");
		String pch = dataJson.getString("PCH");
		double tc = dataJson.getDouble("TC");
		double n = dataJson.getDouble("N");
		
		//
		JSONObject table2Data = dataJson.getJSONObject("table2Data");
		double q = table2Data.getDouble("Q");
		q=this.formateDouble(q, 2);
		table2Data.put("Q", q);
		double r = table2Data.getDouble("R");
		r=this.formateDouble(r, 2);
		table2Data.put("R", r);
		double tj = table2Data.getDouble("TJ");
		tj=this.formateDouble(tj, 2);
		table2Data.put("TJ", tj);
		double rj = table2Data.getDouble("RJ");
		rj=this.formateDouble(rj, 2);
		table2Data.put("RJ", rj);
		double f = table2Data.getDouble("LLMJ");
		double l = table2Data.getDouble("L");
		double j = table2Data.getDouble("J");
		double m = table2Data.getDouble("M");
		m=this.formateDouble(m, 2);
		table2Data.put("M", m);
		double k = table2Data.getDouble("K");
		k=this.formateDouble(k, 2);
		table2Data.put("K", k);
		table2Data.put("N", n);
		
		HResultSecond second = new HResultSecond();
		second.setNm(stcd+pch);
		second.setStcd(stcd);
		second.setPch(pch);
		second.setQ(q);
		second.setRs(r);
		second.setTcj(tj);
		second.setTc(tc);
		second.setRsj(rj);
		second.setLymj(f);
		second.setL(l);
		second.setJ(j);
		second.setM(m);
		second.setK(k);
		second.setN(n);
		double ertc = tc!=0?this.formateDouble(r/tc, 2):0;
		second.setErtc(ertc);
		//计算τ=(0.278FR上)/Qm 
		double js1=table2Data.getDouble("JS1");
		js1=this.formateDouble(js1, 2);
		//计算(R上均^n)/(tc均^0.4 )
		double js2=table2Data.getDouble("JS2");
		js2=this.formateDouble(js2, 2);
		//计算Qm/K
		double js3=table2Data.getDouble("JS3");
		js3=this.formateDouble(js3, 2);
		
		second.setJs1(js1);
		second.setJs2(js2);
		second.setQmk(js3);
		table2Data.put("JS1", js1);
		table2Data.put("JS2", js2);
		table2Data.put("JS3", js3);
		hresultSecondDao.saveOrUpdate(second);
		table.put("table2Data", table2Data);
		table.put("reflag", "1");
		return table;
	}
	/**
	 * 第二步第一个表格计算
	 * @param stcd
	 * @param pch
	 * @param rj
	 * @param tj
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Hashtable calcHuiliuStep2Table1(String stcd,String pch,String rj,String tj){
		Hashtable table = new Hashtable();
		Map table2Map = new HashMap();
		//求Q净
		List<Map> table1List = resultFourthDao.getHuiLiuStep1Table1(stcd,pch);
		if(table1List==null || table1List.size()<1){
			table.put("reflag", "0");
			table.put("message", "缺少流量数据!");
			return null;
		}
		double maxQ = this.jisuanQjinzhi(table1List);
		//统计R上和
		table2Map = this.resultFourthDao.getHuiliuStep2Calc1Rhj(stcd,pch);
		if(table2Map==null){
			table.put("reflag", "0");
			table.put("message", "缺少R数据!");
			return null;
		}
		table2Map.put("Q", maxQ);
		table2Map.put("STCD", stcd);
		table2Map.put("PCH", pch);
		table2Map.put("RJ", rj);
		table2Map.put("TJ", tj);
		table.put("table2Data", table2Map);
		table.put("reflag", "1");
		return table;
	}
	public void deleteHuiliuStep3Result(String stcd,String pch){
		hresultThirdDao.deleteById(stcd+pch);
	}
	public Hashtable<String,Object> saveHuiliuStep3Result(HResultThrid resultThird){
		Hashtable<String,Object> table = new Hashtable<String,Object>();
		try {
			resultThird.setNm(resultThird.getStcd()+resultThird.getPch());
			hresultThirdDao.saveOrUpdate(resultThird);
			String stcd=resultThird.getStcd();
			String pch=resultThird.getPch();
			table.put("reflag", "1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			table.put("reflag", "0");
			table.put("message", "保存失败!");
		}
		return table;
	}
	/**
	 * 查询汇流计算第二步的结果
	 * @param stcd
	 * @param pch
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Hashtable<String,Object> queryHuiliuStep2InitData(String stcd,String pch){
		Hashtable<String,Object> table = new Hashtable<String,Object>();
		try {
			HResultThrid resultThird=this.hresultThirdDao.findByStcdAndPch(stcd,pch);
			if(resultThird==null || CommonUtil.trim(resultThird.getNm()).length()<1){
				resultThird=new HResultThrid();
				resultThird.setStcd(stcd);
				resultThird.setPch(pch);
				HResultSecond resultSecond = this.hresultSecondDao.findByStcdAndPch(stcd, pch);
				if(resultSecond!=null && CommonUtil.trim(resultSecond.getNm()).length()>0){
					resultThird.setRsj(resultSecond.getRsj());
					resultThird.setTcj(resultSecond.getTcj());
					double qm = resultSecond.getQmk()*resultSecond.getK();
					resultThird.setQm(qm);
					resultThird.setM(resultSecond.getM());
					resultThird.setLymj(resultSecond.getLymj());
					resultThird.setL(resultSecond.getL());
					resultThird.setJ(resultSecond.getJ());
				}else{
					Map lymjMap = this.resultFourthDao.getHuiLiuStep1Lymj(stcd, pch);
					if(lymjMap!=null && lymjMap.get("LLMJ")!=null){
						resultThird.setLymj(Double.valueOf(lymjMap.get("LLMJ").toString()).doubleValue());
					}
					Map lastBean = this.hresultDao.queryLastLJvalueBean(stcd);
					if(lastBean!=null && CommonUtil.trim(lastBean.get("L")).length()>0){
						resultThird.setL(Double.valueOf(CommonUtil.trim(lastBean.get("L"))));
					}
					if(lastBean!=null && CommonUtil.trim(lastBean.get("J")).length()>0){
						resultThird.setJ(Double.valueOf(CommonUtil.trim(lastBean.get("J"))));
					}
				}
			}
			table.put("table1Data", resultThird);
			List<Map> subData=this.resultFourthDao.queryHuiliuStep3SubmitData(stcd, pch);
			if(subData!=null && subData.size()>0){
				Map lymjMap = this.resultFourthDao.getHuiLiuStep1Lymj(stcd, pch);
				double lymj = lymjMap!=null && lymjMap.get("LYMJ")!=null?CommonUtil.getFloatValue(lymjMap.get("LYMJ").toString()):0d;
				double rs=calcChanliuStep3Rs(stcd,pch,lymj);
				double rx=calcChanliuStep4Rx(stcd,pch,lymj);
				for(int i=0;i<subData.size();i++){
					Map map = subData.get(i);
					if(map!=null && map.get("Rt")!=null){
						double rt = CommonUtil.getFloatValue(map.get("Rt").toString());
						rt=rt-(1-(rs-rx)/rs)*rt;
						rt=new BigDecimal(rt).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
						map.put("Rt", rt);
					}
				}
			}
			table.put("subData", subData);
			//计算结果
			table.put("table3Data", (resultThird.getJsjg()==null?"":resultThird.getJsjg()));
			Map sjjgMap = resultFourthDao.getSjjgByStcdAndPch(stcd, pch);
			int sjjgfz = (sjjgMap != null && sjjgMap.get("SJJG") != null)
					? CommonUtil.getIntValue(sjjgMap.get("SJJG").toString()) : 0;
			table.put("sjjg", sjjgfz);//时间间隔
			//查询流量
			List<Map> qlist=resultThridDao.queryHuiliuStep3QsData(stcd,pch);
			table.put("thirdData", qlist);
			//
			table.put("reflag", "1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			table.put("reflag", "0");
			table.put("message", "获取初始化数据出错！");
		}
		return table;
	}
	/**
	 * 查询汇流计算第二步数据
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Hashtable queryHuiliuStep2Data(String stcd,String pch){
		Hashtable table = new Hashtable();
		List<Map> table1Data=this.resultFourthDao.getHuiliuStep2Table1(stcd, pch);
		if(table1Data!=null && table1Data.size()>0){
			Map lymjMap = this.resultFourthDao.getHuiLiuStep1Lymj(stcd, pch);
			double lymj = lymjMap!=null && lymjMap.get("LYMJ")!=null?CommonUtil.getFloatValue(lymjMap.get("LYMJ").toString()):0d;
			double rs=calcChanliuStep3Rs(stcd,pch,lymj);
			double rx=calcChanliuStep4Rx(stcd,pch,lymj);
			for(int i=0;i<table1Data.size();i++){
				Map map = table1Data.get(i);
				if(map!=null && map.get("Rt")!=null){
					double rt = CommonUtil.getFloatValue(map.get("Rt").toString());
					rt=rt-(1-(rs-rx)/rs)*rt;
					rt=new BigDecimal(rt).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
					map.put("Rt", rt);
				}
			}
		}
		table.put("table1Data", table1Data);
		//显示需要计算m的数据表格
		List<Map> table2DataList = resultFourthDao.getHuiLiuStep1Table2(stcd,pch);
		// 接口需要调用的计算TC的数据集合
		// 获取时间间隔
		Map sjjgMap = resultFourthDao.getSjjgByStcdAndPch(stcd, pch);
		int sjjgfz = (sjjgMap != null && sjjgMap.get("SJJG") != null)
				? CommonUtil.getIntValue(sjjgMap.get("SJJG").toString()) : 0;
		List<Map> subDataList = new ArrayList<Map>();
		double rhj = 0d;
		if (table2DataList != null && table2DataList.size() > 0) {
			for (int i = 0; i < table2DataList.size(); i++) {
				Map map2 = table2DataList.get(i);
				String sjhj = this.division((i + 1) * sjjgfz, 60);
				rhj = rhj + ((map2 != null && map2.get("R") != null)
						? CommonUtil.getFloatValue(map2.get("R").toString()) : 0d);
				String pj = CommonUtil.getFloatValue(sjhj) > 0 ? this.division(rhj, CommonUtil.getFloatValue(sjhj))
						: "0";
				// 接口传递数据集合
				Map sd = new HashMap();
				sd.put("t", sjhj);
				sd.put("Rt", pj);
				subDataList.add(sd);
			}
		}
		table.put("submitData", subDataList);
		
		Map table2Map = new HashMap();
		HResultSecond hresultSecond=hresultSecondDao.findByStcdAndPch(stcd, pch);
		//以前未计算过则查找计算m的条件
		if(hresultSecond==null || CommonUtil.trim(hresultSecond.getNm()).length()<1){
			//求Q净
			List<Map> table1List = resultFourthDao.getHuiLiuStep1Table1(stcd,pch);
			if(table1List==null || table1List.size()<1){
				table.put("reflag", "0");
				table.put("message", "缺少流量数据!");
				return table;
			}
			double maxQ = this.jisuanQjinzhi(table1List);
			//统计R上和
			table2Map = this.resultFourthDao.getHuiliuStep2Calc1Rhj(stcd,pch);
			if(table2Map==null){
				table.put("reflag", "0");
				table.put("message", "缺少R数据!");
				return table;
			}
			Map lastBean = this.hresultDao.queryLastLJvalueBean(stcd);
			table2Map.put("Q", maxQ);
			table2Map.put("STCD", stcd);
			table2Map.put("PCH", pch);
			table2Map.put("SJJG", sjjgfz);
			table2Map.put("TC", "");
			table2Map.put("L", ((lastBean==null || lastBean.get("L")==null || lastBean.get("L").toString().length()<1)?"":lastBean.get("L").toString()));
			table2Map.put("J",((lastBean==null || lastBean.get("J")==null || lastBean.get("J").toString().length()<1)?"":lastBean.get("J").toString()));
		}else{
			table2Map.put("Q", hresultSecond.getQ());
			table2Map.put("STCD", stcd);
			table2Map.put("PCH", pch);
			table2Map.put("SJJG", sjjgfz);
			table2Map.put("R", hresultSecond.getRs());
			table2Map.put("TJ", hresultSecond.getTcj());
			table2Map.put("RJ", hresultSecond.getRsj());
			table2Map.put("LLMJ", hresultSecond.getLymj());
			table2Map.put("L",hresultSecond.getL());
			table2Map.put("J", hresultSecond.getJ());
			table2Map.put("JS1", hresultSecond.getJs1());
			table2Map.put("K", hresultSecond.getK());
			table2Map.put("JS2", hresultSecond.getJs2());
			table2Map.put("JS3", hresultSecond.getQmk());
			table2Map.put("M", hresultSecond.getM());
			table2Map.put("TC",	hresultSecond.getTc());
			table2Map.put("N", hresultSecond.getN());
		}
		table.put("table2Data", table2Map);
		return table;
	}
	/**
	 * 查询第二步第一个表格数据
	 * @param stcd
	 * @param pch
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(propagation=Propagation.REQUIRED)
	public Hashtable queryHuiliuStep2Table1(String stcd,String pch){
		Hashtable table = new Hashtable();
		List<Map> table1Data=this.resultFourthDao.getHuiliuStep2Table1(stcd, pch);
		table.put("table1Data", table1Data);
		//显示需要计算m的数据表格
		List<Map> table2DataList = resultFourthDao.getHuiLiuStep1Table2(stcd,pch);
		// 接口需要调用的计算TC的数据集合
		// 获取时间间隔
		Map sjjgMap = resultFourthDao.getSjjgByStcdAndPch(stcd, pch);
		int sjjgfz = (sjjgMap != null && sjjgMap.get("SJJG") != null)
				? CommonUtil.getIntValue(sjjgMap.get("SJJG").toString()) : 0;

		List<Map> subDataList = new ArrayList<Map>();
		double rhj = 0d;
		if (table2DataList != null && table2DataList.size() > 0) {
			for (int i = 0; i < table2DataList.size(); i++) {
				Map map2 = table2DataList.get(i);
				String sjhj = this.division((i + 1) * sjjgfz, 60);
				rhj = rhj + ((map2 != null && map2.get("R") != null)
						? CommonUtil.getFloatValue(map2.get("R").toString()) : 0d);
				String pj = CommonUtil.getFloatValue(sjhj) > 0 ? this.division(rhj, CommonUtil.getFloatValue(sjhj))
						: "0";
				// 接口传递数据集合
				Map sd = new HashMap();
				sd.put("t", sjhj);
				sd.put("Rt", pj);
				subDataList.add(sd);
			}
		}
		table.put("submitData", subDataList);
		
		Map table2Map = new HashMap();
		//求Q净
		List<Map> table1List = resultFourthDao.getHuiLiuStep1Table1(stcd,pch);
		if(table1List==null || table1List.size()<1){
			table.put("reflag", "0");
			table.put("message", "缺少流量数据!");
			return table;
		}
		double maxQ = this.jisuanQjinzhi(table1List);
		//统计R上和
		table2Map = this.resultFourthDao.getHuiliuStep2Calc1Rhj(stcd,pch);
		if(table2Map==null){
			table.put("reflag", "0");
			table.put("message", "缺少R数据!");
			return table;
		}
		table2Map.put("Q", maxQ);
		table2Map.put("STCD", stcd);
		table2Map.put("PCH", pch);
		table2Map.put("SJJG", sjjgfz);
		table.put("table2Data", table2Map);
		return table;
	}
	/**
	 * 计算汇流第二步测站下拉框数据
	 * @return
	 */
	public List<Map> getHuiliuStep2StcdSelect(String searchName){
		return this.resultFourthDao.getHuiLiuStep2StcdSelect(searchName);
	}
	/**
	 * 计算汇流第二步测站相关的批次号下拉框数据
	 * @return
	 */
	public List<Map> getHuiliuStep2PchSelect(String stcd){
		return this.resultFourthDao.getHuiLiuStep2PchSelect(stcd);
	}
	/**
	 * 汇流计算第二步的下拉框数据查询
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(propagation=Propagation.REQUIRED)
	public Hashtable getHuiliuStep2Select(){
		Hashtable table = new Hashtable();
		List<Map> step1StcdSelect = this.resultFourthDao.getHuiLiuStep2StcdSelect(null);
		List<Map> step1PchSelect = this.resultFourthDao.getHuiLiuStep2PchSelect(null);
		table.put("stcdSelect", step1StcdSelect);
		table.put("pchSelect", step1PchSelect);
		return table;
	}
}
