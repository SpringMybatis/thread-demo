package com.ibs.thread.demo.liu;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程容器,用来存放线程
 * 
 * 
 * @author zhongjun
 *
 * @param <E> 线程对象
 */
public class ThreadPoolContainer<E> {

	/**
	 * 默认的线程池的大小
	 */
	private int defaultPoolSize = 256;

	/**
	 * 线程池的大小
	 */
	private int poolSize = defaultPoolSize;
	
	
	/**
	 * 构造线程容器  默认256
	 * 
	 */
	public ThreadPoolContainer() {

	}

	/**
	 * 构造线程容器  默认size
	 * 
	 * @param size
	 */
	public ThreadPoolContainer(int size) {
		this.poolSize = size;
	}
	
	
	/**
	 * 加锁信息
	 */
	private Lock lock = new ReentrantLock();
	
	
	/**
	 * 当队列满了，就等待
	 */
	private Condition fullCondition = lock.newCondition();

	
	/**
	 * 队列为空也让其等待
	 */
	private Condition emptyCondition = lock.newCondition();
	
	
	/**
	 * 用来存储消息列队操作
	 */
	private LinkedList<E> queue = new LinkedList<E>();
	
	
	
	/**
	 * 存入线程对象
	 * 
	 * @param item
	 * @throws InterruptedException
	 */
	public void putTask(E item) throws InterruptedException {
		lock.lock();
		try {
			// 队列满了
			if(queue.size()>=poolSize){
				// 等待
				fullCondition.await();
			}
			// 空闲了就加入 线程对象
			queue.add(item);
			// 唤醒在为空等待的队列,可以进行运作
			emptyCondition.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			lock.unlock();
		}
	}
	
	
	/**
	 * 获取队列线程
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public E getLastTask() throws InterruptedException {
		lock.lock();
		E result = null;
		try {
			// 如果当前队列为空，则等待
			while (queue.size() == 0) {
				emptyCondition.await();
			}
			result = queue.getLast();
			// 唤醒已经满的线程，进行继续放入操作
			emptyCondition.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			lock.unlock();
		}
		return result;
	}
	
}
