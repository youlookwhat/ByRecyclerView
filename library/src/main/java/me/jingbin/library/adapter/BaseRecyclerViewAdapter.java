package me.jingbin.library.adapter;


import android.view.View;

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
    private OnItemClickListener<T> onItemClickListener;
    private OnItemLongClickListener<T> onItemLongClickListener;

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerViewHolder holder, final int position) {
        holder.onBaseBindViewHolder(data.get(position), position);

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(v, data.get(position), position);
                }
            });
        }
        if (onItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onItemLongClickListener.onLongClick(v, data.get(position), position);
                }
            });
        }
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

    /**
     * Register a callback to be invoked when an item in this RecyclerView has
     * been clicked.
     *
     * @param listener The callback that will be invoked.
     */
    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.onItemClickListener = listener;
    }


    /**
     * Register a callback to be invoked when an item in this RecyclerView has
     * been long clicked and held
     *
     * @param listener The callback that will run
     */
    public void setOnItemLongClickListener(OnItemLongClickListener<T> listener) {
        this.onItemLongClickListener = listener;
    }

    public interface OnItemClickListener<T> {
        /**
         * Called when a view has been clicked.
         *
         * @param v        The view that was clicked.
         * @param t        the item data Bean
         * @param position The position of the view in the adapter.
         */
        void onClick(View v, T t, int position);
    }

    public interface OnItemLongClickListener<T> {

        /**
         * Called when a view has been clicked and held.
         *
         * @param v        The view that was clicked and held.
         * @param t        the item data Bean
         * @param position The position of the view in the adapter.
         * @return true if the callback consumed the long click, false otherwise.
         */
        boolean onLongClick(View v, T t, int position);
    }
}
