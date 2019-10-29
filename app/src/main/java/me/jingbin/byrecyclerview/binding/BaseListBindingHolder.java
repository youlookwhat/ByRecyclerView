package me.jingbin.byrecyclerview.binding;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import me.jingbin.library.adapter.BaseListHolder;


/**
 * @author jingbin
 */

public class BaseListBindingHolder<B extends ViewDataBinding> extends BaseListHolder {

    public final B binding;

    BaseListBindingHolder(ViewGroup viewGroup, int layoutId) {
        super(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), layoutId, viewGroup, false).getRoot());
        binding = DataBindingUtil.getBinding(getItemView());
    }

}
