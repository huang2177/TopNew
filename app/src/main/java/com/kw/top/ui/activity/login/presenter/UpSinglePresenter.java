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
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/10/9.
 */
public class UpSinglePresenter {
    private Activity mActivity;
    private RegisterView registerView;

    public UpSinglePresenter(Activity mActivity, RegisterView registerView) {
        this.mActivity = mActivity;
        this.registerView = registerView;
    }


    /**
     * 七牛文件上传接口
     */
    public void getQiniuToken(String token, final String path) {
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
                            upPictoQiniu(token, path);
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
     * @param path
     */
    private void upPictoQiniu(String qiniuToken, String path) {
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
                    registerView.UpPicSuccsee(resKey);
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


    /**
     * 男生注册 填写资料
     */
    public void manRegister(String nickName, String weChat, String mResKey, String token) {
        Api.getApiService().addNicknameAndWX(mResKey, nickName, weChat, token)
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
                        if (baseBean.isSuccess()) {
                            RxToast.normal("进入TOP");
                            registerView.UpSingInfoSuccess(baseBean);
                        } else {
                            RxToast.normal(baseBean.getMsg());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        RxToast.normal("设置失败");
                    }
                });
    }
}
