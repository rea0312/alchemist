package com.htjf.common.helper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理类
 * @author LJ
 * @date 2010-12-16
 * Copyright 广东华工九方科技有限公司
 */
public class StringHelper {
	/**
	 * 给定正则表达式，判断字符串格式是否符合
	 * @param source
	 * @param regex
	 * @return
	 */
	public static boolean isRightFormat(String source, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(source);
		return match.matches();
	}
	
	/**
	 * mmds从url中提取特征</tr>
	 * mtk2.wapdfw.com 就得到 wapdfw.com 做为特征.
	 * @param url
	 * @return
	 */
	public static String getFeature(String url) {
		String feature = null;

		// ------修改后的正则表达式，by 小邓
		String regEx = "\\w+(-\\w+)*\\."//
				+ "(\\w+(-\\w+)*)(\\w+(-\\w+)*\\.)?"// .[a-z0-9]
				+ "(.com.cn|.com.hk|.com|.net|.cn|.org|.gov|.tv|.edu|.mil)" + "(\\:\\d*)?";// 端口
		// -------
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(url);
		if (m.find()) {
			feature = m.group(2) + m.group(6);// 对应修改后的正则表达式

		} else {
			regEx = "((\\d+)\\.(\\d+)\\.(\\d+))\\.(\\d+)";
			p = Pattern.compile(regEx);
			m = p.matcher(url);
			if (m.find()) {
				if (url.indexOf(":") != -1)
					feature = url.substring(0, url.indexOf(":"));
				else
					feature = url;

			} else {
				feature = url;// 当两种匹配都不通过时，就将原来URL直接返回做为特征域名。
			}
		}
		return feature;
	}

	/**
	 * 获取域名。
	 * @param url
	 * @return
	 */
	public static String getDomain(String url)
	{
		String domain = null;
		if(url==null)
			return "";
		try {
			domain = new URL(url).getHost();
		} catch (MalformedURLException e) {
		}
		if(domain == null)
			domain =url.substring(0,url.length()>48?48:url.length());
		return domain;
	}
}
