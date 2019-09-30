package me.jingbin.jrecyclerview.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.List;

import me.jingbin.jrecyclerview.R;
import me.jingbin.jrecyclerview.bean.HomeItemBean;
import me.jingbin.jrecyclerview.bean.MainItemBean;
import me.jingbin.jrecyclerview.databinding.ItemHomeBinding;
import me.jingbin.library.adapter.BaseRecyclerViewAdapter;
import me.jingbin.library.adapter.BaseRecyclerViewHolder;

/**
 * @author jingbin
 */
public class MainAdapter extends BaseRecyclerViewAdapter<MainItemBean> {
    public MainAdapter() {
    }

    public MainAdapter(List<MainItemBean> data) {
        super(data);
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_home);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<MainItemBean, ItemHomeBinding> {
        ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        public void onBindViewHolder(MainItemBean bean, final int position) {
            binding.tvText.setText(bean.getTitle());
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    LogHelper.e("内部点击:" + position);
//                }
//            });
        }
    }
}
