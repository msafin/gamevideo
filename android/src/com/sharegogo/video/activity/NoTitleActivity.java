package com.sharegogo.video.activity;

import com.sharegogo.video.game.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

abstract public class NoTitleActivity extends FragmentActivity{

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(arg0);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		setCustomTitle();
		
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

	protected void setCustomTitle()
	{
		TextView textView = (TextView)findViewById(android.R.id.title);
		
		textView.setText(getCustomTitle());
	}
	
	abstract protected String getCustomTitle();
}
