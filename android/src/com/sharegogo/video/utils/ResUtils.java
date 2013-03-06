package com.sharegogo.video.utils;

import android.content.Context;

import com.sharegogo.video.SharegogoVideoApplication;

/**
 * 资源相关辅助函数
 * @author Raymon
 *
 */
public class ResUtils {
	static public String getString(int resId)
	{
		Context context = SharegogoVideoApplication.getApplication();
		
		return context.getResources().getString(resId);
	}
}
