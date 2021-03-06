package com.lyht.business.analysisCalculation.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lyht.base.hibernate.common.PageResults;
import com.lyht.business.analysisCalculation.bean.Result;
import com.lyht.business.analysisCalculation.bean.ResultFifth;
import com.lyht.business.analysisCalculation.bean.ResultFourth;
import com.lyht.business.analysisCalculation.bean.ResultJg;
import com.lyht.business.analysisCalculation.bean.ResultSecond;
import com.lyht.business.analysisCalculation.bean.ResultSixthResult;
import com.lyht.business.analysisCalculation.dao.ResultDao;
import com.lyht.business.analysisCalculation.dao.ResultFifthDao;
import com.lyht.business.analysisCalculation.dao.ResultFourthDao;
import com.lyht.business.analysisCalculation.dao.ResultJgDao;
import com.lyht.business.analysisCalculation.dao.ResultSecondDao;
import com.lyht.business.analysisCalculation.dao.ResultSecondQlDao;
import com.lyht.business.analysisCalculation.dao.ResultSixthDao;
import com.lyht.business.analysisCalculation.dao.ResultSixthResultDao;
import com.lyht.business.analysisCalculation.dao.ResultThirdLessDao;
import com.lyht.business.analysisCalculation.dao.ResultThridDao;
import com.lyht.business.analysisCalculation.dao.StPptnDayRDao;
import com.lyht.business.analysisCalculation.formbean.ResultFifthFormBean;
import com.lyht.business.analysisCalculation.formbean.ResultFormBean;
import com.lyht.business.analysisCalculation.formbean.ResultFourthFormBean;
import com.lyht.business.analysisCalculation.formbean.ResultThridFormBean;
import com.lyht.business.consumer.hydrologicalData.bean.River;
import com.lyht.business.consumer.hydrologicalData.bean.Stbprp;
import com.lyht.business.consumer.hydrologicalData.dao.RiverDao;
import com.lyht.business.consumer.hydrologicalData.dao.StbprpDao;
import com.lyht.business.consumer.hydrologicalData.formbean.RiverFormBean;
import com.lyht.business.consumer.hydrologicalData.formbean.TsqxFormBean;
import com.lyht.business.system.bean.SysStaff;
import com.lyht.util.CommonUtil;
import com.lyht.util.DateUtil;
import com.lyht.util.ExcelUtils;
import com.lyht.util.Line;
import com.lyht.util.LineDifference;
import com.lyht.util.Point;
import com.lyht.util.Randomizer;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 *作者： 刘魁
 *脚本日期:2018年6月4日 15:24:11
 *说明:  产流计算结果service
*/
@Service
@Scope("prototype")
@Transactional
public class ResultService {
	@Resource ResultDao resuldDao;
	@Resource RiverDao riverDao;
	@Resource ResultJgDao rjDao;
	@Resource ResultThridDao resultThridDao;
	@Resource ResultThirdLessDao resultThridLessDao;
	@Resource ResultFourthDao resultFourthDao;
	@Resource
	private ResultSixthDao rxDao;
	@Resource
	private ResultFifthDao rfDao;
	@Resource
	private ResultSecondDao rsDao;
	@Resource 
	private ResultSecondQlDao resultSecondQlDao;
	@Resource 
	private ResultSixthResultDao resultSixthResultDao;
	@Resource private StbprpDao stbprpDao;
	@Resource private StPptnDayRDao stpptndayrDao;
	
	/**
	 * 删除整场洪水信息
	 * @param stcd
	 * @param params
	 */
	@Transactional
	public void deleteAllDataResult(String stcd,String params){
		JSONArray dataList = JSONArray.fromObject(params);
		String pchs="";
		if(dataList!=null && dataList.size()>0){
			for(int i=0;i<dataList.size();i++){
				JSONObject json = dataList.getJSONObject(i);
				if(json.containsKey("PCH")){
					if(pchs!=null && pchs.trim().length()>0){
						pchs=pchs+",'"+json.getString("PCH")+"'";
					}else{
						pchs="'"+json.getString("PCH")+"'";
					}
				}
			}
		}
		if(pchs!=null && pchs.trim().length()>0){
			String[] tableList = new String[]{"C_RESULT","C_FIFTH_PA","C_RESULT_FIFTH",
					"C_RESULT_FOURTH","C_RESULT_JG","C_RESULT_SECOND", "C_RESULT_SECOND_QL",
					"C_RESULT_SIX_PPA","C_RESULT_SIXTH","C_RESULT_SIXTH_RESULT","C_RESULT_THRID",
					"C_RESULT_THRID_LESS","H_RESULT","H_RESULT_FOURTH", "H_RESULT_FOURTH_E", 
					"H_RESULT_FOURTH_Q","H_RESULT_FOURTH_ZHCX","H_RESULT_SECOND",
					"H_RESULT_SECOND_ZHCX","H_RESULT_THIRD_ZHCX","H_RESULT_THRID","H_RESULT_ZHCX"};
			for(int i=0;i<tableList.length;i++){
				resultSixthResultDao.deleteTableData(tableList[i], stcd, pchs);
			}
		}
	}
	/**
	 * 删除产流计算第三步计算结果
	 * @param stcd
	 * @param pch
	 */
	@Transactional
	public void deleteStep3Result(String stcd,String pch){
		resultThridDao.deleteResultByStcdAndPch(stcd, pch);
		resultThridLessDao.deleteByStcdAndPch(stcd, pch);
		ResultSecond rs = rsDao.queryResultSecondByStcdAndPch(stcd, pch);
		rs.setRs(null);
		rs.setSqt3(null);
		rsDao.saveOrUpdate(rs);
	}
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public List<Map> queryStep1Jyl(String stcd,String start,String end,String interval){
			List<Map> resultList = new ArrayList<Map>();
			start=start+":00";
			end=end+":00";
			String searchStart=CommonUtil.getAfterMinitsDateTime(start, -Integer.valueOf(interval));
			searchStart=searchStart.substring(0, 16);//输入的查询开始时间的前一个间隔开始时间作为查询数据的开始时间
			List<Map> jylList = this.resuldDao.queryStep1Jyl(stcd, searchStart, end);
			end=CommonUtil.getAfterMinitsDateTime(end, Integer.valueOf(interval));//扩大一个时间间隔
			String endTime=start;//
			String startTime=searchStart+":00";
			while(CommonUtil.compareLessDateTime(endTime, end)){
				Map map = new HashMap();
				double drp=getJylData(startTime,endTime,jylList);
				map.put("DT", endTime.subSequence(0, 16));
				map.put("DRP", drp);
				resultList.add(map);
				startTime=endTime;
				endTime=CommonUtil.getAfterMinitsDateTime(endTime, Integer.valueOf(interval));
			}
			return resultList;
		}
		@SuppressWarnings("rawtypes")
		private double getJylData(String start,String end,List<Map> jylList){
			double result=0;
			if(jylList!=null && jylList.size()>0){
				for(int i=0;i<jylList.size();i++){
					Map map = jylList.get(i);
					if(map!=null && CommonUtil.trim(map.get("BTM")).length()>0 
							&& CommonUtil.trim(map.get("STM")).length()>0){
						String btm = CommonUtil.trim(map.get("BTM"));
						String stm = CommonUtil.trim(map.get("STM"));
						btm=btm+":00";
						stm=stm+":00";
						double drp=0;
						if(CommonUtil.trim(map.get("DRP")).length()>0){
							drp=CommonUtil.getFloatValue(CommonUtil.trim(map.get("DRP")));
						}
						if(CommonUtil.compareMoreDateTime(start, btm)
								&& CommonUtil.compareLessDateTime(end, stm)){
							//[3,7]在[0,8]之中的情况结果为[3,7]
							long interval=CommonUtil.getDifferenceTime(start, end);
							long sjc=CommonUtil.getDifferenceTime(btm, stm);
							result = drp*interval/sjc;
							double val=new BigDecimal(result).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();//保留2位小数
							return val;
						}else if(CommonUtil.compareMoreDateTime(start, btm)
								&& !CommonUtil.compareLessDateTime(end, stm)
								&& !CommonUtil.compareLessDateTime(stm, start)){
							//[3,7]在[2,6]结果为[3,6]
							long interval=CommonUtil.getDifferenceTime(start, stm);
							long sjc=CommonUtil.getDifferenceTime(btm, stm);
							result = result+drp*interval/sjc;
						}else if(!CommonUtil.compareMoreDateTime(start, btm)
								&& CommonUtil.compareMoreDateTime(end, stm)){
							//[3,7]在[4,6]结果为[4,6]
							result=result+drp;
						}else if(!CommonUtil.compareMoreDateTime(start, btm)
								&& !CommonUtil.compareMoreDateTime(end, stm)
								&& !CommonUtil.compareLessDateTime(end, btm)){
							//[3,7]在[4,8]结果为[4,7]
							long interval=CommonUtil.getDifferenceTime(btm, end);
							long sjc=CommonUtil.getDifferenceTime(btm, stm);
							result = result+drp*interval/sjc;
							double val=new BigDecimal(result).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();//保留2位小数
							return val;
						}
					}
				}
			}
			double val=new BigDecimal(result).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();//保留2位小数
			return val;
		}
		/**
		 * 保存计算结果
		 */
		@Transactional(propagation=Propagation.REQUIRED)
		public Result saveResult(Result result,Result result1,String result11,String INTERVAL,String stcds,String stnms,String start,String end) {
			resuldDao.delResult(result);//先删除这一批次号
			String [] jyl=result.getJyl().split(",");
			String [] yml=result11.split(",");
			String [] ymlTime=result.getYmlTime().split(",");
			String [] stnm=result.getStnm().split(",");
			String [] qz=result.getQz().split(",");
			Object [] arr=splitAry(jyl, qz.length); //切割后的降雨量
			ResultJg t = rjDao.get(result.getStcd()+result.getPch());
			if(t==null || CommonUtil.trim(t.getId()).length()<1){
				t=new ResultJg();  //ResultJg save
				t.setId(result.getStcd()+result.getPch()); //ID
			}
			t.setPch(result.getPch());	 //PCH
			t.setSTCD(result.getStcd()); 	//STCD
			t.setSJJG(Integer.parseInt(INTERVAL)); // SJJG 
			t.setStcds(stcds);
			t.setStnms(stnms);
			t.setQzs(result.getQz());
			t.setKssj(start);
			t.setJssj(end);
			rjDao.saveOrUpdate(t);
			try {
				 ArrayList<String> jyList=new    ArrayList<String>();
					for(Object obj: arr){//打印输出结果  
						   String[] aryItem = (String[]) obj;  
							ArrayList<String> list=new    ArrayList<String>();
							   for(int i=0;i<aryItem.length;i++) {
								    list.add(aryItem[i].toString());
							   }
							   jyList.add(list.toString());
						  }  
							String staffCode=result.getCreateStaff();
							for(int i=0;i<ymlTime.length;i++) {
									String id=Randomizer.nextNumber("r", 7);
									Result result2=new Result();
									String jString=jyList.get(i).replace("[", "").trim();
									String  jString2=jString.replace("]", "").trim();
									result2.setJyl(jString2.trim()); //降雨量
									result2.setQz(result.getQz());//权重
									result2.setStnm(result.getStnm());//测站名称
									double ymlvalue=0;
									if(yml[i]!=null && yml[i].trim().length()>0){
										ymlvalue=Double.parseDouble(yml[i].trim());
									}
									result2.setYml(ymlvalue);//雨面量
									result2.setYmlTime(ymlTime[i].trim());//雨面量时间
									result2.setId(id); //ID
									result2.setCreateStaff(staffCode); //用户编码
									result2.setStcd(result.getStcd());
									result2.setPch(result.getPch());//批次号
							resuldDao.saveResult(result2);
						}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result1;
		}
		/**
		 * 导出到excel
		 */
		@Transactional(propagation=Propagation.REQUIRED)
		public void export(ResultFormBean resultFormBean,PageResults prs,HttpServletRequest request,HttpServletResponse response) throws IOException{
			String year = DateUtil.getYear();//年
			String method = DateUtil.getMonth();//月
			String day = DateUtil.getDay();//日
			String replace="";
			String title="产流计算"+resultFormBean.getResultBean().getPch();
			String [] tab = {"序号","日期"};
			String [] val = {"YML_TIME","JYL","YML"};
			String[] yStrings= {"雨面量"};
		
			List <HashMap<String, Object>> result=resuldDao.getHistory(resultFormBean).getResults();
			ResultFifthFormBean resultFormBean1=new ResultFifthFormBean();
			ResultFifth rf=new ResultFifth();
			rf.setPch(resultFormBean.getResultBean().getPch());
			rf.setStcd(resultFormBean.getResultBean().getStcd());
			resultFormBean1.setResultFifthFormBean(rf);
			List <HashMap<String, Object>> result2=rfDao.getFif(resultFormBean1).getResults();
				 String name=(String) result.get(0).get("STNM");
				 String qz=(String) result.get(0).get("QZ");//权重
				 String[] nStrings	 =name.split(",");//测站名称
				 String[] qzArr=qz.split(",");
				 
				   List list = new ArrayList(Arrays.asList(nStrings));
			        list.addAll(Arrays.asList(yStrings));
			        Object [] c =  list.toArray();//后半部tab
			        String file =null;
			        List <HashMap<String, Object>>list2 = new ArrayList<HashMap<String, Object>>();
			        if(resultFormBean.getResultBean().getId().equals("5")) {//5s
			        	for(int i=0;i<result2.size();i++) {
			        		HashMap<String, Object> map=result2.get(i);
			        		list2.add(map);
			        	}
						String [] val5 = {"DT","JYL","YML","EM","PA"};
						String[] yStrings5= {"雨面量","Em","Pa"};
						  list.addAll(Arrays.asList(yStrings5));
						  title="产流计算第五步"+resultFormBean.getResultBean().getPch();
						 file = ExcelUtils.exportChanLiuExcel5(list2,c,qzArr, request, replace,tab,title,val5);//第五步导出
					}else {
						 file = ExcelUtils.exportChanLiuExcel(result,c,qzArr, request, replace,tab,title,val);//第一步导出
					}
			
			
			response.setContentType("multipart/form-data");  
			String path = request.getSession().getServletContext().getRealPath("/")+file;
			response.setHeader("Content-Disposition", "attachment;fileName="+new String(file.getBytes("UTF-8"),"ISO8859-1"));
	        //通过文件路径获得File对象(假如此路径中有一个download.pdf文件)  
	        File files = new File(path);
	        FileInputStream inputStream = new FileInputStream(files);
	        ServletOutputStream out= response.getOutputStream();
	        int b = 0;  
	        byte[] buffer = new byte[1024];  
	        while (b != -1){  
	            b = inputStream.read(buffer);  
	            //4.写到输出流(out)中  
	            out.write(buffer,0,b);  
	        }  
	        inputStream.close();  
	        out.close();  
	        out.flush();
		}
		
		@Transactional(propagation=Propagation.REQUIRED)
		public void export2(ResultFormBean resultFormBean,String s,String end,PageResults prs,HttpServletRequest request,HttpServletResponse response) throws IOException{
			String year = DateUtil.getYear();//年
			String method = DateUtil.getMonth();//月
			String day = DateUtil.getDay();//日
			String replace="";
			String title="产流计算第二步"+resultFormBean.getResultBean().getPch();
			String [] tab = {"序号","日期","降雨量","流量"};
			String [] val = {"DATE","JYL","LL"};
			List list=resuldDao.getSecondQl(resultFormBean);//先查询C_RESULT_SECOND_QL
			String file=null;
			if(list.size()>0) {//如果有数据
				file= ExcelUtils.SellerStat2Excel(list, request, replace,tab,title,val);
			}else {//没有数据
				List <HashMap<String, Object>> result=resuldDao.step2(resultFormBean,s,end).getResults();
				 file = ExcelUtils.SellerStat2Excel(result, request, replace,tab,title,val);
			}
			response.setContentType("multipart/form-data");  
			String path = request.getSession().getServletContext().getRealPath("/")+file;
			response.setHeader("Content-Disposition", "attachment;fileName="+new String(file.getBytes("UTF-8"),"ISO8859-1"));
	        //通过文件路径获得File对象(假如此路径中有一个download.pdf文件)  
	        File files = new File(path);
	        FileInputStream inputStream = new FileInputStream(files);
	        ServletOutputStream out= response.getOutputStream();
	        int b = 0;  
	        byte[] buffer = new byte[1024];  
	        while (b != -1){  
	            b = inputStream.read(buffer);  
	            //4.写到输出流(out)中  
	            out.write(buffer,0,b);  
	        }  
	        inputStream.close();  
	        out.close();  
	        out.flush();
		}
		@Transactional(propagation=Propagation.REQUIRED)
		public void export3(ResultThridFormBean resultThrid,String hj,String rs,String s,String end,PageResults prs,HttpServletRequest request,HttpServletResponse response) throws IOException{
			String year = DateUtil.getYear();//年
			String method = DateUtil.getMonth();//月
			String day = DateUtil.getDay();//日
			String replace="";
			String title="产流计算第三步"+resultThrid.getResultThrid().getPch();
			String [] tab = {"序号","日期","时差(分钟)","流量(m³/s)","Q*T(m³)"};
			String [] val = {"DATE","SC","LL","QT"};
			List <HashMap<String, Object>> result=resultThridDao.getStep3(resultThrid).getResults();
			String file = ExcelUtils.exportCl3(result,hj,rs, request, replace,tab,title,val);
			response.setContentType("multipart/form-data");  
			String path = request.getSession().getServletContext().getRealPath("/")+file;
			response.setHeader("Content-Disposition", "attachment;fileName="+new String(file.getBytes("UTF-8"),"ISO8859-1"));
	        //通过文件路径获得File对象(假如此路径中有一个download.pdf文件)  
	        File files = new File(path);
	        FileInputStream inputStream = new FileInputStream(files);
	        ServletOutputStream out= response.getOutputStream();
	        int b = 0;  
	        byte[] buffer = new byte[1024];  
	        while (b != -1){  
	            b = inputStream.read(buffer);  
	            //4.写到输出流(out)中  
	            out.write(buffer,0,b);  
	        }  
	        inputStream.close();  
	        out.close();  
	        out.flush();
		}
		public Hashtable<String,Object> exportChanliuStep4Excel4(String pch,String data,HttpServletRequest request){
			Hashtable<String,Object> table =new Hashtable<String,Object>();
			String title="产流计算第四步"+ pch;
			String [] tab = {"序号","日期","时差(分钟)","流量(地下)","Q*T"};
			String [] val = {"DATE","INTERVAL","LL","QT"};
			JSONArray dataList = JSONArray.fromObject(data);
			String file = ExcelUtils.exportChanliuStep4Excel(dataList, request, pch, tab,title,val);
			table.put("reflag", "1");
			table.put("fileUrl", file);
			return table;
		}
		@Transactional(propagation=Propagation.REQUIRED)
		public void export4(ResultFourth rFourth,String hj,String rs,String s,String end,PageResults prs,HttpServletRequest request,HttpServletResponse response) throws IOException{
			String year = DateUtil.getYear();//年
			String method = DateUtil.getMonth();//月
			String day = DateUtil.getDay();//日
			String replace="";
			String title="产流计算第四步"+ rFourth.getPch();
			String [] tab = {"序号","日期","时差(分钟)","流量(地下)","Q*T"};
			String [] val = {"DATE","SC","LL","QT"};
			ResultFourthFormBean resultFourth=new ResultFourthFormBean ();
			resultFourth.setResultFourth(rFourth);
			List <HashMap<String, Object>> result=resultFourthDao.getStep4Table(resultFourth).getResults();
			String file = ExcelUtils.exportCl4(result,hj,rs, request, replace,tab,title,val);
			response.setContentType("multipart/form-data");  
			String path = request.getSession().getServletContext().getRealPath("/")+file;
			response.setHeader("Content-Disposition", "attachment;fileName="+new String(file.getBytes("UTF-8"),"ISO8859-1"));
	        //通过文件路径获得File对象(假如此路径中有一个download.pdf文件)  
	        File files = new File(path);
	        FileInputStream inputStream = new FileInputStream(files);
	        ServletOutputStream out= response.getOutputStream();
	        int b = 0;  
	        byte[] buffer = new byte[1024];  
	        while (b != -1){  
	            b = inputStream.read(buffer);  
	            //4.写到输出流(out)中  
	            out.write(buffer,0,b);  
	        }  
	        inputStream.close();  
	        out.close();  
	        out.flush();
		}
		
		@Transactional(propagation=Propagation.REQUIRED)
		public void export6(Map map,Map map1,String stcd,String pch,String start,String end,PageResults prs,HttpServletRequest request,HttpServletResponse response) throws IOException{
			String year = DateUtil.getYear();//年
			String method = DateUtil.getMonth();//月
			String day = DateUtil.getDay();//日
			String replace="";
			String title="产流计算第六步"+pch;
			String [] tab = {"序号","日期","P","E雨","∑(P-E雨)","Pa+∑(P-E雨)","∑R查","时段R查","R改","R改大→小","∑R改"};
			String [] val = {"DATE","P","E","EPE","PAPE","ERC","SDRC","R","RGDX","ER"};
			List<Map> table1Data=resuldDao.queryStep6ResultData(stcd, pch);
			table1Data.add(map);
			String file = ExcelUtils.SellerStat2Excel6(table1Data, map1,request, replace,tab,title,val);
			response.setContentType("multipart/form-data");  
			String path = request.getSession().getServletContext().getRealPath("/")+file;
			response.setHeader("Content-Disposition", "attachment;fileName="+new String(file.getBytes("UTF-8"),"ISO8859-1"));
	        //通过文件路径获得File对象(假如此路径中有一个download.pdf文件)  
	        File files = new File(path);
	        FileInputStream inputStream = new FileInputStream(files);
	        ServletOutputStream out= response.getOutputStream();
	        int b = 0;  
	        byte[] buffer = new byte[1024];  
	        while (b != -1){  
	            b = inputStream.read(buffer);  
	            //4.写到输出流(out)中  
	            out.write(buffer,0,b);  
	        }  
	        inputStream.close();  
	        out.close();  
	        out.flush();
		}
		
		//第六步单站综合及误差统计导出到excel
		@Transactional(propagation=Propagation.REQUIRED)
		public void chanliu6Excel(List data,PageResults prs,HttpServletRequest request,HttpServletResponse response) throws IOException{
			String year = DateUtil.getYear();//年
			String method = DateUtil.getMonth();//月
			String day = DateUtil.getDay();//日
			String replace="";
			String title="产流计算第六步单站综合及误差统计";
			String [] tab = {"洪号","Tc","Rc","RcTc","FC","FC查","FC-FC查","是否合格"};
			String [] val = {"PCH","TC","RC","RCTC","FC","FCC","FCS","SFHG"};
			String file = ExcelUtils.exportExcel(data, request, replace,tab,title,val);
			response.setContentType("multipart/form-data");  
			String path = request.getSession().getServletContext().getRealPath("/")+file;
			response.setHeader("Content-Disposition", "attachment;fileName="+new String(file.getBytes("UTF-8"),"ISO8859-1"));
	        //通过文件路径获得File对象(假如此路径中有一个download.pdf文件)  
	        File files = new File(path);
	        FileInputStream inputStream = new FileInputStream(files);
	        ServletOutputStream out= response.getOutputStream();
	        int b = 0;  
	        byte[] buffer = new byte[1024];  
	        while (b != -1){  
	            b = inputStream.read(buffer);  
	            //4.写到输出流(out)中  
	            out.write(buffer,0,b);  
	        }  
	        inputStream.close();  
	        out.close();  
	        out.flush();
		}
		@Transactional(propagation=Propagation.REQUIRED)
		public void export7(String stcd,String pch,String start,String end,PageResults prs,HttpServletRequest request,HttpServletResponse response) throws IOException{
			String year = DateUtil.getYear();//年
			String method = DateUtil.getMonth();//月
			String day = DateUtil.getDay();//日
			String replace="";
			String title="产流计算结果"+pch;
			String [] tab = {"序号","洪号","开始时间","结束时间","Q","P","Pa","E雨",
					"Pa+P-E雨","R实","R查","△R","△R/R实","合格否","tc","Rc","Rc/tc","Fc","Fc查","(Fc-Fc查)/Fc" ,"合格否"};
			String [] val = {"PCH","BTM","ETM","Q","P","PA","E"
					,"PAPE","RS","ERC","SJR","SJRBRS","CLSFHG","TC","RC","RCTC","FC","FCC","FCS","XSSFHG"};
			List<Map> table1Data=resuldDao.queryStep7ResultData(stcd);
			String file = ExcelUtils.SellerStat2Excel7(table1Data, request, replace,tab,title,val);
			response.setContentType("multipart/form-data");  
			String path = request.getSession().getServletContext().getRealPath("/")+file;
			response.setHeader("Content-Disposition", "attachment;fileName="+new String(file.getBytes("UTF-8"),"ISO8859-1"));
	        //通过文件路径获得File对象(假如此路径中有一个download.pdf文件)  
	        File files = new File(path);
	        FileInputStream inputStream = new FileInputStream(files);
	        ServletOutputStream out= response.getOutputStream();
	        int b = 0;  
	        byte[] buffer = new byte[1024];  
	        while (b != -1){  
	            b = inputStream.read(buffer);  
	            //4.写到输出流(out)中  
	            out.write(buffer,0,b);  
	        }  
	        inputStream.close();  
	        out.close();  
	        out.flush();
		}
		
		/**
		 * 根据id删除
		 */
		public void delResult(Result result) {
			resuldDao.delResult(result);
		}
		
		/**
		 * 获取计算历史记录条数
		 * @param resultFormBean
		 * @return
		 */
		@Transactional(propagation=Propagation.REQUIRED)
		public  PageResults getSelect(ResultFormBean resultFormBean){
			return resuldDao.getSelect(resultFormBean);
		}
		
		/**
		 * 查询计算历史
		 * @param resultFormBean
		 * @return
		 */
		@Transactional(propagation=Propagation.REQUIRED)
		public  PageResults getHistory(ResultFormBean resultFormBean){
			return resuldDao.getHistory(resultFormBean);
		}
		@SuppressWarnings("rawtypes")
		public Hashtable<String,Object> queryStep1TableDataByStcdAndPch(String stcd,String pch){
			Hashtable<String,Object> table = new Hashtable<String,Object>();
			ResultJg timeJg=rjDao.get(stcd+pch);
			List<Map> resultList=resuldDao.queryStep1DataHistory(stcd,pch);
			table.put("rows", resultList);
			table.put("header", timeJg);
			return table;
		}
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Hashtable<String,Object> queryStep1TableData(String stcd,String pch,String start,String end,String interval,String stcds,String stnms,String qzs){
			//1.判断开始时间，结束时间，时间间隔参数是否发生变化，如果发生变化就重新计算数据，
			//否则判断传入的站点数据是否为空，为空则读取原有计算结果，
			//否则判断传入的站点数据与原有计算站点数据是否一致，如果一致读取原有数据，
			//否则计算新数据
			//如果没有原有数据，则计算新数据
			Hashtable<String,Object> table = new Hashtable<String,Object>();
			List<Map> resultList = new ArrayList<Map>();
			ResultJg timeJg=rjDao.get(stcd+pch);
			if(timeJg!=null && CommonUtil.trim(timeJg.getKssj()).equals(CommonUtil.trim(start))
					&& CommonUtil.trim(timeJg.getJssj()).equals(CommonUtil.trim(end))
					&& timeJg.getSJJG()!=null 
					&& timeJg.getSJJG()==Integer.valueOf(interval)
					&& CommonUtil.trim(timeJg.getStcds()).equals(CommonUtil.trim(stcds))){
				resultList=resuldDao.queryStep1DataHistory(stcd,pch);
			}else{
				resultList=calcNewList(stcd,pch,start,end,interval,stcds);
			}
			if(timeJg==null){
				timeJg=new ResultJg();
			}
			table.put("rows", resultList);
			table.put("header", timeJg);
			return table;
		}
		@SuppressWarnings({ "rawtypes", "unchecked" })
		private List<Map> calcNewList(String stcd,String pch,String start,String end,String interval,String stcds){
			List<Map> resultList = new ArrayList<Map>();
			start=start.trim();
			end=end.trim();
			String searchStart = CommonUtil.getAfterMinitsDateTime(start+":00", -Integer.valueOf(interval));
			searchStart=searchStart.substring(0, 16);
			List<Map> jylList = resuldDao.queryPptnByTime(stcds, searchStart, end+":00");
			if(jylList!=null && jylList.size()>0){
				Hashtable<String,List<Map>> tableData = new Hashtable<String,List<Map>>();
				String bm=null;
				List<Map> bmlist=null;
				for(int i=0;i<jylList.size();i++){
					Map map = jylList.get(i);
					String st = map.get("STCD")!=null?map.get("STCD").toString():"";
					if(i==0){
						bm=st;
						bmlist=new ArrayList<Map>();
					}
					if(bm!=null && bm.length()>0 && bm.equals(st)){
						bmlist.add(map);
					}else if(bm!=null && bm.length()>0 && !bm.equals(st)){
						if(bmlist!=null && bmlist.size()>0){
							tableData.put(bm, bmlist);
							bmlist=new ArrayList<Map>();
							bm=st;
							bmlist.add(map);
						}
					}
				}
				if(bm!=null && bm.length()>0 && bmlist!=null && bmlist.size()>0){
					tableData.put(bm, bmlist);
				}
				String[] stcdList = stcds.split(",");
				if(stcdList!=null && stcdList.length>0){
					String calTime =start+":00"; 
					String startTime=searchStart+":00";
					while(CommonUtil.compareLessDateTime(calTime, end+":00")){
						Map map = new HashMap();
						map.put("STCD", stcd);
						map.put("PCH", pch);
						map.put("DATE", calTime);
						map.put("YML", "");
						String jyl="";
						for(int j=0;j<stcdList.length;j++){
							double drp=0;
							String ss = stcdList[j];
							List<Map> jylArr = tableData.get(ss);
							if(jylArr!=null){
								drp=getJylData(startTime,calTime,jylArr);
							}
							if(j==0){
								jyl=String.valueOf(drp);
							}else{
								jyl=jyl+","+String.valueOf(drp);
							}
						}
						map.put("JYL", jyl);
						resultList.add(map);
						startTime=calTime;
						calTime=CommonUtil.getAfterMinitsDateTime(calTime, Integer.valueOf(interval));
					}
				}
			}
			return resultList;
		}
		/**
		 * 根据方案编码查询方案参数值
		 * @param planCode
		 * @return
		 */
		public Hashtable<String,Object> queryPlanParamValue(String planCode){
			Hashtable<String,Object> table = new Hashtable<String,Object>();
			List<Map> relist = resuldDao.queryPlanParamValue(planCode);
			Map<String,String> params = new HashMap<String,String>();
			if(relist!=null && relist.size()>0){
				for(int i=0;i<relist.size();i++){
					Map pvmap = relist.get(i);
					if(pvmap!=null 
							&& CommonUtil.trim(pvmap.get("PARA_SYMBOL")).length()>0
							&& CommonUtil.trim(pvmap.get("PARA_VALUE")).length()>0){
						params.put(CommonUtil.trim(pvmap.get("PARA_SYMBOL")), CommonUtil.trim(pvmap.get("PARA_VALUE")));
					}
				}
			}
			table.put("paramsValue", params);
			return table;
		}
		@SuppressWarnings("rawtypes")
		public Hashtable<String,Object> queryStep5HistoryData(String stcd,String pch,SysStaff  mSysStaff){
			Hashtable<String,Object> table = new Hashtable<String,Object>();
			List<Map> planList = resuldDao.queryStep5PlanList(stcd,mSysStaff);
			ResultSecond second = rsDao.queryResultSecondByStcdAndPch(stcd, pch);
			table.put("planList", planList);
			if(second!=null){
				table.put("second", second);
			}
			if(second!=null && CommonUtil.trim(second.getEmstcd()).length()>0){
				Stbprp stinfo=this.stbprpDao.get(CommonUtil.trim(second.getEmstcd()));
				table.put("stinfo", stinfo);
			}
			List<Map> step5List = resuldDao.queryStep5History(stcd, pch);
			if(step5List==null || step5List.size()<1){
				step5List = resuldDao.queryStep1History(stcd, pch);
			}
			table.put("rows", step5List);
			return table;
		}
		@SuppressWarnings("rawtypes")
		public Hashtable<String,Object> queryStep5HistoryData(String stcd,String pch,String start,String end,String interval,String stcds,String stnms,String qzs,String emstcd){
			Hashtable<String,Object> table = new Hashtable<String,Object>();
			List<Map> resultList = new ArrayList<Map>();
			ResultJg timeJg=rjDao.get(stcd+pch);
			ResultSecond second = rsDao.queryResultSecondByStcdAndPch(stcd, pch);
			String oldEmstcd="";
			if(second!=null){
				oldEmstcd=second.getEmstcd();
			}
			if(timeJg!=null && CommonUtil.trim(timeJg.getKssj()).equals(CommonUtil.trim(start))
					&& CommonUtil.trim(timeJg.getJssj()).equals(CommonUtil.trim(end))
					&& timeJg.getSJJG()!=null 
					&& timeJg.getSJJG()==Integer.valueOf(interval)
					&& CommonUtil.trim(timeJg.getStcds()).equals(CommonUtil.trim(stcds))
					&& CommonUtil.trim(emstcd).equals(CommonUtil.trim(oldEmstcd))){
				resultList=resuldDao.queryStep5History(stcd,pch);
			}else{
				if(Integer.valueOf(interval)==1440){
					resultList=resuldDao.queryStep5InitData(stcd, pch, interval, start, end, stcds, stnms, qzs,emstcd);
				}else{
					resultList=calcDayNewList(stcd,pch,start,end,interval,stcds,emstcd);
				}
				if(timeJg!=null){
					timeJg.setKssj5(start);
					timeJg.setJssj5(end);
					timeJg.setSjjg5(Integer.valueOf(interval));
					timeJg.setStcds5(stcds);
					timeJg.setStnms5(stnms);
					timeJg.setQzs5(qzs);
				}
			}
			table.put("rows", resultList);
			table.put("header", timeJg);
			return table;
		}
		@SuppressWarnings({ "rawtypes", "unchecked" })
		private List<Map> calcDayNewList(String stcd,String pch,String start,String end,String interval,String stcds,String emstcd){
			List<Map> resultList = new ArrayList<Map>();
			String searchStart = CommonUtil.getAfterMinitsDateTime(start+":00", -Integer.valueOf(interval));
			searchStart=searchStart.substring(0, 16);
			List<Map> jylList = resuldDao.queryDayPptnByTime(stcds, searchStart, end);
			List<Map> zflList = resuldDao.queryDayEvByTime(emstcd, searchStart, end);
			if(jylList!=null && jylList.size()>0){
				Hashtable<String,List<Map>> tableData = new Hashtable<String,List<Map>>();
				String bm=null;
				List<Map> bmlist=null;
				for(int i=0;i<jylList.size();i++){
					Map map = jylList.get(i);
					String st = map.get("STCD")!=null?map.get("STCD").toString():"";
					if(i==0){
						bm=st;
						bmlist=new ArrayList<Map>();
					}
					if(bm!=null && bm.length()>0 && bm.equals(st)){
						bmlist.add(map);
					}else if(bm!=null && bm.length()>0 && !bm.equals(st)){
						if(bmlist!=null && bmlist.size()>0){
							tableData.put(bm, bmlist);
							bmlist=new ArrayList<Map>();
							bm=st;
							bmlist.add(map);
						}
					}
				}
				if(bm!=null && bm.length()>0 && bmlist!=null && bmlist.size()>0){
					tableData.put(bm, bmlist);
				}
				String[] stcdList = stcds.split(",");
				if(stcdList!=null && stcdList.length>0){
					String calTime =start; 
					String startTime=searchStart+":00";
					while(CommonUtil.compareLessDateTime(calTime, end)){
						Map map = new HashMap();
						map.put("STCD", stcd);
						map.put("PCH", pch);
						map.put("DATE", calTime);
						map.put("YML", "");
						String jyl="";
						for(int j=0;j<stcdList.length;j++){
							double drp=0;
							String ss = stcdList[j];
							List<Map> jylArr = tableData.get(ss);
							if(jylArr!=null){
								drp=getDayJylData(startTime,calTime,jylArr);
							}
							if(j==0){
								jyl=String.valueOf(drp);
							}else{
								jyl=jyl+","+String.valueOf(drp);
							}
						}
						map.put("JYL", jyl);
						//计算蒸发量
						double dye=getDayEvData(searchStart,calTime,zflList);
						map.put("EM", dye);
						resultList.add(map);
						startTime=calTime;
						calTime=CommonUtil.getAfterMinitsDateTime(calTime, Integer.valueOf(interval));
					}
				}
			}
			return resultList;
		}
		private double getDayEvData(String start,String end,List<Map> zflList){
			double result=0;
			if(zflList!=null && zflList.size()>0){
				for(int i=0;i<zflList.size();i++){
					Map map = zflList.get(i);
					if(map!=null && CommonUtil.trim(map.get("TM")).length()>0){
						String tm=CommonUtil.trim(map.get("TM"));
						tm=tm+":00";
						double drp=0;
						if(CommonUtil.trim(map.get("DYE")).length()>0){
							drp=CommonUtil.getFloatValue(CommonUtil.trim(map.get("DYE")));
						}
						//start<tm and end<=tm
						//
						if(!CommonUtil.compareMoreDateTime(start, tm)
								&& CommonUtil.compareLessDateTime(end, tm)){
							long interval=CommonUtil.getDifferenceTime(start, end);
							result = result+drp*interval/1440;
							return new BigDecimal(result).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();//保留2位小数
						}else if(!CommonUtil.compareMoreDateTime(start, tm)
								&& CommonUtil.compareMoreDateTime(end, tm)){
							long interval=CommonUtil.getDifferenceTime(start, tm);
							result = result+drp*interval/1440;
							start=tm;
						}
					}
				}
			}
			return new BigDecimal(result).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();//保留2位小数
		}
		private double getDayJylData(String start,String end,List<Map> jylList){
			double result=0;
			if(jylList!=null && jylList.size()>0){
				for(int i=0;i<jylList.size();i++){
					Map map = jylList.get(i);
					if(map!=null && CommonUtil.trim(map.get("TM")).length()>0){
						String tm=CommonUtil.trim(map.get("TM"));
						tm=tm+":00";
						double drp=0;
						if(CommonUtil.trim(map.get("DYP")).length()>0){
							drp=CommonUtil.getFloatValue(CommonUtil.trim(map.get("DYP")));
						}
						//start<tm and end<=tm
						if(!CommonUtil.compareMoreDateTime(start, tm)
								&& CommonUtil.compareLessDateTime(end, tm)){
							long interval=CommonUtil.getDifferenceTime(start, end);
							result = drp*interval/1440;
							return new BigDecimal(result).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();//保留2位小数
						}else if(!CommonUtil.compareMoreDateTime(start, tm)
								&& CommonUtil.compareMoreDateTime(end, tm)){
							long interval=CommonUtil.getDifferenceTime(start, tm);
							result = result+drp*interval/1440;
							start=tm;
						}
					}
				}
			}
			return new BigDecimal(result).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();//保留2位小数
		}
		@SuppressWarnings("rawtypes")
		public Hashtable<String,Object> queryStep5DayData(String stcd,String pch,String beginDate,String endDate,String interval,SysStaff  mSysStaff){
			Hashtable<String,Object> table = new Hashtable<String,Object>();
			List<Map> planList = resuldDao.queryStep5PlanList(stcd,mSysStaff);
			ResultJg resultJg=rjDao.get(stcd+pch);
			ResultSecond second = rsDao.queryResultSecondByStcdAndPch(stcd, pch);
			table.put("planList", planList);
			if(second!=null){
				table.put("second", second);
			}else{
				table.put("reflag", "0");
				table.put("message", "缺少产流计算第二步结果!");
				return table;
			}
			String emstcd="";
			if(second!=null && CommonUtil.trim(second.getEmstcd()).length()>0){
				Stbprp stinfo=this.stbprpDao.get(CommonUtil.trim(second.getEmstcd()));
				if(stinfo!=null){
					table.put("stinfo", stinfo);
					emstcd=stinfo.getStcd();
				}
			}
			List<Map> step5List = resuldDao.queryStep5History(stcd, pch);
			if(step5List==null || step5List.size()<1){
				if(resultJg!=null 
						&& CommonUtil.trim(resultJg.getStcds()).length()>0
						&& CommonUtil.trim(resultJg.getStnms()).length()>0){
					resultJg.setKssj5(beginDate);
					resultJg.setJssj5(endDate);
					resultJg.setStcds5(resultJg.getStcds());
					resultJg.setStnms5(resultJg.getStnms());
					resultJg.setQzs5(resultJg.getQzs());
					resultJg.setSjjg5(Integer.valueOf(interval));
					step5List=resuldDao.queryStep5InitData(stcd, pch, interval, beginDate, endDate, resultJg.getStcds5(), resultJg.getStnms5(), resultJg.getQzs5(),emstcd);
				}
			}
			table.put("header", resultJg);
			table.put("rows", step5List);
			table.put("reflag", "1");
			return table;
		}
		public List<Map> queryStep6ZfzList(String start,String end,String searchText){
			return resuldDao.queryStep6Zfzlist(start, end,searchText);
		}
		/**
		 * 计算第五步关联日蒸发量
		 * @param start
		 * @param end
		 * @param stcd
		 * @param interval
		 * @return
		 */
		public Hashtable<String,String> queryStep5EdataTable(String start,String end,String stcd,String interval){
			Hashtable<String,String> edata = new Hashtable<String,String>();
			String searchStart = CommonUtil.getAfterMinitsDateTime(start+":00", -Integer.valueOf(interval));
			String searchEnd = CommonUtil.getAfterMinitsDateTime(end+":00", 1440);
			List<Map> relist = resuldDao.queryStep6DayZfdata(searchStart.substring(0, 16), searchEnd.substring(0, 16), stcd);
			//统计蒸发量分部时间段
			String calcTime=start+":00";
			while(CommonUtil.compareLessDateTime(calcTime, end+":00")){
				String calcStart = CommonUtil.getAfterMinitsDateTime(calcTime, -Integer.valueOf(interval));
				double dye=getDayEvData(calcStart,calcTime,relist);
				String key=calcTime.substring(0,16);
				key=key.replace("-", "");
				key=key.replace(" ", "");
				key=key.replace(":", "");
				edata.put("key"+key, String.valueOf(dye));
				calcTime=CommonUtil.getAfterMinitsDateTime(calcTime, Integer.valueOf(interval));
			}
			return edata;
		}
		/**
		 * 记录日蒸发量分配的时段
		 * @param dataList
		 * @param interval
		 * @return
		 */
		public Hashtable<String,Double> getYesCalcTimeOfDay(JSONArray dataList,String interval){
			Hashtable<String,Double> table = new Hashtable<String,Double>();
			if(dataList!=null && dataList.size()>0){
				for(int i=0;i<dataList.size();i++){
					JSONObject data = dataList.getJSONObject(i);
					if(data!=null && data.containsKey("P") 
							&& data.containsKey("DATE")){
						String date = data.getString("DATE");
						double p = data.getDouble("P");
						String prdate=CommonUtil.getAfterMinitsDateTime(date+":00", -Integer.valueOf(interval));
						if(p<=CommonUtil.LIMITEP && p>0){
							String limeDate = date.substring(0, 10)+" "+CommonUtil.TIMESTR+":00";
							if(CommonUtil.compareLessDateTime(date+":00", limeDate)){
								String enddate=date+":00";
								String preLimeDate = CommonUtil.getAfterMinitsDateTime(limeDate, -1440);
								while(CommonUtil.compareMoreDateTime(preLimeDate, prdate)){
									String key=preLimeDate.substring(0, 10);
									key=key.substring(0, 10);
									key=key.replace("-", "");
									key=key.replace(":", "");
									key=key.replace(" ", "");
									double d=table.get("key"+key)!=null?table.get("key"+key):0;
									long t = CommonUtil.getDifferenceTime(preLimeDate, enddate);
									d=d+t;
									table.put("key"+key, d);
									enddate=preLimeDate;
									preLimeDate=CommonUtil.getAfterMinitsDateTime(preLimeDate, -1440);
								}
								long lt=CommonUtil.getDifferenceTime(prdate, enddate);
								if(lt>0){
									String key=preLimeDate.substring(0, 10);
									key=key.substring(0, 10);
									key=key.replace("-", "");
									key=key.replace(":", "");
									key=key.replace(" ", "");
									double d=table.get("key"+key)!=null?table.get("key"+key):0;
									d=d+lt;
									table.put("key"+key, d);
								}
							}else {
								if(CommonUtil.compareMoreDateTime(prdate, limeDate)){
									String key=limeDate.substring(0, 10);
									key=key.replace("-", "");
									key=key.replace(":", "");
									key=key.replace(" ", "");
									double d=table.get("key"+key)!=null?table.get("key"+key):0;
									d=d+Integer.valueOf(interval);
									table.put("key"+key, d);
								}else{
									//prdate<limeDate and date>limeDate
									//界限值的前一天
									String key1=limeDate.substring(0, 10);
									key1=key1.replace("-", "");
									key1=key1.replace(":", "");
									key1=key1.replace(" ", "");
									double d1=table.get("key"+key1)!=null?table.get("key"+key1):0;
									long t1=CommonUtil.getDifferenceTime(limeDate, date+":00");
									d1=d1+t1;
									table.put("key"+key1, d1);
									//界限值后一天
									String enddate=limeDate;
									String preLimeDate = CommonUtil.getAfterMinitsDateTime(limeDate, -1440);
									while(CommonUtil.compareMoreDateTime(preLimeDate, prdate)){
										String key=preLimeDate.substring(0, 10);
										key=key.substring(0, 10);
										key=key.replace("-", "");
										key=key.replace(":", "");
										key=key.replace(" ", "");
										double d=table.get("key"+key)!=null?table.get("key"+key):0;
										long t = CommonUtil.getDifferenceTime(preLimeDate, enddate);
										d=d+t;
										table.put("key"+key, d);
										enddate=preLimeDate;
										preLimeDate=CommonUtil.getAfterMinitsDateTime(preLimeDate, -1440);
									}
									long lt=CommonUtil.getDifferenceTime(prdate, enddate);
									if(lt>0){
										String key=preLimeDate.substring(0, 10);
										key=key.substring(0, 10);
										key=key.replace("-", "");
										key=key.replace(":", "");
										key=key.replace(" ", "");
										double d=table.get("key"+key)!=null?table.get("key"+key):0;
										d=d+lt;
										table.put("key"+key, d);
									}
								}
							}
						}
					}
				}
			}
			return table;
		}
		/**
		 * 获取降雨量超过某个设定值的时间段，这些时间段没有蒸发量
		 * @param dataList
		 * @return
		 */
		private Hashtable<String,Double> getNoCalcTimeOfDay(JSONArray dataList,String interval){
			Hashtable<String,Double> table = new Hashtable<String,Double>();
			if(dataList!=null && dataList.size()>0){
				for(int i=0;i<dataList.size();i++){
					JSONObject data = dataList.getJSONObject(i);
					if(data!=null && data.containsKey("P") 
							&& data.containsKey("DATE")){
						String date = data.getString("DATE");
						double p = data.getDouble("P");
						String prdate=CommonUtil.getAfterMinitsDateTime(date+":00", -Integer.valueOf(interval));
						if(p>CommonUtil.LIMITEP || p==0){
							String limeDate = date.substring(0, 10)+" "+CommonUtil.TIMESTR+":00";
							if(CommonUtil.compareLessDateTime(date+":00", limeDate)){
								String enddate=date+":00";
								String preLimeDate = CommonUtil.getAfterMinitsDateTime(limeDate, -1440);
								while(CommonUtil.compareMoreDateTime(preLimeDate, prdate)){
									String key=preLimeDate.substring(0, 10);
									key=key.substring(0, 10);
									key=key.replace("-", "");
									key=key.replace(":", "");
									key=key.replace(" ", "");
									double d=table.get("key"+key)!=null?table.get("key"+key):0;
									long t = CommonUtil.getDifferenceTime(preLimeDate, enddate);
									d=d+t;
									table.put("key"+key, d);
									enddate=preLimeDate;
									preLimeDate=CommonUtil.getAfterMinitsDateTime(preLimeDate, -1440);
								}
								long lt=CommonUtil.getDifferenceTime(prdate, enddate);
								if(lt>0){
									String key=preLimeDate.substring(0, 10);
									key=key.substring(0, 10);
									key=key.replace("-", "");
									key=key.replace(":", "");
									key=key.replace(" ", "");
									double d=table.get("key"+key)!=null?table.get("key"+key):0;
									d=d+lt;
									table.put("key"+key, d);
								}
							}else {
								if(CommonUtil.compareMoreDateTime(prdate, limeDate)){
									String key=limeDate.substring(0, 10);
									key=key.replace("-", "");
									key=key.replace(":", "");
									key=key.replace(" ", "");
									double d=table.get("key"+key)!=null?table.get("key"+key):0;
									d=d+Integer.valueOf(interval);
									table.put("key"+key, d);
								}else{
									//prdate<limeDate and date>limeDate
									//界限值的前一天
									String key1=limeDate.substring(0, 10);
									key1=key1.replace("-", "");
									key1=key1.replace(":", "");
									key1=key1.replace(" ", "");
									double d1=table.get("key"+key1)!=null?table.get("key"+key1):0;
									long t1=CommonUtil.getDifferenceTime(limeDate, date+":00");
									d1=d1+t1;
									table.put("key"+key1, d1);
									//界限值后一天
									String enddate=limeDate;
									String preLimeDate = CommonUtil.getAfterMinitsDateTime(limeDate, -1440);
									while(CommonUtil.compareMoreDateTime(preLimeDate, prdate)){
										String key=preLimeDate.substring(0, 10);
										key=key.substring(0, 10);
										key=key.replace("-", "");
										key=key.replace(":", "");
										key=key.replace(" ", "");
										double d=table.get("key"+key)!=null?table.get("key"+key):0;
										long t = CommonUtil.getDifferenceTime(preLimeDate, enddate);
										d=d+t;
										table.put("key"+key, d);
										enddate=preLimeDate;
										preLimeDate=CommonUtil.getAfterMinitsDateTime(preLimeDate, -1440);
									}
									long lt=CommonUtil.getDifferenceTime(prdate, enddate);
									if(lt>0){
										String key=preLimeDate.substring(0, 10);
										key=key.substring(0, 10);
										key=key.replace("-", "");
										key=key.replace(":", "");
										key=key.replace(" ", "");
										double d=table.get("key"+key)!=null?table.get("key"+key):0;
										d=d+lt;
										table.put("key"+key, d);
									}
								}
							}
						}
					}
				}
			}
			return table;
		}
		private double calcDayEvn(String date,String interval,Hashtable<String,Double> dayEvTable,Hashtable<String,Double> timeTable){
			double em=0;
			String prdate=CommonUtil.getAfterMinitsDateTime(date+":00", -Integer.valueOf(interval));
			String limeDate = date.substring(0, 10)+" "+CommonUtil.TIMESTR+":00";
			if(CommonUtil.compareLessDateTime(date+":00", limeDate)){
				String enddate=date+":00";
				String preLimeDate = CommonUtil.getAfterMinitsDateTime(limeDate, -1440);
				while(CommonUtil.compareMoreDateTime(preLimeDate, prdate)){
					String key=preLimeDate.substring(0, 10);
					key=key.substring(0, 10);
					key=key.replace("-", "");
					key=key.replace(":", "");
					key=key.replace(" ", "");
					//获取日蒸发量
					double zfl=dayEvTable.get("key"+key)!=null?dayEvTable.get("key"+key):0;
					//获取当日未参与分摊蒸发量的时间
					double ntm = timeTable.get("key"+key)!=null?timeTable.get("key"+key):0;
					long t = CommonUtil.getDifferenceTime(preLimeDate, enddate);
//					if(ntm<1440){
//						em=em+(zfl*t/(1440-ntm));
//					}
					if(ntm>0){
						em=em+(zfl*t/ntm);
					}
					enddate=preLimeDate;
					preLimeDate=CommonUtil.getAfterMinitsDateTime(preLimeDate, -1440);
				}
				long lt=CommonUtil.getDifferenceTime(prdate, enddate);
				if(lt>0){
					String key=preLimeDate.substring(0, 10);
					key=key.substring(0, 10);
					key=key.replace("-", "");
					key=key.replace(":", "");
					key=key.replace(" ", "");
					//获取日蒸发量
					double zfl=dayEvTable.get("key"+key)!=null?dayEvTable.get("key"+key):0;
					//获取当日未参与分摊蒸发量的时间
					double ntm = timeTable.get("key"+key)!=null?timeTable.get("key"+key):0;
//					if(ntm<1440){
//						em=em+(zfl*lt/(1440-ntm));
//					}
					if(ntm>0){
						em=em+(zfl*lt/ntm);
					}
				}
			}else {
				if(CommonUtil.compareMoreDateTime(prdate, limeDate)){
					String key=limeDate.substring(0, 10);
					key=key.replace("-", "");
					key=key.replace(":", "");
					key=key.replace(" ", "");
					//获取日蒸发量
					double zfl=dayEvTable.get("key"+key)!=null?dayEvTable.get("key"+key):0;
					//获取当日未参与分摊蒸发量的时间
					double ntm = timeTable.get("key"+key)!=null?timeTable.get("key"+key):0;
//					if(ntm<1440){
//						em=em+(zfl*Integer.valueOf(interval)/(1440-ntm));
//					}
					if(ntm>0){
						em=em+(zfl*Integer.valueOf(interval)/ntm);
					}
				}else{
					//prdate<limeDate and date>limeDate
					//界限值的前一天
					String key1=limeDate.substring(0, 10);
					key1=key1.replace("-", "");
					key1=key1.replace(":", "");
					key1=key1.replace(" ", "");
					//获取日蒸发量
					double zfl1=dayEvTable.get("key"+key1)!=null?dayEvTable.get("key"+key1):0;
					//获取当日未参与分摊蒸发量的时间
					double ntm1 = timeTable.get("key"+key1)!=null?timeTable.get("key"+key1):0;
					long t1 = CommonUtil.getDifferenceTime(limeDate, date+":00");
//					if(ntm1<1440){
//						em=em+(zfl1*t1/(1440-ntm1));
//					}
					if(ntm1>0){
						em=em+(zfl1*t1/ntm1);
					}
					//界限值后一天
					String enddate=limeDate;
					String preLimeDate = CommonUtil.getAfterMinitsDateTime(limeDate, -1440);
					while(CommonUtil.compareMoreDateTime(preLimeDate, prdate)){
						String key=preLimeDate.substring(0, 10);
						key=key.substring(0, 10);
						key=key.replace("-", "");
						key=key.replace(":", "");
						key=key.replace(" ", "");
						//获取日蒸发量
						double zfl=dayEvTable.get("key"+key)!=null?dayEvTable.get("key"+key):0;
						//获取当日未参与分摊蒸发量的时间
						double ntm = timeTable.get("key"+key)!=null?timeTable.get("key"+key):0;
						long t = CommonUtil.getDifferenceTime(preLimeDate, enddate);
//						if(ntm<1440){
//							em=em+(zfl*t/(1440-ntm));
//						}
						if(ntm>0){
							em=em+(zfl*t/ntm);
						}
						enddate=preLimeDate;
						preLimeDate=CommonUtil.getAfterMinitsDateTime(preLimeDate, -1440);
					}
					long lt=CommonUtil.getDifferenceTime(prdate, enddate);
					if(lt>0){
						String key=preLimeDate.substring(0, 10);
						key=key.substring(0, 10);
						key=key.replace("-", "");
						key=key.replace(":", "");
						key=key.replace(" ", "");
						//获取日蒸发量
						double zfl=dayEvTable.get("key"+key)!=null?dayEvTable.get("key"+key):0;
						//获取当日未参与分摊蒸发量的时间
						double ntm = timeTable.get("key"+key)!=null?timeTable.get("key"+key):0;
//						if(ntm<1440){
//							em=em+(zfl*lt/(1440-ntm));
//						}
						if(ntm>0){
							em=em+(zfl*lt/ntm);
						}
					}
				}
			}
			return em;
		}
		@SuppressWarnings("rawtypes")
		public Hashtable<String,Object> queryEdata(String start,String end,String stcd,String interval,String data){
			Hashtable<String,Object> table = new Hashtable<String,Object>();
			JSONArray qdataList = JSONArray.fromObject(data);
			Hashtable<String,Double> timeTable = getYesCalcTimeOfDay(qdataList,interval);
			List<Map> relist = resuldDao.queryStep6DayZfdata(start, end, stcd);
			Hashtable<String,Double> dayEvTable = getDayEvTable(relist);
			if(qdataList!=null && qdataList.size()>0){
				double epe=0;
				for(int i=0;i<qdataList.size();i++){
					JSONObject dataJson = qdataList.getJSONObject(i);
					if(dataJson!=null && dataJson.containsKey("P") 
							&& dataJson.containsKey("DATE")){
						String date = dataJson.getString("DATE");
						double p = dataJson.getDouble("P");
						double pe=p;
						double em=0;
						if(p<=CommonUtil.LIMITEP && p>0){
							em = calcDayEvn(date,interval,dayEvTable,timeTable);
							em = new BigDecimal(em).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留2位小数
							pe=pe-em;
						}
						pe=new BigDecimal(pe).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();//保留2位小数
						epe=epe+pe;
						double cepe=new BigDecimal(epe).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();//保留2位小数
						dataJson.put("E", em);
						dataJson.put("PE", pe);
						dataJson.put("EPE", cepe);
					}
				}
			}
			table.put("tableData", qdataList);
			return table;
		}
		@SuppressWarnings("rawtypes")
		private Hashtable<String,Double> getDayEvTable(List<Map> relist){
			Hashtable<String,Double> dayEvTable = new Hashtable<String,Double>();
			if(relist!=null && relist.size()>0){
				for(int i=0;i<relist.size();i++){
					Map dataMap = relist.get(i);
					if(dataMap!=null){
						String tm = CommonUtil.trim(dataMap.get("TM").toString());
						if(dataMap.get("DYE")!=null && CommonUtil.trim(dataMap.get("DYE").toString()).length()>0){
							double dye=CommonUtil.getFloatValue(CommonUtil.trim(dataMap.get("DYE").toString()));
							String key = CommonUtil.getAfterMinitsDateTime(tm+":00", -1440);//前一天的蒸发量
							key=key.substring(0, 10);
							key=key.replace("-", "");
							dayEvTable.put("key"+key, dye);
						}
					}
				}
			}
			return dayEvTable;
		}
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Hashtable<String,Object> queryStep2InitData(String stcd,String pch,String start,String end,String interval){
			Hashtable<String,Object> table = new Hashtable<String,Object>();
			List<Map> step2DataList = this.resultSecondQlDao.queryStep2InitData(stcd, pch, start, end,Integer.valueOf(interval).intValue());
			List<Map> calc2DataList = this.resultSecondQlDao.queryStep2InitData(stcd, pch);
			List<Map> riverLlList = this.resultSecondQlDao.queryStep2LlData(stcd, start, end);
			Map minQmap = resultSecondQlDao.queryLastByStartTime(stcd, start);
			Map maxQmap = resultSecondQlDao.queryLastByEndTime(stcd, end);
			if(minQmap!=null){
				riverLlList.add(0, minQmap);
			}
			if(maxQmap!=null){
				riverLlList.add(maxQmap);
			}
			if(step2DataList!=null && step2DataList.size()>0){
				for(int i=0;i<step2DataList.size();i++){
					Map dataMap = step2DataList.get(i);
					Map calcMap = findCalcMap(calc2DataList,dataMap);
					if(calcMap!=null && calcMap.get("LL")!=null && CommonUtil.trim(calcMap.get("LL").toString()).length()>0){
						dataMap.put("LL", calcMap.get("LL"));
					}else{
						double calq=findLlData(dataMap,riverLlList);
						dataMap.put("LL", calq);
					}
				}
			}
			ResultSecond second=rsDao.queryResultSecondByStcdAndPch(stcd, pch);
			if(second==null){
				second=new ResultSecond();
			}
			table.put("second", second);
			table.put("dataList", step2DataList);
			return table;
		}
		private double findLlData(Map dataMap,List<Map> riverLlList){
			double calValue=0;
			String tm=CommonUtil.trim(dataMap.get("DATE").toString());
			if(riverLlList!=null && riverLlList.size()>0){
				double bvalue=0;
				String btime=null;
				for(int i=0;i<riverLlList.size();i++){
					Map map = riverLlList.get(i);
					if(map!=null && map.get("TM")!=null && map.get("Q")!=null 
							&& CommonUtil.trim(map.get("Q").toString()).length()>0){
						String tm1=CommonUtil.trim(map.get("TM").toString());
						double q1=CommonUtil.getFloatValue(CommonUtil.trim(map.get("Q").toString()));
						if(tm.equals(tm1)){
							return q1;
						}else if(!CommonUtil.compareMoreDateTime(tm1+":00", tm+":00")){
							if(i+1<riverLlList.size()){
								Map map2=riverLlList.get(i+1);
								String tm2=CommonUtil.trim(map2.get("TM").toString());
								double q2=CommonUtil.getFloatValue(CommonUtil.trim(map2.get("Q").toString()));
								if(tm.equals(tm2)){
									return q2;
								}else if(!CommonUtil.compareLessDateTime(tm2+":00", tm+":00")){
									long tx0=CommonUtil.getDifferenceTime(tm1+":00", tm+":00");
									long t10=CommonUtil.getDifferenceTime(tm1+":00", tm2+":00");
									double qx=q1+tx0*(q2-q1)/t10;
									calValue = new BigDecimal(qx).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();//保留2位小数
									return calValue;
								}
							}
						}else{
							if(i==0){
								if(i+1<riverLlList.size()){
									Map map2=riverLlList.get(i+1);
									String tm2=CommonUtil.trim(map2.get("TM").toString());
									double q2=CommonUtil.getFloatValue(CommonUtil.trim(map2.get("Q").toString()));
									long cj21=CommonUtil.getDifferenceTime(tm1+":00", tm2+":00");
									long cj1x=CommonUtil.getDifferenceTime(tm+":00", tm1+":00");
									double qcj21=q2-q1;
									double qx=q1-qcj21*cj1x/cj21;
									calValue = new BigDecimal(qx).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();//保留2位小数
								}
								return calValue;
							}
						}
					}
				}
			}
			return calValue;
		}
		@SuppressWarnings("rawtypes")
		private Map findCalcMap(List<Map> calc2DataList,Map dataMap){
			if(calc2DataList!=null && calc2DataList.size()>0){
				for(int i=0;i<calc2DataList.size();i++){
					Map calcMap = calc2DataList.get(i);
					if(calcMap!=null && dataMap!=null 
							&& CommonUtil.trim(calcMap.get("DATE").toString()).equals(CommonUtil.trim(dataMap.get("DATE").toString()))){
						return calcMap;
					}
				}
			}
			return null;
		}
		@Transactional(propagation=Propagation.REQUIRED)
		public void deleteChanliuStep2Data(String stcd,String pch){
			ResultJg jgbean = rjDao.get(stcd+pch);
			if(jgbean!=null && CommonUtil.trim(jgbean.getId()).length()>0){
				jgbean.setKssj2(null);
				jgbean.setJssj2(null);
				jgbean.setSjjg2(null);
				rjDao.saveOrUpdate(jgbean);
			}
			resultSecondQlDao.deleteStep2Data(stcd, pch);
		}
		/**
		 * 步骤2入口color:['#000000','#0000EE','#6699FF','#FF8833','#6666FF','#FF9966','#66CCFF','#FFCC66','#99CCFF','#FFCC99','#CCFFFF'],
		 * @param resultFormBean
		 * @param start
		 * @param end
		 * @return
		 */
		@Transactional(propagation=Propagation.REQUIRED)
		public PageResults step2(ResultFormBean resultFormBean,String start,String end) {
			return resuldDao.step2(resultFormBean,start,end);
		}
		//获得流量数据
		@Transactional(propagation=Propagation.REQUIRED)
		public PageResults getLiuLiang(ResultFormBean resultFormBean,String start,String end ) {
			return resuldDao.getLiuLiang(resultFormBean, start, end);
		}
		
		//获得降雨量数据
		@Transactional(propagation=Propagation.REQUIRED)
		public PageResults getyml(ResultFormBean resultFormBean,String start,String end ) {
			return resuldDao.getyml(resultFormBean, start, end);
		}
		
		/**
		 * 修改降雨量/雨面量
		 */
		@Transactional(propagation=Propagation.REQUIRED)
		public void updateTableData(ResultFormBean resultFormBean,RiverFormBean mRiverFormBean) {
			if(resultFormBean.getResultBean().getYml()==0) {
				//如果没有修改的降雨量就不修改
			}else {//否则修改
				 resuldDao.updateTableData(resultFormBean);//修改降雨量
			}
			River mRiver = mRiverFormBean.getmRiverInfoBean();
			String stcd=mRiver.getStcd();
			String tm=mRiver.getTm();
			tm=tm.replace("-", "");
			tm=tm.replace(":", "");
			tm=tm.replace(" ", "");
			mRiver.setNm(stcd+tm);
			riverDao.saveOrUpdate(mRiver);
		}
		
		
		/**
		 * 步骤三，退水曲线
		 */
		@Transactional(propagation=Propagation.REQUIRED)
		public PageResults getTsqx(ResultFormBean resultFormBean,TsqxFormBean mTsqxFormBean) {
		return resuldDao.getTsqx(resultFormBean, mTsqxFormBean) ;
		}
		/**
		 * 流量最大值
		 * @return
		 */
		@Transactional(propagation=Propagation.REQUIRED)
		public List maxLL(ResultFormBean resultFormBean,String start,String end) {
			return  resuldDao.maxLL(resultFormBean,start,end);
		}
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Hashtable<String,Object> queryChanliuStep3EchartData(String stcd,String pch,String start,String end,String intval,String user){
			Hashtable<String,Object> echartTable=new Hashtable<String,Object>();
			List list=resuldDao.queryChanliuStep2MaxLl(stcd,pch);//流量最大值查询接口
			if(list==null || list.size()<1){
				list = resuldDao.maxLL(stcd, pch, start, end);
			}
			HashMap<String, Object> maxMin= list!=null && list.size()>0?(HashMap<String, Object>) list.get(0):null;
			Object max=  maxMin!=null?maxMin.get("maxLL"):null;
			Object min=  maxMin!=null?maxMin.get("minLL"):null;
			double llmax=max!=null?Double.parseDouble(max.toString()):0;
			double llmin=min!=null?Double.parseDouble(min.toString()):0;
			echartTable.put("maxLL", llmax);
			echartTable.put("minLL", llmin);
			List<Map> liuLiangList=	resuldDao.queryChanliuStep2Liuliang(stcd,pch,intval);//获得流量的list去遍历封装数据
			if(liuLiangList==null || liuLiangList.size()<1){
				echartTable.put("reflag", "0");
				echartTable.put("message", "缺少产流计算第二步的结果!");
				return echartTable;
				//liuLiangList=resuldDao.getLiuLiang(stcd, pch, start, end,intval);
			}
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
			echartTable.put("llData", array); //流量
			//查询以前切点保存数据
			List<Map> thirdResult=resuldDao.getCResultThird(stcd,pch);	
			if(thirdResult==null || thirdResult.size()<1){
				thirdResult=new ArrayList<Map>();
				//计算QT
				if(liuLiangList!=null && liuLiangList.size()>0){
					for(int i=0;i<liuLiangList.size()-1;i++){
						Map map1 = liuLiangList.get(i);
						Map map2 = liuLiangList.get(i+1);
						if(map1!=null && CommonUtil.trim(map1.get("Q")).length()>0
								&& map2!=null && CommonUtil.trim(map2.get("Q")).length()>0){
							double q1=CommonUtil.getFloatValue(map1.get("Q").toString());
							double q2=CommonUtil.getFloatValue(map2.get("Q").toString());
							double qt=(q1+q2)*Integer.valueOf(intval)*60/2;
							qt=new BigDecimal(qt).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();//保留3位小数
							map1.put("QT", qt);
							thirdResult.add(map1);
						}
					}
					Map lastMap = liuLiangList.get(liuLiangList.size()-1);
					lastMap.put("QT", 0);
					thirdResult.add(lastMap);
				}
			}else{
				List<Map> thirdLess=resuldDao.getCResultThirdLess(stcd,pch);
				echartTable.put("thirdLess", thirdLess);
			}
			echartTable.put("thirdResult", thirdResult);
			echartTable.put("tableList", thirdResult);
			//降雨量
			List ymllist=resuldDao.queryChanliuStep2MaxJyl(stcd,pch);
			if(ymllist==null || ymllist.size()<1){
				ymllist=resuldDao.maxYml(stcd, pch, start, end);
			}
			HashMap<String, Object> ymllistmax= ymllist!=null && ymllist.size()>0?(HashMap<String, Object>) ymllist.get(0):null;
			Object JYL=  ymllistmax!=null?ymllistmax.get("JYL"):null;
			double maxJyl=JYL!=null?Double.parseDouble(JYL.toString()):0;
			echartTable.put("maxJYL", maxJyl);
			
			List<Map> jList= resuldDao.queryChanliuStep2Jyl(stcd,pch);//降雨量
			if(jList==null || jList.size()<1){
				jList=resuldDao.getyml(stcd, pch, start, end);
			}
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
			echartTable.put("jylData", strArray);//降雨量
			//end
			
			//退水曲线
			TsqxFormBean mTsqxFormBean=new TsqxFormBean ();
			List<Map> tsqxlist=resuldDao.numHongFeng(stcd);//得到洪峰的值，去重
			List<Map> list2=resuldDao.getTsqx(stcd);//查询退水曲线
			double maxTsqx=0;
			double minTsqx=0;
			List<Map> tsqxDataList = new ArrayList<Map>();
			for(int i=0;i<tsqxlist.size();i++) {//退水曲线的洪峰，遍历之后去结果去匹配
				HashMap< String, Object> dataMap=new HashMap< String, Object>();
				dataMap.put("NAME", tsqxlist.get(i).get("QM"));
				Double qm=(Double) tsqxlist.get(i).get("QM"); //得到洪峰
				Double maxQ=(Double) tsqxlist.get(i).get("MAXQ"); 
				Double minQ=(Double) tsqxlist.get(i).get("MINQ"); 
				if(i==0){
					maxTsqx=maxQ.doubleValue();
					minTsqx=minQ.doubleValue();
				}else{
					if(maxQ.doubleValue()>maxTsqx){
						maxTsqx=maxQ.doubleValue();
					}
					if(minQ.doubleValue()<minTsqx){
						minTsqx=minQ.doubleValue();
					}
				}
				List list3=new ArrayList();
				for(int j=0;j<list2.size();j++) {
					Double list2qm= (Double) list2.get(j).get("QM");
					if(qm.equals(list2qm)) {//如果洪峰相等 那么就添加相应的流量和时间段到集合里，这个集合是json数组的数据来源
						Object [] data=new Object [2];
						data[0]=list2.get(j).get("T");
						data[1]=list2.get(j).get("Q");
						Double Q = (Double)data[1];
						list3.add(data); //添加到list里
					}
				}
				dataMap.put("DATA", list3);//DATA的数据是list3就是上面添加的T和Q
				tsqxDataList.add(dataMap);
			}
			Object[]  tsqxArray = tsqxDataList.toArray();//list转JSON
			echartTable.put("tsqxData", tsqxArray);//将拼装好的Object数组添加到map里
			List<Double> tlist1=new ArrayList<Double>();
			List<Map> tlist=resultThridDao.sd(stcd,user);
			for(int i=0;i<tlist.size();i++) {
			 double t=(double) tlist.get(i).get("T");
			 tlist1.add(t);
			}
			List<Map> secondList = rsDao.querySecondByStcdAndPch(stcd, pch);
			echartTable.put("tsqxXdata", tlist1.toArray());
			echartTable.put("tsqxMax", maxTsqx);
			echartTable.put("tsqxMin", minTsqx);
			echartTable.put("secondList", secondList);
			echartTable.put("reflag", "1");
			//end
			return echartTable;
		}
		/**
		 * JYL最大值
		 * @return
		 */
		@Transactional(propagation=Propagation.REQUIRED)
		public List maxYml(ResultFormBean resultFormBean,String start,String end) {
			return  resuldDao.maxYml(resultFormBean,start,end);
		}
		/**
		 * 退水曲线洪峰个数统计，用来生成json用
		 */
		@Transactional(propagation=Propagation.REQUIRED)
		public List numHongFeng(ResultFormBean resultFormBean) {
			return resuldDao.numHongFeng(resultFormBean);
		}
		public List<Map> queryStep7ResultData(String stcd,String pch){
			return resuldDao.queryStep6ResultData(stcd, pch);
		}
		public List<Map> queryStep7ResultData(String stcd){
			return resuldDao.queryStep7ResultData(stcd);
		}
	public List<HashMap<String, Object>> queryStep7startEnd(String stcd,String pch) {
		ResultFormBean resultFormBean =new ResultFormBean ();
		resultFormBean.getResultBean().setStcd(stcd);
		resultFormBean.getResultBean().setPch(pch);
		List<Map> secondData=rsDao.querySecondByStcdAndPch(stcd, pch);
		Object paString=secondData.get(0).get("PA");
		Object rsString=secondData.get(0).get("RS");
		PageResults p=	resuldDao.queryStep7startEnd(resultFormBean);//查询第三步开始时间，结束时间
		List<HashMap<String, Object>> pList=p.getResults();
		pList.get(0).put("PA", paString);
		pList.get(0).put("RS", rsString);
			return pList;
		}

		public int deleteStep6Result(String stcd,String pch){
			return resuldDao.deleteStep6Result(stcd, pch);
		}
		@SuppressWarnings("rawtypes")
		public Hashtable<String,Object> queryStep6InitData(String stcd,String pch,String start,String end,String interval){
			Hashtable<String,Object> table = new Hashtable<String,Object>();
			List<Map> table1Data=resuldDao.queryStep6ResultData(stcd, pch);
			ResultSecond secondData = rsDao.queryResultSecondByStcdAndPch(stcd, pch);
			if(secondData!=null){
				table.put("secondData", secondData);
				//查询第五步关联方案信息
				if(CommonUtil.trim(secondData.getFanm()).length()>0){
					List<Map> relist = resuldDao.queryPlanParamValue(CommonUtil.trim(secondData.getFanm()));
					Map<String,String> params = new HashMap<String,String>();
					if(relist!=null && relist.size()>0){
						for(int i=0;i<relist.size();i++){
							Map pvmap = relist.get(i);
							if(pvmap!=null 
									&& CommonUtil.trim(pvmap.get("PARA_SYMBOL")).length()>0
									&& CommonUtil.trim(pvmap.get("PARA_VALUE")).length()>0){
								params.put(CommonUtil.trim(pvmap.get("PARA_SYMBOL")), CommonUtil.trim(pvmap.get("PARA_VALUE")));
							}
						}
					}
					table.put("planValue", params);
				}
			}else{
				table.put("reflag", "0");
				table.put("message", "缺少产流计算第二步的结果!");
				return table;
			}
			String zfstcd="";
			if(secondData!=null && CommonUtil.trim(secondData.getId()).length()>0){
				zfstcd=secondData.getZstcd();
				if(CommonUtil.trim(zfstcd).length()>0){
					Stbprp stbprp = stbprpDao.get(zfstcd);
					if(stbprp!=null){
						table.put("stinfo", stbprp);
					}
				}
			}
			if(table1Data==null||table1Data.size()<1){
				table1Data=resuldDao.queryStep6InitData(stcd, pch, start, end);
				JSONArray qdataList = JSONArray.fromObject(table1Data);
				if(CommonUtil.trim(zfstcd).length()>0){
					Hashtable<String,Double> timeTable = getYesCalcTimeOfDay(qdataList,interval);
					List<Map> relist = resuldDao.queryStep6DayZfdata(start, end, zfstcd);
					Hashtable<String,Double> dayEvTable = getDayEvTable(relist);
					if(qdataList!=null && qdataList.size()>0){
						double epe=0;
						for(int i=0;i<qdataList.size();i++){
							JSONObject dataJson = qdataList.getJSONObject(i);
							if(dataJson!=null && dataJson.containsKey("P") 
									&& dataJson.containsKey("DATE")){
								String date = dataJson.getString("DATE");
								double p = dataJson.getDouble("P");
								double pe=p;
								double em=0;
								if(p<=CommonUtil.LIMITEP && p>0){
									em = calcDayEvn(date,interval,dayEvTable,timeTable);
									em = new BigDecimal(em).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留2位小数
									pe=pe-em;
								}
								pe=new BigDecimal(pe).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();//保留2位小数
								epe=epe+pe;
								double cepe=new BigDecimal(epe).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();//保留2位小数
								dataJson.put("E", em);
								dataJson.put("PE", pe);
								dataJson.put("EPE", cepe);
							}
						}
					}
				}
				table.put("tableData", qdataList);
			}else{
				table.put("tableData", table1Data);
			}
			ResultSixthResult resultBean=this.resultSixthResultDao.queryByStcdAndPch(stcd, pch);
			resultBean.setStcd(stcd);
			resultBean.setPch(pch);
			table.put("resultBean", resultBean);
			List<Map> echartData= resuldDao.queryStep6EchartData(stcd,pch);
			table.put("echartData", echartData);
			table.put("reflag", "1");
			return table;
		}
		@Transactional(propagation=Propagation.REQUIRED)
		public  PageResults getStep6(ResultFormBean resultFormBean,String start,String end) {
			return resuldDao.getStep6(resultFormBean, start, end);
		}
		private boolean isChange(int rowIdex,String field,JSONArray editData){
			if(editData!=null && editData.size()>0){
				for(int i=0;i<editData.size();i++){
					JSONObject change = editData.getJSONObject(i);
					int id=change.getInt("rowIndex");
					String f=change.getString("field");
					if(id==rowIdex && CommonUtil.trim(f).equals(CommonUtil.trim(field))){
						return true;
					}
				}
			}
			return false;
		}
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private void clearZeroData(JSONArray tableData,List<Map> rgmapList,int zlen,double ezvalues){
			rgmapList.clear();
			double pj=ezvalues/zlen;
			int num=0;
			double total=0;
			for(int i=0;i<tableData.size();i++){
				JSONObject trdata = tableData.getJSONObject(i);
				double r = trdata.getDouble("R");
				if(r>0){
					r=r+pj;
					r = new BigDecimal(r).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
					if(r>0){
						num++;
					}else{
						total=total+r;
						r=0;
					}
				}
				trdata.put("R", r);
				Map map = new HashMap();
				map.put("DATE", trdata.getString("DATE"));
				map.put("SDRC", trdata.getDouble("SDRC"));
				map.put("RGDX", r);
				rgmapList.add(map);
			}
			if(num>0 && total<0){
				clearZeroData(tableData,rgmapList,num,total);
			}
		}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Hashtable<String, Object> caclStep6Data(String stcd, String pch, String rs, String rx, String pa,
			String lymj, String data) {
		Hashtable<String, Object> table = new Hashtable<String, Object>();
		try {
			JSONObject dataJson = JSONObject.fromObject(data);
			int interval = dataJson.getInt("interval");
			int erisedit = dataJson.getInt("erisedit");
			JSONArray tableData = dataJson.getJSONArray("tabledata");
			JSONArray echartData = dataJson.getJSONArray("echartdata");
			JSONArray editData = dataJson.getJSONArray("editData");
			List<Line> lineList = new ArrayList<Line>();
			if (echartData != null && echartData.size() > 0) {
				for (int i = 0; i < echartData.size(); i++) {
					JSONObject lineJson = echartData.getJSONObject(i);
					JSONArray pointJson = lineJson.getJSONArray("pointList");
					Line l = new Line();
					l.setPa(lineJson.getDouble("pa"));
					List<Point> plist = JSONArray.toList(pointJson, Point.class);
					l.setPointList(plist);
					lineList.add(l);
				}
			}
			if (tableData != null && tableData.size() > 0) {
				double epe = 0;
				int zlen = 0;
				for (int i = 0; i < tableData.size(); i++) {
					JSONObject trdata = tableData.getJSONObject(i);
					// 计算EPE
					double p = trdata.getDouble("P");
					double e = trdata.getDouble("E");
					double pe = p - e;
					pe = new BigDecimal(pe).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					trdata.put("PE", pe);
					if (i == 0) {
						epe = pe;
					} else {
						epe = epe + pe;
					}
					epe = new BigDecimal(epe).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					trdata.put("EPE", epe);
					// 计算PAPE
					double pape = Double.valueOf(pa).doubleValue() + epe;
					pape = new BigDecimal(pape).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					trdata.put("PAPE", pape);
					// 计算ER查
					double erc = 0;
					if (erisedit == 1 && isChange(i, "ERC", editData)) {
						erc = trdata.getDouble("ERC");
					} else {
						double sdpa = Double.valueOf(pa).doubleValue();
						erc = LineDifference.getX(lineList, sdpa, epe);
						erc = new BigDecimal(erc).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						trdata.put("ERC", erc);
					}
					// 计算SDRC
					double sdrc = erc;
					if (i > 0) {
						double qerc = tableData.getJSONObject(i - 1).getDouble("ERC");
						sdrc = erc - qerc;
					}
					if (sdrc > 0) {
						zlen++;
					}
					sdrc = new BigDecimal(sdrc).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					trdata.put("SDRC", sdrc);
				}
				// 计算R修正值
				double inrs = Double.valueOf(rs).doubleValue();
				JSONObject lastTr = tableData.getJSONObject(tableData.size() - 1);
				double lastErc = lastTr.getDouble("ERC");
				double rxzz = zlen != 0 ? (inrs - lastErc) / zlen : 0;
				rxzz = new BigDecimal(rxzz).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
				//
				// 计算R改
				List<Map> rgmapList = new ArrayList<Map>();
				int zrglen=0;
				double zrgval=0;
				for (int i = 0; i < tableData.size(); i++) {
					JSONObject trdata = tableData.getJSONObject(i);
					double sdrc = trdata.getDouble("SDRC");
					double rz = sdrc + rxzz;
					rz = new BigDecimal(rz).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
					if(rz>0){
						zrglen++;
					}else{
						zrgval=zrgval+rz;
						rz=0;
					}
					trdata.put("R", rz);
					// 获取计算R改值的list集合
					Map map = new HashMap();
					map.put("DATE", trdata.getString("DATE"));
					map.put("SDRC", sdrc);
					map.put("RGDX", rz);
					rgmapList.add(map);
					//
				}
				if(zrgval<0 && zrglen>0){
					clearZeroData(tableData,rgmapList,zrglen,zrgval);
				}
				Collections.sort(rgmapList, new Comparator<Map>() {
					@Override
					public int compare(Map o1, Map o2) {
						double v1 = Double.valueOf(o1.get("RGDX").toString()).doubleValue();
						double v2 = Double.valueOf(o2.get("RGDX").toString()).doubleValue();
						return v1 > v2 ? -1 : 1;
					}
				});
				System.out.println("---paixu----" + JSONArray.fromObject(rgmapList).toString());
				// 计算ER
				double er = 0;
				for (int i = 0; i < rgmapList.size(); i++) {
					Map mm = rgmapList.get(i);
					double rg = Double.valueOf(mm.get("RGDX").toString()).doubleValue();
					if (i == 0) {
						er = rg;
					} else {
						er = er + rg;
					}
					er = new BigDecimal(er).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
					mm.put("ER", er);
					if (i < tableData.size()) {
						tableData.getJSONObject(i).put("RGDX", rg);
						tableData.getJSONObject(i).put("ER", er);
					}
				}
			}
			if (tableData != null && tableData.size() > 0) {
				if (Double.valueOf(rx) > 0) {
					Map map = calcFcAndTc(tableData, interval, Double.valueOf(rx).doubleValue());
					if (map != null) {
						map.put("STCD", stcd);
						map.put("PCH", pch);
						table.put("FCJG", map);
					} else {
						map=new HashMap();
						map.put("STCD", stcd);
						map.put("PCH", pch);
						table.put("FCJG", map);
					}
				}
			}
			table.put("tableData", tableData);
			table.put("reflag", "1");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			table.put("reflag", "0");
			table.put("message", "计算出错!");
		}
		return table;
	}
	private String changeDateString(String dateStr){
		String[] dateArr = dateStr.split(" ");
		String dayStr=null;
		String timeStr=null;
		String yyyy="",mm="",dd="",hh="",mi="",ss="";
		if(dateArr!=null && dateArr.length==1){
			dayStr = dateArr[0];
		}else if(dateArr!=null && dateArr.length==2){
			dayStr = dateArr[0];
			timeStr = dateArr[1];
		}
		if(dayStr!=null){
			String[] dayArr=dayStr.split("-");
			if(dayArr!=null && dayArr.length>0){
				yyyy=dayArr[0];
			}
			if(dayArr!=null && dayArr.length>1){
				mm=dayArr[1];
			}
			if(dayArr!=null && dayArr.length>2){
				dd=dayArr[2];
			}
		}
		if(timeStr!=null){
			String[] timeArr=timeStr.split(":");
			if(timeArr!=null && timeArr.length>0){
				hh=timeArr[0];
			}
			if(timeArr!=null && timeArr.length>1){
				mi=timeArr[1];
			}
			if(timeArr!=null && timeArr.length>2){
				ss=timeArr[2];
			}
		}
		if(mm!=null && mm.length()<2){
			mm="0"+mm;
		}
		if(dd!=null && dd.length()<2){
			dd="0"+dd;
		}
		if(hh!=null && hh.length()<2){
			hh="0"+hh;
		}
		if(mi!=null && mi.length()<2){
			mi="0"+mi;
		}
		if(ss!=null && ss.length()<2){
			ss="0"+ss;
		}
		if(yyyy!=null && mm!=null && dd!=null && hh!=null && mi!=null){
			return yyyy+"-"+mm+"-"+dd+" "+hh+":"+mi;
		}
		return null;
	}
	private double getR(JSONArray calcrdata,JSONObject trdata){
		if(calcrdata!=null && calcrdata.size()>0){
			for(int i=0;i<calcrdata.size();i++){
				JSONObject rdata = calcrdata.getJSONObject(i);
				String tm = rdata.getString("T");
				tm=changeDateString(tm);
				double r=rdata.getDouble("R");
				String rtm=trdata.getString("DATE");
				if(CommonUtil.trim(tm).length()>0
						&& CommonUtil.trim(rtm).length()>0 
						&& CommonUtil.trim(tm).equals(CommonUtil.trim(rtm))){
					return r;
				}
			}
		}
		return 0;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Hashtable<String, Object> saveCalcServiceResult(String stcd, String pch, String rs, String rx, String pa,
			String lymj,String zfzstcd,Double Im,Double b, String data) {
		Hashtable<String, Object> table = new Hashtable<String, Object>();
		try {
			JSONObject dataJson = JSONObject.fromObject(data);
			int interval = dataJson.getInt("interval");
			JSONArray tableData = dataJson.getJSONArray("tabledata");
			JSONArray calcrdata = dataJson.getJSONArray("calcrdata");
			if (tableData != null && tableData.size() > 0) {
				double epe = 0;
				int zlen = 0;
				for (int i = 0; i < tableData.size(); i++) {
					JSONObject trdata = tableData.getJSONObject(i);
					// 计算EPE
					double p = trdata.getDouble("P");
					double e = trdata.getDouble("E");
					double pe = p - e;
					pe = new BigDecimal(pe).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					trdata.put("PE", pe);
					if (i == 0) {
						epe = pe;
					} else {
						epe = epe + pe;
					}
					epe = new BigDecimal(epe).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					trdata.put("EPE", epe);
					// 计算PAPE
					double pape = Double.valueOf(pa).doubleValue() + epe;
					pape = new BigDecimal(pape).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					trdata.put("PAPE", pape);
					// 计算ER查
					double erc = 0;
					erc = getR(calcrdata,trdata);
					erc = new BigDecimal(erc).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					trdata.put("ERC", erc);
					// 计算SDRC
					double sdrc = erc;
					if (i > 0) {
						double qerc = tableData.getJSONObject(i - 1).getDouble("ERC");
						sdrc = erc - qerc;
					}
					if (sdrc > 0) {
						zlen++;
					}
					sdrc = new BigDecimal(sdrc).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					trdata.put("SDRC", sdrc);
				}
				// 计算R修正值
				double inrs = Double.valueOf(rs).doubleValue();
				JSONObject lastTr = tableData.getJSONObject(tableData.size() - 1);
				double lastErc = lastTr.getDouble("ERC");
				double rxzz = zlen != 0 ? (inrs - lastErc) / zlen : 0;
				rxzz = new BigDecimal(rxzz).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
				//
				// 计算R改
				List<Map> rgmapList = new ArrayList<Map>();
				int zrglen=0;
				double zrgval=0;
				for (int i = 0; i < tableData.size(); i++) {
					JSONObject trdata = tableData.getJSONObject(i);
					double sdrc = trdata.getDouble("SDRC");
					double rz = sdrc>0?(sdrc + rxzz):sdrc;
					rz = new BigDecimal(rz).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
					if(rz>0){
						zrglen++;
					}else{
						zrgval=zrgval+rz;
						rz=0;
					}
					trdata.put("R", rz);
					// 获取计算R改值的list集合
					Map map = new HashMap();
					map.put("DATE", trdata.getString("DATE"));
					map.put("SDRC", sdrc);
					map.put("RGDX", rz);
					rgmapList.add(map);
					//
				}
				if(zrgval<0 && zrglen>0){
					clearZeroData(tableData,rgmapList,zrglen,zrgval);
				}
				Collections.sort(rgmapList, new Comparator<Map>() {
					@Override
					public int compare(Map o1, Map o2) {
						Double v1 = Double.valueOf(o1.get("RGDX").toString());
						Double v2 = Double.valueOf(o2.get("RGDX").toString());
						return v2.compareTo(v1);
					}
				});
				System.out.println("---paixu----" + JSONArray.fromObject(rgmapList).toString());
				// 计算ER
				double er = 0;
				for (int i = 0; i < rgmapList.size(); i++) {
					Map mm = rgmapList.get(i);
					double rg = Double.valueOf(mm.get("RGDX").toString()).doubleValue();
					if (i == 0) {
						er = rg;
					} else {
						er = er + rg;
					}
					er = new BigDecimal(er).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
					mm.put("ER", er);
					if (i < tableData.size()) {
						tableData.getJSONObject(i).put("RGDX", rg);
						tableData.getJSONObject(i).put("ER", er);
					}
				}
			}
			if (tableData != null && tableData.size() > 0) {
				if (Double.valueOf(rx) > 0) {
					Map map = calcFcAndTc(tableData, interval, Double.valueOf(rx).doubleValue());
					if (map != null) {
						map.put("STCD", stcd);
						map.put("PCH", pch);
						table.put("FCJG", map);
					} else {
						map=new HashMap();
						map.put("STCD", stcd);
						map.put("PCH", pch);
						table.put("FCJG", map);
					}
				}
			}
			table.put("tableData", tableData);
			table.put("reflag", "1");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			table.put("reflag", "0");
			table.put("message", "计算出错!");
		}
		return table;
	}
		//[18.91,17.00,14.53,12.82,12.49,12.04,11.73,7.62,7.62,7.55,7.54,6.59,6.44,5.40,4.90,4.75,3.99,3.99,3.99,3.99,3.99,3.99,3.99,3.99,3.99,3.99,3.99,3.99,3.99,3.99,3.20,2.24
		
//		private Map calcFcAndTcTest(double[] rg,int interval){
//			double[] rlist={18.91,17.00,14.53,12.82,12.49,12.04,11.73,7.62,7.62,7.55,7.54,6.59,6.44,5.40,4.90,4.75,3.99,3.99,3.99,3.99,3.99,3.99,3.99,3.99,3.99,3.99,3.99,3.99,3.99,3.99,3.20,2.24};
//			double fc=19;
//			double fcj=4.07;
//			
//		}
		private double getRcValue(JSONArray tableData,int len){
			double er=0;
			for(int i=0;i<len;i++){
				er=er+tableData.getJSONObject(i).getDouble("RGDX");
			}
			return er;
		}
		//FC计算小—>TC小；∑_(fc>R改大→小)▒R也小，FC试算大
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private Map calcFcAndTc(JSONArray tableData,double interval,double rx){
			List<Map> relist = new ArrayList<Map>();
			int len = tableData.size();
			double erx=0;
			double tc=0;
			double tcerhj=0;
			for(int i=len-1;i>0;i--){
				JSONObject data = tableData.getJSONObject(i);
				double rg=data.getDouble("RGDX");
				erx=erx+rg;
				int n=len-i;
				tc=(len-n)*interval;
				double fcj=(rx-erx)*60/((len-n)*interval);
				fcj=new BigDecimal(fcj).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				data.put("FCJ", fcj);
				double nrg=0;
				if(i>0){
					nrg=tableData.getJSONObject(i-1).getDouble("RGDX");
				}
				System.out.println("---------rg----="+rg+"---------fcj----="+fcj+"-----------len-n----"+i);
				if(fcj>rg){
					double wc=0;
					double fc=fcj;
					if(i>0 && fcj<nrg){
						wc=0;
						fc=fcj;
					}else if(i>0 && fcj>=nrg){
						wc=fcj-nrg;
						fc=nrg;
					}else if(i==0){
						wc=0;
						fc=fcj;
					}
					fc=new BigDecimal(fc).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					Map map = new HashMap();
					map.put("FCJ", fcj);
					map.put("FCS", fcj);
					map.put("FC", fc);
					map.put("WC", wc);
					map.put("N", i);
					double th=tc/60;
					th=new BigDecimal(th).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					map.put("TC", th);
					double erhj=getRcValue(tableData,i);
					double rctc=tc!=0?erhj*60/tc:0;
					rctc=new BigDecimal(rctc).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					map.put("RCTC", rctc);
					erhj=new BigDecimal(erhj).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					map.put("RC", erhj);
					relist.add(map);
				}
			}
			if(relist!=null && relist.size()>1){
				Collections.sort(relist, new Comparator<Map>(){
					@Override
					public int compare(Map o1, Map o2) {
						if(Double.valueOf(o1.get("WC").toString()).doubleValue()>Double.valueOf(o2.get("WC").toString())){
							return 1;
						}else if(Double.valueOf(o1.get("WC").toString()).doubleValue()<Double.valueOf(o2.get("WC").toString())){
							return -1;
						}else{
							return 0;
						}
					}
				});
				return relist.get(0);
			}
			return null;
		}
		public List<Map> queryStep6RrffEcharts(String stcd){
			return resuldDao.queryStep6RrffEcharts(stcd);
		}
		
		
		
		
		   /**
	     * splitAry方法<br>
	     * @param ary 要分割的数组
	     * @param subSize 分割的块大小
	     * @return
	     *
	     */
	    private static Object[] splitAry(String[] ary, int subSize) {
	         int count = ary.length % subSize == 0 ? ary.length / subSize: ary.length / subSize + 1;
	 
	         List<List<String>> subAryList = new ArrayList<List<String>>();
	 
	         for (int i = 0; i < count; i++) {
	          int index = i * subSize;
	          List<String> list = new ArrayList<String>();
	          int j = 0;
	              while (j < subSize && index < ary.length) {
	                  list.add(ary[index++]);
	                  j++;
	              }
	          subAryList.add(list);
	         }
	          
	         Object[] subAry = new Object[subAryList.size()];
	          
	         for(int i = 0; i < subAryList.size(); i++){
	              List<String> subList = subAryList.get(i);
	              String[] subAryItem = new String[subList.size()];
	              for(int j = 0; j < subList.size(); j++){
	                  subAryItem[j] = subList.get(j);
	              }
	              subAry[i] = subAryItem;
	         }
	          
	         return subAry;
	         }
	
}
