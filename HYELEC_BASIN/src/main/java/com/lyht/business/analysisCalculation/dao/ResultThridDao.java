package com.lyht.business.analysisCalculation.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lyht.base.hibernate.basedao.HibernateBaseDao;
import com.lyht.base.hibernate.common.PageResults;
import com.lyht.business.analysisCalculation.bean.Result;
import com.lyht.business.analysisCalculation.bean.ResultThrid;
import com.lyht.business.analysisCalculation.formbean.ResultFormBean;
import com.lyht.business.analysisCalculation.formbean.ResultThridFormBean;
import com.lyht.business.consumer.hydrologicalData.bean.River;
import com.lyht.util.CommonUtil;
import com.lyht.util.Randomizer;
/**
 *产流计算第三步计算结果保存DAO
 * @author 刘魁
 */
@Repository
@Scope("prototype")
public class ResultThridDao extends HibernateBaseDao<ResultThrid> {
	
		/**
		 * 保存计算结果3
		 */
	public void saveResultThrid(ResultThrid resultThrid) {
		StringBuilder sql=new StringBuilder();
			String id=Randomizer.nextNumber("rt", 6);
		sql.append("INSERT INTO C_RESULT_THRID (TM,INTERVAL,Q,QT,SUMQ,R,STCD,ID,SUMQT) ");
		sql.append("VALUES('"+resultThrid.getTm()+"','"+CommonUtil.trim(resultThrid.getInterval())+"','"+CommonUtil.trim(resultThrid.getQ())+"',");
		sql.append("'"+CommonUtil.trim(resultThrid.getQt())+"','"+CommonUtil.trim(resultThrid.getSumQ())+"','"+CommonUtil.trim(resultThrid.getR())+"','"+CommonUtil.trim(resultThrid.getStcd())+"',");
		sql.append("'"+id+"','"+CommonUtil.trim(resultThrid.getSumQt())+"')");
		this.exectueSQL(sql.toString());
	}
	
	/**
	 * 根据时间获取单条数据
	 * @param resultThrid
	 * @return
	 */
	public ResultThrid getResultThridByTm(ResultThrid resultThrid) {
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT * FROM C_RESULT_THRID WHERE TM='"+resultThrid.getTm()+"'");
		List<ResultThrid> resultThridList=null;
		ResultThrid thrid=new ResultThrid();
		try{
			resultThridList=this.getSession().createSQLQuery(sql.toString()).addEntity(ResultThrid.class).list();
			for(int i=0;i<resultThridList.size();i++){
				thrid=resultThridList.get(0);
			}
		}catch (Exception e) {
			e.getStackTrace();
		}
		return thrid;
	}
	
	/**
	 * 获取数据
	 */
	public PageResults getStep3(ResultThridFormBean resultThrid) {
		StringBuffer  sql=new StringBuffer ();
		sql.append("SELECT CONVERT(CHAR(16),  C.TM, 120)DATE,C.INTERVAL SC,C.Q LL,C.QT QT FROM C_RESULT_THRID C ORDER BY C.TM ASC");
		return this.findPageByFetchedSql(sql.toString(), null, resultThrid.getPageBean().getOffset(),resultThrid.getPageBean().getLimit(), null);
	}
	
	/**
	 * 流量求和
	 */
	public List  sumQ(ResultThridFormBean resultThrid) {
		StringBuffer  sql=new StringBuffer ();
		sql.append("SELECT SUM(C.Q) LL FROM C_RESULT_THRID C WHERE C.STCD='"+resultThrid.getResultThrid().getStcd()+"'");
		return this.createSQLQuerybyMap(sql.toString());
	}
	
	/**
	 * Q*T求和
	 */
	public List  sumQT(ResultThridFormBean resultThrid) {
		StringBuffer  sql=new StringBuffer ();
		sql.append("SELECT SUM(C.QT) QT FROM C_RESULT_THRID C WHERE C.STCD='"+resultThrid.getResultThrid().getStcd()+"'");
		return this.createSQLQuerybyMap(sql.toString());
	}
	
	/**
	 * 产流，第四步左边的数据查询接口
	 */
	public PageResults getStep4(ResultThridFormBean resultThrid ) {
		StringBuffer  sql=new StringBuffer ();
		String stcd=resultThrid.getResultThrid().getStcd();
		sql.append("SELECT 	CONVERT(CHAR(16),  C.TM, 120)DATE, ISNULL(C.Q, 0 ) LL FROM C_RESULT_THRID C ");
		if(stcd!=null||!stcd.equals("")) {
			sql.append("  WHERE  C.STCD='"+stcd+"'  ORDER BY C.TM ASC ");
		}
		return this.findPageByFetchedSql(sql.toString(), null, resultThrid.getPageBean().getOffset(),resultThrid.getPageBean().getLimit(), null);
	}
	
	
	/**
	 * 第四步流量的最大值
	 */
	public List  maxQ(ResultThridFormBean resultThrid) {
		StringBuffer  sql=new StringBuffer ();
		sql.append("SELECT MAX(C.Q) LL FROM C_RESULT_THRID C WHERE C.STCD='"+resultThrid.getResultThrid().getStcd()+"'");
		return this.createSQLQuerybyMap(sql.toString());
	}
}