package com.sharegogo.video.activity;

import com.sharegogo.video.SharegogoVideoApplication;
import com.sharegogo.video.data.MySqliteHelper;
import com.sharegogo.video.game.R;
import java.sql.SQLException;
import java.util.HashMap;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.view.Window;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.UMFeedbackService;

public class MainActivity extends SherlockFragmentActivity{
	private static final String[] CONTENT = new String[] { "分类", "最新", "最热", "推荐"};
    private static final int[] ICONS = new int[] {
            R.drawable.perm_group_calendar,
            R.drawable.perm_group_camera,
            R.drawable.perm_group_device_alarms,
            R.drawable.perm_group_location,
    };
    
    private TabHost mTabHost = null;
    private TabManager mTabManager;
    private ViewPager mPager = null;
    private TabPageIndicator mIndicator = null;
    private FragmentStatePagerAdapter mGoogleMusicAdapter = null;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setSupportProgressBarIndeterminateVisibility(false);
        
        setContentView(R.layout.activity_main);
        
		mGoogleMusicAdapter = new GoogleMusicAdapter(getSupportFragmentManager());
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mGoogleMusicAdapter);

        mIndicator = (TabPageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        
        setupTabHost();
    }
    
	private void setupTabHost()
	{
		mTabHost.setup();
		
		mTabManager = new TabManager(this, mTabHost, R.id.dynamic_content);
		
		TabSpec online = mTabHost.newTabSpec("online");
		online.setIndicator(getIndicatorView("在线视频",R.drawable.ic_video));
		mTabManager.addTab(online, null, null);
		
		TabSpec history = mTabHost.newTabSpec("history");
		history.setIndicator(getIndicatorView("播放历史",R.drawable.ic_history));
		mTabManager.addTab(history, FavoriteFragment.class, null);
		
		TabSpec favorite = mTabHost.newTabSpec("favorite");
		favorite.setIndicator(getIndicatorView("我的收藏",R.drawable.ic_favorite));
		mTabManager.addTab(favorite, FavoriteFragment.class, null);
		
		TabSpec more = mTabHost.newTabSpec("more");
		more.setIndicator(getIndicatorView("更多",R.drawable.ic_more));
		mTabManager.addTab(more, MoreFragment.class, null);
		
		TabWidget tabWidget = mTabHost.getTabWidget();
		tabWidget.setStripEnabled(true);
	}
	
	private View getIndicatorView(String label,int icon)
	{
		LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
		
		View indicator = inflater.inflate(R.layout.tab_indicator, null);
		
		TextView title = (TextView)indicator.findViewById(android.R.id.title);
		ImageView image = (ImageView)indicator.findViewById(android.R.id.icon);
		
		title.setText(label);
		image.setImageResource(icon);
		
		return indicator;
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
			//当前不是online
			if(mTabHost.getCurrentTab() != 0)
			{
				this.finish();
				
				return true;
			}
			
			//如果是online如果是最后一层
			int count = this.getSupportFragmentManager().getBackStackEntryCount();
			
			if(count == 0)
			{
				this.finish();
				
				return true;
			}
			
		}
		
		return super.onKeyDown(keyCode, event);
	}


	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// TODO Auto-generated method stub
		
        MenuItem searchItem = menu.add(Menu.NONE, R.id.menu_id_search, 1, R.string.menu_search);
        searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        searchItem.setIcon(R.drawable.ic_action_search);

        return true;
	}

    
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
		case R.id.menu_id_search:
			gotoSearchActivity();
			break;
			default:
				break;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();
	}

	private void gotoPlayActivity()
	{
		Intent intent = new Intent(Intent.ACTION_MAIN);
		
		intent.setClass(this, PlayActivity.class);
		
		startActivity(intent);
	}
	
	private void gotoSearchActivity()
	{
		Intent intent = new Intent(Intent.ACTION_MAIN);
		
		intent.setClass(this, SearchActivity.class);
		
		startActivity(intent);
	}
	
   class GoogleMusicAdapter extends FragmentStatePagerAdapter {

        public GoogleMusicAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	if(position == 0)
        	{
        		return new GameListFragment();
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

    public static class TabManager implements TabHost.OnTabChangeListener {
        private final FragmentActivity mActivity;
        private final TabHost mTabHost;
        private final int mContainerId;
        private final HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
        TabInfo mLastTab;

        static final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;
            private Fragment fragment;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
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

        public TabManager(FragmentActivity activity, TabHost tabHost, int containerId) {
            mActivity = activity;
            mTabHost = tabHost;
            mContainerId = containerId;
            mTabHost.setOnTabChangedListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
            tabSpec.setContent(new DummyTabFactory(mActivity));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args);

            // Check to see if we already have a fragment for this tab, probably
            // from a previously saved state.  If so, deactivate it, because our
            // initial state is that a tab isn't shown.
            info.fragment = mActivity.getSupportFragmentManager().findFragmentByTag(tag);
            if (info.fragment != null && !info.fragment.isDetached()) {
                FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                ft.detach(info.fragment);
                ft.commit();
            }

            mTabs.put(tag, info);
            mTabHost.addTab(tabSpec);
        }

        @Override
        public void onTabChanged(String tabId) {
            TabInfo newTab = mTabs.get(tabId);
            if (mLastTab != newTab) {
                FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                if (mLastTab != null) {
                    if (mLastTab.fragment != null) {
                        ft.detach(mLastTab.fragment);
                    }
                }
                
                if (newTab != null) {
                    if (newTab.fragment == null && newTab.clss != null) {
                        newTab.fragment = Fragment.instantiate(mActivity,
                                newTab.clss.getName(), newTab.args);
                        ft.add(mContainerId, newTab.fragment, newTab.tag);
                    } else {
                    	if(newTab.fragment != null)
                    		ft.attach(newTab.fragment);
                    }
                }

                mLastTab = newTab;
                ft.commit();
                mActivity.getSupportFragmentManager().executePendingTransactions();
               
                int index = mTabHost.getCurrentTab();
                TabWidget tabWidget = mTabHost.getTabWidget();
                View view = tabWidget.getChildTabViewAt(index);
                TextView title = (TextView)view .findViewById(android.R.id.title);
                
                mActivity.setTitle(title.getText());
            }
        }
    }
}


