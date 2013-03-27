package com.sharegogo.video.http;

public interface ResponseHandler {
	/**
	 * 成功获取数据，运行在UI线程
	 * @param data
	 */
	public void onSuccess(Object data);
	/**
	 * 获取数据失败，运行在UI线程
	 * @param msg
	 */
	public void onFailed(int what,Object msg);
	
	/**
	 * 数据的持久性，运行在工作线程
	 * @param data
	 */
	public boolean onPersistent(Object data);
}
