package me.jingbin.library.skeleton;

import androidx.recyclerview.widget.RecyclerView;

import me.jingbin.library.ByRecyclerView;

/**
 * Created by jingbin on 2020-03-06.
 * 骨架图处理,在设置完 ByRV 的一切配置后执行
 */
public class BySkeleton {

    /**
     * 设置item骨架图：通过额外setAdapter实现 【在之前 不能 setAdapter()】
     * 配置方法：
     * adapter      必须！
     * load         item骨架图        默认 layout_by_default_item_skeleton
     * shimmer      是否有微光动画     默认 true
     * angle        微光角度          默认 20
     * frozen       是否不可滑动       默认 true不可滑动
     * color        微光的颜色        默认 R.color.by_skeleton_shimmer_color
     * duration     微光一次显示时间   默认 1000
     * count        item个数         默认 10
     */
    public static ByRVItemSkeletonScreen.Builder bindItem(RecyclerView recyclerView) {
        return new ByRVItemSkeletonScreen.Builder(recyclerView);
    }

    /**
     * 设置view骨架图：通过setStateView实现 【在之前 需要 setAdapter()】
     * 配置方法：
     * load         必须！view骨架图
     * shimmer      是否有微光动画     默认 true
     * angle        微光角度          默认 20
     * color        微光的颜色        默认 R.color.by_skeleton_shimmer_color
     * duration     微光一次显示时间   默认 1000
     */
    public static ByStateViewSkeletonScreen.Builder bindView(ByRecyclerView recyclerView) {
        return new ByStateViewSkeletonScreen.Builder(recyclerView);
    }

}
