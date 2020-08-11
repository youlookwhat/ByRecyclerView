package me.jingbin.byrecyclerview.activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.app.BaseActivity;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.databinding.ActivitySimpleBinding;
import me.jingbin.byrecyclerview.utils.DataUtil;
import me.jingbin.byrecyclerview.utils.ToastUtil;
import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;
import me.jingbin.library.decoration.SpacesItemDecoration;
import me.jingbin.library.view.OnItemChildFilterClickListener;

/**
 * @author jingbin
 */
public class ItemClickActivity extends BaseActivity<ActivitySimpleBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        setTitle("设置点击/长按事件");

        initAdapter();
    }

    private void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        SpacesItemDecoration itemDecoration = new SpacesItemDecoration(this, SpacesItemDecoration.VERTICAL);
        itemDecoration.setDrawable(R.drawable.shape_line);
        binding.recyclerView.addItemDecoration(itemDecoration);
        binding.recyclerView.setOnLoadMoreListener(new ByRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                binding.recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.recyclerView.loadMoreEnd();
                    }
                }, 500);
            }
        });
        binding.recyclerView.setAdapter(new BaseRecyclerAdapter<DataItemBean>(R.layout.item_item_click, DataUtil.get(ItemClickActivity.this, 20)) {
            @Override
            protected void bindView(BaseByViewHolder<DataItemBean> holder, DataItemBean bean, int position) {
                // 设置子View的点击/长按事件
                holder.setText(R.id.tv_click, position + ": " + "itemClick / itemLongClick")
                        .addOnClickListener(R.id.tv_item_click)
                        .addOnLongClickListener(R.id.tv_item_click);
            }
        });
        binding.recyclerView.setOnItemClickListener(new ByRecyclerView.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                ToastUtil.showToast("itemClick:" + position);
            }
        });
        binding.recyclerView.setOnItemLongClickListener(new ByRecyclerView.OnItemLongClickListener() {
            @Override
            public boolean onLongClick(View v, int position) {
                ToastUtil.showToast("itemLongClick:" + position);
                return true;
            }
        });
        binding.recyclerView.setOnItemChildClickListener(new OnItemChildFilterClickListener() {
            @Override
            public void onSingleClick(View view, int position) {
                ToastUtil.showToast("itemChildClick:" + position);
            }
        });
        binding.recyclerView.setOnItemChildLongClickListener(new ByRecyclerView.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(View view, int position) {
                ToastUtil.showToast("itemChildLongClick:" + position);
                return true;
            }
        });
    }

}
