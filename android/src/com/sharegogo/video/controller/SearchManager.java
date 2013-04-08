package com.sharegogo.video.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.BasicNameValuePair;

import com.sharegogo.config.HttpConstants;
import com.sharegogo.video.data.CategoryList;
import com.sharegogo.video.data.SearchResult;
import com.sharegogo.video.data.VideoDetail;
import com.sharegogo.video.data.VideoList;
import com.sharegogo.video.http.BasicResponseHandler;
import com.sharegogo.video.http.HttpManager;
import com.sharegogo.video.http.ResponseHandler;
import com.sharegogo.video.utils.HttpUtils;

/**
 * ËÑË÷¹ÜÀíÆ÷
 * @author Raymon
 *
 */
public class SearchManager {
	static final public int TYPE_LIST_HOT = 0;
	static final public int TYPE_LIST_LATEST = 1;
	static final public int TYPE_LIST_RECOMMEND = 2;
	
	static private SearchManager mInstance;
	
	private SearchManager()
	{
		
	}
	
	static public SearchManager getInstance()
	{
		if(mInstance == null)
		{
			synchronized(SearchManager.class)
			{
				if(mInstance == null)
				{
					mInstance = new SearchManager();
				}
			}
		}
		
		return mInstance;
	}
	
	
	public void doSearch(String key,int pageNum,int pageSize,int listType,int asc,ResponseHandler handler)
	{
		HttpRequest httpRequest = new BasicHttpRequest(HttpGet.METHOD_NAME, HttpConstants.URL_SEARCH);
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		NameValuePair keyPair;
		try {
			keyPair = new BasicNameValuePair("keyWord",URLEncoder.encode(key, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			keyPair = new BasicNameValuePair("keyWord",key);
		} 
		NameValuePair pageNumPair = new BasicNameValuePair("pageNum",String.valueOf(pageNum));
		NameValuePair pageSizePair = new BasicNameValuePair("pageSize",String.valueOf(pageSize));
		NameValuePair listTypePair = new BasicNameValuePair("listType",String.valueOf(TYPE_LIST_HOT));
		NameValuePair ascPair = new BasicNameValuePair("asc",String.valueOf(asc));
		
		params.add(HttpUtils.getTokenPair());
		params.add(keyPair);
		params.add(pageNumPair);
		params.add(pageSizePair);
		params.add(listTypePair);
		params.add(ascPair);
		
		HttpManager.doRequest(httpRequest, params, handler, SearchResult.class);
	}
	
	
	static public void test()
	{
		getInstance().doSearch("game", 0, 10, TYPE_LIST_HOT,0, new BasicResponseHandler());
	}
}
