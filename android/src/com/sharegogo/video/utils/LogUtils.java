package com.sharegogo.video.utils;

import com.sharegogo.video.game.BuildConfig;
import com.umeng.common.Log;

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
