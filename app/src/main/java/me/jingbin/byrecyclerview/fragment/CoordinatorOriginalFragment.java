package me.jingbin.byrecyclerview.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.adapter.DataAdapter;
import me.jingbin.byrecyclerview.adapter.FeedStaggerAdapter;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.databinding.FragmentRefreshBinding;
import me.jingbin.byrecyclerview.utils.DataUtil;
import me.jingbin.byrecyclerview.utils.DensityUtil;
import me.jingbin.byrecyclerview.utils.ToastUtil;
import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.decoration.GridSpaceItemDecoration;
import me.jingbin.library.decoration.SpacesItemDecoration;
import me.jingbin.library.view.OnItemFilterClickListener;

/**
 * @author jingbin
 */
public class CoordinatorOriginalFragment extends BaseFragment<FragmentRefreshBinding> {

    private static final String TYPE = "mType";
    private String mType = "Android";
    private boolean mIsPrepared;
    private boolean mIsFirst = true;
    private FeedStaggerAdapter feedStaggerAdapter;
    private int page = 0;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public static CoordinatorOriginalFragment newInstance(String type) {
        CoordinatorOriginalFragment fragment = new CoordinatorOriginalFragment();
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
        return R.layout.fragment_refresh;
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
        binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
        binding.recyclerView.addItemDecoration(new GridSpaceItemDecoration(DensityUtil.dip2px(activity, 8f)));
        feedStaggerAdapter = new FeedStaggerAdapter((DataUtil.getStickyData()));
        binding.recyclerView.setAdapter(feedStaggerAdapter);
        binding.recyclerView.setOnItemClickListener(new OnItemFilterClickListener() {
            @Override
            protected void onSingleClick(View v, int position) {
                if ("点我回到顶部".equals(feedStaggerAdapter.getItemData(position).getTitle())) {
                    binding.recyclerView.scrollToPosition(0);
                }
            }
        });
        // 设置自动刷新
        binding.recyclerView.setOnLoadMoreListener(true, 1, new ByRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (page == 2) {
                    binding.recyclerView.loadMoreEnd();
                    return;
                }
                page = 2;
                feedStaggerAdapter.addData(DataUtil.getStickyData());
                binding.recyclerView.loadMoreComplete();
            }
        }, 1000);

        mIsFirst = false;
    }
}
