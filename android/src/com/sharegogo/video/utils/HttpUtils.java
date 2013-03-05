package com.sharegogo.video.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

import com.sharegogo.config.BuildConfig;

public class HttpUtils {
	
	static public String getUserAgent()
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append(DeviceInfo.getSystemVersion());
		builder.append("/");
		builder.append(BuildConfig.client_name);
		builder.append("/");
		builder.append(DeviceInfo.getManufacturer());
		builder.append("/");
		builder.append(DeviceInfo.getDeviceName());
		builder.append("/");
		builder.append(DeviceInfo.getDeviceName());
		builder.append("/");
		builder.append(BuildConfig.client_version);
		
		return builder.toString();
	}
	
	static public void httpUtilsTest()
	{
		Log.e("test", getUserAgent());
	}
}