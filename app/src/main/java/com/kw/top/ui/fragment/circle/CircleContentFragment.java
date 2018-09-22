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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.adapter.TopCircleAdapter;
import com.kw.top.base.BaseFragment;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.TopCircleBean;
import com.kw.top.bean.event.CircleRefreshEvent;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.ConstantValue;
import com.kw.top.ui.activity.circle.SendCircleActivity;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;
import com.kw.top.view.RecycleViewDivider;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static com.luck.picture.lib.tools.PictureFileUtils.getDiskCacheDir;

/**
 * author: zy
 * data  : 2018/5/27
 * des   : Top圈内容
 */

public class CircleContentFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    private String topType = "01"; //top圈类型('01'.公共圈,'02'.私密圈,'03'.好友圈)
    private int mCurrentPage = 1;
    private TopCircleAdapter mAdapter;
    private List<TopCircleBean> mList = new ArrayList<>();
    private String loadMore = "";
    private String outputCameraPath;
    private Dialog dialog;
    private RxPermissions rxPermissions;
    protected String cameraPath;
    private String recordVideoSecond = 60 + "";//录制秒数

    public static CircleContentFragment fragment;

   /* public static CircleContentFragment onNewInstance(String top_type) {
        CircleContentFragment fragment = new CircleContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("TYPE", top_type);
        fragment.setArguments(bundle);
        return fragment;
    }*/


    public static CircleContentFragment newInstance() {
        if (fragment == null) {
            fragment = new CircleContentFragment();
        }
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public int getContentView() {
        return R.layout.frament_circle_content;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
//        topType = getArguments().getString("TYPE", "01");

        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);

        mAdapter = new TopCircleAdapter(getActivity(), mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), 1, R.color.status_color));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        rxPermissions = new RxPermissions(getActivity());
    }

    @Override
    public void initData() {
        getTopData();
    }


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (loadMore.equals("1")) {
            mCurrentPage++;
            getTopData();
        } else {
            refreshLayout.finishLoadMoreWithNoMoreData();
        }

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mCurrentPage = 1;
        getTopData();
    }

    private void getTopData() {
        Api.getApiService().getTopCircle(topType, mCurrentPage + "", ConstantValue.ONE_PAGE_NUM + "", getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showProgressDialog();
                    }
                })
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        mRefreshLayout.finishRefresh();
                        mRefreshLayout.setNoMoreData(false);
                        mRefreshLayout.finishLoadMore();
                        if (baseBean.isSuccess()) {
                            try {
                                List<TopCircleBean> list = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<List<TopCircleBean>>() {
                                }.getType());
                                if (mCurrentPage == 1) {
                                    mList.clear();
                                }
                                mList.addAll(list);
                                mAdapter.notifyDataSetChanged();
                                loadMore = baseBean.getMsg();
                                if ("0".equals(baseBean.getMsg())) {
                                    mRefreshLayout.finishLoadMoreWithNoMoreData();
                                }
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                        } else if (baseBean.getCode().equals("-44")) {
                            RxToast.normal(getResources().getString(R.string.login_out));
                            SPUtils.clear(getActivity());
                            startActivity(LoginActivity.class);
                            getActivity().finish();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                        mRefreshLayout.finishRefresh();
                        mRefreshLayout.setNoMoreData(false);
                        mRefreshLayout.finishLoadMore();
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshCircle(CircleRefreshEvent event) {
        if (event.isRefresh())
            initData();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }


    @OnClick(R.id.iv_circle)
    public void OnClick() {
        showModPhotosView();
    }

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
                    SendCircleActivity.startActivity(getActivity(), 0, "");
                    break;
                case R.id.select_photo_camera_bt://拍摄
                    outputCameraPath = getDiskCacheDir(getContext()) + "/top/video/"; //录制视频输出路径
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
        if (resultCode == RESULT_OK && requestCode == PictureConfig.REQUEST_CAMERA) {
            SendCircleActivity.startActivity(getContext(), 1, cameraPath);
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

    /**
     * 权限
     */
    public void getPermission() {
        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            startOpenCameraVideo();
                        } else {
                            RxToast.normal(getString(R.string.picture_camera));
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
