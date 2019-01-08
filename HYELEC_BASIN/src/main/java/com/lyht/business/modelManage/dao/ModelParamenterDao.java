package com.lyht.business.modelManage.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lyht.base.hibernate.basedao.HibernateBaseDao;
import com.lyht.base.hibernate.common.PageResults;
import com.lyht.business.modelManage.bean.ModelParamenter;
import com.lyht.business.modelManage.formbean.ModelInfoFormBean;
import com.lyht.business.modelManage.formbean.ModelParaFormBean;
import com.lyht.util.CommonUtil;

/**
 *作者： 刘魁
 *脚本日期:2018年5月7日 21:41:11
 *说明:  模型参数值Dao
*/
@Repository
@Scope("prototype")
@SuppressWarnings("rawtypes")
public class ModelParamenterDao extends HibernateBaseDao<ModelParamenter> {

		/**
		 * 根据模型获取模型值
		 */
		public PageResults getParByInfo(ModelInfoFormBean mInfoBean) {
			StringBuilder sql=new StringBuilder();
			sql.append("SELECT PARA_CODE,MODEL_CODE,PARA_NAME,IS_REQUIRED,PARA_SYMBOL,PARA_TYPE,PARA_MAX,PARA_MIN  FROM MODEL_PARAMENTER WHERE ");
			sql.append("MODEL_CODE='"+mInfoBean.getModelInfoFormBean().getModelCode()+"'");
			int pageNo=mInfoBean.getPageBean().getLimit()>0?mInfoBean.getPageBean().getOffset()/mInfoBean.getPageBean().getLimit():0;
			pageNo=pageNo+1;
			mInfoBean.getPageBean().setPageNo(pageNo);
		return this.findPageByFetchedSql(sql.toString(), null, mInfoBean.getPageBean().getPageNo(),mInfoBean.getPageBean().getLimit(), null);
		}
		/**
		 * 根据Modecode获取modelpara集合
		 * @param modelCode
		 * @param mBean
		 * @return
		 */
		public PageResults getParByModelCode(String  modelCode,ModelParaFormBean mBean) {
			StringBuilder sql=new StringBuilder();
			sql.append("SELECT PARA_CODE,MODEL_CODE,PARA_NAME,IS_REQUIRED,PARA_SYMBOL,PARA_TYPE,PARA_MAX,PARA_MIN  FROM MODEL_PARAMENTER WHERE ");
			sql.append("MODEL_CODE= "+modelCode);
			int pageNo=mBean.getPageBean().getLimit()>0?mBean.getPageBean().getOffset()/mBean.getPageBean().getLimit():0;
			pageNo=pageNo+1;
			mBean.getPageBean().setPageNo(pageNo);
			return this.findPageByFetchedSql(sql.toString(), null, mBean.getPageBean().getPageNo(),mBean.getPageBean().getLimit(), null);
		}
		/**
		 * 根据主键删除实体
		 * @param 主键
		 * */
		public void delModelParaById(String ids) {
			StringBuilder sql=new StringBuilder();
			sql.append("DELETE FROM MODEL_PARAMENTER WHERE PARA_CODE IN ('"+ids+"')");
			this.exectueSQL(sql.toString());
		}
		
		/**
		 * 根据modecode删除实体
		 * @param model_code
		 */
		public void delModelParaBymCode(String code) {
			StringBuilder sql=new StringBuilder();
			sql.append("DELETE FROM MODEL_PARAMENTER WHERE MODEL_CODE IN ('"+code+"')");
			this.exectueSQL(sql.toString());
		}
		
		/**
		 * 增加实体对象
		 * */
		public void saveModelParamenter( ModelParamenter modelParamenter){
			StringBuilder sql=new StringBuilder();
			sql.append("INSERT INTO MODEL_PARAMENTER (PARA_CODE,MODEL_CODE,PARA_NAME,IS_REQUIRED,PARA_SYMBOL,PARA_TYPE,PARA_MAX,PARA_MIN ) ");
			sql.append( "VALUES ('"+CommonUtil.trim(modelParamenter.getParaCode())+"','"+CommonUtil.trim(modelParamenter.getModelCode())+"',");
			sql.append( "'"+CommonUtil.trim(modelParamenter.getParaName())+"','"+CommonUtil.trim(modelParamenter.getIsRequired())+"','"+CommonUtil.trim(modelParamenter.getParaSymbol())+"',");
		   sql.append("'"+CommonUtil.trim(modelParamenter.getParaType())+"',");
			sql.append("'"+CommonUtil.trim(modelParamenter.getParaMax())+"','"+CommonUtil.trim(modelParamenter.getParaMin())+"')");
			this.exectueSQL(sql.toString());
		}
		
		/**
		 * 修改实体对象
		 * */
		public void updateModelParamenter(ModelParamenter modelParamenter) {
			StringBuilder sql=new StringBuilder();
			sql.append("UPDATE MODEL_PARAMENTER SET PARA_NAME='"+modelParamenter.getParaName()+"',IS_REQUIRED='"+modelParamenter.getIsRequired()+"'	,PARA_SYMBOL='"+modelParamenter.getParaSymbol()+"' , ");
			sql.append("PARA_TYPE='"+modelParamenter.getParaType()+"',PARA_MAX='"+modelParamenter.getParaMax()+"',PARA_MIN='"+modelParamenter.getParaMin()+"'");
			sql.append("WHERE PARA_CODE='"+modelParamenter.getParaCode()+"'");
			this.exectueSQL(sql.toString());			
		}
		
}
