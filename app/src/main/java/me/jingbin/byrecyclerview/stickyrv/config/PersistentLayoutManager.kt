package me.jingbin.byrecyclerview.stickyrv.config

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PersistentLayoutManager(context: Context) : LinearLayoutManager( context) {

    /**
     * ViewPager2左右切换时，会触发RecyclerView.onDetachedFromWindow，进而触发LayoutManager的这个方法，导致错乱
     * 故此处禁用onDetachedFromWindow，保存好数据
     */
    override fun onDetachedFromWindow(view: RecyclerView?, recycler: RecyclerView.Recycler?) {
    }
}