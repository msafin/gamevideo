package com.sharegogo.video.activity;

import java.util.List;

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
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.sharegogo.video.SharegogoVideoApplication;
import com.sharegogo.video.controller.CategoryListLoader;
import com.sharegogo.video.controller.GameCategoryAdapter;
import com.sharegogo.video.data.VideoList;
import com.sharegogo.video.data.CategoryList.CategoryListItem;
import com.sharegogo.video.game.R;

public class AuthorFragment extends SherlockFragment implements OnItemClickListener,LoaderManager.LoaderCallbacks<List<CategoryListItem>>{
	private static final int CATEGORYLIST_LOADER = 1;
	private GameCategoryAdapter mAuthorAdapter = null;
	private long mParentId = -1;
	private String mCategoryName = null;
	private ListView mListView = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
		Bundle args = this.getArguments();
		
		if(args != null)
		{
			mParentId = args.getLong("parentId");
			mCategoryName = args.getString("categoryName");
		}
		
		mAuthorAdapter = new GameCategoryAdapter();
		
		//本地获取
		this.getLoaderManager().initLoader(CATEGORYLIST_LOADER, null, this);
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
		mListView.setAdapter(mAuthorAdapter);
		
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		VideoListFragment videoListFragment = new VideoListFragment();
		Bundle args = new Bundle();
		CategoryListItem item = (CategoryListItem)mAuthorAdapter.getItem(arg2 - 1);
		args.putLong("cid", item.id);
		args.putInt("listType", VideoList.TYPE_LIST_LATEST);
		args.putString("categoryName", mCategoryName);
		
		videoListFragment.setArguments(args);
		
		this.getFragmentManager().beginTransaction()
			.addToBackStack(null)
			.add(R.id.dynamic_content,videoListFragment)
			.commit();
	}

	@Override
	public Loader<List<CategoryListItem>> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return new CategoryListLoader(SharegogoVideoApplication.getApplication(),mParentId);
	}

	@Override
	public void onLoadFinished(Loader<List<CategoryListItem>> arg0,
			List<CategoryListItem> arg1) {
		// TODO Auto-generated method stub
		List<CategoryListItem> categoryList = arg1;
		
		if(categoryList != null && categoryList.size() > 0)
		{
			mAuthorAdapter.clearData();
			mAuthorAdapter.addData(categoryList);
			mListView.setAdapter(mAuthorAdapter);
		}
	}

	@Override
	public void onLoaderReset(Loader<List<CategoryListItem>> arg0) {
		// TODO Auto-generated method stub
		
	}
}
