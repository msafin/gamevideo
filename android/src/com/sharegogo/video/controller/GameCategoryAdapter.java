package com.sharegogo.video.controller;

import com.sharegogo.video.data.CategoryList.CategoryListItem;
import com.sharegogo.video.game.R;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class GameCategoryAdapter extends GameBaseAdapter<CategoryListItem>{

	public GameCategoryAdapter()
	{
		this(R.layout.list_item);
	}
	
	public GameCategoryAdapter(int layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Object createTag(View view) {
		// TODO Auto-generated method stub
		ViewTag tag = new ViewTag();
		
		tag.mLeftImageView = (ImageView)view.findViewById(android.R.id.icon);
		tag.mTitle = (TextView)view.findViewById(android.R.id.title);
		tag.mRightImageView = (ImageView)view.findViewById(android.R.id.icon1);
		
		return tag;
	}

	@Override
	protected View setupView(int position, View view) {
		// TODO Auto-generated method stub
		ViewTag tag = (ViewTag)view.getTag();
		
		CategoryListItem game = (CategoryListItem)getItem(position);
		
		tag.mLeftImageView.setVisibility(View.GONE);
		tag.mTitle.setText(game.name);
		
		return view;
	}
	
	static private class ViewTag
	{
		public ImageView mLeftImageView;
		public TextView mTitle;
		public ImageView mRightImageView;
	}
}
