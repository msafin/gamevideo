package  com.sharegogo.video.http.impl.utils;

import java.nio.charset.Charset;
import java.util.Map;


/**
 * Http配置项
 * 
 * @author weizhengqin
 * @date: 2012-09-25
 */
public class HttpConfig{
	
	// 连接超时时长
	public static final int CONN_TIME_OUT = 10 * 1000;
	// 读取数据超时时间
	public static final int READ_TIME_OUT = 30 * 1000;
	// 缓冲区大小
	public static final int BUFFER_SIZE =  8*1024;
	// 重新连接最多次数
	public static final int MAX_RE_CONN = 3;
	// 分段请求块长度:默认
	public static final int SPLIT_REQUEST_DEFAULT_SIZE = 100 * 1024;
	// 分段请求块长度:wifi下
	public static final int SPLIT_REQUEST_WIFI_SIZE = 1000 * 1024;
	
	// 最大并发线程数
	public static final int MAX_THREAD_COUNT = 3;
	
	public static final Charset CHARSET_DEFAULT_ENCODE=HttpUtils.ASCII;


}