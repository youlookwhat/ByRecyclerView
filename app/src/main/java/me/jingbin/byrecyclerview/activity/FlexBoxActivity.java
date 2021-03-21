package me.jingbin.byrecyclerview.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.app.BaseActivity;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.binding.BaseBindingAdapter;
import me.jingbin.byrecyclerview.binding.BaseBindingHolder;
import me.jingbin.byrecyclerview.databinding.ActivitySimpleBinding;
import me.jingbin.byrecyclerview.databinding.ItemTagBinding;
import me.jingbin.byrecyclerview.databinding.LayoutEmptyBinding;
import me.jingbin.byrecyclerview.databinding.LayoutFooterViewBinding;
import me.jingbin.byrecyclerview.databinding.LayoutHeaderViewBinding;
import me.jingbin.byrecyclerview.databinding.LayoutLoadingBinding;
import me.jingbin.byrecyclerview.utils.DataUtil;
import me.jingbin.byrecyclerview.utils.ToastUtil;
import me.jingbin.byrecyclerview.view.ByFlexboxLayoutManager;
import me.jingbin.library.ByRecyclerView;

/**
 * @author jingbin
 * FlexboxLayoutManager处理
 * 1.使用自定义的 ByFlexboxLayoutManager
 * 2.不能设置加载更多
 * 3.显示EmptyView后不能再加载数据
 */
public class FlexBoxActivity extends BaseActivity<ActivitySimpleBinding> {

    private int statue = 1;
    private LayoutEmptyBinding emptyBinding;
    private LayoutLoadingBinding loadingBinding;

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
        loadingBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.layout_loading, (ViewGroup) binding.recyclerView.getParent(), false);

        // 注意使用 ByFlexboxLayoutManager
        ByFlexboxLayoutManager layoutManager = new ByFlexboxLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
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
        binding.recyclerView.setStateView(loadingBinding.getRoot());

        binding.recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (statue == 1) {
                    // 1秒后显示 数据
                    binding.recyclerView.setStateViewEnabled(false);
                    mAdapter.setNewData(DataUtil.getFlexData());

                    headerBinding.tvText.setText("点我显示空布局");
                    headerBinding.tvText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            binding.recyclerView.setStateView(emptyBinding.getRoot());
                            mAdapter.setNewData(null);
                            headerBinding.tvText.setText("头部布局");
                        }
                    });
                    statue = 2;
                }
            }
        }, 1000);

    }

    private final BaseBindingAdapter<DataItemBean, ItemTagBinding> mAdapter = new BaseBindingAdapter<DataItemBean, ItemTagBinding>(R.layout.item_tag) {
        @Override
        protected void bindView(@NonNull BaseBindingHolder holder, @NonNull DataItemBean bean, @NonNull ItemTagBinding binding, int position) {
            binding.tvText.setText(bean.getTitle());
        }
    };

}
