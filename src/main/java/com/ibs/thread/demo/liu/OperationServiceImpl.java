package com.ibs.thread.demo.liu;

import java.util.concurrent.Semaphore;

public class OperationServiceImpl implements OperationService<String> {

	public Semaphore maxRunThread;

	@SuppressWarnings("static-access")
	@Override
	public boolean doinvoke(Semaphore sema, String param) throws Exception {
		// 得到当前操作的许可
		sema.acquire();
		boolean exec = false;
		try {
			Thread.currentThread().sleep(3*1000);
			System.out.println("休息3秒:true");
			exec = true;
		} catch (Exception e) {
			throw e;
		} finally {
			// 释放操作的许可
			sema.release();
		}
		return exec;
	}
	
}
