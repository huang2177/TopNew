package com.kw.top.bean;

import java.util.List;

/**
 * author: zy
 * data  : 2018/6/10
 * des   :
 */

public class UserCircleBean {

    /**
     * thumbsUpNumAndThumbsNumSum : {"thumbsNumSum":0,"thumbsUpNumSum":0}
     * dynamicList : [{"thumbsUpNum":"0","dynamicId":23,"createTime":"06月09日","dynamicPic":"[\"http://p9u9lweaa.bkt.clouddn.com/c520c3385e1734952079\"","thumbsNum":"0","textContent":"嘎嘎嘎","userId":5}]
     */

    private ReceiveBeam thumbsUpNumAndThumbsNumSum;
    private List<TopCircleBean> dynamicList;

    public ReceiveBeam getReceive() {
        return thumbsUpNumAndThumbsNumSum;
    }

    public void setReceive(ReceiveBeam thumbsUpNumAndThumbsNumSum) {
        this.thumbsUpNumAndThumbsNumSum = thumbsUpNumAndThumbsNumSum;
    }

    public List<TopCircleBean> getDynamicList() {
        return dynamicList;
    }

    public void setDynamicList(List<TopCircleBean> dynamicList) {
        this.dynamicList = dynamicList;
    }

    public static class ReceiveBeam {
        /**
         * thumbsNumSum : 0
         * thumbsUpNumSum : 0
         */

        private int thumbsNumSum;
        private int thumbsUpNumSum;

        public int getThumbsNumSum() {
            return thumbsNumSum;
        }

        public void setThumbsNumSum(int thumbsNumSum) {
            this.thumbsNumSum = thumbsNumSum;
        }

        public int getThumbsUpNumSum() {
            return thumbsUpNumSum;
        }

        public void setThumbsUpNumSum(int thumbsUpNumSum) {
            this.thumbsUpNumSum = thumbsUpNumSum;
        }
    }
}
