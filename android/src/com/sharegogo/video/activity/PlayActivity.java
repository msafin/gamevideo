package com.sharegogo.video.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.sharegogo.config.HttpConstants;
import com.sharegogo.video.SharegogoVideoApplication;
import com.sharegogo.video.controller.FavoriteManager;
import com.sharegogo.video.controller.VideoManager;
import com.sharegogo.video.data.Favorite;
import com.sharegogo.video.data.MySqliteHelper;
import com.sharegogo.video.data.VideoDetail;
import com.sharegogo.video.game.R;
import com.sharegogo.video.http.ResponseHandler;
import com.sharegogo.video.settings.SharegogoVideoSettings;
import com.sharegogo.video.utils.DeviceInfo;
import com.sharegogo.video.utils.NetworkUtils;
import com.sharegogo.video.utils.ResUtils;
import com.sharegogo.video.utils.UIUtils;
import com.sharegogo.video.view.VideoViewEx;
import com.sharegogo.video.view.VideoViewEx.PlayPauseListener;

public class PlayActivity extends FragmentActivity implements OnClickListener, ResponseHandler, OnTouchListener, OnPreparedListener, OnErrorListener, PlayPauseListener{
	static final public String KEY_VIDEO_AUTHOR = "video_author";
	static final public String KEY_VIDEO_NAME = "video_name";
	static final public String KEY_VIDEO_SOURCE = "video_source";
	static final public String KEY_VIDEO_ID = "video_id";
	static final public String KEY_FLASH_URL = "flash_url";
	
	private WebView mWebView = null;
	private String mUrl = null;
	private String mPlayUrl = null;
	private long mVideoId = -1;
	private ImageButton mBtnFavorite;
	private ImageButton mBtnRecommend;
	private ImageButton mBtnShare;
	private Button mGotoBrowser;
	private Favorite mFavorite;
	private View mDownloadNote;
	private ProgressDialog mProgressDialog = null;
	private VideoDetail mVideoDetail = null;
	private boolean bFlashInstalled = false;
	private String mVId = null;
	private VideoViewEx mVideoView = null;
	private View mAdsView = null;
	private MediaController mMediaController = null;
	private GestureDetector mGestureDetector = null;
	private boolean mRestored = false;
	private int mPosition = 0;
	private ImageButton mBtnPlay = null;
	private ImageButton mBtnFullScreen = null;
	private boolean mIsFullScreen = false;
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what)
			{
				case 10000:
				{
					mVideoView.setVideoURI(Uri.parse("http://3g.youku.com/pvs?id="+mVId+"&format=3gphd"));
				}
				break;
				case 99:
					break;
				default:
					break;
			}
		}
			
	};

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(arg0);
		setContentView(R.layout.play_activity);
		
		mRestored = arg0 != null;
		
		mAdsView = findViewById(R.id.adsMogoView);
		mBtnFavorite = (ImageButton)findViewById(R.id.btn_favorite);
		mBtnRecommend = (ImageButton)findViewById(R.id.btn_recommend);
		mBtnShare = (ImageButton)findViewById(R.id.btn_share);
		mDownloadNote = findViewById(R.id.flash_downlaod_note);
		mGotoBrowser = (Button)findViewById(R.id.btn_goto_browser);
		mBtnPlay = (ImageButton)findViewById(R.id.play);
		mBtnFullScreen = (ImageButton)findViewById(R.id.full_screen);
		mBtnPlay.setOnClickListener(this);
		mBtnPlay.setVisibility(View.GONE);
		mBtnFullScreen.setOnClickListener(this);
		ImageButton download = (ImageButton)findViewById(R.id.download);
		download.setOnClickListener(this);
		
		mBtnFavorite.setOnClickListener(this);
		mBtnRecommend.setOnClickListener(this);
		mBtnShare.setOnClickListener(this);
		mGotoBrowser.setOnClickListener(this);
		
		mWebView = (WebView)findViewById(R.id.webView1);
		mVideoView = (VideoViewEx)findViewById(R.id.video_view);
		mVideoView.setPlayPauseListener(this);
		
		mMediaController = new MediaController(this);
		mMediaController.setMediaPlayer(mVideoView);
		mMediaController.setAnchorView(mVideoView);
		mVideoView.setMediaController(mMediaController);
		mVideoView.setOnTouchListener(this);
		mVideoView.setOnPreparedListener(this);
		mVideoView.setOnErrorListener(this);
		mMediaController.hide();
		
		Intent intent = this.getIntent();
		mVideoId = intent.getLongExtra(KEY_VIDEO_ID, -1);
		mUrl = intent.getStringExtra(KEY_FLASH_URL);
		
		mFavorite = FavoriteManager.getInstance().getFavorite(mVideoId);
		if(mFavorite != null)
		{
			mBtnFavorite.setImageResource(R.drawable.ic_favorited);
		}
		
		mGestureDetector = new GestureDetector(this,new MyGestureListener());
		
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage(getString(R.string.loading_video_detail));
		mProgressDialog.show();
		
		if(!isUseFlash())
		{
			mWebView.setVisibility(View.INVISIBLE);
			
			bFlashInstalled = false;
        	VideoManager.getInstance().getVideoDetail(mVideoId, PlayActivity.this);
		}
		else
		{
			mVideoView.setVisibility(View.INVISIBLE);
			mMediaController.setVisibility(View.INVISIBLE);
			
	        if(isFlashInstalled())
	        {
	        	bFlashInstalled = true;
	        	
	        	mWebView.loadUrl("file:///android_asset/index.html");
	            mWebView.getSettings().setPluginsEnabled(true);
	            mWebView.getSettings().setJavaScriptEnabled(true);
	            mWebView.getSettings().setPluginState(PluginState.ON);
	            mWebView.addJavascriptInterface(new VideoInterface(), "VideoInterface");
	            mWebView.setWebChromeClient(new MyWebChromeClient());
	            mWebView.setWebViewClient(new MyWebViewClient());
	        }
	        else
	        {
	        	bFlashInstalled = false;
	        	VideoManager.getInstance().getVideoDetail(mVideoId, PlayActivity.this);
	        }
		}
	}
	
	private boolean isUseFlash()
	{
		if(SharegogoVideoSettings.getUseFlash() == 1)
		{
			return true;
		}
		
		return false;
	}
	
	private void showFullScreen()
	{
		mBtnFavorite.setVisibility(View.GONE);
		mBtnShare.setVisibility(View.GONE);
		mGotoBrowser.setVisibility(View.GONE);
		mAdsView.setVisibility(View.GONE);
		mBtnFullScreen.setVisibility(View.GONE);
		
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		int flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		getWindow().setFlags(flags, flags);
		
		mIsFullScreen = true;
	}
	
	private void exitFullScreen()
	{
		mBtnFavorite.setVisibility(View.VISIBLE);
		mBtnShare.setVisibility(View.VISIBLE);
		mGotoBrowser.setVisibility(View.VISIBLE);
		mAdsView.setVisibility(View.VISIBLE);
		mBtnFullScreen.setVisibility(View.VISIBLE);
		
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		int flag=WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN;
		getWindow().setFlags(flag, flag);
		
		mIsFullScreen = false;
	}
	
	public void get(){  
        BufferedReader in = null;  
        try{
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(mPlayUrl);  
            request.addHeader("user-agent", 
            		"Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543 Safari/419.3");
            HttpResponse response = client.execute(request);   
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));       
            String line = "";  
 
            while((line = in.readLine()) != null){  
                int pos = line.indexOf("vc-vid=");
                if(pos >0){
                String temp = line.substring(pos, pos+100);
                String vidt = temp.split("&sct=")[0];
                
                mVId = vidt.split("=")[1];
                break;
                
                }                
            }  
            
            in.close();  
            Message msg = Message.obtain(mHandler);
            if(mVId != null){
            	msg.what = 10000;
            }else
            {
            	msg.what = 99;
            }
             
            msg.sendToTarget();
        }catch(Exception e){  
            e.printStackTrace();
        }finally{  
            if(in != null){  
                try{  
                    in.close();  
                }catch(IOException ioe){  
                    Log.e("", ioe.toString());  
                }  
            }  
        }  
    }
   
    private class MyWebChromeClient extends WebChromeClient{
    	   
			@Override
			public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
				// TODO Auto-generated method stub
				return super.onConsoleMessage(consoleMessage);
			}

			@Override
			public void onConsoleMessage(String message, int lineNumber,
					String sourceID) {
				// TODO Auto-generated method stub
				super.onConsoleMessage(message, lineNumber, sourceID);
				Log.e("test", message);
			}
        }
	        
    private class MyWebViewClient extends WebViewClient{
	@Override
	public void onPageFinished(WebView view, String url) {
		// TODO Auto-generated method stub
			super.onPageFinished(view, url);
		
			Log.e("test", "onPageFinished");
			//从搜索结果页跳转过
			if(mUrl != null)
			{
				mWebView.loadUrl("javascript:callJS()");  //java调用js的函数	
				
				if(!NetworkUtils.isNetworkAvailable())
				{
					Toast.makeText(PlayActivity.this, ResUtils.getString(R.string.cannot_play_for_network), 2000).show();
				}
				
				mProgressDialog.dismiss();
				
				return;
			}
			
			VideoDetail detail = VideoManager.getInstance().getVideoDetail(mVideoId);
			if(detail != null && detail.flashUrl != null && detail.flashUrl.length() > 0)
			{
				onSuccess(detail);
			}
			else
			{
				VideoManager.getInstance().getVideoDetail(mVideoId, PlayActivity.this);
			}
			
			if(!NetworkUtils.isNetworkAvailable())
			{
				Toast.makeText(PlayActivity.this, ResUtils.getString(R.string.cannot_play_for_network), 2000).show();
			}
		}
    }
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(!isUseFlash())
		{
			mPosition = mVideoView.getCurrentPosition();
			if(mVideoView.isPlaying())
			{
				mVideoView.suspend();
			}
		}
		else
		{
			if(isFlashInstalled())
			{
				try {
		            Class.forName("android.webkit.WebView")
		                    .getMethod("onPause", (Class[]) null)
		                    .invoke(mWebView, (Object[]) null);
		        } catch (Exception e) {
		        	e.printStackTrace();
		        }
				mWebView.pauseTimers();
			}
		}
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(!isUseFlash())
		{
			if(!mProgressDialog.isShowing())
			{
				mProgressDialog.setMessage(getString(R.string.loading_video));
				mProgressDialog.show();
			}
		}
		else
		{
			//进入的时候flash没有安装
			if(!bFlashInstalled && isFlashInstalled())
			{
				mWebView.loadUrl("file:///android_asset/index.html");
		        mWebView.getSettings().setPluginsEnabled(true);
		        mWebView.getSettings().setJavaScriptEnabled(true);
		        mWebView.getSettings().setPluginState(PluginState.ON);
		        mWebView.addJavascriptInterface(new VideoInterface(), "VideoInterface");
		        mWebView.setWebChromeClient(new MyWebChromeClient());
		        mWebView.setWebViewClient(new MyWebViewClient());
			}
			
			if(isFlashInstalled())
			{
		        try{
			        Class.forName("android.webkit.WebView")
			        .getMethod("onResume", (Class[]) null)
			        .invoke(mWebView, (Object[]) null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				mWebView.resumeTimers();
				
				bFlashInstalled = true;
			}
			else
			{
				bFlashInstalled = false;
			}
			
			if(isFlashInstalled())
			{
				mDownloadNote.setVisibility(View.INVISIBLE);
			}
			else
			{
				mDownloadNote.setVisibility(View.VISIBLE);
			}
		}
	}


	private String buildHtml()
	{
		Log.e("test", "buildHtml");
		StringBuilder builder = new StringBuilder();
		builder.append("<embed src=\"");
		builder.append(mUrl);
		builder.append("\" allowFullScreen=\"true\" quality=\"high\" width=\"100%\" height=\"100%\" align=\"middle\" allowScriptAccess=\"always\" type=\"application/x-shockwave-flash\"></embed>");
		
		return builder.toString();
	}
	
	private Favorite buildFavorite()
	{
		Favorite item = new Favorite();
		
		item.video_id = mVideoId;
		item.update = System.currentTimeMillis();
		
		return item;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mHandler.removeMessages(10000);
		mHandler.removeMessages(99);
	}

	private class VideoInterface {
	
	    public String getFlash() 
	    { 
	    	return buildHtml(); 
	    }
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.btn_favorite:
			mFavorite = FavoriteManager.getInstance().getFavorite(mVideoId);
			if(mFavorite != null)
			{
				delFavorite();
			}
			else
			{
				addFavorite();
			}
			break;
		case R.id.btn_recommend:
			break;
		case R.id.btn_share:
			UIUtils.gotoShareActivity(this);
			break;
		case R.id.download:
			downloadFlashPlugin();
			break;
		case R.id.btn_goto_browser:
			if(mVideoDetail != null && mVideoDetail.playurl != null)
			{
				UIUtils.gotoBrowserActivity(this, mVideoDetail.playurl);
			}
			break;
		case R.id.play:
			if(!mVideoView.isPlaying())
			{
				mVideoView.start();
			}
			break;
		case R.id.full_screen:
			onFullScreen();
			break;
			default:
			break;
		}
	}
	
	private void addFavorite()
	{
		if(FavoriteManager.getInstance().addFavorite(buildFavorite()))
		{
			Toast.makeText(getApplication(), R.string.add_favorite_success, 2000).show();
			mBtnFavorite.setImageResource(R.drawable.ic_favorited);
		}
		else
		{
			Toast.makeText(getApplication(), R.string.add_favorite_fail, 2000).show();
		}
	}
	
	private void delFavorite()
	{
		if(FavoriteManager.getInstance().delFavorite(mFavorite))
		{
			Toast.makeText(getApplication(), R.string.del_favorite_success, 2000).show();
			mBtnFavorite.setImageResource(R.drawable.ic_favorite);
		}
		else
		{
			Toast.makeText(getApplication(), R.string.del_favorite_fail, 2000).show();
		}
	}
	
	private boolean isFlashInstalled()
	{
		PackageManager pm = getPackageManager();  
		
        List<PackageInfo> infoList = pm  .getInstalledPackages(PackageManager.GET_SERVICES);  
        for (PackageInfo info : infoList) 
        {  
            if ("com.adobe.flashplayer".equals(info.packageName)) 
            {  
                return true;  
            }  
        }  
        
        return false;  
	}
	
	private void downloadFlashPlugin()
	{
		Uri uri = Uri.parse(HttpConstants.FLASH_DOWNLOAD_URL);
		
		Intent intent = new Intent(Intent.ACTION_VIEW,uri);
		
		startActivity(intent);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = this.getMenuInflater();
		
		inflater.inflate(R.menu.play_activity, menu);
		
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
		case R.id.menu_search:
			gotoSearchActivity();
			break;
		case R.id.menu_quit:
			SharegogoVideoApplication.getApplication().onApplicationExit();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void gotoSearchActivity()
	{
		Intent intent = new Intent(Intent.ACTION_MAIN);
		
		intent.setClass(this, SearchActivity.class);
		
		startActivity(intent);
	}


	@Override
	public void onSuccess(Object data) {
		// TODO Auto-generated method stub
		VideoDetail videoDetail = (VideoDetail)data;
		
		mVideoDetail = videoDetail;
		mUrl = videoDetail.flashUrl;
		mPlayUrl = videoDetail.playurl;
		
		if(!isUseFlash())
		{
			mProgressDialog.setMessage(getString(R.string.loading_video));
			
			new Thread(){

				@Override
				public void run() {
					get();
				}
	    		
	    	}.start();
		}
		else
		{
			mProgressDialog.dismiss();
			
			if(mUrl != null)
			{
				if(isFlashInstalled())
					mWebView.loadUrl("javascript:callJS()");  //java调用js的函数
			}
			else if(videoDetail.playurl != null)
			{
				UIUtils.gotoBrowserActivity(this,videoDetail.playurl);
			}
			else
			{
				Toast.makeText(getApplicationContext(), getString(R.string.load_video_detail_failed), 1000).show();
			}
		}
	}

	@Override
	public void onFailed(int what, Object msg) {
		// TODO Auto-generated method stub
		if(mProgressDialog.isShowing())
			mProgressDialog.dismiss();
		
		if(msg != null && msg instanceof String)
		{
			Toast.makeText(getApplicationContext(), (String)msg, 1000).show();
		}
	}


	@Override
	public boolean onPersistent(Object data) {
		// TODO Auto-generated method stub
		MySqliteHelper dbHelper = SharegogoVideoApplication.getApplication().getHelper();
		VideoDetail videoDetail = (VideoDetail)data;
		
		try {
			
			Dao<VideoDetail,String> dao = dbHelper.getDao(VideoDetail.class);
			dao.createOrUpdate(videoDetail);
			
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return false;
		}
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		mGestureDetector.onTouchEvent(arg1);
		
		return true;
	}

	private void onFullScreen()
	{
		if(mBtnFavorite.getVisibility() == View.VISIBLE)
		{
			showFullScreen();
		}
		else
		{
			exitFullScreen();
		}
	}
	
	private class MyGestureListener extends GestureDetector.SimpleOnGestureListener
	{
		@Override
		public boolean onDoubleTap(MotionEvent e) {
			// TODO Auto-generated method stub
			onFullScreen();
			
			return true;
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			if(mMediaController.isShown())
			{
				mMediaController.hide();
			}
			else
			{
				mMediaController.show();
			}
			
			if(mIsFullScreen)
			{
				if(mBtnFullScreen.getVisibility() == View.VISIBLE)
				{
					mBtnFullScreen.setVisibility(View.GONE);
				}
				else
				{
					mBtnFullScreen.setVisibility(View.VISIBLE);
				}
			}
			
			return false;
		}
	}

	@Override
	public void onPrepared(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		if(!mRestored)
		{
			mVideoView.seekTo(100);
		}
		
		if(mPosition > 0)
		{
			mVideoView.seekTo(mPosition);
		}
		
		mProgressDialog.dismiss();
		
		mMediaController.show(10 * 1000);
		mBtnPlay.setVisibility(View.VISIBLE);
		
		Toast.makeText(this, R.string.double_full_screen, 2000).show();
	}

	@Override
	public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		mProgressDialog.dismiss();
		Toast.makeText(this, R.string.load_video_failed, 1000).show();
		
		return true;
	}

	@Override
	public void onVideoPlay() {
		// TODO Auto-generated method stub
		mBtnPlay.setVisibility(View.GONE);
	}

	@Override
	public void onVideoPause() {
		// TODO Auto-generated method stub
		mBtnPlay.setVisibility(View.VISIBLE);
	}
}
