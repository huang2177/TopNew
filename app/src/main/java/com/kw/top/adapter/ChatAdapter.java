package com.kw.top.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.kw.top.R;
import com.kw.top.bean.UploadBean;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.ConstantValue;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.activity.PictureActivity;
import com.kw.top.ui.activity.VideoPlayActivity;
import com.kw.top.ui.activity.VideoViewActivity;
import com.kw.top.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kw.top.ui.activity.club.BaseChatActivity.HEAD_URL;
import static com.kw.top.ui.activity.club.BaseChatActivity.IMAGE_PATH;
import static com.kw.top.ui.activity.club.BaseChatActivity.MSG_STATUS;
import static com.kw.top.ui.activity.club.BaseChatActivity.NICK_NAME;
import static com.kw.top.ui.activity.club.BaseChatActivity.VIP;

/**
 * author  ： zy
 * date    ： 2018/6/20
 * des     ：
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity mContext;
    private List<EMMessage> mList = new ArrayList<>();
    private static final int TEXT_TYPE = 0; //文本
    private static final int IMAGE_TYPE = 1;//图片
    private static final int VIDEO_TYPE = 2;//视频
    private LayoutInflater mInflater;
    private String myId ="";

    public ChatAdapter(Activity context, List<EMMessage> list) {
        this.mContext = context;
        this.mList = list;
        mInflater = LayoutInflater.from(mContext);
        myId = SPUtils.getString(mContext, ConstantValue.KEY_CHAT_NUM, "");
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage.Type type = mList.get(position).getType();
        if (type == EMMessage.Type.TXT) {
            return TEXT_TYPE;
        } else if (type == EMMessage.Type.IMAGE) {
            return IMAGE_TYPE;
        } else if (type == EMMessage.Type.VIDEO) {
            return VIDEO_TYPE;
        } else {
            return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TEXT_TYPE) {
            View textview = mInflater.inflate(R.layout.item_chat_text, parent, false);
            TextViewHolder textViewHolder = new TextViewHolder(textview);
            return textViewHolder;
        } else if (viewType == IMAGE_TYPE) {
            View imageview = mInflater.inflate(R.layout.item_chat_image, parent, false);
            return new ImageViewHolder(imageview);
        } else if (viewType == VIDEO_TYPE) {
            View videoview = mInflater.inflate(R.layout.item_chat_video, parent, false);
            return new VideoViewHolder(videoview);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case TEXT_TYPE:
                fillTextView((TextViewHolder) holder, position);
                break;
            case IMAGE_TYPE:
                fillImageView((ImageViewHolder) holder, position);
                break;
            case VIDEO_TYPE:
                fillVideoView((VideoViewHolder) holder, position);
                break;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (payloads == null)//|| mUploadBeanList == null
            return;
        Log.e("tag", "===========  position " + position);
        EMMessage emMessage = mList.get(position);
        int msg_status = 0;
        msg_status = emMessage.getIntAttribute(MSG_STATUS,0);
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case TEXT_TYPE:
                break;
            case IMAGE_TYPE:
                ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
                if (msg_status == -1) {
                    //失败
                    imageViewHolder.mProgressRight.setVisibility(View.GONE);
                } else if (msg_status == 1) {
                    //发送中
                    imageViewHolder.mProgressRight.setVisibility(View.VISIBLE);
                } else {
                    //发送成功
                    imageViewHolder.mProgressRight.setVisibility(View.GONE);
                }
                break;
            case VIDEO_TYPE:
                VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
                if (msg_status == -1) {
                    //失败
                    videoViewHolder.mPbVideo.setVisibility(View.GONE);
                } else if (msg_status == 1) {
                    //发送中
                    videoViewHolder.mPbVideo.setVisibility(View.VISIBLE);
                } else {
                    //发送成功
                    videoViewHolder.mPbVideo.setVisibility(View.GONE);
                }
                break;
        }
    }

    /**
     * 填充视频
     *
     * @param holder
     * @param position
     */
    private void fillVideoView(VideoViewHolder holder, int position) {
        EMMessage emMessage = mList.get(position);
        if (emMessage.getFrom().equalsIgnoreCase(myId)) {
            //自己的view
            holder.mRlVideoitemFrom.setVisibility(View.GONE);
            holder.mRlVideoitemTo.setVisibility(View.VISIBLE);
            String url = "";
            EMVideoMessageBody videoMessageBody = (EMVideoMessageBody) emMessage.getBody();
            url = videoMessageBody.getThumbnailSecret();
            Log.e("tag","===========  "+videoMessageBody.getLocalThumb() + "   " + videoMessageBody.getThumbnailSecret() + "  "+ videoMessageBody.getThumbnailUrl());
            try {
                holder.mTvNicknameTo.setText(emMessage.getStringAttribute(NICK_NAME));
                url = emMessage.getStringAttribute(IMAGE_PATH);
                Glide.with(mContext).load(url).into(holder.mIvVideoTo);
                GlideTools.setVipResourceS(holder.mIvVipTo, emMessage.getIntAttribute(VIP));
                Glide.with(mContext).load(HttpHost.qiNiu + emMessage.getStringAttribute(HEAD_URL)).apply(GlideTools.getHeadOptions()).into(holder.mCiHeadTo);
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
            final String finalUrl = url;
            holder.mRlVideoitemTo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VideoViewActivity.startActivity(mContext,finalUrl);
                }
            });
        } else {
            //别人的view
            holder.mRlVideoitemFrom.setVisibility(View.GONE);
            holder.mRlVideoitemTo.setVisibility(View.VISIBLE);
            EMVideoMessageBody videoMessageBody = (EMVideoMessageBody) emMessage.getBody();
            final String url = videoMessageBody.getLocalThumb();
            Glide.with(mContext).load(url).into(holder.mIvVideoFrom);
            try {
                holder.mTvNicknameFrom.setText(emMessage.getStringAttribute(NICK_NAME));
                GlideTools.setVipResourceS(holder.mIvVideoFrom, emMessage.getIntAttribute(VIP));
                Glide.with(mContext).load(HttpHost.qiNiu + emMessage.getStringAttribute(HEAD_URL)).apply(GlideTools.getHeadOptions()).into(holder.mCiHeadFrom);
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
            holder.mRlVideoitemFrom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VideoViewActivity.startActivity(mContext,url);
                }
            });
        }
    }

    /**
     * 填充图片
     *
     * @param holder
     * @param position
     */
    private void fillImageView(final ImageViewHolder holder, int position) {
        EMMessage emMessage = mList.get(position);
        if (emMessage.getFrom().equalsIgnoreCase(myId)) {
            //自己的view
            holder.mRlIvFrom.setVisibility(View.GONE);
            holder.mRlIvTo.setVisibility(View.VISIBLE);
            String url = "";
            int msg_status = emMessage.getIntAttribute(MSG_STATUS, 1);
            EMImageMessageBody imageMessageBody = (EMImageMessageBody) emMessage.getBody();
            url = imageMessageBody.getThumbnailUrl();
            if (TextUtils.isEmpty(url)) {
                try {
                    url = emMessage.getStringAttribute(IMAGE_PATH);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            } else {
                url = imageMessageBody.getThumbnailUrl();
            }
            GlideTools.setImageSize(mContext, holder.mIvImageTo, url);
            final String finalUrl = url;
            holder.mIvImageTo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PictureActivity.startActivity(mContext, finalUrl);
                }
            });
            try {
                holder.mTvNicknameTo.setText(emMessage.getStringAttribute(NICK_NAME));
                GlideTools.setVipResourceS(holder.mIvVipTo, emMessage.getIntAttribute(VIP));
                Glide.with(mContext).load(HttpHost.qiNiu+emMessage.getStringAttribute(HEAD_URL)).apply(GlideTools.getOptions()).into(holder.mCiHeadTo);
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        } else {
            //别人的view
            holder.mRlIvFrom.setVisibility(View.VISIBLE);
            holder.mRlIvTo.setVisibility(View.GONE);
            final EMImageMessageBody imageMessageBody = (EMImageMessageBody) emMessage.getBody();
            GlideTools.setImageSize(mContext, holder.mIvImageFrom, imageMessageBody.getThumbnailUrl());
            holder.mIvImageFrom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PictureActivity.startActivity(mContext, imageMessageBody.getThumbnailUrl());
                }
            });
            try {
                holder.mTvNicknameFrom.setText(emMessage.getStringAttribute(NICK_NAME));
                GlideTools.setVipResourceS(holder.mIvVipFrom, emMessage.getIntAttribute(VIP));
                Glide.with(mContext).load(HttpHost.qiNiu+emMessage.getStringAttribute(HEAD_URL)).apply(GlideTools.getOptions()).into(holder.mCiHeadFrom);
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 填充文本
     *
     * @param holder
     * @param position
     */
    private void fillTextView(TextViewHolder holder, int position) {
        EMMessage emMessage = mList.get(position);
        EMTextMessageBody messageBody = (EMTextMessageBody) emMessage.getBody();
        if (emMessage.getFrom().equalsIgnoreCase(myId)) {
            Log.e("tag","=========== 自己view  form " + emMessage.getFrom() + "   my " +  myId);
            //自己的
            holder.mRlFrom.setVisibility(View.GONE);
            holder.mRlTo.setVisibility(View.VISIBLE);
            holder.mTvMsgTo.setText(messageBody.getMessage());
            try {
                holder.mTvNicknameTo.setText(emMessage.getStringAttribute(NICK_NAME));
                GlideTools.setVipResourceS(holder.mIvVipTo, emMessage.getIntAttribute(VIP));
                Glide.with(mContext).load(HttpHost.qiNiu+emMessage.getStringAttribute(HEAD_URL)).apply(GlideTools.getOptions()).into(holder.mCiHeadTo);
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("tag","=========== 别人的view  form " + emMessage.getFrom() + "   my " +  myId);
            //别人的
            holder.mRlFrom.setVisibility(View.VISIBLE);
            holder.mRlTo.setVisibility(View.GONE);
            holder.mTvMsgFrom.setText(messageBody.getMessage());
            try {
                holder.mTvNicknameFrom.setText(emMessage.getStringAttribute(NICK_NAME));
                GlideTools.setVipResourceS(holder.mIvVipFrom, emMessage.getIntAttribute(VIP));
                Glide.with(mContext).load(HttpHost.qiNiu+emMessage.getStringAttribute(HEAD_URL)).apply(GlideTools.getOptions()).into(holder.mCiHeadFrom);
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class TextViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mCiHeadFrom;
        TextView mTvNicknameFrom;
        ImageView mIvVipFrom;
        RelativeLayout mRlFrom;
        CircleImageView mCiHeadTo;
        TextView mTvNicknameTo;
        ImageView mIvVipTo;
        RelativeLayout mRlTo;
        TextView mTvMsgFrom, mTvMsgTo;

        public TextViewHolder(View itemView) {
            super(itemView);
            mCiHeadFrom = itemView.findViewById(R.id.ci_head_from);
            mTvNicknameFrom = itemView.findViewById(R.id.tv_nickname_from);
            mIvVipFrom = itemView.findViewById(R.id.iv_vip_from);
            mRlFrom = itemView.findViewById(R.id.rl_from);
            mTvMsgFrom = itemView.findViewById(R.id.tv_msg_from);

            mCiHeadTo = itemView.findViewById(R.id.ci_head_to);
            mTvNicknameTo = itemView.findViewById(R.id.tv_nickname_to);
            mIvVipTo = itemView.findViewById(R.id.iv_vip_to);
            mRlTo = itemView.findViewById(R.id.rl_to);
            mTvMsgTo = itemView.findViewById(R.id.tv_msg_to);
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mCiHeadFrom;
        TextView mTvNicknameFrom;
        ImageView mIvVipFrom;
        ImageView mIvImageFrom;
        RelativeLayout mRlIvFrom;

        CircleImageView mCiHeadTo;
        TextView mTvNicknameTo;
        ImageView mIvVipTo;
        ImageView mIvImageTo;
        RelativeLayout mRlIvTo;

        public ProgressBar mProgressLift, mProgressRight;

        public ImageViewHolder(View itemView) {
            super(itemView);
            mCiHeadFrom = itemView.findViewById(R.id.ci_head_from);
            mTvNicknameFrom = itemView.findViewById(R.id.tv_nickname_from);
            mIvVipFrom = itemView.findViewById(R.id.iv_vip_from);
            mIvImageFrom = itemView.findViewById(R.id.iv_image_from);
            mRlIvFrom = itemView.findViewById(R.id.rl_iv_from);

            mCiHeadTo = itemView.findViewById(R.id.ci_head_to);
            mTvNicknameTo = itemView.findViewById(R.id.tv_nickname_to);
            mIvVipTo = itemView.findViewById(R.id.iv_vip_to);
            mIvImageTo = itemView.findViewById(R.id.iv_image_to);
            mRlIvTo = itemView.findViewById(R.id.rl_iv_to);

            mProgressLift = itemView.findViewById(R.id.progress_bar_left);
            mProgressRight = itemView.findViewById(R.id.progress_bar_right);
        }
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mCiHeadFrom;
        TextView mTvNicknameFrom;
        ImageView mIvVipFrom;
        ImageView mIvVideoFrom;
        TextView mTvDurationFrom;
        RelativeLayout mRlVideoFrom;
        RelativeLayout mRlVideoitemFrom;

        CircleImageView mCiHeadTo;
        TextView mTvNicknameTo;
        ImageView mIvVipTo;
        ImageView mIvVideoTo;
        TextView mTvDurationTo;
        RelativeLayout mRlVideoTo;
        RelativeLayout mRlVideoitemTo;

        ProgressBar mPbVideo;

        public VideoViewHolder(View itemView) {
            super(itemView);
            mCiHeadFrom = itemView.findViewById(R.id.ci_head_from);
            mTvNicknameFrom = itemView.findViewById(R.id.tv_nickname_from);
            mIvVipFrom = itemView.findViewById(R.id.iv_vip_from);
            mIvVideoFrom = itemView.findViewById(R.id.iv_video_from);
            mTvDurationFrom = itemView.findViewById(R.id.tv_duration_from);
            mRlVideoFrom = itemView.findViewById(R.id.rl_video_from);
            mRlVideoitemFrom = itemView.findViewById(R.id.rl_videoitem_from);

            mCiHeadTo = itemView.findViewById(R.id.ci_head_to);
            mTvNicknameTo = itemView.findViewById(R.id.tv_nickname_to);
            mIvVipTo = itemView.findViewById(R.id.iv_vip_to);
            mIvVideoTo = itemView.findViewById(R.id.iv_video_to);
            mTvDurationTo = itemView.findViewById(R.id.tv_duration_to);
            mRlVideoTo = itemView.findViewById(R.id.rl_video_to);
            mRlVideoitemTo = itemView.findViewById(R.id.rl_videoitem_to);

            mPbVideo = itemView.findViewById(R.id.progress_bar_video);
        }
    }
}
