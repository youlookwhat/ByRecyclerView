
## ByRecyclerView

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
	implementation 'com.github.youlookwhat:ByRecyclerView:1.0.7'         // 默认AndroidX版本
	implementation "com.github.youlookwhat:ByRecyclerView:1.0.8-support" // support版本引入
}
```

### 加入布局并预览
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
    
mRecyclerView.setRefreshing(true);  // 手动启动刷新
mRecyclerView.setRefreshing(false); // 取消刷新重置参数
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
}, 1000); // 1000为延迟多少毫秒调用，不传视为不延迟
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

### 添加item长按事件
```java
recyclerView.setOnItemLongClickListener(new ByRecyclerView.OnItemLongClickListener() {
    @Override
    public boolean onLongClick(View v, int position) {
        return false;
    }
});
```

### addHeaderView 、 addFooterView
> 可传入View或对应布局layout

```java
// add
recyclerView.addHeaderView(getView());
recyclerView.addFooterView(getView());
// remove
recyclerView.removeHeaderView(getView);
recyclerView.removeFooterView(getView);
// or
recyclerView.removeAllHeaderView();
recyclerView.removeAllFooterView();
```

### setEmptyView
> 可传入View或对应布局layout

```java
recyclerView.setEmptyView(getView());
或
recyclerView.setEmptyView(R.layout.layout_empty);
```

### 设置不满一屏不加载
```java
recyclerView.setNotFullScreenNoLoadMore();
```

### 设置加载更多布局底部padding
```java
// 为了底部透明显示
recyclerView.setLoadingMoreBottomHeight(50);
```

### 自定义下拉刷新 或 加载更多布局
```java
recyclerView.setLoadingMoreView(new NeteaseLoadMoreView(this));
recyclerView.setRefreshHeaderView(new NeteaseRefreshHeaderView(this));
```

## About me
 - **QQ：** 770413277
 - **简书：**[Jinbeen](http://www.jianshu.com/users/e43c6e979831/latest_articles)
 - **Blog：**[http://jingbin.me](http://jingbin.me)
 - **Email：** jingbin127@163.com
 - **QQ交流群：**[![](https://img.shields.io/badge/%E7%BE%A4%E5%8F%B7-727379132-orange.svg?style=flat-square)](https://shang.qq.com/wpa/qunwpa?idkey=5685061359b0a767674cd831d8261d36b347bde04cc23746cb6570e09ee5c8aa)