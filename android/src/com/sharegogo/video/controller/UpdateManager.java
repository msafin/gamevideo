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
public class UpdateManager implements ResponseHandler {
	static private UpdateManager mInstance;
	private UpdateInfo mUpdateInfo = null;
	private List<CheckUpdateObserver> mObservers = null;
	
	private UpdateManager()
	{
		mObservers = new ArrayList<CheckUpdateObserver>();
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
	
	
	public void checkUpdate(CheckUpdateObserver observer)
	{
		synchronized(mObservers)
		{
			if(observer != null)
			{
				mObservers.add(observer);
				
				if(mUpdateInfo != null)
				{
					observer.onCheckUpdateSuccess(mUpdateInfo);
					mObservers.remove(observer);
					
					return;
				}
			}
		}
		
		checkUpate();
	}
	
	
	public void checkUpate()
	{
		HttpRequest httpRequest = new BasicHttpRequest(HttpGet.METHOD_NAME, HttpConstants.URL_UPDATE);
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		NameValuePair machinePair = new BasicNameValuePair("machine",HttpUtils.getUserAgent());
		NameValuePair versionPair = new BasicNameValuePair("version",BuildingConfig.client_version);
		
		params.add(HttpUtils.getTokenPair());
		params.add(machinePair);
		params.add(versionPair);
		
		HttpManager.doRequest(httpRequest, params, this,UpdateInfo.class);
	}
	
	static public void test()
	{
		getInstance().checkUpate();
	}

	public UpdateInfo getUpdateInfo()
	{
		return mUpdateInfo;
	}
	
	@Override
	public void onSuccess(Object data) {
		// TODO Auto-generated method stub
		mUpdateInfo = (UpdateInfo)data;
		
		synchronized(mObservers)
		{
			for(CheckUpdateObserver observer : mObservers)
			{
				observer.onCheckUpdateSuccess(mUpdateInfo);
			}
			
			mObservers.clear();
		}
	}

	@Override
	public void onFailed(int what, Object msg) {
		// TODO Auto-generated method stub
		mUpdateInfo = null;
		
		synchronized(mObservers)
		{
			for(CheckUpdateObserver observer : mObservers)
			{
				observer.onCheckUpdateFailed();
			}
			
			mObservers.clear();
		}
	}

	@Override
	public boolean onPersistent(Object data) {
		// TODO Auto-generated method stub
		return true;
	}
	
	static public interface CheckUpdateObserver
	{
		public void onCheckUpdateSuccess(UpdateInfo updateInfo);
		public void onCheckUpdateFailed();
	}
}
