package me.jingbin.byrecyclerview.stickyrv

import android.content.Context
import android.util.AttributeSet
import android.widget.OverScroller
import androidx.recyclerview.widget.RecyclerView
import me.jingbin.library.ByRecyclerView
import java.lang.reflect.Field

/**
 * 一个可以方便获取Y轴速率的RecyclerView基类
 */
open class BaseRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ByRecyclerView(context, attrs, defStyleAttr) {

    private val overScroller: OverScroller;

    private val scrollerYObj: Any;

    private val velocityYField: Field;

    init {
        // 1. mViewFlinger对象获取
        val viewFlingField = RecyclerView::class.java.getDeclaredField("mViewFlinger")
        viewFlingField.isAccessible = true
        var viewFlingObj = viewFlingField.get(this)

        // 2. overScroller对象获取
        val overScrollerFiled = viewFlingObj.javaClass.getDeclaredField("mOverScroller")
        overScrollerFiled.isAccessible = true
        overScroller = overScrollerFiled.get(viewFlingObj) as OverScroller

        // 3. scrollerY对象获取
        val scrollerYFiled = OverScroller::class.java.getDeclaredField("mScrollerY")
        scrollerYFiled.isAccessible = true
        scrollerYObj = scrollerYFiled.get(overScroller)

        // 4. Y轴速率filed获取
        velocityYField = scrollerYObj.javaClass.getDeclaredField("mCurrVelocity")
        velocityYField.isAccessible = true
    }

    /**
     * 获取垂直方向的速率
     */
    fun getVelocityY(): Int = (velocityYField.get(scrollerYObj) as Float).toInt()

    /**
     * 停止滑动fling
     */
    fun stopFling() {
        this.overScroller.forceFinished(true)
        velocityYField.set(scrollerYObj, 0)
    }
}