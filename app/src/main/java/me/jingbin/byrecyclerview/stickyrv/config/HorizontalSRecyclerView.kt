package me.jingbin.byrecyclerview.stickyrv.config

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import me.jingbin.library.ByRecyclerView

/**
 * Created by jingbin on 3/21/21.
 * 在 ViewPager - RecyclerView 中的item里有RecyclerView需要左右滑动时使用
 */
class HorizontalSRecyclerView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ByRecyclerView(context, attrs, defStyleAttr) {

    //按下时 的X坐标
    private var downX = 0f

    //按下时 的Y坐标
    private var downY = 0f

    override fun dispatchTouchEvent(e: MotionEvent): Boolean {
        val x = e.x
        val y = e.y
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                //将按下时的坐标存储
                downX = x
                downY = y
            }
            MotionEvent.ACTION_MOVE -> {
                //获取到距离差
                val dx = x - downX
                val dy = y - downY
                //通过距离差判断方向
                val orientation = getOrientation(dx, dy)
                val location = intArrayOf(0, 0)
                getLocationOnScreen(location)
                when (orientation) {
                    // 上下滑动时抛给ChildRecyclerView处理
                    "d" -> parent.requestDisallowInterceptTouchEvent(false)
                    "u" -> parent.requestDisallowInterceptTouchEvent(false)
                    "r" ->
                        if (canScrollHorizontally(-1)) {
                            // 可以向右滑动时，自己处理，可以内部左右滑
                            parent.requestDisallowInterceptTouchEvent(true)
                        } else {
                            // 右滑动到顶时，交给parent处理，使其可以滑到ViewPager下一个的position
                            parent.requestDisallowInterceptTouchEvent(false)
                        }
                    "l" ->
                        if (canScrollHorizontally(1)) {
                            parent.requestDisallowInterceptTouchEvent(true)
                        } else {
                            parent.requestDisallowInterceptTouchEvent(false)
                        }
                }
            }
        }
        return super.dispatchTouchEvent(e)
    }

    private fun getOrientation(dx: Float, dy: Float): String {
        return if (Math.abs(dx) > Math.abs(dy)) {
            //X轴移动
            if (dx > 0) "r" else "l" //右,左
        } else {
            //Y轴移动
            if (dy > 0) "d" else "u" //下,上
        }
    }
}