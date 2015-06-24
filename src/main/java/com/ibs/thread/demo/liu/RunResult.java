package com.ibs.thread.demo.liu;

import java.io.Serializable;

public class RunResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 操作是否成功
	 */
	private volatile boolean isSuceess = false;

	/**
	 * 当前运行的id信息
	 */
	private int id;

	/**
	 * 组ID
	 */
	private int groupId;

	public boolean isSuceess() {
		return isSuceess;
	}

	public void setSuceess(boolean isSuceess) {
		this.isSuceess = isSuceess;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

}
