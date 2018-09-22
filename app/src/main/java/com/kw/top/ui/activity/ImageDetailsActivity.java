package com.kw.top.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kw.top.R;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.ImageBean;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.ScreenUtil;
import com.kw.top.view.PinchImageView;
import com.kw.top.view.PinchImageViewPager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author  ： zy
 * date    ： 2018/7/10
 * des     ：
 */

public class ImageDetailsActivity extends BaseActivity {


    @BindView(R.id.view_pager)
    PinchImageViewPager mViewPager;
    @BindView(R.id.back_iv)
    ImageView mBackIv;
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.title_layout)
    RelativeLayout mTitleLayout;

    private List<ImageBean> mList = new ArrayList<>();
    private LinkedList<PinchImageView> mViewCacge;
    private int mCurrentIndex = 0;
    private MyPagerAdapter mPagerAdapter;

    public static void startActivity(Context context, int position, List<ImageBean> list) {
        Intent intent = new Intent(context,ImageDetailsActivity.class);
        intent.putExtra("INDEX", position);
        intent.putExtra("LIST", (Serializable) list);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_image_details;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        mCurrentIndex = getIntent().getIntExtra("INDEX", 0);
        mList.clear();
        mList.addAll((ArrayList<ImageBean>) getIntent().getSerializableExtra("LIST"));
        mViewCacge = new LinkedList<>();

        mTitleTv.setText((mCurrentIndex + 1) + "/" + mList.size());

        mPagerAdapter = new MyPagerAdapter();
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(mCurrentIndex);

        mViewPager.setOnPageChangeListener(new PinchImageViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentIndex = position;
                mTitleTv.setText((mCurrentIndex + 1) + "/" + mList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(R.id.back_iv)
    public void onViewClicked() {
        finish();
    }


    class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final PinchImageView piv;
            if (mViewCacge.size() > 0) {
                piv = mViewCacge.remove();
                piv.reset();
            } else {
                piv = new PinchImageView(ImageDetailsActivity.this);
            }
            Glide.with(ImageDetailsActivity.this)
                    .asBitmap()
                    .load(HttpHost.qiNiu + mList.get(position).getDynamicPic())
                    .apply(GlideTools.getOptions())
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(final Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            piv.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    showSaveDialog(resource);
                                    return false;
                                }
                            });
                            return false;
                        }
                    })
                    .into(piv);
            container.addView(piv);
            return piv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            PinchImageView piv = (PinchImageView) object;
            container.removeView(piv);
            mViewCacge.add(piv);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            PinchImageView piv = (PinchImageView) object;
            mViewPager.setMainPinchImageView(piv);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }



    private void showSaveDialog(final Bitmap resource) {
        View view = View.inflate(this, R.layout.dialog_save_image, null);
        final Dialog dialog = new Dialog(this, R.style.charge_dialog_style);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.dip2px(this, 112));
        window.setGravity(Gravity.BOTTOM);
        dialog.show();

        view.findViewById(R.id.tv_save_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImageToGallery(ImageDetailsActivity.this, resource);
                RxToast.normal("图片已保存到系统相册");
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    //保存文件到指定路径
    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "top";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
