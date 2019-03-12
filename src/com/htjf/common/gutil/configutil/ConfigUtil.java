package com.htjf.common.gutil.configutil;

import java.util.Map;

import com.htjf.common.gutil.logutil.LogInfoUtil;
import com.htjf.common.lutil.dom4j.XmlSupportImpl;

/**
 * @author qwz
 * 读取cfg.xml操作
 */
public class ConfigUtil {
	
	public static ConfigUtil instance = null;
	private Map configureMap = null;

	 private String filename = "config/cfg.xml";
	
	private ConfigUtil() {
		
		String file = this.getClass().getResource("/").getPath()+ filename;
		
		try {
			configureMap = new XmlSupportImpl().readDocument(file);
		} catch (Exception e) {
			LogInfoUtil.logError(e, "ConfigUtil");
		}
	}

	public static ConfigUtil getInstance() {
		if (null == instance) {
			synchronized (ConfigUtil.class) {
				if (null == instance) {
					instance = new ConfigUtil();
				}
			}
		}
		return instance;
	}

	
	public String getValue(String key) {
		return (String) configureMap.get(key);
	}

	public static void main(String[] argv) {
		System.out.println(ConfigUtil.getInstance().getValue("sync_data_path"));
	}

}
