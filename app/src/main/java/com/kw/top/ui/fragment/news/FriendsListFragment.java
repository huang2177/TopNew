package com.kw.top.ui.fragment.news;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.adapter.FriendListAdapter;
import com.kw.top.adapter.SBFriendAdapter;
import com.kw.top.base.FriendBean;
import com.kw.top.base.MVPBaseFragment;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.SBFriendTagBean;
import com.kw.top.bean.SBFriendsBean;
import com.kw.top.bean.event.RefreshFriendEvent;
import com.kw.top.listener.OnClickListener;
import com.kw.top.listener.OnDeleteListener;
import com.kw.top.listener.OnFriendClickListener;
import com.kw.top.listener.OnFriendDeleteListener;
import com.kw.top.retrofit.Api;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.ComResultTools;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.ui.activity.news.ChatActivity;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;
import com.kw.top.view.UILetterListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: zy
 * data  : 2018/6/2
 * des   : 好友列表
 */

public class FriendsListFragment extends MVPBaseFragment<NewsListContract.View, NewsListPresenter> implements NewsListContract.View, OnFriendClickListener, OnFriendDeleteListener {

    @BindView(R.id.letter_lv)
    UILetterListView mLetterLv;
    @BindView(R.id.friend_lv)
    ListView mFriendLv;
    private List<FriendBean> mFriendList = new ArrayList<>();
    private List<SBFriendTagBean> mSBFriends = new ArrayList<>();
    private SBFriendAdapter mSBFriendAdapter;
    WindowManager mWindowManager;
    TextView mOverlay;
    Handler mHandler = new Handler();
    OverlayThread mOverlayThread;

    public static FriendsListFragment newInstance(String type) {
        FriendsListFragment fragment = new FriendsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("TYPE", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_new_list;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        mSBFriendAdapter = new SBFriendAdapter(getContext(), mSBFriends, this, this);
        mFriendLv.setAdapter(mSBFriendAdapter);

        initOverlay();
        List<String> mLetters = new ArrayList<>();
        char a = 'A';
        for (int i = 0; i < 26; i++) {
            mLetters.add(a + "");
            a++;
        }
        mLetterLv.setSections(mLetters);
    }

    private void initOverlay() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        mOverlay = (TextView) inflater.inflate(R.layout.widget_overlay, null);
        mOverlay.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        mWindowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.addView(mOverlay, lp);
        mOverlayThread = new OverlayThread();
    }

    @Override
    public void initListener() {
        mLetterLv.setOnTouchingLetterChangedListener(new UILetterListView.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
//                HashMap<String, Integer> alphaIndexer = mPresenter.getAlphaIndexer();
//                if (alphaIndexer.get(s) != null) {
//                    int position = alphaIndexer.get(s);
//                    mBrandLv.setSelection(position + mBrandLv.getHeaderViewsCount());

                for (int i = 0; i < mSBFriends.size(); i++) {
                    if (mSBFriends.get(i).getTag().equals(s)) {
                        mFriendLv.setSelection(i);
                    }
                }
                mOverlay.setText(s);
                mOverlay.setVisibility(View.VISIBLE);
                mHandler.removeCallbacks(mOverlayThread);
                // 延迟一秒后执行，让overlay为不可见
                mHandler.postDelayed(mOverlayThread, 500);
//                }
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        try {
            if (mWindowManager != null) {
                mWindowManager.removeView(mOverlay);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.gc();
    }

    @Override
    protected void initPresenter() {
        mPresenter.getFriendsList(getToken());
    }

    @Override
    public void getFriendListResult(BaseBean baseBean) {
        if (null != baseBean && baseBean.isSuccess()) {
            try {
                SBFriendsBean sbFriendsBean = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<SBFriendsBean>() {
                }.getType());
                mSBFriends.clear();
                if (sbFriendsBean.getA() != null && sbFriendsBean.getA().size() > 0)
                    mSBFriends.add(new SBFriendTagBean("A", sbFriendsBean.getA()));
                if (sbFriendsBean.getB() != null && sbFriendsBean.getB().size() > 0)
                    mSBFriends.add(new SBFriendTagBean("B", sbFriendsBean.getB()));
                if (sbFriendsBean.getC() != null && sbFriendsBean.getC().size() > 0)
                    mSBFriends.add(new SBFriendTagBean("C", sbFriendsBean.getC()));
                if (sbFriendsBean.getD() != null && sbFriendsBean.getD().size() > 0)
                    mSBFriends.add(new SBFriendTagBean("D", sbFriendsBean.getD()));
                if (sbFriendsBean.getE() != null && sbFriendsBean.getE().size() > 0)
                    mSBFriends.add(new SBFriendTagBean("E", sbFriendsBean.getE()));
                if (sbFriendsBean.getF() != null && sbFriendsBean.getF().size() > 0)
                    mSBFriends.add(new SBFriendTagBean("F", sbFriendsBean.getF()));
                if (sbFriendsBean.getG() != null && sbFriendsBean.getG().size() > 0)
                    mSBFriends.add(new SBFriendTagBean("G", sbFriendsBean.getG()));
                if (sbFriendsBean.getH() != null && sbFriendsBean.getH().size() > 0)
                    mSBFriends.add(new SBFriendTagBean("H", sbFriendsBean.getH()));
                if (sbFriendsBean.getI() != null && sbFriendsBean.getI().size() > 0)
                    mSBFriends.add(new SBFriendTagBean("I", sbFriendsBean.getI()));
                if (sbFriendsBean.getJ() != null && sbFriendsBean.getJ().size() > 0)
                    mSBFriends.add(new SBFriendTagBean("J", sbFriendsBean.getJ()));
                if (sbFriendsBean.getK() != null && sbFriendsBean.getK().size() > 0)
                    mSBFriends.add(new SBFriendTagBean("K", sbFriendsBean.getK()));
                if (sbFriendsBean.getL() != null && sbFriendsBean.getL().size() > 0)
                    mSBFriends.add(new SBFriendTagBean("L", sbFriendsBean.getL()));
                if (sbFriendsBean.getM() != null && sbFriendsBean.getM().size() > 0)
                    mSBFriends.add(new SBFriendTagBean("M", sbFriendsBean.getM()));
                if (sbFriendsBean.getN() != null && sbFriendsBean.getN().size() > 0)
                    mSBFriends.add(new SBFriendTagBean("N", sbFriendsBean.getN()));
                if (sbFriendsBean.getO() != null && sbFriendsBean.getO().size() > 0)
                    mSBFriends.add(new SBFriendTagBean("O", sbFriendsBean.getO()));
                if (sbFriendsBean.getP() != null && sbFriendsBean.getP().size() > 0)
                    mSBFriends.add(new SBFriendTagBean("P", sbFriendsBean.getP()));
                if (sbFriendsBean.getQ() != null && sbFriendsBean.getQ().size() > 0)
                    mSBFriends.add(new SBFriendTagBean("Q", sbFriendsBean.getQ()));
                if (sbFriendsBean.getR() != null && sbFriendsBean.getR().size() > 0)
                    mSBFriends.add(new SBFriendTagBean("R", sbFriendsBean.getR()));
                if (sbFriendsBean.getS() != null && sbFriendsBean.getS().size() > 0)
                    mSBFriends.add(new SBFriendTagBean("S", sbFriendsBean.getS()));
                if (sbFriendsBean.getT() != null && sbFriendsBean.getT().size() > 0)
                    mSBFriends.add(new SBFriendTagBean("T", sbFriendsBean.getT()));
                if (sbFriendsBean.getU() != null && sbFriendsBean.getU().size() > 0)
                    mSBFriends.add(new SBFriendTagBean("U", sbFriendsBean.getU()));
                if (sbFriendsBean.getV() != null && sbFriendsBean.getV().size() > 0)
                    mSBFriends.add(new SBFriendTagBean("V", sbFriendsBean.getV()));
                if (sbFriendsBean.getW() != null && sbFriendsBean.getW().size() > 0)
                    mSBFriends.add(new SBFriendTagBean("W", sbFriendsBean.getW()));
                if (sbFriendsBean.getX() != null && sbFriendsBean.getX().size() > 0)
                    mSBFriends.add(new SBFriendTagBean("X", sbFriendsBean.getX()));
                if (sbFriendsBean.getY() != null && sbFriendsBean.getY().size() > 0)
                    mSBFriends.add(new SBFriendTagBean("Y", sbFriendsBean.getY()));
                if (sbFriendsBean.getZ() != null && sbFriendsBean.getZ().size() > 0)
                    mSBFriends.add(new SBFriendTagBean("Z", sbFriendsBean.getZ()));

                mSBFriendAdapter.notifyDataSetChanged();

            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        } else if (baseBean.getCode().equals("-44")) {
            RxToast.normal(getResources().getString(R.string.login_out));
            SPUtils.clear(getActivity());
            startActivity(LoginActivity.class);
            getActivity().finish();
            //ComResultTools.resultData(getActivity(), baseBean);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshFriend(RefreshFriendEvent friendEvent) {
        if (friendEvent.isRefresh())
            initPresenter();

    }

    private void deleteFriend(String friendId) {
        showProgressDialog();
        Api.getApiService().deleteFriend(friendId + "", getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        hideProgressDialog();
                        if (baseBean.isSuccess()) {
//                            Iterator<FriendBean> iterator = mFriendList.iterator();
//                            while (iterator.hasNext()) {
//                                FriendBean bean = iterator.next();
//                                if (bean.getFriendsId() == friendId)
//                                    iterator.remove();   //注意这个地方
//                            }
                            initPresenter();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgressDialog();
                    }
                });
    }

    @Override
    public void onDelete(View view, final String friendId) {
        new AlertDialog.Builder(getContext()).setTitle("删除好友")
                .setMessage("确认删除好友？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                deleteFriend(friendId);
            }
        }).show();
    }

    @Override
    public void onClick(View view, FriendBean friendBean) {
        if (null != friendBean && !TextUtils.isEmpty(friendBean.getFriendAccount())) {
            ChatActivity.startActivity(getContext(), friendBean.getFriendAccount(), friendBean.getNickName(), HttpHost.qiNiu + friendBean.getHeadImg(), friendBean.getNickName());
        } else {
            RxToast.normal("用户信息异常");
        }
    }

    // 设置overlay不可见
    private class OverlayThread implements Runnable {
        @Override
        public void run() {
            mOverlay.setVisibility(View.GONE);
        }
    }
}
