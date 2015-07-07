package com.ibs.thread.demo.jdk;

import java.util.Random;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author zhongjun
 * 它是包含Delayed 元素的一个无界阻塞队列，只有在延迟期满时才能从中提取元素。
 * 该队列的头部 是延迟期满后保存时间最长的 Delayed 元素。
 * 如果延迟都还没有期满，则队列没有头部，并且 poll 将返回 null。
 * 当一个元素的 getDelay(TimeUnit.NANOSECONDS) 方法返回一个小于等于 0 的值时，将发生到期。
 * 即使无法使用 take 或 poll 移除未到期的元素，也不会将这些元素作为正常元素对待。
 * 例如，size 方法同时返回到期和未到期元素的计数。此队列不允许使用 null 元素。
 */
public class DelayQueueTest {

	private class Stadium implements Delayed {
		long trigger;

		public Stadium(long i) {
			trigger = System.currentTimeMillis() + i;
		}

		@Override
		public long getDelay(TimeUnit arg0) {
			long n = trigger - System.currentTimeMillis();
			return n;
		}

		@Override
		public int compareTo(Delayed arg0) {
			return (int) (this.getDelay(TimeUnit.MILLISECONDS) - arg0.getDelay(TimeUnit.MILLISECONDS));
		}

		public long getTriggerTime() {
			return trigger;
		}

	}

	public static void main(String[] args) throws Exception {
		Random random = new Random();
		DelayQueue<Stadium> queue = new DelayQueue<Stadium>();
		DelayQueueTest t = new DelayQueueTest();
		for (int i = 0; i < 5; i++) {
			int it = random.nextInt(30000);
			queue.add(t.new Stadium(it));
		}
		// Thread.sleep(2000);
		while (true) {
			Stadium s = queue.take();// 延时时间未到就一直等待
			if (s != null) {
				System.out.println(System.currentTimeMillis() - s.getTriggerTime());// 基本上是等于0
			}
			if (queue.size() == 0)
				break;
		}
	}
}
