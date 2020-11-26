package me.jingbin.byrecyclerview.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.app.BaseActivity;
import me.jingbin.byrecyclerview.databinding.ActivityRefreshBinding;
import me.jingbin.byrecyclerview.databinding.ActivityRefreshTypeBinding;
import me.jingbin.byrecyclerview.fragment.MultiGridFragment;
import me.jingbin.byrecyclerview.fragment.MultiLinearFragment;
import me.jingbin.byrecyclerview.fragment.MultiStaggeredFragment;
import me.jingbin.byrecyclerview.fragment.RefreshFragment;
import me.jingbin.byrecyclerview.fragment.SwipeRefreshFragment;
import me.jingbin.byrecyclerview.view.MyFragmentPagerAdapter;

/**
 * @author jingbin
 */
public class MultiTypeItemActivity extends BaseActivity<ActivityRefreshTypeBinding> {

    private ArrayList<String> mTitleList = new ArrayList<>(3);
    private ArrayList<Fragment> mFragments = new ArrayList<>(3);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_type);
        setTitle("多类型列表");

        initView();
    }

    private void initView() {
        initFragmentList();
//        initViewPage();
        initViewPage2();
    }

    private void initViewPage() {
//        MyFragmentPagerAdapter myAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments, mTitleList);
//        binding.viewPager.setAdapter(myAdapter);
//        binding.viewPager.setOffscreenPageLimit(2);
//        myAdapter.notifyDataSetChanged();
//        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }

    private void initViewPage2() {
        binding.tabLayout.setTabMode(TabLayout.MODE_FIXED);
        binding.viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (position == 0) {
                    return MultiLinearFragment.newInstance("");
                } else if (position == 1) {
                    return MultiGridFragment.newInstance("");
                } else {
                    return MultiStaggeredFragment.newInstance("");
                }
            }

            @Override
            public int getItemCount() {
                return mTitleList.size();
            }
        });
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(binding.tabLayout, binding.viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(mTitleList.get(position));
            }
        });
        tabLayoutMediator.attach();
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
