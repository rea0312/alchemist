package com.htjf.common.gutil.logutil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 日志输出
 *	
 */
public class LogInfoUtil {

	public static Log log = LogFactory.getLog(LogInfoUtil.class);
	
	private LogInfoUtil(){}
	
	public static void systemout(Object e,String classPath){
		systemout(e,classPath,true);
	}
	public static void systemout(Object e,String classPath,boolean isPrint){
		if(isPrint){
			log.info(classPath+"--"+e);
		}
	}
	public static void logError(Exception e,String classPath){
		log.error(classPath,e);
	}
	
	public static void debug(String classPath){
		log.debug(classPath);
	}
}
