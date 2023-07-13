package com.baymax104.chatapp.entity

import org.ktorm.entity.Entity

/**
 * User实体
 * @author John
 */
interface User : Entity<User> {

    companion object : Entity.Factory<User>()

    var id: Int
    var username: String
    var age: String
    var gender: String
    var account: String
    var password: String

}