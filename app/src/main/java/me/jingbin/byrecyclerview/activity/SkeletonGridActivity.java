package me.jingbin.byrecyclerview.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.app.BaseActivity;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.databinding.ActivitySimpleBinding;
import me.jingbin.byrecyclerview.utils.DataUtil;
import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;
import me.jingbin.library.decoration.GridSpaceItemDecoration;
import me.jingbin.library.skeleton.ByRVItemSkeletonScreen;
import me.jingbin.library.skeleton.BySkeleton;

/**
 * @author jingbin
 */
public class SkeletonGridActivity extends BaseActivity<ActivitySimpleBinding> {

    private int page = 1;
    private BaseRecyclerAdapter mAdapter;
    private ByRVItemSkeletonScreen skeletonScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        setTitle("骨架图 grid");

        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new BaseRecyclerAdapter<DataItemBean>(R.layout.item_grid_image) {
            @Override
            protected void bindView(BaseByViewHolder<DataItemBean> holder, DataItemBean bean, int position) {

            }
        };
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setRefreshEnabled(true);
        binding.recyclerView.addItemDecoration(new GridSpaceItemDecoration(2, 2, false).setStartFrom(1));
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
        binding.recyclerView.setOnRefreshListener(new ByRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                mAdapter.setNewData(DataUtil.get(SkeletonGridActivity.this, 6));
            }
        }, 500);

        // 这是一组
        skeletonScreen = BySkeleton
                .bindItem(binding.recyclerView)
                .adapter(mAdapter)
                .load(R.layout.item_skeleton_grid_image)
                .shimmer(true)// 是否有动画
                .angle(30)// 微光角度
                .frozen(true) // 是否不可滑动
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
                    mAdapter.setNewData(DataUtil.get(SkeletonGridActivity.this, 20));
                }
            }, 3000);
        } else {
            mAdapter.addData(DataUtil.getMore(SkeletonGridActivity.this, 20, page));
            binding.recyclerView.loadMoreComplete();
        }
    }

}
