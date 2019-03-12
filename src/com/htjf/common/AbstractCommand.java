/*
 * Copyright @  2011广东华仝九方科技有限公司
 * All rights reserved.
 */

package com.htjf.common;

import java.util.concurrent.BlockingQueue;

public abstract class  AbstractCommand extends Thread {
	protected org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AbstractCommand.class);
	private BlockingQueue queue = null;
	private boolean threadSleep=false;
	private long sleepTime=1000*10;//10秒
 

 
	public void setQueue(BlockingQueue q){
		this.queue = q;
	}
	
	/**
	 * 是否将线程睡眠/启动
	 * @param isSleep true 睡眠线程，false唤醒
	 */
	public void sleepThread(boolean isSleep){
		threadSleep=isSleep;
	}
	
	/** 不断地执行，这里不进行睡眠 */ 
	public void run() {
		try {
			while (true) {
				if(threadSleep){
					Thread.sleep(sleepTime);
					continue;
				}
				
				consume(queue.take());
			}
		} catch (Throwable ex) {
	 	log.warn("消费线程执行出错,ex=" + ex.getMessage());
			ex.printStackTrace();
		}

	}
	
	public boolean isThreadSleep(){
		return this.threadSleep;
	}
	

 

	/**
	 * 使用者须实现此方法论
	 * @param o
	 */
	public abstract void consume(Object o);

}
