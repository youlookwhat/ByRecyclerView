package me.jingbin.byrecyclerview.stickyrv.config

import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import me.jingbin.byrecyclerview.R
import me.jingbin.byrecyclerview.binding.BaseBindingHolder
import me.jingbin.byrecyclerview.databinding.StickyrvItemMainFeedsBinding
import me.jingbin.library.adapter.BaseByRecyclerViewAdapter
import me.jingbin.library.adapter.BaseByViewHolder

class StickAdapter(private val activity: AppCompatActivity) : BaseByRecyclerViewAdapter<String, BaseByViewHolder<String>>() {

    private var tabsLoaded = false
    private var loadingTabsListener: (() -> Unit)? = null

    companion object {
        // 正在加载tabs
        private const val VIEW_TYPE_LOADING_TABS = 1

        // 商品流
        private const val VIEW_TYPE_FEEDS = 2

        private val COLOR_TAB_NORMAL by lazy { Color.parseColor("#333333") }
        private val COLOR_TAB_SELECTED by lazy { Color.parseColor("#2483D9") }
    }

    override fun getItemViewType(position: Int): Int = when (position) {
        0 -> if (tabsLoaded) VIEW_TYPE_FEEDS else VIEW_TYPE_LOADING_TABS
        else -> VIEW_TYPE_FEEDS
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseByViewHolder<String> {
        return when (viewType) {
            VIEW_TYPE_LOADING_TABS -> LoadingViewHolder(parent, R.layout.stickyrv_item_loading_footer)
            else -> FeedViewHolder(parent, R.layout.stickyrv_item_main_feeds, activity)
        }
    }

    override fun onBindViewHolder(holder: BaseByViewHolder<String>, position: Int) {
//        super.onBindViewHolder(holder, position)
        if (holder is LoadingViewHolder) {
            loadingTabsListener?.invoke()
        }
    }

    /**信息流*/
    class FeedViewHolder(viewGroup: ViewGroup?, layoutId: Int, activity: AppCompatActivity) :
            BaseBindingHolder<String, StickyrvItemMainFeedsBinding>(viewGroup, layoutId) {

        private val tabList = ArrayList<TextView>()
        private val feedsViewPager = binding.mainFeedsViewPager

        /**构造方法*/
        init {
            feedsViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            feedsViewPager.offscreenPageLimit = 5
            feedsViewPager.adapter = FeedsListJavaAdapter(activity)
            bindTabs()
        }

        private fun bindTabs() {

            val tabClickListener = View.OnClickListener {
                val indexOf = tabList.indexOf(it)
                feedsViewPager.currentItem = indexOf
            }

            tabList.add(binding.feedsTab1)
            tabList.add(binding.feedsTab2)
            tabList.add(binding.feedsTab3)
            tabList.add(binding.feedsTab4)
            tabList.add(binding.feedsTab5)
            for (tab in tabList) {
                tab.setOnClickListener(tabClickListener)
            }

            feedsViewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    onTabChanged(position)
                }
            })
            onTabChanged(0)
        }

        private fun onTabChanged(position: Int) {
            val num = tabList.size
            for (i in 0 until num) {
                val textView = tabList[i]
                if (i == position) {
                    textView.setTextColor(COLOR_TAB_SELECTED)
                    textView.paint.isFakeBoldText = true
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                } else {
                    textView.setTextColor(COLOR_TAB_NORMAL)
                    textView.paint.isFakeBoldText = false
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                }
            }
        }

        override fun onBindingView(holder: BaseBindingHolder<*, *>?, bean: String?, position: Int) {

        }

    }

    /**加载中的布局*/
    class LoadingViewHolder(viewGroup: ViewGroup?, layoutId: Int) : BaseByViewHolder<String>(viewGroup, layoutId) {

        override fun onBaseBindView(holder: BaseByViewHolder<String>?, bean: String?, position: Int) {

        }
    }

    override fun getItemCount(): Int {
        return 1
    }

    fun setLoadingTabsListener(listener: () -> Unit) {
        this.loadingTabsListener = listener
    }

    fun onTabsLoaded() {
        tabsLoaded = true
        refreshNotifyItemChanged(0)
    }
}