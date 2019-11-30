package me.jingbin.byrecyclerview.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.adapter.DataAdapter;
import me.jingbin.byrecyclerview.app.BaseActivity;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.databinding.ActivitySimpleBinding;
import me.jingbin.byrecyclerview.databinding.LayoutEmptyBinding;
import me.jingbin.byrecyclerview.databinding.LayoutErrorBinding;
import me.jingbin.byrecyclerview.databinding.LayoutFooterViewBinding;
import me.jingbin.byrecyclerview.databinding.LayoutHeaderViewBinding;
import me.jingbin.byrecyclerview.databinding.LayoutLoadingBinding;
import me.jingbin.byrecyclerview.utils.DataUtil;
import me.jingbin.byrecyclerview.utils.ToastUtil;
import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.decoration.SpacesItemDecoration;

/**
 * @author jingbin
 */
public class StateViewActivity extends BaseActivity<ActivitySimpleBinding> {

    private int statue = 1;
    private int page = 1;
    private DataAdapter mAdapter;
    private LayoutLoadingBinding loadingBinding;
    private LayoutEmptyBinding emptyBinding;
    private LayoutErrorBinding errorBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        setTitle("设置StateView");

        initAdapter();
    }

    private void initAdapter() {
        final LayoutHeaderViewBinding headerBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.layout_header_view, (ViewGroup) binding.recyclerView.getParent(), false);
        LayoutFooterViewBinding footerBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.layout_footer_view, (ViewGroup) binding.recyclerView.getParent(), false);
        loadingBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.layout_loading, (ViewGroup) binding.recyclerView.getParent(), false);
        emptyBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.layout_empty, (ViewGroup) binding.recyclerView.getParent(), false);
        errorBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.layout_error, (ViewGroup) binding.recyclerView.getParent(), false);

        mAdapter = new DataAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        SpacesItemDecoration itemDecoration = new SpacesItemDecoration(this, SpacesItemDecoration.VERTICAL, 1);
        itemDecoration.setDrawable(R.drawable.shape_line);
        binding.recyclerView.addItemDecoration(itemDecoration);
        binding.recyclerView.setAdapter(mAdapter);
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
                        mAdapter.addData(DataUtil.getMore(StateViewActivity.this, 6, page));
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
        binding.recyclerView.setStateView(loadingBinding.getRoot());
        binding.recyclerView.setLoadMoreEnabled(false);

        emptyBinding.getRoot().setOnClickListener(listener);
        errorBinding.getRoot().setOnClickListener(listener);
        loadingBinding.tvText.setOnClickListener(listener);

        loadingBinding.tvText.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (statue == 1) {
                    binding.recyclerView.setStateView(errorBinding.getRoot());
                    statue = 2;
                }
            }
        }, 1000);

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            binding.recyclerView.setStateView(loadingBinding.getRoot());
            binding.recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (statue == 2) {
                        binding.recyclerView.setStateView(emptyBinding.getRoot());
                        statue = 3;
                        return;
                    }
                    if (statue == 3) {
                        binding.recyclerView.setStateViewEnabled(false);
                        binding.recyclerView.setLoadMoreEnabled(true);
                        mAdapter.setNewData(DataUtil.get(StateViewActivity.this, 6));
                    }
                }
            }, 1000);
        }
    };

}
