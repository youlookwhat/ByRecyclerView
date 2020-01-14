package me.jingbin.byrecyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import me.jingbin.byrecyclerview.adapter.MainAdapter;
import me.jingbin.byrecyclerview.bean.MainItemBean;
import me.jingbin.byrecyclerview.databinding.ActivityMainBinding;
import me.jingbin.byrecyclerview.utils.DataUtil;
import me.jingbin.byrecyclerview.utils.WebUtil;
import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.decoration.SpacesItemDecoration;

/**
 * @author jingbin
 * link to https://github.com/youlookwhat/ByRecyclerView
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.icon_action_info));
        setSupportActionBar(binding.toolbar);

        final MainAdapter homeAdapter = new MainAdapter(DataUtil.getMainActivityList(this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(homeAdapter);
        binding.recyclerView.addItemDecoration(new SpacesItemDecoration(this, SpacesItemDecoration.VERTICAL));
        binding.recyclerView.setOnItemClickListener(new ByRecyclerView.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                MainItemBean itemData = homeAdapter.getItemData(position);
                if (itemData.getCls() != null) {
                    startActivity(new Intent(MainActivity.this, itemData.getCls()));
                }
            }
        });
        binding.recyclerView.setLoadMoreEnabled(true);
        binding.recyclerView.loadMoreEnd();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.actionbar_update);
        item.setTitle("当前版本:" + BuildConfig.VERSION_NAME);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionbar_info:
                WebUtil.openLink(MainActivity.this, "https://github.com/youlookwhat/ByRecyclerView");
                break;
            case R.id.actionbar_update:
                WebUtil.openLink(MainActivity.this, "https://fir.im/byrecyclerview");
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.recyclerView.destroy();
    }
}
