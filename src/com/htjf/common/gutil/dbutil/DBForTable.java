package com.htjf.common.gutil.dbutil;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.htjf.common.gutil.logutil.LogInfoUtil;

public class DBForTable {
	/**
	 * 执行存储过程
	 * @param month
	 * @return
	 * @throws SQLException
	 */
	public static boolean creatActiveMonthTable(String month) throws SQLException{
		boolean success=false;	    
	    Connection conn = null;
		conn = DBUtil.getConnection();
		CallableStatement proc = null;   		
		proc = conn.prepareCall("{ call PRO_ACTIVE_MONTH("+month+")  }"); 
		proc.execute(); 
		conn.close();
		success=true;
		return success;	   
	}
	/**
	 * 查询表是否已插入数据
	 * @param sql
	 * @return boolean 有数据：true 无数据：false
	 * @throws Exception
	 */
	public static boolean isExitsData(String tableName) throws Exception {
		List<String> firstColumnStrList = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExits=false;
		String sql="select count(*) as num from "+tableName;
		try {
			conn = DBUtil.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				isExits=rs.getInt("num")>0?true:false;
			}
		}catch (Exception e) {
			LogInfoUtil.logError(e,"");
		}
		finally {
			DBUtil.close(rs, ps, conn);
		}
		return isExits;
	}
	/**
	 * @Description: TODO 清空表数据
	 * @param @param tableName
	 * @param @return
	 * @param @throws Exception    
	 * @return  
	 * @throws
	 */
	public static boolean truncateTable(String tableName) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		boolean isClean=false;
		String sql="truncate table "+tableName;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			int i=ps.executeUpdate();
			isClean=i>=0?true:false;
			conn.commit();
		}catch (Exception e) {
			LogInfoUtil.logError(e,"");
		}
		finally {
			DBUtil.close(ps, conn);
		}
		return isClean;
	}
}
