package com.sharegogo.video.activity;

import java.sql.SQLException;
import java.util.List;

import com.actionbarsherlock.app.SherlockFragment;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.j256.ormlite.dao.Dao;
import com.sharegogo.video.SharegogoVideoApplication;
import com.sharegogo.video.controller.GameCategoryAdapter;
import com.sharegogo.video.controller.HistoryManager;
import com.sharegogo.video.controller.VideoAdapter;
import com.sharegogo.video.data.GameVideo;
import com.sharegogo.video.data.History;
import com.sharegogo.video.data.MySqliteHelper;
import com.sharegogo.video.game.R;
import com.sharegogo.video.utils.UIUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class VideoListFragment extends SherlockFragment implements OnItemClickListener{
	private List<GameVideo> mAllVideo = null;
	private VideoAdapter mVideoAdapter = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		MySqliteHelper data = SharegogoVideoApplication.getApplication().getHelper();
		
		
		try {
			Dao<GameVideo,String> dao = data.getDao(GameVideo.class);
			mAllVideo = dao.queryForAll();
			mVideoAdapter = new VideoAdapter(SharegogoVideoApplication.getApplication());
			mVideoAdapter.setData(mAllVideo);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View view = inflater.inflate(R.layout.list_layout, null);
		
		PullToRefreshListView pullListView = (PullToRefreshListView)view.findViewById(R.id.pull_refresh_list);
		
		ListView list = pullListView.getRefreshableView();
		
		list.setAdapter(mVideoAdapter);
		list.setOnItemClickListener(this);
		
		return view;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		GameVideo video = (GameVideo)mVideoAdapter.getItem(arg2-1);
		
		UIUtils.gotoPlayActivity(video,getActivity());
		
		addHistory(video);
	}
	
	private void addHistory(GameVideo video)
	{
		History history = new History();
		
		history.video_id = video.id;
		history.update = System.currentTimeMillis();
		
		HistoryManager.getInstance().addHistory(history);
	}
}
