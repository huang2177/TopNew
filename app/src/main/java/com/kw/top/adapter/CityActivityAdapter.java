package com.kw.top.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kw.top.R;
import com.kw.top.bean.CityBean;
import com.kw.top.bean.CityTagBean;
import com.kw.top.tools.Logger;
import com.kw.top.ui.RecommendActivity;
import com.kw.top.utils.OnItemClickListener;
import com.kw.top.view.RecycleViewDivider;
import com.kw.top.view.XListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shibing on 2018/9/11.
 */

public class CityActivityAdapter extends BaseAdapter {


    private Context mContext;
    private HashMap<String, List<CityBean>> list;
    private List<String> keys;

    List<CityBean> listAddsName;

    public CityActivityAdapter(Context mContext, HashMap<String, List<CityBean>> list) {
        this.list = list;
        this.mContext = mContext;
        keys = new ArrayList<>();
        keys.addAll(list.keySet());

    }

    /*@NonNull
    @Override
    public ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.city_item, null, false);
        return new ViewHodler(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHodler holder, int position) {
        String key = keys.get(position);
        holder.item_city.setText(key);
        // holder.item_recyler.setLayoutManager(new LinearLayoutManager(mContext));
        CityItemAdapter adapter = new CityItemAdapter(mContext, list.get(key));
        holder.item_recyler.setAdapter(adapter);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }*/

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.city_item, null, false);
            viewHodler = new ViewHodler(convertView);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }

        String key = keys.get(position);
        viewHodler.item_city.setText(key);
        viewHodler.item_recyler.setLayoutManager(new LinearLayoutManager(mContext));
        //viewHodler.item_recyler.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, R.color.status_color));
        CityItemAdapter adapter = new CityItemAdapter(mContext, list.get(key));
        viewHodler.item_recyler.setAdapter(adapter);

        listAddsName = list.get(key);

        Logger.e("------list", listAddsName.size() + "");
        adapter.addOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(mContext, RecommendActivity.class);
                intent.putExtra("addsName", listAddsName.get(position).getmAddrName());
                mContext.startActivity(intent);
            }
        });


        return convertView;
    }


    public class ViewHodler {
        @BindView(R.id.city_tag)
        TextView item_city;
        @BindView(R.id.city_item_recyler)
        RecyclerView item_recyler;

        public ViewHodler(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }


}
