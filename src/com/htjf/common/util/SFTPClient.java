package com.htjf.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * SFTP工具类，具有下载，删除SFTP文件功能
 * @author LJ
 * @date 2010-12-9
 * Copyright 广东华工九方科技有限公司
 * 修改于2017-7-27 CZCheng
 */
public class SFTPClient {
	private final Logger log = Logger.getLogger(getClass());
	
	protected Session sshSession = null;
	protected ChannelSftp sftp = null;
	
    private String host = "";		//SFTP服务器
    private int port = -1;			//端口
    private String username = "";	//登录用户名
    private String password = "";	//登录密码
    
    /**
     * 构造函数
     * @param host
     * @param port
     * @param username
     * @param password
     */
	public SFTPClient(String host, String username, String password, int port) {
		super();
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}
    
	/**
	 * 连接SFTP服务器
	 * @throws Exception
	 */
    public void connect() throws Exception {
    	JSch jsch = new JSch();
    	
    	this.sshSession = jsch.getSession(username, host, port);
    	this.sshSession.setPassword(password);
    	Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        this.sshSession.setConfig(sshConfig);
        this.sshSession.connect();

        Channel channel = this.sshSession.openChannel("sftp");
        channel.connect();
        this.sftp = (ChannelSftp)channel;
    }
    
    /**
     * 关闭SFTP连接
     * @throws Exception
     */
    public void close() {
    	try {
    		if (this.sftp != null)
    			this.sftp.disconnect();
    		
    		if (this.sshSession != null)
    			this.sshSession.disconnect();
    	} catch (Exception e) {
    		log.error("SFTP close error!!!", e);
    	} finally {
    		this.sftp = null;
    		this.sshSession = null;
    	}
    }
    
    /**
     * 设置SFTP的路径
     * @param directory 要设置的路径
     * @throws Exception 
     */
    public void setDirectory(String directory) throws Exception {
    	this.sftp.cd(directory);
    }
    
    /**
     * 下载文件
     * @param downloadFile 需要下载的文件名
     * @param saveFile 保存在本地的完整文件名
     * @param directory 远程文件路径
     * @throws Exception
     */
    public void download(String directory, String downloadFile,String saveFile) throws Exception {
    	this.setDirectory(directory);
    	this.sftp.get(downloadFile, new FileOutputStream(new File(saveFile)));
    }
    
    /**
     * 下载目录下所有的文件
     * @param directory
     * @param localPath
     * @param nameReg
     * @throws Exception
     * @date 2012-9-25
     */
    public void downloadAllFile(String directory, String localPath, String nameReg) throws Exception {
    	this.setDirectory(directory);
		Pattern pattern = Pattern.compile(nameReg);
		
		Vector vector = listFiles(".");
    	if (null != vector) {
    		Iterator iterator = vector.iterator();
    		while (iterator.hasNext()) {
    			String lineStr = iterator.next().toString();
    			String fileName = lineStr.substring(lineStr.lastIndexOf(" ") + 1);
    			
    			Matcher matcher = pattern.matcher(fileName);
	    		if (matcher.matches()) {
	    			this.sftp.get(fileName, new FileOutputStream(new File(localPath + fileName)));
	    			this.sftp.rm(fileName);
	    		}
    		}
    	}
    }
    
    
    /**
     * 上传文件
     * @param directory 上传的目录
     * @param uploadFile 要上传的本地文件完整路径
     * @throws Exception
     */
    public void upload(String directory, String uploadFile) throws Exception {
    	this.setDirectory(directory);
    	File file = new File(uploadFile);
    	this.sftp.put(new FileInputStream(file), file.getName());
    }
    
    /**
     * 修改文件名
     * @param directory 目录
     * @param oldname 原名
     * @param newname 新名
     * @throws Exception
     */
    public void rename(String directory,String oldname, String newname) throws Exception {
    	this.setDirectory(directory);
    	this.sftp.rename(oldname, newname);
    }
    
    /**
     * 修改文件名
     * @param directory 目录
     * @param code 原名
     * @param fileName 新名
     * @throws Exception
     */
    public void chmod(String directory,String code, String fileName) throws Exception {
    	this.setDirectory(directory);
    	this.sftp.chmod(Integer.parseInt(code,8), fileName);
    }
    
    /**
     * 获取远程sftp的文件名列表
     * @param directory
     * @return
     */
    public Vector listFiles(String directory) throws SftpException{
		return sftp.ls(directory);
    	
    }
       
    
    /**
     * 删除文件
     * @param directory 要删除文件所在目录
     * @param deleteFile 要删除的文件
     * @throws Exception
     */
    public void delete(String directory, String deleteFile) throws Exception {
    	this.setDirectory(directory);
    	this.sftp.rm(deleteFile);
    }
    
}
