package com.kw.top.ui.fragment.news;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.kw.top.R;
import com.kw.top.adapter.MessageListAdapter;
import com.kw.top.base.BaseFragment;
import com.kw.top.tools.ChatHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * author  ： zy
 * date    ： 2018/6/20
 * des     ：
 */

public class NewsListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private List<EMMessage> mList = new ArrayList<>();
    private MessageListAdapter mAdapter;

    @Override
    public int getContentView() {
        return R.layout.fragment_news_list;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new MessageListAdapter(getContext(), mList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        //获得所有会话
        Map<String, EMConversation> allConver = ChatHelper.getInstance().getAllConversations();
        if (allConver.size()>0)
            mList.clear();
        for (Map.Entry<String, EMConversation> entry : allConver.entrySet()) {
            EMConversation conversation = entry.getValue();
            List<EMMessage> list = conversation.getAllMessages();
            if (list != null && list.size() > 0)
                mList.add(list.get(list.size()-1));
//            for (int i =0; i<list.size();i++){
//                EMMessage emMessage = list.get(i);
//                Log.e("tag","==========  message  " + emMessage.getBody().toString() +"  chatType " +emMessage.getChatType() +emMessage.getTo()
//                        +"  " + emMessage.getMsgId()+" " + emMessage.getUserName() +" " + emMessage.getMsgTime());
//            }
        }

        mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        initData();
    }
}
