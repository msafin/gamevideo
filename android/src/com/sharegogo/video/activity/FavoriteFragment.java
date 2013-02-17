package com.sharegogo.video.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.j256.ormlite.dao.Dao;
import com.sharegogo.video.SharegogoVideoApplication;
import com.sharegogo.video.controller.FavoriteAdapter;
import com.sharegogo.video.data.Favorite;
import com.sharegogo.video.data.FavoriteListItem;
import com.sharegogo.video.data.GameVideo;
import com.sharegogo.video.data.MySqliteHelper;
import com.sharegogo.video.game.R;

public class FavoriteFragment extends SherlockListFragment implements LoaderManager.LoaderCallbacks<List<FavoriteListItem>>{
	static private final int LOADER_ID = 1;
	private FavoriteAdapter mAdapter = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
		
		mAdapter = new FavoriteAdapter(getActivity().getApplicationContext(),R.layout.favorite_item);
		
		this.getLoaderManager().initLoader(LOADER_ID, null, this);
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.favorite_fragment, null);
	}


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		
        MenuItem editItem = menu.add(Menu.NONE, R.id.menu_id_edit, 0, R.string.menu_edit);
        editItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        
        MenuItem clearItem = menu.add(Menu.NONE, R.id.menu_id_clear, 0, R.string.menu_clear);
        clearItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
		case R.id.menu_id_edit:
			if(item.getIcon() == null)
			{
				item.setIcon(R.drawable.ic_accept);
				enterEditMode();
			}
			else
			{
				item.setIcon(null);
				exitEditMode();
			}
			break;
		case R.id.menu_id_clear:
			break;
			default:
				break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	private void enterEditMode()
	{
		mAdapter.enterEditMode();
	}
	
	private void exitEditMode()
	{
		mAdapter.exitEditMode();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		setHasOptionsMenu(true);
	}

	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		FavoriteListItem item = (FavoriteListItem)mAdapter.getItem(position);
		
		if(item.mode == FavoriteListItem.MODE_NORMAL)
		{
			gotoPlayActivity();
		}
		else if(item.mode == FavoriteListItem.MODE_EDIT)
		{
			delFavoriteItem(item);
		}
	}

	private void delFavoriteItem(FavoriteListItem item)
	{
		MySqliteHelper helper = SharegogoVideoApplication.getApplication().getHelper();
		
		try {
			Dao<Favorite,String> favoriteDao = null;
			
			favoriteDao = helper.getDao(Favorite.class);
			Favorite deleteItem = new Favorite();
			deleteItem.id = item.id;
			
			favoriteDao.delete(deleteItem);
			
			mAdapter.delItem(item);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void gotoPlayActivity()
	{
		Intent intent = new Intent(Intent.ACTION_MAIN);
		
		intent.setClass(getActivity(), PlayActivity.class);
		
		startActivity(intent);
	}
	
	@Override
	public Loader<List<FavoriteListItem>> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return new FavoriteLoader(getActivity().getApplicationContext());
	}
	
	@Override
	public void onLoadFinished(Loader<List<FavoriteListItem>> arg0,
			List<FavoriteListItem> arg1) {
		// TODO Auto-generated method stub
		
		mAdapter.setData(arg1);
		
		setListAdapter(mAdapter);
	}


	@Override
	public void onLoaderReset(Loader<List<FavoriteListItem>> arg0) {
		// TODO Auto-generated method stub
		
	}

	static private class FavoriteLoader extends AsyncTaskLoader<List<FavoriteListItem>>
	{
		public FavoriteLoader(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public List<FavoriteListItem> loadInBackground() {
			// TODO Auto-generated method stub
			MySqliteHelper helper = SharegogoVideoApplication.getApplication().getHelper();
			
			Dao<Favorite,String> favoriteDao = null;
			List<Favorite> favorite = null;
			List<FavoriteListItem> data = null;
			
			try {
				favoriteDao = helper.getDao(Favorite.class);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				favorite = favoriteDao.queryForAll();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(favorite != null && favorite.size() > 0)
			{
				Dao<GameVideo,String> videoDao = null;
				
				try {
					videoDao = helper.getDao(GameVideo.class);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(videoDao != null)
				{
					data = new ArrayList<FavoriteListItem>();
					
					for(Favorite item:favorite)
					{
						FavoriteListItem favoriteItem = new FavoriteListItem();
					
						favoriteItem.id = item.id;
						
						try {
							favoriteItem.video = videoDao.queryForId(String.valueOf(item.videoId));
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						data.add(favoriteItem);
					}
				}
			}
			
			return data;
		}

		@Override
		protected void onStartLoading() {
			// TODO Auto-generated method stub
			super.onStartLoading();
			
			this.forceLoad();
		}
	}
}
