package com.htjf.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密操作类 
 * Author：slh 
 * time : 2011-06-30 
 * Copyright 华仝九方科技有限公司
 */
public class MD5FileHelper {

	// 初始化md5字符
	private final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	private MessageDigest md = null;

	/**
	 * 初始化md5信息-摘要加密实例
	 */
	public MD5FileHelper() {
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
			System.out.println("无法初始化，操作系统可能不支持MD5编码");
		}
	}

	/**
	 * 获取md5值
	 * @param filename
	 * @return
	 */
	public String getFileMD5(String filename) {
		File f = new File(filename);
		return getFileMD5(f);
	}

	/**
	 * 获取md5值
	 * @param file
	 * @return
	 */
	public String getFileMD5(File file) {
		String result = "";
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			FileChannel channel = in.getChannel();
			long len = file.length();
			result = md5ByBuffer(channel, len);
			
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * md5加密处理
	 * @param channel
	 * @param len
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	private String md5ByBuffer(FileChannel channel, long len)
			throws IOException, Exception {
		ByteBuffer buff = ByteBuffer.allocate(1024);

		while (channel.read(buff) != -1) {
			buff.flip();
			md.update(buff);
			buff.flip();
		}
		channel.close();
		byte[] data = md.digest();
		return bytesToHex(data);
	}

	/**
	 * 生成md5值
	 * @param bt
	 * @return
	 */
	public String bytesToHex(byte[] bt) {
		StringBuffer buffer = new StringBuffer(bt.length * 2);
		for (int i = 0; i < bt.length; i++) {
			byteToHex(bt[i], buffer);
		}
		return buffer.toString();
	}

	/**
	 * 取得md5的单个字符
	 * @param b
	 * @param buffer
	 */
	private void byteToHex(byte b, StringBuffer buffer) {
		char c1 = hexDigits[(b & 0xf0) >> 4];
		char c2 = hexDigits[(b & 0xf)];
		buffer.append(c1);
		buffer.append(c2);
	}
}
