package com.kw.top.ui.fragment.find;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.kw.top.R;
import com.kw.top.adapter.SearchAdapter;
import com.kw.top.base.BaseActivity_;
import com.kw.top.bean.BaseBean;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.ConstantValue;
import com.kw.top.ui.fragment.find.bean.HomeBean;
import com.kw.top.utils.RxToast;
import com.kw.top.view.XListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class SearchActivity extends BaseActivity_ implements TextView.OnEditorActionListener, AdapterView.OnItemClickListener {


    @BindView(R.id.ed_search)
    EditText edSearch;
    @BindView(R.id.search_listview)
    XListView listView;
    private SearchAdapter searchAdapter;
    private String searchContent;
    private List<HomeBean> homeBeanList;

    @Override
    public int getContentView() {
        return R.layout.activity_search;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        initviews();
    }

    private void initviews() {
        edSearch.setOnEditorActionListener(this);
        //getHonePageData("02", searchContent, "1", "50", getToken());
    }


    @OnClick(R.id.tv_canle)
    public void OnClick() {
        finish();
    }

    /**
     * 搜索按钮
     *
     * @param v
     * @param actionId
     * @param event
     * @return
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        searchContent = edSearch.getText().toString();
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (TextUtils.isEmpty(searchContent)) {
                RxToast.normal("请输入搜索内容");
                return false;
            }
            getHonePageData("02", searchContent, "1", "50", getToken());
            return false;
        }
        return false;
    }


    /**
     * 搜索接口
     *
     * @param type
     * @param nikename
     * @param nowPage
     * @param pageNum
     * @param token
     */
    private void getHonePageData(String type, String nikename, String nowPage, String pageNum, String token) {
        showProgressDialog();
        Api.getApiService().getAllUserList(type, nikename, nowPage, pageNum, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean<List<HomeBean>>>() {
                    @Override
                    public void call(BaseBean<List<HomeBean>> baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
                            SuccessData(baseBean);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });

    }

    /**
     * 数据请求成功
     *
     * @param baseBean
     */
    private void SuccessData(BaseBean<List<HomeBean>> baseBean) {
        if (baseBean.getData().size() == 0) {
            return;
        }
        homeBeanList = baseBean.getData();
        searchAdapter = new SearchAdapter(this);
        listView.setAdapter(searchAdapter);
        searchAdapter.setList(homeBeanList);
        listView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, HomePageDetailsActivity.class);
        intent.putExtra(ConstantValue.KEY_USER_ID, homeBeanList.get(position).getUserId());
        startActivity(intent);
    }
}
