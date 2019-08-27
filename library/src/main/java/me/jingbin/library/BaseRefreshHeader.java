package me.jingbin.library;

/**
 * 刷新头必须实现此接口
 *
 * @author jingbin
 */
interface BaseRefreshHeader {

    /**
     * 正常
     */
    int STATE_NORMAL = 0;
    /**
     * 释放刷新
     */
    int STATE_RELEASE_TO_REFRESH = 1;
    /**
     * 正在刷新
     */
    int STATE_REFRESHING = 2;
    /**
     * 刷新完成
     */
    int STATE_DONE = 3;

    void onMove(float delta);

    boolean releaseAction();

    void refreshComplete();

    void setState(int state);

    int getState();

    int getVisibleHeight();
}
