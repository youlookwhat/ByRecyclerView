package me.jingbin.library;

import android.view.View;

/**
 * 自定义加载更多布局 必须实现此接口
 *
 * @author jingbin
 */
public interface BaseLoadMore {

    int STATE_LOADING = 0;  // 加载中...，显示布局
    int STATE_COMPLETE = 1; // 加载完成，隐藏布局
    int STATE_NO_MORE = 2;  // 没有更多内容了，显示布局
    int STATE_FAILURE = 3;  // 加载失败，请点我重试，显示布局

    /**
     * 设置对应加载状态
     */
    void setState(int state);

    /**
     * 失败布局view，为了设置点击事件
     */
    View getFailureView();

    /**
     * 给加载更多底部增加一定的空白高度
     *
     * @param heightDp 单位dp
     */
    void setLoadingMoreBottomHeight(float heightDp);
}
