package com.sharegogo.video.http;

import java.util.Map;

import android.content.Context;
import android.util.Log;

public class HttpTest {
	
	static final String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&offset=0&limit=50";
		
	static public void testHttp(Context context)
	{
		HttpServer httpServer = HttpServer.getInstance(context);
		
		httpServer.request(url, new MyTextHandler());
	}
	
	
	static class MyTextHandler implements ITextHandler
	{

		@Override
		public void onHandleSuccess(int status, Map<String, String> headers,
				String body) {
			// TODO Auto-generated method stub
			Log.e("http", body);
		}

		@Override
		public void onHandleSuccess(int status, Map<String, String> headers,
				byte[] body, int length) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onThrowable(int code) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
