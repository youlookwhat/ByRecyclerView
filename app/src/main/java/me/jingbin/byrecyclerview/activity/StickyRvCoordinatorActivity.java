package me.jingbin.byrecyclerview.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.app.BaseActivity;
import me.jingbin.byrecyclerview.databinding.ActivityStickyRvCoordBinding;
import me.jingbin.byrecyclerview.stickrvcool.config.FeedsPagerAdapter;
import me.jingbin.byrecyclerview.stickrvcool.config.HomeIndicatorHelper;

/**
 * @author jingbin
 */
public class StickyRvCoordinatorActivity extends BaseActivity<ActivityStickyRvCoordBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky_rv_coord);
        setTitle("CoordinatorLayout 嵌套滑动置顶");
        initAdapter();
    }

    private void initAdapter() {
        binding.mainFeedsViewpager.setAdapter(new FeedsPagerAdapter(this));
        HomeIndicatorHelper homeIndicatorHelper = new HomeIndicatorHelper(this);
        homeIndicatorHelper.setViewPager(binding.mainFeedsViewpager);

        binding.mainRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                binding.mainRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.mainRefreshLayout.finishRefresh();
                    }
                }, 1000);
            }
        });
    }

    public ActivityStickyRvCoordBinding get() {
        return binding;
    }
}
