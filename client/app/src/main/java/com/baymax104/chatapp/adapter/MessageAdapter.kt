package com.baymax104.chatapp.adapter

import com.baymax104.basemvvm.view.BaseAdapter
import com.baymax104.chatapp.R
import com.baymax104.chatapp.databinding.ItemMessageBinding
import com.baymax104.chatapp.entity.ChatDirection.REPLY
import com.baymax104.chatapp.entity.ChatDirection.SEND
import com.baymax104.chatapp.entity.ChatMessage
import com.baymax104.chatapp.entity.ChatType.IMAGE
import com.baymax104.chatapp.entity.ChatType.TEXT
import com.bumptech.glide.Glide

/**
 * 聊天界面适配器
 * @author John
 */
class MessageAdapter : BaseAdapter<ChatMessage, ItemMessageBinding>(R.layout.item_message) {
    override fun onBind(binding: ItemMessageBinding, item: ChatMessage) {
        binding.message = item
        with(binding) {
            when {
                item.chatDirection == REPLY && item.type == TEXT -> {
                    replyText.text = item.content
                }

                item.chatDirection == REPLY && item.type == IMAGE -> {
                    Glide.with(root).load(item.content).into(replyImage)
                }

                item.chatDirection == SEND && item.type == TEXT -> {
                    sendText.text = item.content
                }

                item.chatDirection == SEND && item.type == IMAGE -> {
                    Glide.with(root).load(item.content).into(sendImage)
                }

                else -> {}
            }
        }
    }
}