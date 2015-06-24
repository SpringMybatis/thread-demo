package com.ibs.thread.demo.liu;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;


public class ThreadStringTest {

	/**
	 * 自增序列
	 */
	// private IntSequence sequence = new IntSequence();
	
	/**
	 * 进行任务的分配
	 */
	private ThreadPoolContainer<FutureTask<RunResult>> threadPool = new ThreadPoolContainer<FutureTask<RunResult>>();

	/**
	 * 任务队列信息
	 */
	private final ThreadGroupOption<String> groupOper = new ThreadGroupOption<String>(threadPool);
	
	/**
	 * 恢复的任务分配
	 */
	private ThreadPoolContainer<FutureTask<RunResult>> statethreadPool = new ThreadPoolContainer<FutureTask<RunResult>>();

	/**
	 * 恢复队列信息
	 */
	private final ThreadGroupOption<String> stategroupOper = new ThreadGroupOption<String>(statethreadPool);
	
	
	/**
	 * 操作借口
	 * 
	 */
	OperationService<String> operationService = new OperationServiceImpl();
	
	/**
	 * 进行状态恢复的队列的长度
	 */
	private int maxStateThreadPoolSize = 10;

	/**
	 * 最大线程池中运行的线程数量
	 */
	private int maxThreadPoolSize = 10;
	
	/**
	 * 用来执行任务的线程
	 */
	private ExecutorService executors = Executors.newFixedThreadPool(maxThreadPoolSize + maxStateThreadPoolSize);
	
	public void threadInit() {
		// 进行消费者队列的初始化
		for (int i = 0; i < maxThreadPoolSize; i++) {
			executors.submit(new ThreadOperation(threadPool));
		}
		/*// 进行状态恢复的消费队列的初始化
		for (int i = 0; i < maxStateThreadPoolSize; i++) {
			executors.submit(new ThreadOperation(statethreadPool));
		}*/
	}
	
	public static void main(String[] args) throws Exception{
		ThreadStringTest tst = new ThreadStringTest();
		tst.threadInit();
		for(int k=1;k<4;k++){
			tst.execService1(k);
		}
		
		/*for(int d=1;d<d;d++){
			tst.execService2(d);
		}*/
	}
	
	
	public void execService1(int k) throws Exception{
		// 构造线程同时运行数量控制
		Semaphore sema = new Semaphore(3);
		for(int j = 0; j < 5; j++){
			groupOper.putTask(k, operationService, "go"+j, sema);
			Thread.currentThread().sleep(5000);
		}
		groupOper.getRsp(k);
	}
	
	
	
	
	
	public void execService2(int k){
		// 构造线程同时运行数量控制
		Semaphore sema = new Semaphore(3);
		for(int j = 0; j < 5; j++){
			stategroupOper.putTask(k, operationService, "go"+j, sema);
		}
		stategroupOper.getRsp(k);
	}

}
