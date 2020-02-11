package me.jingbin.library.stick.handle;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

/**
 * Created by jingbin on 2020-02-11.
 */
public class StickyHeaderHandler {

    private static final int INVALID_POSITION = -1;
    public static final int NO_ELEVATION = -1;
    public static final int DEFAULT_ELEVATION = 5;

    private final RecyclerView mRecyclerView;
    private RecyclerView.ViewHolder currentViewHolder;
    private View currentHeader;

    private final boolean checkMargins;

    private List<Integer> mHeaderPositions;

    private int orientation;
    private boolean dirty;

    private int lastBoundPosition = INVALID_POSITION;
    private float headerElevation = NO_ELEVATION;
    private int cachedElevation = NO_ELEVATION;

    private final ViewTreeObserver.OnGlobalLayoutListener visibilityObserver = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            int visibility = StickyHeaderHandler.this.mRecyclerView.getVisibility();
            if (currentHeader != null) {
                currentHeader.setVisibility(visibility);
            }
        }
    };

    public StickyHeaderHandler(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
        checkMargins = recyclerViewHasPadding();
    }

    public void setHeaderPositions(List<Integer> headerPositions) {
        this.mHeaderPositions = headerPositions;
    }

    /**
     * @param firstVisiblePosition 第一个可见item
     * @param visibleHeaders       当前可见的所有header位置
     * @param viewFactory          header视图构造器
     * @param atTop                第0个item完全可见
     */
    public void updateHeaderState(int firstVisiblePosition, Map<Integer, View> visibleHeaders, ViewHolderFactory viewFactory, boolean atTop) {
        int headerPositionToShow = atTop ? INVALID_POSITION : getHeaderPositionToShow(firstVisiblePosition, visibleHeaders.get(firstVisiblePosition));
        View headerToCopy = visibleHeaders.get(headerPositionToShow);
        if (headerPositionToShow != lastBoundPosition) {
            if (headerPositionToShow == INVALID_POSITION || (checkMargins && headerAwayFromEdge(headerToCopy))) {
                // 如果header刚好贴边，就无需加入
                dirty = true;
                safeDetachHeader();
                lastBoundPosition = INVALID_POSITION;
            } else {
                // 否则就创建一个header视图
                lastBoundPosition = headerPositionToShow;
                RecyclerView.ViewHolder viewHolder = viewFactory.getViewHolderForPosition(headerPositionToShow);
                attachHeader(viewHolder, headerPositionToShow);
            }
        } else if (checkMargins && headerAwayFromEdge(headerToCopy)) {
            detachHeader(lastBoundPosition);
            lastBoundPosition = INVALID_POSITION;
        }
        checkHeaderPositions(visibleHeaders);
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                checkElevation();
            }
        });
    }

    private void checkHeaderPositions(final Map<Integer, View> visibleHeaders) {
        if (currentHeader == null) {
            return;
        }
        if (currentHeader.getHeight() == 0) {
            waitForLayoutAndRetry(visibleHeaders);
            return;
        }
        boolean reset = true;
        for (Map.Entry<Integer, View> entry : visibleHeaders.entrySet()) {
            if (entry.getKey() <= lastBoundPosition) {
                continue;
            }
            View nextHeader = entry.getValue();
            reset = offsetHeader(nextHeader) == -1;
            break;
        }
        if (reset) {
            resetTranslation();
        }
        currentHeader.setVisibility(View.VISIBLE);
    }

    public void setElevateHeaders(int dpElevation) {
        if (dpElevation != NO_ELEVATION) {
            cachedElevation = dpElevation;
        } else {
            headerElevation = NO_ELEVATION;
            cachedElevation = NO_ELEVATION;
        }
    }

    public void reset(int orientation) {
        this.orientation = orientation;
        lastBoundPosition = INVALID_POSITION;
        dirty = true;
        safeDetachHeader();
    }

    public void clearHeader() {
        detachHeader(lastBoundPosition);
    }

    public void clearVisibilityObserver() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(visibilityObserver);
        } else {
            mRecyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(visibilityObserver);
        }
    }

    private float offsetHeader(View nextHeader) {
        boolean shouldOffsetHeader = shouldOffsetHeader(nextHeader);
        float offset = -1;
        if (shouldOffsetHeader) {
            if (orientation == LinearLayoutManager.VERTICAL) {
                offset = -(currentHeader.getHeight() - nextHeader.getY());
                currentHeader.setTranslationY(offset);
            } else {
                offset = -(currentHeader.getWidth() - nextHeader.getX());
                currentHeader.setTranslationX(offset);
            }
        }
        return offset;
    }

    private boolean shouldOffsetHeader(View nextHeader) {
        if (orientation == LinearLayoutManager.VERTICAL) {
            return nextHeader.getY() < currentHeader.getHeight();
        } else {
            return nextHeader.getX() < currentHeader.getWidth();
        }
    }

    private void resetTranslation() {
        if (orientation == LinearLayoutManager.VERTICAL) {
            currentHeader.setTranslationY(0);
        } else {
            currentHeader.setTranslationX(0);
        }
    }

    private int getHeaderPositionToShow(int firstVisiblePosition, @Nullable View headerForPosition) {
        int headerPositionToShow = INVALID_POSITION;
        if (headerIsOffset(headerForPosition)) {
            int offsetHeaderIndex = mHeaderPositions.indexOf(firstVisiblePosition);
            if (offsetHeaderIndex > 0) {
                return mHeaderPositions.get(offsetHeaderIndex - 1);
            }
        }
        for (Integer headerPosition : mHeaderPositions) {
            if (headerPosition <= firstVisiblePosition) {
                // 寻找第一个可见的 item 所关联的 header 的位置
                headerPositionToShow = headerPosition;
            } else {
                break;
            }
        }
        return headerPositionToShow;
    }

    private boolean headerIsOffset(View headerForPosition) {
        return headerForPosition != null && (orientation == LinearLayoutManager.VERTICAL ? headerForPosition.getY() > 0 : headerForPosition.getX() > 0);
    }

    private void attachHeader(RecyclerView.ViewHolder viewHolder, int headerPosition) {
        if (currentViewHolder == viewHolder) {
            mRecyclerView.getAdapter().onBindViewHolder(currentViewHolder, headerPosition);
            currentViewHolder.itemView.requestLayout();
            checkTranslation();
            dirty = false;
            return;
        }
        detachHeader(lastBoundPosition);
        this.currentViewHolder = viewHolder;
        mRecyclerView.getAdapter().onBindViewHolder(currentViewHolder, headerPosition);
        this.currentHeader = currentViewHolder.itemView;
        resolveElevationSettings(currentHeader.getContext());
        currentHeader.setVisibility(View.INVISIBLE);
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(visibilityObserver);
        getRecyclerParent().addView(currentHeader);
        if (checkMargins) {
            updateLayoutParams(currentHeader);
        }
        dirty = false;
    }

    private int currentDimension() {
        if (currentHeader == null) {
            return 0;
        }
        if (orientation == LinearLayoutManager.VERTICAL) {
            return currentHeader.getHeight();
        } else {
            return currentHeader.getWidth();
        }
    }

    private boolean headerHasTranslation() {
        if (currentHeader == null) {
            return false;
        }
        if (orientation == LinearLayoutManager.VERTICAL) {
            return currentHeader.getTranslationY() < 0;
        } else {
            return currentHeader.getTranslationX() < 0;
        }
    }

    private void updateTranslation(int diff) {
        if (currentHeader == null) {
            return;
        }
        if (orientation == LinearLayoutManager.VERTICAL) {
            currentHeader.setTranslationY(currentHeader.getTranslationY() + diff);
        } else {
            currentHeader.setTranslationX(currentHeader.getTranslationX() + diff);
        }
    }

    private void checkTranslation() {
        final View view = currentHeader;
        if (view == null) {
            return;
        }
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            int previous = currentDimension();

            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                if (currentHeader == null) {
                    return;
                }

                int newDimen = currentDimension();
                if (headerHasTranslation() && previous != newDimen) {
                    updateTranslation(previous - newDimen);
                }
            }
        });
    }

    private void checkElevation() {
        if (headerElevation != NO_ELEVATION && currentHeader != null) {
            if (orientation == LinearLayoutManager.VERTICAL && currentHeader.getTranslationY() == 0
                    || orientation == LinearLayoutManager.HORIZONTAL && currentHeader.getTranslationX() == 0) {
                elevateHeader();
            } else {
                settleHeader();
            }
        }
    }

    private void elevateHeader() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (currentHeader.getTag() != null) {
                return;
            }
            currentHeader.setTag(true);
            currentHeader.animate().z(headerElevation);
        }
    }

    private void settleHeader() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (currentHeader.getTag() != null) {
                currentHeader.setTag(null);
                currentHeader.animate().z(0);
            }
        }
    }

    private void detachHeader(int position) {
        if (currentHeader != null) {
            getRecyclerParent().removeView(currentHeader);
            clearVisibilityObserver();
            currentHeader = null;
            currentViewHolder = null;
        }
    }

    private void updateLayoutParams(View currentHeader) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) currentHeader.getLayoutParams();
        matchMarginsToPadding(params);
    }

    private void matchMarginsToPadding(ViewGroup.MarginLayoutParams layoutParams) {
        @Px int leftMargin = orientation == LinearLayoutManager.VERTICAL ? mRecyclerView.getPaddingLeft() : 0;
        @Px int topMargin = orientation == LinearLayoutManager.VERTICAL ? 0 : mRecyclerView.getPaddingTop();
        @Px int rightMargin = orientation == LinearLayoutManager.VERTICAL ? mRecyclerView.getPaddingRight() : 0;
        layoutParams.setMargins(leftMargin, topMargin, rightMargin, 0);
    }

    private boolean headerAwayFromEdge(View headerToCopy) {
        return headerToCopy != null && (orientation == LinearLayoutManager.VERTICAL ? headerToCopy.getY() > 0 : headerToCopy.getX() > 0);
    }

    private boolean recyclerViewHasPadding() {
        return mRecyclerView.getPaddingLeft() > 0 || mRecyclerView.getPaddingRight() > 0 || mRecyclerView.getPaddingTop() > 0;
    }

    private ViewGroup getRecyclerParent() {
        return (ViewGroup) mRecyclerView.getParent();
    }

    private void waitForLayoutAndRetry(final Map<Integer, View> visibleHeaders) {
        final View view = currentHeader;
        if (view == null) {
            return;
        }
        view.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                        if (currentHeader == null) {
                            return;
                        }
                        getRecyclerParent().requestLayout();
                        checkHeaderPositions(visibleHeaders);
                    }
                });
    }

    private void safeDetachHeader() {
        final int cachedPosition = lastBoundPosition;
        getRecyclerParent().post(new Runnable() {
            @Override
            public void run() {
                if (dirty) {
                    detachHeader(cachedPosition);
                }
            }
        });
    }

    private void resolveElevationSettings(Context context) {
        if (cachedElevation != NO_ELEVATION && headerElevation == NO_ELEVATION) {
            headerElevation = dp2px(context, cachedElevation);
        }
    }

    public View getCurrentHeader() {
        return currentHeader;
    }

    private float dp2px(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale;
    }
}
