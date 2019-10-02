package me.jingbin.byrecyclerview.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.List;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.bean.MainItemBean;
import me.jingbin.byrecyclerview.databinding.ItemMainBinding;
import me.jingbin.library.adapter.BaseByRecyclerViewAdapter;
import me.jingbin.library.adapter.BaseByRecyclerViewHolder;

/**
 * @author jingbin
 */
public class MainAdapter extends BaseByRecyclerViewAdapter<MainItemBean> {

    public MainAdapter(List<MainItemBean> data) {
        super(data);
    }

    @NonNull
    @Override
    public BaseByRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_main);
    }

    private class ViewHolder extends BaseByRecyclerViewHolder<MainItemBean, ItemMainBinding> {
        ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        public void onBindViewHolder(MainItemBean bean, final int position) {
            binding.tvText.setText(position + 1 + "、 " + bean.getTitle());
        }
    }
}
