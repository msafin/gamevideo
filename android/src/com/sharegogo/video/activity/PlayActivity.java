package com.sharegogo.video.activity;

import com.sharegogo.video.controller.FavoriteManager;
import com.sharegogo.video.data.Favorite;
import com.sharegogo.video.game.R;
import com.sharegogo.video.utils.UIUtils;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.HttpAuthHandler;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class PlayActivity extends FragmentActivity implements OnClickListener{
	static final public String KEY_FLASH_URL = "flash_url";
	static final public String KEY_VIDEO_ID = "video_id";
	
	private WebView mWebView = null;
	private String mUrl = null;
	private long mVideoId = -1;
	private ImageButton mBtnFavorite;
	private ImageButton mBtnRecommend;
	private ImageButton mBtnShare;
	private Favorite mFavorite;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(arg0);
		setContentView(R.layout.play_activity);
		
		mBtnFavorite = (ImageButton)findViewById(R.id.btn_favorite);
		mBtnRecommend = (ImageButton)findViewById(R.id.btn_recommend);
		mBtnShare = (ImageButton)findViewById(R.id.btn_share);
		
		mBtnFavorite.setOnClickListener(this);
		mBtnRecommend.setOnClickListener(this);
		mBtnShare.setOnClickListener(this);
		
		
		mWebView = (WebView)findViewById(R.id.webView1);
		
		Intent intent = this.getIntent();
		mUrl = intent.getStringExtra(KEY_FLASH_URL);
		mVideoId = intent.getLongExtra(KEY_VIDEO_ID, -1);
		
		mFavorite = FavoriteManager.getInstance().getFavorite(mVideoId);
		if(mFavorite != null)
		{
			mBtnFavorite.setImageResource(R.drawable.ic_favorited);
		}
		
		mWebView.loadUrl("file:///android_asset/index.html");
        mWebView.getSettings().setPluginsEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setPluginState(PluginState.ON);
        mWebView.addJavascriptInterface(new VideoInterface(), "VideoInterface");
        mWebView.setWebChromeClient(new WebChromeClient() {
	        	   public void onProgressChanged(WebView view, int progress) {
	        	     // Activities and WebViews measure progress with different scales.
	        	     // The progress meter will automatically disappear when we reach 100%
	        	   }

				@Override
				public Bitmap getDefaultVideoPoster() {
					// TODO Auto-generated method stub
					return super.getDefaultVideoPoster();
				}

				@Override
				public View getVideoLoadingProgressView() {
					// TODO Auto-generated method stub
					return super.getVideoLoadingProgressView();
				}

				@Override
				public void getVisitedHistory(ValueCallback<String[]> callback) {
					// TODO Auto-generated method stub
					super.getVisitedHistory(callback);
				}

				@Override
				public void onCloseWindow(WebView window) {
					// TODO Auto-generated method stub
					super.onCloseWindow(window);
				}

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
				}

				@Override
				public boolean onCreateWindow(WebView view, boolean isDialog,
						boolean isUserGesture, Message resultMsg) {
					// TODO Auto-generated method stub
					return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
				}

				@Override
				public void onExceededDatabaseQuota(String url,
						String databaseIdentifier, long quota,
						long estimatedDatabaseSize, long totalQuota,
						QuotaUpdater quotaUpdater) {
					// TODO Auto-generated method stub
					super.onExceededDatabaseQuota(url, databaseIdentifier, quota,
							estimatedDatabaseSize, totalQuota, quotaUpdater);
				}

				@Override
				public void onGeolocationPermissionsHidePrompt() {
					// TODO Auto-generated method stub
					super.onGeolocationPermissionsHidePrompt();
				}

				@Override
				public void onGeolocationPermissionsShowPrompt(String origin,
						Callback callback) {
					// TODO Auto-generated method stub
					super.onGeolocationPermissionsShowPrompt(origin, callback);
				}

				@Override
				public void onHideCustomView() {
					// TODO Auto-generated method stub
					super.onHideCustomView();
				}

				@Override
				public boolean onJsAlert(WebView view, String url, String message,
						JsResult result) {
					// TODO Auto-generated method stub
					return super.onJsAlert(view, url, message, result);
				}

				@Override
				public boolean onJsBeforeUnload(WebView view, String url,
						String message, JsResult result) {
					// TODO Auto-generated method stub
					return super.onJsBeforeUnload(view, url, message, result);
				}

				@Override
				public boolean onJsConfirm(WebView view, String url,
						String message, JsResult result) {
					// TODO Auto-generated method stub
					return super.onJsConfirm(view, url, message, result);
				}

				@Override
				public boolean onJsPrompt(WebView view, String url, String message,
						String defaultValue, JsPromptResult result) {
					// TODO Auto-generated method stub
					return super.onJsPrompt(view, url, message, defaultValue, result);
				}

				@Override
				public boolean onJsTimeout() {
					// TODO Auto-generated method stub
					return super.onJsTimeout();
				}

				@Override
				public void onReachedMaxAppCacheSize(long requiredStorage,
						long quota, QuotaUpdater quotaUpdater) {
					// TODO Auto-generated method stub
					super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
				}

				@Override
				public void onReceivedIcon(WebView view, Bitmap icon) {
					// TODO Auto-generated method stub
					super.onReceivedIcon(view, icon);
				}

				@Override
				public void onReceivedTitle(WebView view, String title) {
					// TODO Auto-generated method stub
					super.onReceivedTitle(view, title);
				}

				@Override
				public void onReceivedTouchIconUrl(WebView view, String url,
						boolean precomposed) {
					// TODO Auto-generated method stub
					super.onReceivedTouchIconUrl(view, url, precomposed);
				}

				@Override
				public void onRequestFocus(WebView view) {
					// TODO Auto-generated method stub
					super.onRequestFocus(view);
				}
	        	   
	        	 });
	        
	        
	        mWebView.setWebViewClient(new WebViewClient() {
	    	   public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) 
	    	   {
	    		   Log.e("flash", description);
	    	   }

	    	   
			@Override
			public void doUpdateVisitedHistory(WebView view, String url,
					boolean isReload) {
				// TODO Auto-generated method stub
				super.doUpdateVisitedHistory(view, url, isReload);
			}


			@Override
			public void onFormResubmission(WebView view, Message dontResend,
					Message resend) {
				// TODO Auto-generated method stub
				super.onFormResubmission(view, dontResend, resend);
			}


			@Override
			public void onLoadResource(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onLoadResource(view, url);
			}


			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
			}


			@Override
			public void onReceivedHttpAuthRequest(WebView view,
					HttpAuthHandler handler, String host, String realm) {
				// TODO Auto-generated method stub
				super.onReceivedHttpAuthRequest(view, handler, host, realm);
			}


			@TargetApi(12)
			@Override
			public void onReceivedLoginRequest(WebView view, String realm,
					String account, String args) {
				// TODO Auto-generated method stub
				super.onReceivedLoginRequest(view, realm, account, args);
			}


			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler,
					SslError error) {
				// TODO Auto-generated method stub
				super.onReceivedSslError(view, handler, error);
			}


			@Override
			public void onScaleChanged(WebView view, float oldScale, float newScale) {
				// TODO Auto-generated method stub
				super.onScaleChanged(view, oldScale, newScale);
			}


			@Override
			public void onTooManyRedirects(WebView view, Message cancelMsg,
					Message continueMsg) {
				// TODO Auto-generated method stub
				super.onTooManyRedirects(view, cancelMsg, continueMsg);
			}


			@Override
			public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
				// TODO Auto-generated method stub
				super.onUnhandledKeyEvent(view, event);
			}


			@TargetApi(11)
			@Override
			public WebResourceResponse shouldInterceptRequest(WebView view,
					String url) {
				// TODO Auto-generated method stub
				return super.shouldInterceptRequest(view, url);
			}


			@Override
			public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
				// TODO Auto-generated method stub
				return super.shouldOverrideKeyEvent(view, event);
			}


			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				return super.shouldOverrideUrlLoading(view, url);
			}


			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
					super.onPageFinished(view, url);
				
					mWebView.loadUrl("javascript:callJS()");  //java调用js的函数
				}
	    	 });
	}

	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		try {
            Class.forName("android.webkit.WebView")
                    .getMethod("onPause", (Class[]) null)
                    .invoke(mWebView, (Object[]) null);
        } catch (Exception e) {
        	e.printStackTrace();
        }
		mWebView.pauseTimers();
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
        try{
	        Class.forName("android.webkit.WebView")
	        .getMethod("onResume", (Class[]) null)
	        .invoke(mWebView, (Object[]) null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mWebView.resumeTimers();

	}


	private String buildHtml()
	{
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
}
