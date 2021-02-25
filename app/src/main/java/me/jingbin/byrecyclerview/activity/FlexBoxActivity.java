package me.jingbin.byrecyclerview.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

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
import me.jingbin.byrecyclerview.view.ByFlexboxLayoutManager;
import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.decoration.SpacesItemDecoration;

/**
 * @author jingbin
 * FlexboxLayoutManager处理
 * 1.使用自定义的 ByFlexboxLayoutManager
 * 2.不能设置加载更多
 * 3.显示EmptyView后不能再加载数据
 */
public class FlexBoxActivity extends BaseActivity<ActivitySimpleBinding> {

    private int statue = 1;
    private DataAdapter mAdapter;
    private LayoutEmptyBinding emptyBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        setTitle("FlexboxLayoutManager处理");
        initAdapter();
    }

    private void initAdapter() {
        final LayoutHeaderViewBinding headerBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.layout_header_view, (ViewGroup) binding.recyclerView.getParent(), false);
        LayoutFooterViewBinding footerBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.layout_footer_view, (ViewGroup) binding.recyclerView.getParent(), false);
        emptyBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.layout_empty, (ViewGroup) binding.recyclerView.getParent(), false);

        mAdapter = new DataAdapter();
        ByFlexboxLayoutManager layoutManager = new ByFlexboxLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
        SpacesItemDecoration itemDecoration = new SpacesItemDecoration(this, SpacesItemDecoration.VERTICAL, 1);
        itemDecoration.setDrawable(R.drawable.shape_line);
        binding.recyclerView.addItemDecoration(itemDecoration);
        binding.recyclerView.setAdapter(mAdapter);
        binding.recyclerView.setOnItemClickListener(new ByRecyclerView.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                DataItemBean itemData = mAdapter.getItemData(position);
                ToastUtil.showToast(itemData.getTitle());
            }
        });

        binding.recyclerView.addFooterView(footerBinding.getRoot());
        binding.recyclerView.addHeaderView(headerBinding.getRoot());
        // 显示内容
        mAdapter.setNewData(DataUtil.get(FlexBoxActivity.this, 6));

        binding.recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (statue == 1) {
                    // 1秒后显示 EmptyView
                    binding.recyclerView.setStateView(emptyBinding.getRoot());
                    mAdapter.setNewData(null);
                    statue = 2;
                }
            }
        }, 1000);

    }

}
