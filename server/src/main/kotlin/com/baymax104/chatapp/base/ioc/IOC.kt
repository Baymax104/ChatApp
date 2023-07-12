package com.baymax104.chatapp.base.ioc

import kotlin.reflect.KClass

/**
 *@Description
 *@Author John
 *@Date 2023/7/11 16:05
 *@Version 1
 */
class IOC {

    private val container: MutableMap<KClass<*>, Any> = mutableMapOf()

    operator fun set(cl: KClass<*>, instance: Any) {
        container[cl] = instance
    }

    @Suppress("Unchecked_Cast")
    operator fun <T : Any> get(cl: KClass<T>): T = container[cl] as T

}