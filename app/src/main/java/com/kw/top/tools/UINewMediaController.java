package com.kw.top.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kw.top.R;
import com.kw.top.utils.NetworkUtils;
import com.kw.top.view.BaseLayout;
import com.pili.pldroid.player.IMediaController;

import java.util.Locale;

import butterknife.BindView;

/**
 * You can write a custom MediaController instead of this class
 * A MediaController widget must implement all the interface defined by com.pili.pldroid.player.IMediaController
 */
public class UINewMediaController extends BaseLayout implements IMediaController {
    private static int sDefaultTimeout = 3000;

    private static final int SEEK_TO_POST_DELAY_MILLIS = 200;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;


    private MediaPlayerControl mPlayer;
    @BindView(R.id.root_controller_layout)
    View mRootView;

    @BindView(R.id.play_iv)
    ImageView mPlayIv;
    @BindView(R.id.play_time_tv)
    TextView mPlayTimeTv;
    @BindView(R.id.seek_bar)
    SeekBar mSeekBar;
    @BindView(R.id.total_time_tv)
    TextView mTotalTimeTv;
    @BindView(R.id.full_screen_iv)
    ImageView mFullScreenIv;

    /**
     * 是否第一次流量播放播放
     */
    private boolean mIsFirstNotWifiPlay;
    private boolean mShowing;
    private boolean mDragging;
    private boolean mInstantSeeking = true;
    private long mDuration;
    private AudioManager mAM;
    private Runnable mLastSeekBarRunnable;
    private boolean mDisableProgress = false;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            long pos;
            switch (msg.what) {
                case FADE_OUT:
                    hide();
                    break;
                case SHOW_PROGRESS:
                    if (!mPlayer.isPlaying()) {
                        return;
                    }
                    pos = setProgress();
                    if (pos == -1) {
                        return;
                    }

                    if (!mDragging && mShowing) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                        updatePausePlay();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public UINewMediaController(Context context) {
        super(context);
    }

    public UINewMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UINewMediaController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getLayoutId() {
        return R.layout.widget_media_controller_9_0;
    }

    @Override
    public void initView() {
        mAM = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        mSeekBar.setThumbOffset(1);
        mSeekBar.setMax(1000);
        mSeekBar.setEnabled(!mDisableProgress);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mSeekBar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        mPlayIv.setOnClickListener(mOnClickListener);
        mFullScreenIv.setOnClickListener(mOnClickListener);
        mRootView.setOnClickListener(mOnClickListener);
    }

    @Override
    public void setMediaPlayer(MediaPlayerControl player) {
        mPlayer = player;
        updatePausePlay();
    }

    @Override
    public void show() {
        show(sDefaultTimeout);
        updatePausePlay();
    }

    @Override
    public void show(int timeout) {
        if (!mShowing) {
            disableUnsupportedButtons();
            setVisibility(View.VISIBLE);
            mShowing = true;
            if (mOnControllerClickListener != null) {
                mOnControllerClickListener.onShown();
            }
        }
        updatePausePlay();
        mHandler.sendEmptyMessage(SHOW_PROGRESS);
        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(mHandler.obtainMessage(FADE_OUT),
                                        timeout);
        }
    }


    @Override
    public void hide() {
        if (mShowing) {
            try {
                mHandler.removeMessages(SHOW_PROGRESS);
                setVisibility(View.INVISIBLE);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
            mShowing = false;
            if (mOnControllerClickListener != null) {
                mOnControllerClickListener.onHidden();
            }
        }
    }

    private long setProgress() {
        if (mPlayer == null || mDragging) {
            return 0;
        }

        long position = mPlayer.getCurrentPosition();
        long duration = mPlayer.getDuration();
        if (mSeekBar != null) {
            if (duration > 0) {
                long pos = 1000L * position / duration;
                mSeekBar.setProgress((int) pos);
            }
            int percent = mPlayer.getBufferPercentage();
            mSeekBar.setSecondaryProgress(percent * 10);
        }

        mDuration = duration;

        if (mTotalTimeTv != null) {
            mTotalTimeTv.setText(generateTime(mDuration));
        }

        if (mPlayTimeTv != null) {
            mPlayTimeTv.setText(generateTime(position));
        }

        return position;
    }

    @Override
    public boolean isShowing() {
        return mShowing;
    }

    @Override
    public void setAnchorView(View view) {
    }


    private void updatePausePlay() {
        if (mPlayIv == null) {
            return;
        }
        if (mPlayer.isPlaying()) {
            mPlayIv.setImageResource(R.drawable.ic_media_pause);
        } else {
            mPlayIv.setImageResource(R.drawable.ic_media_play);
        }
    }


    private void disableUnsupportedButtons() {
        try {
            if (mPlayIv != null && !mPlayer.canPause()) {
                mPlayIv.setEnabled(false);
            }
        } catch (IncompatibleClassChangeError ex) {
        }
    }


    private static String generateTime(long position) {
        int totalSeconds = (int) (position / 1000);

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        if (hours > 0) {
            return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes,
                                 seconds).toString();
        } else {
            return String.format(Locale.US, "%02d:%02d", minutes, seconds)
                    .toString();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        show(sDefaultTimeout);
        return true;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        show(sDefaultTimeout);
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (event.getRepeatCount() == 0 &&
                (keyCode == KeyEvent.KEYCODE_HEADSETHOOK || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                        || keyCode == KeyEvent.KEYCODE_SPACE)) {
            doPauseResume();
            show(sDefaultTimeout);
            if (mPlayIv != null) {
                mPlayIv.requestFocus();
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP) {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                updatePausePlay();
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_MENU) {
            hide();
            return true;
        } else {
            show(sDefaultTimeout);
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 根据状态
     */
    public void doPauseResume() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            if (mOnControllerClickListener != null) {
                mOnControllerClickListener.onStop();
            }
        } else {
            if (NetworkUtils.isConnected() && mIsFirstNotWifiPlay) {
                mIsFirstNotWifiPlay = false;
            } else {
                if (mOnControllerClickListener != null) {
                    mOnControllerClickListener.onStart();
                }
                mPlayer.start();
            }
        }
        updatePausePlay();
    }


    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!fromUser) {
                return;
            }

            final long newPosition = (mDuration * progress) / 1000;
            String time = generateTime(newPosition);
            if (mInstantSeeking) {
                mHandler.removeCallbacks(mLastSeekBarRunnable);
                mLastSeekBarRunnable = new Runnable() {
                    @Override
                    public void run() {
                        mPlayer.seekTo(newPosition);
                    }
                };
                mHandler.postDelayed(mLastSeekBarRunnable, SEEK_TO_POST_DELAY_MILLIS);
            }

            if (mPlayTimeTv != null) {
                mPlayTimeTv.setText(time);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mDragging = true;
            show(3600000);
            mHandler.removeMessages(SHOW_PROGRESS);
            if (mInstantSeeking) {
                mAM.setStreamMute(AudioManager.STREAM_MUSIC, true);
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (!mInstantSeeking) {
                mPlayer.seekTo((int) (mDuration * mSeekBar.getProgress()) / 1000);
            }

            show(sDefaultTimeout);
            mHandler.removeMessages(SHOW_PROGRESS);
            mAM.setStreamMute(AudioManager.STREAM_MUSIC, false);
            mDragging = false;
            mHandler.sendEmptyMessageDelayed(SHOW_PROGRESS, 1000);
        }
    };


    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.play_iv:
                    doPauseResume();
                    show(sDefaultTimeout);
                    break;
                case R.id.full_screen_iv:
                    if (mOnControllerClickListener != null) {
                        mOnControllerClickListener.onFullScreenClick();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 是否全屏
     *
     * @param isFullScreen 是否全屏
     */
    public void setFullScreen(boolean isFullScreen) {
        if (isFullScreen) {
            mFullScreenIv.setImageResource(R.drawable.ic_media_zoomout);
        } else {
            mFullScreenIv.setImageResource(R.drawable.ic_media_fullscreen);
        }
    }

    private OnControllerClickListener mOnControllerClickListener;

    public void setOnControllerClickListener(OnControllerClickListener controllerClickListener) {
        mOnControllerClickListener = controllerClickListener;
    }

    public interface OnControllerClickListener {
        void onFullScreenClick();

        void onHidden();

        void onShown();

        void onStart();

        void onStop();
    }


}
