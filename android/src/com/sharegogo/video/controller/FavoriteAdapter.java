package com.sharegogo.video.controller;

import com.sharegogo.video.activity.FavoriteFragment.FavoriteListItem;
import com.sharegogo.video.game.R;
import com.sharegogo.video.utils.UIUtils;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class FavoriteAdapter extends GameBaseAdapter<FavoriteListItem>{

	public FavoriteAdapter(int layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Object createTag(View view) {
		// TODO Auto-generated method stub
		ViewTag tag = new ViewTag();
		
		tag.mLeftImageView = (ImageView)view.findViewById(android.R.id.icon);
		tag.mTitle = (TextView)view.findViewById(android.R.id.title);
//		tag.mGameName = (TextView)view.findViewById(R.id.game_name);
		tag.mAuthorName = (TextView)view.findViewById(R.id.author_name);
		tag.mPlay = (ImageButton)view.findViewById(R.id.play_btn);
		tag.mDelete = (ImageButton)view.findViewById(R.id.delete_btn);
				
		return tag;
	}

	@Override
	protected View setupView(int position, View view) {
		// TODO Auto-generated method stub
		FavoriteListItem item = (FavoriteListItem)getItem(position);
		ViewTag tag = (ViewTag)view.getTag();
		
		if(item.video == null)
		{
			return view;
		}
		
		if(item.video.name != null)
		{
			String name = null;
			try{
				String videoName = item.video.name.split("在线播放")[0];
				name = videoName.substring(0, videoName.length()-1);
			}catch(Exception ee)
			{
				ee.printStackTrace();
			}
			if(name == null || name.equals(""))
			{
				name = item.video.name;
			}
			tag.mTitle.setText(name);
			tag.mTitle.setVisibility(View.VISIBLE);
		}
		else
		{
			tag.mTitle.setVisibility(View.INVISIBLE);
		}
		
//		if(item.video.type != null)
//		{
//			tag.mGameName.setText(item.video.type);
//			tag.mGameName.setVisibility(View.VISIBLE);
//		}
//		else
//		{
//			tag.mGameName.setVisibility(View.INVISIBLE);
//		}
		
		if(item.video.author != null)
		{
			tag.mAuthorName.setText("作者:"+item.video.author);
			tag.mAuthorName.setVisibility(View.VISIBLE);
		}
		else
		{
			tag.mAuthorName.setVisibility(View.INVISIBLE);
		}
		
		UIUtils.DisplayImage(item.video.img, tag.mLeftImageView, R.drawable.default_bg);
		
		if(item.mode == FavoriteListItem.MODE_EDIT)
		{
			tag.mPlay.setVisibility(View.INVISIBLE);
			tag.mDelete.setVisibility(View.VISIBLE);
		}
		else if(item.mode == FavoriteListItem.MODE_NORMAL)
		{
			tag.mPlay.setVisibility(View.VISIBLE);
			tag.mDelete.setVisibility(View.INVISIBLE);
		}
		
		return view;
	}

	public void delItem(FavoriteListItem item)
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
			FavoriteListItem item = (FavoriteListItem)obj;
			
			item.mode = FavoriteListItem.MODE_EDIT;
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
				FavoriteListItem item = (FavoriteListItem)obj;
				
				item.mode = FavoriteListItem.MODE_NORMAL;
			}
			
			notifyDataSetChanged();
		}
		
		return true;
	}
	
	static private class ViewTag
	{
		public ImageView mLeftImageView;
		public TextView mTitle;
//		public TextView mGameName;
		public TextView mAuthorName;
		public ImageButton mPlay;
		public ImageButton mDelete;
	}
}
