package com.lyht.business.analysisCalculation.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lyht.base.hibernate.basedao.HibernateBaseDao;
import com.lyht.base.hibernate.common.PageResults;
import com.lyht.business.analysisCalculation.bean.ResultFourth;
import com.lyht.business.analysisCalculation.formbean.ResultFourthFormBean;
import com.lyht.util.CommonUtil;
import com.lyht.util.Randomizer;
/**
 *产流计算第4步计算结果保存DAO
 * @author 刘魁
 */
@Repository
@Scope("prototype")
public class ResultFourthDao extends HibernateBaseDao<ResultFourth> {
	
		/**
		 * 保存计算结果4
		 */
	public void saveResutFourth(ResultFourth ResutFourth) {
		StringBuilder sql=new StringBuilder();
		String id=java.util.UUID.randomUUID().toString();
		sql.append("INSERT INTO C_RESULT_FOURTH (TM,INTERVAL,QDX,QT,ID,STCD,PCH) ");
		sql.append("VALUES('"+ResutFourth.getTm()+"','"+CommonUtil.trim(ResutFourth.getInterval())+"','"+CommonUtil.trim(ResutFourth.getQdx())+"',");
		sql.append("'"+ResutFourth.getQt()+"','"+CommonUtil.trim(id)+"','"+ResutFourth.getStcd()+"','"+ResutFourth.getPch()+"')");
		this.exectueSQL(sql.toString());
	}
	/**
	 * 删除第四步
	 * @param ResutFourth
	 */
	public void delResutFourth(ResultFourth ResutFourth) {
		StringBuilder sql=new StringBuilder();
		sql.append("DELETE FROM  C_RESULT_FOURTH WHERE  PCH='"+ResutFourth.getPch()+"' AND STCD='"+ResutFourth.getStcd()+"'");
		this.exectueSQL(sql.toString());
	}
	
	/**
	 * 根据时间获取单条数据
	 * @param ResutFourth
	 * @return
	 */
	public ResultFourth getResutFourthByTm(ResultFourth ResutFourth) {
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT * FROM C_RESULT_FOURTH WHERE TM='"+ResutFourth.getTm()+"'");
		List<ResultFourth> ResutFourthList=null;
		ResultFourth thrid=new ResultFourth();
		try{
			ResutFourthList=this.getSession().createSQLQuery(sql.toString()).addEntity(ResultFourth.class).list();
			for(int i=0;i<ResutFourthList.size();i++){
				thrid=ResutFourthList.get(0);
			}
		}catch (Exception e) {
			e.getStackTrace();
		}
		return thrid;
	}
	
	/**
	 * 获取table数据
	 */
	public PageResults getStep4Table(ResultFourthFormBean ResutFourth) {
		StringBuffer  sql=new StringBuffer ();
		sql.append("SELECT CONVERT(CHAR(16),  C.TM, 120)DATE,C.INTERVAL SC,C.QDX LL,C.QT QT FROM C_RESULT_FOURTH C  ");
		sql.append("WHERE C.STCD='"+ResutFourth.getResultFourth().getStcd()+"' AND C.PCH='"+ResutFourth.getResultFourth().getPch()+"'  ");
		sql.append("ORDER BY C.TM ASC");
		int pageNo=ResutFourth.getPageBean().getLimit()>0?ResutFourth.getPageBean().getOffset()/ResutFourth.getPageBean().getLimit():0;
		pageNo=pageNo+1;
		ResutFourth.getPageBean().setPageNo(pageNo);
		return this.findPageByFetchedSql(sql.toString(), null, ResutFourth.getPageBean().getPageNo(),ResutFourth.getPageBean().getLimit(), null);
	}
	
	/**
	 * 流量(地下)求和 table
	 */
	public List  sumQdx(ResultFourthFormBean ResutFourth) {
		StringBuffer  sql=new StringBuffer ();
		sql.append("SELECT SUM(C.QDX) LL FROM C_RESULT_FOURTH C WHERE C.STCD='"+ResutFourth.getResultFourth().getStcd()+"'");
		return this.createSQLQuerybyMap(sql.toString());
	}
	
	/** 
	 * Q*T求和 table
	 */
	public List sumQT(ResultFourthFormBean ResutFourth) {
		StringBuffer  sql=new StringBuffer ();
		sql.append("SELECT SUM(C.QT) QT FROM C_RESULT_FOURTH C WHERE C.STCD='"+ResutFourth.getResultFourth().getStcd()+"'");
		return this.createSQLQuerybyMap(sql.toString());
	}
	
	public List<Map> sumQt(String stcd,String pch){
		StringBuffer  sql=new StringBuffer ();
		sql.append("SELECT SUM(C.QT) QT FROM C_RESULT_FOURTH C WHERE C.STCD='"+stcd+"' and C.PCH='"+pch+"'");
		return this.createSQLQuerybyMap(sql.toString());
	}
	
	/**
	 * 汇流计算第一步的第一个表格数据 查询方法
	 */
	public List<Map> getHuiLiuStep1Table1(String stcd,String pch) {
		StringBuffer  sql=new StringBuffer ();
		sql.append("SELECT  CONVERT(CHAR(16),  C.TM, 120) DATE,ISNULL(B.Q, 0 ) LL,  C.QDX LLDX FROM C_RESULT_FOURTH C LEFT JOIN    ");
		sql.append("C_RESULT_THRID B ON C.STCD=B.STCD AND C.PCH=B.PCH AND C.TM=B.TM WHERE 1=1 ");
		if(CommonUtil.trim(pch).length()>0){
			sql.append(" and C.PCH='"+CommonUtil.trim(pch)+"'");
		}
		if(CommonUtil.trim(stcd).length()>0){
			sql.append(" and C.STCD='"+CommonUtil.trim(stcd)+"'");
		}
		sql.append("  ORDER BY C.TM ASC ");
		return this.createSQLQuerybyMap(sql.toString());
	}
	public double queryChanliuStep4SumQt(String stcd,String pch){
		StringBuffer sql = new StringBuffer("SELECT STCD,PCH,SUM(QT) AS QT FROM C_RESULT_FOURTH WHERE STCD='"+stcd+"' and PCH='"+pch+"' group by STCD,PCH");
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
	 * 统计汇流第二步第一个计算的R和
	 * @param stcd
	 * @param pch
	 * @return
	 */
	public Map getHuiliuStep2Calc1Rhj(String stcd,String pch){
		StringBuffer sql = new StringBuffer("SELECT B.STCD,B.PCH,(ISNULL(B.RS,0)-ISNULL(B.RX,0)) AS R,B.LYMJ AS LLMJ FROM C_RESULT_SECOND B WHERE B.STCD='"+stcd+"' and B.PCH='"+pch+"'");
		List<Map> relist = this.createSQLQuerybyMap(sql.toString());
		if(relist!=null && relist.size()>0){
			return relist.get(0);
		}
		return null;
	}
	/**
	 * 汇流计算第一步的第二个表格数据查询方法
	 * @param rf
	 * @return
	 */
	public List<Map> getHuiLiuStep1Table2(String stcd,String pch){
		StringBuffer sql = new StringBuffer("SELECT CONVERT(CHAR(16),  A.TM, 120) DT,ISNULL(A.R,0) AS R "
				+ " FROM C_RESULT_SIXTH A "
				+ "  WHERE 1=1 AND A.STCD='"+stcd+"' and A.PCH='"+pch+"'");
		sql.append("  ORDER BY A.TM ASC ");
		return this.createSQLQuerybyMap(sql.toString());
	}
	public Map queryHuiliuStep1OfRg(String stcd,String pch){
		StringBuffer sql = new StringBuffer("SELECT A.QT AS QT3,B.QT AS QT4,A.STCD,A.PCH FROM"
				+ " (SELECT SUM(QT) AS QT,STCD,PCH FROM C_RESULT_THRID where STCD='"+stcd+"' and PCH='"+pch+"' GROUP BY STCD,PCH) A"
						+ " LEFT JOIN (SELECT SUM(QT) AS QT,STCD,PCH FROM C_RESULT_FOURTH where STCD='"+stcd+"' and PCH='"+pch+"' GROUP BY STCD,PCH) B ON"
								+ " A.STCD=B.STCD AND A.PCH=B.PCH "
								+ " WHERE A.STCD='"+stcd+"' AND A.PCH='"+pch+"'");
		List<Map> relist = this.createSQLQuerybyMap(sql.toString());
		if(relist!=null && relist.size()>0){
			return relist.get(0);
		}
		return null;
	}
	/**
	 * 查询流域面积
	 * @param stcd
	 * @param pch
	 * @return
	 */
	public Map getHuiLiuStep1Lymj(String stcd,String pch){
		StringBuffer sql = new StringBuffer("select * from C_RESULT_SECOND where PCH='"+pch+"' and STCD='"+stcd+"'");
		List<Map> mapList = this.createSQLQuerybyMap(sql.toString());
		if(mapList!=null && mapList.size()>0){
			return mapList.get(0);
		}
		return null;
	}
	/**
	 * 查询汇流计算第三步表格3数据
	 * @param stcd
	 * @param pch
	 * @return
	 */
	public List<Map> queryHuiliuStep3Table3(String stcd,String pch){
		StringBuffer sql = new StringBuffer("SELECT CONVERT(CHAR(16),  A.TM, 120) DT,A.STCD,A.PCH,"
				+ " (ISNULL(A.R,0)-ISNULL(B.R,0)) AS RS,B.Q FROM C_RESULT_SIXTH A "
				+ " LEFT JOIN C_RESULT_THRID B ON A.STCD=B.STCD AND A.PCH=B.PCH AND A.TM=B.TM"
				+ " WHERE A.STCD='"+stcd+"' and A.PCH='"+pch+"' order by A.TM ASC ");
		return this.createSQLQuerybyMap(sql.toString());
	}
	/**
	 * 根据批次号和站码查询时间间隔
	 * @param stcd
	 * @param pch
	 * @return
	 */
	public Map getSjjgByStcdAndPch(String stcd,String pch){
		StringBuffer sql = new StringBuffer("SELECT * FROM C_RESULT_JG WHERE STCD='"+stcd+"' and PCH='"+pch+"'");
		List<Map> list = this.createSQLQuerybyMap(sql.toString());
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 查询汇流计算第一步的测站下拉数据
	 * @return
	 */
	public List<Map> getHuiLiuStep1StcdSelect(){
		StringBuffer sql = new StringBuffer("SELECT A.STCD,B.STNM FROM C_RESULT_FOURTH A"
				+ " LEFT JOIN ST_STBPRP_B B ON A.STCD=B.STCD GROUP BY A.STCD,B.STNM ");
		return this.createSQLQuerybyMap(sql.toString());
	}
	/**
	 * 查询第二步第一个表格数据
	 * @param stcd
	 * @param pch
	 * @return
	 */
	public List<Map> getHuiliuStep2Table1(String stcd,String pch){
		StringBuffer sql = new StringBuffer("SELECT ISNULL(A.R,0) AS Rt,CONVERT(CHAR(16),  A.TM, 120) t FROM C_RESULT_SIXTH A"
				+ " WHERE A.STCD='"+stcd+"' and A.PCH='"+pch+"' order by A.TM ASC ");
		return this.createSQLQuerybyMap(sql.toString());
	}
	public List<Map> queryHuiliuStep3SubmitData(String stcd,String pch){
		StringBuffer sql = new StringBuffer("SELECT ISNULL(A.R,0) AS Rt,CONVERT(CHAR(16),  A.TM, 120) t,ISNULL(C.Q,0) AS Q FROM C_RESULT_SIXTH A"
				+ " LEFT JOIN ST_RIVER_R C ON A.STCD=C.STCD AND A.TM=C.TM WHERE A.STCD='"+stcd+"' and A.PCH='"+pch+"' order by A.TM ASC ");
		return this.createSQLQuerybyMap(sql.toString());
	}
	public double queryMaxRt(String stcd,String pch){
		StringBuffer sql = new StringBuffer("SELECT max(ISNULL(A.R,0)-ISNULL(B.R,0)) AS Rt FROM C_RESULT_SIXTH A"
				+ " LEFT JOIN C_RESULT_THRID B ON A.STCD=B.STCD AND A.TM=B.TM AND A.PCH=B.PCH"
				+ " WHERE A.STCD='"+stcd+"' and A.PCH='"+pch+"'");
		List<Map> relist=this.createSQLQuerybyMap(sql.toString());
		if(relist!=null && relist.size()>0){
			Map map = relist.get(0);
			return Double.valueOf(map.get("Rt").toString()).doubleValue();
		}
		return 0;
	}
	public Map queryChanliuStep2Data(String stcd,String pch){
		StringBuffer sql = new StringBuffer("SELECT * FROM C_RESULT_SECOND WHERE STCD='"+stcd+"' and PCH='"+pch+"'");
		List<Map> relist = this.createSQLQuerybyMap(sql.toString());
		if(relist!=null && relist.size()>0){
			return relist.get(0);
		}
		return new HashMap();
	}
	public Map queryChanliuIntervalData(String stcd,String pch){
		StringBuffer sql = new StringBuffer("SELECT * FROM C_RESULT_JG WHERE STCD='"+stcd+"' and PCH='"+pch+"'");
		List<Map> relist = this.createSQLQuerybyMap(sql.toString());
		if(relist!=null && relist.size()>0){
			return relist.get(0);
		}
		return new HashMap();
	}
	/**
	 * 查询汇流计算第二步的测站下拉数据
	 * @return
	 */
	public List<Map> getHuiLiuStep2StcdSelect(String searchName){
		StringBuffer sql = new StringBuffer("SELECT A.STCD,B.STNM,B.LGTD1,B.LTTD1 FROM C_RESULT_SIXTH A"
				+ " LEFT JOIN ST_STBPRP_B B ON A.STCD=B.STCD where 1=1 and B.STTP!='PP' ");
		if(searchName!=null && searchName.trim().length()>0 && !searchName.trim().equals("*")){
			sql.append(" and B.STNM LIKE '%"+searchName.trim()+"%'");
		}
		sql.append(" GROUP BY A.STCD,B.STNM,B.LGTD1,B.LTTD1 ");
		return this.createSQLQuerybyMap(sql.toString());
	}
	/**
	 * 查询汇流计算第二步的批次号下拉数据
	 * @return
	 */
	public List<Map> getHuiLiuStep2PchSelect(String stcd){
		StringBuffer sql = new StringBuffer("SELECT A.PCH FROM C_RESULT_SIXTH A where 1=1");
		sql.append(" and A.STCD='"+stcd+"'");
		sql.append(" GROUP BY A.PCH ");
		return this.createSQLQuerybyMap(sql.toString());
	}
	/**
	 * 查询汇流计算第一步的批次号下拉数据
	 * @return
	 */
	public List<Map> getHuiLiuStep1PchSelect(){
		StringBuffer sql = new StringBuffer("SELECT A.PCH FROM C_RESULT_FOURTH A GROUP BY A.PCH");
		return this.createSQLQuerybyMap(sql.toString());
	}
}
