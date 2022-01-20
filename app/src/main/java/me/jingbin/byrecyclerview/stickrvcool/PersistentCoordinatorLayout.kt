package me.jingbin.byrecyclerview.stickrvcool

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import me.jingbin.byrecyclerview.R

/**
 * 定制的CoordinatorLayout，第一个子View必须为AppbarLayout
 * 如果底部不用ViewPager的话，可以设置其adapter->getCount=0
 */
class PersistentCoordinatorLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CoordinatorLayout(context, attrs, defStyleAttr) {

    private lateinit var appBarLayout: AppBarLayout

    private var innerViewPager: ViewPager? = null
    private var innerViewPager2: ViewPager2? = null

    private var overScroller: HookedScroller? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount == 2 && getChildAt(0) is AppBarLayout) {
            this.appBarLayout = getChildAt(0) as AppBarLayout
            this.appBarLayout.viewTreeObserver.addOnGlobalLayoutListener {
                if (overScroller == null) {
                    hookScroller()
                }
            }
        } else {
            throw RuntimeException("CustCoordinatorLayout's first child must be AppbarLayout")
        }
    }

    /**
     * 反射behavior塞入hookScroller
     */
    private fun hookScroller() {
        val lp = appBarLayout.layoutParams as LayoutParams
        val behavior = lp.behavior as AppBarLayout.Behavior
        behavior.setDragCallback(PersistentCallback())

        val scrollerField =
            AppBarLayout.Behavior::class.java.superclass!!.superclass!!.getDeclaredField("scroller")
        scrollerField.isAccessible = true
        if (scrollerField.get(behavior) == null) {
            // 1. 初始化HookedScroller
            overScroller = HookedScroller(context) {
                findCurrentPersistentRecyclerView()
            }

            // 2. 注入Scroller
            scrollerField.set(behavior, overScroller)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        // 手指按下时，所有scroll动画都需要停止
        if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
            // behavior滑动停止
            overScroller?.clearPendingMessages()
            overScroller?.forceFinished(true)

            // 底部的RecyclerView滑动停止
            if (ev.y < appBarLayout.bottom) {
                val currentRecyclerView = findCurrentPersistentRecyclerView()
                currentRecyclerView?.stopScroll()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 获取当前的PersistentRecyclerView
     */
    private fun findCurrentPersistentRecyclerView(): PersistentRecyclerView? {
        if (innerViewPager != null) {
            val currentItem = innerViewPager!!.currentItem
            for (i in 0 until innerViewPager!!.childCount) {
                val itemChildView = innerViewPager!!.getChildAt(i)
                val layoutParams = itemChildView.layoutParams as ViewPager.LayoutParams
                val positionField = layoutParams.javaClass.getDeclaredField("position")
                positionField.isAccessible = true
                val position = positionField.get(layoutParams) as Int

                if (!layoutParams.isDecor && currentItem == position) {
                    if (itemChildView is PersistentRecyclerView) {
                        return itemChildView
                    } else {
                        val tagView = itemChildView?.getTag(R.id.tag_saved_child_recycler_view)
                        if (tagView is PersistentRecyclerView) {
                            return tagView
                        }
                    }
                }
            }
        } else if (innerViewPager2 != null) {
            val layoutManagerFiled = ViewPager2::class.java.getDeclaredField("mLayoutManager")
            layoutManagerFiled.isAccessible = true
            val pagerLayoutManager = layoutManagerFiled.get(innerViewPager2) as LinearLayoutManager
            var currentChild = pagerLayoutManager.findViewByPosition(innerViewPager2!!.currentItem)

            if (currentChild is PersistentRecyclerView) {
                return currentChild
            } else {
                val tagView = currentChild?.getTag(R.id.tag_saved_child_recycler_view)
                if (tagView is PersistentRecyclerView) {
                    return tagView
                }
            }
        }
        return null
    }

    fun setInnerViewPager(viewPager: ViewPager?) {
        this.innerViewPager = viewPager
    }

    fun setInnerViewPager2(viewPager2: ViewPager2?) {
        this.innerViewPager2 = viewPager2
    }

    inner class PersistentCallback: AppBarLayout.Behavior.DragCallback() {
        override fun canDrag(appBarLayout: AppBarLayout) = true
    }

}