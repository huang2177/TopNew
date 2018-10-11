package com.kw.top.ui.activity.login;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.listener.ErrorListener;
import com.cjt2325.cameralibrary.listener.JCameraListener;
import com.cjt2325.cameralibrary.util.FileUtil;
import com.kw.top.R;
import com.kw.top.app.AppManager;
import com.kw.top.app.BaseApplication;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.CommandTools;
import com.kw.top.tools.FileTools;
import com.kw.top.tools.QiniuUpLoadManager;
import com.kw.top.ui.activity.NewMainActivity;
import com.kw.top.utils.DisplayUtils;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONException;
import org.json.JSONObject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author  ： zy
 * date    ： 2018/6/16
 * des     ：
 */

public class VideoVerifyActivity extends BaseActivity {

    JCameraView jCameraView;
    ImageView mIvBack;
    TextView mTvTitle;
    TextView mTvTitleRight;
    RelativeLayout mRelativeTitle;
    TextView mTvHint1;
    TextView mTvHint2;
    TextView mTvNumb;
    TextView mTvStart;
    TextView mTvHint3;
    private int num;
    private int state = 0; //0 开始认证  1 正在认证

    @Override
    public int getContentView() {
        return R.layout.activity_video_verify2;
    }

    private void initView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getContentView());
        jCameraView = findViewById(R.id.jcamera_view);
        mIvBack = findViewById(R.id.iv_back);
        mTvTitle = findViewById(R.id.tv_title);
        mTvTitleRight = findViewById(R.id.tv_title_right);
        mRelativeTitle = findViewById(R.id.relative_title);
        mTvHint1 = findViewById(R.id.tv_hint1);
        mTvHint2 = findViewById(R.id.tv_hint2);
        mTvNumb = findViewById(R.id.tv_random_numb);
        mTvStart = findViewById(R.id.tv_start);
        mTvHint3 = findViewById(R.id.tv_hint3);
        num = (int) ((Math.random() * 9 + 1) * 1000);
        mTvNumb.setText(num + "");
        mTvTitleRight.setVisibility(View.GONE);
        mTvTitleRight.setText("身份保密");
        mTvTitle.setText("视频认证");
        initData();
        initListener();
    }

    public void initData() {
        //设置视频保存路径
        jCameraView.setSaveVideoPath(FileTools.getVideoPath());
        jCameraView.setFeatures(JCameraView.BUTTON_STATE_ONLY_RECORDER);
        jCameraView.setTip("");
        jCameraView.setCaptureLayoutVisibility(View.GONE);
        jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE);
        jCameraView.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {
                //错误监听
                Log.i("CJT", "camera error");
                Intent intent = new Intent();
                setResult(103, intent);
                finish();
            }

            @Override
            public void AudioPermissionError() {
                Toast.makeText(VideoVerifyActivity.this, "给点录音权限可以?", Toast.LENGTH_SHORT).show();
            }
        });
        //JCameraView监听
        jCameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap
//                Log.i("JCameraView", "bitmap = " + bitmap.getWidth());
                String path = FileUtil.saveBitmap("JCamera", bitmap);
                Intent intent = new Intent();
                intent.putExtra("path", path);
                setResult(101, intent);
                finish();
            }

            @Override
            public void recordSuccess(final String url, Bitmap firstFrame) {
                //获取视频路径
                Log.e("tag", "=============== path " + url);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getQiniuToken(url);
                    }
                });
            }
        });

    }

    //开始认证UI
    private void startUI() {
        state = 0;
        mTvTitleRight.setText("身份保密");
        mTvTitleRight.setVisibility(View.VISIBLE);
        mTvTitle.setText("视频认证");
        mTvHint1.setVisibility(View.VISIBLE);
        mTvHint2.setVisibility(View.VISIBLE);
        mTvHint3.setVisibility(View.VISIBLE);
        mTvStart.setVisibility(View.VISIBLE);
        mTvNumb.setVisibility(View.GONE);
        ViewGroup.LayoutParams params = jCameraView.getLayoutParams();
        params.width = DisplayUtils.dip2px(this, 230);
        params.height = DisplayUtils.dip2px(this, 300);
        jCameraView.setLayoutParams(params);
        jCameraView.setCaptureLayoutVisibility(View.GONE);
    }

    //认证UI
    private void verifyVI() {
        state = 1;
        mTvTitleRight.setText("");
        mTvTitleRight.setVisibility(View.GONE);
        mTvHint1.setVisibility(View.GONE);
        mTvHint2.setVisibility(View.GONE);
        mTvHint3.setVisibility(View.GONE);
        mTvStart.setVisibility(View.GONE);
        mTvNumb.setVisibility(View.VISIBLE);
        mTvTitle.setText("请在录像中读出一下验证码");
        ViewGroup.LayoutParams params = jCameraView.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        jCameraView.setLayoutParams(params);
        jCameraView.setCaptureLayoutVisibility(View.VISIBLE);
    }


    public void initListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == 1) {
                    startUI();
                } else {
                    finish();
                }
            }
        });

        mTvTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(WomanVipActivity.class);
            }
        });

        mTvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyVI();
            }
        });
    }


    private final int GET_PERMISSION_REQUEST = 100; //权限申请自定义码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPermissions();
    }

    /**
     * 获取权限
     */
    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager
                    .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager
                            .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager
                            .PERMISSION_GRANTED) {
                initView();
            } else {
                //不具有获取权限，需要进行权限申请
                ActivityCompat.requestPermissions(VideoVerifyActivity.this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA}, GET_PERMISSION_REQUEST);
            }
        } else {
            initView();
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GET_PERMISSION_REQUEST) {
            int size = 0;
            if (grantResults.length >= 1) {
                int writeResult = grantResults[0];
                //读写内存权限
                boolean writeGranted = writeResult == PackageManager.PERMISSION_GRANTED;//读写内存权限
                if (!writeGranted) {
                    size++;
                }
                //录音权限
                int recordPermissionResult = grantResults[1];
                boolean recordPermissionGranted = recordPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!recordPermissionGranted) {
                    size++;
                }
                //相机权限
                int cameraPermissionResult = grantResults[2];
                boolean cameraPermissionGranted = cameraPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!cameraPermissionGranted) {
                    size++;
                }
                if (size == 0) {
                    initView();
                } else {
                    Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        jCameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        jCameraView.onPause();
    }

    /**
     * 点击系统返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (state == 1) {
                startUI();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void getQiniuToken(final String path) {
        showProgressDialog();
        //获得七牛Token
        Api.getApiService().getUpToken(getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        if (baseBean.isSuccess()) {
                            String token = (String) baseBean.getData();
                            upFileToQiniu(token, path);
                        } else if (baseBean.getCode().equals("-44")) {
                            RxToast.normal(getResources().getString(R.string.login_out));
                            SPUtils.clear(VideoVerifyActivity.this);
                            startActivity(LoginActivity.class);
                            finish();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });
    }

    private void upFileToQiniu(String token, String path) {
        String key = CommandTools.getAndroidId(BaseApplication.getInstance()) + SystemClock.currentThreadTimeMillis();
        QiniuUpLoadManager.getInstance().uploadFile(path, key, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                //res包含hash、key等信息，具体字段取决于上传策略的设置
                if (info.isOK()) {
                    String resKey = "";
                    hideProgressDialog();
                    RxToast.normal("上传成功");
                    try {
                        resKey = response.getString("key");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    sendUrlToService(resKey);
                    finish();
//                    startActivity(UpPictureActivity.class);
                } else {
                    Log.i("qiniu", "===============Upload Fail");
                    //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                    hideProgressDialog();
                    RxToast.normal("上传失败,请重新上传");
                }
                Log.i("qiniu", "===================" + key + ",\r\n " + info + ",\r\n " + response);
            }

        }, new UploadOptions(null, null, false, new UpProgressHandler() {
            @Override
            public void progress(String key, double percent) {
            }
        }, new UpCancellationSignal() {
            @Override
            public boolean isCancelled() {
                return false;
            }
        }));
    }

    private void sendUrlToService(String resKey) {
        Api.getApiService().addVerifyVideoUrl(resKey, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        RxToast.normal("上传成功,请等待审核");
                        Intent intent = new Intent(VideoVerifyActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        //startActivity(UpPictureActivity.class);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                        RxToast.normal("上传失败,请重新上传");
                    }
                });
    }

}

