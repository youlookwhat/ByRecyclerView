package me.jingbin.byrecyclerview.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.adapter.DataAdapter;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.rebinding.ItemHomeBinding;
import me.jingbin.byrecyclerview.rebinding.LayoutFooterViewBinding;
import me.jingbin.byrecyclerview.rebinding.LayoutHeaderViewBinding;
import me.jingbin.byrecyclerview.utils.ToastUtil;
import me.jingbin.byrecyclerview.view.MyDividerItemDecoration;
import me.jingbin.library.ByRecyclerView;

public class SwipeRefreshActivity extends AppCompatActivity {

    private ByRecyclerView recyclerView;
    private DataAdapter homeAdapter;
    private int page = 1;
    private TextView tvPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_refresh);
        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        tvPosition = findViewById(R.id.tv_position);
        initAdapter();
        initScroll();
    }

    private void initScroll() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (layoutManager != null) {
                    //得到当前界面，第一个子视图的position
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                    tvPosition.setText(String.valueOf(firstVisibleItemPosition));
                }
            }
        });
    }

    LinearLayoutManager layoutManager;
    ItemHomeBinding headerBinding2;

    private void initAdapter() {
        // (ViewGroup) recyclerView.getParent()
        ItemHomeBinding headerBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.item_home, (ViewGroup) recyclerView.getParent(), false);
        final ItemHomeBinding headerBinding7 = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.item_home, (ViewGroup) recyclerView.getParent(), false);
        final LayoutHeaderViewBinding headerViewBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.layout_header_view, (ViewGroup) recyclerView.getParent(), false);
        final LayoutFooterViewBinding footerViewBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.layout_footer_view, (ViewGroup) recyclerView.getParent(), false);
        headerBinding2 = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.item_home, (ViewGroup) recyclerView.getParent(), false);

        headerBinding.tvText.setText("header_1");
        headerBinding2.tvText.setText("header_2");
        headerBinding.tvText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.addHeaderView(headerViewBinding.getRoot());
            }
        });
        headerBinding2.tvText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 1;
                recyclerView.reset();
                recyclerView.setEmptyViewEnabled(false);
                homeAdapter.addAll(get());
                homeAdapter.notifyDataSetChanged();
                recyclerView.loadMoreComplete();
            }
        });
        homeAdapter = new DataAdapter();
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addHeaderView(headerBinding.getRoot());
        recyclerView.addHeaderView(headerBinding2.getRoot());
        recyclerView.addFooterView(footerViewBinding.getRoot());
        MyDividerItemDecoration itemDecoration = new MyDividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL, false, false, false);
        itemDecoration.setDrawable(ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.shape_line));
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setEmptyView(R.layout.layout_empty, (ViewGroup) recyclerView.getParent());
        recyclerView.setAdapter(homeAdapter);

        recyclerView.setEmptyView(R.layout.layout_empty, (ViewGroup) recyclerView.getParent());

        recyclerView.setOnLoadMoreListener(new ByRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page++;
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        homeAdapter.clear();
                        homeAdapter.addAll(get());
                        homeAdapter.notifyDataSetChanged();
                        recyclerView.setEmptyViewEnabled(false);
                        recyclerView.loadMoreEnd();

//                        if (page == 2) {
//                            recyclerView.setEmptyView(R.layout.layout_empty, (ViewGroup) recyclerView.getParent());
//                            recyclerView.noMoreLoading();
//                        } else {
//                            recyclerView.setIsUseEmpty(false);
//                            page++;
//                            homeAdapter.addAll(get());
//                            homeAdapter.notifyDataSetChanged();
//                            recyclerView.loadMoreComplete();
//                        }
                    }
                }, 2000);
            }
        });
        recyclerView.setOnItemClickListener(new ByRecyclerView.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                homeAdapter.getItemData(position);
                ToastUtil.showToast(position + "-----" + homeAdapter.getItemData(position).getTitle());
                ItemHomeBinding headerBinding3 = DataBindingUtil.inflate(LayoutInflater.from(SwipeRefreshActivity.this), R.layout.item_home, (ViewGroup) recyclerView.getParent(), false);
                headerBinding3.tvText.setText(homeAdapter.getItemData(position).getTitle());
                recyclerView.addHeaderView(headerBinding3.getRoot());
                homeAdapter.notifyDataSetChanged();
            }
        });
        recyclerView.setOnItemLongClickListener(new ByRecyclerView.OnItemLongClickListener() {
            @Override
            public boolean onLongClick(View v, int position) {
                ToastUtil.showToast(position + "---长按--" + homeAdapter.getItemData(position).getTitle());
                return true;
            }
        });
    }

    private ArrayList<DataItemBean> get() {
        ArrayList<DataItemBean> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            DataItemBean bean = new DataItemBean();
            bean.setTitle("jingbin:" + i);
            list.add(bean);
        }
        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerView.destroy();
    }
}
