package com.ibs.thread.demo.liu;

import java.util.concurrent.FutureTask;

/**
 * 线程队列的操作
 * 
 * 
 * @author zhongjun
 *
 */
public class ThreadOperation implements Runnable{

	
	/**
	 * 线程队列信息
	 */
	private ThreadPoolContainer<FutureTask<RunResult>> threadPool;
	
	
	/**
	 * 构造线程队列
	 * 
	 * @param threadPool 线程容器
	 */
	public ThreadOperation(ThreadPoolContainer<FutureTask<RunResult>> threadPool){
		this.threadPool = threadPool;
	}


	public void run() {
		
		try {
			// 获取队列里面的线程对象
			FutureTask<RunResult> task = threadPool.getLastTask();
			task.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
}
