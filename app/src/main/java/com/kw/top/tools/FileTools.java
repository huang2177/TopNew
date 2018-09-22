package com.kw.top.tools;

import android.os.Environment;

import java.io.File;

/**
 * author  ： zy
 * date    ： 2018/6/16
 * des     ：
 */

public class FileTools {

    public static final String SDCARD_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String DEFAULT_CACHE_DIR = SDCARD_DIR + "/PLDroidPlayer";

    public static String getVideoPath(){
        String vieo_path = Environment.getExternalStorageDirectory().getPath() + File.separator + "topvideo";
        return vieo_path;
    }

}
