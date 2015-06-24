package com.ibs.thread.demo.main;

import java.util.ArrayList;

public class ParentChildMain {

	private static ArrayList list = new ArrayList();
	private static int controlNum = 0;
	private static int listNum = 0;

	public ParentChildMain() {
		for (int i = 0; i < 50; i++) {
			list.add(String.valueOf(i));
		}
		listNum = list.size();
		ParentChildPoolTest test = new ParentChildPoolTest();
		test.start();
	}

	public static synchronized String getControlObj() {
		if (listNum == controlNum) {
			return null;
		}
		String retValue = (String) list.get(controlNum);
		controlNum++;
		return retValue;
	}

	public static void main(String[] args) {
		ParentChildMain main = new ParentChildMain();
	}

}
