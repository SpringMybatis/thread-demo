package com.ibs.thread.demo.jdk;

import java.util.ArrayList;
import java.util.concurrent.Exchanger;

/**
 * @author zhongjun 
 * 应用举例：有两个缓存区，两个线程分别向两个缓存区fill和take，当且仅当一个满了，两个缓存区交换
 * Exchanger在特定的使用场景比较有用（两个伙伴线程之间的数据交互） 
 */
public class ExchangerTest {

	public static void main(String[] args) {
		final Exchanger<ArrayList<Integer>> exchanger = new Exchanger<ArrayList<Integer>>();
		// 两个缓存区
		final ArrayList<Integer> buff1 = new ArrayList<Integer>(10);
		final ArrayList<Integer> buff2 = new ArrayList<Integer>(10);

		new Thread(new Runnable() {
			@Override
			public void run() {
				ArrayList<Integer> buff = buff1;
				try {
					while (true) {
						if (buff.size() >= 10) {
							// 开始跟另外一个线程交互数据
							buff = exchanger.exchange(buff);
							System.out.println("exchange buff1");
							buff.clear();
						}
						buff.add((int) (Math.random() * 100));
						Thread.sleep((long) (Math.random() * 1000));
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				ArrayList<Integer> buff = buff2;
				while (true) {
					try {
						for (Integer i : buff) {
							System.out.println(i);
						}
						Thread.sleep(1000);
						// 开始跟另外一个线程交换数据
						buff = exchanger.exchange(buff);
						System.out.println("exchange buff2");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}
