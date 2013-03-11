package com.sharegogo.video.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.BasicNameValuePair;

import android.util.Base64;

import com.sharegogo.config.HttpConstants;
import com.sharegogo.video.data.AutoRegister;
import com.sharegogo.video.http.BasicResponseHandler;
import com.sharegogo.video.http.HttpManager;
import com.sharegogo.video.http.ResponseHandler;
import com.sharegogo.video.utils.DeviceInfo;
import com.sharegogo.video.utils.LogUtils;

public class UserManager extends BasicResponseHandler {
	static private UserManager mInstance;
	
	private String mToken;
	private List<TokenObserver> mObservers;
	private boolean bRegistering;
	private UserManager() 
	{
		if(mToken == null)
		{
			autoRegister();
		}
		
		mObservers = new ArrayList<TokenObserver>();
		bRegistering = false;
	}
	
	static public UserManager getInstance()
	{
		if(mInstance == null)
		{
			synchronized(UserManager.class)
			{
				if(mInstance == null)
				{
					mInstance = new UserManager();
				}
			}
		}
		
		return mInstance;
	}
	
	public String getToken(TokenObserver observer)
	{
		synchronized(mInstance)
		{
			if(mToken == null)
			{
				if(!bRegistering)
				{
					autoRegister();
					bRegistering = true;
				}
				
				if(observer != null)
				{
					mObservers.remove(observer);
					mObservers.add(observer);
				}
			}
			
			return mToken;
		}
	}
	
	public String getToken()
	{
		synchronized(mInstance)
		{
			return mToken;
		}
	}
	
	private void autoRegister()
	{
		HttpRequest request = new BasicHttpRequest(HttpPost.METHOD_NAME, HttpConstants.URL_AUTO_REGISTER);
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	
    	String imsi = DeviceInfo.getDeviceImsi();
    	//需要指定NO_WRAP否者会以换行结束
    	String regid = Base64.encodeToString(imsi.getBytes(), Base64.NO_WRAP);
    	NameValuePair regidPair = new BasicNameValuePair("regid",regid);
    	
    	String imei = DeviceInfo.getDeviceImei();
    	String regimei = Base64.encodeToString(imei.getBytes(), Base64.NO_WRAP);
    	NameValuePair imeiPair = new BasicNameValuePair("regimei",regimei);
    	
    	params.add(regidPair);
    	params.add(imeiPair);
    	
    	HttpManager.doRequest(request,params,this,AutoRegister.class);
	}
	
	static public interface TokenObserver
	{
		public void onSuccess();
		public void onFailed();
	}

	@Override
	public void onSuccess(Object data) {
		// TODO Auto-generated method stub
		synchronized(mInstance)
		{
			bRegistering = false;
			
			AutoRegister response = (AutoRegister)data;
			mToken = response.token;
			
			for(TokenObserver observer:mObservers)
			{
				observer.onSuccess();
			}
			
			mObservers.clear();
		}
	}

	@Override
	public void onFailed(int what, Object msg) {
		// TODO Auto-generated method stub
		synchronized(mInstance)
		{
			bRegistering = false;
			
			for(TokenObserver observer:mObservers)
			{
				observer.onFailed();
			}
			
			mObservers.clear();
		}
	}
}
