package com.sharegogo.video.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.VideoView;

public class VideoViewEx extends VideoView {

    private PlayPauseListener mListener;
    private boolean mFullScreen = false;
    private int mWidth = 0;
    private int mHeight = 0;
    
    public VideoViewEx(Context context) {
        super(context);
    }

    public VideoViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoViewEx(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    
    @Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		if(mFullScreen)
		{
			setMeasuredDimension(mWidth,mHeight);
		}
	}

	public void setPlayPauseListener(PlayPauseListener listener) {
        mListener = listener;
    }

    @Override
    public void pause() {
        super.pause();
        if (mListener != null) {
            mListener.onVideoPause();
        }
    }

    @Override
    public void start() {
        super.start();
        if (mListener != null) {
            mListener.onVideoPlay();
        }
    }
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        if (visibility == View.VISIBLE) { 
            super.onWindowVisibilityChanged(visibility);
        }
    }

    public void setFullScreen(boolean fullScreen,int width,int height)
    {
    	mFullScreen = fullScreen;
    	mWidth = width;
    	mHeight = height;
    }
    
   static public interface PlayPauseListener {
        void onVideoPlay();
        void onVideoPause();
    }
}
