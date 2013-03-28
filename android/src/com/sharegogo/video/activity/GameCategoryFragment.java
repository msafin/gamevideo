package com.sharegogo.video.activity;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.j256.ormlite.dao.Dao;
import com.sharegogo.video.SharegogoVideoApplication;
import com.sharegogo.video.controller.GameCategoryAdapter;
import com.sharegogo.video.controller.UserManager;
import com.sharegogo.video.controller.UserManager.TokenObserver;
import com.sharegogo.video.controller.VideoManager;
import com.sharegogo.video.data.CategoryList;
import com.sharegogo.video.data.CategoryList.CategoryListItem;
import com.sharegogo.video.data.MySqliteHelper;
import com.sharegogo.video.game.R;
import com.sharegogo.video.http.ResponseHandler;
import com.sharegogo.video.utils.NetworkUtils;

public class GameCategoryFragment extends SherlockFragment implements OnItemClickListener, 
ResponseHandler, TokenObserver,LoaderManager.LoaderCallbacks<List<CategoryListItem>>{
	private static final int CATEGORYLIST_LOADER = 1;
	private GameCategoryAdapter mGamesAdapter = null;
	private ListView mListView = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		if(NetworkUtils.isNetworkAvailable())
		{
			if(UserManager.getInstance().getToken() != null)
			{
				VideoManager.getInstance().getVideoCategory(0, this);
			}
			else
			{
				UserManager.getInstance().getToken(this);
			}
		}
		else
		{
			//本地获取
			this.getLoaderManager().initLoader(CATEGORYLIST_LOADER, null, this);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View view = inflater.inflate(R.layout.list_layout, null);
		
		PullToRefreshListView pullListView = (PullToRefreshListView)view.findViewById(R.id.pull_refresh_list);
		
		pullListView.setMode(Mode.DISABLED);
		
		mListView = pullListView.getRefreshableView();
		mListView.setOnItemClickListener(this);
		
		return view;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		mListView.setAdapter(null);
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
		GameFragment gameFragment = new GameFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("type", arg2);
		
		gameFragment.setArguments(bundle);
		
		this.getFragmentManager().beginTransaction()
			.addToBackStack(null)
			.add(R.id.dynamic_content,gameFragment)
			.commit();
	}

	@Override
	public void onSuccess(Object data) {
		// TODO Auto-generated method stub
		this.getLoaderManager().restartLoader(CATEGORYLIST_LOADER, null, this);
	}

	@Override
	public void onFailed(int what, Object msg) {
		// TODO Auto-generated method stub
		this.getLoaderManager().restartLoader(CATEGORYLIST_LOADER, null, this);
	}

	/*
	 * 保存数据库
	 */
	@Override
	public boolean onPersistent(Object data) {
		// TODO Auto-generated method stub
		MySqliteHelper dbHelper = SharegogoVideoApplication.getApplication().getHelper();
		CategoryList categoryList = (CategoryList)data;
		
		if(categoryList.categories != null && categoryList.categories.length > 0)
		{
			try {
				Dao<CategoryListItem,String> dao = dbHelper.getDao(CategoryListItem.class);
				
				for(CategoryListItem item:categoryList.categories)
				{
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
	public void onTokenSuccess() {
		// TODO Auto-generated method stub
		VideoManager.getInstance().getVideoCategory(0, this);
	}

	@Override
	public void onTokenFailed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Loader<List<CategoryListItem>> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return new CategoryListLoader(SharegogoVideoApplication.getApplication());
	}

	@Override
	public void onLoadFinished(Loader<List<CategoryListItem>> arg0,
			List<CategoryListItem> arg1) {
		// TODO Auto-generated method stub
		List<CategoryListItem> categoryList = arg1;
		
		if(categoryList != null && categoryList.size() > 0)
		{
			if(mGamesAdapter == null)
			{
				mGamesAdapter = new GameCategoryAdapter(SharegogoVideoApplication.getApplication());
			}
			else
			{
				mGamesAdapter.clearData();
			}
			
			mGamesAdapter.setData(categoryList);
			mListView.setAdapter(mGamesAdapter);
		}
		
	}

	@Override
	public void onLoaderReset(Loader<List<CategoryListItem>> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	static private class CategoryListLoader extends AsyncTaskLoader<List<CategoryListItem>>
	{
		public CategoryListLoader(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public List<CategoryListItem> loadInBackground() {
			// TODO Auto-generated method stub
			MySqliteHelper dbHelper = SharegogoVideoApplication.getApplication().getHelper();
			List<CategoryListItem> categoryList = null;
			
			try {
				Dao<CategoryListItem,String> dao = dbHelper.getDao(CategoryListItem.class);
				
				categoryList = dao.queryForAll();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return categoryList;
		}

		@Override
		protected void onStartLoading() {
			// TODO Auto-generated method stub
			super.onStartLoading();
			forceLoad();
		}
	}
}
