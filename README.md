# JRecyclerView
RecyclerView 上拉加载，集合 databinding 的通用adapter


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