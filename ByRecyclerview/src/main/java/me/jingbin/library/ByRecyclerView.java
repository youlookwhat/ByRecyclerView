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
     * 请设置多类型adapter的 ItemViewType 时，将其值设置为小于10000。
     * 如果用户的adapter中的 ItemViewType 值与它们重复将会强制抛出异常。
     */
    private static final int TYPE_REFRESH_HEADER = 10000;     // RefreshHeader type
    private static final int TYPE_LOAD_MORE_VIEW = 10001;     // LoadingMore type
    private static final int TYPE_EMPTY_VIEW = 10002;         // EmptyView type
    private static final int TYPE_FOOTER_VIEW = 10003;        // FooterView type
    private static final int HEADER_INIT_INDEX = 10004;       // HeaderView 起始type
    private List<Integer> mHeaderTypes = new ArrayList<>();   // HeaderView type集合
    private ArrayList<View> mHeaderViews = new ArrayList<>(); // HeaderView view集合
    private LinearLayout mFooterLayout;                       // FooterView 布局
    private FrameLayout mEmptyLayout;                         // EmptyView  布局

    private boolean mRefreshEnabled = false;              // 设置是否 使用下拉刷新
    private boolean mLoadMoreEnabled = false;             // 设置是否 使用加载更多
    private boolean mHeaderViewEnabled = false;           // 设置是否 显示HeaderView布局
    private boolean mFootViewEnabled = false;             // 设置是否 显示FooterView布局
    private boolean mEmptyViewEnabled = true;             // 设置是否 显示EmptyView布局
    private boolean misNoLoadMoreIfNotFullScreen = false; // 设置是否 数据不满一屏时进行加载

    private boolean mIsLoadingData = false;        // 是否正在加载更多
    private boolean mIsNoMore = false;             // 是否没有更多数据了
    private boolean mIsScrollUp = false;           // 手指是否上滑
    private float mLastY = -1;                     // 手指按下的Y坐标值，用于处理下拉刷新View的高度
    private float mPullStartY = 0;                 // 手指按下的Y坐标值，用于处理不满全屏时是否可进行上拉加载
    private float mDragRate = 2.5f;                // 下拉时候的偏移计量因子，越小拉动距离越短

    private OnRefreshListener mRefreshListener;    // 下拉刷新监听
    private BaseRefreshHeader mRefreshHeader;      // 下拉刷新接口
    private OnLoadMoreListener mLoadMoreListener;  // 加载更多监听
    private BaseLoadMore mLoadMore;                // 加载更多接口
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
     * 添加HeaderView；不可重复添加相同的View
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
     * 根据header的ViewType判断是哪个header
     */
    private View getHeaderViewByType(int itemType) {
        if (!isHeaderType(itemType)) {
            return null;
        }
        return mHeaderViews.get(itemType - HEADER_INIT_INDEX);
    }

    /**
     * 判断一个type是否为HeaderType
     */
    private boolean isHeaderType(int itemViewType) {
        return mHeaderViewEnabled && getHeaderViewCount() > 0 && mHeaderTypes.contains(itemViewType);
    }

    /**
     * 判断是否是ByRecyclerView保留的itemViewType
     */
    private boolean isReservedItemViewType(int itemViewType) {
        if (itemViewType == TYPE_REFRESH_HEADER || itemViewType == TYPE_LOAD_MORE_VIEW || itemViewType == TYPE_EMPTY_VIEW || mHeaderTypes.contains(itemViewType)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设置 自定义加载更多View
     */
    public void setLoadingMoreView(BaseLoadMore loadingMore) {
        mLoadMore = loadingMore;
    }

    /**
     * 设置 自定义下拉刷新View
     */
    public void setRefreshHeaderView(BaseRefreshHeader refreshHeader) {
        mRefreshHeader = refreshHeader;
    }

    /**
     * 下拉刷新完成
     */
    public void refreshComplete() {
        if (getPullHeaderSize() > 0) {
            mRefreshHeader.refreshComplete();
        }
        loadMoreComplete();
    }

    /**
     * 加载更多完成
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
     * 没有更多内容
     */
    public void loadMoreEnd() {
        mIsLoadingData = false;
        mIsNoMore = true;
        mLoadMore.setState(BaseLoadMore.STATE_NO_MORE);
    }

    /**
     * 加载更多失败
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
                mLoadMoreListener.onLoadMore();
            }
        });
    }

    /**
     * 手动设置头部刷新
     */
    public void setRefreshing() {
        if (getPullHeaderSize() == 0) {
            return;
        }
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager != null) {
            layoutManager.scrollToPosition(0);
        }
        mRefreshHeader.setState(BaseRefreshHeader.STATE_REFRESHING);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshListener.onRefresh();
            }
        }, 300);
    }

    /**
     * 重置所有状态
     */
    public void reset() {
        refreshComplete();
    }

    /**
     * 设置是否开启下拉刷新
     */
    public void setRefreshEnabled(boolean enabled) {
        mRefreshEnabled = enabled;
        if (mRefreshHeader == null) {
            mRefreshHeader = new SimpleRefreshHeaderView(getContext());
        }
    }

    /**
     * 设置是否开启加载更多
     */
    public void setLoadMoreEnabled(boolean enabled) {
        mLoadMoreEnabled = enabled;
        if (!enabled) {
            mLoadMore.setState(BaseLoadMore.STATE_COMPLETE);
        }
    }

    /**
     * 给加载更多布局一个底部的高度，用于部分页面需要透明显示
     *
     * @param heightDp 单位dp
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
        reset();
    }

    /**
     * 避免用户自己调用getAdapter() 引起的ClassCastException
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
                                || mWrapAdapter.isEmptyView(position)
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
            int lastVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            if (layoutManager.getChildCount() > 0
                    && lastVisibleItemPosition >= layoutManager.getItemCount() - 1
                    && isNoFullScreenLoad(layoutManager)
                    && isScrollLoad(layoutManager)
                    && !mIsNoMore
                    && (!mRefreshEnabled || mRefreshHeader.getState() < BaseRefreshHeader.STATE_REFRESHING)) {
                mIsScrollUp = false;
                mIsLoadingData = true;
                mLoadMore.setState(BaseLoadMore.STATE_LOADING);
                mLoadMoreListener.onLoadMore();
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
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 如果item设置点击事件，则这里获取不到值  一直为0
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
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
                // ==0 原点向下惯性滑动会有效
                // 按下的纵坐标 - 当前的纵坐标(为了更灵敏)
                mIsScrollUp = mLoadMoreEnabled && mPullStartY - ev.getY() >= -10;
                // LogHelper.e("mIsScrollUp:  ", mIsScrollUp + " --- mPullStartY:  " + mPullStartY + " --- " + "ev.getY(): " + ev.getY());

                mPullStartY = 0;
                mLastY = -1;
                if (mRefreshEnabled
                        && mRefreshListener != null
                        && isOnTop()
                        && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                    if (mRefreshHeader.releaseAction()) {
                        mRefreshListener.onRefresh();
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
         * 是否是 EmptyView 布局
         */
        boolean isEmptyView(int position) {
            return mEmptyViewEnabled && mEmptyLayout != null && position == getHeaderViewCount() + getPullHeaderSize();
        }

        /**
         * 是否是 HeaderView 布局
         */
        boolean isHeaderView(int position) {
            return mHeaderViewEnabled && position >= getPullHeaderSize() && position < getHeaderViewCount() + getPullHeaderSize();
        }

        /**
         * 是否是 FooterView 布局
         */
        boolean isFootView(int position) {
            if (mFootViewEnabled && mFooterLayout != null && mFooterLayout.getChildCount() != 0) {
                return position == getItemCount() - 1 - getLoadMoreSize();
            } else {
                return false;
            }
        }

        /**
         * 是否是 上拉加载 footer 布局
         */
        boolean isLoadMore(int position) {
            if (mLoadMoreEnabled) {
                return position == getItemCount() - 1;
            } else {
                return false;
            }
        }

        /**
         * 是否是 头部刷新布局
         */
        boolean isRefreshHeader(int position) {
            if (mRefreshEnabled && mRefreshListener != null) {
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
            } else if (viewType == TYPE_EMPTY_VIEW) {
                return new SimpleViewHolder(mEmptyLayout);
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
            if (isRefreshHeader(position) || isHeaderView(position) || isEmptyView(position) || isFootView(position)) {
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
            if (isHeaderView(position) || isRefreshHeader(position) || isEmptyView(position) || isFootView(position)) {
                return;
            }
            if (adapter != null) {
                // 如果可以下拉刷新，就需要+1
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
                return getPullHeaderSize() + getHeaderViewCount() + getFooterViewSize() + getLoadMoreSize() + getEmptyViewSize() + adapter.getItemCount();
            } else {
                return getPullHeaderSize() + getHeaderViewCount() + getFooterViewSize() + getLoadMoreSize() + getEmptyViewSize();
            }
        }

        /**
         * 获取 item 类型
         */
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
            if (isEmptyView(position)) {
                return TYPE_EMPTY_VIEW;
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
                                || isEmptyView(position)
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
                    || isEmptyView(holder.getLayoutPosition()))) {
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
     * 获取 FooterView 的个数
     */
    int getFooterViewSize() {
        return mFootViewEnabled && mFooterLayout != null && mFooterLayout.getChildCount() != 0 ? 1 : 0;
    }

    /**
     * 获取空布局的个数
     */
    public int getEmptyViewSize() {
        return mEmptyViewEnabled && mEmptyLayout != null && mEmptyLayout.getChildCount() != 0 ? 1 : 0;
    }

    /**
     * 获取 HeaderView的个数
     */
    public int getHeaderViewCount() {
        return mHeaderViewEnabled ? mHeaderViews.size() : 0;
    }

    /**
     * 给itemView设置点击事件和长按事件
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
     * 自定义类型头部布局的个数 = RefreshView + HeaderView  + EmptyView
     */
    public int getCustomTopItemViewCount() {
        return getHeaderViewCount() + getPullHeaderSize() + getEmptyViewSize();
    }

    public interface OnLoadMoreListener {

        void onLoadMore();
    }

    public interface OnRefreshListener {

        void onRefresh();
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
        // 解决和CollapsingToolbarLayout冲突的问题
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
     * 区别是否需要算上刷新布局
     * 如果使用控件自带的下拉刷新，则计算position时需要算上
     */
    public int getPullHeaderSize() {
        if (mRefreshEnabled && mRefreshListener != null) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 如果使用上拉刷新，则计算position时需要算上
     */
    private int getLoadMoreSize() {
        if (mLoadMoreEnabled) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * @param layoutResId layoutResId
     * @param viewGroup   recyclerView.getParent()
     */
    public void setEmptyView(int layoutResId, ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutResId, viewGroup, false);
        setEmptyView(view);
    }

    /**
     * 设置是否显示 EmptyView
     */
    public void setEmptyViewEnabled(boolean emptyViewEnabled) {
        this.mEmptyViewEnabled = emptyViewEnabled;
    }

    public void setEmptyView(View emptyView) {
        boolean insert = false;
        if (mEmptyLayout == null) {
            mEmptyLayout = new FrameLayout(emptyView.getContext());
            final LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            final ViewGroup.LayoutParams lp = emptyView.getLayoutParams();
            if (lp != null) {
                layoutParams.width = lp.width;
                layoutParams.height = lp.height;
            }
            mEmptyLayout.setLayoutParams(layoutParams);
            insert = true;
        }
        mEmptyLayout.removeAllViews();
        mEmptyLayout.addView(emptyView);
        mEmptyViewEnabled = true;
        if (insert) {
            if (getEmptyViewSize() == 1) {
                int position = getHeaderViewCount() + getPullHeaderSize();
                if (mWrapAdapter != null) {
                    mWrapAdapter.getOriginalAdapter().notifyItemInserted(position);
                }
            }
        }
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
     * 不满一屏时，根据上滑的距离判断是否加载
     */
    private boolean isScrollLoad(LayoutManager layoutManager) {
        if (isFullScreen(layoutManager)) {
            return true;
        } else {
            return mIsScrollUp;
        }
    }

    /**
     * 设置不满一屏不加载
     */
    public void setNotFullScreenNoLoadMore() {
        misNoLoadMoreIfNotFullScreen = true;
    }

    /**
     * 不满一屏是否加载，默认加载
     */
    private boolean isNoFullScreenLoad(LayoutManager layoutManager) {
        if (misNoLoadMoreIfNotFullScreen) {
            return isFullScreen(layoutManager);
        } else {
            return true;
        }
    }

    /**
     * 是否是满屏
     */
    private boolean isFullScreen(LayoutManager layoutManager) {
        return layoutManager.getItemCount() > layoutManager.getChildCount();
    }

    /**
     * 移除单个HeaderView
     * tip: 移除后不能再次添加HeaderView,可能type重复导致添加失败
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
     * 移除所有HeaderView
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
     * 移除FooterView
     * 因为都是在一个item中处理布局，所以不必刷新布局，除非删除掉全部的FooterView
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
     *
     * @param rate 越大，意味着，用户要下拉滑动更久来触发下拉刷新。相反越小，就越短距离
     */
    public void setDragRate(float rate) {
        if (rate <= 0.5) {
            return;
        }
        mDragRate = rate;
    }

    public interface OnItemClickListener {

        void onClick(View v, int position);
    }

    public interface OnItemLongClickListener {

        boolean onLongClick(View v, int position);
    }

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
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
        if (mEmptyLayout != null) {
            mEmptyLayout.removeAllViews();
            mEmptyLayout = null;
        }
    }
}
