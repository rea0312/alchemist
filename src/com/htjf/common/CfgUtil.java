/**
 * CfgUtil.java 2010-6-29 上午09:47:05
 */
package com.htjf.common;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @author aiyan
 * @version 1.0
 * 
 */
public class CfgUtil {
	@SuppressWarnings("rawtypes")
	private static Map configureMap = null;
	static {
//		String file = ClassLoader.getSystemResource("")+"" ;
		String file = CfgUtil.class.getResource("/config/").getPath() + "cfg.xml";
		System.out.println(file);
		
//		String file = "/home/mmds/project/hn_engine/bin/cfg.xml";

		try {
			file = java.net.URLDecoder.decode(file, "utf-8");
			configureMap = readDocument(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getValue(String key) {
		return (String) configureMap.get(key);
	}

	public static String getString(String key, String defaultValue) {
		if (!configureMap.containsKey(key)) {
			return defaultValue;
		} else {
			return (String) configureMap.get(key);
		}
	}

	public static Integer getInt(String key, Integer defaultValue) {
		if (!configureMap.containsKey(key)) {
			return defaultValue;
		} else {
			try {
				return Integer.parseInt((String)(configureMap.get(key)));
			} catch (Exception ex) {
				return defaultValue;
			}
		}
	}

	public static Long getLong(String key, Long defaultValue) {
		if (!configureMap.containsKey(key)) {
			return defaultValue;
		} else {
			try {
				return (Long) configureMap.get(key);
			} catch (Exception ex) {
				return defaultValue;
			}

		}
	}

	public static Double getDouble(String key, Double defaultValue) {
		if (!configureMap.containsKey(key)) {
			return defaultValue;
		} else {
			try {
				return (Double) configureMap.get(key);
			} catch (Exception ex) {
				return defaultValue;
			}
		}
	}

	public static Boolean getBoolean(String key, Boolean defaultValue) {
		if (!configureMap.containsKey(key)) {
			return defaultValue;
		} else {
			try {
				String str=(configureMap.get(key)+"").toLowerCase();
				return Boolean.valueOf(str);				 
			} catch (Exception ex) {
				return defaultValue;
			}
		}
	}
	private static Map<String,String>  readDocument(String file) throws Exception{
		// TODO Auto-generated method stub
		Map<String,String> map = new HashMap<String,String>();
		SAXReader saxReader =  new SAXReader();

		/**
		 *  先通过 org.dom4j.io.SAXReader 读入整份XML文档
		 */
		Document document = saxReader.read(new FileInputStream(new File(file)));
		Element root = document.getRootElement();
		@SuppressWarnings("rawtypes")
		List list = root.elements();
		for(int i=0;i<list.size();i++){
			Element ele = (Element) list.get(i);
			map.put(ele.getName(), ele.getText());
		}
		return map;
	}
	
}
