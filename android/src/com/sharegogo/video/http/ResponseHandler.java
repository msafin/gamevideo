package com.sharegogo.video.http;

public interface ResponseHandler {
	/**
	 * �ɹ���ȡ���ݣ�������UI�߳�
	 * @param data
	 */
	public void onSuccess(Object data);
	/**
	 * ��ȡ����ʧ�ܣ�������UI�߳�
	 * @param msg
	 */
	public void onFailed(int what,Object msg);
	
	/**
	 * ���ݵĳ־��ԣ������ڹ����߳�
	 * @param data
	 */
	public boolean onPersistent(Object data);
}
