package com.sharegogo.video.utils;

import android.util.Log;

import com.sharegogo.video.game.BuildConfig;

/**
 * LogÏà¹Øº¯Êý
 * @author Raymon
 *
 */
public class LogUtils {
	
	static public void e(String tag,String message)
	{
		if(!BuildConfig.DEBUG)
		{
			return;
		}
		
		Log.e(tag, message);
	}
}
