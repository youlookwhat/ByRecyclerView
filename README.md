# ByRecyclerView
RecyclerView 下拉刷新、加载更多、HeaderView，FooterView，EmptyView、item点击事件；BaseRecyclerAdapter、BaseListAdapter


## 功能特性
 - 1. 支持 下拉刷新、加载更多
 - 2. 可设置自定义 下拉刷新布局 和 加载更多布局
 - 3. 添加/移除 HeaderView、FooterView
 - 4. 设置空布局 EmptyView
 - 5. 添加item的点击/长按事件
 - 6. 优化过的BaseRecyclerAdapter (结合databinding，简易高效)
 - 7. 优化过的BaseListAdapter (ListView结合databinding适配器)
 - 8. 可随意切换 自带下拉刷新布局 / SwipeRefreshLayout
 - 9. 默认使用AndoridX，且支持Support


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
	compile 'com.github.youlookwhat:ByRecyclerView:1.0'
}
```

3.最后在android里添加

```
android {
    // adapter 使用到
    dataBinding {
        enabled = true
    }
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
public class DataAdapter extends BaseRecyclerAdapter<DataItemBean, ItemHomeBinding> {

    public DataAdapter(List<DataItemBean> data) {
        super(R.layout.item_home, data);
    }

    @Override
    protected void bindView(DataItemBean bean, ItemHomeBinding binding, int position) {
        binding.tvText.setText(bean.getTitle() + ": " + position);
    }

}
```

### 使用下拉刷新
```java
        mRecyclerView.setOnRefreshListener(new ByRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.setNewData(list);       // 设置及刷新数据
                mRecyclerView.refreshComplete(); // 刷新完成
            }
        });
        
        mRecyclerView.setRefreshing(); // 手动启动刷新
```

### 使用加载更多
```java
        mRecyclerView.setOnLoadMoreListener(new ByRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
					mAdapter.addData(list);             // 设置及刷新数据
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


## 与XRecyclerView、BRVAH对比

<!--ByRecyclerView 借鉴了XRecyclerView和BRVAH的很多地方。

 - 其中上拉刷新、加载更多、添加HeaderView参考于XRecyclerView，且在其基础上进行了深度优化，使其可以设置自定义的下拉刷新布局 和 加载更多布局。
 - FooterView、EmptyView、item点击/长按事件 参考于BRVAH，优化了BRVAH的加载更多逻辑，使其首屏上拉才加载而不是不足一屏才加载。-->

||ByRecyclerView|XRecyclerView|BaseRecyclerViewAdapterHelper|
|:--:|:--:|:--:|:--:|
|下拉刷新布局|继承基类自定义布局|只能简单设置样式|无|
|加载更多布局|继承基类自定义布局|继承基类自定义类|继承基类设置对应布局|
|加载更多机制|不足满屏上拉加载，超过后触底加载|触底加载|不足满屏即加载|
|HeaderView|多ViewType区别|多ViewType区别|同一个item|
|FooterView|同一个item|不能添加|同一个item|
|EmptyView|可设置|不能设置|可设置|
|item点击/长按事件|有|无|有|


## 感谢与参考
 - [XRecyclerView](https://github.com/XRecyclerView/XRecyclerView)
 - [BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)

