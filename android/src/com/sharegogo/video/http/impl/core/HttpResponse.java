package  com.sharegogo.video.http.impl.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


import java.util.zip.GZIPInputStream;

import com.sharegogo.video.http.impl.utils.DynamicBytes;
import com.sharegogo.video.http.impl.utils.HttpConfig;
import com.sharegogo.video.http.impl.utils.HttpUtils;

/**
 * http响应信息的通用处理，基类
 * @author 		weizhengqin
 * @date:		2012-09-21	
 */
public abstract class HttpResponse{
	
	protected int 					status;
	protected DynamicBytes 			body;
	protected Map<String,String> 	headers;
	
	
	public HttpResponse(){
//		body = new DynamicBytes(HttpConfig.BUFFER_SIZE);
		headers = new HashMap<String, String>();
	}
	
	
	/**
	 * 返回状态码信息
	 * @param statuscode
	 */
	public void onStatusReceived(int statuscode) {
		this.status  = statuscode;
	}
	
	
	/**
	 * 返回http头部信息
	 * @param headers http头部信息
	 */
	public void	onHeadersReceived(Map<String, String> headers) {
		this.headers = headers;
	}
	
	
	/**
	 * 返回部分数据
	 * @param buf	 数据的buf
	 * @param length 数据的长度
	 * @return 
	 */
	public int	onBodyPartReceived(byte[] buf, int length) {
		body.append(buf, 0, length);
		return length;
	}
	 
	
	/**
	 * 数据接收完毕后调用, 虚函数
	 */
	public  void onCompleted(){
		
		//是否加密
		String encode = headers.get(HttpUtils.CONTENT_ENCODE);
		try{
			ByteArrayInputStream bis = new ByteArrayInputStream(
					  						body.get(), 0, body.length());
			InputStream is = bis;
			if (encode != null) {
				encode = encode.toLowerCase();
				// deflate || x-deflate 解压
				if("gzip".equals(encode) || "x-gzip".equals(encode)){
					is = new GZIPInputStream(bis);
				}
				int read = 0;
				byte[] buffer = new byte[4096];
				DynamicBytes unzipped = new DynamicBytes(body.length());
				while ((read = is.read(buffer)) != -1) {
				  unzipped.append(buffer, 0, read);
				}
				body = unzipped;
			}
			
		}catch (IOException e) {
				e.printStackTrace();
		}

	}
	
	
	/**
	 * 请求失败抛出异常，虚函数
	 * @param t
	 */
	public abstract void onThrowable(int t);
}