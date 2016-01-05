package com.ibs.zj.qrcode.wordcount;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;

public class PutTaskService {

	/**
	 * 线程队列信息
	 */
	private ThreadContainer<FutureTask<Result>> threadPool;

	public PutTaskService(ThreadContainer<FutureTask<Result>> threadPool) {
		this.threadPool = threadPool;
	}
	
	/**
	 * 添加任务
	 * 
	 * @param doTaskService 数据库操作接口 
	 * @param runParams     运行参数
	 * @param semaphore     信号量
	 * @param cyclicBarrier 栅栏
	 */
	public void putTask(final CountSevice countSevice,final String runParams,final Semaphore semaphore,final CyclicBarrier cyclicBarrier,final CountDownLatch doneSignal,final ConcurrentHashMap<String,Integer> dataMap){
		final Result rsp = new Result();
		FutureTask<Result> task = new FutureTask<Result>(new Callable<Result>() {
			public Result call() throws Exception {
				boolean result = countSevice.countWorld(runParams, semaphore, cyclicBarrier,doneSignal,dataMap);
				// 设置操作结果信息
				rsp.setFileName(runParams);
				rsp.setSuccess(result);
				return rsp;
			}
		});
		// 加入到容器
		try {
			threadPool.put(task);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			System.out.println(runParams+"加入队列");
		}
	}
	
}
