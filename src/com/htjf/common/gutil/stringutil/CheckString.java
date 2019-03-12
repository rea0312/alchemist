package com.htjf.common.gutil.stringutil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CheckString {
	
	public static String getParaToString(HttpServletRequest request,
			HttpServletResponse response, String para) {
		
		String paraValue = request.getParameter(para);
		if ("".equals(paraValue) || null == paraValue || "null".equals(paraValue)) {
			return "";
		} else {
			return paraValue.trim();
		}
	}
	
	public static int getParaToInteger(HttpServletRequest request,
			HttpServletResponse response, String para) {
		String paraValue = request.getParameter(para);
		if ("".equals(paraValue) || null == paraValue || "null".equals(paraValue)) {
			return -1;
		} else {
			if(isNum(paraValue.trim()))
				return Integer.parseInt(paraValue);
			else 
				return -1;
		}
	}

	public static int checkID(String s)
	{
	   if((s == null)||(s.length() == 0)||!s.matches("^[0-9]*$"))
	   {
	    return 0;
	   }
	   else
	   {
	    if(s.length() < 10)
	    {
	     return Integer.parseInt(s);
	      }
	      else
	      {
	        return 0;
	      } 
	   }
	}
	public static boolean isNum(String s)
	{
	   if((s == null)||(s.length() == 0)||!s.matches("^[0-9]*$"))
	   {
	    return false;
	   }
	   else
	   {
	    return true;
	   }
	}
	
	public static boolean isNumLetter(String s){
		if((s == null)||(s.length() == 0)||!s.matches("^[A-Za-z0-9]+$"))
		   {
		    return false;
		   }
		   else
		   {
		    return true;
		   }
	}
	
	/**
	 * 判断 para是否包含有空信息，有则返回true,否则返回false
	 * @param  para
	 * @return boolean
	 */
	public static boolean isLegal(Object para) {

		if (null == para || "".equals(para) || "null".equals(para)||" ".equals(para)) {
			return true;
		} else {
			return false;
		}
	}
	
}
