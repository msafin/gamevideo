package  com.sharegogo.video.http.impl.utils;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.params.HttpParams;

import com.sharegogo.video.http.HttpServer;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;


/**
 * Http代理设置
 * 
 * @author weizhengqin
 * @date: 2012-09-25
 */
public class HttpProxy{
	

	/**
	 * (api)默认代理的端口号 
	 * @return
	 */
	public static int getProxyPort(){
		return android.net.Proxy.getDefaultPort();
	}
	
	
	/**
	 * (api)默认代理的ip地址
	 * @return
	 */
	public static String getProxyHost(){
		return android.net.Proxy.getDefaultHost();
	}
	
	
	/**
	 * (api)填充默认代理
	 * @param httpParams
	 */
	public static void fillProxy(final HttpParams  httpParams ){
		
		HttpHost proxy = new HttpHost(getProxyHost(), getProxyPort());
		httpParams.setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
	}
	
	
	/**
	 * (api)非wifi下填充默认代理
	 * @param ctx
	 * @param httpParams
	 */
	public static void fillProxyIfNeed(Context ctx, final HttpParams httpParams){
		
		if(!HttpNetType.isWifi(ctx)){
			fillProxy(httpParams);
		}
	}
	
	public static Proxy getProxy() {
		InetSocketAddress pxy = null;
		Proxy proxy = null;
		String strProxyHost = android.net.Proxy.getDefaultHost();
		int proxyport = android.net.Proxy.getDefaultPort();
		
		if (strProxyHost != null && strProxyHost.trim().length() > 0) {
			pxy = new InetSocketAddress(strProxyHost, proxyport);
			proxy = new Proxy(java.net.Proxy.Type.HTTP,pxy);
		}
		
		return proxy;
	}	
	
	
	
	
	public static String getProxyString(){
		InetSocketAddress pxy = null;
		Proxy proxy = null;
		String strProxyHost = android.net.Proxy.getDefaultHost();
		int proxyport = android.net.Proxy.getDefaultPort();
		
		String proxyString = null;
		if (strProxyHost != null && strProxyHost.trim().length() > 0) {
			proxyString = "http://" + strProxyHost +":"+ String.valueOf(proxyport);
		
		}
		
		return proxyString;
	}
	
}