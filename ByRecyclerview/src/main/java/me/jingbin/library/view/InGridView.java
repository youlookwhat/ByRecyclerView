package me.jingbin.library.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * item布局 不可使用ConstraintLayout，会有适配问题
 *
 * @author jingbin
 */
public class InGridView extends GridView {

    public InGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public InGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InGridView(Context context) {
        super(context);
    }

    /**
     * 设置不滚动
     * 其中onMeasure函数决定了组件显示的高度与宽度；
     * makeMeasureSpec函数中第一个函数决定布局空间的大小，第二个参数是布局模式
     * MeasureSpec.AT_MOST的意思就是子控件需要多大的控件就扩展到多大的空间
     * 同样的道理，ListView也适用
     */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}