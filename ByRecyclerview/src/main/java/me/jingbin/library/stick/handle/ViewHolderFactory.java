package me.jingbin.library.stick.handle;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by jingbin on 2020-02-11.
 */
public class ViewHolderFactory {

    private final RecyclerView recyclerView;

    private RecyclerView.ViewHolder currentViewHolder;

    private int currentViewType;

    public ViewHolderFactory(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.currentViewType = -1;
    }

    public RecyclerView.ViewHolder getViewHolderForPosition(int position) {
        if (currentViewType != recyclerView.getAdapter().getItemViewType(position)) {
            currentViewType = recyclerView.getAdapter().getItemViewType(position);
            currentViewHolder = recyclerView.getAdapter().createViewHolder((ViewGroup) recyclerView.getParent(), currentViewType);
        }
        return currentViewHolder;
    }
}
