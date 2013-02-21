package com.sharegogo.video.utils;

import android.content.Context;

import com.sharegogo.video.SharegogoVideoApplication;

public class ResUtils {
	static public String getString(int resId)
	{
		Context context = SharegogoVideoApplication.getApplication();
		
		return context.getResources().getString(resId);
	}
}
