package me.jingbin.byrecyclerview.adapter;

import java.util.List;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.databinding.ItemHomeBinding;
import me.jingbin.library.adapter.BaseRecyclerAdapter;

/**
 * @author jingbin
 */
public class DataAdapter extends BaseRecyclerAdapter<DataItemBean, ItemHomeBinding> {

    public DataAdapter() {
        super(R.layout.item_home);
    }

    public DataAdapter(List<DataItemBean> data) {
        super(R.layout.item_home, data);
    }

    @Override
    protected void bindView(DataItemBean bean, ItemHomeBinding binding, int position) {
        binding.tvText.setText(bean.getTitle());
    }

}
