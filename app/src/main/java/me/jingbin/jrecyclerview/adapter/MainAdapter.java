package me.jingbin.jrecyclerview.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.List;

import me.jingbin.jrecyclerview.R;
import me.jingbin.jrecyclerview.bean.HomeItemBean;
import me.jingbin.jrecyclerview.bean.MainItemBean;
import me.jingbin.jrecyclerview.databinding.ItemHomeBinding;
import me.jingbin.jrecyclerview.databinding.ItemMainBinding;
import me.jingbin.library.adapter.BaseRecyclerViewAdapter;
import me.jingbin.library.adapter.BaseRecyclerViewHolder;

/**
 * @author jingbin
 */
public class MainAdapter extends BaseRecyclerViewAdapter<MainItemBean> {

    public MainAdapter(List<MainItemBean> data) {
        super(data);
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_main);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<MainItemBean, ItemMainBinding> {
        ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        public void onBindViewHolder(MainItemBean bean, final int position) {
            binding.tvText.setText(position + 1 + "、 " + bean.getTitle());
        }
    }
}
