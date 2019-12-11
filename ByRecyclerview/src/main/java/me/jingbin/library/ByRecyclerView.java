package me.jingbin.library;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;

import me.jingbin.library.adapter.BaseByRecyclerViewAdapter;

/**
 * @author jingbin
 * https://github.com/youlookwhat/ByRecyclerView
 */
public class ByRecyclerView extends RecyclerView {

    /**
     * Set the ItemViewType of the multiType adapter to a value less than 10000!!
     * 多类型适配器的ItemViewType需要设置为小于10000的值!!
     */
    private static final int TYPE_REFRESH_HEADER = 10000;     // RefreshHeader type
    private static final int TYPE_LOAD_MORE_VIEW = 10001;     // LoadingMore type
    private static final int TYPE_STATE_VIEW = 10002;         // StateView type
    private static final int TYPE_FOOTER_VIEW = 10003;        // FooterView type
    private static final int HEADER_INIT_INDEX = 10004;       // HeaderView starting type
    private List<Integer> mHeaderTypes = new ArrayList<>();   // HeaderView type list
    private ArrayList<View> mHeaderViews = new ArrayList<>(); // HeaderView view list
    private LinearLayout mFooterLayout;                       // FooterView layout
    private FrameLayout mStateLayout;                         // StateView  layout

    private boolean mRefreshEnabled = false;              // 是否 启动下拉刷新
    private boolean mLoadMoreEnabled = false;             // 是否 启动加载更多
    private boolean mHeaderViewEnabled = false;           // 是否 显示 HeaderView
    private boolean mFootViewEnabled = false;             // 是否 显示 FooterView
    private boolean mStateViewEnabled = true;             // 是否 显示 StateView
    private boolean misNoLoadMoreIfNotFullScreen = false; // 是否 不满一屏不加载更多

    private boolean mIsLoadingData = false;        // 是否正在加载更多
    private boolean mIsNoMore = false;             // 是否没有更多数据了
    private boolean mIsScrollUp = false;           // 手指是否上滑
    private float mLastY = -1;                     // 手指按下的Y坐标值，用于处理下拉刷新View的高度
    private float mPullStartY = 0;                 // 手指按下的Y坐标值，用于处理不满全屏时是否可进行上拉加载
    private float mPullMaxY;                       // 手指上滑最高点的值，值越小位置越高
    private float mDragRate = 2.5f;                // 下拉时候的偏移计量因子，越小拉动距离越短
    private long mLoadMoreDelayMillis = 0;         // 延迟多少毫秒后再调用加载更多接口

    private OnRefreshListener mRefreshListener;    // 下拉刷新监听
    private BaseRefreshHeader mRefreshHeader;      // 自定义下拉刷新布局需要实现的接口
    private OnLoadMoreListener mLoadMoreListener;  // 加载更多监听
    private BaseLoadMore mLoadMore;                // 自定义加载更多布局需要实现的接口

    private OnItemClickListener onItemClickListener;                      // item  点击事件
    private OnItemLongClickListener onItemLongClickListener;              // item  长按事件
    private OnItemChildClickListener mOnItemChildClickListener;           // 子View 点击事件
    private OnItemChildLongClickListener mOnItemChildLongClickListener;   // 子View 长按事件

    private AppBarStateChangeListener.State appbarState = AppBarStateChangeListener.State.EXPANDED;
    private final RecyclerView.AdapterDataObserver mDataObserver = new DataObserver();

    private WrapAdapter mWrapAdapter;

    public ByRecyclerView(Context context) {
        this(context, null);
    }

    public ByRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ByRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) {
            return;
        }
        init();
    }

    private void init() {
        mLoadMore = new SimpleLoadMoreView(getContext());
        mLoadMore.setState(BaseLoadMore.STATE_COMPLETE);
    }

    /**
     * Add the header layout using the layout id.
     *
     * @param layoutResId
     */
    public void addHeaderView(int layoutResId) {
        addHeaderView(getLayoutView(layoutResId));
    }

    /**
     * Add a header layout before the content list or empty layout.
     *
     * @param headerView
     */
    public void addHeaderView(View headerView) {
        mHeaderTypes.add(HEADER_INIT_INDEX + mHeaderViews.size());
        mHeaderViews.add(headerView);
        mHeaderViewEnabled = true;
        if (mWrapAdapter != null) {
            mWrapAdapter.getOriginalAdapter().notifyItemInserted(getPullHeaderSize() + getHeaderViewCount() - 1);
        }
    }

    /**
     * Determine which HeaderView it is based on itemType
     */
    private View getHeaderViewByType(int itemType) {
        if (!isHeaderType(itemType)) {
            return null;
        }
        return mHeaderViews.get(itemType - HEADER_INIT_INDEX);
    }

    /**
     * Determines whether a type is HeaderType
     */
    private boolean isHeaderType(int itemViewType) {
        return mHeaderViewEnabled && getHeaderViewCount() > 0 && mHeaderTypes.contains(itemViewType);
    }

    /**
     * Determine if it is the itemViewType reserved by ByRecyclerView
     */
    private boolean isReservedItemViewType(int itemViewType) {
        return itemViewType == TYPE_REFRESH_HEADER || itemViewType == TYPE_LOAD_MORE_VIEW || itemViewType == TYPE_STATE_VIEW || mHeaderTypes.contains(itemViewType);
    }

    /**
     * Set custom to LoadingMoreView
     */
    public void setLoadingMoreView(BaseLoadMore loadingMore) {
        mLoadMore = loadingMore;
        mLoadMore.setState(BaseLoadMore.STATE_COMPLETE);
    }

    /**
     * Set custom to RefreshHeaderView
     */
    public void setRefreshHeaderView(BaseRefreshHeader refreshHeader) {
        mRefreshHeader = refreshHeader;
    }

    /**
     * Load more complete. You can also continue the pull up.
     */
    public void loadMoreComplete() {
        if (getLoadMoreSize() == 0) {
            return;
        }
        mIsNoMore = false;
        mIsLoadingData = false;
        mLoadMore.setState(BaseLoadMore.STATE_COMPLETE);
    }

    /**
     * The end. Cannot be loaded with pull up.
     */
    public void loadMoreEnd() {
        mIsLoadingData = false;
        mIsNoMore = true;
        mLoadMore.setState(BaseLoadMore.STATE_NO_MORE);
    }

    /**
     * Load more failures. Continue to pull up or click load.
     */
    public void loadMoreFail() {
        if (getLoadMoreSize() == 0 || mLoadMore.getFailureView() == null || mLoadMoreListener == null) {
            return;
        }
        mIsLoadingData = false;
        mLoadMore.setState(BaseLoadMore.STATE_FAILURE);
        mLoadMore.getFailureView().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsLoadingData = true;
                mLoadMore.setState(BaseLoadMore.STATE_LOADING);
                if (mLoadMoreDelayMillis <= 0) {
                    mLoadMoreListener.onLoadMore();
                } else {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mLoadMoreListener.onLoadMore();
                        }
                    }, mLoadMoreDelayMillis);
                }
            }
        });
    }

    /**
     * Set refresh status. false: Reset to load more states
     *
     * @param refreshing true: Start fresh； false: fresh complete
     */
    public void setRefreshing(boolean refreshing) {
        if (refreshing) {
            if (getPullHeaderSize() == 0 || mRefreshHeader.getState() == BaseRefreshHeader.STATE_REFRESHING) {
                return;
            }
            LayoutManager layoutManager = getLayoutManager();
            if (layoutManager != null) {
                layoutManager.scrollToPosition(0);
            }
            mRefreshHeader.setState(BaseRefreshHeader.STATE_REFRESHING);
            if (mRefreshListener != null) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshListener.onRefresh();
                    }
                }, 300);
            }
        } else {
            if (getPullHeaderSize() > 0) {
                mRefreshHeader.refreshComplete();
            }
            loadMoreComplete();
        }
    }

    /**
     * Set whether to enable drop-down refresh.
     */
    public void setRefreshEnabled(boolean enabled) {
        mRefreshEnabled = enabled;
        if (mRefreshHeader == null) {
            mRefreshHeader = new SimpleRefreshHeaderView(getContext());
        }
    }

    /**
     * Set whether to start loading more.
     */
    public void setLoadMoreEnabled(boolean enabled) {
        mLoadMoreEnabled = enabled;
        if (!enabled) {
            mLoadMore.setState(BaseLoadMore.STATE_COMPLETE);
        }
    }

    /**
     * Give load more layout a bottom height for parts of the page that need to be displayed transparently.
     *
     * @param heightDp Unit of dp
     */
    public void setLoadingMoreBottomHeight(float heightDp) {
        mLoadMore.setLoadingMoreBottomHeight(heightDp);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof BaseByRecyclerViewAdapter) {
            ((BaseByRecyclerViewAdapter) adapter).setRecyclerView(this);
        }
        mWrapAdapter = new WrapAdapter(adapter);
        super.setAdapter(mWrapAdapter);
        adapter.registerAdapterDataObserver(mDataObserver);
        mDataObserver.onChanged();
        setRefreshing(false);
    }

    /**
     * Avoid the ClassCastException caused by the user's own call to getAdapter().
     */
    @Override
    public Adapter getAdapter() {
        if (mWrapAdapter != null) {
            return mWrapAdapter.getOriginalAdapter();
        } else {
            return null;
        }
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        if (mWrapAdapter != null) {
            if (layout instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) layout);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (mWrapAdapter.isHeaderView(position)
                                || mWrapAdapter.isFootView(position)
                                || mWrapAdapter.isLoadMore(position)
                                || mWrapAdapter.isStateView(position)
                                || mWrapAdapter.isRefreshHeader(position))
                                ? gridManager.getSpanCount() : 1;
                    }
                });

            }
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE && mLoadMoreListener != null && !mIsLoadingData && mLoadMoreEnabled) {
            LayoutManager layoutManager = getLayoutManager();
            if (layoutManager == null) {
                return;
            }
            int lastVisibleItemPosition = -1;
            if (layoutManager instanceof LinearLayoutManager) {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();

            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastCompletelyVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);

            }
            if (layoutManager.getChildCount() > 0
                    && !mIsNoMore
                    && lastVisibleItemPosition == mWrapAdapter.getItemCount() - 1 // 最后一个完全可视item是最后一个item
                    && isNoFullScreenLoad()
                    && isScrollLoad()
                    && (!mRefreshEnabled || mRefreshHeader.getState() < BaseRefreshHeader.STATE_REFRESHING)) {
                mIsScrollUp = false;
                mIsLoadingData = true;
                mLoadMore.setState(BaseLoadMore.STATE_LOADING);
                if (mLoadMoreDelayMillis <= 0) {
                    mLoadMoreListener.onLoadMore();
                } else {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mLoadMoreListener.onLoadMore();
                        }
                    }, mLoadMoreDelayMillis);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        if (mPullStartY == 0) {
            mPullStartY = ev.getY();
            mPullMaxY = mPullStartY;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // If the item sets the click event, it doesn't get a value here and it stays at 0
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (ev.getY() < mPullMaxY) {
                    mPullMaxY = ev.getY();
                }
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (mRefreshEnabled && mRefreshListener != null && isOnTop() && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                    mRefreshHeader.onMove(deltaY / mDragRate);
                    if (mRefreshHeader.getVisibleHeight() > 0 && mRefreshHeader.getState() < BaseRefreshHeader.STATE_REFRESHING) {
                        return false;
                    }
                }
                break;
            default:
                /*
                 * 判断是否上拉了逻辑：
                 *  开启了加载更多
                 *  按下的纵坐标 - 最后的纵坐标>=-10(为了更灵敏,原点向下惯性滑动时==0)
                 *  最高点的纵坐标 - 松开时的纵坐标<=150(为了防止上滑后然后再下拉)
                 */
                mIsScrollUp = mLoadMoreEnabled && mPullStartY - ev.getY() >= -10 && ev.getY() - mPullMaxY <= 150;

                mPullStartY = 0;
                mLastY = -1;
                if (mRefreshEnabled
                        && isOnTop()
                        && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                    if (mRefreshHeader.releaseAction()) {
                        if (mRefreshListener != null) {
                            mRefreshListener.onRefresh();
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private boolean isOnTop() {
        if (mRefreshHeader != null
                && mRefreshHeader instanceof View
                && ((View) mRefreshHeader).getParent() != null) {
            return true;
        } else {
            return false;
        }
    }

    private class DataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    }

    private class WrapAdapter extends RecyclerView.Adapter<ViewHolder> {

        private RecyclerView.Adapter adapter;

        WrapAdapter(RecyclerView.Adapter adapter) {
            this.adapter = adapter;
        }

        RecyclerView.Adapter getOriginalAdapter() {
            return this.adapter;
        }

        /**
         * Is it a StateView layout
         */
        boolean isStateView(int position) {
            return mStateViewEnabled && mStateLayout != null && position == getHeaderViewCount() + getPullHeaderSize();
        }

        /**
         * Is it a HeaderView layout
         */
        boolean isHeaderView(int position) {
            return mHeaderViewEnabled && position >= getPullHeaderSize() && position < getHeaderViewCount() + getPullHeaderSize();
        }

        /**
         * Is it a FootView layout
         */
        boolean isFootView(int position) {
            if (mFootViewEnabled && mFooterLayout != null && mFooterLayout.getChildCount() != 0) {
                return position == getItemCount() - 1 - getLoadMoreSize();
            } else {
                return false;
            }
        }

        /**
         * Is it a LoadMoreView layout
         */
        boolean isLoadMore(int position) {
            if (mLoadMoreEnabled) {
                return position == getItemCount() - 1;
            } else {
                return false;
            }
        }

        /**
         * Is it a RefreshHeaderView layout
         */
        boolean isRefreshHeader(int position) {
            if (mRefreshEnabled) {
                return position == 0;
            } else {
                return false;
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == TYPE_REFRESH_HEADER) {
                return new SimpleViewHolder((View) mRefreshHeader);
            } else if (isHeaderType(viewType)) {
                return new SimpleViewHolder(getHeaderViewByType(viewType));
            } else if (viewType == TYPE_STATE_VIEW) {
                return new SimpleViewHolder(mStateLayout);
            } else if (viewType == TYPE_FOOTER_VIEW) {
                return new SimpleViewHolder(mFooterLayout);
            } else if (viewType == TYPE_LOAD_MORE_VIEW) {
                return new SimpleViewHolder((View) mLoadMore);
            }
            ViewHolder viewHolder = adapter.onCreateViewHolder(parent, viewType);
            bindViewClickListener(viewHolder);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (isRefreshHeader(position) || isHeaderView(position) || isStateView(position) || isFootView(position)) {
                return;
            }
            int adjPosition = position - getCustomTopItemViewCount();
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    adapter.onBindViewHolder(holder, adjPosition);
                }
            }
        }

        /**
         * some times we need to override this
         */
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> objectList) {
            if (isHeaderView(position) || isRefreshHeader(position) || isStateView(position) || isFootView(position)) {
                return;
            }
            if (adapter != null) {
                // Get the position with the type of the header removed
                int adjPosition = position - getCustomTopItemViewCount();
                int adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    if (objectList.isEmpty()) {
                        adapter.onBindViewHolder(holder, adjPosition);
                    } else {
                        adapter.onBindViewHolder(holder, adjPosition, objectList);
                    }
                }
            }
        }

        @Override
        public int getItemCount() {
            if (adapter != null) {
                return getPullHeaderSize() + getHeaderViewCount() + getFooterViewSize() + getLoadMoreSize() + getStateViewSize() + adapter.getItemCount();
            } else {
                return getPullHeaderSize() + getHeaderViewCount() + getFooterViewSize() + getLoadMoreSize() + getStateViewSize();
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (isRefreshHeader(position)) {
                return TYPE_REFRESH_HEADER;
            }
            if (isHeaderView(position)) {
                position = position - getPullHeaderSize();
                return mHeaderTypes.get(position);
            }
            if (isFootView(position)) {
                return TYPE_FOOTER_VIEW;
            }
            if (isStateView(position)) {
                return TYPE_STATE_VIEW;
            }
            if (isLoadMore(position)) {
                return TYPE_LOAD_MORE_VIEW;
            }
            int adapterCount;
            if (adapter != null) {
                int adjPosition = position - getCustomTopItemViewCount();
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    int type = adapter.getItemViewType(adjPosition);
                    if (isReservedItemViewType(type)) {
                        throw new IllegalStateException("ByRecyclerView require itemViewType in adapter should be less than 10000 !");
                    }
                    return type;
                }
            }
            return 0;
        }

        @Override
        public long getItemId(int position) {
            if (adapter != null && position >= getCustomTopItemViewCount()) {
                int adjPosition = position - getCustomTopItemViewCount();
                if (adjPosition < adapter.getItemCount()) {
                    return adapter.getItemId(adjPosition);
                }
            }
            return -1;
        }

        @Override
        public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        // 占一行
                        return (isHeaderView(position)
                                || isFootView(position)
                                || isLoadMore(position)
                                || isStateView(position)
                                || isRefreshHeader(position))
                                ? gridManager.getSpanCount() : 1;
                    }
                });
            }
            adapter.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
            adapter.onDetachedFromRecyclerView(recyclerView);
        }

        @Override
        public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams
                    && (isHeaderView(holder.getLayoutPosition())
                    || isFootView(holder.getLayoutPosition())
                    || isRefreshHeader(holder.getLayoutPosition())
                    || isLoadMore(holder.getLayoutPosition())
                    || isStateView(holder.getLayoutPosition()))) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }

        @Override
        public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
            adapter.onViewDetachedFromWindow(holder);
        }

        @Override
        public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
            adapter.onViewRecycled(holder);
        }

        @Override
        public boolean onFailedToRecycleView(@NonNull RecyclerView.ViewHolder holder) {
            return adapter.onFailedToRecycleView(holder);
        }

        @Override
        public void unregisterAdapterDataObserver(@NonNull AdapterDataObserver observer) {
            adapter.unregisterAdapterDataObserver(observer);
        }

        @Override
        public void registerAdapterDataObserver(@NonNull AdapterDataObserver observer) {
            adapter.registerAdapterDataObserver(observer);
        }

        private class SimpleViewHolder extends RecyclerView.ViewHolder {
            SimpleViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    /**
     * Get the number of FooterView
     */
    int getFooterViewSize() {
        return mFootViewEnabled && mFooterLayout != null && mFooterLayout.getChildCount() != 0 ? 1 : 0;
    }

    /**
     * Get the number of StateView
     */
    public int getStateViewSize() {
        return mStateViewEnabled && mStateLayout != null && mStateLayout.getChildCount() != 0 ? 1 : 0;
    }

    /**
     * Get the number of HeaderView
     */
    public int getHeaderViewCount() {
        return mHeaderViewEnabled ? mHeaderViews.size() : 0;
    }

    /**
     * Set the click event and the long press event to the itemView
     */
    private void bindViewClickListener(final ViewHolder viewHolder) {
        if (viewHolder == null) {
            return;
        }
        final View view = viewHolder.itemView;
        if (onItemClickListener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(v, viewHolder.getLayoutPosition() - getCustomTopItemViewCount());
                }
            });
        }
        if (onItemLongClickListener != null) {
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onItemLongClickListener.onLongClick(v, viewHolder.getLayoutPosition() - getCustomTopItemViewCount());
                }
            });
        }
    }

    /**
     * Number of custom type header layouts = RefreshView + HeaderView + StateView
     * 自定义类型头部布局的个数：用于取到正确的position
     */
    public int getCustomTopItemViewCount() {
        return getHeaderViewCount() + getPullHeaderSize() + getStateViewSize();
    }

    public interface OnLoadMoreListener {

        void onLoadMore();
    }

    public interface OnRefreshListener {

        void onRefresh();
    }

    /**
     * @param delayMillis How many milliseconds is the delay before the call OnLoadMoreListener
     */
    public void setOnLoadMoreListener(OnLoadMoreListener listener, long delayMillis) {
        setLoadMoreEnabled(true);
        mLoadMoreListener = listener;
        mLoadMoreDelayMillis = delayMillis;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        setLoadMoreEnabled(true);
        mLoadMoreListener = listener;
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        setRefreshEnabled(true);
        mRefreshListener = listener;
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // solve CollapsingToolbarLayout conflict
        AppBarLayout appBarLayout = null;
        ViewParent p = getParent();
        while (p != null) {
            if (p instanceof CoordinatorLayout) {
                break;
            }
            p = p.getParent();
        }
        if (p != null) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) p;
            final int childCount = coordinatorLayout.getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                final View child = coordinatorLayout.getChildAt(i);
                if (child instanceof AppBarLayout) {
                    appBarLayout = (AppBarLayout) child;
                    break;
                }
            }
            if (appBarLayout != null) {
                appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
                    @Override
                    public void onStateChanged(AppBarLayout appBarLayout, State state) {
                        appbarState = state;
                    }
                });
            }
        }
    }

    /**
     * Distinguish whether the refresh layout needs to be counted
     * If you use the drop-down refresh that comes with the control, you need to count position
     */
    public int getPullHeaderSize() {
        if (mRefreshEnabled) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * If a pull-up refresh is used, position needs to be counted
     */
    private int getLoadMoreSize() {
        if (mLoadMoreEnabled) {
            return 1;
        } else {
            return 0;
        }
    }

    public void setEmptyView(int layoutResId) {
        setStateView(layoutResId);
    }

    public void setEmptyView(View emptyView) {
        setStateView(emptyView);
    }

    /**
     * Sets whether the EmptyView is displayed
     */
    public void setEmptyViewEnabled(boolean stateViewEnabled) {
        setStateViewEnabled(stateViewEnabled);
    }

    /**
     * @param layoutResId layoutResId
     */
    public void setStateView(int layoutResId) {
        setStateView(getLayoutView(layoutResId));
    }

    /**
     * Sets whether the StateView is displayed
     */
    public void setStateViewEnabled(boolean stateViewEnabled) {
        this.mStateViewEnabled = stateViewEnabled;
    }

    /**
     * Set the status layout, which can include: EmptyView, LoadingView, ErrorView, etc
     * The positions are:
     * RefreshView -> HeaderViews -> StateView -> [list ContentView] -> FooterView -> LoadMoreView
     */
    public void setStateView(View stateView) {
        boolean insert = false;
        if (mStateLayout == null) {
            mStateLayout = new FrameLayout(stateView.getContext());
            final LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            final ViewGroup.LayoutParams lp = stateView.getLayoutParams();
            if (lp != null) {
                layoutParams.width = lp.width;
                layoutParams.height = lp.height;
            }
            mStateLayout.setLayoutParams(layoutParams);
            insert = true;
        }
        mStateLayout.removeAllViews();
        mStateLayout.addView(stateView);
        mStateViewEnabled = true;
        if (insert) {
            if (getStateViewSize() == 1) {
                int position = getHeaderViewCount() + getPullHeaderSize();
                if (mWrapAdapter != null) {
                    mWrapAdapter.getOriginalAdapter().notifyItemInserted(position);
                }
            }
        }
    }

    /**
     * Obtain View through layoutResId
     *
     * @param layoutResId layoutResId
     */
    private View getLayoutView(int layoutResId) {
        return LayoutInflater.from(getContext()).inflate(layoutResId, (ViewGroup) this.getParent(), false);
    }

    public int addFooterView(int layoutResId) {
        return addFooterView(getLayoutView(layoutResId), -1, LinearLayout.VERTICAL);
    }

    public int addFooterView(View footer) {
        return addFooterView(footer, -1, LinearLayout.VERTICAL);
    }

    public int addFooterView(View footer, int index) {
        return addFooterView(footer, index, LinearLayout.VERTICAL);
    }

    public int addFooterView(View footer, int index, int orientation) {
        if (mFooterLayout == null) {
            mFooterLayout = new LinearLayout(footer.getContext());
            if (orientation == LinearLayout.VERTICAL) {
                mFooterLayout.setOrientation(LinearLayout.VERTICAL);
                mFooterLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            } else {
                mFooterLayout.setOrientation(LinearLayout.HORIZONTAL);
                mFooterLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        }
        final int childCount = mFooterLayout.getChildCount();
        if (index < 0 || index > childCount) {
            index = childCount;
        }
        mFooterLayout.addView(footer, index);
        mFootViewEnabled = true;
        if (mFooterLayout.getChildCount() == 1) {
            if (mWrapAdapter != null) {
                int position = mWrapAdapter.getOriginalAdapter().getItemCount() + getCustomTopItemViewCount();
                if (position != -1) {
                    mWrapAdapter.getOriginalAdapter().notifyItemInserted(position);
                }
            }
        }
        return index;
    }

    public void setHeaderViewEnabled(boolean headerViewEnabled) {
        this.mHeaderViewEnabled = headerViewEnabled;
    }

    public void setFootViewEnabled(boolean footViewEnabled) {
        this.mFootViewEnabled = footViewEnabled;
    }

    /**
     * Less than a screen, according to the distance up to determine whether to load
     */
    private boolean isScrollLoad() {
        return isFullScreen() || mIsScrollUp;
    }

    /**
     * Set less than one screen not to load
     */
    public void setNotFullScreenNoLoadMore() {
        misNoLoadMoreIfNotFullScreen = true;
    }

    /**
     * If a screen is not loaded. default load
     */
    private boolean isNoFullScreenLoad() {
        if (misNoLoadMoreIfNotFullScreen) {
            return isFullScreen();
        } else {
            return true;
        }
    }

    /**
     * Is it full screen
     */
    private boolean isFullScreen() {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager == null) {
            return false;
        }
        if (layoutManager instanceof LinearLayoutManager) {
            final LinearLayoutManager llm = (LinearLayoutManager) layoutManager;

            return (llm.findLastCompletelyVisibleItemPosition() + 1) != mWrapAdapter.getItemCount() ||
                    llm.findFirstCompletelyVisibleItemPosition() != 0;

        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager sglm = (StaggeredGridLayoutManager) layoutManager;

            int[] last = new int[sglm.getSpanCount()];
            sglm.findLastCompletelyVisibleItemPositions(last);

            int[] first = new int[sglm.getSpanCount()];
            sglm.findFirstCompletelyVisibleItemPositions(first);

            return findMax(last) + 1 != mWrapAdapter.getItemCount() || first[0] != 0;
        }
        return false;
    }

    /**
     * remove HeaderView
     * tip:
     * You cannot add the HeaderView again after removing it,
     * because the type may repeat, causing the add to fail
     */
    public void removeHeaderView(@NonNull View header) {
        if (getHeaderViewCount() == 0) {
            return;
        }
        if (mWrapAdapter != null) {
            int index = -1;
            for (int i = 0; i < mHeaderViews.size(); i++) {
                if (mHeaderViews.get(i) == header) {
                    mHeaderViews.remove(mHeaderViews.get(i));
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                mHeaderTypes.remove(index);
                mWrapAdapter.getOriginalAdapter().notifyItemRemoved(getPullHeaderSize() + index);
            }
        }
    }

    /**
     * remove all HeaderView
     */
    public void removeAllHeaderView() {
        if (getHeaderViewCount() == 0) {
            return;
        }
        mHeaderViewEnabled = false;
        if (mWrapAdapter != null) {
            mHeaderViews.clear();
            mHeaderTypes.clear();
            mWrapAdapter.getOriginalAdapter().notifyItemRangeRemoved(getPullHeaderSize(), getHeaderViewCount());
        }
    }

    /**
     * remove FooterView
     * Because you're dealing with the layout in one item,
     * you don't have to refresh the layout unless you delete all the FooterView
     *
     * @param footer
     */
    public void removeFooterView(View footer) {
        if (mFootViewEnabled && mFooterLayout != null && mFooterLayout.getChildCount() != 0) {
            mFooterLayout.removeView(footer);
            if (mWrapAdapter != null && mFooterLayout.getChildCount() == 0) {
                int position = mWrapAdapter.getOriginalAdapter().getItemCount() + getCustomTopItemViewCount();
                if (position != -1) {
                    mWrapAdapter.getOriginalAdapter().notifyItemRemoved(position);
                }
            }
        }
    }

    /**
     * remove all footer view from mFooterLayout and set null to mFooterLayout
     */
    public void removeAllFooterView() {
        if (mFootViewEnabled && mFooterLayout != null && mFooterLayout.getChildCount() != 0) {
            mFooterLayout.removeAllViews();
            if (mWrapAdapter != null) {
                int position = mWrapAdapter.getOriginalAdapter().getItemCount() + getCustomTopItemViewCount();
                if (position != -1) {
                    mWrapAdapter.getOriginalAdapter().notifyItemRemoved(position);
                }
            }
        }
    }

    /**
     * 设置下拉时候的偏移计量因子。y = deltaY/mDragRate，默认值 3
     * The larger, the longer the user has to scroll down to trigger the pull-down refresh.
     * The smaller the opposite, the shorter the distance
     *
     * @param rate The larger, the longer the user has to scroll down to trigger the pull-down refresh. The smaller the opposite, the shorter the distance
     *             越大，意味着，用户要下拉滑动更久来触发下拉刷新。相反越小，就越短距离
     */
    public void setDragRate(float rate) {
        if (rate <= 0.5) {
            return;
        }
        mDragRate = rate;
    }

    /**
     * Whether load more ongoing
     */
    public boolean isLoadingMore() {
        return mIsLoadingData;
    }

    /**
     * Whether refreshing
     */
    public boolean isRefreshing() {
        return mRefreshHeader != null && mRefreshHeader.getState() == BaseRefreshHeader.STATE_REFRESHING;
    }

    public interface OnItemClickListener {

        void onClick(View v, int position);
    }

    public interface OnItemLongClickListener {

        boolean onLongClick(View v, int position);
    }

    public interface OnItemChildClickListener {

        void onItemChildClick(View view, int position);
    }

    public interface OnItemChildLongClickListener {

        boolean onItemChildLongClick(View view, int position);
    }

    /**
     * holder.setByRecyclerView(getRecyclerView()).addOnClickListener(R.id.tv_text);
     *
     * @return The callback to be invoked with an itemchild in this RecyclerView has
     * been clicked, or null id no callback has been set.
     */
    @Nullable
    public final OnItemChildClickListener getOnItemChildClickListener() {
        return mOnItemChildClickListener;
    }

    /**
     * holder.setByRecyclerView(getRecyclerView()).addOnLongClickListener(R.id.tv_text);
     *
     * @return The callback to be invoked with an itemChild in this RecyclerView has
     * been long clicked, or null id no callback has been set.
     */
    @Nullable
    public final OnItemChildLongClickListener getOnItemChildLongClickListener() {
        return mOnItemChildLongClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    public void setOnItemChildClickListener(OnItemChildClickListener listener) {
        mOnItemChildClickListener = listener;
    }

    public void setOnItemChildLongClickListener(OnItemChildLongClickListener listener) {
        mOnItemChildLongClickListener = listener;
    }

    /**
     * call it when you finish the activity,
     */
    public void destroy() {
        mRefreshEnabled = false;
        mLoadMoreEnabled = false;
        if (mHeaderViews != null) {
            mHeaderViews.clear();
            mHeaderViews = null;
        }
        if (mHeaderTypes != null) {
            mHeaderTypes.clear();
            mHeaderTypes = null;
        }
        if (mFooterLayout != null) {
            mFooterLayout.removeAllViews();
            mFooterLayout = null;
        }
        if (mStateLayout != null) {
            mStateLayout.removeAllViews();
            mStateLayout = null;
        }
    }
}
