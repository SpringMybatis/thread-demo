package com.ibs.thread.demo.jdk;

import java.util.concurrent.CountDownLatch;

/**
 * @author zhongjun
 * CountDownLatch的使用举例
 * 作用：管理一组线程，等待所有的线程执行完毕，在去执行主线程
 * 列子：十个线程从1数到100,数完之后在输出OK,否则一直等待
 *     假设我们要打印1-100，最后再输出“Ok“。
 *     1-100的打印顺序不要求统一，只需保证“Ok“是在最后出现即可
 * CounDownLatch对于管理一组相关线程非常有用。上述示例代码中就形象地描述了两种使用情况。
 * 第一种是计算器为1，代表了两种状态，开关。
 * 第二种是计数器为N，代表等待N个操作完成。
 * 今后我们在编写多线程程序时，可以使用这个构件来管理一组独立线程的执行
 */
public class CountDownLatchTest {
	
	private static final int countN = 10;

	public static void main(String[] args) throws Exception{
		// 线程执行完毕信号
		CountDownLatch doneSignal = new CountDownLatch(countN);  
        // 开始执行信号
		CountDownLatch startSignal = new CountDownLatch(1);
		for (int i = 1; i <= countN; i++) {  
			// 线程启动了  
            new Thread(new CountDownThread(i, doneSignal, startSignal)).start();
        }  
        System.out.println("begin------------");
        // 线程开始执行信号  
        startSignal.countDown();
        // 等待所有的线程执行完毕  
        doneSignal.await();
        
        System.out.println("Ok");  
		
	}
	
	/**
	 * 线程类
	 * 
	 * @author zhongjun
	 *
	 */
	static class CountDownThread implements Runnable{

		private final CountDownLatch doneSignal;  
        private final CountDownLatch startSignal;  
        private int beginIndex;  
  
        CountDownThread(int beginIndex, CountDownLatch doneSignal,  
                CountDownLatch startSignal) {  
            this.startSignal = startSignal;  
            this.beginIndex = beginIndex;  
            this.doneSignal = doneSignal;  
        }  
		
		@Override
		public void run() {
			try {
				// 等待开始执行信号的发布  
				startSignal.await();
				beginIndex = (beginIndex - 1) * 10 + 1;  
                for (int i = beginIndex; i <= beginIndex + 10; i++) {  
                    System.out.println(i);  
                } 
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// 执行完毕信号+1
				doneSignal.countDown();
			}
		}
		
	}
	
}
