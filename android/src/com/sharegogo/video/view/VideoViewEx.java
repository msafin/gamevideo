package com.sharegogo.video.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class VideoViewEx extends VideoView {

    private PlayPauseListener mListener;

    public VideoViewEx(Context context) {
        super(context);
    }

    public VideoViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoViewEx(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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

   static public interface PlayPauseListener {
        void onVideoPlay();
        void onVideoPause();
    }
}
