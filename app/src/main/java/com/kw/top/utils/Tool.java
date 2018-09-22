package com.kw.top.utils;

import android.widget.TextView;

import com.kw.top.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by shibing on 2018/9/18.
 */

public class Tool {


    /**
     * 倒计时
     *
     * @param textView 控件
     * @param waitTime 倒计时总时长
     * @param interval 倒计时的间隔时间
     * @param hint     倒计时完毕时显示的文字
     */
    public static void countDown(final TextView textView, long waitTime, long interval, final String hint) {
        textView.setEnabled(false);
        android.os.CountDownTimer timer = new android.os.CountDownTimer(waitTime, interval) {

            @Override
            public void onTick(long millisUntilFinished) {
                textView.setText((millisUntilFinished / 1000) + " 秒重新获取");
                textView.setBackgroundResource(R.drawable.shape_818181);
            }

            @Override
            public void onFinish() {
                textView.setEnabled(true);
                textView.setText(hint);
                textView.setBackgroundResource(R.drawable.shape_yellow_bg);
            }
        };
        timer.start();
    }


    public static boolean isChinaPhoneLegal(String str)
            throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147,145))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
