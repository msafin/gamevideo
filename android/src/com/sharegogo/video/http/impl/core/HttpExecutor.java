package  com.sharegogo.video.http.impl.core;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import java.net.Proxy;

import com.sharegogo.video.http.HttpServer;
import com.sharegogo.video.http.impl.utils.HttpConfig;
import com.sharegogo.video.http.impl.utils.HttpNetType;
import com.sharegogo.video.http.impl.utils.HttpProxy;
import com.sharegogo.video.http.impl.utils.HttpUtils;



/**
 * 处理一个http请求
 * 
 * @author weizhengqin
 * @date: 2012-09-25
 */
public class HttpExecutor {
	private final Object mLock = new Object();
	private byte[]  	buffer;
	
	//当前处理的请求
	private HttpRequest curreq = null;
	
	private HttpResponse listener = null; 

	private boolean bCancel = false;
	
	public HttpExecutor(){
		buffer = new byte[HttpConfig.BUFFER_SIZE];
	}
	
	/**
	 * (api)当前正在执行request
	 * @return
	 */
	public HttpRequest getCurrRequest(){
		return this.curreq;
	}
	
	
	/**
	 *(api) 执行请求全流程
	 * @param req
	 * @throws Exception
	 */
	public void request(HttpRequest req) 
			throws Exception{
		
		if(req == null) return;
		
		curreq = req;
		URL url = new URL(req.getUrl());
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
				int contentBeginIdx = req.getUrl().indexOf('/', 7);  	
				String temp = HttpProxy.getProxyString() 
						+ req.getUrl().substring(contentBeginIdx);
				
				URL urltemp = new URL(temp); 
				urlconn = (HttpURLConnection)urltemp.openConnection();
				
				urlconn.setRequestProperty("X-Online-Host",  
						req.getUrl().substring(7, contentBeginIdx));
			}
		}
		else
		{
			urlconn = (HttpURLConnection)url.openConnection();
		}
		
		
		//设置header信息
		setRequestHeaders(urlconn, req);
		
		//设置handler
		if(req.getBinaryHandler() != null){
			listener = new HttpResponseBinary(req.getBinaryHandler());
		}else if(req.getTextHandler() != null){
			listener = new HttpResponseText(req.getTextHandler());
		}
		
		
		try{
			//设置超时
			urlconn.setConnectTimeout(HttpConfig.CONN_TIME_OUT);
			urlconn.setReadTimeout(HttpConfig.READ_TIME_OUT);
			urlconn.setDoInput(true);
			
			//设置传输方式，android4.0以上必须设置，否则按照post提交
			if(req.getMethod().equals(HttpRequest.HTTP_GET)){
				urlconn.setDoOutput(false);
			}else{
				urlconn.setDoOutput(true);
			}
			
			//post方式设置参数信息
			if("post".equals(req.getMethod())){
				setRequestParams(urlconn, req);
			}
						
			try
			{
				urlconn.connect();
			}
			catch(IOException e)
			{
				listener.onThrowable(HttpUtils.HTTP_404_NOTFOUND);
				return;
			}
			
			synchronized(mLock)
			{
				if(bCancel)
				{
					listener.onThrowable(HttpUtils.HTTP_REQUEST_CANCEL);
				}
			}
			
			int respcode = urlconn.getResponseCode();
			if(HttpUtils.HTTP_416_RANGE_ERR == respcode){
				listener.onThrowable(HttpUtils.HTTP_416_RANGE_ERR);
			}
			
			if( respcode == HttpUtils.HTTP_200_OK || 
				respcode == HttpUtils.HTTP_206_PARTIAL){		

				listener.onStatusReceived(respcode);
				
				//取得头部信息
				getResponseHeaders(urlconn, req);
				//处理传输回的数据
				getBodyReceived(urlconn, req);
			}else{
				listener.onThrowable(HttpUtils.HTTP_400_BADREQUEST);
			}
			
		}catch( Exception e){
			if(listener != null) {
				listener.onThrowable(HttpUtils.HTTP_404_NOTFOUND);
			}
			e.printStackTrace();
		}finally{
			listener = null;
		}

	}

	
	/**
	 * (api)取消当前的http请求
	 */
	public void  cancel(){
		synchronized(mLock)
		{
			bCancel = true;
			if(listener != null)
			{
				listener.onThrowable(HttpUtils.HTTP_REQUEST_CANCEL);
			}
		}
	}
	
	/**
	 * (api)结束请求,释放资源
	 */
	public void release(){
		
	}
	
	
	
	
	/**
	 * 设置头部信息
	 * @param urlconn
	 * @param req
	 * @throws ProtocolException
	 */
	private void setRequestHeaders(HttpURLConnection urlconn, HttpRequest req ) 
			throws ProtocolException{
		
		 //设置请求方式
		urlconn.setRequestMethod(req.getMethod());  //post/get 方式
		urlconn.setRequestProperty("ACCEPT", HttpUtils.ACCEPT_TYPE);
//		urlconn.setRequestProperty("Content-type", HttpUtils.COMMON_CONTENT_TYPE);
		urlconn.setRequestProperty("User-Agent", HttpUtils.DEFAULT_USER_AGENT);
//		urlconn.setRequestProperty("Referer", HttpUtils.DEFUALT_REFERER);
		
		 //读取headers并进行设置
		HashMap<String, String> reqheaders =  req.getHeaders();
		if(reqheaders!= null){
			Iterator iter = reqheaders.entrySet().iterator();
			while(iter.hasNext()){
				Map.Entry<String,String> entry = (Map.Entry<String,String>)iter.next();
				String key 	= entry.getKey();
				String value= entry.getValue();	
				urlconn.setRequestProperty(key, value);
			}
		}
	}
	
	
	/**
	 * 设置参数信息
	 * @param urlconn
	 * @param req
	 * @throws IOException
	 */
	private void setRequestParams( HttpURLConnection urlconn, HttpRequest req ) 
			throws IOException{
	
		String params = req.getParamString();
		if(params != null && params.length() > 0){
			
			//写入字符串参数
			byte[] data = params.getBytes();	
			DataOutputStream  dos = new DataOutputStream(urlconn.getOutputStream());
			dos.write(data);
			dos.flush();
			dos.close();
		}else{
			
			//写入二进制信息
			byte[] data = req.getParamByte();
			if(data.length>0){
				DataOutputStream dos = new DataOutputStream(urlconn.getOutputStream());	
				dos.write(data);
				dos.flush();
				dos.close();
			}
		}
	}
	
	
	/**
	 * 取得返回的头部信息
	 * @param urlconn
	 * @param req
	 */
	private void getResponseHeaders(HttpURLConnection  urlconn, HttpRequest req ){
		
		Map<String, String> headers = new HashMap<String, String>();
		
		Map<String,List<String>> map = urlconn.getHeaderFields();
		
		//key转换成大写
		Map<String,Object> respheaders = new HashMap<String,Object>();
		Iterator iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key =(String) entry.getKey();
			Object val = entry.getValue();
			//key有可能为null
			if(key != null)
				respheaders.put(key.toUpperCase(), val);
		}
		
		//content-encode
		List<String> temp = (List<String>)respheaders.get(HttpUtils.CONTENT_ENCODE);
		String encode = (temp !=null && temp.size() > 0) ? temp.get(0) : null;
		headers.put(HttpUtils.CONTENT_ENCODE, encode);
		
		//content-length
		temp = (List<String>) respheaders.get(HttpUtils.CONTENT_LENGTH);
		String length = (temp !=null && temp.size() > 0) ? temp.get(0) : null;
		headers.put(HttpUtils.CONTENT_LENGTH, length);
		
		//content-type
		temp = (List<String>)respheaders.get(HttpUtils.CONTENT_TYPE);
		String type = (temp !=null && temp.size() > 0) ? temp.get(0) : null;
		headers.put(HttpUtils.CONTENT_TYPE, type);
		
		//回调
		if(listener != null) {
			listener.onHeadersReceived(headers);
		}
			
	}
	
	
	/**
	 * 得到传输回的数据
	 * @param urlconn
	 * @param req
	 * @throws IOException
	 */
	private void getBodyReceived(HttpURLConnection  urlconn, HttpRequest req )
		   throws IOException{

		InputStream is = urlconn.getInputStream();
		int count = -1;
		
		//处理返回的部分数据
		do{
			synchronized(mLock)
			{
				if(bCancel)
				{
					is.close();
					urlconn.disconnect();
					listener.onThrowable(HttpUtils.HTTP_REQUEST_CANCEL);
					break;
				}
			}
			count = is.read(buffer);
			if(count==-1) break;
			
			if(listener != null){
				listener.onBodyPartReceived(buffer, count);
			}	
		}while(true);
		
		//传输完成回调,应该还要判断是否完成
		if(listener != null){
			synchronized(mLock)
			{
				if(!bCancel)
				{
					listener.onCompleted();
				}
			}
		}	
		
		//关闭资源
		is.close();
		urlconn.disconnect();	
	}
	
}