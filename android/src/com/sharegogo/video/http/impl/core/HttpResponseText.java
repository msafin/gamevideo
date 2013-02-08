package  com.sharegogo.video.http.impl.core;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.sharegogo.video.http.ITextHandler;
import com.sharegogo.video.http.impl.utils.DynamicBytes;
import com.sharegogo.video.http.impl.utils.HttpConfig;
import com.sharegogo.video.http.impl.utils.HttpUtils;


/**
 * 处理http响应的文本信息
 * @author 		weizhengqin
 * @date:		2012-10-08	
 */
public class  HttpResponseText extends HttpResponse{


	private  ITextHandler 	 	handler;
		
	public HttpResponseText(ITextHandler h){
		handler = h;
	}
	
	
	/**
	 * 返回http头部信息
	 * @param headers http头部信息
	 */
	public void	onHeadersReceived(Map<String, String> headers) {
		super.onHeadersReceived(headers);
		String length = headers.get(HttpUtils.CONTENT_LENGTH);
		if(length != null && length.length()!=0){
			body = new DynamicBytes(Integer.parseInt(length+1));
		}else{
			body = new DynamicBytes(HttpConfig.BUFFER_SIZE);
		}
	}
	

	@Override
	public void onCompleted() {
		super.onCompleted();
		String result = null;
		try {
			//result = new String(body.get(),detectCharset());
			result = new String(body.get());
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		if(handler != null)
		{
			handler.onHandleSuccess(status, headers, result);
			handler.onHandleSuccess(status, headers, body.get(),body.length());
		}
		
	}

	@Override
	public void onThrowable(int code) {
		if(handler != null)
			handler.onThrowable(code);	
	}
	
	
	/////////////////////////////////////////////////////////////////////////
	//内部函数
	
	//判断字符集，默认为utf-8
	public String detectCharset(){
		Charset result = parseCharset(headers.get(HttpUtils.CONTENT_TYPE));
		if(result == null){
			result=HttpConfig.CHARSET_DEFAULT_ENCODE;
		}
		
		return result.toString();
	}

	
	
	//从http头部信息得到使用的字符集
    public static Charset parseCharset(String type) {
        if (type != null) {
            try {
                type = type.toLowerCase();
                int i = type.indexOf(HttpUtils.CHARSET);
                if (i != -1) {
                    String charset = type.substring(i + HttpUtils.CHARSET.length())
                            .trim();
                    return Charset.forName(charset);
                }
            } catch (Exception ignore) {
            }
        }
        return null;
    }
}