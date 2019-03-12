package com.htjf.bus;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.htjf.common.db.BaseDAO;
import com.htjf.common.util.CSVUtils;
import com.htjf.common.util.CfgUtil;
import com.htjf.common.util.DateUtil;
import com.htjf.common.util.ZipUtil;



/**
 *与阿里巴巴共享样本
 *malw_core_black 恶意黑名单
 *
 *mmds_core_alch_phone 阿里巴巴phone样本
 *mmds_core_alch_url 阿里巴巴url样本
 *mmds_core_alch_apk 阿里巴巴apk样本
 *mmds_core_alch_MD5 阿里巴巴apk样本MD5
 *mmds_core_alch_downMD5 阿里巴巴apk待下载样本MD5
 *
 *mmds_core_share_phone 共享phone样本
 *mmds_core_share_url 共享url样本
 *mmds_core_share_apk 共享apk样本
 *mmds_core_share_file 共享样本名
 *
 *prc_susp_file_virus 病毒样本表
 *
 *API Key: b1a082f31974fc4dd8fe3cfc11d976e4
 *API Secret: c5fbe30c259368685041b415dc9e4ad6
 *
 * @author CZCheng
 * @date 2017-12-19 15:30:38
 * */
public class SampleShare {
	private static Logger log = Logger.getLogger(SampleShare.class);
	
	private final static String PHONESAMPLE_UPLOAD_PATH = CfgUtil.getValue("PHONESAMPLE_UPLOAD_PATH");
	private final static String PHONESAMPLE_DOWNLOAD_PATH = CfgUtil.getValue("PHONESAMPLE_DOWNLOAD_PATH");
	
	private final static String URLSAMPLE_UPLOAD_PATH = CfgUtil.getValue("URLSAMPLE_UPLOAD_PATH");
	private final static String URLSAMPLE_DOWNLOAD_PATH = CfgUtil.getValue("URLSAMPLE_DOWNLOAD_PATH");
	
	private final static String APKSAMPLE_UPLOAD_PATH = CfgUtil.getValue("APKSAMPLE_UPLOAD_PATH");
	private final static String APKSAMPLE_DOWNLOAD_PATH = CfgUtil.getValue("APKSAMPLE_DOWNLOAD_PATH");
	
	private final static String APIKey = CfgUtil.getValue("APIKey");
	private final static String APISecret = CfgUtil.getValue("APISecret");
	
	private final static String UPLOAD_URL = CfgUtil.getValue("UPLOAD_URL");
	private final static String DOWNLAD_URL = CfgUtil.getValue("DOWNLAD_URL");
	private final static String SELECT_URL = CfgUtil.getValue("SELECT_URL");
	
	/**
	 * 获取阿里病毒样本MD5
	 * */
	private void getMD5() {
		log.info("开始获取阿里病毒样本MD5");
		
		String detele = "delete from mmds_core_alch_downMD5";
		
		String sql = "insert into mmds_core_alch_MD5(md5,datadate,insdate) values(?,to_date(?,'yyyymmdd'),SYSDATE)";
		String sql1 = "insert into mmds_core_alch_downMD5(md5,datadate,insdate) values(?,to_date(?,'yyyymmdd'),SYSDATE)";
		
		String sql2 = "delete from mmds_core_alch_downMD5 where md5 in (select md5 from mmds_core_alch_downMD5 a left join (select mdfive from prc_susp_file_virus) b "
				+ "on a.md5=b.mdfive where b.mdfive is not null)";
		
		String selectMD5 =SELECT_URL+"?apikey="+APIKey+"&datatype=apk";
		String result=httpsClientUtil.doGet(selectMD5);
		JSONObject json= JSONObject.fromObject(result);
		String message = json.get("message").toString();
		JSONObject payload= JSONObject.fromObject(json.get("payload"));
		JSONArray md5=JSONArray.fromObject(payload.get("md5"));
		
		try {
			if("Success".equals(message)){
				List<Object[]> paras=new ArrayList<Object[]>();
				for (int i = 0; i < md5.size(); i++) {
					String tmp[]=new String[2];
					tmp[0]=md5.get(i)+"";
					tmp[1]=DateUtil.getCurrentDateStr("yyyyMMdd");
					paras.add(tmp);
				}
				log.info("清理待下载MD5:"+BaseDAO.execute(detele));
				log.info("阿里病毒样本MD5:"+BaseDAO.batchExecuteSQL(sql, paras));
				int a = BaseDAO.batchExecuteSQL(sql1, paras);
				int b = BaseDAO.executeUpdate(sql2);
				log.info("待下载病毒样本MD5:"+(a-b));
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 恶意URL下载
	 * */
	private void downUrl() {
		log.info("开始下载恶意URL样本");
		String sql = "insert into mmds_core_alch_file(filename,type,insdate) values(?,'url',SYSDATE)";
		String downUrl =DOWNLAD_URL+"?apikey="+APIKey+"&datatype=url";
		String result=httpsClientUtil.downloadFileImpl(downUrl, URLSAMPLE_DOWNLOAD_PATH);
		try {
			if(result!=null){
				log.info("下载恶意URL样本"+result+"成功！");
				BaseDAO.executeUpdate(sql, result);
			}else{
				log.info("下载恶意URL样本"+result+"失败！");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 欺诈电话下载
	 * */
	private void downPhone() {
		log.info("开始下载欺诈电话样本");
		String sql = "insert into mmds_core_alch_file(filename,type,insdate) values(?,'phone',SYSDATE)";
		String downUrl =DOWNLAD_URL+"?apikey="+APIKey+"&datatype=phone";
		String result=httpsClientUtil.downloadFileImpl(downUrl, PHONESAMPLE_DOWNLOAD_PATH);
		try {
			if(result!=null){ 
				log.info("下载欺诈电话样本"+result+"成功！");
				BaseDAO.executeUpdate(sql, result);
			}else{
				log.info("下载欺诈电话样本"+result+"失败！");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 病毒样本下载
	 * */
	private void downApk() {
		log.info("开始下载病毒样本");
		String sql = "insert into mmds_core_alch_file(filename,type,insdate) values(?,'apk',SYSDATE)";
		String md5sql = "select md5 from mmds_core_alch_downMD5";
		try {
			List<Map<String, Object>> md5list = BaseDAO.query(md5sql);
			for (Map<String, Object> map : md5list) {
				String downApk =DOWNLAD_URL+"?apikey="+APIKey+"&datatype=apk&fileid="+map.get("md5");
				String result=httpsClientUtil.downloadFileImpl(downApk, APKSAMPLE_DOWNLOAD_PATH);
				if(result!=null){
					log.info("下载病毒样本"+result+"成功！");
					BaseDAO.executeUpdate(sql, result);
				}else{
					log.info("下载病毒样本"+result+"失败！");
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 恶意URL上传
	 * */
	private void UrlSample() {
		log.info("开始上传恶意URL");
		String date = DateUtil.getPreDateStr("yyyyMMdd");
		String uploadUrl =UPLOAD_URL+"?apikey="+APIKey+"&datatype=url";
		String sqldomain = "select domain as url,to_char(statdate,'yyyy-mm-dd')time from malw_core_black where (attr='3' or attr='4') and to_char(statdate,'yyyymmdd')='"+date+"'";
		String sqlurl = "select ori_url as url,to_char(statdate,'yyyy-mm-dd')time from malw_core_black where attr='2' and to_char(statdate,'yyyymmdd')='"+date+"'";
		
		String sql = "insert into mmds_core_share_url(url_domain,datadate,insdate) values(?,to_date(?,'yyyy-mm-dd'),SYSDATE)";
		String sql1 = "insert into mmds_core_share_file(filename,type,message,code,insdate) values(?,?,?,?,SYSDATE)";
		
		List<Map<String, Object>> dataList=new ArrayList<Map<String, Object>>();
		LinkedHashMap<String, Object> map=new LinkedHashMap<String, Object>();
		map.put("url", "url");
		map.put("time", "time");
		try {
			List<Map<String, Object>> domain = BaseDAO.query(sqldomain);
			log.info("domain:"+domain.size());
			List<Map<String, Object>> url = BaseDAO.query(sqlurl);
			log.info("url:"+url.size());
			dataList.addAll(domain);
			dataList.addAll(url);
			log.info("dataList:"+dataList.size());
			File urlSample = CSVUtils.createCSVFile(dataList, map, URLSAMPLE_UPLOAD_PATH, "URL_"+DateUtil.getCurrentDateStr("yyyyMMdd"));
			//压缩样本
			ZipUtil.DeflateFileEncryption(URLSAMPLE_UPLOAD_PATH,date+".zip",urlSample.toString(),APISecret);
			//上传样本
			String result = httpsClientUtil.uploadFileImpl(uploadUrl,URLSAMPLE_UPLOAD_PATH,date+".zip");
			
			JSONObject json= JSONObject.fromObject(result);
			String message = json.get("message").toString();
			JSONObject payload= JSONObject.fromObject(json.get("payload"));
			JSONArray results=JSONArray.fromObject(payload.get("results"));
			String code = JSONObject.fromObject(results.get(0)).get("code").toString();
			if("Success".equals(message)){
				List<Object[]> paras=new ArrayList<Object[]>();
				for (Map<String, Object> ph : dataList) {
					String tmp[]=new String[2];
					tmp[0]=ph.get("url")+"";
					tmp[1]=ph.get("time")+"";
					paras.add(tmp);	
				}
				log.info("成功上传数据UrlSample："+BaseDAO.batchExecuteSQL(sql, paras));
				
			}
			BaseDAO.executeUpdate(sql1, date+".zip","url",message,code);
			
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 高频号码上传
	 * */
	private void PhoneSample() {
		log.info("开始上传高频号码");
		String date = DateUtil.getPreDateStr("yyyyMMdd");
		String uploadUrl =UPLOAD_URL+"?apikey="+APIKey+"&datatype=phone";
		String sql = "select num as Phone,to_char(filedate,'yyyy-mm-dd')time from  intercept_result_jx where to_char(filedate,'yyyymmdd')='"+date+"'";
		String sql1 = "insert into mmds_core_share_phone(phone,datadate,insdate) values(?,to_date(?,'yyyy-mm-dd'),SYSDATE)";
		String sql2 = "insert into mmds_core_share_file(filename,type,message,code,insdate) values(?,?,?,?,SYSDATE)";
		
		LinkedHashMap<String, Object> map=new LinkedHashMap<String, Object>();
		map.put("Phone", "Phone");
		map.put("time", "time");
		try {
			List<Map<String, Object>> phone = BaseDAO.query(sql);
			log.info("phone:"+phone.size());
			//生成样本
			File phoneSample = CSVUtils.createCSVFile(phone, map, PHONESAMPLE_UPLOAD_PATH, "Phone_"+DateUtil.getCurrentDateStr("yyyyMMdd"));
			//压缩样本
			ZipUtil.DeflateFileEncryption(PHONESAMPLE_UPLOAD_PATH,date+".zip",phoneSample.toString(),APISecret);
			//上传样本
			String result = httpsClientUtil.uploadFileImpl(uploadUrl,PHONESAMPLE_UPLOAD_PATH,date+".zip");
			
			JSONObject json= JSONObject.fromObject(result);
			String message = json.get("message").toString();
			JSONObject payload= JSONObject.fromObject(json.get("payload"));
			JSONArray results=JSONArray.fromObject(payload.get("results"));
			String code = JSONObject.fromObject(results.get(0)).get("code").toString();
			if("Success".equals(message)){
				List<Object[]> paras=new ArrayList<Object[]>();
				for (Map<String, Object> ph : phone) {
					String tmp[]=new String[2];
					tmp[0]=ph.get("Phone")+"";
					tmp[1]=ph.get("time")+"";
					paras.add(tmp);	
				}
				log.info("成功上传数据PhoneSample："+BaseDAO.batchExecuteSQL(sql1, paras));
				
			}
			BaseDAO.executeUpdate(sql2, date+".zip","Phone",message,code);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 病毒样本上传
	 * */
	private void ApkSample() {
		log.info("开始上传病毒样本");
		String sql2 = "insert into mmds_core_share_file(filename,type,message,code,insdate) values(?,?,?,?,SYSDATE)";
		
		File flie = new File(APKSAMPLE_UPLOAD_PATH);
		File[] fliename = flie.listFiles();
		for (File file : fliename) {
			log.info("开始上传:"+file.getName());
			String result = httpsClientUtil.uploadFileImpl(UPLOAD_URL+"?apikey="+APIKey+"&datatype=apk",APKSAMPLE_UPLOAD_PATH,file.getName());
			JSONObject json= JSONObject.fromObject(result);
			String message = json.get("message").toString();
			JSONObject payload= JSONObject.fromObject(json.get("payload"));
			JSONArray results=JSONArray.fromObject(payload.get("results"));
			String code = JSONObject.fromObject(results.get(0)).get("code").toString();
			
			if("Success".equals(message)){
				log.info("成功上传病毒样本:"+file.getName());
			}
			try {
				BaseDAO.executeUpdate(sql2, file.getName(),"apk",message,code);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		
	}
	
	protected void start() {
//		getMD5();
////		下载样本
		downUrl();
//		downPhone();
//		downApk();
		//上传样本
//		PhoneSample();
//		UrlSample();
//		ApkSample();
	}
	
	public static void main(String[] args) {
		log.info("阿里巴巴共享样本接口启动...");
		
//		Calendar calendar = Calendar.getInstance();
//	    calendar.add(Calendar.DAY_OF_MONTH,1);
//	    calendar.set(Calendar.HOUR_OF_DAY, 2);
//	    calendar.set(Calendar.MINUTE, 0);
//	    calendar.set(Calendar.SECOND, 0);
//	    
	    final SampleShare task = new SampleShare();
//	    log.info("启动时间："+calendar.getTime());
//	    TimerTask timerTask = new TimerTask() {
//	    	
//	    	@Override
//	    	public void run() {
	    		task.start();
//	    	}
//	    };
//	    
//	    Timer timer =  new Timer();
//	    timer.schedule(timerTask, calendar.getTime(),  24*60*60*1000);
	}

}
