package me.jingbin.byrecyclerview.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.app.BaseActivity;
import me.jingbin.byrecyclerview.databinding.ActivityRefreshBinding;
import me.jingbin.byrecyclerview.fragment.RefreshFragment;
import me.jingbin.byrecyclerview.fragment.SwipeRefreshFragment;
import me.jingbin.byrecyclerview.view.MyFragmentPagerAdapter;

/**
 * @author jingbin
 */
public class LoadMoreActivity extends BaseActivity<ActivityRefreshBinding> {

    private ArrayList<String> mTitleList = new ArrayList<>(2);
    private ArrayList<Fragment> mFragments = new ArrayList<>(2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
        setTitle("自动加载更多、松手加载更多");

        initView();
    }

    private void initView() {
        initFragmentList();
        MyFragmentPagerAdapter myAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments, mTitleList);
        binding.viewPager.setAdapter(myAdapter);
        binding.viewPager.setOffscreenPageLimit(1);
        myAdapter.notifyDataSetChanged();
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }

    private void initFragmentList() {
        mTitleList.clear();
        mTitleList.add("自动加载更多");
        mTitleList.add("松手加载更多");
        mFragments.add(RefreshFragment.newInstance("auto"));
        mFragments.add(RefreshFragment.newInstance(""));
    }

}
