package me.jingbin.library.decoration;

/*
 * Copyright 2019. Bin Jing (https://github.com/youlookwhat)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * 给 GridLayoutManager or StaggeredGridLayoutManager 设置间距，可设置去除首尾间距个数
 *
 * @author jingbin
 * https://github.com/youlookwhat/ByRecyclerView
 */

public class GridSpaceItemDecoration extends RecyclerView.ItemDecoration {

    /**
     * 每行个数
     */
    private int mSpanCount;
    /**
     * 间距
     */
    private int mSpacing;
    /**
     * 距屏幕周围是否也有间距
     */
    private boolean mIncludeEdge;
    /**
     * 头部 不显示间距的item个数
     */
    private int mStartFromSize;
    /**
     * 尾部 不显示间距的item个数 默认不处理最后一个item的间距
     */
    private int mEndFromSize = 1;
    /**
     * 瀑布流 头部第一个整行的position
     */
    private int fullPosition = -1;
    /**
     * 横向还是纵向
     */
    private int mOrientation = RecyclerView.VERTICAL;

    public GridSpaceItemDecoration(int spacing) {
        this(spacing, true);
    }

    /**
     * @param spacing     item 间距
     * @param includeEdge item 距屏幕周围是否也有间距
     */
    public GridSpaceItemDecoration(int spacing, boolean includeEdge) {
        this.mSpacing = spacing;
        this.mIncludeEdge = includeEdge;
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

            if (mIncludeEdge) {
                /*
                 *示例：
                 * spacing = 10 ；spanCount = 3
                 * ---------10--------
                 * 10   3+7   6+4    10
                 * ---------10--------
                 * 10   3+7   6+4    10
                 * ---------10--------
                 */
                if (fullSpan) {
                    outRect.left = 0;
                    outRect.right = 0;
                } else {
                    if (mOrientation == RecyclerView.VERTICAL) {
                        outRect.left = mSpacing - column * mSpacing / mSpanCount;
                        outRect.right = (column + 1) * mSpacing / mSpanCount;
                    } else {
                        // 如果是横向对应的是上下
                        outRect.top = mSpacing - column * mSpacing / mSpanCount;
                        outRect.bottom = (column + 1) * mSpacing / mSpanCount;
                    }
                }

                if (spanGroupIndex > -1) {
                    // grid 显示规则
                    if (spanGroupIndex < 1 && position < mSpanCount) {
                        if (mOrientation == RecyclerView.VERTICAL) {
                            // 第一行才有上间距
                            outRect.top = mSpacing;
                        } else {
                            // 第一列才有左间距
                            outRect.left = mSpacing;
                        }
                    }
                } else {
                    if (fullPosition == -1 && position < mSpanCount && fullSpan) {
                        // 找到头部第一个整行的position，后面的上间距都不显示
                        fullPosition = position;
                    }
                    // Stagger显示规则 头部没有整行或者头部体验整行但是在之前的position显示上间距
                    boolean isFirstLineStagger = (fullPosition == -1 || position < fullPosition) && (position < mSpanCount);
                    if (isFirstLineStagger) {
                        if (mOrientation == RecyclerView.VERTICAL) {
                            // 第一行才有上间距
                            outRect.top = mSpacing;
                        } else {
                            outRect.left = mSpacing;
                        }
                    }
                }
                if (mOrientation == RecyclerView.VERTICAL) {
                    outRect.bottom = mSpacing;
                } else {
                    outRect.right = mSpacing;
                }

            } else {
                /*
                 *示例：
                 * spacing = 10 ；spanCount = 3
                 * --------0--------
                 * 0   3+7   6+4    0
                 * -------10--------
                 * 0   3+7   6+4    0
                 * --------0--------
                 */
                if (fullSpan) {
                    outRect.left = 0;
                    outRect.right = 0;
                } else {
                    if (mOrientation == RecyclerView.VERTICAL) {
                        outRect.left = column * mSpacing / mSpanCount;
                        outRect.right = mSpacing - (column + 1) * mSpacing / mSpanCount;
                    } else {
                        outRect.top = column * mSpacing / mSpanCount;
                        outRect.bottom = mSpacing - (column + 1) * mSpacing / mSpanCount;
                    }
                }

                if (spanGroupIndex > -1) {
                    if (spanGroupIndex >= 1) {
                        if (mOrientation == RecyclerView.VERTICAL) {
                            // 超过第0行都显示上间距
                            outRect.top = mSpacing;
                        } else {
                            // 超过第0列都显示左间距
                            outRect.left = mSpacing;
                        }
                    }
                } else {
                    if (fullPosition == -1 && position < mSpanCount && fullSpan) {
                        // 找到头部第一个整行的position
                        fullPosition = position;
                    }
                    // Stagger上间距显示规则
                    boolean isStaggerShowTop = position >= mSpanCount || (fullSpan && position != 0) || (fullPosition != -1 && position != 0);

                    if (isStaggerShowTop) {
                        if (mOrientation == RecyclerView.VERTICAL) {
                            // 超过第0行都显示上间距
                            outRect.top = mSpacing;
                        } else {
                            // 超过第0列都显示左间距
                            outRect.left = mSpacing;
                        }
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
    public GridSpaceItemDecoration setStartFrom(int startFromSize) {
        this.mStartFromSize = startFromSize;
        return this;
    }

    /**
     * 设置从哪个位置 结束设置间距。默认为1，默认用户设置了上拉加载
     *
     * @param endFromSize 一般为FooterView的个数 + 加载更多布局(不一定设置)
     */
    public GridSpaceItemDecoration setEndFromSize(int endFromSize) {
        this.mEndFromSize = endFromSize;
        return this;
    }

    /**
     * 设置从哪个位置 结束设置间距
     *
     * @param startFromSize 一般为HeaderView的个数 + 刷新布局(不一定设置)
     * @param endFromSize   默认为1，一般为FooterView的个数 + 加载更多布局(不一定设置)
     */
    public GridSpaceItemDecoration setNoShowSpace(int startFromSize, int endFromSize) {
        this.mStartFromSize = startFromSize;
        this.mEndFromSize = endFromSize;
        return this;
    }
}
