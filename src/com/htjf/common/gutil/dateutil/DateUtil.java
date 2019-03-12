package com.htjf.common.gutil.dateutil;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import com.htjf.common.gutil.logutil.LogInfoUtil;

/***
 * 日期集合
 * 
 * @author LJP
 * 
 * 
 */
public class DateUtil {

	/**
	 * 日期转换工具
	 */

		public static final String C_DATE_DIVISION = "-";

		public static final String C_TIME_PATTON_DEFAULT = "yyyy-MM-dd HH:mm:ss";

		public static final String C_DATE_PATTON_DEFAULT = "yyyy-MM-dd";

		public static final String C_DATA_PATTON_YYYYMMDD = "yyyyMMdd";

		public static final String C_TIME_PATTON_HHMMSS = "HH:mm:ss";

		public static final int C_ONE_SECOND = 1000;

		public static final int C_ONE_MINUTE = 60 * C_ONE_SECOND;

		public static final int C_ONE_HOUR = 60 * C_ONE_MINUTE;

		public static final long C_ONE_DAY = 24 * C_ONE_HOUR;

		/**
		 * Return the current date
		 * 
		 * @return － DATE<br>
		 */
		public static Date getCurrentDate() {
			Calendar cal = Calendar.getInstance();
			Date currDate = cal.getTime();
			return currDate;
		}

		/**
		 * Return the current date string
		 * 
		 * @return － 产生的日期字符串<br>
		 */
		public static String getCurrentDateStr() {
			Calendar cal = Calendar.getInstance();
			Date currDate = cal.getTime();

			return format(currDate);
		}

		/**
		 * Return the current date in the specified format
		 * 
		 * @param strFormat
		 * @return
		 */
		public static String getCurrentDateStr(String strFormat) {
			Calendar cal = Calendar.getInstance();
			Date currDate = cal.getTime();

			return format(currDate, strFormat);
		}
		
		public static String getDateStr(String dateStr,String strFormat) {
			
			if(dateStr==null||dateStr.toLowerCase().equals("null")){
				return "";
			}
			Date date = DateUtil.parseDate(strFormat,dateStr);
			return format(date, strFormat);
		}

		/**
		 * Parse a string and return a date value
		 * 
		 * @param dateValue
		 * @return
		 * @throws Exception
		 */
		public static Date parseDate(String dateValue) {
			return parseDate(C_DATE_PATTON_DEFAULT, dateValue);
		}

		/**
		 * Parse a strign and return a datetime value
		 * 
		 * @param dateValue
		 * @return
		 */
		public static Date parseDateTime(String dateValue) {
			return parseDate(C_TIME_PATTON_DEFAULT, dateValue);
		}

		/**
		 * Parse a string and return the date value in the specified format
		 * 
		 * @param strFormat
		 * @param dateValue
		 * @return
		 * @throws ParseException
		 * @throws Exception
		 */
		public static Date parseDate(String strFormat, String dateValue) {
			if (dateValue == null)
				return null;

			if (strFormat == null)
				strFormat = C_TIME_PATTON_DEFAULT;

			SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);
			Date newDate = null;

			try {
				newDate = dateFormat.parse(dateValue);
			} catch (ParseException pe) {
				newDate = null;
			}

			return newDate;
		}

		/**
		 * 将Timestamp类型的日期转换为系统参数定义的格式的字符串。
		 * 
		 * @param aTs_Datetime
		 *            需要转换的日期。
		 * @return 转换后符合给定格式的日期字符串
		 */
		public static String format(Date aTs_Datetime) {
			return format(aTs_Datetime, C_DATE_PATTON_DEFAULT);
		}

		/**
		 * 将Timestamp类型的日期转换为系统参数定义的格式的字符串。
		 * 
		 * @param aTs_Datetime
		 *            需要转换的日期。
		 * @return 转换后符合给定格式的日期字符串
		 */
		public static String formatTime(Date aTs_Datetime) {
			return format(aTs_Datetime, C_TIME_PATTON_DEFAULT);
		}

		/**
		 * 将Date类型的日期转换为系统参数定义的格式的字符串。
		 * 
		 * @param aTs_Datetime
		 * @param as_Pattern
		 * @return
		 */
		public static String format(Date aTs_Datetime, String as_Pattern) {
			if (aTs_Datetime == null || as_Pattern == null)
				return null;

			SimpleDateFormat dateFromat = new SimpleDateFormat();
			dateFromat.applyPattern(as_Pattern);

			return dateFromat.format(aTs_Datetime);
		}

		/**
		 * @param aTs_Datetime
		 * @param as_Format
		 * @return
		 */
		public static String formatTime(Date aTs_Datetime, String as_Format) {
			if (aTs_Datetime == null || as_Format == null)
				return null;

			SimpleDateFormat dateFromat = new SimpleDateFormat();
			dateFromat.applyPattern(as_Format);

			return dateFromat.format(aTs_Datetime);
		}

		public static String getFormatTime(Date dateTime) {
			return formatTime(dateTime, C_TIME_PATTON_HHMMSS);
		}

		/**
		 * @param aTs_Datetime
		 * @param as_Pattern
		 * @return
		 */
		public static String format(Timestamp aTs_Datetime, String as_Pattern) {
			if (aTs_Datetime == null || as_Pattern == null)
				return null;

			SimpleDateFormat dateFromat = new SimpleDateFormat();
			dateFromat.applyPattern(as_Pattern);

			return dateFromat.format(aTs_Datetime);
		}

		/**
		 * 取得指定日期N天后的日期
		 * 
		 * @param date
		 * @param days
		 * @return
		 */
		public static Date addDays(Date date, int days) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);

			cal.add(Calendar.DAY_OF_MONTH, days);

			return cal.getTime();
		}
		public static Date addMonths(Date date, int months) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);

			cal.add(Calendar.MONTH, months);

			return cal.getTime();
		}
		
		/**
		 * 计算两个日期之间相差的天数
		 * 
		 * @param date1
		 * @param date2
		 * @return
		 */
		public static int daysBetween(Date date1, Date date2) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date1);
			long time1 = cal.getTimeInMillis();
			cal.setTime(date2);
			long time2 = cal.getTimeInMillis();
			long between_days = (time2 - time1) / (1000 * 3600 * 24);

			return Integer.parseInt(String.valueOf(between_days));
		}

		/**
		 * 计算当前日期相对于"1977-12-01"的天数
		 * 
		 * @param date
		 * @return
		 */
		public static long getRelativeDays(Date date) {
			Date relativeDate = DateUtil.parseDate("yyyy-MM-dd", "1977-12-01");

			return DateUtil.daysBetween(relativeDate, date);
		}

		public static Date getDateBeforTwelveMonth() {
			String date = "";
			Calendar cla = Calendar.getInstance();
			cla.setTime(getCurrentDate());
			int year = cla.get(Calendar.YEAR) - 1;
			int month = cla.get(Calendar.MONTH) + 1;
			if (month > 9) {
				date = String.valueOf(year) + C_DATE_DIVISION
						+ String.valueOf(month) + C_DATE_DIVISION + "01";
			} else {
				date = String.valueOf(year) + C_DATE_DIVISION + "0"
						+ String.valueOf(month) + C_DATE_DIVISION + "01";
			}

			Date dateBefore = parseDate(date);
			return dateBefore;
		}

		/**
		 * 传入时间字符串,加一天后返回Date
		 * 
		 * @param date
		 *            时间 格式 YYYY-MM-DD
		 * @return
		 */
		public static Date addDate(String date) {
			if (date == null) {
				return null;
			}

			Date tempDate = parseDate(C_DATE_PATTON_DEFAULT, date);
			String year = format(tempDate, "yyyy");
			String month = format(tempDate, "MM");
			String day = format(tempDate, "dd");

			GregorianCalendar calendar = new GregorianCalendar(Integer
					.parseInt(year), Integer.parseInt(month) - 1, Integer
					.parseInt(day));

			calendar.add(GregorianCalendar.DATE, 1);
			return calendar.getTime();
		}

//		下一天
		public static String nextDay(String currentDate, int i,String form) {
			Calendar c = Calendar.getInstance();
			if(currentDate.length()==8){//yyyyMMdd
				int y = Integer.parseInt(currentDate.substring(0, 4));
				int m = Integer.parseInt(currentDate.substring(4, 6));
				int d = Integer.parseInt(currentDate.substring(6, 8));
				c.set(Calendar.YEAR, y);
				c.set(Calendar.MONTH, m - 1);
				c.set(Calendar.DATE, d + i);
			}else if(currentDate.length()==4){//MMdd
				int m = Integer.parseInt(currentDate.substring(0, 2));
				int d = Integer.parseInt(currentDate.substring(2, 4));
				c.set(Calendar.MONTH, m - 1);
				c.set(Calendar.DATE, d + i);
			}
			return DateUtil.format(c.getTime(), form);
		}
		public static String preDay7(String currentDate, int i) {
			Calendar c = Calendar.getInstance();
			int y = Integer.parseInt(currentDate.substring(0, 4));
			int m = Integer.parseInt(currentDate.substring(4, 6));
			int d = Integer.parseInt(currentDate.substring(6, 8));
			c.set(Calendar.YEAR, y);
			c.set(Calendar.MONTH, m - 1);
			c.set(Calendar.DATE, d - i);

			return DateUtil.format(c.getTime(), "yyyy-MM-dd");
		}
		public static String preDay7_(String currentDate, int i) {
			Calendar c = Calendar.getInstance();
			int y = Integer.parseInt(currentDate.substring(0, 4));
			int m = Integer.parseInt(currentDate.substring(4, 6));
			int d = Integer.parseInt(currentDate.substring(6, 8));
			c.set(Calendar.YEAR, y);
			c.set(Calendar.MONTH, m - 1);
			c.set(Calendar.DATE, d - i);

			return DateUtil.format(c.getTime(), "yyyyMMdd");
		}
		/**
		 * 计算当前月份的之前日期
		 * DateUtil.preMonth()
		 * @param currentDate
		 * @param addMonth
		 * @return
		 * @return String
		 * Author：Liu Hong
		 * Nov 22, 2010 3:55:56 PM
		 */
		public static String preMonth(String currentDate,int addMonth) {
			Calendar c = Calendar.getInstance();
			int y = Integer.parseInt(currentDate.substring(0, 4));
			int m = Integer.parseInt(currentDate.substring(4, 6));
			int d = Integer.parseInt(currentDate.substring(6, 8));
			c.set(Calendar.YEAR, y);
			c.set(Calendar.MONTH, m - addMonth - 1);
			c.set(Calendar.DATE, d);

			return DateUtil.format(c.getTime(), "yyyy-MM-dd");
		}
		public static String yestedayString(String dayFormat) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -1);
			String yestedayDate = new SimpleDateFormat(dayFormat).format(calendar
					.getTime());
			return yestedayDate;
		}

		public static String preDayn(String dayFormat, int n) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -n);
			String day = new SimpleDateFormat(dayFormat).format(calendar.getTime());
			return day;
		}

		/**
		 * 格式化时间，返回一定格式的字符串
		 * 
		 * @param calendar
		 * @param formatStr
		 * @return
		 */
		public static String formatDate(Date date, String formatStr) {
			SimpleDateFormat format = new SimpleDateFormat(formatStr);
			return format.format(date);
		}
		/**
		 * 求指定日期的前n天的日期
		 * @param currentDate 指定日期
		 * @param i n
		 * @return 字符串格式的日期
		 */
		public static String preDay(String currentDate, int i,String format1,String format2) {
			// TODO Auto-generated method stub
			Calendar c = Calendar.getInstance();
			c.setTime(DateUtil.parseDate(format1, currentDate));
			c.add(Calendar.DATE, i);
			return DateUtil.format(c.getTime(), format2);
		}
		
		
		public static String getFormatStr(String srcStr,String srcFor,String dstFor) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(srcFor);
			Date date = null;
			try {
				date = dateFormat.parse(srcStr);
			} catch (ParseException e) {
				LogInfoUtil.logError(e, "DateUtil getFormatStr");
			}
			return DateUtil.format(date, dstFor);
		}
		/**
		 * 获取当前天的前n天的日期
		 */
		public static Object getPreXDate(int x, int type, String formatStr) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -x);
			
			Date currDate = cal.getTime();
			
			String resStr = format(currDate, formatStr);
			Date resDate = parse(resStr, formatStr);
			Calendar resCal = Calendar.getInstance();
			resCal.setTime(resDate);
			
			
			if (type == 1)
				return resCal;
			else if (type == 2)
				return resDate;
			else if (type == 3)
				return resStr;
			else
				return null;
		}
		
		public static Date parse(String dataStr, String pattern) {
			if (dataStr == null || pattern == null)
				return null;
			
			SimpleDateFormat dateFromat = new SimpleDateFormat(pattern);
			Date resDate = null;
			try {
				resDate = dateFromat.parse(dataStr);
			} catch (ParseException e) {
				LogInfoUtil.logError(e, "DateUtil parse");
			}
			return resDate;
		}
		/**
		 * 获取当前天的前一天日期
		 * */
		public static String getPreDateStr(String strFormat) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			Date currDate = cal.getTime();
			return format(currDate, strFormat);
		}
		
		
	public static int dayForWeek(String pTime) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(format.parse(pTime));
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}
/**
 * @Description: 某月的第一天
 * @param @param monthStr 年月或年月日    
 * @return  
 */
	public static String getMonthOfOne(String monthStr,String formatStr){
		  SimpleDateFormat format= new SimpleDateFormat(formatStr);
		    Calendar lastDate = Calendar.getInstance();
		    try {
				lastDate.setTime(format.parse(monthStr));
			} catch (ParseException e) {
				LogInfoUtil.logError(e,"DateUtil");
			}
		    lastDate.set(Calendar.DAY_OF_MONTH, 1);
		    monthStr = format.format(lastDate.getTime());
		    return monthStr;
	}
	
	public static Calendar stringToCalendar(String date, String format) {
		Calendar cal = Calendar.getInstance();
		try {
			SimpleDateFormat df = new SimpleDateFormat(format);
			Date dt = df.parse(date);
			cal.setTime(dt);
		} catch (ParseException e) {
		}
		return cal;
	}
	
	public static java.sql.Date excelToDate(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Date date1 = null;
		try {
			date1 = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		java.sql.Date sd = new java.sql.Date(date1.getTime());
		return sd;	
	}
	
	
	/**
	 * Excel2003 String类型转Date格式String
	 * @param strdate
	 * @return
	 */
	public static String excel2003DateForm(String strdate) {

		if(strdate==null || "".equals(strdate)){
			return "";
		}
		String str[] = strdate.split(" ");
		String str2[] = str[0].split("/");
		//String date = str2[]
		StringBuffer sb = new StringBuffer();
		for(int i=str2.length-1;i>=0;i--){
			if(str2[i].length()<2){
				str2[i]="0"+str2[i];
			}
			if( i==str2.length-1 && str2[i].length()<4){
				str2[i]="20"+str2[i];
			}
			sb=sb.append(str2[i]);
		}		
		return sb.toString();
}
	/**
	 * @Description: 某月的最后一天
	 * @param @param monthStr 年月或年月日    
	 * @return  
	 */
	public static String getMonthOfLast(String monthStr,String formatStr){
		  SimpleDateFormat formats = new SimpleDateFormat(formatStr);
		  Calendar lastDate = Calendar.getInstance();
		 
		  try {
			  	monthStr=format(formats.parse(monthStr),formatStr);
				lastDate.setTime(formats.parse(monthStr));
			} catch (ParseException e) {
				LogInfoUtil.logError(e,"DateUtil");
			}
		  lastDate.set(Calendar.DATE, 1);
		  lastDate.add(Calendar.MONTH, 1);
		  lastDate.add(Calendar.DATE, -1);
		  
		  monthStr = formats.format(lastDate.getTime());
		return monthStr;
	}
	
	/**
	 * 求指定日期的前n月的日期
	 * @param currentDate 指定日期
	 * @param i n
	 * @param format1 处理字符串格式
	 * @param format2 返回日期字符串格式
	 * @return 字符串格式的日期
	 */
	public static String preMonth(String currentDate, int i,String format1,String format2) {
		Calendar c = Calendar.getInstance();
		c.setTime(DateUtil.parseDate(format1, currentDate));
		c.add(Calendar.MONTH, i);
		return DateUtil.format(c.getTime(), format2);
	}
	public static void main(String[] args) {
		try {
			
			String s = "2013-05-06 16:08";
			String x=DateUtil.getMonthOfOne("20130701","yyyyMMdd");
			//String y=DateUtil.getMonthOfLast("201306","yyyyMMdd");
			System.out.println(x);
			System.out.println(preMonth("20130701", -1, "yyyyMMdd","yyyy-MM-dd"));
			System.out.println(preMonth("20130731", 3));
			System.out.println(getDateStr("2013-05-01","yyyy-MM"));
			System.out.println(preDay("20130715",1, "yyyyMMdd","yyyyMMdd"));
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}
	
	 /** 
	 * 取得当前日期是多少周 
	 * 
	 * @param date 
	 * @return 
	 */  
	 public static int getWeekOfYear(Date date) {  
		 Calendar c = new GregorianCalendar();  
		 c.setFirstDayOfWeek(Calendar.MONDAY);  
		 c.setMinimalDaysInFirstWeek(7);  
		 c.setTime (date);  
		  
		 return c.get(Calendar.WEEK_OF_YEAR);  
	 }  
	 
	 /** 
	  * 得到某一年周的总数 
	  * 
	  * @param year 
	  * @return 
	  */  
	  public static int getMaxWeekNumOfYear(int year) {  
		  Calendar c = new GregorianCalendar();  
		  c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);  
		   
		  return getWeekOfYear(c.getTime());  
	  }  
	  
	  /** 
	   * 取得指定日期所在周的第一天 
	   * 
	   * @param date 
	   * @return 
	   */  
	   public static Date getFirstDayOfWeek(Date date) {  
		   Calendar c = new GregorianCalendar();  
		   c.setFirstDayOfWeek(Calendar.MONDAY);  
		   c.setTime(date);  
		   c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday  
		   return c.getTime ();  
	   } 
	   
	   /** 
	    * 取得当前日期所在周的第一天 
	    * 
	    * @param date 
	    * @return 
	    */  
	    public static Date getFirstDayOfWeek() {  
		    Calendar c = new GregorianCalendar();  
		    c.setFirstDayOfWeek(Calendar.MONDAY);  
		    c.setTime(new Date());  
		    c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday  
		    return c.getTime ();  
	    } 
	    
	    /** 
	     * 得到某年某周的第一天 
	     * 
	     * @param year 
	     * @param week 
	     * @return 
	     */  
	     public static Date getFirstDayOfWeek(int year, int week) {  
		     Calendar c = new GregorianCalendar();  
		     c.set(Calendar.YEAR, year);  
		     c.set (Calendar.MONTH, Calendar.JANUARY);  
		     c.set(Calendar.DATE, 1);  
		      
		     Calendar cal = (GregorianCalendar) c.clone();  
		     cal.add(Calendar.DATE, week * 7);  
		      
		     return getFirstDayOfWeek(cal.getTime ());  
	     } 
	     
	     /**
	      * 传入格式为YYYY-MM-DD格式的String日期，判断相差的月份
	      * @param fromDate String
	      * @param toDate   String
	      * @return int
	      * @throws ParseException
	      */
	     public static int getBetweenMonth (String fromDate, String toDate) throws ParseException{

	         SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
	         Calendar c = Calendar.getInstance();
	         c.setTime(sdf.parse(toDate));
	         int year1 = c.get(Calendar.YEAR);
	         int month1 = c.get(Calendar.MONTH);
	          
	         c.setTime(sdf.parse(fromDate));
	         int year2 = c.get(Calendar.YEAR);
	         int month2 = c.get(Calendar.MONTH);
	          
	         int result;
	         if(year1 == year2) {
	             result = month1 - month2;
	         } else {
	             result = 12*(year1 - year2) + month1 - month2;
	         }

	    	 return result;
	    	 
	     }
}
