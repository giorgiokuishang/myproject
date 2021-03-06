package com.lyht.business.modelManage.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lyht.base.hibernate.common.PageResults;
import com.lyht.business.modelManage.bean.ModelInfo;
import com.lyht.business.modelManage.bean.ModelParamenter;
import com.lyht.business.modelManage.dao.ModelInfoDao;
import com.lyht.business.modelManage.dao.ModelParamenterDao;
import com.lyht.business.modelManage.formbean.ModelInfoFormBean;
import com.lyht.util.DateUtil;
import com.lyht.util.ExcelUtils;
import com.lyht.util.ExcelVersionUtil;
import com.lyht.util.ImportExeclUtil;
import com.lyht.util.Randomizer;
/**
 *作者： 刘魁
 *脚本日期:2018年5月8日 21:41:11
 *说明:  模型信息Service
*/
@Service
@Scope("prototype")
@Transactional
@SuppressWarnings("rawtypes")
public class ModelInfoService {
	@Resource
	private ModelInfoDao mInfoDao;
	@Resource
	private ModelParamenterDao mPDao;
	
	/**
	 * 获取模型列表数据
	 * */
	@Transactional(propagation=Propagation.REQUIRED)
	public  PageResults getModelInfoListData(ModelInfoFormBean mInfoBean) {
		return mInfoDao.getModelInfoListData(mInfoBean);
	}
	
	/**
	 * 根据主键获取模型信息实体
	 * @return PageResults
	 * */
	@Transactional(propagation=Propagation.REQUIRED)
	public PageResults getModelInfoByCode(String ids) {
		return mInfoDao.getModelInfoByCode(ids);
	}
	
	/**
	 * 根据主键获取模型信息实体
	 * @return ModelInfo
	 * */
	@Transactional(propagation=Propagation.REQUIRED)
	public ModelInfo getModeInfo(String code) {
		ModelInfo modelInfo=new ModelInfo();
		if(code!=null || code.equals("")) {
			modelInfo=mInfoDao.getModelInfoById(code);
		}
		return modelInfo;
	}
	/**
	 * 增加实体对象
	 * */
	@Transactional(propagation=Propagation.REQUIRED)
	public ModelInfo create(ModelInfo modelInfo,ModelInfoFormBean modelInfoFormBean) {
		String modelCode=Randomizer.nextNumber("mx", 6);
		modelInfo.setModelCode(modelCode); 
		mInfoDao.saveModelInfo(modelInfo);  //保存模型
		List<ModelParamenter>  list=modelInfoFormBean.getModelParaList();
		for(ModelParamenter modelParamenter:list) {
			if(modelParamenter!=null) {
				modelParamenter.setModelCode(modelInfoFormBean.getModelInfoFormBean().getModelCode()); //set modelCode
				modelParamenter.setParaCode(Randomizer.nextNumber("mx", 4));//设置主键
				mPDao.saveModelParamenter(modelParamenter);
			}
		}
		return modelInfo;
	}
	
	/**
	 * 修改实体对象,修改模型和参数值
	 * */
	@Transactional(propagation=Propagation.REQUIRED)
	public ModelInfo update(ModelInfo modelInfo,ModelParamenter mp,ModelInfoFormBean modelInfoFormBean){
		mInfoDao.updateModelInfo(modelInfo);//修改模型
		List<ModelParamenter>  list=modelInfoFormBean.getModelParaList();
		for(ModelParamenter modelParamenter:list) {
			if(modelParamenter.getParaCode()!=null&& modelParamenter.getParaCode().trim().length()>0) { //如果不为空就修改
				modelParamenter.setModelCode(modelInfoFormBean.getModelInfoFormBean().getModelCode()); //set modelCode
				mPDao.updateModelParamenter(modelParamenter);//修改
			}else { //否则新增
				modelParamenter.setModelCode(modelInfoFormBean.getModelInfoFormBean().getModelCode()); //set modelCode
				String paraCode=Randomizer.nextNumber("mxCS", 4);
				modelParamenter.setParaCode(paraCode);
				mPDao.saveModelParamenter(modelParamenter);
			}
			
		}
		
/*		String[] paraName=mp.getParaName().split(",");
		String [] paraSymbol=mp.getParaSymbol().split(",");
		String [] paraCode=mp.getParaCode().split(",");
		String [] paraMax=mp.getParaMax().split(",");
		String [] paraMin=mp.getParaMin().split(",");
		if(!mp.getParaName().equals("")) {
			if(paraName.length>0) {
				for(int i=0;i<paraName.length;i++){
					if(paraCode[i].equals("")||paraCode[i]==null) {//如果没有paraCode就新增，有就修改
						 paraCode[i]=Randomizer.nextNumber("CS", 6);
						mp.setParaCode(paraCode[i].trim());
						mp.setParaName(paraName[i].trim());
						mp.setParaSymbol(paraSymbol[i].trim());
						mp.setModelCode(modelInfo.getModelCode().trim());
						mp.setParaMax(paraMax[i].trim());
						mp.setParaMin(paraMin[i].trim());
						mPDao.saveModelParamenter(mp);//新增
					}else {
						mp.setParaCode(paraCode[i].trim());
						mp.setParaName(paraName[i].trim());
						mp.setParaSymbol(paraSymbol[i].trim());
						mp.setParaMax(paraMax[i].trim());
						mp.setParaMin(paraMin[i].trim());
						mPDao.updateModelParamenter(mp);//修改
					}
				}
			}
		}*/
	
		return modelInfo;
	}
	
	/**
	 * 根据主键删除模型信息实体
	 * */
	@Transactional(propagation=Propagation.REQUIRED)
	public void delModelInfoByCodes(String ids) {
		String[] idary=ids.split(",");
		for(int i=0;i<idary.length;i++){
			mInfoDao.delModelInfoByCodes(idary[i]); //删除模型
			mPDao.delModelParaBymCode(idary[i]); //删除参数值
		}
	}
	
	/**
	 * 导入
	 */
	@SuppressWarnings("static-access")
	@Transactional(propagation=Propagation.REQUIRED)
	public void importModel(File[] file,String[] fileFileName) throws IOException{
		String modelCode=Randomizer.nextNumber("mx", 6);//生产模型编码 xm+6个数字 共八位
		File[] srcFiles = file;
		InputStream in = null;
		ExcelVersionUtil ev=new ExcelVersionUtil();
		ImportExeclUtil importExeclUtil=new ImportExeclUtil();
		for(int i = 0; i < srcFiles.length; i++){
			in = new BufferedInputStream(new FileInputStream(srcFiles[i]));
			boolean isExcel2003 = true;// 根据文件名判断文件是2003版本还是2007版本  
            if (ev.isExcel2007(fileFileName[i])) {  
                isExcel2003 = false;  
            }
            //通过工具栏ImportExeclUtil中的read方法解析excel
            List<List<String>> dataLst =importExeclUtil.read(in,isExcel2003);
            for(int j=1;j<dataLst.size();j++){
            	List<String> list=dataLst.get(j);
            	ModelInfo modelInfo=new ModelInfo();
            	modelInfo.setModelCode(modelCode);
            	modelInfo.setModelName(list.get(2));
            	modelInfo.setModelType(list.get(3));
            	modelInfo.setRemark(list.get(4));
            	modelInfo.setState( Integer.parseInt(list.get(5)));
            	mInfoDao.save(modelInfo);
            }
	}
	
	}
	
	/**
	 * 导出
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void export(ModelInfoFormBean modelInfoFormBean ,PageResults prs,HttpServletRequest request,HttpServletResponse response) throws IOException{
		String year = DateUtil.getYear();//年
		String method = DateUtil.getMonth();//月
		String day = DateUtil.getDay();//日
		String replace="模型列表_"+year + "年" + method + "月" +day + "日";
		String title="模型列表";
		String [] tab = {"序号","模型分类","模型名称","参数数量","模型描述","状态"};
		String [] val = {"MODE_TYPE","MODEL_NAME","CSSL","REMARK","STATE"};
		List result=mInfoDao.getModelListData_export(modelInfoFormBean).getResults();
		String file = ExcelUtils.SellerStat2Excel(result, request, replace,tab,title,val);
		response.setContentType("multipart/form-data");  
		String path = request.getSession().getServletContext().getRealPath("/")+file;
		response.setHeader("Content-Disposition", "attachment;fileName="+new String(file.getBytes("UTF-8"),"ISO8859-1"));
        //通过文件路径获得File对象(假如此路径中有一个download.pdf文件)  
        File files = new File(path);
        FileInputStream inputStream = new FileInputStream(files);
        ServletOutputStream out= response.getOutputStream();
        int b = 0;  
        byte[] buffer = new byte[1024];  
        while (b != -1){  
            b = inputStream.read(buffer);  
            //4.写到输出流(out)中  
            out.write(buffer,0,b);  
        }  
        inputStream.close();  
        out.close();  
        out.flush();
	}
	
	
	/**
	 * getModelnamebyType
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public PageResults getModelNamebyType(ModelInfoFormBean modelInfoFormBean) {
		return mInfoDao.getModelNamebyType(modelInfoFormBean);
	}
	
	/**
	 *  * 汇流
	 * @param modelInfoFormBean
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public PageResults getModelHuiliu(ModelInfoFormBean modelInfoFormBean) {
		return mInfoDao.getModelHuiliu(modelInfoFormBean);
	}
	
	/**
	 *  产流
	 * @param modelInfoFormBean
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public PageResults getModelChanliu(ModelInfoFormBean modelInfoFormBean) {
		return mInfoDao.getModelChanliu(modelInfoFormBean);
	}
	
}
