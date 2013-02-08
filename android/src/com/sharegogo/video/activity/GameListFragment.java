package com.sharegogo.video.activity;

import java.sql.SQLException;
import java.util.List;

import com.actionbarsherlock.app.SherlockFragment;
import com.j256.ormlite.dao.Dao;
import com.sharegogo.video.SharegogoVideoApplication;
import com.sharegogo.video.controller.GameAdapter;
import com.sharegogo.video.data.Game;
import com.sharegogo.video.data.MySqliteHelper;
import com.sharegogo.video.game.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class GameListFragment extends SherlockFragment implements OnItemClickListener{
	private List<Game> mAllGames = null;
	private GameAdapter mGamesAdapter = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		MySqliteHelper data = SharegogoVideoApplication.getApplication().getHelper();
		
		
		try {
			Dao<Game,String> dao = data.getDao(Game.class);
			mAllGames = dao.queryForAll();
			mGamesAdapter = new GameAdapter(SharegogoVideoApplication.getApplication());
			mGamesAdapter.setData(mAllGames);
			
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
		
		ListView list = (ListView)view.findViewById(android.R.id.list);
		
		list.setAdapter(mGamesAdapter);
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
		GameFragment gameFragment = new GameFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("type", arg2);
		
		gameFragment.setArguments(bundle);
		
		this.getFragmentManager().beginTransaction()
			.addToBackStack(null)
			.add(R.id.dynamic_content,gameFragment)
			.commit();
	}
}
