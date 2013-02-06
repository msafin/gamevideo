package com.sharegogo.video;

import java.sql.SQLException;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.UMFeedbackService;

public class MainActivity extends SherlockFragmentActivity implements OnTabChangeListener {
    //private static final String[] CONTENT = new String[] { "Calendar", "Camera", "Alarms", "Location" };
    private static final String[] CONTENT = new String[] { "Recent1", "Artists", "Albums", "Songs", "Playlists", "Genres" };
    private static final int[] ICONS = new int[] {
            R.drawable.perm_group_calendar,
            R.drawable.perm_group_camera,
            R.drawable.perm_group_device_alarms,
            R.drawable.perm_group_location,
    };
    
    private TabHost mTabHost = null;
    private ViewPager mPager = null;
    private TabPageIndicator mIndicator = null;
    private FragmentPagerAdapter mHistoryAdapter = null;
    private FragmentPagerAdapter mGoogleMusicAdapter = null;
    private FragmentPagerAdapter mFavoriteAdapter = null;
    private FragmentPagerAdapter mMoreAdapter = null;
	private MySqliteHelper mDataHelper = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        mGoogleMusicAdapter = new GoogleMusicAdapter(getSupportFragmentManager());
        mHistoryAdapter = new HistoryAdapter(getSupportFragmentManager());
        mFavoriteAdapter = new FavoriteAdapter(getSupportFragmentManager());
        mMoreAdapter = new MoreAdapter(getSupportFragmentManager());
        
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mGoogleMusicAdapter);

        mIndicator = (TabPageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        
//        final ActionBar ab = getSupportActionBar();
//        
//        ab.setListNavigationCallbacks(ArrayAdapter
//                .createFromResource(this, R.array.sections,
//                        R.layout.sherlock_spinner_dropdown_item),
//                new OnNavigationListener() {
//                    public boolean onNavigationItemSelected(int itemPosition,
//                            long itemId) {
//                        // FIXME add proper implementation
//                        
//                        return false;
//                    }
//                });
//        
//        showDropDownNav();
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        
        setupTabHost();
        
       getHelper();
        
        try
        {
        	Dao<Account,String> dao = mDataHelper.getDao(Account.class);
        	Account arg0 = new Account();
        	
        	arg0.setName("wei");
        	arg0.setPassword("zhengqin");
        	
        	dao.createIfNotExists(arg0);
        	
        	Account arg1 = new Account();
        	arg1.setName("weiEx");
        	arg1.setPassword("zhengqin1");
        	arg1.setTest("test");
        	
        	dao.createIfNotExists(arg1);
        	
        	Account arg2 = new Account();
        	arg2.setName("weiEx1");
        	arg2.setPassword("zhengqin1");
        	arg2.setTest("test");
        	
        	dao.createIfNotExists(arg2);
        	
        }
        catch(SQLException e)
        {
        	e.printStackTrace();
        	
        } 
    }
    
	private void setupTabHost()
	{
		mTabHost.setup();
		
		TabSpec online = mTabHost.newTabSpec("online");
		online.setIndicator("在线视频");
		online.setContent(new DummyTabFactory(this));
		mTabHost.addTab(online);
		
		TabSpec history = mTabHost.newTabSpec("history");
		history.setIndicator("播放历史");
		history.setContent(new DummyTabFactory(this));
		mTabHost.addTab(history);
		
		TabSpec favorite = mTabHost.newTabSpec("favorite");
		favorite.setIndicator("我的收藏");
		favorite.setContent(new DummyTabFactory(this));
		mTabHost.addTab(favorite);
		
		TabSpec more = mTabHost.newTabSpec("more");
		more.setIndicator("更多");
		more.setContent(new DummyTabFactory(this));
		mTabHost.addTab(more);
		
		TabWidget tabWidget = mTabHost.getTabWidget();
		tabWidget.setStripEnabled(true);
		
		mTabHost.setOnTabChangedListener(this);
	}
	
    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			this.finish();
			
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}


	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// TODO Auto-generated method stub
        MenuItem populateItem = menu.add(Menu.NONE, 1, 0, "搜索");
        populateItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        populateItem.setIcon(R.drawable.ic_action_search);
        populateItem.setActionView(R.layout.collapsible_edittext);
        
        MenuItem clearItem = menu.add(Menu.NONE, 2, 0, "排序");
        clearItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        
//        SubMenu subMenu1 = menu.addSubMenu("更多");
//        subMenu1.add(Menu.NONE, 3, 0,"历史");
//        subMenu1.add(Menu.NONE, 4, 0,"反馈");
//        subMenu1.add(Menu.NONE, 5, 0,"设置");
//        subMenu1.add(Menu.NONE, 6, 0,"退出");
//        
//        MenuItem subMenu1Item = subMenu1.getItem();
//        subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return true;
	}

    
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		
		if(item.getItemId() == 4)
			UMFeedbackService.openUmengFeedbackSDK(this); 
		
		return super.onMenuItemSelected(featureId, item);
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (mDataHelper != null) {
			OpenHelperManager.releaseHelper();
			mDataHelper = null;
		}
		
		super.onDestroy();
	}

	private MySqliteHelper getHelper() {
		
		if (mDataHelper == null) {
			mDataHelper = OpenHelperManager.getHelper(this, MySqliteHelper.class);
		}
		
		return mDataHelper;
	}
	
    static class DummyTabFactory implements TabHost.TabContentFactory {
        private final Context mContext;

        public DummyTabFactory(Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
    }

    class HistoryAdapter extends FragmentPagerAdapter
    {
    	FragmentManager mFm = null;
    	
		public HistoryAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
			mFm = fm;
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return TestFragment.newInstance("history");
		}

        @Override
        public CharSequence getPageTitle(int position) {
            return "history";
        }
        
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return POSITION_NONE;
		}
 
		@Override
		public void destroyItem(ViewGroup container, int position,
				Object object) {
			// TODO Auto-generated method stub
			super.destroyItem(container, position, object);
			
			if(object instanceof Fragment)
			{
				mFm.beginTransaction().remove((Fragment)object).commit();
			}
		}
    }
    
    class FavoriteAdapter extends FragmentPagerAdapter
    {
    	FragmentManager mFm = null;
    	
		public FavoriteAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
			mFm = fm;
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return TestFragment.newInstance("favorite");
		}

        @Override
        public CharSequence getPageTitle(int position) {
            return "favorite";
        }
        
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return POSITION_NONE;
		}
 
		@Override
		public void destroyItem(ViewGroup container, int position,
				Object object) {
			// TODO Auto-generated method stub
			super.destroyItem(container, position, object);
			
			if(object instanceof Fragment)
			{
				mFm.beginTransaction().remove((Fragment)object).commit();
			}
		}
    }
    
    class MoreAdapter extends FragmentPagerAdapter
    {
    	FragmentManager mFm = null;
    	
		public MoreAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
			mFm = fm;
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return TestFragment.newInstance("more");
		}

        @Override
        public CharSequence getPageTitle(int position) {
            return "more";
        }
        
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return POSITION_NONE;
		}
 
		@Override
		public void destroyItem(ViewGroup container, int position,
				Object object) {
			// TODO Auto-generated method stub
			super.destroyItem(container, position, object);
			
			if(object instanceof Fragment)
			{
				mFm.beginTransaction().remove((Fragment)object).commit();
			}
		}
    }
    
	   class GoogleMusicAdapter extends FragmentPagerAdapter {
		   FragmentManager mFm = null;
	        public GoogleMusicAdapter(FragmentManager fm) {
	            super(fm);
	            mFm = fm;
	        }

	        @Override
	        public Fragment getItem(int position) {
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

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				// TODO Auto-generated method stub
				super.destroyItem(container, position, object);
				
				if(object instanceof Fragment)
				{
					mFm.beginTransaction().remove((Fragment)object).commit();
				}
			}
	    }


	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		int index = mTabHost.getCurrentTab();
		View view = mTabHost.getTabWidget().getChildAt(index);
		TextView title = (TextView)view.findViewById(android.R.id.title);
		
		setTitle(title.getText());
		
		if(tabId.contentEquals("history"))
		{
			mIndicator.setVisibility(View.GONE);
			mPager.setAdapter(mHistoryAdapter);
			mIndicator.notifyDataSetChanged();
			mHistoryAdapter.notifyDataSetChanged();
		}
		else if(tabId.contentEquals("online"))
		{
			mIndicator.setVisibility(View.VISIBLE);
			mPager.setAdapter(mGoogleMusicAdapter);
			mIndicator.notifyDataSetChanged();
			mGoogleMusicAdapter.notifyDataSetChanged();
		}
		else if(tabId.contentEquals("favorite"))
		{
			mIndicator.setVisibility(View.GONE);
			mPager.setAdapter(mFavoriteAdapter);
			mIndicator.notifyDataSetChanged();
			mFavoriteAdapter.notifyDataSetChanged();
		}
		else if(tabId.contentEquals("more"))
		{
			mIndicator.setVisibility(View.GONE);
			mPager.setAdapter(mMoreAdapter);
			mIndicator.notifyDataSetChanged();
			mMoreAdapter.notifyDataSetChanged();
		}
	}
//	class GoogleMusicAdapter extends FragmentPagerAdapter implements IconPagerAdapter{
//        public GoogleMusicAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return TestFragment.newInstance(CONTENT[position % CONTENT.length]);
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return CONTENT[position % CONTENT.length].toUpperCase();
//        }
//
//        @Override 
//        public int getIconResId(int index) {
//            return ICONS[index];
//          }
//        
//        @Override
//        public int getCount() {
//            return CONTENT.length;
//        }
//    }
}


