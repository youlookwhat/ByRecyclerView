package me.jingbin.byrecyclerview.listbinding;


import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import java.util.List;

import me.jingbin.library.adapter.BaseByListViewAdapter;

/**
 * @author jingbin
 */

public abstract class BaseListAdapter<T, B extends ViewDataBinding> extends BaseByListViewAdapter<T, BaseListBindingHolder<T, B>> {

    private int mLayoutId;

    public BaseListAdapter(@LayoutRes int layoutId) {
        mLayoutId = layoutId;
    }

    public BaseListAdapter(@LayoutRes int layoutId, List<T> data) {
        super(data);
        mLayoutId = layoutId;
    }

    @NonNull
    @Override
    public BaseListBindingHolder<T, B> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent, mLayoutId);
    }

    private class ViewHolder extends BaseListBindingHolder<T, B> {
        ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        protected void onBindingView(B binding, T bean, int position) {
            bindView(bean, binding, position);
        }
    }

    protected abstract void bindView(T bean, B binding, int position);
}

