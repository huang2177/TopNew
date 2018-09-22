package com.kw.top.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kw.top.R;
import com.kw.top.bean.ImageBean;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.activity.PictureActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * author  ： zy
 * date    ： 2018/7/4
 * des     ：
 */

public class CirclePagerAdapter extends PagerAdapter {

    private Activity mContext;
    private List<ImageBean> mList = new ArrayList<>();

    public CirclePagerAdapter(Activity context,List<ImageBean> list){
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = View.inflate(mContext, R.layout.item_guide,null);
        ImageView imageView = view.findViewById(R.id.iv_image);
        Glide.with(mContext).asBitmap().load(HttpHost.qiNiu + mList.get(position).getDynamicPic())
                .apply(GlideTools.getOptions())
                .into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureActivity.startActivity(mContext,mList.get(position).getDynamicPic());
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
