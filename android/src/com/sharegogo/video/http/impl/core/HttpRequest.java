package  com.sharegogo.video.http.impl.core;

import java.util.HashMap;
import java.util.Map;

import com.sharegogo.video.http.IBinaryHandler;
import com.sharegogo.video.http.ITextHandler;




/**
 * 网络请求结构体
 * 
 * @author weizhengqin
 * @date: 2012-09-21
 */
public class HttpRequest{
	
	public static final String HTTP_GET  = "GET"; //get请求 必须大写
	public static final String HTTP_POST = "POST"; //post请求 必须大写
	
	//tag
	private String		tag;
	
	//唯一ID
	private long		reqId = 0; 
	//请求方式
	private	String	reqmethod = HTTP_GET; 
	//头部信息
	private HashMap<String,String> reqheaders; 
	
	//目的url
	private String	reqUrl = null;
	//字符串参数
	private String  reqParamString = null; 
	//二进制参数
	private byte[] 	reqParamByte; 
	
	//是否自动为头部填充默认信息
	private boolean	autoHead = true; 
	
	//回调接口，处理返回的信息
	private ITextHandler   	text_handler = null;
	private IBinaryHandler 	binary_handler =  null;
	
	private HttpResponse  	response = null;
	
	/**
	 * 默认构造
	 */
	
	public HttpRequest(){
		
	}

	
	/**
	 * 带handler的构造体
	 * @param handler
	 */
	public HttpRequest(ITextHandler handler){
		this(true, handler);
	}
	
	
	
	public HttpRequest(IBinaryHandler handler){
		this(true, handler);
	}
	
	
	/**
	 * 生成header的请求体
	 * @param autoHead
	 */
	public HttpRequest(boolean autoHead, ITextHandler handler){
		this.autoHead = autoHead;
		this.text_handler = handler;
	}
	
	
	public HttpRequest(boolean autoHead, IBinaryHandler handler){
		this.autoHead = autoHead;
		this.binary_handler = handler;
	}
	
	
	/**
	 * 带url的请求体
	 *
	 * @param url
	 */
	public HttpRequest(String url, ITextHandler handler ){
		this(true,handler);
		this.reqUrl = url;
	}
	
	public HttpRequest(String url, IBinaryHandler handler ){
		this(true,handler);
		this.reqUrl = url;
	}
	
	
	/**
	 * 带url和autohead的请求体
	 * @param url
	 * @param autoHead
	 * @param handler
	 */
	public HttpRequest(boolean autoHead,String url, ITextHandler handler ){
		this(url, handler);
		this.autoHead = autoHead;
	}
	
	public HttpRequest(boolean autoHead,String url ,IBinaryHandler handler ){
		this(url, handler);
		this.autoHead = autoHead;
	}
	
	
	/**
	 * 带url和字符串参数的请求体
	 * 
	 * @param url
	 */		
	public HttpRequest(String url, String reqcontent, ITextHandler handler){
		this(true, url, handler);
		this.reqParamString = reqcontent;
	}

	public HttpRequest(String url, String reqcontent, IBinaryHandler handler){
		this(true, url, handler);
		this.reqParamString = reqcontent;
	}
	

	/**
	 * 带url和字符串以及的请求体
	 * @param url
	 * @param reqcontent
	 * @param autohead
	 * @param handler
	 */
	public HttpRequest(String url, String reqcontent, boolean autohead, ITextHandler handler){
		this(url,reqcontent,handler);
		this.autoHead = autohead;
	}
	
	public HttpRequest(String url, String reqcontent, boolean autohead, IBinaryHandler handler){
		this(url,reqcontent,handler);
		this.autoHead = autohead;
	}
	
	
	/**
	 * 带url和二进制参数的请求体
	 * 
	 * @param url
	 * @param reqcontent
	 */
	public HttpRequest(String url, byte[] reqcontent, ITextHandler handler){
		this(true,url,handler);
		this.reqParamByte = reqcontent;
	}
	
	public HttpRequest(String url, byte[] reqcontent, IBinaryHandler handler){
		this(true,url,handler);
		this.reqParamByte = reqcontent;
	}
	
	
	/**
	 * 带url和二进制参数及autohead的请求体
	 * @param url
	 * @param reqcontent
	 * @param autohead
	 * @param handler
	 */
	public HttpRequest(String url, byte[] reqcontent, boolean autohead, ITextHandler handler){
		this(url, reqcontent,handler);
		this.autoHead = autohead;
	}

	public HttpRequest(String url, byte[] reqcontent, boolean autohead, IBinaryHandler handler){
		this(url, reqcontent,handler);
		this.autoHead = autohead;
	}
	
	/**
	 * 设置文本回调接口
	 */
	public void setTextHandler(ITextHandler handler){
		this.text_handler = handler;
	}
	
	
	/**
	 * 设置二进制回调接口
	 */
	public void setBinaryHandler(IBinaryHandler handler){
		this.binary_handler = handler;
	}
	
	
	/**
	 * 设置连接方式  "get"/"put"
	 * 
	 * @param method
	 */
	public void setRequestMethod(String method){
		this.reqmethod = method;
	}
	
	
	/**
	 * 设置头部信息
	 * 
	 * @param headers
	 */
	public void setHeaders(HashMap<String, String> headers){
		this.reqheaders = headers;
	}
	

	/**
	 * 设置url
	 * 
	 * @param url
	 */
	public void setUrl(String url){
		this.reqUrl = url;
	}
	
	
	/**
	 * 设置字符串参数
	 * 
	 * @param param
	 */
	public void setParamString(String param){
		this.reqParamString = param;
	}
	
	
	/**
	 * 设置二进制参数
	 * 
	 * @param param
	 */
	public void setParamByte(byte[] param){
		this.reqParamByte = param;
	}
	
	
	
	/**
	 * 设置是否自动填充头部信息
	 * 
	 * @param auto
	 */
	public void setAutoHead(boolean auto){
		this.autoHead = auto;
	}
	
	
	/**
	 * 设置id
	 * @param id
	 */
	public void setId(long id){
		this.reqId = id;
	}
	
	
	/**
	 * 设置tag
	 */
	public void setTag(String tag){
		this.tag = tag;
	}
	
	
	/**
	 * 返回tag
	 */
	public String getTag(){
		return this.tag;
	}
	
	
	/**
	 * 返回id
	 * 
	 */
	public long getId(){
		return this.reqId;
	}
	
	
	/**
	 * 返回Url
	 * 
	 * @return
	 */
	public String getUrl(){
		return this.reqUrl;
	}
	

	/**
	 * 返回头部信息
	 * 
	 * @return
	 */
	public HashMap<String, String> getHeaders(){
		return this.reqheaders;
	}
	
	
	/**
	 * 返回字符串参数
	 * 
	 * @return
	 */
	public String getParamString(){
		return this.reqParamString;
	}
	
	
	/**
	 * 返回二进制参数
	 * 
	 * @return
	 */
	public byte[] getParamByte(){
		return this.reqParamByte;
	}
	
	
	
	/**
	 * 请求方式
	 * @return
	 */
	public String getMethod(){
		return this.reqmethod;
	}
	
	
	
	/**
	 * 返回文本hanlder
	 * @return
	 */
	public ITextHandler getTextHandler(){
		return this.text_handler;
	}
	
	/**
	 * 返回二进制信息handler
	 * @return
	 */
	public IBinaryHandler getBinaryHandler(){
		return this.binary_handler;
	}
	
	
}