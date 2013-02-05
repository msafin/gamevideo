package com.sharegogo.video;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.ArrayAdapter;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.UMFeedbackService;

public class MainActivity extends SherlockFragmentActivity {
    //private static final String[] CONTENT = new String[] { "Calendar", "Camera", "Alarms", "Location" };
    private static final String[] CONTENT = new String[] { "Recent", "Artists", "Albums", "Songs", "Playlists", "Genres" };
    private static final int[] ICONS = new int[] {
            R.drawable.perm_group_calendar,
            R.drawable.perm_group_camera,
            R.drawable.perm_group_device_alarms,
            R.drawable.perm_group_location,
    };
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        FragmentPagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        
        final ActionBar ab = getSupportActionBar();
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
        
        MenuItem clearItem = menu.add(Menu.NONE, 2, 0, "收藏");
        clearItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        
        SubMenu subMenu1 = menu.addSubMenu("更多");
        subMenu1.add(Menu.NONE, 3, 0,"历史");
        subMenu1.add(Menu.NONE, 4, 0,"反馈");
        subMenu1.add(Menu.NONE, 5, 0,"设置");
        subMenu1.add(Menu.NONE, 6, 0,"退出");
        
        MenuItem subMenu1Item = subMenu1.getItem();
        subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return true;
	}

    
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		
		if(item.getItemId() == 4)
			UMFeedbackService.openUmengFeedbackSDK(this); 
		
		return super.onMenuItemSelected(featureId, item);
	}

	   class GoogleMusicAdapter extends FragmentPagerAdapter {
	        public GoogleMusicAdapter(FragmentManager fm) {
	            super(fm);
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


