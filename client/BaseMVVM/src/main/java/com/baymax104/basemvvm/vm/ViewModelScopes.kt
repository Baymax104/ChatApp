package com.baymax104.basemvvm.vm

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

/**
 * 全局Application作用域ViewModel存储对象.
 */
object ApplicationViewModelStore : ViewModelStoreOwner {
    override val viewModelStore: ViewModelStore = ViewModelStore()
}

/**
 * Activity的Application作用域委托
 * @param VM ViewModel类型
 * @return ViewModel实例
 */
@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.activityViewModels(): Lazy<VM> =
    viewModels()

/**
 * Activity的Activity作用域委托
 * @param VM ViewModel类型
 * @return ViewModel实例
 */
@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.applicationViewModels(): Lazy<VM> =
    ViewModelLazy(
        VM::class,
        { ApplicationViewModelStore.viewModelStore },
        { defaultViewModelProviderFactory },
        { defaultViewModelCreationExtras }
    )

/**
 * Fragment的Application作用域委托
 * @param VM ViewModel类型
 * @return ViewModel实例
 */
@MainThread
inline fun <reified VM : ViewModel> Fragment.applicationViewModels(): Lazy<VM> =
    viewModels({ ApplicationViewModelStore })

/**
 * Fragment的Activity作用域委托
 * @param VM ViewModel类型
 * @return ViewModel实例
 */
@MainThread
inline fun <reified VM : ViewModel> Fragment.activityViewModels(): Lazy<VM> =
    activityViewModels()

/**
 * Fragment的Fragment作用域委托
 * @param VM ViewModel类型
 * @return ViewModel实例
 */
@MainThread
inline fun <reified VM : ViewModel> Fragment.fragmentViewModels(): Lazy<VM> =
    viewModels()
