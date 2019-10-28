package me.jingbin.library.adapter;


import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import java.util.List;

/**
 * 单一 item 类型 adapter
 *
 * @author jingbin
 * link to https://github.com/youlookwhat/ByRecyclerView
 */
public abstract class BaseRecyclerAdapter<T> extends BaseByRecyclerViewAdapter<T, BaseByViewHolder<T>> {

    private int mLayoutId;

    public BaseRecyclerAdapter(@LayoutRes int layoutId) {
        mLayoutId = layoutId;
    }

    public BaseRecyclerAdapter(@LayoutRes int layoutId, List<T> data) {
        super(data);
        mLayoutId = layoutId;
    }

    @NonNull
    @Override
    public BaseByViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent, mLayoutId);
    }

    private class ViewHolder extends BaseByViewHolder<T> {
        ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        protected void onBaseBindView(BaseByViewHolder<T> holder, T bean, int position) {
            bindView(holder, bean, position);
        }
    }

    protected abstract void bindView(BaseByViewHolder<T> holder, T bean, int position);
}
