package me.jingbin.byrecyclerview.stickyrv.config

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import me.jingbin.byrecyclerview.stickyrv.config.FeedsJavaFragment

/**
 * Created by jingbin on 3/20/21.
 */
class FeedsListJavaAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return FeedsJavaFragment()
    }
}