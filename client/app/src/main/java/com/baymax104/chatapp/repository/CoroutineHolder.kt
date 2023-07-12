package com.baymax104.chatapp.repository


/**
 * 协程持有者
 * @author John
 */
object CoroutineHolder {
    var coroutine: ClientCoroutine? = null
}