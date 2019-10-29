package me.jingbin.byrecyclerview.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.adapter.DataAdapter;
import me.jingbin.byrecyclerview.adapter.ListView2Adapter;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.databinding.FragmentListviewBinding;
import me.jingbin.byrecyclerview.databinding.FragmentRefreshBinding;
import me.jingbin.byrecyclerview.databinding.LayoutFooterViewBinding;
import me.jingbin.byrecyclerview.databinding.LayoutHeaderViewBinding;
import me.jingbin.byrecyclerview.utils.DataUtil;
import me.jingbin.byrecyclerview.utils.ToastUtil;
import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.config.ByDividerItemDecoration;

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
        LayoutHeaderViewBinding headerBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.layout_header_view, bindingView.listView, false);
        LayoutFooterViewBinding footerBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.layout_footer_view, bindingView.listView, false);

        ListView2Adapter mAdapter = new ListView2Adapter(DataUtil.get(activity, 30));
        bindingView.listView.setAdapter(mAdapter);
        bindingView.listView.addFooterView(footerBinding.getRoot());
        bindingView.listView.addHeaderView(headerBinding.getRoot());

        bindingView.listView.setHeaderDividersEnabled(false);
        bindingView.listView.setFooterDividersEnabled(false);
        mIsFirst = false;
    }
}
