package com.ibs.thread.demo.jdk;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author zhongjun
 * “支持两个附加操作的 Queue，这两个操作是：获取元素时等待队列变为非空，以及存储元素时等待空间变得可用。“
 * 这里我们主要讨论BlockingQueue的最典型实现：LinkedBlockingQueue 和ArrayBlockingQueue。
 * 两者的不同是底层的数据结构不够，一个是链表，另外一个是数组。 
 * 后面将要单独解释其他类型的BlockingQueue和SynchronousQueue 
 * BlockingQueue的经典用途是 生产者-消费者模式
 */
public class BlockingQueueTest {
	public static void main(String[] args) {
		final BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>(3);
		final Random random = new Random();

		class Producer implements Runnable {
			@Override
			public void run() {
				while (true) {
					try {
						int i = random.nextInt(100);
						System.out.println("put:"+i);
						queue.put(i);// 当队列达到容量时候，会自动阻塞的
						if (queue.size() == 3) {
							System.out.println("full");
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		class Consumer implements Runnable {
			@Override
			public void run() {
				while (true) {
					try {
						Integer i = queue.take();// 当队列为空时，也会自动阻塞
						System.out.println("take:"+i);
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		new Thread(new Producer()).start();
		new Thread(new Consumer()).start();
	}
}
