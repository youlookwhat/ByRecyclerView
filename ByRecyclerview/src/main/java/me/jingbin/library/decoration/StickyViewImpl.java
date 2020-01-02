package me.jingbin.library.decoration;

import android.view.View;

import me.jingbin.library.R;


/**
 * @author jingbin
 */
public class StickyViewImpl implements StickyView {

    @Override
    public boolean isStickyView(View view) {
        return view.getId() == R.id.id_by_sticky_item;
    }

    @Override
    public int getStickViewType() {
        return StickyView.TYPE_STICKY_VIEW;
    }
}
