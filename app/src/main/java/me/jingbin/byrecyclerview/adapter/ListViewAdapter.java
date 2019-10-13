package me.jingbin.byrecyclerview.adapter;


import java.util.List;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.databinding.ItemHomeBinding;
import me.jingbin.library.adapter.BaseListAdapter;


/**
 * @author jingbin
 */
public class ListViewAdapter extends BaseListAdapter<DataItemBean, ItemHomeBinding> {

    public ListViewAdapter(List<DataItemBean> data) {
        super(R.layout.item_home, data);
    }

    @Override
    protected void bindView(DataItemBean bean, ItemHomeBinding binding, int position) {
        binding.tvText.setText(bean.getTitle());
    }
}
