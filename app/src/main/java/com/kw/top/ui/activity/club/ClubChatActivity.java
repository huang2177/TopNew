package com.kw.top.ui.activity.club;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.adapter.message.EMAMessage;
import com.kw.top.R;
import com.kw.top.base.MVPBaseActivity;
import com.kw.top.bean.ClubBean;
import com.kw.top.bean.event.MessageEvent;
import com.kw.top.tools.ConstantValue;
import com.kw.top.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author  ： zy
 * date    ： 2018/6/19
 * des     ：
 */

public class ClubChatActivity extends BaseChatActivity {

    private String title,//标题
//            toChatUsername,//发送信息的对象 ID
            chat_type; //消息类型 0单人，1群组
    private String clubid;

    public static void startActivity(Context context,String title,String toChatUsername,String chat_type,String clubid){
        Intent intent = new Intent(context, ClubChatActivity.class);
        intent.putExtra("TITLE", title);
        intent.putExtra("TOUSERNAME",toChatUsername);
        intent.putExtra("CHATTYPE",chat_type);
        intent.putExtra("CLUBID",clubid);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void initView() {
        super.initView();
        title = getIntent().getStringExtra("TITLE");
        toChatUsername = getIntent().getStringExtra("TOUSERNAME");
        chat_type = getIntent().getStringExtra("CHATTYPE");

        mTvTitle.setText(title);
        if (chat_type.equals("0")){
            //单人
            chatType = EMAMessage.EMAChatType.SINGLE;
        }else if (chat_type.equals("1")){
            //群消息
            chatType = EMAMessage.EMAChatType.GROUP;
            mTvTitleRight.setBackgroundResource(R.mipmap.icon_club_edit);
            clubid = getIntent().getStringExtra("CLUBID");
            mTvTitleRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    ClubDetailsActivity.startActivity(ClubChatActivity.this,clubid);
                }
            });
        }
        headUrl = SPUtils.getString(this, ConstantValue.KEY_HEAD,"");
        nickname = SPUtils.getString(this,ConstantValue.KEY_NAME,"");
        vip =1;

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClubChatActivity.this.finish();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        //获取此会话的所有消息
        if (null == conversation)
            return;
        List<EMMessage> messages = conversation.getAllMessages();
        mList.addAll(messages);
        mChatAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(mChatAdapter.getItemCount()-1);
        //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
        //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
//        List<EMMessage> messages = conversation.loadMoreMsgFromDB(startMsgId, pagesize);
    }

    @Override
    public void onRefresh() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMessage(MessageEvent messageEvent){
        Log.e("tag","==========  eventbus ");
        if (null != messageEvent.getEMMessage() && messageEvent.getEMMessage().getTo().equals(toChatUsername)){
            mList.add(messageEvent.getEMMessage());
            if (null != mChatAdapter){
                mChatAdapter.notifyDataSetChanged();
                mRecyclerView.smoothScrollToPosition(mChatAdapter.getItemCount()-1);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }
}
