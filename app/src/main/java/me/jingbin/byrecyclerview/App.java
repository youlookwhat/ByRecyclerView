package me.jingbin.byrecyclerview;


import android.content.Context;

import androidx.multidex.MultiDexApplication;

import me.weishu.reflection.Reflection;


public class App extends MultiDexApplication {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // https://github.com/tiann/FreeReflection，解决[CoordinatorLayout 嵌套滑动置顶(惯性滑动方案)]反射问题
        Reflection.unseal(base);
    }
}
