package com.sharegogo.video.activity;

import java.util.Arrays;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.sharegogo.video.SharegogoVideoApplication;
import com.sharegogo.video.controller.MoreAdapter;
import com.sharegogo.video.game.R;
import com.sharegogo.video.utils.UIUtils;
import com.umeng.fb.UMFeedbackService;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.xp.common.ExchangeConstants;
import com.umeng.xp.controller.ExchangeDataService;
import com.umeng.xp.view.ExchangeViewManager;

public class MoreFragment extends SherlockFragment implements OnItemClickListener{
	private MoreAdapter mAdapter = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
		
		mAdapter = new MoreAdapter();
		
		Resources res = getActivity().getResources();
		String[] more = res.getStringArray(R.array.more);
		
		mAdapter.addData(Arrays.asList(more));
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.more_fragment, null);
		
		ListView list = (ListView)view.findViewById(android.R.id.list);
        
		list.setAdapter(mAdapter);
		
		list.setOnItemClickListener(this);
		
		return view;
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	private void gotoSettingActivity()
	{
		Intent intent = new Intent(Intent.ACTION_MAIN);
			
		intent.setClass(getActivity(), SettingsActivity.class);
			
		startActivity(intent);
	}
	
	private void gotoAboutActivity()
	{
		Intent intent = new Intent(Intent.ACTION_MAIN);
			
		intent.setClass(getActivity(), AboutActivity.class);
			
		startActivity(intent);
	}
	
	private void gotoFeedbackActivity()
	{
		UMFeedbackService.setGoBackButtonVisible();
		UMFeedbackService.openUmengFeedbackSDK(getActivity());
	}
	
	private void gotoRecommendActivity()
	{
		ExchangeDataService service = new ExchangeDataService();
		new ExchangeViewManager(getActivity(),service) .addView(ExchangeConstants.type_list_curtain, null); 
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		switch(arg2)
		{
		//精品推荐
		case 0:
			gotoRecommendActivity();
			break;
		//分享
		case 1:
			UIUtils.gotoShareActivity(getActivity());
			break;
		//设置
		case 2:
			gotoSettingActivity();
			break;
		//支持和反馈
		case 3:
			gotoFeedbackActivity();
			break;
		//关于
		case 4:
			gotoAboutActivity();
			break;
		//退出
		case 5:
			SharegogoVideoApplication.getApplication().onApplicationExit();
			break;
		default:
			break;
		}
	}
}
