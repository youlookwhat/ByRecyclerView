package me.jingbin.byrecyclerview.stickrvcool.config

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import me.jingbin.byrecyclerview.R
import me.jingbin.byrecyclerview.adapter.FeedStaggerAdapter
import me.jingbin.byrecyclerview.stickrvcool.PersistentRecyclerView
import me.jingbin.byrecyclerview.stickyrv.config.PersistentStaggeredGridLayoutManager
import me.jingbin.byrecyclerview.utils.DataUtil
import me.jingbin.byrecyclerview.utils.DensityUtil
import me.jingbin.library.decoration.GridSpaceItemDecoration
import me.jingbin.library.view.OnItemFilterClickListener

class FeedsListFragment : Fragment(R.layout.fragment_feeds_list) {

    private var page: Int = 1
    private var recyclerView: PersistentRecyclerView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.recyclerView = view.findViewById<PersistentRecyclerView>(R.id.recycler_view)
        val layoutManager = PersistentStaggeredGridLayoutManager(2)
        this.recyclerView!!.layoutManager = layoutManager
        recyclerView!!.addItemDecoration(GridSpaceItemDecoration(DensityUtil.dip2px(context, 8f)))
        val feedStaggerAdapter = FeedStaggerAdapter(DataUtil.getStickyData())
        this.recyclerView!!.adapter = feedStaggerAdapter
        // 处理有viewpager2时的刷新兼容性问题
        recyclerView!!.setDispatchTouch(true)
        recyclerView!!.setOnItemClickListener(object : OnItemFilterClickListener() {
            override fun onSingleClick(v: View, position: Int) {
                if ("点我回到顶部" == feedStaggerAdapter.getItemData(position).title) {
                    recyclerView!!.scrollToPosition(0)
                }
            }
        })
        recyclerView!!.setOnLoadMoreListener(true, 1, {
            if (page == 2) {
                recyclerView!!.loadMoreEnd()
                return@setOnLoadMoreListener
            }
            page = 2
            feedStaggerAdapter.addData(DataUtil.getStickyData())
            recyclerView!!.loadMoreComplete()
        }, 1000)
    }
}