package me.jingbin.jrecyclerview.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import me.jingbin.jrecyclerview.R;
import me.jingbin.jrecyclerview.bean.HomeItemBean;
import me.jingbin.jrecyclerview.databinding.ItemHomeBinding;
import me.jingbin.library.adapter.BaseRecyclerViewAdapter;
import me.jingbin.library.adapter.BaseRecyclerViewHolder;

public class HomeAdapter extends BaseRecyclerViewAdapter<HomeItemBean> {
    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_home);
    }


    private class ViewHolder extends BaseRecyclerViewHolder<HomeItemBean, ItemHomeBinding> {
        ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        public void onBindViewHolder(HomeItemBean object, int position) {
            binding.tvText.setText(object.getTitle());
        }
    }
}
