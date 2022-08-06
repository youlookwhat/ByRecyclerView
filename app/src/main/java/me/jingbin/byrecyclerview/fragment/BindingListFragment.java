package me.jingbin.byrecyclerview.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.adapter.ListViewBindingAdapter;
import me.jingbin.byrecyclerview.databinding.FragmentListviewBinding;
import me.jingbin.byrecyclerview.databinding.LayoutFooterViewBinding;
import me.jingbin.byrecyclerview.databinding.LayoutHeaderViewBinding;
import me.jingbin.byrecyclerview.utils.DataUtil;

/**
 * @author jingbin
 */
public class BindingListFragment extends BaseFragment<FragmentListviewBinding> {

    private static final String TYPE = "mType";
    private String mType = "Android";
    private boolean mIsPrepared;
    private boolean mIsFirst = true;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public static BindingListFragment newInstance(String type) {
        BindingListFragment fragment = new BindingListFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(TYPE);
        }
    }

    @Override
    public int setContent() {
        return R.layout.fragment_listview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 准备就绪
        mIsPrepared = true;
        initAdapter();
    }

    @Override
    protected void loadData() {
        if (!mIsPrepared || !mIsVisible || !mIsFirst) {
            return;
        }
        initAdapter();
    }

    private void initAdapter() {
        LayoutHeaderViewBinding headerBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.layout_header_view, binding.listView, false);
        LayoutFooterViewBinding footerBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.layout_footer_view, binding.listView, false);

        ListViewBindingAdapter mAdapter = new ListViewBindingAdapter(DataUtil.get(activity, 30));
        binding.listView.setAdapter(mAdapter);
        binding.listView.addFooterView(footerBinding.getRoot());
        binding.listView.addHeaderView(headerBinding.getRoot());

        binding.listView.setHeaderDividersEnabled(false);
        binding.listView.setFooterDividersEnabled(false);
        mIsFirst = false;
    }
}
