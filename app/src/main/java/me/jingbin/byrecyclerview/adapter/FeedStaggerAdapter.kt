package me.jingbin.byrecyclerview.adapter

import android.util.Log
import me.jingbin.byrecyclerview.R
import me.jingbin.byrecyclerview.bean.DataItemBean
import me.jingbin.byrecyclerview.binding.BaseBindingAdapter
import me.jingbin.byrecyclerview.binding.BaseBindingHolder
import me.jingbin.byrecyclerview.databinding.ItemFeedGridBinding
import me.jingbin.byrecyclerview.utils.DensityUtil
import me.jingbin.byrecyclerview.utils.ViewUtil
import java.util.ArrayList

/**
 * Created by jingbin on 3/21/21.
 */
class FeedStaggerAdapter(list: ArrayList<DataItemBean>) : BaseBindingAdapter<DataItemBean, ItemFeedGridBinding>(R.layout.item_feed_grid, list) {

    override fun bindView(holder: BaseBindingHolder<*, *>, bean: DataItemBean, binding: ItemFeedGridBinding, position: Int) {
        binding.tvText.text = bean.title
        ViewUtil.setHeight(binding.ivImage, DensityUtil.dip2px(binding.tvText.context, 190f))
        Log.e("----position", position.toString()+"")
    }

}