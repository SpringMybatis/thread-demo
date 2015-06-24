package com.ibs.thread.demo.liu;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;

/**
 * 组操作集合
 * 
 * @author zhongjun
 *
 * @param <E>
 */
public class ThreadGroupOption<E> {

	
	/**
	 * 用来存放临时运行线程信息
	 */
	private ConcurrentHashMap<Integer, Map<Integer, FutureTask<RunResult>>> putGroup = new ConcurrentHashMap<Integer, Map<Integer, FutureTask<RunResult>>>();

	
	/**
	 * 线程队列信息
	 */
	private ThreadPoolContainer<FutureTask<RunResult>> threadPool;
	
	
	/**
	 * 构造线程队列
	 * 
	 * @param threadPool 线程容器
	 */
	public ThreadGroupOption(ThreadPoolContainer<FutureTask<RunResult>> threadPool){
		this.threadPool = threadPool;
	}
	
	/**
	 * 存入组信息
	 * 
	 * 
	 * @param groupId
	 * @param oper
	 * @param runParam
	 * @param sema
	 */
	public void putTask(int groupId, final OperationService<E> operationService, final E runParam, final Semaphore sema) {
		// 从临时组取出组信息
		Map<Integer, FutureTask<RunResult>> fuMap = putGroup.get(groupId);
		// 如果没有组信息
		if(fuMap==null){
			fuMap = new ConcurrentHashMap<Integer, FutureTask<RunResult>>();
		}
		// 获取组ID
		int id = IntSequence.getInstance().nextSeqValue();
		// 运行结果封装
		final RunResult runResult = new RunResult();
		runResult.setId(id);
		runResult.setGroupId(groupId);
		// 进行多线程的任务运行
		FutureTask<RunResult> task = new FutureTask<RunResult>(new Callable<RunResult>() {
			@Override
			public RunResult call() throws Exception {
				long starttime = System.currentTimeMillis();
				boolean inrsp = operationService.doinvoke(sema, runParam);
				runResult.setSuceess(inrsp);
				long endTime = System.currentTimeMillis();
				System.out.println("运行时间:"+(endTime-starttime));
				return runResult;
			}
		});
		// 组信息
		fuMap.put(id, task);
		// 存入临时组
		putGroup.put(groupId, fuMap);
		try {
			threadPool.putTask(task);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			System.out.println("put success:"+groupId);
		}
	}
	
	/**
	 * 得到当前组操作的结果信息
	 * 
	 * @param groupId
	 * @return
	 */
	public boolean getRsp(int groupId) {
		boolean rsp = true;
		try {
			Map<Integer, FutureTask<RunResult>> fuMap = putGroup.get(groupId);
			if (null != fuMap && !fuMap.isEmpty()) {
				Iterator<Entry<Integer, FutureTask<RunResult>>> runIter = fuMap.entrySet().iterator();
				while (runIter.hasNext()) {
					Entry<Integer, FutureTask<RunResult>> entry = runIter.next();
					FutureTask<RunResult> futr = entry.getValue();
					RunResult thSRsp = futr.get();
					rsp = thSRsp.isSuceess();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return rsp;
	}
	
	
	
	/**
	 * 进行清除操作
	 * 
	 * @param groupId
	 */
	public void cleanGroupOperation(int groupId) {
		putGroup.remove(groupId);
	}
	
}
