package me.jingbin.byrecyclerview.utils;

import android.view.View;

import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;

/**
 * @author jingbin
 */
public class TabLayoutUtil {

    public static void setTabClick(TabLayout tabLayout) {
        setTabClick(tabLayout, null);
    }

    /**
     * 点击动画
     */
    public static void setTabClick(final TabLayout tabLayout, final OnTabClickListener listener) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab == null) {
                return;
            }
            Class c = tab.getClass();
            try {
                Field field = c.getDeclaredField("view");
                field.setAccessible(true);
                final View view = (View) field.get(tab);
                if (view == null) {
                    return;
                }
                // 加了以后点击没水波纹效果
//                view.setBackground(new ProxyDrawable(view));
                view.setTag(i);
                final int finalI = i;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (int) view.getTag();
                        if (listener != null) {
                            listener.onTabClick(position);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnTabClickListener {
        void onTabClick(int position);
    }

}
