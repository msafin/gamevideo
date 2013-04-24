package com.sharegogo.video.controller;

import com.sharegogo.video.SharegogoVideoApplication;
import com.sharegogo.video.activity.HistoryFragment.HistoryListItem;
import com.sharegogo.video.game.R;
import com.sharegogo.video.utils.UIUtils;

import android.content.Context;
import android.support.v4.util.TimeUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class HistoryAdapter extends GameBaseAdapter<HistoryListItem>{

	public HistoryAdapter(int layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Object createTag(View view) {
		// TODO Auto-generated method stub
		ViewTag tag = new ViewTag();
		
		tag.mLeftImageView = (ImageView)view.findViewById(android.R.id.icon);
		tag.mTitle = (TextView)view.findViewById(android.R.id.title);
		tag.mGameName = (TextView)view.findViewById(R.id.game_name);
		tag.mAuthorName = (TextView)view.findViewById(R.id.author_name);
		tag.mTime = (TextView)view.findViewById(R.id.time);
		tag.mPlay = (ImageButton)view.findViewById(R.id.play_btn);
		tag.mDelete = (ImageButton)view.findViewById(R.id.delete_btn);
				
		return tag;
	}

	@Override
	protected View setupView(int position, View view) {
		// TODO Auto-generated method stub
		HistoryListItem item = (HistoryListItem)getItem(position);
		ViewTag tag = (ViewTag)view.getTag();
		
		if(item.video != null)
		{
			if(item.video.name != null)
				tag.mTitle.setText(item.video.name);
			
			if(item.video.author != null)
				tag.mAuthorName.setText(item.video.author);
			
			UIUtils.DisplayImage(item.video.img, tag.mLeftImageView, R.drawable.default_bg);
			
			if(item.video.type != null)
			{
				tag.mGameName.setText(item.video.type);
				tag.mGameName.setVisibility(View.VISIBLE);
			}
			else
			{
				tag.mGameName.setVisibility(View.INVISIBLE);
			}
		}
			
		
		String time = DateUtils.formatDateTime(
				SharegogoVideoApplication.getApplication(), 
				item.time,  
				DateUtils.FORMAT_NUMERIC_DATE | 
				DateUtils.FORMAT_SHOW_TIME | 
				DateUtils.FORMAT_SHOW_DATE |
				DateUtils.FORMAT_SHOW_YEAR |
				DateUtils.FORMAT_24HOUR);
		tag.mTime.setText(time);
		
		if(item.mode == HistoryListItem.MODE_EDIT)
		{
			tag.mPlay.setVisibility(View.INVISIBLE);
			tag.mDelete.setVisibility(View.VISIBLE);
		}
		else if(item.mode == HistoryListItem.MODE_NORMAL)
		{
			tag.mPlay.setVisibility(View.VISIBLE);
			tag.mDelete.setVisibility(View.INVISIBLE);
		}
		
		return view;
	}

	public void delItem(HistoryListItem item)
	{
		mData.remove(item);
		
		notifyDataSetChanged();
	}
	
	public boolean enterEditMode()
	{
		if(mData == null)
		{
			return false;
		}
		
		for(Object obj:mData)
		{
			HistoryListItem item = (HistoryListItem)obj;
			
			item.mode = HistoryListItem.MODE_EDIT;
		}
		
		notifyDataSetChanged();
		
		return true;
	}
	
	public boolean exitEditMode()
	{
		if(mData != null)
		{
			for(Object obj:mData)
			{
				HistoryListItem item = (HistoryListItem)obj;
				
				item.mode = HistoryListItem.MODE_NORMAL;
			}
			
			notifyDataSetChanged();
		}
		
		return true;
	}
	
	static private class ViewTag
	{
		public ImageView mLeftImageView;
		public TextView mTitle;
		public TextView mGameName;
		public TextView mAuthorName;
		public TextView mTime;
		public ImageButton mPlay;
		public ImageButton mDelete;
	}
}
