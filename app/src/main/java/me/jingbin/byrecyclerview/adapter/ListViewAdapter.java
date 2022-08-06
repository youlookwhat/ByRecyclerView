package me.jingbin.byrecyclerview.adapter;


import android.view.ViewGroup;

import java.util.List;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.library.adapter.BaseListHolder;
import me.jingbin.library.adapter.BaseListAdapter;


/**
 * @author jingbin
 */
public class ListViewAdapter extends BaseListAdapter<DataItemBean, BaseListHolder> {

    public ListViewAdapter(List<DataItemBean> data) {
        super(data);
    }

    @Override
    protected BaseListHolder onCreateViewHolder(ViewGroup parent, int position) {
        return new BaseListHolder(parent, R.layout.item_home);
    }

    @Override
    protected void onBindView(BaseListHolder holder, DataItemBean bean, int position) {
        holder.setText(R.id.tv_text, bean.getTitle() + "_" + position);
    }

}
