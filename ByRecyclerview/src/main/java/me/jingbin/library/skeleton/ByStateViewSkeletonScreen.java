package me.jingbin.library.skeleton;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.ColorRes;
import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.core.content.ContextCompat;

import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.R;

/**
 * Created by jingbin on 2020/03/07.
 */

public class ByStateViewSkeletonScreen implements SkeletonScreen {

    private static final String TAG = ByStateViewSkeletonScreen.class.getName();
    private final ByRecyclerView mByRecyclerView;
    private final int mSkeletonResID;
    private final int mShimmerColor;
    private final boolean mShimmer;
    private final int mShimmerDuration;
    private final int mShimmerAngle;
    private boolean loadMoreEnabled;
    private boolean refreshEnabled;

    private ByStateViewSkeletonScreen(Builder builder) {
        mByRecyclerView = builder.mByRecyclerView;
        mSkeletonResID = builder.mSkeletonLayoutResID;
        mShimmer = builder.mShimmer;
        mShimmerDuration = builder.mShimmerDuration;
        mShimmerAngle = builder.mShimmerAngle;
        mShimmerColor = builder.mShimmerColor;
    }

    private ShimmerLayout generateShimmerContainerLayout(ViewGroup parentView) {
        final ShimmerLayout shimmerLayout = (ShimmerLayout) LayoutInflater.from(mByRecyclerView.getContext()).inflate(R.layout.layout_by_skeleton_shimmer, parentView, false);
        shimmerLayout.setShimmerColor(mShimmerColor);
        shimmerLayout.setShimmerAngle(mShimmerAngle);
        shimmerLayout.setShimmerAnimationDuration(mShimmerDuration);
        View innerView = LayoutInflater.from(mByRecyclerView.getContext()).inflate(mSkeletonResID, shimmerLayout, false);
        ViewGroup.LayoutParams lp = innerView.getLayoutParams();
        if (lp != null) {
            shimmerLayout.setLayoutParams(lp);
        }
        shimmerLayout.addView(innerView);
        shimmerLayout.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                shimmerLayout.startShimmerAnimation();
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                shimmerLayout.stopShimmerAnimation();
            }
        });
        shimmerLayout.startShimmerAnimation();
        return shimmerLayout;
    }

    private View generateSkeletonLoadingView() {
        ViewParent viewParent = mByRecyclerView.getParent();
        if (viewParent == null) {
            Log.e(TAG, "the source view have not attach to any view");
            return null;
        }
        ViewGroup parentView = (ViewGroup) viewParent;
        if (mShimmer) {
            return generateShimmerContainerLayout(parentView);
        }
        return LayoutInflater.from(mByRecyclerView.getContext()).inflate(mSkeletonResID, parentView, false);
    }

    @Override
    public void show() {
        loadMoreEnabled = mByRecyclerView.isLoadMoreEnabled();
        refreshEnabled = mByRecyclerView.isRefreshEnabled();

        mByRecyclerView.setRefreshEnabled(false);
        mByRecyclerView.setLoadMoreEnabled(false);
        mByRecyclerView.setStateView(generateSkeletonLoadingView());
    }

    @Override
    public void hide() {
        mByRecyclerView.setStateViewEnabled(false);
        mByRecyclerView.setLoadMoreEnabled(loadMoreEnabled);
        mByRecyclerView.setRefreshEnabled(refreshEnabled);
    }


    public static class Builder {
        private final ByRecyclerView mByRecyclerView;
        private int mSkeletonLayoutResID;
        private boolean mShimmer = true;
        private int mShimmerColor;
        private int mShimmerDuration = 1000;
        private int mShimmerAngle = 20;

        public Builder(ByRecyclerView byRecyclerView) {
            this.mByRecyclerView = byRecyclerView;
            this.mShimmerColor = ContextCompat.getColor(mByRecyclerView.getContext(), R.color.by_skeleton_shimmer_color);
        }

        /**
         * @param skeletonLayoutResID the loading skeleton layoutResID
         */
        public Builder load(@LayoutRes int skeletonLayoutResID) {
            this.mSkeletonLayoutResID = skeletonLayoutResID;
            return this;
        }

        /**
         * @param shimmerColor the shimmer color
         */
        public Builder color(@ColorRes int shimmerColor) {
            this.mShimmerColor = ContextCompat.getColor(mByRecyclerView.getContext(), shimmerColor);
            return this;
        }

        /**
         * @param shimmer whether show shimmer animation
         */
        public Builder shimmer(boolean shimmer) {
            this.mShimmer = shimmer;
            return this;
        }

        /**
         * the duration of the animation , the time it will take for the highlight to move from one end of the layout
         * to the other.
         *
         * @param shimmerDuration Duration of the shimmer animation, in milliseconds
         */
        public Builder duration(int shimmerDuration) {
            this.mShimmerDuration = shimmerDuration;
            return this;
        }

        /**
         * @param shimmerAngle the angle of the shimmer effect in clockwise direction in degrees.
         */
        public Builder angle(@IntRange(from = 0, to = 30) int shimmerAngle) {
            this.mShimmerAngle = shimmerAngle;
            return this;
        }

        public ByStateViewSkeletonScreen show() {
            ByStateViewSkeletonScreen skeletonScreen = new ByStateViewSkeletonScreen(this);
            skeletonScreen.show();
            return skeletonScreen;
        }

    }
}
