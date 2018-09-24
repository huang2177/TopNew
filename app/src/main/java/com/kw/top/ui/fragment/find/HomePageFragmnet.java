package com.kw.top.ui.fragment.find;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.base.BaseFragment;
import com.kw.top.bean.AllUserBean;
import com.kw.top.bean.BaseBean;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.ConstantValue;
import com.kw.top.tools.Logger;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.ui.fragment.find.adapter.GlideImageLoader;
import com.kw.top.ui.fragment.find.adapter.HomePageAdapter;
import com.kw.top.ui.fragment.find.baen.HomeBean;
import com.kw.top.utils.OnItemClickListener;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by shibing on 2018/9/24.
 */

public class HomePageFragmnet extends BaseFragment implements OnItemClickListener {


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

    private String type = "01";

    private List<HomeBean> beanList;

    public static HomePageFragmnet pageFragmnet;
    private List<String> listBanner;
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
        getHonePageData(type, "1", "100", getToken());
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        initDefault();
        initBanner();
        initRecyler();
    }


    /**
     * 默认展示第一个
     */
    private void initDefault() {
        tv_gz.setBackgroundResource(R.drawable.shape_homepage_left);
        tv_zr.setBackgroundResource(R.drawable.shape_gray_homepage);
        tv_zx.setBackgroundResource(R.drawable.shape_homepage_gray_right);
    }


    @OnClick({R.id.homepage_gz, R.id.homepage_zr, R.id.homepage_zx})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.homepage_gz:
                type = "01";
                tv_gz.setBackgroundResource(R.drawable.shape_homepage_left);
                tv_zr.setBackgroundResource(R.drawable.shape_gray_homepage);
                tv_zx.setBackgroundResource(R.drawable.shape_homepage_gray_right);
                getHonePageData(type, "1", "100", getToken());
                break;
            case R.id.homepage_zr:
                type = "02";
                tv_gz.setBackgroundResource(R.drawable.shape_homepage_gray_left);
                tv_zr.setBackgroundResource(R.drawable.shape_homepage);
                tv_zx.setBackgroundResource(R.drawable.shape_homepage_gray_right);
                getHonePageData(type, "1", "100", getToken());
                break;
            case R.id.homepage_zx:
                type = "03";
                tv_gz.setBackgroundResource(R.drawable.shape_homepage_gray_left);
                tv_zr.setBackgroundResource(R.drawable.shape_gray_homepage);
                tv_zx.setBackgroundResource(R.drawable.shape_homepage_right);
                getHonePageData(type, "1", "100", getToken());
                break;
        }
    }


    private void initBanner() {
        listBanner = Arrays.asList("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1537798939698&di=4dd961771014cef12cd6905f2d810623&imgtype=0&src=http%3A%2F%2Fhiphotos.baidu.com%2Fimage%2F%2577%3D%2537%2533%2530%3B%2563%2572%256F%2570%3D%2530%2C%2532%2538%2C%2537%2533%2530%2C%2534%2530%2535%2Fsign%3D1cd79e93a164034f0fcdc0059ff81a43%2Fd000baa1cd11728b103cdc6cc2fcc3cec3fd2c6e.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1537798939697&di=2c9b26acf60d5f147b505cdd859fc9e1&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D924562cf9b2397ddc274904731ebd8c2%2Fa044ad345982b2b727df62ec3badcbef76099b52.jpg");
        banner.setBannerStyle(1);
        banner.setImageLoader(new GlideImageLoader());  //设置图片加载器
        banner.setImages(listBanner);  //设置图片集合
        banner.isAutoPlay(true);  //设置自动轮播，默认为true
        banner.setDelayTime(3000);   //设置轮播时间
        banner.setIndicatorGravity(BannerConfig.CENTER);//设置指示器位置（当banner模式中有指示器时）
        banner.start();//banner设置方法全部调用完毕时最后调用
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
                adapter = new HomePageAdapter(getActivity(), beanList);
                recyclerView.setAdapter(adapter);
                adapter.addOnItemOnClick(this);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        } else {
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
        Intent intent = new Intent(getActivity(), HomePageDetailsActivity.class);
        startActivity(intent);
    }
}
