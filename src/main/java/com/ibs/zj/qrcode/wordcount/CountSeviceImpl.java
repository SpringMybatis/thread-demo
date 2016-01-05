package com.ibs.zj.qrcode.wordcount;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class CountSeviceImpl implements CountSevice {

	public boolean countWorld(String runParams, Semaphore semaphore,
			CyclicBarrier cyclicBarrier, CountDownLatch doneSignal,
			ConcurrentHashMap<String, Integer> dataMap) {
		Boolean flag = false;
		try {
			// 获取信号量
			semaphore.acquire();
			// 创建文件
			File file = new File(runParams);
			// 字符拼接
			StringBuilder sb = new StringBuilder();
			// 判断文件是否存在
			if (file.isFile() && file.exists()) { 
				// 考虑到编码格式
				InputStreamReader read = new InputStreamReader(new FileInputStream(file),"UTF-8");
				// 输入流reader
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					sb.append(lineTxt);
				}
				read.close();
				bufferedReader.close();
			} else {
				System.out.println("找不到指定的文件");
			}
			// 处理单词
			if(!"".equals(sb.toString())){
				CountUtil.countWorld(sb.toString(), dataMap);
				flag = true;
			}
			semaphore.release();
			doneSignal.countDown();
			cyclicBarrier.await();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return flag;
	}

}
