package com.ibs.zj.qrcode.wordcount;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public interface CountSevice {

	public boolean countWorld(String runParams, Semaphore semaphore,
			CyclicBarrier cyclicBarrier, CountDownLatch doneSignal,
			ConcurrentHashMap<String, Integer> dataMap);

}
