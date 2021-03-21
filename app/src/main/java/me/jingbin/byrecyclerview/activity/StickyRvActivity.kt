package me.jingbin.byrecyclerview.activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import me.jingbin.byrecyclerview.R
import me.jingbin.byrecyclerview.app.BaseActivity
import me.jingbin.byrecyclerview.databinding.ActivityStickyRvBinding
import me.jingbin.byrecyclerview.stickyrv.config.StickAdapter
import me.jingbin.byrecyclerview.utils.DensityUtil

/**
 * @author jingbin
 */
class StickyRvActivity : BaseActivity<ActivityStickyRvBinding>() {

    // 广告是否关闭
    private var floatAdClosed = false
    private var stickAdapter: StickAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sticky_rv)

        title = "RecyclerView 嵌套滑动置顶"

        stickAdapter = StickAdapter(this)

        binding.parentRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.parentRecyclerView.adapter = stickAdapter
        binding.parentRecyclerView.setOnRefreshListener({ binding.parentRecyclerView.isRefreshing = false }, 1000)

        binding.parentRecyclerView.addHeaderView(R.layout.layout_skeleton_headerview)
        binding.parentRecyclerView.addHeaderView(R.layout.layout_sticky_headerview)
        binding.parentRecyclerView.addHeaderView(R.layout.layout_sticky_headerview2)

        stickAdapter!!.setLoadingTabsListener {
            // 800毫秒后加载信息流数据
            binding.homeFloatLayout.postDelayed({ stickAdapter?.onTabsLoaded() }, 500L)
        }

        binding.parentRecyclerView.setStickyListener {
            if (!floatAdClosed) {
                if (it) {
                    // 置顶了
                    binding.homeFloatLayout.visibility = View.VISIBLE
                } else {
                    // 没有置顶
                    binding.homeFloatLayout.visibility = View.GONE
                }
            }
        }
        if (!floatAdClosed) {
            // 设置悬浮布局的高度
            binding.parentRecyclerView.setStickyHeight(DensityUtil.dip2px(this, 50f))
        }
        binding.tvText.setOnClickListener {
            binding.homeFloatLayout.visibility = View.GONE
            floatAdClosed = true
            binding.parentRecyclerView.setStickyHeight(0)
        }
    }

}
