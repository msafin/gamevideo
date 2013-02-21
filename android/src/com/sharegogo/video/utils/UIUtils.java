package com.sharegogo.video.utils;

import android.content.Context;
import android.content.Intent;

import com.sharegogo.video.SharegogoVideoApplication;
import com.sharegogo.video.activity.PlayActivity;

public class UIUtils {
	
	static public void gotoPlayActivity(String url,Context context)
	{
		Intent intent = new Intent(Intent.ACTION_MAIN);
		
		intent.setClass(context, PlayActivity.class);
		intent.putExtra(PlayActivity.KEY_FLASH_URL, url);
		
		context.startActivity(intent);
	}
}
