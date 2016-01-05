package com.ibs.zj.qrcode.wordcount;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class CountWorld {

	public static void main(String[] args) {
		final String word = "This that is a good boy. I have 100 yuan that who 1 2 2 2 22 am i, hello world.";
		final ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<String, Integer>();
		CountUtil.countWorld(word, map);
		for(Entry<String, Integer> entry : map.entrySet()){
			System.out.println(entry.getKey()+"----"+map.get(entry.getKey()));
		}
	}

}
