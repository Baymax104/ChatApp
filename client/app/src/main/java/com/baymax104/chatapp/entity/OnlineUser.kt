package com.baymax104.chatapp.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.baymax104.chatapp.BR

/**
 * 主页面带额外状态User
 * @author John
 */
class OnlineUser(notice: Boolean, user: User) : BaseObservable() {

    @Bindable
    var notice: Boolean = notice
        set(value) {
            field = value
            notifyPropertyChanged(BR.notice)
        }

    @Bindable
    var user: User = user
        set(value) {
            field = value
            notifyPropertyChanged(BR.user)
        }

    constructor(user: User) : this(false, user)
}