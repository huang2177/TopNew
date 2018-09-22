package com.kw.top.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kw.top.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author  ： zy
 * date    ： 2018/6/20
 * des     ：
 */

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
   // private List<EMMessage> mList = new ArrayList<>();

    public MessageListAdapter(Context context, List<String> list) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        //mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_message_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      /*  final EMMessage emMessage = mList.get(position);
        EMMessage.Type type = emMessage.getType();
        if (type == EMMessage.Type.TXT) {
            EMTextMessageBody messageBody = (EMTextMessageBody) emMessage.getBody();
            holder.mTvMsg.setText(messageBody.getMessage());
        } else if (type == EMMessage.Type.IMAGE) {
            holder.mTvMsg.setText("[图片]");
        } else if (type == EMMessage.Type.VIDEO) {
            holder.mTvMsg.setText("[视频]");
        }

        EMMessage.ChatType chatType = emMessage.getChatType();
        String name ="";
        Log.e("tag","===============   to " + emMessage.getTo() + "  from " + emMessage.getFrom() +"  time "+emMessage.getMsgTime());
        if (chatType == EMMessage.ChatType.GroupChat) {
            final EMGroup emGroup = EMClient.getInstance().groupManager().getGroup(emMessage.getTo());
            if (null == emGroup){
                holder.mTvNickname.setText("未知账号");
            }else {
                final String groupName = emGroup.getGroupName();
                Log.e("tag", "============  GroupName " + groupName);
                holder.mTvNickname.setText(groupName);
                holder.mCiHead.setImageResource(R.mipmap.icon_group);
                holder.RlMessageItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(emMessage.getTo())){
                            RxToast.normal(mContext,"信息异常,请刷新后再试", Toast.LENGTH_SHORT).show();
                        }else {
                            ClubChatActivity.startActivity(mContext, groupName,emMessage.getTo(),"1","");
                        }
                    }
                });
            }
        } else if (chatType == EMMessage.ChatType.Chat) {
            try {
                name = emMessage.getStringAttribute(NICK_NAME);
                holder.mTvNickname.setText(name);
                Glide.with(mContext).load(emMessage.getStringAttribute(HEAD_URL)).apply(GlideTools.getOptions()).into(holder.mCiHead);
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
            final String finalName = name;
            holder.RlMessageItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClubChatActivity.startActivity(mContext, finalName,emMessage.getTo(),"0","");
                }
            });
        }
        holder.mTvTime.setText(TimeUtils.getNewChatTime(emMessage.getMsgTime()));*/
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout RlMessageItem;
        CircleImageView mCiHead;
        TextView mTvNickname;
        TextView mTvMsg;
        TextView mTvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            RlMessageItem = itemView.findViewById(R.id.message_item);
            mCiHead = itemView.findViewById(R.id.ci_head);
            mTvNickname = itemView.findViewById(R.id.tv_nickname);
            mTvMsg = itemView.findViewById(R.id.tv_msg);
            mTvTime = itemView.findViewById(R.id.tv_time);
        }
    }

}
