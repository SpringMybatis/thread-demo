package com.ibs.thread.demo.jdk;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author zhongjun 
 * 对于某个容器，我们规定，最多只能容纳n个线程同时操作使用信号量来模拟实现 
 * 
 * Semaphore通常用于对象池的控制
 */
public class SemaphoreTest {

	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		SemaphoreTest t = new SemaphoreTest();
		final BoundedHashSet<String> set = t.getSet();

		// 三个线程同时操作add
		for (int i = 0; i < 3; i++) {
			exec.execute(new Runnable() {
				public void run() {
					try {
						set.add(Thread.currentThread().getName());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		}

		// 三个线程同时操作remove
		for (int j = 0; j < 3; j++) {
			exec.execute(new Runnable() {
				public void run() {
					set.remove(Thread.currentThread().getName());
				}
			});
		}
		exec.shutdown();

	}

	public BoundedHashSet<String> getSet() {
		return new BoundedHashSet<String>(2);// 定义一个边界约束为2的线程
	}

	/**
	 * 定义一个BoundedHashSet
	 * @author zhongjun
	 * @param <T>
	 */
	class BoundedHashSet<T> {
		private final Set<T> set;
		private final Semaphore semaphore;

		public BoundedHashSet(int bound) {
			this.set = Collections.synchronizedSet(new HashSet<T>());
			this.semaphore = new Semaphore(bound, true);
		}

		/**
		 * 添加对象
		 * @param o
		 * @throws InterruptedException
		 */
		public void add(T o) throws InterruptedException {
			semaphore.acquire();// 信号量控制可访问的线程数目
			set.add(o);
			System.out.printf("add:%s%n", o);
		}

		/**
		 * 删除对象
		 * @param o
		 */
		public void remove(T o) {
			if (set.remove(o)){
				semaphore.release();// 释放掉信号量
			}
			System.out.printf("remove:%s%n", o);
		}
	}

}
