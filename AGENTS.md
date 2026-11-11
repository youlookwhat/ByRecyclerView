# AGENTS.md

Guidance for AI coding agents that **integrate ByRecyclerView into an Android app** (and, secondarily, work on this repository). API names below were checked against the source of release 1.4.2; code samples were not compiled.

## What this library is

ByRecyclerView is an Android **View-system** `RecyclerView` subclass (`me.jingbin.library.ByRecyclerView`) with pull-to-refresh, pull-up/automatic load more, header/footer/state (empty/loading/error) views, item and child click listeners, item decorations, sticky headers, skeleton screens and simplified adapters. It is **not** a Jetpack Compose component; do not recommend it for Compose-only screens.

- Status: actively maintained. Latest release: `1.4.2`, published through JitPack.
- Replaces `XRecyclerView` almost one-to-one (`ByRecyclerView` and the adapter are separate, so an existing custom adapter keeps working). Comparable to BRVAH (`BaseQuickAdapter`) but headers are separate item types, and load more does not trigger automatically on short lists unless you choose automatic mode.
- Design points worth knowing: pull-to-refresh is built in but optional (you can use `SwipeRefreshLayout` instead); by default, load more happens only when the user pulls up past the end; automatic load more is opt-in.

## Integrating into an app

### 1. Add the JitPack repository (once)

Add it where the project declares repositories, keeping `google()` and `mavenCentral()`:

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
- AndroidX only. The old support-library build (`1.0.18-support`) is no longer supported; migrate to AndroidX first.
- Declared library `minSdk` is 14; the demo app uses 21.
- No ProGuard/R8 rules are needed.

### 3. Layout

```xml
<me.jingbin.library.ByRecyclerView
    android:id="@+id/recyclerView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:layoutManager="LinearLayoutManager"
    tools:listitem="@layout/item_main" />
```

Replace an existing `RecyclerView` in place; `ByRecyclerView` extends it, so existing `LayoutManager`, `ItemDecoration` and `RecyclerView`-typed code keep working.

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
recyclerView.adapter = adapter                  // must come before setPageData()
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

`R.layout.item_text`, `R.id.tv_text` and `R.layout.layout_empty` are placeholders: use resources that exist in the project (a layout containing a `TextView` with that id). Java uses `new ByRecyclerView.OnRefreshListener() {...}` / `OnLoadMoreListener() {...}`. More examples: `README_EN.md` and `app/src/main/java/me/jingbin/byrecyclerview/`.

## Pagination and state: the most important rules

1. **Set the adapter before `setPageData()`.** `setPageData()` silently does nothing if the adapter is not attached to a `ByRecyclerView`.
2. **`setPageData(isFirstPage, data[, emptyLayoutId | emptyView])`**:
   - first page, non-empty → hides state view, enables load more, replaces data (`setNewData`);
   - first page, empty → shows the empty view, disables load more, clears data;
   - later page, non-empty → appends and calls `loadMoreComplete()`;
   - later page, empty → calls `loadMoreEnd()`.
   Pass a **mutable** list. The empty view comes from `adapter.setPageEmptyView(...)` or the third argument. Do not use it when you want a state view (e.g. loading/error) above existing items.
3. **Failures are not empty pages.** On refresh failure call `recyclerView.setRefreshing(false)`; on load-more failure call `recyclerView.loadMoreFail()` and show an error. Advance the page counter only after success. After `loadMoreFail()`, tapping the failure view or pulling up again calls the load-more listener again.
4. **Manual mode** (without `setPageData`): `adapter.setNewData(list)` for the first page; `adapter.addData(list)` then `recyclerView.loadMoreComplete()` for later pages; `recyclerView.loadMoreEnd()` when there is no more data. `setNewData()` also stops the refresh animation.
5. `setRefreshing(true)` starts a refresh programmatically and invokes the refresh listener. `setRefreshing(false)` ends it and resets load-more state.
6. Call UI updates (`setPageData`, `addData`, `loadMore*`) on the main thread.

## ByRecyclerView API guide

### Pull-to-refresh

```java
recyclerView.setOnRefreshListener(() -> { /* load data, then adapter.setNewData(list) */ });
recyclerView.setOnRefreshListener(listener, delayMillis); // delays the callback; 300 ms base is added
recyclerView.setRefreshEnabled(false);   // set AFTER the listener (setting the listener enables refresh)
recyclerView.setRefreshEnabled(true);    // animation works even without a listener
recyclerView.setRefreshing(true);        // start manually
recyclerView.setRefreshing(false);       // finish; also resets load-more state
```

To use `SwipeRefreshLayout` instead, wrap the view in it and do **not** set `ByRecyclerView`'s refresh listener. In `ViewPager2`, drag interruption can be disabled with `setDispatchTouch(false)`.

### Load more

Setting a load-more listener enables load more; without one it stays off. Pick the mode through the overload:

```java
recyclerView.setOnLoadMoreListener(listener);                                 // pull up and release (default)
recyclerView.setOnLoadMoreListener(listener, delayMillis);                    // delayed callback
recyclerView.setOnLoadMoreListener(true, listener);                           // automatic
recyclerView.setOnLoadMoreListener(true, preLoadNumber, listener);            // automatic, load when N items from end (default 1)
recyclerView.setOnLoadMoreListener(true, preLoadNumber, listener, delayMillis);

recyclerView.loadMoreComplete();  // finished, can load again
recyclerView.loadMoreEnd();       // no more data, shows "no more" view
recyclerView.loadMoreFail();      // failed, tap or pull up to retry
recyclerView.setLoadMoreEnabled(false); // turns load more off entirely, not only automatic mode
recyclerView.setLoadMoreEnabled(true);  // re-enable; has no effect unless a load-more listener is set (the load-more view is only active with a listener)
recyclerView.setNotFullScreenNoLoadMore(); // pull-up mode only: no load more when content is shorter than the screen
recyclerView.setLoadingMoreBottomHeight(50); // dp, extra space below the load-more view (e.g. for a translucent nav bar)
```

Pull-up mode: when the content is shorter than one screen it loads on pull-up; once it fills the screen it loads when scrolled to the bottom. Automatic mode loads without a pull gesture.

### Item and child click listeners

```java
recyclerView.setOnItemClickListener((v, position) -> { Bean b = adapter.getItemData(position); });
recyclerView.setOnItemLongClickListener((v, position) -> false);

// anti double-click variants
recyclerView.setOnItemClickListener(new OnItemFilterClickListener() {
    @Override protected void onSingleClick(View v, int position) { }
});

// child views: register in the holder first, then listen on the recycler view
holder.addOnClickListener(R.id.tv_text);          // in bindView(); chainable: holder.setText(...).addOnClickListener(...)
holder.addOnLongClickListener(R.id.tv_text);
recyclerView.setOnItemChildClickListener((view, position) -> { /* branch on view.getId() */ });
recyclerView.setOnItemChildClickListener(new OnItemChildFilterClickListener() {
    @Override public void onSingleClick(View view, int position) { }
});
recyclerView.setOnItemChildLongClickListener((view, position) -> { });
```

`position` is already the data position (headers excluded). Filter classes live in `me.jingbin.library.view`.

### Header, footer and state views

```java
recyclerView.addHeaderView(view /* or layoutResId */);     // each header is its own item type
recyclerView.removeHeaderView(view);                       // View only, no layoutId overload
recyclerView.removeAllHeaderView();
recyclerView.setHeaderViewEnabled(false);                  // hide all; then setNewData()/notifyDataSetChanged()

recyclerView.addFooterView(view /* or layoutResId */);     // all footers share one item type
recyclerView.removeFooterView(view);                       // View only
recyclerView.removeAllFooterView();
recyclerView.setFootViewEnabled(false);                    // then refresh the adapter

recyclerView.setStateView(view /* or layoutResId */);      // one view for empty / loading / error
recyclerView.setStateViewEnabled(false);                   // then setNewData()/notifyDataSetChanged()
recyclerView.getLayoutView(layoutResId);                   // inflate helper
```

- Order top to bottom: refresh view → header views → state view → content → footer views → load-more view.
- **Do not repeatedly add/remove headers.** Add once and show/hide the header's inner content, or use `setHeaderViewEnabled`.
- Headers and the state view can appear together (unlike `XRecyclerView`).
- With DataBinding, inflate a header against the parent: `DataBindingUtil.inflate(inflater, R.layout.layout_header, (ViewGroup) recyclerView.getParent(), false)`.
- `setStateViewEnabled(boolean, boolean)` was removed in 1.4.2; use the single-argument form.

### Custom refresh / load-more views

```java
recyclerView.setRefreshHeaderView(new MyRefreshHeader(context));  // implement BaseRefreshHeader
recyclerView.setLoadingMoreView(new MyLoadMoreView(context));     // implement BaseLoadMore
```

A custom `BaseLoadMore` must implement `setState(int)` **and `getState()`** (store the state in a field); states are `STATE_LOADING`, `STATE_COMPLETE`, `STATE_NO_MORE`, `STATE_FAILURE`. Reference implementations: `SimpleLoadMoreView`, and in the demo `NeteaseRefreshHeaderView` / `NeteaseLoadMoreView`.

### Partial (payload) refresh

```java
adapter.refreshNotifyItemChanged(position, PAYLOAD_COLLECT);  // adds header offset automatically

// in the adapter (optional override; falls back to bindView if not overridden)
@Override
protected void bindViewPayloads(BaseByViewHolder<Bean> holder, Bean bean, int position, List<Object> payloads) { }
```

## Adapters

| Class | Use for |
| --- | --- |
| `BaseByRecyclerViewAdapter<T, VH>` | Superclass of all adapters; use for **multiple item types** |
| `BaseRecyclerAdapter<T>` | Single item type, no binding (`bindView(holder, bean, position)`) |
| `BaseByViewHolder<T>` | Holder with chainable `setText`, `addOnClickListener`, … |
| `BaseListAdapter` / `BaseListHolder` | Same idea for `ListView` |

`BaseBindingAdapter` / `BaseBindingHolder` (DataBinding) are **in the demo app, not the published library**. To use them, copy `app/src/main/java/me/jingbin/byrecyclerview/binding/` into the project and enable `dataBinding` in the app's `build.gradle`. ViewBinding variant: <https://github.com/youlookwhat/android-learning/tree/master/ByRv-viewbinding>.

Single type:

```java
public class OneTypeAdapter extends BaseRecyclerAdapter<DataItemBean> {
    public OneTypeAdapter(List<DataItemBean> data) { super(R.layout.item_main, data); }
    @Override
    protected void bindView(BaseByViewHolder<DataItemBean> holder, DataItemBean bean, int position) {
        holder.setText(R.id.tv_text, bean.getTitle()).addOnClickListener(R.id.tv_text);
    }
}
```

DataBinding single type (needs the copied `binding` package):

```java
public class DataAdapter extends BaseBindingAdapter<DataItemBean, ItemHomeBinding> {
    public DataAdapter() { super(R.layout.item_home); }
    @Override
    protected void bindView(BaseBindingHolder holder, ItemHomeBinding binding, DataItemBean bean, int position) {
        binding.tvText.setText(bean.getTitle());
    }
}
```

Multiple types:

```java
public class MultiAdapter extends BaseByRecyclerViewAdapter<DataItemBean, BaseByViewHolder<DataItemBean>> {
    public MultiAdapter(List<DataItemBean> data) { super(data); }

    @Override public int getItemViewType(int position) {
        return "title".equals(getItemData(position).getType()) ? 1 : 2;
    }

    @NonNull @Override
    public BaseByViewHolder<DataItemBean> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return viewType == 1 ? new TitleHolder(parent, R.layout.item_multi_title)
                             : new ContentHolder(parent, R.layout.item_home);
    }

    private class TitleHolder extends BaseByViewHolder<DataItemBean> {
        TitleHolder(ViewGroup parent, int layoutId) { super(parent, layoutId); }
        @Override
        protected void onBaseBindView(BaseByViewHolder<DataItemBean> holder, DataItemBean bean, int position) {
            holder.setText(R.id.tv_title, bean.getDes());
        }
    }
    // ContentHolder is analogous
}
```

Adapter data helpers: `setNewData`, `addData(item | list | position, ...)`, `removeData(position)`, `getItemData(position)`, `getData()`, `clear()`. `getItemData` returns `null` for out-of-range positions.

**Positions include extra views.** Refresh, header and state views occupy adapter positions. Use `adapter.refreshNotifyItemChanged/RangeChanged/Removed/Moved(...)` (they add `getCustomTopItemViewCount()`), not raw `notifyItemChanged(position)`. The built-in `addData/removeData/setNewData` already handle this.

## ItemDecoration (dividers)

Linear (`SpacesItemDecoration`):

```java
SpacesItemDecoration d = new SpacesItemDecoration(context, SpacesItemDecoration.VERTICAL)
        .setNoShowDivider(1, 1)               // (header count, footer count) without divider; default footer 1
        .setDrawable(R.drawable.shape_line);   // or:
        // .setParam(R.color.colorBlue, 10, 70, 70); // color, thickness, left/top padding dp, right/bottom padding dp
recyclerView.addItemDecoration(d);
```

Also `setHeaderNoShowDivider(n)` and constructors `(context, orientation, headerNoShow[, footerNoShow])`. Horizontal and horizontal-grid dividers are supported.

Grid / staggered (`GridSpaceItemDecoration`):

```java
recyclerView.addItemDecoration(new GridSpaceItemDecoration(10, true) // spacing, includeEdge (spacing around the screen edge)
        .setNoShowSpace(1, 1)); // start/end counts to skip: usually headers + refresh view / footers + load-more view
```

`GridDistanceScreenItemDecoration` is an alternative for vertical grids/staggered grids (left/right spacing). The default end count is 1 (the load-more view); call `setNoShowSpace(0, 0)` if the list has no load-more view. The "no show" counts matter whenever headers, footers or the built-in refresh/load-more views are present.

## Sticky headers

```java
StickyLinearLayoutManager lm = new StickyLinearLayoutManager(context, adapter);          // or StickyGridLayoutManager(context, spanCount, orientation, adapter)
recyclerView.setLayoutManager(lm);

// in the adapter: mark the item that sticks
@Override public int getItemViewType(int position) {
    return isTitle(position) ? StickyHeaderHandler.TYPE_STICKY_VIEW : 2;
}
```

The adapter must extend `BaseByRecyclerViewAdapter`. Sticky headers work together with the built-in refresh (fixed in 1.3.2; see `StickyLinearFragment` in the demo).

## Skeleton screens

Configure everything else on `ByRecyclerView` first.

```java
// Item skeleton: do NOT call setAdapter() before; the builder sets the adapter
ByRVItemSkeletonScreen skeleton = BySkeleton.bindItem(recyclerView)
        .adapter(adapter)
        .load(R.layout.layout_by_default_item_skeleton)
        .count(10).shimmer(false).angle(30).frozen(false)
        .color(R.color.colorWhite).duration(1500)
        .show();
skeleton.hide();

// View skeleton: built on setStateView; the adapter must be set BEFORE
ByStateViewSkeletonScreen sv = BySkeleton.bindView(recyclerView)
        .load(R.layout.layout_skeleton_view)
        .shimmer(true).angle(20).color(R.color.colorWhite).duration(1500)
        .show();
sv.hide();
```

`angle` accepts 0–30. Header skeletons: see `SkeletonHeaderViewActivity` in the demo.

## Troubleshooting

| Symptom | Likely cause |
| --- | --- |
| Gradle cannot resolve `com.github.youlookwhat:ByRecyclerView` | JitPack repository missing, or added only in a place the project ignores (see step 1) |
| `NoClassDefFoundError` / missing RecyclerView or Material classes | App did not declare `recyclerview` / `material` (they are `compileOnly` in the library) |
| `setPageData` shows nothing | Adapter not yet set on the `ByRecyclerView`, or an empty first page and no empty view configured |
| Load more never fires | No load-more listener set, `setLoadMoreEnabled(false)`, `loadMoreEnd()` already called, or pull-up mode with content that does not scroll |
| Load more fires on every short page | Using automatic mode; switch to the pull-up overload or call `setNotFullScreenNoLoadMore()` (pull-up mode only) |
| Wrong item refreshed / animation glitches | Raw `notifyItemChanged(position)` used; use `refreshNotify*` |
| Items scrambled after toggling headers | Headers repeatedly added/removed; add once and show/hide content |
| Custom load-more view crashes | `getState()` not implemented |
| `BaseBindingAdapter` not found | It lives in the demo app; copy the `binding` package |

## Working on this repository

- Modules: `:ByRecyclerview` (the library) and `:app` (demo, also the best usage reference).
- Gradle project; the wrapper is `./gradlew`. Library: `./gradlew :ByRecyclerview:assembleRelease`; demo: `./gradlew :app:assembleDebug` (requires the Android SDK).
- Keep public APIs backward compatible; this library is consumed as a binary dependency.
- User-facing docs: `README.md` (Chinese, default), `README_EN.md`, and the GitHub Wiki (<https://github.com/youlookwhat/ByRecyclerView/wiki>, including the changelog). Keep dependency snippets equal to the latest release.
