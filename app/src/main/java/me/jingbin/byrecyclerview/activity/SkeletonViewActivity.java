package me.jingbin.byrecyclerview.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.adapter.DataAdapter;
import me.jingbin.byrecyclerview.app.BaseActivity;
import me.jingbin.byrecyclerview.databinding.ActivitySimpleBinding;
import me.jingbin.byrecyclerview.utils.DataUtil;
import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.decoration.SpacesItemDecoration;
import me.jingbin.library.skeleton.BySkeleton;
import me.jingbin.library.skeleton.ByStateViewSkeletonScreen;

/**
 * @author jingbin
 */
public class SkeletonViewActivity extends BaseActivity<ActivitySimpleBinding> {

    private int page = 1;
    private DataAdapter mAdapter;
    private ByStateViewSkeletonScreen skeletonScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        setTitle("骨架图 View");

        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new DataAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.addItemDecoration(new SpacesItemDecoration(this).setDrawable(R.drawable.shape_line));
        // 设置view 需要设置adapter
        binding.recyclerView.setAdapter(mAdapter);
        binding.recyclerView.addHeaderView(R.layout.layout_skeleton_headerview);
        binding.recyclerView.setOnLoadMoreListener(new ByRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (page == 3) {
                    binding.recyclerView.loadMoreEnd();
                    return;
                }
                page++;
                refresh(true);
            }
        }, 500);

        binding.recyclerView.setHeaderViewEnabled(false);
        // 这是一组
        skeletonScreen = BySkeleton
                .bindView(binding.recyclerView)
                .load(R.layout.layout_skeleton_view)
                .angle(20)// 微光角度
                .color(R.color.colorWhite)// 动画的颜色
                .duration(1500)
                .shimmer(true)// 是否有动画
                .show();
        refresh(false);
    }

    private void refresh(boolean isLoadMore) {
        if (!isLoadMore) {
            binding.recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.recyclerView.setHeaderViewEnabled(true);
                    skeletonScreen.hide();
                    mAdapter.setNewData(DataUtil.get(SkeletonViewActivity.this, 10));
                }
            }, 3000);
        } else {
            mAdapter.addData(DataUtil.getMore(SkeletonViewActivity.this, 20, page));
            binding.recyclerView.loadMoreComplete();
        }
    }

}
