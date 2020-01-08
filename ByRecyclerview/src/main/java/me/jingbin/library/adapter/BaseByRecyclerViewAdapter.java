package me.jingbin.library.adapter;


import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

import me.jingbin.library.ByRecyclerView;

/**
 * Can be used for basic RecyclerViewAdapter or binding ByRecyclerView use.
 *
 * @author jingbin
 * link to https://github.com/youlookwhat/ByRecyclerView
 */
public abstract class BaseByRecyclerViewAdapter<T, K extends BaseByViewHolder> extends RecyclerView.Adapter<K> {

    private ByRecyclerView mRecyclerView;
    private List<T> mData = new ArrayList<>();

    protected BaseByRecyclerViewAdapter() {
    }

    protected BaseByRecyclerViewAdapter(List<T> data) {
        this.mData = data == null ? new ArrayList<T>() : data;
    }

    @Override
    public void onBindViewHolder(@NonNull K holder, final int position) {
        holder.setByRecyclerView(mRecyclerView);
        holder.onBaseBindView(holder, mData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addAll(List<T> data) {
        this.mData.addAll(data);
    }

    public void clear() {
        mData.clear();
    }

    public List<T> getData() {
        return mData;
    }

    public T getItemData(int position) {
        return mData == null ? null : mData.get(position);
    }

    /**
     * adapter bind ByRecyclerView
     * You don't have to call it, it's automatically matched.
     * 自己不必调用，已自动匹配
     */
    public void setRecyclerView(ByRecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public void addData(int position, T data) {
        mData.add(position, data);
        position = position + getCustomTopItemViewCount();
        notifyItemRangeInserted(position, 1);
        compatibilityDataSizeChanged(1);
    }

    /**
     * 添加一条数据
     */
    public void addData(T data) {
        int startPosition = mData.size();
        mData.add(data);
        startPosition = startPosition + getCustomTopItemViewCount();
        notifyItemRangeInserted(startPosition, 1);
        compatibilityDataSizeChanged(1);
    }

    /**
     * 添加一组数据
     */
    public void addData(List<T> data) {
        int startPosition = mData.size();
        this.mData.addAll(data);
        startPosition = startPosition + getCustomTopItemViewCount();
        notifyItemRangeInserted(startPosition, data.size());
        compatibilityDataSizeChanged(data.size());
    }

    public void addData(int position, List<T> data) {
        int startPosition = mData.size();
        this.mData.addAll(data);
        startPosition = startPosition + getCustomTopItemViewCount();
        notifyItemRangeInserted(startPosition, data.size());
        compatibilityDataSizeChanged(data.size());
    }

    /**
     * 用户初始化数据，当滑动到底后再次下拉刷新
     */
    public void setNewData(List<T> data) {
        this.mData = data == null ? new ArrayList<T>() : data;
        if (mRecyclerView != null) {
            mRecyclerView.setRefreshing(false);
        }
        notifyDataSetChanged();
    }

    /**
     * 移除一条数据
     */
    public void removeData(@IntRange(from = 0) int position) {
        mData.remove(position);
        int internalPosition = position + getCustomTopItemViewCount();
        notifyItemRemoved(internalPosition);
        // 如果移除的是最后一个，忽略
        if (position != mData.size()) {
            notifyItemRangeChanged(internalPosition, mData.size() - internalPosition);
        }
    }

    /**
     * list列表数据头部的view数量：RefreshView + HeaderView + EmptyView
     */
    public int getCustomTopItemViewCount() {
        if (mRecyclerView != null) {
            return mRecyclerView.getCustomTopItemViewCount();
        } else {
            return 0;
        }
    }

    /**
     * 如果使用了 RefreshView 、 HeaderView 、EmptyView，则刷新position的时候需要加上使用的view的数量
     */
    public final void refreshNotifyItemChanged(int position) {
        notifyItemChanged(position + getCustomTopItemViewCount());
    }

    public final void refreshNotifyItemRemoved(int position) {
        notifyItemRemoved(position + getCustomTopItemViewCount());
    }

    public final void refreshNotifyItemMoved(int fromPosition, int toPosition) {
        notifyItemMoved(fromPosition + getCustomTopItemViewCount(), toPosition + getCustomTopItemViewCount());
    }


    /**
     * compatible getPullHeaderSize、getHeaderViewCount and getStateViewSize may change
     *
     * @param size Need compatible data size
     */
    private void compatibilityDataSizeChanged(int size) {
        final int dataSize = mData == null ? 0 : mData.size();
        if (dataSize == size) {
            notifyDataSetChanged();
        }
    }

    public ByRecyclerView getRecyclerView() {
        return mRecyclerView;
    }
}
