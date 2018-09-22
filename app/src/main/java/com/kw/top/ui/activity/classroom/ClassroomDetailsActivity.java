package com.kw.top.ui.activity.classroom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.adapter.CommentAdapter;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.ClassroomBean;
import com.kw.top.bean.ClassroomDetailsBean;
import com.kw.top.bean.CommentBean;
import com.kw.top.retrofit.Api;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.ComResultTools;
import com.kw.top.tools.ConstantValue;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.activity.PictureActivity;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;
import com.kw.top.utils.TimeUtils;
import com.kw.top.utils.htmltext.LinkMovementMethodExt;
import com.kw.top.utils.htmltext.MImageGetter;
import com.kw.top.utils.htmltext.MessageSpan;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author  ： zy
 * date    ： 2018/6/17
 * des     ：
 */

public class ClassroomDetailsActivity extends BaseActivity {

    ImageView mIvBack;
    TextView mTvTitle;
    TextView mTvTitleRight;
    RelativeLayout mRelativeTitle;
    TextView mTvClassTitle;
    TextView mTvDesc;
    ImageView mIvImage;
    TextView mTvTime;
    @BindView(R.id.et_commnet)
    EditText mEtCommnet;
    @BindView(R.id.iv_send)
    ImageView mIvSend;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.webView)
    WebView mWebView;
    private String classid, retUserid;
    private List<CommentBean> mList = new ArrayList<>();
    private CommentAdapter mAdapter;
    private String commentContent;

    public static void startActivity(Context context, String classId) {
        Intent intent = new Intent(context, ClassroomDetailsActivity.class);
        intent.putExtra("ID", classId);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_classroom_details;
    }

    public void initView() {
        mTvTitle.setText("课堂详情");
        classid = getIntent().getStringExtra("ID");
        if (TextUtils.isEmpty(classid)) {
            RxToast.normal("数据异常,请稍后再试");
            return;
        }
        retUserid = SPUtils.getString(this, ConstantValue.KEY_USER_ID, "");
        mAdapter = new CommentAdapter(this, mList,null,null);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        initWebView();
    }

    public void initData() {
        showProgressDialog();
        Api.getApiService().getClassroomDes(classid, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            ClassroomDetailsBean classroomDetailsBean = null;
                            try {
                                classroomDetailsBean = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<ClassroomDetailsBean>() {
                                }.getType());
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                            if (null == classroomDetailsBean)
                                return;
                            ClassroomBean classroomBean = classroomDetailsBean.getClassroom();
                            mList.addAll(classroomDetailsBean.getCommentList());
                            mAdapter.notifyDataSetChanged();

                            mTvClassTitle.setText(classroomBean.getClassroomName());
                            htmlText(classroomBean.getContent());
                            mTvTime.setText(classroomBean.getCreateTime());
                            if (!TextUtils.isEmpty(classroomBean.getHomePic()))
                                Glide.with(ClassroomDetailsActivity.this).load(HttpHost.qiNiu + classroomBean.getHomePic())
                                        .apply(GlideTools.getOptions()).into(mIvImage);
                        } else {
                            ComResultTools.resultData(ClassroomDetailsActivity.this,baseBean);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });
    }

    private void htmlText(String content) {
        mWebView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
//        mTvDesc.setText(Html.fromHtml(content, new MImageGetter(mTvDesc, ClassroomDetailsActivity.this), null));


//        Handler handler = new Handler() {
//            public void handleMessage(Message msg) {
//                int what = msg.what;
//                if (what == 200) {
//                    MessageSpan ms = (MessageSpan) msg.obj;
//                    Object[] spans = (Object[]) ms.getObj();
//
//                    for (Object span : spans) {
//                        if (span instanceof ImageSpan) {
//                            PictureActivity.startActivity(ClassroomDetailsActivity.this, ((ImageSpan) span).getSource());
//                        }
//                    }
//                }
//            }
//
//            ;
//        };
//        mTvDesc.setMovementMethod(LinkMovementMethodExt.getInstance(handler, ImageSpan.class));
//        mTvDesc.setVisibility(View.VISIBLE);
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
        mTvClassTitle = findViewById(R.id.tv_class_title);
        mTvDesc = findViewById(R.id.tv_desc);
        mIvImage = findViewById(R.id.iv_image);
        mTvTime = findViewById(R.id.tv_time);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
        initData();
    }

    @OnClick(R.id.iv_send)
    public void onViewClicked() {
        commentContent = mEtCommnet.getText().toString().trim();
        if (TextUtils.isEmpty(commentContent)) {
            RxToast.normal("请输入评论内容");
        } else {
            sendComment();
        }
    }

    private void sendComment() {
        showProgressDialog();
        Api.getApiService().addClassComment(classid, retUserid, commentContent, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            mEtCommnet.setText("");
                            CommentBean commentBean = new CommentBean();
                            commentBean.setComTime(TimeUtils.getTime(System.currentTimeMillis(), "yyyy年MM月dd日 HH:mm"));
                            commentBean.setComContent(commentContent);
                            commentBean.setComHeadImg(SPUtils.getString(ClassroomDetailsActivity.this, ConstantValue.KEY_HEAD, ""));
                            commentBean.setComNickName(SPUtils.getString(ClassroomDetailsActivity.this, ConstantValue.KEY_NAME, ""));
                            mList.add(commentBean);
                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.smoothScrollToPosition(mList.size() - 1);
                            RxToast.normal("评论成功");
                        } else if (baseBean.getCode().equals("-44")) {
                            RxToast.normal(getResources().getString(R.string.login_out));
                            SPUtils.clear(ClassroomDetailsActivity.this);
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

    private void initWebView() {
        WebSettings webSettings = mWebView.getSettings();
        // 支持 Js 使用
        webSettings.setJavaScriptEnabled(true);
        // 开启DOM缓存
        webSettings.setDomStorageEnabled(true);
        // 开启数据库缓存
        webSettings.setDatabaseEnabled(true);
        // 支持自动加载图片
//        webSettings.setLoadsImagesAutomatically(hasKitkat());
        // 设置 WebView 的缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 支持启用缓存模式
        webSettings.setAppCacheEnabled(true);
        // 设置 AppCache 最大缓存值(现在官方已经不提倡使用，已废弃)
        webSettings.setAppCacheMaxSize(8 * 1024 * 1024);
        // Android 私有缓存存储，如果你不调用setAppCachePath方法，WebView将不会产生这个目录
        webSettings.setAppCachePath(getCacheDir().getAbsolutePath());
//         数据库路径
//        if (!hasKitkat()) {
//            webSettings.setDatabasePath(getDatabasePath("html").getPath());
//        }
//         关闭密码保存提醒功能
        webSettings.setSavePassword(false);
        // 支持缩放
        webSettings.setSupportZoom(true);
        // 设置 UserAgent 属性
        webSettings.setUserAgentString("");
        // 允许加载本地 html 文件/false
        webSettings.setAllowFileAccess(true);
        // 允许通过 file url 加载的 Javascript 读取其他的本地文件,Android 4.1 之前默认是true，在 Android 4.1 及以后默认是false,也就是禁止
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowFileAccessFromFileURLs(false);
        }
        // 允许通过 file url 加载的 Javascript 可以访问其他的源，包括其他的文件和 http，https 等其他的源，
        // Android 4.1 之前默认是true，在 Android 4.1 及以后默认是false,也就是禁止
        // 如果此设置是允许，则 setAllowFileAccessFromFileURLs 不起做用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowUniversalAccessFromFileURLs(false);
        }

        //显示不全
        webSettings.setUseWideViewPort(true);//适应分辨率
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.setBuiltInZoomControls(true); // 启用内置缩放装置
        webSettings.setLoadsImagesAutomatically(true); // 支持自动加载图片
        //自动播放音乐
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webSettings.setMediaPlaybackRequiresUserGesture(false);
        }
//        mWebView.loadUrl("file:///android_asset/page/index.html");//加载asset文件夹下html
//        webView.loadUrl("http://139.196.35.30:8080/OkHttpTest/apppackage/test.html");//加载url
//
//        webView.loadUrl("file:///android_asset/test.html");//加载asset文件夹下html
//
//        方式3：加载手机sdcard上的html页面
//        webView.loadUrl("content://com.ansen.webview/sdcard/test.html");
    }

}
