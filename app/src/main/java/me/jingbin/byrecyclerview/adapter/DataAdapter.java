package me.jingbin.byrecyclerview.adapter;

import java.util.List;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.binding.BaseBindingHolder;
import me.jingbin.byrecyclerview.databinding.ItemHomeBinding;
import me.jingbin.byrecyclerview.binding.BaseBindingAdapter;

/**
 * @author jingbin
 */
public class DataAdapter extends BaseBindingAdapter<DataItemBean, ItemHomeBinding> {

    public DataAdapter() {
        super(R.layout.item_home);
    }

    public DataAdapter(List<DataItemBean> data) {
        super(R.layout.item_home, data);
    }

    @Override
    protected void bindView(BaseBindingHolder holder, ItemHomeBinding binding, DataItemBean bean, int position) {
        binding.tvText.setText(bean.getTitle() + ": " + position);
//        holder.setByRecyclerView(getRecyclerView()).addOnClickListener(R.id.tv_text);
    }

}
