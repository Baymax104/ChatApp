package com.baymax104.chatapp.entity

/**
 * 聊天界面消息实体
 * @author John
 */
class ChatMessage() {
    var name: String = ""
    var content: String = ""
    var chatDirection: ChatDirection = ChatDirection.DEFAULT
    var type = ChatType.DEFAULT

    constructor(
        name: String,
        content: String,
        chatDirection: ChatDirection,
        type: ChatType
    ) : this() {
        this.name = name
        this.content = content
        this.chatDirection = chatDirection
        this.type = type
    }

}