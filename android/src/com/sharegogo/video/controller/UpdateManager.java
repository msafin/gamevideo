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
import com.sharegogo.video.data.SearchResult;
import com.sharegogo.video.data.UpdateInfo;
import com.sharegogo.video.http.BasicResponseHandler;
import com.sharegogo.video.http.HttpManager;
import com.sharegogo.video.http.ResponseHandler;
import com.sharegogo.video.utils.HttpUtils;

/**
 * Éý¼¶¹ÜÀíÆ÷
 * @author Raymon
 *
 */
public class UpdateManager {
	static private UpdateManager mInstance;
	
	private UpdateManager()
	{
	}
	
	static public UpdateManager getInstance()
	{
		if(mInstance == null)
		{
			synchronized(UpdateManager.class)
			{
				if(mInstance == null)
				{
					mInstance = new UpdateManager();
				}
			}
		}
		
		return mInstance;
	}
	
	public void checkUpate(ResponseHandler handler)
	{
		HttpRequest httpRequest = new BasicHttpRequest(HttpGet.METHOD_NAME, HttpConstants.URL_UPDATE);
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		NameValuePair machinePair = new BasicNameValuePair("machine",HttpUtils.getUserAgent());
		NameValuePair versionPair = new BasicNameValuePair("version",BuildingConfig.client_version);
		
		params.add(HttpUtils.getTokenPair());
		params.add(machinePair);
		params.add(versionPair);
		
		HttpManager.doRequest(httpRequest, params, handler,UpdateInfo.class);
	}
	
	static public void test()
	{
		getInstance().checkUpate(new BasicResponseHandler());
	}
}
