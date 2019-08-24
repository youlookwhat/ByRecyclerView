package me.jingbin.jrecyclerview;


import android.content.Context;

import androidx.multidex.MultiDexApplication;


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
}
