package me.jingbin.byrecyclerview.base;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import me.jingbin.library.adapter.BaseByViewHolder;


/**
 * @author jingbin
 */

public abstract class BaseBindingHolder<T, B extends ViewDataBinding> extends BaseByViewHolder {

    public final B binding;

    public BaseBindingHolder(ViewGroup viewGroup, int layoutId) {
        // 注意要依附 viewGroup，不然显示item不全
        super(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), layoutId, viewGroup, false).getRoot());
        // 得到这个View绑定的Binding
        binding = DataBindingUtil.getBinding(this.itemView);

    }

//    @Override
//    protected void onBindView(BaseByViewHolder holder, T bean, int position) {
//        onBindView(binding, bean, position);
//    }

    abstract void onBindView(B binding, T bean, int position);
//
//    public BaseBindingHolder(View view) {
//        super(view);
//        this.binding = (B) view.getTag(R.id.BaseQuickAdapter_databinding_support);
//    }
//
//    @Override
//    public void onBindView(BaseByViewHolder holder, T bean, int position) {
//
//    }
}
