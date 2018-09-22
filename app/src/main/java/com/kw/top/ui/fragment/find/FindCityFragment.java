package com.kw.top.ui.fragment.find;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.adapter.CityActivityAdapter;
import com.kw.top.base.BaseFragment;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.CityBean;
import com.kw.top.bean.CityTagBean;
import com.kw.top.bean.TopListBean;
import com.kw.top.retrofit.Api;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;
import com.kw.top.tools.Logger;
import com.kw.top.ui.RecommendActivity;
import com.kw.top.ui.activity.active.MarvellousActivity;
import com.kw.top.view.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by shibing on 2018/9/5.
 */

public class FindCityFragment extends BaseFragment {


    public static FindCityFragment fragment;
    private CityActivityAdapter activityAdapter;
    @BindView(R.id.city_recyler)
    XListView recyclerView;


    public static FindCityFragment newInstant() {
        if (fragment == null) {
            fragment = new FindCityFragment();
        }
        return fragment;
    }


    @Override
    public int getContentView() {
        return R.layout.fragment_city;


    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        getCity();
    }


    @OnClick({R.id.city_beijin, R.id.city_shanghai, R.id.city_chongqian, R.id.city_tianjin})
    public void OnClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.city_beijin:
                intent = new Intent(getActivity(), RecommendActivity.class);
                intent.putExtra("addsName", "北京市");
                startActivity(intent);
                break;
            case R.id.city_shanghai:
                intent = new Intent(getActivity(), RecommendActivity.class);
                intent.putExtra("addsName", "上海市");
                startActivity(intent);
                break;
            case R.id.city_chongqian:
                intent = new Intent(getActivity(), RecommendActivity.class);
                intent.putExtra("addsName", "重庆市");
                startActivity(intent);
                break;
            case R.id.city_tianjin:
                intent = new Intent(getActivity(), RecommendActivity.class);
                intent.putExtra("addsName", "天津市");
                startActivity(intent);
                break;
        }
    }


    private void getCity() {
        String token = getToken();
        showProgressDialog();
        Api.getApiService().queryCity(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (!baseBean.isSuccess()) {
                            return;
                        }

                        try {
                            HashMap<String, List<CityBean>> citys = parseJson(baseBean.getJsonData());
                            activityAdapter = new CityActivityAdapter(getActivity(), citys);
                            recyclerView.setAdapter(activityAdapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });
    }

    private HashMap<String, List<CityBean>> parseJson(String jsonData) {
        HashMap<String, List<CityBean>> citys = null;
        try {
            if (TextUtils.isEmpty(jsonData)) {
                return citys;
            }
            citys = new HashMap<String, List<CityBean>>(32);
            JSONObject jsonObject = new JSONObject(jsonData);

            // 动态获取key值
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                JSONArray keyJSON = jsonObject.getJSONArray(key);
                List<CityBean> cityBeans = new ArrayList<>();
                for (int i = 0; i < keyJSON.length(); i++) {
                    JSONObject cityJson = keyJSON.getJSONObject(i);
                    String cityName = cityJson.getString("addrName");
                    CityBean cityBean = new CityBean(cityName);
                    cityBeans.add(cityBean);
                }
                citys.put(key, cityBeans);
            }
        } catch (JSONException e) {

        }
        return citys;
    }
}
