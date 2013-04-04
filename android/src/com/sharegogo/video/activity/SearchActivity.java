package com.sharegogo.video.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.sharegogo.video.game.R;

public class SearchActivity extends NoTitleActivity implements OnClickListener{

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		setContentView(R.layout.search_activity);
		ImageButton search = (ImageButton)findViewById(R.id.search_btn);
		search.setOnClickListener(this);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
