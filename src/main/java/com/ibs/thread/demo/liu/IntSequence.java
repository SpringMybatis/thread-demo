package com.ibs.thread.demo.liu;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自增加长的序列化
 * 
 * @author liujun
 * 
 * @date 2014年6月10日
 * @vsersion 0.0.1
 */
public class IntSequence {

	private static final IntSequence SeqINstance = new IntSequence();

	/**
	 * 得到实例对象信息
	 * 
	 * @return
	 */
	public static IntSequence getInstance() {
		return SeqINstance;
	}

	AtomicInteger seqValue = new AtomicInteger(0);

	/**
	 * 以原子方式进行加1 ，得到序列值
	 * 
	 * @return
	 */
	public int nextSeqValue() {
		boolean exec = seqValue.compareAndSet(Integer.MAX_VALUE, 0);

		if (!exec) {
			return seqValue.incrementAndGet();
		}

		return 0;
	}

	/**
	 * 得到当前序列的值
	 * 
	 * @return
	 */
	public int getCurrSeqValue() {
		return seqValue.get();
	}

	public static void main(String[] args) {
		Thread thr = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					System.out.println(IntSequence.getInstance().nextSeqValue());
				}

			}
		});

		thr.start();

		Thread thr3 = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					System.out.println(IntSequence.getInstance().nextSeqValue());
				}

			}
		});

		thr3.start();
	}
}
