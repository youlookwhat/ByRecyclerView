package me.jingbin.jrecyclerview.utils;

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
    public static List<MainItemBean> getMainActivityList() {
        ArrayList<MainItemBean> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            MainItemBean itemBean = new MainItemBean();
            itemBean.setTitle("基本使用");
            itemBean.setCls(SimpleActivity.class);
            list.add(itemBean);
        }
        return list;
    }

}
