package me.jingbin.library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author jingbin
 */
public class SimpleLoadMoreView extends LinearLayout implements BaseLoadMore {

    private View viewBottom;
    private boolean isShowLoadingMoreHeight = false;
    private TextView tvNoMore;
    private TextView tvMoreFailed;
    private LinearLayout llMoreLoading;

    public SimpleLoadMoreView(Context context) {
        super(context);
        initView(context);
    }

    public void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.simple_by_load_more_view, this);
        viewBottom = findViewById(R.id.view_bottom);
        llMoreLoading = findViewById(R.id.ll_more_loading);
        tvNoMore = findViewById(R.id.tv_no_more);
        tvMoreFailed = findViewById(R.id.tv_more_failed);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void setState(int state) {
        this.setVisibility(View.VISIBLE);

        switch (state) {
            case STATE_LOADING:
                llMoreLoading.setVisibility(VISIBLE);
                tvNoMore.setVisibility(GONE);
                tvMoreFailed.setVisibility(GONE);
                break;
            case STATE_COMPLETE:
                llMoreLoading.setVisibility(VISIBLE);
                tvNoMore.setVisibility(GONE);
                tvMoreFailed.setVisibility(GONE);
                this.setVisibility(View.GONE);
                break;
            case STATE_NO_MORE:
                tvNoMore.setVisibility(VISIBLE);
                llMoreLoading.setVisibility(GONE);
                tvMoreFailed.setVisibility(GONE);
                break;
            case STATE_FAILURE:
                tvMoreFailed.setVisibility(VISIBLE);
                llMoreLoading.setVisibility(GONE);
                tvNoMore.setVisibility(GONE);
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
     * 得到失败的布局，给设置点击重试
     */
    @Override
    public View getFailureView() {
        return tvMoreFailed;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
