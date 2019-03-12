package com.htjf.common.lutil.configutil;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * 资源路径配置
 * @author yxq
 * @date 2012-12-31
 */
public class LocalPathUtil {
	private static Logger log = Logger.getLogger(LocalPathUtil.class);
	private LocalPathUtil(){
		
	}
     //目录的相对父路径
	 //具体的资源名称拼接
	private static String imageParentPath="/common/images/";
	private static String cssParentPath="/common/css/";
	private static String jsParentPath="/common/js/";
	private static String fusionCharParent="/common/fusionChart/";
	/**
	 * getContentPath+/common/fusionChart/+resuoceName
	 * @param request
	 * @param resuoceName 具体的资源名称
	 * @return getContentPath+imagePath/cssParentPath/jsPath+resuoceName 完整路径
	 */
	public static String getImagePath(HttpServletRequest request,String resuoceName) {
		//日志打印
		return getContentPath(request)+imageParentPath+resuoceName;
	}
	public static String getCssPath(HttpServletRequest request,String resuoceName) {
		return getContentPath(request)+cssParentPath+resuoceName;
	}
	
	public static String getJsPath(HttpServletRequest request,String resuoceName) {
		return getContentPath(request)+jsParentPath+resuoceName;
	}
	public static String getFusionChar(HttpServletRequest request,String resuoceName){
		return getContentPath(request)+fusionCharParent+resuoceName;
	}
	public static String getImagePath(String resuoceName) {
		//日志打印
		return imageParentPath+resuoceName;
	}
	public static String getCssPath(String resuoceName) {
		return cssParentPath+resuoceName;
	}
	
	public static String getJsPath(String resuoceName) {
		return jsParentPath+resuoceName;
	}
	public static String getFusionChar(String resuoceName){
		return fusionCharParent+resuoceName;
	}
	/*
	 *resuoceName 完整资源路径  如：/common/images/page.gif
	 */
	public static String getResourePath(String resuoceName){
		return resuoceName;
	}
	public static String getResourePath(HttpServletRequest request,String resuoceName){
		return getContentPath(request)+resuoceName;
	}
	public static String getContentPath(HttpServletRequest request) {
		return request.getContextPath();
	}
	
}
