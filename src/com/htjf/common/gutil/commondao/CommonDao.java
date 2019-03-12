/**
 * WarmDao.java 2009-4-27 下午05:04:13
 */
package com.htjf.common.gutil.commondao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.htjf.common.gutil.dbutil.DBUtil;
import com.htjf.common.gutil.logutil.LogInfoUtil;


/**
 * @author aiyan
 * @version 1.0
 *
 */
public class CommonDao {
	private String pool;
	private String sql;
	private String dataSetSql;
	private String[] keys;
	private String sort = "order by id asc";

	private int dbType;
	private int pageIndex;
	private int pageSize;
	private int pageTotal;

	/**
	 * @return the dbType
	 */
	public int getDbType() {
		return dbType;
	}

	/**
	 * @param dbType
	 *            the dbType to set
	 */
	public void setDbType(int dbType) {
		this.dbType = dbType;
	}

	/**
	 * @return the keys
	 */
	public String[] getKeys() {
		return keys;
	}

	/**
	 * @return the pageIndex
	 */
	public int getPageIndex() {
		return pageIndex;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @return the pool
	 */
	public String getPool() {
		return pool;
	}

	/**
	 * @return the sql
	 */
	public String getSql() {
		return sql;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	/**
	 * @return the dataSetSql
	 */
	public String getDataSetSql() {
		return dataSetSql;
	}

	/**
	 * @param dataSetSql
	 *            the dataSetSql to set
	 */
	public void setDataSetSql(String dataSetSql) {
		this.dataSetSql = dataSetSql;
	}

	/**
	 * @param pageTotal
	 *            the pageTotal to set
	 */
	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}

	public void setPool(String pool) {
		this.pool = pool;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public void setKeys(String[] keys) {
		this.keys = keys;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public void setDBType(int dbType) {
		this.dbType = dbType;
	}

	public void exeSql(String sql) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DBUtil.getConnection(pool);
			stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			LogInfoUtil.logError(e, "CommonDao  eseSql");
		} finally {
			DBUtil.closeQuiet(stmt, conn);
		}

	}

	// 对于一个临时来说，只能在一个连接中进行操作，所以有必要每次使用同一个连接，此方法为临时表而做的
	public void exeSql(String sql, Connection conn) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			LogInfoUtil.logError(e, "CommonDao  eseSql");
		} finally {
			DBUtil.closeQuiet(stmt);
		}
	}

	public int getPageTotal() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int num = 0;

		try {
			conn = DBUtil.getConnection(pool);
			String countSql = "select count(*) from (" + sql + ") t ";
			pstmt = conn.prepareStatement(countSql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				num = rs.getInt(1);
			}
			pageTotal = num;
		} catch (SQLException e) {
			LogInfoUtil.logError(e, "CommonDao  getPageTotal");
		} finally {
			DBUtil.closeQuiet(rs, pstmt, conn);
		}

		return pageTotal;
	}

	public int getPageTotal(Connection conn) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int num = 0;

		try {
			String countSql = "select count(*) from (" + sql + ") t";
			pstmt = conn.prepareStatement(countSql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				num = rs.getInt(1);
			}
			pageTotal = num;
		} catch (SQLException e) {
			LogInfoUtil.logError(e, "CommonDao  getPageTotal");
		} finally {
			DBUtil.closeQuiet(rs, pstmt);
		}

		return pageTotal;
	}

	public List getListMapNoPage() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<HashMap> list = new ArrayList<HashMap>();
		try {
			conn = DBUtil.getConnection(pool);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (String key : keys) {
					map.put(key, (rs.getString(key.replace("'", ""))+"").equals("null")?" ":rs.getString(key.replace("'", "")));


				}
				list.add((HashMap) map);
			}

		} catch (SQLException e) {
			LogInfoUtil.logError(e, "CommonDao  getListMapNoPage");
		} finally {
			DBUtil.closeQuiet(rs, stmt, conn);
		}

		return list;
	}

	public List getListMap() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<HashMap> list = new ArrayList<HashMap>();
		try {
			conn = DBUtil.getConnection(pool);

			if (pageIndex * pageSize != 0) {
				if (dbType == 1) {
					sql = mysqlPageSql(sql);
				} 
				else if (dbType == 3)
					{
					sql = oracleserverPageSql(sql, keys, sort);
					}
				else {
					sql = sqlserverPageSql(sql, keys, sort);
				}

			}
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (String key : keys) {
					map.put(key, (rs.getString(key.replace("'", ""))+"").equals("null")?" ":rs.getString(key.replace("'", "")));

				}
				list.add((HashMap) map);
			}

		} catch (SQLException e) {
			LogInfoUtil.logError(e, "CommonDao  getListMap");
		} finally {
			DBUtil.closeQuiet(rs, pstmt, conn);
		}

		return list;
	}

	// 对于一个临时来说，只能在一个连接中进行操作，所以有必要每次使用同一个连接，此方法为临时表而做的
	public List getListMap(Connection conn) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<HashMap> list = new ArrayList<HashMap>();
		try {
			if (pageIndex * pageSize != 0) {
				if (dbType == 1) {
					sql = mysqlPageSql(sql);
				} else if (dbType == 3)
					{
					sql = oracleserverPageSql(sql, keys, sort);
					}else {
					sql = sqlserverPageSql(sql, keys, sort);
				}

			}

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (String key : keys) {
					map.put(key, (rs.getString(key.replace("'", ""))+"").equals("null")?" ":rs.getString(key.replace("'", "")));

				}
				list.add((HashMap) map);
			}

		} catch (SQLException e) {
			LogInfoUtil.logError(e, "CommonDao  getListMap");
		} finally {
			DBUtil.closeQuiet(rs, pstmt);
		}

		return list;
	}

	private String sqlserverPageSql(String userSql, String[] keys, String sort) {
		if (keys.length < 1) {
			return "";
		}

		String sql = "select * from " + " (select row_number() over(" + sort
				+ ") as row";

		for (String key : keys) {
			sql = sql + "," + key;
		}
		sql += " from (" + userSql + ") as sql) as list "
				+ " where row between " + ((pageIndex - 1) * pageSize + 1)
				+ " and " + pageIndex * pageSize;
		return sql;

	}

	private String mysqlPageSql(String userSql) {
		String sql = userSql + " limit " + (pageIndex - 1) * pageSize + " , "
				+ pageSize;
		return sql;

	}
	
	private String oracleserverPageSql(String userSql, String[] keys, String sort) {
		if (keys.length < 1) {
			return "";
		}
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append("select * from"); 
		sbuffer.append("(select ");
		for (String key : keys) {
			sbuffer.append( key+"," );
		}
		sbuffer.append(" rownum row_num from "); 
		sbuffer.append("(" + userSql + ") a "); 
		sbuffer.append("where rownum <="+pageIndex * pageSize+") where row_num  >= "+((pageIndex - 1) * pageSize + 1)); 
//		System.out.println(sbuffer);
		return sbuffer.toString();

	}

}
