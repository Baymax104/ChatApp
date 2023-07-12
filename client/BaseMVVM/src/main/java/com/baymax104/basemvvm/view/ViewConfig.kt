package com.baymax104.basemvvm.view

import android.util.SparseArray

/**
 * [BaseActivity] 和 [BaseFragment] 的绑定参数封装类.
 * @author John
 */
class ViewConfig(
    /**
     * 布局ID.
     */
    val layout: Int
) {
    /**
     * 参数映射，键值对为参数ID-参数对象.
     */
    @JvmField
    val params = SparseArray<Any>()

    /**
     * 参数添加方法.
     * @param param 参数对
     * @return this 对象，便于链式调用
     */
    fun add(vararg param: Pair<Int, Any>): ViewConfig {
        param.forEach { (id, value) ->
            if (params[id] == null) {
                params.put(id, value)
            }
        }
        return this
    }
}