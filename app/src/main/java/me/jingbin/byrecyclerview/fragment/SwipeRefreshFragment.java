package me.jingbin.byrecyclerview.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.adapter.DataAdapter;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.rebinding.FragmentSwipeRefreshBinding;
import me.jingbin.byrecyclerview.utils.DataUtil;
import me.jingbin.byrecyclerview.utils.ToastUtil;
import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.config.ByDividerItemDecoration;

/**
 * @author jingbin
 */
public class SwipeRefreshFragment extends BaseFragment<FragmentSwipeRefreshBinding> {

    private static final String TYPE = "mType";
    private String mType = "";
    private boolean mIsPrepared;
    private boolean mIsFirst = true;
    private DataAdapter mAdapter;
    private ByRecyclerView recyclerView;
    private int page;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public static SwipeRefreshFragment newInstance(String type) {
        SwipeRefreshFragment fragment = new SwipeRefreshFragment();
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
        return R.layout.fragment_swipe_refresh;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 准备就绪
        mIsPrepared = true;
    }

    @Override
    protected void loadData() {
        if (!mIsPrepared || !mIsVisible || !mIsFirst) {
            return;
        }
        initAdapter();
    }

    private void initAdapter() {
        recyclerView = getView(R.id.recyclerView);
        mAdapter = new DataAdapter(DataUtil.get(activity, 10));
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        ByDividerItemDecoration itemDecoration = new ByDividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL, false);
        itemDecoration.setDrawable(ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.shape_line));
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setOnLoadMoreListener(new ByRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (page == 3) {
                            recyclerView.loadMoreEnd();
                            return;
                        }
                        page++;
                        mAdapter.addData(DataUtil.getMore(activity, 10, page));
                        recyclerView.loadMoreComplete();
                    }
                }, 500);
            }
        });
        recyclerView.setOnItemClickListener(new ByRecyclerView.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                DataItemBean itemData = mAdapter.getItemData(position);
                ToastUtil.showToast(itemData.getTitle());
            }
        });
        bindingView.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bindingView.swipeRefreshLayout.setRefreshing(false);
                        page = 1;
                        mAdapter.setNewData(DataUtil.getMore(activity, 10, page));
                    }
                }, 500);
            }
        });
        mIsFirst = false;
    }
}
