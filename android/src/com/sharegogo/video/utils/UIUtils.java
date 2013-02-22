package com.sharegogo.video.utils;

import android.content.Context;
import android.content.Intent;

import com.sharegogo.video.SharegogoVideoApplication;
import com.sharegogo.video.activity.PlayActivity;
import com.sharegogo.video.data.GameVideo;

public class UIUtils {
	
	static public void gotoPlayActivity(GameVideo video,Context context)
	{
		Intent intent = new Intent(Intent.ACTION_MAIN);
		
		intent.setClass(context, PlayActivity.class);
		intent.putExtra(PlayActivity.KEY_FLASH_URL, video.url);
		intent.putExtra(PlayActivity.KEY_VIDEO_ID,video.id);
		
		context.startActivity(intent);
	}
}
