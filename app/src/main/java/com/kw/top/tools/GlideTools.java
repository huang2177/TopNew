package com.kw.top.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.kw.top.R;
import com.kw.top.app.BaseApplication;
import com.kw.top.utils.GlideRoundTransform;

import java.io.File;
import java.math.BigDecimal;
import java.security.MessageDigest;

import static com.bumptech.glide.load.resource.bitmap.VideoDecoder.FRAME_OPTION;

/**
 * author  ： zy
 * date    ： 2018/6/16
 * des     ：
 */

public class GlideTools {

    private static RequestOptions options;
    private static RequestOptions headOptions;
    private static RequestOptions sRequestOptions;//不加裁剪
    private static RequestOptions videoOptions;

    public static RequestOptions getVideoOptions(final Context context) {
        if (videoOptions == null) {
            videoOptions = RequestOptions.frameOf(1);
            videoOptions.set(FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST);
            videoOptions.transform(new BitmapTransformation() {
                @Override
                protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
                    return toTransform;
                }

                @Override
                public void updateDiskCacheKey(MessageDigest messageDigest) {
                    try {
                        messageDigest.update((context.getPackageName() + "RotateTransform").getBytes("utf-8"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return videoOptions;
    }

    public static RequestOptions getOptions() {
        if (options == null)
            options = new RequestOptions()
                    .placeholder(R.mipmap.image_loading)
                    .error(R.mipmap.image_loading)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
        return options;
    }

    public static RequestOptions getSOptions() {
        if (sRequestOptions == null)
            sRequestOptions = new RequestOptions()
                    .placeholder(R.mipmap.image_loading)
                    .error(R.mipmap.image_loading)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
        return sRequestOptions;
    }

    public static RequestOptions getHeadOptions() {
        if (headOptions == null)
            headOptions = new RequestOptions()
                    .placeholder(R.mipmap.icon_default_head)
                    .dontAnimate()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
        return headOptions;
    }

    public static void setVipResourceS(ImageView imageView, int grade) {
        switch (grade) {
            case 0:
                imageView.setImageDrawable(null);
                break;
            case 1:
                imageView.setImageResource(R.mipmap.icon_vip1_s);
                break;
            case 2:
                imageView.setImageResource(R.mipmap.icon_vip2_s);
                break;
            case 3:
                imageView.setImageResource(R.mipmap.icon_vip3_s);
                break;
            case 4:
                imageView.setImageResource(R.mipmap.icon_vip4_s);
                break;
            case 5:
                imageView.setImageResource(R.mipmap.icon_vip5_s);
                break;
            case 6:
                imageView.setImageResource(R.mipmap.icon_vip6_s);
                break;
            case 7:
                imageView.setImageResource(R.mipmap.icon_vip7_s);
                break;
            case 8:
                imageView.setImageResource(R.mipmap.icon_vip8_s);
                break;
            case 9:
                imageView.setImageResource(R.mipmap.icon_vip9_s);
                break;
            case 10:
                imageView.setImageResource(R.mipmap.icon_vip10_s);
                break;
            case 11:
                imageView.setImageResource(R.mipmap.icon_vip11_s);
            case 12:
                imageView.setImageResource(R.mipmap.icon_vip12_s);
                break;
            case 13:
                imageView.setImageResource(R.mipmap.icon_vip13_s);
                break;
            case 14:
                imageView.setImageResource(R.mipmap.icon_vip14_s);
                break;
            case 15:
                imageView.setImageResource(R.mipmap.icon_vip15_s);
                break;
            case 16:
                imageView.setImageResource(R.mipmap.icon_vip16_s);
                break;
            case 17:
                imageView.setImageResource(R.mipmap.icon_vip17_s);
                break;
            case 18:
                imageView.setImageResource(R.mipmap.icon_vip17_s);
                break;
            case 19:
                imageView.setImageResource(R.mipmap.icon_vip19_s);
                break;
            case 20:
                imageView.setImageResource(R.mipmap.icon_vip20_s);
                break;
            default:
                imageView.setImageResource(R.mipmap.icon_vip20_s);
                break;
        }
    }

    public static void setVipToTextView(Context context, TextView textView, int grade) {
        int resId;
        switch (grade) {
            case 0:
//                imageView.setImageDrawable(new BitmapDrawable());
                resId = 0;
                break;
            case 1:
                resId = R.mipmap.icon_vip1_s;
                break;
            case 2:
                resId = R.mipmap.icon_vip2_s;
                break;
            case 3:
                resId = R.mipmap.icon_vip3_s;
                break;
            case 4:
                resId = R.mipmap.icon_vip4_s;
                break;
            case 5:
                resId = R.mipmap.icon_vip5_s;
                break;
            case 6:
                resId = R.mipmap.icon_vip6_s;
                break;
            case 7:
                resId = R.mipmap.icon_vip7_s;
                break;
            case 8:
                resId = R.mipmap.icon_vip8_s;
                break;
            case 9:
                resId = R.mipmap.icon_vip9_s;
                break;
            case 10:
                resId = R.mipmap.icon_vip10_s;
                break;
            default:
                resId = 0;
                break;
        }

        Drawable drawable = null;
        if (resId != 0) {
            drawable = context.getResources().getDrawable(resId);
            //一定要加这行！！！！！！！！！！！
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textView.setCompoundDrawables(null, null, drawable, null);
        }
    }

    public static void setImageSize(final Context context, final ImageView imageView, final String url) {
        final RequestOptions options = new RequestOptions().transform(new GlideRoundTransform(context, 8))
                .dontAnimate()
                .centerCrop()
                .sizeMultiplier(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        ;
        Glide.with(context).asBitmap().load(url).apply(options).listener(new RequestListener<Bitmap>() {

            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {

                return false;
            }
        }).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                int width = resource.getWidth();
                int height = resource.getHeight();
                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                float ratio = width / height;
                if (ratio < 0.4) {
                    width = 204; //这是从微信截图的长度最后需要同一除以 3
                    height = 410;
//            $img.parentElement.classList.add('overflowHeight'); //设置高度溢出部分隐藏
                } else if (ratio >= 0.4 && ratio <= 0.5) {
                    width = 204;
                    height = (int) (204 / ratio);
                } else if (ratio > 0.5 && ratio < 1) {
                    width = (int) (405 * ratio);
                    height = 405;
                } else if (ratio >= 1 && ratio < 1 / 0.5) { //和前面的宽高转置
                    height = (int) (405 * (1 / ratio));
                    width = 405;
                } else if (ratio >= 1 / 0.5 && ratio < 1 / 0.4) {
                    height = 204;
                    width = (int) (204 / (1 / ratio));
                } else if (ratio >= 1 / 0.4) {
                    height = 204; //这是从微信截图的长度最后需要同一除以 3
                    width = 410;
//            $img.parentElement.classList.add('overflowWidth');
                } else {
                    height /= 3;
                    width /= 3;
                }

                params.width = width;
                params.height = height;
                imageView.setImageBitmap(resource);
//                Glide.with(context).load(url).apply(options).into(imageView);
            }
        });
    }

    public static final String glide_path = BaseApplication.getInstance().getCacheDir() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR;

    /**
     * 获取Glide造成的缓存大小
     *
     * @return CacheSize
     */
    public static String getCacheSize(Context context) {
        try {
            return getFormatSize(getFolderSize(new File(glide_path)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    private static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 删除指定目录下的文件，这里用于缓存的删除
     *
     * @param filePath       filePath
     * @param deleteThisPath deleteThisPath
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (File file1 : files) {
                        deleteFolderFile(file1.getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {
                        file.delete();
                    } else {
                        if (file.listFiles().length == 0) {
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 格式化单位
     *
     * @param size size
     * @return size
     */
    private static String getFormatSize(double size) {

        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "M";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }


}
