package me.jingbin.library.stickyview;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.jingbin.library.adapter.BaseByRecyclerViewAdapter;

/**
 * @author jingbin
 * original author：https://github.com/smuyyh/StickyHeaderRecyclerView
 */
public class StickyLinearLayoutManager extends LinearLayoutManager {

    private BaseByRecyclerViewAdapter mBaseAdapter;
    private StickyHeaderHandler mHeaderHandler;

    private List<Integer> mHeaderPositions = new ArrayList<>();

    private ViewHolderFactory viewHolderFactory;

    private int headerElevation = StickyHeaderHandler.NO_ELEVATION;

    public StickyLinearLayoutManager(Context context, BaseByRecyclerViewAdapter headerProvider) {
        this(context, VERTICAL, false, headerProvider);
    }

    public StickyLinearLayoutManager(Context context, int orientation, boolean reverseLayout, BaseByRecyclerViewAdapter headerProvider) {
        super(context, orientation, reverseLayout);
        this.mBaseAdapter = headerProvider;
    }

    public void elevateHeaders(boolean elevateHeaders) {
        elevateHeaders(elevateHeaders ? StickyHeaderHandler.DEFAULT_ELEVATION : StickyHeaderHandler.NO_ELEVATION);
    }

    public void elevateHeaders(int dpElevation) {
        this.headerElevation = dpElevation > 0 ? dpElevation : StickyHeaderHandler.NO_ELEVATION;
        if (mHeaderHandler != null) {
            mHeaderHandler.setElevateHeaders(headerElevation);
        }
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        cacheHeaderPositions();
        if (mHeaderHandler != null) {
            resetHeaderHandler();
        }
    }

    @Override
    public void scrollToPosition(int position) {
        super.scrollToPositionWithOffset(position, 0);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int scroll = super.scrollVerticallyBy(dy, recycler, state);
        if (Math.abs(scroll) > 0) {
            if (mHeaderHandler != null) {
                mHeaderHandler.updateHeaderState(findFirstVisibleItemPosition(), getVisibleHeaders(), viewHolderFactory, findFirstCompletelyVisibleItemPosition() == 0);
            }
        }
        return scroll;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int scroll = super.scrollHorizontallyBy(dx, recycler, state);
        if (Math.abs(scroll) > 0) {
            if (mHeaderHandler != null) {
                mHeaderHandler.updateHeaderState(findFirstVisibleItemPosition(), getVisibleHeaders(), viewHolderFactory, findFirstCompletelyVisibleItemPosition() == 0);
            }
        }
        return scroll;
    }

    @Override
    public void removeAndRecycleAllViews(RecyclerView.Recycler recycler) {
        super.removeAndRecycleAllViews(recycler);
        if (mHeaderHandler != null) {
            mHeaderHandler.clearHeader();
        }
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        viewHolderFactory = new ViewHolderFactory(view);
        mHeaderHandler = new StickyHeaderHandler(view);
        mHeaderHandler.setElevateHeaders(headerElevation);
        if (mHeaderPositions.size() > 0) {
            mHeaderHandler.setHeaderPositions(mHeaderPositions);
            resetHeaderHandler();
        }
        super.onAttachedToWindow(view);
    }

    @Override
    public void onDetachedFromWindow(RecyclerView view, RecyclerView.Recycler recycler) {
        if (mHeaderHandler != null) {
            mHeaderHandler.clearVisibilityObserver();
        }
        super.onDetachedFromWindow(view, recycler);
    }

    private void resetHeaderHandler() {
        mHeaderHandler.reset(getOrientation());
        mHeaderHandler.updateHeaderState(findFirstVisibleItemPosition(), getVisibleHeaders(), viewHolderFactory, findFirstCompletelyVisibleItemPosition() == 0);
    }

    private Map<Integer, View> getVisibleHeaders() {
        Map<Integer, View> visibleHeaders = new LinkedHashMap<>();

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            int dataPosition = getPosition(view);
            if (mHeaderPositions.contains(dataPosition)) {
                visibleHeaders.put(dataPosition, view);
            }
        }
        return visibleHeaders;
    }

    private void cacheHeaderPositions() {
        mHeaderPositions.clear();
        List adapterData = mBaseAdapter.getData();
        if (adapterData == null) {
            if (mHeaderHandler != null) {
                mHeaderHandler.setHeaderPositions(mHeaderPositions);
            }
            return;
        }

        for (int i = 0; i < adapterData.size(); i++) {
            if (StickyHeaderHandler.TYPE_STICKY_VIEW == mBaseAdapter.getItemViewType(i)) {
                int customTopItemViewCount = mBaseAdapter.getCustomTopItemViewCount();
                mHeaderPositions.add(i+customTopItemViewCount);
            }
        }
        if (mHeaderHandler != null) {
            mHeaderHandler.setHeaderPositions(mHeaderPositions);
        }
    }

}
