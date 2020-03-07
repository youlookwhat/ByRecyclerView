package me.jingbin.library.skeleton;

import androidx.recyclerview.widget.RecyclerView;

import me.jingbin.library.ByRecyclerView;

/**
 * Created by jingbin on 2020-03-06.
 * 骨架图处理,在设置完 RV的一切配置后执行
 */
public class BySkeleton {

    /**
     * 设置RecyclerView的item骨架图
     * 通过单独的adapter设置，所以需要传入RV，adapter(隐藏后设置)，以及要显示的itemLayout
     */
    public static ByRVItemSkeletonScreen.Builder bindItem(RecyclerView recyclerView) {
        return new ByRVItemSkeletonScreen.Builder(recyclerView);
    }

    /**
     * 设置一整块View作为骨架图
     * 通过ByRecyclerView里的setStateView()实现
     */
    public static ByStateViewSkeletonScreen.Builder bindView(ByRecyclerView recyclerView) {
        return new ByStateViewSkeletonScreen.Builder(recyclerView);
    }

}
