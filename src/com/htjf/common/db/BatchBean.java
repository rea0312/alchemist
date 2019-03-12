
package com.htjf.common.db;


/**
 * 
 * <p>Title: SQL执行实体BEAN</p>
 * <p>Description: 主要用于批量执行时的SQL与绑定值一起捆绑</p>
 * @author LongYd
 * @version 1.0
 * @date 2012-3-31 	
 * 修订历史：
 * 日期          作者        参考         描述
 */
public class BatchBean {
 
	private String sql;
	private Object[] values;

	 
	/**
	 * 执行SQL的构造函数
	 * 
	 * @param sql
	 * @param values
	 */
	public BatchBean(String sql, Object[] values) {
		this.sql = sql;
		this.values = values;
	
	}




	public String getSql() {
		return sql;
	}

	public Object[] getValues() {
		return values;
	}

 


}
