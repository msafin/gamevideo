package com.sharegogo.video.controller;

import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharegogo.video.SharegogoVideoApplication;
import com.sharegogo.video.data.VideoDetail;
import com.sharegogo.video.game.R;
import com.sharegogo.video.utils.UIUtils;

public class VideoAdapter extends GameBaseAdapter<VideoDetail>{

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
		tag.mHot = (TextView)view.findViewById(R.id.hot);
		tag.mUpdateTime = (TextView)view.findViewById(R.id.update_time);
		tag.mRightImageView = (ImageView)view.findViewById(android.R.id.icon1);
		
		return tag;
	}

	@Override
	protected View setupView(int position, View view) {
		// TODO Auto-generated method stub
		ViewTag tag = (ViewTag)view.getTag();
		
		VideoDetail video = (VideoDetail)getItem(position);
		String name = null;
		try{
			String videoName = video.name.split("在线播放")[0];
			name = videoName.substring(0, videoName.length()-1);
		}catch(Exception ee)
		{
			ee.printStackTrace();
		}
		if(name == null || name.equals(""))
		{
			name = video.name;
		}
		tag.mTitle.setText(name);
		if(video.from != null)
			tag.mSource.setText("来自:" + video.from.trim());
		
		tag.mHot.setText("播放:" + String.valueOf(video.hot));
		
		String time = DateUtils.formatDateTime(
				SharegogoVideoApplication.getApplication(), 
				video.lastUpdateTime,  
				DateUtils.FORMAT_NUMERIC_DATE | 
				DateUtils.FORMAT_SHOW_DATE |
				DateUtils.FORMAT_SHOW_YEAR );
		if(time != null)
		{
			time = time.replace('-', '.');
			tag.mUpdateTime.setText("时间:"+time);
		}
		
		UIUtils.DisplayImage(video.img, tag.mLeftImageView, R.drawable.default_bg);
		
		return view;
	}
	
	static private class ViewTag
	{
		public ImageView mLeftImageView;
		public TextView mTitle;
		public TextView mSource;
		public TextView mHot;
		public TextView mUpdateTime;
		public ImageView mRightImageView;
	}
}
