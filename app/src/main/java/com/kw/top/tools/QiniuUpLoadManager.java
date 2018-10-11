package com.kw.top.tools;

import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

/**
 * author: zy
 * data  : 2018/6/5
 * des   :
 */

public class QiniuUpLoadManager {

    private UploadManager mUploadManager;

    private static QiniuUpLoadManager instance = new QiniuUpLoadManager();

    Configuration config = new Configuration.Builder()
            .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
            .putThreshhold(512 * 1024)   // 启用分片上传阀值。默认512K
            .connectTimeout(10)           // 链接超时。默认10秒
            .useHttps(true)               // 是否使用https上传域名
            .responseTimeout(60)          // 服务器响应超时。默认60秒
//                .recorder(null)           // recorder分片上传时，已上传片记录器。默认null
//                .recorder(null, null)   // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
//                .zone(FixedZone.zone0)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
            .build();

    public static QiniuUpLoadManager getInstance() {
        if (instance == null) {
            instance = new QiniuUpLoadManager();
        }
        return instance;
    }

    public QiniuUpLoadManager() {
        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        if (mUploadManager == null) {
            mUploadManager = new UploadManager(config);
        }
    }

    /**
     * 上传文件
     *
     * @param path
     * @param key
     * @param token
     * @param upCompletionHandler
     * @param uploadOptions
     */
    public void uploadFile(String path, String key, String token, UpCompletionHandler upCompletionHandler, UploadOptions uploadOptions) {
        mUploadManager.put(path, key, token, upCompletionHandler, uploadOptions);
    }

    public void uploadFileBytes(byte[] bytes, String key, String token, UpCompletionHandler upCompletionHandler, UploadOptions uploadOptions) {
        mUploadManager.put(bytes, key, token, upCompletionHandler, uploadOptions);
    }

}
