/**
 * ConfigureUtil.java 2009-5-5 下午04:54:23
 */
package com.htjf.common.gutil.configutil;

import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import bsh.This;

import com.htjf.common.gutil.logutil.LogInfoUtil;
import com.htjf.common.lutil.dom4j.XmlSupportImpl;

/**
 * @author aiyan
 * @version 1.0
 *	读取cfg.xml操作
 */
public class ConfigureUtil {
	// private Logger log = Logger.getLogger(ConfigureUtil.class);
	 
	    private String filename = "config/cfg.xml";

	    private Document doc = null;
	    
	    public static ConfigureUtil instance = null;
	    
	    private Map configureMap = null;
	    


	    public ConfigureUtil() {
	    	 String file = this.getClass().getClassLoader().getResource("/").getPath()+ filename;
	    	 //String file = ClassLoader.getSystemResource("").getPath()+ filename;
	    	try {
	    		configureMap = new XmlSupportImpl().readDocument(file);
			} catch (Exception e) {
				LogInfoUtil.logError(e, "ConfigureUtil");
			}
	    }

	    /*
	     * (non-Javadoc)
	     * @see proc.gmcc.sso.config.Configure#getValue(java.lang.String)
	     */
	    public String getValue(String key) {
	    	return (String) configureMap.get(key);
	    }
	    public static void main(String[] argv){
	    	System.out.println(new ConfigureUtil().getValue("sync_data_path"));
	    }


}
