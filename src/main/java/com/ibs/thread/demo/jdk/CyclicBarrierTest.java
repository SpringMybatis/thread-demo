package com.ibs.thread.demo.jdk;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhongjun
 * CyclicBarrier的使用举例
 * 作用：所有的线程都准备好了，再去执行某一件事情
 * 例子：今天晚上我们哥们4个去Happy。就互相通知了一下：
 *     晚上八点准时到xx酒吧门前集合，不见不散！。有个哥们住的近，早早就到了。
 *     有的事务繁忙，刚好踩点到了。无论怎样，先来的都不能独自行动，只能等待所有人
 *     
 * CyclicBarrier就是一个栅栏，等待所有线程到达后再执行相关的操作。barrier 在释放等待线程后可以重用     
 */
public class CyclicBarrierTest {

	private static final int PARTIS = 4;
	
	public static void main(String[] args) {
		// 创建线程池
		ExecutorService threadPool = Executors.newCachedThreadPool();
		
		int parties = PARTIS;
		
		final Random random=new Random();  
		
		final CyclicBarrier cyclicBarrier = new CyclicBarrier(parties, new Runnable() {
			@Override
			public void run() {
				System.out.println("4个哥们都到齐了,let's go......");
			}
		});
		
		for(int i=0;i<4;i++){  
			threadPool.execute(new Runnable(){  
                @Override  
                public void run() {  
                    try {  
                        Thread.sleep(random.nextInt(1000));  
                        System.out.println(Thread.currentThread().getName()+"到了，其他哥们呢"); 
                        // 等待其他哥们  
                        cyclicBarrier.await();
                    } catch (InterruptedException e) {  
                        e.printStackTrace();  
                    } catch (BrokenBarrierException e) {
						e.printStackTrace();
					}  
                }
           });  
        }  
		threadPool.shutdown(); 
	}

}
