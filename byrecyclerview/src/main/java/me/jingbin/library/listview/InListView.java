package me.jingbin.library.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @author jingbin
 */
public class InListView extends ListView {
	
	public InListView(Context context) {
		super(context);
	}
	
	public InListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	public InListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
	
}