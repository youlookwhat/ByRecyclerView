package me.jingbin.byrecyclerview.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.tabs.TabLayout;

import java.util.Arrays;
import java.util.List;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.adapter.DataAdapter;
import me.jingbin.byrecyclerview.app.BaseActivity;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.databinding.ActivityAppbarLayoutBinding;
import me.jingbin.byrecyclerview.databinding.LayoutHeaderViewAppbarBinding;
import me.jingbin.byrecyclerview.utils.DataUtil;
import me.jingbin.byrecyclerview.utils.StatusBarUtil;
import me.jingbin.byrecyclerview.utils.TabLayoutUtil;
import me.jingbin.byrecyclerview.utils.ToastUtil;
import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.decoration.SpacesItemDecoration;

/**
 * @author jingbin
 */
public class AppBarLayoutActivity extends BaseActivity<ActivityAppbarLayoutBinding> {

    private int page = 1;
    private int lastPosition = 0;
    private DataAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appbar_layout);
        StatusBarUtil.showTransparentStatusBarPadding(this, binding.toolbar);
        binding.collapsing.setTitle("CoordinatorLayout 使用示例");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initAdapter();
    }

    private void initAdapter() {
        final LayoutHeaderViewAppbarBinding headerBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.layout_header_view_appbar, (ViewGroup) binding.recyclerView.getParent(), false);
        final LayoutHeaderViewAppbarBinding headerBinding2 = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.layout_header_view_appbar, (ViewGroup) binding.recyclerView.getParent(), false);
        final LayoutHeaderViewAppbarBinding headerBinding3 = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.layout_header_view_appbar, (ViewGroup) binding.recyclerView.getParent(), false);
        List<String> asList = Arrays.asList("Header1", "Header2", "Header3", "Content");
        headerBinding.tvText.setText("Header1");
        headerBinding2.tvText.setText("Header2");
        headerBinding3.tvText.setText("Header3");

        for (String name : asList) {
            TabLayout.Tab tab = binding.tabLayout.newTab();
            tab.setText(name);
            binding.tabLayout.addTab(tab);
        }

        mAdapter = new DataAdapter(DataUtil.getMore(AppBarLayoutActivity.this, 20, page));
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.addHeaderView(headerBinding.getRoot());
        binding.recyclerView.addHeaderView(headerBinding2.getRoot());
        binding.recyclerView.addHeaderView(headerBinding3.getRoot());
        binding.recyclerView.addItemDecoration(new SpacesItemDecoration(this, SpacesItemDecoration.VERTICAL, binding.recyclerView.getCustomTopItemViewCount()));
        binding.recyclerView.setAdapter(mAdapter);

        binding.recyclerView.setOnLoadMoreListener(new ByRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (page == 3) {
                    binding.recyclerView.loadMoreEnd();
                    return;
                }
                page++;
                mAdapter.addData(DataUtil.getMore(AppBarLayoutActivity.this, 6, page));
                binding.recyclerView.loadMoreComplete();
            }
        }, 500);
        binding.recyclerView.setOnItemClickListener(new ByRecyclerView.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                DataItemBean itemData = mAdapter.getItemData(position);
                ToastUtil.showToast(itemData.getTitle());
            }
        });
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (lastPosition != firstVisibleItemPosition) {
                    lastPosition = firstVisibleItemPosition;
                    if (firstVisibleItemPosition >= 3) {
                        binding.tabLayout.getTabAt(3).select();
                    } else {
                        binding.tabLayout.getTabAt(firstVisibleItemPosition).select();
                    }
                }
            }
        });
        TabLayoutUtil.setTabClick(binding.tabLayout, new TabLayoutUtil.OnTabClickListener() {
            @Override
            public void onTabClick(int position) {
                layoutManager.scrollToPositionWithOffset(position, 0);
            }
        });

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.setNewData(DataUtil.getMore(AppBarLayoutActivity.this, 20, page));
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}
