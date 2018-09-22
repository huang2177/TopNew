package com.kw.top.ui.fragment.task;

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
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.adapter.MyTaskAdapter;
import com.kw.top.base.BaseFragment;
import com.kw.top.base.FriendBean;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.TaskBean;
import com.kw.top.listener.OnClickListener;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.ComResultTools;
import com.kw.top.ui.activity.circle.SendCircleActivity;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.ui.activity.task.TaskDetailsActivity;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/**
 * author: zy
 * data  : 2018/5/27
 * des   :
 */

public class TaskContentFragment extends BaseFragment implements OnRefreshListener, OnClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    private int type = 0; // 0任务大厅 1我的任务
    private MyTaskAdapter mAdapter;
    private List<TaskBean> mList = new ArrayList<>();
    protected String cameraPath;
    private String outputCameraPath;
    private String recordVideoSecond = 60 + "";//录制秒数
    private String videoQuality = "1"; //录制视频的质量 0/1
    private RxPermissions rxPermissions;
    private String taskId;


    public static TaskContentFragment newInstance(int type) {
        TaskContentFragment fragment = new TaskContentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("TYPE", type);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public int getContentView() {
        return R.layout.fragment_task_content;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        type = getArguments().getInt("TYPE", 0);
        if (type == 0) {
            getTaskList();
        } else if (type == 1) {
            getMyTaskList();
        } else {
            return;
        }
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setEnableLoadMore(false);

        mAdapter = new MyTaskAdapter(getContext(), mList, type, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void initListener() {
        rxPermissions = new RxPermissions(getActivity());
    }

    @Override
    public void initData() {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (type == 0) {
            getTaskList();
        } else if (type == 1) {
            getMyTaskList();
        } else {
            return;
        }
    }

    //我的任务
    private void getMyTaskList() {
        Api.getApiService().getMyTaskList(getToken())
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
                        if (baseBean.isSuccess()) {
                            try {
                                List<TaskBean> list =  new Gson().fromJson(baseBean.getJsonData(), new TypeToken<List<TaskBean>>() {
                                }.getType());
                                mList.clear();
                                mList.addAll(list);
                                mAdapter.notifyDataSetChanged();
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                        }else {
                            ComResultTools.resultData(getContext(),baseBean);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                        mRefreshLayout.finishRefresh();
                    }
                });
    }

    //任务大厅
    private void getTaskList() {
        Api.getApiService().getTaskList(getToken())
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
                        if (baseBean.isSuccess()) {
                            try {
                                List<TaskBean> list =  new Gson().fromJson(baseBean.getJsonData(), new TypeToken<List<TaskBean>>() {
                                }.getType());
                                mList.clear();
                                mList.addAll(list);
                                mAdapter.notifyDataSetChanged();
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                        }else {
                            ComResultTools.resultData(getContext(),baseBean);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                        mRefreshLayout.finishRefresh();
                    }
                });
    }

    @Override
    public void onClick(View view, int position) {
        TaskBean taskBean = mList.get(position);
        taskId = taskBean.getTaskId() + "";
        if (TextUtils.isEmpty(taskId)){
            RxToast.normal("数据异常,轻稍后再试");
            return;
        }
        switch (view.getId()) {
            case R.id.tv_complete:
                //去完成
                if (taskBean.getValid().equals("1")) {
                    if (taskBean.getType().equals("01")){
                        //图片任务
                        SendCircleActivity.startActivity(getContext(),0,"",taskId,taskBean.getType());
                    }else {
                        //视频任务
                        showModPhotosView();
                    }

                } else {
                    RxToast.normal("任务已经完成了哦～");
                }

                break;
            case R.id.rl_task_item:
                //item
                TaskDetailsActivity.startActivity(getContext(), taskBean.getTaskId() + "",type);
                break;
            case R.id.tv_task_details:
                //查看详情
                TaskDetailsActivity.startActivity(getContext(), taskBean.getTaskId() + "",type);
                break;
        }
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
                    SendCircleActivity.startActivity(getActivity(), 0, "", taskId,"02");
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
            SendCircleActivity.startActivity(getContext(), 1, cameraPath, taskId,"02");
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
