package me.jingbin.library;

/**
 * 下拉刷新布局 必须实现此接口
 *
 * @author jingbin
 */
public interface BaseRefreshHeader {

    int STATE_NORMAL = 0;             // 正常
    int STATE_RELEASE_TO_REFRESH = 1; // 释放刷新
    int STATE_REFRESHING = 2;         // 正在刷新
    int STATE_DONE = 3;               // 刷新完成

    /**
     * 设置对应刷新状态
     */
    void setState(int state);

    /**
     * 返回当前状态
     */
    int getState();

    /**
     * 根据拉动的距离处理刷新状态
     *
     * @param delta 向下拉动的高度
     */
    void onMove(float delta);

    /**
     * 手指释放时的状态处理
     */
    boolean releaseAction();

    /**
     * 刷新完成的处理
     */
    void refreshComplete();

    /**
     * 返回当前的高度
     */
    int getVisibleHeight();
}
