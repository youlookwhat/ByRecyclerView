package me.jingbin.byrecyclerview.stickrvcool.config

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import me.jingbin.byrecyclerview.stickrvcool.config.FeedsListFragment

class FeedsPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return FeedsListFragment()
    }

    override fun getItemCount(): Int {
        return 5
    }
}