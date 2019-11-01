package me.jingbin.byrecyclerview.activity;

import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.adapter.DataAdapter;
import me.jingbin.byrecyclerview.app.BaseActivity;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.databinding.ActivitySimpleBinding;
import me.jingbin.byrecyclerview.utils.DataUtil;
import me.jingbin.byrecyclerview.utils.ToastUtil;
import me.jingbin.byrecyclerview.view.NeteaseLoadMoreView;
import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.SimpleRefreshHeaderView;
import me.jingbin.library.config.ByDividerItemDecoration;

/**
 * @author jingbin
 */
public class SimpleActivity extends BaseActivity<ActivitySimpleBinding> {

    private int page = 1;
    private DataAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        setTitle("基本使用");

        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new DataAdapter(DataUtil.get(this, 6));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        ByDividerItemDecoration itemDecoration = new ByDividerItemDecoration(binding.recyclerView.getContext(), androidx.recyclerview.widget.DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(binding.recyclerView.getContext(), R.drawable.shape_line));
        binding.recyclerView.addItemDecoration(itemDecoration);
        binding.recyclerView.setLoadingMoreView(new NeteaseLoadMoreView(this));
        binding.recyclerView.setRefreshHeaderView(new SimpleRefreshHeaderView(this));
        binding.recyclerView.setAdapter(mAdapter);
        binding.recyclerView.setOnRefreshListener(new ByRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.recyclerView.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        binding.recyclerView.setOnLoadMoreListener(new ByRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                binding.recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (page == 3) {
                            binding.recyclerView.loadMoreEnd();
                            return;
                        }
                        page++;
                        mAdapter.addData(DataUtil.getMore(SimpleActivity.this, 20, page));
                        binding.recyclerView.loadMoreComplete();
                    }
                }, 500);
            }
        });
        binding.recyclerView.setOnItemClickListener(new ByRecyclerView.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                DataItemBean itemData = mAdapter.getItemData(position);
                ToastUtil.showToast(itemData.getTitle());
            }
        });
    }
}
