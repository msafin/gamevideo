package com.sharegogo.video.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.sharegogo.video.game.R;

public class SearchActivity extends NoTitleActivity{

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		setContentView(R.layout.search_activity);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected String getCustomTitle() {
		// TODO Auto-generated method stub
		return "ËÑË÷";
	}
}
