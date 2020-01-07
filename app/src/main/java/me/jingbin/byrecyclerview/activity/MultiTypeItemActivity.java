package me.jingbin.byrecyclerview.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.app.BaseActivity;
import me.jingbin.byrecyclerview.databinding.ActivityRefreshBinding;
import me.jingbin.byrecyclerview.fragment.MultiGridFragment;
import me.jingbin.byrecyclerview.fragment.MultiLinearFragment;
import me.jingbin.byrecyclerview.fragment.MultiStaggeredFragment;
import me.jingbin.byrecyclerview.fragment.RefreshFragment;
import me.jingbin.byrecyclerview.fragment.SwipeRefreshFragment;
import me.jingbin.byrecyclerview.view.MyFragmentPagerAdapter;

/**
 * @author jingbin
 */
public class MultiTypeItemActivity extends BaseActivity<ActivityRefreshBinding> {

    private ArrayList<String> mTitleList = new ArrayList<>(3);
    private ArrayList<Fragment> mFragments = new ArrayList<>(3);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
        setTitle("多类型列表");

        initView();
    }

    private void initView() {
        initFragmentList();
        MyFragmentPagerAdapter myAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments, mTitleList);
        binding.viewPager.setAdapter(myAdapter);
        binding.viewPager.setOffscreenPageLimit(2);
        myAdapter.notifyDataSetChanged();
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }

    private void initFragmentList() {
        mTitleList.clear();
        mTitleList.add("线性列表");
        mTitleList.add("宫格列表");
        mTitleList.add("瀑布流");
        mFragments.add(MultiLinearFragment.newInstance(""));
        mFragments.add(MultiGridFragment.newInstance(""));
        mFragments.add(MultiStaggeredFragment.newInstance(""));
    }

}
