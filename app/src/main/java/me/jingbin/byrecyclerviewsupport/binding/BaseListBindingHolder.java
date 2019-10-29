package me.jingbin.byrecyclerviewsupport.binding;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import me.jingbin.library.adapter.BaseListHolder;


/**
 * https://github.com/youlookwhat/ByRecyclerView
 */
public class BaseListBindingHolder<B extends ViewDataBinding> extends BaseListHolder {

    @Nullable
    public final B binding;

    BaseListBindingHolder(ViewGroup viewGroup, int layoutId) {
        super(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), layoutId, viewGroup, false).getRoot());
        binding = DataBindingUtil.getBinding(getItemView());
    }

    @NonNull
    public B getBinding() {
        if (binding == null) {
            throw new NullPointerException("The binding cannot be null!");
        }
        return binding;
    }
}
