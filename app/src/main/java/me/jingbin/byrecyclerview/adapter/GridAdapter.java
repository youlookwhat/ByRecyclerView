package me.jingbin.byrecyclerview.adapter;

import java.util.List;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.binding.BaseBindingAdapter;
import me.jingbin.byrecyclerview.binding.BaseBindingHolder;
import me.jingbin.byrecyclerview.databinding.ItemGridBinding;
import me.jingbin.byrecyclerview.utils.DensityUtil;
import me.jingbin.byrecyclerview.utils.ViewUtil;

/**
 * @author jingbin
 */
public class GridAdapter extends BaseBindingAdapter<DataItemBean, ItemGridBinding> {

    private boolean isStaggered = false;

    public GridAdapter() {
        super(R.layout.item_grid);
    }

    public GridAdapter(List<DataItemBean> data) {
        super(R.layout.item_grid, data);
    }

    @Override
    protected void bindView(BaseBindingHolder holder, DataItemBean bean, ItemGridBinding binding, int position) {
        if (isStaggered) {
            if (position % 2 == 0) {
                ViewUtil.setHeight(binding.clGrid, DensityUtil.dip2px(binding.clGrid.getContext(), 100));
            } else {
                ViewUtil.setHeight(binding.clGrid, DensityUtil.dip2px(binding.clGrid.getContext(), 200));
            }
            binding.tvTitle.setText("" + position);
        }
    }

    public boolean isStaggered() {
        return isStaggered;
    }

    public void setStaggered(boolean staggered) {
        isStaggered = staggered;
    }
}
