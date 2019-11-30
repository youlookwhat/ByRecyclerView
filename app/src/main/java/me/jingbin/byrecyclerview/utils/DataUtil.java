package me.jingbin.byrecyclerview.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import me.jingbin.byrecyclerview.activity.AppBarLayoutActivity;
import me.jingbin.byrecyclerview.activity.CustomLayoutActivity;
import me.jingbin.byrecyclerview.activity.DataBindingActivity;
import me.jingbin.byrecyclerview.activity.DividerActivity;
import me.jingbin.byrecyclerview.activity.StateViewActivity;
import me.jingbin.byrecyclerview.activity.HeaderFooterActivity;
import me.jingbin.byrecyclerview.activity.ListViewActivity;
import me.jingbin.byrecyclerview.activity.RefreshActivity;
import me.jingbin.byrecyclerview.activity.SimpleActivity;
import me.jingbin.byrecyclerview.bean.DataItemBean;
import me.jingbin.byrecyclerview.bean.MainItemBean;

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
        list.add(new MainItemBean("使用 自带刷新 / SwipeRefreshLayout", RefreshActivity.class));
        list.add(new MainItemBean("添加HeaderView，FooterView", HeaderFooterActivity.class));
        list.add(new MainItemBean("设置StateView(加载中布局/空布局/错误布局)", StateViewActivity.class));
        list.add(new MainItemBean("在 AppBarLayout + RecyclerView 中使用", AppBarLayoutActivity.class));
        list.add(new MainItemBean("BaseListAdapter的使用", ListViewActivity.class));
        list.add(new MainItemBean("使用DataBinding (RecyclerView / ListView)", DataBindingActivity.class));
        list.add(new MainItemBean("自定义下拉刷新布局 / 加载更多布局", CustomLayoutActivity.class));
        list.add(new MainItemBean("自定义行列间距", DividerActivity.class));
        return list;
    }

    /**
     * 一般item的数据
     */
    public static ArrayList<DataItemBean> get(Context context, int num) {
        ArrayList<DataItemBean> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            DataItemBean bean = new DataItemBean();
            bean.setTitle("数据展示");
            list.add(bean);
        }
        return list;
    }

    /**
     * 下一页数据 默认page至少 = 1
     */
    public static ArrayList<DataItemBean> getMore(Context context, int num, int page) {
        ArrayList<DataItemBean> list = new ArrayList<>();
        for (int i = num * (page - 1); i < num * (page); i++) {
            DataItemBean bean = new DataItemBean();
            bean.setTitle("数据展示");
            list.add(bean);
        }
        return list;
    }

}
