package com.ibs.thread.demo.liu;

import java.util.concurrent.Semaphore;

/**
 * 线程操作接口
 * 
 * @author zhongjun
 *
 */
public interface OperationService<E> {

	/**
	 * 用于多线程进行操作的方法
	 * @param sema 用来控制线程在运行时的数量
	 * @param param 参数信息
	 * @return 操作结果 true 成功 ，false 失败
	 * @throws Exception
	 */
	public boolean doinvoke(Semaphore sema,E param)throws Exception;
	
}
