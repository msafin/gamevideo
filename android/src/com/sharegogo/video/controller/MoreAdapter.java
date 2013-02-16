package com.sharegogo.video.controller;

import com.sharegogo.video.data.Game;
import com.sharegogo.video.game.R;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MoreAdapter extends GameBaseAdapter<String>{

	public MoreAdapter(Context context)
	{
		this(context,R.layout.more_list_item);
	}
	
	public MoreAdapter(Context context, int layout) {
		super(context, layout);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Object createTag(View view) {
		// TODO Auto-generated method stub
		ViewTag tag = new ViewTag();
		
		tag.mLeftImageView = (ImageView)view.findViewById(android.R.id.icon);
		tag.mTitle = (TextView)view.findViewById(android.R.id.title);
		
		return tag;
	}

	@Override
	protected View setupView(int position, View view) {
		// TODO Auto-generated method stub
		ViewTag tag = (ViewTag)view.getTag();
		
		String title = (String)getItem(position);
		
		tag.mTitle.setText(title);
		
		return view;
	}
	
	private class ViewTag 
	{
		ImageView mLeftImageView;
		TextView mTitle;
	}
}
