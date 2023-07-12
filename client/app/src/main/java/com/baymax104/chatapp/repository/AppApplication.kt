package com.baymax104.chatapp.repository

import android.app.Application
import com.baymax104.chatapp.entity.User
import com.tencent.mmkv.MMKV

/**
 * 使用Application存储全局数据，在应用的整个生命周期有效.
 * @author John
 */
class AppApplication : Application() {

    var user = User()
        set(value) {
            field = value
            val mmkv: MMKV = MMKV.defaultMMKV()
            mmkv.encode("user", user)
        }

    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
        val mmkv: MMKV = MMKV.defaultMMKV()
        user = if (!mmkv.contains("user")) {
            User()
        } else {
            mmkv.decodeParcelable("user", User::class.java) ?: User()
        }
    }

    fun initUser() {
        user = User()
        val mmkv: MMKV = MMKV.defaultMMKV()
        mmkv.clearAll()
    }

}