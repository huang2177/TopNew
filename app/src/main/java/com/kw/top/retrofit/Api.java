package com.kw.top.retrofit;

import android.util.Log;

import com.kw.top.R;
import com.kw.top.app.Utils;
import com.kw.top.tools.CommandTools;
import com.kw.top.utils.RxToast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author: 正义
 * date  : 2018/4/16
 * desc  :
 */

public class Api {

    private static ApiService sService;

    //请求超时时间
    private static final int DEFAULT_TIMEOUT = 1000 * 30;
    //缓存大小
    private static final long CACHE_SIZE = 10 * 1024 * 1024;

    public static ApiService getApiService() {
        if (null == sService) {
            //设置缓存 10M
            File httpCacheDirectory = new File(Utils.getContext().getCacheDir(),"response");
            Cache cache = new Cache(httpCacheDirectory,CACHE_SIZE);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(DEFAULT_TIMEOUT,TimeUnit.SECONDS)
                    .writeTimeout(DEFAULT_TIMEOUT,TimeUnit.SECONDS)
                    .cache(cache)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(HttpHost.url)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            sService = retrofit.create(ApiService.class);
        }
        return sService;
    }

    /**
     * 线判断网络，网络好时，移除header后添加缓存失效时间为1小时，网络不好时为4周
     */
    private static Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            request = request.newBuilder()
                    .addHeader("X-LC-Id", "LdRmh1jxSc4X16hFI52Orqcf-gzGzoHsz")
                    .addHeader("X-LC-Key", "pqI26wmUTcsl8RF2bYXolaAf")
                    .addHeader("Content-Type", "application/json")
                    .build();
            if (!CommandTools.isNetConnected(Utils.getContext())) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response response = chain.proceed(request);
            final ResponseBody responseBody = response.body();
            final long contentLength = responseBody.contentLength();
            Log.e("smile", "App     response  = " + response);
            Log.e("tag", "===============  response  code " + response.code());
            if (response.code() == 504) {
                RxToast.normal(Utils.getContext().getResources().getString(R.string.net_error));
            }
            if (response.code() == 200) {
                if (CommandTools.isNetConnected(Utils.getContext())) {
                    int maxAge = 0 * 60; // 有网络时 设置缓存超时时间0个小时
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")////清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                            .build();
                } else {
                    int maxStale = 60 * 60 * 24 * 28; // 无网络时，设置超时为4周
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                    RxToast.normal(Utils.getContext().getString(R.string.net_error));
                }
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();
                Charset charset = Charset.forName("UTF-8");
                MediaType contentType = responseBody.contentType();
                charset = contentType.charset(charset);
                if (contentLength != 0) {
                    Log.e("tag", "--------------------------------------------开始打印返回数据----------------------------------------------------");
                    String jsonString = buffer.clone().readString(charset);
                    Log.e("tag", "====="+buffer.clone().readString(charset));
                    Log.e("tag", "--------------------------------------------结束打印返回数据----------------------------------------------------");
                }
            } else if (response.code() == 504) {
                RxToast.normal("当前无网络");
            }
            return response;
        }
    };

}
