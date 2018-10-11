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
import com.kw.top.bean.GiftBean;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.fragment.find.bean.HomeBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author: zy
 * data  : 2018/6/10
 * des   :
 */

public class SearchAdapter extends BaseAdapter {

    private Context mContext;
    private List<HomeBean> mList = new ArrayList<>();

    public SearchAdapter(Context context) {
        this.mContext = context;
    }

    public void setList(List<HomeBean> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (holder == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_search, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext).load(HttpHost.qiNiu + mList.get(position).getHeadImg()).into(holder.imageView);
        holder.tvName.setText(mList.get(position).getNickName());
        holder.tvJob.setText(mList.get(position).getJob());
        holder.tvLocation.setText(mList.get(position).getCity());
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.search_head)
        CircleImageView imageView;
        @BindView(R.id.search_name)
        TextView tvName;
        @BindView(R.id.search_job)
        TextView tvJob;
        @BindView(R.id.search_locaction)
        TextView tvLocation;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
