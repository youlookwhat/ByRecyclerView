package me.jingbin.byrecyclerview.stickrvcool

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import me.jingbin.byrecyclerview.R
import me.jingbin.library.ByRecyclerView

/**
 * 内层的RecyclerView
 */
class PersistentRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ByRecyclerView(context, attrs, defStyleAttr) {

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

        var parentView = this.parent as View
        while (parentView != null) {
            val parentClassName = parentView::class.java.canonicalName
            if ("androidx.viewpager2.widget.ViewPager2.RecyclerViewImpl" == parentClassName) {
                // 使用ViewPager2，parentView的顺序如下:
                // PersistentRecyclerView -> 若干View -> FrameLayout -> RecyclerViewImpl -> ViewPager2 -> 若干View -> ParentRecyclerView

                // 此时lastTraverseView是上方注释中的FrameLayout，算是"ViewPager2.child"，我们此处将ChildRecyclerView设置到FrameLayout的tag中
                // 这个tag会在ParentRecyclerView中用到
                lastTraverseView.setTag(R.id.tag_saved_child_recycler_view, this)
            } else if (parentView is ViewPager) {
                // 使用ViewPager，parentView顺序如下：
                // PersistentRecyclerView -> 若干View -> ViewPager -> 若干View -> ParentRecyclerView
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
            } else if (parentView is PersistentCoordinatorLayout) {
                // 碰到ParentRecyclerView，设置结束
                parentView.setInnerViewPager(viewPager)
                parentView.setInnerViewPager2(viewPager2)
                return
            }

            lastTraverseView = parentView
            parentView = parentView.parent as View
        }
    }
}