package com.lyht.business.analysisCalculation.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lyht.base.hibernate.common.PageResults;
import com.lyht.business.analysisCalculation.bean.Result;
import com.lyht.business.analysisCalculation.dao.ResultDao;
import com.lyht.business.analysisCalculation.formbean.ResultFormBean;
import com.lyht.business.consumer.hydrologicalData.bean.River;
import com.lyht.business.consumer.hydrologicalData.dao.RiverDao;
import com.lyht.business.consumer.hydrologicalData.formbean.RiverFormBean;
import com.lyht.business.consumer.hydrologicalData.formbean.TsqxFormBean;
import com.lyht.util.Randomizer;
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
		/**
		 * 保存计算结果
		 */
		@Transactional(propagation=Propagation.REQUIRED)
		public Result saveResult(Result result,Result result1,String result11) {
			String [] jyl=result.getJyl().split(",");
			String [] yml=result11.split(",");
			String [] ymlTime=result.getYmlTime().split(",");
			String [] stnm=result.getStnm().split(",");
			String [] qz=result.getQz().split(",");
			Object [] arr=splitAry(jyl, qz.length); //切割后的降雨量
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
									result2.setYml(Integer.parseInt(yml[i].trim()));//雨面量
									result2.setYmlTime(ymlTime[i].trim());//雨面量时间
									result2.setId(id); //ID
									result2.setCreateStaff(staffCode); //用户编码
									result2.setPch(result.getPch());//批次号
							resuldDao.saveResult(result2);
						}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result1;
		}

		/**
		 * 根据id删除
		 */
		public void delResult(String ids) {
			resuldDao.delResult(ids);
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
		
		/**
		 * 步骤2入口
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
			if(resultFormBean.getResultBean().getYml()==null) {
				//如果没有修改的降雨量就不修改
			}else {//否则修改
				 resuldDao.updateTableData(resultFormBean);//修改降雨量
			}
			 River mRiver=riverDao.getRiverInfoById(mRiverFormBean);
			// if(mRiverFormBean.getmRiverInfoBean().getQ()
			if(mRiver.getStcd()==null||mRiver.getStcd().equals("")|| mRiver.getTm()==null||mRiver.getTm().equals("")) {
				//新增 河道水清-流量
				riverDao.saveRiverInfo(mRiverFormBean.getmRiverInfoBean());
			}else if(mRiverFormBean.getmRiverInfoBean().getQ()==0){	
				
			}else {//修改 河道水清-流量
				riverDao.updateRiverInfo(mRiverFormBean.getmRiverInfoBean());
			}
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
		public List maxLL() {
			return  resuldDao.maxLL(null);
		}
		
		
		/**
		 * JYL最大值
		 * @return
		 */
		@Transactional(propagation=Propagation.REQUIRED)
		public List maxYml() {
			return  resuldDao.maxYml(null);
		}
		/**
		 * 退水曲线洪峰个数统计，用来生成json用
		 */
		@Transactional(propagation=Propagation.REQUIRED)
		public List numHongFeng(ResultFormBean resultFormBean) {
			return resuldDao.numHongFeng(resultFormBean);
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