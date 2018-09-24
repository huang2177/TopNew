package com.kw.top.ui.fragment.find;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jaeger.library.StatusBarUtil;
import com.kw.top.R;
import com.kw.top.base.BaseActivity_;
import com.kw.top.ui.fragment.find.adapter.GlideImageLoader;
import com.kw.top.utils.StatusUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shibing on 2018/9/24.
 */

public class HomePageDetailsActivity extends BaseActivity_ {


    @BindView(R.id.detalis_banner)
    Banner banner;

    private List<String> listBanner;

    @Override
    public int getContentView() {
        return R.layout.activity_homepage;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        initStatus();
        initBanner();
    }


    /**
     * 状态栏
     */
    private void initStatus() {
        if (true) {
            StatusUtil.setStatusBar(this, false, false);
            StatusUtil.setStatusTextColor(false, this);
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
        banner.setIndicatorGravity(BannerConfig.RIGHT);//设置指示器位置（当banner模式中有指示器时）
        banner.start();//banner设置方法全部调用完毕时最后调用
    }
}
