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
public class LoadingMoreFooter extends LinearLayout {

    public final static int STATE_LOADING = 0;
    public final static int STATE_COMPLETE = 1;
    public final static int STATE_NOMORE = 2;
    private View viewHomeBottom;
    private boolean isFooterMoreHeight = false;
    private LinearLayout llNoMore;
    private LinearLayout llLoading;

    public LoadingMoreFooter(Context context) {
        super(context);
        initView(context);
    }

    public void setFooterMoreHeight(boolean footerMoreHeight) {
        isFooterMoreHeight = footerMoreHeight;
    }

    public LoadingMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.yun_refresh_footer, this);
        viewHomeBottom = findViewById(R.id.view_home_bottom);
        llLoading = (LinearLayout) findViewById(R.id.ll_loading);
        llNoMore = (LinearLayout) findViewById(R.id.ll_no_more);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

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
            case STATE_NOMORE:
                llLoading.setVisibility(GONE);
                llNoMore.setVisibility(VISIBLE);
                this.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        if (isFooterMoreHeight) {
            viewHomeBottom.setVisibility(View.VISIBLE);
        }
    }

    public void reSet() {
        this.setVisibility(GONE);
    }
}
