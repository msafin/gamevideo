package com.sharegogo.video.activity;

import com.sharegogo.video.game.R;

import android.os.Bundle;

public class AboutActivity extends NoTitleActivity{

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		setContentView(R.layout.about_activity);
	}

	@Override
	protected String getCustomTitle() {
		// TODO Auto-generated method stub
		return getResources().getString(R.string.about_title);
	}

}
