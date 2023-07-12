package com.baymax104.chatapp.base.ioc

/**
 *@Description
 *@Author John
 *@Date 2023/7/11 15:37
 *@Version 1
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class PathMapping(val value: String = "./")
