package me.jingbin.byrecyclerview.utils;

import android.app.Activity;
import android.view.View;

import com.gyf.barlibrary.ImmersionBar;

import me.jingbin.byrecyclerview.R;


/**
 * @author jingbin
 * @data 2019-06-07
 * @description 属性解释:
 * transparentStatusBar   透明状态栏
 * transparentBar         透明状态栏和导航栏
 * statusBarDarkFont      状态栏是否显示黑色文字，如不支持则状态栏透明度设置为 statusAlpha
 * navigationBarEnable    设置导航栏是否处理，false不处理，则显示系统的导航栏样式
 * navigationBarDarkIcon  设置导航栏图标颜色是否深色，不受navigationBarEnable影响
 * navigationBarColor     设置导航栏颜色
 * setTitleBar(activity, view)：         给view设置一个状态栏高度的paddingTop
 * setTitleBarMarginTop(activity, view)：给view设置一个状态栏高度的marginTop
 */
public class StatusBarUtil {

    /**
     * 状态栏透明，文字为白色，用于状态栏渐变
     *
     * @param view 需要渐变的布局，背景为渐变背景
     */
    public static void showTransparentStatusBarPadding(Activity activity, View view) {
        // 状态栏透明、文字为白色
        showTransparentStatusBar(activity, false);
        // 增加了一个状态栏高度的padding
        ImmersionBar.setTitleBar(activity, view);
    }

    /**
     * Margin & 状态栏透明
     *
     * @param isDarkFont 状态栏文字是否为黑色
     */
    public static void showTransparentStatusMargin(Activity activity, boolean isDarkFont, View view) {
        showTransparentStatusBar(activity, isDarkFont);
        ImmersionBar.setTitleBarMarginTop(activity, view);
    }

    /**
     * Margin & 状态栏透明
     *
     * @param isDarkFont 状态栏文字是否为黑色
     */
    public static void showMainColorStatusMargin(Activity activity, boolean isDarkFont, View view) {
        showMainColorStatusBar(activity, isDarkFont);
        ImmersionBar.setTitleBarMarginTop(activity, view);
    }

    /**
     * 设置activity状态栏文字的黑白色
     *
     * @param isDarkFont 是否为黑色
     */
    public static void showStatusBarDarkFont(Activity activity, boolean isDarkFont) {
        ImmersionBar.with(activity).statusBarDarkFont(isDarkFont, 0.2f).init();
    }


    /**
     * Margin & 状态栏和导航栏均透明
     *
     * @param isDarkFont 状态栏文字是否为黑色
     */
    public static void showTransparentBarMargin(Activity activity, boolean isDarkFont, View view) {
        showTransparentBar(activity, isDarkFont);
        ImmersionBar.setTitleBarMarginTop(activity, view);
    }

    /**
     * 状态栏和导航栏均透明
     *
     * @param isDarkFont 状态栏文字是否为黑色
     */
    public static void showTransparentBar(Activity activity, boolean isDarkFont) {
        ImmersionBar.with(activity)
                .transparentBar()
                .navigationBarDarkIcon(isDarkFont)
                .init();
    }

    /**
     * 状态栏透明、导航栏白色
     *
     * @param isDarkFont 状态栏文字是否为黑色
     */
    public static void showTransparentStatusBar(Activity activity, boolean isDarkFont) {
        ImmersionBar.with(activity)
                .transparentStatusBar()
                .navigationBarEnable(false)
                .keyboardEnable(true)// 键盘弹起
                .statusBarDarkFont(isDarkFont, 0.2f)
                .init();
    }

    public static void showMainColorStatusBar(Activity activity, boolean isDarkFont) {
        ImmersionBar.with(activity)
                .statusBarColor(R.color.colorPrimary)
                .navigationBarEnable(false)
                .keyboardEnable(true)// 键盘弹起
                .statusBarDarkFont(isDarkFont, 0.2f)
                .init();
    }
}
