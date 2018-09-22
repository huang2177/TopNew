package com.kw.top.ui.activity.circle;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kw.top.R;
import com.kw.top.adapter.GridViewAdapter;
import com.kw.top.base.MVPBaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.listener.OnDeleteListener;
import com.kw.top.listener.OnItemClickListener;
import com.kw.top.tools.ComResultTools;
import com.kw.top.tools.ConstantValue;
import com.kw.top.ui.activity.AppVideoPlayActivity;
import com.kw.top.utils.RxToast;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author: zy
 * data  : 2018/5/27
 * des   : 发布世界圈
 */

public class SendCircleActivity extends MVPBaseActivity<SendCircleContract.View, SendCirclePresenter> implements SendCircleContract.View, OnDeleteListener, OnItemClickListener, View.OnClickListener {

    TextView mTvCancelSend;
    TextView mTvTitle;
    TextView mTvSendCircle;
    EditText mEtContent;
    GridView mGridView;
    ImageView mIvVideo;
    ImageView mIvVideoPlay;
    RelativeLayout mRlVideo;
    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;
    private int type = -1;//0图片 1视频
    private String path;
    private GridViewAdapter mAdapter;
    private List<LocalMedia> mList = new ArrayList<>();
    private List<String> paths = new ArrayList<>();
    private int selectNum = 9;
    private String dynamicType = "01";
    private String taskId;
    private String taskState;//01 图片任务 01视频任务
    private int PIC_TYPE = PictureConfig.TYPE_ALL;
    private String activiId;
    private boolean isGroupTask; // 是否是俱乐部任务

    private String hintMsg = "取消此次发布世界圈?";


    public static void startActivity(Context context, int type, String path) {
        Intent intent = new Intent(context, SendCircleActivity.class);
        intent.putExtra("TYPE", type);
        intent.putExtra("PATH", path);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, int type, String path, String taskid, String taskState) {
        Intent intent = new Intent(context, SendCircleActivity.class);
        intent.putExtra("TYPE", type);
        intent.putExtra("PATH", path);
        intent.putExtra("ID", taskid);
        intent.putExtra("STATE", taskState);
        context.startActivity(intent);
    }

    public static void startActivity(Context context,String taskid, String taskState,boolean groupTask) {
        Intent intent = new Intent(context, SendCircleActivity.class);
        intent.putExtra("ID", taskid);
        intent.putExtra("STATE", taskState);
        intent.putExtra("GROUP_TASK",groupTask);
        context.startActivity(intent);
    }

    public static void startActivity(Context context,String activeId){
        Intent intent = new Intent(context, SendCircleActivity.class);
        intent.putExtra("activeId", activeId);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_publish_circle;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        mTvCancelSend = findViewById(R.id.tv_cancel_send);
        mTvTitle = findViewById(R.id.tv_title);
        mTvSendCircle = findViewById(R.id.tv_send_circle);
        mEtContent = findViewById(R.id.et_content);
        mGridView = findViewById(R.id.grid_view);
        mIvVideo = findViewById(R.id.iv_video);
        mIvVideoPlay = findViewById(R.id.iv_video_play);
        mRlVideo = findViewById(R.id.rl_video);
        mTvCancelSend.setOnClickListener(this);
        mTvSendCircle.setOnClickListener(this);
        mRlVideo.setOnClickListener(this);
        initView();
    }

    public void initView() {
        type = getIntent().getIntExtra("TYPE", 0);
        taskId = getIntent().getStringExtra("ID");
        taskState = getIntent().getStringExtra("STATE");
        activiId = getIntent().getStringExtra("activeId");
        isGroupTask = getIntent().getBooleanExtra("GROUP_TASK",false);

        if (!TextUtils.isEmpty(activiId)){
            mTvTitle.setText("参加活动");
            hintMsg = "放弃参加活动？";
            mRadioGroup.setVisibility(View.GONE);
            mEtContent.setVisibility(View.GONE);
        }

        if (type == 0) {//发图片
        } else {
            //发视频
            path = getIntent().getStringExtra("PATH");
            paths.add(path);
            if (TextUtils.isEmpty(path)) {
                RxToast.normal("视频路径丢失");
            } else {
                mGridView.setVisibility(View.GONE);
                mRlVideo.setVisibility(View.VISIBLE);
                Glide.with(this).load(path).into(mIvVideo);
            }
        }
        if (!TextUtils.isEmpty(taskId)) {
            hintMsg = "放弃任务？";
            if (taskState.equals("01")) {
                mTvTitle.setText("图片任务");
                PIC_TYPE = PictureConfig.TYPE_PICTURE;
                if (isGroupTask){
                    selectNum = 9;
                }else {
                    selectNum = 1;
                }
            } else {
                mTvTitle.setText("视频任务");
                PIC_TYPE = PictureConfig.TYPE_VIDEO;
                selectNum = 1;
            }
            mTvSendCircle.setText("完成");
            mRadioGroup.setVisibility(View.GONE);
        }
        mAdapter = new GridViewAdapter(this, mList, this, this);
        mGridView.setAdapter(mAdapter);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_public:
                        dynamicType = "01";
                        break;
                    case R.id.rb_private:
                        dynamicType = "02";
                        break;
                    case R.id.rb_friend:
                        dynamicType = "03";
                        break;
                }
            }
        });
    }

    @Override
    public void onDelete(View view, int position) {
        mList.remove(position);
        paths.clear();
        for (LocalMedia media : mList) {
            paths.add(media.getPath());
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == ConstantValue.JUMP_RELEASE_IMAGE) {
            mList.addAll(PictureSelector.obtainMultipleResult(data));
            mAdapter.setData(mList);
            paths.clear();
            for (LocalMedia media : mList) {
                paths.add(media.getPath());
                if (media.getPictureType().contains("video")) {
                    PIC_TYPE = PictureConfig.TYPE_VIDEO;
                    type = 1;
                } else {
                    PIC_TYPE = PictureConfig.TYPE_IMAGE;
                    type = 0;
                }
            }
        }
    }

    @Override
    public void upFiledResult() {
        hideProgressDialog();
        RxToast.normal("上传失败");
    }

    @Override
    public void upSuccessResult() {
        hideProgressDialog();
        finish();
    }

    @Override
    public void finishTaskResult(BaseBean baseBean) {
        hideProgressDialog();
        if (baseBean == null){
            RxToast.normal("上传失败");
            return;
        }
        if ( baseBean.isSuccess()) {
            RxToast.normal("完成任务");
        } else {
            ComResultTools.resultData(SendCircleActivity.this,baseBean);
        }
        finish();
    }

    @Override
    public void addActiveResult(BaseBean baseBean) {
        hideProgressDialog();
        if (baseBean == null){
            RxToast.normal("上传失败");
            return;
        }
        if ( baseBean.isSuccess()) {
            RxToast.normal("参加成功");
        } else {
            ComResultTools.resultData(SendCircleActivity.this,baseBean);
        }
        finish();
    }

    @Override
    public void onItemClick(View view, int position) {
        if (mList.size() == 0)
            PIC_TYPE = PictureConfig.TYPE_ALL;
        selectNum = 9 - mList.size();
        PictureSelector.create(this)
                .openGallery(PIC_TYPE)
//                        .theme()
                .maxSelectNum(selectNum)
                .minSelectNum(1)
                .imageSpanCount(4)
                .selectionMode(PictureConfig.MULTIPLE)
                .previewVideo(true)
                .previewImage(true)
                .isCamera(true)
                .isZoomAnim(true)
//                .selectionMedia(mList)
                .forResult(ConstantValue.JUMP_RELEASE_IMAGE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel_send:
                if (paths.size() > 0) {
                    new AlertDialog.Builder(this)
                            .setMessage(hintMsg)
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    SendCircleActivity.this.finish();
                                }
                            })
                            .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .create()
                            .show();
                } else {
                    finish();
                }
                break;
            case R.id.tv_send_circle:
                if (paths.size() == 0) {
                    RxToast.normal("请选择照片");
                } else {
                    if (type == 0) {
                        showProgressDialog("上传图片中...");
                    } else {
                        if (paths.size()>1){
                            RxToast.normal("一次只能上传一个视频哦");
                            return;
                        }
                        showProgressDialog("上传视频中...");
                    }
                    mPresenter.upFile(getToken(), paths, mEtContent.getText().toString().trim(), dynamicType, type, taskId,activiId,isGroupTask);
                }
                break;
            case R.id.rl_video:
                AppVideoPlayActivity.startActivity(this, path);
                break;
        }
    }
}
