package me.jingbin.jrecyclerview.utils;

import android.widget.Toast;

import me.jingbin.jrecyclerview.App;

/**
 * @author jingbin
 */
public class ToastUtil {

    public static void showToast(String text) {
        Toast.makeText(App.getContext(), text, Toast.LENGTH_SHORT).show();
    }
}
