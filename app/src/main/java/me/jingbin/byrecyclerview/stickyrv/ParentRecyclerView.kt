package me.jingbin.byrecyclerview.stickyrv

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.NestedScrollingParent3
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import me.jingbin.byrecyclerview.R

/**
 * 外层的RecyclerView
 */
class ParentRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseRecyclerView(context, attrs, defStyleAttr), NestedScrollingParent3 {

    private var childPagerContainer: View? = null

    private var innerViewPager: ViewPager? = null

    private var innerViewPager2: ViewPager2? = null

    private var doNotInterceptTouchEvent: Boolean = false

    private var stickyListener: ((isAtTop: Boolean) -> Unit)? = null

    private var innerIsStickyTop = false

    /**
     * 顶部悬停的高度
     */
    private var stickyHeight = 0

    init {
        this.overScrollMode = View.OVER_SCROLL_NEVER
        this.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
    }

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        if (e!!.action == MotionEvent.ACTION_DOWN) {
            val childRecyclerView = findCurrentChildRecyclerView()

            // 1. 是否禁止拦截
            doNotInterceptTouchEvent = doNotInterceptTouch(e.rawY, childRecyclerView)

            // 2. 停止Fling
            this.stopFling()
            childRecyclerView?.stopFling()
        }

        return if (doNotInterceptTouchEvent) {
            false
        } else {
            super.onInterceptTouchEvent(e)
        }
    }

    /**
     * 是否禁止拦截TouchEvent事件
     */
    private fun doNotInterceptTouch(rawY: Float, childRecyclerView: ChildRecyclerView?): Boolean {
        if (null != childRecyclerView &&
            null != childPagerContainer &&
            ViewCompat.isAttachedToWindow(childPagerContainer!!)
        ) {
            val coorValue = IntArray(2)
            childRecyclerView.getLocationOnScreen(coorValue)

            val childRecyclerViewY = coorValue[1]
            if (rawY > childRecyclerViewY) {
                return true
            }

            if (childPagerContainer!!.top == stickyHeight) {
                return true
            }
        }

        // 默认不禁止
        return false
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        if (target is ChildRecyclerView) {
            // 下面这一坨代码的主要目的是计算consumeY
            var consumeY = dy
            val childScrollY = target.computeVerticalScrollOffset()
            if (childPagerContainer!!.top > stickyHeight) {
                if (childScrollY > 0 && dy < 0) {
                    consumeY = 0
                } else if (childPagerContainer!!.top - dy < stickyHeight) {
                    consumeY = childPagerContainer!!.top - stickyHeight
                }
            } else if (childPagerContainer!!.top == stickyHeight) {
                consumeY = if (-dy < childScrollY) {
                    0
                } else {
                    dy + childScrollY
                }
            }

            if (consumeY != 0) {
                consumed[1] = consumeY
                this.scrollBy(0, consumeY)
            }
        }
    }

    override fun onScrollStateChanged(state: Int) {
        if (state == SCROLL_STATE_IDLE) {
            val velocityY = getVelocityY()
            if (velocityY > 0) {
                // 滑动到最底部时，骤然停止，这时需要把速率传递给ChildRecyclerView
                val childRecyclerView = findCurrentChildRecyclerView()
                childRecyclerView?.fling(0, velocityY)
            }
        }
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        return target is ChildRecyclerView
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        // do nothing
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        // do nothing
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        // do nothing
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        // do nothing
    }

    /**
     * 获取当前的ChildRecyclerView
     */
    private fun findCurrentChildRecyclerView(): ChildRecyclerView? {
        if (innerViewPager != null) {
            val currentItem = innerViewPager!!.currentItem
            for (i in 0 until innerViewPager!!.childCount) {
                val itemChildView = innerViewPager!!.getChildAt(i)
                val layoutParams = itemChildView.layoutParams as ViewPager.LayoutParams
                val positionField = layoutParams.javaClass.getDeclaredField("position")
                positionField.isAccessible = true
                val position = positionField.get(layoutParams) as Int

                if (!layoutParams.isDecor && currentItem == position) {
                    if (itemChildView is ChildRecyclerView) {
                        return itemChildView
                    } else {
                        val tagView = itemChildView?.getTag(R.id.tag_saved_child_recycler_view)
                        if (tagView is ChildRecyclerView) {
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

            if (currentChild is ChildRecyclerView) {
                return currentChild
            } else {
                val tagView = currentChild?.getTag(R.id.tag_saved_child_recycler_view)
                if (tagView is ChildRecyclerView) {
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

    /**
     * 由ChildRecyclerView上报ViewPager(ViewPager2)的父容器，用做内联滑动逻辑判断，及Touch拦截等
     */
    fun setChildPagerContainer(childPagerContainer: View) {
        if (this.childPagerContainer != childPagerContainer) {
            this.childPagerContainer = childPagerContainer
            this.post {
                adjustChildPagerContainerHeight()
            }
        }
    }

    /**
     * Activity调用方法
     */
    fun setStickyHeight(stickyHeight: Int) {
        val scrollOffset = this.stickyHeight - stickyHeight
        this.stickyHeight = stickyHeight
        this.adjustChildPagerContainerHeight()

        this.post {
            this.scrollBy(0, scrollOffset)
        }
    }

    /**
     * 调整Child容器的高度
     */
    private fun adjustChildPagerContainerHeight() {
        if (null != childPagerContainer) {
            val layoutParams = childPagerContainer!!.layoutParams
            val newHeight = this.height - stickyHeight
            if (newHeight != layoutParams.height) {
                layoutParams.height = newHeight
                childPagerContainer!!.layoutParams = layoutParams
            }
        }
    }

    /**
     * 吸顶回调
     */
    fun setStickyListener(stickyListener: (isAtTop: Boolean) -> Unit) {
        this.stickyListener = stickyListener
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)

        var currentStickyTop = false
        if (childPagerContainer != null && childPagerContainer?.top == stickyHeight) {
            currentStickyTop = true
        }

        if (currentStickyTop != innerIsStickyTop) {
            innerIsStickyTop = currentStickyTop
            stickyListener?.invoke(innerIsStickyTop)
        }
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        try {
            if (android.os.Build.VERSION.SDK_INT <= 19) {
                return true
            }
            return super.onNestedPreFling(target, velocityX, velocityY)
        } catch (e: Exception) {
            return true
        }
    }
}