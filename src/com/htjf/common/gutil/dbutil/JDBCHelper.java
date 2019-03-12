package com.htjf.common.gutil.dbutil;

import java.sql.Connection;
import java.sql.DriverManager;

import com.htjf.common.gutil.configutil.ConfigureUtil;



/**
 * JDBC辅助类
 * @author LJ
 * @date 2010-12-29
 * Copyright 广东华工九方科技有限公司
 */
public class JDBCHelper {
	/**
	 * 获取连接
	 * @param url
	 * @param user
	 * @param pass
	 * @return
	 * @throws Exception
	 */
	public static Connection getConn(String url, String user, String pass) throws Exception {
		String driverName = new ConfigureUtil().getValue("driver-class");
		Class.forName(driverName);
		Connection conn = DriverManager.getConnection(url, user, pass);
		return conn;
	}
	
	/**
	 * 获取默认数据库连接
	 * @return
	 * @throws Exception
	 */
	public static Connection getDefaultConn() throws Exception {
		String ORACLE_URL = new ConfigureUtil().getValue("ORACLE_URL");
		String ORACLE_USER = new ConfigureUtil().getValue("ORACLE_USER");
		String ORACLE_PASS = new ConfigureUtil().getValue("ORACLE_PASS");
		return getConn(ORACLE_URL, ORACLE_USER, ORACLE_PASS);
	}
}
