package com.kw.top.bean;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by zzy on 2018/1/9.
 * mail: 623932889@qq.com
 * Describe:
 */

public class BaseBean<T> implements Serializable {

    private T data;
    private String code;
    private String msg;


    public String getMsg() {
        return msg;
    }

    public String getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        if ("0000".equals(getCode())) {
            return true;
        }
        return false;
    }

    public String getJsonData(){
        return new Gson().toJson(data);
    }

}
