package com.htjf.common.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件处理类
 * 
 * @author LJ
 * @date 2010-12-16 Copyright 广东华工九方科技有限公司
 */
public class FileHelper {

	/**
	 * 移动文件
	 * 
	 * @param fromPath
	 * @param toPath
	 * @param fileName
	 * @throws Exception
	 */
	public static void moveFile(String fromPath, String toPath, String fileName)
			throws Exception {
		File file = new File(fromPath + fileName);
		if (file.isFile()) {
			FileInputStream input = new FileInputStream(file);
			FileOutputStream output = new FileOutputStream(toPath + fileName);

			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = input.read(b)) != -1) {
				output.write(b, 0, len);
			}
			output.flush();
			output.close();
			input.close();
			file.delete();
		}
	}

	/**
	 * 给文件进行重命名
	 * 
	 * @param fromFileName
	 * @param toFileName
	 * @author SLH
	 */
	public static void rename(String fromFileName, String toFileName) {
		File fileFrom = new File(fromFileName);
		File fileTo = new File(toFileName);
		fileFrom.renameTo(fileTo);
	}

	/**
	 * 移动文件
	 * 
	 * @param fromPath
	 * @param toPath
	 * @param fileName
	 * @throws Exception
	 */
	public static void moveFile(String fromRealPath, String toRealPath)
			throws Exception {
		File file = new File(fromRealPath);
		if (file.isFile()) {
			FileInputStream input = new FileInputStream(file);
			FileOutputStream output = new FileOutputStream(toRealPath);

			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = input.read(b)) != -1) {
				output.write(b, 0, len);
			}
			output.flush();
			output.close();
			input.close();
			file.delete();
		}
	}

	/**
	 * 复制文件
	 * 
	 * @param fromPath
	 * @param toPath
	 * @param fileName
	 * @throws Exception
	 * @author SLH
	 */
	public static void copyFile(String fromRealPath, String toRealPath)
			throws Exception {
		File file = new File(fromRealPath);
		if (file.isFile()) {
			FileInputStream input = new FileInputStream(file);
			FileOutputStream output = new FileOutputStream(toRealPath);

			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = input.read(b)) != -1) {
				output.write(b, 0, len);
			}
			output.flush();
			output.close();
			input.close();
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param fileRealPath
	 * @throws Exception
	 */
	public static void deleteFile(String fileRealPath) throws Exception {
		File file = new File(fileRealPath);
		file.delete();
	}
	
	/**
	 * 删除文件夹及其子目录或文件
	 * @param fileRealPath
	 * @return
	 */
	public static boolean deleteDir(String fileRealPath){
		File dir=new File(fileRealPath);
		if(dir.isDirectory()){
			String[] children =dir.list();
				
			for (int i = 0; i < children.length; i++) {
				boolean success=deleteDir(new File(dir, children[i]).getAbsolutePath());
				if(!success)
					return false;
			}
		}
		return dir.delete();
	}
	

	/**
	 * 获取文件大小
	 * 
	 * @param file
	 * @return
	 */
	public static long getFileSize(File file) {
		long fileSize = 0;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			fileSize = fis.available();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (Exception e) {
				}
		}
		return fileSize;
	}
	
	/**
	 * 获取指定目录下所有文件绝对路径
	 * @param realpath
	 * @param filelist
	 * @return	空目录返回null
	 */
	public static List<String> getFPathListRecurse(String realpath,List<String> filelist){
		
		File parent = new File(realpath);
		File[] children = parent.listFiles();
		if(children!=null){
			for (int i = 0; i < children.length; i++) {
				if(children[i].isDirectory()){
					getFPathListRecurse(children[i].getAbsolutePath(),filelist);
				}else{
					filelist.add(children[i].getAbsolutePath());
				}
			}
		}
		return filelist;
	}
	
	
	private static List<File> getFlistRecurse(String dirpath,List<File> fileList){
		
		File parent = new File(dirpath);
		File[] children = parent.listFiles();
		if(children!=null){
			for (int i = 0; i < children.length; i++) {
				if(children[i].isDirectory()){
					getFlistRecurse(children[i].getAbsolutePath(),fileList);
				}else{
					fileList.add(children[i]);
				}
			}
		}
		return fileList;
	}
	
	/**
	 * 递归获取指定目录下所有文件（包括子目录）
	 * @param dirpath
	 * @return	空目录返回null
	 */
	public static List<File> getFlistRecurse(String realpath){
		List<File> fileList = new ArrayList<File>();
		return getFlistRecurse(realpath, fileList);
	}
	
	/**
	 * 列出指定文件夹下，符合格式的文件名称
	 * 
	 * @param filePath
	 *            文件夹路径
	 * @param nameFilter
	 *            文件名称正则表达式
	 * @return
	 */
	public static List<String> getSortFileNames(String filePath,
			String nameFilter) {
		List<String> sortFile = new ArrayList<String>();

		String[] fileNames = new File(filePath).list();
		if (fileNames == null)
			return sortFile;

		for (String fileName : fileNames) {
			if (StringHelper.isRightFormat(fileName.toLowerCase(),
					nameFilter.toLowerCase())) {
				sortFile.add(fileName);
			}
		}
		Collections.sort(sortFile); // 排序
		return sortFile;
	}

	/**
	 * 列出指定文件夹下，指定最多个数，符合格式的文件名称
	 * 
	 * @param filePath
	 *            文件夹路径
	 * @param nameFilter
	 *            文件名称正则表达式
	 * @return
	 */
	public static List<String> getSortFileNames(String filePath,
			String nameFilter, int maxCount) {
		List<String> sortFile = new ArrayList<String>();
		int count = 0;
		
		String[] fileNames = new File(filePath).list();
		if (fileNames == null)
			return sortFile;
		
		Arrays.sort(fileNames);

		for (String fileName : fileNames) {
			if (StringHelper.isRightFormat(fileName.toLowerCase(), nameFilter.toLowerCase())) {
				sortFile.add(fileName);
				count++;
				if (count >= maxCount)
					break;
			}
		}
		Collections.sort(sortFile); // 排序
		return sortFile;
	}
	
	public static List<String> getDescSortFileNames(String filePath,
			String nameFilter, int maxCount) {
		List<String> sortFile = new ArrayList<String>();
		int count = 0;
		
		String[] fileNames = new File(filePath).list(new MyFilter(nameFilter.toLowerCase()));
		if (fileNames == null)
			return sortFile;
		
		Arrays.sort(fileNames, new MyComparator());

		for (String fileName : fileNames) {
			sortFile.add(fileName);
			count++;
			if (count >= maxCount)
				break;
		}
		return sortFile;
	}
	
	public static List<String> getASCSortFileNames(String filePath,
			String nameFilter, int maxCount){
		
		List<String> sortFile = new ArrayList<String>();
		int count = 0;
		
		String[] fileNames = new File(filePath).list(new MyFilter(nameFilter.toLowerCase()));
		if (fileNames == null)
			return sortFile;
		
		Arrays.sort(fileNames, new DialFComparator());

		for (String fileName : fileNames) {
			sortFile.add(fileName);
			count++;
			if (count >= maxCount)
				break;
		}
		return sortFile;
		
	}
	
	
	static class MyFilter implements FilenameFilter {  
		
        private String regex;  
        public MyFilter(String regex) {  
            this.regex = regex;  
        }  
        
        public boolean accept(File dir, String name) {
        	
        	Pattern pattern = Pattern.compile(regex);
    		Matcher match = pattern.matcher(name.toLowerCase());
    		return match.matches();

        }  
        
    }
	
	static class MyComparator implements Comparator<String> {

		@Override
		public int compare(String file, String newFile) {
			
			String fileTime = null;
			String newFileTime = null;
			
			if(file.startsWith("PS_HTTP_"))
				fileTime = file.substring(35,49);
			else if(file.startsWith("S1U-103-"))
				fileTime = file.substring(8,22);
			
			if(newFile.startsWith("PS_HTTP_"))
				newFileTime = newFile.substring(35,49);
			else if(newFile.startsWith("S1U-103-"))
				newFileTime = newFile.substring(8,22);
			
			return fileTime.compareTo(newFileTime);
			
		}  
    }  
	
	/**
	 * 拨测文件扫描  文件时间倒序
	 */
	static class DialFComparator implements Comparator<String>{
		
		@Override
		public int compare(String file, String newFile) {
			
			String fileTime = null;
			String newFileTime = null;
			
			if(file.startsWith("PS_HTTP_"))
				fileTime = file.substring(35, 49);
			else if(file.startsWith("S1U-"))
				fileTime = file.substring(9,23);
			
			if(newFile.startsWith("PS_HTTP_"))
				newFileTime = newFile.substring(35,49);
			else if(newFile.startsWith("S1U-"))
				newFileTime = newFile.substring(9,23);
			
			return fileTime.compareTo(newFileTime) * -1;
		}
	}

	/**
	 * 获取文件根据正则表达式过滤需要的文件列表
	 */
	public static String[] getFileArray(String path, final String regex) {
		File dir = new File(path);
		String[] fileNames = dir.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {

				if (name.matches(regex)) {// 获取.txt后缀的文件名
					return true;
				}
				return false;
			}
		});

		return fileNames;
	}

	/**
	 * 获取文件根据正则表达式过滤需要的文件列表
	 */
	public static List<String> getFileList(final String path,
			final String regex, final String suffix) {
		File dir = new File(path);
		final List<String> names = new ArrayList<String>();
		dir.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if (name.matches(regex)) {// 获取.txt后缀的文件名
					if (suffix != null) {// 标记正在处理
						FileHelper.rename(path + File.separator + name, path
								+ File.separator + name + suffix);
						name += suffix;
					}

					names.add(name);

					return true;
				}
				return false;
			}
		});

		Collections.sort(names);// 排序

		return names;
	}

	/**
	 * 列出指定文件夹下的文件名称
	 * 
	 * @param filePath
	 *            文件夹路径
	 * @return
	 */
	public static List<String> getSortFileNames(String filePath) {
		List<String> sortFile = new ArrayList<String>();

		String[] fileNames = new File(filePath).list();
		if (fileNames == null)
			return sortFile;

		for (String fileName : fileNames) {
			sortFile.add(fileName);
		}
		Collections.sort(sortFile); // 排序
		return sortFile;
	}

	/**
	 * 列出指定文件夹下的文件名称（注：主要用疑似病毒体md5加密）
	 * 
	 * @param filePath
	 *            文件夹路径
	 * @return
	 */
	public static List<String> getSortFileNames(String filePath, Date date) {
		List<String> sortFile = new ArrayList<String>();
		SimpleDateFormat dateFromat = new SimpleDateFormat("yyyyMMdd");
		String dateStr = dateFromat.format(date);
		String dateStr1 = "";
		String[] fileNames = new File(filePath).list();
		if (fileNames == null)
			return sortFile;

		for (String fileName : fileNames) {
			dateStr1 = fileName.substring(0, 8);
			// 过滤病毒名称为.sis或者.sisx或者.apk
			if (dateStr1.compareTo(dateStr) == 0
					&& (fileName.endsWith(".sis") || fileName.endsWith(".sisx") || fileName
							.endsWith(".apk"))) {
				sortFile.add(fileName);
			}
		}
		Collections.sort(sortFile); // 排序
		return sortFile;
	}

	/**
	 * 读取文件，获取BufferedReader
	 * 
	 * @param fileRealPath
	 * @return
	 */
	public static BufferedReader getFileSource(String fileRealPath) {
		File srcFile = new File(fileRealPath);
		if (!srcFile.exists()) {
			return null;
		}
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(srcFile), "UTF-8"));
		} catch (Exception e) {
			reader = null;
			e.printStackTrace();
		}
		return reader;
	}

	/**
	 * 读取文件
	 * 
	 * @param fileRealPath
	 * @return
	 * @throws FileNotFoundException
	 */
	public static BufferedReader getSource(String fileRealPath)
			throws FileNotFoundException {
		File srcFile = new File(fileRealPath);
		if (!srcFile.exists()) {
			return null;
		}
		return new BufferedReader(new FileReader(srcFile));
	}

	/**
	 * 读取文件里面的内容
	 * 
	 * @param filePath
	 * @return
	 */
	public static String readFile(String filePath) {
		File file = null;
		file = new File(filePath);
		if (!file.exists()) {
			return "";
		}
		FileReader reader = null;
		StringBuffer sbuff = new StringBuffer();
		BufferedReader buff = null;
		try {
			reader = new FileReader(file);
			buff = new BufferedReader(reader);
			String temp = "";

			while ((temp = buff.readLine()) != null) {
				sbuff.append(temp + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				buff.close();
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sbuff.toString();
	}

	/**
	 * 判断文件是否存在或者是否为文件
	 * 
	 * @param path
	 * @return
	 */
	public static boolean isFileOrExists(String path) {
		File file = new File(path);
		return file.exists() && file.exists();
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param filePath
	 * @return
	 * @date 2012-10-13
	 */
	public static boolean fileExists(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}

	public static void saveAsFile(String name, String text) {
		try {
			java.io.PrintWriter pw = new java.io.PrintWriter(name);
			pw.print(text);
			pw.flush();
			pw.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
