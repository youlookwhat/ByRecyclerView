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
import me.jingbin.library.skeleton.ByRVItemSkeletonScreen;
import me.jingbin.library.skeleton.BySkeleton;

/**
 * @author jingbin
 */
public class SkeletonHeaderViewActivity extends BaseActivity<ActivitySimpleBinding> {

    private int page = 1;
    private DataAdapter mAdapter;
    private ByRVItemSkeletonScreen skeletonScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        setTitle("骨架图 含有headerView");

        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new DataAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.addItemDecoration(new SpacesItemDecoration(this).setDrawable(R.drawable.shape_line));
//        binding.recyclerView.setAdapter(mAdapter);
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

        // 这是一组
        skeletonScreen = BySkeleton
                .bindItem(binding.recyclerView)
                .adapter(mAdapter)
                .load(R.layout.layout_by_default_item_skeleton)
                .shimmer(false)// 是否有动画
                .angle(30)// 微光角度
                .frozen(false) // 是否不可滑动
                .color(R.color.colorWhite)// 动画的颜色
                .duration(1500)
                .count(10)
                .show();
        refresh(false);
    }

    private void refresh(boolean isLoadMore) {
        if (!isLoadMore) {
            binding.recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    skeletonScreen.hide();
                    mAdapter.setNewData(DataUtil.get(SkeletonHeaderViewActivity.this, 10));
                }
            }, 3000);
        } else {
            mAdapter.addData(DataUtil.getMore(SkeletonHeaderViewActivity.this, 20, page));
            binding.recyclerView.loadMoreComplete();
        }
    }

}
