package com.sharegogo.video;

import com.umeng.analytics.MobclickAgent;

import android.app.Application;

public class SharegogoVideoApplication extends Application{

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		MobclickAgent.updateOnlineConfig(this);
	}

}
