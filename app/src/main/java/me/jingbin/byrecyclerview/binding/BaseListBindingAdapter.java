package me.jingbin.byrecyclerview.binding;


import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.databinding.ViewDataBinding;

import java.util.List;

import me.jingbin.library.adapter.BaseListAdapter;

/**
 * @author jingbin
 */

public abstract class BaseListBindingAdapter<T, B extends ViewDataBinding> extends BaseListAdapter<T, BaseListBindingHolder<B>> {

    private int mLayoutId;

    public BaseListBindingAdapter(@LayoutRes int layoutId) {
        mLayoutId = layoutId;
    }

    public BaseListBindingAdapter(@LayoutRes int layoutId, List<T> data) {
        super(data);
        mLayoutId = layoutId;
    }

    @Override
    protected BaseListBindingHolder<B> onCreateViewHolder(ViewGroup parent, int position) {
        return new BaseListBindingHolder<>(parent, mLayoutId);
    }

    @Override
    protected void onBindView(BaseListBindingHolder<B> holder, T bean, int position) {
        bindView(bean, holder.binding, mLayoutId);
    }

    protected abstract void bindView(T bean, B binding, int position);
}

