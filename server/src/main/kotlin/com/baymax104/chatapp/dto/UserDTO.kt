package com.baymax104.chatapp.dto

import com.baymax104.chatapp.entity.User

/**
 * UserDTO
 */
class UserDTO() {

    var id: Int = 0
    var username: String = ""
    var age: Int = 0
    var gender: String = ""
    var account: String = ""
    var password: String = ""

    constructor(user: User) : this() {
        this.id = user.id
        this.username = user.username
        this.age = user.age
        this.gender = user.gender
        this.account = user.account
        this.password = user.password
    }
}