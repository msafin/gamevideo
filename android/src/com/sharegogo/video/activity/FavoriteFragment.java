package com.sharegogo.video.activity;


import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.sharegogo.video.controller.FavoriteAdapter;
import com.sharegogo.video.controller.FavoriteManager;
import com.sharegogo.video.controller.HistoryManager;
import com.sharegogo.video.data.Favorite;
import com.sharegogo.video.data.VideoList.VideoListItem;
import com.sharegogo.video.game.R;
import com.sharegogo.video.utils.ResUtils;
import com.sharegogo.video.utils.UIUtils;

public class FavoriteFragment extends SherlockListFragment implements LoaderManager.LoaderCallbacks<List<FavoriteFragment.FavoriteListItem>>, OnClickListener{
	static private final int LOADER_ID = 1;
	
	private FavoriteAdapter mAdapter = null;
	private TextView mEmptyText = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
		
		mAdapter = new FavoriteAdapter(R.layout.favorite_item);
		
		this.getLoaderManager().initLoader(LOADER_ID, null, this);
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.favorite_fragment, null);
		mEmptyText = (TextView)view.findViewById(R.id.empty_text);
		
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
				if(mAdapter.enterEditMode())
				{
					item.setIcon(R.drawable.ic_accept);
				}
			}
			else
			{
				if(mAdapter.exitEditMode())
				{
					item.setIcon(null);
				}
			}
			break;
		case R.id.menu_id_clear:
			showClearFavoriteDialog();
			break;
			default:
				break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	private void showClearFavoriteDialog()
	{
		new GameDialogFragment(
				R.string.clear_favorite,
				R.string.clear_favorite_confirm,
				R.string.dialog_ok,
				R.string.dialog_cancel,
				this
				).show(getFragmentManager(), null);
	}
	
	private void clearFavorite()
	{
		if(FavoriteManager.getInstance().clearFavorite())
		{
			mAdapter.clearData();
			
			Toast toast = Toast.makeText(getActivity(), R.string.clear_favorite_complete, 2000);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		switch(which)
		{
		case DialogInterface.BUTTON_POSITIVE:
			clearFavorite();
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			
			break;
		}
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
			UIUtils.gotoPlayActivity(item.video,getActivity());
			
			if(HistoryManager.getInstance().addHistory(item.video.id))
			{
				Toast.makeText(getActivity(), "success", 1000).show();
			}
			else
			{
				Toast.makeText(getActivity(), "failed", 1000).show();
			}
		}
		else if(item.mode == FavoriteListItem.MODE_EDIT)
		{
			delFavoriteItem(item);
		}
	}

	private void delFavoriteItem(FavoriteListItem item)
	{
		Favorite delItem = new Favorite();
		delItem.id = item.id;
		delItem.video_id = item.video.id;
		
		if(FavoriteManager.getInstance().delFavorite(delItem))
		{
			mAdapter.delItem(item);
		}
	}
	
	@Override
	public Loader<List<FavoriteFragment.FavoriteListItem>> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return new FavoriteLoader(getActivity().getApplicationContext());
	}
	
	@Override
	public void onLoadFinished(Loader<List<FavoriteListItem>> arg0,
			List<FavoriteListItem> arg1) {
		// TODO Auto-generated method stub
		
		if(arg1 != null && arg1.size() > 0)
		{
			mAdapter.addData(arg1);
		
			setListAdapter(mAdapter);
			
			mEmptyText.setVisibility(View.GONE);
		}
		else
		{
			mEmptyText.setVisibility(View.VISIBLE);
			mEmptyText.setText(R.string.favorite_empty);
		}
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
			return FavoriteManager.getInstance().getFavoriteList();
		}

		@Override
		protected void onStartLoading() {
			// TODO Auto-generated method stub
			super.onStartLoading();
			
			this.forceLoad();
		}
	}
	
	static public class FavoriteListItem {
		static final public int MODE_NORMAL = 0;
		static final public int MODE_EDIT = 1;
		
		public long id;
		public VideoListItem video;
		public int mode;
		
		public FavoriteListItem()
		{
			mode = MODE_NORMAL;
		}
	}
}
