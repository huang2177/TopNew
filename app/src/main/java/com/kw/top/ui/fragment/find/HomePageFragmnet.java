package com.kw.top.ui.fragment.find;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.base.BaseFragment;
import com.kw.top.bean.BaseBean;
import com.kw.top.retrofit.Api;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.ui.fragment.find.adapter.GlideImageLoader;
import com.kw.top.ui.fragment.find.adapter.HomePageAdapter;
import com.kw.top.ui.fragment.find.baen.HomeBean;
import com.kw.top.utils.OnItemClickListener;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.scwang.smartrefresh.layout.constant.SpinnerStyle.FixedBehind;

/**
 * Created by shibing on 2018/9/24.
 */

public class HomePageFragmnet extends BaseFragment implements OnItemClickListener, OnRefreshListener, OnLoadMoreListener, OnBannerListener {


    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.homepage_recylerview)
    RecyclerView recyclerView;
    @BindView(R.id.homepage_gz)
    TextView tv_gz;
    @BindView(R.id.homepage_zr)
    TextView tv_zr;
    @BindView(R.id.homepage_zx)
    TextView tv_zx;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    private String type;
    private int nowPage = 1, pageNum = 20;

    private List<HomeBean> beanList;

    private List<HomeBean> RefreshList;

    public static HomePageFragmnet pageFragmnet;
    private List<Integer> listBanner;
    private HomePageAdapter adapter;

    public static HomePageFragmnet newInstance() {
        if (pageFragmnet == null) {
            pageFragmnet = new HomePageFragmnet();
        }
        return pageFragmnet;
    }


    @Override
    public int getContentView() {
        return R.layout.fragmnet_homepage;
    }


    @Override
    public void initData() {
        getHonePageData("01", nowPage + "", pageNum + "", getToken());
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        initDefault();
        initBanner();
        initRecyler();
        RefreshLayout();
    }


    private void RefreshLayout() {
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()).setSpinnerStyle(FixedBehind));
        RefreshList = new ArrayList<>();
    }


    /**
     * 默认展示第一个
     */
    private void initDefault() {
        tv_gz.setBackgroundResource(R.drawable.shape_homepage_left);
        tv_zr.setBackgroundResource(R.drawable.shape_gray_homepage);
        tv_zx.setBackgroundResource(R.drawable.shape_homepage_gray_right);

    }


    @OnClick({R.id.homepage_gz, R.id.homepage_zr, R.id.homepage_zx, R.id.home_search})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.homepage_gz:
                type = "01";
                tv_gz.setBackgroundResource(R.drawable.shape_homepage_left);
                tv_zr.setBackgroundResource(R.drawable.shape_gray_homepage);
                tv_zx.setBackgroundResource(R.drawable.shape_homepage_gray_right);
                getHonePageData(type, nowPage + "", pageNum + "", getToken());
                break;
            case R.id.homepage_zr:
                type = "02";
                tv_gz.setBackgroundResource(R.drawable.shape_homepage_gray_left);
                tv_zr.setBackgroundResource(R.drawable.shape_homepage);
                tv_zx.setBackgroundResource(R.drawable.shape_homepage_gray_right);
                getHonePageData(type, nowPage + "", pageNum + "", getToken());
                break;
            case R.id.homepage_zx:
                type = "03";
                tv_gz.setBackgroundResource(R.drawable.shape_homepage_gray_left);
                tv_zr.setBackgroundResource(R.drawable.shape_gray_homepage);
                tv_zx.setBackgroundResource(R.drawable.shape_homepage_right);
                getHonePageData(type, nowPage + "", pageNum + "", getToken());
                break;
            case R.id.home_search:
                //TipOffDialog reportDialog = new TipOffDialog(getActivity());
                //reportDialog.show();
                break;
        }
    }


    private void initBanner() {
        listBanner = Arrays.asList(R.drawable.ic_banner3, R.drawable.ic_banner2);
        banner.setBannerStyle(1);
        banner.setImageLoader(new GlideImageLoader());  //设置图片加载器
        banner.setImages(listBanner);  //设置图片集合
        banner.isAutoPlay(true);  //设置自动轮播，默认为true
        banner.setDelayTime(3000);   //设置轮播时间
        banner.setIndicatorGravity(BannerConfig.CENTER);//设置指示器位置（当banner模式中有指示器时）
        banner.start();//banner设置方法全部调用完毕时最后调用
        banner.setOnBannerListener(this);
    }


    /**
     * recylerview
     */
    private void initRecyler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
    }


    /**
     * 获取首页数据
     */
    private void getHonePageData(String type, String nowPage, String pageNum, String token) {
        showProgressDialog();
        Api.getApiService().getAllUserList(type, nowPage, pageNum, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        SuccessData(baseBean);


                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });

    }


    private void SuccessData(BaseBean baseBean) {
        if (baseBean.isSuccess()) {
            try {
                beanList = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<List<HomeBean>>() {
                }.getType());

                if (nowPage == 1) {
                    RefreshList.clear();
                    RefreshList.addAll(beanList);
                    mRefreshLayout.finishRefresh();
                    adapter = new HomePageAdapter(getActivity(), RefreshList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    RefreshList.addAll(beanList);
                    mRefreshLayout.finishLoadMore();
                    adapter.notifyDataSetChanged();
                }
                adapter.addOnItemOnClick(this);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        } else {
            mRefreshLayout.finishLoadMore();
            mRefreshLayout.finishRefresh();
            RxToast.normal(getResources().getString(R.string.login_out));
            SPUtils.clear(getActivity());
            startActivity(LoginActivity.class);
            getActivity().finish();
        }
    }


    @Override
    public void initListener() {

    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), RankingListActivity.class);
        startActivity(intent);

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        nowPage = 1;
        getHonePageData(type, nowPage + "", pageNum + "", getToken());
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        nowPage++;
        getHonePageData(type, nowPage + "", pageNum + "", getToken());
    }


    /**
     * banner  监听事件
     *
     * @param position
     */
    @Override
    public void OnBannerClick(int position) {
        switch (position) {
            case 0:
                startActivity(RuleActivity.class);
                break;
            case 1:
                startActivity(RankingListActivity.class);
                break;
        }
    }
}
