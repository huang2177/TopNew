package com.kw.top.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kw.top.R;
import com.netease.nim.uikit.business.robot.parser.elements.group.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by shibing on 2018/9/27.
 */

public class EaseChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Activity mActivity;

    public EaseChatAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_easechat, null, false);
        return new ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }


    public class ViewHodler extends RecyclerView.ViewHolder {

        @BindView(R.id.chat_hade)
        CircleImageView imageView;
        @BindView(R.id.chat_name)
        TextView tvName;
        @BindView(R.id.chat_content)
        TextView tvContent;
        @BindView(R.id.chat_message)
        TextView tvMessage;

        public ViewHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
