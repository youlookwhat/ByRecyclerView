package me.jingbin.byrecyclerviewsupport;

import java.util.List;

import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;

public class DataAdapter extends BaseRecyclerAdapter<DataBean> {

    public DataAdapter() {
        super(R.layout.item_data);
    }

    public DataAdapter(List<DataBean> data) {
        super(R.layout.item_data, data);
    }

    @Override
    protected void bindView(BaseByViewHolder<DataBean> holder, DataBean bean, int position) {
        holder.setText(R.id.tv_title, bean.getTitle() + "" + position);
    }
}
