package com.sharegogo.video.activity;

import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sharegogo.video.game.R;
import com.sharegogo.video.settings.SharegogoVideoSettings;
import com.sharegogo.video.utils.ResUtils;

public class SettingsActivity extends PreferenceActivity implements OnPreferenceChangeListener{
	private Preference mWifiImage = null;
	private Preference mUseFlash = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.settings_activity);
		
		this.addPreferencesFromResource(R.xml.settings_activity);
		
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
		
		mWifiImage = findPreference(ResUtils.getString(R.string.key_wifi_image));
		mWifiImage.setOnPreferenceChangeListener(this);
		
		mUseFlash = findPreference(ResUtils.getString(R.string.key_use_flash));
		mUseFlash.setOnPreferenceChangeListener(this);
		
		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB)
		{
			mUseFlash.setEnabled(false);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		// TODO Auto-generated method stub
		if(preference == mWifiImage)
		{
			if((Boolean)newValue == true)
			{
				SharegogoVideoSettings.setWifiFetchImage(1);
			}
			else
			{
				SharegogoVideoSettings.setWifiFetchImage(0);
			}
		}
		else if(preference == mUseFlash)
		{

			if((Boolean)newValue == true)
			{
				SharegogoVideoSettings.setUseFlash(1);
			}
			else
			{
				SharegogoVideoSettings.setUseFlash(0);
			}
		}
		
		return true;
	}

}
