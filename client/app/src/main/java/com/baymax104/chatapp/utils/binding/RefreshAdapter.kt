package com.baymax104.chatapp.utils.binding

import androidx.databinding.BindingAdapter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener

/**
 *
 * @author John
 */
object RefreshAdapter {

    @JvmStatic
    @BindingAdapter("refresh_onRefresh")
    fun SmartRefreshLayout.onRefresh(listener: OnRefreshListener) {
        setOnRefreshListener(listener)
    }

}