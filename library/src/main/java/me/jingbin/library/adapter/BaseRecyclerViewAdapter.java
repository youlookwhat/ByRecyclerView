package me.jingbin.library.adapter;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jingbin
 * link to https://github.com/youlookwhat/JRecyclerView
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private List<T> data = new ArrayList<>();

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerViewHolder holder, final int position) {
        holder.onBaseBindViewHolder(data.get(position), position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addAll(List<T> data) {
        this.data.addAll(data);
    }

    public void add(T object) {
        data.add(object);
    }

    /**
     * 在首条添加bean
     */
    public void addFirst(int position, T object) {
        data.add(position, object);
    }

    public void clear() {
        data.clear();
    }

    public void remove(T object) {
        data.remove(object);
    }

    public void remove(int position) {
        data.remove(position);
    }

    public void removeAll(List<T> data) {
        this.data.retainAll(data);
    }

    public List<T> getData() {
        return data;
    }

    public T getItemData(int position) {
        return data.get(position);
    }
}
