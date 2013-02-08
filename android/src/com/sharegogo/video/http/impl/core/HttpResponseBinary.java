package  com.sharegogo.video.http.impl.core;

import java.util.Map;

import com.sharegogo.video.http.IBinaryHandler;


/**
 * 处理http响应返回的二进制信息
 * @author 		weizhengqin
 * @date:		2012-09-21	
 */
public class  HttpResponseBinary extends HttpResponse{

	
	private IBinaryHandler  handler;
	
	public HttpResponseBinary( IBinaryHandler  h){
		this.handler = h;
	}
	
	
	/**
	 * 收到头部信息
	 */
	@Override
	public void onHeadersReceived(Map<String, String> headers) {
		// TODO Auto-generated method stub
		super.onHeadersReceived(headers);
		if(handler != null)
		{
			handler.onHeadersReceived(headers);
		}
	}


	/**
	 * 收到部分数据
	 */
	@Override
	public int onBodyPartReceived(byte[] buf, int length) {
//		super.onBodyPartReceived(buf, length);
		if(handler != null){
			handler.onHandleBodyPart(buf, length);
		}
		return length;
	}

	/**
	 * 收到全部数据
	 */
	@Override
	public void onCompleted() {
//		super.onCompleted();
		if(handler != null){
			handler.onHandleSuccess(status, headers);
		}
	}

	/**
	 * 异常处理
	 */
	@Override
	public void onThrowable(int code) {
		if(handler!=null)
			handler.onThrowable(code);
		
	}
	
	
	
}