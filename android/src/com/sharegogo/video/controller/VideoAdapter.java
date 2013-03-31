package com.sharegogo.video.controller;

import com.sharegogo.video.data.VideoList.VideoListItem;
import com.sharegogo.video.game.R;
import com.sharegogo.video.utils.UIUtils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoAdapter extends GameBaseAdapter<VideoListItem>{

	public VideoAdapter()
	{
		this(R.layout.video_list_item);
	}
	
	public VideoAdapter(int layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Object createTag(View view) {
		// TODO Auto-generated method stub
		ViewTag tag = new ViewTag();
		
		tag.mLeftImageView = (ImageView)view.findViewById(android.R.id.icon);
		tag.mTitle = (TextView)view.findViewById(android.R.id.title);
		tag.mSource = (TextView)view.findViewById(R.id.source);
		tag.mRightImageView = (ImageView)view.findViewById(android.R.id.icon1);
		
		return tag;
	}

	@Override
	protected View setupView(int position, View view) {
		// TODO Auto-generated method stub
		ViewTag tag = (ViewTag)view.getTag();
		
		VideoListItem video = (VideoListItem)getItem(position);
		
		tag.mTitle.setText(video.name);
		if(video.from != null)
			tag.mSource.setText("ю╢вт:" + video.from.trim());
		
		UIUtils.DisplayImage(video.img, tag.mLeftImageView, R.drawable.ic_launcher);
		
		return view;
	}
	
	static private class ViewTag
	{
		public ImageView mLeftImageView;
		public TextView mTitle;
		public TextView mSource;
		public ImageView mRightImageView;
	}
}
