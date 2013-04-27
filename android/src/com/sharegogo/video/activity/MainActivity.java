package com.sharegogo.video.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.adsmogo.adview.AdsMogoLayout;
import com.sharegogo.video.SharegogoVideoApplication;
import com.sharegogo.video.controller.NotifyManager;
import com.sharegogo.video.controller.UpdateManager;
import com.sharegogo.video.controller.VideoAdapter;
import com.sharegogo.video.controller.VideoManager;
import com.sharegogo.video.data.NotifyList;
import com.sharegogo.video.data.VideoList;
import com.sharegogo.video.data.CategoryList.CategoryListItem;
import com.sharegogo.video.data.NotifyList.NotifyListItem;
import com.sharegogo.video.data.UpdateInfo;
import com.sharegogo.video.game.R;
import com.sharegogo.video.utils.NetworkUtils;
import com.sharegogo.video.utils.ResUtils;
import com.sharegogo.video.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.TabPageIndicator;

public class MainActivity extends BaseActivity implements OnClickListener, android.view.View.OnClickListener, OnPageChangeListener{
	private static final String[] CONTENT = new String[] { "分类", "最新", "最热"};
    private static final int[] ICONS = new int[] {
            R.drawable.perm_group_calendar,
            R.drawable.perm_group_camera,
            R.drawable.perm_group_device_alarms,
            R.drawable.perm_group_location,
    };
    private static final int MSG_CHANGE_NOTIFY = 1;
    
    private TabHost mTabHost = null;
    private TabManager mTabManager;
    private ViewPager mPager = null;
    private TabPageIndicator mIndicator = null;
    private FragmentStatePagerAdapter mOnlineVideoAdapter = null;
    private UpdateInfo mUpdateInfo = null;
    private GameDialogFragment mUpdateNote = null;
    private View mNotify = null;
    private ImageButton mClose = null;
    private TextView mNotifyTitle = null;
    private TextView mNotifyContent = null;
    private	boolean bCloseByUser = false;
    private NotifyList mNotifyList = null;
    private int mNofityIndex = 0;
    private List<VideoListFragmentEx> mFragments = new ArrayList<VideoListFragmentEx>();
    private Handler mHandler = new Handler()
    {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what)
			{
				case MSG_CHANGE_NOTIFY:
					changeNotify();
					if(!bCloseByUser)
					{
						mHandler.sendEmptyMessageDelayed(MSG_CHANGE_NOTIFY, 3000);
					}
					break;
				default:
					break;
			}
			super.handleMessage(msg);
		}
    	
    };
    
    private void changeNotify()
    {
    	mNofityIndex++;
    	
    	int index = mNofityIndex % mNotifyList.count;
    	
		NotifyListItem firstItem = mNotifyList.list[index];
		
		mNotifyTitle.setText(firstItem.notifyTitleString);
		mNotifyContent.setText(firstItem.notifyContentString);
    }
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        mOnlineVideoAdapter = new OnlineVideoAdapter(getSupportFragmentManager());
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setOffscreenPageLimit(2);
        mPager.setAdapter(mOnlineVideoAdapter);
        mIndicator = (TabPageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        mNotify = findViewById(R.id.notify);
        mNotify.setVisibility(View.GONE);
        mClose = (ImageButton)findViewById(R.id.close);
        mClose.setOnClickListener(this);
        mNotifyTitle = (TextView)findViewById(R.id.title);
        mNotifyContent = (TextView)findViewById(R.id.content);
        
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        
        setupTabHost();
        
        mIndicator.setOnPageChangeListener(this);
    }
    
	private void setupTabHost()
	{
		mTabHost.setup();
		
		mTabManager = new TabManager(this, mTabHost, R.id.dynamic_content);
		
		TabSpec online = mTabHost.newTabSpec("online");
		online.setIndicator(getIndicatorView(getString(R.string.online_video),R.drawable.ic_video));
		mTabManager.addTab(online, null, null,0);
		
		TabSpec history = mTabHost.newTabSpec("history");
		history.setIndicator(getIndicatorView(getString(R.string.play_history),R.drawable.ic_history));
		mTabManager.addTab(history, HistoryFragment.class, null,1);
		
		TabSpec favorite = mTabHost.newTabSpec("favorite");
		favorite.setIndicator(getIndicatorView(getString(R.string.my_favorite),R.drawable.ic_favorite));
		mTabManager.addTab(favorite, FavoriteFragment.class, null,2);
		
		TabSpec more = mTabHost.newTabSpec("more");
		more.setIndicator(getIndicatorView(getString(R.string.more_info),R.drawable.ic_more));
		mTabManager.addTab(more, MoreFragment.class, null,3);
		
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
		mUpdateInfo = UpdateManager.getInstance().getUpdateInfo();
		boolean hasShowUpdate = SharegogoVideoApplication.getApplication().bShowUpdate;
		
		if(mUpdateInfo != null && !hasShowUpdate)
		{
			mUpdateNote = new GameDialogFragment(
					R.drawable.ic_about,
					R.string.update_note,
					mUpdateInfo.desc,
					R.string.update_now,
					R.string.update_later,
					this);
			
			mUpdateNote.show(this.getSupportFragmentManager(), null);
			
			SharegogoVideoApplication.getApplication().bShowUpdate = true;
		}
		
		mNotifyList = NotifyManager.getInstance().getNotifyList();
		
		if(mNotifyList != null && 
			mNotifyList.list != null &&
			mNotifyList.list.length > 0 &&
			!bCloseByUser)
		{
			NotifyListItem firstItem = mNotifyList.list[0];
			
			mNotifyTitle.setText(firstItem.notifyTitleString);
			mNotifyContent.setText(firstItem.notifyContentString);
			
			mNotify.setVisibility(View.VISIBLE);
			
			if(mNotifyList.count > 1)
			{
				mHandler.sendEmptyMessageDelayed(MSG_CHANGE_NOTIFY, 3000);
			}
		}
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
			onPopBackStack();
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

	private void onPopBackStack()
	{
		int count = this.getSupportFragmentManager().getBackStackEntryCount();
		BackStackEntry entry = null;
		if(count > 0)
		{
			entry = this.getSupportFragmentManager().getBackStackEntryAt(count - 1);
		}
		
		if(count <= 1)
		{
			getSupportActionBar().setHomeButtonEnabled(false);
			getSupportActionBar().setIcon(R.drawable.ic_launcher);
		}
		
		if(entry != null && entry.getName() != null)
			setTitle(entry.getName());
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
		case R.id.menu_id_search:
			gotoSearchActivity();
			break;
		case android.R.id.home:
			onPopBackStack();
			this.getSupportFragmentManager().popBackStack();
			break;
			default:
				break;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		AdsMogoLayout.clear();
		
		super.onDestroy();
	}
	
	private void gotoSearchActivity()
	{
		Intent intent = new Intent(Intent.ACTION_MAIN);
		
		intent.setClass(this, SearchActivity.class);
		
		startActivity(intent);
	}
	
   class OnlineVideoAdapter extends FragmentStatePagerAdapter {

        public OnlineVideoAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	switch(position)
        	{
        	case 0:
        		return new GameCategoryFragment();
        	case 1:
        	{
        		VideoListFragmentEx videoListFragment = new VideoListFragmentEx();
        		Bundle args = new Bundle();
        		args.putLong("cid", 0);
        		args.putInt("listType", VideoList.TYPE_LIST_LATEST);
        		videoListFragment.setArguments(args);
        		
        		mFragments.add(videoListFragment);
        		
        		return videoListFragment;
        	}
        	case 2:
        	{
        		VideoListFragmentEx videoListFragment = new VideoListFragmentEx();
        		Bundle args = new Bundle();
        		args.putLong("cid", 0);
        		args.putInt("listType", VideoList.TYPE_LIST_HOT);
        		videoListFragment.setArguments(args);
        		
        		mFragments.add(videoListFragment);
        		
        		return videoListFragment;
        	}
        	default:
        		return new GameCategoryFragment();
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
            private int index;
            private String title;
            
            TabInfo(String _tag, Class<?> _class, Bundle _args,int _index) {
                tag = _tag;
                clss = _class;
                args = _args;
                index = _index;
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

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args,int index) {
            tabSpec.setContent(new DummyTabFactory(mActivity));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args,index);

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
                
                //如果是在线视频，记录当前title
                if(mLastTab != null && mLastTab.index == 0)
                {
                	TabInfo onlineTab = mTabs.get("online");
                	onlineTab.title = mActivity.getTitle().toString();
                }
                
                mLastTab = newTab;
                ft.commit();
                mActivity.getSupportFragmentManager().executePendingTransactions();
               
                int index = mTabHost.getCurrentTab();
                TabWidget tabWidget = mTabHost.getTabWidget();
                View view = tabWidget.getChildTabViewAt(index);
                TextView title = (TextView)view .findViewById(android.R.id.title);
                mActivity.setTitle(title.getText());
                
                SherlockFragmentActivity sherlocFragmentkActivity = (SherlockFragmentActivity)mActivity;
                if(index != 0)
                {
                	sherlocFragmentkActivity.getSupportActionBar().setHomeButtonEnabled(false);
                	sherlocFragmentkActivity.getSupportActionBar().setIcon(R.drawable.ic_launcher);
                }
                else
                {
        			int count = sherlocFragmentkActivity.getSupportFragmentManager().getBackStackEntryCount();
        			
        			if(count > 0)
        			{
        				sherlocFragmentkActivity.getSupportActionBar().setHomeButtonEnabled(true);
                    	sherlocFragmentkActivity.getSupportActionBar().setIcon(R.drawable.ic_back);
        			}
        			else
        			{
        				sherlocFragmentkActivity.getSupportActionBar().setHomeButtonEnabled(false);
                    	sherlocFragmentkActivity.getSupportActionBar().setIcon(R.drawable.ic_launcher);
        			}
        			
        			TabInfo onlineTab = mTabs.get("online");
        			if(onlineTab.title != null)
        				mActivity.setTitle(onlineTab.title);
                }
            }
        }
    }

	private void updateNow()
	{
		mUpdateNote.dismiss();
		
		UIUtils.gotoBrowserActivity(this, mUpdateInfo.url);
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		switch(which)
		{
		case DialogInterface.BUTTON_POSITIVE:
			updateNow();
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
			case R.id.close:
				bCloseByUser = true;
				mNotify.setVisibility(View.GONE);
				mHandler.removeMessages(MSG_CHANGE_NOTIFY);
				break;
			default:
				break;
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
		
		int index = arg0 - 1;
		if(index >= mFragments.size())
		{
			return;
		}
		
		VideoListFragmentEx fragment = mFragments.get(index);
		
		VideoAdapter adapter = fragment.getAdapter();
		if(adapter.getCount() <= 0)
		{
			fragment.startLoadVideo();
		}
	}
}


