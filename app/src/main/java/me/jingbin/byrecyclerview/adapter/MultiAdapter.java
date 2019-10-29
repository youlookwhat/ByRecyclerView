package me.jingbin.byrecyclerview.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.databinding.LayoutEmptyBinding;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.rxbinding.BaseBindingHolder;
import me.jingbin.library.adapter.BaseByRecyclerViewAdapter;
import me.jingbin.library.adapter.BaseByViewHolder;

/**
 * 多种类型 示例
 *
 * @author jingbin
 */
public class MultiAdapter extends BaseByRecyclerViewAdapter<DataItemBean, BaseByViewHolder<DataItemBean>> {

    @NonNull
    @Override
    public BaseByViewHolder<DataItemBean> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (11 == viewType) {
            return new View2Holder(parent, R.layout.layout_footer_view);
        } else {
            return new ViewHolder(parent, R.layout.layout_empty);
        }
    }

    private class View2Holder extends BaseBindingHolder<DataItemBean, LayoutEmptyBinding> {
        View2Holder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        protected void onBindingView(LayoutEmptyBinding binding, DataItemBean bean, int position) {

        }
    }

    private class ViewHolder extends BaseBindingHolder<DataItemBean, LayoutEmptyBinding> {
        ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        protected void onBindingView(LayoutEmptyBinding binding, DataItemBean bean, int position) {

        }
    }
}
