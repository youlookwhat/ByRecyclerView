package me.jingbin.library.adapter;

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

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * ListView adapter
 *
 * @author jingbin
 * link to https://github.com/youlookwhat/ByRecyclerView
 */
public abstract class BaseListAdapter<T, VH extends BaseListHolder> extends BaseAdapter {

    private List<T> mData = new ArrayList<>();

    protected BaseListAdapter() {
    }

    protected BaseListAdapter(List<T> data) {
        this.mData = data == null ? new ArrayList<T>() : data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VH holder;
        if (convertView == null) {
            holder = onCreateViewHolder(parent, position);
            convertView = holder.getItemView();
            convertView.setTag(holder);
        } else {
            holder = (VH) convertView.getTag();
        }
        if (holder != null) {
            onBindView(holder, getItem(position), position);
        }
        return convertView;
    }

    protected abstract VH onCreateViewHolder(ViewGroup parent, int position);

    protected abstract void onBindView(VH holder, T bean, int position);


    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> data) {
        this.mData = data;
    }

    public void addAll(List<T> data) {
        this.mData.addAll(data);
    }

    public void removeAll(List<T> data) {
        this.mData.removeAll(data);
    }

    public void add(T t) {
        this.mData.add(t);
    }

    public void clear() {
        this.mData.clear();
    }


}
