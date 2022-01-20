package me.jingbin.byrecyclerview.stickrvcool.config

import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import me.jingbin.byrecyclerview.activity.StickyRvCoordinatorActivity

class HomeIndicatorHelper(mainActivity: StickyRvCoordinatorActivity) {

    private val tabList = ArrayList<TextView>()
    private var viewPager2: ViewPager2? = null
    private var pageChangeListener: ViewPager2.OnPageChangeCallback

    companion object {
        private val COLOR_TAB_NORMAL by lazy { Color.parseColor("#333333") }
        private val COLOR_TAB_SELECTED by lazy { Color.parseColor("#2483D9") }
    }

    init {
        // 1. 按钮点击
        val tabClickListener = View.OnClickListener {
            val index = tabList.indexOf(it)
            viewPager2?.currentItem = index
        }

        // 2. 页面切换
        pageChangeListener = object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                onTabChanged(position)
            }
        }

        // 3. 初始化
        tabList.add(mainActivity.get().feedsTab1)
        tabList.add(mainActivity.get().feedsTab2)
        tabList.add(mainActivity.get().feedsTab3)
        tabList.add(mainActivity.get().feedsTab4)
        tabList.add(mainActivity.get().feedsTab5)

        for (itemTab in tabList) {
            itemTab.setOnClickListener(tabClickListener)
        }

        // 4. 选中第一个
        onTabChanged(0)
    }

    private fun onTabChanged(position: Int) {
        val num = tabList.size
        for (i in 0 until num) {
            val itemTab = tabList[i]
            if (i == position) {
                itemTab.setTextColor(COLOR_TAB_SELECTED)
                itemTab.paint.isFakeBoldText = true
                itemTab.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            } else {
                itemTab.setTextColor(COLOR_TAB_NORMAL)
                itemTab.paint.isFakeBoldText = false
                itemTab.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
            }
        }
    }

    fun setViewPager(viewPager2: ViewPager2) {
        this.viewPager2 = viewPager2
        viewPager2.registerOnPageChangeCallback(pageChangeListener)
    }
}