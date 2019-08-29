package me.jingbin.jrecyclerview.activity;

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

import me.jingbin.jrecyclerview.R;
import me.jingbin.jrecyclerview.adapter.HomeAdapter;
import me.jingbin.jrecyclerview.bean.HomeItemBean;
import me.jingbin.jrecyclerview.databinding.ItemHomeBinding;
import me.jingbin.jrecyclerview.utils.ToastUtil;
import me.jingbin.jrecyclerview.view.MyDividerItemDecoration;
import me.jingbin.library.JRecyclerView;

public class SwipeRefreshActivity extends AppCompatActivity {

    private JRecyclerView recyclerView;
    private HomeAdapter homeAdapter;
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
        headerBinding2 = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.item_home, (ViewGroup) recyclerView.getParent(), false);

        headerBinding.tvText.setText("header_1");
        headerBinding2.tvText.setText("header_2");
        homeAdapter = new HomeAdapter();
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(null);
        recyclerView.addHeaderView(headerBinding.getRoot());
        recyclerView.addHeaderView(headerBinding2.getRoot());
        MyDividerItemDecoration itemDecoration = new MyDividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL, false, true, true);
        itemDecoration.setDrawable(ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.shape_line));
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(homeAdapter);

        homeAdapter.addAll(get());
        homeAdapter.notifyDataSetChanged();
        recyclerView.loadMoreComplete();
        recyclerView.setLoadingListener(new JRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                recyclerView.reset();
            }

            @Override
            public void onLoadMore() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (page == 2) {
                            recyclerView.noMoreLoading();
                        } else {
                            page++;
                            homeAdapter.addAll(get());
                            homeAdapter.notifyDataSetChanged();
                            recyclerView.loadMoreComplete();
                        }
                    }
                }, 2000);
            }
        });
        recyclerView.setOnItemClickListener(new JRecyclerView.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                homeAdapter.getItemData(position);
                ToastUtil.showToast(position + "-----" + homeAdapter.getItemData(position).getTitle());
            }
        });
        recyclerView.setOnItemLongClickListener(new JRecyclerView.OnItemLongClickListener() {
            @Override
            public boolean onLongClick(View v, int position) {
                ToastUtil.showToast(position + "---长按--" + homeAdapter.getItemData(position).getTitle());
                return true;
            }
        });
    }

    private ArrayList<HomeItemBean> get() {
        ArrayList<HomeItemBean> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            HomeItemBean bean = new HomeItemBean();
            bean.setTitle("jingbin:" + i);
            list.add(bean);
        }
        return list;
    }
}
