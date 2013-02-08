package  com.sharegogo.video.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import android.content.Context;

import com.sharegogo.video.http.impl.core.HttpQueue;
import com.sharegogo.video.http.impl.core.HttpRequest;
import com.sharegogo.video.http.impl.core.HttpScheduler;
import com.sharegogo.video.http.impl.utils.HttpConfig;
import com.sharegogo.video.http.impl.utils.HttpNetType;
import com.sharegogo.video.http.impl.utils.HttpProxy;
import com.sharegogo.video.http.impl.utils.HttpUtils;
import com.sharegogo.video.http.impl.utils.HttpNetType.onNetChangedListener;



/**
 * Http模块对外接口类，单例，外界通过该类实现请求服务器
 * @author weizhengqin
 * @date: 2012-09-25
 */
public class HttpServer{
	
	//实例
	public static 		Context	  	mAppContext = null;
	public volatile static 		HttpServer server;
	
	//调度器
	private HttpScheduler  scheduler = null;
	
	//请求队列
	private	HttpQueue  queue = null;
	
	
	/**
	 * 禁止外部创建实例
	 */
	private HttpServer(){
		
	}

	/**
	 * 获取实例
	 * @return
	 */
	public static HttpServer getInstance(Context ctx){
		if(server == null){
			//线程安全单例
			synchronized(HttpServer.class){  
				if(server == null){
					server = new HttpServer();
					server.init();
					mAppContext = ctx;
				}	
			}
		}		
		return server;
	}
	
	
	/**
	 * 返回应用程序context
	 * @return
	 */
	public static  Context getContext(){
		return mAppContext;
	}
	
	
	/**
	 * 监听网络状态变化
	 * @param listener
	 */
	public static void setOnNetChangedListener(onNetChangedListener listener ){
		HttpNetType.setOnNetChangedListener(listener);
	}
	
	
	public static void removeOnNetChangedListener(onNetChangedListener listener){
		HttpNetType.removeOnNetChangedListener(listener);
	}
	
	
	/**
	 * 直接通过url请求服务器文本数据
	 * @param url
	 * @param handler
	 * @return
	 */
	public long request(String url, ITextHandler handler){
		HttpRequest req = new HttpRequest(url,handler);
		return request(req);
	}
	
	/**
	 * 直接通过url请求服务器二进制数据 
	 * @param url
	 * @param handler
	 * @return
	 */
	public long request(String url, IBinaryHandler handler){
		HttpRequest req = new HttpRequest(url,handler);
		return request(req);
	}
	
	/**
	 * 同步请求,现在只有统计模块用到
	 * @param url
	 * @throws Exception
	 */
	public long request(String reqUrl) throws Exception{
		
		if(reqUrl == null)
		{
			return -1;
		}
		
		URL url = new URL(reqUrl);
		HttpURLConnection  urlconn = null;
		Proxy proxy = HttpProxy.getProxy();
		
		if(HttpNetType.isWap() && proxy!= null){
			//wap下设置代理
			int currNetWorkType = HttpNetType.getNetType();
			//电信wap
			if(currNetWorkType == HttpNetType.TYPE_CTWAP)
			{
				urlconn = (HttpURLConnection)url.openConnection(proxy);
			}
			else
			{
				int contentBeginIdx = reqUrl.indexOf('/', 7);  	
				String temp = HttpProxy.getProxyString() 
						+ reqUrl.substring(contentBeginIdx);
				
				URL urltemp = new URL(temp); 
				urlconn = (HttpURLConnection)urltemp.openConnection();
				
				urlconn.setRequestProperty("X-Online-Host",  
						reqUrl.substring(7, contentBeginIdx));
			}
		}
		else
		{
			urlconn = (HttpURLConnection)url.openConnection();
		}
		
		
		//设置header信息
		urlconn.setRequestMethod(HttpRequest.HTTP_GET);
		urlconn.setRequestProperty("ACCEPT", HttpUtils.ACCEPT_TYPE);
//		urlconn.setRequestProperty("Content-type", HttpUtils.COMMON_CONTENT_TYPE);
		urlconn.setRequestProperty("User-Agent", HttpUtils.DEFAULT_USER_AGENT);
//		urlconn.setRequestProperty("Referer", HttpUtils.DEFUALT_REFERER);
		
		try{
			//设置超时
			urlconn.setConnectTimeout(HttpConfig.CONN_TIME_OUT);
			urlconn.setReadTimeout(HttpConfig.READ_TIME_OUT);
			urlconn.setDoOutput(false);
						
			try
			{
				urlconn.connect();
			}
			catch(IOException e)
			{
				return HttpUtils.HTTP_404_NOTFOUND;
			}
			
			int respcode = urlconn.getResponseCode();
			
			urlconn.disconnect();
			
			return respcode;
		}
		catch( Exception e)
		{
			e.printStackTrace();
			
			return HttpUtils.HTTP_404_NOTFOUND;
		}
	}
	
	/**
	 * 发出一个http请求
	 * @param req
	 * @return
	 */
	public long request(HttpRequest req){
		long id = queue.push_front(req);
		scheduler.scheduler();
		return id;
	}
	
	
	/**
	 * 取消一个请求
	 * @param req
	 */
	public void cancel(HttpRequest req){
		scheduler.cancel(req);
		queue.remove(req);
	}
	
	/**
	 * 根据id取消请求
	 * @param id
	 */
	public void cancel(long id){
		scheduler.cancel(id);
		queue.remove(id);
	}
	
	
	/**
	 * 取消同一类tag的请求
	 */
	public void cancel(String tag){
		scheduler.cancel(tag);
		queue.remove(tag);
	}
	
	
	/**
	 * 结束所有http请求，释放资源
	 */
	public void release(){
		scheduler.release();
		queue.clear();
		server = null;
	}
	
	
	//////////////////////////////////////////////////////////
	//网络状态相关接口
	
	/**
	 * 
	 * @return
	 */
	public static  boolean isNetAvailable(){
		return HttpNetType.isNetAvailable(mAppContext);
	}
	
	
	//////////////////////////////////////////////////////////////////
	//内部函数
	
	/**
	 * 初始化请求队列和调度器
	 */
	private void init(){
		
		scheduler = new HttpScheduler();
		queue = HttpQueue.getInstance();
	}
	
	
	

}