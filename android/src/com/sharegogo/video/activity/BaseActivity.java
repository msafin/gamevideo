package com.sharegogo.video.activity;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * ActivityµÄ»ùÀà
 * @author weizhengqin
 * @date 2013-3-12
 * @version 1.0
 */
public class BaseActivity extends SherlockFragmentActivity{
	private static List<BaseActivity> mActivityList = new ArrayList<BaseActivity>(); 
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		mActivityList.add(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mActivityList.remove(this);
	}
	
	static public void onAppExit()
	{
		for(BaseActivity activity:mActivityList)
		{
			activity.finish();
		}
		
		mActivityList.clear();
	}
}
