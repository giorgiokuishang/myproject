package com.lyht.business.analysisCalculation.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lyht.base.hibernate.basedao.HibernateBaseDao;
import com.lyht.base.hibernate.common.PageResults;
import com.lyht.business.analysisCalculation.bean.ResultFifth;
import com.lyht.business.analysisCalculation.bean.ResultFourth;
import com.lyht.business.analysisCalculation.formbean.ResultFifthFormBean;
import com.lyht.business.analysisCalculation.formbean.ResultFormBean;
import com.lyht.util.CommonUtil;
import com.lyht.util.Randomizer;
/**
 * 第五步DAO
 * @author 刘魁
 *
 */
@Repository
@Scope("prototype")
public class ResultFifthDao  extends HibernateBaseDao<ResultFifth>{
	public void saveResutFifth(ResultFifth rf) {
		StringBuilder sql=new StringBuilder();
		String id=java.util.UUID.randomUUID().toString();
		sql.append("INSERT INTO C_RESULT_FIFTH (ID,DATE,STCD,YML,EM,PA,PCH,STNM,QZ,JYL,INTERVAL) ");
		sql.append("VALUES('"+id+"','"+CommonUtil.trim(rf.getDate())+"','"+CommonUtil.trim(rf.getStcd())+"',");
		sql.append("'"+rf.getYml()+"',"+rf.getEm()+","+rf.getPa()+" ,'"+rf.getPch()+"', ");
		sql.append("'"+rf.getStnm()+"','"+rf.getQz()+"', '"+rf.getJyl()+"' ,'"+rf.getInterval()+"')");
		this.exectueSQL(sql.toString());
	}
	
	public void  delf(ResultFifth rf) {
		StringBuilder sql=new StringBuilder();
		sql.append("DELETE FROM C_RESULT_FIFTH ");
		sql.append("WHERE STCD='"+rf.getStcd()+"' AND PCH='"+rf.getPch()+"' AND DATE='"+rf.getDate()+"'");
		this.exectueSQL(sql.toString());
	}
	public void deleteByStcdAndPch(String stcd,String pch){
		StringBuilder sql=new StringBuilder();
		sql.append("DELETE FROM C_RESULT_FIFTH ");
		sql.append("WHERE STCD='"+stcd+"' AND PCH='"+pch+"'");
		this.exectueSQL(sql.toString());
	}
	public PageResults getFif(ResultFifthFormBean rfBean){
		StringBuffer  sql=new StringBuffer ();
		sql.append("SELECT F.*,P.PA  PA1 ,CONVERT(CHAR(16),  F.DATE, 120) AS DT FROM C_RESULT_FIFTH F  ");
		sql.append("  LEFT JOIN C_FIFTH_PA P ON  F.STCD=P.STCD AND F.PCH=P.PCH  ");
		sql.append("WHERE F.PCH='"+rfBean.getResultFifthFormBean().getPch()+"' AND F.STCD='"+rfBean.getResultFifthFormBean().getStcd()+"'  ORDER BY F.DATE ASC");
		int pageNo=rfBean.getPageBean().getLimit()>0?rfBean.getPageBean().getOffset()/rfBean.getPageBean().getLimit():0;
		pageNo=pageNo+1;
		rfBean.getPageBean().setPageNo(pageNo);
		return this.findPageByFetchedSql(sql.toString(), null, rfBean.getPageBean().getPageNo(),rfBean.getPageBean().getLimit(), null);
	}
}
