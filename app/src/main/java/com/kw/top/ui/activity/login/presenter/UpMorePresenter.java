package com.kw.top.ui.activity.login.presenter;

import android.app.Activity;
import android.os.SystemClock;
import android.util.Log;

import com.kw.top.app.BaseApplication;
import com.kw.top.bean.BaseBean;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.CommandTools;
import com.kw.top.tools.QiniuUpLoadManager;
import com.kw.top.utils.RxToast;
import com.luck.picture.lib.entity.LocalMedia;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/10/9.
 */
public class UpMorePresenter {
    private Activity mActivity;
    private RegisterView registerView;
    private List<String> listKey;

    public UpMorePresenter(Activity mActivity, RegisterView registerView) {
        this.mActivity = mActivity;
        this.registerView = registerView;
        listKey = new ArrayList<>();
    }


    /**
     * 七牛文件上传接口
     */
    public void getQiniuToken(String token, final List<LocalMedia> mList) {
        Api.getApiService().getUpToken(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                    }
                })
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        if (baseBean.isSuccess()) {
                            String token = (String) baseBean.getData();
                            Log.e("tag", "=============  token " + token);
                            upPictoQiniu(token, mList);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }


    /**
     * 上传单张图片
     *
     * @param qiniuToken
     * @param mList
     */
    private void upPictoQiniu(String qiniuToken, List<LocalMedia> mList) {
        listKey.clear();
        for (int i = 0; i < mList.size(); i++) {
            String path = mList.get(i).getPath();
            String key = CommandTools.getAndroidId(BaseApplication.getInstance()) + SystemClock.currentThreadTimeMillis();
            QiniuUpLoadManager.getInstance().uploadFile(path, key, qiniuToken, new UpCompletionHandler() {
                @Override
                public void complete(String key, ResponseInfo info, JSONObject response) {
                    //res包含hash、key等信息，具体字段取决于上传策略的设置
                    if (info.isOK()) {
                        String resKey = "";
                        try {
                            resKey = response.getString("key");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        listKey.add(resKey);
                        registerView.UpMorePicSuccess(listKey);
                        // upImage(resKey);

                    } else {
                        Log.i("qiniu", "===============Upload Fail");
                        //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                        RxToast.normal("上传失败,请重新上传");
                    }
                    Log.i("qiniu", "===================" + key + ",\r\n " + info + ",\r\n " + response);
                }

            }, new UploadOptions(null, null, false, new UpProgressHandler() {
                @Override
                public void progress(String key, double percent) {

                }
            }, new UpCancellationSignal() {
                @Override
                public boolean isCancelled() {
                    return false;
                }
            }));
        }
    }


    public void addGirlData(Map<String, String> map) {
        Api.getApiService().addGirlData(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        registerView.UpGirlInfoSuccess(baseBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        registerView.UpGirlInfoSuccess(null);
                    }
                });
    }

}
