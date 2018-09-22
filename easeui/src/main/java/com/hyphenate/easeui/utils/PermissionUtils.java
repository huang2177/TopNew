package com.hyphenate.easeui.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;

/**
 * author: zy
 * data  : 2018/6/5
 * des   :
 */

public class PermissionUtils {
    public static int REQUEST = 1;


    public static final String S_AUDIO = Manifest.permission.RECORD_AUDIO;


    public static void applyPermission(Activity activity, String[] permission) {
        ActivityCompat.requestPermissions(activity, permission, REQUEST);
    }

    public static boolean checkRequestPermissionResult(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 权限申请回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public static void onRequestPermissionResult(int requestCode, String[] permissions,
                                                 int[] grantResults) {
        if (requestCode == REQUEST) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    handleDeny(permissions[i]);
                }
            }
        }
    }


    private static void handleDeny(String permission) {
        if (PermissionUtils.S_AUDIO.equals(permission)) {
        } else {

        }
    }

}
