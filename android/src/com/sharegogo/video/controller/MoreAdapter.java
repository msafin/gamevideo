package com.sharegogo.video.controller;

import com.sharegogo.video.game.R;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MoreAdapter extends GameBaseAdapter<String>{
	private int[] mIcons = new int[]{
			R.drawable.ic_share,
			R.drawable.ic_setting,
			R.drawable.ic_feedback,
			R.drawable.ic_update,
			R.drawable.ic_about,
			R.drawable.ic_quit_list};
	
	public MoreAdapter()
	{
		this(R.layout.more_list_item);
	}
	
	public MoreAdapter(int layout) {
		super(layout);
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
		tag.mLeftImageView.setImageResource(mIcons[position]);
		
		return view;
	}
	
	private class ViewTag 
	{
		ImageView mLeftImageView;
		TextView mTitle;
	}
}
