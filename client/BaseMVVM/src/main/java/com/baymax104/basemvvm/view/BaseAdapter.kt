package com.baymax104.basemvvm.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.baymax104.basemvvm.view.BaseAdapter.BaseViewHolder
import com.baymax104.basemvvm.view.BaseAdapter.ListHandlerFactory
import com.baymax104.basemvvm.view.BaseAdapter.ListHandlerFactory.OnItemClickListener

/**
 * RecyclerView 适配器父类，封装了子项的数据绑定以及事件绑定.
 * 利用绑定参数配置的思想，将列表子项的数据实体和事件处理作为绑定参数，统一绑定到视图中.
 * 其中，[ListHandlerFactory] 子类重写的 [ListHandlerFactory.getBindingConfig] 返回
 * 子项的事件处理对象参数，调用 [.setFactory] 将重写类对象传入，即可将事件绑定到子项中.
 * 数据实体则由适配器子类在重写的 [.onBind] 方法中绑定
 * @param <E> 列表数据实体类型
 * @param <B> DataBinding 类型
 * @author John
 */
abstract class BaseAdapter<E, B : ViewDataBinding>(@LayoutRes protected var layout: Int)
    : RecyclerView.Adapter<BaseViewHolder>() {

    var list: List<E> = ArrayList()
    var factory: ListHandlerFactory? = null

    /**
     * 子类重写该方法，将 item 绑定到 binding 对象中.
     * @param binding 子项布局的 ViewDataBinding 对象
     * @param item 子项数据实体对象
     */
    protected abstract fun onBind(binding: B, item: E)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding =
            DataBindingUtil.inflate<B>(LayoutInflater.from(parent.context), layout, parent, false)
        return BaseViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<B>(holder.itemView)
        val pos: Int = holder.adapterPosition
        val element: E? = list[pos]
        if (binding != null && element != null) {
            onBind(binding, element)
            if (factory != null) {
                val config = factory!!.getBindingConfig()
                config.params.forEach { key, value -> binding.setVariable(key, value) }
            }
            binding.executePendingBindings()
        }
    }

    override fun getItemCount(): Int = list.size

    /**
     * 适配器事件处理对象工厂.
     * 其中包含 [OnItemClickListener] 接口，通常使用该接口的实现对象进行事件定义.
     */
    abstract class ListHandlerFactory {
        /**
         * 事件监听接口，data 参数为子项的数据对象，view 参数为子项布局 View 对象.
         * 在视图绑定中使用 Lambda 表达式传入 data 参数，例如 onClick="@{v->listener.onClick(data, v)}".
         * @param <T> 子项数据对象类型
         */
        fun interface OnItemClickListener<T> {
            fun onClick(data: T, view: View)
        }

        /**
         * 由子类重写该方法，设置参数映射，返回 BindingConfig 对象.
         * @return [BindingConfig]
         */
        abstract fun getBindingConfig(): BindingConfig
    }

    class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}