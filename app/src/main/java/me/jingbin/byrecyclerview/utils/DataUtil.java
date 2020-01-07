package me.jingbin.byrecyclerview.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import me.jingbin.byrecyclerview.activity.AppBarLayoutActivity;
import me.jingbin.byrecyclerview.activity.CustomLayoutActivity;
import me.jingbin.byrecyclerview.activity.DataBindingActivity;
import me.jingbin.byrecyclerview.activity.DividerActivity;
import me.jingbin.byrecyclerview.activity.ItemClickActivity;
import me.jingbin.byrecyclerview.activity.MultiItemActivity;
import me.jingbin.byrecyclerview.activity.MultiTypeItemActivity;
import me.jingbin.byrecyclerview.activity.StateViewActivity;
import me.jingbin.byrecyclerview.activity.HeaderFooterActivity;
import me.jingbin.byrecyclerview.activity.ListViewActivity;
import me.jingbin.byrecyclerview.activity.RefreshActivity;
import me.jingbin.byrecyclerview.activity.SimpleActivity;
import me.jingbin.byrecyclerview.activity.StickyItemActivity;
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
        list.add(new MainItemBean("多类型列表", MultiTypeItemActivity.class));
        list.add(new MainItemBean("使用 自带刷新 / SwipeRefreshLayout", RefreshActivity.class));
        list.add(new MainItemBean("设置HeaderView，FooterView", HeaderFooterActivity.class));
        list.add(new MainItemBean("设置StateView (加载中布局/空布局/错误布局)", StateViewActivity.class));
        list.add(new MainItemBean("设置点击/长按事件 (item或item里的子View)", ItemClickActivity.class));
        list.add(new MainItemBean("在 AppBarLayout + RecyclerView 中使用", AppBarLayoutActivity.class));
        list.add(new MainItemBean("BaseListAdapter的使用", ListViewActivity.class));
        list.add(new MainItemBean("使用DataBinding (RecyclerView / ListView)", DataBindingActivity.class));
        list.add(new MainItemBean("自定义下拉刷新布局 / 加载更多布局", CustomLayoutActivity.class));
        list.add(new MainItemBean("自定义行列间距", DividerActivity.class));
        list.add(new MainItemBean("Item 悬浮置顶", StickyItemActivity.class));
        return list;
    }

    /**
     * 一般item的数据
     */
    public static ArrayList<DataItemBean> getMultiData(Context context, int num) {
        ArrayList<DataItemBean> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            DataItemBean bean = new DataItemBean();
            if (i == 0
                    || i == 2 || i == 4
//                    || i == 6 || i == 8
                    || i == 10 || i == 12
//                    || i == 22 || i == 19
//                    || i == 13 || i == 15
                    || i == 34 || i == 40
                    || i == 17 || i == 30) {
                bean.setType("title");
                bean.setDes("我是标题");
            } else {
                bean.setType("content");
                bean.setDes("我是内容");
            }
            list.add(bean);
        }
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
