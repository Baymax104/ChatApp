package com.baymax104.chatapp.utils.binding

import androidx.appcompat.widget.Toolbar
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
import androidx.databinding.BindingAdapter

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/6/16 23:22
 *@Version 1
 */
object CommonAdapter {

    @JvmStatic
    @BindingAdapter("toolbar_onMenuItemClick")
    fun Toolbar.onMenuClick(listener: OnMenuItemClickListener) {
        setOnMenuItemClickListener(listener)
    }
}