package me.jingbin.byrecyclerview.adapter;

import java.util.List;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;

/**
 * @author jingbin
 */
public class OneTypeAdapter extends BaseRecyclerAdapter<DataItemBean> {

    public OneTypeAdapter(List<DataItemBean> data) {
        super(R.layout.item_main, data);
    }

    @Override
    protected void bindView(BaseByViewHolder<DataItemBean> holder, DataItemBean bean, int position) {
        holder.setText(R.id.tv_text, bean.getTitle())
                .addOnClickListener(R.id.tv_text)
                .addOnLongClickListener(R.id.tv_text);
    }

}
