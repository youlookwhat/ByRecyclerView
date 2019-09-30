package me.jingbin.byrecyclerview.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.List;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.bean.HomeItemBean;
import me.jingbin.byrecyclerview.databinding.ItemHomeBinding;
import me.jingbin.library.adapter.BaseRecyclerViewAdapter;
import me.jingbin.library.adapter.BaseRecyclerViewHolder;

/**
 * @author jingbin
 */
public class HomeAdapter extends BaseRecyclerViewAdapter<HomeItemBean> {
    public HomeAdapter() {
    }

    public HomeAdapter(List<HomeItemBean> data) {
        super(data);
    }

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
        public void onBindViewHolder(HomeItemBean bean, final int position) {
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
