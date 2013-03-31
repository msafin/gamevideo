package com.sharegogo.video;

import android.app.Application;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sharegogo.video.activity.BaseActivity;
import com.sharegogo.video.controller.UserManager;
import com.sharegogo.video.data.MySqliteHelper;
import com.sharegogo.video.lib.LibInterface;
import com.umeng.analytics.MobclickAgent;

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
		
		//GamePeople.makePhonyData(this);
		//GameVideo.makePhonyData(this);
		//Favorite.makePhonyData(this);
		
		//HttpTest.testHttp(this);
		new LibInterface().init(this);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
        .threadPoolSize(3) // default
        .threadPriority(Thread.NORM_PRIORITY - 1) // default
        .denyCacheImageMultipleSizesInMemory()
        .memoryCacheSize(3 * 1024 * 1024)
        .discCacheFileCount(100)
        .discCacheFileNameGenerator(new Md5FileNameGenerator()) // default
        .enableLogging()
        .build();
		
		ImageLoader.getInstance().init(config);
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
	
		BaseActivity.onAppExit();
		
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
