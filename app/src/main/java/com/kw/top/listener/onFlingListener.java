package com.kw.top.listener;

/**
 * author: zy
 * data  : 2018/5/20
 * des   :
 */

public interface onFlingListener {
    void removeFirstObjectInAdapter();
    void onLeftCardExit(Object dataObject);
    void onRightCardExit(Object dataObject);
    void onAdapterAboutToEmpty(int itemsInAdapter);
    void onScroll(float progress, float scrollXProgress);
}
