package com.lyht.util;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@SuppressWarnings({ "deprecation", "rawtypes" })
public class ExcelUtils {
	public static String SellerStat2Excel( List data ,HttpServletRequest request ,String ymonth, String [] tabHead ,String sheetTitle,String[] val) {
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetTitle);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
        sheet.setDefaultColumnWidth((short) 15); 
        // 第四步，创建单元格，并设置值表头 设置表头居中s
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        HSSFCell cell = null;
        int index = 1;
        for (String str : tabHead) {
			if(cell == null) {
				cell = row.createCell((short) 0);
				cell.setCellValue(str);
		        cell.setCellStyle(style);
			}else{
				cell = row.createCell((short) index);
				cell.setCellValue(str);
		        cell.setCellStyle(style);
		        index ++;
			}
		}
        style.setLocked(true);
        
        int i = 0;
        try {
        	for (Object obj : data) {
        		Map map = null;
        		if(obj instanceof Map){
        			map = (Map)obj;
        		}
                row = sheet.createRow((int) i + 1);
                int num = 1;
                row.createCell((short) 0).setCellValue(i+1);
                // 第四步，创建单元格，并设置值
                for (String str : val) {
                	Object object=map.get(str);
                	if(null==object){
                		row.createCell((short) num).setCellValue("");
                	}else{
                		row.createCell((short) num).setCellValue(object.toString());
                	}
	                num++;
                }
                i++;
        	}
        } catch (Exception e) {
            e.printStackTrace();
        }
        String realPath = request.getSession().getServletContext().getRealPath("/");
        // 第六步，将文件存到指定位置
        String path = realPath+ymonth+"_"+sheetTitle+".xls";
        try {
        	FileOutputStream  fout = new FileOutputStream(path);
            wb.write(fout);
            fout.flush();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ymonth+"_"+sheetTitle+".xls";
    }
	
	//产流第三步
	public static String exportCl3( List data ,String hj,String rs,HttpServletRequest request ,String ymonth, String [] tabHead ,String sheetTitle,String[] val) {
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetTitle);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
        sheet.setDefaultColumnWidth((short) 15); 
        // 第四步，创建单元格，并设置值表头 设置表头居中s
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        HSSFCell cell = null;
        int index = 1;
        for (String str : tabHead) {
			if(cell == null) {
				cell = row.createCell( 0);
				cell.setCellValue(str);
		        cell.setCellStyle(style);
			}else{
				cell = row.createCell(index);
				cell.setCellValue(str);
		        cell.setCellStyle(style);
		        index ++;
			}
		}
        style.setLocked(true);
        
        int i = 0;
        try {
        	for (Object obj : data) {
        		Map map = null;
        		if(obj instanceof Map){
        			map = (Map)obj;
        		}
                row = sheet.createRow((int) i + 1);
                int num = 1;
                row.createCell(0).setCellValue(i+1);
                // 第四步，创建单元格，并设置值
                for (String str : val) {
                	Object object=map.get(str);
                	if(null==object){
                		row.createCell( num).setCellValue("");
                	}else{
                		row.createCell( num).setCellValue(object.toString());
                	}
	                num++;
                }
                i++;
        	}
        	   row = sheet.createRow((int) i + 1);
               row.createCell(1).setCellValue("流量合计");
               row.createCell(4).setCellValue(hj);
               row = sheet.createRow((int) i + 2);
               row.createCell(1).setCellValue("R实(mm)");
               row.createCell(4).setCellValue(rs);
          
        } catch (Exception e) {
            e.printStackTrace();
        }
        String realPath = request.getSession().getServletContext().getRealPath("/");
        // 第六步，将文件存到指定位置
        String path = realPath+ymonth+"_"+sheetTitle+".xls";
        try {
        	FileOutputStream  fout = new FileOutputStream(path);
            wb.write(fout);
            fout.flush();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ymonth+"_"+sheetTitle+".xls";
    }
		public static String exportChanliuStep4Excel(JSONArray dataList,HttpServletRequest request ,String pch,String [] tabHead ,String sheetTitle,String[] val){
			// 第一步，创建一个webbook，对应一个Excel文件
	        HSSFWorkbook wb = new HSSFWorkbook();
	        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
	        HSSFSheet sheet = wb.createSheet(sheetTitle);
	        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
	        HSSFRow row = sheet.createRow((int) 0);
	        sheet.setDefaultColumnWidth((short) 15); 
	        // 第四步，创建单元格，并设置值表头 设置表头居中s
	        HSSFCellStyle style = wb.createCellStyle();
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
	        HSSFCell cell = null;
	        int index = 1;
	        for (String str : tabHead) {
				if(cell == null) {
					cell = row.createCell( 0);
					cell.setCellValue(str);
			        cell.setCellStyle(style);
				}else{
					cell = row.createCell(index);
					cell.setCellValue(str);
			        cell.setCellStyle(style);
			        index ++;
				}
			}
	        style.setLocked(true);
	        
	        try {
	        	for (int i=0;i<dataList.size();i++) {
	        		JSONObject map = dataList.getJSONObject(i);
	                row = sheet.createRow((int) i + 1);
	                int num = 1;
	                row.createCell(0).setCellValue(i+1);
	                // 第四步，创建单元格，并设置值
	                for (String str : val) {
	                	Object object=map.get(str);
	                	if(null==object){
	                		row.createCell( num).setCellValue("");
	                	}else{
	                		row.createCell( num).setCellValue(object.toString());
	                	}
		                num++;
	                }
	        	}
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        String realPath = request.getSession().getServletContext().getRealPath("/");
	        // 第六步，将文件存到指定位置
	        String path = realPath+pch+"_"+sheetTitle+".xls";
	        try {
	        	FileOutputStream  fout = new FileOutputStream(path);
	            wb.write(fout);
	            fout.flush();
	            fout.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return pch+"_"+sheetTitle+".xls";
		}
	   //产流第4步
		public static String exportCl4( List data ,String hj,String rs,HttpServletRequest request ,String ymonth, String [] tabHead ,String sheetTitle,String[] val) {
	        // 第一步，创建一个webbook，对应一个Excel文件
	        HSSFWorkbook wb = new HSSFWorkbook();
	        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
	        HSSFSheet sheet = wb.createSheet(sheetTitle);
	        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
	        HSSFRow row = sheet.createRow((int) 0);
	        sheet.setDefaultColumnWidth((short) 15); 
	        // 第四步，创建单元格，并设置值表头 设置表头居中s
	        HSSFCellStyle style = wb.createCellStyle();
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
	        HSSFCell cell = null;
	        int index = 1;
	        for (String str : tabHead) {
				if(cell == null) {
					cell = row.createCell( 0);
					cell.setCellValue(str);
			        cell.setCellStyle(style);
				}else{
					cell = row.createCell(index);
					cell.setCellValue(str);
			        cell.setCellStyle(style);
			        index ++;
				}
			}
	        style.setLocked(true);
	        
	        int i = 0;
	        try {
	        	for (Object obj : data) {
	        		Map map = null;
	        		if(obj instanceof Map){
	        			map = (Map)obj;
	        		}
	                row = sheet.createRow((int) i + 1);
	                int num = 1;
	                row.createCell(0).setCellValue(i+1);
	                // 第四步，创建单元格，并设置值
	                for (String str : val) {
	                	Object object=map.get(str);
	                	if(null==object){
	                		row.createCell( num).setCellValue("");
	                	}else{
	                		row.createCell( num).setCellValue(object.toString());
	                	}
		                num++;
	                }
	                i++;
	        	}
	        	   row = sheet.createRow((int) i + 1);
	               row.createCell(1).setCellValue("流量(地下)合计");
	               row.createCell(4).setCellValue(hj);
	               row = sheet.createRow((int) i + 2);
	               row.createCell(1).setCellValue("R实(mm)");
	               row.createCell(4).setCellValue(rs);
	          
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        String realPath = request.getSession().getServletContext().getRealPath("/");
	        // 第六步，将文件存到指定位置
	        String path = realPath+ymonth+"_"+sheetTitle+".xls";
	        try {
	        	FileOutputStream  fout = new FileOutputStream(path);
	            wb.write(fout);
	            fout.flush();
	            fout.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return ymonth+"_"+sheetTitle+".xls";
	    }
	
	public static String SellerStat2Excel6( List data ,Map map1,HttpServletRequest request ,String ymonth, String [] tabHead ,String sheetTitle,String[] val) {
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetTitle);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
        sheet.setDefaultColumnWidth( 15); 
        // 第四步，创建单元格，并设置值表头 设置表头居中s
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        HSSFCell cell = null;
        int index = 1;
        for (String str : tabHead) {
			if(cell == null) {
				cell = row.createCell(0);
				cell.setCellValue(str);
		        cell.setCellStyle(style);
			}else{
				cell = row.createCell( index);
				cell.setCellValue(str);
		        cell.setCellStyle(style);
		        index ++;
			}
		}
        style.setLocked(true);
        
        int i = 0;
        try {
        	for (Object obj : data) {
        		Map map = null;
        		if(obj instanceof Map){
        			map = (Map)obj;
        		}
                row = sheet.createRow((int) i + 1);
                int num = 1;
                row.createCell( 0).setCellValue(i+1);
                // 第四步，创建单元格，并设置值
                for (String str : val) {
                	Object object=map.get(str);
                	if(null==object){
                		row.createCell( num).setCellValue("");
                	}else{
                		row.createCell( num).setCellValue(object.toString());
                	}
	                num++;
                }
                i++;
        	}
            row = sheet.createRow((int) i + 1);
            row.createCell(0).setCellValue("洪号");
            row.createCell(1).setCellValue("Tc");
            row.createCell(2).setCellValue("Rc");
            row.createCell(3).setCellValue("Rc/Tc");
            row.createCell(4).setCellValue("Fc");
            row = sheet.createRow((int) i + 2);
            row.createCell(0).setCellValue(map1.get("pch").toString());
            row.createCell(1).setCellValue(map1.get("tc").toString());
            row.createCell(2).setCellValue(map1.get("rc").toString());
            row.createCell(3).setCellValue(map1.get("rctc").toString());
            row.createCell(4).setCellValue(map1.get("fc").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String realPath = request.getSession().getServletContext().getRealPath("/");
        // 第六步，将文件存到指定位置
        String path = realPath+ymonth+"_"+sheetTitle+".xls";
        try {
        	FileOutputStream  fout = new FileOutputStream(path);
            wb.write(fout);
            fout.flush();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ymonth+"_"+sheetTitle+".xls";
    }
	/**导出到excel，不包含序号
	 * @return
	 */
	public static String exportExcel( List data ,HttpServletRequest request ,String ymonth, String [] tabHead ,String sheetTitle,String[] val) {
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetTitle);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
        sheet.setDefaultColumnWidth(15); 
        // 第四步，创建单元格，并设置值表头 设置表头居中s
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        HSSFCell cell = null;
        int index = 1;
        for (String str : tabHead) {
			if(cell == null) {
				cell = row.createCell(0);
				cell.setCellValue(str);
		        cell.setCellStyle(style);
			}else{
				cell = row.createCell( index);
				cell.setCellValue(str);
		        cell.setCellStyle(style);
		        index ++;
			}
		}
        style.setLocked(true);
        
        int i = 0;
        try {
        	for (Object obj : data) {
        		Map map = null;
        		if(obj instanceof Map){
        			map = (Map)obj;
        		}
                row = sheet.createRow((int) i + 1);
                int num = 0;
                // 第四步，创建单元格，并设置值
                for (String str : val) {
                	Object object=map.get(str);
                	if(null==object){
                		row.createCell( num).setCellValue("");
                	}else if(str.equals("MJMC")){//是否合格
                		if(Double.parseDouble(object.toString())>=0&&Double.parseDouble(object.toString())<=0.2) {
                			row.createCell(num).setCellValue("合格");
                		}else {
                			row.createCell(num).setCellValue("不合格");
                		}
                	}else {
                		row.createCell(num).setCellValue(object.toString());
                	}
	                num++;
                }
                i++;
        	}
        } catch (Exception e) {
            e.printStackTrace();
        }
        String realPath = request.getSession().getServletContext().getRealPath("/");
        // 第六步，将文件存到指定位置
        String path = realPath+ymonth+"_"+sheetTitle+".xls";
        try {
        	FileOutputStream  fout = new FileOutputStream(path);
            wb.write(fout);
            fout.flush();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ymonth+"_"+sheetTitle+".xls";
    }
	
	public static String exporth4Excel3( List data ,HttpServletRequest request ,String ymonth, String [] tabHead ,String sheetTitle,String[] val) {
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetTitle);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
        sheet.setDefaultColumnWidth(15); 
        // 第四步，创建单元格，并设置值表头 设置表头居中s
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        HSSFCell cell = null;
        int index = 1;
        for (String str : tabHead) {
			if(cell == null) {
				cell = row.createCell(0);
				cell.setCellValue(str);
		        cell.setCellStyle(style);
			}else{
				cell = row.createCell( index);
				cell.setCellValue(str);
		        cell.setCellStyle(style);
		        index ++;
			}
		}
        style.setLocked(true);
        
        int i = 0;
        double mjmc=0;
        try {
        	for (Object obj : data) {
        		Map map = null;
        		if(obj instanceof Map){
        			map = (Map)obj;
        		}
                row = sheet.createRow((int) i + 1);
                int num = 0;
              
                // 第四步，创建单元格，并设置值
                for (String str : val) {
                	Object object=map.get(str);
                	if(null==object){
                		if(mjmc<=0.2&&mjmc>0) {
                			row.createCell(num).setCellValue("合格");
                		}else {
                			row.createCell(num).setCellValue("不合格");
                		}
                	}else if(str.equals("MJMC")){
                		mjmc=Double.parseDouble(object.toString());
                		row.createCell(num).setCellValue(object.toString());
                	}else {
                		row.createCell(num).setCellValue(object.toString());
                	}
	                num++;
                }
                i++;
        	}
        } catch (Exception e) {
            e.printStackTrace();
        }
        String realPath = request.getSession().getServletContext().getRealPath("/");
        // 第六步，将文件存到指定位置
        String path = realPath+ymonth+"_"+sheetTitle+".xls";
        try {
        	FileOutputStream  fout = new FileOutputStream(path);
            wb.write(fout);
            fout.flush();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ymonth+"_"+sheetTitle+".xls";
    }
	
	/**
	 * 产流计算导出到excel，跨行跨列
	 * @param data
	 * @param request
	 * @param ymonth
	 * @param tabHead
	 * @param sheetTitle
	 * @param val
	 * @return
	 */
	public static String exportChanLiuExcel( List data , Object[] nStrings, String[] qzArr,HttpServletRequest request ,String ymonth, String [] tabHead ,String sheetTitle,String[] val) {
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetTitle);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        sheet.addMergedRegion(new CellRangeAddress(0,1,0,0));//合并序号
        sheet.addMergedRegion(new CellRangeAddress(0,1,1,1));//合并时间
        HSSFRow row = sheet.createRow((int) 0);
        HSSFCellStyle style = wb.createCellStyle();
        HSSFCellStyle style2 = wb.createCellStyle();
        sheet.setDefaultColumnWidth(10); 
        sheet.setColumnWidth(1, 20*256);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        
        HSSFCell  cell= null;
        cell=row.createCell(0);
        cell.setCellValue("序号");
        cell.setCellStyle(style);
        cell=row.createCell(1);
        cell.setCellValue("日期");
        cell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(0,1,2+qzArr.length*2,2+qzArr.length*2));//合并最后一列
        for(int i=0;i<qzArr.length;i++) {
        	  sheet.addMergedRegion(new CellRangeAddress(0,0,2+2*i,3+2*i));//合并中间的测站，权重
        		 cell=row.createCell(2+2*i);
                 cell.setCellValue((String)nStrings[i]);
                 cell.setCellStyle(style);
        }
      	cell=row.createCell(2+qzArr.length*2);
        cell.setCellValue("雨面量");
        cell.setCellStyle(style);
       
        row = sheet.createRow((int)  1);
        HSSFCell  cell1= null;
        for(int i=0;i<qzArr.length;i++) {
        	cell1=row.createCell(2+2*i);
        	cell1.setCellValue("权重");
        	cell1=row.createCell(3+2*i);
        	cell1.setCellValue((String)qzArr[i]);
        }

        style.setLocked(true);
        int i = 0;
        try {
         	List ymllist=new ArrayList();
       	for (Object obj : data) {
        		Map map = null;
        		if(obj instanceof Map){
        			map = (Map)obj;
        		}
               row = sheet.createRow((int) i + 2);
                int num = 1;
                row.createCell( 0).setCellValue(i+1);
                // 第四步，创建单元格，并设置值
                for (String str : val) {
                	Object object=map.get(str);
                	if(str.equals("YML")) {
                	
                		  row.createCell(2+qzArr.length*2).setCellValue(Double.parseDouble(object.toString()));
                	}
                	if(str.equals("JYL")) {
                		String[]arr=object.toString().split(",");
                		for(int j=0;j<arr.length;j++) {
                			 row.createCell(2+j*2).setCellValue(Double.parseDouble(arr[j]));
                			  sheet.addMergedRegion(new CellRangeAddress(2+i,2+i,2+2*j,3+2*j));
                		}
                	}else {
                		if(null==object){
                    		row.createCell(num).setCellValue("");
                    	}else{
                    		row.createCell(num).setCellValue(object.toString());
                    	}
                	}
	                num++;
                }
                i++;
              
        	}
        } catch (Exception e) {
            e.printStackTrace();
        }
        String realPath = request.getSession().getServletContext().getRealPath("/");
        // 第六步，将文件存到指定位置
        String path = realPath+ymonth+""+sheetTitle+".xls";
        try {
        	FileOutputStream  fout = new FileOutputStream(path);
            wb.write(fout);
            fout.flush();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ymonth+""+sheetTitle+".xls";
    }
	
	
	public static String exportChanLiuExcel5( List data , Object[] nStrings, String[] qzArr,HttpServletRequest request ,String ymonth, String [] tabHead ,String sheetTitle,String[] val) {
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetTitle);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        sheet.addMergedRegion(new CellRangeAddress(0,1,0,0));//合并序号
        sheet.addMergedRegion(new CellRangeAddress(0,1,1,1));//合并时间
        HSSFRow row = sheet.createRow((int) 0);
        HSSFCellStyle style = wb.createCellStyle();
        HSSFCellStyle style2 = wb.createCellStyle();
        sheet.setDefaultColumnWidth(10); 
        sheet.setColumnWidth(1, 20*256);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        
        HSSFCell  cell= null;
        cell=row.createCell(0);
        cell.setCellValue("序号");
        cell.setCellStyle(style);
        cell=row.createCell(1);
        cell.setCellValue("日期");
        cell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(0,1,2+qzArr.length*2,2+qzArr.length*2));//合并
        sheet.addMergedRegion(new CellRangeAddress(0,1,3+qzArr.length*2,3+qzArr.length*2));//
        sheet.addMergedRegion(new CellRangeAddress(0,1,4+qzArr.length*2,4+qzArr.length*2));//
        for(int i=0;i<qzArr.length;i++) {
        	  sheet.addMergedRegion(new CellRangeAddress(0,0,2+2*i,3+2*i));//合并中间的测站，权重
        		 cell=row.createCell(2+2*i);
                 cell.setCellValue((String)nStrings[i]);
                 cell.setCellStyle(style);
        }
      	cell=row.createCell(2+qzArr.length*2);
        cell.setCellValue("雨面量");
        cell.setCellStyle(style);
    	cell=row.createCell(3+qzArr.length*2);
        cell.setCellValue("Em");
        cell.setCellStyle(style);
        cell=row.createCell(4+qzArr.length*2);
        cell.setCellValue("Pa");
        cell.setCellStyle(style);
       
        row = sheet.createRow((int)  1);
        HSSFCell  cell1= null;
        for(int i=0;i<qzArr.length;i++) {
        	cell1=row.createCell(2+2*i);
        	cell1.setCellValue("权重");
        	cell1=row.createCell(3+2*i);
        	cell1.setCellValue((String)qzArr[i]);
        }

        style.setLocked(true);
        int i = 0;
        try {
         	List ymllist=new ArrayList();
       	for (Object obj : data) {
        		Map map = null;
        		if(obj instanceof Map){
        			map = (Map)obj;
        		}
               row = sheet.createRow((int) i + 2);
                int num = 1;
                row.createCell( 0).setCellValue(i+1);
                // 第四步，创建单元格，并设置值
                for (String str : val) {
                	Object object=map.get(str);
                	if(str.equals("YML")) {
                		  row.createCell(2+qzArr.length*2).setCellValue(Double.parseDouble(object.toString()));
                	}
                	if(str.equals("EM")) {
              		  row.createCell(3+qzArr.length*2).setCellValue(Double.parseDouble(object.toString()));
              	}
                	if(str.equals("PA")) {
              		  row.createCell(4+qzArr.length*2).setCellValue(Double.parseDouble(object.toString()));
              	}
                	if(str.equals("JYL")) {
                		String[]arr=object.toString().split(",");
                		for(int j=0;j<arr.length;j++) {
               			 row.createCell(2+j*2).setCellValue(Double.parseDouble(arr[j]));
               			 sheet.addMergedRegion(new CellRangeAddress(2+i,2+i,2+2*j,3+2*j));
                		}
                	}else {
                		if(null==object){
                    		row.createCell(num).setCellValue("");
                    	}else{
                    		row.createCell(num).setCellValue(object.toString());
                    	}
                	}
	                num++;
                }
                i++;
              
        	}
        } catch (Exception e) {
            e.printStackTrace();
        }
        String realPath = request.getSession().getServletContext().getRealPath("/");
        // 第六步，将文件存到指定位置
        String path = realPath+ymonth+""+sheetTitle+".xls";
        try {
        	FileOutputStream  fout = new FileOutputStream(path);
            wb.write(fout);
            fout.flush();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ymonth+""+sheetTitle+".xls";
    }

	public static String SellerStat2Excel7( List<Map> data ,HttpServletRequest request ,String ymonth, String [] tabHead ,String sheetTitle,String[] val) {
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetTitle);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
        sheet.setDefaultColumnWidth(10); 
        sheet.setColumnWidth(1, 20*256);
        // 第四步，创建单元格，并设置值表头 设置表头居中s
        HSSFCellStyle style = wb.createCellStyle();
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,13));//合并产流
        sheet.addMergedRegion(new CellRangeAddress(0,0,13,20));//合并下渗
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        double dr=0;
        double sjrbrs=0;
        double qt2=0;
        Object rs=data.get(0).get("RS");
        Object erc=data.get(0).get("ERC");
        if(rs!=null&&erc!=null) {
        	dr=Double.parseDouble(rs.toString())-Double.parseDouble(erc.toString());
        	sjrbrs=(Double.parseDouble(rs.toString())-Double.parseDouble(erc.toString()))/Double.parseDouble(rs.toString());
        	 qt2=new BigDecimal(sjrbrs*100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        	 if(Math.abs(qt2)<=0.2) {//取绝对值
        		  data.get(0).put("CLSFHG", "合格");
        	 }else {
        		 data.get(0).put("CLSFHG", "不合格");
        	 }
        }
        String sj=String.valueOf(qt2+"%") ;
        data.get(0).put("SJR", dr);
        data.get(0).put("SJRBRS", sj);
        HSSFCell cell = null;
        cell = row.createCell( 0);
		cell.setCellValue("产流");
        cell.setCellStyle(style);
        cell = row.createCell( 13);
   		cell.setCellValue("下渗");
          cell.setCellStyle(style);
        int index = 0;
        row = sheet.createRow((1));
        for (String str : tabHead) {
			if(cell == null) {
				cell = row.createCell( 0);
				cell.setCellValue(str);
		        cell.setCellStyle(style);
			}else{
				cell = row.createCell( index);
				cell.setCellValue(str);
		        cell.setCellStyle(style);
		        index ++;
			}
		}
   
        double fcs=0;
        int i = 0;
        try {
        	for (Object obj : data) {
        		Map map = null;
        		if(obj instanceof Map){
        			map = (Map)obj;
        		}
                row = sheet.createRow((int) i + 2);
                int num = 1;
                row.createCell(0).setCellValue(i+1);
                // 第四步，创建单元格，并设置值
                for (String str : val) {
                	Object object=map.get(str);
                	if(null==object){
                		if(str.equals("PAPE")) {//如果是pape
                			row.createCell(num).setCellValue(0.00);
                		}else {//下渗是否合格
                			 if(fcs<=0.2) {
 	                			row.createCell(num).setCellValue("合格");
 	                   	 }else {
 	                   		row.createCell(num).setCellValue("不合格");
 	                   	 }
                		}
                	}else if(str.equals("FCS")){
                		fcs=Double.parseDouble(object.toString());
                		row.createCell(num).setCellValue(object.toString());
                	}else {
                		row.createCell(num).setCellValue(object.toString());
                	}
	                num++;
                }
                i++;
        	}
        } catch (Exception e) {
            e.printStackTrace();
        }
        String realPath = request.getSession().getServletContext().getRealPath("/");
        // 第六步，将文件存到指定位置
        String path = realPath+ymonth+""+sheetTitle+".xls";
        try {
        	FileOutputStream  fout = new FileOutputStream(path);
            wb.write(fout);
            fout.flush();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ymonth+""+sheetTitle+".xls";
    }
	
	public static String exportHltable3( List data ,HttpServletRequest request ,String ymonth, String [] tabHead ,String sheetTitle,String[] val) {
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetTitle);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
        sheet.setDefaultColumnWidth(10); 
        sheet.setColumnWidth(1, 20*256);
        // 第四步，创建单元格，并设置值表头 设置表头居中s
        HSSFCellStyle style = wb.createCellStyle();
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,12));//合并产流
        sheet.addMergedRegion(new CellRangeAddress(0,0,13,19));//合并下渗
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        
        HSSFCell cell = null;
        cell = row.createCell( 0);
		cell.setCellValue("产流");
        cell.setCellStyle(style);
        cell = row.createCell( 13);
   		cell.setCellValue("下渗");
          cell.setCellStyle(style);
        int index = 0;
        row = sheet.createRow((1));
        for (String str : tabHead) {
			if(cell == null) {
				cell = row.createCell( 0);
				cell.setCellValue(str);
		        cell.setCellStyle(style);
			}else{
				cell = row.createCell( index);
				cell.setCellValue(str);
		        cell.setCellStyle(style);
		        index ++;
			}
		}
   
        
        int i = 0;
        try {
        	for (Object obj : data) {
        		Map map = null;
        		if(obj instanceof Map){
        			map = (Map)obj;
        		}
                row = sheet.createRow((int) i + 2);
                int num = 1;
                row.createCell(0).setCellValue(i+1);
                // 第四步，创建单元格，并设置值
                for (String str : val) {
                	Object object=map.get(str);
                	if(null==object){
                		row.createCell(num).setCellValue("");
                	}else{
                		row.createCell(num).setCellValue(object.toString());
                	}
	                num++;
                }
                i++;
        	}
        } catch (Exception e) {
            e.printStackTrace();
        }
        String realPath = request.getSession().getServletContext().getRealPath("/");
        // 第六步，将文件存到指定位置
        String path = realPath+ymonth+""+sheetTitle+".xls";
        try {
        	FileOutputStream  fout = new FileOutputStream(path);
            wb.write(fout);
            fout.flush();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ymonth+""+sheetTitle+".xls";
    }
	
	//单站综合及误差统计
	public static String exportHuiliu2Excel( List data ,HttpServletRequest request ,String ymonth, String [] tabHead ,String sheetTitle,String[] val) {
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetTitle);
        sheet.addMergedRegion(new CellRangeAddress(0,1,0,0));//
        sheet.addMergedRegion(new CellRangeAddress(0,1,1,1));//
        sheet.addMergedRegion(new CellRangeAddress(0,1,2,2));//
        sheet.addMergedRegion(new CellRangeAddress(0,0,3,5));//
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
        sheet.setDefaultColumnWidth(15); 
        // 第四步，创建单元格，并设置值表头 设置表头居中s
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
     
        HSSFCell cell = null;
        cell = row.createCell( 0);
		cell.setCellValue("洪号");
        cell.setCellStyle(style);
        cell = row.createCell( 1);
		cell.setCellValue("R上/tc(毫米/小时)");
        cell.setCellStyle(style);
        cell = row.createCell( 2);
      	cell.setCellValue("m计");
        cell.setCellStyle(style);
        cell = row.createCell( 3);
		cell.setCellValue("R上/tc ~m");
        cell.setCellStyle(style);
        int index = 1;
        row=sheet.createRow((int) 1);
        for(int i=0;i<tabHead.length;i++) {
        	cell = row.createCell( i);
        	cell.setCellValue(tabHead[i]);
        	 cell.setCellStyle(style);
        }
        style.setLocked(true);
        
        int i = 0;
        try {
        	for (Object obj : data) {
        		Map map = null;
        		if(obj instanceof Map){
        			map = (Map)obj;
        		}
                row = sheet.createRow((int) i + 2);
                int num = 0;
                // 第四步，创建单元格，并设置值
                for (String str : val) {
                	Object object=map.get(str);
                	if(null==object){
                		row.createCell(num).setCellValue("");
                	}else{
                		row.createCell(num).setCellValue(object.toString());
                	}
	                num++;
                }
                i++;
        	}
        } catch (Exception e) {
            e.printStackTrace();
        }
        String realPath = request.getSession().getServletContext().getRealPath("/");
        // 第六步，将文件存到指定位置
        String path = realPath+ymonth+"_"+sheetTitle+".xls";
        try {
        	FileOutputStream  fout = new FileOutputStream(path);
            wb.write(fout);
            fout.flush();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ymonth+"_"+sheetTitle+".xls";
    }
	
	
		//计算m值
		public static String exportHlStep2( List data ,String n,HttpServletRequest request ,String ymonth, String [] tabHead ,String sheetTitle,String[] val) {
	        // 第一步，创建一个webbook，对应一个Excel文件
	        HSSFWorkbook wb = new HSSFWorkbook();
	        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
	        HSSFSheet sheet = wb.createSheet(sheetTitle);
	        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
	        HSSFRow row = sheet.createRow((int) 0);
	        sheet.setDefaultColumnWidth(15); 
	        // 第四步，创建单元格，并设置值表头 设置表头居中s
	        HSSFCellStyle style = wb.createCellStyle();
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
	     
	        HSSFCell cell = null;
	        cell = row.createCell( 9);
			cell.setCellValue("N="+n+"");
	        cell.setCellStyle(style);
	        for(int i=0;i<9;i++) {
	        	cell = row.createCell( i);
	        	cell.setCellValue(tabHead[i]);
	        	 cell.setCellStyle(style);
	        	 sheet.addMergedRegion(new CellRangeAddress(0,1,i,i));//合并
	        }
	        sheet.addMergedRegion(new CellRangeAddress(0,0,9,12));//合并
	        row = sheet.createRow((int) 1);
	        for(int i=0;i<tabHead.length;i++) {
	        	cell = row.createCell( i);
	        	cell.setCellValue(tabHead[i]);
	        	 cell.setCellStyle(style);
	        }
	        style.setLocked(true);
	        
	        int i = 0;
	        try {
	        	for (Object obj : data) {
	        		Map map = null;
	        		if(obj instanceof Map){
	        			map = (Map)obj;
	        		}
	                row = sheet.createRow((int) i + 2);
	                int num = 0;
	                // 第四步，创建单元格，并设置值
	                for (String str : val) {
	                	Object object=map.get(str);
	                	if(null==object){
	                		row.createCell(num).setCellValue("");
	                	}else{
	                		row.createCell(num).setCellValue(object.toString());
	                	}
		                num++;
	                }
	                i++;
	        	}
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        String realPath = request.getSession().getServletContext().getRealPath("/");
	        // 第六步，将文件存到指定位置
	        String path = realPath+ymonth+"_"+sheetTitle+".xls";
	        try {
	        	FileOutputStream  fout = new FileOutputStream(path);
	            wb.write(fout);
	            fout.flush();
	            fout.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return ymonth+"_"+sheetTitle+".xls";
	    }
		
		public static String step3_export3( List data ,HttpServletRequest request ,String ymonth, String [] tabHead ,String sheetTitle,String[] val) {
	        // 第一步，创建一个webbook，对应一个Excel文件
	        HSSFWorkbook wb = new HSSFWorkbook();
	        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
	        HSSFSheet sheet = wb.createSheet(sheetTitle);
	        sheet.addMergedRegion(new CellRangeAddress(0,1,0,0));//
	        sheet.addMergedRegion(new CellRangeAddress(0,1,1,1));//
	        sheet.addMergedRegion(new CellRangeAddress(0,1,2,2));//
	        sheet.addMergedRegion(new CellRangeAddress(0,0,3,5));//
	        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
	        HSSFRow row = sheet.createRow((int) 0);
	        sheet.setDefaultColumnWidth(15); 
	        // 第四步，创建单元格，并设置值表头 设置表头居中s
	        HSSFCellStyle style = wb.createCellStyle();
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
	     
	        HSSFCell cell = null;
	        cell = row.createCell( 0);
			cell.setCellValue("洪号");
	        cell.setCellStyle(style);
	        cell = row.createCell( 1);
			cell.setCellValue("Q/F(毫米/小时)");
	        cell.setCellStyle(style);
	        cell = row.createCell( 2);
	      	cell.setCellValue("m计");
	        cell.setCellStyle(style);
	        cell = row.createCell( 3);
			cell.setCellValue("Q/F ~m");
	        cell.setCellStyle(style);
	        int index = 1;
	        row=sheet.createRow((int) 1);
	        for(int i=0;i<tabHead.length;i++) {
	        	cell = row.createCell( i);
	        	cell.setCellValue(tabHead[i]);
	        	 cell.setCellStyle(style);
	        }
	        style.setLocked(true);
	        
	        int i = 0;
	        try {
	        	for (Object obj : data) {
	        		Map map = null;
	        		if(obj instanceof Map){
	        			map = (Map)obj;
	        		}
	                row = sheet.createRow((int) i + 2);
	                int num = 0;
	                // 第四步，创建单元格，并设置值
	                for (String str : val) {
	                	Object object=map.get(str);
	                	if(null==object){
	                		row.createCell(num).setCellValue("");
	                	}else{
	                		row.createCell(num).setCellValue(object.toString());
	                	}
		                num++;
	                }
	                i++;
	        	}
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        String realPath = request.getSession().getServletContext().getRealPath("/");
	        // 第六步，将文件存到指定位置
	        String path = realPath+ymonth+"_"+sheetTitle+".xls";
	        try {
	        	FileOutputStream  fout = new FileOutputStream(path);
	            wb.write(fout);
	            fout.flush();
	            fout.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return ymonth+"_"+sheetTitle+".xls";
	    }
		
		public static String step4_export3( List data ,HttpServletRequest request ,String ymonth, String [] tabHead ,String sheetTitle,String[] val) {
	        // 第一步，创建一个webbook，对应一个Excel文件
	        HSSFWorkbook wb = new HSSFWorkbook();
	        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
	        HSSFSheet sheet = wb.createSheet(sheetTitle);
	        sheet.addMergedRegion(new CellRangeAddress(0,1,0,0));//
	        sheet.addMergedRegion(new CellRangeAddress(0,0,1,3));//
	       for(int i=0;i<7;i++) {
	    	   sheet.addMergedRegion(new CellRangeAddress(0,1,4+i,4+i));//
	       }
	        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
	        HSSFRow row = sheet.createRow((int) 0);
	        sheet.setDefaultColumnWidth(15); 
	        // 第四步，创建单元格，并设置值表头 设置表头居中s
	        HSSFCellStyle style = wb.createCellStyle();
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
	     
	        HSSFCell cell = null;
	        cell = row.createCell( 0);
	     			cell.setCellValue("洪号");
	     	        cell.setCellStyle(style);
	     	        cell = row.createCell( 1);
	     			cell.setCellValue("优选");
	     	        cell.setCellStyle(style);
	     	        cell = row.createCell( 4);
	     	      	cell.setCellValue("a>1/3amax");
	     	        cell.setCellStyle(style);
	     	        cell = row.createCell( 5);
	     			cell.setCellValue("m1查");
	     	        cell.setCellStyle(style);
	     	       cell = row.createCell( 6);
	     			cell.setCellValue("k计");
	     	        cell.setCellStyle(style);
	     	       cell = row.createCell( 7);
	     			cell.setCellValue("M计");
	     	        cell.setCellStyle(style);
	     	       cell = row.createCell( 8);
	     			cell.setCellValue("M查");
	     	        cell.setCellStyle(style);
	     	       cell = row.createCell( 9);
	     			cell.setCellValue("误差");
	     	        cell.setCellStyle(style);
	     	       cell = row.createCell( 10);
	     			cell.setCellValue("是否合格");
	     	        cell.setCellStyle(style);
	        row=sheet.createRow((int) 1);
	        for(int i=0;i<tabHead.length;i++) {
	        	cell = row.createCell( i);
	        	cell.setCellValue(tabHead[i]);
	        	 cell.setCellStyle(style);
	        }
	        style.setLocked(true);
	        
	        int i = 0;
	        try {
	        	for (Object obj : data) {
	        		Map map = null;
	        		if(obj instanceof Map){
	        			map = (Map)obj;
	        		}
	                row = sheet.createRow((int) i + 2);
	                int num = 0;
	                // 第四步，创建单元格，并设置值
	                for (String str : val) {
	                	Object object=map.get(str);
	                	if(null==object){
	                		row.createCell(num).setCellValue("");
	                	}else{
	                		row.createCell(num).setCellValue(object.toString());
	                	}
		                num++;
	                }
	                i++;
	        	}
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        String realPath = request.getSession().getServletContext().getRealPath("/");
	        // 第六步，将文件存到指定位置
	        String path = realPath+ymonth+"_"+sheetTitle+".xls";
	        try {
	        	FileOutputStream  fout = new FileOutputStream(path);
	            wb.write(fout);
	            fout.flush();
	            fout.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return ymonth+"_"+sheetTitle+".xls";
	    }
		
		
		public static String exportHlStep5t2( List data ,String n,HttpServletRequest request ,String ymonth, String [] tabHead ,String sheetTitle,String[] val) {
	        // 第一步，创建一个webbook，对应一个Excel文件
	        HSSFWorkbook wb = new HSSFWorkbook();
	        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
	        HSSFSheet sheet = wb.createSheet(sheetTitle);
	        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
	        HSSFRow row = sheet.createRow((int) 0);
	        sheet.setDefaultColumnWidth(15); 
	        // 第四步，创建单元格，并设置值表头 设置表头居中s
	        HSSFCellStyle style = wb.createCellStyle();
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
	     
	        HSSFCell cell = null;
	        cell = row.createCell( 9);
			cell.setCellValue("N="+n+"");
	        cell.setCellStyle(style);
	        for(int i=0;i<9;i++) {
	        	cell = row.createCell( i);
	        	cell.setCellValue(tabHead[i]);
	        	 cell.setCellStyle(style);
	        	 sheet.addMergedRegion(new CellRangeAddress(0,1,i,i));//合并
	        }
	        sheet.addMergedRegion(new CellRangeAddress(0,0,9,14));//合并
	        row = sheet.createRow((int) 1);
	        for(int i=0;i<tabHead.length;i++) {
	        	cell = row.createCell( i);
	        	cell.setCellValue(tabHead[i]);
	        	 cell.setCellStyle(style);
	        }
	        style.setLocked(true);
	        
	        int i = 0;
	        try {
	        	for (Object obj : data) {
	        		Map map = null;
	        		if(obj instanceof Map){
	        			map = (Map)obj;
	        		}
	                row = sheet.createRow((int) i + 2);
	                int num = 0;
	                // 第四步，创建单元格，并设置值
	                for (String str : val) {
	                	Object object=map.get(str);
	                	if(null==object){
	                		row.createCell(num).setCellValue("");
	                	}else if(str.equals("MJMC")){//是否合格
	                		if(Double.parseDouble(object.toString())>=0&&Double.parseDouble(object.toString())<=0.2) {
	                			row.createCell(num).setCellValue("合格");
	                		}else {
	                			row.createCell(num).setCellValue("不合格");
	                		}
	                	}else {
	                		row.createCell(num).setCellValue(object.toString());
	                	}
		                num++;
	                }
	                i++;
	        	}
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        String realPath = request.getSession().getServletContext().getRealPath("/");
	        // 第六步，将文件存到指定位置
	        String path = realPath+ymonth+"_"+sheetTitle+".xls";
	        try {
	        	FileOutputStream  fout = new FileOutputStream(path);
	            wb.write(fout);
	            fout.flush();
	            fout.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return ymonth+"_"+sheetTitle+".xls";
	    }
}
