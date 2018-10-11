package com.kw.top.ui.activity.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kw.top.R;
import com.kw.top.app.AppManager;
import com.kw.top.app.BaseApplication;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.ComResultTools;
import com.kw.top.tools.CommandTools;
import com.kw.top.tools.ConstantValue;
import com.kw.top.tools.Logger;
import com.kw.top.tools.QiniuUpLoadManager;
import com.kw.top.ui.activity.NewMainActivity;
import com.kw.top.ui.activity.login.presenter.RegisterView;
import com.kw.top.ui.activity.login.presenter.UpSinglePresenter;
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

import java.io.ByteArrayOutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: Administrator
 * data  : 2018/5/2
 * des   : 登录 -- 填写基本信息
 */

public class BaseInfoActivity extends BaseActivity implements View.OnClickListener, RegisterView {

    ImageView mIvBack;
    TextView mTvTitle;
    TextView mTvTitleRight;
    RelativeLayout mRelativeTitle;
    EditText mEtNikename;
    EditText mEtWechat;
    TextView mTvInto;
    @BindView(R.id.ci_head)
    CircleImageView mCiHead;
    private String path;
    private LocalMedia localMedia;
    private String mResKey;
    private String sex;
    private boolean userPrivate;//身份保密
    private byte[] datas = null;
    private UpSinglePresenter presenter;
    private String headImg;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, BaseInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_base_info;
    }

    private void initView() {
        presenter = new UpSinglePresenter(this, this);
        sex = SPUtils.getString(this, ConstantValue.KEY_SEX, "");
        userPrivate = SPUtils.getBoolean(this, ConstantValue.KEY_PRIVATE, false);
        if (userPrivate) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_vip_head);
            mCiHead.setImageResource(R.mipmap.icon_vip_head);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            datas = baos.toByteArray();
        }
    }

    private void initData() {
        mTvTitle.setText("填写基本信息");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        setContentView(getContentView());
        ButterKnife.bind(this);
        mIvBack = findViewById(R.id.iv_back);
        mTvTitle = findViewById(R.id.tv_title);
        mTvTitleRight = findViewById(R.id.tv_title_right);
        mRelativeTitle = findViewById(R.id.relative_title);
        mEtNikename = findViewById(R.id.et_nikename);
        mEtWechat = findViewById(R.id.et_wechat);
        mTvInto = findViewById(R.id.tv_into);
        mRelativeTitle.setBackgroundColor(getResources().getColor(R.color.transparent));
        mIvBack.setOnClickListener(this);
        mTvInto.setOnClickListener(this);
        initView();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_into:
                String nickName = mEtNikename.getText().toString().trim();
                String weChat = mEtWechat.getText().toString().trim();
                if (TextUtils.isEmpty(nickName)) {
                    RxToast.normal("请填写昵称");
                    return;
                }
                if (TextUtils.isEmpty(weChat)) {
                    RxToast.normal("请填写微信号");
                    return;
                }
                if (TextUtils.isEmpty(headImg)) {
                    RxToast.normal("请上传头像");
                    return;
                }
                presenter.manRegister(nickName, weChat, headImg, getToken());
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void showHint() {
        new AlertDialog.Builder(this)
                .setTitle("提示:")
                .setMessage("信息上传成功，请等待审核～")
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppManager.getAppManager().finishAllActivity();
                        BaseInfoActivity.this.finish();
                    }
                }).show();
    }

    @OnClick(R.id.ci_head)
    public void onViewClicked() {
        PictureSelector.create(this)
                .openGallery(PictureConfig.TYPE_IMAGE)
                .maxSelectNum(1)
                .minSelectNum(1)
                .imageSpanCount(4)
                .selectionMode(PictureConfig.MULTIPLE)
                .previewImage(true)
                .isCamera(true)
                .isZoomAnim(true)
                .enableCrop(true)// 是否裁剪 true or false
                .withAspectRatio(1, 1)
                .isDragFrame(true)// 是否可拖动裁剪框(固定)
                .rotateEnabled(false) // 裁剪是否可旋转图片 true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格
                .forResult(ConstantValue.JUMP_RELEASE_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == ConstantValue.JUMP_RELEASE_IMAGE) {
            List<LocalMedia> list = PictureSelector.obtainMultipleResult(data);
            if (list.size() > 0) {
                localMedia = list.get(0);
                path = localMedia.getCutPath();
                Glide.with(BaseInfoActivity.this).load(path).into(mCiHead);
                presenter.getQiniuToken(getToken(), path);

            }
        }
    }


    /**
     * 上传单张图片
     *
     * @param resKey
     */
    @Override
    public void UpPicSuccsee(String resKey) {
        headImg = resKey;
        Logger.e("---resKey---", resKey);
    }

    /**
     * 上传多张图片
     *
     * @param listkey
     */
    @Override
    public void UpMorePicSuccess(List<String> listkey) {

    }

    /**
     * 请求女生资料
     *
     * @param baseBean
     */
    @Override
    public void UpGirlInfoSuccess(BaseBean baseBean) {

    }

    /**
     * 请求男生资料
     *
     * @param baseBean
     */
    @Override
    public void UpSingInfoSuccess(BaseBean baseBean) {
        RxToast.normal("资料完善成功");

        Intent intent = new Intent(this, NewMainActivity.class);
        startActivity(intent);
        finish();
    }
}
