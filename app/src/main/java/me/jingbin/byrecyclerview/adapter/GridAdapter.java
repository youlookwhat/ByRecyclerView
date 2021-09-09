package me.jingbin.byrecyclerview.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.List;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.utils.DensityUtil;
import me.jingbin.byrecyclerview.utils.ViewUtil;
import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;
import me.jingbin.library.stickyview.StickyHeaderHandler;

/**
 * @author jingbin
 */
public class GridAdapter extends BaseRecyclerAdapter<DataItemBean> {

    private boolean isStaggered = false;

    public GridAdapter() {
        super(R.layout.item_grid);
    }

    public GridAdapter(List<DataItemBean> data) {
        super(R.layout.item_grid, data);
    }

    @Override
    protected void bindView(BaseByViewHolder<DataItemBean> holder, DataItemBean bean, int position) {
        if (isStaggered) {
            View clGrid = holder.getView(R.id.cl_grid);
            TextView tvTitle = holder.getView(R.id.tv_title);
            if (position % 2 == 0) {
                ViewUtil.setHeight(clGrid, DensityUtil.dip2px(clGrid.getContext(), 100));
            } else {
                ViewUtil.setHeight(clGrid, DensityUtil.dip2px(clGrid.getContext(), 200));
            }
            tvTitle.setText("" + position);
        }
    }

    /**
     * 设置Grid多类型
     */
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
                    int adapterPosition = position - byRecyclerView.getCustomTopItemViewCount();
                    switch (adapterPosition) {
                        case 0:
                            // title栏显示一行
                            return gridManager.getSpanCount();
                        case 4:
                            return 3;
                        case 5:
                            return 3;
                        case 6:
                            return gridManager.getSpanCount();
                        default:
                            //默认显示2列
                            return 2;
                    }
                }
            });
        }
    }

    /**
     * 设置StaggeredGrid多类型
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == 2 || position == 8) {
            return StickyHeaderHandler.TYPE_STICKY_VIEW;
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

    public boolean isStaggered() {
        return isStaggered;
    }

    public void setStaggered(boolean staggered) {
        isStaggered = staggered;
    }
}
