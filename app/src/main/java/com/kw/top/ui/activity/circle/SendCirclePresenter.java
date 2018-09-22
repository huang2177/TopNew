package com.kw.top.ui.activity.circle;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.kw.top.app.BaseApplication;
import com.kw.top.base.BasePresenterImpl;
import com.kw.top.bean.BaseBean;
import com.kw.top.retrofit.Api;
import com.kw.top.tools.CommandTools;
import com.kw.top.tools.QiniuUpLoadManager;
import com.kw.top.utils.ArrayUtils;
import com.kw.top.utils.RxToast;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: zy
 * data  : 2018/6/4
 * des   :
 */

public class SendCirclePresenter extends BasePresenterImpl<SendCircleContract.View> implements SendCircleContract.Presenter {

    private List<String> urls = new ArrayList<>();
    private int count = 0;
    private String content;//朋友圈内容
    private String dynamicType; //top圈类型('01'.公共圈,'02'.私密圈)
    private String myToken;
    private int type = 0;//0图片 1视频
    private String taskId;
    private String activeId;
    private boolean isGroupTask;

    @Override
    public void upFile(String myToken,List<String> paths, final String content, String dynamicType,
                       int type,String taskId,String activeId,boolean isGroupTask) {
        this.content = content;
        this.myToken = myToken;
        this.dynamicType = dynamicType;
        this.type = type;
        this.taskId = taskId;
        this.activeId = activeId;
        this.isGroupTask = isGroupTask;
        getQiniuToken(paths);
    }

    private void getQiniuToken(final List<String> paths){
        //获得七牛Token
        Api.getApiService().getUpToken(myToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        String token = (String) baseBean.getData();
                        upFileToQiniu(token, paths);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.upFiledResult();
                    }
                });
    }

    //上传到七牛
    private void upFileToQiniu(String token, final List<String> paths) {
        urls.clear();
        count =0;
        for (int i = 0; i < paths.size(); i++) {
            String path = paths.get(i);
            String key = CommandTools.getAndroidId(BaseApplication.getInstance()) + SystemClock.currentThreadTimeMillis();

            QiniuUpLoadManager.getInstance().uploadFile(path, key, token, new UpCompletionHandler() {
                @Override
                public void complete(String key, ResponseInfo info, JSONObject response) {
                    //res包含hash、key等信息，具体字段取决于上传策略的设置
                    if (info.isOK()) {
                        ++count;
                        String resKey = "";
                        try {
                            resKey = response.getString("key");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        urls.add(resKey);
                        if (count >= paths.size()) {
                            if (!TextUtils.isEmpty(taskId)){
                                if (isGroupTask){
                                    finishClubTask(taskId,ArrayUtils.arrayToStr(urls));
                                }else {
                                    //完成任务
                                    finishTask(taskId,ArrayUtils.arrayToStr(urls),myToken);
                                }
                            }else if (!TextUtils.isEmpty(activeId)){
                                addActive(ArrayUtils.arrayToStr(urls));
                            }else {
                                //发送朋友圈
                                sendFriendCircle(ArrayUtils.arrayToStr(urls));
                            }

                        }
                    } else {
                        Log.i("qiniu", "===============Upload Fail");
                        //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                        RxToast.normal("上传失败,请重新上传");
                        mView.upFiledResult();
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

    //完成任务
    @Override
    public void finishTask(String taskId, String urlAddress, String token) {
        Api.getApiService().finishTask(taskId,urlAddress,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        mView.finishTaskResult(baseBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.finishTaskResult(null);
                    }
                });
    }

    //完成俱乐部任务
    public void finishClubTask(String taskId, String urlAddress){
        String taskPic="",taskVideo="";
        if (type == 0) {
            taskPic = urlAddress;
        } else {
            taskVideo = urlAddress;
        }
        Api.getApiService().userFinishClubTask(taskId,taskPic,taskVideo,myToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        mView.finishTaskResult(baseBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.finishTaskResult(null);
                    }
                });
    }

    //参加活动
    public void addActive(String urls) {
        String activityPic="",activityVideo = "";
        if (type == 0) {
            activityPic = urls;
        } else {
            activityVideo = urls;
        }
        Api.getApiService().addActive(activeId,activityPic,activityVideo,myToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        mView.addActiveResult(baseBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.addActiveResult(null);
                    }
                });
    }

    //发送朋友圈
    private void sendFriendCircle(String url) {
        String dynamicPic = "", dynamicVideo = "";
        if (type == 0) {
            dynamicPic = url;
        } else {
            dynamicVideo = url;
        }
        Log.e("tag","===============   urls " + url);
        Api.getApiService().sendTopCircle(content, dynamicPic, dynamicVideo, dynamicType, myToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        mView.upSuccessResult();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.upFiledResult();
                        RxToast.normal("上传失败");
                    }
                });
    }
}
