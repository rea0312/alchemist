package com.htjf.common.util;

import java.io.File;
import java.util.ArrayList;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.apache.log4j.Logger;

/**
 * Zip压缩解压工具
 * @author CZCheng
 * @date 2018-1-10 11:00:57
 * */
public class ZipUtil {
	
	private static Logger log = Logger.getLogger(ZipUtil.class);
	
	/**
	 *  创建压缩包添 加文件到压缩包中（未设置加密）
	 *  @param zipPath 压缩路径
	 *  @param zipFileName 压缩文件名
	 *  @param filePath 待压缩文件全路径
	 * @return 
	 * */
	public static void DeflateFile(String zipPath,String zipFileName,String filePath) {
        try {
            ZipFile zipFile = new ZipFile(zipPath+File.separator+zipFileName);
              
            ArrayList<File> filesToAdd = new ArrayList<File>();
            filesToAdd.add(new File(filePath));
              
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // set compression method to deflate compression             
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL); 
            zipFile.addFiles(filesToAdd, parameters);
              
        } catch (ZipException e) {
        	log.error(e.getMessage(), e);
        }   
    }
	
	/**
	 *  创建压缩包添 加文件夹到压缩包中（未设置加密）
	 *  @param zipPath 压缩路径
	 *  @param zipFileName 压缩文件名
	 *  @param filePath 待压缩文件夹路径
	 * */
	public static void DeflateFolder(String zipPath,String zipFileName,String filePath) {
        try {
            ZipFile zipFile = new ZipFile(zipPath+File.separator+zipFileName);
              
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            zipFile.addFolder(filePath, parameters);
              
        } catch (ZipException e) {
        	log.error(e.getMessage(), e);
        }   
    }
	
	/**
	 *  创建压缩包添 加文件到压缩包中（设置加密）
	 *  @param zipPath 压缩路径
	 *  @param zipFileName 压缩文件名
	 *  @param filePath 待压缩文件全路径
	 *  @param Password 加密密码
	 * */
	public static void DeflateFileEncryption(String zipPath,String zipFileName,String filePath,String Password) {
        try {
            ZipFile zipFile = new ZipFile(zipPath+File.separator+zipFileName);
              
            ArrayList<File> filesToAdd = new ArrayList<File>();
            filesToAdd.add(new File(filePath));
            
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); 
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL); 
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
            parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
            parameters.setPassword(Password);
            
            zipFile.addFiles(filesToAdd, parameters);
              
        } catch (ZipException e) {
        	log.error(e.getMessage(), e);
        }   
    }
	
	
	/**
	 *  创建压缩包添 加文件夹到压缩包中（设置加密）
	 *  @param zipPath 压缩路径
	 *  @param zipFileName 压缩文件名
	 *  @param filePath 待压缩文件夹路径
	 *  @param Password 加密密码
	 * */
	public static void DeflateFolderEncryption(String zipPath,String zipFileName,String filePath,String Password) {
        try {
            ZipFile zipFile = new ZipFile(zipPath+File.separator+zipFileName);
              
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
            parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
            parameters.setPassword(Password);
            zipFile.addFolder(filePath, parameters);
              
        } catch (ZipException e) {
        	log.error(e.getMessage(), e);
        }   
    }
	
	/**
	 *  解压缩包（设置加密）
	 *  @param zipPath 压缩包路径
	 *  @param zipFileName 压缩包名
	 *  @param uzipPath 解压路径
	 *  @param Password 解压密码
	 * */
	 public static void ExtractFilePassword(String zipPath,String zipFileName,String uzipPath,String Password) {
         
	        try {
	            ZipFile zipFile = new ZipFile(zipPath+File.separator+zipFileName);
	            zipFile.setPassword(Password);
	            zipFile.extractAll(uzipPath);
	        } catch (ZipException e) {
	        	log.error(e.getMessage(), e);
	        }
	    }
	 
	/**
	 * 解压缩包（未设置加密）
	 * @param zipPath  压缩包路径
	 * @param zipFileName  压缩包名
	 * @param uzipPath 解压路径
	 * */
	public static void ExtractFile(String zipPath, String zipFileName,String uzipPath) {
		try {
			ZipFile zipFile = new ZipFile(zipPath + File.separator+ zipFileName);
			zipFile.extractAll(uzipPath);
		} catch (ZipException e) {
			log.error(e.getMessage(), e);
		}
	}

}
