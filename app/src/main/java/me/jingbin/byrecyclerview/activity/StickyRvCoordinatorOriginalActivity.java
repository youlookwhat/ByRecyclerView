package me.jingbin.byrecyclerview.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.app.BaseActivity;
import me.jingbin.byrecyclerview.databinding.ActivityStickyRvCoordBinding;
import me.jingbin.byrecyclerview.databinding.ActivityStickyRvCoordOriginalBinding;
import me.jingbin.byrecyclerview.fragment.CoordinatorOriginalFragment;
import me.jingbin.byrecyclerview.fragment.RefreshFragment;
import me.jingbin.byrecyclerview.fragment.SwipeRefreshFragment;
import me.jingbin.byrecyclerview.stickrvcool.config.FeedsPagerAdapter;
import me.jingbin.byrecyclerview.stickrvcool.config.HomeIndicatorHelper;
import me.jingbin.byrecyclerview.view.MyFragmentPagerAdapter;

/**
 * @author jingbin
 * CoordinatorLayout 嵌套滑动置顶(原始方案)
 */
public class StickyRvCoordinatorOriginalActivity extends BaseActivity<ActivityStickyRvCoordOriginalBinding> {

    private final ArrayList<String> mTitleList = new ArrayList<>(5);
    private final ArrayList<Fragment> mFragments = new ArrayList<>(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky_rv_coord_original);
        setTitle("CoordinatorLayout 嵌套滑动置顶");
        initView();
    }

    private void initView() {
        initFragmentList();
        MyFragmentPagerAdapter myAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments, mTitleList);
        binding.viewPager.setAdapter(myAdapter);
        binding.viewPager.setOffscreenPageLimit(mTitleList.size() - 1);
        myAdapter.notifyDataSetChanged();
        binding.tabLayout.setupWithViewPager(binding.viewPager);
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

    private void initFragmentList() {
        mTitleList.clear();
        mTitleList.add("关注");
        mTitleList.add("推荐");
        mTitleList.add("直播");
        mTitleList.add("进口");
        mTitleList.add("实惠");
        mFragments.add(CoordinatorOriginalFragment.newInstance(""));
        mFragments.add(CoordinatorOriginalFragment.newInstance(""));
        mFragments.add(CoordinatorOriginalFragment.newInstance(""));
        mFragments.add(CoordinatorOriginalFragment.newInstance(""));
        mFragments.add(CoordinatorOriginalFragment.newInstance(""));
    }
}
