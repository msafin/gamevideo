package com.sharegogo.video;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.sharegogo.video.controller.NotifyManager;
import com.sharegogo.video.controller.SearchManager;
import com.sharegogo.video.controller.UpdateManager;
import com.sharegogo.video.controller.UserManager;
import com.sharegogo.video.controller.VideoManager;
import com.sharegogo.video.data.Favorite;
import com.sharegogo.video.data.Game;
import com.sharegogo.video.data.GamePeople;
import com.sharegogo.video.data.GameVideo;
import com.sharegogo.video.data.MySqliteHelper;
import com.sharegogo.video.http.HttpManager;
import com.sharegogo.video.lib.LibInterface;
import com.sharegogo.video.utils.DeviceInfo;
import com.sharegogo.video.utils.HttpUtils;
import com.umeng.analytics.MobclickAgent;

import android.app.Application;

public class SharegogoVideoApplication extends Application{
	private MySqliteHelper mDataHelper = null;
	static private SharegogoVideoApplication mInstance = null;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		MobclickAgent.updateOnlineConfig(this);
		
		mInstance = this;
		UserManager.getInstance();
		
		Game.makePhonyData(this);
		GamePeople.makePhonyData(this);
		GameVideo.makePhonyData(this);
		//Favorite.makePhonyData(this);
		
		//HttpTest.testHttp(this);
		new LibInterface().init(this);
		
		//DeviceInfo.test();
		//HttpUtils.httpUtilsTest();
		//VideoManager.test();
		//SearchManager.test();
		//UpdateManager.test();
		//NotifyManager.test();
	}

	public void onApplicationExit()
	{
		if (mDataHelper != null) {
			OpenHelperManager.releaseHelper();
			mDataHelper = null;
		}
		
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	static public SharegogoVideoApplication getApplication()
	{
		return mInstance;
	}
	
	public MySqliteHelper getHelper() {
		
		if (mDataHelper == null) {
			mDataHelper = OpenHelperManager.getHelper(this, MySqliteHelper.class);
		}
		
		return mDataHelper;
	}
}
