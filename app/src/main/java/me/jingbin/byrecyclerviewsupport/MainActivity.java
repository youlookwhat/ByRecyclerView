package me.jingbin.byrecyclerviewsupport;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.jingbin.byrecyclerviewsupport.databinding.ActivityMainBinding;
import me.jingbin.byrecyclerviewsupport.databinding.HeaderViewBinding;
import me.jingbin.library.ByRecyclerView;

/**
 * @author jingbin
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        HeaderViewBinding bindingHeader = DataBindingUtil.inflate(getLayoutInflater(), R.layout.header_view, (ViewGroup) binding.recyclerView.getParent(), false);

        final DataAdapter dataAdapter = new DataAdapter(getData());
        final ByRecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addHeaderView(bindingHeader.getRoot());
        recyclerView.setAdapter(dataAdapter);

        recyclerView.setOnLoadMoreListener(new ByRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                dataAdapter.addData(getData());
                recyclerView.loadMoreComplete();
            }
        }, 1000);

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataAdapter.setNewData(getData());
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    private ArrayList<DataBean> getData() {
        ArrayList<DataBean> dataBeans = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            DataBean dataBean = new DataBean("我是数据");
            dataBeans.add(dataBean);
        }
        return dataBeans;
    }
}
