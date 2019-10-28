package me.jingbin.library.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * ListView adapter
 *
 * @author jingbin
 * link to https://github.com/youlookwhat/ByRecyclerView
 */
public abstract class BaseByListViewAdapter<T, VH extends BaseByListHolder<T>> extends BaseAdapter {

    private List<T> mData = new ArrayList<>();

    protected BaseByListViewAdapter() {
    }

    protected BaseByListViewAdapter(List<T> data) {
        this.mData = data == null ? new ArrayList<T>() : data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VH holder;
        if (convertView == null) {
            holder = onCreateViewHolder(parent, position);
            convertView = holder.getItemView();
            convertView.setTag(holder);
        } else {
            holder = (VH) convertView.getTag();
        }
        if (holder != null) {
            holder.onBaseBindView(holder,getItem(position), position);
        }
        return convertView;
    }

    protected abstract VH onCreateViewHolder(ViewGroup parent, int position);


    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> data) {
        this.mData = data;
    }

    public void addAll(List<T> data) {
        this.mData.addAll(data);
    }

    public void removeAll(List<T> data) {
        this.mData.removeAll(data);
    }

    public void add(T t) {
        this.mData.add(t);
    }

    public void clear() {
        this.mData.clear();
    }


}
