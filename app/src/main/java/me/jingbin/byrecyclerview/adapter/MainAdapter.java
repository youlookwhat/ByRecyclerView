package me.jingbin.byrecyclerview.adapter;

import java.util.List;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.bean.MainItemBean;
import me.jingbin.byrecyclerview.binding.BaseBindingHolder;
import me.jingbin.byrecyclerview.databinding.ItemMainBinding;
import me.jingbin.byrecyclerview.binding.BaseBindingAdapter;

/**
 * @author jingbin
 */
public class MainAdapter extends BaseBindingAdapter<MainItemBean, ItemMainBinding> {

    public MainAdapter(List<MainItemBean> data) {
        super(R.layout.item_main, data);
    }

    @Override
    protected void bindView(BaseBindingHolder holder, MainItemBean bean, ItemMainBinding binding, int position) {
        binding.tvSort.setText(position + 1 + "、 ");
        binding.tvText.setText(bean.getTitle());
    }

}
