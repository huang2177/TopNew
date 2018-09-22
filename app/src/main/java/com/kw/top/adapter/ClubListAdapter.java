package com.kw.top.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kw.top.R;
import com.kw.top.bean.ClubBean;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;
import com.kw.top.tools.Logger;
import com.kw.top.ui.activity.club.ClubDetailsActivity;
import com.kw.top.ui.activity.news.ChatActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author  ： zy
 * date    ： 2018/6/17
 * des     ：
 */

public class ClubListAdapter extends RecyclerView.Adapter<ClubListAdapter.ViewHolder> {

    private Context mContext;
    private List<ClubBean> mList = new ArrayList<>();
    private int type;

    public ClubListAdapter(Context context, List<ClubBean> list, int type) {
        this.mContext = context;
        this.mList = list;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_club_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ClubBean clubBean = mList.get(position);
        holder.mTvClubName.setText(clubBean.getClubName());
        holder.mTvPenpleNum.setText(clubBean.getPeopleNum() + "人");
        Spanned red = Html.fromHtml("每日红包" + "<font color='#DEAC6A'>" + "¥" + clubBean.getDayRedAmount() + "</font>" + "/人");
        holder.mTvRedNum.setText(red);
        Spanned send = Html.fromHtml("已累计送出" + "<font color='#DEAC6A'>" + "¥" + clubBean.getSendRedPacketSum() + "</font>");
        holder.mTvAllAmount.setText(send);
        Glide.with(mContext).load(HttpHost.qiNiu + clubBean.getHeadImg()).apply(GlideTools.getOptions()).into(holder.mCiIcon);

        holder.mRlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 0) {
                    Intent intent = new Intent(mContext, ChatActivity.class);
                    intent.putExtra("userId", clubBean.getGroupid());
                    Logger.e("-----服务器自己的群组id", clubBean.getGroupid());
                 //   intent.putExtra(EaseConstant.EXTRA_TOOLBAR_TITLE, clubBean.getClubName());
                //    intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                    mContext.startActivity(intent);
                } else if (type == 1) {
                    ClubDetailsActivity.startActivity(mContext, clubBean.getGroupid(), clubBean.getClubId() + "");
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mCiIcon;
        TextView mTvClubName;
        TextView mTvRedNum;
        TextView mTvAllAmount;
        TextView mTvPenpleNum;
        RelativeLayout mRlItem;

        public ViewHolder(View itemView) {
            super(itemView);
            mCiIcon = itemView.findViewById(R.id.ci_icon);
            mTvClubName = itemView.findViewById(R.id.tv_club_name);
            mTvRedNum = itemView.findViewById(R.id.tv_red_num);
            mTvAllAmount = itemView.findViewById(R.id.tv_all_amount);
            mTvPenpleNum = itemView.findViewById(R.id.tv_penple_num);
            mRlItem = itemView.findViewById(R.id.rl_item);
        }
    }

}
