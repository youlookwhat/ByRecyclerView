# AGENTS.md

Guidance for AI coding agents that **integrate ByRecyclerView into an Android app** (and, secondarily, work on this repository).

## What this library is

ByRecyclerView is an Android **View-system** `RecyclerView` subclass (`me.jingbin.library.ByRecyclerView`) with pull-to-refresh, pull-up/automatic load more, header/footer/state (empty) views, item click listeners, item decorations, sticky headers, skeleton screens and simplified adapters. It is **not** a Jetpack Compose component; do not recommend it for Compose-only screens.

Status: actively maintained. Latest release: `1.4.2`, published through JitPack.

## Integrating into an app

### 1. Add the JitPack repository (once)

Add it where the project declares repositories, and keep `google()` and `mavenCentral()`:

- `settings.gradle(.kts)` → `dependencyResolutionManagement { repositories { ... } }`, or
- root `build.gradle` → `allprojects { repositories { ... } }` for older projects.

```groovy
maven { url 'https://jitpack.io' }          // Groovy
maven { url = uri("https://jitpack.io") }   // Kotlin DSL
```

Do not add it in both places. If the project uses `RepositoriesMode.FAIL_ON_PROJECT_REPOS`, only use `settings.gradle`.

### 2. Add dependencies in the app module

```groovy
implementation 'com.github.youlookwhat:ByRecyclerView:1.4.2'
implementation 'androidx.recyclerview:recyclerview:1.3.0'
implementation 'com.google.android.material:material:1.10.0'
```

- RecyclerView and Material are `compileOnly` in the library, so **the app must declare them**. If the project already has them, keep its existing compatible versions.
- The coordinate is `com.github.youlookwhat:ByRecyclerView` (JitPack), **not** `me.jingbin.library`.
- The library is AndroidX only. If the project still uses the old support library, migrate first or do not use this library.
- Declared library `minSdk` is 14; the demo app uses 21.

### 3. Layout

```xml
<me.jingbin.library.ByRecyclerView
    android:id="@+id/recyclerView"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

Replace an existing `RecyclerView` in place. `ByRecyclerView` extends it, so existing `LayoutManager`, `ItemDecoration` and `findViewById<RecyclerView>` code keep working.

### 4. Minimal working setup (Kotlin)

```kotlin
import androidx.recyclerview.widget.LinearLayoutManager
import me.jingbin.library.ByRecyclerView
import me.jingbin.library.adapter.BaseByViewHolder
import me.jingbin.library.adapter.BaseRecyclerAdapter

val recyclerView = findViewById<ByRecyclerView>(R.id.recyclerView)
recyclerView.layoutManager = LinearLayoutManager(this)

val adapter = object : BaseRecyclerAdapter<String>(R.layout.item_text) {
    override fun bindView(holder: BaseByViewHolder<String>, bean: String, position: Int) {
        holder.setText(R.id.tv_text, bean)
    }
}
recyclerView.adapter = adapter                 // must come before setPageData()
adapter.setPageEmptyView(R.layout.layout_empty) // optional, used by setPageData()

recyclerView.setOnRefreshListener {
    // load page 1, then:
    adapter.setPageData(true, firstPage)
}
recyclerView.setOnLoadMoreListener(true, 1) { // true = automatic load more
    // load next page, then:
    adapter.setPageData(false, nextPage)
}
```

Java uses `new ByRecyclerView.OnRefreshListener() {...}` / `OnLoadMoreListener() {...}`. Full examples: `README_EN.md` and `app/src/main/java/me/jingbin/byrecyclerview/`.

## Rules that prevent common mistakes

1. **Set the adapter before `setPageData()`.** `setPageData()` silently does nothing if the adapter is not attached to a `ByRecyclerView`.
2. **`setPageData(isFirstPage, data)`** handles pagination:
   - first page, non-empty → replaces data, enables load more;
   - first page, empty → shows the empty view, disables load more;
   - later page, non-empty → appends and calls `loadMoreComplete()`;
   - later page, empty → calls `loadMoreEnd()`.
   Pass a **mutable** list. It cannot be used to show a state view above existing items.
3. **Failures are not empty pages.** On refresh failure call `recyclerView.setRefreshing(false)`; on load-more failure call `recyclerView.loadMoreFail()` and show an error. Advance the page counter only after success.
4. **Manual mode** (instead of `setPageData`): `adapter.setNewData(list)` for the first page; `adapter.addData(list)` then `recyclerView.loadMoreComplete()` for later pages; `recyclerView.loadMoreEnd()` when there is no more data.
5. **Load-more mode is chosen by the listener overload.** `setOnLoadMoreListener(listener)` is pull-up-and-release; `setOnLoadMoreListener(true, preLoadNumber, listener)` is automatic. `setLoadMoreEnabled(false)` disables load more entirely.
6. Setting a refresh listener enables the built-in pull-to-refresh header. To use `SwipeRefreshLayout` instead, do not set `ByRecyclerView`'s refresh listener.
7. **Positions include extra views.** Header, footer, refresh and state views occupy adapter positions. Use `adapter.refreshNotifyItemChanged(position[, payload])` and related `refreshNotify*` helpers, and `adapter.getItemData(position)` / listener `position` arguments, rather than raw `notifyItemChanged(position)`.
8. After `setStateViewEnabled(false)` or `setHeaderViewEnabled(false)`, refresh data with `setNewData()` or `notifyDataSetChanged()`.
9. Do not repeatedly call `addHeaderView()` / `removeHeaderView()`. Add the header once and show or hide its content.
10. Use `setOnItemClickListener` / `setOnItemLongClickListener` on `ByRecyclerView`; child-view clicks need `holder.addOnClickListener(R.id.view_id)` in `bindView` plus `setOnItemChildClickListener`. Prefer the `...Filter...` listener variants to prevent double clicks.
11. Multiple item types or data binding: extend `BaseByRecyclerViewAdapter<T, VH>` with a `BaseByViewHolder` subclass. The `BaseBindingAdapter` helpers live in the **demo app** (`app/src/main/java/me/jingbin/byrecyclerview/binding/`), not in the published library; copy them if needed. DataBinding or ViewBinding must be enabled in the app's `build.gradle`.
12. No ProGuard/R8 rules are required.

## Quick reference

| Need | API |
| --- | --- |
| Pull-to-refresh | `setOnRefreshListener`, `setRefreshing(boolean)` |
| Load more | `setOnLoadMoreListener`, `loadMoreComplete/End/Fail`, `setLoadMoreEnabled` |
| Header / footer | `addHeaderView`, `addFooterView` |
| Empty / state view | `adapter.setPageEmptyView`, `setStateView`, `setStateViewEnabled` |
| Dividers | `SpacesItemDecoration` (linear), `GridSpaceItemDecoration` (grid/staggered) |
| Skeleton | `BySkeleton.bindItem(...)` / `bindView(...)` |
| Sticky header | `StickyLinearLayoutManager` + `StickyHeaderHandler.TYPE_STICKY_VIEW` |
| Partial refresh | `bindViewPayloads` + `refreshNotifyItemChanged(position, payload)` |

## Working on this repository

- Modules: `:ByRecyclerview` (the library) and `:app` (demo, also the best usage reference).
- Gradle project; the wrapper is `./gradlew`. Library: `./gradlew :ByRecyclerview:assembleRelease`; demo: `./gradlew :app:assembleDebug` (requires the Android SDK).
- Keep public APIs backward compatible; this library is consumed as a binary dependency.
- User-facing docs are in `README.md` (Chinese, default) and `README_EN.md`. Keep both in sync, and keep the version in dependency snippets equal to the latest release.
