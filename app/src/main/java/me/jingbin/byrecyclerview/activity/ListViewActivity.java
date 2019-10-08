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
import me.jingbin.byrecyclerview.adapter.ListViewAdapter;
import me.jingbin.byrecyclerview.app.BaseActivity;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.databinding.ActivityListViewBinding;
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
public class ListViewActivity extends BaseActivity<ActivityListViewBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        setTitle("BaseByListViewAdapter的使用");

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
