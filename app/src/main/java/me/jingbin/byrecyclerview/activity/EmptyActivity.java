package me.jingbin.byrecyclerview.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.adapter.DataAdapter;
import me.jingbin.byrecyclerview.app.BaseActivity;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.databinding.ActivitySimpleBinding;
import me.jingbin.byrecyclerview.databinding.LayoutEmptyBinding;
import me.jingbin.byrecyclerview.databinding.LayoutFooterViewBinding;
import me.jingbin.byrecyclerview.databinding.LayoutHeaderViewBinding;
import me.jingbin.byrecyclerview.utils.DataUtil;
import me.jingbin.byrecyclerview.utils.ToastUtil;
import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.config.ByDividerItemDecoration;

/**
 * @author jingbin
 */
public class EmptyActivity extends BaseActivity<ActivitySimpleBinding> {

    private int page = 1;
    private DataAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        setTitle("设置EmptyView");

        initAdapter();
    }

    private void initAdapter() {
        final LayoutHeaderViewBinding headerBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.layout_header_view, (ViewGroup) binding.recyclerView.getParent(), false);
        LayoutFooterViewBinding footerBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.layout_footer_view, (ViewGroup) binding.recyclerView.getParent(), false);
        final LayoutEmptyBinding emptyBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.layout_empty, (ViewGroup) binding.recyclerView.getParent(), false);


        mAdapter = new DataAdapter(binding.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        ByDividerItemDecoration itemDecoration = new ByDividerItemDecoration(binding.recyclerView.getContext(), DividerItemDecoration.VERTICAL, false);
        itemDecoration.setDrawable(ContextCompat.getDrawable(binding.recyclerView.getContext(), R.drawable.shape_line));
        binding.recyclerView.addItemDecoration(itemDecoration);
        binding.recyclerView.setAdapter(mAdapter);
        binding.recyclerView.setScrollBottomLoad(true);
        binding.recyclerView.setOnLoadMoreListener(new ByRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                binding.recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (page == 2) {
                            binding.recyclerView.loadMoreEnd();
                            return;
                        }
                        page++;
                        mAdapter.addData(DataUtil.getMore(EmptyActivity.this, 6, page));
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

        binding.recyclerView.addFooterView(footerBinding.getRoot());
        binding.recyclerView.addHeaderView(headerBinding.getRoot());
        binding.recyclerView.setEmptyView(emptyBinding.getRoot());
        binding.recyclerView.setLoadMoreEnabled(false);

        emptyBinding.tvText.setText("空布局\n(点我展示列表数据)");
        emptyBinding.tvText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.setNewData(DataUtil.get(EmptyActivity.this, 6));
                binding.recyclerView.setLoadMoreEnabled(true);
                binding.recyclerView.setEmptyViewEnabled(false);
            }
        });

    }

}
