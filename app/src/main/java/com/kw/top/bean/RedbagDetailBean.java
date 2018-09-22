package com.kw.top.bean;

import java.util.List;

/**
 * author  ： zy
 * date    ： 2018/7/14
 * des     ：
 */

public class RedbagDetailBean {

    /**
     * redPackageDetails : {"headImg":"c520c3385e1734953944","nickName":"Nice","shareSum":"1","surplusShareSum":"1","redPackageType":"0","amountSum":"100"}
     * redPackageDetailsList : [{"amount":"100","headImg":"c520c3385e1734953944","nickName":"Nice","getTime":"07月15日","grade":"15"}]
     */

    private RedPackageDetailsBean redPackageDetails;
    private List<RedPackageDetailsListBean> redPackageDetailsList;

    public RedPackageDetailsBean getRedPackageDetails() {
        return redPackageDetails;
    }

    public void setRedPackageDetails(RedPackageDetailsBean redPackageDetails) {
        this.redPackageDetails = redPackageDetails;
    }

    public List<RedPackageDetailsListBean> getRedPackageDetailsList() {
        return redPackageDetailsList;
    }

    public void setRedPackageDetailsList(List<RedPackageDetailsListBean> redPackageDetailsList) {
        this.redPackageDetailsList = redPackageDetailsList;
    }

    public static class RedPackageDetailsBean {
        /**
         * headImg : c520c3385e1734953944
         * nickName : Nice
         * shareSum : 1
         * surplusShareSum : 1
         * redPackageType : 0
         * amountSum : 100
         */

        private String headImg;
        private String nickName;
        private String shareSum; //发放份数
        private int surplusShareSum; //剩余份数
        private String redPackageType;
        private String amountSum;

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getShareSum() {
            return shareSum;
        }

        public void setShareSum(String shareSum) {
            this.shareSum = shareSum;
        }

        public int getSurplusShareSum() {
            return surplusShareSum;
        }

        public void setSurplusShareSum(int surplusShareSum) {
            this.surplusShareSum = surplusShareSum;
        }

        public String getRedPackageType() {
            return redPackageType;
        }

        public void setRedPackageType(String redPackageType) {
            this.redPackageType = redPackageType;
        }

        public String getAmountSum() {
            return amountSum;
        }

        public void setAmountSum(String amountSum) {
            this.amountSum = amountSum;
        }
    }

    public static class RedPackageDetailsListBean {
        /**
         * amount : 100
         * headImg : c520c3385e1734953944
         * nickName : Nice
         * getTime : 07月15日
         * grade : 15
         */

        private String amount;
        private String headImg;
        private String nickName;
        private String getTime;
        private int grade;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getGetTime() {
            return getTime;
        }

        public void setGetTime(String getTime) {
            this.getTime = getTime;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }
    }
}
