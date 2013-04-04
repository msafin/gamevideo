package com.sharegogo.video.activity;

import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.j256.ormlite.dao.Dao;
import com.sharegogo.video.SharegogoVideoApplication;
import com.sharegogo.video.controller.HistoryManager;
import com.sharegogo.video.controller.VideoAdapter;
import com.sharegogo.video.controller.VideoListLoader;
import com.sharegogo.video.controller.VideoManager;
import com.sharegogo.video.data.History;
import com.sharegogo.video.data.MySqliteHelper;
import com.sharegogo.video.data.VideoDetail;
import com.sharegogo.video.data.VideoList;
import com.sharegogo.video.data.VideoList.VideoListItem;
import com.sharegogo.video.game.R;
import com.sharegogo.video.http.ResponseHandler;
import com.sharegogo.video.utils.NetworkUtils;
import com.sharegogo.video.utils.ResUtils;
import com.sharegogo.video.utils.UIUtils;

public class VideoListFragment extends SherlockFragment implements OnItemClickListener,
	LoaderManager.LoaderCallbacks<List<VideoDetail>>, ResponseHandler,OnClickListener{
	private static final int VIDEO_LOADER = 1;
	private static final int PAGE_SIZE = 15;
	private VideoAdapter mVideoAdapter = null;
	private ListView mListView = null;
	private View mLoadMore = null;
	private View mLoading = null;
	private long mCId = -1;
	private int mListType = VideoList.TYPE_LIST_LATEST;
	private String mCategoryName = null;
	private int mVideoCount = 0;
	private ProgressDialog mProgressDialog = null;
	
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
			mCId = args.getLong("cid");
			mListType = args.getInt("listType");
			mCategoryName = args.getString("categoryName");
		}
		
		mVideoAdapter = new VideoAdapter();
		
		mProgressDialog = new ProgressDialog(activity);
		mProgressDialog.setMessage(ResUtils.getString(R.string.loading));
		
		mProgressDialog.show();
		
		if(NetworkUtils.isNetworkAvailable())
		{
			//加载第一页
			VideoManager.getInstance().getVideoList(mCId, mListType,1 , PAGE_SIZE, 1, this);
		}
		else
		{
			//没有网络从数据库中一次性加载
			this.getLoaderManager().initLoader(VIDEO_LOADER, null, this);
		}
	}


	private void showLoading()
	{
		mLoading.setVisibility(View.VISIBLE);
		mLoadMore.setVisibility(View.GONE);
	}
	
	private void showLoadMore(boolean show)
	{
		if(show)
		{
			mLoadMore.setVisibility(View.VISIBLE);
			mLoading.setVisibility(View.GONE);
		}
		else
		{
			mLoadMore.setVisibility(View.GONE);
			mLoading.setVisibility(View.GONE);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View view = inflater.inflate(R.layout.list_layout, null);
		View footerView = inflater.inflate(R.layout.load_more, null);
		
		PullToRefreshListView pullListView = (PullToRefreshListView)view.findViewById(R.id.pull_refresh_list);
		pullListView.setMode(Mode.DISABLED);
		
		mLoadMore = footerView.findViewById(R.id.load_more);
		mLoading = footerView.findViewById(R.id.loading_more);
		
		mLoading.setOnClickListener(this);
		mLoadMore.setOnClickListener(this);
		
		mListView = pullListView.getRefreshableView();
		mListView.setOnItemClickListener(this);
		mListView.addFooterView(footerView);
		mListView.setFooterDividersEnabled(true);

		mListView.setAdapter(mVideoAdapter);
		
		showLoadMore(false);
		
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
		VideoDetail video = (VideoDetail)mVideoAdapter.getItem(arg2-1);
		
		UIUtils.gotoPlayActivity(video,getActivity());
		
		addHistory(video);
	}
	
	private void addHistory(VideoDetail video)
	{
		History history = new History();
		
		history.video_id = video.id;
		history.update = System.currentTimeMillis();
		
		HistoryManager.getInstance().addHistory(history);
	}

	@Override
	public void onSuccess(Object data) {
		// TODO Auto-generated method stub
		mProgressDialog.dismiss();
		
		VideoList videoList = (VideoList)data;
		if(videoList != null && videoList.list != null && videoList.list.length > 0)
		{
			mVideoCount = videoList.count;
			
			mVideoAdapter.addData(videoList.toVideoDetailList());
		}
		
		if(mVideoAdapter.getCount() < mVideoCount)
		{
			showLoadMore(true);
		}
		else
		{
			showLoadMore(false);
		}
	}


	@Override
	public void onFailed(int what, Object msg) {
		// TODO Auto-generated method stub
		mProgressDialog.dismiss();
		
		if(mVideoAdapter.getCount() < mVideoCount)
		{
			showLoadMore(true);
		}
		else
		{
			showLoadMore(false);
		}
	}


	@Override
	public boolean onPersistent(Object data) {
		// TODO Auto-generated method stub
		MySqliteHelper dbHelper = SharegogoVideoApplication.getApplication().getHelper();
		VideoList videoList = (VideoList)data;
		
		if(videoList.list != null && videoList.list.length > 0)
		{
			try {
				Dao<VideoDetail,String> dao = dbHelper.getDao(VideoDetail.class);
				
				for(VideoListItem item:videoList.list)
				{
					VideoDetail detail = item.toVideoDetail();
					detail.type = mCategoryName;
					detail.cid = mCId;
					
					dao.createOrUpdate(detail);
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
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId())
		{
		case R.id.load_more:
			showLoading();
			int pageNum = mVideoAdapter.getCount() / PAGE_SIZE + 1;
			Log.e("test","count = " + mVideoAdapter.getCount() + ",page = " + pageNum);
			VideoManager.getInstance().getVideoList(mCId, mListType, pageNum, PAGE_SIZE, 1, this);
			break;
		case R.id.loading_more:
			break;
		}
	}


	@Override
	public void onLoadFinished(Loader<List<VideoDetail>> arg0,
			List<VideoDetail> arg1) {
		// TODO Auto-generated method stub
		mProgressDialog.dismiss();
		
		//没有网络的时候才会通过loader加载
		mVideoAdapter.clearData();
		mVideoAdapter.addData(arg1);
	}


	@Override
	public void onLoaderReset(Loader<List<VideoDetail>> arg0) {
		// TODO Auto-generated method stub
		mProgressDialog.dismiss();
	}


	@Override
	public Loader<List<VideoDetail>> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return new VideoListLoader(SharegogoVideoApplication.getApplication(),mCId,mListType);
	}
}
