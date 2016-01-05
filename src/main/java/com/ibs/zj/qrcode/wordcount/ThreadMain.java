package com.ibs.zj.qrcode.wordcount;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;

public class ThreadMain {

	/**
	 * 线程池里面的线程数目
	 * 
	 */
	private static int maxThreadPoolSize = 10;

	/**
	 * 初始化线程池
	 * 
	 */
	private static ExecutorService executors = Executors.newFixedThreadPool(maxThreadPoolSize);

	/**
	 * 线程容器队列
	 * 
	 */
	private static ThreadContainer<FutureTask<Result>> container = new ThreadContainer<FutureTask<Result>>();
	
	/**
	 * 添加消息的service
	 * 
	 */
	private static PutTaskService putTaskService = new PutTaskService(container);

	/**
	 * 统计单词的service
	 * 
	 */
	private static CountSevice countSevice = new CountSeviceImpl();
	
	public static void main(String[] args) throws Exception {
		// 读取消息队列的线程
		for (int i = 0; i < maxThreadPoolSize; i++) {
			executors.submit(new ThreadRun(container));
		}
		// 获取文件的List
		List<String> list = CountUtil.getFileList();
		// 定义读取的信号量--假设4个
		Semaphore semaphore = new Semaphore(4);
		// 文件数 
		int fileSzie = list.size();
		// 同步栅栏
		CyclicBarrier cyclicBarrier = new CyclicBarrier(fileSzie, new Runnable() {
			public void run() {
				System.out.println("单词统计完毕,以下是输出结果:");
				System.out.println();
			}
		});
		// 线程执行完毕信号
		CountDownLatch doneSignal = new CountDownLatch(fileSzie);  
		// 数据存储的Map
		ConcurrentHashMap<String,Integer> dataMap = new ConcurrentHashMap<String,Integer>(); 
		// 线程队列添加任务
		for(int i=0;i<fileSzie;i++){
			String fileName = list.get(i);
			putTaskService.putTask(countSevice,fileName,semaphore,cyclicBarrier,doneSignal,dataMap);
		}
		// 多线程跑完
		doneSignal.await();
		// 结果排序
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();  
		List<Entry<String, Integer>> resultList = new ArrayList<Entry<String,Integer>>(dataMap.entrySet());
		Collections.sort(resultList, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> o1,Entry<String, Integer> o2) {
				int value1 = o1.getValue();  
				int value2 = o2.getValue();  
				return value2 - value1;  
			}  
		});
		Iterator<Map.Entry<String, Integer>> iter = resultList.iterator();  
        Map.Entry<String, Integer> tmpEntry = null;  
        while (iter.hasNext()) {  
            tmpEntry = iter.next();  
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        } 
		// 输出运行结果
		for(Entry<String, Integer> entry:sortedMap.entrySet()){
			System.out.println(entry.getKey()+"="+sortedMap.get(entry.getKey()));
		}
		// 关闭线程池
		// container.setShutdownFlag(true);
		executors.shutdown();
	}

}
