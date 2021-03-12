package me.jingbin.byrecyclerview.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 修复 addData() 后接着 setLoadMoreEnabled(false) 导致的崩溃问题
 * link to https://github.com/youlookwhat/ByRecyclerView
 */
public class ByLinearLayoutManager extends LinearLayoutManager {

    public ByLinearLayoutManager(Context context) {
        super(context);
    }

    public ByLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public ByLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
