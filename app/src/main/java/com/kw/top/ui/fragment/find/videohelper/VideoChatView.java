package com.kw.top.ui.fragment.find.videohelper;

import com.kw.top.bean.BaseBean;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.Logger;
import com.kw.top.ui.fragment.find.bean.RoomNumBean;
import com.kw.top.ui.fragment.find.bean.WatchStateBean;
import com.kw.top.utils.RxToast;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Des: 直播界面相关 请求数据
 * Created by huang on 2018/10/2 0002 11:07
 */
public class VideoChatView {
    private VideoChatHelper mHelper;

    public VideoChatView(VideoChatHelper helper) {
        mHelper = helper;
    }

    /**
     * 创建房间
     */
    public void createRoom(String anchorId, String audienceId, String token) {
        Api.getApiService().createRoom(anchorId, audienceId, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean<RoomNumBean>>() {
                    @Override
                    public void call(BaseBean<RoomNumBean> baseBean) {
                        if (baseBean.isSuccess()) {
                            mHelper.bindCreateRoom(baseBean.getData().roomNum);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        RxToast.normal("打开视频聊天失败！");
                    }
                });
    }


    public void updateCallNum(String userId, String token) {
        Api.getApiService().updateCallNum(userId, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        if (baseBean.isSuccess()) {
                            Logger.e("-------", "更新用户被呼叫次数成功");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        RxToast.normal("请求失败！");
                    }
                });
    }

    /**
     * 关闭房间
     */
    public void closeRoom(String roomNum, String token) {
        Api.getApiService().closeRoom(roomNum, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    /**
     * 查看用户钻石是否够继续观看直播
     */
    public void isAdequate(String roomNum, String token) {
        Api.getApiService().isAdequate(roomNum, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean<WatchStateBean>>() {
                    @Override
                    public void call(BaseBean<WatchStateBean> baseBean) {
                        if (baseBean.isSuccess()) {
                            mHelper.isAdequate(baseBean.getData().watchState);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

}
