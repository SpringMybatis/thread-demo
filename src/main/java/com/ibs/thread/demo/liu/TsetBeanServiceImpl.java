package com.ibs.thread.demo.liu;

import java.util.concurrent.Semaphore;

public class TsetBeanServiceImpl implements OperationService<ParamsBean> {

	public Semaphore maxRunThread;
	
	@SuppressWarnings("static-access")
	@Override
	public boolean doinvoke(Semaphore sema, ParamsBean param) throws Exception {
		// 得到当前操作的许可
		sema.acquire();
		boolean exec = false;
		try {
			Thread.currentThread().sleep(5*1000);
			System.out.println("休息5秒:true,"+param.getId()+":"+param.getSql());
			exec = true;
		} catch (Exception e) {
			throw e;
		} finally {
			// 进行释放许可操作
			sema.release();
		}
		return exec;
	}

}
