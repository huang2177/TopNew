package com.kw.top.bean;

import java.util.List;

/**
 * author  ： zy
 * date    ： 2018/6/16
 * des     ：
 */

public class ReceiveRedBean {

        /**
         * getRedAmountSum : 648
         * getRedCouponsList : [{"amount":"84.31","headImg":"aebb28fd2e03c3611933","nickName":"Nice","getTime":"07月19日","grade":"15"},{"amount":"564.29","headImg":"aebb28fd2e03c3611933","nickName":"Nice","getTime":"07月19日","grade":"15"}]
         */

        private int getRedAmountSum;
        private List<GetRedCouponsListBean> getRedCouponsList;

        public int getGetRedAmountSum() {
            return getRedAmountSum;
        }

        public void setGetRedAmountSum(int getRedAmountSum) {
            this.getRedAmountSum = getRedAmountSum;
        }

        public List<GetRedCouponsListBean> getGetRedCouponsList() {
            return getRedCouponsList;
        }

        public void setGetRedCouponsList(List<GetRedCouponsListBean> getRedCouponsList) {
            this.getRedCouponsList = getRedCouponsList;
        }

        public static class GetRedCouponsListBean {
            /**
             * amount : 84.31
             * headImg : aebb28fd2e03c3611933
             * nickName : Nice
             * getTime : 07月19日
             * grade : 15
             */

            private String amount;
            private String headImg;
            private String nickName;
            private String getTime;
            private int grade;
            private String redPackageId;

            public String getRedPackageId() {
                return redPackageId;
            }

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
