package com.ibs.thread.demo.main;

public class ParentChildPoolTest extends Thread {

	int numThreads = 10;
	int numTasks = 4;

	public ParentChildPoolTest() {

	}

	public void run() {
		// 生成线程池
		ThreadPool threadPool = new ThreadPool(numThreads);
		// 运行任务
		for (int i = 0; i < numTasks; i++) {
			threadPool.runTask(new ParentChildDoThread());
		}
		// 关闭线程池并等待所有任务完成
		threadPool.join();
		threadPool.close();
		System.out.println("1111111111111111111111");
	}

	public static void main(String[] args) {

	}

}
