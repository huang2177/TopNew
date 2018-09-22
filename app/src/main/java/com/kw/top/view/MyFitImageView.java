package com.kw.top.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kw.top.utils.DisplayUtils;

/**
 * Created by shibing on 2018/9/10.
 */

public class MyFitImageView extends ImageView {


    private int screenWidth;
    private int screenHeight;
    private int displayWidth;
    private int displayHeight;


    public MyFitImageView(Context context) {
        this(context, null);
    }

    public MyFitImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyFitImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        screenWidth = DisplayUtils.getScreenWidth(getContext());
        screenHeight = DisplayUtils.getScreenHeight(getContext());

    }


    public void setSize(int bitmapWidth, int bitmapHeight) {
        displayWidth = screenWidth;
        displayHeight = screenHeight;

        //计算出按比例拉伸后的宽度和高度
        displayWidth = screenHeight * bitmapWidth / bitmapHeight;
        displayHeight = screenWidth * bitmapHeight / bitmapWidth;
        //判断如果以图片高度拉伸到屏幕的高度,按照相应的拉伸比是否能够拉伸超过屏幕宽度或者等于屏幕宽度,否则以另一边为基准

        if (displayWidth >= screenWidth) {
            displayHeight = screenHeight;
        } else {
            displayWidth = screenWidth;
        }

        //TODO 拉伸后截取中间的部分
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) getLayoutParams();
        lp.leftMargin = ((screenWidth - displayWidth) / 4);
        lp.topMargin = ((screenHeight - displayHeight) / 2);
        setLayoutParams(lp);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(displayWidth, displayHeight);
    }

}
