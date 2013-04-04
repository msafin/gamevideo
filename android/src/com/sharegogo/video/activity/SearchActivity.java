package com.sharegogo.video.activity;

import java.util.Arrays;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.sharegogo.video.controller.HistoryManager;
import com.sharegogo.video.controller.SearchManager;
import com.sharegogo.video.controller.VideoAdapter;
import com.sharegogo.video.controller.VideoManager;
import com.sharegogo.video.data.History;
import com.sharegogo.video.data.SearchResult;
import com.sharegogo.video.data.VideoDetail;
import com.sharegogo.video.data.VideoList;
import com.sharegogo.video.game.R;
import com.sharegogo.video.http.ResponseHandler;
import com.sharegogo.video.utils.UIUtils;

public class SearchActivity extends NoTitleActivity implements OnClickListener, ResponseHandler, OnItemClickListener, TextWatcher{
	private static final int PAGE_SIZE = 15;
	private ProgressDialog mProgressDialog = null;
	private EditText mEditText = null;
	private String mKeyword = null;
	private VideoAdapter mVideoAdapter = null;
	private ListView mListView = null;
	private View mLoadMore = null;
	private View mLoading = null;
	private int mResultCount = 0;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		setContentView(R.layout.search_activity);
		
		View footerView = LayoutInflater.from(this).inflate(R.layout.load_more, null);
		
		PullToRefreshListView pullListView = (PullToRefreshListView)findViewById(R.id.pull_refresh_list);
		pullListView.setMode(Mode.DISABLED);
		
		mLoadMore = footerView.findViewById(R.id.load_more);
		mLoading = footerView.findViewById(R.id.loading_more);
		
		mLoading.setOnClickListener(this);
		mLoadMore.setOnClickListener(this);
		
		mListView = pullListView.getRefreshableView();
		mListView.setOnItemClickListener(this);
		mListView.addFooterView(footerView);
		mListView.setFooterDividersEnabled(true);

		mVideoAdapter = new VideoAdapter();
		mListView.setAdapter(mVideoAdapter);
		
		ImageButton search = (ImageButton)findViewById(R.id.search_btn);
		search.setOnClickListener(this);
		mEditText = (EditText)findViewById(R.id.search_text);
		mEditText.addTextChangedListener(this);
		
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage(getString(R.string.searching));
		
		showLoadMore(false);
	}

	private void showLoading()
	{
		mLoading.setVisibility(View.VISIBLE);
		mLoadMore.setVisibility(View.GONE);
	}
	
	private void showLoadMore(boolean show)
	{
		if(show)
		{
			mLoadMore.setVisibility(View.VISIBLE);
			mLoading.setVisibility(View.GONE);
		}
		else
		{
			mLoadMore.setVisibility(View.GONE);
			mLoading.setVisibility(View.GONE);
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected String getCustomTitle() {
		// TODO Auto-generated method stub
		return "ËÑË÷";
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.search_btn:
			mProgressDialog.show();
			mKeyword = mEditText.getText().toString();
			
			SearchManager.getInstance().doSearch(mKeyword, 1, PAGE_SIZE, VideoList.TYPE_LIST_LATEST, 1, this);
			break;
		case R.id.load_more:
			showLoading();
			int pageNum = mVideoAdapter.getCount() / PAGE_SIZE + 1;
			SearchManager.getInstance().doSearch(mKeyword, pageNum, PAGE_SIZE, VideoList.TYPE_LIST_LATEST, 1, this);
			break;
		case R.id.loading_more:
			break;
		}

	}

	@Override
	public void onSuccess(Object data) {
		// TODO Auto-generated method stub
		mProgressDialog.dismiss();
		
		SearchResult result = (SearchResult)data;
		if(result != null && result.list != null && result.list.length > 0)
		{
			mResultCount = result.count;
			
			mVideoAdapter.addData(Arrays.asList(result.list));
		}
		
		if(mVideoAdapter.getCount() < mResultCount)
		{
			showLoadMore(true);
		}
		else
		{
			showLoadMore(false);
		}
	}

	@Override
	public void onFailed(int what, Object msg) {
		// TODO Auto-generated method stub
		mProgressDialog.dismiss();
		Toast.makeText(this, getString(R.string.no_search_result), 1000).show();
		
		if(mVideoAdapter.getCount() < mResultCount)
		{
			showLoadMore(true);
		}
		else
		{
			showLoadMore(false);
		}
	}

	@Override
	public boolean onPersistent(Object data) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		VideoDetail video = (VideoDetail)mVideoAdapter.getItem(arg2-1);
		
		UIUtils.gotoPlayActivity(video,this);
		
		addHistory(video);
	}
	
	private void addHistory(VideoDetail video)
	{
		History history = new History();
		
		history.video_id = video.id;
		history.update = System.currentTimeMillis();
		
		HistoryManager.getInstance().addHistory(history);
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		mVideoAdapter.clearData();
		showLoadMore(false);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}
}
