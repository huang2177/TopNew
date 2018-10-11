package com.kw.top.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kw.top.R;
import com.kw.top.listener.OnDeleteListener;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

public class ImagePickerAdapter extends RecyclerView.Adapter<ImagePickerAdapter.SelectedPicViewHolder> {
    private int maxImgCount;
    private Context mContext;
    private List<LocalMedia> mData;
    private LayoutInflater mInflater;
    private OnRecyclerViewItemClickListener listener;
    private OnDeleteListener mOnDeleteListener;
    private boolean isAdded;   //是否额外添加了最后一个图片
    public static final int IMAGE_ITEM_ADD = -1;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * 删除监听事件
     *
     * @param mOnDeleteListener
     */
    public void setDetalteListener(OnDeleteListener mOnDeleteListener) {
        this.mOnDeleteListener = mOnDeleteListener;
    }

    public void setImages(List<LocalMedia> data) {
        mData = new ArrayList<>(data);
        if (getItemCount() < maxImgCount) {
            mData.add(new LocalMedia());
            isAdded = true;
        } else {
            isAdded = false;
        }
        notifyDataSetChanged();
    }

    public List<LocalMedia> getImages() {
        //由于图片未选满时，最后一张显示添加图片，因此这个方法返回真正的已选图片
        if (isAdded) return new ArrayList<>(mData.subList(0, mData.size() - 1));
        else return mData;
    }

    public ImagePickerAdapter(Context mContext, List<LocalMedia> data, int maxImgCount) {
        this.mContext = mContext;
        this.maxImgCount = maxImgCount;
        this.mInflater = LayoutInflater.from(mContext);
        setImages(data);
    }

    @Override
    public SelectedPicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectedPicViewHolder(mInflater.inflate(R.layout.list_hairpost_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SelectedPicViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class SelectedPicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView iv_img, iv_detalte;
        private int clickPosition;

        public SelectedPicViewHolder(View itemView) {
            super(itemView);
            iv_img = (ImageView) itemView.findViewById(R.id.hairpost_item_image);
            iv_detalte = (ImageView) itemView.findViewById(R.id.airpost_item_detalte);
        }

        public void bind(final int position) {
            //设置条目的点击事件
            itemView.setOnClickListener(this);
            //根据条目位置设置图片
            LocalMedia item = mData.get(position);
            if (isAdded && position == getItemCount() - 1) {
                iv_img.setImageResource(R.drawable.tjzp);
                clickPosition = IMAGE_ITEM_ADD;   //添加照片
                iv_detalte.setVisibility(View.GONE);
            } else {
                Glide.with(mContext).load(item.getPath()).into(iv_img);

                clickPosition = position;
            }


            /**
             * 删除监听事件
             */
            iv_detalte.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public void onClick(View v) {
            if (clickPosition == IMAGE_ITEM_ADD) {
                if (listener != null) listener.onItemClick(v, clickPosition);
            } else {
                mOnDeleteListener.onDelete(v, clickPosition);
            }

        }

    }
}