package com.kw.top.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.kw.top.R;
import com.kw.top.base.BaseActivity;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.GlideTools;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.ScreenUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * author  ： zy
 * date    ： 2018/6/24
 * des     ：
 */

public class PictureActivity extends BaseActivity {

    ImageView mImage;
    ProgressBar mProgressBar;

    private String path;
    private PhotoViewAttacher mPhotoViewAttacher;
    private int screenWidth;

    public static void startActivity(Activity activity, String path) {
        Intent intent = new Intent(activity, PictureActivity.class);
        intent.putExtra("PATH", path);
        activity.startActivity(intent);
    }


    @Override
    public int getContentView() {
        return R.layout.activity_picture;
    }

    public void initView() {
        screenWidth = ScreenUtil.getScreenWidth(this) - ScreenUtil.dip2px(this, 6);
        path = getIntent().getStringExtra("PATH");
        mPhotoViewAttacher = new PhotoViewAttacher(mImage);
        Glide.with(this).asBitmap().load(HttpHost.qiNiu + path)
                .apply(GlideTools.getOptions())
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        RxToast.normal("加载失败");
                        mImage.setImageResource(R.mipmap.ic_launcher);
                        mProgressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(final Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        mPhotoViewAttacher = new PhotoViewAttacher(mImage);
                        mProgressBar.setVisibility(View.GONE);
                        mPhotoViewAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                            @Override
                            public void onPhotoTap(View view, float x, float y) {
                                finish();
                            }
                        });
                        mPhotoViewAttacher.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                showSaveDialog(resource);
                                return false;
                            }
                        });
                        return false;
                    }
                }).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                ViewGroup.LayoutParams params = mImage.getLayoutParams();
                if (resource.getWidth() < 400) {
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                } else {
                    int width = screenWidth;//* resource.getWidth() / 400;
                    int height = width * resource.getHeight() / resource.getWidth();
                    params.width = width;
                    params.height = height;
                }
                mImage.setLayoutParams(params);
                mImage.setImageBitmap(resource);
            }
        });
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
                saveImageToGallery(PictureActivity.this, resource);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(getContentView());
        mImage = findViewById(R.id.image);
        mProgressBar = findViewById(R.id.progress_bar);
        initView();
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
