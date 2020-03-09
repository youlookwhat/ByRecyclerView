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

import java.util.Arrays;
import java.util.List;

import me.jingbin.byrecyclerview.activity.AppBarLayoutActivity;
import me.jingbin.byrecyclerview.activity.HeaderFooterActivity;
import me.jingbin.byrecyclerview.activity.ItemClickActivity;
import me.jingbin.byrecyclerview.activity.SecondTypeActivity;
import me.jingbin.byrecyclerview.activity.StateViewActivity;
import me.jingbin.byrecyclerview.activity.StickyItemActivity;
import me.jingbin.byrecyclerview.databinding.ActivityMainBinding;
import me.jingbin.byrecyclerview.utils.WebUtil;
import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;
import me.jingbin.library.decoration.SpacesItemDecoration;

/**
 * @author jingbin
 * link to https://github.com/youlookwhat/ByRecyclerView
 */
public class MainActivity extends AppCompatActivity {

    private List<String> list = Arrays.asList(
            "下拉刷新、加载更多",
            "Add HeaderView/FooterView",
            "设置StateView",
            "item 点击/长按",
            "adapter (多类型、databinding、ListView)",
            "万能分割线",
            "item 悬浮置顶",
            "Skeleton 骨架图",
            "CoordinatorLayout + RecyclerView 使用示例"
    );
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.icon_action_info));
        setSupportActionBar(binding.toolbar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(new BaseRecyclerAdapter<String>(R.layout.item_main, list) {
            @Override
            protected void bindView(BaseByViewHolder<String> holder, String bean, int position) {
                holder.setText(R.id.tv_sort, (position + 1) + "、");
                holder.setText(R.id.tv_text, bean);
            }
        });
        binding.recyclerView.addItemDecoration(new SpacesItemDecoration(this, SpacesItemDecoration.VERTICAL).setNoShowDivider(0, 0));
        binding.recyclerView.setOnItemClickListener(new ByRecyclerView.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                switch (position) {
                    case 0:// 下拉刷新、加载更多
                        SecondTypeActivity.start(v.getContext(), "refreshLoadMore");
                        break;
                    case 1:// Add HeaderView/FooterView
                        startActivity(new Intent(MainActivity.this, HeaderFooterActivity.class));
                        break;
                    case 2:// 设置StateView
                        startActivity(new Intent(MainActivity.this, StateViewActivity.class));
                        break;
                    case 3:// item 点击/长按
                        startActivity(new Intent(MainActivity.this, ItemClickActivity.class));
                        break;
                    case 4:// adapter (多类型、databinding、ListView)
                        SecondTypeActivity.start(v.getContext(), "adapter");
                        break;
                    case 5:// 万能分割线
                        SecondTypeActivity.start(v.getContext(), "Divider");
                        break;
                    case 6:// item 悬浮置顶
                        startActivity(new Intent(MainActivity.this, StickyItemActivity.class));
                        break;
                    case 7:// Skeleton 骨架图
                        SecondTypeActivity.start(v.getContext(), "Skeleton");
                        break;
                    case 8:// CoordinatorLayout + RecyclerView 使用示例
                        startActivity(new Intent(MainActivity.this, AppBarLayoutActivity.class));
                        break;
                    default:
                        break;

                }
            }
        });
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
                WebUtil.openLink(MainActivity.this, "http://d.6short.com/byrecyclerview");
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
