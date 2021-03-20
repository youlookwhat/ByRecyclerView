package me.jingbin.byrecyclerview.stickyrv.config;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import me.jingbin.byrecyclerview.R;
import me.jingbin.byrecyclerview.adapter.DataAdapter;
import me.jingbin.byrecyclerview.stickyrv.ChildRecyclerView;
import me.jingbin.byrecyclerview.utils.DataUtil;
import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.view.OnItemFilterClickListener;

public class FeedsJavaFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stickyrv_fragment_feeds_list, null);
        PersistentStaggeredGridLayoutManager layoutManager = new PersistentStaggeredGridLayoutManager(2);
        if (rootView instanceof ChildRecyclerView){
            final ChildRecyclerView childRecyclerView = (ChildRecyclerView) rootView;
//            childRecyclerView.addItemDecoration(new GridItemDecoration(ContextExtensionsKt.dp2px(getActivity(),8f)));
            childRecyclerView.setLayoutManager(layoutManager);
            childRecyclerView.setAdapter(new DataAdapter(DataUtil.getFlexData()));
//            childRecyclerView.setAdapter(new FeedsListAdapter(getActivity()));
            childRecyclerView.setOnItemClickListener(new OnItemFilterClickListener() {
                @Override
                protected void onSingleClick(View v, int position) {
                    childRecyclerView.scrollToPosition(0);
                }
            });
            childRecyclerView.setOnLoadMoreListener(new ByRecyclerView.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    childRecyclerView.loadMoreEnd();
                }
            },1000);
        }

        return rootView;
    }
}
