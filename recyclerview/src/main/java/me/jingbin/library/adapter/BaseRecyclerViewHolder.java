package me.jingbin.library.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author jingbin
 */
public abstract class BaseRecyclerViewHolder<T, D extends ViewDataBinding> extends RecyclerView.ViewHolder {

    protected D binding;

    /**
     * root:如果attachToRoot(也就是第三个参数)为true, 那么root就是为新加载的View指定的父View。
     * 否则，root只是一个为返回View层级的根布局提供LayoutParams值的简单对象。
     * attachToRoot: 新加载的布局是否添加到root，如果为false，root参数仅仅用于为xml根布局创建正确的LayoutParams子类
     * （列如：根布局为LinearLayout，则用LinearLayout.LayoutParam）。
     */
    public BaseRecyclerViewHolder(ViewGroup viewGroup, int layoutId) {
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
    public abstract void onBindViewHolder(T bean, final int position);

    /**
     * 当数据改变时，binding会在下一帧去改变数据，如果我们需要立即改变，就去调用executePendingBindings方法。
     */
    void onBaseBindViewHolder(T bean, final int position) {
        onBindViewHolder(bean, position);
        binding.executePendingBindings();
    }
}
