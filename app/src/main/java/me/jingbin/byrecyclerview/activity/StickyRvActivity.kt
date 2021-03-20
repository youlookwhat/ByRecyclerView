package me.jingbin.byrecyclerview.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import me.jingbin.byrecyclerview.R
import me.jingbin.byrecyclerview.app.BaseActivity
import me.jingbin.byrecyclerview.databinding.ActivityStickyRvBinding
import me.jingbin.byrecyclerview.databinding.LayoutHeaderViewBinding
import me.jingbin.byrecyclerview.stickyrv.config.StickAdapter

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

//        setHeaderView()

        binding.parentRecyclerView.addHeaderView(R.layout.layout_header_view)
        binding.parentRecyclerView.addHeaderView(R.layout.layout_header_view)
        binding.parentRecyclerView.addHeaderView(R.layout.layout_header_view)
        binding.parentRecyclerView.addHeaderView(R.layout.layout_header_view)
        binding.parentRecyclerView.addHeaderView(R.layout.layout_header_view)
        binding.parentRecyclerView.addHeaderView(R.layout.layout_header_view)

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
            binding.parentRecyclerView.setStickyHeight(dp2px(50F).toInt())
        }
        binding.tvText.setOnClickListener {
            binding.homeFloatLayout.visibility = View.GONE
            floatAdClosed = true
            binding.parentRecyclerView.setStickyHeight(0)
        }
    }

    private fun setHeaderView() {
        val headerBinding: LayoutHeaderViewBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.layout_header_view, binding.parentRecyclerView.parent as ViewGroup?, false)
        binding.parentRecyclerView.addHeaderView(headerBinding.root)
    }

    /**
     * dp转换成px
     */
    fun Context.dp2px(dpValue: Float): Float {
        var scale = resources.displayMetrics.density
        return dpValue * scale + 0.5f
    }
}
