package com.sharegogo.video.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.sharegogo.video.SharegogoVideoApplication;
import com.sharegogo.video.game.R;
import com.sharegogo.video.utils.ResUtils;

public class SharegogoVideoSettings {
	
	static private SharedPreferences getSharedPreferences()
	{
		Context context = SharegogoVideoApplication.getApplication();
		
		return context.getSharedPreferences("settings", Context.MODE_PRIVATE);
	}
	
	static public void setWifiFetchImage(int value)
	{
		getSharedPreferences().edit().putInt(ResUtils.getString(R.string.key_wifi_image), value).commit();
	}
	
	static public int getWifiFetchImage()
	{
		return getSharedPreferences().getInt(ResUtils.getString(R.string.key_wifi_image), 0);
	}
}
