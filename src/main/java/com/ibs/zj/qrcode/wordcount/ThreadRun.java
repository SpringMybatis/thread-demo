package com.ibs.zj.qrcode.wordcount;

import java.util.concurrent.FutureTask;

public class ThreadRun implements Runnable {

	private ThreadContainer<FutureTask<Result>> container = null;

	public ThreadRun(ThreadContainer<FutureTask<Result>> container) {
		this.container = container;
	}

	public void run() {
		// 轮询去取队列消息
		while (true) {
			// 关闭线程池需要的标识
			/*if(container.isShutdownFlag()){
				break;
			}*/
			FutureTask<Result> task = container.get();
			if (null != task) {
				task.run();
			}
		}
	}

}
