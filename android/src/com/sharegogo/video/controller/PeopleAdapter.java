package com.sharegogo.video.controller;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharegogo.video.data.GamePeople;
import com.sharegogo.video.game.R;

public class PeopleAdapter extends GameBaseAdapter<GamePeople>{

	public PeopleAdapter(Context context)
	{
		this(context,R.layout.list_item);
	}
	
	public PeopleAdapter(Context context, int layout) {
		super(context, layout);
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
		
		GamePeople people = (GamePeople)getItem(position);
		
		tag.mTitle.setText(people.name);
		
		return view;
	}
	
	static private class ViewTag
	{
		public ImageView mLeftImageView;
		public TextView mTitle;
		public ImageView mRightImageView;
	}
}
