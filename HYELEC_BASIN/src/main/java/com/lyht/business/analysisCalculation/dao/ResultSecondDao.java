package com.lyht.business.analysisCalculation.dao;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lyht.base.hibernate.basedao.HibernateBaseDao;
import com.lyht.base.hibernate.common.PageResults;
import com.lyht.business.analysisCalculation.bean.ResultSecond;
import com.lyht.util.CommonUtil;
import com.lyht.util.Randomizer;
/**
 * ResultSecondDao
 * @author 刘魁
 *
 */
@Repository
@Scope("prototype")
@SuppressWarnings("rawtypes")
public class ResultSecondDao extends HibernateBaseDao<ResultSecond> {
	//保存
	public void saveResultSecond(ResultSecond rs) {
		StringBuilder sql=new StringBuilder();
		String id=java.util.UUID.randomUUID().toString();
	sql.append("INSERT INTO C_RESULT_SECOND (STCD,LYMJ,PCH,ID,PA,RS,RX,SQT3,SQT4 )");
	sql.append("VALUES('"+rs.getSTCD()+"',"+CommonUtil.trim(rs.getLLMJ())+",'"+CommonUtil.trim(rs.getPch())+"','"+id+"' ,");
	sql.append(""+rs.getPa()+","+rs.getRs()+" , "+rs.getRx()+","+rs.getSqt3()+","+rs.getSqt4()+" ) ");
	this.exectueSQL(sql.toString());
	}
	
	/**
	 * 删除流域面积
	 */
	public void delResultSecond(ResultSecond rs) {
		StringBuilder sql=new StringBuilder();
	sql.append("DELETE  FROM C_RESULT_SECOND WHERE STCD='"+rs.getSTCD()+"' AND PCH='"+rs.getPch()+"'");
	this.exectueSQL(sql.toString());
	}
	public void updateI1AndI2(ResultSecond rs){
		StringBuilder sql=new StringBuilder();
		sql.append("UPDATE C_RESULT_SECOND SET I1="+rs.getI1()+",I2="+rs.getI2());
		sql.append("WHERE STCD='"+rs.getSTCD()+"' AND PCH='"+rs.getPch()+"'");
		this.exectueSQL(sql.toString());
	}
	public void updateQ1AndQ2(ResultSecond rs){
		StringBuilder sql=new StringBuilder();
		sql.append("UPDATE C_RESULT_SECOND SET Q1="+rs.getQ1()+",Q2="+rs.getQ2());
		sql.append("WHERE STCD='"+rs.getSTCD()+"' AND PCH='"+rs.getPch()+"'");
		this.exectueSQL(sql.toString());
	}
	//修改LYMj
	public void updateSecondLymj(ResultSecond rs) {
		StringBuilder sql=new StringBuilder();
		sql.append("UPDATE C_RESULT_SECOND SET LYMJ="+rs.getLLMJ()+"");
		sql.append("WHERE STCD='"+rs.getSTCD()+"' AND PCH='"+rs.getPch()+"'");
		this.exectueSQL(sql.toString());
	}
	//修改RS
	public void updateSecondRs(ResultSecond rs) {
		StringBuilder sql=new StringBuilder();
		sql.append("UPDATE C_RESULT_SECOND SET RS="+rs.getRs()+",SQT3="+rs.getSqt3());
		sql.append("WHERE STCD='"+rs.getSTCD()+"' AND PCH='"+rs.getPch()+"'");
		this.exectueSQL(sql.toString());
	}
	//修改RX
		public void updateSecondRx(ResultSecond rs) {
			StringBuilder sql=new StringBuilder();
			sql.append("UPDATE C_RESULT_SECOND SET RX="+rs.getRx()+",SQT4="+rs.getSqt4());
			sql.append("WHERE STCD='"+rs.getSTCD()+"' AND PCH='"+rs.getPch()+"'");
			this.exectueSQL(sql.toString());
		}
		public void updateSecondFcinfo(ResultSecond rs){
			StringBuilder sql=new StringBuilder();
			sql.append("UPDATE C_RESULT_SECOND SET FCS="+rs.getFcs()+",FCJ="+rs.getFcj()+",FC="+rs.getFc()+",TC="+rs.getTc());
			sql.append("WHERE STCD='"+rs.getSTCD()+"' AND PCH='"+rs.getPch()+"'");
			this.exectueSQL(sql.toString());
		}
		//修改PA
		public void updateSecondPa(ResultSecond rs) {
			StringBuilder sql=new StringBuilder();
			sql.append("UPDATE C_RESULT_SECOND SET PA="+rs.getPa()+"");
			sql.append("WHERE STCD='"+rs.getSTCD()+"' AND PCH='"+rs.getPch()+"'");
			this.exectueSQL(sql.toString());
		}
	//根据STCD,PCH查询
	public PageResults getSecond(ResultSecond rs) {
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT   S.*  FROM C_RESULT_SECOND  S WHERE S.STCD='"+rs.getSTCD()+"' AND S.PCH='"+rs.getPch()+"'");
		return this.findPageByFetchedSql(sql.toString(), null, 0, 100000000, null);
	}
	public List<Map> querySecondByStcdAndPch(String stcd,String pch){
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT   S.*  FROM C_RESULT_SECOND  S WHERE S.STCD='"+stcd+"' AND S.PCH='"+pch+"'");
		return this.createSQLQuerybyMap(sql.toString());
	}
	public ResultSecond queryResultSecondByStcdAndPch(String stcd,String pch){
		StringBuffer hql = new StringBuffer("from ResultSecond where stcd=? and pch=?");
		List<ResultSecond> relist = this.getListByHQL(hql.toString(), new Object[]{stcd,pch});
		if(relist!=null && relist.size()>0){
			return relist.get(0);
		}
		return null;
	}
}
