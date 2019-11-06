package me.jingbin.byrecyclerview.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.adapter.DataAdapter;
import me.jingbin.byrecyclerview.adapter.GridAdapter;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.databinding.FragmentRefreshBinding;
import me.jingbin.byrecyclerview.utils.DataUtil;
import me.jingbin.byrecyclerview.utils.DensityUtil;
import me.jingbin.byrecyclerview.utils.ToastUtil;
import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.decoration.GridSpaceItemDecoration;
import me.jingbin.library.decoration.SpacesItemDecoration;

/**
 * @author jingbin
 */
public class GridFragment extends BaseFragment<FragmentRefreshBinding> {

    private static final String TYPE = "mType";
    private String mType = "Android";
    private boolean mIsPrepared;
    private boolean mIsFirst = true;
    private GridAdapter mAdapter;
    private ByRecyclerView recyclerView;
    private int page = 1;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public static GridFragment newInstance(String type) {
        GridFragment fragment = new GridFragment();
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
        recyclerView = getView(R.id.recyclerView);
        mAdapter = new GridAdapter(DataUtil.get(activity, 6));
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 3);
//        recyclerView.setLayoutManager(gridLayoutManager);
        final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        GridSpaceItemDecoration itemDecoration = new GridSpaceItemDecoration(3, DensityUtil.dip2px(activity, 10));
        itemDecoration.setNoShowSpace(1, 1);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setOnLoadMoreListener(new ByRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (page == 4) {
                            recyclerView.loadMoreEnd();
                            return;
                        }
                        if (page == 2) {
                            page++;
                            recyclerView.loadMoreFail();
                            return;
                        }
                        page++;
                        mAdapter.addData(DataUtil.getMore(activity, 6, page));
                        recyclerView.loadMoreComplete();
                    }
                }, 1000);
            }
        });
        recyclerView.setOnItemClickListener(new ByRecyclerView.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                if (position == 0) {
                    recyclerView.removeItemDecorationAt(0);
                    GridSpaceItemDecoration itemDecoration = new GridSpaceItemDecoration(5, DensityUtil.dip2px(activity, 10));
                    itemDecoration.setNoShowSpace(1, 1);
                    recyclerView.addItemDecoration(itemDecoration);
                    staggeredGridLayoutManager.setSpanCount(5);
                } else {
                    DataItemBean itemData = mAdapter.getItemData(position);
                    ToastUtil.showToast(itemData.getTitle());
                    recyclerView.setRefreshing(true);
                }
            }
        });
        recyclerView.setOnRefreshListener(new ByRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        mAdapter.setNewData(DataUtil.getMore(activity, 9, page));
                    }
                }, 1000);
            }
        });
        mIsFirst = false;
    }
}
