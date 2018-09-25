package com.kw.top.ui.activity.news;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kw.top.bean.BaseBean;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.ConstantValue;
import com.kw.top.ui.activity.user_center.RedPacketDetailsActivity;
import com.kw.top.utils.SPUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Des:发红包辅助类
 * Created by huang on 2018/9/24.
 */

public class RedPacketHelper {
    private long mClickTime;
    private Context mContext;

    public RedPacketHelper(Context context) {
        mContext = context;
    }

    public void sendRedPacket(Intent data) {
        String redbagId = data.getStringExtra("REDBAG_ID");
        String content = data.getStringExtra("CONTENT");
        Log.e("-----------", redbagId);
    }

    /**
     * 领取红包点击事件
     *
     * @param redbagId
     */
    private void clickRedbag(final String redbagId) {
        if (System.currentTimeMillis() - mClickTime <= 1000) {
            return;
        }
        mClickTime = System.currentTimeMillis();
        Api.getApiService().clickRedPackage(redbagId, SPUtils.getString(mContext, ConstantValue.KEY_TOKEN, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        if (baseBean.isSuccess()) {
                            //保存领取过的红包ID
//                            EaseSPUtils.saveString(mContext, redbagId, redbagId);
//                            messageList.refresh();
                        }
                        RedPacketDetailsActivity.startActivity(mContext, redbagId);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }
}
