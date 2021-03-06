package com.lyht.business.analysisCalculation.dao;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lyht.base.hibernate.basedao.HibernateBaseDao;
import com.lyht.base.hibernate.common.PageResults;
import com.lyht.business.analysisCalculation.bean.ResultThrid;
import com.lyht.business.analysisCalculation.formbean.ResultThridFormBean;
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
			String id=java.util.UUID.randomUUID().toString();
		sql.append("INSERT INTO C_RESULT_THRID (TM,INTERVAL,Q,QT,SUMQ,R,STCD,ID,SUMQT,PCH) ");
		sql.append("VALUES('"+resultThrid.getTm()+"',"+resultThrid.getInterval()+","+resultThrid.getQ()+",");
		sql.append(""+resultThrid.getQt()+","+resultThrid.getSumQ()+","+resultThrid.getR()+",'"+CommonUtil.trim(resultThrid.getStcd())+"',");
		sql.append("'"+id+"',"+CommonUtil.trim(resultThrid.getSumQt())+",'"+resultThrid.getPch()+"')");
//		System.out.println("========sql============="+sql.toString());
		this.exectueSQL(sql.toString());
	}
	public int deleteResultByStcdAndPch(String stcd,String pch){
		StringBuffer sql = new StringBuffer("delete from C_RESULT_THRID where stcd='"+stcd+"' and pch='"+pch+"'");
		return this.excuteSql(sql.toString(), new Object[]{});
	}
	public double queryStep3SumQt(String stcd,String pch){
		StringBuffer sql = new StringBuffer("SELECT STCD,PCH,SUM(QT) AS QT FROM C_RESULT_THRID WHERE STCD='"+stcd+"' and PCH='"+pch+"' group by STCD,PCH");
		List<Map> relist = this.createSQLQuerybyMap(sql.toString());
		if(relist!=null && relist.size()>0){
			Map map = relist.get(0);
			if(map!=null && map.get("QT")!=null){
				return CommonUtil.getFloatValue(map.get("QT").toString());
			}
		}
		return 0;
	}
	/**
	 * 删除计算结果3
	 */
	public void delResultThrid(ResultThrid resultThrid) {
		StringBuilder sql=new StringBuilder();
	sql.append("DELETE FROM  C_RESULT_THRID WHERE  PCH='"+resultThrid.getPch()+"' AND STCD='"+resultThrid.getStcd()+"'");
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
		sql.append("SELECT CONVERT(CHAR(16),  C.TM, 120)DATE,C.INTERVAL SC,C.Q LL,C.QT QT FROM C_RESULT_THRID C  ");
		sql.append("WHERE C.STCD='"+resultThrid.getResultThrid().getStcd()+"' AND C.PCH='"+resultThrid.getResultThrid().getPch()+"'   ");
		sql.append("ORDER BY C.TM ASC");
		int pageNo=resultThrid.getPageBean().getLimit()>0?resultThrid.getPageBean().getOffset()/resultThrid.getPageBean().getLimit():0;
		pageNo=pageNo+1;
		resultThrid.getPageBean().setPageNo(pageNo);
		return this.findPageByFetchedSql(sql.toString(), null, resultThrid.getPageBean().getPageNo(),resultThrid.getPageBean().getLimit(), null);
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
	
	public List<Map> queryStepLlEchartData(String stcd,String pch){
		StringBuffer  sql=new StringBuffer ();
		sql.append("SELECT 	CONVERT(CHAR(16),  C.TM, 120)DATE, ISNULL(C.Q, 0 ) LL,ISNULL(C.QT,0) AS QT,C.INTERVAL FROM C_RESULT_THRID C ");
		sql.append("  WHERE  C.STCD='"+stcd+"'   ");
		sql.append("  AND  C.PCH='"+pch+"'  ");
		sql.append("ORDER BY C.TM ASC ");
		return this.createSQLQuerybyMap(sql.toString());
	}
	public List<Map> queryHuiliuStep3QsData(String stcd,String pch) {
		StringBuffer  sql=new StringBuffer ();
		sql.append("SELECT  CONVERT(CHAR(16),  C.TM, 120) DATE,(case when (ISNULL(B.Q, 0 )-ISNULL(C.QDX,0))<0 then 0 else (ISNULL(B.Q, 0 )-ISNULL(C.QDX,0)) end) AS LL,ISNULL(B.QT,0) AS QT,B.INTERVAL FROM C_RESULT_THRID B "
				+ "LEFT JOIN  C_RESULT_FOURTH C  ");
		sql.append(" ON B.STCD=C.STCD AND B.PCH=C.PCH AND B.TM=C.TM WHERE 1=1 ");
		if(CommonUtil.trim(pch).length()>0){
			sql.append(" and C.PCH='"+CommonUtil.trim(pch)+"'");
		}
		if(CommonUtil.trim(stcd).length()>0){
			sql.append(" and C.STCD='"+CommonUtil.trim(stcd)+"'");
		}
		sql.append("  ORDER BY C.TM ASC ");
		return this.createSQLQuerybyMap(sql.toString());
	}
	public List<Map> queryStepLlLessEchartData(String stcd,String pch){
		StringBuffer  sql=new StringBuffer ();
		sql.append("SELECT 	CONVERT(CHAR(16),  C.TM, 120)DATE, ISNULL(C.Q, 0 ) LL,ISNULL(C.QT,0) AS QT,C.INTERVAL FROM C_RESULT_THRID_LESS C ");
		sql.append("  WHERE  C.STCD='"+stcd+"'   ");
		sql.append("  AND  C.PCH='"+pch+"'  ");
		sql.append("ORDER BY C.TM ASC ");
		return this.createSQLQuerybyMap(sql.toString());
	}
	public List<Map> queryStep4ResultData(String stcd,String pch){
		StringBuffer  sql=new StringBuffer ();
		sql.append("SELECT 	CONVERT(CHAR(16),  C.TM, 120)DATE, ISNULL(C.QDX, 0 ) LL,C.QT,C.INTERVAL FROM C_RESULT_FOURTH C ");
		sql.append("  WHERE  C.STCD='"+stcd+"'   ");
		sql.append("  AND  C.PCH='"+pch+"'  ");
		sql.append("ORDER BY C.TM ASC ");
		return this.createSQLQuerybyMap(sql.toString());
	}
	/**
	 * 产流，第四步左边的数据查询接口
	 */
	public PageResults getStep4(ResultThridFormBean resultThrid ) {
		StringBuffer  sql=new StringBuffer ();
		String stcd=resultThrid.getResultThrid().getStcd();
		String pch=resultThrid.getResultThrid().getPch();
		sql.append("SELECT 	CONVERT(CHAR(16),  C.TM, 120)DATE, ISNULL(C.Q, 0 ) LL FROM C_RESULT_THRID C ");
		if(stcd!=null&&!stcd.equals("")) {
			sql.append("  WHERE  C.STCD='"+stcd+"'   ");
		}
		if(pch!=null&&pch.equals("")) {
			sql.append("  AND  C.PCH='"+pch+"'  ");
		}
		sql.append("ORDER BY C.TM ASC ");
		int pageNo=resultThrid.getPageBean().getLimit()>0?resultThrid.getPageBean().getOffset()/resultThrid.getPageBean().getLimit():0;
		pageNo=pageNo+1;
		resultThrid.getPageBean().setPageNo(pageNo);
		return this.findPageByFetchedSql(sql.toString(), null, resultThrid.getPageBean().getPageNo(),resultThrid.getPageBean().getLimit(), null);
	}
	
	
	/**
	 * 第四步流量的最大值
	 */
	public List  maxQ(ResultThridFormBean resultThrid) {
		StringBuffer  sql=new StringBuffer ();
		sql.append("SELECT MAX(C.Q) MAXJYL ,MIN(C.Q) MINJYL   FROM C_RESULT_THRID C WHERE C.STCD='"+resultThrid.getResultThrid().getStcd()+"' AND C.PCH='"+resultThrid.getResultThrid().getPch()+"'");
		return this.createSQLQuerybyMap(sql.toString());
	}
	/**
	 * 第四步流量的最小值
	 */
	public List  minQ(ResultThridFormBean resultThrid) {
		StringBuffer  sql=new StringBuffer ();
		sql.append("SELECT MIN(C.Q) LL FROM C_RESULT_THRID C WHERE C.STCD='"+resultThrid.getResultThrid().getStcd()+"'  AND C.PCH='"+resultThrid.getResultThrid().getPch()+"'");
		return this.createSQLQuerybyMap(sql.toString());
	}
	/**
	 * 时段
	 * @param resultThrid
	 * @return
	 */
	public List  sd(ResultThridFormBean resultThrid,String name) {
		StringBuffer  sql=new StringBuffer ();
		sql.append("SELECT a.T FROM ST_TSQX_B a LEFT JOIN ST_STBPRP_B b ON a.STCD = b.STCD WHERE 1 = 1 ");
		sql.append(" AND a.STCD = '"+resultThrid.getResultThrid().getStcd()+"'  AND a.USERNAME = '"+name+"' GROUP BY a.T order by a.T ASC ");
		return this.createSQLQuerybyMap(sql.toString());
	}
	/**
	 * 时段
	 * @param resultThrid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> sd(String stcd,String name) {
		StringBuffer  sql=new StringBuffer ();
		sql.append("SELECT a.T FROM ST_TSQX_B a LEFT JOIN ST_STBPRP_B b ON a.STCD = b.STCD WHERE 1 = 1 ");
		sql.append(" AND a.STCD = '"+stcd+"'  AND a.USERNAME = '"+name+"' GROUP BY a.T order by a.T ASC ");
		return this.createSQLQuerybyMap(sql.toString());
	}
	
}
