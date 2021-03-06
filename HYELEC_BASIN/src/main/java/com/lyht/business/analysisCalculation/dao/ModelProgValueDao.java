package com.lyht.business.analysisCalculation.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lyht.base.hibernate.basedao.HibernateBaseDao;
import com.lyht.base.hibernate.common.PageResults;
import com.lyht.business.analysisCalculation.bean.ModelProgValue;
import com.lyht.business.analysisCalculation.bean.ModelProgram;
import com.lyht.business.analysisCalculation.formbean.ModelProgValFromBean;
import com.lyht.business.analysisCalculation.formbean.ModelProgramFromBean;

/**
 *作者： 刘魁,
 *脚本日期:2018年5月11日 21:41:11
 *说明:  方案参数值Dao
*/
@Repository
@Scope("prototype")
@SuppressWarnings("rawtypes")
public class ModelProgValueDao extends HibernateBaseDao<ModelProgValue>{
	/**
	 * 查找方案参数值
	 */
	public PageResults getProValueList(ModelProgValFromBean mpValue) {
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT CODE,PROG_CODE,MODEL_CODE,PARA_CODE,PARA_VALUE");
		sql.append("FROM MODEL_PROG_VALUE WHERE 1=1");
		sql.append(" ORDER BY CODE");
		int pageNo=mpValue.getPageBean().getLimit()>0?mpValue.getPageBean().getOffset()/mpValue.getPageBean().getLimit():0;
		pageNo=pageNo+1;
		mpValue.getPageBean().setPageNo(pageNo);
		return this.findPageByFetchedSql(sql.toString(), null, mpValue.getPageBean().getPageNo(),mpValue.getPageBean().getLimit(), null);
	}
	
	/**
	 * 根据方案编码查找参数值 
	 *@param modelProgramFromBean
	 */
	public PageResults getProgValueByProg(ModelProgramFromBean modelProgramFromBean) {
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT CODE,PROG_CODE,MODEL_CODE,PARA_CODE,PARA_VALUE WHERE ");
		sql.append("PROG_CODE ='"+modelProgramFromBean.getModelprogramFormBean().getProgCode()+"' ");
		int pageNo=modelProgramFromBean.getPageBean().getLimit()>0?modelProgramFromBean.getPageBean().getOffset()/modelProgramFromBean.getPageBean().getLimit():0;
		pageNo=pageNo+1;
		modelProgramFromBean.getPageBean().setPageNo(pageNo);
		return  this.findPageByFetchedSql(sql.toString(), null, modelProgramFromBean.getPageBean().getPageNo(),modelProgramFromBean.getPageBean().getLimit(), null);
	}
	/**
	 * 根据主键删除方案参数值
	 */
	public void delProValue(String ids) {
		StringBuilder sql=new StringBuilder();
		sql.append("DELETE FROM MODEL_PROG_VALUE WHERE CODE IN ('"+ids+"')");
		this.exectueSQL(sql.toString());
	}
	/**
	 * 新增方案参数值
	 */
	public void saveModelpgm(ModelProgValue modelProgValue ) {
		StringBuilder sql=new StringBuilder();
		sql.append("INSERT INTO MODEL_PROG_VALUE (CODE,PROG_CODE,MODEL_CODE,PARA_CODE,PARA_VALUE) ");
		sql.append("VALUES('"+modelProgValue.getCode()+"','"+modelProgValue.getProgCode()+"'");
		sql.append(",'"+modelProgValue.getModelCode()+"','"+modelProgValue.getParaCode()+"'");
		sql.append(",'"+modelProgValue.getParaValue()+"')");
		this.exectueSQL(sql.toString());
	}
	/**
	 * 修改方案参数值
	 */
	public void updateValue(ModelProgValue modelProgValue) {
		StringBuilder sql=new StringBuilder();
		sql.append("UPDATE MODEL_PROG_VALUE SET PARA_VALUE='"+modelProgValue.getParaValue()+"' WHERE CODE='"+modelProgValue.getCode()+"'");
		this.exectueSQL(sql.toString());
	}
	
	/**
	 * 根据prog_code删除参数值
	 */
	public void deleteProValueByProg(String ids) {
		StringBuilder sql=new StringBuilder();
		sql.append("DELETE FROM MODEL_PROG_VALUE WHERE PROG_CODE = '"+ids+"'");
		this.exectueSQL(sql.toString());
	}
	
	/**
	 * 根据prog_code 获取参数值
	 */
	public PageResults getParaListByProg(ModelProgramFromBean mben) {
		StringBuilder sql=new StringBuilder();
		String ids=mben.getModelprogramFormBean().getProgCode();
		sql.append("SELECT E.*,P.PARA_NAME,P.PARA_SYMBOL  FROM MODEL_PROG_VALUE E  ");
		sql.append("LEFT JOIN MODEL_PARAMENTER P ON E.PARA_CODE=P.PARA_CODE  ");
		sql.append("WHERE E.PROG_CODE='"+ids+"' ");
		int pageNo=mben.getPageBean().getLimit()>0?mben.getPageBean().getOffset()/mben.getPageBean().getLimit():0;
		pageNo=pageNo+1;
		mben.getPageBean().setPageNo(pageNo);
		return this.findPageByFetchedSql(sql.toString(), null, mben.getPageBean().getPageNo(),mben.getPageBean().getLimit(), null);
	}
}
