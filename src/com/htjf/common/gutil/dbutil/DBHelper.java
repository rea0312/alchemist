package com.htjf.common.gutil.dbutil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库常规操作方法集
 * @author LJ
 * @date 2010-12-14
 * Copyright 广东华工九方科技有限公司
 */
public class DBHelper {
	/**
	 * 判断表是否已经存在
	 * @param conn
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public static boolean tableExists(String tableName) throws Exception {
		String selectSQL = 
			"SELECT TABLE_NAME FROM USER_TABLES 			" +
			"WHERE TABLE_NAME = UPPER('" + tableName + "')	";
		return queryDataList(selectSQL).size() > 0;
	}
	
	/**
	 * 判断索引是否已经存在
	 * @param conn
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public static boolean indexExists(String indexName) throws Exception {
		String selectSQL = 
			"SELECT INDEX_NAME FROM USER_INDEXES 			" +
			"WHERE INDEX_NAME = UPPER('" + indexName + "')	";
		return queryDataList(selectSQL).size() > 0;
	}
	
	/**
	 * 查询SQL语句中第一列的值
	 * @param sql
	 * @return List<String> 保存第一列的值的字符串值
	 * @throws Exception
	 */
	public static List<String> queryDataList(String sql) throws Exception {
		List<String> firstColumnStrList = new ArrayList<String>();
		Connection conn = null;
		Statement stat = null;
		ResultSet rest = null;
		
		try {
			conn = DBUtil.getjConnection();
			stat = conn.createStatement();
			rest = stat.executeQuery(sql);
			while (rest.next()) {
				firstColumnStrList.add(rest.getString(1));
			}
		} finally {
			if (rest != null) {rest.close(); rest = null;}
			if (stat != null) {stat.close(); stat = null;}
			if (conn != null) {conn.close(); conn = null;}
		}
		return firstColumnStrList;
	}
	
	/**
	 * 查询SQL语句第一列和第二列的值
	 * @param sql
	 * @return Map<String, String> 第一列为键，第二列为值
	 * @throws Exception
	 */
	public static Map<String, String> queryDataMap(String sql) throws Exception {
		Map<String, String> dataMap = new HashMap<String, String>();
		Connection conn = null;
		Statement stat = null;
		ResultSet rest = null;
		
		try {
			conn = DBUtil.getConnection();
			stat = conn.createStatement();
			rest = stat.executeQuery(sql);
			while (rest.next()) {
				dataMap.put(rest.getString(1), rest.getString(2));
			}
		} finally {
			if (rest != null) {rest.close(); rest = null;}
			if (stat != null) {stat.close(); stat = null;}
			if (conn != null) {conn.close(); conn = null;}
		}
		return dataMap;
	}
	
	/**
	 * 执行给定的SQL语句
	 * @param conn
	 * @param sql
	 * @throws Exception
	 */
	public static void executeSQL(String sql) throws Exception {
		Connection conn = null;
		Statement stat = null;
		try {
			conn = DBUtil.getConnection();
			stat = conn.createStatement();
			stat.execute(sql);
		} finally {
			if (stat != null) {stat.close(); stat = null;}
			if (conn != null) {conn.close(); conn = null;}
		}
	}
	
	public static void executeSQLByJDBC(String sql) throws Exception {
		Connection conn = null;
		Statement stat = null;
		try {
			conn = JDBCHelper.getDefaultConn();
			stat = conn.createStatement();
			stat.execute(sql);
		} finally {
			if (stat != null) {stat.close(); stat = null;}
			if (conn != null) {conn.close(); conn = null;}
		}
	}
	
	/**
	 * 执行给定的SQL语句,同时返回指定字段结果
	 * @param conn
	 * @param sql
	 * @throws Exception
	 */
	public static String executeSQLWithResult(String sql, String filed) throws Exception {
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		String result = "";
		try {
			conn = DBUtil.getConnection();
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			while(rs.next()){
				result = rs.getString(filed);
			}
		} finally {
			if (stat != null) {stat.close(); stat = null;}
			if (conn != null) {conn.close(); conn = null;}
		}
		return result;
	}
	
	/**
	 * 执行给定的SQL语句,同时返回指定结果集
	 * @param conn
	 * @param sql
	 * @throws Exception
	 */
	public static List<Map<String,Object>> executeSQLWithResultList(String sql) throws Exception {
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = null;
		try {
			conn = DBUtil.getjConnection();
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			int j=0;
			while(rs.next()){
				map = new HashMap<String,Object>();
				int colCounts = rs.getMetaData().getColumnCount();
				for(int i=1; i<=colCounts;i++){
					map.put(rs.getMetaData().getColumnName(i).toLowerCase(), rs.getObject(i));
				}	
				list.add(map);

				
			}
		} finally {
			if (stat != null) {stat.close(); stat = null;}
			if (conn != null) {conn.close(); conn = null;}
		}
		
		return list;
	}
	
	/**
	 * 使用给定的数据库连接执行给定的SQL语句
	 * @param conn
	 * @param sql
	 * @throws Exception
	 */
	public static void executeSQL(Connection conn, String sql) throws Exception {
		Statement stat = null;
		try {
			stat = conn.createStatement();
			stat.execute(sql);
		} finally {
			if (stat != null) {stat.close(); stat = null;}
		}
	}
	
	/**
	 * 批量执行Batch
	 * @param ps
	 */
	public static void executeBatch(PreparedStatement ps) throws Exception {
		ps.executeBatch();
		ps.clearBatch();
	}
	
	/**
	 * 创建索引
	 * @param indexName 索引名
	 * @param tableName 表名
	 * @param column 列
	 * @param noLog 是否NOLOGGING模式
	 * @throws Exception
	 */
	public static void createIndex(String indexName, String tableName, String column, boolean noLog) throws Exception {
		Connection conn = null;
		String createSQL = "CREATE INDEX " + indexName + " ON " + tableName + "(" + column + ")";
		createSQL = createSQL + (noLog ? " NOLOGGING" : "");
		try {
			conn = JDBCHelper.getDefaultConn();
			executeSQL(conn, createSQL);
		} finally {
			if (conn != null) {conn.close(); conn = null;}
		}
	}
	
	/**
	 * 删除索引
	 * @param indexName
	 * @throws Exception
	 */
	public static void dropIndex(String indexName) throws Exception {
		String dropSQL = "DROP INDEX " + indexName;
		executeSQL(dropSQL);
	}
	
	/**
	 * 查询SQL语句多字段的值
	 * @param sql
	 * @param String[]
	 * @return List
	 * @throws Exception
	 */
	public static List queryDataListObj(String sql,String[] field) throws Exception {
		List lstDataMap = new ArrayList();
		Connection conn = null;
		Statement stat = null;
		ResultSet rest = null;
		
		try {
			conn = DBUtil.getjConnection();
			stat = conn.createStatement();
			rest = stat.executeQuery(sql);
			while (rest.next()) {
				Object[] obj = new Object[field.length];
				for(int i=0;i<field.length;i++){
					obj[i] = rest.getString(field[i]); 
				}
				lstDataMap.add(obj);
			}
		} finally {
			if (rest != null) {rest.close(); rest = null;}
			if (stat != null) {stat.close(); stat = null;}
			if (conn != null) {conn.close(); conn = null;}
		}
		return lstDataMap;
	}
	/**
	 * 查询SQL语句第一列和第二列的值
	 * @param sql
	 * @return Map<String, String> 第一列为键，第二列为值
	 * @throws Exception
	 */
	public static Map<String, String> queryOrderDataMap(String sql) throws Exception {
		Map<String, String> dataMap = new LinkedHashMap<String, String>();
		Connection conn = null;
		Statement stat = null;
		ResultSet rest = null;
		
		try {
			conn = DBUtil.getConnection("proxool.mmdsdb");
			stat = conn.createStatement();
			rest = stat.executeQuery(sql);
			while (rest.next()) {
				dataMap.put(rest.getString(1), rest.getString(2));
			}
		} finally {
			if (rest != null) {rest.close(); rest = null;}
			if (stat != null) {stat.close(); stat = null;}
			if (conn != null) {conn.close(); conn = null;}
		}
		return dataMap;
	}
	/**
	 * 查询SQL语句多字段的值
	 * @param sql
	 * @param String[]
	 * @return List
	 * @throws Exception
	 */
	public static List<List<String>> queryDataListStr(String sql,String[] field) throws Exception {
		List<List<String>> lstData = new ArrayList<List<String>> ();
		Connection conn = null;
		Statement stat = null;
		ResultSet rest = null;
		try {
			conn = DBUtil.getjConnection();
			stat = conn.createStatement();
			rest = stat.executeQuery(sql);
			while (rest.next()) {
				List<String> obj = new ArrayList<String> ();
				for(int i=0;i<field.length;i++){
					if(field[i].indexOf(" as ")>0){
						obj.add(rest.getString(field[i].split(" as ")[1])); 
					}else{
						obj.add(rest.getString(field[i])); 
					}
				}
				lstData.add(obj);
			}
		} finally {
			if (rest != null) {rest.close(); rest = null;}
			if (stat != null) {stat.close(); stat = null;}
			if (conn != null) {conn.close(); conn = null;}
		}
		return lstData;
	}
}
