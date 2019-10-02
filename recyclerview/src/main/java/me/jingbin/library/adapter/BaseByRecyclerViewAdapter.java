package me.jingbin.library.adapter;


import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.jingbin.library.ByRecyclerView;

/**
 * 可用于基本的RecyclerViewAdapter，也可绑定ByRecyclerView后进行刷新
 *
 * @author jingbin
 * link to https://github.com/youlookwhat/ByRecyclerView
 */
public abstract class BaseByRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseByRecyclerViewHolder> {

    /**
     * 如果使用ByRecyclerView，请先绑定。用于刷新列表定位。
     */
    private ByRecyclerView mRecyclerView;
    private List<T> mData = new ArrayList<>();

    public BaseByRecyclerViewAdapter() {
    }

    public BaseByRecyclerViewAdapter(ByRecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
    }

    public BaseByRecyclerViewAdapter(List<T> data) {
        this.mData = data;
    }

    public BaseByRecyclerViewAdapter(ByRecyclerView recyclerView, List<T> data) {
        this.mRecyclerView = recyclerView;
        this.mData = data;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseByRecyclerViewHolder holder, final int position) {
        holder.onBaseBindViewHolder(mData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addAll(List<T> data) {
        this.mData.addAll(data);
    }

    public void add(T object) {
        mData.add(object);
    }

    public void addFirst(int position, T object) {
        mData.add(position, object);
    }

    public void clear() {
        mData.clear();
    }

    public void remove(T object) {
        mData.remove(object);
    }

    public void remove(int position) {
        mData.remove(position);
    }

    public void removeAll(List<T> data) {
        this.mData.retainAll(data);
    }

    public List<T> getData() {
        return mData;
    }

    public T getItemData(int position) {
        return mData.get(position);
    }

    /**
     * 如果使用{@link #addData(T)#addData(List)#setNewData(List) 请绑定RecyclerView}
     */
    public void setRecyclerView(ByRecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public void addData(int position, T data) {
        mData.add(position, data);
        position = position + getRecyclerViewListTopViewSize();
        notifyItemRangeInserted(position, 1);
    }

    /**
     * 添加一条数据
     * 请注意是否绑定了recyclerView，如不绑定就是普通的RecyclerView操作
     */
    public void addData(T data) {
        int startPosition = mData.size();
        mData.add(data);
        startPosition = startPosition + getRecyclerViewListTopViewSize();
        notifyItemRangeInserted(startPosition, 1);
    }

    /**
     * 请注意是否绑定了recyclerView，如不绑定就是普通的RecyclerView操作
     */
    public void addData(List<T> data) {
        int startPosition = mData.size();
        this.mData.addAll(data);
        startPosition = startPosition + getRecyclerViewListTopViewSize();
        notifyItemRangeInserted(startPosition, data.size());
    }

    public void addData(int position, List<T> data) {
        int startPosition = mData.size();
        this.mData.addAll(data);
        startPosition = startPosition + getRecyclerViewListTopViewSize();
        notifyItemRangeInserted(startPosition, data.size());
    }

    /**
     * 用户初始化数据，当滑动到底后再次下拉刷新
     * 请注意是否绑定了recyclerView，如不绑定就是普通的RecyclerView操作
     */
    public void setNewData(List<T> data) {
        this.mData = data == null ? new ArrayList<T>() : data;
        if (mRecyclerView != null) {
            mRecyclerView.reset();
        }
        notifyDataSetChanged();
        if (mRecyclerView != null) {
            mRecyclerView.refreshComplete();
        }
    }

    /**
     * remove one data
     * 请注意是否绑定了recyclerView，如不绑定就是普通的RecyclerView操作
     */
    public void removeData(@IntRange(from = 0) int position) {
        mData.remove(position);
        int internalPosition = position + getRecyclerViewListTopViewSize();
        notifyItemRemoved(internalPosition);
        // 如果移除的是最后一个，忽略
        if (position != mData.size()) {
            notifyItemRangeChanged(internalPosition, mData.size() - internalPosition);
        }
    }

    /**
     * RefreshView + HeaderView + EmptyView
     */
    private int getRecyclerViewListTopViewSize() {
        if (mRecyclerView != null) {
            return mRecyclerView.getPullHeaderSize() + mRecyclerView.getHeadersCount() + mRecyclerView.getEmptyViewSize();
        } else {
            return 0;
        }
    }
}
