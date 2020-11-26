package me.jingbin.byrecyclerview.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.adapter.MultiAdapter;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.databinding.FragmentRefreshBinding;
import me.jingbin.byrecyclerview.databinding.LayoutHeaderViewBinding;
import me.jingbin.byrecyclerview.utils.DataUtil;
import me.jingbin.byrecyclerview.utils.LogHelper;
import me.jingbin.byrecyclerview.utils.ToastUtil;
import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.decoration.SpacesItemDecoration;

/**
 * @author jingbin
 */
public class MultiLinearFragment extends BaseFragment<FragmentRefreshBinding> {

    private static final String TYPE = "mType";
    private String mType = "Android";
    private boolean mIsFirst = true;
    private MultiAdapter mAdapter;
    private int page = 1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public static MultiLinearFragment newInstance(String type) {
        MultiLinearFragment fragment = new MultiLinearFragment();
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

        initAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIsFirst) {
            mAdapter.setNewData(DataUtil.getMultiData(getActivity(), 50));
            mIsFirst = false;
        }
    }

    @Override
    protected void loadData() {
    }

    /**
     * 有一个问题：
     * 在有headerView 且 第一个headerView没有高度时(如包裹了一个view，但是设置了这个view为gone)，会出现滚动条到不了顶的情况。
     * 解决：一直让第一个headerView有高度，如可以让其无论如何有一个1px的高度
     */
    private void initAdapter() {
        mAdapter = new MultiAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);

        SpacesItemDecoration itemDecoration = new SpacesItemDecoration(getActivity(), SpacesItemDecoration.VERTICAL, 1);
        itemDecoration.setDrawable(R.drawable.shape_line);
        binding.recyclerView.addItemDecoration(itemDecoration);
        binding.recyclerView.setAdapter(mAdapter);
        binding.recyclerView.addHeaderView(R.layout.layout_header_view);
        binding.recyclerView.setOnRefreshListener(new ByRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                mAdapter.setNewData(DataUtil.getMultiData(getActivity(), 20));
            }
        });
        binding.recyclerView.setOnLoadMoreListener(new ByRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (page == 3) {
                    binding.recyclerView.loadMoreEnd();
                    return;
                }
                page++;
                mAdapter.addData(DataUtil.getMultiData(getActivity(), 50));
                binding.recyclerView.loadMoreComplete();
            }
        }, 500);
        binding.recyclerView.setOnItemClickListener(new ByRecyclerView.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                DataItemBean itemData = mAdapter.getItemData(position);
                ToastUtil.showToast(itemData.getDes() + "-" + position);
            }
        });
    }
}
