package com.kw.top.ui.activity.HomePage;

import com.kw.top.bean.BaseBean;

/**
 * Created by shibing on 2018/9/25.
 */

public interface AnchorView {


    /**
     * 查询所有的礼物
     *
     * @param baseBean
     */
    void queryAllGiftResult(BaseBean baseBean);


    /**
     * 送礼物添加好友
     *
     * @param baseBean
     */
    void addFriendResult(BaseBean baseBean);


    /**
     * 送礼物
     *
     * @param baseBean
     */
    void sendGiftResult(BaseBean baseBean);



}
