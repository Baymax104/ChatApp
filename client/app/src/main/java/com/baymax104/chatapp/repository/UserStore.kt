package com.baymax104.chatapp.repository

import com.baymax104.basemvvm.vm.MessageHolder
import com.baymax104.chatapp.entity.User
import com.blankj.utilcode.util.Utils

/**
 * @Description
 * @Author John
 * @email
 * @Date 2023/1/15 22:59
 * @Version 1
 */
object UserStore : MessageHolder() {

    @JvmStatic
    var id: Int
        get() = user.id
        set(value) { user.id = value }

    @JvmStatic
    var username: String
        get() = user.username
        set(value) { user.username = value }

    @JvmStatic
    var gender: String
        get() = user.gender
        set(value) { user.gender = value }

    @JvmStatic
    var account: String
        get() = user.account
        set(value) { user.gender = value }


    @JvmStatic
    var password: String
        get() = user.password
        set(value){
            user.password = value }

    @JvmStatic
    var user: User
        get() = (Utils.getApp() as AppApplication).user
        set(value) {
            val app = Utils.getApp() as AppApplication
            app.user = value
            userLoginEvent.send(true)
        }

    @JvmStatic
    val isLogin: Boolean
        get() = id != -1

    @JvmStatic
    val userLoginEvent: Event<Boolean, Unit> = Event()


    @JvmStatic
    fun initUser() {
        val app = Utils.getApp() as AppApplication
        app.initUser()
        userLoginEvent.send(false)
    }

    @JvmStatic
    fun setUserInfo(user: User) {
        val app = Utils.getApp() as AppApplication
        val origin = app.user
        origin.apply {
            username = user.username
            age = user.age
            gender = user.gender
        }
        app.user = origin
    }
}
