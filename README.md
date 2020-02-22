# ByRecyclerView

[![jitpack][1]][2] 
[![Apache License 2.0][3]][4]
[![API][5]][6]

ByRecyclerView 是基于RecyclerView的扩展，提供了上拉刷新、加载更多、 添加HeaderView/FooterView、setStateView、item点击/长按监听、万能分割线、粘性header、极简Adapter(databinding)等功能，有效的解决了XRecyclerView和BRVAH其中的问题。

## 功能特性
 - 1.支持 下拉刷新、加载更多
 - 2.可随意切换 自带下拉刷新布局 / SwipeRefreshLayout
 - 3.加载更多机制：**不足一屏上拉加载，超过后触底加载(所见即所得)**
 - 4.可设置自定义 下拉刷新布局 和 加载更多布局
 - 5.添加/移除 HeaderView、FooterView
 - 6.设置空布局 EmptyView
 - 7.添加item及子view的点击/长按事件
 - 8.优化过的BaseAdapter (RecyclerView / ListView)，减少大量代码
 - 9.Adapter结合DataBinding使用 (RecyclerView / ListView)
 - 10.可添加万能分隔线（LinearLayout / GridLayout / StaggeredGridLayout）
 - 11.可配置粘性header，StickyView
 - 12.默认使用AndoridX，且支持Support


## Document
 -  👉 [**详细使用见Wiki！！！**](https://github.com/youlookwhat/ByRecyclerView/wiki)

 - [项目介绍](https://github.com/youlookwhat/ByRecyclerView/wiki/%E9%A1%B9%E7%9B%AE%E4%BB%8B%E7%BB%8D) | [更新日志](https://github.com/youlookwhat/ByRecyclerView/wiki/Update-log)
 - [ByRecyclerView：只为改变BRVAH加载更多机制/addHeaderView的问题](https://juejin.im/post/5e0980fbe51d4558083345fc)


## Screenshots
|刷新操作|设置状态布局|多类型列表|万能分割线|
|:--:|:--:|:--:|:--:|
|![刷新操作](https://github.com/youlookwhat/ByRecyclerView/blob/master/art/gif_refresh.gif?raw=true)|![设置状态布局](https://github.com/youlookwhat/ByRecyclerView/blob/master/art/gif_statue.gif?raw=true)|![多类型列表](https://github.com/youlookwhat/ByRecyclerView/blob/master/art/gif_adapter_type.gif?raw=true")|![分割线](https://github.com/youlookwhat/ByRecyclerView/blob/master/art/gif_divider.gif?raw=true")|


### 下载试用
|[AndroidX版本(Apk-Demo)](https://fir.im/byrecyclerview)|[Support版本(CloudReader)](https://fir.im/cloudreader)|
|:--:|:--:|
|<img width="200" height=“200” src="https://github.com/youlookwhat/ByRecyclerView/blob/master/art/png_dowload.png?raw=true"></img>|<img width="200" height=“200” src="https://github.com/youlookwhat/CloudReader/blob/master/file/download.png?raw=true"></img>|

## 简单使用  👉 [**Wiki文档**](https://github.com/youlookwhat/ByRecyclerView/wiki)
1.先在 build.gradle 的 repositories 添加

```
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```

2.然后在dependencies添加

```
dependencies {
	implementation 'com.github.youlookwhat:ByRecyclerView:1.0.16'         // AndroidX版本引入
	implementation "com.github.youlookwhat:ByRecyclerView:1.0.16-support" // support版本引入
}
```

3.在XML布局中引用 ByRecyclerView

```xml
<me.jingbin.library.ByRecyclerView
    android:id="@+id/recyclerView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:layoutManager="LinearLayoutManager"
    tools:listitem="@layout/item_home" />
```

4.使用BaseRecyclerAdapter

```java
mAdapter = new OneTypeAdapter(list);
mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
mRecyclerView.setAdapter(mAdapter);
        
public class OneTypeAdapter extends BaseRecyclerAdapter<String> {

    public OneTypeAdapter(List<String> data) {
        super(R.layout.item_main, data);
    }

    @Override
    protected void bindView(BaseByViewHolder<String> holder, String bean, int position) {
        holder.setText(R.id.view_bottom, bean);
    }
}

mAdapter.setNewData(list);   // 设置第一页数据
```
5.设置监听

```java
// 下拉刷新监听
mRecyclerView.setOnRefreshListener(new ByRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新完成
                mRecyclerView.setRefreshing(false);
            }
        });
// 加载更多监听
mRecyclerView.setOnLoadMoreListener(new ByRecyclerView.OnLoadMoreListener() {
    @Override
    public void onLoadMore() {
         mAdapter.addData(list);            // 设置及刷新数据
         mRecyclerView.loadMoreComplete();  // 加载更多完成 
         mRecyclerView.loadMoreEnd();       // 没有更多内容了
         mRecyclerView.loadMoreFail();      // 加载更多失败
    }
});
```


## 与BRVAH、XRecyclerView对比
||ByRecyclerView| BRVAH | XRecyclerView |
|:--:|:--:|:--:|:--:|
|下拉刷新布局|继承基类自定义布局|无|只能简单设置样式|
|SwipeRefreshLayout|可配合使用|可配合使用|不能使用|
|加载更多布局|继承基类自定义布局|继承基类设置简单布局|继承基类自定义类|
|加载更多机制|不足一屏上拉加载，超过后触底加载|不足一屏即加载|触底加载|
|HeaderView|多ViewType区别|同一个item|多ViewType区别|
|FooterView|同一个item|同一个item|不能添加|
|EmptyView|可设置|可设置|不能设置|
|点击/长按事件|有|有| 无 |

ByRecyclerView 是XRecyclerView的拓展，可完全替换XRecyclerView，对于BRVAH它的优势在于四点：

 - 1.headerView使用的是多type的形式，即一个header就是一个position
 - 2.不足一屏上拉加载，超过后触底加载
 - 3.自带下拉加载布局，也可使用三方刷新框架，比如SwipeRefreshLayout
 - 4.万能分割线(LinearLayout / GridLayout / StaggeredGridLayout)

缺点是还没有BRVAH里的部分功能，比如分组adapter、DiffUtils、item扩展动画...后期会逐步完善。

## 混淆
此资源库没有使用到任何序列化、反序列化、JNI、反射，无需进行额外的混淆操作，并且已经测试通过，在公司项目中使用，如果你在项目混淆之后出现问题，请及时联系我。

## Issues
### QQ讨论群 - 831860628
大家可以加群讨论使用本库时出现的问题，也可以直接提[Issues](https://github.com/youlookwhat/ByRecyclerView/issues)，我会第一时间帮助大家解决。如果此库帮助到了你，还请给个Star、Fork一下，我将更有动力持续迭代优化，非常感谢^_^。

## Thanks
 - [XRecyclerView](https://github.com/XRecyclerView/XRecyclerView)
 - [BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)

## About me
 - **QQ**： 770413277
 - **掘金**：[Jinbeen](https://juejin.im/user/56eec46d1ea49300555a176b/posts)
 - **CSDN**：[Jinbeen](https://blog.csdn.net/jingbin_)
 - **Email**： jingbin127@163.com

## License
```
Copyright (C) 2016 Bin Jing

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[1]:https://jitpack.io/v/youlookwhat/ByRecyclerView.svg
[2]:https://jitpack.io/#youlookwhat/ByRecyclerView
[3]:https://img.shields.io/:License-Apache%202.0-blue.svg
[4]:https://www.apache.org/licenses/LICENSE-2.0.html
[5]:https://img.shields.io/badge/API-14%2B-red.svg?style=flat
[6]:https://android-arsenal.com/api?level=14