package com.htjf.common.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.regex.Pattern;

 
public class StringUtil {
	final public static String DATE_FORMATE_ALL = "yyyy-MM-dd HH:mm:ss";

	final public static String DATE_FORMATE_DATE = "yyyy-MM-dd";

	/**
	 * 将字符串中所有的空格去掉
	 */
	public static String cutSpace(String str) {
		if (str == null)
			return null;
		char[] chr = str.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < chr.length; i++) {
			if (chr[i] != ' ' && chr[i] != '　' && chr[i] != ' ') {
				sb.append(chr[i]);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 截取一定长度字符串
	 */
	static public String subString(Object obj, int Start, int End)
			throws Exception {
		if (obj == null)
			return "";
		String str = obj.toString();
		if (Start < 0) {
			Start = 0;
		}
		if (End > str.length() - Start) {
			End = str.length() - Start;
		}

		char chrArry[] = str.toCharArray();
		char tempArry[] = new char[End - Start];
		int n = 0;
		for (int i = Start; i < End; i++) {
			tempArry[n] = chrArry[i];
			n++;
		}
		return new String(tempArry);
	}
	/**
	 * 判断字符串在另一字符串中出现的次数
	 */
	public static int countMatches(String str, String sub) {
		if (str == null)
			return 0;
		int count = 0;
		for (int idx = 0; (idx = str.indexOf(sub, idx)) != -1; idx += sub
				.length())
			count++;

		return count;
	}

	/**
	 * 去除右边的空格等特殊字符
	 */
	public static String rTrim(String source) {

		if (source == null || source.equalsIgnoreCase("")) {
			return source;
		} else {
			String flag = "";
			flag = source.substring(source.length() - 1);
			while (flag.equals("	") || flag.equals("\t") || flag.equals("\r")
					|| flag.equals("\n")) {
				source = source.substring(0, source.length() - 1);
				if (source.length() == 0) {
					break;
				} else {
					flag = source.substring(source.length() - 1);
				}
			}
		}
		return source;

	}

	/**
	 * 清除特特殊的符号"<",">","'"全部换在全角
	 * 
	 */
	public static String clearSpecialChar(String source) {
		String specila[] = { "<", ">", "'" };
		String tarchar[] = { "〈", "〉", "‘" };
		if (source != null && !source.equalsIgnoreCase("")) {
			for (int i = 0; i < specila.length; i++) {
				source = source.replaceAll(specila[i], tarchar[i]);

			}
		}
		return source;

	}

	/**
	 * 清除特特殊的符号"<",">","'",回车，和换行全部换在全角
	 * 
	 */
	public static String clearSpecialCharAll(String source) {
		String specila[] = { "<", ">", "'", "\r", "\n", "\t" };
		String tarchar[] = { "〈", "〉", "‘", "", "", "" };
		if (source != null && !source.equalsIgnoreCase("")) {
			for (int i = 0; i < specila.length; i++) {
				source = source.replaceAll(specila[i], tarchar[i]);

			}
		}
		return source;

	}

	/**
	 * 判断字符串是不是全是数字
	 * 
	 * @param strnum
	 *            要判断的字符串
	 * @return
	 */
	public static boolean isNumber(String strnum) {
		Pattern p = Pattern.compile("^\\d+$");
		return p.matcher(strnum).find();

	}


	/** 判断字符串是否空串 */
	public static boolean isEmpty(String str) {
		boolean rtn = false;
		if (str == null || str.equalsIgnoreCase("")
				|| str.equalsIgnoreCase("null")) {
			rtn = true;
		}
		return rtn;

	}

	/**
	 * 对空对象的处理 Add by qinzhangbo 2008-6-18
	 * 
	 * @param str
	 * @return
	 */
	public static String ifNull(Object obj) {

		if (obj == null || obj.equals("null") || obj.equals("NULL")) {
			return "";
		}
		return obj.toString().trim();		
	}
	
	
	/**转化编码为gb2312
	 * Add By qinzhangbo 2008-8-4
	 * @param src
	 * @return
	 */
	public static String togb312(String src)
    {
    	String tar=null;
    	try {
    		tar= new String(src.getBytes("ISO-8859-1"),"gb2312");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tar;
    }
	
	/**
	 * 
	 * Add by qinzhangbo 2008-6-20
	 * @param obj
	 * @return
	 */
	public static String ifNullToZero(Object obj) {
		if (obj == null || obj.equals(null)) {
			return "0";
		}
		return obj.toString();
	}
	
 
 
	
 
	
	/**取随机数
	 * Add By qinzhangbo 2008-7-30
	 * @param src
	 * @return
	 */
	public static int getRdm(int src)
	{	
		Random r=new Random(); 		
		int i=r.nextInt(src); 		
		return i;		
	}

	/**
	 * 处理字符串，如果字符串超过某个长度则截取一定长度的字符，后面用省略号替代。
	 * 
	 * @param str
	 *            原字符串
	 * @param i
	 *            截取多少个字符
	 * @return
	 */
	public static String tosubString(String str, int i) {
		int x = str.length();
		if (x > i) {
			String strtemp = str.substring(0, i);
			str = strtemp + "...";
		}
		return str;
	}

	 
	
 
	/**
	 * 去掉字符串最后的,
	 * @param string
	 * @return
	 */
	public static String removeLastComma(String string) {
		String returnString  = new String(string);
		if(returnString.endsWith(","))
			returnString = returnString.substring(0,returnString.length()-1);
		return returnString;
		
	}
	/**
	 * 将原始字节数据转为16进制字串
	 * @param s
	 * @return
	 * @throws IOException
	 */
	public static String toHexString(byte[] data) throws IOException {
		String str = "";
		for (int i = 0; i < data.length; i++) {
			String s4 = "0";
			 switch(data[i]&0xf0)
			 {
			 case 0x00: s4 = "0";break;
			 case 0x10: s4 = "1";break;
			 case 0x20: s4 = "2";break;
			 case 0x30: s4 = "3";break;
			 case 0x40: s4 = "4";break;
			 case 0x50: s4 = "5";break;
			 case 0x60: s4 = "6";break;
			 case 0x70: s4 = "7";break;
			 case 0x80: s4 = "8";break;
			 case 0x90: s4 = "9";break;
			 case 0xa0: s4 = "a";break;
			 case 0xb0: s4 = "b";break;
			 case 0xc0: s4 = "c";break;
			 case 0xd0: s4 = "d";break;
			 case 0xe0: s4 = "e";break;
			 case 0xf0: s4 = "f";break;
			 }
			 switch(data[i]&0x0f)
			 {
			 case 0x00: s4 += "0";break;
			 case 0x01: s4 += "1";break;
			 case 0x02: s4 += "2";break;
			 case 0x03: s4 += "3";break;
			 case 0x04: s4 += "4";break;
			 case 0x05: s4 += "5";break;
			 case 0x06: s4 += "6";break;
			 case 0x07: s4 += "7";break;
			 case 0x08: s4 += "8";break;
			 case 0x09: s4 += "9";break;
			 case 0x0a: s4 += "a";break;
			 case 0x0b: s4 += "b";break;
			 case 0x0c: s4 += "c";break;
			 case 0x0d: s4 += "d";break;
			 case 0x0e: s4 += "e";break;
			 case 0x0f: s4 += "f";break;
			 }
			str = str + s4;
		}
		return str;
	}
	
	/**
	 * 将16进制字串转为原始字节数据
	 **/
	public static byte[] toByteHexString(String s) {
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(
						s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return baKeyword;
	}
	/**
	 * 将16进制字串转为字串
	 **/
	public static String toStringHex(String s) {
		if(s.length()<2)
			return null;
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(
						s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "utf-8");// UTF-16le:Not
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}


		
}
