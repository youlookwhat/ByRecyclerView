package me.jingbin.byrecyclerviewsupport;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

        ArrayList<DataBean> dataBeans = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            DataBean dataBean = new DataBean("我是数据");
            dataBeans.add(dataBean);
        }
        DataAdapter dataAdapter = new DataAdapter(dataBeans);
        ByRecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(dataAdapter);
    }
}
