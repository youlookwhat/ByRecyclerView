package me.jingbin.byrecyclerview.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.adapter.GridAdapter;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.databinding.FragmentRefreshBinding;
import me.jingbin.byrecyclerview.utils.DataUtil;
import me.jingbin.byrecyclerview.utils.DensityUtil;
import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.decoration.GridSpaceItemDecoration;

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
        mAdapter = new GridAdapter(DataUtil.get(activity, 20));

        if ("grid".equals(mType)) {
            // 宫格
            GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 6);
            recyclerView.setLayoutManager(gridLayoutManager);
            // 四周也有间距
            GridSpaceItemDecoration itemDecoration = new GridSpaceItemDecoration(
                    DensityUtil.dip2px(activity, 10), true);
            // 去掉首尾的分割线 (刷新头部和加载更多尾部)
            recyclerView.addItemDecoration(itemDecoration.setNoShowSpace(2, 1));

        } else {
            // 瀑布流
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
            // 四周没有间距
            GridSpaceItemDecoration itemDecoration = new GridSpaceItemDecoration(
                    DensityUtil.dip2px(activity, 10), true);
            recyclerView.addItemDecoration(itemDecoration.setNoShowSpace(2, 1));
            mAdapter.setStaggered(true);
        }
        recyclerView.addHeaderView(R.layout.layout_header_view);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setOnLoadMoreListener(new ByRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (page == 4) {
                    recyclerView.loadMoreEnd();
                    return;
                }
                page++;
                mAdapter.addData(DataUtil.getMore(activity, 20, page));
                recyclerView.loadMoreComplete();
            }
        }, 1000);
        recyclerView.setOnItemClickListener(new ByRecyclerView.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                DataItemBean itemData = mAdapter.getItemData(position);
//                ToastUtil.showToast(itemData.getTitle());
//                recyclerView.setRefreshing(true);
            }
        });
        recyclerView.setOnRefreshListener(new ByRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        mAdapter.setNewData(DataUtil.getMore(activity, 20, page));
                    }
                }, 1000);
            }
        });
        mIsFirst = false;
    }
}
