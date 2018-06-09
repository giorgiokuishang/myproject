package com.lyht.business.analysisCalculation.dao;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lyht.base.hibernate.basedao.HibernateBaseDao;
import com.lyht.business.analysisCalculation.bean.Result;
/**
 *作者： 刘魁
 *脚本日期:2018年6月4日 15:24:11
 *说明:  产流计算结果Dao
*/
@Repository
@Scope("prototype")
public class ResultDao extends HibernateBaseDao<Result> {
		/**
		 * 保存计算结果
		 */
	public void saveResult(Result result) {
		StringBuilder sql=new StringBuilder();
		Date  date=new Date();
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		 String dateString = formatter.format(date);
		sql.append("INSERT INTO C_RESULT (YML,CREATE_TIME,YML_TIME,CREATE_STAFF,PCH,STCD) ");
		sql.append("VALUES('"+result.getYml()+"','"+dateString+"','"+result.getYmlTime()+"',");
		sql.append("'"+result.getCreateStaff()+"','"+result.getPch()+"','"+result.getStcd()+"')");
		this.exectueSQL(sql.toString());
	}
	
	/**
	 * 根据id删除
	 */
	public void delResult(String ids) {
		StringBuilder sql=new StringBuilder();
		sql.append("DELETE FROM C_RESULT WHERE PCH IN ('"+ids+"')");
		this.exectueSQL(sql.toString());
	}
}
