package me.jingbin.byrecyclerview.base;


import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import java.util.List;

import me.jingbin.library.adapter.BaseByRecyclerAdapter;

/**
 * @author jingbin
 */

public  abstract class BaseBindingAdapter<T, B extends ViewDataBinding> extends BaseByRecyclerAdapter<T, BaseBindingHolder<T, B>> {

    private int mLayoutId;

    public BaseBindingAdapter(@LayoutRes int layoutId) {
        mLayoutId = layoutId;
    }

    public BaseBindingAdapter(@LayoutRes int layoutId, List<T> data) {
        super(data);
        mLayoutId = layoutId;
    }

    @NonNull
    @Override
    public BaseBindingHolder<T, B> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent, mLayoutId);
    }

    @Override
    protected void onBindView(BaseBindingHolder<T, B> holder, T bean, int position) {

    }

    private class ViewHolder extends BaseBindingHolder<T, B> {
        ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        void onBindView(B binding, T bean, int position) {
            bindView(bean, binding, position);
        }

    }

    protected abstract void bindView(T bean, B binding, int position);
}

