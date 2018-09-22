package com.kw.top.utils.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.kw.top.utils.MainHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzy on 2018/1/9.
 * mail: 623932889@qq.com
 * Describe:
 */

public class MPermissionUtils {

    private static int mRequestCode = -1;

    public static void requestPermissionsResult(Activity activity, int requestCode
            , String[] permission, IPermissionListener callback){
        requestPermissions(activity, requestCode, permission, callback);
    }

    public static void requestPermissionsResult(android.app.Fragment fragment, int requestCode
            , String[] permission, IPermissionListener callback){
        requestPermissions(fragment, requestCode, permission, callback);
    }

    public static void requestPermissionsResult(android.support.v4.app.Fragment fragment, int requestCode
            , String[] permission, IPermissionListener callback){
        requestPermissions(fragment, requestCode, permission, callback);
    }

    /**
     * 请求权限处理
     * @param object        activity or fragment
     * @param requestCode   请求码
     * @param permissions   需要请求的权限
     * @param callback      结果回调
     */
    @TargetApi(Build.VERSION_CODES.M)
    private static void requestPermissions(Object object, int requestCode
            , String[] permissions, IPermissionListener callback){

        checkCallingObjectSuitability(object);
        mOnPermissionListener = callback;

        if(checkPermissions(getContext(object), permissions)){
            if(mOnPermissionListener != null)
                mOnPermissionListener.onPermissionGranted();
        }else{
            List<String> deniedPermissions = getDeniedPermissions(getContext(object), permissions);
            if(deniedPermissions.size() > 0){
                mRequestCode = requestCode;
                if(object instanceof Activity){
                    ((Activity) object).requestPermissions(deniedPermissions
                            .toArray(new String[deniedPermissions.size()]), requestCode);
                }else if(object instanceof android.app.Fragment){
                    ((android.app.Fragment) object).requestPermissions(deniedPermissions
                            .toArray(new String[deniedPermissions.size()]), requestCode);
                }else if(object instanceof android.support.v4.app.Fragment){
                    ((android.support.v4.app.Fragment) object).requestPermissions(deniedPermissions
                            .toArray(new String[deniedPermissions.size()]), requestCode);
                }else{
                    mRequestCode = -1;
                }
            }
        }
    }

    /**
     * 获取上下文
     */
    private static Context getContext(Object object) {
        Context context;
        if(object instanceof android.app.Fragment){
            context = ((android.app.Fragment) object).getActivity();
        }else if(object instanceof android.support.v4.app.Fragment){
            context = ((android.support.v4.app.Fragment) object).getActivity();
        }else{
            context = (Activity) object;
        }
        return context;
    }

    /**
     * 请求权限结果，对应onRequestPermissionsResult()方法。
     */
    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(mRequestCode != -1 && requestCode == mRequestCode){
            if(verifyPermissions(grantResults)){
                if(mOnPermissionListener != null)
                    mOnPermissionListener.onPermissionGranted();
            }else{
                if(mOnPermissionListener != null)
                    mOnPermissionListener.onPermissionDenied();
            }
        }
    }

    /**
     * 显示提示对话框
     */
    public static void showTipsDialog(final Context context){
        new AlertDialog.Builder(context)
                .setTitle("提示信息")
                .setMessage("当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings(context);
                    }
                }).show();
    }

    public static void checkRecoder(final Context context, final MediaRecorder mRecorder){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int flag1 = 0;
                for (int i = 0; i < 5; i++) {
                    try {
                        Thread.sleep(200);
                        if(mRecorder != null){
                            mRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
                                @Override
                                public void onError(MediaRecorder mr, int what, int extra) {
                                    Log.e("tag" ,mr.toString() + what + extra);
                                }
                            });
                            int maxAmplitude = mRecorder.getMaxAmplitude();
                            flag1 += maxAmplitude;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
                if (flag1 == 0) {
                    MainHandler.getInstance().post(new Runnable() {
                        public void run() {
                            //没有权限的操作
                            showTipsDialog(context);
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 启动当前应用设置页面
     * @param context
     */
    private static void startAppSettings(Context context){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:"+context.getPackageName()));
        context.startActivity(intent);
    }

    /**
     * 验证权限是否已经授权
     * @param grantResults
     * @return
     */
    private static boolean verifyPermissions(int[] grantResults){
        for (int grantResult : grantResults){
            if (grantResult != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    /**
     * 获取权限列表中所有需要授权的权限
     * @param context
     * @param permissions
     * @return
     */
    private static List<String> getDeniedPermissions(Context context, String... permissions){
        List<String> deniedPermissions = new ArrayList<>();
        for (String permission : permissions){
            if (ContextCompat.checkSelfPermission(context,permission)== PackageManager.PERMISSION_DENIED){
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions;
    }

    /**
     * 检查传递对象的正确性
     * @param object 必须为activity or fragment
     */
    private static void checkCallingObjectSuitability(Object object){
        if(object == null){
            throw new NullPointerException("Activity or fragment should not be null");
        }

        boolean isActivity = object instanceof Activity;
        boolean isSupportFragment = object instanceof android.support.v4.app.Fragment;
        boolean isAppFragment = object instanceof android.app.Fragment;
        if (!(isActivity || isSupportFragment || isAppFragment)){
            throw new IllegalArgumentException(
                    "Caller must be an Activity or a Fragment"
            );
        }
    }

    /**
     * 检查所有的权限是否已经授权
     * @param context
     * @param permissions 权限列表
     * @return
     */
    private static boolean checkPermissions(Context context, String... permissions){
        if(isOverMarshmallow()){
            for (String permission : permissions) {
                if(ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断当前手机API版本是否>=6.0
     */
    private static boolean isOverMarshmallow(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private static IPermissionListener mOnPermissionListener;

}
