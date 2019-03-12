package com.htjf.common.gutil.dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.ProxoolFacade;
import org.logicalcobwebs.proxool.admin.SnapshotIF;
import org.logicalcobwebs.proxool.configuration.JAXPConfigurator;

import com.htjf.common.gutil.configutil.ConfigureUtil;
import com.htjf.common.gutil.logutil.LogInfoUtil;

/**
 * 
 * 数据库相关操作
 * @author QiuSH
 * 
 */
public class DBUtil {
	private static int activeCount = 0; 	
	public static int type = -1;
	
	/*
	private static String proxoolFile = "classes/config/proxool.xml"; 
	//private static String proxoolFileOracle = "proxool4oracle.xml"; 
	
	static{
		try {
         Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
			
			String path = DBUtil.class.getResource("/").getFile();
			JAXPConfigurator.configure(path + proxoolFile, false);
			
		} catch (Exception e) {
			LogInfoUtil.logError(e, "");
		}
	}
	*/
	
	static {
		try {
			Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
			String configPath = DBUtil.class.getResource("/config/proxool.xml").getFile();
			configPath = java.net.URLDecoder.decode(configPath, "utf-8");
			
			JAXPConfigurator.configure(configPath, false);
		} catch (ProxoolException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	/**
	 * @根据配置文件判断数据库
	 * @oracle 0 sqlserver 1
	 */
	/*private static int getDbType() {
		
		String db = new ConfigureUtil().getValue("current_db_connect");
		if ("oracle".equals(db)) {
			type = 0;
		}else if("sqlserver".equals(db)){
			type = 1;
		}
		return type;
	}*/

	private static void showSnapshotInfo(String str) {
		if (true) {
			try {
				SnapshotIF snapshot = ProxoolFacade.getSnapshot(str, true);
				int curActiveCount = snapshot.getActiveConnectionCount();// 获得活动连接数
				int availableCount = snapshot.getAvailableConnectionCount();// 获得可得到的连接数
				int maxCount = snapshot.getMaximumConnectionCount();// 获得总连接数
				if (curActiveCount != activeCount)// 当活动连接数变化时输出的信息
				{
					LogInfoUtil.systemout(str+"  ActiveCount:" + curActiveCount+ "(active)  availableCount:" + availableCount+ "(available)  maxCount:" + maxCount + "(max)","",true);
					activeCount = curActiveCount;
				}
			} catch (ProxoolException e) {
				LogInfoUtil.logError(e, "");
			}
		}
	}   
	
	
	public static Connection getjConnection() {
		String pool = "proxool.mmds";
		Connection con = null;
		try {
			con = DriverManager.getConnection(pool);
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return con;
	}
	
	/**
	 * 根据传入参数，创建数据库Connection
	 * @return
	 */
	public static Connection getDbConnection(String DbPrefix) {
		Connection conn = null;
		try {
			//获取数据库连接属性
			ConfigureUtil configUtil = new ConfigureUtil();
			String driver = configUtil.getValue(DbPrefix + "_driver");
			String url = configUtil.getValue(DbPrefix + "_url");
			String userName = configUtil.getValue(DbPrefix + "_username");
			String password  = configUtil.getValue(DbPrefix + "_password");
			
			//加载驱动
			Class.forName(driver);
			//创建连接
			conn = DriverManager.getConnection(url, userName, password);
		} catch (Exception e) {
			LogInfoUtil.logError(e, "");
		}
		return conn;
	}
	
	public static Connection getConnection(String pool) {
		Connection conn = null;
		try {
			conn = getjConnection();
		} catch (Exception e) {
			LogInfoUtil.logError(e, "");
		}
		return conn;
	}
	
	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = getjConnection();
		} catch (Exception e) {
			LogInfoUtil.logError(e, "");
		}
		return conn;
	}

	public static void close(Connection conn) throws SQLException {
		if (conn != null) {
			conn.close();
		}
	}

	public static void closeQuiet(Connection conn) {
		try {
			close(conn);
		} catch (Exception e) {
			LogInfoUtil.logError(e, "");
		}
	}

	public static void closeQuiet(ResultSet rs) {
		try {
			close(rs);
		} catch (Exception e) {
			LogInfoUtil.logError(e, "");
		}
	}

	public static void close(ResultSet rs) throws SQLException {
		if (rs != null) {
			rs.close();
		}
	}

	public static void closeQuiet(Statement stmt) {
		try {
			close(stmt);
		} catch (Exception e) {
			LogInfoUtil.logError(e, "");
		}
	}

	public static void close(Statement stmt){
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void closeQuiet(ResultSet rs, Statement stmt) {
		closeQuiet(rs);
		closeQuiet(stmt);
	}

	public static void close(ResultSet rs, Statement stmt) throws SQLException {
		close(rs);
		close(stmt);
	}

	public static void closeQuiet(Statement stmt, Connection conn) {
		closeQuiet(stmt);
		closeQuiet(conn);
	}

	public static void close(Statement stmt, Connection conn)
			throws SQLException {
		close(stmt);
		close(conn);
	}

	public static void closeQuiet(ResultSet rs, Statement stmt, Connection conn) {
		closeQuiet(rs);
		closeQuiet(stmt, conn);
	}
	
	public static void closeQuiet(ResultSet rs, Connection conn) {
		closeQuiet(rs);
		closeQuiet(conn);
	}

	public static void close(ResultSet rs, Statement stmt, Connection conn)
			throws SQLException {
		close(rs);
		close(stmt, conn);
	}

	/**
	 * 判断表是否存在
	 * @param tableName
	 * @return
	 */
	public static boolean hasTable(String tableName){
		Connection conn = null;
		PreparedStatement st = null;
		 ResultSet rs=null;
        String sql="SELECT TABLE_NAME FROM USER_TABLES WHERE TABLE_NAME=UPPER(?)";
        try {
        	conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			st = conn.prepareStatement(sql);
			st.setString(1, tableName);
			rs=st.executeQuery();
			if(null!=rs&&rs.next()){
				LogInfoUtil.systemout("**** table exits****",tableName);
				return true;
			}else{
				return false;
			}
        } catch (Exception e) {
        	LogInfoUtil.logError(e,"");
        	return false;
        }finally{
        	try {
				DBUtil.close(rs,st,conn);
			} catch (SQLException e) {
				LogInfoUtil.logError(e,"");
			}
        }
	}
	
}
