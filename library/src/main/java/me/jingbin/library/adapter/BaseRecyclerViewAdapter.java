package me.jingbin.library.adapter;


import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jingbin on 2016/11/25
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    protected List<T> data = new ArrayList<>();
    protected OnItemClickListener<T> listener;
    protected OnItemLongClickListener<T> onItemLongClickListener;

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
        holder.onBaseBindViewHolder(data.get(position), position);

        // 如这里写了，adapter列表里面写点击事件可能会失效
//        holder.itemView.setOnClickListener(new PerfectClickListener() {
//            @Override
//            protected void onNoDoubleClick(View v) {
//                if (listener!=null){
//                    listener.onClick(data.get(position), position);
//                }
//            }
//        });

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

    // 在首条添加bean
    public void addFirst(int position,T object) {
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

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.listener = listener;
    }


    public List<T> getData() {
        return data;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }
}
