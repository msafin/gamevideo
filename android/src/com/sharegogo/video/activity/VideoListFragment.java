package com.sharegogo.video.activity;

import java.sql.SQLException;
import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnPullEventListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.j256.ormlite.dao.Dao;
import com.sharegogo.video.SharegogoVideoApplication;
import com.sharegogo.video.controller.HistoryManager;
import com.sharegogo.video.controller.VideoAdapter;
import com.sharegogo.video.controller.VideoManager;
import com.sharegogo.video.data.CategoryList;
import com.sharegogo.video.data.History;
import com.sharegogo.video.data.MySqliteHelper;
import com.sharegogo.video.data.VideoList;
import com.sharegogo.video.data.CategoryList.CategoryListItem;
import com.sharegogo.video.data.VideoList.VideoListItem;
import com.sharegogo.video.game.R;
import com.sharegogo.video.http.ResponseHandler;
import com.sharegogo.video.utils.NetworkUtils;
import com.sharegogo.video.utils.UIUtils;

public class VideoListFragment extends SherlockFragment implements OnItemClickListener,
	LoaderManager.LoaderCallbacks<VideoList>, ResponseHandler, OnPullEventListener<ListView>{
	private static final int PAGE_SIZE = 15;
	private VideoAdapter mVideoAdapter = null;
	private ListView mListView = null;
	private long mCID = -1;
	private int mListType = VideoList.TYPE_LIST_LATEST;
	private String mCategoryName = null;
	private int mVideoCount = 0;
	private int mPageNum = 1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
		Bundle args = getArguments();
		if(args != null)
		{
			mCID = args.getLong("cid");
			mListType = args.getInt("listType");
			mCategoryName = args.getString("categoryName");
		}
		
		if(NetworkUtils.isNetworkAvailable())
		{
			VideoManager.getInstance().getVideoList(mCID, mListType,mPageNum , PAGE_SIZE, 1, this);
		}
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View view = inflater.inflate(R.layout.list_layout, null);
		
		PullToRefreshListView pullListView = (PullToRefreshListView)view.findViewById(R.id.pull_refresh_list);
		pullListView.setOnPullEventListener(this);
		pullListView.setPullToRefreshOverScrollEnabled(false);
		
		mListView = pullListView.getRefreshableView();
		
		mListView.setOnItemClickListener(this);
		
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
		mListView.setAdapter(null);
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
		VideoListItem video = (VideoListItem)mVideoAdapter.getItem(arg2-1);
		
		UIUtils.gotoPlayActivity(video,getActivity());
		
		addHistory(video);
	}
	
	private void addHistory(VideoListItem video)
	{
		History history = new History();
		
		history.video_id = video.id;
		history.update = System.currentTimeMillis();
		
		HistoryManager.getInstance().addHistory(history);
	}

	@Override
	public Loader<VideoList> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<VideoList> arg0, VideoList arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<VideoList> arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onSuccess(Object data) {
		// TODO Auto-generated method stub
		VideoList videoList = (VideoList)data;
		if(videoList != null && videoList.list != null && videoList.list.length > 0)
		{
			mVideoCount = videoList.count;
			
			if(mVideoAdapter == null)
			{
				mVideoAdapter = new VideoAdapter();
				
				mListView.setAdapter(mVideoAdapter);
			}
			
			mVideoAdapter.addData(Arrays.asList(videoList.list));
		}
	}


	@Override
	public void onFailed(int what, Object msg) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean onPersistent(Object data) {
		// TODO Auto-generated method stub
		MySqliteHelper dbHelper = SharegogoVideoApplication.getApplication().getHelper();
		VideoList videoList = (VideoList)data;
		
		if(videoList.list != null && videoList.list.length > 0)
		{
			try {
				Dao<VideoListItem,String> dao = dbHelper.getDao(VideoListItem.class);
				
				for(VideoListItem item:videoList.list)
				{
					item.categoryName = mCategoryName;
					dao.createOrUpdate(item);
				}
				
				return true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				return false;
			}
		}
		
		return false;
	}


	@Override
	public void onPullEvent(PullToRefreshBase<ListView> refreshView,
			State state, Mode direction) {
		// TODO Auto-generated method stub
		switch(state)
		{
		case PULL_TO_REFRESH:
			mPageNum++;
			VideoManager.getInstance().getVideoList(mCID, mListType,mPageNum , PAGE_SIZE, 1, this);
			break;
			default:
				break;
		}
	}
}
