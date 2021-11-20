package me.jingbin.byrecyclerview.activity;

import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.adapter.PayloadAdapter;
import me.jingbin.byrecyclerview.app.BaseActivity;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.databinding.ActivitySimpleBinding;
import me.jingbin.byrecyclerview.utils.DataUtil;
import me.jingbin.byrecyclerview.view.NeteaseLoadMoreView;
import me.jingbin.byrecyclerview.view.NeteaseRefreshHeaderView;
import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.decoration.SpacesItemDecoration;

/**
 * @author jingbin
 */
public class ItemPayloadActivity extends BaseActivity<ActivitySimpleBinding> {

    private PayloadAdapter mAdapter;
    private int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        setTitle("item 局部刷新");

        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new PayloadAdapter(DataUtil.get(this, 6));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        SpacesItemDecoration itemDecoration = new SpacesItemDecoration(this, SpacesItemDecoration.VERTICAL, 1);
        itemDecoration.setDrawable(ContextCompat.getDrawable(binding.recyclerView.getContext(), R.drawable.shape_line));
        binding.recyclerView.addItemDecoration(itemDecoration);
        binding.recyclerView.setLoadingMoreView(new NeteaseLoadMoreView(this));
        binding.recyclerView.setRefreshHeaderView(new NeteaseRefreshHeaderView(this));
        binding.recyclerView.setAdapter(mAdapter);
        binding.recyclerView.setOnRefreshListener(new ByRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.recyclerView.setRefreshing(false);
            }
        }, 2000);
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
                        mAdapter.addData(DataUtil.getMore(ItemPayloadActivity.this, 20, page));
                        binding.recyclerView.loadMoreComplete();
                    }
                }, 100);
            }
        });
        binding.recyclerView.setOnItemChildClickListener(new ByRecyclerView.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_zan:
                        DataItemBean itemData = mAdapter.getItemData(position);
                        itemData.setIsZan(itemData.getIsZan() == 0 ? 1 : 0);
                        mAdapter.refreshNotifyItemChanged(position, PayloadAdapter.PAYLOAD_ZAN);
                        break;
                    case R.id.tv_collect:
                        DataItemBean itemData2 = mAdapter.getItemData(position);
                        itemData2.setIsCollect(itemData2.getIsCollect() == 0 ? 1 : 0);
                        mAdapter.refreshNotifyItemChanged(position, PayloadAdapter.PAYLOAD_COLLECT);
                        break;
                }
            }
        });
        binding.recyclerView.setOnItemClickListener(new ByRecyclerView.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                if (position == 0) {
                    List<DataItemBean> data = mAdapter.getData();
                    for (int i = 0; i < 4; i++) {
                        data.get(i).setIsCollect(1);
                    }
                    // 从position=0，往后itemCount个刷新收藏状态
                    mAdapter.refreshNotifyItemRangeChanged(0, 4, PayloadAdapter.PAYLOAD_COLLECT);
                }
            }
        });
    }

}
