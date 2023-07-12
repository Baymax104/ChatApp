package com.baymax104.chatapp.base.thread

/**
 *@Description
 *@Author John
 *@Date 2023/7/11 0:01
 *@Version 1
 */
object ThreadMap {

    private val map: MutableMap<Int, ServerThread> = mutableMapOf()

    val size get() = map.size

    operator fun set(id: Int, thread: ServerThread) {
        map[id] = thread
    }

    operator fun minusAssign(id: Int) {
        map.remove(id)
    }

    operator fun get(id: Int) = map[id]

    override fun toString(): String {
        return "ThreadMap(size=$size, map=$map)"
    }
}