package com.ibs.thread.demo.jdk;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * @author zhongjun
 * 一种阻塞队列，其中每个插入操作必须等待另一个线程的对应移除操作 ，反之亦然。
 * 同步队列没有任何内部容量，甚至连一个队列的容量都没有。
 * 不能在同步队列上进行 peek，因为仅在试图要移除元素时，该元素才存在；
 * 除非另一个线程试图移除某个元素，否则也不能（使用任何方法）插入元素；
 * 也不能迭代队列，因为其中没有元素可用于迭代。
 * 队列的头 是尝试添加到队列中的首个已排队插入线程的元素；
 * 如果没有这样的已排队线程，则没有可用于移除的元素并且 poll() 将会返回 null。
 * 对于其他 Collection 方法（例如 contains），SynchronousQueue 作为一个空 collection。
 * 此队列不允许 null 元素。 
 *  同步队列类似于 CSP 和 Ada 中使用的 rendezvous 信道。
 *  它非常适合于传递性设计，在这种设计中，在一个线程中运行的对象要将某些信息、事件或任务传递给在另一个线程中运行的对象，它就必须与该对象同步。
 *  
 *  队列竟然是没有内部容量的。
 *  这个队列其实是BlockingQueue的一种实现。
 *  每个插入操作必须等待另一个线程的对应移除操作，反之亦然。
 *  它给我们提供了在线程之间交换单一元素的极轻量级方法 
 *  
 *  SynchronousQueue主要用于单个元素在多线程之间的传递 
 *  
 */
public class SynchronousQueueTest {

	class Producer implements Runnable {
		private BlockingQueue<String> queue;
		List<String> objects = Arrays.asList("one", "two", "three");
		public Producer(BlockingQueue<String> q) {
			this.queue = q;
		}
		@Override
		public void run() {
			try {
				for (String s : objects) {
					queue.put(s);// 产生数据放入队列中
					System.out.printf("put:%s%n", s);
				}
				queue.put("Done");// 已完成的标志
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	class Consumer implements Runnable {
		private BlockingQueue<String> queue;

		public Consumer(BlockingQueue<String> q) {
			this.queue = q;
		}

		@Override
		public void run() {
			String obj = null;
			try {
				while (!((obj = queue.take()).equals("Done"))) {
					System.out.println(obj);// 从队列中读取对象
					Thread.sleep(3000); // 故意sleep，证明Producer是put不进去的
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		BlockingQueue<String> q = new SynchronousQueue<String>();
		SynchronousQueueTest t = new SynchronousQueueTest();
		new Thread(t.new Producer(q)).start();
		new Thread(t.new Consumer(q)).start();
	}

}
