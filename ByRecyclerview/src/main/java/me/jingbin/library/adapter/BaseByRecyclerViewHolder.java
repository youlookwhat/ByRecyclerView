package me.jingbin.library.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;


/**
 * @author jingbin
 */
public abstract class BaseByRecyclerViewHolder<T, D extends ViewDataBinding> extends RecyclerView.ViewHolder {

    protected D binding;

    public BaseByRecyclerViewHolder(ViewGroup viewGroup, int layoutId) {
        // 注意要依附 viewGroup，不然显示item不全
        super(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), layoutId, viewGroup, false).getRoot());
        // 得到这个View绑定的Binding
        binding = DataBindingUtil.getBinding(this.itemView);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param bean     the data of bind
     * @param position the item position of recyclerView
     */
    public abstract void onBindView(T bean, int position);

    /**
     * 当数据改变时，binding会在下一帧去改变数据，如果我们需要立即改变，就去调用executePendingBindings方法。
     */
    void onBaseBindView(T bean, int position) {
        onBindView(bean, position);
        binding.executePendingBindings();
    }
}
