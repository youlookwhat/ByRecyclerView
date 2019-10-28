package me.jingbin.byrecyclerview.adapter;


import android.view.ViewGroup;

import java.util.List;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.library.adapter.BaseByListHolder;
import me.jingbin.library.adapter.BaseByListViewAdapter;


/**
 * @author jingbin
 */
public class ListView2Adapter extends BaseByListViewAdapter<DataItemBean, BaseByListHolder<DataItemBean>> {

    public ListView2Adapter(List<DataItemBean> data) {
        super(data);
    }

    @Override
    protected BaseByListHolder<DataItemBean> onCreateViewHolder(ViewGroup parent, int position) {
        return new BaseByListHolder<DataItemBean>(parent, R.layout.item_home) {
            @Override
            protected void onBaseBindView(BaseByListHolder<DataItemBean> holder, DataItemBean bean, int position) {
                holder.setText(R.id.tv_text, bean.getTitle());
            }
        };
    }
}
