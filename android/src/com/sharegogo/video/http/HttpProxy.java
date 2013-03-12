package  com.sharegogo.video.http;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * Http代理
 * @author weizhengqin
 * @date 2013-3-12
 * @version 1.0
 */
public class HttpProxy{
	
	public static Proxy getProxy() 
	{
		InetSocketAddress pxy = null;
		Proxy proxy = null;
		String strProxyHost = android.net.Proxy.getDefaultHost();
		int proxyport = android.net.Proxy.getDefaultPort();
		
		if (strProxyHost != null && strProxyHost.trim().length() > 0) 
		{
			pxy = new InetSocketAddress(strProxyHost, proxyport);
			proxy = new Proxy(java.net.Proxy.Type.HTTP,pxy);
		}
		
		return proxy;
	}	
	
	public static String getProxyString()
	{
		String strProxyHost = android.net.Proxy.getDefaultHost();
		int proxyport = android.net.Proxy.getDefaultPort();
		
		String proxyString = null;
		if (strProxyHost != null && strProxyHost.trim().length() > 0) 
		{
			proxyString = "http://" + strProxyHost +":"+ String.valueOf(proxyport);
		
		}
		
		return proxyString;
	}
}