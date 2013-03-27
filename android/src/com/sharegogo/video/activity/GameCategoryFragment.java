package com.sharegogo.video.activity;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.actionbarsherlock.app.SherlockFragment;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.j256.ormlite.dao.Dao;
import com.sharegogo.video.SharegogoVideoApplication;
import com.sharegogo.video.controller.GameCategoryAdapter;
import com.sharegogo.video.controller.VideoManager;
import com.sharegogo.video.data.CategoryList;
import com.sharegogo.video.data.CategoryList.CategoryListItem;
import com.sharegogo.video.data.MySqliteHelper;
import com.sharegogo.video.game.R;
import com.sharegogo.video.http.ResponseHandler;
import com.sharegogo.video.utils.NetworkUtils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class GameCategoryFragment extends SherlockFragment implements OnItemClickListener, ResponseHandler{
	private List<CategoryListItem> mAllGames = null;
	private GameCategoryAdapter mGamesAdapter = null;
	private ListView mListView = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		if(NetworkUtils.isNetworkAvailable())
		{
			VideoManager.getInstance().getVideoCategory(0, this);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View view = inflater.inflate(R.layout.list_layout, null);
		
		PullToRefreshListView pullListView = (PullToRefreshListView)view.findViewById(R.id.pull_refresh_list);
		
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
		CategoryList categoryList = (CategoryList)data;
		
		if(categoryList.categories != null)
		{
			mAllGames = Arrays.asList(categoryList.categories);
			if(mAllGames != null)
			{
				mGamesAdapter = new GameCategoryAdapter(SharegogoVideoApplication.getApplication());
				mGamesAdapter.setData(mAllGames);
				mListView.setAdapter(mGamesAdapter);
			}
		}
		
	}

	@Override
	public void onFailed(int what, Object msg) {
		// TODO Auto-generated method stub
		if(msg != null && msg instanceof String)
		{
			Toast.makeText(SharegogoVideoApplication.getApplication(), (String)msg, 1000).show();
		}
	}

	/*
	 * ±£´æÊý¾Ý¿â
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
}
