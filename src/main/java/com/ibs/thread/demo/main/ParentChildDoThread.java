package com.ibs.thread.demo.main;

public class ParentChildDoThread extends Thread {

	public void run() {
		String value = ParentChildMain.getControlObj();
		while (value != null) {
			System.out.println("this is test:" + value);
			try {
				this.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			value = ParentChildMain.getControlObj();
		}
	}

}
