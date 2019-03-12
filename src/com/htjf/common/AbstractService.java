package com.htjf.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

/**
 * <p>
 * Description: MMDS后台任务执行业务类，采用生产者--消费者模式 与 订阅者模式 进行重构
 * </p>
 * 
 * @author LongYoudong
 * @version 1.0 2010-8-20 下午03:33:15 修订历史： 日期 作者 参考 描述
 */
public abstract class AbstractService {

	protected org.apache.log4j.Logger log = Logger.getLogger(AbstractService.class);

	private int defaultQueueSize = 0;
	private BlockingQueue queue = null;
	private int maxThreads = 0; // 线程数阀值

	protected List<AbstractCommand> threadPooler = new ArrayList<AbstractCommand>(); // 线程池

	public void init(Class commandClass, int threadSize) throws Exception {
		if (threadSize <= 0) {
			threadSize = 1;
		}

		this.maxThreads = threadSize;
		this.defaultQueueSize = threadSize + 2; // 队列比线程多两个
		queue = new ArrayBlockingQueue(defaultQueueSize, true);
		
		this.initThreadPool(commandClass, threadSize);

	}

	/**
	 * 线程批量启动
	 * 
	 * @param command 消费线程
	 * @param Size
	 *            要启动的线程数量
	 */
	private void initThreadPool(Class<AbstractCommand> commandClass, int threadSize) throws Exception {
		for (int i = 0; i < threadSize; i++) {
			AbstractCommand	command = commandClass.newInstance();
			command.setQueue(queue);
			threadPooler.add(command);
			command.start();
		}
		//taskMonitCrawler();
		//new TaskMonitCrawler().start();

		log.info("当前开启的线程数量"+threadSize);
	}
	/**
	 * 监控线程池中的线程状态
	 */
	private void taskMonitCrawler(){
		while(true){
			for(int x=0;x<maxThreads;x++){
				System.out.println("线程 "+this.threadPooler.get(x).getName()+"的状态是："+this.threadPooler.get(x).getState());
			}
		}
	}
	
	/**
	 * 线程调整
	 * 
	 * @param size
	 * @return
	 */
	public boolean abjuestThread(int size) {
		
		if(this.getWorkingThreads()==size){
			log.info("工作线程数量 与 要求调整的数量"+size+"相同，不执行的线程数量调整");
			return false;
		}
		
		log.info("要求调整的线程数量="+size);
		if (size == threadPooler.size()) {
			return false;
		} else if (size >= threadPooler.size()) {
			if (threadPooler.size() >= maxThreads) {
				log.warn("线程调整失败，原因：要求开启线程量=" + size + ",实际线程阀值=" + maxThreads + ",当前有运行的线程=" + threadPooler.size());
				return false;
			} else {
				/** 全部开启 */
				for (AbstractCommand o : threadPooler) {
					o.sleepThread(false);
				}

			}
		} else {
			log.info("要求开户线程数量:"+size+",可调整的最大线程数量"+threadPooler.size());
			/** 全部关闭 */
			for (AbstractCommand o : threadPooler) {
				o.sleepThread(true);
			}

			/** 开启部分 */
			for (int i = 0; i < size; i++) {
				threadPooler.get(i).sleepThread(false);
			}
		}
		return true;
	}
	
	protected int getWorkingThreads(){
		int i=0;
		for (AbstractCommand o : threadPooler) {
			if(!o.isThreadSleep()){
				i+=1;
			}
		}
		return i;
	}

	/** 执行调度 */
	public boolean submit(Object o) {
		try {
			if (o == null)
				return false;
			queue.put(o);
			//System.out.println("queue size " + queue.size());
			return true;
		} catch (Exception ex) {
			log.warn("生产线程执行出错,ex=" + ex.getMessage());
			ex.printStackTrace();
		}
		return false;
	}

	public BlockingQueue getQueue() {
		return queue;
	}

	/** 业务处理子类 */
	public abstract void fire() throws Exception;

}
