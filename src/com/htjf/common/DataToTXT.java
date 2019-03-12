package com.htjf.common;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DataToTXT
{
	public Log log = LogFactory.getLog(this.getClass());
	
	public final static int EXCEL_MAX_COL = 50000;							//excel每页最大容纳行数
	public final static String DATALIST_ATT_NAME = "txt_datalist";			//request中存放datalist的attribute的名字
	public final static String COLDEF_ATT_NAME = "txt_coldef";				//request中存放column define的attribute的名字
	public final static String SQL_ATT_NAME = "txt_sql";					//request中存放sql语句的attribute的名字
	public final static String SOURCE_TYPE_ATT_NAME = "txt_source_type";	//request中存放source type的attribute的名字
	public final static String DEFAULT_NULL_REPLACER = "";
	public final static int DEFAULT_FLASH_PERLINES = 2000;
	public final static int DEFAULT_FETCH_SIZE = 1;
	
	public final static int JDBC_SOURCE_TYPE = 1;		//jdbc的sql 数据来源的类型
	public final static int LIST_SOURCE_TYPE = 2;		//java的List对象 数据来源的类型
	public final static int DEFAULT_SOURCE_TYPE = LIST_SOURCE_TYPE;
	
	//数据来源的类型(jdbc的sql，java的List对象)
	private int sourceType = DEFAULT_SOURCE_TYPE;
	
	private String filePath = "";
	//列定义字符串
	private String colDefStr;
	//真正的列定义
	private ArrayList colDef = new ArrayList();
	//数据
	private List dataList;
	
	private Connection conn = null;
	
	private String sql = "";
	
	//输出字符集
	private String charsetName ;
	//空值替代符
	private String nullReplacer = DEFAULT_NULL_REPLACER;
	//输出流每flush一次的行数
	private int flushPerLines = DEFAULT_FLASH_PERLINES;
	
	private int resultSetFetchSize = DEFAULT_FETCH_SIZE;

	public DataToTXT(String colDefStr, List dataList)
	{
		super();
		this.colDefStr = colDefStr;
		this.dataList = dataList;
		this.sourceType = LIST_SOURCE_TYPE;
	}
	
	public DataToTXT(String colDefStr, Connection conn, String sql, int fetchSize)
	{
		super();
		this.colDefStr = colDefStr;
		this.conn = conn;
		this.sql = sql;
		this.resultSetFetchSize = fetchSize;
		this.sourceType = JDBC_SOURCE_TYPE;
	}
	
	public DataToTXT(String colDefStr, Connection conn, String sql)
	{
		super();
		this.colDefStr = colDefStr;
		this.conn = conn;
		this.sql = sql;
		this.sourceType = JDBC_SOURCE_TYPE;
	}
	
	private final static String COL_DEF_STR_BIG_SPLITOR = "\\,";
	private final static String COL_DEF_STR_SMALL_SPLITOR = "\\|";
	/**
	 * 交验colDefStr
	 * 标准格式为：name|姓名,no|号码
	 * @return
	 */
	private boolean validColDef()
	{
		String[] cols = colDefStr.split(COL_DEF_STR_BIG_SPLITOR);
		
		if(cols.length == 0) return false;
		
		for(int i = 0 ; i < cols.length ; i ++)
		{
			String[] tmpc = cols[i].split(COL_DEF_STR_SMALL_SPLITOR);
			if( tmpc.length != 2 ||
				tmpc[0].trim().length() == 0 ||
				tmpc[1].trim().length() == 0 )
				
			{
				return false;
			}
			tmpc = null;
		}	
		
		return true;
	}
	
	/**
	 * 把colDefStr转换进colDef
	 * @throws Exception
	 */
	private void unpackColDef()throws Exception
	{
		if( !validColDef() ) throw new Exception("列定义格式不正确."); 
		
		String[] cols = colDefStr.split(COL_DEF_STR_BIG_SPLITOR);
		
		String[] tmps ;
		for(int i = 0 ; i < cols.length ; i ++)
		{
			 tmps = cols[i].split(COL_DEF_STR_SMALL_SPLITOR);
			 colDef.add( new ColDef(tmps[0].trim() , tmps[1].trim()) );
			 tmps = null;
		}
		
		colDef.trimToSize(); 		
	}
	
	private final static String FILE_COL_SPLITOR = "|";
	private final static String FILE_NEW_LINE = "\r\n";
	/**
	 * 读取datalist写入目标输出流中
	 * @throws Exception
	 */
	private void write(java.io.OutputStream os)throws Exception
	{
		try
		{
			//write列名
			writeTittle(os);
			
			if( sourceType == LIST_SOURCE_TYPE )
			{
				writeFromList(os);
			}
			else if( sourceType == JDBC_SOURCE_TYPE )
			{
				writeFromJdbc(os);
			}
			
		}
		catch(Exception e)
		{
			throw new Exception("Write File ERROR!",e);
		}
	}
	
	private void writeTittle(java.io.OutputStream os)throws Exception
	{
		//write列名
		StringBuffer sbt = new StringBuffer();
		ColDef cd = null;
		for(int i = 0 ; i < colDef.size() ; i ++)
		{
			cd = (ColDef)colDef.get(i);
			sbt.append(cd.caption);
			sbt.append(FILE_COL_SPLITOR);
		}
		sbt.append(FILE_NEW_LINE);
		writeBytes( os , sbt );
		sbt = null;
	}
	
	private void writeFromList(java.io.OutputStream os)throws Exception
	{
		log.info("writeFromList :" + dataList.size());
		StringBuffer sb = new StringBuffer();
		Map tmpM = null;
		for(int dataIdx = 0 ; dataIdx < dataList.size() ; dataIdx ++ )
		{
			sb = new StringBuffer();
			tmpM = (Map) dataList.get(dataIdx);
			
			ColDef tmpcd = null;
			Object tmpcontext = null;
			for(int colIdx = 0 ; colIdx < colDef.size() ; colIdx ++)
			{
				tmpcd = (ColDef)colDef.get(colIdx);
				tmpcontext = tmpM.get( tmpcd.name );				
				if( tmpcontext != null ) sb.append( tmpcontext.toString().trim() );//去掉 null
				else sb.append( nullReplacer );
				sb.append(FILE_COL_SPLITOR);					
				
				tmpcontext = null;
				tmpcd = null;
			}
			
			sb.append(FILE_NEW_LINE);
			//检查是否有指定charsetName，无则用默认的charsetName
			writeBytes( os , sb );
			
			if( dataIdx%flushPerLines == 0 ) os.flush();
			sb = null;
			tmpM = null;
		}
		//final flush
		os.flush();
	}
	
	private void writeFromJdbc(java.io.OutputStream os)throws Exception
	{
		log.info("writeFromJdbc :" + sql);
		StringBuffer sb = new StringBuffer();
		Statement st = null;
		ResultSet rs = null;
		try
		{			
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			rs.setFetchSize(resultSetFetchSize);
			int dataIdx = 0;
			while(rs.next())
			{
				sb = new StringBuffer();
				ColDef tmpcd = null;
				Object tmpcontext = null;
				for(int colIdx = 0 ; colIdx < colDef.size() ; colIdx ++)
				{
					tmpcd = (ColDef)colDef.get(colIdx);
					tmpcontext = rs.getString( tmpcd.name );
					
					if( tmpcontext != null ) sb.append( tmpcontext.toString().trim() );//去掉 null
					else sb.append( nullReplacer );
					//还需要去掉换行 200904301754
					sb.append(FILE_COL_SPLITOR);					
					
					tmpcontext = null;
					tmpcd = null;
				}
				
				sb.append(FILE_NEW_LINE);
				//检查是否有指定charsetName，无则用默认的charsetName
				writeBytes( os , sb );
				
				dataIdx ++;
				if( dataIdx%flushPerLines == 0 ) os.flush();
				sb = null;
			}
			//final flush
			os.flush();
		}
		finally
		{
			if(rs != null) rs.close();
			if(st != null) st.close();
		}
	}
	
	public String createTXT(String filePath)throws Exception
	{
		this.filePath = filePath; 
		
		unpackColDef();
		
		FileOutputStream fos = null ;
		try
		{
			fos = new FileOutputStream( new File(filePath) );
			write(fos);
		}
		finally
		{
			if(fos != null) fos.close();
		}
		
		return filePath;
	}
	
	public void writeTXT(java.io.OutputStream os)throws Exception
	{
		unpackColDef();
		
		write(os);
	}	
	
	/**
	 * 检查是否有指定charsetName，无则用默认的charsetName
	 * @param os
	 * @param sb
	 * @throws Exception
	 */
	private void writeBytes(java.io.OutputStream os , StringBuffer sb)throws Exception
	{
		if( charsetName == null ) os.write( sb.toString().getBytes(charsetName) );
		else os.write( sb.toString().getBytes() );
	}	

	public int getFlushPerLines()
	{
		return flushPerLines;
	}

	public void setFlushPerLines(int flushPerLines)
	{
		this.flushPerLines = flushPerLines;
	}

	public String getCharsetName()
	{
		return charsetName;
	}

	public void setCharsetName(String charsetName) throws Exception
	{
		if( !Charset.isSupported(charsetName) )throw new Exception("Do not support specify charsetName: " + charsetName);
		this.charsetName = charsetName;
	}	

	public String getNullReplacer()
	{
		return nullReplacer;
	}

	public void setNullReplacer(String nullReplacer)
	{
		this.nullReplacer = nullReplacer;
	}

	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}	

	public int getResultSetFetchSize()
	{
		return resultSetFetchSize;
	}

	public void setResultSetFetchSize(int resultSetFetchSize)
	{
		this.resultSetFetchSize = resultSetFetchSize;
	}
	
	class ColDef
	{
		String caption;
		String name;
		ColDef(String name, String caption)
		{
			super();
			this.caption = caption;
			this.name = name;
		}		
	}
	
	
	public final static void main(String[] args)throws Exception
	{
		String colDefStr = "name|姓名,no|号码";
		List dataList = new ArrayList();
		
		Map mm = new HashMap();
		mm.put("name", "yu");
		mm.put("no", "12345");
		
		dataList.add(mm);
		
		Map mm2 = new HashMap();
		mm2.put("name", "xu");
		mm2.put("no", null);
		
		dataList.add(mm2);
		
		DataToTXT dtx = new DataToTXT( colDefStr, dataList);
		
		dtx.setCharsetName("utf-8");
		
		//dtx.setNullReplacer("null");
		
		System.out.println(dtx.createTXT("e:/abc.txt"));
	}

	

}
