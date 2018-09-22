package com.hyphenate.easeui.utils;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hyphenate.easeui.R;

/**
 * author  ： zy
 * date    ： 2018/7/7
 * des     ：
 */

public class GlideUtils {

    private static RequestOptions options;
    private static RequestOptions headOptions;

    public static RequestOptions getOptions() {
        if (options == null)
            options = new RequestOptions()
                    .placeholder(R.drawable.ease_default_expression)
                    .dontAnimate()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
        return options;
    }

    public static RequestOptions getHeadOptions() {
        if (headOptions == null)
            headOptions = new RequestOptions()
                    .placeholder(R.drawable.ease_default_avatar)
                    .dontAnimate()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
        return headOptions;
    }
}
