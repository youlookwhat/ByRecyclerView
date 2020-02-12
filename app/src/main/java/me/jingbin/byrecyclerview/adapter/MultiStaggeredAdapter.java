package me.jingbin.byrecyclerview.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.List;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.binding.BaseBindingHolder;
import me.jingbin.byrecyclerview.databinding.ItemMultiGridBinding;
import me.jingbin.byrecyclerview.utils.DensityUtil;
import me.jingbin.byrecyclerview.utils.ViewUtil;
import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.adapter.BaseByRecyclerViewAdapter;
import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.stickyview.StickyHeaderHandler;

/**
 * 多种类型 示例
 *
 * @author jingbin
 */
public class MultiStaggeredAdapter extends BaseByRecyclerViewAdapter<DataItemBean, BaseByViewHolder<DataItemBean>> {

    public MultiStaggeredAdapter(List<DataItemBean> data) {
        super(data);
    }

    @Override
    public int getItemViewType(int position) {
        if (0 <= position && position < getData().size()) {
            DataItemBean itemData = getItemData(position);
            if ("title".equals(itemData.getType())) {
                return StickyHeaderHandler.TYPE_STICKY_VIEW;
            } else {
                return 2;
            }
        }
        return 2;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull BaseByViewHolder<DataItemBean> holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        ByRecyclerView byRecyclerView = holder.getByRecyclerView();
        if (lp != null
                && byRecyclerView != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && (byRecyclerView.isHeaderView(holder.getLayoutPosition())
                || byRecyclerView.isFootView(holder.getLayoutPosition())
                || byRecyclerView.isRefreshHeader(holder.getLayoutPosition())
                || byRecyclerView.isLoadMoreView(holder.getLayoutPosition())
                || byRecyclerView.isStateView(holder.getLayoutPosition())
                || holder.getItemViewType() == StickyHeaderHandler.TYPE_STICKY_VIEW)) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    @NonNull
    @Override
    public BaseByViewHolder<DataItemBean> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (StickyHeaderHandler.TYPE_STICKY_VIEW == viewType) {
            return new TitleHolder(parent, R.layout.item_multi_title);
        } else {
            return new ViewHolder(parent, R.layout.item_multi_grid);
        }
    }

    /**
     * 使用 BaseByViewHolder
     */
    private class TitleHolder extends BaseByViewHolder<DataItemBean> {
        TitleHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        protected void onBaseBindView(BaseByViewHolder<DataItemBean> holder, DataItemBean bean, int position) {
            holder.setText(R.id.tv_title, bean.getDes() + "-" + position);
        }
    }

    /**
     * 使用 BaseBindingHolder
     */
    private class ViewHolder extends BaseBindingHolder<DataItemBean, ItemMultiGridBinding> {
        ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        protected void onBindingView(BaseBindingHolder holder, DataItemBean bean, int position) {
            binding.tvText.setText(bean.getDes() + "-" + position);
            if (position % 2 == 0) {
                ViewUtil.setHeight(binding.cvItemGrid, DensityUtil.dip2px(binding.tvText.getContext(), 94));
            } else {
                ViewUtil.setHeight(binding.cvItemGrid, DensityUtil.dip2px(binding.tvText.getContext(), 200));
            }
        }
    }
}
