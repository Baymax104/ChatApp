package com.baymax104.chatapp.adapter

import com.baymax104.basemvvm.view.BaseAdapter
import com.baymax104.chatapp.R
import com.baymax104.chatapp.databinding.ItemOnlineUserBinding
import com.baymax104.chatapp.entity.User

/**
 * 用户列表Adapter
 * @author John
 */
class OnlineUserAdapter : BaseAdapter<User, ItemOnlineUserBinding>(R.layout.item_online_user) {
    override fun onBind(binding: ItemOnlineUserBinding, item: User) {
        binding.user = item
    }
}