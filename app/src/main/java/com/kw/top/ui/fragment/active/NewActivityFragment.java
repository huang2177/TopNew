package com.kw.top.ui.fragment.active;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.base.BaseFragment;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.TopListBean;
import com.kw.top.retrofit.Api;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.ConstantValue;
import com.kw.top.tools.GlideTools;
import com.kw.top.tools.Logger;
import com.kw.top.ui.activity.active.MarvellousActivity;
import com.kw.top.ui.activity.active.TopListActivity;
import com.kw.top.ui.activity.club.ClubActivity;
import com.kw.top.ui.activity.find.FindDetailsActivity;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.ui.activity.task.TopTaskActivity;
import com.kw.top.ui.fragment.find.HomePageDetailsActivity;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class NewActivityFragment extends BaseFragment {


    @BindView(R.id.activity_slb_image)
    ImageView imageViewSlb;
    @BindView(R.id.activity_mlb_image)
    ImageView imageViewMlb;
    @BindView(R.id.activity_slb_name)
    TextView tvSlb_name;
    @BindView(R.id.activity_slb_zs)
    TextView tvSlb_zs;
    @BindView(R.id.activity_mlb_name)
    TextView tvMlb_name;
    @BindView(R.id.activity_mlb_zs)
    TextView tvMlb_zs;
    private List<TopListBean> mlblist = null;
    private List<TopListBean> slblist = null;

    public static NewActivityFragment fragment;

    public static NewActivityFragment newInstance() {
        if (fragment == null) {
            fragment = new NewActivityFragment();
        }
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_new_activity;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

    }


    @OnClick({R.id.activity_jlb, R.id.activity_jchd, R.id.activity_rw, R.id.activity_gd, R.id.activity_slb_image, R.id.activity_mlb_image})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.activity_jlb:  //俱乐部列表
                startActivity(ClubActivity.class);
                break;
            case R.id.activity_jchd: //精彩活动
                startActivity(MarvellousActivity.class);
                break;
            case R.id.activity_rw:   //任务
                startActivity(TopTaskActivity.class);
                break;
            case R.id.activity_gd:  // 更多
                startActivity(TopListActivity.class);
                break;
            case R.id.activity_slb_image:  //魅力榜图片
               /* Intent intent = new Intent(getActivity(), HomePageDetailsActivity.class);
                intent.putExtra(ConstantValue.KEY_USER_ID,mlblist.get(0).getUserId());
                getActivity().startActivity(intent);*/
                FindDetailsActivity.startActivity(getContext(), mlblist.get(0).getUserId());
                break;
            case R.id.activity_mlb_image:  //实力榜图片
                /*Intent intent1 = new Intent(getActivity(), HomePageDetailsActivity.class);
                intent1.putExtra(ConstantValue.KEY_USER_ID,slblist.get(0).getUserId());
                getActivity().startActivity(intent1);*/
                FindDetailsActivity.startActivity(getContext(), slblist.get(0).getUserId());
                break;
        }
    }


    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        getsSlbTopList();
        getMlbTopList();

    }


    /**
     * 魅力榜
     */
    private void getMlbTopList() {
        String token = getToken();
        //showProgressDialog();
        Api.getApiService().getTopBeautifulList("01", "1", "1", token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {

                        try {
                            mlblist = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<List<TopListBean>>() {
                            }.getType());

                            Logger.e("----baseBean---", baseBean.getJsonData());
                            Glide.with(getActivity())
                                    .load(HttpHost.qiNiu + mlblist.get(0).getHeadImg())
                                    .apply(GlideTools.getOptions())
                                    .into(imageViewSlb);

                            tvSlb_name.setText(mlblist.get(0).getNickName());
                            tvSlb_zs.setText(mlblist.get(0).getGlamour());

                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }


    /**
     * 实力榜
     */
    private void getsSlbTopList() {
        String token = getToken();
        //showProgressDialog();
        Api.getApiService().getTopBeautifulList("02", "1", "1", token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {

                        if (baseBean.isSuccess()) {

                            try {
                                slblist = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<List<TopListBean>>() {
                                }.getType());

                                Glide.with(getActivity())
                                        .load(HttpHost.qiNiu + slblist.get(0).getHeadImg())
                                        .apply(GlideTools.getOptions())
                                        .into(imageViewMlb);
                                tvMlb_name.setText(slblist.get(0).getNickName());
                                tvMlb_zs.setText(slblist.get(0).getAmount());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else if (baseBean.getCode().equals("-44")) {
                            RxToast.normal(getResources().getString(R.string.login_out));
                            SPUtils.clear(getActivity());
                            startActivity(LoginActivity.class);
                            getActivity().finish();
                        }
                    }

                }, new Action1<Throwable>()

                {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}
