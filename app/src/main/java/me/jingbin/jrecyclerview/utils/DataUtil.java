package me.jingbin.jrecyclerview.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import me.jingbin.jrecyclerview.activity.SimpleActivity;
import me.jingbin.jrecyclerview.bean.MainItemBean;

/**
 * 生成数据工具类
 *
 * @author jingbin
 */
public class DataUtil {

    /**
     * 首页的数据
     */
    public static List<MainItemBean> getMainActivityList(Context context) {
        ArrayList<MainItemBean> list = new ArrayList<>();
        list.add(new MainItemBean("基本使用", SimpleActivity.class));
        list.add(new MainItemBean("基本使用", SimpleActivity.class));
        list.add(new MainItemBean("基本使用", SimpleActivity.class));
        list.add(new MainItemBean("基本使用", SimpleActivity.class));
        list.add(new MainItemBean("基本使用", SimpleActivity.class));
        list.add(new MainItemBean("基本使用", SimpleActivity.class));
        list.add(new MainItemBean("基本使用", SimpleActivity.class));
        list.add(new MainItemBean("基本使用", SimpleActivity.class));
        list.add(new MainItemBean("基本使用", SimpleActivity.class));
        list.add(new MainItemBean("基本使用", SimpleActivity.class));
        return list;
    }

}
