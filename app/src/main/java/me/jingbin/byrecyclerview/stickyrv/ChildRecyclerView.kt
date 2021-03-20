package me.jingbin.byrecyclerview.stickyrv

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import me.jingbin.byrecyclerview.R

/**
 * 内层的RecyclerView
 */
class ChildRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseRecyclerView(context, attrs, defStyleAttr) {

    private var parentRecyclerView: ParentRecyclerView? = null

    private val mTouchSlop: Int
    private var downX: Float = 0f
    private var downY: Float = 0f

    private var dragState: Int = DRAG_IDLE

    companion object {
        private const val DRAG_IDLE = 0
        private const val DRAG_VERTICAL = 1
        private const val DRAG_HORIZONTAL = 2
    }

    init {
        val configuration = ViewConfiguration.get(context)
        mTouchSlop = configuration.scaledTouchSlop
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)

        // 是否已经停止scrolling
        if (state == SCROLL_STATE_IDLE) {
            // 这里是考虑到当整个childRecyclerView被detach之后，及时上报parentRecyclerView
            val velocityY = getVelocityY()
            if (velocityY < 0 && computeVerticalScrollOffset() == 0) {
                parentRecyclerView?.fling(0, velocityY)
            }
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            // ACTION_DOWN 触摸按下，保存临时变量
            dragState = DRAG_IDLE
            downX = ev.rawX
            downY = ev.rawY
            this.stopFling()
        }else if (ev.action == MotionEvent.ACTION_MOVE) {
            // ACTION_MOVE 判定垂直还是水平滑动
            //获取到距离差
            val xDistance = Math.abs(ev.rawX - downX)
            val yDistance = Math.abs(ev.rawY - downY)
            if (xDistance > yDistance && xDistance > mTouchSlop) {
                // 水平滑动
                return true
            }else if(xDistance == 0f && yDistance == 0f){
                // 点击
                return super.onInterceptTouchEvent(ev)
            }else if(yDistance >= xDistance && yDistance > 8f){
                // 垂直滑动
                return true
            }
        }
//        Log.e("super.onInter",super.onInterceptTouchEvent(ev).toString())
        return super.onInterceptTouchEvent(ev)
    }

    /**
     * 这段逻辑主要是RecyclerView最底部，垂直上拉后居然还能左右滑动，不能忍
     */
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            // 一上来就禁止ParentRecyclerView拦截Touch事件
            parent.requestDisallowInterceptTouchEvent(true)
        } else if (ev.action == MotionEvent.ACTION_MOVE) {
            // ACTION_MOVE 判定垂直还是水平滑动
//            if (dragState == DRAG_IDLE) {
            //获取到距离差
            val xDistance = Math.abs(ev.rawX - downX)
            val yDistance = Math.abs(ev.rawY - downY)

            if (xDistance > yDistance && xDistance > mTouchSlop) {
                // 水平滑动
                dragState = DRAG_HORIZONTAL
                // touch事件允许 ViewPager / ViewPager2 处理
                parent.requestDisallowInterceptTouchEvent(false)
            } else if (yDistance > xDistance && yDistance > mTouchSlop) {
                // 垂直滑动
                dragState = DRAG_VERTICAL
                parent.requestDisallowInterceptTouchEvent(true)
            }
//            }
        }
        return super.onTouchEvent(ev)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        connectToParent()
    }

    /**
     * 跟ParentView建立连接，主要两件事情 -
     * 1. 将自己上报ViewPager/ViewPager2，通过tag关联到currentItem的View中
     * 2. 将ViewPager/ViewPager2报告给ParentRecyclerView
     * 这一坨代码需要跟ParentRecyclerView连起来看，否则可能会懵
     */
    private fun connectToParent() {
        var viewPager: ViewPager? = null
        var viewPager2: ViewPager2? = null
        var lastTraverseView: View = this

        var parentView:View? = this.parent as View
        while (parentView != null) {
            val parentClassName = parentView::class.java.canonicalName
            if ("androidx.viewpager2.widget.ViewPager2.RecyclerViewImpl" == parentClassName) {
                // 使用ViewPager2，parentView的顺序如下:
                // ChildRecyclerView -> 若干View -> FrameLayout -> RecyclerViewImpl -> ViewPager2 -> 若干View -> ParentRecyclerView

                // 此时lastTraverseView是上方注释中的FrameLayout，算是"ViewPager2.child"，我们此处将ChildRecyclerView设置到FrameLayout的tag中
                // 这个tag会在ParentRecyclerView中用到
                lastTraverseView.setTag(R.id.tag_saved_child_recycler_view, this)
            } else if (parentView is ViewPager) {
                // 使用ViewPager，parentView顺序如下：
                // ChildRecyclerView -> 若干View -> ViewPager -> 若干View -> ParentRecyclerView
                // 此处将ChildRecyclerView保存到ViewPager最直接的子View中
                if (lastTraverseView != this) {
                    // 这个tag会在ParentRecyclerView中用到
                    lastTraverseView.setTag(R.id.tag_saved_child_recycler_view, this)
                }

                // 碰到ViewPager，需要上报给ParentRecyclerView
                viewPager = parentView
            } else if (parentView is ViewPager2) {
                // 碰到ViewPager2，需要上报给ParentRecyclerView
                viewPager2 = parentView
            } else if (parentView is ParentRecyclerView) {
                // 碰到ParentRecyclerView，设置结束
                parentView.setInnerViewPager(viewPager)
                parentView.setInnerViewPager2(viewPager2)
                parentView.setChildPagerContainer(lastTraverseView)
                this.parentRecyclerView = parentView
                return
            }

            lastTraverseView = parentView
            parentView = parentView.parent as View
        }
    }


    override fun dispatchTouchEvent(e: MotionEvent): Boolean {
        val x = e.rawX
        val y = e.rawY
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                //将按下时的坐标存储
                downX = x
                downY = y
                //                True if the child does not want the parent to
//                intercept touch events.
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                //获取到距离差
                val dx: Float = x - downX
                val dy: Float = y - downY
//                Log.d("aaa", "ACTION_MOVE")
                //通过距离差判断方向
                val orientation = getOrientation(dx, dy)
//                DebugUtil.debug("jingbin","child-------------"+orientation);
                val location = intArrayOf(0, 0)
                getLocationOnScreen(location)
                when (orientation) {
                    "d" -> if (canScrollVertically(-1)) {
//                        Log.e("ChildRecyclerView", "d   不要拦截")
                        parent.requestDisallowInterceptTouchEvent(true)
                    } else { //内层RecyclerView下拉到最顶部时候不再处理事件
                        if(dy < 24f){
                            parent.requestDisallowInterceptTouchEvent(true)
                        }else{
                            parent.requestDisallowInterceptTouchEvent(false)
                        }
//                        Log.e("ChildRecyclerView", "d  拦截")
                    }
                    "u" -> {
//                        if (canScrollVertically(1)) {
//                            Log.e("ChildRecyclerView", "u   不要拦截")
                        parent.requestDisallowInterceptTouchEvent(true)
//                        } else { //内层RecyclerView上拉到最底部时候不再处理事件
//                            parent.requestDisallowInterceptTouchEvent(false)
//                            Log.d("aaa", "u  拦截")
//                        }
                    }
                    "r" -> {
//                        Log.e("ChildRecyclerView", "r  不要拦截")
//                        parent.requestDisallowInterceptTouchEvent(false)
                    }
                    "l" -> {
//                        Log.e("ChildRecyclerView", "l  不要拦截")
//                        parent.requestDisallowInterceptTouchEvent(false)
                    }
                }
            }
        }
        return super.dispatchTouchEvent(e)
    }

    private fun getOrientation(dx: Float, dy: Float): String {
//        DebugUtil.debug("jingbin","child----------------------------------dx:dy"+dx+":"+dy);
        return if (Math.abs(dx) > Math.abs(dy)) {
            //X轴移动
            if (dx > 0) "r" else "l" //右,左
        } else {
            //Y轴移动
            if (dy > 0) "d" else "u" //下//上
        }
    }

}