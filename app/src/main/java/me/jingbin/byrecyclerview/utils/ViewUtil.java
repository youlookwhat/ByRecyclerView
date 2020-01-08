package me.jingbin.byrecyclerview.utils;

import android.view.View;
import android.view.ViewGroup;

public class ViewUtil {

    public static void setHeight(View view, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height;
    }
}
