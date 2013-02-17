package com.sharegogo.video.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.sharegogo.video.game.R;

public class SettingActivity extends PreferenceActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.setting_activity);
		
		this.addPreferencesFromResource(R.xml.setting_activity);
		
		TextView textView = (TextView)findViewById(android.R.id.title);
		
		textView.setText(R.string.setting_title);
		
		Button back = (Button)findViewById(R.id.back);
		
		back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
