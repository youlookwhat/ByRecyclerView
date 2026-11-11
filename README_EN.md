# ByRecyclerView

English | [简体中文](README.md)

**ByRecyclerView is an Android RecyclerView library** for building list screens with pull-to-refresh, automatic or pull-up load more, headers, footers, empty states, and a simplified adapter. It extends the Android View-based RecyclerView; it is not a native Jetpack Compose component.

## Status

**Actively maintained.** Latest release: [1.4.2](https://github.com/youlookwhat/ByRecyclerView/releases/tag/1.4.2), available via JitPack. [![JitPack](https://jitpack.io/v/youlookwhat/ByRecyclerView.svg)](https://jitpack.io/#youlookwhat/ByRecyclerView) Please report problems in [Issues](https://github.com/youlookwhat/ByRecyclerView/issues).

## Integrating with an AI coding assistant

The [AI integration guide](AGENTS.md) describes the APIs and common pitfalls. It is not automatically loaded into other projects, so ask the assistant to read it explicitly. For example:

> Use https://github.com/youlookwhat/ByRecyclerView to add pull-to-refresh and automatic load more to this Android View-based list. Read the repository's README and AGENTS.md first, adapt the integration to this project's Gradle configuration and data source, handle empty, failure, retry and end-of-list states, and build and verify it. Report anything you could not verify.

Adjust the request to your app and preferred loading mode. ByRecyclerView is not a native Compose list.

## Features

- Pull-to-refresh, with customizable refresh and load-more views; can also be used with SwipeRefreshLayout.
- Pull-up or automatic load more, with a configurable preload distance.
- Header, footer, and state views; `setPageData` handles first-page empty states and subsequent-page updates.
- Simplified adapters, item and child click/long-click listeners, and payload updates.
- Linear, grid, and staggered-grid item decorations, sticky headers, and skeleton screens.
- DataBinding support and a separate [ViewBinding example](https://github.com/youlookwhat/android-learning/tree/master/ByRv-viewbinding/app/src/main/java/me/jingbin/byrv/viewbinding).

## Installation

For projects using central repository management, add JitPack to `settings.gradle`:

```groovy
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

Or in `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

For older projects that use `allprojects.repositories` in the root `build.gradle`, add JitPack there instead.

Add these dependencies to the app module:

```groovy
dependencies {
    implementation 'com.github.youlookwhat:ByRecyclerView:1.4.2'
    implementation 'androidx.recyclerview:recyclerview:1.3.0'
    implementation 'com.google.android.material:material:1.10.0'
}
```

Kotlin DSL:

```kotlin
dependencies {
    implementation("com.github.youlookwhat:ByRecyclerView:1.4.2")
    implementation("androidx.recyclerview:recyclerview:1.3.0")
    implementation("com.google.android.material:material:1.10.0")
}
```

The library declares RecyclerView and Material as `compileOnly`, so the app must provide them. If your app already includes them, use versions compatible with your project. The library module declares `minSdk` 14; the effective minimum also depends on the dependencies selected by your app.

## Quick start

Add the view to an Activity layout:

```xml
<me.jingbin.library.ByRecyclerView
    android:id="@+id/recyclerView"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

The following Java example uses local data to demonstrate refresh and the end-of-list state; replace it with your own data source. `android.R.layout.simple_list_item_1` supplies the item layout. Add these imports to your Activity:

```java
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.ArrayList;
import java.util.Arrays;
import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;
```

Then use this code after `setContentView(...)`:

```java
ByRecyclerView recyclerView = findViewById(R.id.recyclerView);
recyclerView.setLayoutManager(new LinearLayoutManager(this));

BaseRecyclerAdapter<String> adapter =
        new BaseRecyclerAdapter<String>(android.R.layout.simple_list_item_1) {
    @Override
    protected void bindView(BaseByViewHolder<String> holder, String item, int position) {
        holder.setText(android.R.id.text1, item);
    }
};
recyclerView.setAdapter(adapter);

TextView emptyView = new TextView(this);
emptyView.setText("No items");
adapter.setPageEmptyView(emptyView);

recyclerView.setOnRefreshListener(new ByRecyclerView.OnRefreshListener() {
    @Override
    public void onRefresh() {
        adapter.setPageData(true, new ArrayList<>(Arrays.asList("First", "Second")));
    }
});
recyclerView.setOnLoadMoreListener(true, 1, new ByRecyclerView.OnLoadMoreListener() {
    @Override
    public void onLoadMore() {
        adapter.setPageData(false, new ArrayList<String>()); // No more pages
    }
});
adapter.setPageData(true, new ArrayList<>(Arrays.asList("First", "Second")));
```

The equivalent setup in Kotlin. Add these imports to your Activity:

```kotlin
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import me.jingbin.library.ByRecyclerView
import me.jingbin.library.adapter.BaseByViewHolder
import me.jingbin.library.adapter.BaseRecyclerAdapter
```

Then use this code after `setContentView(...)`:

```kotlin
val recyclerView = findViewById<ByRecyclerView>(R.id.recyclerView)
recyclerView.layoutManager = LinearLayoutManager(this)
val adapter = object : BaseRecyclerAdapter<String>(android.R.layout.simple_list_item_1) {
    override fun bindView(holder: BaseByViewHolder<String>, item: String, position: Int) {
        holder.setText(android.R.id.text1, item)
    }
}
recyclerView.adapter = adapter
adapter.setPageEmptyView(TextView(this).apply { text = "No items" })
recyclerView.setOnRefreshListener {
    adapter.setPageData(true, arrayListOf("First", "Second"))
}
recyclerView.setOnLoadMoreListener(true, 1) {
    adapter.setPageData(false, arrayListOf<String>())
}
adapter.setPageData(true, arrayListOf("First", "Second"))
```

`setPageData` requires the adapter to be attached to a `ByRecyclerView` first. A nonempty first page replaces the data; an empty first page displays the configured empty view and disables load more. A nonempty later page is appended and completes loading; an empty later page marks the end. Use a mutable list, since later pages are appended to it. This helper is not intended for showing a state view above existing list items.

For real requests, update the UI on the main thread. Do not treat a failed request as an empty successful page: stop a failed refresh with `recyclerView.setRefreshing(false)`, or call `recyclerView.loadMoreFail()` for a failed load-more request, and show an error to the user. Advance your page cursor only after success, and manage concurrent requests in your app. To manage the load-more state yourself, use `adapter.addData(...)` followed by `loadMoreComplete()`, or `loadMoreEnd()` when there are no more pages.

`setOnLoadMoreListener(true, 1, ...)` enables automatic loading near the end. The overload without the boolean uses pull-up-and-release loading. `setLoadMoreEnabled(false)` disables load more entirely.

## More examples

- [Chinese README](README.md): item decorations, header/footer views, clicks, sticky headers, and skeleton screens.
- [Demo app](app/src/main/java/me/jingbin/byrecyclerview/) and [Wiki](https://github.com/youlookwhat/ByRecyclerView/wiki).
- [Release notes](https://github.com/youlookwhat/ByRecyclerView/releases).

## License

[Apache License 2.0](LICENSE).
