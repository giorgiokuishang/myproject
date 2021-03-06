package com.lyht.business.analysisCalculation.dao;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lyht.base.hibernate.basedao.HibernateBaseDao;
import com.lyht.base.hibernate.common.PageResults;
import com.lyht.business.analysisCalculation.bean.ModelProgram;
import com.lyht.business.analysisCalculation.formbean.ModelProgramFromBean;
import com.lyht.business.modelManage.bean.ModelInfo;
import com.lyht.business.modelManage.formbean.ModelInfoFormBean;
import com.lyht.business.system.bean.SysStaff;
import com.lyht.util.CommonUtil;
/**
 *作者： 刘魁
 *脚本日期:2018年5月11日 21:41:11
 *说明:  模型方案Dao
*/
@Repository
@Scope("prototype")
@SuppressWarnings("rawtypes")
public class ModelProgramDao extends HibernateBaseDao<ModelProgram>  {

	/**
	 * 查询同名方案
	 */
	public List<ModelProgram> queryModelProgramByCodeOrName(String planCode,String planName,String stcd){
		StringBuffer hql = new StringBuffer("from ModelProgram where progName=? and stcd=? ");
		if(CommonUtil.trim(planCode).length()>0){
			hql.append(" and progCode!='"+CommonUtil.trim(planCode)+"'");
		}
		return this.getListByHQL(hql.toString(), new Object[]{planName,stcd});
	}
	/**
	 * 查找模型方案
	 */
	public PageResults getModelProgram(ModelProgramFromBean mpBean,ModelInfoFormBean modelInfoFormBean ,SysStaff  mSysStaff) {
		StringBuilder sql=new StringBuilder();
		String str=spliceStrModelProg(mpBean);
		String str1=spliceStrModelInfo(modelInfoFormBean);
		sql.append("SELECT M.* ,B.STNM AS STNM,F.MODEL_NAME AS MODEL_NAME  FROM MODEL_PROGRAM M  LEFT JOIN ST_STBPRP_B B ON M.STCD=B.STCD ");
		sql.append("LEFT JOIN MODEL_INFO F ON M.MODEL_CODE=F.MODEL_CODE   ");
		if(mSysStaff!=null) {
			if(mSysStaff.getStaffCode()!=null||!mSysStaff.getStaffCode().equals("")) {
				sql.append("WHERE M.CREATE_STAFF='"+mSysStaff.getStaffCode()+"'");
			}
		}
		if(null!=str1 && !"".equals(str1)){
			sql.append(str1);
		}
		if(null!=str && !"".equals(str)){
			sql.append(str);
		}
		sql.append(" ORDER BY CREATE_TIME DESC");
		int pageNo=mpBean.getPageBean().getLimit()>0?mpBean.getPageBean().getOffset()/mpBean.getPageBean().getLimit():0;
		pageNo=pageNo+1;
		mpBean.getPageBean().setPageNo(pageNo);
		return this.findPageByFetchedSql(sql.toString(), null, mpBean.getPageBean().getPageNo(),mpBean.getPageBean().getLimit(), null);
	}
	
	/**
	 * 根据主键删除模型方案
	 */
	public void delModelPgm(String ids) {
		StringBuilder sql=new StringBuilder();
		sql.append("DELETE FROM MODEL_PROGRAM WHERE PROG_CODE IN ('"+ids+"')");
		this.exectueSQL(sql.toString());
	}
	
	/**
	 * 根据主键ID获取实体
	 * @return PageResults
	 * */
	public PageResults getModelpgm(String id) {
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT PROG_CODE,MODEL_CODE,PROG_NAME,CREATE_STAFF,CREATE_TIME,REMARK  FROM MODEL_PROGRAM  WHERE 1=1 ");
		if(id.length()>0){
			sql.append(" AND PROG_CODE ='"+id+"'");
		}
		sql.append(" ORDER BY PROG_CODE");
		return this.findPageByFetchedSql(sql.toString(), null, 0, 100000000, null);
	}
	
	/**
	 * 根据PROG_CODE获取实体
	 */
	@SuppressWarnings("unchecked")
	public PageResults  getModelProgramById(ModelProgramFromBean mBean) {
		ModelProgram modelProgram=new ModelProgram();
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT M.MODEL_NAME, B.STNM,S.STAFF_NAME ,P.*  ");
		sql.append(" FROM MODEL_PROGRAM  P  ");
		sql.append("LEFT JOIN ST_STBPRP_B B ON P.STCD=B.STCD  ");
		sql.append("LEFT JOIN SYS_STAFF S ON P.CREATE_STAFF=S.STAFF_CODE ");
		sql.append("LEFT JOIN MODEL_INFO M ON P.MODEL_CODE=M.MODEL_CODE  ");
		sql.append("WHERE PROG_CODE= '"+mBean.getModelprogramFormBean().getProgCode()+"'  ");
		int pageNo=mBean.getPageBean().getLimit()>0?mBean.getPageBean().getOffset()/mBean.getPageBean().getLimit():0;
		pageNo=pageNo+1;
		mBean.getPageBean().setPageNo(pageNo);
		return this.findPageByFetchedSql(sql.toString(), null, mBean.getPageBean().getPageNo(),mBean.getPageBean().getLimit(), null);
	}
	/**
	 * 新增方案
	 */
	public void saveModelpgm(ModelProgram modelProgram) {
		StringBuilder sql=new StringBuilder();
		sql.append("INSERT INTO MODEL_PROGRAM (PROG_CODE,MODEL_CODE,PROG_NAME,CREATE_STAFF,CREATE_TIME,REMARK,STCD) ");
		sql.append("VALUES ('"+modelProgram.getProgCode()+"', '"+modelProgram.getModelCode()+"'");
		sql.append(",'"+modelProgram.getProgName()+"', '"+modelProgram.getCreateStaff()+"'");
		sql.append(",'"+modelProgram.getCreateTime()+"','"+modelProgram.getRemark()+"' , '"+modelProgram.getStcd()+"' );");
		this.exectueSQL(sql.toString());
	}
	
	/**
	 * 修改方案
	 */
	public void update(ModelProgram modelProgram) {
		StringBuilder sql=new StringBuilder();
		sql.append("UPDATE MODEL_PROGRAM SET MODEL_CODE='"+modelProgram.getModelCode()+"', ");
		sql.append("PROG_NAME='"+modelProgram.getProgName()+"' ,CREATE_STAFF='"+modelProgram.getCreateStaff()+"',");
	    sql.append("CREATE_TIME='"+modelProgram.getCreateTime()+"',REMARK='"+modelProgram.getRemark()+"',");
	    sql.append("STCD='"+modelProgram.getStcd()+"'");
		sql.append("WHERE PROG_CODE='"+modelProgram.getProgCode()+"' ");
	    this.exectueSQL(sql.toString());				
	}
	/**
	 * 根据条件查询
	 */
	private String spliceStrModelProg(ModelProgramFromBean mpBean){
		StringBuilder sql=new StringBuilder();
		if(null!=mpBean){
			if(CommonUtil.trim(mpBean.getSearchName()).length()>0){
				sql.append("AND ((M.PROG_NAME LIKE '%"+CommonUtil.trim(mpBean.getSearchName())+"%') ");
				sql.append("OR (M.CREATE_TIME LIKE '%"+CommonUtil.trim(mpBean.getSearchName())+"%') ");
				sql.append("OR (M.REMARK LIKE '%"+CommonUtil.trim(mpBean.getSearchName())+"%')) ");
			}
		}	if(null!=mpBean.getModelprogramFormBean()){
			if(CommonUtil.trim(mpBean.getModelprogramFormBean().getProgName()).length()>0){
				sql.append(" AND M.PROG_NAME LIKE '%"+CommonUtil.trim(mpBean.getModelprogramFormBean().getProgName())+"%'");
				}
			if(CommonUtil.trim(mpBean.getModelprogramFormBean().getCreateTime()).length()>0){
				sql.append(" AND M.CREATE_TIME LIKE '%"+CommonUtil.trim(mpBean.getModelprogramFormBean().getCreateTime())+"%'");
				}
			if(CommonUtil.trim(mpBean.getModelprogramFormBean().getRemark()).length()>0){
				sql.append(" AND M.REMARK LIKE '%"+CommonUtil.trim(mpBean.getModelprogramFormBean().getRemark())+"%'");
				}
		    }
		return sql.toString();
	}
	
	private String spliceStrModelInfo(ModelInfoFormBean mInfoBean){
		StringBuilder sql=new StringBuilder();
		if(null!=mInfoBean){
			if(CommonUtil.trim(mInfoBean.getSearchName()).length()>0){
				sql.append("AND ((B.MODEL_CODE LIKE '%"+CommonUtil.trim(mInfoBean.getSearchName())+"%') ");
		
			}	if(null!=mInfoBean.getModelInfoFormBean()){
					if(CommonUtil.trim(mInfoBean.getModelInfoFormBean().getModelCode()).length()>0){
					sql.append(" AND B.MODEL_CODE LIKE '%"+CommonUtil.trim(mInfoBean.getModelInfoFormBean().getModelCode())+"%'");
					}
			}
		}
		return sql.toString();
	}
}
