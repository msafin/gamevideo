package com.sharegogo.video.controller;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.j256.ormlite.dao.Dao;
import com.sharegogo.video.SharegogoVideoApplication;
import com.sharegogo.video.data.MySqliteHelper;
import com.sharegogo.video.data.VideoDetail;
import com.sharegogo.video.data.VideoList;

public class VideoListLoader extends AsyncTaskLoader<List<VideoDetail>>
{
	private long mCid = -1;
	private int mListType = VideoList.TYPE_LIST_LATEST;
	
	public VideoListLoader(Context context,long cid,int listType) {
		super(context);
		// TODO Auto-generated constructor stub
		mCid = cid;
		mListType = listType;
	}

	@Override
	public List<VideoDetail> loadInBackground() {
		// TODO Auto-generated method stub
		MySqliteHelper dbHelper = SharegogoVideoApplication.getApplication().getHelper();
		List<VideoDetail> videoList = null;
		
		try {
			Dao<VideoDetail,String> dao = dbHelper.getDao(VideoDetail.class);
			
			videoList = dao.queryForEq("cid", String.valueOf(mCid));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return videoList;
	}

	@Override
	protected void onStartLoading() {
		// TODO Auto-generated method stub
		super.onStartLoading();
		forceLoad();
	}
}
