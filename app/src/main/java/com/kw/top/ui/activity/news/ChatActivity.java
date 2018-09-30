package com.kw.top.ui.activity.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kw.top.R;
import com.kw.top.base.FriendBean;
import com.kw.top.base.MyEaseBaseActivity;
import com.kw.top.redpacket.RedPacketAction;
import com.kw.top.tools.ConstantValue;
import com.kw.top.ui.activity.task.ClubTaskListActivity;
import com.kw.top.ui.fragment.find.HomePageDetailsActivity;
import com.kw.top.utils.SPUtils;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.api.model.session.SessionEventListener;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.session.fragment.MessageFragment;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.avchat.model.AVChatAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.Arrays;
import java.util.Map;

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
        Map<String, Object> extension = message.getRemoteExtension();
        if (extension != null) {
            Object o = extension.get(message.getFromAccount());
            if (o != null) {
                String userId = o.toString();
                if (!TextUtils.isEmpty(userId) && userId.contains(".")) {
                    userId = userId.substring(0, userId.indexOf("."));
                }
                Intent intent = new Intent(context, HomePageDetailsActivity.class);
                intent.putExtra(ConstantValue.KEY_USER_ID, userId);
                context.startActivity(intent);
            }
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
