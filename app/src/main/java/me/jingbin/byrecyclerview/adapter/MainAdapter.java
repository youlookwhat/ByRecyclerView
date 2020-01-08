package me.jingbin.byrecyclerview.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.List;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.bean.MainItemBean;
import me.jingbin.byrecyclerview.binding.BaseBindingHolder;
import me.jingbin.byrecyclerview.databinding.ItemMainBinding;
import me.jingbin.library.adapter.BaseByRecyclerViewAdapter;
import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.decoration.StickyView;

/**
 * @author jingbin
 */
public class MainAdapter extends BaseByRecyclerViewAdapter<MainItemBean, BaseByViewHolder<MainItemBean>> {

    public MainAdapter(List<MainItemBean> data) {
        super(data);
    }

    @Override
    public int getItemViewType(int position) {
        if (0 <= position && position < getData().size()) {
            MainItemBean itemData = getItemData(position);
            if (itemData.isCategoryName()) {
                return StickyView.TYPE_STICKY_VIEW;
            } else {
                return 2;
            }
        }
        return 2;
    }

    @NonNull
    @Override
    public BaseByViewHolder<MainItemBean> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (StickyView.TYPE_STICKY_VIEW == viewType) {
            return new TitleHolder(parent, R.layout.item_multi_title);
        } else {
            return new ViewHolder(parent, R.layout.item_main);
        }
    }

    private class TitleHolder extends BaseByViewHolder<MainItemBean> {
        TitleHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        protected void onBaseBindView(BaseByViewHolder<MainItemBean> holder, MainItemBean bean, int position) {
            holder.setText(R.id.tv_title, bean.getTitle());
        }
    }

    private class ViewHolder extends BaseBindingHolder<MainItemBean, ItemMainBinding> {
        ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        protected void onBindingView(BaseBindingHolder holder, MainItemBean bean, int position) {
            binding.tvText.setText(bean.getTitle());
            binding.tvSort.setText(bean.getSort() + "、");

        }
    }

}
