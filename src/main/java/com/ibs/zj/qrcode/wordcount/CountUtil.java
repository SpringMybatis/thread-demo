package com.ibs.zj.qrcode.wordcount;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CountUtil {

	
	/**
	 * 判断单个字符
	 * 
	 */
	private static Pattern pattern = Pattern.compile("^[a-zA-Z]|[0-9]$");
	
	
	/**
	 * 文件名存储的list
	 */
	private static List<String> list = new ArrayList<String>();

	static {
		Properties pps = null;
		InputStream in = null;
		try {
			pps = new Properties();
			in = ThreadMain.class.getClassLoader().getResourceAsStream("qrcode.properties");
			pps.load(in);
			String path = pps.getProperty("multi.thread.dir");
			File file = new File(path);
			File[] tempList = file.listFiles();
			for(File f:tempList){
				if(f.isFile()){
					list.add(f.getPath());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 处理单词，记录个数
	 * 
	 * @param word
	 * @param map
	 */
	public static synchronized void countWorld(final String word,final ConcurrentHashMap<String, Integer> map){
		// 对字符串分组
		String[] array = word.split("\\.");
		for (String s : array) {
			// 单词的分割
			BreakIterator boundary = BreakIterator.getWordInstance();
			boundary.setText(s);
			int start = boundary.first();
			for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()){
				String sub = s.substring(start, end);
				if(sub.length()>1){
					processCount(sub, map);
				}else if(sub.length() == 1){
					Matcher matcher = pattern.matcher(sub);
					if ( matcher.matches() ) {
						processCount(sub, map);
					}
				}
			}
		}
	}
	
	/**
	 * 记录次数
	 * 
	 * @param word
	 * @param map
	 */
	public static void processCount(final String word,final ConcurrentHashMap<String, Integer> map){
		Integer number = map.get(word);
		if(null!=number){
			number = number + 1;
		}else{
			number = 1;
		}
		map.put(word, number);
	}
	
	/**
	 * 获取文件list
	 * 
	 * @return
	 */
	public static List<String> getFileList(){
		return list;
	}
	
	
	public static void main(String[] args) {
		for(String s:list){
			System.out.println(s);
		}
	}
	
}
