package com.kw.top.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kw.top.R;
import com.kw.top.bean.ActiveUserBean;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.activity.active.ActiveDetailsActivity;
import com.kw.top.ui.activity.active.UserActiveDetailsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * author  ： zy
 * date    ： 2018/6/17
 * des     ：
 */

public class ActiveDetailsAdapter extends BaseAdapter {

    private Context mContext;
    private List<ActiveUserBean> mList = new ArrayList<>();
    private OnClickListener mOnClickListener;
    private String activeId;

    public interface OnClickListener {
        void onClick(View view, int position, String useId);
    }

    public ActiveDetailsAdapter(Context context, List<ActiveUserBean> list,OnClickListener clickListener,String activeId) {
        this.mContext = context;
        this.mList = list;
        this.mOnClickListener = clickListener;
        this.activeId = activeId;
    }

    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int poisition, View concertView, ViewGroup parent) {
        final ActiveUserBean userBean = mList.get(poisition);
        ViewHolder holder = null;
        if (concertView == null) {
            holder = new ViewHolder();
            concertView = LayoutInflater.from(mContext).inflate(R.layout.item_active_details, parent, false);
            holder.mTvRank = concertView.findViewById(R.id.tv_rank);
            holder.mIvCrown = concertView.findViewById(R.id.iv_crown);
            holder.mIvImage = concertView.findViewById(R.id.iv_image);
            holder.mTvAdmire = concertView.findViewById(R.id.tv_admire);
            holder.mTvAdmireNum = concertView.findViewById(R.id.tv_admire_num);
            concertView.setTag(holder);
        } else {
            holder = (ViewHolder) concertView.getTag();
        }
        if (poisition == 0) {
            holder.mIvCrown.setVisibility(View.VISIBLE);
            holder.mIvCrown.setImageResource(R.mipmap.icon_crown1);
            holder.mTvRank.setVisibility(View.GONE);
        } else if (poisition == 1) {
            holder.mIvCrown.setVisibility(View.VISIBLE);
            holder.mIvCrown.setImageResource(R.mipmap.icon_crown2);
            holder.mTvRank.setVisibility(View.GONE);
        } else if (poisition == 2) {
            holder.mIvCrown.setVisibility(View.VISIBLE);
            holder.mIvCrown.setImageResource(R.mipmap.icon_crown3);
            holder.mTvRank.setVisibility(View.GONE);
        } else {
            holder.mIvCrown.setVisibility(View.GONE);
            holder.mTvRank.setVisibility(View.VISIBLE);
            holder.mTvRank.setText((poisition + 1) + "");
        }
        holder.mTvAdmireNum.setText(userBean.getThumbsUpNum() + " 赞");
        Glide.with(mContext).load(HttpHost.qiNiu + userBean.getHeadImg()).apply(GlideTools.getOptions()).into(holder.mIvImage);
        holder.mTvAdmire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickListener.onClick(view, poisition,userBean.getUserId()+"");
            }
        });
        holder.mIvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserActiveDetailsActivity.startActivity(mContext,userBean.getUserId()+"",activeId);
            }
        });
        return concertView;
    }


    class ViewHolder {
        TextView mTvRank;
        ImageView mIvCrown;
        ImageView mIvImage;
        TextView mTvAdmire;
        TextView mTvAdmireNum;
    }
}
