package com.kw.top.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.IOException;


public class FileUtil {
    public static String getAppCacheDir(Context context) {
        String storageRootPath = null;
        try {
            if (context.getExternalCacheDir() != null) {
                storageRootPath = context.getExternalCacheDir().getCanonicalPath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(storageRootPath)) {
            storageRootPath = Environment.getExternalStorageDirectory() + "/" + context.getPackageName();
        }
        return storageRootPath;
    }
}
