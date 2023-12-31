package com.baymax104.basemvvm.vm

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.baymax104.basemvvm.web.NetLifeCycle
import com.baymax104.basemvvm.web.NetLifeCycleObserver
import com.baymax104.basemvvm.web.NoNetLifeCycle

/**
 * ViewModel组件之一，负责页面的数据请求.
 * 实现了[NetLifeCycle]接口，在网络请求开始时调用所有观察者的[NetLifeCycleObserver.onStart]方法，
 * 在网络请求结束时调用所有观察者的[NetLifeCycleObserver.onFinish]方法
 * @author John
 */
abstract class Requester : ViewModel(), NetLifeCycle {

    /**
     * 网络请求回调，封装了页面设置的回调以及[NetLifeCycle]对象，用于Repository层控制网络周期进行.
     * @param E 请求成功回调的数据类型
     * @property onSuccess 请求成功回调
     * @property onFail 请求失败回调
     * @property lifeCycle 网络生命周期对象，实现[NetLifeCycle]接口
     */
    class ReqCallback<E> {

        @JvmField var onSuccess: (E) -> Unit = {}
        @JvmField var onFail: (String) -> Unit = {}
        @JvmField var lifeCycle: NetLifeCycle = NoNetLifeCycle

        companion object {
            inline fun <E> build(block: ReqCallback<E>.() -> Unit) = ReqCallback<E>().apply(block)
        }

        fun success(block: (E) -> Unit) = apply { onSuccess = block }
        fun fail(block: (String) -> Unit) = apply { onFail = block }
        fun lifecycle(owner: NetLifeCycle) = apply { lifeCycle = owner }
    }

    private val observers: MutableList<NetLifeCycleObserver> = mutableListOf()

    /**
     * 注册网络周期观察者.
     * @param observer 观察者，实现了[NetLifeCycleObserver]
     * @param lifecycleOwner 观察者的生命周期
     */
    fun registerObserver(observer: NetLifeCycleObserver, lifecycleOwner: LifecycleOwner) {
        observers.add(observer)
        lifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                unregisterObserver(observer)
            }
        })
    }

    /**
     * 取消观察者注册.
     * @param observer 取消注册的观察者对象
     */
    private fun unregisterObserver(observer: NetLifeCycleObserver) {
        observers.remove(observer)
    }

    /**
     * 重写[NetLifeCycle.onStart]方法，调用所有观察者的[NetLifeCycleObserver.onStart]方法.
     */
    override fun onStart() {
        observers.forEach { it.onStart() }
    }

    /**
     * 重写[NetLifeCycle.onFinish]方法，调用所有观察者的[NetLifeCycleObserver.onFinish]方法.
     */
    override fun onFinish() {
        observers.forEach { it.onFinish() }
    }
}
