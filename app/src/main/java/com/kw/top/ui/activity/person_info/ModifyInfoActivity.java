package com.kw.top.ui.activity.person_info;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kw.top.R;
import com.kw.top.adapter.GridViewAdapter;
import com.kw.top.adapter.ImagePickerAdapter;
import com.kw.top.base.BaseActivity_;
import com.kw.top.listener.OnDeleteListener;
import com.kw.top.listener.OnItemClickListener;
import com.kw.top.tools.ConstantValue;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ModifyInfoActivity extends BaseActivity_ implements ImagePickerAdapter.OnRecyclerViewItemClickListener, OnDeleteListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.modify_grid_view)
    RecyclerView recyclerView;
    private ImagePickerAdapter mAdapter;
    private List<LocalMedia> selImageList;
    private int maxImgCount = 5;
    public static final int IMAGE_ITEM_ADD = -1;
    private ArrayList<LocalMedia> images = null;

    @Override
    public int getContentView() {
        return R.layout.activity_modify;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        initviews();
    }

    private void initviews() {
        tvTitle.setText("编辑资料");
        initRecyler();
    }


    /**
     * 初始化recylerview
     */
    private void initRecyler() {
        selImageList = new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        mAdapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setDetalteListener(this);
    }


    @OnClick({R.id.iv_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }


    @Override
    public void onItemClick(View view, int position) {
        if (position == IMAGE_ITEM_ADD) {
            PictureSelector.create(this)
                    .openGallery(PictureConfig.TYPE_IMAGE)
                    .maxSelectNum(maxImgCount)
                    .minSelectNum(1)
                    .imageSpanCount(5)
                    .selectionMode(PictureConfig.MULTIPLE)
                    .previewImage(true)
                    .isCamera(true)
                    .isZoomAnim(true)
                    .selectionMedia(images)
                    .forResult(ConstantValue.JUMP_RELEASE_IMAGES);
        }
    }


    /**
     * 选择图片
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == ConstantValue.JUMP_RELEASE_IMAGES) {  //多张图片
            images = (ArrayList<LocalMedia>) PictureSelector.obtainMultipleResult(data);
            if (images != null) {
                selImageList.addAll(images);
                mAdapter.setImages(selImageList);
            }
        }
    }

    /**
     * 删除照片
     *
     * @param view
     * @param position
     */
    @Override
    public void onDelete(View view, int position) {
        selImageList.remove(position);
        mAdapter.notifyDataSetChanged();
    }
}
