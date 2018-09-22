package com.kw.top.ui.activity.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kw.top.R;
import com.kw.top.adapter.UpPicAdapter;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.listener.OnDeleteListener;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.ComResultTools;
import com.kw.top.tools.CommandTools;
import com.kw.top.tools.ConstantValue;
import com.kw.top.tools.QiniuUpLoadManager;
import com.kw.top.ui.activity.person_info.EditInfoActivity;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: Administrator
 * data  : 2018/5/2
 * des   : 登录 -- 上传照片
 */

public class UpPictureActivity extends BaseActivity implements OnDeleteListener, View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_title_right)
    TextView mTvTitleRight;
    @BindView(R.id.relative_title)
    RelativeLayout mRelativeTitle;
    @BindView(R.id.rl_add)
    RelativeLayout mRlAdd;
    @BindView(R.id.recycler_view_uppic)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_confirm_up)
    TextView mTvConfirmUp;
    private int imageCount = 3;
    private List<LocalMedia> mList = new ArrayList<>();
    private List<String> mUrls = new ArrayList<>();

    private UpPicAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    private int count = 0;

    @Override
    public int getContentView() {
        return R.layout.activity_up_picture;
    }

    public void initView() {
        mTvTitle.setText("请上传三张您颜值最高的照片");
        mRelativeTitle.setBackgroundColor(getResources().getColor(R.color.transparent));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new UpPicAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("正在上传...");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == ConstantValue.JUMP_RELEASE_IMAGE) {
            mList = PictureSelector.obtainMultipleResult(data);
            mAdapter.setData(mList);
        }
    }

    @Override
    public void onDelete(View view, int position) {
        mList.remove(position);
        mAdapter.notifyDataSetChanged();
    }


    private void getQiniuToken() {
        Api.getApiService().getUpToken(getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        mProgressDialog.show();
                    }
                })
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        if (baseBean.isSuccess()) {
                            String token = (String) baseBean.getData();
                            Log.e("tag", "=============  token " + token);
                            upPictoQiniu(token);
                        } else {
                            ComResultTools.resultData(UpPictureActivity.this,baseBean);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mProgressDialog.dismiss();
                    }
                });
    }

    private void upPictoQiniu(String token) {
        for (int i = 0; i < mList.size(); i++) {
            String path = mList.get(i).getPath();
            String key = CommandTools.getAndroidId(this) + SystemClock.currentThreadTimeMillis();
            Log.e("tag", "==============  id key " + key);
            QiniuUpLoadManager.getInstance().uploadFile(path, key, token, new UpCompletionHandler() {
                @Override
                public void complete(String key, ResponseInfo info, JSONObject response) {
                    //res包含hash、key等信息，具体字段取决于上传策略的设置
                    if (info.isOK()) {
                        Log.i("qiniu", "===============Upload Success" + response.toString() + "   ###  " + new Gson().toJson(response));
                        ++count;
                        String resKey = "";
                        try {
                            resKey = response.getString("key");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("tag", "================  reskey " + resKey);
                        mUrls.add(resKey);
                        if (count == mList.size()) {
                            upImage(new Gson().toJson(mUrls));
                        }

                    } else {
                        Log.i("qiniu", "===============Upload Fail");
                        //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
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

    }

    //将七牛返回的图片上传到服务器
    private void upImage(String images) {
        Api.getApiService().addPhoto(images, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        mProgressDialog.dismiss();
                        RxToast.normal("图片上传成功");
                        Log.e("tag", "============= 上传成功 " + baseBean.getData().toString());
                        EditInfoActivity.startActivity(UpPictureActivity.this,false);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mProgressDialog.dismiss();
                        RxToast.normal("图片上传失败");
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        setContentView(getContentView());
        mIvBack = findViewById(R.id.iv_back);
        mTvTitle = findViewById(R.id.tv_title);
        mTvTitleRight = findViewById(R.id.tv_title_right);
        mRelativeTitle = findViewById(R.id.relative_title);
        mRlAdd = findViewById(R.id.rl_add);
        mRecyclerView = findViewById(R.id.recycler_view_uppic);
        mTvConfirmUp = findViewById(R.id.tv_confirm_up);
        mRlAdd.setOnClickListener(this);
        mTvConfirmUp.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_add:
                PictureSelector.create(this)
                        .openGallery(PictureConfig.TYPE_IMAGE)
//                        .theme()
                        .maxSelectNum(imageCount)
                        .minSelectNum(1)
                        .imageSpanCount(4)
                        .selectionMode(PictureConfig.MULTIPLE)
                        .previewImage(true)
                        .isCamera(true)
                        .isZoomAnim(true)
                        .selectionMedia(mList)
                        .forResult(ConstantValue.JUMP_RELEASE_IMAGE);

                break;
            case R.id.tv_confirm_up:
                count = 0;
                mUrls.clear();
                if (mList.size() > 0)
                    getQiniuToken();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
