package me.jingbin.byrecyclerview.stickyrv.config

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import me.jingbin.byrecyclerview.R
import me.jingbin.byrecyclerview.adapter.FeedStaggerAdapter
import me.jingbin.byrecyclerview.stickyrv.ChildRecyclerView
import me.jingbin.byrecyclerview.utils.DataUtil
import me.jingbin.byrecyclerview.utils.DensityUtil
import me.jingbin.library.decoration.GridSpaceItemDecoration
import me.jingbin.library.view.OnItemFilterClickListener

/**
 * Created by jingbin on 3/21/21.
 */
class FeedsFragment : Fragment() {

    var page: Int = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(R.layout.stickyrv_fragment_feeds_list, null)
        val layoutManager = PersistentStaggeredGridLayoutManager(2)
        if (rootView is ChildRecyclerView) {
            val childRecyclerView = rootView
            childRecyclerView.layoutManager = layoutManager
            val feedStaggerAdapter = FeedStaggerAdapter(DataUtil.getStickyData())
            childRecyclerView.addItemDecoration(GridSpaceItemDecoration(DensityUtil.dip2px(context,8f)))
            childRecyclerView.adapter = feedStaggerAdapter
            childRecyclerView.setOnItemClickListener(object : OnItemFilterClickListener() {
                override fun onSingleClick(v: View, position: Int) {
                    if ("点我回到顶部" == feedStaggerAdapter.getItemData(position).title) {
                        childRecyclerView.scrollToPosition(0)
                    }
                }
            })
            childRecyclerView.setOnLoadMoreListener({
                if (page == 2) {
                    childRecyclerView.loadMoreEnd()
                    return@setOnLoadMoreListener
                }
                page = 2
                feedStaggerAdapter.addData(DataUtil.getStickyData())
                childRecyclerView.loadMoreComplete()
            }, 1000)
        }
        return rootView
    }
}