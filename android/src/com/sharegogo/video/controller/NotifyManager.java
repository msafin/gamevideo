package com.sharegogo.video.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.BasicNameValuePair;

import com.sharegogo.config.BuildingConfig;
import com.sharegogo.config.HttpConstants;
import com.sharegogo.video.data.NotifyList;
import com.sharegogo.video.data.UpdateInfo;
import com.sharegogo.video.http.BasicResponseHandler;
import com.sharegogo.video.http.HttpManager;
import com.sharegogo.video.http.ResponseHandler;
import com.sharegogo.video.utils.HttpUtils;

public class NotifyManager {
	static private NotifyManager mInstance;

	private NotifyManager() 
	{
	}
	
	static public NotifyManager getInstance()
	{
		if(mInstance == null)
		{
			synchronized(NotifyManager.class)
			{
				if(mInstance == null)
				{
					mInstance = new NotifyManager();
				}
			}
		}
		
		return mInstance;
	}
	
	public void getNotify(ResponseHandler handler)
	{
		HttpRequest httpRequest = new BasicHttpRequest(HttpGet.METHOD_NAME, HttpConstants.URL_NOTIFY_LIST);
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(HttpUtils.getTokenPair());
		
		HttpManager.doRequest(httpRequest, params, handler,NotifyList.class);
	}
	
	static public void test()
	{
		getInstance().getNotify(new BasicResponseHandler());
	}
}
