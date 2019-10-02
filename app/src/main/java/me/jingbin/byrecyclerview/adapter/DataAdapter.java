package me.jingbin.byrecyclerview.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.List;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.databinding.ItemHomeBinding;
import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.adapter.BaseByRecyclerViewAdapter;
import me.jingbin.library.adapter.BaseByRecyclerViewHolder;

/**
 * @author jingbin
 */
public class DataAdapter extends BaseByRecyclerViewAdapter<DataItemBean> {

    public DataAdapter() {
    }

    public DataAdapter(ByRecyclerView recyclerView) {
        super(recyclerView);
    }

    public DataAdapter(ByRecyclerView recyclerView, List<DataItemBean> data) {
        super(recyclerView, data);
    }

    @NonNull
    @Override
    public BaseByRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_home);
    }

    private class ViewHolder extends BaseByRecyclerViewHolder<DataItemBean, ItemHomeBinding> {
        ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        public void onBindViewHolder(DataItemBean bean, final int position) {
            binding.tvText.setText(bean.getTitle());
        }
    }
}
