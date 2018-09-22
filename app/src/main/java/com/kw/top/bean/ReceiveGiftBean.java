package com.kw.top.bean;

import java.util.List;

/**
 * author  ： zy
 * date    ： 2018/6/16
 * des     ：
 */

public class ReceiveGiftBean {

        /**
         * myGift : [{"giftAmount":"200","amountType":"0","giftName":"黄瓜","giftPicture":"1.jpg","num":"1","cash":"0"}]
         * giftCoupons : 200
         */

        private int giftCoupons;
        private List<ReceiveSendGiftBean> myGift;

        public int getGiftCoupons() {
            return giftCoupons;
        }

        public void setGiftCoupons(int giftCoupons) {
            this.giftCoupons = giftCoupons;
        }

        public List<ReceiveSendGiftBean> getMyGift() {
            return myGift;
        }

        public void setMyGift(List<ReceiveSendGiftBean> myGift) {
            this.myGift = myGift;
        }


}
