package me.jingbin.byrecyclerview.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.List;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.binding.BaseBindingHolder;
import me.jingbin.byrecyclerview.databinding.ItemHomeBinding;
import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.adapter.BaseByRecyclerViewAdapter;
import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.decoration.StickyView;

/**
 * 多种类型 示例
 *
 * @author jingbin
 */
public class MultiAdapter extends BaseByRecyclerViewAdapter<DataItemBean, BaseByViewHolder<DataItemBean>> {

    public MultiAdapter(List<DataItemBean> data) {
        super(data);
    }

    @Override
    public int getItemViewType(int position) {
        if (0 <= position && position < getData().size()) {
            DataItemBean itemData = getItemData(position);
            if ("title".equals(itemData.getType())) {
                return StickyView.TYPE_STICKY_VIEW;
            } else {
                return 2;
            }
        }
        return 2;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            final ByRecyclerView byRecyclerView = (ByRecyclerView) recyclerView;

            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (byRecyclerView.isLoadMoreView(position)
                            || byRecyclerView.isFootView(position)
                            || byRecyclerView.isStateView(position)
                            || byRecyclerView.isRefreshHeader(position)
                            || byRecyclerView.isHeaderView(position)) {
                        return gridManager.getSpanCount();
                    }
                    int type = getItemViewType(position - byRecyclerView.getCustomTopItemViewCount());
                    switch (type) {
                        case StickyView.TYPE_STICKY_VIEW:
                            // title栏显示一列
                            return gridManager.getSpanCount();
                        default:
                            //默认显示2列
                            return 3;
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public BaseByViewHolder<DataItemBean> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (StickyView.TYPE_STICKY_VIEW == viewType) {
            return new TitleHolder(parent, R.layout.item_multi_title);
        } else {
            return new ViewHolder(parent, R.layout.item_home);
        }
    }

    private class TitleHolder extends BaseByViewHolder<DataItemBean> {
        TitleHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        protected void onBaseBindView(BaseByViewHolder<DataItemBean> holder, DataItemBean bean, int position) {
            holder.setText(R.id.tv_title, bean.getDes() + "-" + position);
        }
    }

    private class ViewHolder extends BaseBindingHolder<DataItemBean, ItemHomeBinding> {
        ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        protected void onBindingView(BaseBindingHolder holder, DataItemBean bean, int position) {
            binding.tvText.setText(bean.getDes() + "-" + position);
        }
    }
}
