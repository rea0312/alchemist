package com.htjf.common.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.htjf.common.db.BatchBean;
import com.htjf.common.db.DBUtil;
import com.htjf.common.util.Utility;
import com.htjf.common.util.DateUtil;

/**
 * 
 * <p>
 * Title: 系统数据库操作基类
 * </p>
 * 
 * @author LongYd
 * @version 1.0
 * @date 2012-5-4 修订历史： 日期 作者 参考 描述
 */
public class BaseDAO {
	public static Logger logger = Logger.getLogger(BaseDAO.class);
	public static final long  BATCH_NUM=1000;//批处理每次提交的数量

	/**
	 * 执行DML语句
	 * 
	 * @param sql
	 * @return 影响记录的条数
	 */
	public static boolean execute(String sql) throws SQLException {
		boolean flag = true;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn =DBUtil.getConnection();
			ps = conn.prepareStatement(sql);
			conn.setAutoCommit(false);
			ps.execute();
			conn.commit();

		} catch (SQLException e) {
			flag = false;
			logger.error("执行SQL失败： " + e.getMessage() + "\n sql::" + sql);
			e.printStackTrace();
			conn.rollback();
			throw e;

		} finally {
			DBUtil.release(ps,conn);
		}
		return flag;
	}
	
	/**
	 * 执行长时间的DML语句
	 * 
	 * @param sql
	 * @return 影响记录的条数
	 */
	public static boolean executeLTimeConn(String sql) throws SQLException {
		boolean flag = true;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn =DBUtil.getLongConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			ps.execute();
			conn.commit();

		} catch (SQLException e) {
			flag = false;
			logger.error("执行SQL失败： " + e.getMessage() + "\n sql::" + sql);
			e.printStackTrace();
			conn.rollback();
			throw e;

		} finally {
			DBUtil.release(ps,conn);
		}
		return flag;
	}

	/**
	 * 执行更新或删除的SQL语句
	 * 
	 * @param sql
	 * @return 影响记录的条数
	 */
	public static int executeUpdate(String sql) throws SQLException {
		int flag = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			flag = ps.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			flag = -1;
			logger.error("执行SQL失败： " + e.getMessage() + "\n sql::" + sql);
			conn.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.release(ps,conn);
		}
		return flag;
	}

	/**
	 * 执行增、删、改语句
	 * 
	 * @param sql
	 *            SQL语句
	 * @param values
	 *            参数列表
	 * @return int 所影响的行数
	 * @throws Exception
	 */
	public static int executeUpdate(String sql, Object... values) throws Exception {
		int flag = 0;
		if (Utility.isEmpty(sql)) {
			logger.warn("[执行SQL语句失败:空的SQL语句]");
			return flag;
		}
		Connection conn = DBUtil.getConnection();
		conn.setAutoCommit(false);
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			if (values != null) {// 设置参数
				for (int i = 0; i < values.length; i++) {
					ps.setObject(i + 1, values[i]);
				}
			}
			flag = ps.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			flag = -1;
			logger.error("执行SQL失败： " + e.getMessage() + "\n sql::" + sql);
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			DBUtil.release(ps,conn);
		}
		return flag;
	}

	 

	/**
	 * 执行更新或删除的SQL语句
	 * 
	 * @param sqls []
	 * @return 影响记录的条数
	 */
	public static boolean executeUpdate(String[] sqls) throws SQLException {
		boolean flag = true;
		Connection conn = null;
		PreparedStatement ps = null;
		String sqlcache = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			for (int i = 0; i < sqls.length; i++) {
				String sql = sqls[i];
				sqlcache = sql;
				ps = conn.prepareStatement(sql);
				ps.executeUpdate();
				conn.commit();
				ps.close();
			}
		} catch (SQLException e) {
			flag = false;

			logger.error("出错的SQL=" + sqlcache);
			conn.rollback();
			throw e;

		} finally {
			DBUtil.release(ps,conn);
		}
		return flag;
	}

	
	/**
	 * 执行存储过程
	 * @param procedureNames
	 * @throws Exception
	 */
	public static void executeProcedure(String[] procedureNames) throws Exception{
		
	 
		Connection conn = null;
		CallableStatement proc = null;
		try {
			conn = DBUtil.getLongConnection(); /**长链接，不超时断开*/
			for(String pro:procedureNames){
		    	logger.info("START  >> " + pro);
		    	proc = conn.prepareCall("{CALL " + pro + "}");
		    	proc.execute();
		    	logger.info("FINISH  >> " + pro);
		    	proc.close();
		    	proc = null;
			}
		} catch (SQLException e) {
			logger.error("执行存储过程失败： " + e.getMessage() + "\n sql::" + procedureNames);
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.release(proc,conn);
		}
		 
	}

	/**
	 * 执行统计SQL语句
	 * 
	 * @param sql
	 * @return 记录数
	 */
	public static long queryCount(String sql, Object... values) throws SQLException {

		long rtn = -1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try { 
			conn = DBUtil.getConnection();
			ps = conn.prepareStatement(sql);

			if (values != null)// 设置参数
				for (int i = 0; i < values.length; i++)
					ps.setObject(i + 1, values[i]);

			rs = ps.executeQuery();
			if (rs.next()) {
				rtn = rs.getLong(1);
			}

		} catch (SQLException e) {
			logger.error("执行SQL失败： " + e.getMessage() + "\n sql::" + sql);
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.release(rs,ps,conn);
		}
		return rtn;
	}

	/**
	 * 执行单个查询SQL语句
	 * 
	 * @param sql
	 * @return 查询结果
	 */
	public static Object querySingle(String sql) throws SQLException {

		Object rtn = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				rtn = rs.getObject(1);
			}

		} catch (SQLException e) {
			logger.error("执行SQL失败： " + e.getMessage() + "\n sql::" + sql);
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.release(rs,ps,conn);
		}
		return rtn;
	}
	
	
	/**
	 * 执行单个查询SQL语句
	 * @param sql
	 * @param values PreparedStatement的可变参数
	 * @return
	 * 
	 */
	public static Object querySingle(String sql, Object[] values) throws SQLException {

		Object rtn = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			ps = conn.prepareStatement(sql);
			for(int i=0; i<values.length; i++)
				ps.setObject(i+1, values[i]);
			rs = ps.executeQuery();
			if (rs.next()) {
				rtn = rs.getObject(1);
			}

		} catch (SQLException e) {
			logger.error("执行SQL失败： " + e.getMessage() + "\n sql::" + sql);
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.release(rs,ps,conn);
		}
		return rtn;
	}

	
	/**
	 * 检查表是否在oracle中存在
	 * 
	 * @param tablename
	 *            要检查的表名
	 * @return
	 * @throws SQLException
	 */
	public static boolean objectExists(String objectname) throws SQLException {
		String sql = " select count(*) from user_objects where object_name = upper('" + objectname + "')";

		return queryCount(sql) > 0 ? true : false;
	}
	
	/**
	 * 检查表是否在oracle中存在
	 * 
	 * @param tablename
	 *            要检查的表名
	 * @return
	 * @throws SQLException
	 */
	public static boolean tableExists(String tablename) throws SQLException {
		String sql = " select count(*) from INFORMATION_SCHEMA.tables where table_name = upper('" + tablename + "')";

		return queryCount(sql) > 0 ? true : false;
	}
	public static boolean tableOracleExists(String tablename) throws SQLException {
		String sql = " select count(*) from user_tables where table_name = upper('" + tablename + "')";
		return queryCount(sql) > 0 ? true : false;
	}

	/**
	 * 判断索引是否已经存在
	 * 
	 * @param conn
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public static boolean indexExists(String indexName) throws Exception {
		String sql = "SELECT count(*) FROM USER_INDEXES 			" + "WHERE INDEX_NAME = UPPER('" + indexName + "')	";
		return queryCount(sql) > 0 ? true : false;
	}

	/**
	 * 删除表
	 * 
	 * @param tablename
	 *            要删除的表
	 * @return
	 * @throws SQLException
	 */
	public static void dropTable(String tablename) throws SQLException {
		if (tableExists(tablename)) {
			String sql = "drop table " + tablename;
			execute(sql);
		}

	}

	/**
	 * 检查表是否在oracle中存在
	 * 
	 * @param tablename
	 *            要检查的表名
	 * @return
	 * @throws SQLException
	 */
	public static boolean truncateTable(String tablename) throws SQLException {
		String sql = "\n truncate table " + tablename;

		return execute(sql);
	}

	/**
	 * 执行批操作
	 * 
	 * @param batchs
	 * @return 是否提交
	 * @throws Exception
	 */
	public static boolean batchExecute(BatchBean... batchs) {
		if (batchs == null || batchs.length == 0) {

			return false;
		}
		// System.out.println("批量执行的SQL数据=="+batchs.length+" ");
		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;
		boolean isCommit = true;
		String sql = null;
		try {
			  conn.setAutoCommit(false);// 设置手动提交事务
			 
			for (BatchBean batch : batchs) {
				sql = null;
				 
				Object[] values = null;

				sql = batch.getSql();
				values = batch.getValues();

				if (Utility.isEmpty(sql)) {// 空SQL结束循环,回滚事务
					isCommit = false;

					break;
				}

				ps = conn.prepareStatement(sql);
				if (values != null)
					for (int i = 0; i < values.length; i++)
						ps.setObject(i + 1, values[i]);

				ps.executeUpdate();
				conn.commit();
				ps.close();
			}
			isCommit = true;
		} catch (Exception e) {
			isCommit = false;
			e.printStackTrace();
			logger.error("出错的SQL=" + sql);

		} finally {
			try {
				if (isCommit) {

					conn.commit();
				} else {

					conn.rollback();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			DBUtil.release(ps,conn);
		}
		return isCommit;
	}

	/**
	 * 查询得到列表
	 * 
	 * @return list{map{{字段名:字段值},{...}},{...}}
	 */
	public static List<Map<String, Object>> query(String sql, Object[] values) throws SQLException {
		List<Map<String, Object>> als = new ArrayList<Map<String, Object>>();

		Connection conn = null;
		java.sql.PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
			conn = DBUtil.getConnection();
			ps = conn.prepareStatement(sql);
				if (values != null)// 设置参数
					for (int i = 0; i < values.length; i++)
						ps.setObject(i + 1, values[i]); 

			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			while (rs.next()) {
				Map<String, Object> hm = new HashMap<String, Object>();
				for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
					// 数据库中的字段名为关键字
					hm.put(rsmd.getColumnName(i).toLowerCase(), rs.getObject(i));
				}
				als.add(hm);
			}

		} catch (SQLException e) {

			e.printStackTrace();
			logger.error("执行SQL失败： " + e.getMessage() + "\n sql::" + sql);
			throw e;
		} finally {
			DBUtil.release(rs,ps,conn);
		}
		return als;
	}

	/**
	 * 批量执行SQL语句 每1000条提交一次
	 * 
	 * @param sql
	 * @param paras
	 * @return
	 * @throws SQLException
	 */
	public static int batchExecuteSQL(String sql, List<Object[]> paras) throws SQLException {
		int count = 0;
		Connection conn = DBUtil.getConnection();
		int[] counts = null;
		
		PreparedStatement ps = null;
		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			if (paras != null && !paras.isEmpty()) {
				for (int i = 0; i < paras.size(); i++) {
					Object[] items = paras.get(i);
					for (int j = 0; j < items.length; j++) {
						ps.setObject(j + 1, items[j]);
					}
					ps.addBatch();
					if (i > 0 && i % BATCH_NUM == 0) {
						counts = ps.executeBatch();
						count += counts.length;
					}
				}
			}
			counts = ps.executeBatch();
			count += counts == null ? 0 : counts.length;
		 	conn.commit();
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
		} finally {
			DBUtil.release(ps,conn);
		}
		return count;
	}

	/**
	 * 查询结果
	 * 
	 * @param sql
	 * @return List<String[]> 保存字符串值数组
	 * @throws Exception
	 */
	public static List<String[]> queryDataArrList(String sql) throws Exception {
		List<String[]> als = new ArrayList<String[]>();

		Connection conn = null;
		java.sql.PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
			conn = DBUtil.getConnection();
			ps = conn.prepareStatement(sql);

			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			while (rs.next()) {
				String[] vals = new String[rsmd.getColumnCount()];
				
				Object val = null;
				for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
					// 数据库中的字段名为关键字
					val = rs.getObject(i);
					if(val!=null)
						vals[i-1] = val.toString();
					else
						vals[i-1] = "";
				}
				als.add(vals);
			}

		} catch (SQLException e) {

			e.printStackTrace();
			logger.error("执行SQL失败： " + e.getMessage() + "\n sql::" + sql);
			throw e;
		} finally {
			DBUtil.release(rs,ps,conn);
		}
		return als;
	}
	
	
	/**
	 * 查询SQL语句中第一列的值
	 * 
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
			conn = DBUtil.getConnection();
			stat = conn.createStatement();
			rest = stat.executeQuery(sql);
			while (rest.next()) {
				firstColumnStrList.add(rest.getString(1));
			}
		} finally {
			DBUtil.release(rest,stat,conn);

		}
		return firstColumnStrList;
	}
	
	
	

	/**
	 * 查询SQL语句第一列和第二列的值
	 * 
	 * @param sql
	 * @return Map<String, String> 第一列为键，第二列为值
	 * @throws Exception
	 */
	public static Map<String, String> queryDataMap(String sql, String keyField, String valueField) throws Exception {
		Map<String, String> dataMap = new HashMap<String, String>();
		Connection conn = null;
		Statement stat = null;
		ResultSet rest = null;
		keyField = keyField.toLowerCase();
		valueField = valueField.toLowerCase();
		try {
			conn = DBUtil.getConnection();
			stat = conn.createStatement();
			rest = stat.executeQuery(sql);
			while (rest.next()) {
				dataMap.put(rest.getString(keyField), rest.getString(valueField));
			}
		} finally {
			DBUtil.release(rest,stat,conn);
		}
		return dataMap;
	}

	/**
	 * 执行给定的SQL语句,同时返回指定字段结果
	 * 
	 * @param conn
	 * @param sql
	 * @throws Exception
	 */
	public static String executeSQLWithResult(String sql, String field) throws Exception {
		List<Map<String, Object>> lst = query(sql, null);
		if (lst != null && lst.size() > 0) {
			Map<String, Object> map = lst.get(0);
			return map.get(field)+"";
		} else {
			return "";
		}

	}
	public static void upDate(List<Map<String,Object>> list)throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		conn = DBUtil.getConnection();
		final String currentDay = DateUtil.getCurrentDateStr("yyyyMMdd");
		final String urlTable = "MMDS_TEST_SPIDER_DOWN" + currentDay;
		try {
			stmt = conn.createStatement();
			for(int x=0;x<list.size();x++)
			{
				String uSql="update  " + urlTable + " set stadate = '0' where ID = "+list.get(x).get("id");
				stmt.execute(uSql);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBUtil.release(stmt,conn);
		}
		
	}

	/**
	 * 执行给定的SQL语句,同时返回指定结果集
	 * 
	 * @param conn
	 * @param sql
	 * @throws Exception
	 */
	public static List<Map<String, Object>> query(String sql) throws Exception {
		return BaseDAO.query(sql, null);

	}

	/**
	 * 创建索引
	 * 
	 * @param indexName
	 *            索引名
	 * @param tableName
	 *            表名
	 * @param column
	 *            列
	 * @param noLog
	 *            是否NOLOGGING模式
	 * @throws Exception
	 */
	public static void createIndex(String indexName, String tableName, String column, boolean noLog) throws Exception {
		try{
			String createSQL = "CREATE INDEX " + indexName + " ON " + tableName + "(" + column + ")";
			createSQL = createSQL + (noLog ? " NOLOGGING" : "");
			BaseDAO.execute(createSQL);
			
		}catch(Exception ex){
			logger.error("索引"+ indexName+"可能已经存在，或 表"+tableName+"字段"+column+"已经建立了索引");
		}
	}

	/**
	 * 删除索引
	 * 
	 * @param indexName
	 * @throws Exception
	 */
	public static void dropIndex(String indexName) throws Exception {
		try{
		String dropSQL = "DROP INDEX " + indexName;
		BaseDAO.execute(dropSQL);
		}catch(Exception e)
		{
			logger.error("索引："+indexName+"可能不存在");
		}
	}
}
