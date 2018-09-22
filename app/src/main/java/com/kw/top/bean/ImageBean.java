package com.kw.top.bean;

import java.io.Serializable;

/**
 * author  ： zy
 * date    ： 2018/7/4
 * des     ：
 */

public class ImageBean implements Serializable{
    /**
     * dynamicPic : c520c3385e17349511384
     */

    private String dynamicPic;
    private String dynamicVideo;
    private String picOrVideoType; //1 视频 0图片

    public ImageBean(String dynamicPic){
        this.dynamicPic = dynamicPic;
    }

    public String getPicOrVideoType() {
        return picOrVideoType;
    }

    public void setPicOrVideoType(String picOrVideoType) {
        this.picOrVideoType = picOrVideoType;
    }

    public String getDynamicVideo() {
        return dynamicVideo;
    }

    public void setDynamicVideo(String dynamicVideo) {
        this.dynamicVideo = dynamicVideo;
    }

    public String getDynamicPic() {
        return dynamicPic;
    }

    public void setDynamicPic(String dynamicPic) {
        this.dynamicPic = dynamicPic;
    }
}
