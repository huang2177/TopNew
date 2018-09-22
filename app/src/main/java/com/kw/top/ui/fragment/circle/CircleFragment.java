package com.kw.top.ui.fragment.circle;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.kw.top.R;
import com.kw.top.base.BaseFragment;
import com.kw.top.ui.activity.VideoPlayActivity;
import com.kw.top.ui.activity.circle.SendCircleActivity;
import com.kw.top.utils.RxToast;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static android.app.Activity.RESULT_OK;

/**
 * author: zy
 * data  : 2018/6/3
 * des   :
 */

public class CircleFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.rb_three_left)
    RadioButton mRbThreeLeft;
    @BindView(R.id.rb_three_center)
    RadioButton mRbThreeCenter;
    @BindView(R.id.rb_three_right)
    RadioButton mRbThreeRight;
    @BindView(R.id.radio_group_circle)
    RadioGroup mRadioGroupCircle;
    @BindView(R.id.frame_layout_circle)
    FrameLayout mFrameLayoutCircle;
    @BindView(R.id.iv_send_circle)
    ImageView mIvSendCircle;
    private CircleContentFragment mPublicFragment, mProvicyFragment, mFriendsFragment;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;
    private List<Fragment> mFragments = new ArrayList<>();
    protected String cameraPath;
    private String outputCameraPath;
    private String recordVideoSecond = 60+"";//录制秒数
    private String videoQuality = "1"; //录制视频的质量 0/1
    private RxPermissions rxPermissions;

    public static CircleFragment newInstance() {
        CircleFragment findFrament = new CircleFragment();
        return findFrament;
    }

    @Override
    public int getContentView() {
        return R.layout.frament_circle;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {
        mRadioGroupCircle.setOnCheckedChangeListener(this);
        rxPermissions = new RxPermissions(getActivity());
    }

    @Override
    public void initData() {
        mFragmentManager = getActivity().getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();

        mFragments.add(mPublicFragment);
        mFragments.add(mProvicyFragment);
        mFragments.add(mFriendsFragment);
        if (null == mPublicFragment) {
            //mPublicFragment = CircleContentFragment.onNewInstance("01");
            mTransaction.add(R.id.frame_layout_circle, mPublicFragment);
        }
        mTransaction.show(mPublicFragment);
        mTransaction.commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        mTransaction = mFragmentManager.beginTransaction();
        if (null!=mPublicFragment)
            mTransaction.hide(mPublicFragment);
        if (null!=mProvicyFragment)
            mTransaction.hide(mProvicyFragment);
        if (null!=mFriendsFragment)
            mTransaction.hide(mFriendsFragment);
        switch (checkedId) {
            case R.id.rb_three_left:
                if (null == mPublicFragment) {
                    //mPublicFragment = CircleContentFragment.onNewInstance("01");
                    mTransaction.add(R.id.frame_layout_circle, mPublicFragment);
                }
                mTransaction.show(mPublicFragment);
                break;
            case R.id.rb_three_center:
                if (null == mProvicyFragment) {
                    //mProvicyFragment = CircleContentFragment.onNewInstance("02");
                    mTransaction.add(R.id.frame_layout_circle, mProvicyFragment);
                }
                mTransaction.show(mProvicyFragment);
                break;
            case R.id.rb_three_right:
                if (null == mFriendsFragment) {
                    //mFriendsFragment = CircleContentFragment.onNewInstance("03");
                    mTransaction.add(R.id.frame_layout_circle, mFriendsFragment);
                }
                mTransaction.show(mFriendsFragment);
                break;
        }
        mTransaction.commit();
    }

    @OnClick(R.id.iv_send_circle)
    public void onViewClicked() {
        showModPhotosView();
    }

    private Dialog dialog;

    private void showModPhotosView() {
        View view = View.inflate(getContext(), R.layout.dialog_choose_circle, null);
        Button select_photo_camera = (Button) view.findViewById(R.id.select_photo_camera_bt);
        Button select_photo_image = (Button) view.findViewById(R.id.select_photo_local_bt);
        Button tv_cancel = (Button) view.findViewById(R.id.tv_cancel);
        select_photo_camera.setOnClickListener(cameraClick);
        select_photo_image.setOnClickListener(cameraClick);
        tv_cancel.setOnClickListener(cameraClick);
        dialog = new Dialog(getContext(), R.style.charge_dialog_style);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.popupAnimation); // 添加动画
        dialog.show();
    }

    private View.OnClickListener cameraClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialog.dismiss();
            switch (view.getId()) {
                case R.id.select_photo_local_bt://相册选取
                    SendCircleActivity.startActivity(getActivity(),0,"");
                    break;
                case R.id.select_photo_camera_bt://拍摄
                    outputCameraPath =getDiskCacheDir(getContext()) +"/top/video/"; //录制视频输出路径
                    getPermission();
                    break;
                case R.id.tv_cancel://取消
                    break;

            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK&& requestCode == PictureConfig.REQUEST_CAMERA){
            SendCircleActivity.startActivity(getContext(),1,cameraPath);
        }
    }

    /**
     * start to camera、video
     */
    public void startOpenCameraVideo() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (cameraIntent.resolveActivity(getContext().getPackageManager()) != null) {
            File cameraFile = PictureFileUtils.createCameraFile(getContext(), PictureConfig.TYPE_VIDEO,
                    outputCameraPath, PictureFileUtils.POST_VIDEO);
            cameraPath = cameraFile.getAbsolutePath();
            Uri imageUri = parUri(cameraFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, recordVideoSecond);
            cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(cameraIntent, PictureConfig.REQUEST_CAMERA);
        }
    }

    /**
     * 生成uri
     *
     * @param cameraFile
     * @return
     */
    private Uri parUri(File cameraFile) {
        Uri imageUri;
        String authority = getContext().getPackageName() + ".provider";
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(getContext(), authority, cameraFile);
        } else {
            imageUri = Uri.fromFile(cameraFile);
        }
        return imageUri;
    }

    public void getPermission() {
        rxPermissions.request(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            startOpenCameraVideo();
                        } else {
                            RxToast.normal(getString(com.luck.picture.lib.R.string.picture_camera));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }
}
