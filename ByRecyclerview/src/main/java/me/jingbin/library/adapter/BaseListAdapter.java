package me.jingbin.library.adapter;

import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.databinding.ViewDataBinding;

import java.util.List;

/**
 * @author jingbin
 * @data 2019-10-11
 * @description ListView adapter精简适配器
 */
public abstract class BaseListAdapter<T, V extends ViewDataBinding> extends BaseByListViewAdapter<T> {

    private int mLayoutId;

    public BaseListAdapter(@LayoutRes int layoutId) {
        mLayoutId = layoutId;
    }

    public BaseListAdapter(@LayoutRes int layoutId, List<T> data) {
        super(data);
        mLayoutId = layoutId;
    }

    @Override
    protected BaseByListViewHolder createHolder(ViewGroup parent, int position) {
        return new ViewHolder(parent, mLayoutId);
    }

    private class ViewHolder extends BaseByListViewHolder<T, V> {

        ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        public void onBindView(T bean, int position) {
            bindView(bean, binding, position);
        }
    }

    protected abstract void bindView(T bean, V binding, int position);

}
