package me.jingbin.byrecyclerview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayoutManager;

/**
 * 处理 FlexboxLayoutManager 在RecyclerView中添加HeaderView或EmptyView
 * link to https://github.com/youlookwhat/ByRecyclerView
 */
public class MyFlexboxLayoutManager extends FlexboxLayoutManager {

    public MyFlexboxLayoutManager(Context context) {
        super(context);
    }

    public MyFlexboxLayoutManager(Context context, int flexDirection) {
        super(context, flexDirection);
    }

    public MyFlexboxLayoutManager(Context context, int flexDirection, int flexWrap) {
        super(context, flexDirection, flexWrap);
    }

    public MyFlexboxLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 将LayoutParams转换成新的FlexboxLayoutManager.LayoutParams
     */
    @Override
    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        if (lp instanceof RecyclerView.LayoutParams) {
            return new FlexboxLayoutManager.LayoutParams((RecyclerView.LayoutParams) lp);
        } else if (lp instanceof ViewGroup.MarginLayoutParams) {
            return new FlexboxLayoutManager.LayoutParams((ViewGroup.MarginLayoutParams) lp);
        } else {
            return new FlexboxLayoutManager.LayoutParams(lp);
        }
    }
}
