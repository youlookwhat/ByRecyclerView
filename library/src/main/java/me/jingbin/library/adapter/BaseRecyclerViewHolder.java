package me.jingbin.library.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by jingbin on 2016/11/25
 */
public abstract class BaseRecyclerViewHolder<T, D extends ViewDataBinding> extends RecyclerView.ViewHolder {

    public D binding;

    /**
     * root:如果attachToRoot(也就是第三个参数)为true, 那么root就是为新加载的View指定的父View。
     * 否则，root只是一个为返回View层级的根布局提供LayoutParams值的简单对象。
     * attachToRoot: 新加载的布局是否添加到root，如果为false，root参数仅仅用于为xml根布局创建正确的LayoutParams子类
     * （列如：根布局为LinearLayout，则用LinearLayout.LayoutParam）。
     */
    public BaseRecyclerViewHolder(ViewGroup viewGroup, int layoutId) {
        // 注意要依附 viewGroup，不然显示item不全!! https://juejin.im/entry/5979349e5188253def54b052
        super(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), layoutId, viewGroup, false).getRoot());
        // 得到这个View绑定的Binding
        binding = DataBindingUtil.getBinding(this.itemView);
    }

    /**
     * @param object   the data of bind
     * @param position the item position of recyclerView
     */
    public abstract void onBindViewHolder(T object, final int position);

    /**
     * 当数据改变时，binding会在下一帧去改变数据，如果我们需要立即改变，就去调用executePendingBindings方法。
     */
    void onBaseBindViewHolder(T object, final int position) {
        onBindViewHolder(object, position);
        binding.executePendingBindings();
    }
}
