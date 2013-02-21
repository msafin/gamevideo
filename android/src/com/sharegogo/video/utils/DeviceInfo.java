package com.sharegogo.video.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.sharegogo.video.SharegogoVideoApplication;

public class DeviceInfo {
	
	static public String getDeviceId()
	{
		Context context = SharegogoVideoApplication.getApplication();
		
		TelephonyManager telephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		
		return telephonyMgr.getDeviceId();
	}
	
	static public void test()
	{
		Log.e("test", getDeviceId());
	}
}
