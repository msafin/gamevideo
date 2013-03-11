package com.sharegogo.video.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.BasicNameValuePair;

import com.sharegogo.config.HttpConstants;
import com.sharegogo.video.data.CategoryList;
import com.sharegogo.video.data.VideoDetail;
import com.sharegogo.video.data.VideoList;
import com.sharegogo.video.http.BasicResponseHandler;
import com.sharegogo.video.http.HttpManager;
import com.sharegogo.video.http.ResponseHandler;
import com.sharegogo.video.utils.HttpUtils;

/**
 *  ”∆µπ‹¿Ì∆˜
 * @author Raymon
 *
 */
public class VideoManager {
	static final public int TYPE_LIST_HOT = 0;
	static final public int TYPE_LIST_LATEST = 1;
	static final public int TYPE_LIST_RECOMMEND = 2;
	
	static private VideoManager mInstance;
	
	private VideoManager()
	{
		
	}
	
	static public VideoManager getInstance()
	{
		if(mInstance == null)
		{
			synchronized(VideoManager.class)
			{
				if(mInstance == null)
				{
					mInstance = new VideoManager();
				}
			}
		}
		
		return mInstance;
	}
	
	public void getVideoCategory(int cid,ResponseHandler handler)
	{
		HttpRequest httpRequest = new BasicHttpRequest(HttpGet.METHOD_NAME, HttpConstants.URL_CATEGORY_LIST);
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		NameValuePair cidPair = new BasicNameValuePair("cid",String.valueOf(cid));
		
		params.add(HttpUtils.getTokenPair());
		params.add(cidPair);
		
		HttpManager.doRequest(httpRequest, params, handler, CategoryList.class);
	}
	
	public void getVideoLis(int cid,int listType,int pageNum,int pageSize,int asc,ResponseHandler handler)
	{
		HttpRequest httpRequest = new BasicHttpRequest(HttpGet.METHOD_NAME, HttpConstants.URL_VIDEO_LIST);
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		NameValuePair cidPair = new BasicNameValuePair("cid",String.valueOf(cid)); 
		NameValuePair listTypePair = new BasicNameValuePair("listType",String.valueOf(TYPE_LIST_HOT));
		NameValuePair pageNumPair = new BasicNameValuePair("pageNum",String.valueOf(pageNum));
		NameValuePair pageSizePair = new BasicNameValuePair("pageSize",String.valueOf(pageSize));
		NameValuePair ascPair = new BasicNameValuePair("asc",String.valueOf(asc));
		
		params.add(HttpUtils.getTokenPair());
		params.add(cidPair);
		params.add(listTypePair);
		params.add(pageNumPair);
		params.add(pageSizePair);
		params.add(ascPair);
		
		HttpManager.doRequest(httpRequest, params, handler, VideoList.class);
	}
	
	public void getVideoDetail(long videoId,ResponseHandler handler)
	{
		HttpRequest httpRequest = new BasicHttpRequest(HttpGet.METHOD_NAME, HttpConstants.URL_VIDEO_DETAIL);
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		NameValuePair cidPair = new BasicNameValuePair("id",String.valueOf(videoId));
		
		params.add(HttpUtils.getTokenPair());
		params.add(cidPair);
		
		HttpManager.doRequest(httpRequest, params, handler, VideoDetail.class);
	}
	
	static public void test()
	{
		getInstance().getVideoCategory(0, new BasicResponseHandler());
		getInstance().getVideoLis(0, TYPE_LIST_HOT, 0, 10, 0, new BasicResponseHandler());
		getInstance().getVideoDetail(0,  new BasicResponseHandler());
	}
}
