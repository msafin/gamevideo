package com.sharegogo.video.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.sharegogo.video.SharegogoVideoApplication;
import com.sharegogo.video.controller.VideoAdapter;
import com.sharegogo.video.data.VideoList;
import com.sharegogo.video.game.R;
import com.viewpagerindicator.TabPageIndicator;

public class GameFragment extends SherlockFragment implements OnPageChangeListener{
	private static final String[] CONTENT = new String[] { "解说", "最新", "最热"};
    private ViewPager mPager = null;
    private TabPageIndicator mIndicator = null;
    private FragmentStatePagerAdapter mGameAdapter = null;
    private long mParentId = -1;
    private List<VideoListFragmentEx> mFragments = new ArrayList<VideoListFragmentEx>();
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if(args != null)
		{
			mParentId = args.getLong("parentId");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.game_fragment, null);
		
		mGameAdapter = new GameAdapter(getFragmentManager());
        mPager = (ViewPager)view.findViewById(R.id.game_pager);
        mPager.setOffscreenPageLimit(2);
        mPager.setAdapter(mGameAdapter);
        
        mIndicator = (TabPageIndicator)view.findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

        mIndicator.setOnPageChangeListener(this);
        
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
		mPager = null;
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
	
	class GameAdapter extends FragmentStatePagerAdapter {
		  FragmentManager mFm = null;
		  
	        public GameAdapter(FragmentManager fm) {
	            super(fm);
	            mFm = fm;
	        }

	        @Override
	        public Fragment getItem(int position) {
	        	SharegogoVideoApplication context =SharegogoVideoApplication.getApplication();
	        	
	        	switch(position)
	        	{
		        	case 0:
		        		return Fragment.instantiate(context, AuthorFragment.class.getName(), getArguments());
		        	case 1:
		        	{
		        		VideoListFragmentEx videoListFragment = new VideoListFragmentEx();
		        		Bundle args = new Bundle();
		        		args.putLong("cid", 26);
		        		args.putInt("listType", VideoList.TYPE_LIST_LATEST);
		        		videoListFragment.setArguments(args);
		        		
		        		mFragments.add(videoListFragment);
		        		
		        		return videoListFragment;
		        	}
		        	case 2:
	        		{
	            		VideoListFragmentEx videoListFragment = new VideoListFragmentEx();
	            		Bundle args = new Bundle();
	            		args.putLong("cid", 26);
	            		args.putInt("listType", VideoList.TYPE_LIST_HOT);
	            		videoListFragment.setArguments(args);
	            		
	            		mFragments.add(videoListFragment);
	            		
	            		return videoListFragment;
	        		}
		        	default:
		        		return Fragment.instantiate(context, AuthorFragment.class.getName(), getArguments());
	        	}
	        
	        }

	        @Override
	        public CharSequence getPageTitle(int position) {
	            return CONTENT[position % CONTENT.length].toUpperCase();
	        }

	        @Override
	        public int getCount() {
	            return CONTENT.length;
	        }
	        
			@Override
			public int getItemPosition(Object object) {
				// TODO Auto-generated method stub
				return POSITION_NONE;
			}
	    }

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		if(arg0 == 0)
		{
			return;
		}
		
		VideoListFragmentEx fragment = mFragments.get(arg0 - 1);
		
		VideoAdapter adapter = fragment.getAdapter();
		if(adapter.getCount() <= 0)
		{
			fragment.startLoadVideo();
		}
	}
}
