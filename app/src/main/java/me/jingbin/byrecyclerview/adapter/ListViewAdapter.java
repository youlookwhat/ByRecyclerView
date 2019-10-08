package me.jingbin.byrecyclerview.adapter;


import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.List;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.databinding.ItemHomeBinding;
import me.jingbin.library.adapter.BaseByListViewAdapter;
import me.jingbin.library.adapter.BaseByListViewHolder;


/**
 * @author jingbin
 */
public class ListViewAdapter extends BaseByListViewAdapter<DataItemBean> {

    public ListViewAdapter(List<DataItemBean> data) {
        super(data);
    }

    @NonNull
    @Override
    public BaseByListViewHolder createHolder(@NonNull ViewGroup parent, int viewType) {
        return new OneViewHolder(parent, R.layout.item_home);
    }

    private class OneViewHolder extends BaseByListViewHolder<DataItemBean, ItemHomeBinding> {

        OneViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindView(DataItemBean bean, final int position) {
            if (bean != null) {
                binding.tvText.setText(bean.getTitle());
            }
        }
    }


}
