package com.baymax104.basemvvm.view

import android.util.SparseArray

/**
 * [BaseAdapter] 的绑定参数封装类.
 * @author John
 */
class BindingConfig {
    /**
     * 参数映射.
     * @see ViewConfig.params
     */
    @JvmField
    val params = SparseArray<Any>()

    companion object {
        fun add(vararg param: Pair<Int, Any>): BindingConfig = BindingConfig().add(param)
    }

    /**
     * 参数添加方法.
     * @see ViewConfig.add
     */
    fun add(param: Array<out Pair<Int, Any>>): BindingConfig {
        param.forEach { (id, value) ->
            if (params[id] == null) {
                params.put(id, value)
            }
        }
        return this
    }

}