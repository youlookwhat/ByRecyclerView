package me.jingbin.jrecyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import me.jingbin.jrecyclerview.activity.SimpleActivity;
import me.jingbin.jrecyclerview.activity.SwipeRefreshActivity;
import me.jingbin.jrecyclerview.adapter.HomeAdapter;
import me.jingbin.jrecyclerview.bean.HomeItemBean;
import me.jingbin.library.JRecyclerView;

/**
 * @author jingbin
 * link to https://github.com/youlookwhat/JRecyclerView
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void goSimple(View view) {
        startActivity(new Intent(this, SimpleActivity.class));
    }

    public void goSwipeRefresh(View view) {
        startActivity(new Intent(this, SwipeRefreshActivity.class));
    }
}
