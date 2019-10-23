package me.jingbin.library.adapter;

import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import java.util.List;

/**
 * @author jingbin
 * RecyclerView adapter适配器 适合单种item类型的情况
 */
public abstract class BaseRecyclerAdapter<T, V extends ViewDataBinding> extends BaseByRecyclerViewAdapter<T> {

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
    public BaseByRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent, mLayoutId);
    }

    private class ViewHolder extends BaseByRecyclerViewHolder<T, V> {
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
