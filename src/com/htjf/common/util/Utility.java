package com.htjf.common.util;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用于日常的字符串操作、日期转换等工具
 * 
 * @author longyd 2011-08-04
 * @version 1.0
 */

public class Utility {
	/**
	 * 将一个字符转换成当前环境编码（GB2312）的字符串
	 * 
	 * @param szStr
	 *            字符串
	 * @return 字符串
	 */
	public static String toGB2312(String szStr) {
		if (szStr == null)
			return null;
		char[] chr = new char[szStr.length()];
		szStr.getChars(0, szStr.length(), chr, 0);
		byte[] b = new byte[chr.length];
		for (int i = 0; i < chr.length; i++) {
			b[i] = (byte) chr[i];
		}
		return (new String(b));
	}

	/**
	 * 将一个字符转换成iso-8859-1编码的字符串
	 * 
	 * @param szStr
	 *            字符串
	 * @return 字符串
	 */
	public static String toISO8859(String szStr) {
		if (szStr == null)
			return null;
		byte[] dbByte = szStr.getBytes();
		try {
			return (new String(dbByte, "iso-8859-1"));
		} catch (Exception e) {
			return null;
		}
	}
	//把url中的中参数（以iso-8859-1编码）转换成UTF-8,即还原为中文
	//
	public static String iso8859ToUTF_8(String szStr) {
		if (szStr == null)
			return null;
		byte[] dbByte =null;
		try{
			dbByte = szStr.getBytes("iso-8859-1");
		}catch(Exception e){
			System.out.println("exception");
		}
		
		try {
			return (new String(dbByte, "UTF-8"));
		} catch (Exception e) {
			return null;
		}
	}
	//把url中的中参数（以ASCII编码）转换成UTF-8,即还原为中文
	//
	public static String asciiToUTF_8(String szStr) {
		if (szStr == null)
			return null;
		byte[] dbByte =null;
		try{
			dbByte = szStr.getBytes("ASCII");
		}catch(Exception e){
			System.out.println("exception");
		}
		
		try {
			return (new String(dbByte, "UTF-8"));
		} catch (Exception e) {
			return null;
		}
	}
	//把url中的中参数（以GB2312编码）转换成UTF-8,即还原为中文
	//
	public static String ma1ToMa2(String szStr,String ma1,String ma2) {
		if (szStr == null)
			return null;
		byte[] dbByte =null;
		try{
			dbByte = szStr.getBytes(ma1);
		}catch(Exception e){
			System.out.println("exception");
		}
		
		try {
			return (new String(dbByte, ma2));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获得整型绝对值中(二进制)最高位为1的位置数值,由0开始;
	 * 
	 * @param intIn
	 *            要转换的十进制数
	 * @return 返回二进制的位数,0就返回-1
	 */
	public static int getMaxBitIndex(int intIn) {
		int iBit = 0;
		if (intIn == 0)
			return -1; // **** 如果参数为0,就当是-1位处理
		intIn = Math.abs(intIn);
		for (int i = 1; intIn >= 2; i++) {
			intIn = intIn / 2;
			iBit = i;
		}
		return iBit;
	}

	public static int stringToInteger(String str) {
		if (isEmpty(str))
			return Integer.MIN_VALUE;

		str = str.trim();
		if (!isInteger(str))
			return Integer.MIN_VALUE;

		return (new Integer(str.trim())).intValue();
	}
	
	public static long stringToLong(String str) {
		if (isEmpty(str))
			return Long.MIN_VALUE;

		str = str.trim();
		if (!isLong(str))
			return Long.MIN_VALUE;

		return (new Long(str.trim())).longValue();
	}
	
	public static float stringToFloat(String str){
		if (isEmpty(str)) return Float.MIN_VALUE;
		
		str = str.trim();
		if(!isFloat(str))
			return Float.MAX_VALUE;
		
		return new Float(str.trim()).floatValue();
	}

	/**
	 * 把布尔类型的数据转为整数，但值只有1和0，以适用Sybase数据库的Bit类型（一般用于Sybase数据库SQL语句中）
	 * 
	 * @param bValue
	 *            要转换的boolean值
	 * @return int 转换后的整数
	 */
	public static int booleanToBit(boolean bValue) {
		if (bValue)
			return 1;
		else
			return 0;
	}

	/**
	 * 将整数类型转为boolean类型（一般用于获取Sybase数据库的Bit类型值）
	 */
	public static boolean bitToBoolean(int iBitValue) throws Exception {
		if (iBitValue == 1)
			return true;
		else if (iBitValue == 0)
			return false;
		else
			throw new Exception("DataType.bitToBoolean()参数不合法");
	}

	public static java.util.Date stringToUtilDate(String aValue) {
		if (!isDate(aValue))
			return null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd H:mm");// .getDateInstance();
		// df.setLenient(false);
		java.util.Date date = null;
		try {
			date = df.parse(aValue.trim());
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		return date;
	}
	
	public static java.util.Date stringToUtilDate(String aValue,String format) {
		 
		SimpleDateFormat df = new SimpleDateFormat(format);
		java.util.Date date = null;
		try {
			date = df.parse(aValue.trim());
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		return date;
	}	

	public static java.sql.Date stringToSqlDate(String aValue) {
		java.util.Date date =stringToUtilDate(aValue);
		return (new java.sql.Date(date.getTime()));
	}

	public static String timeToString_24h(java.util.Date utilDate,
			String sepaChar) {
		if (utilDate == null)
			return null;
		if (sepaChar == null)
			sepaChar = ".";
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy" + sepaChar
				+ "MM" + sepaChar + "dd H:mm:ss");
		return dateFormate.format(utilDate);
	}

	public static String toHour_24h(java.util.Date utilDate) {
		if (utilDate == null)
			return null;
		SimpleDateFormat dateFormate = new SimpleDateFormat("H:mm:ss");
		return dateFormate.format(utilDate);
	}

	public static String dateToString(java.util.Date utilDate) {
		if (utilDate == null)
			return null;

		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy年MM月dd日");
		return dateFormate.format(utilDate);
	}

	
	/**
	 * 
	 * @param utilDate
	 * @param mark 日期表达式
	 *            
	 * @return
	 */
	public static String dateToString(java.util.Date utilDate, String mark) {
	 
		SimpleDateFormat dateFormate = new SimpleDateFormat(mark);
		return dateFormate.format(utilDate);
	}
	

	
	public static String dateToLongString(java.util.Date utilDate) {
		 String sepaChar = "-";
		if (utilDate == null)
			return null;
		if (sepaChar == null)
			sepaChar = ".";
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy" + sepaChar
				+ "MM" + sepaChar + "dd HH:mm");
		return dateFormate.format(utilDate);
	}

	public static String dateToSimpleString(java.util.Date utilDate) {
		if (utilDate == null)
			return null;
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormate.format(utilDate);
	}
	
	/**
	 * 将日期转为字段字符串(加上单引号)
	 * 
	 * @param utilDate
	 * @param sepaChar
	 * @return
	 */
	public static String dateToDBColumnStr(java.util.Date utilDate,
			String sepaChar) {
		if (utilDate == null)
			return null;

		if (sepaChar == null)
			sepaChar = ".";
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy" + sepaChar
				+ "MM" + sepaChar + "dd");
		return ("'" + dateFormate.format(utilDate) + "'");
	}

	/**
	 * 返回指定生日的年龄（周岁）
	 * 
	 * @param timestamp
	 * @return
	 */
	public static int getAge(java.util.Date timestamp) {
		Calendar nowCal = Calendar.getInstance();
		int nowY = nowCal.get(Calendar.YEAR);

		Calendar birthCal = Calendar.getInstance();
		birthCal.setTime(timestamp);
		int birthY = birthCal.get(Calendar.YEAR);

		return nowY - birthY;
	}

	/**
	 * 判断字符串是否为空字符串
	 * 
	 * @param szStr
	 *            字符串
	 * @return true null、空字符串、全部为空格；false 不是空字符串
	 */
	public static boolean isEmpty(String szStr) {
		if (szStr == null)
			return true;
		szStr = szStr.trim();
		if (szStr.length() < 1)
			return true;
		return false;
	}

	/**
	 * 删除字符串前后空格
	 * 
	 * @param szStr
	 *            字符串
	 * @return 删除前后空格的字符串；null 参数为null
	 */
	public static String trim(String szStr) {
		if (szStr == null)
			return null;
		return szStr.trim();
	}

	/**
	 * 判别字符串是否为Null
	 * 
	 * @param szStr
	 *            字符串
	 * @return boolean true:为Null值;false:不为Null值
	 */
	public static boolean isNull(String strStr) {
		if (strStr == null)
			return true;
		else
			return false;
	}

	/**
	 * 转换字符串中的所有单引号(')为(\'),目的是为了配合JavaScipt的输出
	 * 
	 * @param 待转换的字符串
	 * @Return 转换后的字符串
	 */
	public static String parseJavaScriptQuotes(String strString) {
		if (isEmpty(strString))
			return strString;

		String strNewString = "";

		for (int i = 0; i < strString.length(); i++) {
			if (strString.charAt(i) == 39)
				strNewString += "\\'"; // 转换成\'
			else if (strString.charAt(i) == 34)
				strNewString += "\\\""; // 转换成\"
			else
				strNewString += strString.charAt(i); // 保持不变
		}

		return strNewString;
	}

	/**
	 * 判断输入的参数是否是Boolean类型
	 * 
	 * @param strValue
	 *            输入参数
	 * @return boolean True:不是Boolean类型;False:是Boolean类型
	 */
	public static boolean isBoolean(String strValue) {
		if (isEmpty(strValue))
			return false;

		strValue = strValue.trim();
		if (strValue.toLowerCase().equals("false")
				|| strValue.toLowerCase().equals("true"))
			return true;
		else
			return false;
	}

	/**
	 * 判断输入的参数是否是字节类型
	 * 
	 * @param strValue
	 *            输入参数 （范围 ：-128< 参数值 <127)
	 * @return boolean True:不是Byte类型;False:是Byte类型
	 */
	public static boolean isByte(String strValue) {
		if (isEmpty(strValue))
			return false;

		try {
			Byte.valueOf(strValue.trim());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 判断输入的参数是否是Short类型
	 * 
	 * @param strValue
	 *            输入参数
	 * @return boolean True:不是Short类型;False:是Short类型
	 */
	public static boolean isShort(String strValue) {
		if (isEmpty(strValue))
			return false;

		try {
			Short.valueOf(strValue.trim());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 判断输入的参数是否是Integer类型
	 * 
	 * @param strValue
	 *            输入参数
	 * @return boolean True:不是Integer类型;False:是Integer类型
	 */
	public static boolean isInteger(String strValue) {
		if (isEmpty(strValue))
			return false;

		try {
			Integer.valueOf(strValue.trim());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 判断输入的参数是否是Long类型
	 * 
	 * @param strValue
	 *            输入参数
	 * @return boolean True:不是Long类型;False:是Long类型
	 */
	public static boolean isLong(String strValue) {
		if (isEmpty(strValue))
			return false;

		try {
			Long.valueOf(strValue.trim());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public static boolean isLong2(String strValue) {
		if (isEmpty(strValue))
			return false;

		try {
			Long.valueOf(strValue);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 判断输入的参数是否是Float类型
	 * 
	 * @param strValue
	 *            输入参数
	 * @return boolean True:不是Float类型;False:是Float类型
	 */
	public static boolean isFloat(String strValue) {
		if (isEmpty(strValue))
			return false;

		try {
			Float.valueOf(strValue.trim());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 判断输入的参数是否是double类型
	 * 
	 * @param strValue
	 *            输入参数
	 * @return boolean True:不是Double类型;False:是Double类型
	 */
	public static boolean isDouble(String strValue) {
		if (isEmpty(strValue))
			return false;

		try {
			Double.valueOf(strValue.trim());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 判断输入的参数是否为null
	 * 
	 * @param strValue
	 *            输入参数
	 * @return boolean True:不是String类型;False:是String类型
	 */
	public static boolean isString(String strValue) {
		if (isEmpty(strValue))
			return false;
		else
			return true;
	}

	/**
	 * 判断输入的参数是否是Date类型
	 * 
	 * @param strValue
	 *            输入参数
	 * @return boolean True:不是Date类型;False:是Date类型
	 */
	public static boolean isDate(String strValue) {
		if (isEmpty(strValue))
			return false;

		try {
			DateFormat df = DateFormat.getDateInstance();
			Date date = df.parse(strValue.trim());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 判断输入的参数是否是File类型
	 * 
	 * @param strValue
	 *            输入参数
	 * @return boolean True:不是File类型;False:是File类型
	 */
	public static boolean isFile(String strValue) {
		if (isEmpty(strValue))
			return false;
		return true;

	}

	/**
	 * 将字符串分裂成两个字符串,与分隔符相同的字符自动去掉
	 * 
	 * @param aStr
	 * @param splitChar
	 * @return
	 */

	public static String[] split(String aStr, String splitChar) {

		ArrayList v = splitToList(aStr, splitChar);

		String[] array = new String[v.size()];
		for (int i = 0; i < v.size(); i++) {
			String temp = v.get(i).toString();
			if (temp.equals(splitChar))
				continue;
			else
				array[i] = temp;
		}

		return array;
	}

	public static ArrayList splitToList(String aStr, String splitChar) {
		String theStr = aStr;
		if ((aStr == null) || aStr.equals(""))
			return null;

		int splitCharLen = splitChar.length();

		ArrayList v = new ArrayList();
		while (true) {
			int index = theStr.indexOf(splitChar);

			if (index == -1)
				break;

			String one = theStr.substring(0, index);
			// 与分隔符相同的字符自动去掉
			// System.out.println("one:" + one);
			// System.out.println("splitChar" + splitChar);
			if (one.length() > 0 && !one.equals(splitChar))
				v.add(one.trim());

			theStr = theStr.substring(index + splitCharLen);
		}

		if (theStr.length() > 0 && !theStr.equals(splitChar))
			v.add(theStr.trim());
		// System.out.println("theStr:" + theStr);

		return v;
	}

	public static List arrayToList(String[] ss) {
		if (ss == null)
			return new ArrayList();
		ArrayList list = new ArrayList();
		for (int i = 0; i < ss.length; i++) {
			list.add(ss[i].trim());
		}

		return list;
	}

	/**
	 * 返回字符串长度,一个中文字返回2,英文字符就返回1
	 * 
	 * @param strString
	 *            输入参数
	 * @return int 返回字符串长度,如果字符串是null返回0.
	 */
	public static int getStringLength(String strString) {
		if (strString == null)
			return 0;
		return strString.getBytes().length;
	}

	/**
	 * 利用反射功能,调用对象中的方法(方法名为字符串)
	 * 
	 * @param aObj 对象名
	 * @param aMethodName 方法名
	 * @param classes 参数类名
	 * @param objs 参数值
	 * @return
	 */
	public static Object invokeMethod(Object aObj, String aMethodName,
			Class[] classes, Object[] objs) {
		Class aTextClass = aObj.getClass();
		Method aMethod = null;
		try {
			aMethod = aTextClass.getMethod(aMethodName, classes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Object result = null;
		try {
			result = aMethod.invoke(aObj, objs);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 将条件与原语句联接(用PrepareStatement方式)
	 * 
	 * @param hasWhereFlag
	 *            是否已有 WHERE 标识
	 * @param origiSQL
	 * @param wheres
	 *            包含着每个查询条件,每个条件占用一个ArrayList,ArrayList中每个元素如下: 0:字段名 1:字段值(条件)
	 *            2:联接的方式,如"like","=",">"等 3:字段值(条件)的范围符,如单引号,双引号,或为空
	 *            4:条件运算符,如"and","or"
	 * @return
	 */
	public static String joinWhere(boolean hasWhereTag, String sql,
			ArrayList wheres) {
		// System.out.println("----origin sql----:"+ sql);

		boolean isFirstFilter = false; // 是否为第一个条件

		if (!hasWhereTag) {
			isFirstFilter = true;
			sql += " where ";
		}

		for (int i = 0; i < wheres.size(); i++) {
			ArrayList aWhere = (ArrayList) wheres.get(i);
			// System.out.println("----joinWhere----:" +
			// aWhere.get(i).toString());
			if (aWhere.size() < 4)
				System.out.println("----joinWhere----:参数不合法");
			// 如果值为空则不加入sql语句
			if (aWhere.get(1) == null)
				continue;
			else if (aWhere.get(1) != null
					&& aWhere.get(1).toString().equals(""))
				continue;

			String arithTag = aWhere.get(4).toString(); // 运算符
			if (isFirstFilter)
				arithTag = ""; // 如果是第一个条件,则不加运算符
			sql += " " + arithTag + " " + aWhere.get(0).toString() + " "
					+ aWhere.get(2).toString() + " " + aWhere.get(3).toString()
					+ aWhere.get(1).toString() + aWhere.get(3).toString();

			isFirstFilter = false;
		}

		return sql;
	}

 

	/**
	 * 将列名截取到30个字符串以内
	 * 
	 * @param columnName
	 * @return
	 */
	public static String trimColumnName(String columnName,int lengths) {
		if (columnName.length() <= lengths)
			return columnName;
		else
			return columnName.substring(0, lengths);

	}

	public static ArrayList MapToArrayList(Map map) {
		ArrayList result = new ArrayList();
		Set set = map.keySet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			Object obj = map.get(it.next());
			result.add(obj);
		}
		return result;
	}

	public static String[] objectsToStrings(Object[] objs) {
		if (objs == null)
			return null;
		String[] strs = new String[objs.length];
		for (int i = 0; i < objs.length; i++) {
			strs[i] = (String) objs[i];
		}
		return strs;
	}

	public static String[] listToStrings(List list) {
		String[] strs = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			if (obj == null)
				strs[i] = null;
			else
				strs[i] = obj.toString();
		}
		return strs;
	}

	/**
	 * 从指定的字符串中提取第一部分的数字
	 * 
	 * @param str
	 * @return
	 */
	public static int distrillNum(String str) {
		if(str==null) return -1;
		str = str.trim();
		int len = str.length();
		String returnValue = "";
		for (int i = 0; i < len; i++) {
			char c = str.charAt(i);
			if (c >= 48 && c <= 57) {
				returnValue += c;
			}
		}

		if (returnValue.equals(""))
			return -1;
		else
			return (new Integer(returnValue)).intValue();
	}

	public static String arrayToString(String[] strs, String split) {
		if (strs == null)
			return null;
		String value = "";
		for (int i = 0; i < strs.length; i++) {
			value += strs[i];
			if (i != strs.length - 1)
				value += split;
		}

		return value;
	}

	/**
	 * 分裂成数组
	 * 
	 * @param str
	 * @param seperate
	 *            分隔符,可以是多种类型
	 * @return
	 */
	public static String[] regSplit(String str, String[] seperate) {
		// List list = new ArrayList();
		// String ss = "abc,ddd，444，";
		// Pattern p = Pattern.compile("[ab]");
		// Matcher m = p.matcher("aaaaab");
		// boolean b = m.matches();
		String[] rs = str.split("[,，]");
		for (int i = 0; i < rs.length; i++) {
			System.out.println("....." + rs[i]);
		}
		return rs;
	}

	/**
	 * 将11位手机号码的中间位数屏蔽
	 * @param usermobile
	 * @return
	 */
	public static String maskMobile(String usermobile){
		String maskmobile = "";
		if(usermobile!=null && !usermobile.equals("") && usermobile.length()>=11){
			maskmobile = usermobile.substring(0,4) + "****" + usermobile.substring(8);
		}
		else
			maskmobile = usermobile;
		
		return maskmobile;
		
	}
	
	/**
	 * 限制字符串长度
	 * @param str 要截取的字符串
	 * @param maxLen 最大英文长度
	 * @return 返回类似 "广东省..."
	 */
	public String limitLength(String str,int maxLen){
		if(str==null || str.equals("")) return "";
		byte[] b = str.getBytes();
		//int len = str.length(); 
		if(b.length>maxLen && b.length>3){
			int limitLen = maxLen -3; 
			int engLen = 0;			  //英文长度
			int chnLen = 0;			  //中文长度
			for(int i=0;i<str.length();i++){
				//Cstr.charAt(i);
			}
			String newStr = str.substring(0,maxLen-2);
			return newStr+"...";
		}
		else
			return str;
	}
	
	

	public static java.util.Date stringToDateWithSecond(String aValue) {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		java.util.Date date = null;
		try {
			date = df.parse(aValue.trim());
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		return date;
	}
	
	
	/**
	 * 时间宽度计划
	 * @param currDate 起始时间
	 * @param days 日期跨度
	 * @return
	 */
	public static java.util.Date parseDate(Date currDate,int days) {
		
		Calendar cal=Calendar.getInstance();
		cal.setTime(currDate);		
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
 
	}
	
	/**
	 * 检查数据单元是否存在
	 *
	 * @param array
	 * @param ele
	 * @return
	 * @author LongYd
	 */
	public static boolean isExists(String[] array,String ele){
		for(int i=0;i<array.length;i++){
			if(array[i].equals(ele)){
				return true;
			}
		}
		return false;
	}
	
	 /** 
     * MD5 加密 
     */  
    public static String getMD5Str(byte[] src) {  
        MessageDigest messageDigest = null;    
        try {  
            messageDigest = MessageDigest.getInstance("MD5");    
            messageDigest.reset();  
  
            messageDigest.update(src);  
        } catch (NoSuchAlgorithmException e) { 
            return src.toString();
        } 
  
        byte[] byteArray = messageDigest.digest();  
  
        StringBuffer md5StrBuff = new StringBuffer();  
  
        for (int i = 0; i < byteArray.length; i++) {              
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)  
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));  
            else  
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));  
        }  
  
        return md5StrBuff.toString();  
    } 
	
}

