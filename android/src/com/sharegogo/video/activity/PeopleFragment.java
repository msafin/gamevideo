package com.sharegogo.video.activity;

import java.sql.SQLException;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.j256.ormlite.dao.Dao;
import com.sharegogo.video.SharegogoVideoApplication;
import com.sharegogo.video.controller.PeopleAdapter;
import com.sharegogo.video.data.GamePeople;
import com.sharegogo.video.data.MySqliteHelper;
import com.sharegogo.video.game.R;

public class PeopleFragment extends SherlockFragment implements OnItemClickListener{
	private PeopleAdapter mPeopleAdapter = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Bundle bundle = this.getArguments();
		
		if(bundle != null)
		{
			int type = bundle.getInt("type");
			
			MySqliteHelper helper = 
					SharegogoVideoApplication.getApplication().getHelper();
			
			try {
				Dao<GamePeople,String> dao = helper.getDao(GamePeople.class);
				
				List<GamePeople> allPeople = dao.queryForEq("gameType", String.valueOf(type));
				
				if(allPeople != null && allPeople.size() > 0)
				{
					mPeopleAdapter = new PeopleAdapter(SharegogoVideoApplication.getApplication());
					mPeopleAdapter.setData(allPeople);
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.list_layout, null);
		
		PullToRefreshListView pullListView = (PullToRefreshListView)view.findViewById(R.id.pull_refresh_list);
		ListView list = pullListView.getRefreshableView();
		
		if(mPeopleAdapter != null)
		{
			list.setAdapter(mPeopleAdapter);
			list.setOnItemClickListener(this);
		}
		
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		VideoListFragment videoListFragment = new VideoListFragment();
		
		this.getFragmentManager().beginTransaction()
			.addToBackStack(null)
			.add(R.id.dynamic_content,videoListFragment)
			.commit();
	}
}
