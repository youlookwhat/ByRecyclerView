# ByRecyclerView
RecyclerView 下拉刷新、上拉加载，集合 databinding 的通用adapter、HeaderView，FooterView，EmptyView、item点击事件、集合databinding的ListView精简Adapter


## 功能特点
 - 结合databinding的Adapter，可适用于的一般 RecyclerView 和 ByRecyclerView
 - 可添加item的点击事件、长按事件
 - 一行代码添加或隐藏 HeaderView，FooterView，EmptyView，可随意搭配使用
 - 可使用自带下拉刷新布局 或 SwipeRefreshLayout
 - 可设置自定义下拉刷新布局 或 自定义加载更多布局
 - 备有结合databinding的ListViewAdapter，给ListView设置adapter更简易快速


## 与XRecyclerView、BRVAH对比

||ByRecyclerView|XRecyclerView|BaseRecyclerViewAdapterHelper|
|:--:|:--:|:--:|:--:|
|下拉刷新布局|继承基类自定义布局|只能简单设置样式|无|
|加载更多布局|继承基类自定义布局|继承基类自定义类|继承基类设置对应布局|
|加载更多机制|不足满屏上拉加载，超过后触底加载|触底加载|不足满屏即加载|
|HeaderView|每个header即一个item|每个header即一个item|多个header是同一个item|
|FooterView|多个footer是同一个item|不能添加|多个footer是同一个item|
|EmptyView|对应的是一个item|不能设置|对应的是一个item|
|是否自定义RecyclerView|是，在此基础上实现上拉刷新和加载更多|是|否|
|是否有Adapter|有，基于databinding的RecyclerView|无，需自己实现Adapter|有，加载更多和Adapter全一体|
|item点击/长按事件|有|无|有|



## 接入文档

## Demo

## Thanks
 - [XRecyclerView](https://github.com/XRecyclerView/XRecyclerView)
 - [BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)

### 混淆代码参考
```java
 # com.github.CymChad:BaseRecyclerViewAdapterHelper:2.9.41
 -keep class com.chad.library.adapter.** {
 *;
 }
 -keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
 -keep public class * extends com.chad.library.adapter.base.BaseViewHolder
 -keepclassmembers  class **$** extends com.chad.library.adapter.base.BaseViewHolder {
      <init>(...);
 }
```