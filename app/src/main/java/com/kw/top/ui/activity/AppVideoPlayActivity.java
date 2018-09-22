package com.kw.top.ui.activity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.kw.top.R;
import com.kw.top.base.BaseActivity;
import com.kw.top.utils.RxToast;
import com.kw.top.view.UIVideoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author  ： zy
 * date    ： 2018/7/8
 * des     ：
 */

public class AppVideoPlayActivity extends BaseActivity implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {
    @BindView(R.id.video_view)
    VideoView mVideoView;
    @BindView(R.id.iv_play)
    ImageView mIvPlay;
    @BindView(R.id.picture_left_back)
    ImageView mPictureLeftBack;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    private String video_path = "";
    private MediaController mMediaController;
    private int mPositionWhenPaused = -1;

    public static void startActivity(Context context, String video_path) {
        Intent intent = new Intent(context, AppVideoPlayActivity.class);
        intent.putExtra("video_path", video_path);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.picture_activity_video_play;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);

        video_path = getIntent().getStringExtra("video_path");
        mVideoView.setBackgroundColor(Color.BLACK);
        mMediaController = new MediaController(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setMediaController(mMediaController);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (null != mIvPlay) {
            mIvPlay.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        RxToast.normal("视频播放出错");
        return false;
    }


    @Override
    public void onStart() {
        // Play Video
        mVideoView.setVideoPath(video_path);
        mVideoView.start();
        super.onStart();
    }

    @Override
    public void onPause() {
        // Stop video when the activity is pause.
        mPositionWhenPaused = mVideoView.getCurrentPosition();
        mVideoView.stopPlayback();

        super.onPause();
    }


    @Override
    protected void onResume() {
        // Resume video player
        if (mPositionWhenPaused >= 0) {
            mVideoView.seekTo(mPositionWhenPaused);
            mPositionWhenPaused = -1;
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mMediaController = null;
        mVideoView = null;
        mIvPlay = null;
        super.onDestroy();
    }

    @OnClick({R.id.iv_play, R.id.picture_left_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_play:
                mVideoView.start();
                mIvPlay.setVisibility(View.INVISIBLE);
                break;
            case R.id.picture_left_back:
                finish();
                break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new ContextWrapper(newBase) {
            @Override
            public Object getSystemService(String name) {
                if (Context.AUDIO_SERVICE.equals(name)) {
                    return getApplicationContext().getSystemService(name);
                }
                return super.getSystemService(name);
            }
        });
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mProgressBar.setVisibility(View.GONE);
        mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    // video started
                    mVideoView.setBackgroundColor(Color.TRANSPARENT);
                    return true;
                }
                return false;
            }
        });
    }
}
