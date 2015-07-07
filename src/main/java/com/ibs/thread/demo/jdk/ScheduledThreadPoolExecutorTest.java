package com.ibs.thread.demo.jdk;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhongjun 
 * 我们定期让定时器抛异常 
 * 我们定期从控制台打印系统时间
 * 
 * a.	Timer是基于绝对时间的。容易受系统时钟的影响。 
 * b.	Timer只新建了一个线程来执行所有的TimeTask。所有TimeTask可能会相关影响 
 * c.	Timer不会捕获TimerTask的异常，只是简单地停止。这样势必会影响其他TimeTask的执行。 
 * 
 * 是时候把你的定时器换成 ScheduledThreadPoolExecutor了 
 */
public class ScheduledThreadPoolExecutorTest {

	public static void main(String[] args) {
		ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);

		exec.scheduleAtFixedRate(new Runnable() {// 每隔一段时间就触发异常
					@Override
					public void run() {
						throw new RuntimeException();
					}
				}, 1000, 5000, TimeUnit.MILLISECONDS);

		exec.scheduleAtFixedRate(new Runnable() {// 每隔一段时间打印系统时间，证明两者是互不影响的
					@Override
					public void run() {
						System.out.println(System.nanoTime());
					}
				}, 1000, 2000, TimeUnit.MILLISECONDS);

	}

}
