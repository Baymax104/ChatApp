package com.baymax104.chatapp.adapter

import com.baymax104.basemvvm.view.BaseAdapter
import com.baymax104.chatapp.R
import com.baymax104.chatapp.databinding.ItemOnlineUserBinding
import com.baymax104.chatapp.entity.OnlineUser

/**
 * 用户列表Adapter
 * @author John
 */
class OnlineUserAdapter :
    BaseAdapter<OnlineUser, ItemOnlineUserBinding>(R.layout.item_online_user) {
    override fun onBind(binding: ItemOnlineUserBinding, item: OnlineUser) {
        binding.user = item
    }
}