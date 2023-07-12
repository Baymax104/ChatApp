package com.baymax104.chatapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class FragmentAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    var fragments: List<Fragment> = listOf()

    override fun createFragment(position: Int): Fragment = fragments[position]

    override fun getItemCount(): Int = fragments.size
}