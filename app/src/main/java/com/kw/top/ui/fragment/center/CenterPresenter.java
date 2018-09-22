package com.kw.top.ui.fragment.center;

import android.os.SystemClock;
import android.util.Log;

import com.kw.top.app.BaseApplication;
import com.kw.top.base.BasePresenterImpl;
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

import org.json.JSONException;
import org.json.JSONObject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: zy
 * data  : 2018/6/13
 * des   :
 */

public class CenterPresenter extends BasePresenterImpl<CenterContract.View> implements CenterContract.Presenter {

    private String userToken;

    @Override
    public void upPhoto(final String path, String token) {
        userToken = token;
        Api.getApiService().getUpToken(token)
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
                        String qiniuToken = (String) baseBean.getData();
                        Log.e("tag", "=============  token " + qiniuToken);
                        upPictoQiniu(qiniuToken,path);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.upPhotoResult(null,"");
                    }
                });
    }

    private void upPictoQiniu(String qiniuToken, String path) {
        String key = CommandTools.getAndroidId(BaseApplication.getInstance())+ SystemClock.currentThreadTimeMillis();
        QiniuUpLoadManager.getInstance().uploadFile(path, key, qiniuToken, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                //res包含hash、key等信息，具体字段取决于上传策略的设置
                if (info.isOK()) {
                    String resKey = "";
                    try {
                        resKey = response.getString("key");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    upImage(resKey);

                } else {
                    Log.i("qiniu", "===============Upload Fail");
                    //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                    RxToast.normal("上传失败,请重新上传");
                    mView.upPhotoResult(null,"");
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

    private void upImage(final String resKey) {
        Api.getApiService().upPhoto(resKey,userToken)
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
                        mView.upPhotoResult(baseBean,resKey);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.upPhotoResult(null,"");
                    }
                });
    }
}
