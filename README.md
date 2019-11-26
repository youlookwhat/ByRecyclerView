# ByRecyclerView

[![jitpack][1]][2] 
[![Apache License 2.0][3]][4]
[![API][5]][6]

RecyclerView实现：下拉刷新、加载更多、设置 HeaderView / FooterView / EmptyView、item点击/长按事件；

优化过的Adapter：极简，减少大量代码，结合DataBinding使用


## 功能特性
 - 1.支持 下拉刷新、加载更多
 - 2.可随意切换 自带下拉刷新布局 / SwipeRefreshLayout
 - 3.加载更多机制：**不足一屏上拉加载，超过后触底加载(所见即所得)**
 - 4.可设置自定义 下拉刷新布局 和 加载更多布局
 - 5.添加/移除 HeaderView、FooterView
 - 6.设置空布局 EmptyView
 - 7.添加item的点击/长按事件
 - 8.优化过的BaseAdapter (RecyclerView / ListView)，减少大量代码
 - 9.Adapter结合DataBinding使用 (RecyclerView / ListView)
 - 10.可添加万能分隔线（LinearLayout / GridLayout / StaggeredGridLayout）
 - 11.默认使用AndoridX，且支持Support


## 文档
 - [项目介绍](https://github.com/youlookwhat/ByRecyclerView/wiki/%E9%A1%B9%E7%9B%AE%E4%BB%8B%E7%BB%8D) | [**详细使用见Wiki**](https://github.com/youlookwhat/ByRecyclerView/wiki)

## 简单接入
### 引入
#### 1.先在 build.gradle 的 repositories 添加
```
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```

#### 2.然后在dependencies添加
```
dependencies {
	implementation 'com.github.youlookwhat:ByRecyclerView:1.0.9'         // AndroidX版本引入
	implementation "com.github.youlookwhat:ByRecyclerView:1.0.9-support" // support版本引入
}
```

### 开始使用
#### 1.在XML布局中引用 ByRecyclerView

```xml
<me.jingbin.library.ByRecyclerView
    android:id="@+id/recyclerView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:layoutManager="LinearLayoutManager"
    tools:listitem="@layout/item_home" />
```

#### 2.使用BaseRecyclerAdapter

```java
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
#### 3.加载更多监听

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

## 答疑
### QQ讨论群 - 831860628
大家可以加群讨论使用本库时出现的问题，也可以直接提[Issues](https://github.com/youlookwhat/ByRecyclerView/issues)，我会第一时间帮助大家解决。如果此库帮助到了你，还请给个Star、Fork一下，我将更有动力持续迭代优化，非常感谢^_^。

## 混淆
此资源库没有使用到任何序列化、反序列化、JNI、反射，无需进行额外的混淆操作，并且已经测试通过，在公司项目中使用，如果你在项目混淆之后出现问题，请及时联系我。

## 与BRVAH、XRecyclerView对比
ByRecyclerView 在XRecyclerView基础上进行了深度优化，使其可以设置自定义的下拉刷新布局 和 加载更多布局等。优化了BRVAH的加载更多逻辑，使其首屏上拉才加载而不是不足一屏就加载。


||ByRecyclerView| BaseRecyclerViewAdapterHelper | XRecyclerView |
|:--:|:--:|:--:|:--:|
|下拉刷新布局|继承基类自定义布局|无|只能简单设置样式|
|加载更多布局|继承基类自定义布局|继承基类设置简单布局|继承基类自定义类|
|加载更多机制|不足一屏上拉加载，超过后触底加载|不足一屏即加载|触底加载|
|HeaderView|多ViewType区别|同一个item|多ViewType区别|
|FooterView|同一个item|同一个item|不能添加|
|EmptyView|可设置|可设置|不能设置|
|item点击/长按事件|有|有| 无 |


## 感谢与参考
 - [XRecyclerView](https://github.com/XRecyclerView/XRecyclerView)
 - [BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)

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