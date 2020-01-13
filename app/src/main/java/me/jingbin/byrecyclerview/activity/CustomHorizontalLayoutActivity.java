package me.jingbin.byrecyclerview.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.adapter.DataAdapter;
import me.jingbin.byrecyclerview.app.BaseActivity;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.databinding.ActivityHorizontalBinding;
import me.jingbin.byrecyclerview.databinding.ActivitySimpleBinding;
import me.jingbin.byrecyclerview.utils.DataUtil;
import me.jingbin.byrecyclerview.utils.ToastUtil;
import me.jingbin.byrecyclerview.view.HorizontalLoadMoreView;
import me.jingbin.byrecyclerview.view.NeteaseRefreshHeaderView;
import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;
import me.jingbin.library.decoration.SpacesItemDecoration;

/**
 * @author jingbin
 */
public class CustomHorizontalLayoutActivity extends BaseActivity<ActivityHorizontalBinding> {

    private int page = 1;
    private BaseRecyclerAdapter<DataItemBean> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal);
        setTitle("自定义加载更多布局 (横向)");

        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new BaseRecyclerAdapter<DataItemBean>(R.layout.item_horizonal, DataUtil.get(this, 7)) {
            @Override
            protected void bindView(BaseByViewHolder<DataItemBean> holder, DataItemBean bean, int position) {
                holder.setText(R.id.tv_text, bean.getTitle() + ": " + position);
            }
        };
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        SpacesItemDecoration itemDecoration = new SpacesItemDecoration(this, SpacesItemDecoration.HORIZONTAL);
        binding.recyclerView.addItemDecoration(itemDecoration.setParam(R.color.colorAccent, 1));
        // 设置加载更多view
        binding.recyclerView.setLoadingMoreView(new HorizontalLoadMoreView(this));
        binding.recyclerView.setAdapter(mAdapter);
        binding.recyclerView.setOnLoadMoreListener(new ByRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                binding.recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (page == 4) {
                            binding.recyclerView.loadMoreEnd();
                            return;
                        }
                        if (page == 2) {
                            page++;
                            binding.recyclerView.loadMoreFail();
                            return;
                        }
                        page++;
                        mAdapter.addData(DataUtil.getMore(CustomHorizontalLayoutActivity.this, 7, page));
                        binding.recyclerView.loadMoreComplete();
                    }
                }, 500);
            }
        });
        binding.recyclerView.setOnItemClickListener(new ByRecyclerView.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                DataItemBean itemData = mAdapter.getItemData(position);
                ToastUtil.showToast(itemData.getTitle());
            }
        });
    }
}
