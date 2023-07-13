package com.baymax104.chatapp.repository

import com.baymax104.chatapp.entity.ChatMessage
import com.baymax104.chatapp.entity.User

/**
 * 聊天消息集合
 * @author John
 */
object ChatMap {

    private val map: MutableMap<Int, MutableList<ChatMessage>> = mutableMapOf()

    val size get() = map.size

    operator fun get(id: Int) = map[id]

    operator fun set(id: Int, list: MutableList<ChatMessage>) {
        map[id] = list
    }

    fun init(users: List<User>) {
        users.forEach { map[it.id] = mutableListOf() }
    }
}