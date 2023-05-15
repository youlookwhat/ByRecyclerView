package me.jingbin.byrecyclerview.adapter;

import android.view.Gravity;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.binding.BaseBindingHolder;
import me.jingbin.byrecyclerview.databinding.ItemBadgeBinding;
import me.jingbin.library.adapter.BaseByRecyclerViewAdapter;
import me.jingbin.library.adapter.BaseByViewHolder;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * @author jingbin
 */
public class BadgeAdapter extends BaseByRecyclerViewAdapter<DataItemBean, BaseByViewHolder<DataItemBean>> {

    @NonNull
    @Override
    public BaseByViewHolder<DataItemBean> onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new BadgeViewHolder(viewGroup, R.layout.item_badge);
    }

    private class BadgeViewHolder extends BaseBindingHolder<DataItemBean, ItemBadgeBinding> {

        private Badge badge;

        public BadgeViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
            badge = new QBadgeView(viewGroup.getContext()).bindTarget(binding.clItemHome);
            badge.setBadgeGravity(Gravity.CENTER_VERTICAL | Gravity.END);
            badge.setGravityOffset(40f, true);
            badge.setShowShadow(true);
        }

        @Override
        protected void onBindingView(BaseBindingHolder holder, DataItemBean bean, int position) {
            if (position == 0) {
                badge.setBadgeNumber(3);
            } else {
                badge.setBadgeNumber(0);
            }
        }

    }
}
