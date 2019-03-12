package com.htjf.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ToolUtil {

	/**
	 * 如果src大于0,返回src,否则返回target
	 * 
	 * @param src
	 * @param target
	 * @return
	 */
	public static int gtZero(int src, int target) {
		return src > 0 ? src : target;
	}

	/**
	 * 字符串转整型
	 * 
	 * @param src
	 * @param _default
	 *            转换失败时的默认值
	 * @return
	 */
	public static int convertInt(String src, int _default) {
		try {
			return Integer.parseInt(src);
		} catch (Exception e) {
			return _default;
		}
	}

	/**
	 * 字符串转double型
	 * 
	 * @param src
	 * @param _default
	 * @return
	 */
	public static double convertDouble(String src, int _default) {
		try {
			return Double.parseDouble(src);
		} catch (Exception e) {
			return _default;
		}
	}

	/**
	 * 获得当前日期时间的默认格式的字符串表示
	 * 
	 * @return
	 */
	public static String getDefaultDatetime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	/**
	 * 获得当前日期的默认格式的字符串表示
	 * 
	 * @return
	 */
	public static String getDefaultDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

	/**
	 * 字符串转为Date型
	 * 
	 * @param date,
	 *            格式为"yyyy-MM-dd"
	 * @return
	 */
	public static Date StringToDate(String date) {
		return StringToDate(date, "yyyy-MM-dd");
	}

	/**
	 * 字符串转为Date型
	 * 
	 * @param date
	 * @param formate
	 *            格式
	 * @return
	 */
	public static Date StringToDate(String date, String formate) {
		SimpleDateFormat formatter = new SimpleDateFormat(formate);
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			System.out.println("字符串 " + date + " 不能转换为Date类型,字符串格式为 " + formate
					+ ".");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获得当前日期的中文件字符串表示
	 * 
	 * @return
	 */
	public static String getDateCN() {
		return new SimpleDateFormat("yyyy年MM月dd日 EEE").format(new Date());
	}

	/**
	 * 判断字符串是否为NULL或空字串
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isEmpty(String src) {
		if (src == null || "".equals(src.trim()))
			return true;
		return false;
	}

	/**
	 * 判断两个对象是否相等(注:obj1和obj2都为null时也返回true)
	 * 
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	public static boolean isEquals(Object obj1, Object obj2) {
		if (obj1 == null) {
			if (obj2 == null)
				return true;
			return false;
		} else {
			return obj1.equals(obj2);
		}
	}

	/**
	 * 用with连接字符串数组成一个字符串
	 * 
	 * @param args
	 * @param with
	 * @return
	 */
	public static String concatWith(String[] args, String with) {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < args.length; i++) {
			s.append(args[i]);
			if (i < args.length - 1)
				s.append(with);
		}
		return s.toString();
	}

	/**
	 * 将字符串编码由8859_1转到GBK
	 * 
	 * @param src
	 * @return
	 */
	public static String convert8859ToGBK(String src) {
		return convertEncoding(src, "8859_1", "GBK");
	}

	/**
	 * 将字符串编码由GBK转到UTF-8
	 * 
	 * @param src
	 * @return
	 */
	public static String convertGBKToUTF8(String src) {
		return convertEncoding(src, "GBK", "UTF-8");
	}

	/**
	 * 转换字符串编码
	 * 
	 * @param src
	 *            源字符串
	 * @param srcEncoding
	 *            源字符串编码
	 * @param targetEncoding
	 *            目标字符串编码
	 * @return
	 */
	public static String convertEncoding(String src, String srcEncoding,
			String targetEncoding) {
		try {
			src = new String(src.getBytes(srcEncoding), targetEncoding);
		} catch (Exception e) {
		}
		return src;
	}

	/**
	 * 判断字符串数组tars中是否存在字符串src
	 * 
	 * @param src
	 * @param tars
	 * @return
	 */
	public static boolean isExist(String src, String[] tars) {
		for (int i = 0; i < tars.length; i++) {
			if (src.equals(tars[i]))
				return true;
		}
		return false;
	}

	/**
	 * 计算任意两个日期之间的天数
	 * 
	 * @param d1
	 * @param d2
	 * @return 返回天数
	 */
	public static int getDaysBetween(Calendar d1, Calendar d2) {
		if (d1.after(d2)) { // 保证d1的日期在前
			Calendar swap = d1;
			d1 = d2;
			d2 = swap;
		}

		int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
		int y2 = d2.get(Calendar.YEAR);
		int y1 = d1.get(Calendar.YEAR);
		if (y1 != y2) {
			d1 = (Calendar) d1.clone();
			do {
				days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
				d1.add(Calendar.YEAR, 1);
			} while (d1.get(Calendar.YEAR) != y2);
		}
		return days;
	}

	/**
	 * 计算两个时间之间相隔天数,传进Calendar对象
	 * 
	 * @param startday
	 *            开始时间
	 * @param endday
	 *            结束时间
	 * @return
	 */
	public static int getIntervalDays(Calendar startday, Calendar endday) {
		// 确保startday在endday之前
		if (startday.after(endday)) {
			Calendar cal = startday;
			startday = endday;
			endday = cal;
		}
		// 分别得到两个时间的毫秒数
		long sl = startday.getTimeInMillis();
		long el = endday.getTimeInMillis();

		long ei = el - sl;
		// 根据毫秒数计算间隔天数
		return (int) (ei / (1000 * 60 * 60 * 24));
	}

	/**
	 * 计算两个时间之间相隔天数,传进Date对象
	 * 
	 * @param startday
	 *            开始时间
	 * @param endday
	 *            结束时间
	 * @return
	 */
	public static int getIntervalDays(Date startday, Date endday) {
		// 确保startday在endday之前
		if (startday.after(endday)) {
			Date cal = startday;
			startday = endday;
			endday = cal;
		}
		// 分别得到两个时间的毫秒数
		long sl = startday.getTime();
		long el = endday.getTime();

		long ei = el - sl;
		// 根据毫秒数计算间隔天数
		return (int) (ei / (1000 * 60 * 60 * 24));
	}

}
