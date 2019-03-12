package com.htjf.common.util;


/**
 * 网络限速
 * @copyright 华仝九方
 * @author LUOJUN
 * @time 2014-10-23 下午02:27:03
 */
public class BandwidthLimiter {
	
	/**
	 * 利用睡眠限速
	 * @param length 要传输的字节数Byte
	 * @param downloadTime 耗费的时间时间
	 * @param speed 控制速度KB/s
	 */
    public static void doLimit(int length, long downloadTime, int speed) {
    	long timeCostPerChunk = (1000000000l * length) / (speed * 1024);
    	long sleepTime = timeCostPerChunk - downloadTime;
    	if (sleepTime > 0) {
    		try {
                Thread.sleep(sleepTime / 1000000, (int) (sleepTime % 1000000));
            } catch (Exception e) {
                e.printStackTrace();
            }
    	}
    }
}