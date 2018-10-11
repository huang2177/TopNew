package com.kw.top.ui.fragment.find;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.base.BaseFragment;
import com.kw.top.bean.BaseBean;
import com.kw.top.retrofit.Api;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.ConstantValue;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.ui.fragment.find.adapter.RanKingAdapter;
import com.kw.top.ui.fragment.find.bean.RankingBean;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;
import com.kw.top.view.XListView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class VIPFragmnet extends BaseFragment implements AdapterView.OnItemClickListener {


    @BindView(R.id.vip_listview)
    XListView listView;
    @BindView(R.id.ranking_vip_head)
    CircleImageView imageView;
    @BindView(R.id.ranking_vip_name)
    TextView tvName;
    @BindView(R.id.ranking_vip_num)
    TextView tvNum;
    private RanKingAdapter ranKingAdapter;


    private List<RankingBean> rankingBeanList;

    @Override
    public int getContentView() {
        return R.layout.fragmnet_vip;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        getRankingList();
    }


    /**
     * 获取首页数据
     */
    private void getRankingList() {
        showProgressDialog();
        Api.getApiService().getRankingList("02", "1", "10  ", getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        SuccessData(baseBean);


                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });

    }


    private void SuccessData(BaseBean baseBean) {
        if (baseBean.isSuccess()) {
            rankingBeanList = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<List<RankingBean>>() {
            }.getType());
            if (rankingBeanList.size() == 0) {
                return;
            }
            Glide.with(getActivity())
                    .asBitmap()
                    .load(HttpHost.qiNiu + rankingBeanList.get(0).getHeadImg())
                    .apply(GlideTools.getOptions())
                    .into(imageView);
            tvName.setText(rankingBeanList.get(0).getNickName());
            tvNum.setText(rankingBeanList.get(0).getGiftAmountSum());
            ranKingAdapter = new RanKingAdapter(getActivity());
            listView.setAdapter(ranKingAdapter);
            ranKingAdapter.setData(rankingBeanList);
            listView.setOnItemClickListener(this);
        } else {

            RxToast.normal(getResources().getString(R.string.login_out));
            SPUtils.clear(getActivity());
            startActivity(LoginActivity.class);
            getActivity().finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), HomePageDetailsActivity.class);
        intent.putExtra(ConstantValue.KEY_USER_ID, rankingBeanList.get(position + 1).getUserId());
        startActivity(intent);
    }

    @OnClick(R.id.ranking_vip_head)
    public void OnClick() {
        Intent intent = new Intent(getActivity(), HomePageDetailsActivity.class);
        intent.putExtra(ConstantValue.KEY_USER_ID, rankingBeanList.get(0).getUserId());
        startActivity(intent);
    }
}
