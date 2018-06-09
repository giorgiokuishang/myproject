package com.lyht.business.consumer.hydrologicalData.dao;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lyht.base.hibernate.basedao.HibernateBaseDao;
import com.lyht.base.hibernate.common.PageResults;
import com.lyht.business.consumer.hydrologicalData.bean.River;
import com.lyht.business.consumer.hydrologicalData.formbean.RiverFormBean;
import com.lyht.util.CommonUtil;
import com.lyht.util.DateUtil;
import com.lyht.util.Randomizer;

@Repository
@Scope("prototype")
@SuppressWarnings("rawtypes")
public class RiverDao extends HibernateBaseDao<River>{

	/**
	 * 获取列表数据
	 * */
	public PageResults getRiverListData(RiverFormBean mRiverFormBean){
		StringBuilder sql=new StringBuilder();
		String str=spliceStrRiver(mRiverFormBean);
		sql.append("SELECT river.STCD,river.TM,river.Z,river.Q,river.XSA,river.XSAVV,river.XSMXV, ");
		sql.append(" river.FLWCHRCD,river.WPTN,river.MSQMT,river.MSAMT,river.MSVMT,b.STNM AS STNM FROM ST_RIVER_R AS river ");
		sql.append(" LEFT JOIN ST_STBPRP_B AS b ON b.STCD = river.STCD WHERE 1=1 ");
		if(null!=str && !"".equals(str)){
			sql.append(str);
		}
		sql.append(" ORDER BY river.STCD");
		return this.findPageByFetchedSql(sql.toString(), null
				,mRiverFormBean.getPageBean().getOffset()
				,mRiverFormBean.getPageBean().getLimit()
				,null);
	}
	/**
	 * 获取列表数据用于导出
	 * */
	public PageResults getRiverListData_export(RiverFormBean mRiverFormBean){
		StringBuilder sql=new StringBuilder();
		String str=spliceStrRiver(mRiverFormBean);
		sql.append("SELECT STCD,TM,Z,Q,XSA,XSAVV,XSMXV,FLWCHRCD,WPTN,MSQMT,MSAMT,MSVMT FROM ST_RIVER_R WHERE 1=1 ");
		if(null!=str && !"".equals(str)){
			sql.append(str);
		}
		sql.append(" ORDER BY STCD");
		return this.findPageByFetchedSql(sql.toString(), null
				,mRiverFormBean.getPageBean().getOffset()
				,mRiverFormBean.getPageBean().getLimit()
				,null);
	}
	
	/**
	 * 根据主键ID获取日蒸发量实体
	 * */
	public PageResults getRiverInfoListByIds(String ids){
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT STCD,TM,Z,Q,XSA,XSAVV,XSMXV,FLWCHRCD,WPTN,MSQMT,MSAMT,MSVMT FROM ST_RIVER_R WHERE 1=1 ");
		if(ids.length()>0){
			sql.append(" AND STCD IN ('"+ids+"')");
		}
		sql.append(" ORDER BY STCD");
		return this.findPageByFetchedSql(sql.toString(), null, 0, 100000000, null);
	}
	
	/**
	 * 根据主键ID删除 实体
	 * */
	public void deletRiverInfoByIds(String ids,String ids_){
		StringBuilder sql=new StringBuilder();
		sql.append("DELETE FROM ST_RIVER_R WHERE STCD = '"+ids+"' AND TM='"+ids_+"'");
		this.exectueSQL(sql.toString());
	}
	
	/**
	 * 增加实体对象
	 * */
	public void saveRiverInfo(River mRiver){
		String sql=execQL(mRiver); //执行sql语句操作
		this.exectueSQL(sql);
	}
	
	/**
	 * 修改实体对象
	 * */
	public void updateRiverInfo(River mRiver){
		String sql = updateById(mRiver);//执行sql语句操作
		this.exectueSQL(sql);
	}
	
	/**
	 * 根据主键ID获取实体
	 * */
	@SuppressWarnings("unchecked")
	public River getRiverInfoById(RiverFormBean mRiverFormBean){
		River mRiver=new River();
		String sql=execById(mRiverFormBean);//执行sql语句操作
		List<River> mRiverList=null;
		try{
			mRiverList=this.getSession().createSQLQuery(sql.toString()).addEntity(River.class).list();
			for(int i=0;i<mRiverList.size();i++){
				mRiver=mRiverList.get(0);
			}
		}catch (Exception e) {
			e.getStackTrace();
		}
		return mRiver;
	}
	
	
	/**
	 * 根据条件查询 
	 * */
	private String spliceStrRiver(RiverFormBean mRiverFormBean){
		StringBuilder sql=new StringBuilder();
		if(null!=mRiverFormBean){
			if(CommonUtil.trim(mRiverFormBean.getSearchName()).length()>0){
				sql.append("AND ((river.STCD LIKE '%"+CommonUtil.trim(mRiverFormBean.getSearchName())+"%') ");
				sql.append("OR (river.TM LIKE '%"+CommonUtil.trim(mRiverFormBean.getSearchName())+"%') ");
				sql.append("OR (river.Z LIKE '%"+CommonUtil.trim(mRiverFormBean.getSearchName())+"%') ");
				sql.append("OR (river.Q  LIKE '%"+CommonUtil.trim(mRiverFormBean.getSearchName())+"%') ");
				sql.append("OR (river.FLWCHRCD  LIKE '%"+CommonUtil.trim(mRiverFormBean.getSearchName())+"%') ");
				sql.append("OR (river.WPTN  LIKE '%"+CommonUtil.trim(mRiverFormBean.getSearchName())+"%') ");
				sql.append("OR (STNM  LIKE '%"+CommonUtil.trim(mRiverFormBean.getSearchName())+"%')) ");
			}
			if(null!=mRiverFormBean.getmRiverInfoBean()){
				if(CommonUtil.trim(mRiverFormBean.getmRiverInfoBean().getStcd()).length()>0){
					sql.append(" AND river.STCD = '"+CommonUtil.trim(mRiverFormBean.getmRiverInfoBean().getStcd())+"'");
				}
				if(CommonUtil.trim(mRiverFormBean.getmRiverInfoBean().getTm()).length()>0){
					sql.append(" AND river.TM = '"+CommonUtil.trim(mRiverFormBean.getmRiverInfoBean().getTm())+"'");
				}
				if(CommonUtil.trim(mRiverFormBean.getmRiverInfoBean().getFlwchrcd()).length()>0){
					sql.append(" AND river.FLWCHRCD = '"+CommonUtil.trim(mRiverFormBean.getmRiverInfoBean().getFlwchrcd())+"'");
				}
				if(CommonUtil.trim(mRiverFormBean.getmRiverInfoBean().getWptn()).length()>0){
					sql.append(" AND river.WPTN = '"+CommonUtil.trim(mRiverFormBean.getmRiverInfoBean().getWptn())+"'");
				}
			}
		}
		return sql.toString();
	}
	
	/**
	 * 执行新增语句
	 * */
	private String execQL(River mRiver){
		StringBuilder sql=new StringBuilder();
		sql.append("INSERT INTO ST_RIVER_R (STCD,TM,Z,Q,XSA,XSAVV,XSMXV,FLWCHRCD,WPTN,MSQMT,MSAMT,MSVMT) ");
		sql.append("VALUES ('"+CommonUtil.trim(mRiver.getStcd())+"',");
		sql.append("'"+DateUtil.ConvertTimestamp(mRiver.getTm())+"','"+CommonUtil.trim(mRiver.getZ())+"',");
		sql.append("'"+CommonUtil.trim(mRiver.getQ())+"','"+CommonUtil.trim(mRiver.getXsa())+"',");
		sql.append("'"+CommonUtil.trim(mRiver.getXsavv())+"','"+CommonUtil.trim(mRiver.getXsmxv())+"',");
		sql.append("'"+CommonUtil.trim(mRiver.getFlwchrcd())+"','"+CommonUtil.trim(mRiver.getWptn())+"',");
		sql.append("'"+CommonUtil.trim(mRiver.getMsqmt())+"','"+CommonUtil.trim(mRiver.getMsamt())+"',");
		sql.append("'"+CommonUtil.trim(mRiver.getMsvmt())+"')");
		return sql.toString();
	}
	
	/**
	 * 根据ID执行查询语句
	 * */
	private String execById(RiverFormBean mRiverFormBean){
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT STCD,TM,Z,Q,XSA,XSAVV,XSMXV,FLWCHRCD,WPTN,MSQMT,");
		sql.append("MSAMT,MSVMT FROM ST_RIVER_R WHERE STCD ='"+mRiverFormBean.getmRiverInfoBean().getStcd()+"' ");
		sql.append(" AND TM='"+mRiverFormBean.getmRiverInfoBean().getTm()+"'");
		return sql.toString();
	}
	
	/**
	 * 根据ID执行修改语句
	 * */
	private String updateById(River mRiver){
		StringBuilder sql=new StringBuilder();
		sql.append("UPDATE ST_RIVER_R SET STCD = '"+CommonUtil.trim(mRiver.getStcd())+"', TM = '"+CommonUtil.trim(mRiver.getTm())+"', ");
		sql.append(" Z = '"+CommonUtil.trim(mRiver.getZ())+"', Q = '"+CommonUtil.trim(mRiver.getQ())+"', XSA = '"+CommonUtil.trim(mRiver.getXsa())+"',");
		sql.append(" XSAVV = '"+CommonUtil.trim(mRiver.getXsavv())+"', XSMXV = '"+CommonUtil.trim(mRiver.getXsmxv())+"', FLWCHRCD = '"+CommonUtil.trim(mRiver.getFlwchrcd())+"', WPTN = '"+CommonUtil.trim(mRiver.getWptn())+"',");
		sql.append(" MSQMT = '"+CommonUtil.trim(mRiver.getMsqmt())+"', MSAMT = '"+CommonUtil.trim(mRiver.getMsamt())+"', MSVMT = '"+CommonUtil.trim(mRiver.getMsvmt())+"'  WHERE STCD = '"+CommonUtil.trim(mRiver.getStcd())+"'");
		sql.append(" AND TM='"+CommonUtil.trim(mRiver.getTm())+"'");
		return sql.toString();
	}
}
