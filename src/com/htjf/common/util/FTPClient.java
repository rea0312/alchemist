package com.htjf.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPFile;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.enterprisedt.net.ftp.FileTransferClient;
import com.htjf.common.helper.FileHelper;

/**
 * FTP工具类，具有下载，删除FTP文件功能
 * @author LJ
 * @date 2010-12-9
 * Copyright 广东华工九方科技有限公司
 */
public class FTPClient {
    private final Logger log = Logger.getLogger(getClass());

    protected FileTransferClient ftp = null;  
    
    private String serverIP = "";	//FTP服务器地址
    private String userName = "";	//登录用户名
    private String pass = "";		//登录密码
    private String ftpPath = "";	//FTP路径
    
    /**
     * 构造函数
     * @param serverIP
     * @param userName
     * @param pass
     * @param ftpPath
     */
    public FTPClient(String serverIP, String userName, String pass, String ftpPath) {
        super();
        this.serverIP = serverIP;
        this.userName = userName;
        this.pass = pass;
        this.ftpPath = ftpPath;
    }

    /**
     * 连接ftp
     * @throws Exception
     */
    public void connect() throws Exception {
        ftp = new FileTransferClient();  
        ftp.setRemoteHost(serverIP);
        ftp.setUserName(userName);
        ftp.setPassword(pass);

        ftp.connect();

        ftp.setContentType(FTPTransferType.BINARY);
        ftp.getAdvancedFTPSettings().setConnectMode(FTPConnectMode.PASV);
        
        if(!ftpPath.equals("")) this.changeDirectory(ftpPath);
    }
    

    /**
     * 关闭ftp连接
     * @throws Exception
     */
    public void close() {
        if (ftp != null) {
            try {
                ftp.disconnect();
            } catch (Exception ex) {
                log.error("FTP close error!", ex);
            } finally {
                ftp = null;
            }
        }
    }

    /**
     * 下载文件
     * 本地文件名取远程文件名
     * @param localPath 本地目录
     * @param remoteFileName 远程文件名称
     * @throws Exception
     */
    public void downloadFile(String localPath, String remoteFileName) throws Exception {
        ftp.downloadFile(localPath, remoteFileName);
    }
    
    /**
     * 
     * 按一定速度FTP下载文件
     * @param localPath
     * @param remoteFileName
     * @param speed KB/s
     * @param buffSize 每次读取的字节数
     * @throws Exception
     * 
     */
    public void downloadFile(String localPath, String remoteFileName, int speed, int buffSize) throws Throwable {
    	
    	InputStream is = null;
    	FileOutputStream fos = null;
    	long beginTimeMark = 0l;
    	long endTimeMark = 0l;
    	try {
    		
    		is = ftp.downloadStream(remoteFileName);   
	        fos = new FileOutputStream(localPath);
	        
			byte[] b = new byte[buffSize];
			boolean goOn = false;
			
			beginTimeMark = System.nanoTime();
			int nRead = is.read(b, 0, buffSize);
			endTimeMark = System.nanoTime();
			goOn = (nRead > 0);
			BandwidthLimiter.doLimit(nRead, (endTimeMark - beginTimeMark), speed);
			
			while (goOn) {
				fos.write(b, 0, nRead);
				beginTimeMark = System.nanoTime();
				nRead = is.read(b, 0, buffSize);
				endTimeMark = System.nanoTime();
				goOn = (nRead > 0);
				BandwidthLimiter.doLimit(nRead, (endTimeMark - beginTimeMark), speed);
			}
			fos.flush();
    	} finally {
    		if (null != is)
    			is.close();
    		if (null != fos)
    			fos.close();
    	}
    }
    
    
    
    /**
     * 按一定速度FTP下载文件
     * @param localPath
     * @param remoteFileName
     * @param speed KB/s
     * @param buffSize 每次读取的字节数
     * @throws Exception
     */
    public void limtDownFile(String localPath, String remoteFileName, int speed, int buffSize) throws Throwable{
    	
    	String tmpfile = localPath + File.separator + remoteFileName + ".tmp";
    	String realfile = localPath + File.separator + remoteFileName;
    	
    	InputStream is = null;
    	FileOutputStream fos = null;
    	long beginTimeMark = 0l;
    	long endTimeMark = 0l;
    	
    	byte[] b = new byte[buffSize];
    	int nRead = 0;
    	boolean goOn = true;
    	
    	try {
			
    		is = ftp.downloadStream(remoteFileName);
    		fos = new FileOutputStream(tmpfile);
    		
    		while(goOn){
    			beginTimeMark = System.nanoTime();
    			nRead = is.read(b, 0, buffSize);
    			endTimeMark = System.nanoTime();
    			goOn = (nRead > 0);
    			if(goOn)
    				fos.write(b, 0, nRead);
    			BandwidthLimiter.doLimit(nRead, (endTimeMark - beginTimeMark), speed);
    			
    		}
    		
    		fos.flush();
    		
		} finally{
			if (null != is)
    			is.close();
    		if (null != fos)
    			fos.close();
		}
    	FileHelper.moveFile(tmpfile, realfile);
    }
    
    public String pwd() throws Exception{
    	return ftp.getRemoteDirectory();
    }
    
    /**
     * 下载文件夹中符合命名规则的文件
     * @param localPath 本地目录
     * @param nameReg 文件名正则表达式
     * @throws Exception
     */
    public void downloadAllFile(String localPath, String nameReg) throws Exception {
		Pattern pattern = Pattern.compile(nameReg);
		
    	String[] fileNames = ftp.directoryNameList();
    	if (null != fileNames) {
	    	for (int i = 0; i < fileNames.length; i ++) {
	    		Matcher matcher = pattern.matcher(fileNames[i]);
	    		if (matcher.matches())
	    			this.downloadFile(localPath, fileNames[i]);
	    	}
    	}
    }
    
    /**
     * 下载文件夹中符合命名规则的文件，并且在远程ftp目录中删除文件
     * @param localPath
     * @param nameReg
     * @throws Exception
     * @date 2012-9-15
     */
    public void downloadAndDeleteAllFile(String localPath, String nameReg) throws Exception {
		Pattern pattern = Pattern.compile(nameReg);
    	String[] fileNames = ftp.directoryNameList();
    	if (null != fileNames) {
	    	for (int i = 0; i < fileNames.length; i ++) {
	    		Matcher matcher = pattern.matcher(fileNames[i]);
	    		if (matcher.matches()) {
	    			this.downloadFile(localPath, fileNames[i]);
	    			this.deleteFile(fileNames[i]);
	    		}
	    	}
    	}
    }
    
    public void uploadFile(String localFileName, String remoteFileName) throws Exception {
        ftp.uploadFile(localFileName, remoteFileName + ".tmp");
        ftp.rename(remoteFileName + ".tmp", remoteFileName);
    }
    
    /**
     * 返回ftp远程文件名列表
     * @throws Exception
     */
    public String[] getFileNames()throws Exception {
        return ftp.directoryNameList();
    }
    
    /**
     * 删除ftp远程文件
     * @param remoteFileName 远程文件名称
     * @throws Exception
     */
    public void deleteFile(String remoteFileName)throws Exception {
        ftp.deleteFile(remoteFileName);
    }

    /**
     * 改变ftp远程目录
     * @param directoryName
     * @throws Exception
     */
    public void changeDirectory(String directoryName) throws Exception {
        ftp.changeDirectory(directoryName);
    }
    
    /**
     * 远程文件是否存在
     * @param remoteFileName
     * @return
     * @throws Exception
     */
    public boolean isFileExist(String remoteFileName) throws Exception{
    	return ftp.exists(remoteFileName);
    }
    
    /**
     * 返回ftp远程 文件夹及文件名列表 
     * @return
     * @throws Exception
     */
    public List<String> getFileNames(String nameReg)throws Exception {
    	
    	Pattern pattern = Pattern.compile(nameReg);
    	
    	FTPFile[] ftpFile=ftp.directoryList();
    	List<String> flist = new ArrayList<String>();
    	
    	for (int i = 0; i < ftpFile.length; i++) {
    		
			Matcher matcher = pattern.matcher(ftpFile[i].getName());
			if(matcher.matches())
				flist.add(ftpFile[i].getName());
		}
    	
    	return flist;
    }
    /***
     * 下载文件
     * @param localPath
     * 			本地文件存放目录
     * @param remoteFullFilePath
     * 			远端文件全路径
     * @throws FTPException
     * @throws IOException
     */
    public void downloaFile(String localPath,String remoteFullFilePath) throws Exception{
    	
    	int fileSepratorIndex = remoteFullFilePath.lastIndexOf("/");
    	int length = remoteFullFilePath.length();
    	String parentPath = remoteFullFilePath.substring(0,fileSepratorIndex);
    	String fileName = remoteFullFilePath.substring(fileSepratorIndex+1,length);
    	
    	File tmpLocalFile = new File(localPath + File.separator + fileName+".tmp");
    	File localFile = new File(localPath + File.separator + fileName);
    	
    	ftp.changeDirectory(parentPath);
    	
    	if(isFileExist(fileName))
    		downloadFile(localFile.getAbsolutePath(), fileName);
    	else
    		log.error("文件不存在："+remoteFullFilePath);
    	
    	tmpLocalFile.renameTo(localFile);
    	
    }
    
    /**
     * 下载文件
     * 本地自定义文件名
     * @param localPath 本地目录
     * @param localFileName 本地文件名称
     * @param remoteFileName 远程文件名称
     */
    public void dowloadFile(String localPath,String localFileName, String remoteFileName)throws Exception{
    		
    	InputStream in = null;
    	FileOutputStream fos = null;
    	
    	in = ftp.downloadStream(remoteFileName);
    	fos = new FileOutputStream(localPath+File.separator+localFileName);
    	
    	byte[] bytes= new byte[2048];
    	int b = 0;
    	while((b=in.read(bytes, 0, bytes.length))!=-1){
    		fos.write(bytes, 0, b);
    	}
    	
		try {
			in.close();
			fos.close();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
    }
    
    
}
