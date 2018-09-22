package com.kw.top.ui.activity.login;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kw.top.R;
import com.kw.top.base.BaseActivity;
import com.kw.top.view.camerasurfaceview.CameraSurfaceView;
import com.kw.top.view.camerasurfaceview.CheckPermissionsUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author  ： zy
 * date    ： 2018/6/11
 * des     ：
 */

public class TestStartActivity extends BaseActivity {
    @Override
    public int getContentView() {
        return 0;
    }
//    @BindView(R.id.tv_start)
//    TextView mTvStart;
//    @BindView(R.id.camera_surface)
//    CameraSurfaceView mCameraSurface;
//
//    @Override
//    public int getContentView() {
//        return R.layout.activity_start_verify;
//    }
//
//    public void initView() {
////        getPermissions();
//        CheckPermissionsUtil checkPermissionsUtil = new CheckPermissionsUtil(this);
//        checkPermissionsUtil.requestAllPermission(this);
//        mCameraSurface.setDefaultCamera(false);
//    }
//
//    @OnClick({ R.id.tv_start})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.tv_start:
////                startActivity(new Intent(this, TestVerify2Activity.class));
//                finish();
//                break;
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
////        mCameraSurface.closeCamera();
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//    }
//
//
//    private final int GET_PERMISSION_REQUEST = 100; //权限申请自定义码
//
//    /**
//     * 获取权限
//     */
//    private void getPermissions() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager
//                    .PERMISSION_GRANTED &&
//                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager
//                            .PERMISSION_GRANTED &&
//                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager
//                            .PERMISSION_GRANTED) {
////                startActivityForResult(new Intent(SexActivity.this, VideoVerifyActivity.class), 100);
//            } else {
//                //不具有获取权限，需要进行权限申请
//                ActivityCompat.requestPermissions(TestStartActivity.this, new String[]{
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.RECORD_AUDIO,
//                        Manifest.permission.CAMERA}, GET_PERMISSION_REQUEST);
//            }
//        } else {
////            startActivityForResult(new Intent(SexActivity.this, VideoVerifyActivity.class), 100);
//        }
//    }
//
//    @TargetApi(23)
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == GET_PERMISSION_REQUEST) {
//            int size = 0;
//            if (grantResults.length >= 1) {
//                int writeResult = grantResults[0];
//                //读写内存权限
//                boolean writeGranted = writeResult == PackageManager.PERMISSION_GRANTED;//读写内存权限
//                if (!writeGranted) {
//                    size++;
//                }
//                //录音权限
//                int recordPermissionResult = grantResults[1];
//                boolean recordPermissionGranted = recordPermissionResult == PackageManager.PERMISSION_GRANTED;
//                if (!recordPermissionGranted) {
//                    size++;
//                }
//                //相机权限
//                int cameraPermissionResult = grantResults[2];
//                boolean cameraPermissionGranted = cameraPermissionResult == PackageManager.PERMISSION_GRANTED;
//                if (!cameraPermissionGranted) {
//                    size++;
//                }
//                if (size == 0) {
////                    startActivityForResult(new Intent(SexActivity.this, VideoVerifyActivity.class), 100);
//                } else {
//                    Toast.makeText(this, "请到设置-权限管理中开启", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//            }
//        }
//    }

}
