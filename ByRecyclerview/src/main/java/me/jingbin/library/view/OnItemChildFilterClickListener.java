package me.jingbin.library.view;

import android.view.View;

import java.util.Calendar;

import me.jingbin.library.ByRecyclerView;

/**
 * 子View的点击事件，避免在1秒内出发多次点击
 *
 * @author jingbin
 */
public abstract class OnItemChildFilterClickListener implements ByRecyclerView.OnItemChildClickListener {

    private static final int BY_MIN_CLICK_DELAY_TIME = 1000;
    private long mLastClickTime = 0;
    private int mViewId = -1;

    @Override
    public void onItemChildClick(View v, int position) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        int viewId = v.getId();
        if (mViewId != viewId) {
            mViewId = viewId;
            mLastClickTime = currentTime;
            onSingleClick(v, position);
            return;
        }
        if (currentTime - mLastClickTime > BY_MIN_CLICK_DELAY_TIME) {
            mLastClickTime = currentTime;
            onSingleClick(v, position);
        } else {
            onFastClick(v, position);
        }
    }

    /**
     * 单次点击事件
     */
    protected abstract void onSingleClick(View v, int position);

    /**
     * 快速点击事件
     */
    protected void onFastClick(View v, int position) {
    }
}
