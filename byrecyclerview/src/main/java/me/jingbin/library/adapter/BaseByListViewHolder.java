package me.jingbin.library.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * @author jingbin
 */
public abstract class BaseByListViewHolder<T, V extends ViewDataBinding> {

    private View itemView;
    protected V binding;

    public BaseByListViewHolder(ViewGroup parent, int layoutId) {
        this.itemView = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutId, parent, false).getRoot();
        binding = DataBindingUtil.getBinding(this.itemView);
    }

    View getItemView() {
        return itemView;
    }

    protected abstract void onBindView(T positionData, int position);
}
