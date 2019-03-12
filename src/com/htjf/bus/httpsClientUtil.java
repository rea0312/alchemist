package com.htjf.bus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * @author CZCheng
 * @date 2017-12-19 15:30:35
 * */
public class httpsClientUtil {
	
	private static Logger log = Logger.getLogger(httpsClientUtil.class);
	
	
	/**
	 *https Get请求
	 *@param url 请求链接
	 * */
	public static String doGet(String url) {
		String result = "";
		HttpClient httpClient = null;
		try {
			String urlNameString = url;
			HttpGet get = new HttpGet(urlNameString);

			// https免证书
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {return null;}
				public void checkClientTrusted(X509Certificate[] certs,String authType) {}
				public void checkServerTrusted(X509Certificate[] certs,String authType) {}
			} };
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, trustAllCerts, new SecureRandom());
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					sslcontext,SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

			// 发送请求
			HttpResponse response = httpClient.execute(get);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {  
            try {
            	// 关闭连接,释放资源
				httpClient.getConnectionManager().shutdown();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}  
        }  
		log.info(result);
		return result;
	}
	 
	 
	/**
	 * https 上传文件 
	 * @param Url 请求链接
	 * @param localFilePath 本地文件存放路径
	 * @param FieldName 文件名
	 * */
	public static String uploadFileImpl(String Url, String localFilePath,String FieldName) {
		String result = null;
		HttpClient httpClient = null;
		try {
			HttpPost httppost = new HttpPost(Url);
			FileBody binFileBody = new FileBody(new File(localFilePath+ File.separator + FieldName));

			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
			// add the file params
			multipartEntityBuilder.addPart(FieldName, binFileBody);
			// 设置上传的其他参数
			// setUploadParams(multipartEntityBuilder, params);
			HttpEntity reqEntity = multipartEntityBuilder.build();
			httppost.setEntity(reqEntity);

			// https免证书
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {return null;}
				public void checkClientTrusted(X509Certificate[] certs,String authType) {}
				public void checkServerTrusted(X509Certificate[] certs,String authType) {}
			} };
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, trustAllCerts, new SecureRandom());
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					sslcontext,SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

			HttpResponse response = httpClient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, "UTF-8");
				EntityUtils.consume(entity);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				// 关闭连接,释放资源
				httpClient.getConnectionManager().shutdown();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		log.info(result);
		return result;
	}
	 
	 /**
	  * https 下载文件
	  * @param url 请求链接
	  * @param filepath 文件存放路径
	  * */
	 public static  String downloadFileImpl(String url, String filepath) {
		HttpClient httpClient = null;
		String result = null;
		try {
			HttpGet httpget = new HttpGet(url);

			// https免证书
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {return null;}
				public void checkClientTrusted(X509Certificate[] certs,String authType) {}
				public void checkServerTrusted(X509Certificate[] certs,String authType) {}
			} };
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, trustAllCerts, new SecureRandom());
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					sslcontext,SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

			// 发送请求
			HttpResponse response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			log.info(response.toString());
			if(response.toString().contains("filename=")){
				// 文件名
				String filename = response.toString().split("filename=")[1].split(",")[0];

				InputStream is = entity.getContent();
				File file = new File(filepath + File.separator + filename);
				file.getParentFile().mkdirs();
				FileOutputStream fileout = new FileOutputStream(file);
				/**
				 * 根据实际运行效果 设置缓冲区大小
				 */
				byte[] buffer = new byte[1024];
				int ch = 0;
				while ((ch = is.read(buffer)) != -1) {
					fileout.write(buffer, 0, ch);
				}
				is.close();
				fileout.flush();
				fileout.close();
				result=filename;
			}
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally {  
            try {
            	// 关闭连接,释放资源
				httpClient.getConnectionManager().shutdown();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}  
        }  
		return result;
	}

	
	public static void main(String[] args) throws Exception {
		httpsClientUtil http = new httpsClientUtil();
		
//		http.doGet("https://wlc-alchemist.alibaba.com/select_hash?apikey=b1a082f31974fc4dd8fe3cfc11d976e4&datatype=apk");
//		File flie = new File("F:\\Data\\malw\\apk\\apk");
//		File[] fliename = flie.listFiles();
//		for (File file : fliename) {
//			log.info(file.getName());
//			http.uploadFileImpl("https://wlc-alchemist.alibaba.com/upload_sample?apikey=b1a082f31974fc4dd8fe3cfc11d976e4&datatype=apk","F:\\Data\\malw\\apk\\apk",file.getName());
//		}
//		BufferedReader reader = null;
//		InputStreamReader in =null;
//		List<String> malwareList = new ArrayList<String>();
//		try {
//			in=new InputStreamReader(new FileInputStream(flie),"UTF-8");
//			reader = new BufferedReader(in);		
//			String str = null;
//			
//			while ((str = reader.readLine()) != null) {
//				malwareList.add(str);
//			}
//			in.close();
//			reader.close();
//		}  catch (Exception e) {
//			log.error(e.getMessage(), e);
//		}
//		for (String string : malwareList) {
//			http.downloadFileImpl("https://wlc-alchemist.alibaba.com/download_sample?apikey=b1a082f31974fc4dd8fe3cfc11d976e4&datatype=apk&fileid=sss","F:\\Data\\malw\\txds\\apk");
//				
//		}
		
		
		
//		http.remindHttp();
		
		http.uploadFileImpl("https://wlc-alchemist.alibaba.com/upload_sample?apikey=b1a082f31974fc4dd8fe3cfc11d976e4&datatype=url","F:\\Data\\SampleShare\\upurl","20180209.rar");
		http.uploadFileImpl("https://wlc-alchemist.alibaba.com/upload_sample?apikey=b1a082f31974fc4dd8fe3cfc11d976e4&datatype=phone","F:\\Data\\SampleShare\\upphone","20180209.rar");
//		http.downloadFileImpl("https://wlc-alchemist.alibaba.com/download_sample?apikey=b1a082f31974fc4dd8fe3cfc11d976e4&datatype=url","F:\\Data\\malw\\txds");
//		http.downloadFileImpl("https://wlc-alchemist.alibaba.com/download_sample?apikey=b1a082f31974fc4dd8fe3cfc11d976e4&datatype=phone","F:\\Data\\malw\\txds\\5");
	}
	
}
