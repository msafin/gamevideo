package com.sharegogo.video.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.sharegogo.video.controller.VideoAdapter;
import com.sharegogo.video.data.VideoList;
import com.sharegogo.video.game.R;
import com.viewpagerindicator.TabPageIndicator;

public class GameFragmentEx extends SherlockFragment implements OnPageChangeListener{
	private static final String[] CONTENT = new String[] { "最新", "最热"};
    private ViewPager mPager = null;
    private TabPageIndicator mIndicator = null;
    private FragmentStatePagerAdapter mGameAdapter = null;
    private long mParentId = -1;
    private String mCategoryName = null;
    private List<VideoListFragmentEx> mFragments = new ArrayList<VideoListFragmentEx>();
    private boolean bFirst = true;
    private Handler mHandler = new Handler();
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if(args != null)
		{
			mParentId = args.getLong("parentId");
			mCategoryName = args.getString("categoryName");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.game_fragment_ex, null);
		
		mGameAdapter = new GameAdapter(getFragmentManager());
        mPager = (ViewPager)view.findViewById(R.id.game_pager_ex);
        mPager.setOffscreenPageLimit(1);
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
		if(bFirst)
		{
			mHandler.post(new Runnable()
			{

				@Override
				public void run() {
					// TODO Auto-generated method stub
					onPageSelected(0);
				}
				
			});
			
			bFirst = false;
		}
	}
	
	class GameAdapter extends FragmentStatePagerAdapter {
		  FragmentManager mFm = null;
		  
	        public GameAdapter(FragmentManager fm) {
	            super(fm);
	            mFm = fm;
	        }

	        @Override
	        public Fragment getItem(int position) {
	        	VideoListFragmentEx videoListFragment = new VideoListFragmentEx();
        		Bundle args = new Bundle();
        		args.putLong("cid", mParentId);
        		args.putString("categoryName", mCategoryName);
        		
	        	switch(position)
	        	{
		        	case 0:
		        	{
		        		args.putInt("listType", VideoList.TYPE_LIST_LATEST);
		        		break;
		        	}
		        	case 1:
		        	default:
	        		{
	            		args.putInt("listType", VideoList.TYPE_LIST_HOT);
	        		}
	        	}
	        	
	        	videoListFragment.setArguments(args);
        		
        		mFragments.add(videoListFragment);
        		
        		return videoListFragment;
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
		if(arg0 >= mFragments.size())
		{
			return;
		}
		
		VideoListFragmentEx fragment = mFragments.get(arg0);
		
		VideoAdapter adapter = fragment.getAdapter();
		if(adapter.getCount() <= 0)
		{
			fragment.startLoadVideo();
		}
	}
}
