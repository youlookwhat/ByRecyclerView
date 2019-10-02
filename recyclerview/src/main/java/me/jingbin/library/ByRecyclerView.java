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

import me.jingbin.library.config.LogHelper;

/**
 * @author jingbin
 * link to https://github.com/youlookwhat/JRecyclerView
 */
public class ByRecyclerView extends RecyclerView {

    /**
     * 下面的ItemViewType是保留值(ReservedItemViewType),如果用户的adapter与它们重复将会强制抛出异常。
     * 不过为了简化,我们检测到重复时对用户的提示是ItemViewType必须小于10000
     * 设置一个很大的数字,尽可能避免和用户的adapter冲突
     */
    private static final int TYPE_REFRESH_HEADER = 10000;
    private static final int TYPE_LOADING_FOOTER = 10001;
    private static final int TYPE_EMPTY_VIEW = 10002;
    private static final int TYPE_FOOTET_VIEW = 10003;
    /**
     * HeaderView 起始type
     */
    private static final int HEADER_INIT_INDEX = 10004;
    /**
     * 每个header必须有不同的type,不然滚动的时候顺序会变化
     */
    private List<Integer> sHeaderTypes = new ArrayList<>();
    /**
     * HeaderView 数组
     */
    private ArrayList<View> mHeaderViews = new ArrayList<>();

    private WrapAdapter mWrapAdapter;
    /**
     * EmptyView 布局
     */
    private FrameLayout mEmptyLayout;
    /**
     * 是否使用EmptyView
     */
    private boolean mEmptyViewEnabled = true;
    /**
     * 是否正在加载更多
     */
    private boolean isLoadingData = false;
    /**
     * 是否没有更多数据了
     */
    private boolean isNoMore = false;
    /**
     * 设置是否能 下拉刷新
     */
    private boolean mRefreshEnabled = false;
    /**
     * 设置是否能 加载更多
     */
    private boolean mLoadMoreEnabled = false;
    private boolean mFootViewEnabled = false;
    private boolean mHeaderViewEnabled = false;
    /**
     * 手指是否上滑
     */
    private boolean isScrollUp = false;
    /**
     * 首页列表增加一个tabhost的高度
     */
    private boolean isFooterMoreHeight = false;

    private OnLoadMoreListener mLoadMoreListener;
    private OnRefreshListener mRefreshListener;
    private BaseRefreshHeader mRefreshHeader;
    private View mFootView;
    private AppBarStateChangeListener.State appbarState = AppBarStateChangeListener.State.EXPANDED;
    private final RecyclerView.AdapterDataObserver mDataObserver = new DataObserver();
    private float mLastY = -1;
    private float mPullStartY = 0;
    private float mDragRate = 3;

    public ByRecyclerView(Context context) {
        this(context, null);
    }

    public ByRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ByRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mFootView = new LoadingMoreFooter(getContext());
        mFootView.setVisibility(GONE);
    }

    /**
     * 添加HeaderView；不可重复添加相同的View
     *
     * @param headerView HeaderView
     * @param isNotify   是否立即刷新
     */
    public void addHeaderView(View headerView, boolean isNotify) {
        sHeaderTypes.add(HEADER_INIT_INDEX + mHeaderViews.size());
        mHeaderViews.add(headerView);
        mHeaderViewEnabled = true;
        if (mWrapAdapter != null && isNotify) {
            mWrapAdapter.getOriginalAdapter().notifyItemInserted(getPullHeaderSize() + getHeadersCount() - 1);
        }
    }

    public void addHeaderView(View view) {
        addHeaderView(view, true);
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
        return mHeaderViews.size() > 0 && sHeaderTypes.contains(itemViewType);
    }

    /**
     * 判断是否是JRecyclerView保留的itemViewType
     */
    private boolean isReservedItemViewType(int itemViewType) {
        if (itemViewType == TYPE_REFRESH_HEADER || itemViewType == TYPE_LOADING_FOOTER || itemViewType == TYPE_EMPTY_VIEW || sHeaderTypes.contains(itemViewType)) {
            return true;
        } else {
            return false;
        }
    }

    public void setFootView(final View view) {
        mFootView = view;
    }

    public void loadMoreComplete() {
        isLoadingData = false;
        if (mFootView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) mFootView).setState(LoadingMoreFooter.STATE_COMPLETE);
        } else {
            mFootView.setVisibility(View.GONE);
        }
    }

    public void setNoMore(boolean noMore) {
        isLoadingData = false;
        isNoMore = noMore;
        if (mFootView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) mFootView).setState(isNoMore ? LoadingMoreFooter.STATE_NO_MORE : LoadingMoreFooter.STATE_COMPLETE);
        } else {
            mFootView.setVisibility(View.GONE);
        }
    }

    public void refresh() {
        if (mRefreshEnabled && mRefreshListener != null) {
            mRefreshHeader.setState(BaseRefreshHeader.STATE_REFRESHING);
            mRefreshListener.onRefresh();
        }
    }

    public void reset() {
//        setNoMore(false);
        loadMoreComplete();
        refreshComplete();
    }

    public void refreshComplete() {
        if (mRefreshEnabled) {
            mRefreshHeader.refreshComplete();
        }
        setNoMore(false);
    }

    /**
     * 没有更多内容
     */
    public void loadMoreEnd() {
        isLoadingData = false;
        isNoMore = true;
        if (mFootView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) mFootView).setState(LoadingMoreFooter.STATE_NO_MORE);
        } else {
            mFootView.setVisibility(View.GONE);
        }
    }

    public void setRefreshHeader(BaseRefreshHeader refreshHeader) {
        mRefreshHeader = refreshHeader;
    }

    public void setRefreshEnabled(boolean enabled) {
        mRefreshEnabled = enabled;
        if (mRefreshHeader == null) {
            mRefreshHeader = new ProgressRefreshHeader(getContext());
        }
    }

    /**
     * 设置列表底部增加一个tabhost的高度
     *
     * @param enabled 默认为不增加，正常
     */
    public void setFooterMoreHeightEnabled(boolean enabled) {
        isFooterMoreHeight = enabled;
        if (mFootView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) mFootView).setFooterMoreHeight(true);
        }
    }

    public void setLoadMoreEnabled(boolean enabled) {
        mLoadMoreEnabled = enabled;
        if (!enabled) {
            if (mFootView instanceof LoadingMoreFooter) {
                ((LoadingMoreFooter) mFootView).setState(LoadingMoreFooter.STATE_COMPLETE);
            }
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mWrapAdapter = new WrapAdapter(adapter);
        super.setAdapter(mWrapAdapter);
        adapter.registerAdapterDataObserver(mDataObserver);
        mDataObserver.onChanged();
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
        if (state == RecyclerView.SCROLL_STATE_IDLE && mLoadMoreListener != null && !isLoadingData && mLoadMoreEnabled) {
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
//            Log.e("-----11", "--" + (lastVisibleItemPosition >= layoutManager.getItemCount() - 1));
//            Log.e("-----22", "--" + (layoutManager.getItemCount() > layoutManager.getChildCount()));
//            Log.e("-----22-list1``", "--" + (layoutManager.getItemCount()));
//            Log.e("-----22-list2``", "--" + (layoutManager.getChildCount()));
//            Log.e("-----33", "--" + (!isNoMore));
//            Log.e("-----44", "--" + (mRefreshHeader.getState() < BaseRefreshHeader.STATE_REFRESHING));
            // 取消那条后，只有一条信息也可以刷新
            if (layoutManager.getChildCount() > 0
                    && lastVisibleItemPosition >= layoutManager.getItemCount() - 1
//                    && layoutManager.getItemCount() > layoutManager.getChildCount()
                    && !isNoMore
                    && isScrollUp
                    && (!mRefreshEnabled || mRefreshHeader.getState() < BaseRefreshHeader.STATE_REFRESHING)) {
                isLoadingData = true;
                isScrollUp = false;
                if (mFootView instanceof LoadingMoreFooter) {
                    ((LoadingMoreFooter) mFootView).setState(LoadingMoreFooter.STATE_LOADING);
                } else {
                    mFootView.setVisibility(View.VISIBLE);
                }

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
//                isScrollUp = mLoadMoreEnabled && ev.getY() - mPullStartY <= 0;
                // 按下的纵坐标 - 当前的纵坐标(为了更灵敏)
                isScrollUp = mLoadMoreEnabled && mPullStartY - ev.getY() >= -10;
                LogHelper.e("isScrollUp:  ", isScrollUp + " --- mPullStartY:  " + mPullStartY + " --- " + "ev.getY(): " + ev.getY());

                mPullStartY = 0;
                mLastY = -1;
                if (mRefreshEnabled && mRefreshListener != null && isOnTop() && appbarState == AppBarStateChangeListener.State.EXPANDED) {
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
            return mEmptyViewEnabled && mEmptyLayout != null && position == mHeaderViews.size() + getPullHeaderSize();
        }

        /**
         * 是否是 HeaderView 布局
         */
        boolean isHeaderView(int position) {
            return mHeaderViewEnabled && position >= getPullHeaderSize() && position < mHeaderViews.size() + getPullHeaderSize();
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
            } else if (viewType == TYPE_FOOTET_VIEW) {
                return new SimpleViewHolder(mFooterLayout);
            } else if (viewType == TYPE_LOADING_FOOTER) {
                return new SimpleViewHolder(mFootView);
            }
            ViewHolder viewHolder = adapter.onCreateViewHolder(parent, viewType);
            bindViewClickListener(viewHolder);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (isHeaderView(position) || isRefreshHeader(position) || isEmptyView(position) || isFootView(position)) {
                return;
            }
            int adjPosition = position - getCustomItemUpViewCount();
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
                int adjPosition = position - getCustomItemUpViewCount();
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
                return getPullHeaderSize() + getHeadersCount() + getFooterViewSize() + getLoadMoreSize() + getEmptyViewSize() + adapter.getItemCount();
            } else {
                return getPullHeaderSize() + getHeadersCount() + getFooterViewSize() + getLoadMoreSize() + getEmptyViewSize();
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
                return sHeaderTypes.get(position);
            }
            if (isFootView(position)) {
                return TYPE_FOOTET_VIEW;
            }
            if (isEmptyView(position)) {
                return TYPE_EMPTY_VIEW;
            }
            if (isLoadMore(position)) {
                return TYPE_LOADING_FOOTER;
            }
            int adapterCount;
            if (adapter != null) {
                int adjPosition = position - getCustomItemUpViewCount();
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    int type = adapter.getItemViewType(adjPosition);
                    if (isReservedItemViewType(type)) {
                        throw new IllegalStateException("JRecyclerView require itemViewType in adapter should be less than 10000 !");
                    }
                    return type;
                }
            }
            return 0;
        }

        @Override
        public long getItemId(int position) {
            if (adapter != null && position >= getCustomItemUpViewCount()) {
                int adjPosition = position - getCustomItemUpViewCount();
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
    int getEmptyViewSize() {
        return mEmptyViewEnabled && mEmptyLayout != null && mEmptyLayout.getChildCount() != 0 ? 1 : 0;
    }

    /**
     * 获取 HeaderView的个数
     */
    public int getHeadersCount() {
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
                    onItemClickListener.onClick(v, viewHolder.getLayoutPosition() - getCustomItemUpViewCount());
                }
            });
        }
        if (onItemLongClickListener != null) {
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onItemLongClickListener.onLongClick(v, viewHolder.getLayoutPosition() - getCustomItemUpViewCount());
                }
            });
        }
    }

    /**
     * 自定义类型头部布局的个数 = 刷新头布局 + HeaderView  + EmptyView
     */
    private int getCustomItemUpViewCount() {
        return getHeadersCount() + getPullHeaderSize() + getEmptyViewSize();
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
                int position = getHeadersCount() + getPullHeaderSize();
                if (mWrapAdapter != null) {
                    mWrapAdapter.getOriginalAdapter().notifyItemInserted(position);
                }
            }
        }
    }

    private LinearLayout mFooterLayout;

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
                int position = mWrapAdapter.getOriginalAdapter().getItemCount() + getCustomItemUpViewCount();
                if (position != -1) {
                    mWrapAdapter.getOriginalAdapter().notifyItemInserted(position);
                }
            }
        }
        return index;
    }

    public void setHeaderViewEnabled(boolean headerViewEnabled) {
        this.mHeaderViewEnabled = headerViewEnabled;
        if (!mHeaderViewEnabled) {
            if (mHeaderViews.size() > 0) {
                if (mWrapAdapter != null) {
                    mWrapAdapter.getOriginalAdapter().notifyItemRangeRemoved(getPullHeaderSize(), mHeaderViews.size());
                    mHeaderViews.clear();
                }
            }
        }
    }

    public void setFootViewEnabled(boolean footViewEnabled) {
        this.mFootViewEnabled = footViewEnabled;
        if (!mFootViewEnabled) {
            if (mFooterLayout.getChildCount() != 0) {
                if (mWrapAdapter != null) {
                    int position = mWrapAdapter.getOriginalAdapter().getItemCount() + getCustomItemUpViewCount();
                    if (position != -1) {
                        mWrapAdapter.getOriginalAdapter().notifyItemRemoved(position);
                    }
                }
            }
        }
    }

    /**
     * remove footer view from mFooterLayout,
     * When the child count of mFooterLayout is 0, mFooterLayout will be set to null.
     *
     * @param footer
     */
    public void removeFooterView(View footer) {
        if (mFootViewEnabled && mFooterLayout != null && mFooterLayout.getChildCount() != 0) {
            mFooterLayout.removeView(footer);
            if (mWrapAdapter != null && mFooterLayout.getChildCount() == 0) {
                int position = mWrapAdapter.getOriginalAdapter().getItemCount() + getCustomItemUpViewCount();
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
                int position = mWrapAdapter.getOriginalAdapter().getItemCount() + getCustomItemUpViewCount();
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
        mLoadMoreEnabled = false;
        mRefreshEnabled = false;
        if (mHeaderViews != null) {
            mHeaderViews.clear();
            mHeaderViews = null;
        }
        if (sHeaderTypes != null) {
            sHeaderTypes.clear();
            mHeaderViews = null;
        }
        if (mEmptyLayout != null) {
            mEmptyLayout.removeAllViews();
            mEmptyLayout = null;
        }
    }
}
