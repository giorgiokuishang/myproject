package com.lyht.business.consumer.hydrologicalData.dao;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lyht.base.hibernate.basedao.HibernateBaseDao;
import com.lyht.base.hibernate.common.PageResults;
import com.lyht.business.consumer.hydrologicalData.bean.Pptn;
import com.lyht.business.consumer.hydrologicalData.formbean.PptnFormBean;
import com.lyht.business.search.formBean.SearchFormBean;
import com.lyht.util.CommonUtil;
import com.lyht.util.DateUtil;

@Repository
@Scope("prototype")
@SuppressWarnings("rawtypes")
public class PptnDao extends HibernateBaseDao<Pptn>{
	
	public List<Map> getPptnListByRain(SearchFormBean searchFormBean){
		// DATEADD函数（时间类型，小时数，时间）
		StringBuffer sql = new StringBuffer( "select a.STCD, CONVERT(varchar(100), a.TM, 120) as TM,a.DRP,a.DYP ,b.STNM,b.LGTD1,b.LTTD1  "
				+ "from ST_PPTN_R a left join ST_STBPRP_B b on a.STCD = b.STCD where 1=1 " ) ;
		//显示实时条件
		if(searchFormBean.getTime() > 0 ){
			sql.append(" and a.TM >= DATEADD(hh,"+searchFormBean.getTime()*-1+",GETDATE()) ");
		}
		
		//显示实时条件
		if(CommonUtil.getLength(CommonUtil.trim(searchFormBean.getStcd())) > 0 ){
			sql.append(" and a.STCD  = '"+searchFormBean.getStcd()+"' ");
		}
		//查询开始时间
		if(CommonUtil.getLength(CommonUtil.trim(searchFormBean.getStartTime())) > 0){
			sql.append( " and TM >= '"+searchFormBean.getStartTime()+"'  ");
		}
		//查询结束时间
		if(CommonUtil.getLength(CommonUtil.trim(searchFormBean.getEndTime())) > 0){
			sql.append( " and TM <= '"+searchFormBean.getEndTime()+"'  ");
		}
		
		sql.append("order by STCD,TM desc");
		System.out.println(sql.toString());
		return this.createSQLQuerybyMap(sql.toString());
	}
	
	
	/**
	 * 获取降水量列表数据
	 * */
	public PageResults getPptnListData(PptnFormBean mPptnFormBean){
		StringBuffer  sql=new StringBuffer ();
		String str=spliceStrPptn(mPptnFormBean);
		sql.append("SELECT ppth.STCD,ppth.TM,ppth.DRP,ppth.INTV,ppth.PDR,ppth.DYP,ppth.WTH,b.STNM FROM ST_PPTN_R AS ppth ");
		sql.append(" LEFT JOIN ST_STBPRP_B AS b ON b.STCD = ppth.STCD WHERE 1=1 ");
		if(null!=str && !"".equals(str)){
			sql.append(str);
		}
		sql.append(" ORDER BY ppth.STCD");
		return this.findPageByFetchedSql(sql.toString(), null
				,mPptnFormBean.getPageBean().getOffset()
				,mPptnFormBean.getPageBean().getLimit()
				,null);
	}
	
	/**
	 * 根据主键ID删除降水量实体
	 * */
	public void deletPptnInfoByIds(String ids,String ids_){
		StringBuffer  sql=new StringBuffer ();
		sql.append("DELETE FROM ST_PPTN_R WHERE STCD = '"+ids+"' AND TM='"+ids_+"'");
		this.exectueSQL(sql.toString());
	}
	
	/**
	 * 增加实体对象
	 * */
	public void savePptnInfo(Pptn mPptn){
		String tm=mPptn.getTm();
		StringBuffer  sql=new StringBuffer ();
		sql.append("INSERT INTO ST_PPTN_R (STCD,TM,DRP,INTV,PDR,DYP,WTH) VALUES ");
		sql.append("('"+CommonUtil.trim(mPptn.getStcd())+"','"+CommonUtil.trim(DateUtil.ConvertTimestamp(tm))+"',");
		sql.append("'"+CommonUtil.trim(mPptn.getDrp())+"','"+CommonUtil.trim(mPptn.getIntv())+"',");
		sql.append("'"+CommonUtil.trim(mPptn.getPdr())+"','"+CommonUtil.trim(mPptn.getDyp())+"',");
		sql.append("'"+CommonUtil.trim(mPptn.getWth())+"')");
		this.exectueSQL(sql.toString());
	}
	
	/**
	 * 修改实体对象
	 * */
	public void updatePptnInfo(Pptn mPptn){
		StringBuffer  sql=new StringBuffer ();
		sql.append("UPDATE ST_PPTN_R SET STCD='"+mPptn.getStcd()+"',TM='"+mPptn.getTm()+"',");
		sql.append("DRP='"+mPptn.getDrp()+"',INTV='"+mPptn.getIntv()+"',");
		sql.append("PDR='"+mPptn.getPdr()+"',DYP='"+mPptn.getDyp()+"',WTH='"+mPptn.getWth()+"' ");
		sql.append("WHERE STCD='"+mPptn.getStcd()+"' AND TM='"+mPptn.getTm()+"'");
		this.exectueSQL(sql.toString());
	}
	
	/**
	 * 根据主键ID获取实体
	 * */
	@SuppressWarnings("unchecked")
	public Pptn getPptnInfoById(PptnFormBean mPptnFormBean){
		Pptn mPptn=new Pptn();
		StringBuffer  sql=new StringBuffer ();
		sql.append("SELECT ltrim(rtrim(STCD)) AS STCD,TM,DRP,INTV,PDR,");
		sql.append("DYP,WTH FROM ST_PPTN_R WHERE STCD='"+mPptnFormBean.getmPptnInfoBean().getStcd()+"' ");
		sql.append("AND TM='"+mPptnFormBean.getmPptnInfoBean().getTm()+"'");
		List<Pptn> mPptnList=null;
		try{
			mPptnList=this.getSession().createSQLQuery(sql.toString()).addEntity(Pptn.class).list();
			for(int i=0;i<mPptnList.size();i++){
				mPptn=mPptnList.get(0);
			}
		}catch (Exception e) {
			e.getStackTrace();
		}
		return mPptn;
	}
	
	/**
	 * 根据条件查询降水量
	 * */
	private String spliceStrPptn(PptnFormBean mPptnFormBean){
		StringBuffer  sql=new StringBuffer ();
		if(null!=mPptnFormBean){
			if(CommonUtil.trim(mPptnFormBean.getSearchName()).length()>0){
				sql.append("AND ((ppth.STCD LIKE '%"+CommonUtil.trim(mPptnFormBean.getSearchName())+"%') ");
				sql.append("OR (ppth.TM LIKE '%"+CommonUtil.trim(mPptnFormBean.getSearchName())+"%') ");
				sql.append("OR (ppth.DRP LIKE '%"+CommonUtil.trim(mPptnFormBean.getSearchName())+"%') ");
				sql.append("OR (ppth.INTV LIKE '%"+CommonUtil.trim(mPptnFormBean.getSearchName())+"%') ");
				sql.append("OR (ppth.PDR LIKE '%"+CommonUtil.trim(mPptnFormBean.getSearchName())+"%') ");
				sql.append("OR (ppth.DYP LIKE '%"+CommonUtil.trim(mPptnFormBean.getSearchName())+"%') ");
				sql.append("OR (STNM LIKE '%"+CommonUtil.trim(mPptnFormBean.getSearchName())+"%')) ");
			}
			if(null!=mPptnFormBean.getmPptnInfoBean()){
				if(CommonUtil.trim(mPptnFormBean.getmPptnInfoBean().getStcd()).length()>0){
					sql.append(" AND ppth.STCD LIKE '%"+CommonUtil.trim(mPptnFormBean.getmPptnInfoBean().getStcd())+"%'");
				}
				if(CommonUtil.trim(mPptnFormBean.getmPptnInfoBean().getTm()).length()>0){
					sql.append(" AND ppth.TM LIKE '%"+CommonUtil.trim(mPptnFormBean.getmPptnInfoBean().getTm())+"%'");
				}
			}
		}
		return sql.toString();
	}
	
}
