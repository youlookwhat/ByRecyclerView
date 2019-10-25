package me.jingbin.byrecyclerviewsupport;

import java.util.List;

import me.jingbin.byrecyclerviewsupport.databinding.ItemDataBinding;
import me.jingbin.library.adapter.BaseRecyclerAdapter;

public class DataAdapter extends BaseRecyclerAdapter<DataBean, ItemDataBinding> {

    public DataAdapter(int layoutId) {
        super(layoutId);
    }

    public DataAdapter( List<DataBean> data) {
        super(R.layout.item_data, data);
    }

    @Override
    protected void bindView(DataBean bean, ItemDataBinding binding, int position) {
        binding.tvTitle.setText(bean.getTitle());
    }
}
