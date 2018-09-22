package com.kw.top.bean;

/**
 * author: zy
 * data  : 2018/5/24
 * des   :
 */

public class MyGiftBean {

        /**
         * redCoupons : 0
         * couponsSum : 500000.00
         * giftCoupons : 200
         */

        private String redCoupons;
        private String couponsSum;
        private int giftCoupons;

        public String getRedCoupons() {
            return redCoupons;
        }

        public void setRedCoupons(String redCoupons) {
            this.redCoupons = redCoupons;
        }

        public String getCouponsSum() {
            return couponsSum;
        }

        public void setCouponsSum(String couponsSum) {
            this.couponsSum = couponsSum;
        }

        public int getGiftCoupons() {
            return giftCoupons;
        }

        public void setGiftCoupons(int giftCoupons) {
            this.giftCoupons = giftCoupons;
        }
}
