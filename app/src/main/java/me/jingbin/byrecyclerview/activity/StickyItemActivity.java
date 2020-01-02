package me.jingbin.byrecyclerview.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.app.BaseActivity;
import me.jingbin.byrecyclerview.databinding.ActivityRefreshBinding;
import me.jingbin.byrecyclerview.fragment.StickyGridFragment;
import me.jingbin.byrecyclerview.fragment.StickyLinearFragment;
import me.jingbin.byrecyclerview.view.MyFragmentPagerAdapter;

/**
 * @author jingbin
 */
public class StickyItemActivity extends BaseActivity<ActivityRefreshBinding> {

    private ArrayList<String> mTitleList = new ArrayList<>(2);
    private ArrayList<Fragment> mFragments = new ArrayList<>(2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
        setTitle("Item 悬浮置顶");

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
        mTitleList.add("线性列表");
        mTitleList.add("宫格列表");
        mFragments.add(StickyLinearFragment.newInstance(""));
        mFragments.add(StickyGridFragment.newInstance(""));
    }

}
