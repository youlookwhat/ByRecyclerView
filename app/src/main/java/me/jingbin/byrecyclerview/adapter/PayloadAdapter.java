package me.jingbin.byrecyclerview.adapter;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.binding.BaseBindingAdapter;
import me.jingbin.byrecyclerview.binding.BaseBindingHolder;
import me.jingbin.byrecyclerview.databinding.ItemPayloadBinding;
import me.jingbin.byrecyclerview.utils.LogHelper;

/**
 * @author jingbin
 */
public class PayloadAdapter extends BaseBindingAdapter<DataItemBean, ItemPayloadBinding> {

    public final static int PAYLOAD_ZAN = 1;
    public final static int PAYLOAD_COLLECT = 2;

    public PayloadAdapter() {
        super(R.layout.item_payload);
    }

    public PayloadAdapter(List<DataItemBean> data) {
        super(R.layout.item_payload, data);
    }

    @Override
    protected void bindView(@NonNull BaseBindingHolder holder, @NonNull DataItemBean bean, @NonNull ItemPayloadBinding binding, int position) {
        if (position == 0) {
            binding.tvText.setText("将0-3全部置为“已收藏”");
        } else {
            binding.tvText.setText(bean.getTitle() + ": " + position);
        }

        binding.tvZan.setText(bean.getIsZan() == 1 ? "已赞" : "点赞");
        binding.tvCollect.setText(bean.getIsCollect() == 1 ? "已收藏" : "收藏");
        holder.addOnClickListener(R.id.tv_zan);
        holder.addOnClickListener(R.id.tv_collect);
        LogHelper.e("---bindView: " + position);
    }

    @Override
    protected void bindViewPayloads(@NonNull BaseBindingHolder holder, @NonNull DataItemBean bean, @NonNull ItemPayloadBinding binding, int position, @NonNull List<Object> payloads) {
        // 删除掉这一行，自己处理，不走bindView()
        // super.bindViewPayloads(holder, bean, binding, position, payloads);
        LogHelper.e("---bindViewPayloads: " + position);
        for (Object p : payloads) {
            int code = (int) p;
            switch (code) {
                case PAYLOAD_ZAN:
                    binding.tvZan.setText(bean.getIsZan() == 1 ? "已赞" : "点赞");
                    break;
                case PAYLOAD_COLLECT:
                    binding.tvCollect.setText(bean.getIsCollect() == 1 ? "已收藏" : "收藏");
                    break;
                default:
                    break;
            }
        }
    }
}
