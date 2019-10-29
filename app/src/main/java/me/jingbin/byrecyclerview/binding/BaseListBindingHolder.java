package me.jingbin.byrecyclerview.binding;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

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
