package com.baymax104.chatapp.utils.binding

import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.baymax104.basemvvm.view.BaseAdapter
import com.blankj.utilcode.util.Utils

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/6/16 18:44
 *@Version 1
 */
object ListAdapter {


    @JvmStatic
    @BindingAdapter("recycler_adapter", "recycler_data")
    fun <T, B : ViewDataBinding> RecyclerView.adapter(adapter: BaseAdapter<T, B>, data: List<T>) {
        adapter.list = data
        this.adapter = adapter
    }

    @JvmStatic
    @BindingAdapter("recycler_hasDivider")
    fun RecyclerView.divider(boolean: Boolean) {
        if (boolean) {
            addItemDecoration(DividerItemDecoration(Utils.getApp(), DividerItemDecoration.VERTICAL))
        }
    }

}