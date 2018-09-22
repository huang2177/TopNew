package com.kw.top.bean;

import java.util.List;

/**
 * author: zy
 * data  : 2018/6/6
 * des   :
 */

public class DiamondBean {

    private List<ProductlListBean> productlList;

    public List<ProductlListBean> getProductlList() {
        return productlList;
    }

    public void setProductlList(List<ProductlListBean> productlList) {
        this.productlList = productlList;
    }

    public static class ProductlListBean {
        /**
         * amount : 10000
         * id : 4
         * changeAmount : 13000
         */

        private String amount;
        private int id;
        private String changeAmount;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getChangeAmount() {
            return changeAmount;
        }

        public void setChangeAmount(String changeAmount) {
            this.changeAmount = changeAmount;
        }
    }
}
