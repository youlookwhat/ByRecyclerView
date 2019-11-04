package me.jingbin.library.divider;

/*
 * Copyright 2019. Bin Jing (https://github.com/youlookwhat)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 可以去除头部尾部divider的ItemDecoration
 *
 * @author jingbin
 * https://github.com/youlookwhat/ByRecyclerView
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;
    private static final String TAG = "itemDivider";
    private Drawable mDivider;
    private final Rect mBounds = new Rect();
    private Context mContext;
    /**
     * 在AppTheme里配置 android:listDivider
     */
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    /**
     * 头部 不显示分割线的item个数
     */
    private int mHeaderNoShowSize = 0;
    /**
     * 尾部 不显示分割线的item格式 默认不显示最后一个item的分割线
     */
    private int mFooterNoShowSize = 1;
    /**
     * Current orientation. Either {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    private int mOrientation;


    public SpacesItemDecoration(Context context, int orientation) {
        this(context, orientation, 0, 1);
    }

    public SpacesItemDecoration(Context context, int orientation, int headerNoShowSize) {
        this(context, orientation, headerNoShowSize, 1);
    }

    /**
     * Creates a divider {@link RecyclerView.ItemDecoration}
     *
     * @param context          Current context, it will be used to access resources.
     * @param orientation      Divider orientation. Should be {@link #HORIZONTAL} or
     *                         {@link #VERTICAL}.
     * @param headerNoShowSize not show header divider size
     * @param footerNoShowSize not show footer divider size
     */
    public SpacesItemDecoration(Context context, int orientation, int headerNoShowSize, int footerNoShowSize) {
        mContext = context;
        mHeaderNoShowSize = headerNoShowSize;
        mFooterNoShowSize = footerNoShowSize;
        setOrientation(orientation);
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        if (mDivider == null) {
            Log.w(TAG, "@android:attr/listDivider was not set in the theme used for this " + "SpacesItemDecoration. Please set that attribute all call setDrawable()");
        }
        a.recycle();
    }

    /**
     * Sets the orientation for this divider. This should be called if
     * {@link RecyclerView.LayoutManager} changes orientation.
     *
     * @param orientation {@link #HORIZONTAL} or {@link #VERTICAL}
     */
    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException("Invalid orientation. It should be either HORIZONTAL or VERTICAL");
        }
        mOrientation = orientation;
    }

    /**
     * Sets the {@link Drawable} for this divider.
     *
     * @param drawable Drawable that should be used as a divider.
     */
    public void setDrawable(Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("drawable cannot be null.");
        }
        mDivider = drawable;
    }

    public void setDrawable(@DrawableRes int id) {
        setDrawable(ContextCompat.getDrawable(mContext, id));
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null || mDivider == null) {
            return;
        }
        if (mOrientation == VERTICAL) {
            drawVertical(canvas, parent, state);
        } else {
            drawHorizontal(canvas, parent, state);
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        canvas.save();
        final int left;
        final int right;
        //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right,
                    parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        final int childCount = parent.getChildCount();
        final int lastPosition = state.getItemCount() - 1;
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final int childRealPosition = parent.getChildAdapterPosition(child);

            // 过滤到头部不显示的分割线
            if (childRealPosition < mHeaderNoShowSize) {
                continue;
            }
            // 过滤到尾部不显示的分割线
            if (childRealPosition <= lastPosition - mFooterNoShowSize) {
                parent.getDecoratedBoundsWithMargins(child, mBounds);
                final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
                final int top = bottom - mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
        }
        canvas.restore();
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        canvas.save();
        final int top;
        final int bottom;
        //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
        if (parent.getClipToPadding()) {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(parent.getPaddingLeft(), top,
                    parent.getWidth() - parent.getPaddingRight(), bottom);
        } else {
            top = 0;
            bottom = parent.getHeight();
        }

        final int childCount = parent.getChildCount();
        final int lastPosition = state.getItemCount() - 1;
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final int childRealPosition = parent.getChildAdapterPosition(child);

            // 过滤到头部不显示的分割线
            if (childRealPosition < mHeaderNoShowSize) {
                continue;
            }
            // 过滤到尾部不显示的分割线
            if (childRealPosition <= lastPosition - mFooterNoShowSize) {
                parent.getDecoratedBoundsWithMargins(child, mBounds);
                final int right = mBounds.right + Math.round(child.getTranslationX());
                final int left = right - mDivider.getIntrinsicWidth();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
        }
        canvas.restore();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mDivider == null) {
            outRect.set(0, 0, 0, 0);
            return;
        }
        if (mOrientation == VERTICAL) {
            //parent.getChildCount() 不能拿到item的总数
            int lastPosition = state.getItemCount() - 1;
            int position = parent.getChildAdapterPosition(view);
            if (position <= lastPosition - mFooterNoShowSize) {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, 0, 0);
            }
        } else {
            int lastPosition = state.getItemCount() - 1;
            int position = parent.getChildAdapterPosition(view);
            if (position <= lastPosition - mFooterNoShowSize) {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            } else {
                outRect.set(0, 0, 0, 0);
            }
        }
    }

    /**
     * 设置不显示分割线的item位置
     *
     * @param headerNoShowSize 头部 不显示分割线的item个数
     * @param footerNoShowSize 尾部 不显示分割线的item个数，默认1，不显示最后一个,最后一个一般为加载更多view
     */
    public void setNoShowDivider(int headerNoShowSize, int footerNoShowSize) {
        this.mHeaderNoShowSize = headerNoShowSize;
        this.mFooterNoShowSize = footerNoShowSize;
    }

    public void setHeaderNoShowDivider(int headerNoShowSize) {
        this.mHeaderNoShowSize = headerNoShowSize;
    }

}
