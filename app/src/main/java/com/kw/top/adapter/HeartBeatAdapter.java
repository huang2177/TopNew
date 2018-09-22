package com.kw.top.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kw.top.R;
import com.kw.top.bean.AllUserBean;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;

import java.util.ArrayList;
import java.util.Collection;

/**
 * author: zy
 * data  : 2018/5/20
 * des   :
 */

public class HeartBeatAdapter extends BaseAdapter {

    ArrayList<AllUserBean> mList;
    private Context mContext;

    public HeartBeatAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float density = dm.density;
        cardWidth = (int) (dm.widthPixels - (2 * 18 * density));
        cardHeight = (int) (dm.heightPixels - (318 * density));
    }

    private int cardWidth;
    private int cardHeight;

    public void addAll(Collection<AllUserBean> collection) {
        if (isEmpty()) {
            mList.addAll(collection);
            notifyDataSetChanged();
        } else {
            mList.addAll(collection);
        }
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return mList.isEmpty();
    }

    public void remove(int index) {
        if (index > -1 && index < mList.size()) {
            mList.remove(index);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        if (mList == null || mList.size() == 0) return null;
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        AllUserBean allUserBean = (AllUserBean) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_new_item, parent, false);
            holder = new ViewHolder();
            convertView.setTag(holder);
            convertView.getLayoutParams().width = cardWidth;
            holder.tvNickname = (TextView) convertView.findViewById(R.id.tv_nickname);
            holder.tvDes = (TextView) convertView.findViewById(R.id.tv_des);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.ivVip = (ImageView) convertView.findViewById(R.id.iv_vip);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvNickname.setText(allUserBean.getNickName());
        holder.tvDes.setText(allUserBean.getAge()+"岁  "+ allUserBean.getCity()+"  "+ allUserBean.getConstellation());

        GlideTools.setVipResourceS(holder.ivVip,Integer.parseInt(allUserBean.getGrade()));
        Glide.with(mContext)
                .load(HttpHost.qiNiu+ allUserBean.getHeadImg())
                .apply(GlideTools.getOptions())
                .into(holder.image);

        return convertView;
    }

    private static class ViewHolder {
        ImageView image, ivVip, ivVideo;
        TextView tvNickname, tvDes;

    }
}
