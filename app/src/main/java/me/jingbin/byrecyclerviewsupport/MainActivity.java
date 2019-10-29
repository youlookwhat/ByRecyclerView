package me.jingbin.byrecyclerviewsupport;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import me.jingbin.library.ByRecyclerView;

/**
 * @author jingbin
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DataAdapter dataAdapter = new DataAdapter(getData());
        final ByRecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(dataAdapter);

        recyclerView.setOnLoadMoreListener(new ByRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dataAdapter.addAll(getData());
                        recyclerView.loadMoreEnd();
                    }
                }, 1000);
            }
        });
        recyclerView.setOnItemClickListener(new ByRecyclerView.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                dataAdapter.setNewData(getData());
            }
        });

    }


    private ArrayList<DataBean> getData() {
        ArrayList<DataBean> dataBeans = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            DataBean dataBean = new DataBean("我是数据");
            dataBeans.add(dataBean);
        }
        return dataBeans;
    }
}
