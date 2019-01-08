package com.lyht.business.consumer.hydrologicalData.dao;

import java.util.List;

import org.apache.struts2.json.bridge.StringBridge;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lyht.base.hibernate.basedao.HibernateBaseDao;
import com.lyht.base.hibernate.common.PageResults;
import com.lyht.business.consumer.hydrologicalData.bean.Tsqx;
import com.lyht.business.consumer.hydrologicalData.formbean.TsqxFormBean;
import com.lyht.util.CommonUtil;
import com.lyht.util.Randomizer;

@Repository
@Scope("prototype")
@SuppressWarnings("rawtypes")
public class TsqxDao extends HibernateBaseDao<Tsqx>{

	/**
	 * 获取退水曲线列表数据
	 * */
	public PageResults getTsqxListData(TsqxFormBean mTsqxFormBean){
		StringBuilder sql=new StringBuilder();
		if(mTsqxFormBean.getmTsqxInfoBean().getStcd()!=null&&!"".equals(mTsqxFormBean.getmTsqxInfoBean().getStcd().trim())){
			sql.append(" SELECT a.STCD AS STCD,a.USERNAME AS USERNAME,"
					+ "a.QM AS QM,a.Q AS Q,a.T AS T,b.STNM AS STNM FROM ST_TSQX_B a LEFT JOIN ST_STBPRP_B b ON a.STCD = b.STCD WHERE 1 = 1 ");
			sql.append(" AND a.STCD = '"+CommonUtil.trim(mTsqxFormBean.getmTsqxInfoBean().getStcd())+"' ");
			if(mTsqxFormBean.getmTsqxInfoBean().getUsername()!= null &&!"".equals(mTsqxFormBean.getmTsqxInfoBean().getUsername().trim())){
				sql.append(" AND a.USERNAME = '"+CommonUtil.trim(mTsqxFormBean.getmTsqxInfoBean().getUsername())+"' ");
			}
			sql.append(" order by a.QM ASC,a.T asc ");
		}else{
			sql.append(" SELECT a.STCD AS STCD,a.USERNAME AS USERNAME,"
					+ "b.STNM AS STNM FROM ST_TSQX_B a LEFT JOIN ST_STBPRP_B b ON a.STCD = b.STCD WHERE 1 = 1   ");
			if(mTsqxFormBean.getSearchName()!=null && !"".equals(mTsqxFormBean.getSearchName().trim())){
				sql.append(" AND (b.STNM LIKE '%"+CommonUtil.trim(mTsqxFormBean.getSearchName())+"%' ) ");
			}
			sql.append(" GROUP BY a.STCD,a.USERNAME,b.STNM ORDER BY a.STCD");
		}
		System.out.println(sql.toString());
		int pageNo=mTsqxFormBean.getPageBean().getLimit()>0?mTsqxFormBean.getPageBean().getOffset()/mTsqxFormBean.getPageBean().getLimit():0;
		pageNo=pageNo+1;
		mTsqxFormBean.getPageBean().setPageNo(pageNo);
		return this.findPageByFetchedSql(sql.toString(), null
				,mTsqxFormBean.getPageBean().getPageNo()
				,mTsqxFormBean.getPageBean().getLimit()
				,null);
	}
	public PageResults getTsqxListDatas(TsqxFormBean mTsqxFormBean){
		StringBuilder sql=new StringBuilder();
		sql.append(" SELECT a.USERNAME,a.Q,a.QM,a.T,b.STNM AS STCD FROM ST_TSQX_B a LEFT JOIN ST_STBPRP_B b ON a.STCD = b.STCD WHERE 1 = 1  ");
		if(mTsqxFormBean.getSearchName()!= null &&!"".equals(mTsqxFormBean.getSearchName().trim())){
			sql.append(" AND b.STNM LIKE '%"+CommonUtil.trim(mTsqxFormBean.getSearchName())+"%' ");
		}
		System.out.println(sql.toString());
		int pageNo=mTsqxFormBean.getPageBean().getLimit()>0?mTsqxFormBean.getPageBean().getOffset()/mTsqxFormBean.getPageBean().getLimit():0;
		pageNo=pageNo+1;
		mTsqxFormBean.getPageBean().setPageNo(pageNo);
		return this.findPageByFetchedSql(sql.toString(), null
				,mTsqxFormBean.getPageBean().getPageNo()
				,mTsqxFormBean.getPageBean().getLimit()
				,null);
	}
	
	/**
	 * 根据主键ID获取退水曲线实体
	 * */
	public PageResults getTsqxInfoListByIds(String ids){
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT STCD,USERNAME,QM,Q,T FROM ST_TSQX_B WHERE 1=1 ");
		if(ids.length()>0){
			sql.append(" AND STCD IN ('"+ids+"')");
		}
		sql.append(" ORDER BY STCD");
		return this.findPageByFetchedSql(sql.toString(), null, 0, 100000000, null);
	}
	
	/**
	 * 根据主键ID删除退水曲线实体
	 * */
	public void deletTsqxInfoByIds(String ids,String userName){
		StringBuilder sql=new StringBuilder();
		sql.append("DELETE FROM ST_TSQX_B WHERE STCD IN ('"+ids+"')");
		if(userName!=null&&!"".equals(userName)){
			sql.append(" AND USERNAME = '"+userName+"' ");
		}
		this.exectueSQL(sql.toString());
	}
	
	/**
	 * 增加实体对象
	 * */
	public void saveTsqxInfo(Tsqx mTsqx){
		StringBuilder sql=new StringBuilder();
		sql.append("INSERT INTO ST_TSQX_B (STCD,USERNAME,QM,Q,T) VALUES ");
		sql.append("('"+CommonUtil.trim(mTsqx.getStcd())+"','"+CommonUtil.trim(mTsqx.getUsername())+"',");
		sql.append("'"+CommonUtil.trim(mTsqx.getQm())+"','"+CommonUtil.trim(mTsqx.getQ())+"',");
		sql.append("'"+CommonUtil.trim(mTsqx.getT())+"')");
		this.exectueSQL(sql.toString());
	}
	public List<Tsqx> queryTsqxByStcd(String stcd){
		StringBuffer hql = new StringBuffer("from Tsqx where stcd=?");
		return this.getListByHQL(hql.toString(), new Object[]{stcd});
	}
	/**
	 * 修改实体对象
	 * */
	public void updateTsqxInfo(TsqxFormBean mTsqxFormBean){
		StringBuilder sql=new StringBuilder();
		sql.append("UPDATE ST_TSQX_B SET STCD='"+mTsqxFormBean.getIds()+"',");
		sql.append("USERNAME='"+mTsqxFormBean.getmTsqxInfoBean().getUsername()+"',");
		sql.append("QM='"+mTsqxFormBean.getmTsqxInfoBean().getQm()+"',");
		sql.append("Q='"+mTsqxFormBean.getmTsqxInfoBean().getQ()+"',");
		sql.append("T='"+mTsqxFormBean.getmTsqxInfoBean().getT()+"'");
		sql.append("WHERE STCD='"+mTsqxFormBean.getIds()+"'");
		this.exectueSQL(sql.toString());
	}
	
	/**
	 * 根据主键ID获取实体
	 * */
	@SuppressWarnings("unchecked")
	public Tsqx getTsqxInfoById(String stcd){
		Tsqx mTsqx=new Tsqx();
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT A.STCD,B.STNM,A.USERNAME,A.QM,A.Q,A.T FROM ST_TSQX_B A ");
		sql.append("LEFT JOIN ST_STBPRP_B B on A.STCD=B.STCD WHERE A.STCD='"+stcd+"' ");
		List<Tsqx> mTsqxList=null;
		try{
			mTsqxList=this.getSession().createSQLQuery(sql.toString()).addEntity(Tsqx.class).list();
			for(int i=0;i<mTsqxList.size();i++){
				mTsqx=mTsqxList.get(0);
			}
		}catch (Exception e) {
			e.getStackTrace();
		}
		return mTsqx;
	}
	
	/**
	 * 根据条件查询退水曲线
	 * */
	private String spliceStrTsqx(TsqxFormBean mTsqxFormBean){
		StringBuilder sql=new StringBuilder();
		if(null!=mTsqxFormBean){
			if(CommonUtil.trim(mTsqxFormBean.getSearchName()).length()>0){
				sql.append("AND ((A.STCD LIKE '%"+CommonUtil.trim(mTsqxFormBean.getSearchName())+"%') ");
				sql.append("OR (A.USERNAME LIKE '%"+CommonUtil.trim(mTsqxFormBean.getSearchName())+"%') ");
				sql.append("OR (A.QM LIKE '%"+CommonUtil.trim(mTsqxFormBean.getSearchName())+"%') ");
				sql.append("OR (A.Q LIKE '%"+CommonUtil.trim(mTsqxFormBean.getSearchName())+"%') ");
				sql.append("OR (A.T LIKE '%"+CommonUtil.trim(mTsqxFormBean.getSearchName())+"%')) ");
			}
			if(null!=mTsqxFormBean.getmTsqxInfoBean()){
				if(CommonUtil.trim(mTsqxFormBean.getmTsqxInfoBean().getStcd()).length()>0){
					sql.append(" AND A.STCD = '"+CommonUtil.trim(mTsqxFormBean.getmTsqxInfoBean().getStcd())+"'");
				}
				if(CommonUtil.trim(mTsqxFormBean.getmTsqxInfoBean().getUsername()).length()>0){
					sql.append(" AND A.USERNAME = '"+CommonUtil.trim(mTsqxFormBean.getmTsqxInfoBean().getUsername())+"'");
				}
				/*if(CommonUtil.trim(mTsqxFormBean.getmTsqxInfoBean().getQm()).length()>0){
					sql.append(" AND A.QM = '"+CommonUtil.trim(mTsqxFormBean.getmTsqxInfoBean().getQm())+"'");
				}
				if(CommonUtil.trim(mTsqxFormBean.getmTsqxInfoBean().getQ()).length()>0){
					sql.append(" AND A.Q = '"+CommonUtil.trim(mTsqxFormBean.getmTsqxInfoBean().getQ())+"'");
				}
				if(CommonUtil.trim(mTsqxFormBean.getmTsqxInfoBean().getT()).length()>0){
					sql.append(" AND A.T = '"+CommonUtil.trim(mTsqxFormBean.getmTsqxInfoBean().getT())+"'");
				}*/
			}
		}
		return sql.toString();
	}
	/*
	 * 修改之前先删除
	 */
	public void delete(TsqxFormBean mTsqxFormBean) {
		StringBuilder sql = new StringBuilder();
		sql.append(" DELETE FROM ST_TSQX_B WHERE 1 = 1 ");
		sql.append(" AND STCD = '"+CommonUtil.trim(mTsqxFormBean.getmTsqxInfoBean().getStcd())+"'");
//		sql.append(" AND USERNAME = '"+CommonUtil.trim(mTsqxFormBean.getmTsqxInfoBean().getUsername())+"'");
		System.out.println(sql.toString());
		this.exectueSQL(sql.toString());
	}

	/*
	 * 修改删除后重新插入数据
	 */
	public void saveTsqxInfo(TsqxFormBean mTsqxFormBean, String qm, String q, String t) {
		StringBuilder sql=new StringBuilder();
		sql.append("INSERT INTO ST_TSQX_B (STCD,USERNAME,QM,Q,T) VALUES ");
		sql.append("('"+CommonUtil.trim(mTsqxFormBean.getmTsqxInfoBean().getStcd())+"','"+CommonUtil.trim(mTsqxFormBean.getmTsqxInfoBean().getUsername())+"',");
		sql.append("'"+Float.parseFloat(qm)+"','"+Float.parseFloat(q)+"',");
		sql.append("'"+Float.parseFloat(t)+"')");
		System.out.println(sql.toString());
		this.exectueSQL(sql.toString());
	}
}
