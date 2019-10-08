package me.jingbin.library;

/**
 * 自定义加载更多布局 必须实现此接口
 *
 * @author jingbin
 */
interface BaseLoadingMore {

    /**
     * 加载中...，显示布局
     */
    int STATE_LOADING = 0;
    /**
     * 加载完成，隐藏布局
     */
    int STATE_COMPLETE = 1;
    /**
     * 没有更多数据，显示布局
     */
    int STATE_NO_MORE = 2;

    /**
     * 设置对应加载状态
     */
    void setState(int state);


}
