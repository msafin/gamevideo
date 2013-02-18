package com.sharegogo.video.controller;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

abstract public  class GameBaseAdapter<T> extends BaseAdapter{
	private Context mContext = null;
	private int mLayoutId = -1;
	protected List<T> mData = null;
	
	public GameBaseAdapter(Context context,int layout)
	{
		mContext = context;
		mLayoutId = layout;
	}
	
	public void setData(List<T> data)
	{
		mData = data;
		
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mData != null)
		{
			return mData.size();
		}
		
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		if(mData != null)
		{
			return mData.get(arg0);
		}
		
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		if(mData != null)
		{
			return arg0;
		}
		
		return 0;
	}

	public void clearData()
	{
		if(mData != null)
		{
			mData.clear();
			mData = null;
		}
		
		this.notifyDataSetChanged();
	}
	
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if(arg1 == null)
		{
			LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			arg1 = inflater.inflate(mLayoutId, null);
			
			Object tag = createTag(arg1);
			arg1.setTag(tag);
		}
		
		return setupView(arg0,arg1);
	}
	
	abstract protected  Object createTag(View view);
	abstract protected  View setupView(int position,View view);
}
