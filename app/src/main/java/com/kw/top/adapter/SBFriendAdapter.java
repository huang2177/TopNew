package com.kw.top.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kw.top.R;
import com.kw.top.bean.SBFriendTagBean;
import com.kw.top.bean.SBFriendsBean;
import com.kw.top.listener.OnClickListener;
import com.kw.top.listener.OnDeleteListener;
import com.kw.top.listener.OnFriendClickListener;
import com.kw.top.listener.OnFriendDeleteListener;
import com.kw.top.view.RecycleViewDivider;

import java.util.List;

/**
 * author  ： zy
 * date    ： 2018/8/18
 * des     ：
 */

public class SBFriendAdapter extends BaseAdapter {

    private Context mContext;
    private List<SBFriendTagBean> mList;
    private OnFriendClickListener mOnClickListener;
    private OnFriendDeleteListener mOnDeleteListener;

    public SBFriendAdapter(Context context, List<SBFriendTagBean> list, OnFriendClickListener onClickListener, OnFriendDeleteListener onDeleteListener) {
        this.mContext = context;
        this.mList = list;
        this.mOnClickListener = onClickListener;
        this.mOnDeleteListener = onDeleteListener;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sbfriend, null);
            holder.tvTag = convertView.findViewById(R.id.tv_tag);
            holder.mRecyclerView = convertView.findViewById(R.id.recycler_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvTag.setText(mList.get(position).getTag());
        FriendListAdapter adapter = new FriendListAdapter(mContext, mList.get(position).getList(), mOnClickListener, mOnDeleteListener);
        holder.mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //holder.mRecyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, R.color.white));
        holder.mRecyclerView.setAdapter(adapter);
        return convertView;
    }

    class ViewHolder {
        TextView tvTag;
        RecyclerView mRecyclerView;
    }

}
