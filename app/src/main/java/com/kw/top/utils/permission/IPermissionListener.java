package com.kw.top.utils.permission;

/**
 * Created by zzy on 2018/1/9.
 * mail: 623932889@qq.com
 * Describe:
 */

public interface IPermissionListener {

    /**
     * 权限允许的回调
     */
    void onPermissionGranted();

    /**
     * 权限拒绝的回调
     */
    void onPermissionDenied();

}
