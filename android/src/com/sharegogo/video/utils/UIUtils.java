package com.sharegogo.video.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.sharegogo.video.activity.PlayActivity;
import com.sharegogo.video.data.VideoDetail;
import com.sharegogo.video.settings.SharegogoVideoSettings;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

/**
 * UIÏà¹Ø¸¨Öúº¯Êý
 * @author Raymon
 *
 */
public class UIUtils {
	
	static public void gotoPlayActivity(VideoDetail video,Context context,long progress)
	{
		if(video == null)
		{
			return;
		}
		
		Intent intent = new Intent(Intent.ACTION_MAIN);
		
		intent.setClass(context, PlayActivity.class);
		intent.putExtra(PlayActivity.KEY_VIDEO_NAME, video.name);
		intent.putExtra(PlayActivity.KEY_VIDEO_AUTHOR, video.author);
		intent.putExtra(PlayActivity.KEY_VIDEO_SOURCE, video.from);
		intent.putExtra(PlayActivity.KEY_VIDEO_ID,video.id);
		intent.putExtra(PlayActivity.KEY_FLASH_URL,video.flashUrl);
		intent.putExtra(PlayActivity.KEY_VIDEO_VID,video.getVideoId());
		intent.putExtra(PlayActivity.KEY_VIDEO_PROGRESS, progress);
		
		context.startActivity(intent);
	}
	
	static public void gotoShareActivity(Context context)
	{
		SocializeConfig config = new SocializeConfig();
		config.setShareMail(false);
		config.setPlatforms(SHARE_MEDIA.TENCENT,SHARE_MEDIA.SINA);
		UMSocialService controller = UMServiceFactory.getUMSocialService("sharegogo", RequestType.SOCIAL); 
		controller.setConfig(config);
		
		controller.openShare(context,false);
	}
	
	static public void gotoBrowserActivity(Activity activity,String url)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW);
		
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setData(Uri.parse(url));
		
		activity.startActivity(intent);
	}
	
	static public void DisplayImage(String uri,ImageView imageView,int defaultImage)
	{
		if(SharegogoVideoSettings.getWifiFetchImage() == 0 ||
			(SharegogoVideoSettings.getWifiFetchImage() == 1 && NetworkUtils.getNetType() == NetworkUtils.TYPE_WIFI))
			
		DisplayImage(uri,imageView,defaultImage,null);
	}
	
	static public void DisplayImage(String uri,ImageView imageView,int defaultImage,ImageLoadingListener listener)
	{
		DisplayImageOptions options = new DisplayImageOptions.Builder()
        .showStubImage(defaultImage)
        .resetViewBeforeLoading()
        .delayBeforeLoading(1000)
        .showImageForEmptyUri(defaultImage)
        .cacheInMemory()
        .cacheOnDisc()
        .imageScaleType(ImageScaleType.EXACTLY)
        .build();
		
		if(listener != null)
		{
			ImageLoader.getInstance().displayImage(uri, imageView, options,listener);
		}
		else
		{
			ImageLoader.getInstance().displayImage(uri, imageView, options);
		}
	}
}
