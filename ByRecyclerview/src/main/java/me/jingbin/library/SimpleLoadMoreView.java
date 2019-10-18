package me.jingbin.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * @author jingbin
 */
public class SimpleLoadMoreView extends LinearLayout implements BaseLoadMore {

    private View viewBottom;
    private boolean isShowLoadingMoreHeight = false;
    private LinearLayout llNoMore;
    private LinearLayout llLoading;

    public SimpleLoadMoreView(Context context) {
        this(context, null);
    }

    public SimpleLoadMoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleLoadMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.simple_by_load_more_view, this);
        viewBottom = findViewById(R.id.view_bottom);
        llLoading = findViewById(R.id.ll_loading);
        llNoMore = findViewById(R.id.ll_no_more);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void setState(int state) {
        switch (state) {
            case STATE_LOADING:
                llLoading.setVisibility(VISIBLE);
                llNoMore.setVisibility(GONE);
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_COMPLETE:
                llLoading.setVisibility(VISIBLE);
                llNoMore.setVisibility(GONE);
                this.setVisibility(View.GONE);
                break;
            case STATE_NO_MORE:
                llLoading.setVisibility(GONE);
                llNoMore.setVisibility(VISIBLE);
                this.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        if (isShowLoadingMoreHeight) {
            viewBottom.setVisibility(View.VISIBLE);
        } else {
            viewBottom.setVisibility(View.GONE);
        }
    }

    /**
     * 为了部分页面底部实现透明效果，这里提高一个底部栏的高度
     * 如果没有可不用理会
     */
    @Override
    public void setLoadingMoreBottomHeight(float heightDp) {
        if (heightDp > 0) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dip2px(heightDp));
            viewBottom.setLayoutParams(lp);
            isShowLoadingMoreHeight = true;
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
