package com.htjf.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;



/**
 * CSV文件工具类
 * @author CZCheng
 * @date 2017-12-19 09:57:32
 * */
public class CSVUtils {
	private static Logger log = Logger.getLogger(CSVUtils.class);
	
	 /**
     * 生成为CVS文件
     * 
     * @param exportData 源数据List
     * @param map csv文件的列表头map
     * @param outPutPath 文件路径
     * @param fileName 文件名称头
     * @return
     */
    public static File createCSVFile(List<Map<String, Object>> exportData, LinkedHashMap map, String outPutPath, String fileName) {
        File csvFile = null;
        BufferedWriter csvFileOutputStream = null;
        List<String> head=new ArrayList<String>();

        try {
            File file = new File(outPutPath);
            if (!file.exists()) {
                file.mkdir();
            }
            // 定义文件名格式并创建
            csvFile = new File(outPutPath+File.separator+fileName+".csv");
            log.info("csvFile：" + csvFile);
            // UTF-8使正确读取分隔符","
            csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "UTF-8"));
            // 写入文件头部
            for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator.hasNext();) {
                java.util.Map.Entry propertyEntry = 
                        (java.util.Map.Entry) propertyIterator.next();
                head.add( propertyEntry.getValue() != null ?
                		""+ propertyEntry.getValue() : "");
                csvFileOutputStream.write(
                        "" + (String) propertyEntry.getValue() != null ?
                                (String) propertyEntry.getValue() : "" + "");
                if (propertyIterator.hasNext()) {
                    csvFileOutputStream.write(",");
                }
            }
            csvFileOutputStream.newLine();
            // 写入文件内容
            for (Map<String, Object> map2 : exportData) {
            	String tmpStr="";
				for (String string : head) {
					tmpStr+=map2.get(string)+",";
				}
				tmpStr=tmpStr.substring(0, tmpStr.length()-1);
				csvFileOutputStream.write(tmpStr);
				csvFileOutputStream.newLine();
			}
            csvFileOutputStream.flush();
        } catch (Exception e) {
        	log.error(e.getMessage(), e);
        } finally {
            try {
                csvFileOutputStream.close();
            } catch (IOException e) {
            	log.error(e.getMessage(), e);
            }
        }
        return csvFile;
    }
    
    /**
     * 生成为CVS文件
     * 
     * @param content 源数据 List<String[]>
     * @param header csv文件的列表头 String[]
     * @param outPutPath 文件路径
     * @param fileName 文件名称头
     * @return
     */
    public static void createCSVFile(List<String[]> content, String[] header, String outPutPath, String fileName){

        String filePath = outPutPath+File.separator+fileName+".csv";

        try {
            // 创建CSV写对象
            CsvWriter csvWriter = new CsvWriter(filePath,',', Charset.forName("GBK"));
            CsvWriter csvWriters = new CsvWriter(filePath);

            // 写表头
            csvWriter.writeRecord(header);
            //内容
            for (String[] string : content) {
            	csvWriter.writeRecord(string);
			}
            
            csvWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 读取CVS文件
     * 
     * @param filePath 文件路径
     * @return
     */
    public static List<String[]> readCSVFile(String filePath){
    	List<String[]> resultlist= new ArrayList<String[]>();
        try {
            // 创建CSV读对象
            CsvReader csvReader = new CsvReader(filePath,',',Charset.forName("UTF-8"));
            // 读表头
            csvReader.readHeaders();
            while (csvReader.readRecord()){
            	// 读一整行
            	String tmp[]=csvReader.getRawRecord().split("\\,");
            	resultlist.add(tmp);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
		return resultlist;
    }
    public static void main(String[] args) {
    	  File csvFile = null;
          BufferedWriter csvFileOutputStream = null;
          BufferedReader csvFileinputStream = null;
          try {
        	  csvFile =new File("F:\\Data\\test\\123.csv");
        	 List<String[]> aa = readCSVFile("F:\\Data\\test\\123.csv");
        	  //csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "UTF-8"));
        	  csvFileinputStream=new BufferedReader(new InputStreamReader(new FileInputStream(csvFile),"UTF-8"));
        	  String str = "";
        	  while ((str=csvFileinputStream.readLine())!=null) {
        		  log.info(str);
			}
        	  csvFileinputStream.close();      	  
        	  //log.info("csvFileOutputStream：" + csvFileOutputStream);
          }catch (Exception e) {
          	log.error(e.getMessage(), e);
          }
	}
}
