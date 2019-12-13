package me.jingbin.byrecyclerview.adapter;

import java.util.List;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.binding.BaseBindingAdapter;
import me.jingbin.byrecyclerview.binding.BaseBindingHolder;
import me.jingbin.byrecyclerview.databinding.ItemGridBinding;

/**
 * @author jingbin
 */
public class GridAdapter extends BaseBindingAdapter<DataItemBean, ItemGridBinding> {

    public GridAdapter() {
        super(R.layout.item_grid);
    }

    public GridAdapter(List<DataItemBean> data) {
        super(R.layout.item_grid, data);
    }

    @Override
    protected void bindView(BaseBindingHolder holder, DataItemBean bean, ItemGridBinding binding, int position) {

    }
}
