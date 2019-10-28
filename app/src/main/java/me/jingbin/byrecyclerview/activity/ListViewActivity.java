package me.jingbin.byrecyclerview.activity;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.adapter.ListViewAdapter;
import me.jingbin.byrecyclerview.app.BaseActivity;
import me.jingbin.byrecyclerview.rebinding.ActivityListViewBinding;
import me.jingbin.byrecyclerview.rebinding.LayoutFooterViewBinding;
import me.jingbin.byrecyclerview.rebinding.LayoutHeaderViewBinding;
import me.jingbin.byrecyclerview.utils.DataUtil;

/**
 * @author jingbin
 */
public class ListViewActivity extends BaseActivity<ActivityListViewBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        setTitle("BaseListAdapter的使用");

        initAdapter();
    }

    private void initAdapter() {
        LayoutHeaderViewBinding headerBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.layout_header_view, binding.listView, false);
        LayoutFooterViewBinding footerBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.layout_footer_view, binding.listView, false);

        ListViewAdapter mAdapter = new ListViewAdapter(DataUtil.get(this, 20));
        binding.listView.setAdapter(mAdapter);
        binding.listView.addFooterView(footerBinding.getRoot());
        binding.listView.addHeaderView(headerBinding.getRoot());

        binding.listView.setHeaderDividersEnabled(false);
        binding.listView.setFooterDividersEnabled(false);
    }
}
