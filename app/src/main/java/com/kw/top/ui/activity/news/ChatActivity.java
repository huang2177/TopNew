package com.kw.top.ui.activity.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kw.top.R;
import com.kw.top.bean.FriendBean;
import com.kw.top.base.MyEaseBaseActivity;
import com.kw.top.tools.ConstantValue;
import com.kw.top.ui.activity.task.ClubTaskListActivity;
import com.kw.top.ui.fragment.find.HomePageDetailsActivity;
import com.netease.nim.uikit.api.model.session.SessionEventListener;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author  ： zy
 * date    ： 2018/7/1
 * des     ：
 */

public class ChatActivity extends MyEaseBaseActivity implements View.OnClickListener, SessionEventListener {
    @BindView(R.id.tv_task_state)
    TextView mTaskState;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    public String userId;
    private ChatFragment fragment;

    public static void startActivity(Context context, FriendBean bean) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("data", bean);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_chat);
        ButterKnife.bind(this);
        initView();
        back(ivBack);
        getClubTaskState();
    }


    private void initView() {
        NimUIKitImpl.setSessionListener(this);
        FriendBean bean = (FriendBean) getIntent().getSerializableExtra("data");
        userId = bean.getFriendAccount();
        tvTitle.setText(bean.getNickName());

        fragment = new ChatFragment().newInstance(bean);
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();

        mTaskState.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        ClubTaskListActivity.startActivity(ChatActivity.this, userId, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAvatarClicked(Context context, IMMessage message) {
        String account = message.getFromAccount().replace(ConstantValue.ACCOUNT_TEXT,"");
        if (!TextUtils.isEmpty(account)) {
            int userId = Integer.parseInt(account);
            Intent intent = new Intent(context, HomePageDetailsActivity.class);
            intent.putExtra(ConstantValue.KEY_USER_ID, String.valueOf(userId));
            context.startActivity(intent);
        }
    }

    @Override
    public void onAvatarLongClicked(Context context, IMMessage message) {

    }

    @Override
    public void onAckMsgClicked(Context context, IMMessage message) {

    }

    private void getClubTaskState() {
//        int chatType = getIntent().getIntExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
//        if (chatType == EaseConstant.CHATTYPE_GROUP) {
//            Api.getApiService().userClubTaskState(userId, getToken())
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .unsubscribeOn(Schedulers.io())
//                    .subscribe(new Subscriber<BaseBean>() {
//                        @Override
//                        public void onCompleted() {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//
//                        }
//
//                        @Override
//                        public void onNext(BaseBean baseBean) {
//                            if (baseBean.isSuccess()) {
//                                String state = ((LinkedTreeMap<String, String>) baseBean.getData()).get("finishTaskState");
//                                if (state.equals("0")) {
//                                    mTaskState.setVisibility(View.VISIBLE);
//                                }
//                            }
//                        }
//                    });
//        }
    }
}
