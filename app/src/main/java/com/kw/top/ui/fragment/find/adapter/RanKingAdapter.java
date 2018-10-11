package com.kw.top.ui.fragment.find.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kw.top.R;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;
import com.kw.top.ui.fragment.find.bean.RankingBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/9/30.
 */

public class RanKingAdapter extends BaseAdapter {


    private Activity mActivity;
    private List<RankingBean> list;

    public RanKingAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void setData(List<RankingBean> data) {
        this.list = data;
    }


    @Override
    public int getCount() {
        return 9;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHodler viewHodler = null;
        if (view == null) {
            view = LayoutInflater.from(mActivity).inflate(R.layout.item_ranking, viewGroup, false);
            viewHodler = new ViewHodler(view);
            view.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) view.getTag();
        }
        Glide.with(mActivity)
                .asBitmap()
                .load(HttpHost.qiNiu + list.get(i + 1).getHeadImg())
                .apply(GlideTools.getOptions())
                .into(viewHodler.hade);
        viewHodler.tvPostion.setText("NO." + (i + 2));
        viewHodler.tvName.setText(list.get(i + 1).getNickName());
        int allDiamon = (int) Double.parseDouble(list.get(i + 1).getGiftAmountSum());
        viewHodler.tvNum.setText(allDiamon + "");
        return view;
    }


    public class ViewHodler {
        @BindView(R.id.ranking_postion)
        TextView tvPostion;
        @BindView(R.id.ranking_hade)
        CircleImageView hade;
        @BindView(R.id.ranking_name)
        TextView tvName;
        @BindView(R.id.ranking_num)
        TextView tvNum;

        public ViewHodler(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
