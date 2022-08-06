package me.jingbin.byrecyclerview.binding;

/*
 * Copyright 2019. Bin Jing (https://github.com/youlookwhat)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.databinding.ViewDataBinding;

import java.util.List;

import me.jingbin.library.adapter.BaseListAdapter;

/**
 * https://github.com/youlookwhat/ByRecyclerView
 */
public abstract class BaseListBindingAdapter<T, B extends ViewDataBinding> extends BaseListAdapter<T, BaseListBindingHolder<B>> {

    private int mLayoutId;

    public BaseListBindingAdapter(@LayoutRes int layoutId) {
        mLayoutId = layoutId;
    }

    public BaseListBindingAdapter(@LayoutRes int layoutId, List<T> data) {
        super(data);
        mLayoutId = layoutId;
    }

    @Override
    protected BaseListBindingHolder<B> onCreateViewHolder(ViewGroup parent, int position) {
        return new BaseListBindingHolder<>(parent, mLayoutId);
    }

    @Override
    protected void onBindView(BaseListBindingHolder<B> holder, T bean, int position) {
        bindView(bean, holder.getBinding(), position);
        holder.getBinding().executePendingBindings();
    }

    protected abstract void bindView(T bean, B binding, int position);
}

