package  com.sharegogo.video.http;

import java.util.Map;

/**
 * 业务相关接口，调用者实现，处理http响应返回的文本信息
 * @author 		weizhengqin
 * @date:		2012-09-21	
 */
public interface  ITextHandler{
	
	/**
	 * 处理请求服务器返回的文本信息
	 * @param status	http状态码
	 * @param headers	http头部信息
	 * @param body		http文本信息
	 */
	public void onHandleSuccess(int status, Map<String, String> headers, String body);
	
	
	/**
	 * 处理请求服务器返回的二进制信息
	 * @param status	http状态码
	 * @param headers	http头部信息
	 * @param body		http二进制
	 */
	public void onHandleSuccess(int status, Map<String, String> headers, byte[] body,int length);
	
	/**
	 * 请求失败，抛回异常
	 * @param t
	 */
	public void onThrowable(int code);
	
}