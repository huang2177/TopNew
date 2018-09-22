package com.kw.top.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.ProgressBar;

import com.kw.top.R;
import com.kw.top.base.BaseActivity;
import com.kw.top.view.UIVideoView;

/**
 * author  ： zy
 * date    ： 2018/6/25
 * des     ：
 */

public class VideoViewActivity extends BaseActivity implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, UIVideoView.videoPlayPauseListener {
    UIVideoView mVideoView;
    ProgressBar mProgressBar;
    private MediaController mMediaController;
    private String video_path = "";

    public static void startActivity(Activity activity, String videoPath) {
        Intent intent = new Intent(activity, VideoViewActivity.class);
        intent.putExtra("PATH", videoPath);
        activity.startActivity(intent);
//        activity.overridePendingTransition(0, 0);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_videoview;
    }

    private void initView() {
        video_path = getIntent().getStringExtra("PATH");
        initVideo();
    }

    private void initVideo() {
        mVideoView.setVideoPath(video_path);
        mMediaController = new MediaController(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setVideoPlayPauseListener(this);
        mVideoView.start();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mProgressBar.setVisibility(View.GONE);
        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
                if (i == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    mVideoView.setBackgroundColor(Color.TRANSPARENT);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onVideostart() {

    }

    @Override
    public void onVideoPause() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getContentView());
        mVideoView = findViewById(R.id.video_view);
        mProgressBar = findViewById(R.id.progress_bar);
        initView();
    }

    @Override
    public void onPause() {
        if (mVideoView != null) {
            mVideoView.stopPlayback();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mVideoView != null) {
            mVideoView.stopPlayback();
        }
    }

    @Override
    public void onDestroy() {
        mMediaController = null;
        mVideoView = null;
        super.onDestroy();
    }

}
