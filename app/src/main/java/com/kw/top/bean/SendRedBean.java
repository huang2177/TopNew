package com.kw.top.bean;

import android.text.TextUtils;

import java.util.List;

/**
 * author  ： zy
 * date    ： 2018/6/16
 * des     ：
 */

public class SendRedBean {

    /**
     * sendRedPackageSum : 2000
     * sendRedPackageList : [{"valid":"1","redPackageId":48,"shareSum":"20","surplusShareSum":"20","amountSum":"1000"},{"valid":"1","redPackageId":49,"shareSum":"2","surplusShareSum":"2","amountSum":"1000"}]
     */

    private String sendRedPackageSum;
    private List<SendRedPackageListBean> sendRedPackageList;

    public String getSendRedPackageSum() {
        return sendRedPackageSum;
    }

    public void setSendRedPackageSum(String sendRedPackageSum) {
        this.sendRedPackageSum = sendRedPackageSum;
    }

    public List<SendRedPackageListBean> getSendRedPackageList() {
        return sendRedPackageList;
    }

    public void setSendRedPackageList(List<SendRedPackageListBean> sendRedPackageList) {
        this.sendRedPackageList = sendRedPackageList;
    }

    public static class SendRedPackageListBean {
        /**
         * valid : 1
         * redPackageId : 48
         * shareSum : 20
         * surplusShareSum : 20
         * amountSum : 1000
         */

        private String valid;
        private String shareSum; //发放的份数
        private String surplusShareSum; //剩余份数
        private String amountSum;
        private String redPackageId;

        public String getValid() {
            return valid;
        }

        public void setValid(String valid) {
            this.valid = valid;
        }

        public String getRedPackageId() {
            return redPackageId;
        }

        public void setRedPackageId(String redPackageId) {
            this.redPackageId = redPackageId;
        }

        public String getShareSum() {
            return shareSum;
        }

        public void setShareSum(String shareSum) {
            this.shareSum = shareSum;
        }

        public String getSurplusShareSum() {
            if (TextUtils.isEmpty(surplusShareSum)){
                return "0";
            }
            return surplusShareSum;
        }

        public void setSurplusShareSum(String surplusShareSum) {
            this.surplusShareSum = surplusShareSum;
        }

        public String getAmountSum() {
            return amountSum;
        }

        public void setAmountSum(String amountSum) {
            this.amountSum = amountSum;
        }
    }
}
