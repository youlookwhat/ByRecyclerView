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
public class SkeletonListActivity extends BaseActivity<ActivitySimpleBinding> {

    private int page = 1;
    private DataAdapter mAdapter;
    private ByRVItemSkeletonScreen skeletonScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        setTitle("骨架图 list");

        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new DataAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.addItemDecoration(new SpacesItemDecoration(this).setDrawable(R.drawable.shape_line));
//        binding.recyclerView.setAdapter(mAdapter);
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

        // 设置item骨架图：需要放在配置recyclerView之后设置！！
        skeletonScreen = BySkeleton
                .bindItem(binding.recyclerView)
                .adapter(mAdapter)// 必须设置adapter，且在此之前不要设置adapter
                .shimmer(false)// 是否有动画
                .load(R.layout.layout_by_default_item_skeleton)// item骨架图
                .angle(30)// 微光角度
                .frozen(false) // 是否不可滑动
                .color(R.color.colorWhite)// 动画的颜色
                .duration(1500)// 微光一次显示时间
                .count(10)// item个数
                .show();
        refresh(false);
    }

    private void refresh(boolean isLoadMore) {
        if (!isLoadMore) {
            binding.recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    skeletonScreen.hide();
                    mAdapter.setNewData(DataUtil.get(SkeletonListActivity.this, 6));
                }
            }, 3000);
        } else {
            mAdapter.addData(DataUtil.getMore(SkeletonListActivity.this, 20, page));
            binding.recyclerView.loadMoreComplete();
        }
    }

}
