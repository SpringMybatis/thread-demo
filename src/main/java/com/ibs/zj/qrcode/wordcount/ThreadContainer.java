package com.ibs.zj.qrcode.wordcount;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadContainer<V> {

	/**
	 * 容器最大容量
	 * 
	 */
	private int maxNum = 256;

	/**
	 * 容器的大小
	 */
	private int containerSize = maxNum;
	
	/*private boolean shutdownFlag = false;

	public boolean isShutdownFlag() {
		return shutdownFlag;
	}

	public void setShutdownFlag(boolean shutdownFlag) {
		this.shutdownFlag = shutdownFlag;
	}*/

	/**
	 * 无参构造
	 */
	public ThreadContainer() {

	}

	/**
	 * 有参数构造
	 * 
	 * @param size
	 */
	public ThreadContainer(int size) {
		this.containerSize = size;
	}

	/**
	 * 加锁
	 * 
	 */
	private Lock lock = new ReentrantLock();

	/**
	 * 容器队列空的时候加锁
	 * 
	 */
	private Condition emptyLock = lock.newCondition();

	/**
	 * 容器队列满了时候加锁
	 * 
	 */
	private Condition fullLock = lock.newCondition();

	/**
	 * 存储线程的队列
	 * 
	 */
	private LinkedList<V> linkedList = new LinkedList<V>();

	/**
	 * 放入消息队列
	 * 
	 * @param v
	 */
	public void put(V v) {
		lock.lock();
		try {
			while (linkedList.size() >= containerSize) {
				// 满则等待
				fullLock.await();
			}
			linkedList.add(v);
			// 唤醒
			emptyLock.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 取出消息
	 * 
	 * @return
	 */
	public V get() {
		lock.lock();
		V v = null;
		try {
			while (linkedList.size() == 0) {
				// 满则等待
				emptyLock.await();
			}
			v = linkedList.removeLast();
			// 唤醒
			fullLock.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return v;
	}

}
