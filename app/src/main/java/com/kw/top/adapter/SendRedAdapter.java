package com.kw.top.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kw.top.R;
import com.kw.top.bean.SendRedBean;
import com.kw.top.ui.activity.user_center.RedPacketDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * author  ： zy
 * date    ： 2018/6/16
 * des     ：
 */

public class SendRedAdapter extends RecyclerView.Adapter<SendRedAdapter.ViewHolder> {

    private Context mContext;
    private List<SendRedBean.SendRedPackageListBean> mList = new ArrayList<>();

    public SendRedAdapter(Context context,List<SendRedBean.SendRedPackageListBean> listBeans){
        this.mContext = context;
        this.mList = listBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_send_red, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SendRedBean.SendRedPackageListBean listBean = mList.get(position);
        holder.mTvAmount.setText(listBean.getAmountSum());
        if (Integer.parseInt(listBean.getSurplusShareSum())==0){
            holder.mTvRedState.setText("已枪光");
        }else {
            holder.mTvRedState.setText("剩余"+listBean.getSurplusShareSum()+"个红包未领取");
        }
        holder.mRlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RedPacketDetailsActivity.startActivity(mContext,listBean.getRedPackageId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvAmount;
        TextView mTvRedState;
        RelativeLayout mRlItem;
        public ViewHolder(View itemView) {
            super(itemView);
            mTvAmount = itemView.findViewById(R.id.tv_amount);
            mTvRedState = itemView.findViewById(R.id.tv_red_state);
            mRlItem = itemView.findViewById(R.id.rl_item);
        }
    }
}
