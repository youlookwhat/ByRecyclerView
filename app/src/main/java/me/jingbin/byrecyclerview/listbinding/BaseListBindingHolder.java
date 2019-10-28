package me.jingbin.byrecyclerview.listbinding;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import me.jingbin.library.adapter.BaseByListHolder;


/**
 * @author jingbin
 */

public abstract class BaseListBindingHolder<T, B extends ViewDataBinding> extends BaseByListHolder<T> {

    public final B binding;

    public BaseListBindingHolder(ViewGroup viewGroup, int layoutId) {
        super(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), layoutId, viewGroup, false).getRoot());
        binding = DataBindingUtil.getBinding(getItemView());
    }

    @Override
    protected void onBaseBindView(BaseByListHolder<T> holder, T bean, int position) {
        onBindingView(binding, bean, position);
    }

    protected abstract void onBindingView(B binding, T bean, int position);
}
