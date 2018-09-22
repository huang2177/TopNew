package com.kw.top.ui.activity.active;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.adapter.ActiveDetailsAdapter;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.ActiveBean;
import com.kw.top.bean.ActiveDetailsBean;
import com.kw.top.bean.ActiveUserBean;
import com.kw.top.bean.BaseBean;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.ComResultTools;
import com.kw.top.ui.activity.circle.SendCircleActivity;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.htmltext.MImageGetter;
import com.kw.top.view.ScrollGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author  ： zy
 * date    ： 2018/6/17
 * des     ：
 */

public class ActiveDetailsActivity extends BaseActivity implements ActiveDetailsAdapter.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_title_right)
    TextView mTvTitleRight;
    @BindView(R.id.relative_title)
    RelativeLayout mRelativeTitle;
    @BindView(R.id.tv_active_title)
    TextView mTvActiveTitle;
    @BindView(R.id.grid_view)
    ScrollGridView mGridView;
    @BindView(R.id.tv_desc)
    TextView mTvDesc;
    private String activeId;
    private List<ActiveUserBean> mList = new ArrayList<>();
    private ActiveDetailsAdapter mAdapter;
    private EditText mEditText;
    private ActiveUserBean userBean;
    private int mPosition;
    private String valid = "0";

    public static void startActivity(Context context, String activeId) {
        Intent intent = new Intent(context, ActiveDetailsActivity.class);
        intent.putExtra("ID", activeId);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_active;
    }

    private void initView() {

        mTvTitle.setText("活动详情");
        mTvTitleRight.setText("参加比赛");

        activeId = getIntent().getStringExtra("ID");
        mAdapter = new ActiveDetailsAdapter(this, mList, this, activeId);
        mGridView.setAdapter(mAdapter);

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTvTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid.equals("1"))
                    SendCircleActivity.startActivity(ActiveDetailsActivity.this, activeId);
            }
        });
    }

    private void initData() {
        showProgressDialog();
        Api.getApiService().getActiveDetails(activeId, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            try {
                                ActiveDetailsBean activeDetailsBean = null;
                                activeDetailsBean = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<ActiveDetailsBean>() {
                                }.getType());
                                ActiveBean activeBean = activeDetailsBean.getActivityDes();
                                valid = activeBean.getValid();
                                mTvActiveTitle.setText(activeBean.getActivityName());
                                mList.addAll(activeDetailsBean.getActivityUserList());
                                mTvDesc.setText(Html.fromHtml(activeBean.getDescribes(), new MImageGetter(mTvDesc, ActiveDetailsActivity.this), null));
                                mAdapter.notifyDataSetChanged();
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ComResultTools.resultData(ActiveDetailsActivity.this, baseBean);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });
    }

    @Override
    public void onClick(View view, final int position, String useId) {
        userBean = mList.get(position);
        if (null == userBean)
            return;
        mPosition = position;
        showLikeDialog();
    }

    private void showLikeDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_like, null);
        view.findViewById(R.id.tv_award).setOnClickListener(mOnClickListener);
        mEditText = view.findViewById(R.id.et_num);
        mLikeDialog = new Dialog(ActiveDetailsActivity.this, R.style.charge_dialog_style);
        mLikeDialog.setContentView(view);
        mLikeDialog.setCanceledOnTouchOutside(true);
        Window window = mLikeDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.popupAnimation); // 添加动画
        mLikeDialog.show();
    }

    private Dialog mLikeDialog;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mLikeDialog.dismiss();
            String num = "";
            if (null != mEditText) {
                num = mEditText.getText().toString();
            }
            if (!TextUtils.isEmpty(num)) {
                addLike(num);
            }
        }
    };

    private void addLike(final String thumbsUpNum) {
        showProgressDialog();
        Api.getApiService().activeAward(activeId, userBean.getUserId() + "", thumbsUpNum, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            RxToast.normal("点赞成功");
                            int num = Integer.parseInt(userBean.getThumbsUpNum());
                            num += Integer.parseInt(thumbsUpNum);
                            userBean.setThumbsUpNum((num) + "");
                            mList.set(mPosition, userBean);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            RxToast.normal(baseBean.getMsg() + "");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                        RxToast.normal("点赞失败");
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        initView();
        initData();
    }

}
