package me.jingbin.jrecyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import me.jingbin.jrecyclerview.adapter.HomeAdapter;
import me.jingbin.jrecyclerview.bean.HomeItemBean;
import me.jingbin.library.JRecyclerView;

public class MainActivity extends AppCompatActivity {

    private int page = 1;
    private HomeAdapter homeAdapter;
    private JRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);

        homeAdapter = new HomeAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(homeAdapter);
        homeAdapter.addAll(get());
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
                            recyclerView.loadMoreComplete();
                        }
                    }
                }, 2000);
            }
        });
    }

    private ArrayList<HomeItemBean> get() {
        ArrayList<HomeItemBean> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            HomeItemBean bean = new HomeItemBean();
            bean.setTitle("jingbin:" + i);
            list.add(bean);
        }
        return list;
    }
}
