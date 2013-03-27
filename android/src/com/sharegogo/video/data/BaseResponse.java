package com.sharegogo.video.data;

/**
 * 服务器应答基类
 * @author Raymon
 *
 */
public class BaseResponse {
	static final public int STATUS_OK = 1;
	static final public int STATUS_ERROR = 0;
	
	public int status;
	public String msg;
}
