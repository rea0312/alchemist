/**
 * DBUtil.java 2009-4-19 下午10:52:30
 */
package com.htjf.common.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.configuration.JAXPConfigurator;

/**
 * @author aiyan
 * @version 1.0
 * 
 */
public class DBUtil {
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
 

	public static Connection getConnection() {
		String pool = "proxool.mmds";
		Connection con = null;
		try {
			con = DriverManager.getConnection(pool);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}

	
	public static Connection getLongConnection() {
		String pool = "proxool.mmds";
		Connection con = null;
		try {
			con = DriverManager.getConnection(pool);
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return con;
	}

	public static void releaseConnection(Connection con) {
		
		try {
			if (con != null) {
				con.close();
				con = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void releaseResultSet(ResultSet rs) {

		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public static void releaseStatement(Statement stmt) {

		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public static void releaseStatement(PreparedStatement pstmt) {

		try {
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public static void release(Statement stmt, Connection con) {

		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		try {
			if (con != null) {
				con.close();
				con = null;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public static void release(PreparedStatement pstmt, Connection con) {

		try {
			if (pstmt != null) {
				
				pstmt.close();
				pstmt = null;
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		try {
			if (con != null) {
				if(!con.isClosed()){
					con.close();
				}
				con = null;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public static void release(ResultSet rs, Statement stmt, Connection con) {

		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		try {
			if (con != null) {
				con.close();
				con = null;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public static void release(ResultSet rs, PreparedStatement pstmt, Connection con) {

		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		try {
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		try {
			if (con != null) {
				con.close();
				con = null;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

}
