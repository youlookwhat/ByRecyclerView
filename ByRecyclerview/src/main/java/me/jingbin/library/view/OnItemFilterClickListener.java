package me.jingbin.library.view;

import android.view.View;

import me.jingbin.library.ByRecyclerView;

/**
 * item的点击事件，避免在1秒内触发多次点击
 *
 * @author jingbin
 */
public abstract class OnItemFilterClickListener implements ByRecyclerView.OnItemClickListener {

    private long mLastClickTime = 0;
    private long mTimeInterval = 1000L;

    public OnItemFilterClickListener() {

    }

    public OnItemFilterClickListener(long interval) {
        this.mTimeInterval = interval;
    }

    @Override
    public void onClick(View v, int position) {
        long nowTime = System.currentTimeMillis();
        if (nowTime - mLastClickTime > mTimeInterval) {
            mLastClickTime = nowTime;
            // 单次点击事件
            onSingleClick(v, position);
        }
    }

    /**
     * 单次点击事件
     */
    protected abstract void onSingleClick(View v, int position);

}
