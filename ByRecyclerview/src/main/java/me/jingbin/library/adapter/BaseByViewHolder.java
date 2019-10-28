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
public abstract class BaseByViewHolder<T> extends RecyclerView.ViewHolder {

    private final SparseArray<View> views;

    public BaseByViewHolder(ViewGroup viewGroup, int layoutId) {
        this(LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false));
    }

    public BaseByViewHolder(@NonNull View itemView) {
        super(itemView);
        this.views = new SparseArray<>();
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param bean     the data of bind
     * @param position the item position of recyclerView
     */
    protected abstract void onBaseBindView(BaseByViewHolder<T> holder, T bean, int position);

    @SuppressWarnings("unchecked")
    public <V extends View> V getView(@IdRes int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (V) view;
    }
}
