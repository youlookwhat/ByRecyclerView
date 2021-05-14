package me.jingbin.byrecyclerview.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.app.BaseActivity;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.binding.BaseBindingAdapter;
import me.jingbin.byrecyclerview.binding.BaseBindingHolder;
import me.jingbin.byrecyclerview.databinding.ActivitySimpleBinding;
import me.jingbin.byrecyclerview.databinding.ItemMainBinding;
import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.decoration.SpacesItemDecoration;

/**
 * @author jingbin
 * 二级分类展示页面
 */
public class SecondTypeActivity extends BaseActivity<ActivitySimpleBinding> {

    private List<DataItemBean> list = new ArrayList<>();
    private BaseBindingAdapter<DataItemBean, ItemMainBinding> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);

        String type = getIntent().getStringExtra("type");
        switch (type) {
            case "refreshLoadMore":
                setTitle("下拉刷新、加载更多");
                list.add(new DataItemBean("使用 自带刷新 / SwipeRefreshLayout", RefreshActivity.class));
                list.add(new DataItemBean("自定义下拉刷新布局 / 加载更多布局", CustomLayoutActivity.class));
                list.add(new DataItemBean("自定义加载更多布局 (横向)", CustomHorizontalLayoutActivity.class));
                break;
            case "adapter":
                setTitle("adapter");
                list.add(new DataItemBean("多类型列表 (线性/宫格/瀑布流)", MultiTypeItemActivity.class));
                list.add(new DataItemBean("使用DataBinding (RecyclerView / ListView)", DataBindingActivity.class));
                list.add(new DataItemBean("BaseListAdapter的使用 (ListView)", ListViewActivity.class));
                break;
            case "Divider":
                setTitle("万能分割线");
                list.add(new DataItemBean("设置分割线 (线性布局)", DividerLinearActivity.class));
                list.add(new DataItemBean("设置分割线 (宫格/瀑布流)", DividerGridActivity.class));
                list.add(new DataItemBean("设置横向分割线 (宫格)", HorizontalGridDividerActivity.class));
                break;
            case "Skeleton":
                setTitle("Skeleton 骨架图");
                list.add(new DataItemBean("list", SkeletonListActivity.class));
                list.add(new DataItemBean("grid", SkeletonGridActivity.class));
                list.add(new DataItemBean("View", SkeletonViewActivity.class));
                list.add(new DataItemBean("HeaderView", SkeletonHeaderViewActivity.class));
                break;
            default:
                break;
        }
        initAdapter();
    }

    private void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.addItemDecoration(new SpacesItemDecoration(this, SpacesItemDecoration.VERTICAL).setNoShowDivider(0, 0).setDrawable(R.drawable.shape_line));
        mAdapter = new BaseBindingAdapter<DataItemBean, ItemMainBinding>(R.layout.item_main, list) {
            @Override
            protected void bindView(@NonNull BaseBindingHolder holder, @NonNull DataItemBean bean, @NonNull ItemMainBinding binding, int position) {
                binding.tvText.setText(bean.getTitle());
            }
        };
        binding.recyclerView.setAdapter(mAdapter);
        binding.recyclerView.setOnItemClickListener(new ByRecyclerView.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                DataItemBean itemData = mAdapter.getItemData(position);
                if (itemData.getCls() != null) {
                    startActivity(new Intent(SecondTypeActivity.this, itemData.getCls()));
                }
            }
        });
    }

    public static void start(Context context, String type) {
        Intent intent = new Intent(context, SecondTypeActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

}
