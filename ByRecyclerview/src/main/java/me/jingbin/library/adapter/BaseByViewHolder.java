package me.jingbin.library.adapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author jingbin
 */
public class BaseByViewHolder extends RecyclerView.ViewHolder {

    private final SparseArray<View> views;

    public BaseByViewHolder(ViewGroup viewGroup, int layoutId) {
        super(LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false));
        this.views = new SparseArray<>();
    }

    public BaseByViewHolder(@NonNull View itemView) {
        super(itemView);
        this.views = new SparseArray<>();
    }

//    /**
//     * Called by RecyclerView to display the data at the specified position.
//     *
//     * @param bean     the data of bind
//     * @param position the item position of recyclerView
//     */
//    protected  abstract void onBindView(BaseByViewHolder holder, T bean, int position);

//    void onBaseBindView(T bean, int position) {
//        onBindView(this, bean, position);
//    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }
}
