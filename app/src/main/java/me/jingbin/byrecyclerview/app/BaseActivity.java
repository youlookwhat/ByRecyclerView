package me.jingbin.byrecyclerview.app;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import me.jingbin.byrecyclerview.R;
import me.jingbin.library.ByRecyclerView;

/**
 * @author jingbin
 */
public abstract class BaseActivity<V extends ViewDataBinding> extends AppCompatActivity {

    protected int mPage = 1;
    protected V binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActionBar();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.icon_back));
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), layoutResID, null, false);
        super.setContentView(binding.getRoot());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        switch (menu.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(menu);
    }

    protected <T extends View> T getView(int id) {
        return (T) findViewById(id);
    }

    public int getPage() {
        return mPage;
    }

    public void setPage(int mPage) {
        this.mPage = mPage;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ByRecyclerView view = getView(R.id.recyclerView);
        if (view != null) {
            view.destroy();
        }
    }
}
