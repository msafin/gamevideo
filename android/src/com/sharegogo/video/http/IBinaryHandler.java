package  com.sharegogo.video.http;

import java.util.Map;

/**
 * 业务相关接口，调用者实现，处理http响应返回的二进制信息
 * @author 		weizhengqin
 * @date:		2012-09-21	 
 */
public interface  IBinaryHandler{
	
	/**
	 * 返回头部信息
	 * @param headers
	 */
	public void	onHeadersReceived(Map<String, String> headers);
	
	
	/**
	 * 处理请求服务器返回的二进制信息
	 * @param status    状态码
	 * @param headers   头部信息
	 * @param body		二进制信息
	 */
	public void onHandleSuccess(int status, Map<String, String> headers);
	
	
	/**
	 * 处理请求服务器返回的二进制信息
	 * @param status
	 * @param partbody
	 */
	public void onHandleBodyPart(byte[] partbody, int length );
	
	
	/**
	 * 请求失败抛出异常
	 * @param t
	 */
	public void onThrowable(int code);
	
	
}