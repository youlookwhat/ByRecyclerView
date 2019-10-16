# ByRecyclerView
RecyclerView 上拉加载，集合 databinding 的通用adapter


## 功能特点
 - 结合databinding的Adapter，可适用于的一般的 RecyclerView 和 ByRecyclerView
 - 可添加item的点击事件、长按事件
 - 一行代码添加或隐藏 HeaderView，FooterView，EmptyView，可随意搭配使用
 - 可使用自带下拉刷新布局 或 SwipeRefreshLayout
 - 可设置自定义下拉刷新布局 或 自定义加载更多布局
 - 备有结合databinding的ListViewAdapter，给ListView设置adapter更简易快速


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