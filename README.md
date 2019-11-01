# ByRecyclerView

[![jitpack][1]][2] 
[![Apache License 2.0][3]][4]
[![API][5]][6]

自定义RecyclerView实现：下拉刷新、加载更多、设置 HeaderView / FooterView / EmptyView、item点击事件；BaseRecyclerAdapter、BaseListAdapter


## 功能特性
 - 1.支持 下拉刷新、加载更多
 - 2.可设置自定义 下拉刷新布局 和 加载更多布局
 - 3.添加/移除 HeaderView、FooterView
 - 4.设置空布局 EmptyView
 - 5.添加item的点击/长按事件
 - 6.可随意切换 自带下拉刷新布局 / SwipeRefreshLayout
 - 7.优化过的BaseRecyclerAdapter
 - 8.优化过的BaseListAdapter
 - 9.结合DataBinding (RecyclerView / ListView)
 - 10.默认使用AndoridX，且支持Support


## 接入文档
### 引入

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
	implementation 'com.github.youlookwhat:ByRecyclerView:1.0.5'         // 默认AndroidX版本
	implementation "com.github.youlookwhat:ByRecyclerView:1.0.5-support" // support版本引入
}
```

### 加入布局
```xml
<me.jingbin.library.ByRecyclerView
    android:id="@+id/recyclerView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:layoutManager="LinearLayoutManager"
    tools:listitem="@layout/item_home" />
```

### 使用BaseRecyclerAdapter
```java
public class OneTypeAdapter extends BaseRecyclerAdapter<DataItemBean> {

    public OneTypeAdapter(List<DataItemBean> data) {
        super(R.layout.item_main, data);
    }

    @Override
    protected void bindView(BaseByViewHolder<DataItemBean> holder, DataItemBean bean, int position) {
        holder.setText(R.id.view_bottom, bean.getTitle());
    }

}
```

### 使用下拉刷新
```java
mRecyclerView.setOnRefreshListener(new ByRecyclerView.OnRefreshListener() {
    @Override
    public void onRefresh() {
        mAdapter.setNewData(list);  // 设置及刷新数据
    }
});
    
mRecyclerView.setRefreshing(true); // 手动启动刷新
```

### 使用加载更多
```java
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


### 添加item点击事件
```java
mRecyclerView.setOnItemClickListener(new ByRecyclerView.OnItemClickListener() {
    @Override
    public void onClick(View v, int position) {
        DataItemBean itemData = mAdapter.getItemData(position);
    }
});
```


<!--## Demo-->


## 与BRVAH、XRecyclerView对比

<!--ByRecyclerView 借鉴了XRecyclerView和BRVAH的很多地方。

 - 其中上拉刷新、加载更多、添加HeaderView参考于XRecyclerView，且在其基础上进行了深度优化，使其可以设置自定义的下拉刷新布局 和 加载更多布局。
 - FooterView、EmptyView、item点击/长按事件 参考于BRVAH，优化了BRVAH的加载更多逻辑，使其首屏上拉才加载而不是不足一屏才加载。-->

||ByRecyclerView| BaseRecyclerViewAdapterHelper | XRecyclerView |
|:--:|:--:|:--:|:--:|
|下拉刷新布局|继承基类自定义布局|无|只能简单设置样式|
|加载更多布局|继承基类自定义布局|继承基类设置简单布局|继承基类自定义类|
|加载更多机制|不足满屏上拉加载，超过后触底加载|不足满屏即加载|触底加载|
|HeaderView|多ViewType区别|同一个item|多ViewType区别|
|FooterView|同一个item|同一个item|不能添加|
|EmptyView|可设置|可设置|不能设置|
|item点击/长按事件|有|有| 无 |


## 感谢与参考
 - [XRecyclerView](https://github.com/XRecyclerView/XRecyclerView)
 - [BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)

## About me
 - **QQ：** 770413277
 - **简书：**[Jinbeen](http://www.jianshu.com/users/e43c6e979831/latest_articles)
 - **Blog：**[http://jingbin.me](http://jingbin.me)
 - **Email：** jingbin127@163.com
 - **QQ交流群：**[![](https://img.shields.io/badge/%E7%BE%A4%E5%8F%B7-727379132-orange.svg?style=flat-square)](https://shang.qq.com/wpa/qunwpa?idkey=5685061359b0a767674cd831d8261d36b347bde04cc23746cb6570e09ee5c8aa)

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
[3]:https://img.shields.io/:License-Apache-blue.svg
[4]:https://www.apache.org/licenses/LICENSE-2.0.html
[5]:https://img.shields.io/badge/API-14%2B-red.svg?style=flat
[6]:https://android-arsenal.com/api?level=14