package me.jingbin.library.config;

import android.util.Log;

public final class LogHelper {
    private static volatile boolean DEBUG = true;
    public static String logTag = "jingbin";

    private LogHelper() {
    }

    /**
     * 打开Log
     *
     */
    public static void enableLogging() {
        DEBUG = true;
    }

    /**
     * 关闭Log
     *
     * @author mslan
     * @date 2015年3月1日 下午5:14:50
     */
    public static void disableLogging() {
        DEBUG = false;
    }

    public static void v(String tag, String message) {
        if (DEBUG) {
            Log.v(tag, message);
        }
    }

    public static void d(String tag, String message) {
        if (DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void i(String tag, String message) {
        if (DEBUG) {
            Log.i(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if (DEBUG) {
            Log.w(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (DEBUG) {
            Log.e(tag, message);
        }
    }

    public static void e(String tag, float message) {
        if (DEBUG) {
            Log.e(tag, String.valueOf(message));
        }
    }

    public static void e(String message) {
        if (DEBUG) {
            Log.e(logTag, message);
        }
    }
}
