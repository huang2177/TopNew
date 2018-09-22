package com.kw.top.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * author  ： zy
 * date    ： 2018/5/21
 * des     ：
 */

public class UIVideoView extends VideoView {

    private videoPlayPauseListener mVideoPlayPauseListener;

    public void setVideoPlayPauseListener(videoPlayPauseListener playPauseListener){
        this.mVideoPlayPauseListener = playPauseListener;
    }

    public UIVideoView(Context context) {
        super(context);
    }

    public UIVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UIVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface videoPlayPauseListener{
        void onVideostart();
        void onVideoPause();
    }

    @Override
    public void pause() {
        super.pause();
        if (mVideoPlayPauseListener!=null){
            mVideoPlayPauseListener.onVideoPause();
        }
    }

    @Override
    public void start() {
        super.start();
        if (mVideoPlayPauseListener!=null){
            mVideoPlayPauseListener.onVideostart();
        }
    }
}
