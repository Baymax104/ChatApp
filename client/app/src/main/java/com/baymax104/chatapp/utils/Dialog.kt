package com.baymax104.chatapp.utils

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModel
import com.baymax104.basemvvm.view.BindingConfig
import com.baymax104.basemvvm.vm.applicationViewModels
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.util.XPopupUtils


/**
 * Dialog的Activity作用域委托
 * @param VM ViewModel类型
 * @return ViewModel实例
 */
@MainThread
inline fun <reified VM : ViewModel> BasePopupView.activityViewModels(): Lazy<VM> {
    val activity = XPopupUtils.context2Activity(context) as ComponentActivity
    return activity.viewModels()
}

/**
 * Dialog的Application作用域委托
 * @param VM ViewModel类型
 * @return ViewModel实例
 */
@MainThread
inline fun <reified VM : ViewModel> BasePopupView.applicationViewModels(): Lazy<VM> {
    val activity = XPopupUtils.context2Activity(context) as ComponentActivity
    return activity.applicationViewModels()
}


/**
 * 绑定方法.
 * @param config 视图绑定参数 [BindingConfig] ，默认为 null
 */
@JvmOverloads
fun BasePopupView.bind(config: BindingConfig? = null) {
    // binding
    val binding: ViewDataBinding = DataBindingUtil.bind(popupImplView) ?: return
    binding.lifecycleOwner = this

    config?.params?.forEach { key, value -> binding.setVariable(key, value) }

    // register unbind
    lifecycle.addObserver(LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            binding.unbind()
        }
    })
}

