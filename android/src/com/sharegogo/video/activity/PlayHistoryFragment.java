package com.sharegogo.video.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.actionbarsherlock.app.SherlockFragment;
import com.sharegogo.video.game.R;

public class PlayHistoryFragment extends SherlockFragment{

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.play_history_fragment, null);
		
        return view;
	}
}
