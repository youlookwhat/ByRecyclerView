package me.jingbin.byrecyclerview.adapter;

import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.library.adapter.BaseByRecyclerAdapter;
import me.jingbin.library.adapter.BaseByViewHolder;

/**
 * @author jingbin
 */
public class BaaaAdapter extends BaseByRecyclerAdapter<DataItemBean, BaseByViewHolder> {

//    private int mLayoutId;

//    public BaaaAdapter(List<DataItemBean> data) {
//        super(data);
//        mLayoutId = layoutResId;
//    }

    @NonNull
    @Override
    public BaseByViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseByViewHolder(parent, R.layout.layout_empty);
    }

    @Override
    protected void onBindView(BaseByViewHolder holder, DataItemBean bean, int position) {
        TextView view = holder.getView(R.id.tv_refresh_tip);
        view.setText("");
    }
}
