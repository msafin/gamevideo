package com.sharegogo.video.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.sharegogo.video.SharegogoVideoApplication;
import com.sharegogo.video.game.R;
import com.viewpagerindicator.TabPageIndicator;

public class GameFragment extends SherlockFragment{
	private static final String[] CONTENT = new String[] { "解说", "最新", "最热"};
    private ViewPager mPager = null;
    private TabPageIndicator mIndicator = null;
    private FragmentStatePagerAdapter mGameAdapter = null;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.game_fragment, null);
		
		mGameAdapter = new GameAdapter(getFragmentManager());
        mPager = (ViewPager)view.findViewById(R.id.game_pager);
        mPager.setAdapter(mGameAdapter);
        mPager.setOffscreenPageLimit(3);
        
        mIndicator = (TabPageIndicator)view.findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

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
	        	if(position == 0)
	        	{
	        		SharegogoVideoApplication context =SharegogoVideoApplication.getApplication();
	        		
	        		return Fragment.instantiate(context, AuthorFragment.class.getName(), getArguments());
	        	}
	        	
	            return TestFragment.newInstance(CONTENT[position % CONTENT.length]);
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
}
