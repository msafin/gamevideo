package  com.sharegogo.video.http.impl.utils;

import java.nio.charset.Charset;
import java.util.Map;


/**
 * Http
 * 
 * @author weizhengqin
 * @date: 2012-09-25
 */
public class HttpUtils{
	
	// http头部 一些默认值
	public static final String CHARSET = "charset=";
    public static final Charset ASCII = Charset.forName("US-ASCII");
    public static final Charset UTF_8 = Charset.forName("utf8");
    
	public static final String ACCEPT_TYPE = "*/*";
	public static final String DEFUALT_REFERER = "artistapp.music.baidu.com";
	public static final String KEEP_ALIVE_CONN_TYPE = "Keep-Alive";
	public static final String OCTET_STREAM = "application/octet-stream";
	public static final String COMMON_CONTENT_TYPE = "application/x-www-form-urlencoded";
	public static final String DEFAULT_USER_AGENT ="artistapp_1.0.0";
	
	//返回的头部信息
	public static final String CONTENT_ENCODE="CONTENT-ENCODEING";
	public static final String CONTENT_LENGTH="CONTENT-LENGTH";
	public static final String CONTENT_TYPE="CONTENT-TYPE";
	public static final String CONNECTION="Connection";
	
	//response code 
	public static final int  HTTP_100_CONTINUE=100;

	public static final int  HTTP_200_OK =200;
	public static final int	 HTTP_206_PARTIAL=206;
	
	public static final int	 HTTP_404_NOTFOUND=404;
	public static final int	 HTTP_400_BADREQUEST=400;
	public static final int  HTTP_403_FORBIDDEN = 403;
	public static final int  HTTP_416_RANGE_ERR = 416;
	
	/**
	 * 默认的代理端口号
	 */
	public final static int DEFAULT_PROXY_PORT = 80;
	
	
	public static final int HTTP_REQUEST_CANCEL = 0;
	
}