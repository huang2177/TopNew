package com.kw.top.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kw.top.R;
import com.kw.top.bean.CityBean;
import com.kw.top.bean.CityTagBean;
import com.kw.top.ui.RecommendActivity;
import com.kw.top.utils.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shibing on 2018/9/11.
 */

public class CityItemAdapter extends RecyclerView.Adapter<CityItemAdapter.ViewHodler> {


    private Context mContext;
    private List<CityBean> list;

    private OnItemClickListener listener;

    public CityItemAdapter(Context mContext, List<CityBean> list) {

        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.city_item_name, null, false);
        return new ViewHodler(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHodler holder, final int position) {
        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //   listener.onItemClick(position);
                }
            });
        }
        holder.city_name.setText(list.get(position).getmAddrName());
        holder.city_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RecommendActivity.class);
                intent.putExtra("addsName", list.get(position).getmAddrName());
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHodler extends RecyclerView.ViewHolder {
        @BindView(R.id.city_name)
        TextView city_name;

        public ViewHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    /**
     * 添加item监听
     *
     * @param listener
     */
    public void addOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
