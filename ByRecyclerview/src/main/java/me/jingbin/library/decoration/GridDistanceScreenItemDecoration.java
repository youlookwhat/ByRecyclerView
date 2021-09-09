package me.jingbin.library.decoration;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * 给 纵向的GridLayoutManager or StaggeredGridLayoutManager 设置左右间距，横向的设置左间距
 * <p>
 * 注意：默认是去除尾部 mEndFromSize=1，如果没有加载更多请设置 setNoShowSpace(0,0)
 *
 * @author jingbin
 * https://github.com/youlookwhat/ByRecyclerView
 */
public class GridDistanceScreenItemDecoration extends RecyclerView.ItemDecoration {

    /**
     * 每行个数
     */
    private int mSpanCount;
    /**
     * 间距
     */
    private final int mSpacing;
    /**
     * 头部 不显示间距的item个数
     */
    private int mStartFromSize;
    /**
     * 尾部 不显示间距的item个数 默认不处理最后一个item的间距
     */
    private int mEndFromSize = 1;
    /**
     * 横向还是纵向
     */
    private int mOrientation = RecyclerView.VERTICAL;
    /**
     * 瀑布流 头部第一个整行的position
     */
    private int fullPosition = -1;

    /**
     * @param spacing item 间距
     */
    public GridDistanceScreenItemDecoration(int spacing) {
        this.mSpacing = spacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int lastPosition = state.getItemCount() - 1;
        int position = parent.getChildAdapterPosition(view);
        if (mStartFromSize <= position && position <= lastPosition - mEndFromSize) {

            // 行，如果是横向就是列
            int spanGroupIndex = -1;
            // 列，如果是横向就是行
            int column = 0;
            // 瀑布流是否占满一行
            boolean fullSpan = false;

            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
                int spanCount = gridLayoutManager.getSpanCount();
                // 当前position的spanSize
                int spanSize = spanSizeLookup.getSpanSize(position);
                // 横向还是纵向
                mOrientation = gridLayoutManager.getOrientation();
                // 一行几个，如果是横向就是一列几个
                mSpanCount = spanCount / spanSize;
                // =0 表示是最左边 0 2 4
                int spanIndex = spanSizeLookup.getSpanIndex(position, spanCount);
                // 列
                column = spanIndex / spanSize;
                // 行 减去mStartFromSize,得到从0开始的行
                spanGroupIndex = spanSizeLookup.getSpanGroupIndex(position, spanCount) - mStartFromSize;

            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                // 瀑布流获取列方式不一样
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
                // 横向还是纵向
                mOrientation = staggeredGridLayoutManager.getOrientation();
                // 列
                column = params.getSpanIndex();
                // 是否是全一行
                fullSpan = params.isFullSpan();
                mSpanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
            }
            // 减掉不设置间距的position,得到从0开始的position
            position = position - mStartFromSize;

            /*
             * 纵向示例：
             * spacing = 10 ；spanCount = 3
             * --------0--------
             * 10   0   0    10
             * -------0--------
             * 10   0   0   10
             * --------0--------
             */
            if (mOrientation == RecyclerView.VERTICAL) {
                if (fullSpan || mSpanCount == 1) {
                    // 一整行不设置间距，可以自己在布局设置
                    outRect.left = 0;
                    outRect.right = 0;
                } else {
                    if (column == 0) {
                        outRect.left = mSpacing;
                        outRect.right = 0;
                    } else if (column == mSpanCount - 1) {
                        outRect.left = 0;
                        outRect.right = mSpacing;
                    } else {
                        outRect.left = 0;
                        outRect.right = 0;
                    }
                }
            } else {
                if (fullSpan || mSpanCount == 1) {
                    // 一整行不设置间距，可以自己在布局设置
                    outRect.left = 0;
                }
                if (spanGroupIndex > -1) {
                    if (spanGroupIndex == 0) {
                        // 等于第0列都显示左间距
                        outRect.left = mSpacing;
                    }
                } else {
                    if (fullPosition == -1 && position < mSpanCount && fullSpan) {
                        // 找到头部第一行整行的position，后面的左间距都不显示
                        fullPosition = position;
                    }
                    // Stagger显示规则 头部没有整行或者头部体验整行但是在之前的position显示上间距
                    boolean isFirstLineStagger = (fullPosition == -1 || position < fullPosition) && (position < mSpanCount);
                    if (isFirstLineStagger) {
                        // 第一行才有左间距
                        outRect.left = mSpacing;
                    }
                }
            }
        }
    }

    /**
     * 设置从哪个位置 开始设置间距
     *
     * @param startFromSize 一般为HeaderView的个数 + 刷新布局(不一定设置)
     */
    public GridDistanceScreenItemDecoration setStartFrom(int startFromSize) {
        this.mStartFromSize = startFromSize;
        return this;
    }

    /**
     * 设置从哪个位置 结束设置间距。默认为1，默认用户设置了上拉加载
     *
     * @param endFromSize 一般为FooterView的个数 + 加载更多布局(不一定设置)
     */
    public GridDistanceScreenItemDecoration setEndFromSize(int endFromSize) {
        this.mEndFromSize = endFromSize;
        return this;
    }

    /**
     * 设置从哪个位置 结束设置间距
     *
     * @param startFromSize 一般为HeaderView的个数 + 刷新布局(不一定设置)
     * @param endFromSize   默认为1，一般为FooterView的个数 + 加载更多布局(不一定设置)
     */
    public GridDistanceScreenItemDecoration setNoShowSpace(int startFromSize, int endFromSize) {
        this.mStartFromSize = startFromSize;
        this.mEndFromSize = endFromSize;
        return this;
    }
}
